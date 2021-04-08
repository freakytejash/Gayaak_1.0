package com.example.gayaak_10.student.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentBookDemoCourseBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.AvailableTutorAdapter;
import com.example.gayaak_10.student.model.CategoryDataContractList;
import com.example.gayaak_10.student.model.CourseTutorDataContractList;
import com.example.gayaak_10.student.model.LearningLevelDataContractList;
import com.example.gayaak_10.student.model.TutorByCourseLevel;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.adapter.CourseCategoryTypeAdapter;
import com.example.gayaak_10.tutor.adapter.CourseLevelTypeAdapter;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentBookDemoCourseFragment extends Fragment implements View.OnClickListener {

    private boolean isRescheduled = false;
    private int tutorId = 0;
    private int tutorScheduleId = 0;
    private FragmentStudentBookDemoCourseBinding fragmentHomeBinding;
    private StudentViewModel viewModel;
    private int selectedType = -1;
    private int selectedLevel = -1;
    private ArrayList<LearningLevelDataContractList> levelsList = new ArrayList<>();
    private ArrayList<CategoryDataContractList> courseList = new ArrayList<>();
    private String userType = "";
    private Fragment fragmentName = null;

    public StudentBookDemoCourseFragment(String userType) {
        this.userType = userType;
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
    }

    public StudentBookDemoCourseFragment(String userType, Integer tutorId, Integer tutorScheduleId, boolean b) {
        this.userType = userType;
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
        this.tutorId = tutorId;
        this.tutorScheduleId = tutorScheduleId;
        isRescheduled = b;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentStudentBookDemoCourseBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        Utility.showLoader(getActivity(), true);
        viewModel.getAllCourseLevels().observe(getActivity(), levelCategoryInfo -> {
            Utility.hideLoader();
            if (levelCategoryInfo.categoryDataContractList != null && levelCategoryInfo.learningLevelDataContractList != null) {
                if (!levelCategoryInfo.categoryDataContractList.isEmpty() && !levelCategoryInfo.learningLevelDataContractList.isEmpty()) {
                    fragmentHomeBinding.layoutBookDemoClass.setVisibility(View.VISIBLE);
                    fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.GONE);

                    levelsList = levelCategoryInfo.learningLevelDataContractList;
                    courseList = levelCategoryInfo.categoryDataContractList;
                    /*Log.e("book", "Class: " +App.firstSelectedLevelType);
                    Log.e("book", "Class: " +App.firstSelectedCourseType);*/
                    if (App.firstSelectedCourseType != -1 && App.firstSelectedLevelType != -1) {
                        for (int i = 0; i < levelsList.size(); i++) {
                            if (levelsList.get(i).learningLevelId.equals(App.firstSelectedLevelType)) {
                               /* Log.e("book", "if: " +App.firstSelectedLevelType+ " -> " +levelsList.get(i).learningLevelId);
                                Log.e("book", "tvLevelName: " +levelsList.get(i).name);*/
                                fragmentHomeBinding.tvLevelName.setText(levelsList.get(i).name);
                                selectedLevel = levelsList.get(i).learningLevelId;
                                break;
                            }
                        }

                        for (int i = 0; i < courseList.size(); i++) {
                            if (courseList.get(i).categoryId.equals(App.firstSelectedCourseType)) {
                                // Log.e("book", "if: " +App.firstSelectedCourseType+ " -> " +courseList.get(i).categoryId);

                                selectedType = courseList.get(i).categoryId;
                                // Log.e("book", "tvCourseName: " +courseList.get(i).name);
                                fragmentHomeBinding.tvCourseName.setText(courseList.get(i).name);
                                break;
                            }
                        }
                    }
                }
            } else {
                fragmentHomeBinding.layoutBookDemoClass.setVisibility(View.GONE);
                fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.VISIBLE);
            }

        });

        fragmentHomeBinding.layoutSelectLevel.setOnClickListener(this);
        fragmentHomeBinding.layoutSelectCourse.setOnClickListener(this);
        fragmentHomeBinding.ivDemoTutorBack.setOnClickListener(this);
        fragmentHomeBinding.tvFindTutor.setOnClickListener(this);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDemoTutorBack:
                fragmentHomeBinding.layoutEmptyTutor.setVisibility(View.GONE);
                fragmentHomeBinding.rvAvailableTutor.setVisibility(View.GONE);
                fragmentHomeBinding.ivDemoTutorBack.setVisibility(View.GONE);
                fragmentHomeBinding.layoutBookDemoClass.setVisibility(View.VISIBLE);
                break;

            case R.id.tvFindTutor:
                App.firstSelectedLevelType = -1;
                App.firstSelectedCourseType = -1;
                findTutor(selectedLevel, selectedType);
                break;

            case R.id.layoutSelectCourse:
                openCoursePopup(view);
                break;

            case R.id.layoutSelectLevel:
                openLevelPopup(view);
                break;
        }
    }

    private void openCoursePopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = inflater.inflate(R.layout.popup_tutor_student, null);
        popup.setContentView(layout);
        RecyclerView rvSlotTime = (RecyclerView) layout.findViewById(R.id.rvSlotTime);
        rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CourseCategoryTypeAdapter adapter = new CourseCategoryTypeAdapter(getActivity(), courseList, new CourseCategoryTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                popup.dismiss();
                selectedType = courseList.get(position).categoryId;
                fragmentHomeBinding.tvCourseName.setText(courseList.get(position).name);

            }
        });
        rvSlotTime.setAdapter(adapter);

        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.showAsDropDown(view);
    }

    private void openLevelPopup(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = inflater.inflate(R.layout.popup_tutor_student, null);
        popup.setContentView(layout);
        RecyclerView rvSlotTime = (RecyclerView) layout.findViewById(R.id.rvSlotTime);
        rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CourseLevelTypeAdapter adapter = new CourseLevelTypeAdapter(getActivity(), levelsList, new CourseLevelTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                popup.dismiss();
                selectedLevel = levelsList.get(position).learningLevelId;
                fragmentHomeBinding.tvLevelName.setText(levelsList.get(position).name);

            }
        });
        rvSlotTime.setAdapter(adapter);

        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.showAsDropDown(view);
    }

    private void findTutor(int selectedLevel, int selectedType) {
        //call api
        if (selectedLevel == -1 && selectedType == -1) {
            Toast.makeText(getActivity(), "Please select your course and level type.", Toast.LENGTH_SHORT).show();
            return;
        }
        fragmentHomeBinding.layoutBookDemoClass.setVisibility(View.GONE);
        viewModel.getTutorSchedule(selectedLevel, selectedType).observe(getActivity(), new Observer<TutorByCourseLevel>() {
            @Override
            public void onChanged(TutorByCourseLevel tutorByCourseLevel) {
                if (tutorByCourseLevel != null && tutorByCourseLevel.detail != null) {
                    if (tutorByCourseLevel.detail.courseTutorDataContractList != null) {
                        HashMap<Integer, ArrayList<CourseTutorDataContractList>> hashMap = new HashMap<>();
                        for (int i = 0; i < tutorByCourseLevel.detail.courseTutorDataContractList.size(); i++) {
                            if (!hashMap.containsKey(tutorByCourseLevel.detail.courseTutorDataContractList.get(i).tutorId)) {
                                ArrayList<CourseTutorDataContractList> list = new ArrayList<CourseTutorDataContractList>();
                                list.add(tutorByCourseLevel.detail.courseTutorDataContractList.get(i));
                                hashMap.put(tutorByCourseLevel.detail.courseTutorDataContractList.get(i).tutorId, list);
                            } else {
                                hashMap.get(tutorByCourseLevel.detail.courseTutorDataContractList.get(i).tutorId).add(tutorByCourseLevel.detail.courseTutorDataContractList.get(i));
                            }
                        }
                        setTutorsList(tutorByCourseLevel.detail.courseTutorDataContractList, hashMap);
                        emptyTutorView(View.GONE, View.VISIBLE);
                    } else {
                        emptyTutorView(View.VISIBLE, View.GONE);
                    }
                } else {
                    emptyTutorView(View.VISIBLE, View.GONE);
                }

            }
        });
    }

    private void emptyTutorView(int visible, int gone) {
        fragmentHomeBinding.layoutEmptyTutor.setVisibility(visible); 
        fragmentHomeBinding.rvAvailableTutor.setVisibility(gone);
        fragmentHomeBinding.ivDemoTutorBack.setVisibility(View.VISIBLE);
    }

    private void setTutorsList(ArrayList<CourseTutorDataContractList> courseTutorDataContractLists, HashMap<Integer, ArrayList<CourseTutorDataContractList>> hashMap) {
        fragmentHomeBinding.rvAvailableTutor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AvailableTutorAdapter availableTutorAdapter = new AvailableTutorAdapter(getActivity(), courseTutorDataContractLists, hashMap, new AvailableTutorAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }

            @Override
            public void bookTutor(CourseTutorDataContractList courseTutorDataContractList, String name) {

                DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
                demoTutorRequest.ScheduleId = Integer.valueOf(courseTutorDataContractList.scheduleId);
                demoTutorRequest.StudentTutorBookingId = courseTutorDataContractList.id;
                demoTutorRequest.TutorId = courseTutorDataContractList.tutorId;
                demoTutorRequest.TutorScheduleId = String.valueOf(Integer.valueOf(courseTutorDataContractList.tutorScheduleId));
                demoTutorRequest.CourseId = courseTutorDataContractList.courseId;
                demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(name, "dd MMMM yyyy", "yyyy-MM-dd") + " " + courseTutorDataContractList.time;
                demoTutorRequest.levelid = courseTutorDataContractList.levelId;
                //demoTutorRequest.categoryid = courseTutorDataContractList.;
                demoTutorRequest.classTypeId = 1;
                demoTutorRequest.liveclasstype = 1;
                demoTutorRequest.demoTutorSessionFeedbackId=null;
                demoTutorRequest.isPaymentComplete=false;
                //demoTutorRequest.liveClassDetailId = courseTutorDataContractList.
                String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
                /*if (!userType.isEmpty()) {
                    if (userType.equalsIgnoreCase("Free")) {
                        demoTutorRequest.liveclasstype = 1;
                    } else if (userType.equalsIgnoreCase("Paid")) {
                        demoTutorRequest.liveclasstype = 2;
                    }
                }*/
                Utility.showLoader(getActivity(), false);
                if (isRescheduled) {
                    rescheduleSession();
                }
                viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
                    @Override
                    public void onChanged(DefaultResponse defaultResponse) {
                        Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                        Utility.hideLoader();
                        StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
                    }
                });
            }

            @Override
            public void skipTutor(int position) {
                StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
            }
        });
        fragmentHomeBinding.rvAvailableTutor.setAdapter(availableTutorAdapter);
    }

    private void rescheduleSession() {
        viewModel.manageRescheduleStudent(tutorId, tutorScheduleId).observe(getViewLifecycleOwner(), new Observer<DefaultResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
                if (defaultResponse != null) {
                    Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                    if (defaultResponse.status) {
                        StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
                    }
                } else {
                    Utility.hideLoader();
                    StudentHomeActivity.addFragment(fragmentName, Constant.HOME, getActivity());
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        App.firstSelectedLevelType = -1;
        App.firstSelectedCourseType = -1;
    }
}
