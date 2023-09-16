package com.bkav.aiotcloud.entity;

import com.bkav.aiotcloud.entity.aiobject.CameraConfig;

import org.json.JSONException;
import org.json.JSONObject;
public class UserItem {

    public UserItem(JSONObject aiObjectItem){
        try {
            this.userName = aiObjectItem.getString("userName");
            this.language = aiObjectItem.getString("language");
            this.theme = aiObjectItem.getString("theme");
            this.parentId = aiObjectItem.getInt("parentId");
            this.objectGuid = aiObjectItem.getString("objectGuid");
            this.accountID = aiObjectItem.getInt("accountID");
            this.countriesCode = aiObjectItem.getString("countriesCode");
            this.fullName = aiObjectItem.getString("fullName");
            this.code = aiObjectItem.getString("code");
            this.dateOfBirth = aiObjectItem.getString("dateOfBirth");
            this.gender = aiObjectItem.getInt("gender");
            this.address = aiObjectItem.getString("address");
            this.phone = aiObjectItem.getString("phone");
            this.email = aiObjectItem.getString("email");
            this.password = aiObjectItem.getString("password");
            this.departmentId = aiObjectItem.getInt("departmentId");
            this.departmentName = aiObjectItem.getString("departmentName");
            this.regionId = aiObjectItem.getInt("regionId");
            this.regionName = aiObjectItem.getString("regionName");
            this.positionId = aiObjectItem.getInt("positionId");
            this.positionName = aiObjectItem.getString("positionName");
            this.avatar = aiObjectItem.getString("avatar");
            this.thumbnail = aiObjectItem.getString("thumbnail");
            this.imageActive = aiObjectItem.getString("imageActive");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getCountriesCode() {
        return countriesCode;
    }

    public void setCountriesCode(String countriesCode) {
        this.countriesCode = countriesCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImageActive() {
        return imageActive;
    }

    public void setImageActive(String imageActive) {
        this.imageActive = imageActive;
    }

    private String userName;
    private String language;
    private String theme;
    private int parentId;
    private String objectGuid;
    private int accountID;
    private String countriesCode;
    private String fullName;
    private String code;
    private String dateOfBirth;
    private int gender;
    private String address;
    private String phone;
    private String email;
    private String password;
    private int departmentId;
    private String departmentName;
    private int regionId;
    private String regionName;
    private int positionId;
    private String positionName;
    private String avatar;
    private String thumbnail;
    private String imageActive;

}
