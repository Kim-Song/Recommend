import xlearn as xl
import os
import numpy as np
from sklearn.metrics import roc_auc_score

param = {'epoch':10, 
         'task':'binary', 
         'lr':0.02, 
         'lambda':0.002, 
         'opt':'ftrl', 
         'metric': 'auc',
         #'fold':5
         }

def train_binary_ffm(param):

    lib_path = '../Dataset/libffm/'
    model_path = '../Dataset/ffm_model/'
    result_path = '../Dataset/ffm_result'

    ffm_model = xl.create_ffm()

    print('-----training...-----')

    ffm_model.setTrain(os.path.join(lib_path, "FFM_train_binary.txt"))

    #ffm_model.cv(param)

    ffm_model.fit(param, os.path.join(model_path, "train_ffm_binary_model.out"))

    print('-----predict...-----')

    ffm_model.setSigmoid()
    ffm_model.setTest(os.path.join(lib_path, "FFM_valid_x_binary.txt"))
    ffm_model.predict(os.path.join(model_path, "train_ffm_binary_model.out"), os.path.join(result_path, "valid_binary_output.txt"))

    y_binary_true = []
    with open(os.path.join(lib_path, "FFM_valid_y_binary.txt"), 'r') as file:
        for line in file:
            first_value = int(line.split()[0])
            y_binary_true.append(first_value)

    y_binary_scores = []
    with open(os.path.join(result_path, "valid_binary_output.txt"), 'r') as file:
        for line in file:
            value = float(line.strip())
            y_binary_scores.append(value)

    auc_score = np.sqrt(roc_auc_score(y_binary_true, y_binary_scores))
    print(auc_score)

train_binary_ffm(param)
