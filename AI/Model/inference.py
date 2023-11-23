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

data_path = '../DataSet/'
user_libffm_path = '../Dataset/user_libffm_data_folder/'
lib_path = '../Dataset/lib_dataset/'
user_libffm_result_path = '../Dataset/user_FFM_result_folder/'
encoder_data_path = '../DataSet/Encoder/'
model_path = '../Model/ffm_model/'

def get_binary_recommend_result(user_name):

    problem_number_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_number_Encoder.joblib'))
    problem_algorithm_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_algorithm_Encoder.joblib'))
    
    ffm_binary_model = xl.create_ffm()
    ffm_binary_model.setSigmoid()
    ffm_binary_model.setTest(os.path.join(user_libffm_path, f"{user_name}_libffm_binary.txt"))
    ffm_binary_model.predict(os.path.join(model_path, "ffm_binary_model.out"), os.path.join(user_libffm_result_path, f"{user_name}_ffm_binary_result.txt"))

    result = []
    with open(os.path.join(user_libffm_result_path, f"{user_name}_ffm_binary_result.txt")) as file:
        for line in file:
            value = float(line.strip())
            result.append(value)

    recommend_dict = defaultdict(list)

    with open(os.path.join(user_libffm_path, f"{user_name}_libffm_binary.txt"), 'r') as file:
        user_tier = re.findall(r'1:(\d+):[.\d]+', file.readline())
        user_weak_algorithm = re.findall(r'6:(\d+):[.\d]+', file.readline())

        file.seek(0)
        
        for i, line in enumerate(file):
            values = (re.findall(r'2:(\d+):1', line), re.findall(r'3:(\d+):0.5', line))
            recommend_dict[int(values[1][0])].append(((problem_number_Encoder.inverse_transform([int(values[0][0])]))[0] , result[i]))

    sorted_dict = {key: sorted(value, key=lambda x: -x[1]) for key, value in recommend_dict.items()}
    user_weak_algorithm = [int(x) for x in user_weak_algorithm]
    user_weak_algorithm = problem_algorithm_Encoder.inverse_transform(user_weak_algorithm)

    
    
    return sorted_dict, user_tier, user_weak_algorithm

def get_reg_recommend_result(user_name):

    problem_number_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_number_Encoder.joblib'))
    problem_algorithm_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_algorithm_Encoder.joblib'))
    
    ffm_reg_model = xl.create_ffm()
    ffm_reg_model.setTest(os.path.join(user_libffm_path, f"{user_name}_libffm_reg.txt"))
    ffm_reg_model.predict(os.path.join(model_path, "ffm_reg_model.out"), os.path.join(user_libffm_result_path, f"{user_name}_ffm_reg_result.txt"))

    result = []
    with open(os.path.join(user_libffm_result_path, f"{user_name}_ffm_reg_result.txt")) as file:
        for line in file:
            value = float(line.strip())
            result.append(value)

    recommend_dict = defaultdict(list)

    with open(os.path.join(user_libffm_path, f"{user_name}_libffm_reg.txt"), 'r') as file:
        user_tier = re.findall(r'1:(\d+):[.\d]+', file.readline())
        user_weak_algorithm = re.findall(r'6:(\d+):[.\d]+', file.readline())

        file.seek(0)
        
        for i, line in enumerate(file):
            values = (re.findall(r'2:(\d+):1', line), re.findall(r'3:(\d+):0.5', line))
            recommend_dict[int(values[1][0])].append(((problem_number_Encoder.inverse_transform([int(values[0][0])]))[0] , result[i]))
    
    sorted_dict = {key: sorted(value, key=lambda x: -x[1]) for key, value in recommend_dict.items()}
    user_weak_algorithm = [int(x) for x in user_weak_algorithm]
    user_weak_algorithm = problem_algorithm_Encoder.inverse_transform(user_weak_algorithm)
    
    return sorted_dict, user_tier, user_weak_algorithm


def get_user_non_problem(user):

    user_df = pd.read_csv(os.path.join(data_path, 'final_Baekjoon_유저_크롤링.csv'))

    #학습되어 있지 않은 유저라면,
    if(not user in list(user_df['user_id'].unique())):
        result = -1
        return result
    print("----------------------------------------------------------------------------------------------")
    print(f"Creating {user}'s libffm file...")
    print("----------------------------------------------------------------------------------------------")

    problem_df = pd.read_csv(os.path.join(data_path, 'final_Baekjoon_문제_크롤링.csv'))
    lib_df = pd.read_csv(os.path.join(lib_path, 'Non_Encoding_lib_data.csv'))

    user_ID_Encoder = joblib.load(os.path.join(encoder_data_path, 'user_ID_Encoder.joblib'))
    problem_number_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_number_Encoder.joblib'))
    problem_algorithm_Encoder = joblib.load(os.path.join(encoder_data_path, 'problem_algorithm_Encoder.joblib'))

    problem_list = problem_df['problem_number']
    u_problem_list = user_df.groupby('user_id')['problem_number'].apply(list).to_dict()[user]
    user_tier = user_df.loc[user_df['user_id'] == user,'user_tier'].iloc[0]
    user_weak_algorithm = lib_df.loc[lib_df['user_id']==user, 'user_weak_algorithm'].iloc[0]
    user_weak_algorithm = ast.literal_eval(user_weak_algorithm)[:5]

    ans = [item for item in problem_list if item not in u_problem_list]

    user_non_problem_df = pd.DataFrame({'user_id': [user]*len(ans), 'problem_number': ans, 'user_tier': [user_tier]*len(ans), 'user_weak_algorithm': [user_weak_algorithm] * len(ans)})
    
    semi_problem_df = problem_df.drop(['problem_name',
        'problem_time_condition', 'problem_memory_condition',
        'problem_submission_count', 'problem_answer_submission_count',
        'problem_answered_people_count', 'problem_correct_rate',
        'problem_avg_try'],axis=1)
    
    user_non_problem_df = user_non_problem_df.merge(semi_problem_df, on = ['problem_number'], how='left')
    
    user_non_problem_df['problem_algorithm'] = user_non_problem_df['problem_algorithm'].apply(ast.literal_eval)

    user_non_problem_df['user_id'] = user_ID_Encoder.transform(user_non_problem_df['user_id'])
    user_non_problem_df['problem_number'] = problem_number_Encoder.transform(user_non_problem_df['problem_number'])
    user_non_problem_df['problem_algorithm'] = user_non_problem_df['problem_algorithm'].apply(lambda x: problem_algorithm_Encoder.transform(x))
    user_non_problem_df['user_weak_algorithm'] = user_non_problem_df['user_weak_algorithm'].apply(lambda x: problem_algorithm_Encoder.transform(x))

    return user_non_problem_df

def create_user_ffm_data(user_name, result_df):

    user_id_field = 1
    user_tier_field = 0.5
    problem_number_field = 1
    problem_tier_field = 0.5
    cat_problem_avg_try_field = 0
    problem_algorithm_field = 1
    user_weak_algorithm_field = 5
    
    with open(os.path.join(user_libffm_path, f'{user_name}_libffm_binary.txt'), 'w') as file:
        for i, v in result_df.iterrows():

            result_string = f"0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} 4:{v['cat_problem_avg_try']}:{cat_problem_avg_try_field} "
            
            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')

    with open(os.path.join(user_libffm_path, f'{user_name}_libffm_reg.txt'), 'w') as file:
        for i, v in result_df.iterrows():

            result_string = f"0:{v['user_id']}:{user_id_field} 1:{v['user_tier']}:{user_tier_field} 2:{v['problem_number']}:{problem_number_field} 3:{v['problem_tier']}:{problem_tier_field} 4:{v['cat_problem_avg_try']}:{cat_problem_avg_try_field} "
            
            for num, algorithm_num in enumerate(v['problem_algorithm']):
                result_string += f"5:{algorithm_num}:{(problem_algorithm_field / len(v['problem_algorithm'])):.2f} "
            
            for num, algorithm_num in enumerate(v['user_weak_algorithm']):
                result_string += f"6:{algorithm_num}:{(user_weak_algorithm_field / len(v['user_weak_algorithm']) * (1 - num/10)):.2f} "
            
            file.write(result_string + '\n')


def get_recommend_list(user_name):
    
    binary_dict, user_tier, user_weak_algorithm = get_binary_recommend_result(user_name)
    reg_dict, user_tier, user_weak_algorithm = get_reg_recommend_result(user_name)

    user_tier = int(user_tier[0])
    user_weak_algorithm = list(user_weak_algorithm)

    fin_binary_keys = [x for x in range(6, user_tier + 4) if x<=22]
    fin_binary_dict = {key: binary_dict[key] for key in fin_binary_keys}

    fin_reg_keys = [x for x in range(6, user_tier + 4) if x<=22]
    fin_reg_dict = {key: reg_dict[key] for key in fin_reg_keys}

    fin_binary_dict = {key: [x[0] for x in value[:10]] for key, value in fin_binary_dict.items()}
    fin_reg_dict = {key: [x[0] for x in value[:10]] for key, value in fin_reg_dict.items()}

    fin_dict = {key: list(set(fin_reg_dict[key]+fin_binary_dict[key])) for key in fin_reg_dict.keys()}
    
    selected_keys = random.sample(fin_dict.keys(), 3)
    selected_values = [int(random.choice(fin_dict[key])) for key in selected_keys]


    return selected_values, user_weak_algorithm
