package com.example.gayaak_10.common.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivitySignInBinding;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataContract;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.response.UserLoginData;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignInBinding binding;
    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        binding.btnSignIn.setOnClickListener(this);
        binding.layoutGoogleSignIn.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 3) {
                    String email = charSequence.toString();
                    if (!Utility.isEmailValid(email)) {
                        Log.e(" ============> ", " email check");
                    } else {

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSignUp) {
            clearEditText();
            Utility.hideSoftKeyboard(SignInActivity.this, v);
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        }

        if (v.getId() == R.id.btnSignIn) {
            Utility.hideSoftKeyboard(SignInActivity.this, v);

            checkAllRequiredFields();
        }
        if (v.getId() == R.id.layoutGoogleSignIn) {
            signIn();
        }

        if (v.getId() == R.id.tvForgotPassword) {
            binding.etEmail.setText("");
            binding.etPassword.setText("");
            Utility.hideSoftKeyboard(SignInActivity.this, v);
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        }
    }

    //Email Login
    private void checkAllRequiredFields() {
        if (binding.etEmail.getText().toString().isEmpty()) {
            binding.etEmail.setError("Email cannot be empty.");
            return;
        }

        if (Utility.isEmailValid(binding.etEmail.getText().toString())) {
            binding.etEmail.setError("Please enter correct email address.");
            return;
        }

        if (binding.etPassword.getText().toString().isEmpty()) {
            binding.etPassword.setError("Password cannot be empty.");
            return;
        }

        //   Utility.customDialogBoxAuthenticating(SignInActivity.this, true);
        callLoginMethod(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim());
    }

    /*--------------------------------------------------------------------------------------------*/
    //Google Login
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(getString(R.string.app_name), "signInResult:failed code=" + e.getMessage());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.e(getString(R.string.app_name), "signInResult:account=" + account);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            registerUser(acct.getEmail(), acct.getGivenName(), acct.getFamilyName(), "Google");
        } else {
            Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong. signinpage", "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void registerUser(String email, String firstName, String lastName, String loggedInFrom) {
        UserDataContract userDataProfile = new UserDataContract();
        userDataProfile.userId = 0;
        userDataProfile.roleId = 3;
        userDataProfile.firstName = firstName;
        userDataProfile.lastName = lastName;
        userDataProfile.email = email;
        userDataProfile.phone = "";
        userDataProfile.gender = null;
        userDataProfile.profileImage = "";
        userDataProfile.isEmailVerified = true;
        userDataProfile.isPhoneVerified = true;
        userDataProfile.password = "";
        if (loggedInFrom.equalsIgnoreCase("Google")) {
            userDataProfile.facebookId = null;
            userDataProfile.googleId = 1;
        } else if (loggedInFrom.equalsIgnoreCase("Facebook")) {
            userDataProfile.facebookId = 1;
            userDataProfile.googleId = null;
        } else {
            userDataProfile.facebookId = null;
            userDataProfile.googleId = null;
        }
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


        /*SSO*/
        Log.d("check", "userData -> " + userDataProfile.email);
        Call<UserLoginData> registerCall = Constant.retrofitService.registerUserBySocial(userDataProfile);
        registerCall.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                if (response.isSuccessful()) {
                    if (response.body().status && response.body().detail != null) {
                        App.isSocial = true;
                        if (response.body().detail.tockenString != null) {
                            SharedPrefsUtil.setStringPreferences(SignInActivity.this, "userToken", response.body().detail.tockenString);
                            Constant.token = response.body().detail.tockenString;
                        }
                        if (response.body().detail.userDataContract != null) {
                            Constant.userId = response.body().detail.userDataContract.userId.toString();
                            SharedPrefsUtil.setStringPreferences(SignInActivity.this, "userId", response.body().detail.userDataContract.userId.toString());
                            Utility.customDialogBoxAuthenticating(SignInActivity.this, true);
                            getUserProfile(response.body().detail.userDataContract.userId.toString());
                        } else {
                            GoogleSignIn.getClient(SignInActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail().build()).signOut();
                            Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong...", "");
                        }
                    } else {
                        GoogleSignIn.getClient(SignInActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail().build()).signOut();
                        Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                        Utility.customDialogBoxTextWithSingle(SignInActivity.this, "LoginFailed.", response.body().message);
                    }
                } else {
                    GoogleSignIn.getClient(SignInActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build()).signOut();
                    Utility.customDialogBoxAuthenticating(SignInActivity.this, false);

                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                    Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", mError.message);
                }
            }

            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
                Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", t.getLocalizedMessage());
            }
        });
    }

    /*Simple Email*/
    private void callLoginMethod(String email, String password) {
        Call<UserLoginData> loginByEmail = Constant.retrofitService.loginByEmail(email, password);
        loginByEmail.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Utility.customDialogBoxAuthenticating(SignInActivity.this, true);
                        if (response.body().detail.tockenString != null) {
                            SharedPrefsUtil.setStringPreferences(SignInActivity.this, "userToken", response.body().detail.tockenString);
                            Constant.token = response.body().detail.tockenString;
                        }
                        if (response.body().detail.userDataContract != null) {
                            Constant.userId = response.body().detail.userDataContract.userId.toString();
                            SharedPrefsUtil.setStringPreferences(SignInActivity.this, "userId", response.body().detail.userDataContract.userId.toString());
                            getUserProfile(response.body().detail.userDataContract.userId.toString());
                        } else {
                            GoogleSignIn.getClient(SignInActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail().build()).signOut();
                            Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                            Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong...", "");
                        }
                    } else {
                        Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", response.errorBody().toString());
                    }
                } else {
                    Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", mError.message);
                }
            }

            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
                Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", t.getLocalizedMessage());
            }
        });
    }

    private void getUserProfile(final String userId) {
        Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
        Call<UserDataProfile> callUserProfile = Constant.retrofitServiceHeader.getUserProfile(userId);
        callUserProfile.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        App.userDataContract = response.body();

                        if (App.userDataContract.detail.currencyName==null){
                            App.userDataContract.detail.currencyName="INR";
                        }


                        /*if (App.userDataContract.detail.countryCode == null){
                            App.userDataContract.detail.countryCode=="+91";
                        }

                        if (App.userDataContract.detail.)*/
                        SharedPrefsUtil.setUserPreferences(SignInActivity.this, response.body());
                        goToHome(response.body().detail.roleId);
                    } else {
                        Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                        Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Login Failed", response.body().message.toString());
                    }
                } else {
                    Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong.", mError.message);
                }
            }

            @Override
            public void onFailure(Call<UserDataProfile> call, Throwable t) {
                Utility.customDialogBoxAuthenticating(SignInActivity.this, false);
                Utility.customDialogBoxTextWithSingle(SignInActivity.this, "Something went wrong!!", t.getLocalizedMessage());
                Log.d("failed", "OnFailure" + t.getLocalizedMessage());
            }
        });
    }

    private void goToHome(Integer roleId) {
        clearEditText();

        if (roleId == 3) {
            //student
            Intent intent = new Intent(SignInActivity.this, StudentHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            finish();
        } else if (roleId == 2) {
            //tutor
            Intent intent = new Intent(SignInActivity.this, TutorHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            finish();
        }

       /* new MaterialAlertDialogBuilder(SignInActivity.this)
                .setIcon(R.drawable.gayaak_icon)
                .setTitle("Login Type")
                .setMessage("Are you Student or Tutor?")
                .setPositiveButton("Demo Student", (dialogInterface, i) -> {
                    SharedPrefsUtil.setStringPreferences(SignInActivity.this, "loginType", "Student");
                    Intent intent = new Intent(SignInActivity.this, StudentHomeActivity.class);
                    intent.putExtra("userType", "Free");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                    finish();
                    dialogInterface.dismiss();
                })
                .setNeutralButton("Paid Student", (dialogInterface, i) -> {
                    SharedPrefsUtil.setStringPreferences(SignInActivity.this, "loginType", "Student");
                    Intent intent = new Intent(SignInActivity.this, StudentHomeActivity.class);
                    intent.putExtra("userType", "Paid");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                    finish();
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Tutor", (dialogInterface, i) -> {
                    SharedPrefsUtil.setStringPreferences(SignInActivity.this, "loginType", "Tutor");
                    Intent intent = new Intent(SignInActivity.this, TutorHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                    finish();
                    dialogInterface.dismiss();
                })
                .show();*/
       /* Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        finish();*/
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        finish();
        super.onBackPressed();
    }

    private void clearEditText() {
        binding.etEmail.setText("");
        binding.etPassword.setText("");
    }
}
