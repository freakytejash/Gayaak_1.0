package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemModuleInfoBinding;

public class CourseModuleInfoAdapter extends RecyclerView.Adapter<CourseModuleInfoAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;

    public CourseModuleInfoAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemModuleInfoBinding binding = ItemModuleInfoBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemModuleInfoBinding binding;

        public ViewHolder(ItemModuleInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, OnItemClickListener onItemClickListener) {
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void openModuleVideo(int adapterPosition);
    }
}