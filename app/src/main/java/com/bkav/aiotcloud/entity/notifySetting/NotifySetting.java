package com.bkav.aiotcloud.entity.notifySetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotifySetting {
    private boolean isActive;
    private String identity;
    private List<NotifySettingItem> listSettingItem;

    public NotifySetting(JSONObject NotifySetting) {
        try{
            this.isActive = NotifySetting.getBoolean("isActive");
            this.identity = NotifySetting.getString("identity");
            this.listSettingItem = (List<NotifySettingItem>) NotifySetting.getJSONArray("data");
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}
