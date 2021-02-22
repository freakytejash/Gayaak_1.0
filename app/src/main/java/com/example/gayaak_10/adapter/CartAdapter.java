package com.example.gayaak_10.adapter;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private List<BuyCoursesDetail> coursesDetail = new ArrayList<>();

    public CartAdapter(List<BuyCoursesDetail> coursesDetail, Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.coursesDetail = coursesDetail;
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
        holder.bind(coursesDetail.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return coursesDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCartBinding itemCartBinding;

        public ViewHolder(ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            this.itemCartBinding = itemCartBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(BuyCoursesDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            itemCartBinding.tvCourseName.setText(coursesDetail.name);
            itemCartBinding.tvLevelName.setText(coursesDetail.levelName);
            itemCartBinding.tvCourseFee.setText("" + coursesDetail.price);
            Glide.with(mContext).load(coursesDetail.ThumbnailImage).placeholder(R.drawable.gayaak_icon).into(itemCartBinding.ivCourseImage);

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