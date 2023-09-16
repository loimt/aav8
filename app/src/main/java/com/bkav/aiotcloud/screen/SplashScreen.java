package com.bkav.aiotcloud.screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.splash_layout);

//        if (SharePref.getInstance(getApplicationContext()).getIsLogin()) {
//            LanguageManager.getInstance().changeLanguage(LanguageManager.getInstance().getLanguage(SharePref.getInstance(getApplicationContext()).getLanguage()));
//            ApplicationService.requestManager.login(SharePref.getInstance(getApplicationContext()).getUser(),
//                    SharePref.getInstance(getApplicationContext()).getPassword(), ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
//        }
        new CountDownTimer(1000, 500) {
            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
               if (SharePref.getInstance(getApplicationContext()).getIsLogin()) {
                    LanguageManager.getInstance().changeLanguage(LanguageManager.getInstance().getLanguage(SharePref.getInstance(getApplicationContext()).getLanguage()));
                    ApplicationService.requestManager.login(SharePref.getInstance(getApplicationContext()).getUser(),
                            SharePref.getInstance(getApplicationContext()).getPassword(), ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
                    Intent intent = new Intent(getApplication(), MainScreen.class);
                    intent.putExtra(MainScreen.TYPE, "main");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplication(), LoginActiviry.class);
                    startActivity(intent);
                }
                overridePendingTransition(0, 0);
            }

        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
