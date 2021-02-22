package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemVideoListBinding;
import com.example.gayaak_10.student.model.VideosModuleDataContractList;

import java.util.ArrayList;

public class StudentCourseVideoListAdapter extends RecyclerView.Adapter<StudentCourseVideoListAdapter.ViewHolder> {

    private ArrayList<VideosModuleDataContractList> videoDataArrayList;
    private Context mContext;
    private OnItemClickListener OnItemClickListener;
    public int selectedPosition = 0;

    public StudentCourseVideoListAdapter(Context context, ArrayList<VideosModuleDataContractList> allCoursesDetail, OnItemClickListener onItemClickListener) {
        videoDataArrayList = allCoursesDetail;
        mContext = context;
        OnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemVideoListBinding binding = ItemVideoListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(videoDataArrayList.get(position), mContext, OnItemClickListener);

        if(selectedPosition == position)
            holder.binding.ivVideoMask.setVisibility(View.VISIBLE);
        else
            holder.binding.ivVideoMask.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return videoDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemVideoListBinding binding;

        public ViewHolder(ItemVideoListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(VideosModuleDataContractList videoData, Context mContext, final OnItemClickListener onItemClickListener) {
            binding.tvLevelName.setText(videoData.name);

            Glide.with(mContext).load(videoData.thumbnailImage).placeholder(R.drawable.gayaak_icon).into(binding.ivVideoThumbNail);

            if (!videoData.isfreevedio){
                binding.ivVideoLock.setVisibility(View.VISIBLE);
            }else {
                binding.ivVideoLock.setVisibility(View.INVISIBLE);
            }


            binding.layoutVideoCell.setOnClickListener(view -> {
                selectedPosition = getAdapterPosition();
                if (videoData.isfreevedio){
                    onItemClickListener.paidVideoClick(getAdapterPosition());
                }else {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void paidVideoClick(int position);
    }
}
