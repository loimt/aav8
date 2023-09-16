package com.bkav.aiotcloud.entity.notify;

import org.json.JSONException;
import org.json.JSONObject;

public class NotifyItem {


    public NotifyItem(JSONObject cameraItem){
        try {
            this.eventId = cameraItem.getInt("eventId");
            this.eventName = cameraItem.getString("eventName");
            if (!cameraItem.isNull("content")){
                this.content = cameraItem.getString("content");
            }
            this.seen = cameraItem.getBoolean("seen");
            this.iconNoti = cameraItem.getString("iconNoti");
            this.color = cameraItem.getString("color");
            this.profileId = cameraItem.getInt("profileId");
            this.objGuid = cameraItem.getString("objGuid");
            this.identity = cameraItem.getString("identity");
            this.cameraName = cameraItem.getString("cameraName");
            this.createdAt = cameraItem.getString("createdAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIconNoti() {
        return iconNoti;
    }

    public void setIconNoti(String iconNoti) {
        this.iconNoti = iconNoti;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getObjGuid() {
        return objGuid;
    }

    public void setObjGuid(String objGuid) {
        this.objGuid = objGuid;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    private String color;
    private int profileId;
    private String objGuid;
    private String identity;
    private String cameraName;
    private int eventId;
    private String eventName;
    private String content = "";
    private boolean seen;
    private String iconNoti;
    private String createdAt;

}
