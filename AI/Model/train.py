import xlearn as xl
import os

def train_reg_ffm():

    user_libffm_path = 'Dataset/user_libffm_data_folder/'
    lib_path = 'Dataset/lib_dataset/'

    ffm_model = xl.create_ffm()
    ffm_model.setTrain(os.path.join(lib_path, "FFM_train_reg.txt"))
    ffm_model.setValidate(os.path.join(lib_path, "FFM_valid_reg.txt"))

    param = {'task':'reg', 'lr':0.02, 'lambda':0.002, 'opt':'ftrl', 'metric': 'rmse'}

    ffm_model.fit(param, "ffm_model.out")


def train_binary_ffm():

    user_libffm_path = 'Dataset/user_libffm_data_folder/'
    lib_path = 'Dataset/lib_dataset/'

    ffm_model = xl.create_ffm()
    ffm_model.setTrain(os.path.join(lib_path, "FFM_train_reg.txt"))
    ffm_model.setValidate(os.path.join(lib_path, "FFM_valid_reg.txt"))

    param = {'task':'reg', 'lr':0.02, 'lambda':0.002, 'opt':'ftrl', 'metric': 'rmse'}

    ffm_model.fit(param, "ffm_model.out")