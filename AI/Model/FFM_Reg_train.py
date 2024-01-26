import xlearn as xl
import os
import numpy as np
from sklearn.metrics import mean_squared_error

user_libffm_path = '../Dataset/user_libffm_data_folder/'
lib_path = '../Dataset/lib_dataset/'
user_libffm_result_path = '../Dataset/user_FFM_result_folder/'
encoder_data_path = '../DataSet/Encoder/'
model_path = 'ffm_model/'

param = {'epoch':50, 
         'task':'reg', 
         'lr':0.02, 
         'lambda':0.002, 
         'opt':'ftrl', 
         'metric': 'rmse',
         #'fold':5
         }

def train_reg_ffm(param):

    lib_path = '../Dataset/lib_dataset/'

    ffm_model = xl.create_ffm()
    ffm_model.setTrain(os.path.join(lib_path, "FFM_train_reg.txt"))

    #ffm_model.cv(param)
    print('--------training...--------')
    ffm_model.fit(param, os.path.join(model_path, "train_ffm_reg_model.out"))
    
    ffm_model.setTest(os.path.join(lib_path, "FFM_test_reg.txt"))
    ffm_model.predict(os.path.join(model_path, "train_ffm_reg_model.out"), os.path.join(lib_path, "train_reg_output.txt"))

    y_reg_true = []
    with open(os.path.join(lib_path, "FFM_valid_reg.txt"), 'r') as file:
        for line in file:
            first_value = int(line.split()[0])
            y_reg_true.append(first_value)

    y_reg_scores = []
    with open(os.path.join(lib_path, "train_reg_output.txt"), 'r') as file:
        for line in file:
            value = float(line.strip())
            y_reg_scores.append(value)

    rmse_score = np.sqrt(mean_squared_error(y_reg_true, y_reg_scores)) 
    print(rmse_score)

train_reg_ffm(param)
