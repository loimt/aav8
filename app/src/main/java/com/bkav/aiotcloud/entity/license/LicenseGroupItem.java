package com.bkav.aiotcloud.entity.license;

import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;

import org.json.JSONException;
import org.json.JSONObject;

public class LicenseGroupItem implements TypeAIObject {
    public LicenseGroupItem(JSONObject item){
        try {
            this.licensePlateTypeId = item.getInt("licensePlateTypeId");
            this.objectGuid = item.getString("objectGuid");
            this.licensePlateTypeName = item.getString("licensePlateTypeName");
            if (!item.isNull("description")){
                this.description = item.getString("description");
            }
            this.ownerByUserId = item.getInt("ownerByUserId");
            this.active = item.getBoolean("active");
            this.isUnknow = item.getBoolean("isUnknow");
            this.isDefault = item.getBoolean("isDefault");
            this.identity = item.getString("identity");
            this.backGroundColor = item.getString("backGroundColor");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
//    public int getLicensePlateTypeId() {
//        return licensePlateTypeId;
//    }

    public void setLicensePlateTypeId(int licensePlateTypeId) {
        this.licensePlateTypeId = licensePlateTypeId;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

//    public String getLicensePlateTypeName() {
//        return licensePlateTypeName;
//    }

    public void setLicensePlateTypeName(String licensePlateTypeName) {
        this.licensePlateTypeName = licensePlateTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerByUserId() {
        return ownerByUserId;
    }

    public void setOwnerByUserId(int ownerByUserId) {
        this.ownerByUserId = ownerByUserId;
    }

    @Override
    public int getID() {
        return licensePlateTypeId;
    }

    @Override
    public String getName() {
        return licensePlateTypeName;
    }

    @Override
    public String getBackground() {
        return backGroundColor;
    }

    @Override
    public String getDesciption() {
        return description;
    }

    @Override
    public String getIdentify() {
        return identity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public boolean isUnknow() {
        return isUnknow;
    }

    public void setUnknow(boolean unknow) {
        isUnknow = unknow;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    private int licensePlateTypeId;
    private String objectGuid;

    private boolean isChoose = false;
    private String licensePlateTypeName;
    private String description = "";
    private int ownerByUserId;
    private boolean active;
    private String identity;
    private String backGroundColor;
    private boolean isUnknow;
    private boolean isDefault;
//     "licensePlateTypeId": 6,
//             "objectGuid": "98c4025f-6686-11ee-883b-069e660155e4",
//             "licensePlateTypeName": "Unknown",
//             "description": "Không xác định",
//             "ownerByUserId": 181,
//             "active": true,
//             "identity": "",
//             "backGroundColor": "#818181",
//             "isUnknow": true,
//             "isDefault": true
}
