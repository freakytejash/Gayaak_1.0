package com.example.gayaak_10.tutor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTutorSessionBinding;
import com.example.gayaak_10.tutor.model.TodaySessions;

import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TutorSessionAdapter extends RecyclerView.Adapter<TutorSessionAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<TodaySessions> todaySessionList = new ArrayList<>();

    public TutorSessionAdapter(Context context, ArrayList<TodaySessions> todaySessionList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.todaySessionList = todaySessionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorSessionBinding binding = ItemTutorSessionBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(todaySessionList.get(position), mContext, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return todaySessionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorSessionBinding binding;

        public ViewHolder(ItemTutorSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        public void bind(TodaySessions todaySessions, Context mContext, OnItemClickListener onItemClickListener) {
            binding.tvRescheduleSession.setOnClickListener(view -> onItemClickListener.onSessionReschedule(getAdapterPosition()));
            binding.tvCancelSession.setOnClickListener(view -> onItemClickListener.onSessionCancel(getAdapterPosition()));

            int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            binding.layoutSessionTime.setBackgroundColor(randomAndroidColor);
            Glide.with(mContext).load(todaySessions.StudentProfileImage).placeholder(R.drawable.ic_profile)
                    .apply(new RequestOptions().override(600, 200)).into(binding.ivCourseTutor);
            binding.tvCourseTime.setText(todaySessions.time);
            binding.tvCourseDay.setText(todaySessions.Day);
            binding.tvCourseDate.setText(todaySessions.sessionDate);
            binding.tvUpcomingCourseName.setText(todaySessions.CategoryName);
            binding.tvCourseTutorName.setText(todaySessions.StudentName);
            binding.tvCurrentCourseName.setText(todaySessions.CategoryName);
            binding.tvCurrentCourseLevel.setText(todaySessions.LevelName);
            binding.tvUpcomingCourseLevel.setText(todaySessions.LevelName);
            binding.tvCurrentCourseTutorName.setText(todaySessions.StudentName);
        }
    }

    public interface OnItemClickListener {
        void onSessionReschedule(int position);
        void onSessionCancel(int position);
    }
}