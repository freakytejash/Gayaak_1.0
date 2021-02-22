package com.example.gayaak_10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTrendingCourseBinding;
import com.example.gayaak_10.model.response.CoursesDetail;

import java.util.ArrayList;
import java.util.List;

public class HomeTrendingCourseAdapter extends RecyclerView.Adapter<HomeTrendingCourseAdapter.ViewHolder> {

    public Context mContext;
    private List<CoursesDetail> coursesDetailsList;
    private OnItemClickListener onItemClickListener;

    public HomeTrendingCourseAdapter(Context context, ArrayList<CoursesDetail> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.coursesDetailsList = detail;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTrendingCourseBinding itemTrendingCourseBinding = ItemTrendingCourseBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemTrendingCourseBinding);
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
        public ItemTrendingCourseBinding itemTrendingCourseBinding;

        public ViewHolder(ItemTrendingCourseBinding itemTrendingCourseBinding) {
            super(itemTrendingCourseBinding.getRoot());
            this.itemTrendingCourseBinding = itemTrendingCourseBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(CoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            itemTrendingCourseBinding.tvTrendingCourseName.setText(coursesDetail.name);
            if (coursesDetail.levelName.isEmpty()) {
                itemTrendingCourseBinding.tvTrendingLevel.setVisibility(View.GONE);
            } else {
                itemTrendingCourseBinding.tvTrendingLevel.setVisibility(View.VISIBLE);
                itemTrendingCourseBinding.tvTrendingLevel.setText(coursesDetail.levelName);
            }

            if (coursesDetail.duration == null) {
                itemTrendingCourseBinding.tvDuration.setVisibility(View.GONE);
            } else {
                itemTrendingCourseBinding.tvDuration.setVisibility(View.VISIBLE);
                itemTrendingCourseBinding.tvDuration.setText(coursesDetail.duration);
            }

            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.singer_3).into(itemTrendingCourseBinding.ivTrendingCourseHeader);
            itemTrendingCourseBinding.btnJoinNow.setOnClickListener(view -> {
                onItemClickListener.onItemClickListener(getAdapterPosition());
                  /*  for (int i = 0; i < App.cartList.size(); i++) {
                        Log.d("check", " i => " +i);

                        if (App.cartList.get(i).courseId.equals(coursesDetail.courseId)){
                            Log.d("check", "if => " +coursesDetail.courseId+
                                    " , => "+App.cartList.get(i).courseId);
                            break;
                        }else {
                            Log.d("check", "else => " +coursesDetail.courseId+
                                    " , => "+App.cartList.get(i).courseId);
                            onItemClickListener.onItemClickListener(getAdapterPosition());
                        }
                    }
                }else {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }*/


               /* if (!coursesDetail.isAddedToCart) {
                    coursesDetail.isAddedToCart = true;
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                } else {
                    onItemClickListener.onAlreadyItemClickListener(getAdapterPosition());
                }*/
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onAlreadyItemClickListener(int position);
    }
}
