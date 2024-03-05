import sys, os
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))

import xlearn as xl
import os
import re
import pandas as pd
import numpy as np
from sklearn.preprocessing import LabelEncoder
import joblib
import ast
from collections import defaultdict
import random
import pickle
import mysql.connector
from collections import Counter
from sklearn.model_selection import train_test_split
import time
import urllib.request
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options
from selenium.common.exceptions import StaleElementReferenceException
from selenium.common.exceptions import NoSuchElementException

chrome_options = Options()
chrome_options.add_argument('--headless')  # 웹 브라우저를 표시하지 않고 백그라운드에서 실행
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-dev-shm-usage')


from Server.db import * 
from Server.data import *
from Server.model import * 


model_path = '../Dataset/ffm_model/'
lib_path = "../Dataset/libffm/"

user_id_field = 1
user_tier_field = 0.5
problem_number_field = 1
problem_tier_field = 0.5
user_failed_problem_field = 1
problem_algorithm_field = 1
user_weak_algorithm_field = 1

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

update_user_id_list_query = 'SELECT user_id FROM update_user_table'
cursor.execute(update_user_id_list_query)
update_user_id_list = cursor.fetchall()
update_user_id_list = [u[0] for u in update_user_id_list]



for user_id in update_user_id_list:

    print(f"---Start {user_id} DataPipeline---")

    print('---Updating user information...---')
    user_update(user_id)

    #print('---Creating libffm dataframe...---')
    #libffm_df = create_libffm_df()

    #print('---Encoding libffm dataframe...---')
    #libffm_df = encoding_preprocessing_libffm_df(libffm_df)

    #print('---Creating libffm dataset...---')
    #create_libffm_dataset(libffm_df)

    #print('---Training ffm model...---')
    #train_ffm_model_out()

    #print('---Creating user libffm dataset...---')
    #result_df = create_user_test_libffm(user_id)
    #create_user_ffm_data(user_id, result_df)
    #print(f'{user_id} data updated successfully.')

    delete_user_id_from_update_user_table(user_id)
    print(f'{user_id} data deleted successfully.')

cursor.close()
conn.close()




