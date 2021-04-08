package com.example.gayaak_10.tutor.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.common.login_register.SignInActivity;
import com.example.gayaak_10.common.support.SupportFragment;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorProfileBinding;
import com.example.gayaak_10.common.ChangePasswordFragment;
import com.example.gayaak_10.common.UserInfoFragment;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.example.gayaak_10.utility.Utility.getBitmapFromBase64;

public class TutorProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentTutorProfileBinding binding;

    public TutorProfileFragment(String s) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorProfileBinding.inflate(getLayoutInflater());

        binding.userInfo.setOnClickListener(this);
        binding.ivProfileBack.setOnClickListener(this);
        binding.layoutLogout.setOnClickListener(this);
        binding.layoutChangePassword.setOnClickListener(this);
        binding.layoutMyScheduler.setOnClickListener(this);

        if (App.isSocial) {
            binding.layoutChangePassword.setVisibility(View.GONE);
        } else {
            binding.layoutChangePassword.setVisibility(View.VISIBLE);
        }

        if (App.tutorCoins != null){
            binding.tvWalletPoints.setText(""+App.tutorCoins);
        }

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.user_info:
                TutorHomeActivity.addFragment(new UserInfoFragment("Tutor"), "UserProfile", getActivity());
                break;

            case R.id.layoutLogout:
                App.isSocial = false;
                showLogoutDialog();
                break;

            case R.id.layoutChangePassword:
                TutorHomeActivity.addFragment(new ChangePasswordFragment("Tutor"), "UserProfile", getActivity());
                break;

            case R.id.layoutMyScheduler:
                TutorHomeActivity.addFragment(new TutorSchedulerFragment(), "UserProfile", getActivity());
                break;
            case R.id.layoutSupport:
                TutorHomeActivity.addFragment(new SupportFragment(), "UserProfile",getActivity());
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setProfileData(UserDataProfile userData) {
        if (userData.detail != null) {
            if (!userData.detail.firstName.isEmpty()) {
                binding.profileName.setText(userData.detail.firstName);
            } else if (!userData.detail.lastName.isEmpty()) {
                binding.profileName.setText(userData.detail.lastName);
            } else {
                binding.profileName.setText(userData.detail.email);
            }

            Bitmap decodedProfileImage = getBitmapFromBase64(userData.detail.profileImage);
            Glide.with(this).load(decodedProfileImage)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(binding.ivProfileImage);
        }
    }

    private void openNewFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail().build()).signOut();
                    App.userDataContract = new UserDataProfile();
                    App.coursesDetailArrayList.clear();
                    App.trendingDetailArrayList.clear();
                    App.allCoursesArrayList.clear();
                    App.currencyType = "USD";
                    App.cartList.clear();
                    App.isSocial = false;
                    SharedPrefsUtil.clearAllSharedPreferences(getContext());
                    startActivity(new Intent(getActivity(), SignInActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onResume() {
        setProfileData(App.userDataContract);
        super.onResume();
    }
}
