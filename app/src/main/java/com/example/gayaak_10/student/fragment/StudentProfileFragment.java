package com.example.gayaak_10.student.fragment;

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

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.common.ChangePasswordFragment;
import com.example.gayaak_10.common.UserInfoFragment;
import com.example.gayaak_10.common.login_register.SignInActivity;
import com.example.gayaak_10.common.support.SupportFragment;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentProfileBinding;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static com.example.gayaak_10.utility.Utility.getBitmapFromBase64;

public class StudentProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentProfileBinding binding;
    private String openView = "";

    public StudentProfileFragment(String openView) {
        if (!openView.isEmpty()){
            this.openView = openView;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentProfileBinding.inflate(getLayoutInflater());

        binding.userInfo.setOnClickListener(this);
        binding.myEbooks.setOnClickListener(this);
        binding.ivProfileBack.setOnClickListener(this);
        binding.layoutLogout.setOnClickListener(this);
        binding.layoutChangePassword.setOnClickListener(this);
        binding.layoutBuyCourse.setOnClickListener(this);
        binding.layoutMyCourses.setOnClickListener(this);
        binding.layoutSupport.setOnClickListener(this);
        binding.layoutCoursePlans.setOnClickListener(this);
        binding.layoutPracticeSession.setOnClickListener(this);

        if (App.isSocial) {
            binding.layoutChangePassword.setVisibility(View.GONE);
        }else {
            binding.layoutChangePassword.setVisibility(View.VISIBLE);
        }

        if (!openView.isEmpty() && openView.equalsIgnoreCase("MyCart")) {
            StudentHomeActivity.addFragment(new CourseCartFragment(), Constant.PROFILE, getActivity());
        }

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layoutBuyCourse:
                StudentHomeActivity.addFragment(new CourseCartFragment(), Constant.PROFILE, getActivity());
                break;
            case R.id.user_info:
                StudentHomeActivity.addFragment(new UserInfoFragment("Student"), Constant.PROFILE, getActivity());
                break;
            case R.id.layoutMyCourses:
                StudentHomeActivity.addFragment(new MyCoursesFragment(), Constant.PROFILE, getActivity());
                break;
            case R.id.layoutSupport:
                StudentHomeActivity.addFragment(new SupportFragment(),Constant.PROFILE,getActivity());
                break;
            case R.id.layoutPracticeSession:
                StudentHomeActivity.addFragment(new PracticeRoomFragment(), Constant.PROFILE,getActivity());
                break;

        /*    case R.id.my_ebooks:
                break;
            case R.id.bookALiveClass:
                break;
            case R.id.layoutSupport:
                break;
*/
            case R.id.layoutLogout:
                App.isSocial = false;
                showLogoutDialog();
                break;

            case R.id.layoutChangePassword:
                StudentHomeActivity.addFragment(new ChangePasswordFragment("Student"), Constant.PROFILE, getActivity());
                break;


            case  R.id.layoutCoursePlans:
                StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, getActivity());
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
            }else {
                binding.profileName.setText(userData.detail.email);
            }

            Bitmap decodedProfileImage = getBitmapFromBase64(userData.detail.profileImage);
            Glide.with(this).load(decodedProfileImage)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(binding.ivProfileImage);
        }

        if (!App.cartList.isEmpty()) {
            if (App.cartList.size() > 0) {
                binding.tvCartCount.setVisibility(View.VISIBLE);
                binding.tvCartCount.setText("" + App.cartList.size());
            }
        } else {
            binding.tvCartCount.setVisibility(View.INVISIBLE);
        }

        if (App.userDataContract.detail.userWalletDataContract != null){
            binding.tvWalletPoints.setText(""+App.userDataContract.detail.userWalletDataContract.Coins);
        }
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
                    App.allCourseRecommendedList.clear();
                    App.userLearningCourseList.clear();
                    App.dashboardRegularCourseList.clear();
                    App.studentCartList.clear();
                    App.countryCurrencyName="";
                    App.countryName="";
                    App.noOfSessions=4;
                    App.recommendedCourseCreated=0;
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
