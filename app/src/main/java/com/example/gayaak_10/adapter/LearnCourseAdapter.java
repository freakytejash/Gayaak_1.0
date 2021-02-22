package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTextviewBinding;
import com.example.gayaak_10.student.model.CategoryDataContractList;

import java.util.ArrayList;

public class LearnCourseAdapter extends RecyclerView.Adapter<LearnCourseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CategoryDataContractList> learningList;
    private OnItemClickListener onItemClickListener;

    public LearnCourseAdapter(Context context, ArrayList<CategoryDataContractList> learningList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.learningList = learningList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTextviewBinding binding = ItemTextviewBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(learningList.get(position), onItemClickListener);

        if (learningList.get(position).isSelected) {
            holder.binding.tvCourseType.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_25_filled_coloraccent));
            holder.binding.tvCourseType.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.binding.tvCourseType.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangular_25_filled_white));
            holder.binding.tvCourseType.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return learningList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTextviewBinding binding;

        public ViewHolder(ItemTextviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryDataContractList courseType, OnItemClickListener onItemClickListener) {
            binding.tvCourseType.setText(courseType.name);
            binding.layoutCourseType.setOnClickListener(view -> {
                courseType.isSelected = !courseType.isSelected;
                onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }
}
