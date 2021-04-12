package com.example.gayaak_10.student.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.gayaak_10.model.response.ErrorPojoClass;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CoursePlansAdapter;
import com.example.gayaak_10.student.adapter.CourseWalletPlansAdapter;
import com.example.gayaak_10.student.model.CoinCurrencyConfig;
import com.example.gayaak_10.student.model.CourseDataContractList;
import com.example.gayaak_10.student.model.CoursePlan;
import com.example.gayaak_10.student.model.WalletRechargePlanDataContractList;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.model.request.WalletUpdateRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.GPSTracker;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursePlansFragment extends Fragment implements View.OnClickListener {

    private FragmentCoursePlansBinding binding;
    private CourseWalletPlansAdapter adapter;
    private CoursePlansAdapter coursePlansAdapter;
    private StudentViewModel viewModel;
    private WalletRechargePlanDataContractList selectedPlan = null;
    private CourseDataContractList customPlan =null;
    private int total = 0;

    private String country = "";
    private String countryCurrencyName = "INR";
    private Integer countryCurrencyValue =0;
    private String isFrom = "";
    private int requiredPoints = 0;
    private int noOfSessions= 4;
    private int coursePlanNoOfSessions=0;
    private int coursePlanPrice=0;
    private int customCoursePrice=0;
    private int perSessionPrice=0;
    private int walletPoints;
    private boolean isRescheduled = false;


    public CoursePlansFragment(String isFrom) {
        this.isFrom = isFrom;
    }

    public CoursePlansFragment(String isFrom, boolean isRescheduled) {
        this.isFrom = isFrom;
        this.isRescheduled = isRescheduled;
    }

    public CoursePlansFragment(){

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoursePlansBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(StudentViewModel.class);
        viewModel.getCoinCurrencyConfig();
        countryCurrencyName = ""+App.userDataContract.detail.currencyName;
        App.countryCurrencyName=countryCurrencyName;
        countryCurrencyValue= App.countryCurrencyValue;

        binding.ivPaymentBack.setOnClickListener(this);
        binding.tvProceedPlan.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);
        binding.btnSubtract.setOnClickListener(this);
        binding.radioCustomPlan.setOnClickListener(this);
        binding.radioRecommendedPlan.setOnClickListener(this);

        radioCheck();

        if (App.userDataContract.detail.userWalletDataContract!= null){
            if (App.userDataContract.detail.userWalletDataContract.Coins.equals(null)){
                binding.tvWalletPointsHeader.setText(Utility.withSuffix(0));
            }else {
                binding.tvWalletPointsHeader.setText(Utility.withSuffix(App.userDataContract.detail.userWalletDataContract.Coins));
            }
        }

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
            binding.spCoursePlan.setVisibility(View.GONE);
            binding.tvCoursePlan.setVisibility(View.VISIBLE);
            binding.tvCoursePlan.setText(App.selectedSessionDetail.courseName);
            //done

            binding.tvCustomSessions.setText(String.valueOf(App.noOfSessions));
            customCoursePrice = App.selectedSessionDetail.coursePrice*App.noOfSessions;
            perSessionPrice=App.selectedSessionDetail.coursePrice;

            binding.tvCustomRequiredPoints.setText(""+customCoursePrice);

            binding.radioCustomPlan.setChecked(true);
            binding.radioRecommendedPlan.setChecked(false);

            //  binding.layoutCoursePlan.tvCustomRequiredPoints.setText("" + (App.selectedSessionDetail.coursePrice * App.selectedSessionDetail.courseSessions));

            checkPlans();


        } else {
            binding.radioCustomPlan.setChecked(true);
            binding.radioRecommendedPlan.setChecked(false);
            getPlans();
        }
        return binding.getRoot();
    }

    private void radioCheck() {
        if (!binding.radioCustomPlan.isChecked() && !binding.radioRecommendedPlan.isChecked()){
            binding.radioRecommendedPlan.setChecked(true);
            binding.radioCustomPlan.setChecked(false);
        }
        else if (binding.radioCustomPlan.isChecked()){
            binding.radioRecommendedPlan.setChecked(false);
        }else if (binding.radioRecommendedPlan.isChecked()){
            binding.radioCustomPlan.setChecked(false);
        }
    }

    private void checkPlans() {

        if (App.userDataContract.detail.userWalletDataContract!= null && App.userDataContract.detail.userWalletDataContract.Coins != 0) {
            customCoursePrice = App.selectedSessionDetail.coursePrice * App.noOfSessions;
            App.requiredPoints=customCoursePrice;
            if (App.userDataContract.detail.userWalletDataContract.Coins==null){
                walletPoints=0;
            }else {
                walletPoints = App.userDataContract.detail.userWalletDataContract.Coins;
            }

           /* if (App.requiredPoints <= walletPoints) {
                total = 0;
                binding.layoutPlanTotal.setVisibility(View.VISIBLE);

              //  bindi/ng.layoutCoinInfo.setVisibility(View.VISIBLE);
              //  binding.tvCoinDeductionInfo.setText(" Coins will deduct from your wallet is "+App.requiredPoints);
                binding.tvPlanTotal.setText( ""+countryCurrencyName + total);
                binding.tvProceedPlan.setText("Pay from wallet");
                getPlans();
            } else*/ {
              /*  binding.tvProceedPlan.setText("Proceed");*/
                getPlans();
            }
        } else {
            getPlans();
        }
    }


    @SuppressLint("SetTextI18n")
    private void getPlans() {
        Utility.showLoader(getActivity(), true);
        viewModel.getCoursePlans().observe(Objects.requireNonNull(getActivity()), coursePlan -> {
            Utility.hideLoader();

            if (coursePlan != null && coursePlan.detail.walletRechargePlanDataContractList.size() != 0)
            {
                binding.rvWalletsPlan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                adapter = new CourseWalletPlansAdapter(getActivity(), coursePlan.detail.walletRechargePlanDataContractList, perSessionPrice, position -> {
                    for (int i = 0; i < coursePlan.detail.walletRechargePlanDataContractList.size(); i++) {
                        if ((coursePlan.detail.walletRechargePlanDataContractList.get(i).name
                                .equalsIgnoreCase(coursePlan.detail.walletRechargePlanDataContractList.get(position).name)
                                && binding.radioRecommendedPlan.isChecked()))
                        {
                            coursePlan.detail.walletRechargePlanDataContractList.get(i).isSelected = true;
                            selectedPlan = coursePlan.detail.walletRechargePlanDataContractList.get(i);
                            binding.layoutInfoCoins.setVisibility(View.VISIBLE);


                            String planPrice =""; /*coursePlan.detail.walletRechargePlanDataContractList.get(i).usCurrencyValue + " USD"*/

                                planPrice = ""+ App.countryCurrencyValue + " "+countryCurrencyName;

                                total = coursePlan.detail.walletRechargePlanDataContractList.get(i).noOfCoin
                                        * App.countryCurrencyValue;
                                binding.layoutPlanTotal.setVisibility(View.VISIBLE);
                                binding.tvPlanTotal.setText(""+countryCurrencyName + total);


                            binding.tvCoinInfo.setText("1 coin = " + planPrice );
                        } else if (!binding.radioRecommendedPlan.isChecked())
                        {
                            Toast.makeText(getActivity(),"please select the recommended course check button",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            coursePlan.detail.walletRechargePlanDataContractList.get(i).isSelected = false;
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
                binding.rvWalletsPlan.setAdapter(adapter);
            }

            if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse")) {
                customCoursePrice = App.selectedSessionDetail.coursePrice*App.noOfSessions;
                binding.spCoursePlan.setVisibility(View.GONE);
                binding.tvCoursePlan.setVisibility(View.VISIBLE);
                binding.customCard.setVisibility(View.VISIBLE);

                binding.layoutInfoCoins.setVisibility(View.VISIBLE);

                binding.tvCoinInfo.setText("1 coin = " + App.countryCurrencyValue +" "+App.countryCurrencyName);

                binding.layoutPlanTotal.setVisibility(View.VISIBLE);

                total = ((App.selectedSessionDetail.coursePrice*App.noOfSessions))* App.countryCurrencyValue;
                binding.tvPlanTotal.setText(""+countryCurrencyName + total);


            }
            else
                {
                  /*  binding.radioRecommendedPlan.setChecked(true);
                    binding.radioCustomPlan.setChecked(false);*/
                    if (coursePlan.detail.courseDataContractList==null){
                        binding.spCoursePlan.setVisibility(View.GONE);
                        binding.tvCoursePlan.setVisibility(View.GONE);
                        binding.customCard.setVisibility(View.GONE);
                        binding.contentLayout.setVisibility(View.GONE);
                        binding.emptyDataLayout.setVisibility(View.VISIBLE);
                    }

                if (coursePlan.detail.courseDataContractList != null && coursePlan.detail.courseDataContractList.size() != 0) {
                    binding.spCoursePlan.setVisibility(View.VISIBLE);
                    binding.tvCoursePlan.setVisibility(View.GONE);
                    binding.customCard.setVisibility(View.VISIBLE);

                    binding.layoutInfoCoins.setVisibility(View.VISIBLE);

                    binding.tvCoinInfo.setText("1 coin = " + App.countryCurrencyValue +" "+App.countryCurrencyName);

                    binding.layoutPlanTotal.setVisibility(View.VISIBLE);



                    //done

                    ArrayList<String> courseString= new ArrayList<>();
                    for (int i = 0; i<coursePlan.detail.courseDataContractList.size(); i++){
                        courseString.add(""+coursePlan.detail.courseDataContractList.get(i).name);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, courseString);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spCoursePlan.setAdapter(arrayAdapter);
                   // binding.spCoursePlan.setSelection(0);

                   //customCoursePrice = coursePlan.detail.courseDataContractList.get(0).perSessionPrice*(this.App.noOfSessions);

                    binding.spCoursePlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            App.spinnerSelectedCourse = coursePlan.detail.courseDataContractList.get(position);
                            binding.tvCustomSessions.setText(String.valueOf(App.noOfSessions));
                            customCoursePrice = coursePlan.detail.courseDataContractList.get(position).price*App.noOfSessions;
                            binding.tvCustomRequiredPoints.setText(""+(customCoursePrice));
                            coursePlanNoOfSessions = coursePlan.detail.courseDataContractList.get(position).noofSession;
                            coursePlanPrice = coursePlan.detail.courseDataContractList.get(position).price;

                            customPlan = coursePlan.detail.courseDataContractList.get(position);
                            perSessionPrice = coursePlan.detail.courseDataContractList.get(position).price;

                            total = (customCoursePrice)* App.countryCurrencyValue;
                            binding.tvPlanTotal.setText(""+countryCurrencyName + total);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



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

            case R.id.btn_subtract:
                reduceSession();
                break;

            case R.id.btn_add:
                addSession();
                break;

            case R.id.tvProceedPlan:
                proceedPlan();
                break;

            case R.id.radioCustomPlan:
                if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse"))
                {
                    binding.radioCustomPlan.setChecked(true);
                    binding.radioRecommendedPlan.setChecked(false);
                    checkPlans();
                }
                else {
                    binding.radioCustomPlan.setChecked(true);
                    binding.radioRecommendedPlan.setChecked(false);
                    getPlans();
                    /*binding.radioCustomPlan.setChecked(false);
                    Toast.makeText(getActivity(), "You Can only select custom plan for Recommended Courses ", Toast.LENGTH_LONG).show();
                */}

                break;

            case R.id.radioRecommendedPlan:
                binding.radioRecommendedPlan.setChecked(true);
                binding.radioCustomPlan.setChecked(false);
                binding.layoutInfoCoins.setVisibility(View.GONE);
              //  binding.layoutCoinInfo.setVisibility(View.GONE);
                binding.layoutPlanTotal.setVisibility(View.GONE);
                break;
        }
    }

    private void addSession() {
        if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse")){
            if (App.noOfSessions==App.selectedSessionDetail.courseSessions)
            {
                Toast.makeText(getActivity(),"Maximum sessions of the course is "+App.selectedSessionDetail.courseSessions,Toast.LENGTH_LONG).show();
            }else {

                App.noOfSessions = App.noOfSessions+1;
                checkPlans();
            }
            binding.tvCustomSessions.setText(""+App.noOfSessions);
            binding.tvCustomRequiredPoints.setText(""+(App.selectedSessionDetail.coursePrice* App.noOfSessions));
        }else {
            if (App.noOfSessions==coursePlanNoOfSessions){
                Toast.makeText(getActivity(),"Maximum sessions of the course is "+coursePlanNoOfSessions,Toast.LENGTH_LONG).show();
            }else {
                App.noOfSessions = App.noOfSessions+1;
                getPlans();
                //checkPlans();
            }
            binding.tvCustomSessions.setText(""+App.noOfSessions);
            binding.tvCustomRequiredPoints.setText(""+(coursePlanPrice* App.noOfSessions));
        }
    }

    private void reduceSession() {
        if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse")){
            if ( App.noOfSessions==4 || App.noOfSessions<4){
                Toast.makeText(getActivity(),"minimum required no. of session is 4",Toast.LENGTH_LONG).show();
            }else {
                App.noOfSessions = App.noOfSessions-1;
                checkPlans();
            }
            binding.tvCustomSessions.setText(""+App.noOfSessions);
            binding.tvCustomRequiredPoints.setText(""+(App.selectedSessionDetail.coursePrice* App.noOfSessions));
        }else {
            if ( App.noOfSessions==1){
                Toast.makeText(getActivity(),"minimun no. of session is 1",Toast.LENGTH_LONG).show();
            }else {
                App.noOfSessions = App.noOfSessions-1;
                getPlans();
                //checkPlans();
            }
            binding.tvCustomSessions.setText(""+App.noOfSessions);
            binding.tvCustomRequiredPoints.setText(""+(coursePlanPrice * App.noOfSessions));
        }
    }

    private void proceedPlan() {
        if (binding.tvProceedPlan.getText().toString().equalsIgnoreCase("Pay from wallet")) {
            WalletUpdateRequest plans = new WalletUpdateRequest();
      //      plans.coins = App.requiredPoints;
            plans.coins=0;
            plans.name = binding.tvCoursePlan.getText().toString().trim();
            plans.userId = App.userDataContract.detail.userId;
            plans.courseId = App.selectedSessionDetail.courseId;
            plans.CreditDebit = 2;
            //1 for credit & 2 for debit
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
        }
        else
        {
            if (!isFrom.isEmpty() && isFrom.equalsIgnoreCase("RecommendedCourse"))
            {
                if (binding.radioCustomPlan.isChecked()){
                    int totalPayment = total;
                    if (binding.checkTermsCondition.isChecked()) {
                        StudentHomeActivity.addFragment(new CheckoutFragment(totalPayment, "customRecommendedCourseBuy", country), Constant.PROFILE, getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Please agree to terms and condition", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (binding.radioRecommendedPlan.isChecked())
                {
                    int walletCoins = App.userDataContract.detail.userWalletDataContract.Coins;
                    // int totalRequiredCoins = selectedPlan.noOfCoin+selectedPlan.extraCoin+walletCoins;
                    if (selectedPlan != null && (selectedPlan.noOfCoin+selectedPlan.extraCoin+walletCoins) > customCoursePrice)
                        /*|| (selectedPlan.noOfCoin+selectedPlan.extraCoin+walletCoins) == customCoursePrice*/
                    {
                        if (binding.checkTermsCondition.isChecked()) {
                            //open checkout view
                            StudentHomeActivity.addFragment(new CheckoutFragment(selectedPlan, "planCustomCourseBuy", country), Constant.PROFILE, getActivity());
                        } else {
                            Toast.makeText(getActivity(), "Please agree to terms and condition", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else if (selectedPlan != null && (selectedPlan.noOfCoin+selectedPlan.extraCoin+walletCoins) < customCoursePrice){

                        App.requiredPoints=customCoursePrice-walletCoins;
                        Toast.makeText(getActivity(), "Please select the package of more than "+(customCoursePrice-walletCoins), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getActivity(), "Please at least choose one plan.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            else {
                if (binding.radioCustomPlan.isChecked()){
                    if (binding.checkTermsCondition.isChecked()) {
                        //open checkout view
                        StudentHomeActivity.addFragment(new CheckoutFragment(total, "customBuy", country), Constant.PROFILE, getActivity());
                    } else {
                        Toast.makeText(getActivity(), "Please agree to terms and condition", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (selectedPlan != null) {
                        if (binding.checkTermsCondition.isChecked()) {
                            //open checkout view
                            StudentHomeActivity.addFragment(new CheckoutFragment(selectedPlan, "planBuy", country), Constant.PROFILE, getActivity());
                        } else {
                            Toast.makeText(getActivity(), "Please agree to terms and condition", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please at least choose one plan.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

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
        int demoFeedbackId = SharedPrefsUtil.getIntegerPreferences(getContext(),"demoTutorSessionFeedbackId");
        DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
        demoTutorRequest.ScheduleId = App.selectedSessionDetail.ScheduleId;
        demoTutorRequest.ScheduleId2 = App.selectedSessionDetail.ScheduleId2;
        demoTutorRequest.StudentTutorBookingId = 0;
        demoTutorRequest.TutorId = App.selectedSessionDetail.TutorId;
        demoTutorRequest.TutorScheduleId = String.valueOf(App.selectedSessionDetail.TutorScheduleId);
        demoTutorRequest.CourseId = App.selectedSessionDetail.courseId;
       /* if (isRescheduled){
            demoTutorRequest.liveClassDetailId = String.valueOf(App.selectedSessionDetail.liveClassDetailId);
        }*/
        String day=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            day = DateTimeUtility.getDateFromWeekDay(App.selectedSessionDetail.courseSelectedDay);
        }
        demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(day, "dd MMMM yyyy", "yyyy-MM-dd") + " " + App.selectedSessionDetail.courseSelectedTime;
        demoTutorRequest.levelid = String.valueOf(App.selectedSessionDetail.levelId);
        demoTutorRequest.liveclasstype = 2;
        demoTutorRequest.classTypeId = 2;
        demoTutorRequest.categoryid = App.selectedSessionDetail.categoryId;
        demoTutorRequest.noOfSession = App.noOfSessions;
        demoTutorRequest.demoTutorSessionFeedbackId=demoFeedbackId;
        demoTutorRequest.isPaymentComplete=true;

        viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
       //         Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
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
