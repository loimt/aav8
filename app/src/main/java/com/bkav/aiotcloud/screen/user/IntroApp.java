package com.bkav.aiotcloud.screen.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.BuildConfig;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import org.w3c.dom.Text;

public class IntroApp extends AppCompatActivity {
    private RelativeLayout backButton;
    private TextView contentVersion;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_app);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        setData();
        action();
    }

    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("intro_app"));
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = pInfo.versionName;
            String content = LanguageManager.getInstance().getValue("version") + ": " + version;
            this.contentVersion.setText(content);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void init(){
        this.title = findViewById(R.id.titleIntroSetting);
        this.backButton = findViewById(R.id.backButtonIntro);
        this.contentVersion = findViewById(R.id.contentVersionTxt);
    }
    private void action(){
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}