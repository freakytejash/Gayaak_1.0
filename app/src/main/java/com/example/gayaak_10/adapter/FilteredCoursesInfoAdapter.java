package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemFilteredCoursesBinding;
import com.example.gayaak_10.model.response.FilterData;

import java.util.ArrayList;

public class FilteredCoursesInfoAdapter extends RecyclerView.Adapter<FilteredCoursesInfoAdapter.ViewHolder> {

    public Context mContext;
    private ArrayList<FilterData> coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public FilteredCoursesInfoAdapter(Context context, ArrayList<FilterData> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFilteredCoursesBinding itemCourseDetailBinding = ItemFilteredCoursesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCourseDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(coursesDetailsList.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return coursesDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFilteredCoursesBinding itemCourseDetailBinding;

        public ViewHolder(ItemFilteredCoursesBinding itemCourseDetailBinding) {
            super(itemCourseDetailBinding.getRoot());
            this.itemCourseDetailBinding = itemCourseDetailBinding;
        }


        public void bind(FilterData coursesDetailsList, Context mContext, OnItemClickListener onItemClickListener) {
            itemCourseDetailBinding.tvCourseCategory.setText(coursesDetailsList.name);

            itemCourseDetailBinding.rvCoursesInfo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            FilteredCourseDetailAdapter adapter = new FilteredCourseDetailAdapter(mContext, coursesDetailsList.coursesDetailArrayList, new FilteredCourseDetailAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int courseId) {
                    onItemClickListener.onItemClickListener(courseId);
                }
            });
            itemCourseDetailBinding.rvCoursesInfo.setAdapter(adapter);



        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id);
    }
}