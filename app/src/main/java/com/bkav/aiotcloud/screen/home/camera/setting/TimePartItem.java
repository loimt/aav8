package com.bkav.aiotcloud.screen.home.camera.setting;

import com.bkav.aiotcloud.view.TimeRuleView;

public class TimePartItem {
    public TimePartItem(int startTime, int endTime, String tokenFile){
     this.startTime = startTime;
     this.endTime = endTime;
     this.tokenFile = tokenFile;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getTokenFile() {
        return tokenFile;
    }

    public void setTokenFile(String tokenFile) {
        this.tokenFile = tokenFile;
    }

    private int startTime;
    private int endTime;
    private String tokenFile;

}
