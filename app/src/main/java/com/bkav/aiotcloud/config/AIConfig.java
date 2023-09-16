package com.bkav.aiotcloud.config;

import android.util.Log;

import com.bkav.aiotcloud.entity.aiobject.CameraUnConfigItem;
import com.bkav.aiotcloud.screen.setting.SettingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AIConfig {
    private static String TAG = "AIConfig";
    public static JSONObject createNewConfigFace(CameraUnConfigItem cameraItem, JSONArray zones, JSONObject profileFeatures, JSONArray scheduleDetails) {
        JSONObject payload = new JSONObject();
        try {
            JSONObject monitor = new JSONObject();
            JSONObject script = new JSONObject();
            JSONObject schedule = new JSONObject();

            monitor.put("appIdentity", SettingFragment.FACE_RECOGNIZE);
            payload.put("monitor", monitor);
            payload.put("cameraId", cameraItem.getCameraId());
            payload.put("boxId", 0);
            payload.put("mediaProfile", JSONObject.NULL);
            payload.put("isConnected", cameraItem.getIsActive());
//            payload.put("profileFeatures", profileFeatures);
            payload.put("mediaProfile", JSONObject.NULL);

            schedule.put("startDate", "");
            schedule.put("endDate", "");
            schedule.put("frequencyEvery", 1);
            schedule.put("dailyOccureEvery", 300);
            schedule.put("dailyOccureOneAt", JSONObject.NULL);
            schedule.put("dailyOccureEveryUnit", "Milisecond");
            schedule.put("description", "");
            schedule.put("dayOffId", 0);
            schedule.put("skipDayOff", 0);
            schedule.put("active", 1);


            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");
                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                payload.put("scheduleDetails", scheduleDetails);
            } else {
                schedule.put("frequencyType", "weekly");
                payload.put("scheduleDetails", scheduleDetails);
            }
            payload.put("schedule", schedule);
//            script.put("parameterValue", profileFeatures.toString());
//            payload.put("script", script);
            payload.put("zones", zones);
            payload.put("extra", profileFeatures);
            Log.e(TAG, payload.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public static JSONObject createNewAccessdetect(CameraUnConfigItem cameraItem, JSONArray zones, JSONObject detectConfig, JSONArray scheduleDetails) {
        JSONObject payload = new JSONObject();
        try {
            JSONObject monitor = new JSONObject();
            JSONObject schedule = new JSONObject();
            JSONObject script = new JSONObject();

            monitor.put("appIdentity", SettingFragment.INTRUSION_DETECT);
            payload.put("monitor", monitor);
            payload.put("cameraId", cameraItem.getCameraId());
            payload.put("boxId", 0);
            payload.put("mediaProfile", JSONObject.NULL);
            payload.put("isConnected", cameraItem.getIsActive());
//            payload.put("detectConfig", detectConfig);
            payload.put("mediaProfile", JSONObject.NULL);

            schedule.put("startDate", "");
            schedule.put("endDate", "");
            schedule.put("frequencyEvery", 1);
            schedule.put("dailyOccureEvery", 300);
            schedule.put("dailyOccureOneAt", JSONObject.NULL);
            schedule.put("dailyOccureEveryUnit", "Milisecond");
            schedule.put("description", "");
            schedule.put("dayOffId", 0);
            schedule.put("skipDayOff", 0);
            schedule.put("active", 1);


            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");
                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                payload.put("scheduleDetails", scheduleDetails);

            } else {
                schedule.put("frequencyType", "weekly");
                payload.put("scheduleDetails", scheduleDetails);
            }
            script.put("parameterValue", "");
            payload.put("extra", detectConfig);
            payload.put("schedule", schedule);
            payload.put("zones", zones);
            Log.e(TAG, payload.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public static JSONObject createNewLicensePlate(CameraUnConfigItem cameraItem, JSONArray zones, JSONArray scheduleDetails , String nation) {
        JSONObject payload = new JSONObject();
        try {
            JSONObject monitor = new JSONObject();
            JSONObject schedule = new JSONObject();
            JSONObject detectConfig = new JSONObject();
            JSONObject script = new JSONObject();

            monitor.put("appIdentity", SettingFragment.LICENSE_PLATE);
            payload.put("monitor", monitor);
            payload.put("cameraId", cameraItem.getCameraId());
            payload.put("boxId", 0);
            payload.put("mediaProfile", JSONObject.NULL);
            payload.put("isConnected", cameraItem.getIsActive());
            payload.put("mediaProfile", JSONObject.NULL);

            schedule.put("startDate", "");
            schedule.put("endDate", "");
            schedule.put("frequencyEvery", 1);
            schedule.put("dailyOccureEvery", 300);
            schedule.put("dailyOccureOneAt", JSONObject.NULL);
            schedule.put("dailyOccureEveryUnit", "Milisecond");
            schedule.put("description", "");
            schedule.put("dayOffId", 0);
            schedule.put("skipDayOff", 0);
            schedule.put("active", 1);
            detectConfig.put("nation", nation);


            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");
                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                payload.put("scheduleDetails", scheduleDetails);

            } else {
                schedule.put("frequencyType", "weekly");
                payload.put("scheduleDetails", scheduleDetails);
            }
//            script.put("parameterValue", "");
//            payload.put("script", script);
            payload.put("schedule", schedule);
            payload.put("extra", detectConfig);
            payload.put("zones", zones);
            Log.e(TAG, payload.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    public static JSONObject editLicensePlate(JSONObject editItem, JSONArray zones, JSONArray scheduleDetails, String nation) {
        try {
//            JSONObject script = editItem.getJSONObject("script");
            JSONObject schedule = editItem.getJSONObject("schedule");
            JSONObject detectConfig = new JSONObject();
            JSONArray zonesEdit = editItem.getJSONArray("zones");
            JSONArray scheduleEdit = editItem.getJSONArray("scheduleDetails");
            detectConfig.put("nation", nation);

            for (int index = 0; index < zonesEdit.length(); index++){
                zonesEdit.getJSONObject(index).put("isDelete", true);
            }
            if (zones.length() == 0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("zoneName",  "Full screen");
                jsonObject.put("coordinate",  "[[]]");
                zonesEdit.put(jsonObject);
            }
            for (int indexEdit = 0; indexEdit < zones.length(); indexEdit++){
                zonesEdit.put(zones.getJSONObject(indexEdit));
            }

            editItem.put("zones", zonesEdit);

            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");

                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                editItem.put("scheduleDetails", scheduleDetails);

            } else {
                schedule.put("frequencyType", "weekly");
//                for (int index = 0; index < scheduleEdit.length(); index++){
//                    scheduleEdit.getJSONObject(index).put("isDelete", 1);
//                }
//
//                for (int index = 0; index < scheduleDetails.length(); index++){
//                    scheduleEdit.put(scheduleDetails.getJSONObject(index));
//                }
                editItem.put("scheduleDetails", scheduleDetails);
            }
            editItem.put("schedule", schedule);
//            editItem.put("script", script);
            editItem.put("extra", detectConfig);
            Log.e(TAG, editItem.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return editItem;
    }

    public static JSONObject editAccessDetectConfig(JSONObject editItem, JSONArray zones, JSONObject detectConfig, JSONArray scheduleDetails) {
//        JSONObject payload = new JSONObject();
        try {
//            JSONObject script = editItem.getJSONObject("script");
            JSONObject schedule = editItem.getJSONObject("schedule");
            JSONArray zonesEdit = editItem.getJSONArray("zones");
//            JSONArray scheduleEdit = editItem.getJSONArray("scheduleDetails");

            for (int index = 0; index < zonesEdit.length(); index++){
                zonesEdit.getJSONObject(index).put("isDelete", true);
            }
            if (zones.length() == 0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("zoneName",  "Full screen");
                jsonObject.put("coordinate",  "[[]]");
                zonesEdit.put(jsonObject);
            }
            for (int indexEdit = 0; indexEdit < zones.length(); indexEdit++){
                zonesEdit.put(zones.getJSONObject(indexEdit));
            }

            editItem.put("extra", detectConfig);
            editItem.put("zones", zonesEdit);

            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");

                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                editItem.put("scheduleDetails", scheduleDetails);

            } else {
                schedule.put("frequencyType", "weekly");
//                for (int index = 0; index < scheduleEdit.length(); index++){
//                    scheduleEdit.getJSONObject(index).put("isDelete", 1);
//                }
//
//                for (int index = 0; index < scheduleDetails.length(); index++){
//                    scheduleEdit.put(scheduleDetails.getJSONObject(index));
//                }
                editItem.put("scheduleDetails", scheduleDetails);
            }
            editItem.put("schedule", schedule);
//            script.put("detectConfig", detectConfig.toString());
//            editItem.put("script", script);
            Log.e(TAG, editItem.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return editItem;
    }


    public static JSONObject editAIFaceConfig(JSONObject editItem, JSONArray zones, JSONObject profileFeatures, JSONArray scheduleDetails) {
//        JSONObject payload = new JSONObject();
        try {
//            JSONObject script = editItem.getJSONObject("extra");
            JSONObject schedule = editItem.getJSONObject("schedule");
            JSONArray zonesEdit = editItem.getJSONArray("zones");
//            JSONArray scheduleEdit = editItem.getJSONArray("scheduleDetails");

            for (int index = 0; index < zonesEdit.length(); index++){
                zonesEdit.getJSONObject(index).put("isDelete", true);
            }
            if (zones.length() == 0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("zoneName",  "Full screen");
                jsonObject.put("coordinate",  "[[]]");
                zonesEdit.put(jsonObject);
            }
            for (int indexEdit = 0; indexEdit < zones.length(); indexEdit++){
                zonesEdit.put(zones.getJSONObject(indexEdit));
            }

//            editItem.put("profileFeatures", profileFeatures);
            editItem.put("zones", zonesEdit);

            if (scheduleDetails.length() == 0){
                schedule.put("frequencyType", "daily");
                JSONObject scheduleDetail = new JSONObject();
                scheduleDetail.put("scheduleDetailId", 0);
                scheduleDetail.put("fromAt", "00:00:00");

                scheduleDetail.put("toAt", "23:59:59");
                scheduleDetail.put("dayOfWeekOrMonth", "[]");
                scheduleDetail.put("isDelete", 0);
                scheduleDetails.put(scheduleDetail);
                editItem.put("scheduleDetails", scheduleDetails);
            } else {
                schedule.put("frequencyType", "weekly");
//                for (int index = 0; index < scheduleEdit.length(); index++){
//                    scheduleEdit.getJSONObject(index).put("isDelete", 1);
//                }
//
//                for (int index = 0; index < scheduleDetails.length(); index++){
//                    scheduleEdit.put(scheduleDetails.getJSONObject(index));
//                }
                editItem.put("scheduleDetails", scheduleDetails);
            }
            editItem.put("schedule", schedule);
            editItem.put("extra", profileFeatures);
//            editItem.put("script", script);
            Log.e(TAG + "xxxxx", editItem.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return editItem;
    }
}
