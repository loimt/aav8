package com.bkav.aiotcloud.screen.home.device.genneralsetting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.FilterCamera;
import com.bkav.aiotcloud.screen.home.camera.playback.Playback;
import com.bkav.aiotcloud.screen.home.camera.setting.DayNightModeFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.ImageSettingFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.VideoSettingFragment;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;

public class GenneralInformation extends AppCompatActivity implements ClientHandler {
    public static final String ID = "id";
    public static final String NAME_REGION = "nameRegion";
    @Override
    public void onError(WebRTCClient client, Object error, boolean isFinished) {
        this.showNotification(error == null ? "Error: null" : error.toString());
    }

    @Override
    public void onTrackReceiveStat(WebRTCClient client, PeerConnection peerConnection, TrackStatReport report) {
        this.onTrackReady(report.frameRate() > 0);
    }

    @Override
    public void onSendDataAble(WebRTCClient client, boolean isAvailable) {
        if (isAvailable) {
            try {
                this.webRTCClient.sendMessage(DataChannelConfig.getInforDeviceBox());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onReceiveData(WebRTCClient client, String data) {
        try {
            Log.e("data response ", data);
            this.processResponseDataChannel(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.general_information);
        for (DeviceItem deviceItem : ApplicationService.deviceItems) {
            if (deviceItem.getCameraId() == id) {
                this.deviceItem = deviceItem;
            }
        }
        idRegion = deviceItem.getRegionId();

        this.init();
        this.action();
        this.initWebRTCClient();
        getDetailCam(this.deviceItem.getCameraId());
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
    protected void onDestroy() {
        BrManager.runTask(() -> {
            if (this.webRTCClient != null) {
                this.webRTCClient.stop();
            }
            this.webRTCClient = null;
        });
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();

    }

    private DeviceItem deviceItem;
    private CommonClientOptions clientOptions;
    private WebRTCClient webRTCClient;

    private boolean isBoxChannel;
    private String tokenVS;
    private String sourceToken;

    private TextView title;

    private TextView serialTx;
    private EditText serialInput;

    private TextView modelTx;
    private EditText modelInput;

    private TextView versionTx;
    private EditText versionInput;

    private TextView nameDeviceTx;
    private EditText nameDeviceInput;


    private TextView branchTx;
    private TextView branchInput;

    private Button save;
    private int idRegion;
    private String objectGuid = "";


    private RelativeLayout backIm;

    private void initWebRTCClient() {
        clientOptions = BrManager.createDefaultClientOptions();
        // TODO update streamSRC
        clientOptions.streamMode("DATA");
        clientOptions.streamSRC(null);
        try {
            isBoxChannel = deviceItem.getBoxId() != 0;
            if (isBoxChannel) {
                JSONObject profileCamera = new JSONObject(this.deviceItem.getCameraInfo());
                tokenVS = profileCamera.getString("TokenVS");
                sourceToken = profileCamera.getJSONArray("ListTokenMP").getJSONObject(0).getString("Token");
                clientOptions.streamSRC(sourceToken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.webRTCClient = new WebRTCClient(BrManager.signalingClient, deviceItem.getPeerID(), clientOptions);
        this.webRTCClient.setClientHandler(this);

//        this.webRTCClient.setRemoteVideoView(cameraView);
    }


    private void getDetailCam(int id){
        @SuppressLint("DefaultLocale") String urlCam = String.format(ApplicationService.URL_GET_CAM_DETAIL, id);
        ApplicationService.requestManager.getCameraDetail(urlCam);
    }

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject header = null;
                try {
                    header = jsonData.getJSONObject("envelope").getJSONObject("header");
                    switch (header.getString("component")) {
                        case "device":
                            JSONObject data = jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("data").getJSONObject("data");
                            versionInput.setText(data.getString("firmware_version"));
                            serialInput.setText(data.getString("serial_number"));
                            modelInput.setText(data.getString("model"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void init() {
        this.backIm = findViewById(R.id.backIm);
        this.title = findViewById(R.id.title);

        this.serialTx = findViewById(R.id.serialTx);
        this.serialInput = findViewById(R.id.serialInput);

        this.modelTx = findViewById(R.id.modelTx);
        this.modelInput = findViewById(R.id.modelInput);

        this.versionTx = findViewById(R.id.versionTx);
        this.versionInput = findViewById(R.id.versionInput);

        this.nameDeviceTx = findViewById(R.id.nameDeviceTx);
        this.nameDeviceInput = findViewById(R.id.nameDeviceInput);

        this.branchInput = findViewById(R.id.branchInput);
        this.branchTx = findViewById(R.id.branchTx);

        this.save = findViewById(R.id.save);

        this.title.setText(LanguageManager.getInstance().getValue("edit_infor"));
        this.serialTx.setText(LanguageManager.getInstance().getValue("serial_number"));
        this.versionTx.setText(LanguageManager.getInstance().getValue("version_software"));
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        this.branchTx.setText(LanguageManager.getInstance().getValue("region"));

        this.nameDeviceInput.setText(deviceItem.getCameraName());

        RelativeLayout branchView = findViewById(R.id.branchView);
        branchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RegionList.class);
                startActivityIntent.launch(intent);
            }
        });
    }

    private void action() {
        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameDeviceInput.getText().length() == 0){
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("name_device_cannot_empty"), true);
                } else {
                    JSONObject payload = new JSONObject();
                    try {
                        payload.put("objectGuidCamera", objectGuid);
                        payload.put("cameraName", nameDeviceInput.getText());
                        payload.put("regionId", idRegion);
                        ApplicationService.requestManager.updateCam(payload, ApplicationService.URL_UPDATE_CAM);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
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

    private void onTrackReady(boolean isTrackReady) {
        if (isTrackReady) {

        }
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        idRegion = data.getExtras().getInt(ID);
                        branchInput.setText(data.getExtras().getString(NAME_REGION));
                    }
                }
            });

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

                switch (message.what) {
                    case ApplicationService.GET_CAM_DETAIL_SUCCESS:
                        JSONObject object = null;
                        try {
                            object = new JSONObject(message.obj.toString());
                            nameDeviceInput.setText(object.getString("deviceName"));
                            branchInput.setText(object.getString("regionName"));
                            objectGuid = object.getString("objectGuid");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case ApplicationService.UPDATE_CAM_SUCCESS:
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                        finish();
                        break;
                    case ApplicationService.UPDATE_CAM_FAIL:
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_un_success"), true);
                        finish();
                        break;


                }
        }
    }
}
