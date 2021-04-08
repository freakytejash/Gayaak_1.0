package com.example.gayaak_10.tutor.adapter.recommended;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemSessionStringBinding;
import com.example.gayaak_10.student.model.CourseByStudentId;

import java.util.ArrayList;

public class RegularRescheduleTimeAdapter extends RecyclerView.Adapter<RegularRescheduleTimeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<String> hashMap;

    public RegularRescheduleTimeAdapter(Context context, ArrayList<String> hashMap, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSessionStringBinding binding = ItemSessionStringBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(hashMap.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemSessionStringBinding binding;

        public ViewHolder(ItemSessionStringBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String tutorSlotTime, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(tutorSlotTime);

            binding.layoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(tutorSlotTime);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String data);

    }
}