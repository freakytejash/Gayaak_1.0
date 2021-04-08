package com.example.gayaak_10.tutor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gayaak_10.R;
import com.example.gayaak_10.constants.Constant;
import com.example.gayaak_10.databinding.ItemTutorSessionBinding;
import com.example.gayaak_10.tutor.model.TodaySessions;
import com.example.gayaak_10.utility.DateTimeUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SuppressLint({"SimpleDateFormat", "SetTextI18n"})
@RequiresApi(api = Build.VERSION_CODES.O)
public class TutorTodaySessionAdapter extends RecyclerView.Adapter<TutorTodaySessionAdapter.ViewHolder> {

    public Context mContext;
    private OnItemClickListener onItemClickListener;
    private ArrayList<TodaySessions> todaySessionList = new ArrayList<>();

    public TutorTodaySessionAdapter(Context context, ArrayList<TodaySessions> todaySessionList, OnItemClickListener onItemClickListener) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTutorSessionBinding binding;
        private boolean isAfterStarted = false;

        public ViewHolder(ItemTutorSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(TodaySessions todaySessions, Context mContext, OnItemClickListener onItemClickListener) {

            binding.tvRescheduleSession.setOnClickListener(view -> onItemClickListener.onSessionReschedule(getAdapterPosition()));
            binding.tvCancelSession.setOnClickListener(view -> onItemClickListener.onSessionCancel(getAdapterPosition()));
            Glide.with(mContext).load(todaySessions.StudentProfileImage).placeholder(R.drawable.ic_profile)
                    .apply(new RequestOptions().override(600, 200)).into(binding.ivCourseTutor);
            Glide.with(mContext).load(todaySessions.StudentProfileImage).placeholder(R.drawable.ic_user_placeholder)
                    .apply(new RequestOptions().override(600, 200)).into(binding.ivCurrentCourseTutor);
            int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            binding.layoutSessionTime.setBackgroundColor(randomAndroidColor);
            binding.tvUpcomingCourseName.setText(todaySessions.CategoryName);
            binding.tvCurrentCourseName.setText(todaySessions.CategoryName);
            binding.tvCurrentCourseLevel.setText(todaySessions.LevelName);
            binding.tvUpcomingCourseLevel.setText(todaySessions.LevelName);
            binding.tvCurrentCourseTutorName.setText(todaySessions.StudentName);
            binding.tvCourseTime.setText(todaySessions.time);
            binding.tvCourseDay.setText(todaySessions.Day);
            binding.tvCourseDate.setText(todaySessions.sessionDate);
            binding.btnStartClass.setOnClickListener(view -> onItemClickListener.onStartClass(getAdapterPosition()));
            // binding.tutorSession.setOnClickListener(view -> onItemClickListener.onStartClass(getAdapterPosition()));
            SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");

            try {

                SimpleDateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                Date d = f1.parse(todaySessions.time);
                SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");
                f2.format(Objects.requireNonNull(d)).toLowerCase();
                Date date = format1.parse(f2.format(d));
                String time = todaySessions.Day + " " + DateFormat.format("h:mm a", date);

                // ***** if time less than 24 hours make layoutTutorDetail gone and layoutScheduleTime visible
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE h:mm a");
                Date todaysDate = new Date();
                String currentTimeDay = sdf.format(todaysDate);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE hh:mm a");
                Date date1 = simpleDateFormat.parse(time);
                Date date2 = simpleDateFormat.parse(currentTimeDay);

                assert date1 != null;
                long difference = Objects.requireNonNull(date2).getTime() - date1.getTime();
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                hours = (hours < 0 ? -hours : hours);

                if (days == 0) {
                    if (hours <= Integer.parseInt(Constant.StartTimer)) {
                        int duration;
                        if (isAfterStarted){
                            duration = Integer.parseInt(Constant.EndStartClass);
                        }else {
                            duration = Integer.parseInt(Constant.ShowStartClass);
                        }
                        if (hours == 0 && min <= duration) {
                            binding.layoutStatusClass.setVisibility(View.VISIBLE);
                            binding.tvUpcomingSchedule.setVisibility(View.GONE);

                            //check difference between current and session time and
                            long Difference = getDifference(todaySessions);
                            Difference = (Difference < 0 ? -Difference : Difference);
                            isAfterStarted = true;
                            SessionTimeCountDown(Difference);

                        } else {
                            if (todaySessions.time.contains("am") || todaySessions.time.contains("pm")) {
                                todaySessions.time = DateTimeUtility.convertDateTimeFormate(todaySessions.time, "hh:mma", "hh:mm");
                            }
                            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                            Date dateSession = sdf1.parse(todaySessions.time);
                            Date dateCurrent = sdf1.parse(DateTimeUtility.currentDateTime("HH:mm"));

                            if (dateSession.compareTo(dateCurrent) > 0) {
                                System.out.println("Date1 is after Date2");
                                // start countdown
                                difference = (difference < 0 ? -difference : difference);
                                startCountDown(difference, binding.tvUpcomingCourseTimeHr, binding.tvUpcomingCourseTimeMIN, binding.tvUpcomingCourseTimeSec, todaySessions);
                                binding.layoutStatusClass.setVisibility(View.GONE);
                                binding.layoutScheduleTime.setVisibility(View.VISIBLE);
                                binding.layoutTutorDetail.setVisibility(View.GONE);
                            } else {
                                binding.layoutStatusClass.setVisibility(View.GONE);
                                binding.layoutScheduleTime.setVisibility(View.GONE);
                                binding.layoutTutorDetail.setVisibility(View.VISIBLE);
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

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void startCountDown(long millis, AppCompatTextView tvUpcomingCourseTimeHr, AppCompatTextView tvUpcomingCourseTimeMIN, AppCompatTextView tvUpcomingCourseTimeSec, TodaySessions day) {
            //1584700200 is timestamp in milii seconds (Friday, March 20, 2020 10:30:00 AM)
            //1000(1sec) is time interval to call onTick method
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

                    if (Integer.parseInt(hours.trim()) == 00 &&
                            Integer.parseInt(minutes.trim()) <= Integer.parseInt(Constant.ShowStartClass) &&
                            Integer.parseInt(seconds.trim()) == 00) {
                        Log.e("onTick", "onTick: " + hours + " ->" + minutes + " -> " + seconds);

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

                }
            }.start();
        }

        private void SessionTimeCountDown(long millis) {
            //1584700200 is timestamp in milii seconds (Friday, March 20, 2020 10:30:00 AM)
            //1000(1sec) is time interval to call onTick method
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

                    Log.e("onTick", "onTick: " + hours + " ->" + minutes + " -> " + seconds);

                    if (Integer.parseInt(hours.trim()) == 00 &&
                            Integer.parseInt(minutes.trim()) <= Integer.parseInt(Constant.EndStartClass) &&
                            Integer.parseInt(seconds.trim()) == 00) {
                        Log.e("onTick", "onTick: " + hours + " ->" + minutes + " -> " + seconds);

                        notifyItemChanged(getAdapterPosition());
                    }
                }

                @Override
                public void onFinish() {
                    binding.layoutStatusClass.setVisibility(View.GONE);
                    binding.tvUpcomingSchedule.setVisibility(View.VISIBLE);
                    isAfterStarted = false;
                }
            }.start();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private long getDifference(TodaySessions todaySessions) {
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)

        try {
            SimpleDateFormat f2 = new SimpleDateFormat("h:mm a");

            Date endTime = f1.parse(todaySessions.endTime);
            f2.format(endTime).toLowerCase();
            Date endDate = format1.parse(f2.format(endTime));
            String endDateTime = todaySessions.Day + " " + DateFormat.format("h:mm a", endDate);

            Date todaysDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE h:mm a");
            String currentTimeDay = sdf.format(todaysDate);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE hh:mm a");
            Date date1 = simpleDateFormat.parse(endDateTime);
            Date date2 = simpleDateFormat.parse(currentTimeDay);

            long difference = Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime();
            return difference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public interface OnItemClickListener {
        void onSessionReschedule(int position);

        void onSessionCancel(int position);

        void onStartClass(int position);
    }
}
