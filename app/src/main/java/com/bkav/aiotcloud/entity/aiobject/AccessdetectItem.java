package com.bkav.aiotcloud.entity.aiobject;

import org.json.JSONException;
import org.json.JSONObject;

public class AccessdetectItem {

    public AccessdetectItem(JSONObject jsonObject) {
        try {
            this.cameraId = jsonObject.getInt("cameraId");
            this.address = jsonObject.getString("address");
            this.cameraName = jsonObject.getString("cameraName");
            this.identity = jsonObject.getString("identity");
            this.objGuidEvent = jsonObject.getString("objGuidEvent");
            this.totalDetect = jsonObject.getInt("totalDetect");
            this.filePath = jsonObject.getString("filePath");
            this.fullImage = jsonObject.getString("fullImage");
            this.createdAt = jsonObject.getString("createdAt");
            this.eventId = jsonObject.getInt("eventId");
            this.eventName = jsonObject.getString("eventName");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getObjGuidEvent() {
        return objGuidEvent;
    }
    public String getAddress() {
        return address;
    }


    public void setObjGuidEvent(String objGuidEvent) {
        this.objGuidEvent = objGuidEvent;
    }

    public int getTotalDetect() {
        return totalDetect;
    }

    public void setTotalDetect(int totalDetect) {
        this.totalDetect = totalDetect;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFullImage() {
        return fullImage;
    }

    public void setFullImage(String fullImage) {
        this.fullImage = fullImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    private int cameraId;
    private String cameraName;
    private String identity;
    private String objGuidEvent;
    private int totalDetect;
    private String filePath;
    private String fullImage;
    private String createdAt;
    private int eventId;
    private String eventName;
    private String address;

}

// "cameraId": 3005,
//         "cameraName": "Office Zone",
//         "address": "Bstore",
//         "identity": "person",
//         "noPlate": null,
//         "objGuidEvent": "8988ee39-fd23-470d-97a3-5ead754ba70a",
//         "totalDetect": 0,
//         "filePath": "1|https://fsav3.aiview.ai/1798/035f6afb408bd4c5_34.jpg|,1|https://fsav3.aiview.ai/1798/035f6afb408bd4c5_35.jpg|,1|https://fsav3.aiview.ai/1798/035f6afb408bd4c5_36.jpg|",
//         "objectDetectTypeId": null,
//         "objectDetectTypeName": null,
//         "objectDetectTypeNameTransId": 0,
//         "cropImage": null,
//         "fullImage": "https://fsav3.aiview.ai/1798/035f6afb408bd4c5_36.jpg",
//         "eventId": 25741509,
//         "eventName": "Intrusion",
//         "createdAt": "2023-05-03T16:27:02+00:00",
//         "licensePlate": null,
//         "zoneName": null,
//         "confidence": 0.0,
//         "total": 0
