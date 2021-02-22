package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentCoursePlansBinding;
import com.example.gayaak_10.interfaces.ButtonAction;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CoursePlansAdapter;
import com.example.gayaak_10.student.adapter.CourseWalletPlansAdapter;
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.model.request.WalletUpdateRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.GPSTracker;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.Objects;

public class CoursePlansFragment extends Fragment implements View.OnClickListener {

    private FragmentCoursePlansBinding binding;
    private CourseWalletPlansAdapter adapter;
    private CoursePlansAdapter coursePlansAdapter;
    private StudentViewModel viewModel;
    private WalletRechargePlanDataContractList selectedPlan = null;
    private int total = 0;
    private String country = "";
    private String isFrom = "";
    private int requiredPoints = 0;

    public CoursePlansFragment(String isFrom) {
        this.isFrom = isFrom;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoursePlansBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(StudentViewModel.class);

        binding.ivPaymentBack.setOnClickListener(this);
        binding.tvProceedPlan.setOnClickListener(this);

        try {
            GPSTracker gps = new GPSTracker(getActivity());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            country = Utility.getAddress(getActivity(), latitude, longitude);
            Log.e("countryCheck", "onCreate: " + country);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse")) {
            //check if available in wallet
            binding.rvCoursePlan.setVisibility(View.GONE);
            binding.layoutCoursePlan.getRoot().setVisibility(View.VISIBLE);
            binding.layoutCoursePlan.tvPlanName.setText(App.selectedSessionDetail.courseName);
            binding.layoutCoursePlan.tvWalletPoints.setText("" + (App.selectedSessionDetail.coursePrice * App.selectedSessionDetail.courseSessions));
            if (App.userDataContract.detail.userWalletDataContract!= null && App.userDataContract.detail.userWalletDataContract.Coins != 0) {
                requiredPoints = App.selectedSessionDetail.coursePrice * App.selectedSessionDetail.courseSessions;
                int walletPoints = App.userDataContract.detail.userWalletDataContract.Coins;
                if (requiredPoints <= walletPoints) {
                    total = 0;
                    binding.layoutPlanTotal.setVisibility(View.VISIBLE);
                    binding.tvPlanTotal.setText("$ " + total);
                    binding.tvProceedPlan.setText("Pay from wallet");
                } else {
                    getPlans();
                }
            } else {
                getPlans();
            }
        } else {
            getPlans();
        }
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void getPlans() {
        Utility.showLoader(getActivity(), true);
        viewModel.getCoursePlans().observe(Objects.requireNonNull(getActivity()), coursePlan -> {
            Utility.hideLoader();
            if (coursePlan != null && coursePlan.detail.walletRechargePlanDataContractList.size() != 0) {
                binding.rvWalletsPlan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new CourseWalletPlansAdapter(getActivity(), coursePlan.detail.walletRechargePlanDataContractList, position -> {
                    for (int i = 0; i < coursePlan.detail.walletRechargePlanDataContractList.size(); i++) {
                        if (coursePlan.detail.walletRechargePlanDataContractList.get(i).name.equalsIgnoreCase(coursePlan.detail.walletRechargePlanDataContractList.get(position).name)) {
                            coursePlan.detail.walletRechargePlanDataContractList.get(i).isSelected = true;
                            selectedPlan = coursePlan.detail.walletRechargePlanDataContractList.get(i);
                            binding.layoutInfoCoins.setVisibility(View.VISIBLE);

                            String planPrice = coursePlan.detail.walletRechargePlanDataContractList.get(i).usCurrencyValue + " USD";
                            if (country.equalsIgnoreCase("India")) {
                                planPrice = coursePlan.detail.walletRechargePlanDataContractList.get(i).indiaCurrencyValue + " INR";
                            } else if (country.equalsIgnoreCase("United States") || country.equalsIgnoreCase("USA")) {
                                planPrice = coursePlan.detail.walletRechargePlanDataContractList.get(i).usCurrencyValue + " USD";
                            }
                            binding.tvCoinInfo.setText("1 coin = " + planPrice);
                            total = coursePlan.detail.walletRechargePlanDataContractList.get(i).noOfCoin
                                    * coursePlan.detail.walletRechargePlanDataContractList.get(i).usCurrencyValue;
                            binding.layoutPlanTotal.setVisibility(View.VISIBLE);
                            binding.tvPlanTotal.setText("$ " + total);
                        } else {
                            coursePlan.detail.walletRechargePlanDataContractList.get(i).isSelected = false;
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
                binding.rvWalletsPlan.setAdapter(adapter);

            }
            if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse")) {
                binding.rvCoursePlan.setVisibility(View.GONE);
                binding.layoutCoursePlan.getRoot().setVisibility(View.VISIBLE);
                binding.layoutCoursePlan.tvPlanName.setText(App.selectedSessionDetail.courseName);
                binding.layoutCoursePlan.tvWalletPoints.setText("" + (App.selectedSessionDetail.coursePrice * App.selectedSessionDetail.courseSessions));
            } else {
                if (coursePlan != null && coursePlan.detail.courseDataContractList.size() != 0) {
                    binding.rvCoursePlan.setVisibility(View.VISIBLE);
                    binding.layoutCoursePlan.getRoot().setVisibility(View.GONE);
                    binding.rvCoursePlan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    coursePlansAdapter = new CoursePlansAdapter(getActivity(), coursePlan.detail.courseDataContractList, position -> coursePlansAdapter.notifyDataSetChanged());
                    binding.rvCoursePlan.setAdapter(coursePlansAdapter);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPaymentBack:
                StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
                break;

            case R.id.tvProceedPlan:
                if (binding.tvProceedPlan.getText().toString().equalsIgnoreCase("Pay from wallet")) {
                    WalletUpdateRequest plans = new WalletUpdateRequest();
                    plans.coins = requiredPoints;
                    plans.name = binding.layoutCoursePlan.tvPlanName.getText().toString().trim();
                    plans.userId = App.userDataContract.detail.userId;
                    plans.courseId = App.selectedSessionDetail.courseId;
                    plans.CreditDebit = 1;
                    viewModel.updateWalletDirect(plans).observe(getViewLifecycleOwner(), new Observer<DefaultResponse>() {
                        @Override
                        public void onChanged(DefaultResponse defaultResponse) {
                            if (defaultResponse.status) {
                                Utility.customDialog(getActivity(), "Plan Updated", defaultResponse.message, "Ok", getLayoutInflater(), new ButtonAction() {
                                    @Override
                                    public void buttonClicked() {
                                        Utility.showLoader(getActivity(), false);
                                        getUserProfile(App.userDataContract.detail.userId);
                                    }
                                });

                            }
                        }
                    });
                } else {
                    if (selectedPlan != null) {
                        if (binding.checkTermsCondition.isChecked()) {
                            //open checkout view
                            StudentHomeActivity.addFragment(new CheckoutFragment(selectedPlan, "planBuy"), Constant.PROFILE, getActivity());
                        } else {
                            Toast.makeText(getActivity(), "Please agree to terms and condition", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please at least choose one plan.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void getUserProfile(int userId) {
        viewModel.getUserProfile(userId).observe(getActivity(), userDataProfile -> {
            if (userDataProfile != null) {
                Utility.hideLoader();
                App.userDataContract = userDataProfile;
                SharedPrefsUtil.setUserPreferences(getActivity(), userDataProfile);
            }
            Utility.showLoader(getActivity(), false);
            createSession();
            // StudentHomeActivity.addFragment(new StudentProfileFragment(""), Constant.PROFILE, getActivity());
        });
    }

    private void createSession() {
        DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
        demoTutorRequest.ScheduleId = App.selectedSessionDetail.ScheduleId1;
        demoTutorRequest.StudentTutorBookingId = 0;
        demoTutorRequest.TutorId = App.selectedSessionDetail.TutorId;
        demoTutorRequest.TutorScheduleId = String.valueOf(App.selectedSessionDetail.TutorScheduleId);
        demoTutorRequest.CourseId = App.selectedSessionDetail.courseId;
        String day=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            day = DateTimeUtility.getDateFromWeekDay(App.selectedSessionDetail.courseSelectedDay);
        }
        demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(day, "dd MMMM yyyy", "yyyy-MM-dd") + " " + App.selectedSessionDetail.courseSelectedTime;
        demoTutorRequest.levelid = String.valueOf(App.selectedSessionDetail.levelId);
        demoTutorRequest.liveclasstype = 2;
        demoTutorRequest.classTypeId = 2;
        demoTutorRequest.categoryid = App.selectedSessionDetail.categoryId;

        viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
                Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                Utility.hideLoader();
                Fragment fragmentName = null;
                String userType = SharedPrefsUtil.getStringPreferences(Objects.requireNonNull(getActivity()), "UserType");
                if (userType.equalsIgnoreCase("Free")) {
                    fragmentName = new StudentDemoHomeFragment();
                } else if (userType.equalsIgnoreCase("Paid")) {
                    fragmentName = new StudentRegularFragment();
                }
                StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
            }
        });
    }
}
