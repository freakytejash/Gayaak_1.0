package com.example.gayaak_10.common.login_register;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.UserInfoSocialFragment;
import com.example.gayaak_10.databinding.ActivityUserInfoBinding;
import com.example.gayaak_10.model.response.UserDataContract;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private String password;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDataContract userDataProfile = (UserDataContract) getIntent().getSerializableExtra("userDataProfile");
        boolean hasFragmentType = getIntent().hasExtra("fragmentType");
        boolean isPassword = getIntent().hasExtra("password");
        if (isPassword){
            password = getIntent().getStringExtra("password");
        }
        String email = getIntent().getStringExtra("email");

        if (hasFragmentType){
            String fragmentName = getIntent().getStringExtra("fragmentType");
            if (fragmentName.equalsIgnoreCase("Social")){
                getSupportFragmentManager().beginTransaction().replace(R.id.preferenceContainer, new UserInfoSocialFragment(email, userDataProfile)).commit();
            }else if (fragmentName.equalsIgnoreCase("Email")){
                getSupportFragmentManager().beginTransaction().replace(R.id.preferenceContainer, new UserInfoNewEmailFragment(email, password)).commit();
            }
        }
    }
}
