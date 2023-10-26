package com.bkav.aiotcloud.entity.license;

import org.json.JSONException;
import org.json.JSONObject;

public class LicenseItem {
    public LicenseItem(JSONObject item){
        try {
            this.licensePlateGuid = item.getString("licensePlateGuid");
            this.noPlate = item.getString("noPlate");
            if (!item.isNull("description")){
                this.description = item.getString("description");
            }
            this.licensePlateTypeGuid = item.getString("licensePlateTypeGuid");
            this.licensePlateTypeColor = item.getString("licensePlateTypeColor");
            this.objectDetectTypeName = item.getString("objectDetectTypeName");
            this.licensePlateTypeName = item.getString("licensePlateTypeName");
            this.objectDetectTypeIdentity = item.getString("objectDetectTypeIdentity");

            this.ownerByUserId = item.getInt("ownerByUserId");
            this.objectDetectTypeId = item.getInt("objectDetectTypeId");
            this.licensePlateTypeId = item.getInt("licensePlateTypeId");
            this.objectDetectTypeTransId = item.getInt("objectDetectTypeTransId");
            this.isUnknow = item.getBoolean("isUnknow");
            this.active = item.getBoolean("active");
            this.isDefault = item.getBoolean("isDefault");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLicensePlateGuid() {
        return licensePlateGuid;
    }

    public void setLicensePlateGuid(String licensePlateGuid) {
        this.licensePlateGuid = licensePlateGuid;
    }

    public String getNoPlate() {
        return noPlate;
    }

    public void setNoPlate(String noPlate) {
        this.noPlate = noPlate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicensePlateTypeGuid() {
        return licensePlateTypeGuid;
    }

    public void setLicensePlateTypeGuid(String licensePlateTypeGuid) {
        this.licensePlateTypeGuid = licensePlateTypeGuid;
    }

    public String getLicensePlateTypeColor() {
        return licensePlateTypeColor;
    }

    public void setLicensePlateTypeColor(String licensePlateTypeColor) {
        this.licensePlateTypeColor = licensePlateTypeColor;
    }

    public String getObjectDetectTypeName() {
        return objectDetectTypeName;
    }

    public void setObjectDetectTypeName(String objectDetectTypeName) {
        this.objectDetectTypeName = objectDetectTypeName;
    }

    public String getLicensePlateTypeName() {
        return licensePlateTypeName;
    }

    public void setLicensePlateTypeName(String licensePlateTypeName) {
        this.licensePlateTypeName = licensePlateTypeName;
    }

    public String getObjectDetectTypeIdentity() {
        return objectDetectTypeIdentity;
    }

    public void setObjectDetectTypeIdentity(String objectDetectTypeIdentity) {
        this.objectDetectTypeIdentity = objectDetectTypeIdentity;
    }

    public int getOwnerByUserId() {
        return ownerByUserId;
    }

    public void setOwnerByUserId(int ownerByUserId) {
        this.ownerByUserId = ownerByUserId;
    }

    public int getObjectDetectTypeId() {
        return objectDetectTypeId;
    }

    public void setObjectDetectTypeId(int objectDetectTypeId) {
        this.objectDetectTypeId = objectDetectTypeId;
    }

    public int getLicensePlateTypeId() {
        return licensePlateTypeId;
    }

    public void setLicensePlateTypeId(int licensePlateTypeId) {
        this.licensePlateTypeId = licensePlateTypeId;
    }

    public int getObjectDetectTypeTransId() {
        return objectDetectTypeTransId;
    }

    public void setObjectDetectTypeTransId(int objectDetectTypeTransId) {
        this.objectDetectTypeTransId = objectDetectTypeTransId;
    }

    public boolean isUnknow() {
        return isUnknow;
    }

    public void setUnknow(boolean unknow) {
        isUnknow = unknow;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }


    private String licensePlateGuid;
    private String noPlate;
    private String description;
    private String licensePlateTypeGuid;
    private String licensePlateTypeColor;
    private String objectDetectTypeName;
    private String licensePlateTypeName;
    private String objectDetectTypeIdentity;
    private int ownerByUserId;
    private int objectDetectTypeId;
    private int licensePlateTypeId;
    private int objectDetectTypeTransId;
    private boolean isUnknow;
    private boolean active;
    private boolean isDefault;
//      "licensePlateGuid": "e6aadb5f-250e-4c52-8f99-ab599d6368dc",
//              "noPlate": "29g1 63263",
//              "description": null,
//              "ownerByUserId": 181,
//              "licensePlateTypeGuid": "98c4025f-6686-11ee-883b-069e660155e4",
//              "objectDetectTypeId": 5,
//              "active": true,
//              "licensePlateTypeId": 6,
//              "licensePlateTypeName": "Unknown",
//              "objectDetectTypeTransId": 9,
//              "objectDetectTypeName": "Motor",
//              "objectDetectTypeIdentity": "motor",
//              "isUnknow": false,
//              "isDefault": false,
//              "licensePlateTypeColor": "#818181"
}
