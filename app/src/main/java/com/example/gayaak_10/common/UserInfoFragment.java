package com.example.gayaak_10.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentUserInfoBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataContract;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.model.response.UserDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.fragment.StudentDemoHomeFragment;
import com.example.gayaak_10.student.fragment.StudentProfileFragment;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.fragment.TutorHomeFragment;
import com.example.gayaak_10.tutor.fragment.TutorProfileFragment;
import com.example.gayaak_10.utility.GPSTracker;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gayaak_10.utility.Utility.encodeImageToBase64;
import static com.example.gayaak_10.utility.Utility.getBitmapFromBase64;


public class UserInfoFragment extends Fragment implements View.OnClickListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FragmentUserInfoBinding userInfoBinding;
    private String profileImage = "";
    private boolean isProfileUpdated = false;
    private String userType = "";

    public UserInfoFragment(String userType) {
        this.userType = userType;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userInfoBinding = FragmentUserInfoBinding.inflate(getLayoutInflater());

        setUserData(App.userDataContract);

        userInfoBinding.userInfoBack.setOnClickListener(this);
        userInfoBinding.tvUserInfoName.setOnClickListener(this);
        userInfoBinding.tvAddImage.setOnClickListener(this);
        userInfoBinding.tvSaveInfo.setOnClickListener(this);

        return userInfoBinding.getRoot();
    }

    @SuppressLint("CheckResult")
    private void setUserData(UserDataProfile userData) {
        if (userData.detail != null) {
            Bitmap decodedProfileImage = getBitmapFromBase64(userData.detail.profileImage);

            Glide.with(getActivity()).load(decodedProfileImage)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .apply(new RequestOptions().override(600, 200))
                    .into(userInfoBinding.ivUserImage);
            if (userData.detail.firstName.isEmpty() || userData.detail.firstName == null) {
                userInfoBinding.tvUserInfoName.setText(userData.detail.email);
            } else {
                userInfoBinding.tvUserInfoName.setText(String.format("%s %s", userData.detail.firstName, userData.detail.lastName));
                userInfoBinding.etUserName.setText(userData.detail.firstName + " " + userData.detail.lastName);
            }
           /* try {
                String country = getActivity().getResources().getConfiguration().locale.getDisplayCountry();
                Log.e("country", "onCreate: " +country);
                String countryCode = String.valueOf(Utility.getCurrentCountryCode(getActivity()));
                userInfoBinding.etPhoneCode.setText("+" +countryCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            try {
                if (!Utility.hasPermissions(getActivity(), Utility.PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), Utility.PERMISSIONS, Utility.PERMISSION_ALL);
                }
                GPSTracker gps = new GPSTracker(getActivity());
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                String country = Utility.getAddress(getActivity(), latitude, longitude);
                Log.e("Location", "latitude: " +country);
                if (country.equalsIgnoreCase("India")) {
                    userInfoBinding.etPhoneCode.setText("+91");
                } else {
                    userInfoBinding.etPhoneCode.setText("+1");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            userInfoBinding.etUserInfoEmail.setText(userData.detail.email);
            userInfoBinding.etPhoneNo.setText(userData.detail.phone);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfoBack:
                if (userType.equalsIgnoreCase("Tutor")){
                    TutorHomeActivity.addFragment(new TutorProfileFragment(""), "UserProfile", getActivity());
                }else if (userType.equalsIgnoreCase("Student")){
                    StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
                }
                break;

            case R.id.tvAddImage:
                if (!Utility.hasPermissions(getActivity(), Utility.PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), Utility.PERMISSIONS, Utility.PERMISSION_ALL);
                } else {
                    ImagePicker.Companion.with(this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                }
                break;

            case R.id.tvSaveInfo:
                isProfileUpdated = false;
                userInfoBinding.etUserName.clearFocus();
                userInfoBinding.etPhoneNo.clearFocus();
                Utility.hideSoftKeyboard(getActivity(), view);
                updateUserInfo(userInfoBinding.etUserName.getText().toString().trim(),
                        userInfoBinding.etUserInfoEmail.getText().toString().trim(),
                        userInfoBinding.etPhoneNo.getText().toString(),
                        profileImage);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            userInfoBinding.ivUserImage.setImageURI(fileUri);

            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);
            String encodedImage = encodeImageToBase64(filePath);
            profileImage = encodedImage;
            isProfileUpdated = true;
            Log.d("upload", " ====> " + encodedImage);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserInfo(String name, String email, String number, String profileImage) {
        String firstName = "";
        String lastName = "";

        if (name.contains(" ")) {
            String[] parts = name.split(" ");
            firstName = parts[0];
            lastName = parts[1];
        } else {
            firstName = name;
        }
        UserDetail userData = App.userDataContract.detail;
        if (profileImage.isEmpty()) {
            profileImage = userData.profileImage;
        }

        UserDataContract userDataProfile = new UserDataContract();
        userDataProfile.userId = userData.userId;
        userDataProfile.roleId = userData.roleId;
        userDataProfile.firstName = firstName;
        userDataProfile.lastName = lastName;
        userDataProfile.email = email;
        userDataProfile.phone = number;
        userDataProfile.gender = userData.gender;
        userDataProfile.profileImage = profileImage;
        userDataProfile.isEmailVerified = userData.isEmailVerified;
        userDataProfile.isPhoneVerified = userData.isPhoneVerified;
        userDataProfile.password = userData.password;
        userDataProfile.facebookId = userData.facebookId;
        userDataProfile.googleId = userData.googleId;
        userDataProfile.lastLogin = userData.lastLogin;
        userDataProfile.registerType = userData.registerType;
        userDataProfile.oTP = userData.oTP;
        userDataProfile.loginOrSignup = userData.loginOrSignup;
        userDataProfile.userName = userData.userName;
        userDataProfile.loginUserId = userData.loginUserId;
        userDataProfile.imageName = userData.imageName;
        userDataProfile.userImage = userData.userImage;
        userDataProfile.imageData = userData.imageData;
        userDataProfile.accountTypeName = userData.accountTypeName;
        userDataProfile.isActive = userData.isActive;
        userDataProfile.createdDate = userData.createdDate;
        userDataProfile.modifiedDate = userData.modifiedDate;
        userDataProfile.createdBy = userData.createdBy;
        userDataProfile.modifiedBy = userData.modifiedBy;
        userDataProfile.countryCode = Integer.valueOf(userInfoBinding.etPhoneCode.getText().toString());
        userDataProfile.CountryName = App.userDataContract.detail.CountryName;
        userDataProfile.Countryid = App.userDataContract.detail.CountryId;


        Call<DefaultResponse> updateCall = Constant.retrofitServiceHeader.updateUserInfo(userDataProfile);
        updateCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                isProfileUpdated = false;
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        getUserProfile(response.body().detail);
                        new MaterialAlertDialogBuilder(getContext())
                                .setMessage("Your information have been saved successfully.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        if (userType.equalsIgnoreCase("Tutor")){
                                            TutorHomeActivity.addFragment(new TutorHomeFragment(), "Home", getActivity());
                                        }else if (userType.equalsIgnoreCase("Student")){
                                            // manage regular and demo students view
                                            StudentHomeActivity.addFragment(new StudentDemoHomeFragment(), Constant.HOME, getActivity());
                                        }
                                    }
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
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong." + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUserProfile(final String userId) {
        Call<UserDataProfile> callUserProfile = Constant.retrofitServiceHeader.getUserProfile(userId);
        callUserProfile.enqueue(new Callback<UserDataProfile>() {
            @Override
            public void onResponse(Call<UserDataProfile> call, Response<UserDataProfile> response) {
                if (response.isSuccessful()) {
                    setUserData(response.body());
                    App.userDataContract = response.body();
                    SharedPrefsUtil.setUserPreferences(getActivity(), response.body());
                } else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Something went wrong!!\n" + mError.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataProfile> call, Throwable t) {
                getUserProfile(userId);
                //  userInfoBinding.progressBarUpdateInfo.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong!!\n" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", "OnFailure" + t.getLocalizedMessage());
            }
        });

    }


}
