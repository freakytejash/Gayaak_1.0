package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemScheduleTimeBinding;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;

import java.util.ArrayList;

public class TutorSessionFreeTimeAdapter extends RecyclerView.Adapter<TutorSessionFreeTimeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<ScheduleSlotDetail> tutorSlotTimes = new ArrayList<>();
    private String slotTimeType = "";

    public TutorSessionFreeTimeAdapter(Context context, ArrayList<ScheduleSlotDetail> tutorSlotTimes, String slotTimeType, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.tutorSlotTimes = tutorSlotTimes;
        this.slotTimeType = slotTimeType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemScheduleTimeBinding binding = ItemScheduleTimeBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, tutorSlotTimes.get(position), onItemClickListener, slotTimeType);
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
        public ItemScheduleTimeBinding binding;

        public ViewHolder(ItemScheduleTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, ScheduleSlotDetail tutorSlotTime, OnItemClickListener onItemClickListener, String slotTimeType) {

            if (slotTimeType.equalsIgnoreCase("Free")){
                binding.tvTimeSlot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.free));
                binding.tvTimeSlot.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }else if (slotTimeType.equalsIgnoreCase("Locked")){
                binding.tvTimeSlot.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                binding.tvTimeSlot.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            }
            binding.tvTimeSlot.setText(tutorSlotTime.time);

            binding.layoutSlot.setOnClickListener(view -> {
                onItemClickListener.onItemClickListener(getAdapterPosition());
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

    }
}