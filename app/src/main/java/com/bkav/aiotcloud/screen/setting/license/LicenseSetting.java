package com.bkav.aiotcloud.screen.setting.license;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity;
import com.bkav.aiotcloud.screen.setting.face.customer.ListTypeAI;

public class LicenseSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.license_setting);

        init();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApplicationService.requestManager.getListCameraConfig(SettingFragment.LICENSE_PLATE);
    }

    private TextView cameraSettingtx;
    private TextView manageLicenseTx;
    private TextView manageTypeCustomerTx;
    private TextView title;
    private RelativeLayout backIm;
    private RelativeLayout settingLayout;
    private RelativeLayout licenseLayout;
    private RelativeLayout licenseGroupLayout;


    private void init(){
        this.cameraSettingtx = findViewById(R.id.cameraSettingtx);
        this.manageLicenseTx = findViewById(R.id.manageLicenseTx);
        this.manageTypeCustomerTx = findViewById(R.id.manageGroupLicenseTx);
        this.settingLayout = findViewById(R.id.settingLayout);
        this.licenseLayout = findViewById(R.id.customerLayout);
        this.licenseGroupLayout = findViewById(R.id.typeLayout);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);

        this.cameraSettingtx.setText(LanguageManager.getInstance().getValue("list_ai_object"));
        this.title.setText(LanguageManager.getInstance().getValue("licenseplate"));
        this.manageLicenseTx.setText(LanguageManager.getInstance().getValue("licenseplate_management"));
        this.manageTypeCustomerTx.setText(LanguageManager.getInstance().getValue("licenseplate_managementGroup"));
    }

    private void action(){
        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ListFaceObjectActivity.class);
                intent.putExtra(SettingFragment.TYPE, SettingFragment.LICENSE_PLATE);
                startActivity(intent);
            }
        });

        this.licenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ProfileLicenseManage.class);
                startActivity(intent);
            }
        });

        this.licenseGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ListTypeAI.class);
                intent.putExtra(ListTypeAI.PROFILE, ListTypeAI.FACE);
                startActivity(intent);
            }
        });


    }
}
