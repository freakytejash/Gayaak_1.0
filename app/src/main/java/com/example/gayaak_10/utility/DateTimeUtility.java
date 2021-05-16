    package com.example.gayaak_10.utility;


import android.os.Build;
import android.text.format.DateFormat;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class DateTimeUtility {

    private static String sessionDate;

    public static String currentDateTime() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        System.out.println("Date--->>> " + outputFormat.format(date));
        return outputFormat.format(date);
    }

    public static String currentDateTime(String format) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        System.out.println("Date--->>> " + outputFormat.format(date));
        return outputFormat.format(date);
    }

    public static String convertDateTimeFormatUtil(String strDate, String format) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }


    public static String convertDateTimeFormate(String strDate, String format, String output) {

        Date date = null;

        SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(output/*"yyyy-MM-dd'T'HH:mm:ss.SSS"*/, Locale.ENGLISH);

        try {
            date = inputFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);

        return formattedDate;
    }

    public static String formatDateToString(Date date, String format, String timeZone, String outputFormat) {
        // null check
        if (date == null) return null;
        // create SimpleDateFormat object with input format
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
      //  sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
      //  Log.e("apiTime", "formatDateToString: "+sdf.format(date));
        // return Date in required format with timezone as String
        return convertDateTimeFormate(sdf.format(date), format, outputFormat);

    }

    public static String convertDateStringFormat(String inputDate) {
        Date formattedDate = null;
        // SimpleDateFormat format = new SimpleDateFormat("dd", Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            //String to date
            formattedDate = format.parse(inputDate);
            System.out.println(formattedDate);

            String date = format.format(formattedDate);

            if (date.endsWith("1") && !date.endsWith("11"))
                format = new SimpleDateFormat("dd'ST' EEE");
            else if (date.endsWith("2") && !date.endsWith("12"))
                format = new SimpleDateFormat("dd'ND' EEE");
            else if (date.endsWith("3") && !date.endsWith("13"))
                format = new SimpleDateFormat("dd'RD' EEE");
            else
                format = new SimpleDateFormat("dd'TH' EEE");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format.format(formattedDate);
    }

    public static Date convertDateToString(String inputDate, String inputFormat) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(inputFormat);
        try {
            date = format.parse(inputDate);
            System.out.println(date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long getTimeInMilliseconds(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE h:mm a");
        try {
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(time);
            System.out.println("Given Time in milliseconds : " + date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : " + calendar.getTimeInMillis());

            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateFromWeekDay(String day) {
        ArrayList<LocalDate> arrayList = getDates(day);
        for (int j = 0; j < arrayList.size(); j++) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                String formattedString = arrayList.get(j).format(formatter);
             /*   if (!new SimpleDateFormat("dd LLLL yyyy").parse(formattedString).equals(new Date())) {
//                    Log.e("date", ": " + arrayList.get(j));
                    DateTimeFormatter sdf =  DateTimeFormatter.ofPattern("MMMM, dd");
                    sessionDate = arrayList.get(j).format(sdf);
                    return sessionDate;
                } else*/
                if (!new SimpleDateFormat("dd LLLL yyyy").parse(formattedString).before(new Date())) {
//                    Log.e("date", ": " + arrayList.get(j));
                    //DateTimeFormatter sdf = DateTimeFormatter.ofPattern("MMMM, dd");
                    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                    sessionDate = arrayList.get(j).format(sdf);
                    return sessionDate;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return sessionDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> getDates(String weekDay) {
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
            // Log.e("datesTest", "getDates: " +formatter.format(date) );
            dates.add(date);
        }


        /*YearMonth ym = YearMonth.of(year, month);  // Or ( 2017 , 1 ) for January.
        LocalDate firstOfMonth = ym.atDay(1);

        for (DayOfWeek dayOfWeek : daysOfWeek) {
            LocalDate ld = firstOfMonth.with(TemporalAdjusters.dayOfWeekInMonth(1, dayOfWeek));
            do {
                if (!dates.contains(ld)) {
                    dates.add(ld);
                }
                // Set up next loop.
                ld = ld.plusWeeks(1);
            } while (YearMonth.from(ld).equals(ym));  // While in the targeted month.
//            System.out.println("  dates: " + dates);
            return dates;
        }*/
        return dates;
    }

    public static Date convertDateFormat(String input, String time) {
        Date formattedTime = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat(input);
        format1.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
        try {
            formattedTime = format1.parse(time);
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String convertDateStringFormat(String input, Date time) {
        String formattedTime = "";
        formattedTime = DateFormat.format(input, time).toString();
        return formattedTime;
    }

}





  /* String dtStart = liveClassDataContractList.date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);

                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Mon
                String day = (String) DateFormat.format("dd", date); // 20
                String monthString = (String) DateFormat.format("MMMM", date); // Jun
                String monthNumber = (String) DateFormat.format("MM", date); // 06
                String year = (String) DateFormat.format("yyyy", date); // 2013


                binding.tvClassWeek.setText(dayOfTheWeek);
                binding.tvClassMonth.setText(monthString + " " + day);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
