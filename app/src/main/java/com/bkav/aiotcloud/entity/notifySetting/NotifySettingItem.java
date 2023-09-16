package com.bkav.aiotcloud.entity.notifySetting;

import org.json.JSONException;
import org.json.JSONObject;

public class NotifySettingItem {
    private int id;
    private String notifyContent;
    private boolean isActive;
    private boolean aiBox;
    private int parentId;
    private String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAiBox() {
        return aiBox;
    }

    public void setAiBox(boolean aiBox) {
        this.aiBox = aiBox;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public NotifySettingItem(JSONObject notifySettingItem) {
        try{
            this.id = notifySettingItem.getInt("id");
            this.notifyContent = notifySettingItem.getString("notifyContent");
            this.isActive = notifySettingItem.getBoolean("isActive");
            this.aiBox = notifySettingItem.getBoolean("aiBox");
            this.parentId = notifySettingItem.getInt("parentId");
            this.color = notifySettingItem.getString("color");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
