package com.bkav.aiotcloud.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CameraItem  implements Serializable {
    public CameraItem(JSONObject cameraItem){
        try {
            Log.e("cameraIteam", cameraItem.toString() );
            this.cameraId = cameraItem.getInt("cameraId");
            this.cameraName = cameraItem.getString("cameraName");
            this.regionId = cameraItem.getInt("regionId");
            this.regionName = cameraItem.getString("regionName");
            this.peerID = cameraItem.getString("peerID");
            this.isConnected = cameraItem.getInt("isConnected");
            this.deviceType = cameraItem.getInt("deviceType");
            this.snapShotUrl = cameraItem.getString("snapShotUrl");
            this.avatar = cameraItem.getString("avatar");
            this.isPtzDevice = cameraItem.getBoolean("isPtzDevice");
            this.isZoom = cameraItem.getBoolean("isZoom");
            this.isWifi = cameraItem.getBoolean("isWifi");
            this.isGPS = cameraItem.getBoolean("isGPS");
            this.refactoring = cameraItem.getBoolean("refactoring");
            this.isLive = cameraItem.getBoolean("isLive");
            this.isPlayback = cameraItem.getBoolean("isPlayback");
            this.isAiBox = cameraItem.getBoolean("isAiBox");
            this.is5G = cameraItem.getBoolean("is5G");
            this.boxId = cameraItem.getInt("boxId");
            if (!cameraItem.isNull("cameraInfo")){
                Log.e("cameraInfor" , cameraItem.getString("cameraInfo"));
                this.cameraInfo = cameraItem.getString("cameraInfo");
            }
            this.mediaProfile = cameraItem.getJSONObject("mediaProfile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private int cameraId;
    private String cameraName;
    private int regionId;

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

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public int getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(int isConnected) {
        this.isConnected = isConnected;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getSnapShotUrl() {
        return snapShotUrl;
    }

    public void setSnapShotUrl(String snapShotUrl) {
        this.snapShotUrl = snapShotUrl;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isPtzDevice() {
        return isPtzDevice;
    }

    public void setPtzDevice(boolean ptzDevice) {
        isPtzDevice = ptzDevice;
    }

    public boolean isZoom() {
        return isZoom;
    }

    public void setZoom(boolean zoom) {
        isZoom = zoom;
    }

    public boolean isWifi() {
        return isWifi;
    }

    public void setWifi(boolean wifi) {
        isWifi = wifi;
    }

    public boolean isGPS() {
        return isGPS;
    }

    public void setGPS(boolean GPS) {
        isGPS = GPS;
    }

    public boolean isRefactoring() {
        return refactoring;
    }

    public void setRefactoring(boolean refactoring) {
        this.refactoring = refactoring;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isPlayback() {
        return isPlayback;
    }

    public void setPlayback(boolean playback) {
        isPlayback = playback;
    }
    public boolean isAiBox() {
        return isAiBox;
    }

    public void setAiBox(boolean aiBox) {
        isAiBox = aiBox;
    }

    public boolean isIs5G() {
        return is5G;
    }

    public void setIs5G(boolean is5G) {
        this.is5G = is5G;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public String getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(String cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    public JSONObject getMediaProfile() {
        return mediaProfile;
    }

    public void setMediaProfile(JSONObject mediaProfile) {
        this.mediaProfile = mediaProfile;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    private String regionName;
    private String peerID;
    private int isConnected;
    private int deviceType;
    private String snapShotUrl;
    private String avatar;
    private boolean isPtzDevice;
    private boolean isZoom;
    private boolean isWifi;
    private boolean isGPS;
    private boolean refactoring;
    private boolean isLive;
    private boolean isPlayback;
    private boolean isChoose;
    private boolean isAiBox;
    private boolean is5G;
    private int boxId;
    private String cameraInfo = "";
    private JSONObject mediaProfile;
}
