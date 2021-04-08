package com.example.gayaak_10.tutor.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.ItemModuleBinding;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;

import java.util.ArrayList;

public class TutorCourseModuleAdapter extends RecyclerView.Adapter<TutorCourseModuleAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CourseModuleDetail> detail = new ArrayList<>();

    public TutorCourseModuleAdapter(Context context, ArrayList<CourseModuleDetail> detail,
                                    OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.detail = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemModuleBinding binding = ItemModuleBinding.inflate(layoutInflater, parent, false);
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
        public ItemModuleBinding binding;
        private int selectedPos = -1;

        public ViewHolder(ItemModuleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, CourseModuleDetail courseModuleDetail, OnItemClickListener onItemClickListener) {

            binding.ivLock.setVisibility(View.GONE);
            binding.tvModuleName.setText(courseModuleDetail.name);
            binding.tvModuleDescription.setText(courseModuleDetail.description);

            binding.layoutModuleVideo.setOnClickListener(view -> onItemClickListener.openModuleVideo(getAdapterPosition()));
            binding.layoutModulePDF.setOnClickListener(view -> onItemClickListener.openModulePDF(getAdapterPosition()));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void openModulePDF(int position);
        void openModuleVideo(int adapterPosition);
    }
}