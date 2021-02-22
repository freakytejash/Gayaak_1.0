package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.databinding.ItemEbookBinding;
import com.example.gayaak_10.student.model.EbookData;

import java.util.ArrayList;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<EbookData> ebookDataArrayList = new ArrayList<>();

    public EbookAdapter(Context context, ArrayList<EbookData> ebookDataArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.ebookDataArrayList = ebookDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemEbookBinding binding = ItemEbookBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(ebookDataArrayList.get(position), mContext, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return ebookDataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemEbookBinding binding;

        public ViewHolder(ItemEbookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EbookData ebookData, Context mContext, OnItemClickListener onItemClickListener) {
            Glide.with(mContext).load(ebookData.ebook).into(binding.ivBook);
            binding.tvEbookName.setText(ebookData.ebookName);

            binding.layoutEbookCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}