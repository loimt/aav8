package com.bkav.aiotcloud.entity.device;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceItem {
    public DeviceItem(JSONObject deviceItem) {
        try {
            this.cameraId = deviceItem.getInt("cameraId");
            this.cameraName = deviceItem.getString("cameraName");
            this.peerID = deviceItem.getString("peerID");
            this.isConnected = deviceItem.getInt("isConnected");
            this.refactoring = deviceItem.getBoolean("refactoring");
            this.regionId = deviceItem.getInt("regionId");
            this.snapShotUrl = deviceItem.getString("snapShotUrl");
            this.regionName = deviceItem.getString("regionName");
            this.deviceType = deviceItem.getInt("deviceType");
            this.avatar = deviceItem.getString("avatar");
            this.isPtzDevice = deviceItem.getBoolean("isPtzDevice");
            this.isZoom = deviceItem.getBoolean("isZoom");
            this.isWifi = deviceItem.getBoolean("isWifi");
            this.isGPS = deviceItem.getBoolean("isGPS");
            this.isVms = deviceItem.getBoolean("isVms");
            this.is5G = deviceItem.getBoolean("is5G");
            this.direction = deviceItem.getString("direction");
            this.isLive = deviceItem.getBoolean("isLive");
            this.isPlayback = deviceItem.getBoolean("isPlayback");
            this.boxId = deviceItem.getInt("boxId");
            this.cameraInfo = deviceItem.getString("cameraInfo");
            this.mediaProfile = deviceItem.getJSONObject("mediaProfile");
            this.coordinates = deviceItem.getString("coordinates");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
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

    public boolean isRefactoring() {
        return refactoring;
    }

    public void setRefactoring(boolean refactoring) {
        this.refactoring = refactoring;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getSnapShotUrl() {
        return snapShotUrl;
    }

    public void setSnapShotUrl(String snapShotUrl) {
        this.snapShotUrl = snapShotUrl;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
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

    public boolean isVms() {
        return isVms;
    }

    public void setVms(boolean vms) {
        isVms = vms;
    }

    public boolean isIs5G() {
        return is5G;
    }

    public void setIs5G(boolean is5G) {
        this.is5G = is5G;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    private int cameraId;
    private String cameraName;
    private String peerID;
    private int isConnected;
    private boolean refactoring;
    private int regionId;
    private String snapShotUrl;
    private String regionName;
    private int deviceType;
    private String avatar;
    private boolean isPtzDevice;
    private boolean isZoom;
    private boolean isWifi;
    private boolean isGPS;
    private boolean isVms;
    private boolean is5G;
    private String direction;
    private boolean isLive;
    private boolean isPlayback;
    private int boxId;
    private String cameraInfo;
    private JSONObject mediaProfile;
    private String coordinates;
}
