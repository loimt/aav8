package com.bkav.aiotcloud.screen.viewlan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.entity.aiobject.LanDevice;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.home.optioncam.CheckStatusCam;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

public class ViewOnLan extends AppCompatActivity {
    private static final String TAG = "ViewOnLan";
    public static final String TYPE = "type";
    private String type;
    private ServerFinder serverFinder;
    private MainHandler udpHandle;
    private RecyclerView listLanDevices;
    private AdapterDeviceLan adapterDeviceLan;
    private RelativeLayout backIM;
    private RelativeLayout addDevice;
    boolean checkValidAccount = false;
    private String linkCam;
    private String account;
    private String pass;
    private TextView title;
    private boolean isShow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.view_on_lan);
        udpHandle = new MainHandler();
        init();
        setData();
        action();

    }
    private void init(){
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);
        this.backIM = findViewById(R.id.backButtonLanDevice);
        this.listLanDevices = findViewById(R.id.listLanDevices);
        this.addDevice = findViewById(R.id.addDeviceLan);
        this.title = findViewById(R.id.titleListDeviceLan);

    }
    private void setData(){
        this.listLanDevices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.adapterDeviceLan = new AdapterDeviceLan(getApplicationContext(), ApplicationService.listLanDevices);
        this.listLanDevices.setAdapter(adapterDeviceLan);
        this.title.setText(LanguageManager.getInstance().getValue("list_device"));


    }
    private void action(){
        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                release();
                ApplicationService.listLanDevices.clear();
                finish();
            }
        });
        this.addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDeviceLan.class);
                startActivity(intent);
            }
        });

        this.adapterDeviceLan.setClickListener(new AdapterDeviceLan.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(type.equals("viewLan")){
                    openAuthenDialog(ApplicationService.listLanDevices.get(position));
                }else{
                    openCheckStatusCam(ApplicationService.listLanDevices.get(position));
                }


            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        release();
        ApplicationService.listLanDevices.clear();
    }


    private void release() {
        if (serverFinder != null) {
            serverFinder.interrupt();
        }
    }

    private void openCheckStatusCam(LanDevice lanDevice){
        Intent intent = new Intent(getApplicationContext(), CheckStatusCam.class);
        intent.putExtra(CheckStatusCam.MODEL_KEY, lanDevice.getModel());
        intent.putExtra(CheckStatusCam.SERIAL_KEY, lanDevice.getSerialNumber());
        release();
        startActivity(intent);
    }
    private void openAuthenDialog(LanDevice lanDevice) {
        Dialog dialog = new Dialog(ViewOnLan.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_account);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView accountTitle = dialog.findViewById(R.id.accountTitle);
        EditText inputAcount = dialog.findViewById(R.id.accountInput);
        TextView passTitle = dialog.findViewById(R.id.passTitle);
        EditText inputPass = dialog.findViewById(R.id.passInput);
        TextView noteAcount = dialog.findViewById(R.id.noteAccount);
        TextView alertAccount = dialog.findViewById(R.id.alertAccount);
        TextView alertPass = dialog.findViewById(R.id.alertPass);
        ImageView showPass = dialog.findViewById(R.id.eye);


        accountTitle.setText(LanguageManager.getInstance().getValue("account"));
        inputAcount.setHint(LanguageManager.getInstance().getValue("account_hint"));
        passTitle.setText(LanguageManager.getInstance().getValue("password"));
        inputPass.setHint(LanguageManager.getInstance().getValue("pass_hint"));
        noteAcount.setText(LanguageManager.getInstance().getValue("alert_account_lan"));
        Button confirm = dialog.findViewById(R.id.confirmAuthenAccount);
        confirm.setText(LanguageManager.getInstance().getValue("confirm"));

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShow) {
                    //password is visible
                    inputPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.eye);
                    isShow = true;

                } else {
                    //password gets hided
                    inputPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.showpass);
                    isShow = false;
                }
                inputPass.setSelection(inputPass.getText().length());
            }
        });
        inputPass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputAcount.getText().length()==0){
                    alertAccount.setText(LanguageManager.getInstance().getValue("account_alert"));
                    checkValidAccount = false;
                }else{
                    alertAccount.setText("");
                }
                if(inputPass.getText().length()==0){
                    alertPass.setText(LanguageManager.getInstance().getValue("alert_pass"));
                    checkValidAccount = false;
                }else{
                    alertPass.setText("");
                }
                if(
                        inputAcount.getText().length() > 0 &&
                        inputPass.getText().length() > 0
                ){
                    checkValidAccount = true;
                }

                if(checkValidAccount){
                    JSONObject data = processDataAuthen(inputAcount.getText().toString(), inputPass.getText().toString());
                    account = inputAcount.getText().toString();
                    pass = inputPass.getText().toString();
                    linkCam = lanDevice.getAddrs();
                    String linkAuthen = lanDevice.getAddrs() + "/api/Device";
                    ApplicationService.requestManager.getInfoCamLan(linkAuthen, data);
                }
            }
        });
    }
    public static byte[] sha1(byte[] bytes) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] result = sha.digest(bytes);
            return result;
        } catch (Exception x) {

        }
        return null;
    }
    public static JSONObject processDataAuthen(String account, String pass){
        Random rd = new Random();
        byte[] nonceByte = new byte[32];
        rd.nextBytes(nonceByte);
//        "2008-12-31T16:11:12.003Z"
        LocalDateTime today=LocalDateTime.now();
        DateTimeFormatter formatToday=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String formattedToday=today.format(formatToday);
        byte[] dateByte = formattedToday.getBytes(StandardCharsets.ISO_8859_1);
        byte[] passByte = pass.getBytes(StandardCharsets.ISO_8859_1);
        byte[] dataPass = ByteBuffer.allocate(nonceByte.length + dateByte.length + passByte.length)
                .put(nonceByte)
                .put(dateByte)
                .put(passByte)
                .array();

        byte[] sha1Pass = sha1(dataPass);
        String nonceBase64 = Base64.getEncoder().encodeToString(nonceByte);
        String password = Base64.getEncoder().encodeToString(sha1Pass);

        JSONObject header = new JSONObject();
        JSONObject securityData = new JSONObject();
        ApplicationService.securityObject = securityData;
        JSONObject userNameTokenData = new JSONObject();
        JSONObject envelopeData = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject payload = new JSONObject();
        try {
            userNameTokenData.put("Username", account);
            userNameTokenData.put("Password", password);
            userNameTokenData.put("Nonce", nonceBase64);
            userNameTokenData.put("Created", formattedToday);
            securityData.put("UsernameToken", userNameTokenData);
            header.put("Security", securityData);
            envelopeData.put("Header", header);
            body.put("GetDeviceInformation", "");
            envelopeData.put("Body", body);
            payload.put("Envelope", envelopeData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "processDataAuthen: " + payload);
        return payload;

    }
    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        sendUDP();
    }
    private void sendUDP(){
        serverFinder = new ServerFinder(
                udpHandle,
                DataChannelConfig.getUDPScanDevice());
        serverFinder.start();
    }
    private void clientEnd(){
        serverFinder = null;
    }
    public class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_UDP_DATA_SUCCESS:
                    adapterDeviceLan.notifyDataSetChanged();
                    break;
                case ApplicationService.GET_UDP_DATA_FAIL:
                    ApplicationService.showToast("Nhận dữ liệu thất bại", false);

                    break;
                case ApplicationService.CONNECT_CLIENT_SUCCESS:
                    ApplicationService.showToast("Kết nối client thành công", false);

                    break;
                case ApplicationService.CONNECT_CLIENT_FAIL:
                    ApplicationService.showToast("Kết nối client thất bại", false);
                    break;
                case ApplicationService.GET_CAM_LAN_INFO_FAIL:
                    ApplicationService.showToast("Lấy thông tin cam thất bại", true);
                    break;
                case ApplicationService.GET_CAM_LAN_INFO_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), CameraDetailLan.class);
                    intent.putExtra("linkLan", linkCam );
                    try {
                        JSONObject object = new JSONObject(message.obj.toString());
                        Log.e(TAG, "handleMessage: " +object.getString("RTSP_URL") );
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
                    release();
                    startActivity(intent);
                    break;
                case ApplicationService.CONNECT_CLIENT_END:
                    ApplicationService.showToast("Kết thúc tiến trình", false);
                    clientEnd();
                    break;
            }
        }


    }

}