package com.example.gayaak_10.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.DemoUpcomingClassesBinding;
import com.example.gayaak_10.services.App;
import com.example.gayaak_10.student.model.LiveClassDataContractList;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
@SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "SimpleDateFormat"})
public class DemoUpcomingSessionAdapter extends RecyclerView.Adapter<DemoUpcomingSessionAdapter.ViewHolder> {

    private CountDownTimer countDown;
    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<LiveClassDataContractList> liveClassDataContractList = new ArrayList<>();
    private boolean isAfterStarted = false;

    public DemoUpcomingSessionAdapter(Context context, ArrayList<LiveClassDataContractList> liveClassDataContractList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.liveClassDataContractList = liveClassDataContractList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DemoUpcomingClassesBinding binding = DemoUpcomingClassesBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, liveClassDataContractList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return liveClassDataContractList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DemoUpcomingClassesBinding binding;


        public ViewHolder(DemoUpcomingClassesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


        public void bind(Context mContext, LiveClassDataContractList liveClassDataContractList, OnItemClickListener onItemClickListener) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd h:mm a");
            Glide.with(mContext).load(liveClassDataContractList.TutorProfileImage)
                    .placeholder(mContext.getDrawable(R.drawable.ic_profile))
                    .into(binding.ivCourseTutor);
            binding.tvUpcomingCourseName.setText(liveClassDataContractList.categoryName);
            binding.tvUpcomingCourseLevel.setText(liveClassDataContractList.LevelName);
            binding.tvClassTime.setText(liveClassDataContractList.time);
            binding.tvCourseTutorName.setText(liveClassDataContractList.tutorName);
            binding.tvClassWeek.setText(liveClassDataContractList.day);
            binding.tvClassMonth.setText(liveClassDataContractList.sessionDate);// "November, 25"
            binding.tvCurrentCourseName.setText(liveClassDataContractList.categoryName);
            binding.tvCurrentCourseLevel.setText(liveClassDataContractList.LevelName);
            Glide.with(mContext).load(liveClassDataContractList.TutorProfileImage)
                    .placeholder(mContext.getDrawable(R.drawable.ic_user_placeholder))
                    .into(binding.ivCurrentCourseTutor);
            binding.tvCurrentCourseTutorName.setText(liveClassDataContractList.tutorName);
            binding.btnStartClass.setOnClickListener(view -> onItemClickListener.onStartClass(getAdapterPosition()));
            //  binding.layoutCell.setOnClickListener(view -> onItemClickListener.onItemClickListener(getAdapterPosition()));
            binding.tvStudentReschedule.setOnClickListener(view -> onItemClickListener.onSessionReschedule(getAdapterPosition()));

            binding.tvStudentCancel.setOnClickListener(view -> onItemClickListener.onSessionCancel(getAdapterPosition()));

            try {
                if (liveClassDataContractList.time != null) {
                    // ***** if time less than 24 hours make layoutTutorDetail gone and layoutScheduleTime visible
                    // ***** and if time is same as current time
                    // layoutTutorDetail gone and layoutScheduleTime gone layoutStatusClass visible

                    Date todaysDate = new Date();
                    String convertedCurrentDateTime = DateTimeUtility.formatDateToString(todaysDate, "MMMM, dd h:mm a", Constant.TIMEZONE, "MMMM, dd h:mm a");

                    String time = binding.tvClassMonth.getText().toString() + " " + liveClassDataContractList.time; //November, 23 9:43 PM
                    String startTime = binding.tvClassMonth.getText().toString() + " " + liveClassDataContractList.startTime; //November, 23 9:43 PM

                    // Date date1 = sdf.parse(time);
                    Date date1 = sdf.parse(startTime);
                    Date date2 = sdf.parse(convertedCurrentDateTime);

                    long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();

                    difference = (-1)*difference;
                    float days = (difference / (1000 * 60 * 60 * 24));
                    int daysInt = (int) days;
                    int hours = (int) ((difference - (1000 * 60 * 60 * 24 * daysInt)) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * 24 * daysInt) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    hours = (hours < 0 ? -hours : hours);
                    min = (min < 0 ? -min : min);
                    difference = (difference < 0 ? -difference : difference);



                    if (days <= 0 /*|| (days==-1)*/) {
                      /*  if (days==-1 && liveClassDataContractList.time.equalsIgnoreCase("12:00 AM")){
                            //hours = hours+24;
                            days=0;
                        }*/

                        if (hours < Integer.parseInt(Constant.StartTimer)) {
                            if (getBetweenTime(liveClassDataContractList)) {
                                binding.layoutStatusClass.setVisibility(View.VISIBLE);
                                binding.tvUpcomingSchedule.setVisibility(View.GONE);
                                if (liveClassDataContractList.Price> App.userDataContract.detail.userWalletDataContract.Coins
                                        && liveClassDataContractList.liveClassTypeId==2){
                                    binding.btnStartClass.setText("Recharge your wallet");
                                }

                                //check difference between current and session time and
                                long Difference = getDifference(liveClassDataContractList);
                                Difference = (Difference < 0 ? -Difference : Difference);
                                isAfterStarted = true;
                                SessionTimeCountDown(Difference, liveClassDataContractList);
                            } else {
                                if (getEqualTime(liveClassDataContractList)) {
                                    showSessionCountDown(difference);
                                }
                            }
                        } else {
                            binding.layoutTutorDetail.setVisibility(View.VISIBLE);
                            binding.layoutStatusClass.setVisibility(View.GONE);
                        }
                    } else {
                        binding.layoutTutorDetail.setVisibility(View.VISIBLE);
                        binding.layoutStatusClass.setVisibility(View.GONE);
                        binding.layoutScheduleTime.setVisibility(View.GONE);
                        binding.tvUpcomingSchedule.setVisibility(View.VISIBLE);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void showSessionCountDown(long difference) {
            binding.layoutStatusClass.setVisibility(View.GONE);
            binding.layoutScheduleTime.setVisibility(View.VISIBLE);
            binding.layoutTutorDetail.setVisibility(View.GONE);
            // start countdown
            difference = (difference < 0 ? -difference : difference);
            startCountDown(difference, binding.tvUpcomingCourseTimeHr, binding.tvUpcomingCourseTimeMIN, binding.tvUpcomingCourseTimeHrSec);
        }

        private void startCountDown(long millis, AppCompatTextView tvUpcomingCourseTimeHr, AppCompatTextView tvUpcomingCourseTimeMIN, AppCompatTextView tvUpcomingCourseTimeSec) {
            //1584700200 is timestamp in milii seconds (Friday, March 20, 2020 10:30:00 AM)
            //1000(1sec) is time interval to call onTick method
            countDown = new CountDownTimer(millis, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    String hours = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                    String minutes = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                    String seconds = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    if (Integer.parseInt(hours.trim()) == 00 && Integer.parseInt(minutes.trim()) <= Integer.parseInt(Constant.ShowStartClass) && Integer.parseInt(seconds.trim()) == 00) {
                        notifyItemChanged(getAdapterPosition());
                    }
                    tvUpcomingCourseTimeHr.setText("" + hours + ":");
                    tvUpcomingCourseTimeMIN.setText("   " + minutes + ":");
                    tvUpcomingCourseTimeSec.setText("" + seconds);
                }

                @Override
                public void onFinish() {
                    binding.layoutStatusClass.setVisibility(View.VISIBLE);
                    binding.tvUpcomingSchedule.setVisibility(View.GONE);
                    notifyItemChanged(getAdapterPosition());
                }
            };
            countDown.start();
        }

        private void SessionTimeCountDown(long millis, LiveClassDataContractList liveClassDataContractList) {
/*

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        // Please here set your event date//YYYY-MM-DD
                        Date futureDate = dateFormat.parse("2019-5-30");
                        Date currentDate = new Date();
                        if (!currentDate.after(futureDate)) {
                            long diff = futureDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24  60  60 * 1000);
                            diff -= days  (24  60  60  1000);
                            long hours = diff / (60  60  1000);
                            diff -= hours  (60  60 * 1000);
                            long minutes = diff / (60 * 1000);
                            diff -= minutes  (60  1000);
                            long seconds = diff / 1000;
                            txtDay.setText("" + String.format("%02d", days));
                            txtHour.setText("" + String.format("%02d", hours));
                            txtMinute.setText(""
                                    + String.format("%02d", minutes));
                            txtSecond.setText(""
                                    + String.format("%02d", seconds));
                        } else {
                            tvEventStart.setVisibility(View.VISIBLE);
                            tvEventStart.setText("The event started!");
                            textViewGone();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 1 * 1000);
        }
*/

            new CountDownTimer(millis, 1000) {
                @SuppressLint("SetTextI18n")
                @Override

                public void onTick(long millisUntilFinished) {
                    //Convert milliseconds into hour,minute and seconds
                    String hours = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                    String minutes = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)));
                    String seconds = String.format(Locale.getDefault(), "%02d ", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    Log.e("onTick", "onTickStartCount: " + hours + " ->" + minutes + " -> " + seconds);
                    if (Integer.parseInt(hours.trim()) == 00
                            && Integer.parseInt(minutes.trim()) <= Integer.parseInt(Constant.EndStartClass) && Integer.parseInt(seconds.trim()) == 00) {
                        notifyItemChanged(getAdapterPosition());
                    }
                }

                @Override
                public void onFinish() {
                    binding.layoutStatusClass.setVisibility(View.GONE);
                    binding.tvUpcomingSchedule.setVisibility(View.VISIBLE);
                    binding.layoutTutorDetail.setVisibility(View.VISIBLE);
                    binding.layoutScheduleTime.setVisibility(View.GONE);
                    isAfterStarted = false;
                    onItemClickListener.onSessionNotAttended(liveClassDataContractList);
                }
            }.start();
        }

        @SuppressLint("SimpleDateFormat")
        private long getDifference(LiveClassDataContractList todaySessions) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd h:mm a");

            try {
                Date todaysDate = new Date();
                String convertedCurrentDateTime = DateTimeUtility.formatDateToString(todaysDate, "MMMM, dd h:mm a", Constant.TIMEZONE, "MMMM, dd h:mm a");

                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                Date endTime = f2.parse(todaySessions.endTime);
                String endDate = f2.format(Objects.requireNonNull(endTime));

                String time = binding.tvClassMonth.getText().toString() + " " + endDate;
                Date date1 = sdf.parse(time);
                Date date2 = sdf.parse(convertedCurrentDateTime);

                long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
                return difference;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    private Boolean getBetweenTime(LiveClassDataContractList liveClassDataContractList) {
        try {
            Date time1 = new SimpleDateFormat("h:mm a").parse(liveClassDataContractList.endTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);


            Date time2 = new SimpleDateFormat("h:mm a").parse(liveClassDataContractList.startTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            Date todaysDate = new Date();
            String convertedCurrentDateTime = DateTimeUtility.formatDateToString(todaysDate, "h:mm a", Constant.TIMEZONE, "h:mm a");

            Date d = new SimpleDateFormat("h:mm a").parse(convertedCurrentDateTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.equals(calendar2.getTime()) || x.equals(calendar1.getTime()) ||
                    x.before(calendar1.getTime()) && x.after(calendar2.getTime())) {
                //checks whether the current time is between 14:49:00 and 20:11:13.
                System.out.println(true);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Boolean getEqualTime(LiveClassDataContractList liveClassDataContractList) {
        try {
            Date time1 = new SimpleDateFormat("h:mm a").parse(liveClassDataContractList.endTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(Objects.requireNonNull(time1));
            calendar1.add(Calendar.DATE, 1);

            Date time2 = new SimpleDateFormat("h:mm a").parse(liveClassDataContractList.startTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(Objects.requireNonNull(time2));
            calendar2.add(Calendar.DATE, 1);

            Date todaysDate = new Date();
            String convertedCurrentDateTime = DateTimeUtility.formatDateToString(todaysDate, "h:mm a", Constant.TIMEZONE, "h:mm a");

            Date d = new SimpleDateFormat("h:mm a").parse(convertedCurrentDateTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(Objects.requireNonNull(d));
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.equals(calendar2.getTime()) || x.before(calendar2.getTime())) {
                //checks whether the current time is between 14:49:00 and 20:11:13.
                System.out.println(true);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onSessionReschedule(int position);

        void onSessionCancel(int position);

        void onStartClass(int position);

        void onSessionNotAttended(LiveClassDataContractList liveClassDataContractList);
    }
}