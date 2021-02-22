package com.example.gayaak_10.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemEbookCellBinding;
import com.example.gayaak_10.student.model.EbookDataContract;

import java.util.ArrayList;

public class NewEBookAdapter extends RecyclerView.Adapter<NewEBookAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<EbookDataContract> detail = new ArrayList<>();


    public NewEBookAdapter(Context context, ArrayList<EbookDataContract> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.detail = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemEbookCellBinding binding = ItemEbookCellBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(mContext, detail.get(position), onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemEbookCellBinding binding;

        public ViewHolder(ItemEbookCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, EbookDataContract pdfModuleDataContractList, OnItemClickListener onItemClickListener) {
            binding.tvEbookName.setText(pdfModuleDataContractList.bookName);
            Glide.with(mContext).load(pdfModuleDataContractList.thumbnailImage)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_ebook)).into(binding.ivEBook);
            binding.tvEbookDescription.setText(pdfModuleDataContractList.description);
            binding.ivViewEbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            binding.tvDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDownloadEbook(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDownloadEbook(int position);
    }
}