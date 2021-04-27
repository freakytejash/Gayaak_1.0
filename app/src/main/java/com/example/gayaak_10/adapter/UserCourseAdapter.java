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
import com.example.gayaak_10.student.model.CourseDataContract;
import com.example.gayaak_10.student.model.CourseDataContractList;

import java.util.ArrayList;
import java.util.List;

public class UserCourseAdapter extends RecyclerView.Adapter<UserCourseAdapter.ViewHolder> {

    public Context mContext;
    private List<CourseDataContractList> coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public UserCourseAdapter(Context context, ArrayList<CourseDataContractList> detail, OnItemClickListener onItemClickListener) {
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

        public void bind(CourseDataContractList coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            binding.tvCourseName.setText(coursesDetail.categoryName);
            binding.tvLevelName.setText(coursesDetail.levelName);
            String status =coursesDetail.completedModules+"/"+coursesDetail.moduleCount+" Module Completed";
            binding.tvCourseProgressStatus.setText(status);
            Glide.with(mContext).load(coursesDetail.thumbnailImage).placeholder(R.drawable.singer3).into(binding.ivCourseBg);
            binding.btnContinueCourse.setOnClickListener(view ->
                    onItemClickListener.onItemClickListener(getAdapterPosition()));

        }

        //for click on complete adapter
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
