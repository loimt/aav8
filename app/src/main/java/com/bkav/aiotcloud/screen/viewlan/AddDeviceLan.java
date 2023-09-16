package com.bkav.aiotcloud.screen.viewlan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDeviceLan extends AppCompatActivity {
    RelativeLayout back;
    TextView title;
    TextView ipTitle;
    EditText ipInput;
    TextView alertIp;

    TextView portTitle;
    EditText portInput;
    TextView alertPort;

    TextView accountTitle;
    EditText accountInput;
    TextView alertAccount;

    TextView passTitle;
    EditText passInput;
    TextView alertPass;
    Button confirm;
    ImageView showPass;
    boolean checkValid = false;
    private String linkCam;
    private String account;
    private String pass;
    private boolean isShow = false;


    private static final String TAG = "AddDeviceLan";

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.add_device_lan);
        init();
        action();
        setData();
    }

    private void init(){
        this.back = findViewById(R.id.backButtonAddLanDevice);
        this.title = findViewById(R.id.titleAddDevice);

        this.ipTitle = findViewById(R.id.ipAddressTitle);
        this.ipInput = findViewById(R.id.ipAddressInput);
        this.alertIp = findViewById(R.id.alertIp);

        this.portTitle = findViewById(R.id.portTitle);
        this.portInput = findViewById(R.id.portInput);
        this.alertPort = findViewById(R.id.alertPort);

        this.accountTitle = findViewById(R.id.accountTitle);
        this.accountInput = findViewById(R.id.accountInput);
        this.alertAccount = findViewById(R.id.alertAccount);

        this.passTitle = findViewById(R.id.passTitle);
        this.passInput = findViewById(R.id.passInput);
        this.alertPass = findViewById(R.id.alertPass);

        this.confirm = findViewById(R.id.confirmAddDevice);
        this.showPass = findViewById(R.id.eye);
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValid();
                if(checkValid){
                    JSONObject data = ViewOnLan.processDataAuthen(accountInput.getText().toString(), passInput.getText().toString());
                    account = accountInput.getText().toString();
                    pass = passInput.getText().toString();
                    linkCam = "http://"+
                            ipInput.getText().toString() + ":"
                            + portInput.getText().toString()
                            + "/onvif/device_service";
                    String linkAuthen = linkCam + "/api/Device";
                    Log.e(TAG, "onClick: " + linkAuthen );
                    ApplicationService.requestManager.getInfoCamLan(linkAuthen, data);
                }
            }
        });
        this.showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShow) {
                    //password is visible
                    passInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.eye);
                    isShow = true;

                } else {
                    //password gets hided
                    passInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.showpass);
                    isShow = false;
                }
                passInput.setSelection(passInput.getText().length());
            }
        });
        this.passInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showPass.setClickable(true);
                    if(isShow){
                        showPass.setImageResource(R.drawable.eye);

                    }else{
                        showPass.setImageResource(R.drawable.showpass);
                    }
                }else{
                    showPass.setClickable(false);
                    showPass.setImageResource(0);
                }
            }
        });
    }
    private void setData(){
        confirm.setText(LanguageManager.getInstance().getValue("confirm"));
        title.setText(LanguageManager.getInstance().getValue("enter_device_lan"));
        ipTitle.setText(LanguageManager.getInstance().getValue("ip_address"));
        ipInput.setHint(LanguageManager.getInstance().getValue("hint_ip_address"));
        portTitle.setText(LanguageManager.getInstance().getValue("port"));
        portInput.setHint(LanguageManager.getInstance().getValue("port_hint"));
        accountTitle.setText(LanguageManager.getInstance().getValue("account"));
        accountInput.setHint(LanguageManager.getInstance().getValue("account_hint"));
        passTitle.setText(LanguageManager.getInstance().getValue("password"));
        passInput.setHint(LanguageManager.getInstance().getValue("pass_hint"));

    }
    private void checkValid(){
        if(ipInput.getText().length()==0){
            alertIp.setText(LanguageManager.getInstance().getValue("alert_IP_address"));
            checkValid = false;
        }else{
            alertIp.setText("");
        }
        if(portInput.getText().length()==0){
            alertPort.setText(LanguageManager.getInstance().getValue("alert_port"));
            checkValid = false;
        }else{
            alertPort.setText("");
        }
        if(accountInput.getText().length()==0){
            alertAccount.setText(LanguageManager.getInstance().getValue("account_alert"));
            checkValid = false;
        }else{
            alertAccount.setText("");
        }
        if(passInput.getText().length()==0){
            alertPass.setText(LanguageManager.getInstance().getValue("alert_pass"));
            checkValid = false;
        }else{
            alertPass.setText("");
        }
        if(ipInput.getText().length() > 0 &&
                portInput.getText().length() > 0 &&
                accountInput.getText().length() > 0 &&
                passInput.getText().length() > 0
        ){
            checkValid = true;
        }
    }
    public class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_CAM_LAN_INFO_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("get_cam_lan_fail"), true);
                    break;
                case ApplicationService.GET_CAM_LAN_INFO_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), CameraDetailLan.class);
                    intent.putExtra("linkLan", linkCam );
                    try {
                        JSONObject object = new JSONObject(message.obj.toString());
                        intent.putExtra("Manufacturer", object.getString("Manufacturer"));
                        intent.putExtra("Model", object.getString("Model"));
                        intent.putExtra("FirmwareVersion", object.getString("HardwareId"));
                        intent.putExtra("RTSP_URL", object.getString("RTSP_URL"));
                        intent.putExtra("PTZ_Support", object.getString("PTZ_Support"));
                        intent.putExtra("username", account);
                        intent.putExtra("password", pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    break;
            }
        }


    }

}