package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemCartBinding;
import com.example.gayaak_10.student.model.BuyCoursesDetail;

import java.util.ArrayList;
import java.util.List;

public class CourseCartAdapter extends RecyclerView.Adapter<CourseCartAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private List<BuyCoursesDetail> moduleWithVideoDetails = new ArrayList<>();

    public CourseCartAdapter(List<BuyCoursesDetail> moduleWithVideoDetails, Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.moduleWithVideoDetails = moduleWithVideoDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding itemCartBinding = ItemCartBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(moduleWithVideoDetails.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return moduleWithVideoDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCartBinding itemCartBinding;

        public ViewHolder(ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            this.itemCartBinding = itemCartBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(BuyCoursesDetail moduleWithVideoDetails, Context mContext, final OnItemClickListener onItemClickListener) {
            itemCartBinding.tvCourseName.setText(moduleWithVideoDetails.name);
            itemCartBinding.tvLevelName.setText(moduleWithVideoDetails.levelName);
            itemCartBinding.tvCourseFee.setText("" + moduleWithVideoDetails.price);
            Glide.with(mContext).load(moduleWithVideoDetails.ThumbnailImage).placeholder(R.drawable.gayaak_icon).into(itemCartBinding.ivCourseImage);

            itemCartBinding.tvRemoveCourse.setOnClickListener(view -> {
                        //coursesDetail.isAddedToCart = false;
                        onItemClickListener.removeCourseCart(getAdapterPosition());
                    }
            );


        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void removeCourseCart(int position);
    }
}