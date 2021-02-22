package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemTutorStudentBinding;
import com.example.gayaak_10.student.model.CategoryDataContractList;

import java.util.ArrayList;

public class CourseCategoryTypeAdapter extends RecyclerView.Adapter<CourseCategoryTypeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CategoryDataContractList> categoryDataContractLists;

    public CourseCategoryTypeAdapter(Context context, ArrayList<CategoryDataContractList> categoryDataContractLists, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.categoryDataContractLists = categoryDataContractLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorStudentBinding binding = ItemTutorStudentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, categoryDataContractLists.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return categoryDataContractLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorStudentBinding binding;

        public ViewHolder(ItemTutorStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, CategoryDataContractList tutorSlotTime, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(tutorSlotTime.name);
            binding.layoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

    }
}