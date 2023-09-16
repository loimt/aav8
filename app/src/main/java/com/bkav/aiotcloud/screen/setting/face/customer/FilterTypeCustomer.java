package com.bkav.aiotcloud.screen.setting.face.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.DayItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.ScheduleItem;
import com.bkav.aiotcloud.screen.setting.face.SetHourDialog;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;
import com.bkav.aiotcloud.screen.user.EditUserProfile;

import java.util.ArrayList;

public class FilterTypeCustomer extends AppCompatActivity {
    public static final String STATUS = "status";
    public static final String IDENTITY = "identity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_type_customer);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        this.currentStatus = intent.getIntExtra(STATUS, 1);
        this.currentIdentity = intent.getStringExtra(IDENTITY);
        init();
    }

    private final String all = "all";
    private final String popup = "popup";
    private final String sound = "sound";
    private final String blink = "flicker";

    private String currentIdentity;

    private final String active = "active";
    private final String inactive = "inactive";

    private TextView titleTx;
    private TextView selectWarningTx;

    private TextView allTx;
    private RadioButton allCheck;

    private TextView popupTx;
    private RadioButton popupCheck;

    private TextView soundTx;
    private RadioButton soundCheck;

    private TextView blinkTx;
    private RadioButton blinkCheck;

    private TextView status;

    private TextView allStatusTx;
    private RadioButton allStatusCheck;

    private TextView activingTx;
    private RadioButton activingCheck;

    private TextView inactiveTx;
    private RadioButton inacticeCheck;

    private RelativeLayout reload;

    private String currentType = "all";
    private int currentStatus = 1;

    private void init() {
        this.titleTx = findViewById(R.id.titleTx);

        this.selectWarningTx = findViewById(R.id.selectWarningTx);
        this.allTx = findViewById(R.id.allTx);
        this.allCheck = findViewById(R.id.allCheck);

        this.popupTx = findViewById(R.id.popupTx);
        this.popupCheck = findViewById(R.id.popupCheck);

        this.soundTx = findViewById(R.id.soundTx);
        this.soundCheck = findViewById(R.id.soundCheck);

        this.blinkTx = findViewById(R.id.blinkTx);
        this.blinkCheck = findViewById(R.id.blinkCheck);

        this.status = findViewById(R.id.status);
        this.allStatusCheck = findViewById(R.id.allStatusCheck);
        this.allStatusTx = findViewById(R.id.allStatusTx);

        this.activingTx = findViewById(R.id.activingTx);
        this.activingCheck = findViewById(R.id.activingCheck);


        this.inactiveTx = findViewById(R.id.inactiveTx);
        this.inacticeCheck = findViewById(R.id.inacticeCheck);

        this.reload = findViewById(R.id.reload);

        Button save = findViewById(R.id.save);

        this.titleTx.setText(LanguageManager.getInstance().getValue("filter"));

        this.selectWarningTx.setText(LanguageManager.getInstance().getValue("select_type_warning"));
        this.popupTx.setText(LanguageManager.getInstance().getValue("popup"));
        this.blinkTx.setText(LanguageManager.getInstance().getValue("blink"));
        this.soundTx.setText(LanguageManager.getInstance().getValue("sound"));
        this.allTx.setText(LanguageManager.getInstance().getValue("all"));

        this.allStatusTx.setText(LanguageManager.getInstance().getValue("all"));
        this.activingTx.setText(LanguageManager.getInstance().getValue("activing"));
        this.inactiveTx.setText(LanguageManager.getInstance().getValue("inactive"));
        this.status.setText(LanguageManager.getInstance().getValue("status"));

        this.allCheck.setChecked(true);
        this.activingCheck.setChecked(true);


        this.allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    resetCheckWarning(all);
                }
            }
        });

        this.soundCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    resetCheckWarning(sound);
                }
            }
        });

        this.blinkCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    resetCheckWarning(blink);
                }
            }
        });

        this.popupCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckWarning(popup);
                    }
                }
            }
        });

        this.allStatusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(-1);
                    }
                }
            }
        });

        this.activingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(1);
                    }
                }
            }
        });

        this.inacticeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(0);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataBack = new Intent();
                dataBack.putExtra(ListCustomerActivity.TYPE, ListTypeCustomer.FILTER);
                dataBack.putExtra(ListTypeCustomer.STATUS, currentStatus);
                dataBack.putExtra(ListTypeCustomer.TYPE_SHOW, currentType);
                setResult(RESULT_OK, dataBack);
                finish();
            }
        });

        this.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allCheck.setChecked(true);
                activingCheck.setChecked(true);
                resetCheckWarning(all);
                resetCheckStatus(1);
            }
        });

        if (currentStatus == -1){
            allStatusCheck.setChecked(true);
        } else if (currentStatus == 0){
            inacticeCheck.setChecked(true);
        } else if (currentStatus == 1){
            activingCheck.setChecked(true);
        }

        if (currentIdentity.equals(all)){
            allCheck.setChecked(true);
        } else if(currentIdentity.equals(popup)){
            popupCheck.setChecked(true);
        } else if (currentIdentity.equals(sound)){
            soundCheck.setChecked(true);
        } else if (currentIdentity.equals(blink)){
            blinkCheck.setChecked(true);
        }
    }

    private void resetCheckWarning(String name) {
        this.currentType = name;
        if (name.equals(all)) {
            popupCheck.setChecked(false);
            soundCheck.setChecked(false);
            blinkCheck.setChecked(false);
        } else if (name.equals(popup)) {
            allCheck.setChecked(false);
            soundCheck.setChecked(false);
            blinkCheck.setChecked(false);
        } else if (name.equals(sound)) {
            allCheck.setChecked(false);
            popupCheck.setChecked(false);
            blinkCheck.setChecked(false);
        } else if (name.equals(blink)) {
            allCheck.setChecked(false);
            popupCheck.setChecked(false);
            soundCheck.setChecked(false);
        }
    }

    private void resetCheckStatus(int value) {
        this.currentStatus = value;
        if (value == -1) {
            activingCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == 1) {
            allStatusCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == 0) {
            allStatusCheck.setChecked(false);
            activingCheck.setChecked(false);
        }
    }
}


