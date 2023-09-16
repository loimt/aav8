package com.bkav.aiotcloud.config;

import android.util.Log;

public class DeviceConfig {
    public static String getTimeZoneBox() {
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

    public static String setTimeZoneBox(String data) {
        String dataControl = "{\n" +
                "   \"envelope\":{\n" +
                "      \"header\":{\n" +
                "         \"component\":\"device\"\n" +
                "      },\n" +
                "      \"body\":{\n" +
                "         \"func\":\"put_time\",\n" +
                "         \"data\":\"%s\"\n" +
                "      }\n" +
                "   }\n" +
                "}\n";
        dataControl = String.format(dataControl, data);
//        Log.e("files timeSetting ", dataControl);
        return dataControl;
    }
}
