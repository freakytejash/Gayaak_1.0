package com.example.gayaak_10.tutor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.FragmentTutorSchedulerBinding;
import com.example.gayaak_10.model.response.DefaultResponse;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.tutor.activity.TutorHomeActivity;
import com.example.gayaak_10.tutor.adapter.TutorSessionScheduleAdapter;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;
import com.example.gayaak_10.tutor.model.ScheduleSlotTimeTemp;
import com.example.gayaak_10.tutor.viewmodel.TutorViewModel;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;

public class TutorSchedulerFragment extends Fragment implements View.OnClickListener {

    private FragmentTutorSchedulerBinding binding;
    private TutorViewModel tutorScheduleViewModel;
    private String selectedSchedules = "";
    ArrayList<ScheduleSlotDetail> timeArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTutorSchedulerBinding.inflate(getLayoutInflater());

        binding.ivSchedulerBack.setOnClickListener(this);
        binding.btnUpdateSchedule.setOnClickListener(this);

        return binding.getRoot();

    }

    private void setSlotList(ArrayList<ScheduleSlotTimeTemp> courseSlotTime) {
        binding.rvSchedules.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        TutorSessionScheduleAdapter tutorSessionScheduleAdapter = new TutorSessionScheduleAdapter(getActivity(), courseSlotTime, new TutorSessionScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {

            }

            @Override
            public void onSlotDeleteConfirmed(ScheduleSlotDetail scheduleSlotDetail) {
                Log.e("tutor", "onSlotDeleteConfirmed: " + scheduleSlotDetail.tutorScheduleId);
                tutorScheduleViewModel.postDeleteSchedule(scheduleSlotDetail.tutorScheduleId, App.userDataContract.detail.userId).observe(getActivity(), new Observer<DefaultResponse>() {
                    @Override
                    public void onChanged(DefaultResponse defaultResponse) {
                        if (defaultResponse.status) {
                            Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                            getSlotTime();
                        }
                    }
                });
            }

            @Override
            public void onNewItemSelected(int adapterPosition, String selectedItem) {

            }

            @Override
            public void manageSchedule(ScheduleSlotDetail scheduleSlotDetail) {
                if (selectedSchedules.isEmpty()) {
                    selectedSchedules = scheduleSlotDetail.scheduleId.toString();
                } else {
                    selectedSchedules = selectedSchedules + "," + scheduleSlotDetail.scheduleId;
                }

                if (selectedSchedules.isEmpty()) {
                    Toast.makeText(getActivity(), "Select time slot.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Utility.showLoader(getActivity(), false);
                tutorScheduleViewModel.manageTutorSchedule(selectedSchedules).observe(getActivity(), new Observer<DefaultResponse>() {
                    @Override
                    public void onChanged(DefaultResponse defaultResponse) {
                        Utility.hideLoader();
                        Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                        selectedSchedules = "";
                        getSlotTime();
                      //  TutorHomeActivity.addFragment(new TutorHomeFragment(), Constant.HOME, getActivity());
                    }
                });
            }
        });
        binding.rvSchedules.setAdapter(tutorSessionScheduleAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSchedulerBack:
                TutorHomeActivity.addFragment(new TutorProfileFragment(""), "UserProfile", getActivity());
                break;

            case R.id.btnUpdateSchedule:
                if (selectedSchedules.isEmpty()) {
                    Toast.makeText(getActivity(), "Select time slot.", Toast.LENGTH_SHORT).show();
                    return;
                }
                tutorScheduleViewModel.manageTutorSchedule(selectedSchedules).observe(getActivity(), new Observer<DefaultResponse>() {
                    @Override
                    public void onChanged(DefaultResponse defaultResponse) {
                        Toast.makeText(getActivity(), "" + defaultResponse.message, Toast.LENGTH_SHORT).show();
                        selectedSchedules = "";
                        TutorHomeActivity.addFragment(new TutorHomeFragment(), Constant.HOME, getActivity());
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tutorScheduleViewModel = ViewModelProviders.of(getActivity()).get(TutorViewModel.class);
        getSlotTime();
    }

    private void getSlotTime() {
        Utility.showLoader(getActivity(), false);
        tutorScheduleViewModel.getScheduleSlotTime().observe(getActivity(), scheduleSlotTimeTemps -> {
            if (scheduleSlotTimeTemps == null || scheduleSlotTimeTemps.size() == 0) {
                binding.layoutProgressSchedule.setVisibility(View.GONE);
                binding.layoutSchedule.setVisibility(View.GONE);
                binding.layoutProgressScheduleEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.layoutProgressSchedule.setVisibility(View.GONE);
                binding.layoutProgressScheduleEmpty.setVisibility(View.GONE);
                binding.layoutSchedule.setVisibility(View.VISIBLE);
                Utility.hideLoader();
                setSlotList(scheduleSlotTimeTemps);
            }
        });
    }
}
