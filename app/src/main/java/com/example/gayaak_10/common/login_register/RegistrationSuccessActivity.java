package com.example.gayaak_10.common.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityRegistrationSuccessBinding;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationSuccessActivity extends AppCompatActivity {

    private ActivityRegistrationSuccessBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGoDashboard.setOnClickListener(view -> {
            binding.progressSuccessRegister.setVisibility(View.VISIBLE);
            getUserProfile(getIntent().getStringExtra("userId"));
        });
    }


        private void goToHome(Integer roleId) {
            binding.progressSuccessRegister.setVisibility(View.GONE);

            if (roleId == 3){
                //student
                Intent intent = new Intent(RegistrationSuccessActivity.this, StudentHomeActivity.class);
                intent.putExtra("isNewRegistration", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                finish();
            }else if (roleId == 2){
                //tutor
                Intent intent = new Intent(RegistrationSuccessActivity.this, TutorHomeActivity.class);
                intent.putExtra("isNewRegistration", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                finish();
            }
        }


        private void getUserProfile(final String userId) {
            binding.progressSuccessRegister.setVisibility(View.VISIBLE);
            Call<UserDataProfile> callUserProfile = Constant.retrofitServiceHeader.getUserProfile(userId);
            callUserProfile.enqueue(new Callback<UserDataProfile>() {
                @Override
                public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                    if (response.isSuccessful()) {
                        binding.progressSuccessRegister.setVisibility(View.GONE);
                        App.userDataContract = response.body();
                        SharedPrefsUtil.setStringPreferences(RegistrationSuccessActivity.this, "userId" , userId);
                        SharedPrefsUtil.setUserPreferences(RegistrationSuccessActivity.this, response.body());
                        goToHome(response.body().detail.roleId);
                    }else {
                        Gson gson = new GsonBuilder().create();
                        ErrorPojoClass mError = new ErrorPojoClass();
                        try {
                            mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(RegistrationSuccessActivity.this, "Something went wrong!!\n" + mError.message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDataProfile> call, Throwable t) {
                    getUserProfile(userId);
                    binding.progressSuccessRegister.setVisibility(View.GONE);
                    Toast.makeText(RegistrationSuccessActivity.this, "Something went wrong!!\n" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("failed", "OnFailure" + t.getLocalizedMessage());
                }
            });
        }
}
