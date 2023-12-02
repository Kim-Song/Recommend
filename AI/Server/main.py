from fastapi import FastAPI
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from Model.inference import *

app = FastAPI()

# GET 요청에 대한 엔드포인트 정의
@app.get("/get_list/{user_name}")
def get_list(user_name):

    user_libffm_data_path = '../Dataset/user_libffm_data_folder'
    flag = True

    user_binary_libffm_data_path = os.path.join(user_libffm_data_path, f"{user_name}_libffm_binary.txt")
    user_reg_libffm_data_path = os.path.join(user_libffm_data_path, f"{user_name}_libffm_reg.txt")


    if(not (os.path.exists(user_binary_libffm_data_path) and os.path.exists(user_reg_libffm_data_path))):

        result_df = get_user_non_problem(user_name)

        if(isinstance(result_df,pd.DataFrame)):
            create_user_ffm_data(user_name, result_df)
        else:
            flag = False
            return "존재하지 않는 유저입니다."

    if(flag):
        recommend_list, user_weak_algorithm = get_recommend_list(user_name)
        return {"recommend_list": recommend_list, "user_weak_algorithm": user_weak_algorithm}
    
@app.get("/get_problem_information/{problem_number}")
def get_problem_information(problem_number):
    
    problem_data_path = '../Dataset/Baekjoon_문제_context_크롤링.csv'
    
    problem_df = pd.read_csv(problem_data_path)

    return {
        "problem_context": problem_df.loc[problem_df['problem_number']==int(problem_number), 'problem_description'].values[0],
        "problem_input": problem_df.loc[problem_df['problem_number']==int(problem_number), 'problem_input'].values[0],
        "problem_output": problem_df.loc[problem_df['problem_number']==int(problem_number), 'problem_output'].values[0]
    }