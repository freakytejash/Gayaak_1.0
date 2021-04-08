package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTutorStudentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegularTutorTimeAdapter extends RecyclerView.Adapter<RegularTutorTimeAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<DefaultStringSelected> dateString = new ArrayList<>();

    public RegularTutorTimeAdapter(Context context, ArrayList<DefaultStringSelected> dateString, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.dateString = dateString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorStudentBinding binding = ItemTutorStudentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(dateString.get(position), mContext, onItemClickListener);

       if (dateString.get(position).isSelected){
           holder.binding.tvTime.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_filled_primary));
           holder.binding.tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
       }else {
           holder.binding.tvTime.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_primary));
           holder.binding.tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.black));
       }
    }


    @Override
    public int getItemCount() {
        return dateString.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorStudentBinding binding;

        public ViewHolder(ItemTutorStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        public void bind(DefaultStringSelected localDate, Context mContext, final OnItemClickListener onItemClickListener) {

            String dtStart = localDate.name;
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Monday

                binding.tvTime.setText(dayOfTheWeek);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        onItemClickListener.onItemClickListener(getAdapterPosition(), (String) DateFormat.format("EEEE", format.parse(dtStart)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, String selectedDayName);

        void removeCourseCart(int position);
    }
}