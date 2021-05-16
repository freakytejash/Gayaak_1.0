package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemExpertFeedbackBinding;
import com.example.gayaak_10.model.response.PractiseSessionInfoDetail;
import com.example.gayaak_10.utility.DateTimeUtility;
import com.example.gayaak_10.utility.Utility;

import java.util.ArrayList;

public class ExpertFeedbackAdapter extends RecyclerView.Adapter<ExpertFeedbackAdapter.ViewHolder> {

    public Context mContext;
    private ArrayList<PractiseSessionInfoDetail> sessionInfoDetailArrayList;
    private OnItemClickListener onItemClickListener;

    public ExpertFeedbackAdapter(ArrayList<PractiseSessionInfoDetail> sessionInfoDetailArrayList, Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.sessionInfoDetailArrayList = sessionInfoDetailArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemExpertFeedbackBinding itemExpertFeedbackBinding = ItemExpertFeedbackBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemExpertFeedbackBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sessionInfoDetailArrayList.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return sessionInfoDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemExpertFeedbackBinding itemExpertFeedbackBinding;

        public ViewHolder(ItemExpertFeedbackBinding itemExpertFeedbackBinding) {
            super(itemExpertFeedbackBinding.getRoot());
            this.itemExpertFeedbackBinding = itemExpertFeedbackBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(PractiseSessionInfoDetail practiseSessionInfoDetail, Context mContext, OnItemClickListener onItemClickListener) {
            //itemExpertFeedbackBinding.tvPracticeName.setText(Utility.getFileNameFromURL(practiseSessionInfoDetail.audioFileName));
            itemExpertFeedbackBinding.tvPracticeName.setText(practiseSessionInfoDetail.comment);
            if (practiseSessionInfoDetail.createdDate != null) {
                if (!practiseSessionInfoDetail.createdDate.isEmpty()) {
                    itemExpertFeedbackBinding.tvPracticeDate.setText(DateTimeUtility.convertDateTimeFormatUtil(practiseSessionInfoDetail.createdDate,"dd/MM/yyyy hh:mm a"));
                }
            }

            if (practiseSessionInfoDetail.practiseSessionDataFeedBackContractList == null) {
                itemExpertFeedbackBinding.tvEmptyReview.setVisibility(View.VISIBLE);
                itemExpertFeedbackBinding.tvReview.setVisibility(View.GONE);
                itemExpertFeedbackBinding.ivReview.setVisibility(View.GONE);
            } else {
                itemExpertFeedbackBinding.tvEmptyReview.setVisibility(View.GONE);
                itemExpertFeedbackBinding.tvReview.setVisibility(View.VISIBLE);
                itemExpertFeedbackBinding.ivReview.setVisibility(View.VISIBLE);
            }

           // itemExpertFeedbackBinding.tvSongName.setText(Utility.getFileNameFromURL(practiseSessionInfoDetail.audioFileName));
            itemExpertFeedbackBinding.tvSongName.setText(practiseSessionInfoDetail.comment);
            itemExpertFeedbackBinding.ivPlayRecording.setOnClickListener(view -> {
                onItemClickListener.onItemClickListener(getAdapterPosition());
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}