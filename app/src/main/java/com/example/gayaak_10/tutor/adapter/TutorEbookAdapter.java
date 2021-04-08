package com.example.gayaak_10.tutor.adapter;

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
import com.example.gayaak_10.student.model.PDFModuleDataContractList;

import java.util.ArrayList;

public class TutorEbookAdapter extends RecyclerView.Adapter<TutorEbookAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<PDFModuleDataContractList> detail = new ArrayList<>();


    public TutorEbookAdapter(Context context, ArrayList<PDFModuleDataContractList> detail, OnItemClickListener onItemClickListener) {
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

        public void bind(Context mContext, PDFModuleDataContractList pdfModuleDataContractList, OnItemClickListener onItemClickListener) {
            binding.tvEbookName.setText(pdfModuleDataContractList.name);
            Glide.with(mContext).load(pdfModuleDataContractList.thumbnailImage)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_ebook)).into(binding.ivEBook);
            binding.tvEbookDescription.setText(pdfModuleDataContractList.description);
            binding.ivViewEbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            binding.tvDownload.setOnClickListener(view -> onItemClickListener.onDownload(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDownload(int position);
    }
}