import xlearn as xl
import os
import re
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder
import joblib
import ast
import numpy as np
from collections import defaultdict
import random
import mysql.connector
from collections import defaultdict
import pickle

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

def get_problem_algorithm_list():

    problem_algorithm_list_query = f'SELECT problem_algorithm FROM problem_algorithm_encoder_table;'
    cursor.execute(problem_algorithm_list_query)
    problem_algorithm_list = cursor.fetchall()
    problem_algorithm_list = [p[0] for p in problem_algorithm_list]

    return problem_algorithm_list


def get_other_result_form_list():

    problem_other_result_form_query = f'SELECT problem_number, other_result_form_1, other_result_form_2 FROM problem_information_table'

    cursor.execute(problem_other_result_form_query)
    problem_other_result_form = [(i[0],i[1],i[2]) for i in cursor.fetchall()]
    problem_other_result_form_list = []


    for p_info in problem_other_result_form:

        p_num, form_1, form_2 = p_info
        
        if(form_1 != 0 or form_2 != 0):
            problem_other_result_form_list.append(p_num)

    return problem_other_result_form_list

def insert_update_user(user_id):

    a_query = "INSERT INTO update_user_table (user_id) VALUES (%s) ON DUPLICATE KEY UPDATE user_id = user_id"
    cursor.execute(a_query, (user_id,))
    conn.commit()

def get_binary_recommend_result(user_id):

    user_libffm_path = f'../Dataset/user_libffm_data_folder/{user_id}/'
    model_path = '../Dataset/ffm_model/'
    user_libffm_result_path = f'../Dataset/user_ffm_result/{user_id}/'

    tier_encoder_query = 'SELECT tier, tier_number FROM tier_encoder_table'
    cursor.execute(tier_encoder_query)
    tier_encoder = cursor.fetchall()
    tier_encoder = dict(tier_encoder)

    os.makedirs(user_libffm_result_path, exist_ok=True)
    
    ffm_binary_model = xl.create_ffm()
    ffm_binary_model.setSigmoid()
    ffm_binary_model.setTest(os.path.join(user_libffm_path, f"{user_id}_libffm.txt"))
    ffm_binary_model.predict(os.path.join(model_path, "ffm_binary_model.out"), os.path.join(user_libffm_result_path, f"{user_id}_ffm_binary_result.txt"))

    # 전체 문제 리스트 가져오기
    get_total_problem_info_query = "SELECT problem_number, problem_tier, problem_algorithm FROM problem_information_table"
    cursor.execute(get_total_problem_info_query)
    total_problem_info = cursor.fetchall()

    # 해당 유저가 해결한 문제 가져오기

    get_user_info_query = "SELECT user_solved_problem_list, user_tier, user_failed_problem_list, user_weak_algorithm FROM user_information_table WHERE user_id = %s"
    cursor.execute(get_user_info_query, (user_id,))
    user_info = cursor.fetchall()
    user_solved_problem_list = ast.literal_eval(user_info[0][0])
    user_solved_problem_list = [int(x) for x in user_solved_problem_list]
    user_tier = tier_encoder[user_info[0][1]]
    user_weak_algorithm = ast.literal_eval(user_info[0][3])

    # 전체 문제 리스트에서 해결 문제 빼서 해결하지 못한 문제들을 추리기.
    user_non_solved_problem = [data for data in total_problem_info if data[0] not in user_solved_problem_list]

    # 추천을 위한 최종 dict 만들기
    result = []

    with open(os.path.join(user_libffm_result_path, f"{user_id}_ffm_binary_result.txt")) as file:
            for line in file:
                value = float(line.strip())
                result.append(value)

    recommend_dict = defaultdict(list)
    
    if(len(user_non_solved_problem) != len(result)):
        raise ValueError('예측할 문제의 수와 유저가 풀지 않은 문제의 수가 다릅니다.')

    for p_info, predict_result in zip(user_non_solved_problem, result):
        p_number, p_tier, p_algorithm = p_info
        recommend_dict[tier_encoder[p_tier]].append((int(p_number) , predict_result, ast.literal_eval(p_algorithm)))

    sorted_dict = {key: sorted(value, key=lambda x: -x[1]) for key, value in recommend_dict.items()}

    user_weak_algorithm = user_weak_algorithm[:5]

    return sorted_dict, user_tier, user_weak_algorithm


def get_reg_recommend_result(user_id):

    user_libffm_path = f'../Dataset/user_libffm_data_folder/{user_id}/'
    model_path = '../Dataset/ffm_model/'
    user_libffm_result_path = f'../Dataset/user_ffm_result/{user_id}/'

    tier_encoder_query = 'SELECT tier, tier_number FROM tier_encoder_table'
    cursor.execute(tier_encoder_query)
    tier_encoder = cursor.fetchall()
    tier_encoder = dict(tier_encoder)
  
    os.makedirs(user_libffm_result_path, exist_ok=True)
    
    ffm_reg_model = xl.create_ffm()
    ffm_reg_model.setTest(os.path.join(user_libffm_path, f"{user_id}_libffm.txt"))
    ffm_reg_model.predict(os.path.join(model_path, "ffm_reg_model.out"), os.path.join(user_libffm_result_path, f"{user_id}_ffm_reg_result.txt"))

    # 전체 문제 리스트 가져오기
    get_total_problem_info_query = "SELECT problem_number, problem_tier, problem_algorithm FROM problem_information_table"
    cursor.execute(get_total_problem_info_query)
    total_problem_info = cursor.fetchall()

    # 해당 유저가 해결한 문제 가져오기

    get_user_info_query = "SELECT user_solved_problem_list, user_tier, user_failed_problem_list, user_weak_algorithm FROM user_information_table WHERE user_id = %s"
    cursor.execute(get_user_info_query, (user_id,))
    user_info = cursor.fetchall()
    user_solved_problem_list = ast.literal_eval(user_info[0][0])
    user_solved_problem_list = [int(x) for x in user_solved_problem_list]
    user_tier = tier_encoder[user_info[0][1]]
    user_weak_algorithm = ast.literal_eval(user_info[0][3])


    # 전체 문제 리스트에서 해결 문제 빼서 해결하지 못한 문제들을 추리기.
    user_non_solved_problem = [data for data in total_problem_info if data[0] not in user_solved_problem_list]

    # 추천을 위한 최종 dict 만들기
    result = []

    with open(os.path.join(user_libffm_result_path, f"{user_id}_ffm_reg_result.txt")) as file:
            for line in file:
                value = float(line.strip())
                result.append(value)

    recommend_dict = defaultdict(list)

    if(len(user_non_solved_problem) != len(result)):
        raise ValueError('예측할 문제의 수와 유저가 풀지 않은 문제의 수가 다릅니다.')

    for p_info, predict_result in zip(user_non_solved_problem, result):
        p_number, p_tier, p_algorithm = p_info
        recommend_dict[tier_encoder[p_tier]].append((int(p_number) , predict_result, ast.literal_eval(p_algorithm)))

    sorted_dict = {key: sorted(value, key=lambda x: -x[1]) for key, value in recommend_dict.items()}

    user_weak_algorithm = user_weak_algorithm[:5]
    
    return sorted_dict, user_tier, user_weak_algorithm


def get_recommend_list(user_name, wanted_algorithm_list):
    
    binary_dict, user_tier, user_weak_algorithm = get_binary_recommend_result(user_name)
    reg_dict, user_tier, user_weak_algorithm = get_reg_recommend_result(user_name)
    
    #유효하지 않은 알고리즘이 들어오면 오류 반환.
    problem_algorithm = get_problem_algorithm_list()
    for algorithm in wanted_algorithm_list:
        if(algorithm not in problem_algorithm):
            return "Invalid algorithm", user_weak_algorithm


    # 유저 티어에 비해 너무 높은 문제는 삭제.

    fin_binary_keys = [x for x in range(6, user_tier + 4) if x<=22]
    fin_binary_dict = {key: binary_dict[key] for key in fin_binary_keys}

    fin_reg_keys = [x for x in range(6, user_tier + 4) if x<=22]
    fin_reg_dict = {key: reg_dict[key] for key in fin_reg_keys}
    
    # 점수제, 비율제 문제 제거 -> 나중에 처리되면 다시 추천에 활용
    problem_other_result_form_list = get_other_result_form_list()
    

    #입력 받은 알고리즘 외 문제 제외
    if(wanted_algorithm_list):

        for p_tier, p_info in fin_binary_dict.items():

            f_p_list = []

            for pi in p_info:

                p_number, p_predict, p_algorithm_list = pi
                flag = True

                # 점수제, 비율제 문제 제거 -> 나중에 처리되면 다시 추천에 활용 아래 코드 지우면 돼
                #---
                if(p_number in problem_other_result_form_list):
                    flag = False
                #---

                for u in wanted_algorithm_list:
                    if(u not in p_algorithm_list):
                        flag = False

                if(flag):
                    f_p_list.append((p_number, p_predict))
            
            fin_binary_dict[p_tier] = f_p_list
        
        for p_tier, p_info in fin_reg_dict.items():

            f_p_list = []

            for pi in p_info:

                p_number, p_predict, p_algorithm_list = pi
                flag = True

                # 점수제, 비율제 문제 제거 -> 나중에 처리되면 다시 추천에 활용 아래 코드 지우면 돼
                #---
                if(p_number in problem_other_result_form_list):
                    flag = False
                #---

                for u in wanted_algorithm_list:
                    if(u not in p_algorithm_list):
                        flag = False

                if(flag):
                    f_p_list.append((p_number, p_predict))
            
            fin_reg_dict[p_tier] = f_p_list
    
    fin_binary_dict = {key: [x[0] for x in value[:10]] for key, value in fin_binary_dict.items()}
    fin_reg_dict = {key: [x[0] for x in value[:10]] for key, value in fin_reg_dict.items()}

    fin_dict = {key: list(set(fin_reg_dict[key]+fin_binary_dict[key])) for key in fin_reg_dict.keys()}

    for key, value in list(fin_dict.items()):
        if not value:
            del fin_dict[key]

    if(len(fin_dict)<3):
        return "Not Exist Problem", user_weak_algorithm

    else:
        selected_keys = random.sample(list(fin_dict.keys()), 3)
        selected_values = [random.choice(fin_dict[key]) for key in selected_keys]
        return selected_values, user_weak_algorithm
    
