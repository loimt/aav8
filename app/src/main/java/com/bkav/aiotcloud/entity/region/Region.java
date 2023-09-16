package com.bkav.aiotcloud.entity.region;

import org.json.JSONException;
import org.json.JSONObject;

public class Region {
    private int id;
    private String regionCode;
    private String regionName;
    private int level;
    private int parentId;
    private String description;
    private boolean active;
    private int ownerByUserId;
    private String objectGuid;
    private int typeGround;
    private String imageGround;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getOwnerByUserId() {
        return ownerByUserId;
    }

    public void setOwnerByUserId(int ownerByUserId) {
        this.ownerByUserId = ownerByUserId;
    }

    public String getObjectGuid() {
        return objectGuid;
    }

    public void setObjectGuid(String objectGuid) {
        this.objectGuid = objectGuid;
    }

    public int getTypeGround() {
        return typeGround;
    }

    public void setTypeGround(int typeGround) {
        this.typeGround = typeGround;
    }

    public String getImageGround() {
        return imageGround;
    }

    public void setImageGround(String imageGround) {
        this.imageGround = imageGround;
    }


    public Region(JSONObject region){
        try{
            this.id = region.getInt("regionId");
            this.regionCode = region.getString("regionCode");
            this.regionName = region.getString("regionName");
            this.level = region.getInt("level");
            this.parentId = region.getInt("parentId");
            this.description = region.getString("description");
            this.active = region.getBoolean("active");
            this.ownerByUserId = region.getInt("ownerByUserId");
            this.objectGuid = region.getString("objectGuid");
            this.typeGround = region.getInt("typeGround");
            this.imageGround = region.getString("imageGround");

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}
