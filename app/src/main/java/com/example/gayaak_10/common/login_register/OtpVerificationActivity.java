package com.example.gayaak_10.common.login_register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityOtpVerificationBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataContract;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.response.UserLoginData;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.widgets.customotp.OnOtpCompletionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gayaak_10.common.login_register.SignUpActivity.userDetail;

public class OtpVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityOtpVerificationBinding otpVerificationBinding;
    private String otp1 = "";
    private String emailId = "";
    private String userId = "";
    private UserDataContract userDataProfile;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpVerificationBinding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(otpVerificationBinding.getRoot());

        userDataProfile = (UserDataContract) getIntent().getSerializableExtra("userDataProfile");
        emailId = userDataProfile.email;

        otpVerificationBinding.tvOtpSentNumber.setText("An 4-digit code has been sent to " +emailId+". " +
                "Enter the code below to confirm your email address.");
        otpVerificationBinding.btnVerifyOtp.setOnClickListener(this);
        otpVerificationBinding.btnResend.setOnClickListener(this);
        otpVerificationBinding.ivOtpBack.setOnClickListener(this);

        otpVerificationBinding.otpView.requestFocus();

        otpVerificationBinding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Log.d("onOtpCompleted=>", otp.toString());
                otp1 = otp.toString();
            }

            @Override
            public void onInCompleteOtp(String otp) {
                otp1 = otp.toString();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnVerifyOtp:
                otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
                Utility.hideSoftKeyboard(OtpVerificationActivity.this, view);
                verifyOtp();
                break;

            case R.id.btnResend:
                otpVerificationBinding.otpView.setText("");
                Utility.hideSoftKeyboard(OtpVerificationActivity.this, view);
                sendOtpToEmail(emailId, userId);
                break;

            case R.id.ivOtpBack:
                finish();
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
                break;
        }
    }

    private void verifyOtp() {
        if (otp1.length() == 4) {
            verifyEnteredOtp(otp1);
        } else {
            otpVerificationBinding.progressOtp.setVisibility(View.GONE);
            Toast.makeText(OtpVerificationActivity.this, "Please enter correct OTP", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void verifyEnteredOtp(String otp1) {
        Call<DefaultResponse> verifyOtpCall = Constant.retrofitService.verifyOtp(emailId, otp1);
        verifyOtpCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()){
                    if(response.body().status) {
                    otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
                        createNewUser();
                }else {
                    otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                }
            }else {
                    otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                    Utility.customDialogBoxTextWithSingle(OtpVerificationActivity.this, "Something went wrong.", mError.message);;
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                otpVerificationBinding.otpView.setText("");
                Toast.makeText(OtpVerificationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewUser() {
        otpVerificationBinding.progressOtp.setVisibility(View.GONE);
        registerByEmail(userDataProfile);

    }

    private void registerByEmail(UserDataContract userDataProfile) {
        otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
        UserDataContract userDataContract = new UserDataContract();
        userDataContract.userId = 0;
        userDataContract.roleId = 3;
        userDataContract.firstName = userDataProfile.firstName;
        userDataContract.lastName = userDataProfile.lastName;
        userDataContract.email = userDataProfile.email;
        userDataContract.phone = userDataProfile.phone;
        userDataContract.gender = userDataProfile.gender;
        userDataContract.profileImage = userDataProfile.profileImage;
        userDataContract.isEmailVerified = userDataProfile.isEmailVerified;
        userDataContract.isPhoneVerified = userDataProfile.isPhoneVerified;
        userDataContract.password = userDataProfile.password;
        userDataContract.facebookId = userDataProfile.facebookId;
        userDataContract.googleId = userDataProfile.googleId;
        userDataContract.lastLogin = userDataProfile.lastLogin;
        userDataContract.registerType = userDataProfile.registerType;
        userDataContract.oTP = userDataProfile.oTP;
        userDataContract.loginOrSignup = userDataProfile.loginOrSignup;
        userDataContract.userName = userDataProfile.userName;
        userDataContract.loginUserId = userDataProfile.loginUserId;
        userDataContract.imageName = userDataProfile.imageName;
        userDataContract.userImage = userDataProfile.userImage;
        userDataContract.imageData = userDataProfile.imageData;
        userDataContract.accountTypeName = userDataProfile.accountTypeName;
        userDataContract.isActive = userDataProfile.isActive;
        userDataContract.createdDate = userDataProfile.createdDate;
        userDataContract.modifiedDate = userDataProfile.modifiedDate;
        userDataContract.createdBy = userDataProfile.createdBy;
        userDataContract.modifiedBy = userDataProfile.modifiedBy;
        userDataContract.CountryName = userDataProfile.CountryName;
        userDataContract.countryCode = userDataProfile.countryCode;
        userDataContract.Countryid = userDataProfile.Countryid;

        Call<UserLoginData> registerCall = Constant.retrofitService.postUser(userDataContract);
        registerCall.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                if (response.isSuccessful()) {
                    if (response.body().status && response.body().detail != null) {
                        otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
                        if (response.body().detail.tockenString != null) {
                            SharedPrefsUtil.setStringPreferences(OtpVerificationActivity.this, "userToken", response.body().detail.tockenString);
                            Constant.token = SharedPrefsUtil.getStringPreferences(OtpVerificationActivity.this, "userToken");
                        }
                        if (response.body().detail.userDataContract != null) {
                            SharedPrefsUtil.setStringPreferences(OtpVerificationActivity.this, "userId", response.body().detail.userDataContract.userId.toString());
                            Constant.userId = SharedPrefsUtil.getStringPreferences(OtpVerificationActivity.this, "userId");
                            Log.d("token", " ==> " + Constant.token + " userId -> " + Constant.userId);
                            userDetail = response.body().detail.userDataContract;
                            userDetail.registerType = "Email";
                            getUserProfile( Constant.userId);
                        } else {
                            otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                            Utility.customDialogBoxTextWithSingle(OtpVerificationActivity.this, "", "Email Id exists");
                        }
                    }
                } else {
                    otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                    fetchErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
                otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                Toast.makeText(OtpVerificationActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerByEmail(String emailId, String password) {
        otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
        UserDataContract userDataProfile = new UserDataContract();
        userDataProfile.userId = 0;
        userDataProfile.roleId = 3;
        userDataProfile.firstName = "";
        userDataProfile.lastName = "";
        userDataProfile.email = emailId;
        userDataProfile.phone = "";
        userDataProfile.gender = null;
        userDataProfile.profileImage = "";
        userDataProfile.isEmailVerified = true;
        userDataProfile.isPhoneVerified = true;
        userDataProfile.password = password;
        userDataProfile.facebookId = null;
        userDataProfile.googleId = null;
        userDataProfile.lastLogin = null;
        userDataProfile.registerType = null;
        userDataProfile.oTP = null;
        userDataProfile.loginOrSignup = 0;
        userDataProfile.userName = null;
        userDataProfile.loginUserId = 0;
        userDataProfile.imageName = "";
        userDataProfile.userImage = null;
        userDataProfile.imageData = null;
        userDataProfile.accountTypeName = null;
        userDataProfile.isActive = true;
        userDataProfile.createdDate = null;
        userDataProfile.modifiedDate = null;
        userDataProfile.createdBy = null;
        userDataProfile.modifiedBy = null;

        Call<UserLoginData> registerCall = Constant.retrofitService.postUser(userDataProfile);
        registerCall.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                if (response.isSuccessful()) {
                    if (response.body().status && response.body().detail != null) {
                        otpVerificationBinding.progressOtp.setVisibility(View.VISIBLE);
                        if (response.body().detail.tockenString != null) {
                            SharedPrefsUtil.setStringPreferences(OtpVerificationActivity.this, "userToken", response.body().detail.tockenString);
                            Constant.token = SharedPrefsUtil.getStringPreferences(OtpVerificationActivity.this, "userToken");
                        }
                        if (response.body().detail.userDataContract != null) {
                            SharedPrefsUtil.setStringPreferences(OtpVerificationActivity.this, "userId", response.body().detail.userDataContract.userId.toString());
                            Constant.userId = SharedPrefsUtil.getStringPreferences(OtpVerificationActivity.this, "userId");
                            goToSuccess(Constant.userId);
                        } else {
                            otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                            Utility.customDialogBoxTextWithSingle(OtpVerificationActivity.this, "", "Email Id exists");
                        }
                    }
                } else {
                    otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                    fetchErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
                otpVerificationBinding.progressOtp.setVisibility(View.GONE);
                Toast.makeText(OtpVerificationActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserProfile(final String userId) {
        Call<UserDataProfile> callUserProfile = Constant.retrofitServiceHeader.getUserProfile(userId);
        callUserProfile.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                if (response.isSuccessful()) {
                    App.userDataContract = response.body();
                    SharedPrefsUtil.setUserPreferences(OtpVerificationActivity.this, response.body());
                    goToSuccess(String.valueOf(response.body().detail.userId));
                } else {
                    fetchErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserDataProfile> call, Throwable t) {
                getUserProfile(userId);
                Toast.makeText(OtpVerificationActivity.this, "Something went wrong!!\n" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", "OnFailure" + t.getLocalizedMessage());
            }
        });
    }

    private void fetchErrorResponse(ResponseBody response) {
        otpVerificationBinding.progressOtp.setVisibility(View.GONE);
        Gson gson = new GsonBuilder().create();
        ErrorPojoClass mError = new ErrorPojoClass();
        try {
            mError = gson.fromJson(response.string(), ErrorPojoClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utility.customDialogBoxTextWithSingle(OtpVerificationActivity.this, "Something went wrong.", mError.message);
    }

    private void goToSuccess(String userId) {
        otpVerificationBinding.progressOtp.setVisibility(View.GONE);
        Intent intent = new Intent(OtpVerificationActivity.this, RegistrationSuccessActivity.class);
        intent.putExtra("userId", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        finish();
    }

    private void sendOtpToEmail(final String email, final String userId) {
        Call<DefaultResponse> callOtp = Constant.retrofitService.sendOtp(email);
        callOtp.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Toast.makeText(OtpVerificationActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        otpVerificationBinding.otpView.clearFocus();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(OtpVerificationActivity.this, "Something went wrong.", mError.message);;
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(OtpVerificationActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
