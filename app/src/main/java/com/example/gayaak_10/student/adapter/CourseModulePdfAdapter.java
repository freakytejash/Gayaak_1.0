package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemModulePdfBinding;
import com.example.gayaak_10.student.model.PDFModuleDataContractList;

import java.util.ArrayList;

public class CourseModulePdfAdapter extends RecyclerView.Adapter<CourseModulePdfAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<PDFModuleDataContractList> detail = new ArrayList<>();

    public CourseModulePdfAdapter(Context context, ArrayList<PDFModuleDataContractList> detail, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.detail = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemModulePdfBinding binding = ItemModulePdfBinding.inflate(layoutInflater, parent, false);
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
        public ItemModulePdfBinding binding;

        public ViewHolder(ItemModulePdfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, PDFModuleDataContractList pdfModuleDataContractList, OnItemClickListener onItemClickListener) {
            binding.tvPdfName.setText(pdfModuleDataContractList.name);
            Glide.with(mContext).load(pdfModuleDataContractList.thumbnailImage)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_ebook)).into(binding.ivPdfBook);

            binding.layoutPdfCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}