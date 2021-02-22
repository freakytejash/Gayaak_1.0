package com.example.gayaak_10.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.login_register.SignInActivity;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentChangePasswordBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.fragment.StudentProfileFragment;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.fragment.TutorProfileFragment;
import com.example.gayaak_10.utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FragmentChangePasswordBinding changePasswordBinding;
    private String userType = "";

    public ChangePasswordFragment(String userType) {
        this.userType = userType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        changePasswordBinding = FragmentChangePasswordBinding.inflate(getLayoutInflater());

        changePasswordBinding.changePasswordBack.setOnClickListener(this);
        changePasswordBinding.btnUpdateUserInfo.setOnClickListener(this);


       /* changePasswordBinding.etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Calculate password strength
                Utility.calculateStrength(changePasswordBinding.etNewPassword.getText().toString(),
                        changePasswordBinding.tvPasswordStrength);
            }
        });*/
        return changePasswordBinding.getRoot();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changePasswordBack:
                if (userType.equalsIgnoreCase("Tutor")) {
                    TutorHomeActivity.addFragment(new TutorProfileFragment(""), "UserProfile", getActivity());
                } else if (userType.equalsIgnoreCase("Student")) {
                    StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
                }
                break;

            case R.id.btnUpdateUserInfo:
                Utility.hideSoftKeyboard(getActivity(), view);
                checkPasswordsField();
                break;
        }
    }

    private void checkPasswordsField() {

        if (changePasswordBinding.etOldPassword.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (changePasswordBinding.etNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!changePasswordBinding.etOldPassword.getText().toString().trim().equalsIgnoreCase(App.userDataContract.detail.password)) {
            Toast.makeText(getActivity(), "Password entered is same as the old password. Enter new password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (changePasswordBinding.etNewPassword.getText().toString().trim().equalsIgnoreCase(App.userDataContract.detail.password)) {
            Toast.makeText(getActivity(), "Password entered is same as the old password. Enter new password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!changePasswordBinding.etNewPassword.getText().toString().trim().equalsIgnoreCase(changePasswordBinding.etConfirmPassword.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utility.isPasswordValid(changePasswordBinding.etNewPassword.getText().toString())) {
            Utility.customDialogBoxTextWithSingle(getActivity(), "", "Your password must be of length 6 and must " +
                    "contain one uppercase, lower case, special character and numeric.");

//            Toast.makeText(getActivity(), "Your password must be on length 6 and " +
//                    "contain one uppercase, lower case, special character and numeric.", Toast.LENGTH_LONG).show();
            return;
        }

        Call<DefaultResponse> changePassword = Constant.retrofitService.changeEmailPassword(App.userDataContract.detail.userId,
                changePasswordBinding.etNewPassword.getText().toString().trim(), changePasswordBinding.etOldPassword.getText().toString());
        changePassword.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().status) {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        new MaterialAlertDialogBuilder(getActivity())
                                .setIcon(R.drawable.gayaak_icon)
                                .setMessage("Password Changed Successfully.")
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    openChangedPasswordLogin();
                                })
                                .show();
                    } else {
                        Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong.", response.body().message);
                    }
                } else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong.", mError.message);
                    ;
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChangedPasswordLogin() {
        GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()).signOut();
        App.userDataContract = new UserDataProfile();
        App.coursesDetailArrayList.clear();
        App.trendingDetailArrayList.clear();
        App.allCoursesArrayList.clear();
        App.currencyType = "USD";
        App.cartList.clear();
        App.isSocial = false;
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        getActivity().finish();
    }
}
