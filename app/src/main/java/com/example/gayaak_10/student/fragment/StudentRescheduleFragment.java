package com.example.gayaak_10.student.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentRescheduleBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.student.model.request.DemoTutorRequest;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.adapter.CourseCategoryTypeAdapter;
import com.example.gayaak_10.tutor.adapter.CourseLevelTypeAdapter;
import com.example.gayaak_10.tutor.adapter.recommended.RegularRescheduleTimeAdapter;
import com.example.gayaak_10.tutor.adapter.recommended.RegularScheduleTimeAdapter;
import com.example.gayaak_10.tutor.adapter.recommended.RegularSessionsScheduleAdapter;
import com.example.gayaak_10.tutor.model.TutorSchedule;
import com.example.gayaak_10.tutor.model.TutorScheduleDataContractList;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class StudentRescheduleFragment extends Fragment implements View.OnClickListener {

    private FragmentStudentRescheduleBinding binding;
    private StudentViewModel viewModel;
    private ArrayList<TutorScheduleDataContractList> tutorAvailableSchedule = new ArrayList<>();
    private ArrayList<String> selectedTimeList = new ArrayList<>();
    private TutorScheduleDataContractList tutorAvailableDetail;
    private ArrayList<String> availableDay = new ArrayList<>();
    private ArrayList<String> availableTime = new ArrayList<>();
    private LiveClassDataContractList liveClassDataContractList;
    private TutorCalendarLiveClassDataContractList tutorCalendarLiveClassDataContractList;
    private HashMap<String, ArrayList<String>> hashMap;
    private Fragment fragmentName = null;

    public StudentRescheduleFragment(String userType, TutorCalendarLiveClassDataContractList tutorCalendarLiveClassDataContractList) {
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
        this.tutorCalendarLiveClassDataContractList= tutorCalendarLiveClassDataContractList;
    }

    public StudentRescheduleFragment (String userType, LiveClassDataContractList liveClassDataContractList){
        if (userType.equalsIgnoreCase("Free")) {
            fragmentName = new StudentDemoHomeFragment();
        } else if (userType.equalsIgnoreCase("Paid")) {
            fragmentName = new StudentRegularFragment();
        }
        this.liveClassDataContractList = liveClassDataContractList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentStudentRescheduleBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);
        /*availableDay.add("Select Day");
        availableTime.add("Select Time");*/
        binding.btnReSchedule.setOnClickListener(this);
        binding.layoutWeekClass1.setOnClickListener(this);
        binding.layoutTimeClass1.setOnClickListener(this);

        Utility.showLoader(getContext(),false);
        if (liveClassDataContractList!=null){
            viewModel.getTutorAvailabilityForReschedule(liveClassDataContractList.tutorId).observe(this, tutorSchedule -> {

                        if (tutorSchedule.detail.tutorScheduleDataContractList!=null && !tutorSchedule.detail.tutorScheduleDataContractList.isEmpty()){
                            for (int i=0; i<tutorSchedule.detail.tutorScheduleDataContractList.size();i++){
                                if (tutorSchedule.detail.tutorScheduleDataContractList.get(i).IsScheduleAvailable == 1)
                                {
                                    tutorAvailableSchedule.add(tutorSchedule.detail.tutorScheduleDataContractList.get(i));

                                }
                            }

                            ArrayList<String> dateList = new ArrayList<>();
                            hashMap = new HashMap<>();
                            dateList.add("Monday");
                            dateList.add("Tuesday");
                            dateList.add("Wednesday");
                            dateList.add("Thursday");
                            dateList.add("Friday");
                            dateList.add("Saturday");
                            dateList.add("Sunday");
                            for (int i = 0; i < dateList.size(); i++) {
                                String day = dateList.get(i);
                                for (int j = 0; j < tutorAvailableSchedule.size(); j++) {
                                    if (day.equalsIgnoreCase(tutorAvailableSchedule.get(j).day)) {
                                        availableTime.add(tutorAvailableSchedule.get(j).time);
                                    }
                                    if (j == tutorAvailableSchedule.size() - 1 && availableTime.size() != 0) {
                                        ArrayList<String> tempList = new ArrayList<>(availableTime);
                                        hashMap.put(day, tempList);
                                        availableTime.clear();
                                        break;
                                    }
                                }
                            }
/*
                Set<String> set = new HashSet<>(availableDay);
                availableDay.clear();
                availableDay.addAll(set);
                if (availableDay.size()<0){
                    availableDay.add("No Day Available");
                }*/
                            Utility.hideLoader();
                        }
                    }
            );
            binding.tvTutorName.setText(""+liveClassDataContractList.tutorName);
            binding.tvCourseLevel.setText(""+liveClassDataContractList.LevelName);
            binding.tvCourseName.setText(""+liveClassDataContractList.courseName);
            Glide.with(getContext()).load(liveClassDataContractList.TutorProfileImage)
                    .placeholder(getContext().getDrawable(R.drawable.ic_profile))
                    .into(binding.ivTutorImage);
        }else if (tutorCalendarLiveClassDataContractList!=null){
            viewModel.getTutorAvailabilityForReschedule(tutorCalendarLiveClassDataContractList.tutorId).observe(this, tutorSchedule -> {

                        if (tutorSchedule.detail.tutorScheduleDataContractList!=null && !tutorSchedule.detail.tutorScheduleDataContractList.isEmpty()){
                            for (int i=0; i<tutorSchedule.detail.tutorScheduleDataContractList.size();i++){
                                if (tutorSchedule.detail.tutorScheduleDataContractList.get(i).IsScheduleAvailable == 1)
                                {
                                    tutorAvailableSchedule.add(tutorSchedule.detail.tutorScheduleDataContractList.get(i));

                                }
                            }

                            ArrayList<String> dateList = new ArrayList<>();
                            hashMap = new HashMap<>();
                            dateList.add("Monday");
                            dateList.add("Tuesday");
                            dateList.add("Wednesday");
                            dateList.add("Thursday");
                            dateList.add("Friday");
                            dateList.add("Saturday");
                            dateList.add("Sunday");
                            for (int i = 0; i < dateList.size(); i++) {
                                String day = dateList.get(i);
                                for (int j = 0; j < tutorAvailableSchedule.size(); j++) {
                                    if (day.equalsIgnoreCase(tutorAvailableSchedule.get(j).day)) {
                                        availableTime.add(tutorAvailableSchedule.get(j).time);
                                    }
                                    if (j == tutorAvailableSchedule.size() - 1 && availableTime.size() != 0) {
                                        ArrayList<String> tempList = new ArrayList<>(availableTime);
                                        hashMap.put(day, tempList);
                                        availableTime.clear();
                                        break;
                                    }
                                }
                            }

                            Utility.hideLoader();
                        }
                    }
            );
            binding.tvTutorName.setText(""+tutorCalendarLiveClassDataContractList.tutorName);
            binding.tvCourseLevel.setText(""+tutorCalendarLiveClassDataContractList.levelName);
            binding.tvCourseName.setText(""+tutorCalendarLiveClassDataContractList.categoryName);
            Glide.with(getContext()).load(tutorCalendarLiveClassDataContractList.tutorProfileImage)
                    .placeholder(getContext().getDrawable(R.drawable.ic_profile))
                    .into(binding.ivTutorImage);
        }

        return binding.getRoot();
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnCancel) {
            StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment("Free"), Constant.COURSE_CATALOG, getActivity());
        }

        if (view.getId() == R.id.btnReSchedule) {
            reScheduleSession();
        }

        if (view.getId() == R.id.layoutWeekClass1){
            openDatePopup(view, getContext(), hashMap, binding.tvDayName1);
        }

        if (view.getId() == R.id.layoutTimeClass1){
            openTimePopup(view, getContext(), selectedTimeList, binding.tvClassTime1);
        }

    }

    private void reScheduleSession() {
        /*if (binding.spDayName.getSelectedItem().toString().equalsIgnoreCase(availableDay.get(0))
                || binding.spClassTime.getSelectedItem().toString().equalsIgnoreCase(availableTime.get(0)))
        {
            if (binding.spClassTime.getSelectedItem().toString().equalsIgnoreCase(availableTime.get(0))){
                Toast.makeText(getContext(), "Please Setect the Time", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "Please Select the Day", Toast.LENGTH_LONG).show();
            }
        }
        else */
        {
            for (int i=0; i<tutorAvailableSchedule.size();i++){

                if (binding.tvDayName1.getText().toString().equalsIgnoreCase(tutorAvailableSchedule.get(i).day)
                        && binding.tvClassTime1.getText().toString().equalsIgnoreCase(tutorAvailableSchedule.get(i).time)){
                    tutorAvailableDetail = tutorAvailableSchedule.get(i);
                    if(liveClassDataContractList!=null){
                        createSession(tutorAvailableSchedule.get(i), liveClassDataContractList);
                    }else if (tutorCalendarLiveClassDataContractList!=null){
                        createSessionCalender(tutorAvailableSchedule.get(i), tutorCalendarLiveClassDataContractList);
                    }

                    break;
                }

            }
        }

    }

    private void createSessionCalender(TutorScheduleDataContractList tutorScheduleDataContractList, TutorCalendarLiveClassDataContractList tutorCalendarLiveClassDataContractList) {
        Utility.showLoader(getContext(), false);
        int demoFeedbackId=SharedPrefsUtil.getIntegerPreferences(getContext(),"demoTutorSessionFeedbackId");
        DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
        demoTutorRequest.ScheduleId = tutorScheduleDataContractList.scheduleId;
        demoTutorRequest.StudentTutorBookingId = tutorCalendarLiveClassDataContractList.studentTutorBookingId;
        demoTutorRequest.TutorId = tutorCalendarLiveClassDataContractList.tutorId;
        demoTutorRequest.TutorScheduleId = String.valueOf(tutorScheduleDataContractList.tutorScheduleId);
        demoTutorRequest.CourseId = Integer.parseInt(tutorCalendarLiveClassDataContractList.courseId);
       /* if (isRescheduled){
            demoTutorRequest.liveClassDetailId = String.valueOf(App.selectedSessionDetail.liveClassDetailId);
        }*/
        String day=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            day = DateTimeUtility.getDateFromWeekDay(tutorScheduleDataContractList.day);
        }
        demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(day, "dd MMMM yyyy", "yyyy-MM-dd") + " " + tutorScheduleDataContractList.time;
        demoTutorRequest.levelid = String.valueOf(tutorCalendarLiveClassDataContractList.levelId);
        demoTutorRequest.liveclasstype = 2;
        demoTutorRequest.classTypeId = 2;
        demoTutorRequest.liveClassDetailId=String.valueOf(tutorCalendarLiveClassDataContractList.liveclassdetailId);
        demoTutorRequest.categoryid = tutorCalendarLiveClassDataContractList.categoryId;
        demoTutorRequest.noOfSession = null;
       /* demoTutorRequest.demoTutorSessionFeedbackId=demoFeedbackId;
        demoTutorRequest.isPaymentComplete=null;*/

        viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
                Toast.makeText(getContext(), "" + defaultResponse.message, Toast.LENGTH_LONG).show();
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

    private void openTimePopup(View view, Context mContext, ArrayList<String> list, AppCompatTextView tvClassTime) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(mContext);
        assert inflater != null;
        View layout = inflater.inflate(R.layout.popup_session_schedule, null);
        popup.setContentView(layout);
        RecyclerView rvDates = (RecyclerView) layout.findViewById(R.id.rvDates);
        rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        RegularRescheduleTimeAdapter adapter = new RegularRescheduleTimeAdapter(mContext, list, data -> {
            popup.dismiss();
            if (!binding.tvClassTime1.getText().toString().isEmpty()) {
                if (binding.tvClassTime1.getText().toString().equalsIgnoreCase(data)) {
                    Toast.makeText(mContext, "Already selected. Select any other time", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            tvClassTime.setText(data);
        });
        rvDates.setAdapter(adapter);

        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.showAsDropDown(view);
    }

    private void openDatePopup(View view, Context mContext, HashMap<String, ArrayList<String>> hashMap, AppCompatTextView tvDayName) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popup = new PopupWindow(mContext);
        assert inflater != null;
        View layout = inflater.inflate(R.layout.popup_session_schedule, null);
        popup.setContentView(layout);
        RecyclerView rvDates = layout.findViewById(R.id.rvDates);
        rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        ArrayList<String> dateList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> graph : hashMap.entrySet()) {
            dateList.add(graph.getKey());
        }

        RegularSessionsScheduleAdapter adapter = new RegularSessionsScheduleAdapter(mContext, dateList, day -> {
            popup.dismiss();
            selectedTimeList.clear();
            selectedTimeList.addAll(Objects.requireNonNull(hashMap.get(day)));
            tvDayName.setText(day);

        });
        rvDates.setAdapter(adapter);

        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.showAsDropDown(view);
    }


    private void createSession(TutorScheduleDataContractList tutorScheduleDataContractList, LiveClassDataContractList liveClassDataContractList) {
        Utility.showLoader(getContext(), false);
        int demoFeedbackId=SharedPrefsUtil.getIntegerPreferences(getContext(),"demoTutorSessionFeedbackId");
        DemoTutorRequest demoTutorRequest = new DemoTutorRequest();
        demoTutorRequest.ScheduleId = tutorScheduleDataContractList.scheduleId;
        demoTutorRequest.StudentTutorBookingId = liveClassDataContractList.StudentTutorBookingId;
        demoTutorRequest.TutorId = liveClassDataContractList.tutorId;
        demoTutorRequest.TutorScheduleId = String.valueOf(tutorScheduleDataContractList.tutorScheduleId);
        demoTutorRequest.CourseId = Integer.parseInt(liveClassDataContractList.courseId);
       /* if (isRescheduled){
            demoTutorRequest.liveClassDetailId = String.valueOf(App.selectedSessionDetail.liveClassDetailId);
        }*/
        String day=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            day = DateTimeUtility.getDateFromWeekDay(tutorScheduleDataContractList.day);
        }
        demoTutorRequest.Date = DateTimeUtility.convertDateTimeFormate(day, "dd MMMM yyyy", "yyyy-MM-dd") + " " + tutorScheduleDataContractList.time;
        demoTutorRequest.levelid = String.valueOf(liveClassDataContractList.levelId);
        demoTutorRequest.liveclasstype = 2;
        demoTutorRequest.classTypeId = 2;
        demoTutorRequest.liveClassDetailId=String.valueOf(liveClassDataContractList.liveclassdetailId);
        demoTutorRequest.categoryid = liveClassDataContractList.categoryId;
        demoTutorRequest.noOfSession = null;
       /* demoTutorRequest.demoTutorSessionFeedbackId=demoFeedbackId;
        demoTutorRequest.isPaymentComplete=null;*/

        viewModel.postBookingDemo(App.userDataContract.detail.userId, demoTutorRequest).observe(getActivity(), new Observer<DefaultResponse>() {
            @Override
            public void onChanged(DefaultResponse defaultResponse) {
                Utility.hideLoader();
                Toast.makeText(getContext(), "" + defaultResponse.message, Toast.LENGTH_LONG).show();
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