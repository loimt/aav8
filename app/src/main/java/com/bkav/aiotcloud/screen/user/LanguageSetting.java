package com.bkav.aiotcloud.screen.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.Language;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.SplashScreen;

import java.util.ArrayList;

public class LanguageSetting extends AppCompatActivity {
    private RelativeLayout backButton;
    private RelativeLayout tiengVietLayout;
    private RelativeLayout englishLayout;
    private ImageView tickTiengViet;
    private ImageView tickEnglish;
    private int currentLanguage = 0;
    boolean statusChangeLanguage = false;
    private TextView title;
    private TextView titleVN;
    private TextView titleEng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.language_setting);
        init();
        setData();
        action();

    }
    private void action() {
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.tiengVietLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tickTiengViet.setImageResource(R.drawable.tick_icon);
                tickEnglish.setImageResource(0);
                currentLanguage = 1;
                changeLanguage();
            }
        });
        this.englishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tickEnglish.setImageResource(R.drawable.tick_icon);
                tickTiengViet.setImageResource(0);
                currentLanguage = 0;
                changeLanguage();
            }
        });
    }

    private void setData() {
        this.titleVN.setText(LanguageManager.getInstance().getValue("VN_language_setting"));
        this.titleEng.setText(LanguageManager.getInstance().getValue("Eng_language_setting"));
        this.title.setText(LanguageManager.getInstance().getValue("language"));
        ArrayList<Language> languages = LanguageManager.getInstance().getLanguageList();
        for (int index = 0; index < languages.size(); index++) {
            Language language = languages.get(index);
            if (language.isDefault()) {
                currentLanguage = index;
                if (currentLanguage == 1){
                    tickTiengViet.setImageResource(R.drawable.tick_icon);
                } else {
                    tickEnglish.setImageResource(R.drawable.tick_icon);
                }
            }
        }
    }
    private void changeLanguage() {
        LanguageManager.getInstance().changeLanguage(LanguageManager.getInstance().getLanguage(currentLanguage));
        SharePref.getInstance(getApplicationContext()).setDefaultLanguage(currentLanguage);
        this.title.setText(LanguageManager.getInstance().getValue("language"));
        this.titleVN.setText(LanguageManager.getInstance().getValue("VN_language_setting"));
        this.titleEng.setText(LanguageManager.getInstance().getValue("Eng_language_setting"));
    }
    private void init(){
        this.titleVN = findViewById(R.id.titleLanguageVN);
        this.titleEng = findViewById(R.id.titleLanguageEnglish);
        this.title = findViewById(R.id.titleLanguageSetting);
        this.backButton = findViewById(R.id.backButtonLanguage);
        this.tiengVietLayout = findViewById(R.id.tiengVietLayoutLanguage);
        this.englishLayout = findViewById(R.id.englishLayoutLanguage);
        this.tickEnglish = findViewById(R.id.tickEnglish);
        this.tickTiengViet = findViewById(R.id.tickVietnam);
    }


}