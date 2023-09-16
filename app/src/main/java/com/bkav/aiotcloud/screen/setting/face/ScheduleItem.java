package com.bkav.aiotcloud.screen.setting.face;

import android.util.Log;

import com.bkav.aiotcloud.language.LanguageManager;

import java.io.Serializable;

public class ScheduleItem implements Serializable {
    public ScheduleItem(String timeStart, String timeEnd, String days){
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
        this.days = days;
    }

    public String getDateConverse(){
        String dates = "";
        dates = this.days.replace("]", "").replace("[", "").replace(" ", "");
        Log.e("date", dates);
        if (dates.length() > 0){
            String days[] = dates.split(",");
            String date = "";
            for (int dayIndex = 0; dayIndex < days.length; dayIndex++) {
                if (days[dayIndex].equals("0")) {
                    date = date.concat(LanguageManager.getInstance().getValue("sun")).concat(", ");
                } else if (days[dayIndex].equals("1")) {
                    date = date.concat(LanguageManager.getInstance().getValue("mon")).concat(", ");
                } else if (days[dayIndex].equals("2")) {
                    date = date.concat(LanguageManager.getInstance().getValue("tue")).concat(", ");
                } else if (days[dayIndex].equals("3")) {
                    date = date.concat(LanguageManager.getInstance().getValue("wed")).concat(", ");
                } else if (days[dayIndex].equals("4")) {
                    date = date.concat(LanguageManager.getInstance().getValue("thu")).concat(", ");
                } else if (days[dayIndex].equals("5")) {
                    date = date.concat(LanguageManager.getInstance().getValue("fri")).concat(", ");
                } else if (days[dayIndex].equals("6")) {
                    date = date.concat(LanguageManager.getInstance().getValue("sat")).concat(", ");
                }
            }

            date = date.substring(0, date.length() - 2);
            return date;
        } else {
            return "";
        }

    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }


    private String timeStart;
    private String timeEnd;
    private String days;
}
