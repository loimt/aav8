package com.bkav.aiotcloud.entity.aiobject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class AIObject implements Serializable, CameraConfig {

    public AIObject(JSONObject aiObjectItem){
        this.zones = new ArrayList<>();
        try {
            this.cameraId = aiObjectItem.getInt("cameraId");
            this.monitorId = aiObjectItem.getString("monitorGuid");
            this.cameraName = aiObjectItem.getString("cameraName");
            this.scheduleId = aiObjectItem.getString("scheduleGuid");
            this.statusProcess = aiObjectItem.getInt("isActiveAI");
            this.regionName = aiObjectItem.getString("regionName");
            this.isActive = aiObjectItem.getInt("isActive");
            this.peerID = aiObjectItem.getString("peerID");
            this.snapShotUrl = aiObjectItem.getString("snapShotUrl");
            this.objectGuid = aiObjectItem.getString("objectGuid");
            this.refactoring = aiObjectItem.getBoolean("refactoring");
            if (aiObjectItem.has("zones") && !aiObjectItem.isNull("zones")){
                this.zones = getZones(aiObjectItem.getJSONArray("zones"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ZoneAIObject> getZones(JSONArray jsonArray){
        ArrayList<ZoneAIObject> zones = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++){
            try {
                ZoneAIObject zoneAIObject = new ZoneAIObject(jsonArray.getJSONObject(index));
                zones.add(zoneAIObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return zones;
    }

    @Override
    public String getSnapShotUrl() {
        return this.snapShotUrl;
    }

    @Override
    public String getPeerID() {
        return this.peerID;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    private String  monitorId;
    private int  cameraId;
    private String  cameraName;
    private String  scheduleId;
    private int  statusProcess;
    private String  regionName;
    private int  isActive;
    private String  peerID;
    private String  snapShotUrl;
    private String  objectGuid;
    private boolean  refactoring;
    private ArrayList<ZoneAIObject> zones;

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

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStatusProcess() {
        return statusProcess;
    }

    public void setStatusProcess(int statusProcess) {
        this.statusProcess = statusProcess;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public void setSnapShotUrl(String snapShotUrl) {
        this.snapShotUrl = snapShotUrl;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public boolean isRefactoring() {
        return refactoring;
    }

    public void setRefactoring(boolean refactoring) {
        this.refactoring = refactoring;
    }

    public ArrayList<ZoneAIObject> getZones() {
        return zones;
    }

    public void setZones(ArrayList<ZoneAIObject> zones) {
        this.zones = zones;
    }

}
