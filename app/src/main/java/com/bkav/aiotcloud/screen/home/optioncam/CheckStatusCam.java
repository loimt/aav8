package com.bkav.aiotcloud.screen.home.optioncam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.user.EditUserProfile;
import com.bkav.aiotcloud.util.MapIdentity;

import org.json.JSONException;
import org.json.JSONObject;

public class
CheckStatusCam extends AppCompatActivity {
    public static final String TYPE = "type";
    public static final String SERIAL_KEY = "serial";
    public static final String MODEL_KEY = "model";
    private String serialCam;
    private String modelCam;
    private RelativeLayout back;
    private TextView title;
    private ImageView avatarCam;
    private TextView modelTx;
    private TextView aleartTx;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_status_cam);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    private void init(){
        Intent intent = getIntent();
        this.serialCam = intent.getStringExtra(SERIAL_KEY);
        this.modelCam = intent.getStringExtra(MODEL_KEY);
        this.back = findViewById(R.id.back);
        this.title = findViewById(R.id.title);
        this.avatarCam = findViewById(R.id.avatarCam);
        this.modelTx = findViewById(R.id.modelCamTxt);
        this.aleartTx = findViewById(R.id.alertTx);
        this.confirm = findViewById(R.id.confirm);
    }
    private void setData(){
        this.title.setText(LanguageManager.getInstance().getValue("check_cam_status"));
        this.confirm.setText(LanguageManager.getInstance().getValue("next"));
        disableConfirm();
        this.modelTx.setText(serialCam);
        if(modelCam!=null){
            MapIdentity.mapImageCam(modelCam, avatarCam);
        }else{
            avatarCam.setImageResource(R.drawable.ava_cam_defautl);
        }
        checkStatusCam();
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject payload = new JSONObject();
                    payload.put("serialCamera", serialCam);
                    ApplicationService.requestManager.registerCam(payload, ApplicationService.URL_REGISTER_CAM);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void checkStatusCam(){
        //P2000803FL2000006
        try{
            JSONObject payload = new JSONObject();
            payload.put("serialCamera", serialCam);
            ApplicationService.requestManager.checkStatusCam(payload, ApplicationService.URL_CHECK_STATUS_CAM);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void disableConfirm(){
        this.confirm.setClickable(false);
        this.confirm.setAlpha(0.5f);
    }
    private void enableConfirm(){
        this.confirm.setClickable(true);
        this.confirm.setAlpha(1f);
    }
    private class MainHandler extends Handler {
        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.CHECK_STATUS_CAM_SUCCESS:
                    enableConfirm();
                    aleartTx.setTextColor(Color.GREEN);
                    aleartTx.setText(LanguageManager.getInstance().getValue("valid_camera"));
                    break;
                case ApplicationService.CHECK_STATUS_CAM_FAIL:
                    disableConfirm();
                    String messageER = (String) message.obj;
                    aleartTx.setTextColor(Color.RED);
                    aleartTx.setText(messageER);
                    break;
                case ApplicationService.REGISTER_CAM_FAIL:
                    ApplicationService.showToast("fail", true);
                    break;
                case ApplicationService.REGISTER_CAM_SUCCESS:
                    try {
                        JSONObject object = new JSONObject(message.obj.toString());
                        Intent intent = new Intent(getApplicationContext(), EditCamProfile.class);
                        intent.putExtra(EditCamProfile.OBJECTGUID, object.getString("objectGuid"));
                        intent.putExtra(EditCamProfile.CAMNAME, object.getString("cameraName"));
                        intent.putExtra(EditCamProfile.REGION_ID, object.getString("regionId"));
                        intent.putExtra(EditCamProfile.TYPE, "checkStatus");
                        intent.putExtra(EditCamProfile.MODEL, modelCam);
                        startActivity(intent);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}