package com.example.gayaak_10.student.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentBookRegularCourseBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.RegularAvailableTutorAdapter;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.SelectedSessionDetail;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class StudentBookRegularCourseFragment extends Fragment implements View.OnClickListener {

    private boolean isRescheduled = false;
    private StudentViewModel viewModel;
    private int tutorId = 0;
    private int tutorScheduleId = 0;
    private int liveClassDetailId = 0;
    private FragmentStudentBookRegularCourseBinding fragmentHomeBinding;
    private Fragment fragmentName = null;
    private int scheduledId;
    private int scheduledId2;

    public StudentBookRegularCourseFragment(String userType) {
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
    }

    public StudentBookRegularCourseFragment(String userType,Integer liveClassDetailId, Integer tutorId, Integer tutorScheduleId, boolean b) {
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
        this.liveClassDetailId = liveClassDetailId;
        this.tutorId = tutorId;
        this.tutorScheduleId = tutorScheduleId;
        isRescheduled = b;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentStudentBookRegularCourseBinding.inflate(getLayoutInflater());
        StudentViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(StudentViewModel.class);

        Utility.showLoader(getActivity(), true);

        viewModel.getAllCourseLevelsById().observe(getViewLifecycleOwner(), levelCategoryInfo -> {
            Utility.hideLoader();
            if (levelCategoryInfo.detail != null) {
                if (!levelCategoryInfo.detail.isEmpty()) {
                    setList(levelCategoryInfo.detail);
                    SharedPrefsUtil.setIntegerPreferences(getContext(),"demoTutorSessionFeedbackId",
                            levelCategoryInfo.detail.get(0).DemoTutorSessionFeedbackDataContractList.get(0).DemoTutorSessionFeedbackId);
                    App.tutorFeedbackId=levelCategoryInfo.detail.get(0).DemoTutorSessionFeedbackDataContractList.get(0).DemoTutorSessionFeedbackId;
                }
            } else {
                fragmentHomeBinding.rvAvailableTutor.setVisibility(View.GONE);
                fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.VISIBLE);
            }
        });

        fragmentHomeBinding.ivDemoTutorBack.setOnClickListener(this);

        return fragmentHomeBinding.getRoot();
    }

    private void setList(ArrayList<CourseByStudentId.CourseByStudentDetail> detail) {
        fragmentHomeBinding.rvAvailableTutor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RegularAvailableTutorAdapter availableTutorAdapter = new RegularAvailableTutorAdapter(getActivity(), detail, isRescheduled, new RegularAvailableTutorAdapter.OnItemClickListener() {
            @Override
            public void onPayNow(CourseByStudentId.CourseByStudentDetail courseByStudentDetail, String selectedDay, String selectedTime, String selectedDay2, String selectedTime2, boolean rescheduled) {
                if (!selectedDay.isEmpty() && !selectedTime.isEmpty()){


                       tutorId = courseByStudentDetail.courseTutorsDataContractList.get(0).TutorId;
                    //   tutorScheduleId = Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(0).TutorScheduleId);


                    for (int i=0; i<courseByStudentDetail.courseTutorsDataContractList.size();i++)
                    {
                        if (courseByStudentDetail.courseTutorsDataContractList.get(i).Day.equalsIgnoreCase(selectedDay) &&
                                courseByStudentDetail.courseTutorsDataContractList.get(i).Time.equalsIgnoreCase(selectedTime)){
                            scheduledId=Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(i).ScheduleId);
                            tutorScheduleId=Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(i).TutorScheduleId);
                            break;
                        }
                        else {
                            scheduledId=0;
                        }
                    }

                    if (!selectedDay2.isEmpty() && !selectedTime2.isEmpty()){

                        for (int i=0; i<courseByStudentDetail.courseTutorsDataContractList.size();i++)
                        {
                            if (courseByStudentDetail.courseTutorsDataContractList.get(i).Day.equalsIgnoreCase(selectedDay2) &&
                                    courseByStudentDetail.courseTutorsDataContractList.get(i).Time.equalsIgnoreCase(selectedTime2)){
                                scheduledId2=Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(i).ScheduleId);
                                break;
                            }
                            else {
                                scheduledId2=0;
                            }
                        }
                    }
                    else {
                        scheduledId2=0;
                    }


                    App.selectedSessionDetail = new SelectedSessionDetail(
                            courseByStudentDetail.name,
                            courseByStudentDetail.courseId,
                            courseByStudentDetail.price,
                            courseByStudentDetail.noofSession,
                            selectedDay,
                            selectedTime,
                            tutorId,
                            /*courseByStudentDetail.courseTutorsDataContractList.get(0).TutorId,*/
                            /*Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(0).TutorScheduleId),*/
                            tutorScheduleId,
                            courseByStudentDetail.levelId,
                            courseByStudentDetail.DemoTutorSessionFeedbackDataContractList.get(0).StudentTutorBookingId,
                            scheduledId,
                            scheduledId2,
                            liveClassDetailId,
                            /* Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(0).ScheduleId),*/
                            courseByStudentDetail.categoryId
                            );
                    if (rescheduled){
                       createSession();
                    }else {
                        StudentHomeActivity.addFragment(new CoursePlansFragment("RecommendedCourse"), Constant.PROFILE, getActivity());
                    }
                }

                else {
                    Toast.makeText(getActivity(), "Select both date and time", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancel() {
                StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
            }
        });
        fragmentHomeBinding.rvAvailableTutor.setAdapter(availableTutorAdapter);
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
        demoTutorRequest.liveClassDetailId = String.valueOf(App.selectedSessionDetail.liveClassDetailId);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivDemoTutorBack) {
            fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.GONE);
            fragmentHomeBinding.rvAvailableTutor.setVisibility(View.GONE);
            fragmentHomeBinding.ivDemoTutorBack.setVisibility(View.GONE);
        }
    }
}
