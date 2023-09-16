package com.bkav.aiotcloud.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.bkav.aiotcloud.application.ApplicationService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileManager {
    public static final String TAG = "FileManager";

//    public static void create(Context context) {
//        FileManager.context = context;
//    }

    public static InputStream loadFileFromAssets(String source) {
        InputStream inputStream = null;
        try {
            AssetManager assetManager = ApplicationService.getAppContext().getAssets();
            inputStream = assetManager.open(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static Xml loadXmlFromHome(String source) {
        String line;
        StringBuilder content = new StringBuilder();
        if (ApplicationService.getAppContext() == null) {
            return null;
        }
        AssetManager assetManager = ApplicationService.getAppContext().getAssets();
        try {
            InputStream inputStream = assetManager.open("home/" + source);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            line = reader.readLine();
            if (line == null){
                Log.e("null", "null");
            }
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
            Xml xml = new Xml(content.toString());
            if (xml.isValid()) {
//                Log.e(TAG, "loadXmlFromHome: " + xml.toString());
                return xml;
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "loadXmlFromHome: " + e.toString());
            return null;
        }
    }

    public static Xml loadXmlFromPath(String source) {
        String line;
        StringBuilder content = new StringBuilder();
        try {
            InputStream inputStream = new FileInputStream(source);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
            Xml xml = new Xml(content.toString());
            if (xml.isValid()) {
                return xml;
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e(TAG, "loadXmlFromPath: " + e.toString());
            return null;
        }
    }

    public static Xml loadXmlFromAssets(String source) {
        String line;
        StringBuilder content = new StringBuilder();
        AssetManager assetManager = ApplicationService.getAppContext().getAssets();
        try {
            InputStream inputStream = assetManager.open(source);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            line = reader.readLine();
            while (line != null) {
                content.append(line);
                line = reader.readLine();
            }
//            Log.e("xml", content.toString());
            Xml xml = new Xml(content.toString());
            if (xml.isValid()) {
                return xml;
            } else {
                return null;
            }
        } catch (IOException e) {
            Log.e("xml", content.toString());
            return null;
        }
    }

//    public static Xml loadXml(String source) {
//        boolean homeExtract = SharePref.getInstance(ApplicationService.getAppContext()).getIsExtractedHome();
//        if (homeExtract) {
//            return loadXmlFromPath(source);
//        } else {
//            return loadXmlFromHome(source);
//        }
//    }
}











