package com.bkav.aiotcloud.application;

import static com.bkav.aiotcloud.network.BrManager.getApplicationContext;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.MainScreen;

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifecycleHandler.class.getSimpleName();
    private static boolean isInBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

//        if(isInBackground && SharePref.getInstance(getApplicationContext()).getIsLogin()){
//            Log.e(TAG, "app went to foreground");
////            if (ApplicationService.cameraitems == null || ApplicationService.cameraitems.size() == 0){
//                ApplicationService.requestManager.login(SharePref.getInstance(getApplicationContext()).getUser(),
//                        SharePref.getInstance(getApplicationContext()).getPassword(),
//                        ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
//                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(MainScreen.TYPE, "main");
//                getApplicationContext().startActivity(intent);
////            }
//            isInBackground = false;
//        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.e(TAG, "app went to background");
            isInBackground = true;
        }
    }
}