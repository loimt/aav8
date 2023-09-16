package com.bkav.aiotcloud.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import java.util.Locale;

public class SharePref {
    public static final String KEY_USER = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_LOGIN = "login";
    public static  String KEY_STATE_WIDGET = "stateWidget";
    public static final String KEY_TRICKLE = "trickle";

    public static final String KEY_CURRENT_SETTINGCAM = "settingcam";

    public static final String KEY_DEFAULT_LANGUAGE = "defaultLanguge";

    public static final String TAG = "SharePref";

    @SuppressLint("CommitPrefEdits")
    public static synchronized SharePref getInstance(Context context) {
        if (rootSharedPreferences == null /*|| sharedPreferences == null*/) {
            rootSharedPreferences = context.getSharedPreferences(ApplicationService.KEY_PREFERENCES, Activity.MODE_PRIVATE);
            rootEditor = rootSharedPreferences.edit();
            setSharedPreferences(context);
        }
        return instance;
    }

    public static void setSharedPreferences(Context context) {
        ApplicationService.currentFolder = getSelectedFolder();
        //nếu là folder home thì rootSharedPreferences = preferences
        sharedPreferences = ApplicationService.currentFolder.equalsIgnoreCase(ApplicationService.HOME) ?
                context.getSharedPreferences(ApplicationService.KEY_PREFERENCES, Context.MODE_PRIVATE) :
                context.getSharedPreferences(String.format(Locale.US, "%s_%s",
                        ApplicationService.KEY_PREFERENCES, ApplicationService.currentFolder), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public int getDefaultLanguage() {
        return rootSharedPreferences.getInt(KEY_DEFAULT_LANGUAGE, LanguageManager.VIETNAMESE);
    }

    public boolean getIsLogin() {
        return rootSharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public String getWidgetState() {
        return rootSharedPreferences.getString(getUser(), "");
    }

    public void setWidgetState(String keyToken) {
        rootEditor.putString(getUser(), keyToken);
        rootEditor.commit();
    }

    public String getHomeFolder() {
        return sharedPreferences.getString(ApplicationService.KEY_HOME_FOLDER, "");
    }

    public static String getSelectedFolder() {
        return rootSharedPreferences.getString(ApplicationService.FOLDER_SELECTED, ApplicationService.HOME);
    }

    public String getUser() {
        return rootSharedPreferences.getString(KEY_USER, "");
    }

    public String getPassword() {
        return rootSharedPreferences.getString(KEY_PASSWORD, "");
    }

    public int getLanguage() {
        return rootSharedPreferences.getInt(KEY_DEFAULT_LANGUAGE, 0);
    }

    public boolean getTrickle() {
        return rootSharedPreferences.getBoolean(KEY_TRICKLE, true);
    }


    public int getCurrentSettingCam() {
        return rootSharedPreferences.getInt(KEY_CURRENT_SETTINGCAM, -1);
    }
    public void setCurrentSettingCam(int value) {
        rootEditor.putInt(KEY_CURRENT_SETTINGCAM, value);
        rootEditor.commit();
    }
    public void setDefaultLanguage(int language) {
        rootEditor.putInt(KEY_DEFAULT_LANGUAGE, language);
        rootEditor.commit();
    }

    public void setUserPassword(String user, String password) {
        rootEditor.putString(KEY_USER, user);
        rootEditor.commit();
        rootEditor.putString(KEY_PASSWORD, password);
        rootEditor.commit();
    }

    public void removeAllKey() {
        rootEditor.remove(KEY_USER);
        rootEditor.remove(KEY_PASSWORD);
        rootEditor.remove(KEY_LOGIN);
        rootEditor.remove(KEY_LOGIN);
        rootEditor.remove(KEY_TRICKLE);
//        rootEditor.remove(KEY_STATE_WIDGET);
        rootEditor.commit();
    }

    public void setTrickle(boolean isTrickle) {
        rootEditor.putBoolean(KEY_TRICKLE, isTrickle);
        rootEditor.commit();
    }

    public void setIsLogin(boolean isLogin) {
        rootEditor.putBoolean(KEY_LOGIN, isLogin);
        rootEditor.commit();
    }

    public void setData(String key, String data) {
        rootEditor.putString(key, data);
        rootEditor.commit();
    }

    public String getData(String key) {
        return rootSharedPreferences.getString(key, "");
    }



    private static SharePref instance = new SharePref();
    private static SharedPreferences rootSharedPreferences;
    private static SharedPreferences.Editor rootEditor;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

}
