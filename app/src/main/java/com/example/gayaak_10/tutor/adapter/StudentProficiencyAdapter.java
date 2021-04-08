package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemStudentProficienciesBinding;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;
import com.example.gayaak_10.tutor.model.StudentProficiency;

import java.util.ArrayList;

public class StudentProficiencyAdapter extends RecyclerView.Adapter<StudentProficiencyAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<StudentProficiency> studentProficiencies = new ArrayList<>();

    public StudentProficiencyAdapter(Context context, ArrayList<StudentProficiency> allDays, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.studentProficiencies = allDays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStudentProficienciesBinding binding = ItemStudentProficienciesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.bind(coursesDetail.get(position), mContext, onItemClickListener);
        holder.bind(mContext, studentProficiencies.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return studentProficiencies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemStudentProficienciesBinding binding;
        private TutorSessionFreeTimeAdapter tutorSessionFreeTimeAdapter;
        private ArrayList<ScheduleSlotDetail> tutorSlotTimes = new ArrayList<ScheduleSlotDetail>();
        private ArrayList<ScheduleSlotDetail> bookedSlotTimes = new ArrayList<ScheduleSlotDetail>();
        private ScheduleSlotDetail selectedTimedData;
        private String slotType = "";
        private int selected = 1;

        public ViewHolder(ItemStudentProficienciesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, StudentProficiency studentProficiency, OnItemClickListener onItemClickListener) {
            binding.rvProficiencyName.setText(studentProficiency.proficiencyName);

            binding.radioGrp.setOnCheckedChangeListener((group, checkedId) -> {
                // find which radio button is selected
                if (checkedId == R.id.rbYes) {
                    selected = 0;
                } else if (checkedId == R.id.rbNo) {
                    selected = 1;
                }
                onItemClickListener.onItemClickListener(studentProficiency.proficiencyName, selected);
                Log.e("SelectedProficiency", " => " +studentProficiency.proficiencyName);
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(String proficiencyName, int selected);
    }
}