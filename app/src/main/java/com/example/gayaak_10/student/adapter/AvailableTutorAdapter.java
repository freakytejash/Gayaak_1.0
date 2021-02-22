package com.example.gayaak_10.student.adapter;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.databinding.ItemAvailableTutorsBinding;
import com.example.gayaak_10.student.model.CourseTutorDataContractList;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class AvailableTutorAdapter extends RecyclerView.Adapter<AvailableTutorAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<CourseTutorDataContractList> allTutorsDemosList = new ArrayList<>();
    private HashMap<Integer, ArrayList<CourseTutorDataContractList>> hashMap;

    public AvailableTutorAdapter(Context context, ArrayList<CourseTutorDataContractList> allTutorsDemosList, HashMap<Integer, ArrayList<CourseTutorDataContractList>> hashMap, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.allTutorsDemosList = allTutorsDemosList;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemAvailableTutorsBinding binding = ItemAvailableTutorsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = 0;
        for (Map.Entry<Integer, ArrayList<CourseTutorDataContractList>> graph : hashMap.entrySet()) {
            if (position == i) {
                ArrayList<CourseTutorDataContractList> graphs = graph.getValue();
                Log.e("text", "Key Name: " + graph.getKey());
                if (graphs.get(0).tutorName != null) {
                    holder.binding.tvTutorName.setText(graphs.get(0).tutorName);
                }

                holder.binding.tvCourseLevel.setText(graphs.get(0).levelName);
                if (graphs.get(0).VideoURL != null && !graphs.get(0).VideoURL.isEmpty()) {
                    holder.binding.videoview.setSource(graphs.get(0).VideoURL);
                }
                Glide.with(mContext).load(graphs.get(0).ThumbnailImage).into(holder.binding.ivTutorImage);
                holder.bind(mContext, allTutorsDemosList.get(position), graphs, onItemClickListener);
                break;
            }
            i++;
        }
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemAvailableTutorsBinding binding;
        private AvailableTutorInfoAdapter availableTutorAdapter;
        private AvailableTutorTimeAdapter availableTutorTimeAdapter;
        private ArrayList<String> allDays = new ArrayList<>();
        //    private ArrayList<String> allTime = new ArrayList<>();
        ArrayList<DefaultStringSelected> dateString = new ArrayList<>();
        private String selectedTime = "";
        private String selectedDay = "";
        private DefaultStringSelected selectedDate;

        public ViewHolder(ItemAvailableTutorsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Context mContext, CourseTutorDataContractList allTutorsDemoDetail,
                         ArrayList<CourseTutorDataContractList> courseTutorDataContractLists, OnItemClickListener onItemClickListener) {
            /*if (!allTutorsDemoDetail.isActive){
                return;
            }*/


            allDays.clear();
            dateString.clear();
            for (int i = 0; i < courseTutorDataContractLists.size(); i++) {
                if (!allDays.contains(courseTutorDataContractLists.get(i).day)) {
                    allDays.add(courseTutorDataContractLists.get(i).day);
                }

                /*if (!allTime.contains(courseTutorDataContractLists.get(i).time)){
                    allTime.add(courseTutorDataContractLists.get(i).time);
                }*/
            }

            ArrayList<LocalDate> arrayList = new ArrayList<>();
            for (int i = 0; i < allDays.size(); i++) {
                if (allDays != null) {
                    arrayList = getDates(allDays.get(i));
                    for (int j = 0; j < arrayList.size(); j++) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                            String formattedString = arrayList.get(j).format(formatter);
                            if (new SimpleDateFormat("dd LLLL yyyy").parse(formattedString).equals(new Date())) {
//                                String convertedDate = DateTimeUtility.formatDateToString(
//                                        DateTimeUtility.convertDateToString(formattedString, "dd LLLL yyyy"),
//                                        "dd LLLL yyyy", "GMT-8", "dd LLLL yyyy");
                                dateString.add(new DefaultStringSelected(formattedString, false));
                                break;
                            } else if (!new SimpleDateFormat("dd LLLL yyyy").parse(formattedString).before(new Date())) {
//                                String convertedDate = DateTimeUtility.formatDateToString(
//                                        DateTimeUtility.convertDateToString(formattedString, "dd LLLL yyyy"),
//                                        "dd LLLL yyyy", "GMT-8", "dd LLLL yyyy");
                                dateString.add(new DefaultStringSelected(formattedString, false));
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.e("date", ": " + arrayList.get(j));
                    }
                }
            }


           /* ArrayList<DefaultStringSelected> timeString = new ArrayList<>();
            for (int j = 0; j < allTime.size(); j++) {
                timeString.add(new DefaultStringSelected(allTime.get(j), false));

            }*/
           /* if (allTutorsDemoDetail.time != null) {
                timeString.add(new DefaultStringSelected(allTutorsDemoDetail.time, false));
            }*/

            binding.rvDates.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            Collections.sort(dateString, new Comparator<DefaultStringSelected>() {
                @Override
                public int compare(DefaultStringSelected defaultStringSelected, DefaultStringSelected t1) {
                    try {
                        return new SimpleDateFormat("dd LLLL yyyy").parse(defaultStringSelected.name)
                                .compareTo(new SimpleDateFormat("dd LLLL yyyy").parse(t1.name));
                    } catch (ParseException e) {
                        return 0;
                    }
                }
            });
            availableTutorAdapter = new AvailableTutorInfoAdapter(mContext, dateString, new AvailableTutorInfoAdapter.OnItemClickListener() {

                @Override
                public void onItemClickListener(int position, String selectedDayName) {
                    for (int i = 0; i < dateString.size(); i++) {
                        if (dateString.get(i).name.equalsIgnoreCase(dateString.get(position).name)) {
                            if (dateString.get(i).isSelected) {
                                dateString.get(i).isSelected = false;
                            } else {
                                dateString.get(i).isSelected = true;
                            }
                        } else {
                            dateString.get(i).isSelected = false;
                        }
                    }
                    selectedDay = selectedDayName;
                    selectedDate = dateString.get(position);
                    availableTutorAdapter.notifyDataSetChanged();
                    updateTime(selectedDay, courseTutorDataContractLists, mContext, selectedDate);
                }

                @Override
                public void removeCourseCart(int position) {

                }
            });
            binding.rvDates.setAdapter(availableTutorAdapter);

            //   setTutorTime(mContext, timeString);

            binding.btnSkipTutor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.skipTutor(getAdapterPosition());
                }
            });

            binding.btnBookTutor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedDay.isEmpty() && selectedTime.isEmpty()) {
                        Toast.makeText(mContext, "Please select time and day", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (selectedDay.isEmpty()) {
                        Toast.makeText(mContext, "Please select day", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (selectedTime.isEmpty()) {
                        Toast.makeText(mContext, "Please select time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < courseTutorDataContractLists.size(); i++) {
                        if (selectedTime.equals(courseTutorDataContractLists.get(i).time) &&
                                selectedDay.equals(courseTutorDataContractLists.get(i).day)) {
                            Log.e("clicked", "onClick: " + selectedDate.name);
                            onItemClickListener.bookTutor(courseTutorDataContractLists.get(i), selectedDate.name);
                            break;
                        } else {
                            if (i == courseTutorDataContractLists.size() - 1) {
                                Toast.makeText(mContext, "Please select other day or time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            });
        }

        private void setTutorTime(Context mContext, ArrayList<DefaultStringSelected> timeString) {
            binding.rvTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            availableTutorTimeAdapter = new AvailableTutorTimeAdapter(mContext, timeString, new AvailableTutorTimeAdapter.OnItemClickListener() {

                @Override
                public void onItemClickListener(int position) {
                    for (int i = 0; i < timeString.size(); i++) {
                        if (timeString.get(i).name.equalsIgnoreCase(timeString.get(position).name)) {
                            timeString.get(i).isSelected = true;
                        } else {
                            timeString.get(i).isSelected = false;
                        }
                    }
                    selectedTime = timeString.get(position).name;
                    availableTutorTimeAdapter.notifyDataSetChanged();
                }

                @Override
                public void removeCourseCart(int position) {

                }
            });
            binding.rvTime.setAdapter(availableTutorTimeAdapter);
        }

        private void updateTime(String selectedDay, ArrayList<CourseTutorDataContractList> courseTutorDataContractLists, Context mContext, DefaultStringSelected selectedDate) {
            ArrayList<String> allTime = new ArrayList<>();
            for (int i = 0; i < courseTutorDataContractLists.size(); i++) {
//                String convertedDate = DateTimeUtility.formatDateToString(
//                        DateTimeUtility.convertDateToString(courseTutorDataContractLists.get(i).day, "EEEE"),
//                        "EEEE", "GMT-8", "EEEE");
                if (selectedDay.equalsIgnoreCase(courseTutorDataContractLists.get(i).day)) {
                    if (!allTime.contains(courseTutorDataContractLists.get(i).time)) {
                        allTime.add(courseTutorDataContractLists.get(i).time);
                    }
                }
            }
            ArrayList<DefaultStringSelected> timeString = new ArrayList<>();
            ArrayList<DefaultStringSelected> defaultTimeString = new ArrayList<>();
            for (int j = 0; j < allTime.size(); j++) {
                timeString.add(new DefaultStringSelected(allTime.get(j), false));
            }
            if (selectedDay.equalsIgnoreCase(DateTimeUtility.currentDateTime("EEEE"))) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date todaysDate = new Date();
                    String currentTimeDay = sdf.format(todaysDate);
                    Date date2 = null;
                    date2 = sdf.parse(currentTimeDay);
                    defaultTimeString.clear();
                    for (int i = 0; i < timeString.size(); i++) {
                        //12 November 2020
                        if (!selectedDate.name.equalsIgnoreCase(new SimpleDateFormat("dd MMMM yyyy").format(todaysDate))) {
                            defaultTimeString.add(timeString.get(i));
                        } else {
                            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                            Date date = format1.parse(timeString.get(i).name);
                            if (!new SimpleDateFormat("HH:mm").parse((String) DateFormat.format("HH:mm", date)).before(date2)) {
                                defaultTimeString.add(timeString.get(i));
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else {
                defaultTimeString.addAll(timeString);
            }
            Collections.sort(defaultTimeString, new Comparator<DefaultStringSelected>() {
                @Override
                public int compare(DefaultStringSelected defaultStringSelected, DefaultStringSelected t1) {
                    return defaultStringSelected.name.compareTo(t1.name);
                }
            });

            setTutorTime(mContext, defaultTimeString);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private ArrayList<LocalDate> getDates(String weekDay) {
            try{
                ArrayList<LocalDate> dates = new ArrayList<>();
                String inputUppercase = weekDay.toUpperCase();  // MONDAY

                DayOfWeek dow = DayOfWeek.valueOf(inputUppercase);

                Set<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);
                daysOfWeek.add(dow);

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH) + 1;


                LocalDate first = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstInMonth(dow));
                LocalDate last = first.with(TemporalAdjusters.lastDayOfYear());
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.ENGLISH);
                for (LocalDate date = first; !date.isAfter(last);
                     date = date.with(TemporalAdjusters.next(dow))) {
                    dates.add(date);
                }
                return dates;
            }catch (Exception e){
                e.printStackTrace();
            }
         return null;
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void bookTutor(CourseTutorDataContractList position, String dateName);

        void skipTutor(int position);
    }
}