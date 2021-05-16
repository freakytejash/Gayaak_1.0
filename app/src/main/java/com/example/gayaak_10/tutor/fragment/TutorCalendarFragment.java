package com.example.gayaak_10.tutor.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.common.model.TutorCalendar;
import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.databinding.FragmentTutorCalendarBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.tutor.StudentTutorBookingDataContractList;
import com.example.gayaak_10.model.tutor.StudentsTutorId;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.tutor.adapter.CalendarStudentAdapter;
import com.example.gayaak_10.tutor.adapter.CalendarTutorListAdapter;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.widgets.CustomDialogClass;
import com.example.gayaak_10.widgets.DecisionInterface;
import com.example.gayaak_10.widgets.calendarview.EventDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TutorCalendarFragment extends Fragment implements View.OnClickListener {

    private FragmentTutorCalendarBinding binding;
    List<String> completedList = new ArrayList<>();
    List<String> upcomingList = new ArrayList<>();
    List<String> todaysList = new ArrayList<>();
    List<String> rescheduledList = new ArrayList<>();
    private TutorViewModel tutorScheduleViewModel;
    List<EventDay> events = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    private ArrayList<StudentTutorBookingDataContractList> studentTutorBookingDataContractList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorCalendarBinding.inflate(getLayoutInflater());
        tutorScheduleViewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);

        if (App.tutorCoins!= null){
            binding.tvWalletPoints.setText(Utility.withSuffix(App.tutorCoins));
        }

        /*Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 5);*/

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 2);

        binding.calendar.setMinimumDate(min);
        binding.calendar.setMaximumDate(max);
        //current day
        Calendar calendar1 = Calendar.getInstance();
        events.add(new EventDay(calendar1, R.drawable.circle_filled_gray));

        binding.layoutStudent.setOnClickListener(this);

        tutorScheduleViewModel.getStudentsForCalendar().observe(getActivity(), new Observer<StudentsTutorId>() {
            @Override
            public void onChanged(StudentsTutorId studentsTutorId) {
                if (studentsTutorId != null && studentsTutorId.detail != null && studentsTutorId.detail.studentTutorBookingDataContractList != null) {
                    studentTutorBookingDataContractList = studentsTutorId.detail.studentTutorBookingDataContractList;
                   /* CustomStringSpinnerAdapter customSpinnerAdapter = new CustomStringSpinnerAdapter(getActivity(), studentsTutorId.detail.studentTutorBookingDataContractList,
                            new CustomStringSpinnerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    int id = studentsTutorId.detail.studentTutorBookingDataContractList.get(position).studentId;
                                    getCalendarById(id);
                                }
                            });
                    binding.spinnerStudent.setPrompt(getString(R.string.select_country));
                    binding.spinnerStudent.setAdapter(customSpinnerAdapter);*/
                }
            }
        });
        binding.calendar.setEvents(events);
        return binding.getRoot();
    }

    private void getCalendarById(int studentId) {
        completedList.clear();
        upcomingList.clear();
        todaysList.clear();
        events.clear();
        binding.calendar.setEvents(events);
        tutorScheduleViewModel.getTutorCalendar(App.userDataContract.detail.userId, studentId).observe(getActivity(), new Observer<TutorCalendar>() {
            @Override
            public void onChanged(TutorCalendar tutorCalendar) {
                Log.e("tutorCalendar", "onChanged: " + tutorCalendar.detail.liveClassDataContractList);
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
                            } else {
                                todaysList.add(DateTimeUtility.convertDateTimeFormate(tutorCalendar.detail.liveClassDataContractList.get(i).dateString, "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //   completedList.add(DateTimeUtility.convertDateTimeFormate(tutorCalendar.detail.liveClassDataContractList.get(i).dateString, "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss"));
                    }
                    int totalElements = completedList.size() + upcomingList.size() + todaysList.size();
                    if (totalElements == tutorCalendar.detail.liveClassDataContractList.size()) {
                        for (int i = 0; i < completedList.size(); i++) {
                            Date date = null;
                            try {
                                date = sdf.parse(completedList.get(i));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!events.contains(new EventDay(toCalendar(date)))) {
                                events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_completed));
                            }
                        }
                        for (int i = 0; i < upcomingList.size(); i++) {
                            Date date = null;
                            try {
                                date = sdf.parse(upcomingList.get(i));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!events.contains(new EventDay(toCalendar(date)))) {
                                events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_24_upcoming));

                            }
                        }

                        for (int i = 0; i < todaysList.size(); i++) {
                            Date date = null;
                            try {
                                date = sdf.parse(todaysList.get(i));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!events.contains(new EventDay(toCalendar(date)))) {
                                events.add(new EventDay(toCalendar(date), R.drawable.circle_filled_gray));
                            }
                        }
                        binding.calendar.setEvents(events);
                    }

                    binding.rvTutorScheduleList.setVisibility(View.VISIBLE);
                    binding.layoutEmptySchedule.setVisibility(View.GONE);
                    setScheduleListData(tutorCalendar.detail.liveClassDataContractList);
                } else {
                    binding.rvTutorScheduleList.setVisibility(View.GONE);
                    binding.layoutEmptySchedule.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setScheduleListData(ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList) {
        Utility.hideLoader();
        HashMap<String, ArrayList<TutorCalendarLiveClassDataContractList>> hashMap = new HashMap<>();
        for (int i = 0; i < liveClassDataContractList.size(); i++) {
            if (!hashMap.containsKey(liveClassDataContractList.get(i).dateString)) {
                ArrayList<TutorCalendarLiveClassDataContractList> list = new ArrayList<TutorCalendarLiveClassDataContractList>();
                list.add(liveClassDataContractList.get(i));
                hashMap.put(liveClassDataContractList.get(i).dateString, list);
            } else {
                hashMap.get(liveClassDataContractList.get(i).dateString).add(liveClassDataContractList.get(i));
            }
        }


        binding.rvTutorScheduleList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        CalendarTutorListAdapter calendarTutorListAdapter = new CalendarTutorListAdapter(getActivity(), liveClassDataContractList, hashMap, new CalendarTutorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }

            @Override
            public void onRescheduleSession(int adapterPosition) {
                CustomDialogClass cdd = new CustomDialogClass(getActivity(), new DecisionInterface() {
                    @Override
                    public void decisionYes() {
                        Utility.showLoader(getActivity(), false);
                        tutorScheduleViewModel.manageReschedule(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                liveClassDataContractList.get(adapterPosition).tutorId).observe(getActivity(), new Observer<DefaultResponse>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onChanged(DefaultResponse defaultResponse) {
                                if (defaultResponse != null) {
                                    Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                    if (defaultResponse.status) {
                                        Utility.customDialogBoxTextWithSingle(getActivity(), "Issue raised", "Our support team would contact you for rescheduling.");
                                        //  Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                        tutorScheduleViewModel.manageReschedule(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                                liveClassDataContractList.get(adapterPosition).tutorId)
                                                .removeObservers(getActivity());
                                        /*if (!liveClassDataContractList.isEmpty()) {
                                            tutorScheduleViewModel.manageReschedule(liveClassDataContractList.get(adapterPosition).TutorScheduleId,
                                                    liveClassDataContractList.get(adapterPosition).tutorId)
                                                    .removeObservers(getActivity());
                                        //    liveClassDataContractList.remove(liveClassDataContractList.get(adapterPosition));
                                            setScheduleListData(liveClassDataContractList);
                                        }*/
                                    }
                                } else {
                                    tutorScheduleViewModel.manageReschedule(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                            liveClassDataContractList.get(adapterPosition).tutorId)
                                            .removeObservers(getActivity());
                                    Utility.hideLoader();
                                }
                            }
                        });
                    }

                    @Override
                    public void decisionNo() {

                    }
                }, getString(R.string.sure_reschedule_session));
                cdd.show();
            }

            @Override
            public void onDeleteSession(int adapterPosition) {
                CustomDialogClass cdd = new CustomDialogClass(getActivity(), new DecisionInterface() {
                    @Override
                    public void decisionYes() {
                        Utility.showLoader(getActivity(), false);
                        tutorScheduleViewModel.cancelSession(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                liveClassDataContractList.get(adapterPosition).tutorId).observe(getActivity(), new Observer<DefaultResponse>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onChanged(DefaultResponse defaultResponse) {
                                if (defaultResponse != null) {
                                    Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                    if (defaultResponse.status) {
                                        if (!liveClassDataContractList.isEmpty()) {
                                            tutorScheduleViewModel.cancelSession(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                                    liveClassDataContractList.get(adapterPosition).tutorId)
                                                    .removeObservers(getActivity());
                                            liveClassDataContractList.remove(liveClassDataContractList.get(adapterPosition));
                                            setScheduleListData(liveClassDataContractList);
                                        }
                                    }
                                } else {
                                    tutorScheduleViewModel.cancelSession(liveClassDataContractList.get(adapterPosition).tutorScheduleId,
                                            liveClassDataContractList.get(adapterPosition).tutorId);
                                    Utility.hideLoader();
                                }
                            }
                        });
                    }

                    @Override
                    public void decisionNo() {

                    }
                }, getString(R.string.are_you_sure_you_want_to_remove_this_class));
                cdd.show();
            }
        });
        binding.rvTutorScheduleList.setAdapter(calendarTutorListAdapter);
    }

    public Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutStudent:
                if (!studentTutorBookingDataContractList.isEmpty()) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    PopupWindow popup = new PopupWindow(getActivity());
                    View layout = inflater.inflate(R.layout.popup_tutor_student, null);
                    popup.setContentView(layout);
                    RecyclerView rvSlotTime = (RecyclerView) layout.findViewById(R.id.rvSlotTime);
                    rvSlotTime.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    ArrayList<StudentTutorBookingDataContractList> studentTutorBookingList = new ArrayList<>();
                    for (int i = 0; i < studentTutorBookingDataContractList.size(); i++) {
                        if (!studentTutorBookingDataContractList.get(i).studentName.matches(" ")) {
                            studentTutorBookingList.add(studentTutorBookingDataContractList.get(i));
                        }
                    }
                    CalendarStudentAdapter adapter = new CalendarStudentAdapter(getActivity(), studentTutorBookingList, new CalendarStudentAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClickListener(StudentTutorBookingDataContractList scheduleSlotDetail) {
                            popup.dismiss();
                            events.clear();
                            binding.calendar.refreshDrawableState();
                            binding.calendar.setEvents(events);
                            binding.rvTutorScheduleList.setVisibility(View.GONE);
                            binding.layoutEmptySchedule.setVisibility(View.GONE);
                            binding.tvStudentHeader.setText(scheduleSlotDetail.studentName);
                            getCalendarById(scheduleSlotDetail.studentId);
                        }
                    });
                    rvSlotTime.setAdapter(adapter);
                    Display display = getActivity().getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    popup.setWidth(width - 50);
                    popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    //popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                    // Closes the popup window when touch outside of it - when looses focus
                    popup.setOutsideTouchable(true);
                    popup.setFocusable(true);
                    // Show anchored to button
                    popup.showAsDropDown(view);
                } else {
                    Utility.customDialogBoxTextWithSingle(getActivity(), "", "There are no students registered yet.");
                }
                break;
        }
    }
}

