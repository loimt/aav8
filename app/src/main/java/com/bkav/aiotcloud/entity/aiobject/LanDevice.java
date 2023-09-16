package com.bkav.aiotcloud.entity.aiobject;

import org.json.JSONException;
import org.json.JSONObject;

public class LanDevice {
    private String scopes;
    private String addrs;
    private String model;

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getMetaVers() {
        return metaVers;
    }

    public void setMetaVers(int metaVers) {
        this.metaVers = metaVers;
    }

    private String serialNumber;
    private int metaVers;
    public LanDevice(JSONObject lanDevice) {
        try{
            this.scopes = lanDevice.getString("Scopes");
            this.addrs = lanDevice.getString("XAddrs");
            this.model = lanDevice.getString("Model");
            this.serialNumber = lanDevice.getString("SerialNumber");
            this.metaVers = lanDevice.getInt("MetadataVersion");
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
