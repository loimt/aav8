package com.bkav.aiotcloud.entity.customer;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerItem {

    public CustomerItem(JSONObject customerItem) {
        try {
            if (!customerItem.isNull("customerId")){
                this.customerId = customerItem.getInt("customerId");
            } else {
                this.customerId = customerItem.getInt("profileId");
            }
            if (!customerItem.isNull("timeDetect")){
                this.timeDetect = customerItem.getString("timeDetect");
            }
            this.customerTypeId = customerItem.getInt("customerTypeId");
            this.customerTypeName = customerItem.getString("customerTypeName");
            this.backGroundColor = customerItem.getString("backGroundColor");
            this.fullName = customerItem.getString("fullName");
            this.thumbnailFilePath = customerItem.getString("thumbnailFilePath");
            this.code = customerItem.getString("code");
            this.title = customerItem.getString("title");
            this.active = customerItem.getInt("active");
            this.address = customerItem.getString("address");
            this.dateOfBirth = customerItem.getString("dateOfBirth");
            this.email = customerItem.getString("email");
            this.phone = customerItem.getString("phone");
            this.gender = customerItem.getInt("gender");
            this.description = customerItem.getString("description");
            this.avatarImageId = customerItem.getInt("avatarImageId");
            this.avatarFilePath = customerItem.getString("avatarFilePath");
            this.thumbnailImageId = customerItem.getInt("thumbnailImageId");
            this.objectGuid = customerItem.getString("objectGuid");
            this.eventId = customerItem.getInt("eventId");
            this.cameraId = customerItem.getInt("cameraId");
            this.cameraName = customerItem.getString("cameraName");
            this.isUnknow = customerItem.getBoolean("isUnknow");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(int customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getBackGroundColor() {
        return backGroundColor;
    }

    public void setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvatarImageId() {
        return avatarImageId;
    }

    public void setAvatarImageId(int avatarImageId) {
        this.avatarImageId = avatarImageId;
    }

    public String getAvatarFilePath() {
        return avatarFilePath;
    }

    public void setAvatarFilePath(String avatarFilePath) {
        this.avatarFilePath = avatarFilePath;
    }

    public int getThumbnailImageId() {
        return thumbnailImageId;
    }

    public void setThumbnailImageId(int thumbnailImageId) {
        this.thumbnailImageId = thumbnailImageId;
    }

    public String getThumbnailFilePath() {
        return thumbnailFilePath;
    }

    public void setThumbnailFilePath(String thumbnailFilePath) {
        this.thumbnailFilePath = thumbnailFilePath;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getTimeDetect() {
        return timeDetect;
    }

    public void setTimeDetect(String timeDetect) {
        this.timeDetect = timeDetect;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public boolean isUnknow() {
        return isUnknow;
    }

    public void setUnknow(boolean unknow) {
        isUnknow = unknow;
    }

    private String avatarFilePath;
    private int thumbnailImageId;
    private String thumbnailFilePath;
    private String objectGuid;
    private int eventId;
    private int cameraId;
    private String timeDetect;
    private String cameraName;
    private boolean isUnknow;

    private int customerId;
    private int customerTypeId;
    private String customerTypeName;
    private String backGroundColor;
    private String code;
    private String title;
    private String fullName;
    private int active;
    private String address;
    private String dateOfBirth;
    private String email;
    private String phone;
    private int gender;
    private String description;
    private int avatarImageId;

}
