package com.example.gayaak_10.common.login_register;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.CustomSpinnerAdapter;
import com.example.gayaak_10.adapter.LearnCourseAdapter;
import com.example.gayaak_10.adapter.LearnInterestAdapter;
import com.example.gayaak_10.common.login_register.viewmodel.RegisterViewModel;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCreateUserInfoBinding;
import com.example.gayaak_10.model.response.Countries;
import com.example.gayaak_10.model.response.CountryDetail;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataContract;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.CategoryDataContractList;
import com.example.gayaak_10.student.model.CoinCurrencyConfig;
import com.example.gayaak_10.student.model.LearningLevelDataContractList;
import com.example.gayaak_10.utility.GPSTracker;
import com.example.gayaak_10.utility.Utility;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoNewEmailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<CountryDetail> countryList = new ArrayList<>();
    private LearnCourseAdapter adapter;
    private LearnInterestAdapter courseLevelTypeAdapter;
    private String selectedLevel = "";
    private String selectedCourseType = "";
    private int gender = -1;
    private String selectedCountry = "";
    private String country = "India";
    private RegisterViewModel viewModel;
    private ArrayList<CategoryDataContractList> courseList = new ArrayList<>();
    private FragmentCreateUserInfoBinding binding;
    private String emailId = "";
    private String pass = "";
   // private ArrayList<String> countryList = new ArrayList<>();

    public UserInfoNewEmailFragment(String email, String password) {
        emailId = email;
        pass = password;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateUserInfoBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        binding.etUserEmail.setText(emailId);

        binding.progressBarUpdateInfo.setVisibility(View.VISIBLE);
        viewModel.getAllCourseLevels().observe(getActivity(), levelCategoryInfo -> {
            binding.progressBarUpdateInfo.setVisibility(View.GONE);
            if (levelCategoryInfo.categoryDataContractList != null && levelCategoryInfo.learningLevelDataContractList != null) {
                if (!levelCategoryInfo.categoryDataContractList.isEmpty() && !levelCategoryInfo.learningLevelDataContractList.isEmpty()) {
                    courseList = levelCategoryInfo.categoryDataContractList;
                    setLearningInterest(courseList);
                    setLearningLevel(levelCategoryInfo.learningLevelDataContractList);
                }
            }

        });
        try {
            if (!Utility.hasPermissions(getActivity(), Utility.PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), Utility.PERMISSIONS, Utility.PERMISSION_ALL);
            }
            GPSTracker gps = new GPSTracker(getActivity());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            country = Utility.getAddress(getActivity(), latitude, longitude);
            Log.e("Location", "latitude: " + country);
            if (country.equalsIgnoreCase("India")) {
                binding.etPhoneCode.setText("+91");
            } else {
                binding.etPhoneCode.setText("+1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.spinnerCountry.setOnItemSelectedListener(this);
        getCountries(country);


        binding.radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.female) {
                gender = 0;
            } else if (checkedId == R.id.male) {
                gender = 1;
            } else {
                gender = 2;
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etUserNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(),
                            "Enter your number",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (binding.etUserEmail.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your email address", Toast.LENGTH_LONG).show();
                    return;
                }

                if (binding.etUserName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (gender == -1) {
                    Toast.makeText(getActivity(), "Select your gender.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (selectedLevel.isEmpty()) {
                    Toast.makeText(getActivity(), "Select at least one level", Toast.LENGTH_LONG).show();
                    return;
                }

                if (selectedCourseType.isEmpty()) {
                    Toast.makeText(getActivity(), "Select at least one course type", Toast.LENGTH_LONG).show();
                    return;
                }

/*                if (!selectedCountry.equalsIgnoreCase(country)) {
                    Toast.makeText(getActivity(), "Please select the country you belong to.", Toast.LENGTH_LONG).show();
                    return;
                }*/

                updateUserInfo(binding.etUserName.getText().toString(), emailId,
                        binding.etUserNumber.getText().toString(), gender, selectedCountry, pass);

            }
        });


        return binding.getRoot();
    }

    private void updateUserInfo(String name, String email, String mobileNumber, int gender, String country, String password) {
        String firstName = "";
        String lastName = "";

        if (name.contains(" ")) {
            String[] parts = name.split(" ");
            firstName = parts[0];
            lastName = parts[1];
            Log.d("name", "First -> " + parts[0] + " last -> " + parts[1]);

        } else {
            firstName = name;
            Log.d("name", "First -> " + firstName);
        }

        UserDataContract userDataProfile = new UserDataContract();
        userDataProfile.userId = 0;
        userDataProfile.roleId = 3;
        userDataProfile.firstName = firstName;
        userDataProfile.lastName = lastName;
        userDataProfile.email = email;
        userDataProfile.phone = mobileNumber;
        userDataProfile.gender = gender;
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
        userDataProfile.CountryName = country;
        App.countryName=country;
        userDataProfile.countryCode = Integer.valueOf(binding.etPhoneCode.getText().toString().replace("+", ""));
        if (selectedCountry.equalsIgnoreCase("India")) {
            userDataProfile.Countryid = 1;
        } else {
            userDataProfile.Countryid = 2;
        }

        //send otp
        sendOtpToEmail(email, userDataProfile);

    }

    private void sendOtpToEmail(final String email, UserDataContract userDataProfile) {
        binding.progressBarUpdateInfo.setVisibility(View.VISIBLE);
        Call<DefaultResponse> callOtp = Constant.retrofitService.sendOtp(email);
        callOtp.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                        binding.progressBarUpdateInfo.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), OtpVerificationActivity.class);
                        intent.putExtra("userDataProfile", userDataProfile);
                        getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.progressBarUpdateInfo.setVisibility(View.GONE);
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
                binding.progressBarUpdateInfo.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLearningInterest(ArrayList<CategoryDataContractList> courseList) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rvLearningInterest.setLayoutManager(layoutManager);
        adapter = new LearnCourseAdapter(getActivity(), courseList, position -> {
            selectedCourseType = "";
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).isSelected) {
                    if (selectedCourseType.isEmpty()) {
                        App.firstSelectedCourseType = courseList.get(i).categoryId;
                        selectedCourseType = courseList.get(i).name;
                    } else {
                        selectedCourseType = selectedCourseType + "," + courseList.get(i).name;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        });
        binding.rvLearningInterest.setAdapter(adapter);
    }

    private void setLearningLevel(ArrayList<LearningLevelDataContractList> levelsList) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rvLearningLevel.setLayoutManager(layoutManager);
        courseLevelTypeAdapter = new LearnInterestAdapter(getActivity(), levelsList, position -> {
            selectedLevel = "";
            for (int i = 0; i < levelsList.size(); i++) {
                if (levelsList.get(i).learningLevelId.equals(levelsList.get(position).learningLevelId)) {
                    levelsList.get(i).isSelected = true;
                } else {
                    levelsList.get(i).isSelected = false;
                }
                if (levelsList.get(i).isSelected) {
                    if (selectedLevel.isEmpty()) {
                        selectedLevel = levelsList.get(i).name;
                        App.firstSelectedLevelType = levelsList.get(i).learningLevelId;
                    } else {
                        selectedLevel = selectedLevel + "," + levelsList.get(i).name;
                    }
                }
            }
            courseLevelTypeAdapter.notifyDataSetChanged();
        });
        binding.rvLearningLevel.setAdapter(courseLevelTypeAdapter);
    }

    private void getCountries(String country) {

/*   //     Utility.showLoader(getContext(),false);
        Call<Countries> countryCall =Constant.retrofitServiceHeader.getCountries();
  //      Log.d("call", " filter => " + filterCall.request());
   //     Utility.hideLoader();
        countryCall.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(@NonNull Call<Countries> call, @NonNull Response<Countries> response) {
                assert response.body() != null;
                if (response.body().detail != null) {
                    for (int i = 0; i < response.body().detail.size(); i++) {
                       countryList.addAll(response.body().detail);
                       setCountryData(countryList, country);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Countries> call, @NonNull Throwable t) {

            }
        });*/




        Constant.retrofitService.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Countries>() {
                    @Override
                    public void onNext(Countries countries) {
                        if (countries.status && countries.detail != null) {
                            countryList.addAll(countries.detail);
                            setCountryData(countryList, country);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setCountryData(ArrayList<CountryDetail> countryList, String country) {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), countryList);
        binding.spinnerCountry.setPrompt(getString(R.string.select_country));
        binding.spinnerCountry.setAdapter(customSpinnerAdapter);
        for (int i = 0; i < countryList.size(); i++) {
            if (!country.isEmpty()) {
                if (countryList.get(i).Name.equalsIgnoreCase(country)) {
                    selectedCountry = countryList.get(i).Name;
                    binding.spinnerCountry.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedCountry = countryList.get(position).Name;
        if (countryList.get(position).Name.equalsIgnoreCase("India")) {
            App.currencyType = "INR";
        } else if (countryList.get(position).Name.equalsIgnoreCase("USA")
                || countryList.get(position).Name.equalsIgnoreCase("United States")) {
            App.currencyType = "USD";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        selectedCountry = country;
    }

    private void fetchErrorResponse(ResponseBody response) {
        binding.progressBarUpdateInfo.setVisibility(View.GONE);
        Gson gson = new GsonBuilder().create();
        ErrorPojoClass mError = new ErrorPojoClass();
        try {
            mError = gson.fromJson(response.string(), ErrorPojoClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utility.customDialogBoxTextWithSingle(getActivity(), "Something went wrong.", mError.message);
    }
}
