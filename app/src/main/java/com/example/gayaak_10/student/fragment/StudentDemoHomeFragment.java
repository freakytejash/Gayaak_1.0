package com.example.gayaak_10.student.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.NewCardAdapter;
import com.example.gayaak_10.adapter.UserCourseAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentDemoHomeBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.DemoUpcomingSessionAdapter;
import com.example.gayaak_10.student.model.BuyCoursesDetail;
import com.example.gayaak_10.student.model.CourseDataContract;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.widgets.CarouselEffectTransformer;
import com.example.gayaak_10.zoom.ui.OpenZoomViewActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class StudentDemoHomeFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentDemoHomeBinding binding;
    private boolean isDialogShown = false;
    private StudentViewModel viewModel;
    private DemoUpcomingSessionAdapter sessionAdapter;
    private Integer recommendedCourseCreated=0;

    public StudentDemoHomeFragment(Integer recommendedCourseCreated) {
        this.recommendedCourseCreated = recommendedCourseCreated;
    }

    public StudentDemoHomeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentDemoHomeBinding.inflate(getLayoutInflater());
        binding.btnHomeBookDemoClass.setOnClickListener(this);
        binding.tvWalletPoints.setOnClickListener(this);
        binding.btnRecommendedClass.setOnClickListener(this);

        if (App.recommendedCourseCreated==1){
            binding.btnRecommendedClass.setVisibility(View.GONE);
        }

        if (App.userDataContract.detail.userWalletDataContract!= null){
            binding.tvWalletPoints.setText(Utility.withSuffix(App.userDataContract.detail.userWalletDataContract.Coins));
        }

        return binding.getRoot();
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnHomeBookDemoClass) {
            StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment("Free"), Constant.COURSE_CATALOG, getActivity());
        }

        if (view.getId() == R.id.tvWalletPoints) {
            StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, getActivity());
        }

        if (view.getId() == R.id.btnRecommendedClass){
            StudentHomeActivity.addFragment(new StudentBookRegularCourseFragment("Free"), Constant.COURSE_CATALOG, getActivity());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getUpcomingClasses(StudentViewModel viewModel) {
        viewModel.getDemoUserDashboard(App.userDataContract.detail.userId).observe(getActivity(), demoUserDashboard -> {
        /*    if (demoUserDashboard.detail !=null && demoUserDashboard.detail.courseDataContractList==null){

            }else */
        /*    if (demoUserDashboard.detail !=null && demoUserDashboard.detail.courseDataContractList!=null){
                setUpcomingCourse(demoUserDashboard.detail.courseDataContractList);
            }*/
            if (demoUserDashboard.detail != null && demoUserDashboard.detail.liveClassDataContractList != null) {
                binding.layoutUpcomingClasses.setVisibility(View.VISIBLE);
                setUpcomingClasses(demoUserDashboard.detail.liveClassDataContractList);
                viewModel.getDemoUserDashboard(App.userDataContract.detail.userId).removeObservers(getActivity());
            } else {
                binding.layoutUpcomingClasses.setVisibility(View.GONE);
            }
        });
    }

    private void setUpcomingCourse(ArrayList<CourseDataContract> detail) {
        if (!detail.isEmpty()) {
            binding.layoutUserCourses.setVisibility(View.VISIBLE);
            binding.rvUserLearningCourses.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.rvUserLearningCourses.setAdapter(new UserCourseAdapter(getActivity(), detail, position -> {
                //open module screen
                //      StudentHomeActivity.addFragment(new StudentProgressFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());
            }));
        } else {
            binding.layoutUserCourses.setVisibility(View.GONE);
        }
       /* getUpcomingClasses(viewModel);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpcomingClasses(ArrayList<LiveClassDataContractList> liveClassDataContractList) {
        Utility.hideLoader();
        if (liveClassDataContractList != null && !liveClassDataContractList.isEmpty()) {
            binding.layoutUpcomingClasses.setVisibility(View.VISIBLE);
            binding.rvDemoUpcomingSession.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            sessionAdapter = new DemoUpcomingSessionAdapter(getActivity(), liveClassDataContractList, new DemoUpcomingSessionAdapter.OnItemClickListener() {

                @Override
                public void onItemClickListener(int position) {

                }

                @Override
                public void onSessionReschedule(int position) {
                    rescheduleSession(liveClassDataContractList, position);
                }

                @Override
                public void onSessionCancel(int position) {
                    sessionCancel(liveClassDataContractList, position);
                }

                @Override
                public void onStartClass(int position) {

                    viewModel.postEmailNotification(liveClassDataContractList.get(position).studentId,
                            liveClassDataContractList.get(position).liveclassdetailId).observe(getActivity(), new Observer<DefaultResponse>() {
                        @Override
                        public void onChanged(DefaultResponse defaultResponse) {
                            Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    App.sessionStarted = liveClassDataContractList.get(position);


                    if(liveClassDataContractList.get(position).Price > App.userDataContract.detail.userWalletDataContract.Coins
                            && liveClassDataContractList.get(position).liveClassTypeId==2){
                        StudentHomeActivity.addFragment(new CoursePlansFragment(), Constant.COURSE_CATALOG, getActivity());
                    }
                    else {
                        Constant.meetingNo = liveClassDataContractList.get(position).ZoomMeetingId;
                        Constant.meetingPassword = liveClassDataContractList.get(position).ZoomMeetingPassword;
                        openSession(position, liveClassDataContractList);

                    }

                }

                @Override
                public void onSessionNotAttended(LiveClassDataContractList liveClassDataContractList) {
                    Log.e("noSession", "onSessionNotAttended: " +liveClassDataContractList.studentId);
                }
            });
            binding.rvDemoUpcomingSession.setAdapter(sessionAdapter);
        } else {
            binding.layoutUpcomingClasses.setVisibility(View.GONE);
        }
    }

    private void openSession(int position, ArrayList<LiveClassDataContractList> liveClassDataContractList) {
        if (!Constant.meetingNo.isEmpty()) {
            Intent intent = new Intent(getActivity(), OpenZoomViewActivity.class);
            intent.putExtra("sessionInfo", liveClassDataContractList.get(position));
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
        } else {
            Toast.makeText(getActivity(), "Please contact support for session details", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void rescheduleSession(ArrayList<LiveClassDataContractList> liveClassDataContractList, int position) {
        new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you want to reschedule this session?")
                .setPositiveButton("Yes", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();
                    String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
                    if (liveClassDataContractList.get(position).liveClassTypeId!=null
                            && liveClassDataContractList.get(position).liveClassTypeId.equals(2)){
                        StudentHomeActivity.addFragment(new StudentRescheduleFragment(userType,
                                        liveClassDataContractList.get(position)),
                                Constant.COURSE_CATALOG, getActivity());

                    }else {
                        StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment("Free", liveClassDataContractList.get(position).tutorId,
                                liveClassDataContractList.get(position).TutorScheduleId, true), Constant.COURSE_CATALOG, getActivity());
                        if (!liveClassDataContractList.isEmpty()) {
                            liveClassDataContractList.remove(position);
                        }
                    }
                        })
                ).setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .show();

    }

    private void sessionCancel(ArrayList<LiveClassDataContractList> liveClassDataContractList, int position) {
        new MaterialAlertDialogBuilder(getActivity())
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you to cancel this session?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Utility.showLoader(getActivity(), true);
                    viewModel.cancelSessionStudent(liveClassDataContractList.get(position).TutorScheduleId, liveClassDataContractList.get(position).studentId)
                            .observe(getActivity(), new Observer<DefaultResponse>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onChanged(DefaultResponse defaultResponse) {
                                    if (defaultResponse != null) {
                                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                        if (defaultResponse.status) {
                                            if (!liveClassDataContractList.isEmpty()) {
                                                viewModel.cancelSessionStudent(liveClassDataContractList.get(position).TutorScheduleId, liveClassDataContractList.get(position).studentId)
                                                        .removeObservers(getActivity());
                                                liveClassDataContractList.remove(position);
                                                setUpcomingClasses(liveClassDataContractList);
                                            } else {
                                                Utility.hideLoader();
                                                binding.layoutUpcomingClasses.setVisibility(View.GONE);
                                            }
                                        }
                                    } else Utility.hideLoader();
                                }
                            });

                })
                .setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .show();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getViewPager(ArrayList<CoursesDetail> tempList) {
        if (!tempList.isEmpty()) {
            binding.layoutViewPager.setVisibility(View.VISIBLE);
            NewCardAdapter mCardAdapter = new NewCardAdapter(tempList, getActivity(), position -> {
                addCourseToCart(tempList.get(position));
            });
            binding.viewPager.setAdapter(mCardAdapter);
            mCardAdapter.notifyDataSetChanged();
            binding.viewPager.setOffscreenPageLimit(3);
            binding.viewPager.setPadding(130, 0, 130, 0);
            binding.tabLayout.setupWithViewPager(binding.viewPager, true);
            try {
                binding.viewPager.setPageTransformer(false, new CarouselEffectTransformer(getActivity())); // Set transformer
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            binding.layoutViewPager.setVisibility(View.GONE);
        }
        getUpcomingClasses(viewModel);

    }

    private void addCourseToCart(CoursesDetail selectedCourse) {

        BuyCoursesDetail buyCoursesDetail = new BuyCoursesDetail();
        buyCoursesDetail.courseId = selectedCourse.courseId;
        buyCoursesDetail.detail = selectedCourse.detail;
        buyCoursesDetail.name = selectedCourse.name;
        buyCoursesDetail.price = selectedCourse.price;
        buyCoursesDetail.ThumbnailImage = selectedCourse.ThumbnailImage;
        buyCoursesDetail.levelName = selectedCourse.levelName;

        if (App.cartList.size() <= 0) {
            App.cartList.add(buyCoursesDetail);
            SharedPrefsUtil.setCartCourse(getActivity(), App.cartList);
            showCartDialog();
        } else {
            for (int j = 0; j < App.cartList.size(); j++) {
                if (App.cartList.get(j).courseId.equals(buyCoursesDetail.courseId)) {
                    Toast.makeText(getActivity(), R.string.course_already, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (j == App.cartList.size() - 1) {
                        App.cartList.add(buyCoursesDetail);
                        SharedPrefsUtil.setCartCourse(getActivity(), App.cartList);
                        showCartDialog();
                        break;
                    }
                }
            }
        }
    }

    private void showCartDialog() {

        if (!isDialogShown) {
            isDialogShown = true;
            new MaterialAlertDialogBuilder(getActivity())
                    .setIcon(R.drawable.gayaak_icon)
                    .setTitle("Course added to cart")
                    .setMessage(getString(R.string.course_added_cart))
                    .setPositiveButton("Go to Cart", (dialogInterface, i) -> {
                        isDialogShown = false;
                        StudentHomeActivity.addFragment(new CourseCartFragment(), Constant.PROFILE, getActivity());
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        isDialogShown = false;
                        dialogInterface.dismiss();
                    })
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();


        if (App.recommendedCourseCreated == 1)
        {
            binding.btnRecommendedClass.setVisibility(View.GONE);
        }

        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);
        getUpcomingClasses(viewModel);
        Utility.hideLoader();
        if (!App.allCoursesArrayList.isEmpty()) {
            getViewPager(App.allCoursesArrayList);
        }

    }
}
