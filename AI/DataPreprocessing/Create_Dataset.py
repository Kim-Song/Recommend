import pandas as pd
import numpy as np
import os
from sklearn.preprocessing import LabelEncoder
import joblib
from collections import Counter
from collections import defaultdict
import ast

data_path = '../DataSet/'
encoder_data_path = '../DataSet/Encoder/'

print('-----loading data...-----')

user_baekjoon_df = pd.read_csv(os.path.join(data_path, 'Baekjoon_유저_크롤링.csv'))
problem_baekjoon_df = pd.read_csv(os.path.join(data_path, 'Baekjoon_문제_크롤링.csv'))
user_solvedac_df = pd.read_csv(os.path.join(data_path, 'solved.ac_유저_크롤링.csv'))

# 유저 아이디 목록이 존재하지 않는 것 존재 -> 데이터 오류, 삭제
user_baekjoon_df = user_baekjoon_df[user_baekjoon_df['user_id_list'] == user_baekjoon_df['user_id_list']]

# Problem number 형 변환
# submit_time 데이터형 변환
# memory, time은 틀렸을 시 nan값 -> 결측치는 아니므로 -1 로 대체

print('-----step1...-----')

user_baekjoon_df['problem_number_list'] = user_baekjoon_df['problem_number_list'].astype(int)
user_baekjoon_df['code_memory_list'].fillna(-1, inplace=True)
user_baekjoon_df['code_time_list'].fillna(-1, inplace=True)
user_baekjoon_df['code_memory_list'] = user_baekjoon_df['code_memory_list'].astype(int)
user_baekjoon_df['code_time_list'] = user_baekjoon_df['code_time_list'].astype(int)

problem_list = list(problem_baekjoon_df['problem_number'])
problem_baekjoon_df['problem_algorithm'] = problem_baekjoon_df['problem_algorithm'].apply(ast.literal_eval)
problem_baekjoon_df['cat_problem_avg_try'] = problem_baekjoon_df['problem_avg_try'].apply(lambda x: int(x - 0.5) + 1 if x > 0.5 else 1)

user_baekjoon_df = user_baekjoon_df[user_baekjoon_df['problem_number_list'].isin(problem_list)]

user_list = list(user_solvedac_df['user_name'])

user_baekjoon_df = user_baekjoon_df[user_baekjoon_df['user_id_list'].isin(user_list)]

user_baekjoon_df = pd.merge(user_baekjoon_df, user_solvedac_df[['user_name', 'user_tier']], how='inner', left_on='user_id_list', right_on = 'user_name')
user_baekjoon_df.drop(['code_memory_list', 'code_time_list','code_language_list', 'user_name'], axis=1, inplace=True)
user_baekjoon_df.columns = ['submit_number', 'user_id', 'problem_number', 'problem_result', 'user_tier']

print(user_baekjoon_df)

# 인코더 생성
user_ID_Encoder = LabelEncoder()
user_ID_Encoder.fit(user_list)

print('-----step2...-----')

lib_df = user_baekjoon_df.merge(problem_baekjoon_df[['problem_number', 'problem_tier', 'problem_algorithm','cat_problem_avg_try']], on = ['problem_number'],how='left').drop_duplicates(subset=['user_id', 'problem_number']).reset_index(drop=True)
user_input_df = user_baekjoon_df.loc[:,'user_id':'problem_result']
user_f_df = pd.DataFrame(user_input_df)

# 딕셔너리 생성
result_dict = defaultdict(lambda: defaultdict(lambda: defaultdict(int)))

# 데이터프레임을 순회하면서 딕셔너리에 값 추가
for _, row in user_f_df.iterrows():
    result_dict[row['user_id']][row['problem_number']][row['problem_result']] += 1

s = []
for p in problem_baekjoon_df['problem_algorithm']:
    for i in p:
        s.append(i)

C = Counter(s)
algorithm_list = list(C.keys())

print('-----step3...-----')

target_field = []
user_weak_algorithm = dict()

messages = ['틀렸습니다', '시간 초과', '컴파일 에러', '런타임 에러', '출력 초과', '출력 형식이 잘못되었습니다']

for user in result_dict:
    
    user_algorithm_dict=dict(zip(algorithm_list, [0]*len(algorithm_list)))

    for problem in result_dict[user]:
    
        value = 0
        for key_message in result_dict[user][problem].keys():
            for m in messages:
                if m in key_message:
                    value += result_dict[user][problem][key_message]
                    a_list = problem_baekjoon_df.loc[problem_baekjoon_df['problem_number']==problem, 'problem_algorithm'].values[0]

                    for a in a_list:
                        user_algorithm_dict[a] += 1

        target_field.append(value)

    user_algorithm_dict = dict(sorted(user_algorithm_dict.items(), key = lambda item: item[1],reverse=True))
    user_weak_algorithm[user] = list(user_algorithm_dict.keys())[:10]

lib_df.insert(0, 'target', target_field)
lib_df['user_weak_algorithm'] = lib_df['user_id']
lib_df['user_weak_algorithm'] = lib_df['user_weak_algorithm'].map(user_weak_algorithm)
lib_df.drop(['submit_number', 'problem_result'],axis=1,inplace=True)

print('-----step4...-----')

lib_df.to_csv('../Dataset/lib_dataset/New_Non_Encoding_lib_data.csv',index=False)