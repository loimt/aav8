package com.bkav.aiotcloud.config;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bkav.aiotcloud.language.LanguageManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTimeFormat {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_ROOTH = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String TIME_FORMAT = "HH:mm:ss - dd/MM/yyyy";;
    public static String getTimeFormat(String time, String formatInput, String formatOutput){
        if (time.length()==0){
            return "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(formatInput);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = isoFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(formatOutput);
        String requiredDate = df.format(date);
        return requiredDate;
    }

    public static String getTimeFormatRelative(String time, String formatInput, String formatOutput){
        if (time.length()==0){
            return "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(formatInput);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = isoFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(formatOutput);
        Date timeNow = new Date();

        Calendar calendar = Calendar.getInstance();
        assert date != null;
        calendar.setTimeInMillis(timeNow.getTime() - date.getTime());
        int mYear = calendar.get(Calendar.YEAR) -1970;
        int mMonth = calendar.get(Calendar.MONTH);
        long mDay = TimeUnit.MILLISECONDS.toDays(timeNow.getTime() - date.getTime());
        int mWeek = (calendar.get(Calendar.DAY_OF_MONTH)-1)/7;
        long hr = TimeUnit.MILLISECONDS.toHours(timeNow.getTime() - date.getTime());
        long min = TimeUnit.MILLISECONDS.toMinutes(timeNow.getTime() - date.getTime());
        long sec = TimeUnit.MILLISECONDS.toSeconds(timeNow.getTime() - date.getTime());
        if (mYear > 0){
            return String.format(formatTime, mYear, LanguageManager.getInstance().getValue("year_ago"));
        }
        if (mMonth > 0){
            return String.format(formatTime, mMonth, LanguageManager.getInstance().getValue("month_ago"));
        }
        if (mWeek > 0){
            return String.format(formatTime, mWeek, LanguageManager.getInstance().getValue("week_ago"));
        }
        if (mDay > 0){
            return String.format(formatTime, mDay, LanguageManager.getInstance().getValue("day_ago"));
        }

        if (hr > 0){
            return String.format(formatTime, hr, LanguageManager.getInstance().getValue("hour_ago"));
        }

        if (min > 0){
            return String.format(formatTime, min, LanguageManager.getInstance().getValue("minutes_ago"));
        }
        if (sec < 6 ){
            return LanguageManager.getInstance().getValue("just_now");
        }
        return String.format(formatTime, sec, LanguageManager.getInstance().getValue("second_ago"));
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        Log.e("time format",  "d " + TimeUnit.MILLISECONDS.toDays(diff));
        return TimeUnit.MILLISECONDS.toDays(diff);
    }
    public static String converseTimeRooth(String time, String formatInput, String formatOutput){
        if (time.length()==0){
            return "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(formatInput);
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = isoFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(formatOutput);
        String requiredDate = df.format(date);
        return requiredDate;
    }

    public static String converseSecondToTime(int time, String formatOutput){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(formatOutput);
        TimeZone zone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        isoFormat.setTimeZone(zone);
        String dateString = isoFormat.format(new Date(time * 1000L));
        return dateString;
    }

    public static String formatTime = "%d %s";
}
