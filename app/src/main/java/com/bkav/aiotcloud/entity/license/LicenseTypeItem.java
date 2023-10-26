package com.bkav.aiotcloud.entity.license;

import org.json.JSONException;
import org.json.JSONObject;

public class LicenseTypeItem {
    public LicenseTypeItem(JSONObject item){
        try {
            objectDetectTypeName = item.getString("objectDetectTypeName");
            color = item.getString("color");
            identity = item.getString("identity");
            objectDetectTypeId = item.getInt("objectDetectTypeId");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getObjectDetectTypeId() {
        return objectDetectTypeId;
    }

    public void setObjectDetectTypeId(int objectDetectTypeId) {
        this.objectDetectTypeId = objectDetectTypeId;
    }

    public String getObjectDetectTypeName() {
        return objectDetectTypeName;
    }

    public void setObjectDetectTypeName(String objectDetectTypeName) {
        this.objectDetectTypeName = objectDetectTypeName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    private int objectDetectTypeId;
    private String objectDetectTypeName;
    private String color;
    private String identity;
}
