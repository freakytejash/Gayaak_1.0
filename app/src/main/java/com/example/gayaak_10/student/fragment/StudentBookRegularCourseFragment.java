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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentBookRegularCourseBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.RegularAvailableTutorAdapter;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.SelectedSessionDetail;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class StudentBookRegularCourseFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentBookRegularCourseBinding fragmentHomeBinding;
    private Fragment fragmentName = null;

    public StudentBookRegularCourseFragment(String userType) {
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
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
        RegularAvailableTutorAdapter availableTutorAdapter = new RegularAvailableTutorAdapter(getActivity(), detail, new RegularAvailableTutorAdapter.OnItemClickListener() {
            @Override
            public void onPayNow(CourseByStudentId.CourseByStudentDetail courseByStudentDetail, String selectedDay, String selectedTime) {
                if (!selectedDay.isEmpty() && !selectedTime.isEmpty()){
                    App.selectedSessionDetail = new SelectedSessionDetail(
                            courseByStudentDetail.name,
                            courseByStudentDetail.courseId,
                            courseByStudentDetail.price,
                            courseByStudentDetail.noofSession,
                            selectedDay,
                            selectedTime,
                            courseByStudentDetail.courseTutorsDataContractList.get(0).TutorId,
                            Integer.parseInt(courseByStudentDetail.courseTutorsDataContractList.get(0).TutorScheduleId),
                            courseByStudentDetail.levelId,
                            courseByStudentDetail.DemoTutorSessionFeedbackDataContractList.get(0).StudentTutorBookingId,
                            courseByStudentDetail.DemoTutorSessionFeedbackDataContractList.get(0).ScheduleId1,
                            courseByStudentDetail.categoryId
                            );

                    StudentHomeActivity.addFragment(new CoursePlansFragment("RecommendedCourse"), Constant.PROFILE, getActivity());
                }else {
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivDemoTutorBack) {
            fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.GONE);
            fragmentHomeBinding.rvAvailableTutor.setVisibility(View.GONE);
            fragmentHomeBinding.ivDemoTutorBack.setVisibility(View.GONE);
        }
    }
}
