package com.example.gayaak_10.common.login_register.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.student.model.LevelCategoryInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<LevelCategoryInfo> levelCategoryInfoMutableLiveData;

    public LiveData<LevelCategoryInfo> getAllCourseLevels() {
        if (levelCategoryInfoMutableLiveData == null) {
            levelCategoryInfoMutableLiveData = new MutableLiveData<>();
        }
        getCoursesLevels();
        return levelCategoryInfoMutableLiveData;
    }

    private void getCoursesLevels() {
        Call<LevelCategoryInfo> tutorByCourseLevelCall = Constant.retrofitService.getLevelCategory();
        tutorByCourseLevelCall.enqueue(new Callback<LevelCategoryInfo>() {
            @Override
            public void onResponse(Call<LevelCategoryInfo> call, Response<LevelCategoryInfo> response) {
                levelCategoryInfoMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<LevelCategoryInfo> call, Throwable t) {

            }
        });
    }
}
