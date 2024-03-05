import mysql.connector
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
from collections import Counter
from sklearn.model_selection import train_test_split

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

def create_libffm_df():

    query = "SELECT * FROM user_problem_interaction_table"

    cursor.execute(query)
    user_problem_interaction = cursor.fetchall()

    user_problem_interaction = pd.DataFrame(user_problem_interaction, columns = ['user_problem_key',
    'user_id',
    'problem_number',
    'correct_attempts_count',
    'incorrect_attempts_count',
    'time_limit_exceeded_count',
    'memory_limit_exceeded_count',
    'output_limit_exceeded_count',
    'runtime_error_count', 
    'compile_error_count',
    'output_format_issue_count', 
    'most_used_language',
    'best_memory_usage',
    'fastest_execution_time',
    'memory_limit_optimal_solution_submission_number', 
    'time_limit_optimal_solution'])

    query = "SELECT * FROM user_information_table"

    cursor.execute(query)
    user_information = pd.DataFrame(cursor.fetchall(), columns = ['user_id', 
        'user_key',                                                         
        'user_tier',
        'user_rank',
        'user_solved_problem_list',
        'user_failed_problem_list',
        'user_solved_problem_count',
        'user_weak_algorithm'])

    query = "SELECT * FROM problem_information_table"

    cursor.execute(query)
    problem_information = pd.DataFrame(cursor.fetchall(), columns = ['problem_number',
    'problem_key',
    'problem_name',
    'problem_tier',
    'problem_algorithm',
    'problem_time_condition',
    'problem_memory_condition',
    'problem_submission_count',
    'problem_answer_submission_count',
    'problem_answered_people_count',
    'problem_correct_rate',
    'problem_description',
    'problem_input',
    'problem_output',
    'other_result_form_1',
    'other_result_form_2'])

    new_columns = {'user_key': 'user_id', 'problem_key': 'problem_number'}

    libffm_df = pd.DataFrame(user_problem_interaction.iloc[:,1:11])
    libffm_df.insert(0, 'target', libffm_df.iloc[:,3:10].sum(axis=1))
    libffm_df.drop(['correct_attempts_count', 'incorrect_attempts_count', 'time_limit_exceeded_count', 'memory_limit_exceeded_count', 'output_limit_exceeded_count', 'runtime_error_count', 'compile_error_count', 'output_format_issue_count'], axis=1, inplace=True)
    libffm_df = pd.merge(libffm_df, user_information[['user_id', 'user_key','user_tier','user_failed_problem_list','user_weak_algorithm']], how='inner', on = 'user_id')
    libffm_df = pd.merge(libffm_df, problem_information[['problem_number', 'problem_key','problem_tier','problem_algorithm']], how='inner', on = 'problem_number')
    libffm_df.drop(['user_id','problem_number'], axis=1, inplace = True)
    libffm_df.rename(columns=new_columns, inplace=True)
    libffm_df = libffm_df.sort_values(by = 'user_id').reset_index(drop=True)

    return libffm_df

def encoding_preprocessing_libffm_df(libffm_df):

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

    libffm_df['problem_algorithm'] = libffm_df['problem_algorithm'].apply(ast.literal_eval)
    libffm_df['user_weak_algorithm'] = libffm_df['user_weak_algorithm'].apply(ast.literal_eval)
    libffm_df['user_failed_problem_list'] = libffm_df['user_failed_problem_list'].apply(ast.literal_eval)
    libffm_df['problem_algorithm'] = libffm_df['problem_algorithm'].apply(lambda x: list(map(problem_algorithm_encoder.get, x)))
    libffm_df['user_weak_algorithm'] = libffm_df['user_weak_algorithm'].apply(lambda x: list(map(problem_algorithm_encoder.get, x)))
    libffm_df['user_failed_problem_list'] = libffm_df['user_failed_problem_list'].apply(lambda x: list(map(problem_number_encoder.get, x)))
    libffm_df['user_tier'] = libffm_df['user_tier'].map(tier_encoder)
    libffm_df['problem_tier'] = libffm_df['problem_tier'].map(tier_encoder)

    return libffm_df

def create_libffm_dataset(libffm_df):

    X = libffm_df.iloc[:,1:]
    y = libffm_df.iloc[:,:1]
    X['user_weak_algorithm'] = X['user_weak_algorithm'].apply(lambda x:x[:5])

    X_train, X_valid, y_train, y_valid = train_test_split(X, y, test_size=0.2, random_state=42)

    lib_path = "../Dataset/libffm/"

    user_id_field = 1
    user_tier_field = 0.5
    problem_number_field = 1
    problem_tier_field = 0.5
    user_failed_problem_field = 1
    problem_algorithm_field = 1
    user_weak_algorithm_field = 1

    with open(os.path.join(lib_path, 'FFM_binary.txt'), 'w') as file:
        for i, v in X.iterrows():

            result_string = f"{0 if y.loc[i, 'target']==0 else 1} 0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(lib_path, 'FFM_reg.txt'), 'w') as file:
        for i, v in X.iterrows():

            result_string = f"{y.loc[i, 'target']} 0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    # Binary

    with open(os.path.join(lib_path, 'FFM_train_binary.txt'), 'w') as file:

        for i, v in X_train.iterrows():

            result_string = f"{0 if y_train.loc[i, 'target']==0 else 1} 0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
                
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(lib_path, 'FFM_valid_x_binary.txt'), 'w') as file:
        for i, v in X_valid.iterrows():

            result_string = f"0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(lib_path, 'FFM_valid_y_binary.txt'), 'w') as file:
        for i, v in y_valid.iterrows():

            result_string = f"{0 if v[0]==0 else 1}"
            
            file.write(result_string + '\n')
        
    
    # Regression

    with open(os.path.join(lib_path, 'FFM_train_reg.txt'), 'w') as file:
        for i, v in X_train.iterrows():

            result_string = f"{y_train.loc[i, 'target']} 0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(lib_path, 'FFM_valid_x_reg.txt'), 'w') as file:
        for i, v in X_valid.iterrows():

            result_string = f"0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} "
            
            for num, algorithm_num in enumerate(v['user_failed_problem_list']):
                result_string += f"4:{algorithm_num}:{(user_failed_problem_field / len(v['user_failed_problem_list'])):.2f} "

            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(lib_path, 'FFM_valid_y_reg.txt'), 'w') as file:
        for i, v in y_valid.iterrows():

            result_string = f"{v[0]}"

            file.write(result_string + '\n')
