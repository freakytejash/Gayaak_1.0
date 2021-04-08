package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.common.model.TutorCalendarLiveClassDataContractList;
import com.example.gayaak_10.databinding.ItemTutorScheduleSessionsBinding;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CalendarTutorListAdapter extends RecyclerView.Adapter<CalendarTutorListAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private HashMap<String, ArrayList<TutorCalendarLiveClassDataContractList>> hashMap = new HashMap<>();
    private ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList = new ArrayList<>();

    public CalendarTutorListAdapter(Context context, ArrayList<TutorCalendarLiveClassDataContractList> liveClassDataContractList, HashMap<String, ArrayList<TutorCalendarLiveClassDataContractList>> hashMap, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.hashMap = hashMap;
        this.liveClassDataContractList = liveClassDataContractList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorScheduleSessionsBinding binding = ItemTutorScheduleSessionsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(liveClassDataContractList.get(position), mContext, onItemClickListener);
/*        for (Map.Entry<String, ArrayList<TutorCalendarLiveClassDataContractList>> graph : hashMap.entrySet()) {
            ArrayList<TutorCalendarLiveClassDataContractList> graphs = graph.getValue();
            Log.e("text", "Key Name: " + graph.getKey());
          *//*  if (graphs.get(position).dateString != null) {
                holder.binding.tvScheduleDate.setText(DateTimeUtility.convertDateStringFormat(
                        DateTimeUtility.convertDateTimeFormate(graphs.get(position).dateString,
                                "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss").toUpperCase()));
            }*//*
            holder.binding.tvScheduleDate.setText(DateTimeUtility.convertDateStringFormat(
                    DateTimeUtility.convertDateTimeFormate(graph.getKey(),
                            "MM/dd/yyyy", "dd-M-yyyy hh:mm:ss").toUpperCase()));
            holder.bind(mContext, graphs, onItemClickListener);
        }*/
    }


    @Override
    public int getItemCount() {
        return liveClassDataContractList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorScheduleSessionsBinding binding;

        public ViewHolder(ItemTutorScheduleSessionsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TutorCalendarLiveClassDataContractList liveClassDataContractList, Context mContext, OnItemClickListener onItemClickListener) {

            binding.tvScheduleDate.setText(DateTimeUtility.convertDateStringFormat(
                    DateTimeUtility.convertDateTimeFormate(liveClassDataContractList.dateString,
                            "MM/dd/yyyy", "yyyy-MM-dd'T'HH:mm:ss").toUpperCase()));

          /*  binding.rvCalendarScheduleList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            CalendarScheduleAdapter calendarTutorListAdapter = new CalendarScheduleAdapter(mContext, graphs,new CalendarScheduleAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position) {

                }

                @Override
                public void onDeleteSession(int adapterPosition) {
                    onItemClickListener.onDeleteSession(adapterPosition);
                }

            });
            binding.rvCalendarScheduleList.setAdapter(calendarTutorListAdapter);*/


            binding.tvCourseName.setText(liveClassDataContractList.categoryName);
            binding.tvLevelName.setText(liveClassDataContractList.levelName);
            binding.tvScheduleTime.setText(liveClassDataContractList.time);
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            try {
                Date date = format1.parse(liveClassDataContractList.time);
                System.out.println(date);

                String time = (String) DateFormat.format("HH:mm a", date);
                binding.tvScheduleTime.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.tvTutorReschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onRescheduleSession(getAdapterPosition());
                }
            });

            binding.tvDeleteSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onDeleteSession(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onRescheduleSession(int adapterPosition);

        void onDeleteSession(int adapterPosition);
    }
}