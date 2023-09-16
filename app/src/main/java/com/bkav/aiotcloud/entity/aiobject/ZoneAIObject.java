package com.bkav.aiotcloud.entity.aiobject;

import android.graphics.Point;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZoneAIObject {

    public ZoneAIObject(JSONObject zoneItem){
        try {
            this.zoneId = zoneItem.getInt("zoneId");
            this.createdAt = zoneItem.getString("createdAt");
            this.zoneTypeId = zoneItem.getInt("zoneTypeId");
            this.isDelete = zoneItem.getBoolean("isDelete");
            this.zoneName = zoneItem.getString("zoneName");
            this.coordinate = getListtPoin(zoneItem.getString("coordinate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ZoneAIObject(List<Point> points, String zoneName){
       this.coordinate = points;
       this.zoneName = zoneName;
    }

    private List<Point> getListtPoin(String listPoin){
        List<Point> points = new ArrayList<>();
        listPoin = listPoin.replace("]", "");
        listPoin = listPoin.replace("[", "");
        String coordXY[] = listPoin.split(",");
        Log.e("length","" +  coordXY.length);
        if (coordXY.length > 1){
            for (int index = 0; index < coordXY.length;  index = index+2){
                int x = Integer.parseInt(coordXY[index].replace(" ", ""));
                int y = Integer.parseInt(coordXY[index+1].replace(" ", ""));
                points.add(new Point(x,y));
//                Log.e("xxxxx","" +  coordXY[index].replace(" ", ""));
            }
        }

        return points;
    }

    public JSONObject getNewPayload(){
        JSONObject payload =  new JSONObject();
        try {
            payload.put("zoneName", this.zoneName);
            payload.put("coordinate", getCoordinateToString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private String getCoordinateToString(){
        if (coordinate.size() == 0){
            return "[[]]";
        }
        String resulf = "[[";
            for (Point point :  coordinate){
                resulf = resulf.concat("[" + point.x + "," + point.y+"],");
            }
            resulf = resulf.substring(0, resulf.length()-1);
            resulf = resulf.concat("]]");
        return resulf;
    }

    private int zoneId;

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public List<Point> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<Point> coordinate) {
        this.coordinate = coordinate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getZoneTypeId() {
        return zoneTypeId;
    }

    public void setZoneTypeId(int zoneTypeId) {
        this.zoneTypeId = zoneTypeId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getPresetId() {
        return presetId;
    }

    public void setPresetId(int presetId) {
        this.presetId = presetId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    private String zoneName;
    private List<Point> coordinate;
    private String createdAt;
    private int zoneTypeId;
    private boolean isDelete;
    private int presetId;
}
