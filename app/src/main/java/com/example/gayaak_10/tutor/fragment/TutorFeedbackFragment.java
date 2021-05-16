package com.example.gayaak_10.tutor.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorFeedbackBinding;
import com.example.gayaak_10.model.response.AllCourses;
import com.example.gayaak_10.model.response.CoursesDetail;
import com.example.gayaak_10.model.response.FilteredCourseDetail;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.adapter.AllCoursesAdapter;
import com.example.gayaak_10.tutor.adapter.SessionSlotTimeAdapter;
import com.example.gayaak_10.tutor.adapter.StringListAdapter;
import com.example.gayaak_10.tutor.adapter.StudentProficiencyAdapter;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;
import com.example.gayaak_10.tutor.model.ScheduleSlotTimeTemp;
import com.example.gayaak_10.tutor.model.StudentProficiency;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.tutor.model.request.FeedbackTutorContentRequest;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.Collections;

public class TutorFeedbackFragment extends Fragment implements View.OnClickListener {

    private FragmentTutorFeedbackBinding binding;
    private int isStudentQualify = -1;
    private boolean haveDecidedSession = false;
    private boolean isStudentTolerant = false;
    private ArrayList<StudentProficiency> studentProficiencies = new ArrayList<>();
    private  ArrayList<CoursesDetail> allCoursesArrayList = new ArrayList<CoursesDetail>();
    private TutorViewModel mViewModel;
    private ArrayList<String> sessionDayList = new ArrayList<>();
    private int noOfClass = 0;
    private Integer schedule1 = 0;
    private Integer schedule2 = 1;
    private ArrayList<ScheduleSlotTimeTemp> allScheduleList = new ArrayList<>();
    private TodaySessions tutorSession = new TodaySessions();
    private Integer selectedCourseId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTutorFeedbackBinding.inflate(getLayoutInflater());
        mViewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);
       
        tutorSession = App.tutorSessionStarted;

        mViewModel.getAllCourseByCatIdAndLevelId(tutorSession.CategoryId, tutorSession.levelId).observe(this, new Observer<AllCourses>() {
            @Override
            public void onChanged(AllCourses allCourses) {
                if (allCourses != null) {
                    allCoursesFinalList(allCourses.detail);

                    if (!App.allCourseRecommendedList.isEmpty()) {
                        binding.layoutCourseRecommend.setVisibility(View.VISIBLE);
                    } else {
                        binding.layoutCourseRecommend.setVisibility(View.GONE);
                    }
                }
                else {

                }

            }
        });



        /*mViewModel.getStudentAllCourses().observe(StudentHomeActivity.this, new Observer<FilteredCourseDetail>() {
            @Override
            public void onChanged(FilteredCourseDetail filteredCourseDetail) {
                if (filteredCourseDetail != null && filteredCourseDetail.coursesDetails.size() != 0) {
                    allCoursesFinalList(filteredCourseDetail.coursesDetails);
                } else {
                    openDemoHome();
                }
            }
        });*/

        tutorSession =  Constant.todaySessionsData;

        binding.ivFeedbackBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.layoutCourseRecommend.setOnClickListener(this);
        binding.layoutSelectDat.setOnClickListener(this);
        binding.layoutSelectDay1.setOnClickListener(this);
        binding.layoutTime1.setOnClickListener(this);
        binding.layoutTime2.setOnClickListener(this);


        switch (tutorSession.LevelName){
            case "Beginner Level":
                binding.layoutSitting.setVisibility(View.VISIBLE);
                studentProficiencies.add(new StudentProficiency("Dictation", false));
                studentProficiencies.add(new StudentProficiency("Shruthi", false));

                break;

            case "Intermediate Level":
                binding.layoutSitting.setVisibility(View.GONE);
                studentProficiencies.add(new StudentProficiency("Shruthi", false));
                studentProficiencies.add(new StudentProficiency("Thalam", false));

                break;

            case "Expert Level":
                binding.layoutSitting.setVisibility(View.GONE);
                studentProficiencies.add(new StudentProficiency("Varisais(6)", false));
                studentProficiencies.add(new StudentProficiency("Shruthi", false));
                studentProficiencies.add(new StudentProficiency("Thalam", false));

                break;

            case "Advance Level":
                binding.layoutSitting.setVisibility(View.GONE);
                studentProficiencies.add(new StudentProficiency("Geetham", false));
                studentProficiencies.add(new StudentProficiency("Shruthi", false));
                studentProficiencies.add(new StudentProficiency("Thalam", false));
                studentProficiencies.add(new StudentProficiency("Varisais(6)", false));

                break;
        }


        //getSlotTime();

        binding.rvProficiencies.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        StudentProficiencyAdapter rvProficiencies = new StudentProficiencyAdapter(getActivity(), studentProficiencies, new StudentProficiencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(String proficiencyName, int selected) {
                for (int i = 0; i < studentProficiencies.size(); i++) {
                    if (studentProficiencies.get(i).proficiencyName.equalsIgnoreCase(proficiencyName)) {
                        if (selected == 0) {
                            studentProficiencies.get(i).isSelected = true;
                        } else if (selected == 1) {
                            studentProficiencies.get(i).isSelected = false;
                        }
                        break;
                    }
                }
            }
        });
        binding.rvProficiencies.setAdapter(rvProficiencies);

        binding.radioQualities.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.qualitiesYes) {
                isStudentQualify = 0;
            } else if (checkedId == R.id.qualitiesNo) {
                isStudentQualify = 1;
            }
        });

        binding.radioGroupDateTime.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.dateTimeYes) {
                haveDecidedSession = true;
            } else if (checkedId == R.id.dateTimeNo) {
                haveDecidedSession = false;
            }
        });

        binding.radioGroupTolerance.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.toleranceYes) {
                isStudentTolerant = true;
            } else if (checkedId == R.id.toleranceNo) {
                isStudentTolerant = false;
            }
        });

      /*  binding.radioGroupClassWeek.setOnCheckedChangeListener((group, checkedId) -> {
            // find which radio button is selected
            if (checkedId == R.id.rbOne) {
                noOfClass = 1;
                binding.layoutFirstSession.setVisibility(View.VISIBLE);
                binding.layoutSecondSession.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbtwo) {
                noOfClass = 2;
                binding.layoutFirstSession.setVisibility(View.VISIBLE);
                binding.layoutSecondSession.setVisibility(View.VISIBLE);
            }
        });*/
        return binding.getRoot();
    }


    private void allCoursesFinalList(ArrayList<CoursesDetail> tempCoursesArrayList) {
        Collections.sort(tempCoursesArrayList, (s1, s2) -> s1.name.compareToIgnoreCase(s2.name));


        App.allCourseRecommendedList.clear();
        App.allCourseRecommendedList.addAll(tempCoursesArrayList);
    }

    private void openCoursePopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = inflater.inflate(R.layout.popup_tutor_student, null);
        popup.setContentView(layout);
        RecyclerView rvSlotTime = layout.findViewById(R.id.rvSlotTime);
        rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        AllCoursesAdapter adapter = new AllCoursesAdapter(getActivity(), App.allCourseRecommendedList, new AllCoursesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                popup.dismiss();
                selectedCourseId = App.allCourseRecommendedList.get(position).courseId;
                binding.tvCourseRecommended.setText(App.allCourseRecommendedList.get(position).name);
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


    private void openSessionDayPopup(View view, AppCompatTextView textView, String type) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(getActivity());
        View layout = inflater.inflate(R.layout.popup_tutor_student, null);
        popup.setContentView(layout);
        RecyclerView rvSlotTime = layout.findViewById(R.id.rvSlotTime);
        rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        StringListAdapter adapter = new StringListAdapter(getActivity(), sessionDayList, new StringListAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                popup.dismiss();
                textView.setText(sessionDayList.get(position));

                if (type.equalsIgnoreCase("firstTime")){
                    binding.tvClassTime1.setText("");
                }else if (type.equalsIgnoreCase("secondTime")){
                    binding.tvClassTime2.setText("");
                }
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

    private void openTimePopup(View view, AppCompatTextView textView, String type) {
        ArrayList<ScheduleSlotDetail> scheduleTimeList = new ArrayList<>();
        if (type.equalsIgnoreCase("firstTime")) {
            if (binding.tvDayName1.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select day to select time", Toast.LENGTH_SHORT).show();
                return;
            } else {
                scheduleTimeList = findScheduleByDay(binding.tvDayName1.getText().toString());
            }
        } else if (type.equalsIgnoreCase("secondTime")) {
            if (binding.tvDayName2.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select day to select time", Toast.LENGTH_SHORT).show();
                return;
            } else {
                scheduleTimeList = findScheduleByDay(binding.tvDayName2.getText().toString());
            }
        }
        if (scheduleTimeList != null && !scheduleTimeList.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow popup = new PopupWindow(getActivity());
            View layout = inflater.inflate(R.layout.popup_time, null);
            popup.setContentView(layout);
            RecyclerView rvSlotTime = (RecyclerView) layout.findViewById(R.id.rvSlotTime);
            rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            SessionSlotTimeAdapter adapter = new SessionSlotTimeAdapter(getActivity(), scheduleTimeList, new SessionSlotTimeAdapter.OnItemClickListener() {

                @Override
                public void onItemClickListener(ScheduleSlotDetail scheduleSlotDetail) {
                    popup.dismiss();
                    if (!scheduleSlotDetail.time.equalsIgnoreCase(binding.tvClassTime1.getText().toString().trim()) ||
                            !scheduleSlotDetail.time.equalsIgnoreCase(binding.tvClassTime2.getText().toString().trim())) {

                       if (binding.tvDayName1.getText().toString().equalsIgnoreCase(binding.tvDayName2.getText().toString())){
                           if (scheduleSlotDetail.time.equalsIgnoreCase(binding.tvClassTime1.getText().toString())
                           || scheduleSlotDetail.time.equalsIgnoreCase(binding.tvClassTime2.getText().toString())){
                               Toast.makeText(getActivity(),"Already Selected. Select Different time", Toast.LENGTH_SHORT).show();
                           }else {
                               textView.setText(scheduleSlotDetail.time);
                           }
                       }else {
                           textView.setText(scheduleSlotDetail.time);
                       }
                    }

                    if (!binding.tvClassTime1.getText().toString().isEmpty()) {
                        schedule1 = scheduleSlotDetail.scheduleId;
                    } else if (!binding.tvClassTime2.getText().toString().isEmpty()) {
                        schedule2 = scheduleSlotDetail.scheduleId;
                    }


                    Log.e("time", " => " + scheduleSlotDetail.scheduleId);
                }
            });
            rvSlotTime.setAdapter(adapter);

            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            // Closes the popup window when touch outside of it - when looses focus
            popup.setOutsideTouchable(true);
            popup.setFocusable(true);
            // Show anchored to button
            popup.showAsDropDown(view, 0, 0, Gravity.TOP);
            //popup.showAsDropDown(view);
        } else {
            Toast.makeText(getActivity(), "No schedule found for this day", Toast.LENGTH_SHORT).show();
        }

    }

    private ArrayList<ScheduleSlotDetail> findScheduleByDay(String selectedDay) {
        ArrayList<ScheduleSlotDetail> scheduleTimeList = new ArrayList<>();
        for (int i = 0; i < allScheduleList.size(); i++) {
            if (allScheduleList.get(i).detail != null) {
            //    Log.e("feedback", " size detail :- " + allScheduleList.get(i).detail.size());
                for (int j = 0; j < allScheduleList.get(i).detail.size(); j++) {
                    if (allScheduleList.get(i).dayName.equalsIgnoreCase(selectedDay)) {
                        if (allScheduleList.get(i).detail.get(j).IsScheduleAvailable == 1) {
                            if (!scheduleTimeList.contains(allScheduleList.get(i).detail.get(j))) {
                                scheduleTimeList.add(allScheduleList.get(i).detail.get(j));
                            }
                        }
                    }
                    if (i == allScheduleList.size() - 1) {
                        return scheduleTimeList;
                    }
                }
            }
        }
        return null;
    }

    private void getSlotTime() {
        Utility.showLoader(getActivity(), false);
        mViewModel.getScheduleSlotTime().observe(getViewLifecycleOwner(), scheduleSlotTimeTemps -> {
            if (scheduleSlotTimeTemps == null || scheduleSlotTimeTemps.size() == 0) {
                binding.layoutFirstSession.setVisibility(View.GONE);
                binding.layoutSecondSession.setVisibility(View.VISIBLE);
            } else {
                Utility.hideLoader();
                sessionDayList.clear();
                allScheduleList.clear();
                allScheduleList.addAll(scheduleSlotTimeTemps);
                for (int i = 0; i < scheduleSlotTimeTemps.size(); i++) {
                    sessionDayList.add(scheduleSlotTimeTemps.get(i).dayName);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivFeedbackBack:
                TutorHomeActivity.addFragment(new TutorHomeFragment(), Constant.HOME, getActivity());
                break;

            case R.id.btnSubmit:
                if (isStudentQualify == -1) {
                    Toast.makeText(getActivity(), "Please select if student qualifies or not", Toast.LENGTH_SHORT).show();
                    return;
                }
                Utility.showLoader(getContext(), false);
                submitFeedback(App.tutorSessionStarted, isStudentQualify, haveDecidedSession, isStudentTolerant,
                        binding.etFeedback.getText().toString().trim(), studentProficiencies,
                        noOfClass, schedule1, schedule2, binding.tvCourseRecommended.getText().toString());
                break;

            case R.id.layoutCourseRecommend:
                openCoursePopup(view);
                break;

            case R.id.layoutSelectDat:
                openSessionDayPopup(view, binding.tvDayName1, "firstTime");
                break;

            case R.id.layoutSelectDay1:
                openSessionDayPopup(view, binding.tvDayName2, "secondTime");
                break;

            case R.id.layoutTime1:
                openTimePopup(view, binding.tvClassTime1, "firstTime");
                break;

            case R.id.layoutTime2:
                openTimePopup(view, binding.tvClassTime2, "secondTime");
                break;
        }
    }

    private void submitFeedback(TodaySessions tutorSessionStarted, int isStudentQualify,
                                boolean haveDecidedSession, boolean isStudentTolerant,
                                String feedback, ArrayList<StudentProficiency> studentProficiencies,
                                int noOfClass, Integer schedule1, int schedule2, String s) {

        FeedbackTutorContentRequest feedbackTutorContentRequest = new FeedbackTutorContentRequest();
        feedbackTutorContentRequest.demoTutorSessionFeedbackId = 0;
        feedbackTutorContentRequest.tutorId = tutorSessionStarted.TutorId;
        feedbackTutorContentRequest.feedbackText = feedback;
        feedbackTutorContentRequest.courseId = selectedCourseId;
        feedbackTutorContentRequest.studentId = tutorSessionStarted.StudentId;
        feedbackTutorContentRequest.rating = null;
        feedbackTutorContentRequest.date = DateTimeUtility.currentDateTime();
        feedbackTutorContentRequest.moduleId = null;
        if (isStudentQualify == 1) {
            feedbackTutorContentRequest.isStudentQualifies = false;
        } else {
            feedbackTutorContentRequest.isStudentQualifies = true;
        }
        feedbackTutorContentRequest.isClassTimeDecided = haveDecidedSession;
        /*feedbackTutorContentRequest.noOfClassPerWeek = noOfClass;
        feedbackTutorContentRequest.scheduleId1 = schedule1;
        feedbackTutorContentRequest.scheduleId2 = schedule2;*/
        feedbackTutorContentRequest.isStudentGotSittingTolerance = isStudentTolerant;
        feedbackTutorContentRequest.isProficientInDiction = returnProficiencyResult(studentProficiencies, "Dictation");
        feedbackTutorContentRequest.isProficientInShruthi = returnProficiencyResult(studentProficiencies, "Shruthi");
        feedbackTutorContentRequest.isProficientInThalam = returnProficiencyResult(studentProficiencies, "Thalam");
        feedbackTutorContentRequest.isProficientInVarisais = returnProficiencyResult(studentProficiencies, "Varisais");
        feedbackTutorContentRequest.isProficientInSwarasthanam = returnProficiencyResult(studentProficiencies, "Swarasthanam");
        feedbackTutorContentRequest.isProficientInAlankaram = returnProficiencyResult(studentProficiencies, "Alankaram");
        feedbackTutorContentRequest.isProficientInGeetham = returnProficiencyResult(studentProficiencies, "Geetham");
        feedbackTutorContentRequest.StudentTutorBookingId = tutorSessionStarted.StudentTutorBookingId;


        mViewModel.postSessionFeedback(App.userDataContract.detail.userId, feedbackTutorContentRequest)
                .observe(getActivity(), defaultResponse -> {
                    Utility.hideLoader();
                    if (defaultResponse.status) {
                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                        TutorHomeActivity.addFragment(new TutorHomeFragment(), Constant.HOME, getActivity());
                    } else {
                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String returnProficiencyResult(ArrayList<StudentProficiency> studentProficiencies, String proficiencyName) {
        for (int i = 0; i < studentProficiencies.size(); i++) {
            if (studentProficiencies.get(i).proficiencyName.contains(proficiencyName)) {
                if (studentProficiencies.get(i).isSelected) return "yes";
                else return "no";

            }
        }
        return null;
    }
}