package com.example.gayaak_10.student.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.model.TutorCalendarCompleteList;
import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentStudentCalendarBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.activity.StudentHomeActivity;
import com.example.gayaak_10.student.adapter.CalendarStudentListAdapter;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.SharedPrefsUtil;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.widgets.CustomDialogClass;
import com.example.gayaak_10.widgets.DecisionInterface;
import com.example.gayaak_10.widgets.calendarview.EventDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentCalendarFragment extends Fragment {

    private FragmentStudentCalendarBinding binding;
    List<String> completedList = new ArrayList<>();
    List<String> upcomingList = new ArrayList<>();
    List<String> refillList = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    private StudentViewModel viewModel;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentCalendarBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(StudentViewModel.class);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 2);

        binding.calendar.setMinimumDate(min);
        binding.calendar.setMaximumDate(max);
        //current day
        Calendar calendar1 = Calendar.getInstance();
        events.add(new EventDay(calendar1, R.drawable.circle_filled_gray));


        viewModel.getStudentCalendar(App.userDataContract.detail.userId).observe(getActivity(), tutorCalendar -> {

            if (tutorCalendar.detail != null && tutorCalendar.detail.transactionDetailDataContractLists != null) {
                for (int i = 0; i < tutorCalendar.detail.transactionDetailDataContractLists.size(); i++) {
                    refillList.add(DateTimeUtility.convertDateTimeFormate(tutorCalendar.detail.transactionDetailDataContractLists.get(i).CreditDateAndTime, "yyyy-mm-dd'T'hh:mm:ss", "dd-M-yyyy hh:mm:ss"));
                }

                if (refillList.size() == tutorCalendar.detail.transactionDetailDataContractLists.size()) {
                    for (int i = 0; i < refillList.size(); i++) {
                        Date date = null;
                        try {
                            date = sdf.parse(refillList.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_yellow));
                    }

                    for (int i = 0; i < upcomingList.size(); i++) {
                        Date date = null;
                        try {
                            date = sdf.parse(upcomingList.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_upcoming));
                    }
                    binding.calendar.setEvents(events);
                }
            }

            if (tutorCalendar.detail != null && tutorCalendar.detail.liveClassDataContractList != null) {
                for (int i = 0; i < tutorCalendar.detail.liveClassDataContractList.size(); i++) {

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        Date date1 = sdf.parse(tutorCalendar.detail.liveClassDataContractList.get(i).dateString);
                        Date date2 = sdf.parse(DateTimeUtility.currentDateTime("MM/dd/yyyy"));

                        if (date1.before(date2)) {
                            completedList.add(DateTimeUtility.convertDateTimeFormate(tutorCalendar.detail.liveClassDataContractList.get(i).dateString, "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss"));
                        } else if (date1.after(date2)) {
                            upcomingList.add(DateTimeUtility.convertDateTimeFormate(tutorCalendar.detail.liveClassDataContractList.get(i).dateString, "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                int totalElements = completedList.size() + upcomingList.size();
                if (totalElements == tutorCalendar.detail.liveClassDataContractList.size()) {
                    for (int i = 0; i < completedList.size(); i++) {
                        Date date = null;
                        try {
                            date = sdf.parse(completedList.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_completed));
                    }

                    for (int i = 0; i < upcomingList.size(); i++) {
                        Date date = null;
                        try {
                            date = sdf.parse(upcomingList.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_upcoming));
                    }
                    binding.calendar.setEvents(events);
                }

            }
            ArrayList<TutorCalendarCompleteList> list = new ArrayList<>();
            if (tutorCalendar.detail.liveClassDataContractList != null && tutorCalendar.detail.transactionDetailDataContractLists != null) {
                binding.rvStudentScheduleList.setVisibility(View.VISIBLE);
                binding.layoutEmptySchedule.setVisibility(View.GONE);
                for (int i = 0; i < tutorCalendar.detail.liveClassDataContractList.size(); i++) {
                    list.add(new TutorCalendarCompleteList("session",
                            tutorCalendar.detail.liveClassDataContractList.get(i), null));

                    if (list.size() == tutorCalendar.detail.liveClassDataContractList.size()) {
                        for (int j = 0; j < tutorCalendar.detail.transactionDetailDataContractLists.size(); j++) {
                            list.add(new TutorCalendarCompleteList("refill",
                                    null, tutorCalendar.detail.transactionDetailDataContractLists.get(j)));
                            int totalSize = tutorCalendar.detail.liveClassDataContractList.size() + tutorCalendar.detail.transactionDetailDataContractLists.size();
                            if (list.size() == totalSize) {
                                setList(list);
                            }
                        }
                    }
                }
            } else if (tutorCalendar.detail.liveClassDataContractList != null && tutorCalendar.detail.transactionDetailDataContractLists == null) {
                binding.rvStudentScheduleList.setVisibility(View.VISIBLE);
                binding.layoutEmptySchedule.setVisibility(View.GONE);
                for (int i = 0; i < tutorCalendar.detail.liveClassDataContractList.size(); i++) {
                    list.add(new TutorCalendarCompleteList("session",
                            tutorCalendar.detail.liveClassDataContractList.get(i), null));

                    if (list.size() == tutorCalendar.detail.liveClassDataContractList.size()) {
                        setList(list);
                    }
                }
            } else if (tutorCalendar.detail.liveClassDataContractList == null && tutorCalendar.detail.transactionDetailDataContractLists != null) {
                binding.rvStudentScheduleList.setVisibility(View.VISIBLE);
                binding.layoutEmptySchedule.setVisibility(View.GONE);
                for (int j = 0; j < tutorCalendar.detail.transactionDetailDataContractLists.size(); j++) {
                    list.add(new TutorCalendarCompleteList("refill",
                            null, tutorCalendar.detail.transactionDetailDataContractLists.get(j)));
                    if (list.size() == tutorCalendar.detail.transactionDetailDataContractLists.size()) {
                        setList(list);
                    }
                }
            } else {
                binding.rvStudentScheduleList.setVisibility(View.GONE);
                binding.layoutEmptySchedule.setVisibility(View.VISIBLE);
            }
        });

        binding.calendar.setEvents(events);


        return binding.getRoot();
    }

    private void setList(ArrayList<TutorCalendarCompleteList> list) {
        binding.rvStudentScheduleList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CalendarStudentListAdapter adapter = new CalendarStudentListAdapter(getActivity(), list, new CalendarStudentListAdapter.OnItemClickListener() {
            @Override
            public void onRescheduleSession(TutorCalendarLiveClassDataContractList liveClassData) {
                CustomDialogClass cdd = new CustomDialogClass(getActivity(), new DecisionInterface() {
                    @Override
                    public void decisionYes() {
                        String userType = SharedPrefsUtil.getStringPreferences(getActivity(), "UserType");
                        StudentHomeActivity.addFragment(new StudentBookDemoCourseFragment(userType,
                                        liveClassData.tutorId,
                                        liveClassData.TutorScheduleId, true),
                                Constant.COURSE_CATALOG, getActivity());
                    }

                    @Override
                    public void decisionNo() {

                    }
                }, getString(R.string.sure_reschedule_session));
                cdd.show();
            }

            @Override
            public void onDeleteSession(TutorCalendarLiveClassDataContractList liveClassData) {
                CustomDialogClass cdd = new CustomDialogClass(getActivity(), new DecisionInterface() {
                    @Override
                    public void decisionYes() {
                        Utility.showLoader(getActivity(), true);
                        viewModel.cancelSessionStudent(liveClassData.TutorScheduleId, liveClassData.studentId)
                                .observe(getActivity(), defaultResponse -> {
                                    if (defaultResponse != null) {
                                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                        if (defaultResponse.status) {
                                            Utility.hideLoader();
                                            if (!list.isEmpty()) {
                                                viewModel.cancelSessionStudent(liveClassData.TutorScheduleId, liveClassData.studentId)
                                                        .removeObservers(getActivity());
                                                list.remove(liveClassData);
                                                setList(list);
                                                Utility.hideLoader();
                                            } else {
                                                Utility.hideLoader();
                                                binding.rvStudentScheduleList.setVisibility(View.GONE);
                                                binding.layoutEmptySchedule.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else Utility.hideLoader();
                                });
                    }

                    @Override
                    public void decisionNo() {

                    }
                }, getString(R.string.are_you_sure_you_want_to_remove_this_class));
                cdd.show();
            }

            @Override
            public void onRefillWallet() {
                StudentHomeActivity.addFragment(new CoursePlansFragment(""), Constant.PROFILE, getActivity());
            }

        });
        binding.rvStudentScheduleList.setAdapter(adapter);
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
