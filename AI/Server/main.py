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