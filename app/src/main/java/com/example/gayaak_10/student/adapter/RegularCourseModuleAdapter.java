package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemRegularCourseModuleBinding;
import com.example.gayaak_10.model.tutor.CourseModuleDetail;
import com.example.gayaak_10.student.fragment.StudentProgressFragment;
import com.example.gayaak_10.student.model.CourseByStudentId;
import com.example.gayaak_10.student.model.RegularCourseProgress;
import com.example.gayaak_10.student.viewmodel.StudentViewModel;
import com.example.gayaak_10.tutor.adapter.recommended.RegularSessionsScheduleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegularCourseModuleAdapter extends RecyclerView.Adapter<RegularCourseModuleAdapter.ViewHolder> {

    public Context mContext;
    private RegularCourseModuleAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<CourseModuleDetail> detail = new ArrayList<>();
    private  ArrayList<RegularCourseProgress.ModuledataContractList> courseProgressList = new ArrayList<>();

    public RegularCourseModuleAdapter(Context context, ArrayList<CourseModuleDetail> detail,
                                      ArrayList<RegularCourseProgress.ModuledataContractList> courseProgressList,
                                      RegularCourseModuleAdapter.OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.detail = detail;
        this.courseProgressList = courseProgressList;
    }

    @NonNull
    @Override
    public RegularCourseModuleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemRegularCourseModuleBinding binding = ItemRegularCourseModuleBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RegularCourseModuleAdapter.ViewHolder holder, int position) {
        holder.bind(mContext, detail.get(position), courseProgressList, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemRegularCourseModuleBinding binding;
        private int selectedPos = -1;
        StudentViewModel viewModel;

        public ViewHolder(ItemRegularCourseModuleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, CourseModuleDetail courseModuleDetail,
                         ArrayList<RegularCourseProgress.ModuledataContractList> courseProgressList,
                         OnItemClickListener onItemClickListener) {
            String moduleStatus="nothing";
            if (courseProgressList!= null){
                for (int i=0; i< courseProgressList.size();i++){
                    if (courseModuleDetail.moduleId== courseProgressList.get(i).moduleId){
                        moduleStatus = courseProgressList.get(i).moduleStatus;
                        if (moduleStatus==null){
                            moduleStatus="nothing";
                        }
                    }
                }
            }

            Resources res = mContext.getResources();
            if (moduleStatus.equalsIgnoreCase("Complete")){
                binding.moduleStatusIcon.setImageResource(R.drawable.ic_correct_circle);
                binding.moduleStatusIcon.setColorFilter(res.getColor(R.color.green));
                binding.cardViewModule.getBackground().setTint(res.getColor(R.color.green));
                //   binding.cardViewModule.setBackgroundColor(res.getColor(R.color.green));
            }
            else if (moduleStatus.equalsIgnoreCase("InProgress")){
                binding.moduleStatusIcon.setImageResource(R.drawable.ic_tick_dotted);
                binding.moduleStatusIcon.setColorFilter(res.getColor(R.color.orange_original));
                binding.cardViewModule.getBackground().setTint(res.getColor(R.color.orange_original));
                //  binding.cardViewModule.setBackgroundColor(res.getColor(R.color.orange_original));
            }
            else {
                binding.moduleStatusIcon.setImageResource(R.drawable.ic_3_dot_circle);
                binding.moduleStatusIcon.setColorFilter(res.getColor(R.color.colorAccent));
                binding.cardViewModule.getBackground().setTint(res.getColor(R.color.colorAccent));
                // binding.cardViewModule.setBackgroundColor(res.getColor(R.color.colorAccent));
            }



            binding.moduleName.setText(courseModuleDetail.name);
        //    binding.layoutModuleVideo.setOnClickListener(view -> openVideoPopup(view, mContext, hashMap));
            binding.layoutModuleVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                   /* viewModel.getModuleVideo(courseModuleDetail.moduleId).observe(this, moduleWithVideo -> {
                                if (moduleWithVideo.detail != null){
                                    if (moduleWithVideo.detail.videosModuleDataContractList != null && !moduleWithVideo.detail.videosModuleDataContractList.isEmpty()){
                                        selectedCourse = moduleWithVideo.detail;*/
                    onItemClickListener.openModuleVideo(getAdapterPosition());
                }
            });

            binding.layoutModuleAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.openModuleAudio(getAdapterPosition());
                }
            });

            binding.cardViewModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

     /*   private void openVideoPopup(View view, Context mContext,
                                    HashMap<String, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime>> hashMap,
                                    AppCompatTextView tvDayName) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PopupWindow popup = new PopupWindow(mContext);
            assert inflater != null;
            View layout = inflater.inflate(R.layout.popup_module_video_list, null);
            popup.setContentView(layout);
            RecyclerView rvDates = layout.findViewById(R.id.rvVideoList);
            rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            ArrayList<String> dateList = new ArrayList<>();
            for (Map.Entry<String, ArrayList<CourseByStudentId.CourseByStudentDetail.CourseDayTime>> graph : hashMap.entrySet()) {
                dateList.add(graph.getKey());
            }

            RegularSessionsScheduleAdapter adapter = new RegularSessionsScheduleAdapter(mContext, dateList, day -> {
                popup.dismiss();
                selectedTimeList.clear();
                selectedTimeList.addAll(Objects.requireNonNull(hashMap.get(day)));
                tvDayName.setText(day);

            });
            rvDates.setAdapter(adapter);

            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            // Closes the popup window when touch outside of it - when looses focus
            popup.setOutsideTouchable(true);
            popup.setFocusable(true);
            // Show anchored to button
            popup.showAsDropDown(view);
        }*/
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void openModuleVideo(int position);
        void openModuleAudio(int position);
    }
}
