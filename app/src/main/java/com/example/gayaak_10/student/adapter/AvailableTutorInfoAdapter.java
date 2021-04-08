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
import com.example.gayaak_10.databinding.ItemTutorDatesBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AvailableTutorInfoAdapter extends RecyclerView.Adapter<AvailableTutorInfoAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<DefaultStringSelected> dateString = new ArrayList<>();

    public AvailableTutorInfoAdapter(Context context, ArrayList<DefaultStringSelected> dateString,  OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.dateString = dateString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorDatesBinding binding = ItemTutorDatesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(dateString.get(position), mContext, onItemClickListener);

       if (dateString.get(position).isSelected){
           holder.binding.tvDate.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_filled_primary));
           holder.binding.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.white));
       }else {
           holder.binding.tvDate.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circle_primary));
           holder.binding.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.black));
       }
    }


    @Override
    public int getItemCount() {
        return dateString.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorDatesBinding binding;

        public ViewHolder(ItemTutorDatesBinding binding) {
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

                String dayOfTheWeek = (String) DateFormat.format("EEE", date); // Mon
                String day          = (String) DateFormat.format("dd",   date); // 20
                String monthString  = (String) DateFormat.format("MMM",  date); // Jun
                String monthNumber  = (String) DateFormat.format("MM",   date); // 06
                String year         = (String) DateFormat.format("yyyy", date); // 2013

                binding.tvDate.setText(day);
                binding.tvMonth.setText(monthString);
                binding.tvWeekName.setText(dayOfTheWeek.toUpperCase());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvDate.setOnClickListener(new View.OnClickListener() {
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