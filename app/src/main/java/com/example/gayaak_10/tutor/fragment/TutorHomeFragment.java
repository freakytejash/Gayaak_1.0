package com.example.gayaak_10.tutor.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorHomeBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.model.response.UserDataProfile;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.tutor.adapter.TutorSessionAdapter;
import com.example.gayaak_10.tutor.adapter.TutorTodaySessionAdapter;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.tutor.model.TutorDashboard;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;
import com.example.gayaak_10.zoom.ui.OpenZoomViewActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TutorHomeFragment extends Fragment {

    private FragmentTutorHomeBinding binding;
    private TutorViewModel viewModel;
    private ArrayList<TodaySessions> upcomingSessionList = new ArrayList<>();
    private ArrayList<TodaySessions> todaySessionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentTutorHomeBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);

        viewModel.getDashboard().observe(getActivity(), new Observer<TutorDashboard>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(TutorDashboard tutorDashboard) {
                setDashboard(tutorDashboard);

            }
        });

        binding.layoutUnreadMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private void setDashboard(TutorDashboard tutorDashboard) {
        if (tutorDashboard != null && tutorDashboard.detail != null) {
            binding.tvTotalSessions.setText("" + tutorDashboard.detail.noOfSession);
            binding.tvTotalStudent.setText("" + tutorDashboard.detail.noOfStudent);
            binding.tvTotalCoins.setText("" + tutorDashboard.detail.totalCoin);
            binding.ivPoints.setText(""+tutorDashboard.detail.totalCoin);
            App.tutorCoins = tutorDashboard.detail.totalCoin;


        }
        if (tutorDashboard.detail.todaySessionList != null) {
            // create two list one with today's session and other with upcoming one
            todaySessionList.clear();
            upcomingSessionList.clear();
            for (int i = 0; i < tutorDashboard.detail.todaySessionList.size(); i++) {
                try {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = sdf.parse(tutorDashboard.detail.todaySessionList.get(i).Date);
                        Date date2 = sdf.parse(DateTimeUtility.currentDateTime("yyyy-MM-dd"));


                        System.out.println("date1 : " + sdf.format(date1));
                        System.out.println("date2 : " + sdf.format(date2));

                        if (date1.compareTo(date2) > 0) {
                            System.out.println("Date1 is after Date2");
                            upcomingSessionList.add(tutorDashboard.detail.todaySessionList.get(i));;
                        } else if (date1.compareTo(date2) < 0) {
                            System.out.println("Date1 is before Date2");
                            upcomingSessionList.add(tutorDashboard.detail.todaySessionList.get(i));;
                        } else if (date1.compareTo(date2) == 0) {
                            System.out.println("Date1 is equal to Date2");
                            todaySessionList.add(tutorDashboard.detail.todaySessionList.get(i));
                        } else {
                            System.out.println("How to get here?");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (i == tutorDashboard.detail.todaySessionList.size()){
                    setUpcomingList(upcomingSessionList);
                    setTodayList(todaySessionList);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setUpcomingList(upcomingSessionList);
            setTodayList(todaySessionList);
            binding.layoutEmptySchedule.setVisibility(View.GONE);
        } else {
            binding.layoutUpcomingClasses.setVisibility(View.GONE);
            binding.layoutTodaysSession.setVisibility(View.GONE);
            binding.layoutEmptySchedule.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpcomingList(ArrayList<TodaySessions> upcomingList) {
        Utility.hideLoader();
        if (!upcomingList.isEmpty()) {
            binding.layoutUpcomingClasses.setVisibility(View.VISIBLE);
            binding.rvDemoUpcomingSession.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            TutorSessionAdapter tutorSessionAdapter = new TutorSessionAdapter(getActivity(), upcomingList, new TutorSessionAdapter.OnItemClickListener() {
                @Override
                public void onSessionReschedule(int position) {
                    // session reschedule
                    rescheduleSession(upcomingList.get(position).TutorScheduleId, App.userDataContract.detail.userId, upcomingList, position, "upcoming");
                }

                @Override
                public void onSessionCancel(int position) {
                    cancelSession(upcomingList.get(position).TutorScheduleId, App.userDataContract.detail.userId, upcomingList, position, "upcoming");
                }

            });
            binding.rvDemoUpcomingSession.setAdapter(tutorSessionAdapter);
        } else {
            binding.layoutUpcomingClasses.setVisibility(View.GONE);
        }
    }

    private void rescheduleSession(Integer tutorScheduleId, int userId, ArrayList<TodaySessions> upcomingList, int position, String sessionType) {
        new MaterialAlertDialogBuilder(getActivity())
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you want to reschedule this session?")
                .setPositiveButton("Yes", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            Utility.showLoader(getActivity(), false);
                            viewModel.manageReschedule(tutorScheduleId, userId).observe(getActivity(), new Observer<DefaultResponse>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onChanged(DefaultResponse defaultResponse) {
                                    viewModel.manageReschedule(tutorScheduleId, userId)
                                            .removeObservers(getActivity());
                                    if (defaultResponse != null) {
                                        Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        })
                ).setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.dismiss()))
                .show();

    }

    private void cancelSession(Integer tutorScheduleId, int userId, ArrayList<TodaySessions> upcomingList, int position, String sessionType) {
        new MaterialAlertDialogBuilder(getActivity())
                .setIcon(R.drawable.gayaak_icon)
                .setMessage("Are you sure you to cancel this session?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Utility.showLoader(getActivity(), false);
                    viewModel.cancelSession(tutorScheduleId, userId).observe(getActivity(), new Observer<DefaultResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onChanged(DefaultResponse defaultResponse) {
                            if (defaultResponse != null) {
                                viewModel.cancelSession(tutorScheduleId, userId)
                                        .removeObservers(getActivity());
                                Toast.makeText(getActivity(), defaultResponse.message, Toast.LENGTH_SHORT).show();
                                Utility.hideLoader();
                                if (defaultResponse.status) {
                                    Utility.hideLoader();
                                    if (sessionType.equalsIgnoreCase("today")) {
                                        if (!upcomingList.isEmpty()) {
                                            upcomingList.remove(upcomingList.get(position));
                                            setTodayList(upcomingList);
                                        }
                                    } else if (sessionType.equalsIgnoreCase("upcoming")) {
                                        if (!upcomingList.isEmpty()) {
                                            upcomingList.remove(upcomingList.get(position));
                                            setUpcomingList(upcomingList);
                                        }
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
    private void setTodayList(ArrayList<TodaySessions> todayList) {
        Utility.hideLoader();
        if (!todayList.isEmpty()) {
            binding.layoutTodaysSession.setVisibility(View.VISIBLE);
            binding.rvTodaySession.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            TutorTodaySessionAdapter tutorSessionAdapter = new TutorTodaySessionAdapter(getActivity(), todayList, new TutorTodaySessionAdapter.OnItemClickListener() {

                @Override
                public void onSessionReschedule(int position) {
                    rescheduleSession(todayList.get(position).TutorScheduleId, App.userDataContract.detail.userId, todayList, position, "today");
                }

                @Override
                public void onSessionCancel(int position) {

                }

                @Override
                public void onStartClass(int position) {

                    viewModel.postEmailNotification(todayList.get(position).TutorId,
                            todayList.get(position).liveClassDetailId).observe(getActivity(), new Observer<DefaultResponse>() {
                        @Override
                        public void onChanged(DefaultResponse defaultResponse) {
                            Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    App.tutorSessionStarted = todayList.get(position);
                    /*Uri uri = Uri.parse(todayList.get(position).HostLink);
                    String path = uri.getPath(); // /s/327837283728
                    String meetingId = path.substring(path.lastIndexOf('/') + 1);*/
                    Constant.meetingNo = todayList.get(position).ZoomMeetingId;

                    String passwordFromString = todayList.get(position).hostLink;
                    String password = passwordFromString.substring(passwordFromString.lastIndexOf("pwd=")+4);

                    if (password.equalsIgnoreCase("")){
                        Constant.meetingPassword = todayList.get(position).ZoomMeetingPassword;
                    }
                    else {
                        Constant.meetingPassword = password;
                    }
                   // Constant.meetingPassword = todayList.get(position).ZoomMeetingPassword;
                    App.liveClassId = todayList.get(position).LiveClassTypeId;
                    App.StudentName = todayList.get(position).StudentName;
                    if (!Constant.meetingNo.isEmpty()){
                        Intent intent = new Intent(getActivity(), OpenZoomViewActivity.class);
                        intent.putExtra("classTypeId", todayList.get(position).LiveClassTypeId);
                        intent.putExtra("tutorSessionInfo", todayList.get(position));
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit);
                    }else {
                        Toast.makeText(getActivity(), "Please contact support for session details", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            binding.rvTodaySession.setAdapter(tutorSessionAdapter);
        } else {
            binding.layoutTodaysSession.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setProfileData(UserDataProfile userData) {
        if (userData.detail != null) {
            if (!userData.detail.firstName.isEmpty()) {
                binding.tvName.setText("Hi " + userData.detail.firstName);
            } else if (!userData.detail.lastName.isEmpty()) {
                binding.tvName.setText("Hi " + userData.detail.lastName);
            } else {
                binding.tvName.setText("Hi " + userData.detail.email);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfileData(App.userDataContract);
    }
}
