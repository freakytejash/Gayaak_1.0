package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.common.model.TutorCalendarCompleteList;
import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.databinding.ItemStudentScheduleSessionsBinding;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalendarStudentListAdapter extends RecyclerView.Adapter<CalendarStudentListAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<TutorCalendarCompleteList> liveClassDataContractList = new ArrayList<>();

    public CalendarStudentListAdapter(Context context, ArrayList<TutorCalendarCompleteList> liveClassDataContractList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.liveClassDataContractList = liveClassDataContractList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStudentScheduleSessionsBinding binding = ItemStudentScheduleSessionsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(liveClassDataContractList.get(position), onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return liveClassDataContractList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemStudentScheduleSessionsBinding binding;

        public ViewHolder(ItemStudentScheduleSessionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TutorCalendarCompleteList calendarData, OnItemClickListener onItemClickListener) {
            String date = "";
            String categoryName = "";
            String levelName = "";
            String time = "";

            if (calendarData.type.equalsIgnoreCase("refill")) {
                binding.layoutSession.setVisibility(View.GONE);
                binding.layoutRefill.setVisibility(View.VISIBLE);

                binding.tvRefillPoints.setText("Refill wallet : " +calendarData.transactionData.CreditAmount+" Credits");
                try {
                    date = DateTimeUtility.convertDateStringFormat(
                            DateTimeUtility.convertDateTimeFormate(calendarData.transactionData.CreditDateAndTime,
                                    "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss").toUpperCase());
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    time = (String) DateFormat.format("HH:mm a", format1.parse(calendarData.transactionData.CreditDateAndTime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.tvRefillTime.setText(time);
            } else if (calendarData.type.equalsIgnoreCase("session")) {
                date = DateTimeUtility.convertDateStringFormat(
                        DateTimeUtility.convertDateTimeFormate(calendarData.liveClassData.dateString,
                                "MM/dd/yyyy", "yyyy-MM-dd'T'HH:mm:ss").toUpperCase());
                categoryName = calendarData.liveClassData.categoryName;
                levelName = calendarData.liveClassData.levelName;
                time = calendarData.liveClassData.time;
                try {
                    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                    time = (String) DateFormat.format("HH:mm a", format1.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                binding.layoutSession.setVisibility(View.VISIBLE);
                binding.layoutRefill.setVisibility(View.GONE);
                binding.tvCourseName.setText(categoryName);
                binding.tvLevelName.setText(levelName);
                binding.tvScheduleTime.setText(time);
            }

            binding.tvScheduleDate.setText(date);
            binding.tvRefillMore.setOnClickListener(view -> onItemClickListener.onRefillWallet());
            binding.tvCalendarCancel.setOnClickListener(view -> onItemClickListener.onDeleteSession(calendarData.liveClassData));
            binding.tvCalendarReschedule.setOnClickListener(view -> onItemClickListener.onRescheduleSession(calendarData.liveClassData));
        }
    }

    public interface OnItemClickListener {
        void onRescheduleSession(TutorCalendarLiveClassDataContractList adapterPosition);

        void onDeleteSession(TutorCalendarLiveClassDataContractList adapterPosition);

        void onRefillWallet();
    }
}