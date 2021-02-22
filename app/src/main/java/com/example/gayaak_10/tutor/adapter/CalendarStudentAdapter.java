package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemStudentsCalendarBinding;
import com.example.gayaak_10.model.tutor.StudentTutorBookingDataContractList;

import java.util.ArrayList;

public class CalendarStudentAdapter extends RecyclerView.Adapter<CalendarStudentAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<StudentTutorBookingDataContractList> tutorSlotTimes;

    public CalendarStudentAdapter(Context context, ArrayList<StudentTutorBookingDataContractList> tutorSlotTimes, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.tutorSlotTimes = tutorSlotTimes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStudentsCalendarBinding binding = ItemStudentsCalendarBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, tutorSlotTimes.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return tutorSlotTimes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemStudentsCalendarBinding binding;

        public ViewHolder(ItemStudentsCalendarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, StudentTutorBookingDataContractList tutorSlotTime, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(tutorSlotTime.studentName);
            binding.layoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(tutorSlotTime);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(StudentTutorBookingDataContractList scheduleSlotDetail);

    }
}