package com.bkav.aiotcloud.screen.setting.license;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.screen.setting.face.customer.TypeObjectAdapter;

import java.util.ArrayList;

public class FilterLicense  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_license);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        init();
        action();
        currentStatus = intent.getIntExtra(ProfileLicenseManage.STATUS, -1);
        int currentGroup = intent.getIntExtra(ProfileLicenseManage.GROUP, -1);
        if (currentGroup != -1){
            typeCustomerAdapter.setCurrentSelect(currentGroup);
        }
        licenseInput.setText(intent.getStringExtra(ProfileLicenseManage.LICENSE));
        setCurrent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private final int active = 1;
    private final int inactive = 0;
    private final int allStatus = -1;
    //
    private TextView titleTx;

    private TextView statusTx;
    private TextView allStatusTx;
    private RadioButton allStatusCheck;

    private TextView activingTx;

    private TextView licenseTx;

    private EditText licenseInput;

    private RadioButton activingCheck;

    private TextView inactiveTx;
    private RadioButton inacticeCheck;

    private RelativeLayout reload;

    private TextView groupLicenseTx;

    private Button save;


    private RecyclerView listType;
    private GroupLicenseFilterAdapter typeCustomerAdapter;

    private void setCurrent(){
        switch (currentStatus) {
            case allStatus:
                allStatusCheck.setChecked(true);
                break;
            case active:
                activingCheck.setChecked(true);
                break;
            case inactive:
                inacticeCheck.setChecked(true);
                break;
        }
    }


    private void init() {
        this.titleTx = findViewById(R.id.titleTx);


        this.statusTx = findViewById(R.id.statusTx);

        this.allStatusCheck = findViewById(R.id.allStatusCheck);
        this.allStatusTx = findViewById(R.id.allStatusTx);

        this.activingTx = findViewById(R.id.activingTx);
        this.activingCheck = findViewById(R.id.activingCheck);


        this.inactiveTx = findViewById(R.id.inactiveTx);
        this.inacticeCheck = findViewById(R.id.inacticeCheck);
        this.groupLicenseTx = findViewById(R.id.groupLicenseTx);

        this.licenseTx = findViewById(R.id.licenseTx);
        this.licenseInput = findViewById(R.id.licenseInput);

        this.reload = findViewById(R.id.reload);
        this.save = findViewById(R.id.save);

        this.listType = findViewById(R.id.listType);
        listType.setLayoutManager(new LinearLayoutManager(this));
        typeCustomerAdapter = new GroupLicenseFilterAdapter(this, ApplicationService.licenseGroupItems);
        listType.setAdapter(typeCustomerAdapter);
        listType.setNestedScrollingEnabled(false);

        this.activingCheck.setChecked(true);

        this.titleTx.setText(LanguageManager.getInstance().getValue("filter"));
        this.allStatusTx.setText(LanguageManager.getInstance().getValue("all"));
        this.activingTx.setText(LanguageManager.getInstance().getValue("activing"));
        this.inactiveTx.setText(LanguageManager.getInstance().getValue("inactive"));
        this.statusTx.setText(LanguageManager.getInstance().getValue("status"));
        this.licenseTx.setText(LanguageManager.getInstance().getValue("license"));
        this.licenseInput.setHint(LanguageManager.getInstance().getValue("license_plate_number"));

    }

    private int currentStatus = 1;
    private String licenseCurrent = "";
    private final int all = -1;
    private void action() {

        this.allStatusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(all);
                    }
                }
            }
        });

        this.activingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(active);
                    }
                }
            }
        });

        this.inacticeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(inactive);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataBack = new Intent();
                dataBack.putExtra(ProfileLicenseManage.TYPE, ProfileLicenseManage.FILTER);
                dataBack.putExtra(ProfileLicenseManage.LICENSE, licenseInput.getText().toString());
                dataBack.putExtra(ProfileLicenseManage.GROUP, typeCustomerAdapter.getCurrentGroupSlected());
                dataBack.putExtra(ProfileLicenseManage.STATUS, currentStatus);
                setResult(RESULT_OK, dataBack);
                finish();
            }
        });

        this.reload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                activingCheck.setChecked(true);
                licenseInput.setText("");
                resetCheckStatus(all);
                typeCustomerAdapter.setCurrentSelect(-1);
                typeCustomerAdapter.notifyDataSetChanged();
            }
        });

        typeCustomerAdapter.setClickListener(new ListDayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
    }

    private void resetCheckStatus(int value) {
        currentStatus = value;
        if (value == all) {
            allStatusCheck.setChecked(true);
            activingCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == active) {
            allStatusCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == inactive) {
            allStatusCheck.setChecked(false);
            activingCheck.setChecked(false);
        }
    }
}


