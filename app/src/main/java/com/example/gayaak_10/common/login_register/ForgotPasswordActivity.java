package com.example.gayaak_10.common.login_register;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ActivityForgotPasswordBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.utility.Utility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnForgotPassword.setOnClickListener(this);
        binding.ivBackForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnForgotPassword:
                Utility.hideSoftKeyboard(this, view);
                checkAllRequiredFields();
                break;

            case R.id.ivBackForgotPassword:
                finish();
                overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
                break;
        }
    }

    private void checkAllRequiredFields() {
        if (binding.etForgotEmail.getText().toString().isEmpty()) {
            binding.etForgotEmail.setError("Email cannot be empty.");
            return;
        } else if (Utility.isEmailValid(binding.etForgotEmail.getText().toString())) {
            binding.etForgotEmail.setError("Enter your correct email Id.");
            return;
        } else {
            binding.progressForgotPassword.setVisibility(View.VISIBLE);
            forgotPasswordByEmail(binding.etForgotEmail.getText().toString());
        }
    }

    private void forgotPasswordByEmail(String emailId) {
        Call<DefaultResponse> forgotEmailCall = Constant.retrofitService.forgotPasswordByEmail(emailId);
        forgotEmailCall.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                binding.progressForgotPassword.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        new MaterialAlertDialogBuilder(ForgotPasswordActivity.this)
                                .setIcon(R.drawable.gayaak_icon)
                                .setMessage("Please check your email for the link.")
                                .setPositiveButton("Ok", (dialogInterface, i) -> {
                                    finish();
                                })
                                .show();

                    } else {
                        Utility.customDialogBoxTextWithSingle(ForgotPasswordActivity.this, "Something went wrong.", response.body().message);
                    }
                } else {
                    Gson gson = new GsonBuilder().create();
                    ErrorPojoClass mError = new ErrorPojoClass();
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorPojoClass.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utility.customDialogBoxTextWithSingle(ForgotPasswordActivity.this, "Something went wrong.", mError.message);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                binding.progressForgotPassword.setVisibility(View.GONE);
                Toast.makeText(ForgotPasswordActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        super.onBackPressed();
    }
}