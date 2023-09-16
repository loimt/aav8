package com.bkav.aiotcloud.entity.region;

public class RegionGroup {
    private int id;
    private int regionCode;
    private String regionName;
    private int level;
    private int parentId;
    private String description;
    private boolean active;
    private int ownerByUserId;

    public int getId() {
        return id;
    }

    public RegionGroup(int id, int regionCode, String regionName, int level, int parentId, String description, boolean active, int ownerByUserId, String objectGuid, int typeGround, String imageGround) {
        this.id = id;
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.level = level;
        this.parentId = parentId;
        this.description = description;
        this.active = active;
        this.ownerByUserId = ownerByUserId;
        this.objectGuid = objectGuid;
        this.typeGround = typeGround;
        this.imageGround = imageGround;
    }

    public RegionGroup() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
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

    private String objectGuid;
    private int typeGround;
    private String imageGround;

}
