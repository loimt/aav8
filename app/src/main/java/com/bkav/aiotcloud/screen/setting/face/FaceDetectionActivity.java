package com.bkav.aiotcloud.screen.setting.face;

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
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.screen.setting.face.customer.ListTypeCustomer;

import org.json.JSONException;
import org.json.JSONObject;


public class FaceDetectionActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.face_detection_layout);

        init();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.requestManager.getListCameraConfig(SettingFragment.FACE_RECOGNIZE);
    }

    private TextView cameraSettingtx;
    private TextView manageCustomerTx;
    private TextView manageTypeCustomerTx;
    private TextView title;
    private RelativeLayout backIm;
    private RelativeLayout settingLayout;
    private RelativeLayout customerLayout;
    private RelativeLayout typeLayout;

    private void init(){
        this.cameraSettingtx = findViewById(R.id.cameraSettingtx);
        this.manageCustomerTx = findViewById(R.id.manageCustomerTx);
        this.manageTypeCustomerTx = findViewById(R.id.manageTypeCustomerTx);
        this.settingLayout = findViewById(R.id.settingLayout);
        this.customerLayout = findViewById(R.id.customerLayout);
        this.typeLayout = findViewById(R.id.typeLayout);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);

        this.cameraSettingtx.setText(LanguageManager.getInstance().getValue("list_ai_object"));
        this.title.setText(LanguageManager.getInstance().getValue("face_recognize"));
        this.manageCustomerTx.setText(LanguageManager.getInstance().getValue("manage_face"));
        this.manageTypeCustomerTx.setText(LanguageManager.getInstance().getValue("manage_face_group"));
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
                intent.putExtra(SettingFragment.TYPE, SettingFragment.FACE_RECOGNIZE);
                startActivity(intent);
            }
        });

        this.customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), ListCustomerActivity.class);
                startActivity(intent);
            }
        });

        this.typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), ListTypeCustomer.class);
                startActivity(intent);
            }
        });


    }
}
