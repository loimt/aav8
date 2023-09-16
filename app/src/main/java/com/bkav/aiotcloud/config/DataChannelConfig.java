package com.bkav.aiotcloud.config;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DataChannelConfig {
    public static String control(String data) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ptz\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_ptz_continue\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"pt_mode\":\"%s\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, data);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String getFilesRecord(String sourceToken) {
        String dataControl = "{\n" +
                "  \"envelope\": {\n" +
                "    \"header\": {\n" +
                "      \"component\": \"playback_service\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "      \"func\": \"post_search_findrecordings\",\n" +
                "      \"data\": {\n" +
                "        \"sourceToken\": \"%s\",\n" +
                "        \"timeRange\": {\n" +
                "\n" +
                "        },\n" +
                "        \"maxMatch\": 100\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        dataControl = String.format(dataControl, sourceToken);
        Log.e("files record ", dataControl);
        return dataControl;
    }

    public static String getFilesRecordJob() {

        return "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"playback_service\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"get_recording_recordingjob\"\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
    }

    @SuppressLint("DefaultLocale")
    public static String getFilesRecordCamera(String sourceToken) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"get_video_playback\":{\n" +
                "            \"profile_token\":\"%s\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, sourceToken);
        Log.e("files record ", dataControl);
        return dataControl;
    }

    public static String getInforDeviceBox() {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"device\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"get_information\"\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        return dataControl;
    }

    public static String setTimeZoneBox() {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"device\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"get_time\"\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        return dataControl;
    }
    @SuppressLint("DefaultLocale")
    public static String pauVideoPlaybacbk(String sourceToken) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"post_playback_control\":{\n" +
                "            \"profile_token\":\"%s\",\n" +
                "            \"data\":{\n" +
                "               \"request\":\"pause\",\n" +
                "               \"time_seek\":0\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, sourceToken);
        Log.e("files record ", dataControl);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String pauVideoPlaybacbk() {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ps_service\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"post_control_record\",\n" +
                "         \"params\":{\n" +
                "            \"action\":\"pause\",\n" +
                "            \"seek\":0,\n" +
                "            \"scale\":0\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String getFilesRecord(String sourceToken, long timeStart) {
        String dataControl = "{\n" +
                "  \"envelope\": {\n" +
                "    \"header\": {\n" +
                "      \"component\": \"playback_service\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "      \"func\": \"post_search_findrecordings\",\n" +
                "      \"data\": {\n" +
                "        \"sourceToken\": \"%s\",\n" +
                "        \"timeRange\": {\n" +
                "          \"endTime\": %d\n" +
                "        },\n" +
                "        \"maxMatch\": 100\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        dataControl = String.format(dataControl, sourceToken, timeStart);
//        Log.e("files record ", dataControl);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String selectRecotdBox(String token, int seek) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ps_service\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"post_select_record\",\n" +
                "         \"params\":{\n" +
                "            \"token\":\"%s\",\n" +
                "            \"seek\":%d\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, token, seek);
        Log.e("selectRecord ", dataControl);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String selectRecordCamera(String token,String fileName, int seek) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"post_play_back\":{\n" +
                "            \"profile_token\":\"%s\",\n" +
                "            \"data\":{\n" +
                "               \"url\":\"%s\",\n" +
                "   \"time_seek\":%d\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, token, fileName, seek);
//        Log.e("selectRecord ", dataControl);
        return dataControl;
    }

//    @SuppressLint("DefaultLocale")
//    public static String selectRecordCamera() {
//        String dataControl = "{extend:{\n" +
//                "                      type:\"PLAYBACK\",\n" +
//                "                      src:\"aiview_auto_recorder_20221025164616.mp4\"\n" +
//                "                    }\n}\n";
//        Log.e("selectRecord ", dataControl);
//        return dataControl;
//    }

//    @SuppressLint("DefaultLocale")
//    public static String selectRecotd() {
//        String dataControl = "{\n" +
//                "  \"envelope\": {\n" +
//                "    \"header\": {\n" +
//                "      \"component\": \"ps_service\"\n" +
//                "    },\n" +
//                "    \"body\": {\n" +
//                "      \"func\": \"post_select_record\",\n" +
//                "      \"params\": {\n" +
//                "        \"token\": \"Recording_00201_20221025080927\",\n" +
//                "        \"seek\": 0\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}";
//        Log.e("selectRecord ", dataControl);
//        return dataControl;
//    }

    @SuppressLint("DefaultLocale")
    public static String getStreamMode(String token) {
        String dataControl = "{\n" +
                "    \"type\": \"PLAYBACK\",\n" +
                "    \"src\": \"Recording_00201_20221025090442\",\n" +
                "    \"seek\": 0\n" +
                "}";
//        dataControl = String.format(dataControl, token);
        Log.e("getStreamMode ", dataControl);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String focus(int data) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ptz\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_focus_setting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"focus_mode\":\"Manual\",\n" +
                "               \"focus\":%d\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, data);
        return dataControl;
    }

    public static String zoom(int data) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ptz\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_zoom_setting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"zoom\":%d\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, data);
        return dataControl;
    }

    public static String getImageInfo(String tokenVS) {
        if (tokenVS.length() != 0) {
            String data = "{\n" +
                    "   \"envelope\":{\n" +
                    "      \"header\":{\n" +
                    "         \"component\":\"image_service\"\n" +
                    "      },\n" +
                    "      \"body\":{\n" +
                    "         \"func\":\"get_imagingsettings\",\n" +
                    "         \"params\":{\n" +
                    "            \"token\":\"%s\"\n" +
                    "         }\n" +
                    "      }\n" +
                    "   }\n" +
                    "}\n";
            data = String.format(data, tokenVS);
            return data;
        } else {
            return " {\n" +
                    "   \"envelope\":{\n" +
                    "      \"header\":{\n" +
                    "         \"component\":\"image\"\n" +
                    "      },\n" +
                    "      \"body\":{\n" +
                    "         \"get_imagesetting\":{\n" +
                    "            \"profile_token\":\"PROFILE_468971848\"\n" +
                    "         }\n" +
                    "      }\n" +
                    "   }\n" +
                    "}\n";
        }
    }

    public static String getDisplayInfo() {
        return " {\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"get_displaysetting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
    }

    public static String getUDPScanDevice() {
        return "{\n" +
                "               \"Envelope\":{\n" +
                "                  \"Header\":{\n" +
                "                     \"MessageID\":\"649B726A-D6A2-bf4b-B6CB-2C4F2C036A22\",\n" +
                "                     \"To\":\"discovery\",\n" +
                "                     \"Action\":\"discovery/Probe\"\n" +
                "                  },\n" +
                "                  \"Body\":{\n" +
                "                     \"Probe\":{\n" +
                "                        \"Types\":\"NetworkVideoTransmitter\",\n" +
                "                        \"Scopes\":\"Bkav\"\n" +
                "                     }\n" +
                "                  }\n" +
                "               }\n" +
                "            }";
    }

    public static String getVideoInfor(String channel) {
        String dataControl = " {\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\",\n" +
                "         \"query_params\":{\n" +
                "            \"id\":\"%s\"\n" +
                "         }\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"get_videosetting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, channel);
        return dataControl;
    }

    public static String settingVideo(String data) {
        String dataControl = " {\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_videosetting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":%s\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n" +
                "\n";
        dataControl = String.format(dataControl, data);
        return dataControl;
    }

    @SuppressLint("DefaultLocale")
    public static String settingVideo(String resolution, int frameRate, String bitrate) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"media\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_videosetting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"channel\":\"Main\",\n" +
                "               \"resolution\":\"%s\",\n" +
                "               \"frame_rate\":%d,\n" +
                "               \"bitrate\":\"%s\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n" +
                "\n";

        dataControl = String.format(dataControl, resolution, frameRate, bitrate);
        Log.e("setting video", "data control " + dataControl);
        return dataControl;
    }

    public static String getDayNightInfo() {
        return " {\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ptz\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"get_daynight_setting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
    }

    public static String getOption(String VSToken) {
        String data = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"image_service\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"get_options\",\n" +
                "         \"params\":{\n" +
                "            \"token\":\"%s\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        return String.format(data, VSToken);
    }

    public static String getImageSetting(String VSToken) {
        String data = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"image_service\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"get_imagingsettings\",\n" +
                "         \"params\":{\n" +
                "            \"token\":\"%s\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        return String.format(data, VSToken);
    }

    public static String setDayNight(String value) {
        String data = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"ptz\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_daynight_setting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"day_night_mode\":\"%s\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";

        data = String.format(data, value);
        return data;
    }

    @SuppressLint("DefaultLocale")
    public static String settingImage(int contrast, int sharpness, int brightness, int saturation) {
        String data = " {\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"image\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"put_imagesetting\":{\n" +
                "            \"profile_token\":\"PROFILE_468971848\",\n" +
                "            \"data\":{\n" +
                "               \"appearance\":{\n" +
                "                  \"contrast\":%d,\n" +
                "                  \"sharpness\":%d,\n" +
                "                  \"brightness\":%d,\n" +
                "                  \"saturation\":%d\n" +
                "               },\n" +
                "               \"wdr\":false,\n" +
                "               \"white_balance\":\"Auto\",\n" +
                "               \"orientation\":{\n" +
                "                  \"rotate\":0,\n" +
                "                  \"mirror\":false\n" +
                "               },\n" +
                "               \"ai_turning\":\"Auto\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}\n";

        data = String.format(data, contrast, sharpness, brightness, saturation);
//        Log.e(Tag, "data control " + dataControl);
        return data;
    }

    @SuppressLint("DefaultLocale")
    public static String settingImageBox(String vsToken, String dataControl) {
        String data = "{\n" +
                "               \"envelope\":{\n" +
                "                  \"header\":{\n" +
                "                     \"component\":\"image_service\",\n" +
                "                     \"message_id\":\"put_imagingsettings\"\n" +
                "                  },\n" +
                "                  \"body\":{\n" +
                "                     \"func\":\"put_imagingsettings\",\n" +
                "                     \"params\":{\n" +
                "                        \"token\":\"%s\"\n" +
                "                     },\n" +
                "                     \"data\":{\n" +
                "                            \"videoSourceToken\":\"%s\",\n" +
                "                            \"imagingSettings\":%s\n" +
                "                         }\n" +
                "                  }\n" +
                "               }\n" +
                "            }";
        Log.e("setting image", String.format(data, vsToken, vsToken, dataControl));

        return String.format(data, vsToken, vsToken, dataControl);
    }

    @SuppressLint("DefaultLocale")
    public static String settingImage(JSONObject appearance, JSONObject orientation, String aiTurning, boolean wdr, String white_balance) {
        JSONObject dataSetting = new JSONObject();
        JSONObject envelope = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject putImagesetting = new JSONObject();
        JSONObject data = new JSONObject();


        try {
            envelope.put("header", header);
            envelope.put("body", body);
            header.put("component", "image");
            putImagesetting.put("profile_token", "PROFILE_468971848");
            putImagesetting.put("data", data);
            data.put("ai_turning", aiTurning);
            data.put("orientation", orientation);
            data.put("white_balance", white_balance);
            data.put("wdr", wdr);
            data.put("appearance", appearance);

            body.put("put_imagesetting", putImagesetting);
            dataSetting.put("envelope", envelope);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("data send ", dataSetting.toString());
        return dataSetting.toString();
    }

    public static String settingDisplay(String data) {
        String dataRequest = "{\n" +
                "  \"envelope\": {\n" +
                "    \"header\": {\n" +
                "      \"component\": \"media\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "      \"put_displaysetting\": {\n" +
                "        \"profile_token\": \"PROFILE_468971848\",\n" +
                "        \"data\": %s\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        dataRequest = String.format(dataRequest, data);
        Log.e("request display", dataRequest);

        return dataRequest.toString();
    }

}
