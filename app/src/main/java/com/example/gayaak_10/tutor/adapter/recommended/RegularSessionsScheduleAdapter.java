package com.example.gayaak_10.tutor.adapter.recommended;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemSessionStringBinding;

import java.util.ArrayList;

public class RegularSessionsScheduleAdapter extends RecyclerView.Adapter<RegularSessionsScheduleAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<String> hashMap;

    public RegularSessionsScheduleAdapter(Context context, ArrayList<String> hashMap, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSessionStringBinding binding = ItemSessionStringBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, hashMap.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemSessionStringBinding binding;

        public ViewHolder(ItemSessionStringBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, String day, OnItemClickListener onItemClickListener) {
            binding.tvTime.setText(day);

            binding.layoutTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(day);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(String position);

    }
}