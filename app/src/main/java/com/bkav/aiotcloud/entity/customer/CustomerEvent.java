package com.bkav.aiotcloud.entity.customer;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerEvent {
   private int cameraId;
   private String cameraName;
   private String startedAt;
   private String endedAt;
   private String eventId;
   private String filePath;


    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(String endedAt) {
        this.endedAt = endedAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public CustomerEvent(JSONObject customerItem) {
        try {
            this.cameraId = customerItem.getInt("cameraId");
            this.cameraName = customerItem.getString("cameraName");
            this.startedAt = customerItem.getString("startedAt");
            this.endedAt = customerItem.getString("endedAt");
            this.eventId = customerItem.getString("eventId");
            this.filePath = customerItem.getString("filePath");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
