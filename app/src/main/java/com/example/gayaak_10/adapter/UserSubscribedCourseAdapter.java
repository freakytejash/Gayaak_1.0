package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemMyCoursesBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.ArrayList;
import java.util.List;

public class UserSubscribedCourseAdapter extends RecyclerView.Adapter<UserSubscribedCourseAdapter.ViewHolder> {

    public Context mContext;
    private List<CoursesDetail> coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public UserSubscribedCourseAdapter(Context context, ArrayList<CoursesDetail> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMyCoursesBinding itemMyCoursesBinding = ItemMyCoursesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemMyCoursesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(coursesDetailsList.get(position),mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return coursesDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMyCoursesBinding itemMyCoursesBinding;

        public ViewHolder(ItemMyCoursesBinding itemMyCoursesBinding) {
            super(itemMyCoursesBinding.getRoot());
            this.itemMyCoursesBinding = itemMyCoursesBinding;
        }

        public void bind(CoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            itemMyCoursesBinding.tvCourseName.setText(coursesDetail.name);
            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.gayaak_icon).into(itemMyCoursesBinding.ivCourseImage);
            itemMyCoursesBinding.tvLevelName.setText(coursesDetail.levelName);

            itemMyCoursesBinding.ivCourseOpen.setOnClickListener(view -> {
                onItemClickListener.onItemClickListener(getAdapterPosition());
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}
