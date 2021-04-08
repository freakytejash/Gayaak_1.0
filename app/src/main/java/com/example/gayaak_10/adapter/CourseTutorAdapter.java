package com.example.gayaak_10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemCourseTutorBinding;
import com.example.gayaak_10.model.response.CourseTutorsDataContractList;

import java.util.List;

public class CourseTutorAdapter extends RecyclerView.Adapter<CourseTutorAdapter.ViewHolder> {

    public Context mContext;
    private List<CourseTutorsDataContractList> coursesDetailsList;

    public CourseTutorAdapter(Context context, List<CourseTutorsDataContractList> detail) {
        this.mContext = context;
        this.coursesDetailsList = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCourseTutorBinding itemCourseTutorBinding = ItemCourseTutorBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCourseTutorBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(coursesDetailsList.get(position), mContext);
    }


    @Override
    public int getItemCount() {
        return coursesDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCourseTutorBinding itemCourseTutorBinding;

        public ViewHolder(ItemCourseTutorBinding itemCourseTutorBinding) {
            super(itemCourseTutorBinding.getRoot());
            this.itemCourseTutorBinding = itemCourseTutorBinding;
        }

        public void bind(CourseTutorsDataContractList coursesDetail, Context mContext) {
            Glide.with(mContext).load(coursesDetail.userDataContract.imageName).placeholder(R.drawable.ic_user_placeholder).into(itemCourseTutorBinding.ivCourseTutor);
        }
    }
}