package com.example.gayaak_10.common.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.model.response.ConfigurationData;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.student.model.ModuleEbooks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonViewModel extends ViewModel {
    private MutableLiveData<ModuleEbooks> moduleEbooksMutableLiveData;
    private MutableLiveData<ConfigurationData> configurationDataMutableLiveData;
    private MutableLiveData<UserDataProfile> userDataProfileMutableLiveData;
    private MutableLiveData<WalletCoin> userWalletCoinMutableLiveData;

    /*--------------------------------------------------------------------------------------------*/

    public LiveData<WalletCoin> getWalletCoin(int userId){
        userWalletCoinMutableLiveData = new MutableLiveData<>();
        getWalletCoinById(userId);
        return userWalletCoinMutableLiveData;
    }

    private void getWalletCoinById(int userId) {
        Call<WalletCoin> coinsInWallet = Constant.retrofitService.getWalletCoinById(userId);
        coinsInWallet.enqueue(new Callback<WalletCoin>() {
            @Override
            public void onResponse(Call<WalletCoin> call, Response<WalletCoin> response) {
                if (response.isSuccessful()){
                    if (response.body().status && response.body().detail!=null){
                        userWalletCoinMutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletCoin> call, Throwable t) {

            }
        });
    }

/*-----------------------------------------------------------------------------------------------*/

    public LiveData<ModuleEbooks> getModuleEbook(int moduleId) {
        moduleEbooksMutableLiveData = new MutableLiveData<>();
        getModuleEbookById(moduleId);
        return moduleEbooksMutableLiveData;
    }

    private void getModuleEbookById(int moduleId) {
        Call<ModuleEbooks> moduleWithVideoCall = Constant.retrofitService.getModuleEBooks(moduleId);
        moduleWithVideoCall.enqueue(new Callback<ModuleEbooks>() {
            @Override
            public void onResponse(Call<ModuleEbooks> call, Response<ModuleEbooks> response) {
                if (response.isSuccessful()){
                    if (response.body().status && response.body().detail != null) {
                        moduleEbooksMutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ModuleEbooks> call, Throwable t) {

            }
        });
    }

    public LiveData<ConfigurationData> getConfiguration() {
        configurationDataMutableLiveData = new MutableLiveData<>();
        getConfigData();
        return configurationDataMutableLiveData;
    }


    private void getConfigData() {
        Call<ConfigurationData> call = Constant.retrofitService.getConfigurationData();
        call.enqueue(new Callback<ConfigurationData>() {
            @Override
            public void onResponse(Call<ConfigurationData> call, Response<ConfigurationData> response) {
                if (response.isSuccessful()){
                    if (response.body().status && response.body().detail != null) {
                        configurationDataMutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ConfigurationData> call, Throwable t) {

            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/
    public LiveData<UserDataProfile> getUserProfile(int userId) {
        userDataProfileMutableLiveData = new MutableLiveData<>();
        getUserById(userId);
        return userDataProfileMutableLiveData;
    }

    private void getUserById(int userId) {
        Call<UserDataProfile> call = Constant.retrofitService.getUserProfile(String.valueOf(userId));
        call.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                if (response.isSuccessful()){
                    if (response.body().status && response.body().detail != null) {
                        userDataProfileMutableLiveData.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDataProfile> call, Throwable t) {

            }
        });
    }

}
