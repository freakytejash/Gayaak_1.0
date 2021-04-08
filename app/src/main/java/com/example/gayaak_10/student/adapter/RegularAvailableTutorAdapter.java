package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemAvailableTutorsRegularBinding;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.tutor.adapter.recommended.RegularScheduleTimeAdapter;
import com.example.gayaak_10.tutor.adapter.recommended.RegularSessionsScheduleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegularAvailableTutorAdapter extends RecyclerView.Adapter<RegularAvailableTutorAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CourseByStudentId.CourseByStudentDetail> courseByStudentDetails;
    private boolean isRescheduled;

    public RegularAvailableTutorAdapter(Context context, ArrayList<CourseByStudentId.CourseByStudentDetail> allTutorsDemosList,
                                         boolean isRescheduled, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.courseByStudentDetails = allTutorsDemosList;
        this.isRescheduled = isRescheduled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAvailableTutorsRegularBinding binding = ItemAvailableTutorsRegularBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, courseByStudentDetails.get(position),isRescheduled, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return courseByStudentDetails.size();
    }

    @SuppressLint("InflateParams")
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemAvailableTutorsRegularBinding binding;
        private ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> timeArray = new ArrayList<>();
        private ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> selectedTimeList = new ArrayList<>();
        private int scheduledId;
        public ViewHolder(ItemAvailableTutorsRegularBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Context mContext, CourseByStudentId.CourseByStudentDetail courseByStudentDetail, boolean isRescheduled, OnItemClickListener onItemClickListener) {
            if (courseByStudentDetail.courseTutorsDataContractList == null) {
                return;
            }

            binding.tvCourseName.setText(""+courseByStudentDetail.name);


            if (courseByStudentDetail.tutorDataContract != null)
            {
                binding.tvTutorName.setText(courseByStudentDetail.tutorDataContract.firstName + " " + courseByStudentDetail.tutorDataContract.lastName);
                binding.tvCourseLevel.setText(courseByStudentDetail.levelName);
                if (courseByStudentDetail.tutorDataContract.videoURL != null && !courseByStudentDetail.tutorDataContract.videoURL.isEmpty()) {
                    binding.videoview.setSource(courseByStudentDetail.tutorDataContract.videoURL);
                }
                Glide.with(mContext).load(courseByStudentDetail.tutorDataContract.imageName).into(binding.ivTutorImage);
            } else {
                binding.tvTutorName.setText(courseByStudentDetail.courseTutorsDataContractList.get(0).TutorName);
                binding.tvCourseLevel.setText(courseByStudentDetail.courseTutorsDataContractList.get(0).LevelName);
                if (courseByStudentDetail.courseTutorsDataContractList.get(0).videoURL != null && !courseByStudentDetail.courseTutorsDataContractList.get(0).videoURL.isEmpty()) {
                    binding.videoview.setSource(courseByStudentDetail.courseTutorsDataContractList.get(0).videoURL);
                }
                Glide.with(mContext).load(courseByStudentDetail.courseTutorsDataContractList.get(0).thumbnailImage).into(binding.ivTutorImage);
            }

            ArrayList<String> dateList = new ArrayList<>();
            HashMap<String, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime>> hashMap = new HashMap<>();
            dateList.add("Monday");
            dateList.add("Tuesday");
            dateList.add("Wednesday");
            dateList.add("Thursday");
            dateList.add("Friday");
            dateList.add("Saturday");
            dateList.add("Sunday");
            for (int i = 0; i < dateList.size(); i++) {
                String day = dateList.get(i);
                for (int j = 0; j < courseByStudentDetail.courseTutorsDataContractList.size(); j++) {
                    if (day.equalsIgnoreCase(courseByStudentDetail.courseTutorsDataContractList.get(j).Day)) {
                        timeArray.add(courseByStudentDetail.courseTutorsDataContractList.get(j));
                    }
                    if (j == courseByStudentDetail.courseTutorsDataContractList.size() - 1 && timeArray.size() != 0) {
                        ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> tempList = new ArrayList<>(timeArray);
                        hashMap.put(day, tempList);
                        timeArray.clear();
                        break;
                    }
                }
            }




            binding.btnCancel.setOnClickListener(view -> onItemClickListener.onCancel());
            binding.btnPayNow.setOnClickListener(view -> onItemClickListener.onPayNow(courseByStudentDetail, binding.tvDayName1.getText().toString(),
                    binding.tvClassTime1.getText().toString(), binding.tvDayName2.getText().toString(), binding.tvClassTime2.getText().toString(), isRescheduled));
            binding.layoutWeekClass1.setOnClickListener(view -> openDatePopup(view, mContext, hashMap, binding.tvDayName1));
            binding.layoutWeekClass2.setOnClickListener(view -> openDatePopup(view, mContext, hashMap, binding.tvDayName2));
            binding.ivAddClass.setOnClickListener(view -> {
                if (binding.layoutSecondClass.getVisibility() == View.VISIBLE) {
                    binding.ivAddClass.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_add));
                    binding.layoutSecondClass.setVisibility(View.GONE);
                } else {
                    binding.ivAddClass.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_remove));
                    binding.layoutSecondClass.setVisibility(View.VISIBLE);
                    binding.tvDayName2.setText(mContext.getString(R.string.weekly_class_2));
                    binding.tvClassTime2.setText(mContext.getString(R.string.timing_class_2));
                }
            });
            binding.layoutTimeClass1.setOnClickListener(view -> {
                if (selectedTimeList.size() != 0) {
                    openTimePopup(view, mContext, selectedTimeList, binding.tvClassTime1);
                } else {
                    Toast.makeText(mContext, "Time for selected day is not available.Select any other day.", Toast.LENGTH_SHORT).show();
                }

            });
            binding.layoutTimeClass2.setOnClickListener(view -> {
                if (selectedTimeList.size() != 0) {
                    openTimePopup(view, mContext, selectedTimeList, binding.tvClassTime2);
                } else {
                    Toast.makeText(mContext, "Time for selected day is not available.Select any other day.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void openTimePopup(View view, Context mContext, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime> list, AppCompatTextView tvClassTime) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow popup = new PopupWindow(mContext);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.popup_session_schedule, null);
            popup.setContentView(layout);
            RecyclerView rvDates = (RecyclerView) layout.findViewById(R.id.rvDates);
            rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            RegularScheduleTimeAdapter adapter = new RegularScheduleTimeAdapter(mContext, list, data -> {
                popup.dismiss();
                if (!binding.tvClassTime1.getText().toString().isEmpty()) {
                    if (binding.tvClassTime1.getText().toString().equalsIgnoreCase(data.Time)) {
                        Toast.makeText(mContext, "Already selected. Select any other time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                tvClassTime.setText(data.Time);
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

        private void openDatePopup(View view, Context mContext, HashMap<String, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime>> hashMap, AppCompatTextView tvDayName) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow popup = new PopupWindow(mContext);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.popup_session_schedule, null);
            popup.setContentView(layout);
            RecyclerView rvDates = layout.findViewById(R.id.rvDates);
            rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            ArrayList<String> dateList = new ArrayList<>();
            for (Map.Entry<String, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime>> graph : hashMap.entrySet()) {
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
    }

    public interface OnItemClickListener {
        void onPayNow(CourseByStudentId.CourseByStudentDetail courseByStudentDetail, String selectedDay, String selectedTime, String selectedDay2, String selectedTime2, boolean isRescheduled);
        void onCancel();
    }
}