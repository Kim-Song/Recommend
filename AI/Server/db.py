import pandas as pd
import numpy as np
import time
import os
import urllib.request
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options
import pickle
import re
from sklearn.preprocessing import LabelEncoder
import joblib
from collections import Counter
from collections import defaultdict
import ast
import mysql.connector
from sklearn.model_selection import train_test_split

from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException

chrome_options = Options()
chrome_options.add_argument('--headless')  # 웹 브라우저를 표시하지 않고 백그라운드에서 실행
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-dev-shm-usage')

conn = mysql.connector.connect(
    user='root',
    password='llsy159',
    host='localhost',
    port=3306,
    database='recommend_project'
    #database='recommend'
)

DB_table_name = 'recommend_project'
#DB_table_name = 'recommend'

cursor = conn.cursor()
use_query = f"USE {DB_table_name}"

cursor.execute(use_query)

set_query = "SET innodb_lock_wait_timeout = 28800;"

cursor.execute(set_query) 

def delete_user_id_from_update_user_table(user_id):
    delete_user_id_query = "DELETE FROM update_user_table WHERE user_id = %s;"
    cursor.execute(delete_user_id_query, (user_id,))
    conn.commit()

def prepare_tier_encoder():

    tier_encoder_query = 'SELECT tier_number, tier FROM tier_encoder_table'
    cursor.execute(tier_encoder_query)
    tier_encoder = cursor.fetchall()
    tier_encoder = dict(tier_encoder)

    return tier_encoder

def prepare_problem_list():
    problem_number_query = f"SELECT problem_number From problem_information_table"

    cursor.execute(problem_number_query)

    problem_number_list = [i[0] for i in cursor.fetchall()]

    problem_other_result_form_query = f'SELECT other_result_form_1, other_result_form_2 FROM problem_information_table'

    cursor.execute(problem_other_result_form_query)
    problem_other_result_form = [(i[0],i[1]) for i in cursor.fetchall()]

    problem_other_result_form_1 = [int(i[0]) for i in problem_other_result_form]
    problem_other_result_form_2 = [int(i[1]) for i in problem_other_result_form]

    problem_other_result_form_list_1 = []
    problem_other_result_form_list_2 = []

    for i, v in enumerate(problem_number_list):
        
        if(problem_other_result_form_1[i] != 0):
            problem_other_result_form_list_1.append((v, problem_other_result_form_1[i]))
        if(problem_other_result_form_2[i] != 0):
            problem_other_result_form_list_2.append((v, problem_other_result_form_2[i]))

    problem_other_result_form_dict_1 = dict(problem_other_result_form_list_1)
    problem_other_result_form_dict_2 = dict(problem_other_result_form_list_2)

    return (problem_number_list, problem_other_result_form_dict_1, problem_other_result_form_dict_2)


def crawling_user_information(user_id, problem_number_list):

    user_information_url = 'https://www.acmicpc.net/user/' + str(user_id)

    driver = webdriver.Chrome('../Crawling/chromedriver.exe', options=chrome_options)
    driver.get(user_information_url)


    user_solve_problem_list = []
    tier_value = -1
    rank_value = -1

    #유저 정보가 존재한다면, 유저 정보 긁어오기.
    try:
        user_solve_problem_object = driver.find_element_by_css_selector('.problem-list')
        user_solve_problem = user_solve_problem_object.text.split(' ')
        user_solve_problem_list = [int(problem) for problem in user_solve_problem if int(problem) in problem_number_list]
    
    #존재하지 않는다면, 오류 메세지 출력
    except NoSuchElementException:
        print('해당 유저가 존재하지 않습니다.')
        return -1

    try:
        # id가 "statics"인 테이블 찾기
        table_element = driver.find_element_by_css_selector("#statics")
        
        # 테이블 내에서 '등수' 텍스트를 포함한 <tr> 찾기
        for t in table_element.text.split('\n'):
            if('등수' in t):
                rank_value = int(t.split(' ')[1])
                break

    except NoSuchElementException:
        print('해당 유저의 랭크가 존재하지 않습니다.')

    try:
        solvedac_tier_element = driver.find_element_by_css_selector(".solvedac-tier")
        img_src = solvedac_tier_element.get_attribute("src")

        pattern = r'/tier/(\d+)\.svg'

        match = re.search(pattern, img_src)

        tier_value = int(match.group(1))
    except NoSuchElementException:
        print('해당 유저의 티어가 존재하지 않습니다.')


    driver.quit()

    return (tier_value, rank_value, user_solve_problem_list, len(user_solve_problem_list))


def crawling_user_problem_interaction(user_id):

    user_problem_interaction_url = 'https://www.acmicpc.net/status?user_id=' + str(user_id)
    driver = webdriver.Chrome('../Crawling/chromedriver.exe', options=chrome_options)
    driver.get(user_problem_interaction_url)

    submit_number_list = []
    user_id_list = []
    problem_number_list = []
    problem_result_list = []
    code_memory_list = []
    code_time_list = []
    code_language_list = []


    flag = True

    while(True):

        user_data = driver.find_elements_by_css_selector('[data-can-view]')
        
        try:
            user_next_page = driver.find_element_by_css_selector('#next_page')

        except NoSuchElementException:
            flag = False

        for user_info in user_data:
        
            user_text = user_info.find_elements_by_css_selector("td")
            user_text_list = []

            for ut in user_text:
                user_text_list.append(ut.text)
            
            submit_number_list.append(user_text_list[0])
            user_id_list.append(user_text_list[1])
            problem_number_list.append(user_text_list[2])
            problem_result_list.append(user_text_list[3])
            code_memory_list.append(np.nan if user_text_list[4]=='' else user_text_list[4])
            code_time_list.append(np.nan if user_text_list[5]=='' else user_text_list[5])
            code_language_list.append(user_text_list[6])

        if(flag):
            user_next_page.click()
        else:
            break            
    
    user_problem_interaction_information = pd.DataFrame({
        'submit_number_list': submit_number_list,
        'user_id_list': user_id_list,
        'problem_number_list': problem_number_list,
        'problem_result_list': problem_result_list,
        'code_memory_list': code_memory_list,
        'code_time_list': code_time_list,
        'code_language_list': code_language_list,
    })

    driver.quit()
    
    return user_problem_interaction_information

def user_problem_interaction_preprocessing(user_problem_interaction_df, problem_number_list):
    # 유저 아이디 목록이 존재하지 않는 것 존재 -> 데이터 오류, 삭제 요망
    user_problem_interaction_df = user_problem_interaction_df[user_problem_interaction_df['user_id_list'] == user_problem_interaction_df['user_id_list']]
    user_problem_interaction_df = user_problem_interaction_df[user_problem_interaction_df['code_language_list'] == user_problem_interaction_df['code_language_list']]

    # Problem number 형 변환
    # memory, time은 틀렸을 시 nan값 -> 결측치는 아니므로 -1 로 대체

    user_problem_interaction_df['problem_number_list'] = user_problem_interaction_df['problem_number_list'].astype(int)
    user_problem_interaction_df['code_memory_list'].fillna(-1, inplace=True)
    user_problem_interaction_df['code_time_list'].fillna(-1, inplace=True)
    user_problem_interaction_df['code_memory_list'] = user_problem_interaction_df['code_memory_list'].astype(int)
    user_problem_interaction_df['code_time_list'] = user_problem_interaction_df['code_time_list'].astype(int)

    user_problem_interaction_df = user_problem_interaction_df[user_problem_interaction_df['problem_result_list']!='채점 불가']
    user_problem_interaction_df = user_problem_interaction_df[user_problem_interaction_df['problem_result_list']!='채점 중']

    #DB에 없는 문제 거르는 작업
    user_problem_interaction_df = user_problem_interaction_df[user_problem_interaction_df['problem_number_list'].isin(problem_number_list)]

    return user_problem_interaction_df

def user_problem_interaction_to_DB_form(user_id, user_problem_interaction_df, problem_other_result_form_dict_1, problem_other_result_form_dict_2):

    total_result_dict = dict()
    user_failed_problem = []

    user_problem_interaction_df_groupby_problem_number = user_problem_interaction_df.groupby('problem_number_list')

    other_form_1_key = problem_other_result_form_dict_1.keys()
    other_form_2_key = problem_other_result_form_dict_2.keys()

    for problem_number, row in user_problem_interaction_df_groupby_problem_number:
        
        result_dict = {
            'user_problem_key' : 0,
            'user_id' : 0,
            'problem_number' : 0,
            'correct_attempts_count' : 0,
            'incorrect_attempts_count' : 0,
            'time_limit_exceeded_count' : 0,
            'memory_limit_exceeded_count' : 0,
            'output_limit_exceeded_count' : 0,
            'runtime_error_count' : 0,
            'compile_error_count' : 0,
            'output_format_issue_count' : 0,
            'most_used_language' : 0,
            'best_memory_usage' : 0,
            'fastest_execution_time' : 0,
            'memory_limit_optimal_solution_submission_number' : 0,
            'time_limit_optimal_solution_submission_number' : 0
        }

        correct_attempts_list = []

        result_dict['user_problem_key'] = str(user_id) + '_' + str(problem_number)
        result_dict['user_id'] = user_id
        result_dict['problem_number'] = problem_number
        
        for index, r in row.iterrows():

            flag = False
            result = r['problem_result_list']

            if(problem_number in other_form_1_key):
                if('점' in result):
                    result_score = re.search(r'\d+(\.\d+)?', result).group()
                    if(float(result_score) if '.' in result_score else int(result_score) == problem_other_result_form_dict_1[problem_number]):
                        result_dict['correct_attempts_count'] += 1
                        flag = True
                    else:
                        result_dict['incorrect_attempts_count'] += 1
                elif('틀렸습니다' in result):
                    result_dict['incorrect_attempts_count'] += 1
                elif('시간 초과' in result):
                    result_dict['time_limit_exceeded_count'] += 1
                elif('메모리 초과' in result):
                    result_dict['memory_limit_exceeded_count'] += 1
                elif('출력 초과' in result):
                    result_dict['output_limit_exceeded_count'] += 1
                elif('런타임 에러' in result):
                    result_dict['runtime_error_count'] += 1
                elif('컴파일 에러' in result):
                    result_dict['compile_error_count'] += 1
                elif('출력 형식이 잘못되었습니다' in result):
                    result_dict['output_format_issue_count'] += 1
                else:
                    pass
            elif(problem_number in other_form_2_key):
                if('맞았습니다!!' in result):
                    numbers = re.findall(r'\d+', result)
                    if(numbers[0] == numbers[1]):
                        result_dict['correct_attempts_count'] += 1
                        flag = True
                    elif(numbers[0] != numbers[1]):
                        result_dict['incorrect_attempts_count'] += 1
                elif('틀렸습니다' in result):
                    result_dict['incorrect_attempts_count'] += 1
                elif('시간 초과' in result):
                    result_dict['time_limit_exceeded_count'] += 1
                elif('메모리 초과' in result):
                    result_dict['memory_limit_exceeded_count'] += 1
                elif('출력 초과' in result):
                    result_dict['output_limit_exceeded_count'] += 1
                elif('런타임 에러' in result):
                    result_dict['runtime_error_count'] += 1
                elif('컴파일 에러' in result):
                    result_dict['compile_error_count'] += 1
                elif('출력 형식이 잘못되었습니다' in result):
                    result_dict['output_format_issue_count'] += 1
                else:
                    pass
            else:
                if('맞았습니다!!' in result):
                    result_dict['correct_attempts_count'] += 1
                    flag = True
                elif('틀렸습니다' in result):
                    result_dict['incorrect_attempts_count'] += 1
                elif('시간 초과' in result):
                    result_dict['time_limit_exceeded_count'] += 1
                elif('메모리 초과' in result):
                    result_dict['memory_limit_exceeded_count'] += 1
                elif('출력 초과' in result):
                    result_dict['output_limit_exceeded_count'] += 1
                elif('런타임 에러' in result):
                    result_dict['runtime_error_count'] += 1
                elif('컴파일 에러' in result):
                    result_dict['compile_error_count'] += 1
                elif('출력 형식이 잘못되었습니다' in result):
                    result_dict['output_format_issue_count'] += 1
                else:
                    pass

            if(flag):
                correct_attempts_list.append((r['submit_number_list'], r['code_memory_list'], r['code_time_list'], r['code_language_list']))

        if(result_dict['correct_attempts_count']==0):
            user_failed_problem.append(problem_number)
        
        else:
            frequency_counter = Counter(tuple_[3] for tuple_ in correct_attempts_list)
            most_common_language = frequency_counter.most_common(1)[0][0]
            filtered_tuples = [tuple_ for tuple_ in correct_attempts_list if tuple_[3] == most_common_language]

            best_memory = min(filtered_tuples, key=lambda tuple_: tuple_[1])
            best_time = min(filtered_tuples, key=lambda tuple_: tuple_[2])

            result_dict['most_used_language'] = most_common_language
            result_dict['best_memory_usage'] = best_memory[1]
            result_dict['fastest_execution_time'] = best_time[2]
            result_dict['memory_limit_optimal_solution_submission_number'] = best_memory[0]
            result_dict['time_limit_optimal_solution_submission_number'] = best_time[0]
        
        total_result_dict[problem_number] = result_dict


    return (total_result_dict, user_failed_problem)

def new_user_process(user_id, user_key, user_tier, user_rank, user_solved_problem, user_failed_problem, user_solved_problem_number, user_problem_interaction_info):

    #유저 테이블 데이터 전처리 및 DB 저장 함수 실행. (Insert)
    #- 유저 id가 없으므로, Insert 실행

    user_id_insert_query = "INSERT INTO user_information_table (user_id, user_key, user_tier, user_rank, user_solved_problem_list, user_failed_problem_list, user_solved_problem_count) VALUES (%s, %s, %s, %s, %s, %s, %s);"
    cursor.execute(user_id_insert_query, (user_id, user_key, user_tier, user_rank, str(user_solved_problem), str(user_failed_problem), user_solved_problem_number))
    conn.commit()

    #유저 - 문제 상호작용 테이블 데이터 전처리 및 DB 저장 함수 실행. (Insert)
    #- 유저 - 문제 Key에서 유저 자체가 없으므로, 모든 정보 Insert

    user_problem_interaction_insert_query = "INSERT INTO user_problem_interaction_table (user_problem_key ,user_id ,problem_number ,correct_attempts_count ,incorrect_attempts_count ,time_limit_exceeded_count ,memory_limit_exceeded_count ,output_limit_exceeded_count ,runtime_error_count ,compile_error_count ,output_format_issue_count ,most_used_language ,best_memory_usage ,fastest_execution_time ,memory_limit_optimal_solution_submission_number ,time_limit_optimal_solution_submission_number) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    for i, v in user_problem_interaction_info.items():
        cursor.execute(user_problem_interaction_insert_query, (v['user_problem_key'], v['user_id'], v['problem_number'], v['correct_attempts_count'], v['incorrect_attempts_count'], v['time_limit_exceeded_count'], v['memory_limit_exceeded_count'], v['output_limit_exceeded_count'], v['runtime_error_count'], v['compile_error_count'], v['output_format_issue_count'], v['most_used_language'], v['best_memory_usage'], v['fastest_execution_time'], v['memory_limit_optimal_solution_submission_number'], v['time_limit_optimal_solution_submission_number']))
    
    conn.commit()

def regular_user_process(user_id, user_tier, user_rank, user_solved_problem, user_failed_problem, user_solved_problem_number, user_problem_interaction_info):

    #유저 테이블 데이터 전처리 및 DB 저장 함수 실행. (Update)
    #- 유저 id 찾아서 업데이트
    
    user_table_update_query = "UPDATE user_information_table SET user_tier = %s, user_rank = %s, user_solved_problem_list = %s, user_failed_problem_list = %s, user_solved_problem_count = %s WHERE user_id = %s;"
    cursor.execute(user_table_update_query, (user_tier, user_rank, str(user_solved_problem), str(user_failed_problem), user_solved_problem_number, user_id))
    conn.commit()

    #유저 - 문제 상호작용 테이블 데이터 전처리 및 DB 저장 함수 실행. (Update)
    #- 유저 - 문제 Key가 있다면, Update 없다면, Insert

    user_problem_interaction_insert_query = "INSERT INTO user_problem_interaction_table (user_problem_key ,user_id ,problem_number ,correct_attempts_count ,incorrect_attempts_count ,time_limit_exceeded_count ,memory_limit_exceeded_count ,output_limit_exceeded_count ,runtime_error_count ,compile_error_count ,output_format_issue_count ,most_used_language ,best_memory_usage ,fastest_execution_time ,memory_limit_optimal_solution_submission_number ,time_limit_optimal_solution_submission_number) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    user_problem_interaction_update_query = "UPDATE user_problem_interaction_table SET correct_attempts_count = %s, incorrect_attempts_count = %s, time_limit_exceeded_count = %s, memory_limit_exceeded_count = %s, output_limit_exceeded_count = %s, runtime_error_count = %s, compile_error_count = %s, output_format_issue_count = %s, most_used_language = %s, best_memory_usage = %s, fastest_execution_time = %s, memory_limit_optimal_solution_submission_number = %s, time_limit_optimal_solution_submission_number = %s WHERE user_problem_key = %s;"              
    
    for i, v in user_problem_interaction_info.items():

        up_key = v['user_problem_key']

        check_user_problem_key_query = f'SELECT EXISTS (SELECT TRUE FROM user_problem_interaction_table WHERE user_problem_key = \'{up_key}\') AS value_exists;'
    
        cursor.execute(check_user_problem_key_query)
        
        check_user_problem_key_result = cursor.fetchone()

        if(check_user_problem_key_result[0] == 1):
            cursor.execute(user_problem_interaction_update_query, (v['correct_attempts_count'], v['incorrect_attempts_count'], v['time_limit_exceeded_count'], v['memory_limit_exceeded_count'], v['output_limit_exceeded_count'], v['runtime_error_count'], v['compile_error_count'], v['output_format_issue_count'], v['most_used_language'], v['best_memory_usage'], v['fastest_execution_time'], v['memory_limit_optimal_solution_submission_number'], v['time_limit_optimal_solution_submission_number'], v['user_problem_key']))
        else:
            cursor.execute(user_problem_interaction_insert_query, (v['user_problem_key'], v['user_id'], v['problem_number'], v['correct_attempts_count'], v['incorrect_attempts_count'], v['time_limit_exceeded_count'], v['memory_limit_exceeded_count'], v['output_limit_exceeded_count'], v['runtime_error_count'], v['compile_error_count'], v['output_format_issue_count'], v['most_used_language'], v['best_memory_usage'], v['fastest_execution_time'], v['memory_limit_optimal_solution_submission_number'], v['time_limit_optimal_solution_submission_number']))
    
    conn.commit()

#렉걸려서 잠시 연결 안된 예외도 처리해주자.

def user_update(user_id):

    problem_number_list, problem_other_result_form_dict_1, problem_other_result_form_dict_2 = prepare_problem_list()
    tier_encoder = prepare_tier_encoder()
    #유저 테이블에 필요한 정보 크롤링하는 함수 실행.
    user_information = crawling_user_information(user_id,problem_number_list)
    
    #유저가 존재하지 않는다면,
    if(user_information == -1):
        print('해당 유저가 존재하지 않습니다.')
        #해당 유저의 데이터 삭제하는 코드 실행.

        #근데, 존재하지 않으면 데이터를 집어넣을 일이 없고, 이미 넣어진 상태에서 제거된다는 것은 유저가 탈퇴한 경우인데, 
        #AI의 인풋 데이터가 될 수도 있는 것을 굳이 삭제할 필요가 있을까?... 

        return -1

    #유저의 제출들을 크롤링하는 함수 실행.
    user_problem_interaction = crawling_user_problem_interaction(user_id)

    # user_problem_interaction 전처리 함수 실행

    user_problem_interaction = user_problem_interaction_preprocessing(user_problem_interaction, problem_number_list)
    user_problem_interaction = user_problem_interaction_to_DB_form(user_id, user_problem_interaction, problem_other_result_form_dict_1, problem_other_result_form_dict_2)

    get_user_key_query = 'SELECT max(user_key) FROM user_information_table'
    cursor.execute(get_user_key_query)
    user_key_object = cursor.fetchall()
    user_key = user_key_object[0][0] + 1
    user_tier = tier_encoder[user_information[0]]
    user_rank = user_information[1]
    user_solved_problem = user_information[2]
    user_solved_problem_number = user_information[3]

    user_problem_interaction_info = user_problem_interaction[0]
    user_failed_problem = user_problem_interaction[1]


    # DB의 유저 테이블에 해당 유저가 존재하는지 확인.

    check_user_id_query = f'SELECT EXISTS (SELECT TRUE FROM user_information_table WHERE user_id = \'{user_id}\') AS value_exists;'
    
    cursor.execute(check_user_id_query)
    
    check_user_id_result = cursor.fetchone()
    

    # 있다면, 해당 유저의 정보를 갱신.
    if(check_user_id_result[0] == 1):
        regular_user_process(user_id, user_tier, user_rank, user_solved_problem, user_failed_problem, user_solved_problem_number, user_problem_interaction_info)
        print('기존 유저 갱신')
        
        
    # 없다면, 유저의 정보를 추가
    else:
        new_user_process(user_id, user_key, user_tier, user_rank, user_solved_problem, user_failed_problem, user_solved_problem_number, user_problem_interaction_info)
        print('신규 유저 추가')
        
