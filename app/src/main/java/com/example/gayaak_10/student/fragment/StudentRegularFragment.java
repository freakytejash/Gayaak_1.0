package com.example.gayaak_10.student.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.adapter.UserCourseAdapter;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentRegularHomeBinding;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.DemoUpcomingSessionAdapter;
import com.example.gayaak_10.student.model.CourseDataContract;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.zoom.ui.OpenZoomViewActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class StudentRegularFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentRegularHomeBinding binding;
    private StudentViewModel viewModel;
    private Context mContext;
    private Integer recommendedCourseCreated=0;

    public StudentRegularFragment(Integer recommendedCourseCreated) {
        this.recommendedCourseCreated = recommendedCourseCreated;
    }

    public StudentRegularFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentRegularHomeBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(StudentViewModel.class);

        getUpcomingClasses(viewModel);
        if (!App.coursesDetailArrayList.isEmpty()) {
            setLearningCourseData(App.coursesDetailArrayList);
        }
        binding.btnHomeBookDemoClass.setOnClickListener(this);
        binding.tvWalletPoints.setOnClickListener(this);
        binding.btnRecommendedClass.setOnClickListener(this);

        if (App.recommendedCourseCreated==1){
            binding.btnRecommendedClass.setVisibility(View.GONE);
        }

        if (App.userDataContract.detail.userWalletDataContract != null) {
            binding.tvWalletPoints.setText(Utility.withSuffix(App.userDataContract.detail.userWalletDataContract.Coins));
        }

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHomeBookDemoClass:
                StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment("Paid"), Constant.COURSE_CATALOG, getActivity());
                break;

            case R.id.tvWalletPoints:
                StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, getActivity());
                break;


            case R.id.btnRecommendedClass:
                StudentHomeActivity.addFragment(new StudentBookRegularCourseFragment("Paid"), Constant.COURSE_CATALOG, getActivity());
                break;

        }
    }
    /*---------------------------------------SET UI-----------------------------------------------*/

    private void setLearningCourseData(ArrayList<CoursesDetail> detail) {
       /* if (!detail.isEmpty()) {
            binding.layoutUserCourses.setVisibility(View.VISIBLE);
            binding.rvUserLearningCourses.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.rvUserLearningCourses.setAdapter(new UserCourseAdapter(getActivity(), detail, position -> {
                //open module screen
                StudentHomeActivity.addFragment(new StudentProgressFragment(detail.get(position)), Constant.COURSE_CATALOG, getActivity());
            }));
        } else {
            binding.layoutUserCourses.setVisibility(View.GONE);
        }
        getUpcomingClasses(viewModel);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.recommendedCourseCreated == 1)
        {
            binding.btnRecommendedClass.setVisibility(View.GONE);
        }
    }

    private void getUpcomingClasses(StudentViewModel viewModel) {
        Utility.showLoader(getContext(),false);
        viewModel.getDemoUserDashboard(App.userDataContract.detail.userId).observe(getViewLifecycleOwner(), demoUserDashboard -> {
           /* if (demoUserDashboard.detail !=null && demoUserDashboard.detail.courseDataContractList==null){

            }else if (demoUserDashboard.detail !=null && demoUserDashboard.detail.courseDataContractList!=null){
                setUpcomingCourse(demoUserDashboard.detail.courseDataContractList);
            }*/
            if (demoUserDashboard.detail != null && demoUserDashboard.detail.liveClassDataContractList != null) {
                binding.layoutUpcomingClasses.setVisibility(View.VISIBLE);
                setUpcomingClasses(demoUserDashboard.detail.liveClassDataContractList);
                viewModel.getDemoUserDashboard(App.userDataContract.detail.userId).removeObservers(getViewLifecycleOwner());
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
        getUpcomingClasses(viewModel);
    }


    private void setUpcomingClasses(ArrayList<LiveClassDataContractList> liveClassDataContractList) {
        Utility.hideLoader();
        binding.rvUpcomingSession.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        DemoUpcomingSessionAdapter sessionAdapter = new DemoUpcomingSessionAdapter(getActivity(), liveClassDataContractList, new DemoUpcomingSessionAdapter.OnItemClickListener() {

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
                }else {
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
        binding.rvUpcomingSession.setAdapter(sessionAdapter);
    }

    private void openSession(int position, ArrayList<LiveClassDataContractList> liveClassDataContractList) {
        if (App.userDataContract.detail.userWalletDataContract != null && App.userDataContract.detail.userWalletDataContract.Coins != 0) {
            if (!Constant.meetingNo.isEmpty()) {
                Intent intent = new Intent(getActivity(), OpenZoomViewActivity.class);
                intent.putExtra("sessionInfo", liveClassDataContractList.get(position));
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
            } else {
                Toast.makeText(getActivity(), "Please contact support for session details", Toast.LENGTH_SHORT).show();
            }
        } else {
            Utility.customDialog(getActivity(), "Insufficient Points", "Please upgrade your wallet points to attend this session.",
                    "Upgrade >", getLayoutInflater(), () -> StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, getActivity()));
        }
    }

    private void rescheduleSession(ArrayList<LiveClassDataContractList> liveClassDataContractList, int position) {
        new MaterialAlertDialogBuilder(mContext)
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you want to reschedule this session?")
                .setPositiveButton("Yes", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();

                    String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
                    if (liveClassDataContractList.get(position).liveClassTypeId.equals(2)){
                        StudentHomeActivity.addFragment(new StudentBookRegularCourseFragment(userType,
                                        liveClassDataContractList.get(position).liveclassdetailId,
                                        liveClassDataContractList.get(position).tutorId,
                                        liveClassDataContractList.get(position).TutorScheduleId, true),
                                Constant.COURSE_CATALOG, getActivity());

                    }else {
                        StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment("Paid", liveClassDataContractList.get(position).tutorId,
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
        new MaterialAlertDialogBuilder(mContext)
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you to cancel this session?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Utility.showLoader(mContext, false);
                    viewModel.cancelSessionStudent(liveClassDataContractList.get(position).TutorScheduleId, App.userDataContract.detail.userId).observe(getViewLifecycleOwner(), defaultResponse -> {
                        if (defaultResponse != null) {
                            Toast.makeText(mContext, defaultResponse.message, Toast.LENGTH_SHORT).show();
                            if (defaultResponse.status) {
                                if (!liveClassDataContractList.isEmpty()) {
                                    viewModel.cancelSessionStudent(liveClassDataContractList.get(position).TutorScheduleId,
                                            liveClassDataContractList.get(position).studentId)
                                            .removeObservers(getViewLifecycleOwner());
                                    liveClassDataContractList.remove(liveClassDataContractList.get(position));
                                    setUpcomingClasses(liveClassDataContractList);
                                } else {
                                    Utility.hideLoader();
                                    binding.layoutUpcomingClasses.setVisibility(View.GONE);
                                }
                            }
                        } else Utility.hideLoader();
                    });
                })
                .setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .show();

    }
}
