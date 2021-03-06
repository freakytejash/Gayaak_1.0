package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemUserLearningCoursesBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.ArrayList;
import java.util.List;

public class UserCourseAdapter extends RecyclerView.Adapter<UserCourseAdapter.ViewHolder> {

    public Context mContext;
    private List<CoursesDetail> coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public UserCourseAdapter(Context context, ArrayList<CoursesDetail> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemUserLearningCoursesBinding binding = ItemUserLearningCoursesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(coursesDetailsList.get(position),mContext, onItemClickListener);
       // holder.bind(mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return coursesDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemUserLearningCoursesBinding binding;

        public ViewHolder(ItemUserLearningCoursesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            binding.tvCourseName.setText(coursesDetail.name);
            binding.tvLevelName.setText(coursesDetail.levelName);
            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.singer3).into(binding.ivCourseBg);
            binding.btnContinueCourse.setOnClickListener(view ->
                    onItemClickListener.onItemClickListener(getAdapterPosition()));

        }

        public void bind(Context mContext, OnItemClickListener onItemClickListener) {
            binding.btnContinueCourse.setOnClickListener(new View.OnClickListener() {
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
