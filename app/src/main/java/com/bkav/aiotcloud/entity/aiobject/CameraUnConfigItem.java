package com.bkav.aiotcloud.entity.aiobject;

import org.json.JSONException;
import org.json.JSONObject;

public class CameraUnConfigItem implements CameraConfig{

    public CameraUnConfigItem(JSONObject jsonObject){
        try {
            this.cameraId = jsonObject.getInt("cameraId");
            this.cameraName = jsonObject.getString("cameraName");
            this.userId = jsonObject.getInt("userId");
            this.peerID = jsonObject.getString("peerID");

            this.snapShotUrl = jsonObject.getString("snapShotUrl");
            this.isActive = jsonObject.getInt("isActive");
            this.refactoring = jsonObject.getBoolean("refactoring");
            this.cameraInfo = jsonObject.getString("cameraInfo");
            this.parentId = jsonObject.getInt("parentId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    @Override
    public String getSnapShotUrl() {
        return snapShotUrl;
    }

    public void setSnapShotUrl(String snapShotUrl) {
        this.snapShotUrl = snapShotUrl;
    }

    public int getIsActive() {
        return isActive;
    }

    public boolean isRefactoring() {
        return refactoring;
    }

    public void setRefactoring(boolean refactoring) {
        this.refactoring = refactoring;
    }

    public String getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(String cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    private int  cameraId;
    private String  cameraName;
    private int  userId;
    private String  peerID;
    private String  snapShotUrl;

    private int  isActive;
    private boolean  refactoring;
    private String  cameraInfo;
    private int  parentId;

}

