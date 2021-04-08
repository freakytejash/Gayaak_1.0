package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemSlotTimeBinding;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;

import java.util.ArrayList;

public class SessionSlotTimeAdapter extends RecyclerView.Adapter<SessionSlotTimeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<ScheduleSlotDetail> tutorSlotTimes;

    public SessionSlotTimeAdapter(Context context, ArrayList<ScheduleSlotDetail> tutorSlotTimes, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.tutorSlotTimes = tutorSlotTimes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSlotTimeBinding binding = ItemSlotTimeBinding.inflate(layoutInflater, parent, false);
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
        public ItemSlotTimeBinding binding;

        public ViewHolder(ItemSlotTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, ScheduleSlotDetail tutorSlotTime, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(tutorSlotTime.time);
            binding.layoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(tutorSlotTime);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(ScheduleSlotDetail scheduleSlotDetail);

    }
}