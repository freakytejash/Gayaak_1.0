package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.databinding.DemoUpcomingClassesBinding;
import com.example.gayaak_10.databinding.ItemModuleBinding;
import com.example.gayaak_10.databinding.ItemRegularModuleProgressBinding;
import com.example.gayaak_10.databinding.ItemWalletPlanRecommendedBinding;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.tutor.fragment.TutorCourseModuleAdapter;

import java.util.ArrayList;

public class TutorRegularModuleAdapter extends RecyclerView.Adapter<TutorRegularModuleAdapter.ViewHolder> {

    public Context mContext;
    private TutorRegularModuleAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<CourseModuleDetail> detail = new ArrayList<>();

    public TutorRegularModuleAdapter(Context context, ArrayList<CourseModuleDetail> detail, TutorRegularModuleAdapter.OnItemClickListener onItemClickListener) {
        mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.detail = detail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRegularModuleProgressBinding binding = ItemRegularModuleProgressBinding.inflate(layoutInflater, parent, false);
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemRegularModuleProgressBinding binding;
        private int selectedPos = -1;
        public ViewHolder(ItemRegularModuleProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, CourseModuleDetail courseModuleDetail,
                         TutorRegularModuleAdapter.OnItemClickListener onItemClickListener) {


            binding.tvModuleName.setText(courseModuleDetail.name);
 //           binding.tvModuleDescription.setText(courseModuleDetail.description);

            binding.moduleInProgress.setOnClickListener( view -> onItemClickListener.onModuleStatus(courseModuleDetail.moduleId, 2));
            binding.moduleCompleted.setOnClickListener(view -> onItemClickListener.onModuleStatus(courseModuleDetail.moduleId,1));
            binding.moduleNotDone.setOnClickListener(view -> onItemClickListener.onModuleStatus(courseModuleDetail.moduleId, 0));

//            binding.layoutModuleVideo.setOnClickListener(view -> onItemClickListener.openModuleVideo(getAdapterPosition()));
//            binding.layoutModulePDF.setOnClickListener(view -> onItemClickListener.openModulePDF(getAdapterPosition()));

        }
    }

    public interface OnItemClickListener {

        void onModuleStatus(int moduleId, int statusId);
       // void onModuleCompleted(int moduleId, int statusId);
    }
}
