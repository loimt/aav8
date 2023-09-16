package com.bkav.aiotcloud.screen.home.device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.device.genneralsetting.GenneralInformation;
import com.bkav.aiotcloud.screen.home.device.network.NetworkScreen;
import com.bkav.aiotcloud.screen.home.device.timesetting.DeviceTimeSettingActivity;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;

public class DeviceActivity extends AppCompatActivity implements ClientHandler {
    public static final String ID = "id";
    @Override
    public void onError(WebRTCClient client, Object error, boolean isFinished) {
        this.showNotification(error == null ? "Error: null" : error.toString());
    }

    @Override
    public void onTrackReceiveStat(WebRTCClient client, PeerConnection peerConnection, TrackStatReport report) {
//        this.onTrackReady(report.frameRate() > 0);
    }

    @Override
    public void onReceiveData(WebRTCClient client, String data) {
        try {
            Log.e("resonpse ", data );
            this.processResponseDataChannel(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendDataAble(WebRTCClient client, boolean isAvailable) {
        if (isAvailable){
            try {
                this.webRTCClient.sendMessage(DataChannelConfig.getInforDeviceBox());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        BrManager.runTask(() -> {
            try {
                this.webRTCClient.start();
            } catch (Exception e) {
                e.printStackTrace();
                this.showNotification(e.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.device_activity);
        for (DeviceItem deviceItem : ApplicationService.deviceItems) {
            if (deviceItem.getCameraId() == id) {
                this.deviceItem = deviceItem;
            }
        }
        this.initWebRTCClient();

        this.init();
        this.action();
    }

    private DeviceItem deviceItem;
    private RelativeLayout backIm;
    private TextView title;

    private RelativeLayout inforLayout;
    private TextView generalInforTx;

    private RelativeLayout settingTimeLayout;
    private TextView settingTimeTx;

    private RelativeLayout internetSettingLayout;
    private TextView internetSettingTx;

    private RelativeLayout deviceChannelLayout;
    private TextView deviceChannelTx;

    private RelativeLayout saveLayout;
    private TextView saveTx;

    private RelativeLayout versionLayout;
    private TextView inforVersionTx;

    private RelativeLayout restartLayout;
    private TextView reloadTx;
    private CommonClientOptions clientOptions;
    private WebRTCClient webRTCClient;
    private boolean isBoxChannel;

    private String token = "";
    private String sourceToken;

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                processDataResponse(jsonData);
            }
        });
    }
    private void processDataResponse(JSONObject jsonObject){

    }


    private void init() {
        this.title = findViewById(R.id.title);
        this.inforLayout = findViewById(R.id.inforLayout);
        this.generalInforTx = findViewById(R.id.generalInforTx);
        this.settingTimeLayout = findViewById(R.id.settingTimeLayout);
        this.settingTimeTx = findViewById(R.id.settingTimeTx);

        this.internetSettingLayout = findViewById(R.id.internetSettingLayout);
        this.internetSettingTx = findViewById(R.id.internetSettingTx);

        this.deviceChannelLayout = findViewById(R.id.deviceChannelLayout);
        this.deviceChannelTx = findViewById(R.id.deviceChannelTx);

        this.saveLayout = findViewById(R.id.saveLayout);
        this.saveTx = findViewById(R.id.saveTx);

        this.inforLayout = findViewById(R.id.inforLayout);
        this.generalInforTx = findViewById(R.id.generalInforTx);

        this.versionLayout = findViewById(R.id.versionLayout);
        this.inforVersionTx = findViewById(R.id.inforVersionTx);

        this.restartLayout = findViewById(R.id.restartLayout);
        this.reloadTx = findViewById(R.id.reloadTx);

        this.backIm = findViewById(R.id.backIm);

        this.title.setText(this.deviceItem.getCameraName());

        this.generalInforTx.setText(LanguageManager.getInstance().getValue("general_information"));
        this.settingTimeTx.setText(LanguageManager.getInstance().getValue("time_settings"));
        this.internetSettingTx.setText(LanguageManager.getInstance().getValue("network_settings"));
        this.deviceChannelTx.setText(LanguageManager.getInstance().getValue("device_channels"));
        this.saveTx.setText(LanguageManager.getInstance().getValue("storage"));
        this.reloadTx.setText(LanguageManager.getInstance().getValue("restart"));
    }

    private void action() {
        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.inforLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceActivity.this, GenneralInformation.class);
                intent.putExtra("ID", deviceItem.getCameraId());
                startActivity(intent);
            }
        });

        this.settingTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceActivity.this, DeviceTimeSettingActivity.class);
                intent.putExtra(ID, deviceItem.getCameraId());
                startActivity(intent);
            }
        });

        this.internetSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceActivity.this, NetworkScreen.class);
//                intent.putExtra("ID", deviceItem.getCameraId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        BrManager.runTask(() -> {
            if (this.webRTCClient != null) {
                this.webRTCClient.stop();
            }
            this.webRTCClient = null;
        });
        super.onDestroy();
    }

    private void showNotification(String content) {
        this.runOnUiThread(() -> {
            if (content == null || content.isEmpty()) {
                return;
            }
            Toast toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
            toast.show();
        });
    }

    private void initWebRTCClient() {
        clientOptions = BrManager.createDefaultClientOptions();
        // TODO update streamSRC
        clientOptions.streamMode("DATA");
        clientOptions.streamSRC(null);
        try {
            isBoxChannel = deviceItem.getBoxId() != 0;
            if (isBoxChannel) {
                JSONObject profileCamera = new JSONObject(this.deviceItem.getCameraInfo());
                token = profileCamera.getString("TokenVS");
                sourceToken = profileCamera.getJSONArray("ListTokenMP").getJSONObject(0).getString("Token");
                clientOptions.streamSRC(sourceToken);
            }
        } catch (JSONException e) {
            Log.e("Tag", e.toString());
            e.printStackTrace();
        }

        this.webRTCClient = new WebRTCClient(BrManager.signalingClient, deviceItem.getPeerID(), clientOptions);
        this.webRTCClient.setClientHandler(this);

//        this.webRTCClient.setRemoteVideoView(cameraView);
    }
}
