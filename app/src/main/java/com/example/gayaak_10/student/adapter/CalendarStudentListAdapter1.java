package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.databinding.ItemStudentScheduleSessionsBinding;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarStudentListAdapter1 extends RecyclerView.Adapter<CalendarStudentListAdapter1.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList = new ArrayList<>();

    public CalendarStudentListAdapter1(Context context, ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList, OnItemClickListener onItemClickListener) {
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
        holder.bind(mContext, liveClassDataContractList.get(position), onItemClickListener);
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

        public void bind(Context mContext, TutorCalendarLiveClassDataContractList tutorCalendarLiveClassDataContractList, OnItemClickListener onItemClickListener) {
            binding.tvScheduleDate.setText(DateTimeUtility.convertDateStringFormat(
                    DateTimeUtility.convertDateTimeFormate(tutorCalendarLiveClassDataContractList.dateString,
                            "MM/dd/yyyy", "yyyy-MM-dd'T'HH:mm:ss").toUpperCase()));

            binding.tvCourseName.setText(tutorCalendarLiveClassDataContractList.categoryName);
            binding.tvLevelName.setText(tutorCalendarLiveClassDataContractList.levelName);
            binding.tvScheduleTime.setText(tutorCalendarLiveClassDataContractList.time);
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            try {
                Date date = format1.parse(tutorCalendarLiveClassDataContractList.time);
                System.out.println(date);

                String time = (String) DateFormat.format("HH:mm a", date);
                binding.tvScheduleTime.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvCalendarCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDeleteSession(getAdapterPosition());
                }
            });

            binding.tvCalendarReschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onRescheduleSession(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onRescheduleSession(int adapterPosition);

        void onDeleteSession(int adapterPosition);
    }
}