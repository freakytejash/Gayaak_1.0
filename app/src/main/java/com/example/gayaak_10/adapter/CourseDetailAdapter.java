package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemCourseDetailBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.ViewHolder> {

    public Context mContext;
    private CoursesDetail coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public CourseDetailAdapter(Context context, CoursesDetail detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCourseDetailBinding itemCourseDetailBinding = ItemCourseDetailBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCourseDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(coursesDetailsList, mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCourseDetailBinding itemCourseDetailBinding;

        public ViewHolder(ItemCourseDetailBinding itemCourseDetailBinding) {
            super(itemCourseDetailBinding.getRoot());
            this.itemCourseDetailBinding = itemCourseDetailBinding;
        }

        public void bind(CoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {

            if (coursesDetail.levelName != null){
                itemCourseDetailBinding.tvLevelName.setText(coursesDetail.levelName);
            }else {
                itemCourseDetailBinding.tvLevelName.setText(coursesDetail.name);
            }

            if (coursesDetail.detail != null && !coursesDetail.detail.isEmpty()){
                itemCourseDetailBinding.tvLevelDescription.setVisibility(View.VISIBLE);
                itemCourseDetailBinding.tvLevelDescription.setText(coursesDetail.detail);
            }else {
                itemCourseDetailBinding.tvLevelDescription.setVisibility(View.GONE);
            }

            itemCourseDetailBinding.btnViewCourseVideos.setOnClickListener(view -> onItemClickListener.onItemClickListener(getAdapterPosition()));
            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.gayaak_icon).into(itemCourseDetailBinding.ivCourseImage);
            //itemCourseDetailBinding.tvCourseDescription.setText(coursesDetail.detail);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}