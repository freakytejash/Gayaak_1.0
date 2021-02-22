package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTutorTimeSlotBinding;

import java.util.ArrayList;

public class AvailableTutorTimeAdapter extends RecyclerView.Adapter<AvailableTutorTimeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<DefaultStringSelected> timeString = new ArrayList<>();

    public AvailableTutorTimeAdapter(Context mContext, ArrayList<DefaultStringSelected> timeString, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;
        this.timeString = timeString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorTimeSlotBinding binding = ItemTutorTimeSlotBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(timeString.get(position), mContext, onItemClickListener);

        if (timeString.get(position).isSelected){
            holder.binding.tvSlotTime.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangular_25_filled_coloraccent));
            holder.binding.tvSlotTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }else {
            holder.binding.tvSlotTime.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rectangular_25_filled_white));
            holder.binding.tvSlotTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }


    @Override
    public int getItemCount() {
        return timeString.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorTimeSlotBinding binding;

        public ViewHolder(ItemTutorTimeSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(DefaultStringSelected timeString, Context mContext, final OnItemClickListener onItemClickListener) {
            binding.tvSlotTime.setText(timeString.name);

            binding.layoutCourseType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void removeCourseCart(int position);
    }
}