package com.bkav.aiotcloud.entity.customer;

import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;

import org.json.JSONException;
import org.json.JSONObject;

public class TypeCustomerItem implements TypeAIObject {

    public TypeCustomerItem(JSONObject customerItem) {
        try {
            this.customerTypeId = customerItem.getInt("customerTypeId");
            this.customerTypeName = customerItem.getString("customerTypeName");
            this.description = customerItem.getString("description");
            this.backGroundColor = customerItem.getString("backGroundColor");
            this.filePathBackGroundImage = customerItem.getString("filePathBackGroundImage");
            this.ownerByUserId = customerItem.getInt("ownerByUserId");
            this.isUnknow = customerItem.getBoolean("isUnknow");
            this.objectGuid = customerItem.getString("objectGuid");
            this.identity = customerItem.getString("identity");
            this.handerTypeNotification = customerItem.getString("handerTypeNotification");
            this.active = customerItem.getBoolean("active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    public int getCustomerTypeId() {
//        return customerTypeId;
//    }

    public void setCustomerTypeId(int customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

//    public String getCustomerTypeName() {
//        return customerTypeName;
//    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

//    public String getDescription() {
//        return description;
//    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePathBackGroundImage() {
        return filePathBackGroundImage;
    }

    public void setFilePathBackGroundImage(String filePathBackGroundImage) {
        this.filePathBackGroundImage = filePathBackGroundImage;
    }

//    public String getBackGroundColor() {
//        return backGroundColor;
//    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public int getOwnerByUserId() {
        return ownerByUserId;
    }

    public void setOwnerByUserId(int ownerByUserId) {
        this.ownerByUserId = ownerByUserId;
    }

    public boolean isUnknow() {
        return isUnknow;
    }

    public void setUnknow(boolean unknow) {
        isUnknow = unknow;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

//    public String getIdentity() {
//        return identity;
//    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getHanderTypeNotification() {
        return handerTypeNotification;
    }

    public void setHanderTypeNotification(String handerTypeNotification) {
        this.handerTypeNotification = handerTypeNotification;
    }

    @Override
    public int getID() {
       return customerTypeId;
    }

    @Override
    public String getName() {
        return customerTypeName;
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

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    private int ownerByUserId;
    private boolean isUnknow;
    private String objectGuid;
    private String identity;
    private String handerTypeNotification;
    private boolean active;
    private int customerTypeId;
    private String customerTypeName;
    private String description;
    private String filePathBackGroundImage;
    private String backGroundColor;
    private boolean isChoose = false;

}

