package com.bkav.aiotcloud.screen.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.user.notify_setting.DetailNotifySetting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class NotifySetting extends AppCompatActivity {
    RelativeLayout accessLayout;
    RelativeLayout licenLayout;
    RelativeLayout customerLayout;
    RelativeLayout fireLayout;
    RelativeLayout back;
    TextView title;
    TextView accessTitle;
    TextView licenTitle;
    TextView customerTitle;
    TextView firedectect;
    TextView deviceStatus;
    RelativeLayout deviceLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.notify_setting);
        init();
        setData();
        action();
    }


    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("notify_setting"));
        this.accessTitle.setText(LanguageManager.getInstance().getValue("accessdetect"));
        this.licenTitle.setText(LanguageManager.getInstance().getValue("licenseplate"));
        this.customerTitle.setText(LanguageManager.getInstance().getValue("customerrecognize"));
        this.firedectect.setText(LanguageManager.getInstance().getValue("firedetect"));
        this.deviceStatus.setText(LanguageManager.getInstance().getValue("deviceactivity"));
        this.licenLayout.setVisibility(View.GONE);
        this.fireLayout.setVisibility(View.GONE);
    }

    private void init(){
        this.accessTitle = findViewById(R.id.titleNotifyAcess);
        this.licenTitle = findViewById(R.id.titleNotifyLicen);
        this.customerTitle = findViewById(R.id.titleNotifyCustomer);
        this.firedectect = findViewById(R.id.titleNotifyFireDetect);
        this.title = findViewById(R.id.titleNotifySetting);
        this.accessLayout = findViewById(R.id.accessNotifyLayout);
        this.licenLayout = findViewById(R.id.licenNotifyLayout);
        this.customerLayout = findViewById(R.id.customerNotifyLayout);
        this.fireLayout = findViewById(R.id.fireNotifyLayout);
        this.back = findViewById(R.id.backButtonNotifySetting);
        this.deviceLayout = findViewById(R.id.deviceNotifyLayout);
        this.deviceStatus = findViewById(R.id.titleNotifyDeviceActivity);
    }
    private void action(){

        this.accessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailNotifySetting.class);
                intent.putExtra("identity_notifySetting", "accessdetect");
                startActivity(intent);
            }
        });
        this.licenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailNotifySetting.class);
                intent.putExtra("identity_notifySetting", "customerrecognize");
                startActivity(intent);
            }
        });
        this.fireLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailNotifySetting.class);
                intent.putExtra("identity_notifySetting", "firedetect");
                startActivity(intent);
            }
        });
        this.deviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailNotifySetting.class);
                intent.putExtra("identity_notifySetting", "deviceactivity");
                startActivity(intent);
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
    }
}