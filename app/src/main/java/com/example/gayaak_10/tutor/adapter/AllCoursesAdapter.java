package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemTutorStudentBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.ArrayList;

public class AllCoursesAdapter extends RecyclerView.Adapter<AllCoursesAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CoursesDetail> coursesDetailArrayList;

    public AllCoursesAdapter(Context context, ArrayList<CoursesDetail> coursesDetailArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.coursesDetailArrayList = coursesDetailArrayList;
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
        holder.bind(mContext, coursesDetailArrayList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return coursesDetailArrayList.size();
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

        public void bind(Context mContext, CoursesDetail coursesDetail, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(coursesDetail.name);
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