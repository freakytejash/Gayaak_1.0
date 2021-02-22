package com.example.gayaak_10.common.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivitySignupBinding;
import com.example.gayaak_10.model.response.DefaultResponseStatusMessage;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivitySignupBinding binding;
    public static UserDataContract userDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.layoutGoogleSignIn.setOnClickListener(this);
        binding.createAccount.setOnClickListener(this);
        binding.signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutGoogleSignIn:
                signIn();
                break;
            case R.id.create_account:
                Utility.hideSoftKeyboard(this, view);
                checkAllRequiredFields();
                break;
            case R.id.sign_in:
                clearEditText();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                break;
        }
    }


    /*----------------------------------------------Google Sign Up-------------------------------------*/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            binding.progressSignUp.setVisibility(View.VISIBLE);
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
        if (account != null) {
            String profileImage;
            if (acct.getPhotoUrl() != null) {
                profileImage = acct.getPhotoUrl().toString();
            } else {
                profileImage = "";
            }
            registerUser(acct.getEmail(), "", acct.getGivenName(), acct.getFamilyName(), "", "Google");
        }
    }

    /*----------------------------------------------Email Sign Up-------------------------------------*/
    private void registerUser(String email, String password, String firstName, String lastName, String image, String loggedInFrom) {
        binding.progressSignUp.setVisibility(View.VISIBLE);
        UserDataContract userDataProfile = new UserDataContract();
        userDataProfile.userId = 0;
        userDataProfile.roleId = 3;
        userDataProfile.firstName = firstName;
        userDataProfile.lastName = lastName;
        userDataProfile.email = email;
        userDataProfile.phone = "";
        userDataProfile.gender = null;
        userDataProfile.profileImage = image;
        userDataProfile.isEmailVerified = true;
        userDataProfile.isPhoneVerified = true;
        userDataProfile.password = password;
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


        Log.d("check", "userData -> " + userDataProfile.email);
        registerSocial(userDataProfile);
    }

    private void registerSocial(UserDataContract userDataProfile) {
        Call<UserLoginData> registerCall = Constant.retrofitService.registerUserBySocial(userDataProfile);
        registerCall.enqueue(new Callback<UserLoginData>() {
            @Override
            public void onResponse(Call<UserLoginData> call, Response<UserLoginData> response) {
                if (response.isSuccessful()) {
                    if (response.body().status && response.body().detail != null) {
                        binding.progressSignUp.setVisibility(View.GONE);
                        App.isSocial = true;
                        Log.d("token", " ==> " + response.body().detail.tockenString + " userId -> " + response.body().detail.userDataContract.userId);
                        userDetail = response.body().detail.userDataContract;
                        userDetail.registerType = "Social";
                        storeDataNavigate(response.body());
                    } else {
                        logoutSocialAcc();
                        binding.progressSignUp.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    logoutSocialAcc();
                    binding.progressSignUp.setVisibility(View.GONE);
                    fetchErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserLoginData> call, Throwable t) {
                binding.progressSignUp.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchErrorResponse(ResponseBody response) {
        binding.progressSignUp.setVisibility(View.GONE);
        Gson gson = new GsonBuilder().create();
        ErrorPojoClass mError = new ErrorPojoClass();
        try {
            mError = gson.fromJson(response.string(), ErrorPojoClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utility.customDialogBoxTextWithSingle(SignUpActivity.this, "Something went wrong.", mError.message);
    }

    private void logoutSocialAcc() {
        GoogleSignIn.getClient(SignUpActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()).signOut();
    }

    private void storeDataNavigate(UserLoginData response) {
        if (response.detail.tockenString != null) {
            SharedPrefsUtil.setStringPreferences(SignUpActivity.this, "userToken", response.detail.tockenString);
            Constant.token = SharedPrefsUtil.getStringPreferences(SignUpActivity.this, "userToken");
        }
        if (response.detail.userDataContract != null) {
            SharedPrefsUtil.setStringPreferences(SignUpActivity.this, "userId", response.detail.userDataContract.userId.toString());
            Constant.userId = SharedPrefsUtil.getStringPreferences(SignUpActivity.this, "userId");
            if (response.detail.userDataContract.phone.isEmpty()) {
                openPreferences(response.detail.userDataContract.email, "", "Social", response.detail.userDataContract);
            } else {
                getUserProfile(response.detail.userDataContract.userId.toString());
            }

        } else {
            logoutSocialAcc();
            Utility.customDialogBoxTextWithSingle(SignUpActivity.this, "Something went wrong...", "");
        }
    }

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
            binding.etPassword.setError("Password cannot be empty");
            return;
        }
        if (binding.etConfirmPassword.getText().toString().isEmpty()) {
            binding.etConfirmPassword.setError("Password cannot be empty");
            return;
        }

        if (!binding.etPassword.getText().toString().trim().equalsIgnoreCase(binding.etConfirmPassword.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utility.isPasswordValid(binding.etConfirmPassword.getText().toString())) {
            Utility.customDialogBoxTextWithSingle(SignUpActivity.this, "", "Your password must be of length 6 and must " +
                    "contain one uppercase, lower case, special character and numeric.");
            return;
        }

        if (binding.etConfirmPassword.getText().toString().equals(binding.etPassword.getText().toString())) {
            registerByEmail(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void goToHome(Integer roleId) {
        binding.progressSignUp.setVisibility(View.GONE);
        clearEditText();

        if (roleId == 3) {
            //student
            Intent intent = new Intent(SignUpActivity.this, StudentHomeActivity.class);
            intent.putExtra("isNewRegistration", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            finish();
        } else if (roleId == 2) {
            //tutor
            Intent intent = new Intent(SignUpActivity.this, TutorHomeActivity.class);
            intent.putExtra("isNewRegistration", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            finish();
        }
    }


    private void getUserProfile(final String userId) {
        Call<UserDataProfile> callUserProfile = Constant.retrofitServiceHeader.getUserProfile(userId);
        callUserProfile.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                if (response.isSuccessful()) {
                    App.userDataContract = response.body();
                    SharedPrefsUtil.setUserPreferences(SignUpActivity.this, response.body());
                    goToHome(response.body().detail.roleId);
                } else {
                    fetchErrorResponse(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserDataProfile> call, Throwable t) {
                getUserProfile(userId);
                Toast.makeText(SignUpActivity.this, "Something went wrong!!\n" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", "OnFailure" + t.getLocalizedMessage());
            }
        });
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
        binding.etConfirmPassword.setText("");
    }

    /*Register email*/
    private void registerByEmail(String emailId, String password) {
        binding.progressSignUp.setVisibility(View.VISIBLE);
        Call<DefaultResponseStatusMessage> checkEmail = Constant.retrofitService.checkEmailExist(emailId);
        checkEmail.enqueue(new Callback<DefaultResponseStatusMessage>() {
            @Override
            public void onResponse(Call<DefaultResponseStatusMessage> call, Response<DefaultResponseStatusMessage> response) {
                binding.progressSignUp.setVisibility(View.GONE);
                if (!response.body().status) {
                    openPreferences(emailId, password, "Email", null);
                }else {
                    Utility.customDialogBoxTextWithSingle(SignUpActivity.this, "", "Email id is already registered..");
                }
            }

            @Override
            public void onFailure(Call<DefaultResponseStatusMessage> call, Throwable t) {
                binding.progressSignUp.setVisibility(View.GONE);
                Utility.customDialogBoxTextWithSingle(SignUpActivity.this, "", t.getMessage().toString());
            }
        });
    }

    private void openPreferences(String email, String password, String type, UserDataContract userDataContract) {
        binding.progressSignUp.setVisibility(View.GONE);
        Intent intent = new Intent(SignUpActivity.this, UserInfoActivity.class);
        if (type.equalsIgnoreCase("Social")) {
            intent.putExtra("userDataProfile", userDataContract);
        }
        intent.putExtra("fragmentType", type);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        finish();
    }
}
