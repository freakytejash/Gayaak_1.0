package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemCoursePlanBinding;
import com.example.gayaak_10.student.model.CourseDataContractList;

import java.util.ArrayList;

public class CoursePlansAdapter extends RecyclerView.Adapter<CoursePlansAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CourseDataContractList> coursePlanArrayList = new ArrayList<>();

    public CoursePlansAdapter(Context context, ArrayList<CourseDataContractList> coursePlanArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.coursePlanArrayList = coursePlanArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCoursePlanBinding binding = ItemCoursePlanBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, coursePlanArrayList.get(position), onItemClickListener);

        switch (position){
            case 0:

                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return coursePlanArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCoursePlanBinding binding;

        public ViewHolder(ItemCoursePlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Context mContext, CourseDataContractList coursePlan, OnItemClickListener onItemClickListener) {
            binding.tvWalletPoints.setText(coursePlan.price + " per sessions");
            binding.tvPlanName.setText(coursePlan.name);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}