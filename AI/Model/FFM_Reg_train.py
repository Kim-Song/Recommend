import xlearn as xl
import os
import numpy as np
from sklearn.metrics import mean_squared_error



param = {'epoch':10, 
         'task':'reg', 
         'lr':0.02, 
         'lambda':0.002, 
         'opt':'ftrl', 
         'metric': 'rmse',
         #'fold':5
         }

def train_reg_ffm(param):

    lib_path = '../Dataset/libffm/'
    model_path = '../Dataset/ffm_model/'
    result_path = '../Dataset/ffm_result'

    ffm_model = xl.create_ffm()
    ffm_model.setTrain(os.path.join(lib_path, "FFM_train_reg.txt"))

    #ffm_model.cv(param)
    print('--------training...--------')
    ffm_model.fit(param, os.path.join(model_path, "train_ffm_reg_model.out"))
    
    ffm_model.setTest(os.path.join(lib_path, "FFM_valid_x_reg.txt"))
    ffm_model.predict(os.path.join(model_path, "train_ffm_reg_model.out"), os.path.join(result_path, "valid_reg_output.txt"))

    y_reg_true = []
    with open(os.path.join(lib_path, "FFM_valid_y_reg.txt"), 'r') as file:
        for line in file:
            first_value = int(line.split()[0])
            y_reg_true.append(first_value)

    y_reg_scores = []
    with open(os.path.join(result_path, "valid_reg_output.txt"), 'r') as file:
        for line in file:
            value = float(line.strip())
            y_reg_scores.append(value)

    rmse_score = np.sqrt(mean_squared_error(y_reg_true, y_reg_scores)) 
    print(rmse_score)

train_reg_ffm(param)
