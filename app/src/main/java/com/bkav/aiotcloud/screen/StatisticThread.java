package com.bkav.aiotcloud.screen;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bkav.aiotcloud.application.ApplicationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticThread extends Thread{

    public boolean isRunning = true;
    @Override
    public void run() {
        super.run();
        while (isRunning){
            try {
                    getStatisticEvent();
                Thread.sleep(1000 * 60*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isRunning = false;
    }

    public void getStatisticEvent(){
        ApplicationService.requestManager.getStatisticFR();
        ApplicationService.requestManager.getStatisticLicen(payloadStatistic(ApplicationService.licenseplate), ApplicationService.URL_GET_LIST_STATISTIC);
        ApplicationService.requestManager.getStatisticsIntru(payloadStatistic(ApplicationService.accessdetect), ApplicationService.URL_GET_TOTAL_EVENT_IN_DAY);
        ApplicationService.requestManager.getStatisticsPede(payloadStatistic(ApplicationService.persondetection), ApplicationService.URL_GET_LIST_STATISTIC);
        ApplicationService.requestManager.getStatisticsFide(payloadStatistic(ApplicationService.firedetect), ApplicationService.URL_GET_LIST_STATISTIC);
    }
    private JSONObject payloadStatistic(String model){
        JSONObject payload = new JSONObject();
        try {
            payload.put("appIdentity", model);
            payload.put("pageSize", 1000);
            payload.put("pageIndex", 1);
            payload.put("fromDate", getTimeStart());
            payload.put("toDate", getTimeEnd());
            payload.put("listCamera", "");
            payload.put("listDetectType", "");
            payload.put("showType", "day");
            payload.put("optionTime", 1);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return payload;
    }
    private String getTimeEnd() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + "T23:59:59+07:00";
        return date;
    }

    private String getTimeStart() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(Calendar.getInstance().getTime()) + "T00:00:00+07:00";
        return date;
    }

}
