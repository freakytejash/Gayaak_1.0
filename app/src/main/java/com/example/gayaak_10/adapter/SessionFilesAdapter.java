package com.example.gayaak_10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemRecordedFilesBinding;
import com.example.gayaak_10.model.response.PractiseSessionInfoDetail;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.util.ArrayList;

public class SessionFilesAdapter extends RecyclerView.Adapter<SessionFilesAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<PractiseSessionInfoDetail> sessionInfoDetailArrayList = new ArrayList<>();

    public SessionFilesAdapter(ArrayList<PractiseSessionInfoDetail> sessionInfoDetailArrayList, Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.sessionInfoDetailArrayList = sessionInfoDetailArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRecordedFilesBinding itemRecordedFilesBinding = ItemRecordedFilesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemRecordedFilesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sessionInfoDetailArrayList.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return sessionInfoDetailArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemRecordedFilesBinding itemRecordedFilesBinding;

        public ViewHolder(ItemRecordedFilesBinding itemRecordedFilesBinding) {
            super(itemRecordedFilesBinding.getRoot());
            this.itemRecordedFilesBinding = itemRecordedFilesBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(PractiseSessionInfoDetail coursesDetail, Context mContext, final OnItemClickListener onItemClickListener) {
            itemRecordedFilesBinding.fileNameText.setText(coursesDetail.comment);
            if (coursesDetail.createdDate != null){
                if (!coursesDetail.createdDate.isEmpty()) {
                    itemRecordedFilesBinding.fileDateAddedText.setText(DateTimeUtility.convertDateTimeFormate(coursesDetail.createdDate, "dd/MM/yyyy hh:mm a", "yyyy-MM-dd'T'HH:mm:ss.SSS"));
                }
            }

            itemRecordedFilesBinding.cardView.setOnClickListener(view -> {
               onItemClickListener.onItemClickListener(getAdapterPosition());
            });
        }
    }



    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}