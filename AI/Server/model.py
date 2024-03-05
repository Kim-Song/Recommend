import xlearn as xl
import os
import re
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder
import joblib
import ast
from sklearn.metrics import roc_auc_score
from sklearn.metrics import mean_squared_error
import numpy as np
from collections import defaultdict
import random
import pickle
import mysql.connector


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

model_path = '../Dataset/ffm_model/'
lib_path = "../Dataset/libffm/"

user_id_field = 1
user_tier_field = 0.5
problem_number_field = 1
problem_tier_field = 0.5
user_failed_problem_field = 1
problem_algorithm_field = 1
user_weak_algorithm_field = 1

def train_ffm_model_out():

    ffm_reg_model = xl.create_ffm()
    ffm_reg_model.setTrain(os.path.join(lib_path, "FFM_reg.txt"))

    param = {'task':'reg', 'lr':0.02, 'lambda':0.002, 'opt':'ftrl', 'metric': 'rmse', 'epoch':10}

    ffm_reg_model.fit(param, os.path.join(model_path, "ffm_reg_model.out"))

    ffm_binary_model = xl.create_ffm()
    ffm_binary_model.setTrain(os.path.join(lib_path, "FFM_binary.txt"))

    param = {'task':'binary', 'lr':0.02, 'lambda':0.002, 'opt':'ftrl', 'metric': 'auc','epoch':10}

    ffm_binary_model.fit(param, os.path.join(model_path, "ffm_binary_model.out"))

def create_user_test_libffm(user_id):

    problem_algorithm_encoder_query = 'SELECT problem_algorithm, problem_algorithm_number FROM problem_algorithm_encoder_table'
    cursor.execute(problem_algorithm_encoder_query)
    problem_algorithm_encoder = cursor.fetchall()
    problem_algorithm_encoder = dict(problem_algorithm_encoder)

    tier_encoder_query = 'SELECT tier, tier_number FROM tier_encoder_table'
    cursor.execute(tier_encoder_query)
    tier_encoder = cursor.fetchall()
    tier_encoder = dict(tier_encoder)

    problem_number_encoding_query = 'SELECT problem_number, problem_key FROM problem_information_table'
    cursor.execute(problem_number_encoding_query)
    problem_number_encoder = cursor.fetchall()
    problem_number_encoder = dict(problem_number_encoder)

    # 전체 문제 리스트 가져오기
    get_total_problem_info_query = "SELECT problem_number, problem_tier, problem_algorithm FROM problem_information_table"
    cursor.execute(get_total_problem_info_query)
    total_problem_info = cursor.fetchall()
    
    # 해당 유저가 해결한 문제 가져오기
    
    get_user_info_query = "SELECT user_key, user_solved_problem_list, user_tier, user_failed_problem_list, user_weak_algorithm FROM user_information_table WHERE user_id = %s"
    cursor.execute(get_user_info_query, (user_id,))
    user_info = cursor.fetchall()
    user_key = int(user_info[0][0])
    user_solved_problem_list = ast.literal_eval(user_info[0][1])
    if(user_solved_problem_list==['']):
        user_solved_problem_list = []
    else:
        user_solved_problem_list = [int(x) for x in user_solved_problem_list]
    user_tier = tier_encoder[user_info[0][2]]
    user_failed_problem_list = ast.literal_eval(user_info[0][3])
    user_weak_algorithm = ast.literal_eval(user_info[0][4])

    # 전체 문제 리스트에서 해결 문제 빼서 해결하지 못한 문제들을 추리기.

    user_non_solved_problem = [data for data in total_problem_info if data[0] not in user_solved_problem_list]

    # 그것으로 user_id, problem_number까지 만들기.
    # user_tier, user_failed_problem_list, user_weak_algorithm Merge
    # problem_tier, problem_algorithm Merge

    non_solved_problem_length = len(user_non_solved_problem)
    user_libffm_df = pd.DataFrame(zip([user_key]*non_solved_problem_length, 
                                  [int(problem_number[0]) for problem_number in user_non_solved_problem], 
                                  [user_tier]*non_solved_problem_length, 
                                  [user_failed_problem_list] * non_solved_problem_length,
                                  [user_weak_algorithm[:5]] * non_solved_problem_length,
                                  [tier_encoder[problem_tier[1]] for problem_tier in user_non_solved_problem],
                                  [ast.literal_eval(problem_algorithm[2]) for problem_algorithm in user_non_solved_problem]),
                                  columns = ['user_id', 'problem_number', 'user_tier', 'user_failed_problem_list', 'user_weak_algorithm', 'problem_tier', 'problem_algorithm'])

    # Encoder 불러와서 Encoding

    user_libffm_df['problem_number'] = user_libffm_df['problem_number'].map(problem_number_encoder)
    user_libffm_df['problem_algorithm'] = user_libffm_df['problem_algorithm'].apply(lambda x: list(map(problem_algorithm_encoder.get, x)))
    user_libffm_df['user_failed_problem_list'] = user_libffm_df['user_failed_problem_list'].apply(lambda x: list(map(problem_number_encoder.get, x)))
    user_libffm_df['user_weak_algorithm'] = user_libffm_df['user_weak_algorithm'].apply(lambda x: list(map(problem_algorithm_encoder.get, x)))
    
    # 반환

    return user_libffm_df


def create_user_ffm_data(user_id, result_df):
    
    user_libffm_path = f'../Dataset/user_libffm_data_folder/{user_id}/'

    os.makedirs(user_libffm_path, exist_ok=True)

    with open(os.path.join(user_libffm_path, f'{user_id}_libffm.txt'), 'w') as file:
        for i, v in result_df.iterrows():

            result_string = f"0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')


