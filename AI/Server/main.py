from fastapi import FastAPI, Depends
import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

from Server.services import * 
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

app = FastAPI()

# 동적 로직을 처리하는 의존성 함수 정의
async def dynamic_logic(user_id: str, wanted_algorithm_list: str = None):
    if  wanted_algorithm_list is None:
        # param2가 제공되지 않은 경우에 대한 로직
        return (user_id, [])
    else:
        # param2가 제공된 경우에 대한 로직
        return (user_id, wanted_algorithm_list)


@app.get("/get_list")
async def get_list(logic_result: str = Depends(dynamic_logic)):

    user_id, wanted_algorithm_list = logic_result
    if(isinstance(wanted_algorithm_list, str)):
        wanted_algorithm_list = wanted_algorithm_list.split(',')

    #일단 DB에 들어온 이름 사용자 추가.
    insert_update_user(user_id)
    
    user_libffm_data_path = f'../Dataset/user_libffm_data_folder/{user_id}/'

    user_libffm_data_path = os.path.join(user_libffm_data_path, f"{user_id}_libffm.txt")
    
    if(os.path.exists(user_libffm_data_path)):
        
        recommend_list, user_weak_algorithm = get_recommend_list(user_id, wanted_algorithm_list)
        return {"recommend_list": recommend_list, "user_weak_algorithm": user_weak_algorithm}

    else:
        return "존재하지 않는 유저입니다. 유저님의 정보가 모델에 반영되면 문제를 추천해드리겠습니다."


@app.get("/get_problem_information")
async def get_problem_information(problem_number: int):
    
    problem_info_query = "SELECT problem_description, problem_input, problem_output FROM problem_information_table WHERE problem_number = %s;"
    cursor.execute(problem_info_query, (problem_number, ))
    problem_info = cursor.fetchall()
    
    return {
        "problem_context": problem_info[0][0],
        "problem_input": problem_info[0][1],
        "problem_output": problem_info[0][2]
    }


