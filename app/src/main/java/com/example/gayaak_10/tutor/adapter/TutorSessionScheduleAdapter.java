package com.example.gayaak_10.tutor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gayaak_10.R;
import com.example.gayaak_10.databinding.ItemTutorScheduleBinding;
import com.example.gayaak_10.tutor.model.ScheduleSlotDetail;
import com.example.gayaak_10.tutor.model.ScheduleSlotTimeTemp;
import com.example.gayaak_10.utility.Utility;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

public class TutorSessionScheduleAdapter extends RecyclerView.Adapter<TutorSessionScheduleAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<ScheduleSlotTimeTemp> sessionDates = new ArrayList<>();

    public TutorSessionScheduleAdapter(Context context, ArrayList<ScheduleSlotTimeTemp> allDays, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.sessionDates = allDays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTutorScheduleBinding binding = ItemTutorScheduleBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.bind(coursesDetail.get(position), mContext, onItemClickListener);
        holder.bind(mContext, sessionDates.get(position), onItemClickListener);
        holder.binding.layoutDeleteSlot.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return sessionDates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemTutorScheduleBinding binding;
        private TutorSessionFreeTimeAdapter tutorSessionFreeTimeAdapter;
        private ArrayList<ScheduleSlotDetail> tutorSlotTimes = new ArrayList<ScheduleSlotDetail>();
        private ArrayList<ScheduleSlotDetail> bookedSlotTimes = new ArrayList<ScheduleSlotDetail>();
        private ScheduleSlotDetail selectedTimedData;
        private String slotType = "";

        public ViewHolder(ItemTutorScheduleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context mContext, ScheduleSlotTimeTemp sessionSchedule, OnItemClickListener onItemClickListener) {
            binding.tvSessionDay.setText(sessionSchedule.dayName.substring(0, 3).toUpperCase());

            if (sessionSchedule.detail != null) {
                for (int i = 0; i < sessionSchedule.detail.size(); i++) {
                    if (sessionSchedule.detail.get(i).IsScheduleAvailable == 1) {
                        tutorSlotTimes.add(sessionSchedule.detail.get(i));
                    } else if (sessionSchedule.detail.get(i).IsScheduleAvailable == 0) {
                        bookedSlotTimes.add(sessionSchedule.detail.get(i));
                    }
                }
                addFreeSlot(mContext, tutorSlotTimes);
                addLockedSlots(mContext, bookedSlotTimes);
            }

            binding.layoutSelectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.layoutDeleteSlot.setVisibility(View.GONE);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    PopupWindow popup = new PopupWindow(mContext);
                    View layout = inflater.inflate(R.layout.popup_time, null);
                    popup.setContentView(layout);
                    RecyclerView rvSlotTime = (RecyclerView) layout.findViewById(R.id.rvSlotTime);
                    rvSlotTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    SessionSlotTimeAdapter adapter = new SessionSlotTimeAdapter(mContext, sessionSchedule.detail, new SessionSlotTimeAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClickListener(ScheduleSlotDetail scheduleSlotDetail) {
                            popup.dismiss();
                            if (scheduleSlotDetail.isSelected) {
                                Toast.makeText(mContext, "Already added.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("time", " => " + scheduleSlotDetail.scheduleId);
                            //call manage session api
                            if (!tutorSlotTimes.contains(scheduleSlotDetail)) {
                                tutorSlotTimes.add(scheduleSlotDetail);
                                addFreeSlot(mContext, tutorSlotTimes);
                                onItemClickListener.manageSchedule(scheduleSlotDetail);
                            }

                        }
                    });
                    rvSlotTime.setAdapter(adapter);

                    popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                    // Closes the popup window when touch outside of it - when looses focus
                    popup.setOutsideTouchable(true);
                    popup.setFocusable(true);
                    // Show anchored to button
                    popup.showAsDropDown(view,0,0, Gravity.TOP);
                    //popup.showAsDropDown(view);
                }
            });

            binding.tvYesDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slotType.equalsIgnoreCase("Locked")){
                        Utility.customDialogBoxTextWithSingle(mContext,"","Contact support for deleting this schedule.");
                     //   Toast.makeText(mContext, "Contact support for deleting this schedule.", Toast.LENGTH_SHORT).show();
                    }else {
                        if (selectedTimedData.tutorScheduleId == null){
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        onItemClickListener.onSlotDeleteConfirmed(selectedTimedData);
                    }

                    binding.layoutDeleteSlot.setVisibility(View.GONE);
                }
            });

            binding.tvNoDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.layoutDeleteSlot.setVisibility(View.GONE);
                }
            });
        }

        private void addLockedSlots(Context mContext, ArrayList<ScheduleSlotDetail> tutorSlotTimes) {
            if (tutorSlotTimes != null && !tutorSlotTimes.isEmpty()) {
                binding.rvBookedSchedule.setVisibility(View.VISIBLE);
                //binding.rvBookedSchedule.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setAlignItems(AlignItems.FLEX_END);
                binding.rvFreeSchedule.setLayoutManager(layoutManager);
                tutorSessionFreeTimeAdapter = new TutorSessionFreeTimeAdapter(mContext, tutorSlotTimes, "Locked", new TutorSessionFreeTimeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        slotType = "Locked";
                        binding.layoutDeleteSlot.setVisibility(View.VISIBLE);
                        tutorSessionFreeTimeAdapter.notifyItemChanged(position);
                    }

                });
                binding.rvBookedSchedule.setAdapter(tutorSessionFreeTimeAdapter);
            } else {
                binding.rvBookedSchedule.setVisibility(View.INVISIBLE);
            }
        }

        private void addFreeSlot(Context mContext, ArrayList<ScheduleSlotDetail> tutorSlotTimes) {
            if (tutorSlotTimes != null && !tutorSlotTimes.isEmpty()) {
                binding.rvFreeSchedule.setVisibility(View.VISIBLE);
//                binding.rvFreeSchedule.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setAlignItems(AlignItems.FLEX_END);
                binding.rvFreeSchedule.setLayoutManager(layoutManager);
                tutorSessionFreeTimeAdapter = new TutorSessionFreeTimeAdapter(mContext, tutorSlotTimes, "Free", new TutorSessionFreeTimeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        selectedTimedData = tutorSlotTimes.get(position);
                        binding.layoutDeleteSlot.setVisibility(View.VISIBLE);
                        tutorSessionFreeTimeAdapter.notifyItemChanged(position);
                    }

                });
                binding.rvFreeSchedule.setAdapter(tutorSessionFreeTimeAdapter);
            } else {
                binding.rvFreeSchedule.setVisibility(View.INVISIBLE);
            }
        }

        /*
        public void findDates(Date date) throws ParseException {
            String firstDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            String firstTime = new SimpleDateFormat("K:mm a").format(date);
            String secondDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            String secondTime = "12:00 PM";

            String format = "dd/MM/yyyy K:mm a";

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            Date dateObj1 = sdf.parse(firstDate + " " + firstTime);
            Date dateObj2 = sdf.parse(secondDate + " " + secondTime);
            System.out.println("Date Start: " + dateObj1);
            System.out.println("Date End: " + dateObj2);

            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot = new Date(dif);
                System.out.println("Hour Slot --->" + slot);
                dif += 3600000;
            }
        }*/
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onSlotDeleteConfirmed(ScheduleSlotDetail scheduleSlotDetail);

        void onNewItemSelected(int adapterPosition, String selectedItem);

        void manageSchedule(ScheduleSlotDetail scheduleId);
    }
}