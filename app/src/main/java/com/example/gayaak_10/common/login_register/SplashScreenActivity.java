package com.example.gayaak_10.common.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.viewmodel.CommonViewModel;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivitySplashScreenBinding;
import com.example.gayaak_10.model.response.ConfigurationData;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.TimeZone;

public class SplashScreenActivity extends AppCompatActivity {

    private CommonViewModel viewModel = null;
    private String country = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ActivitySplashScreenBinding splashScreenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = splashScreenBinding.getRoot();
        setContentView(view);
        viewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        viewModel.getConfiguration().observe(this, new Observer<ConfigurationData>() {
            @Override
            public void onChanged(ConfigurationData configurationData) {
                if (configurationData.detail != null && configurationData.detail.size() != 0){
                    for (int i = 0; i < configurationData.detail.size(); i++) {
                        if (configurationData.detail.get(i).name.contains("Countdown Timer Start")){
                            Constant.StartTimer = configurationData.detail.get(i).value;
                        }
                        if (configurationData.detail.get(i).name.contains("Prior Link Display")){
                            Constant.ShowStartClass = configurationData.detail.get(i).value;
                        }

                        if (configurationData.detail.get(i).configurationId == 4){
                            Constant.EndStartClass = configurationData.detail.get(i).value;
                        }
                    }
                }
            }
        });

        TimeZone tz = TimeZone.getDefault();
        Constant.TIMEZONE = tz.getDisplayName(false, TimeZone.SHORT);

        if (!Utility.hasPermissions(SplashScreenActivity.this, Utility.PERMISSIONS)) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this, Utility.PERMISSIONS, Utility.PERMISSION_ALL);
        }

        App.userDataContract = SharedPrefsUtil.getUserPreferences(SplashScreenActivity.this, Constant.USER_DATA);
        country = SharedPrefsUtil.getStringPreferences(SplashScreenActivity.this, Constant.COUNTRY);
        ArrayList<BuyCoursesDetail> cartList = SharedPrefsUtil.getCartCourse(SplashScreenActivity.this, Constant.CART_DATA);
        if (cartList != null && !cartList.isEmpty()){
            App.cartList.clear();
            App.cartList.addAll(cartList);
        }

        if ( App.userDataContract != null ) {
            Constant.token = SharedPrefsUtil.getStringPreferences(SplashScreenActivity.this, "userToken");
            Constant.userId = SharedPrefsUtil.getStringPreferences(SplashScreenActivity.this, "userId");
            Log.d("token" , " ==> " +Constant.token+ " userId -> " +Constant.userId);

            getUserProfile(App.userDataContract.detail.userId);
        }

        splashScreenBinding.btnGetStarted.setOnClickListener(v -> {
            if (!Utility.hasPermissions(SplashScreenActivity.this, Utility.PERMISSIONS)) {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, Utility.PERMISSIONS, Utility.PERMISSION_ALL);
            }

            Intent intent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            finish();
        });
    }

    private void getUserProfile(int userId) {
        viewModel.getUserProfile(userId).observe(SplashScreenActivity.this, userDataProfile -> {
            if (userDataProfile != null){
                App.userDataContract = userDataProfile;
                SharedPrefsUtil.setUserPreferences(SplashScreenActivity.this, userDataProfile);
            }else {
                if (country!= null){
                    switch (country){
                        case "India":
                            App.currencyType = "INR";
                            break;
                        case "USA":
                            App.currencyType = "USD";
                            break;
                    }
                }
            }
            openView();
        });
    }

    private void openView() {
        App.userDataContract = SharedPrefsUtil.getUserPreferences(SplashScreenActivity.this, Constant.USER_DATA);
        if (App.userDataContract.detail.googleId == 1 || App.userDataContract.detail.facebookId == 1){
            App.isSocial = true;
        }
        if (App.userDataContract.detail.roleId == 3){
            Intent intent = new Intent(SplashScreenActivity.this, StudentHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else  if (App.userDataContract.detail.roleId == 2){
            Intent intent = new Intent(SplashScreenActivity.this, TutorHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
