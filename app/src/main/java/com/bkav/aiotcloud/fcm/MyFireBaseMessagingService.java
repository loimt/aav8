package com.bkav.aiotcloud.fcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.notify.detail.NotifyDetailScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.bkav.aiotcloud.application.ApplicationService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = MyFireBaseMessagingService.class.getName();
    private final static AtomicInteger c = new AtomicInteger(0);

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.e(TAG, "onMessageReceived: " + "remoteMessage" + remoteMessage.getData());
        if(ApplicationService.TOKEN == null){
            if (SharePref.getInstance(getApplicationContext()).getIsLogin()) {
                LanguageManager.getInstance().changeLanguage(LanguageManager.getInstance().getLanguage(SharePref.getInstance(getApplicationContext()).getLanguage()));
                ApplicationService.requestManager.login(SharePref.getInstance(getApplicationContext()).getUser(),
                        SharePref.getInstance(getApplicationContext()).getPassword(), ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
            }
        }
        Map<String, String> notification = remoteMessage.getData();
        Bitmap bitmapImage=null;
        String strColor = notification.get("colorBackground");
        String strstrTitleTitle = notification.get("title");
        String strMessage = notification.get("body");
        String strurlImage = notification.get("urlImageString");
        String strEventID = notification.get("eventId");

        String strIdentify = notification.get("identity");
        if(new String(strIdentify).equals("deviceactivity")){
            if(strColor.equals("#4AA541")){
                strMessage = strMessage + LanguageManager.getInstance().getValue("camera_on");
            }else{
                strMessage = strMessage + LanguageManager.getInstance().getValue("camera_off");
            }
        }else{
            assert strMessage != null;
            String[] locationArray = strMessage.split("\\|");
            strMessage = LanguageManager.getInstance().getValue(strIdentify)
                    + LanguageManager.getInstance().getValue("at") + locationArray[1]
                    + LanguageManager.getInstance().getValue("in") + locationArray[2];
        }
        if(new String(strIdentify).equals("accessdetect") || new String(strIdentify).equals("intrusion")){
            String fnm = "access_notfy_ava";
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
            bitmapImage = BitmapFactory.decodeResource(getResources(), imgId);
        } else if (new String(strIdentify).equals("firedetect")) {
            String fnm = "firedetect_notify_ava";
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
            bitmapImage = BitmapFactory.decodeResource(getResources(),imgId);
        }else if(new String(strIdentify).equals("deviceactivity")){
            if(strColor==null){
                String fnm = "camera_icon_no_signal";
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                bitmapImage = BitmapFactory.decodeResource(getResources(),imgId);
            }else{
                if(new String(strColor).equals("#4AA541")){
                    String fnm = "camera_icon_signal";
                    String PACKAGE_NAME = getApplicationContext().getPackageName();
                    int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                    bitmapImage = BitmapFactory.decodeResource(getResources(),imgId);
                }else{
                    String fnm = "camera_icon_no_signal";
                    String PACKAGE_NAME = getApplicationContext().getPackageName();
                    int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                    bitmapImage = BitmapFactory.decodeResource(getResources(),imgId);
                }
            }
        }else if(new String(strIdentify).equals("customerrecognize")){
            if(strurlImage.isEmpty()){
                String fnm = "default_image_notify"; //  this is image file name
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
                bitmapImage = BitmapFactory.decodeResource(getResources(), imgId);
            } else {
                try {
                    InputStream inputStream = new URL(strurlImage).openStream();
                    bitmapImage = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String fnm = "default_image_notify"; //  this is image file name
            String PACKAGE_NAME = getApplicationContext().getPackageName();
            int imgId = getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + fnm, null, null);
            bitmapImage = BitmapFactory.decodeResource(getResources(), imgId);
        }
//        Log.e(TAG, "strEventIDxx: " + strEventID );
        sendNotification(strMessage, bitmapImage, strEventID, strIdentify);
    }

    private void sendNotification(String strMessage, Bitmap bitmapImage, String strEventID, String strIdentify) {
        Intent resultIntent = new Intent(this, NotifyDetailScreen.class);
        resultIntent.putExtra(NotifyDetailScreen.OBJGUID, strEventID);
        resultIntent.putExtra(NotifyDetailScreen.TYPE, "1");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultPendingIntent = PendingIntent.getActivity(
        //                this, getID(), resultIntent, PendingIntent.FLAG_IMMUTABLE
        //        );
        Notification notification = new NotificationCompat.Builder(this, ApplicationService.CHANNEl_ID)
                .setContentTitle(LanguageManager.getInstance().getValue(strIdentify))
                .setLargeIcon(bitmapImage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(strMessage))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(this);
        Log.e(TAG, "strEventIDxx1: " + strEventID );
        notificationCompat.notify(getID(), notification);
    }

    private static int getID(){
        return c.incrementAndGet();
    }

    @Override
    public void onNewToken(@NonNull String token){
        ApplicationService.FIREBASE_ID = token;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}