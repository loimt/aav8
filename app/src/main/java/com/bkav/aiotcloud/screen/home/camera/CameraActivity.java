package com.bkav.aiotcloud.screen.home.camera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.setting.DayNightModeFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.ImageSettingFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.LightSettingMain;
import com.bkav.aiotcloud.screen.home.camera.playback.Playback;
import com.bkav.aiotcloud.screen.home.camera.setting.VideoSettingFragment;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.shopgun.android.zoomlayout.ZoomLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity implements ClientHandler {

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
        getDetailCam(id);
        if (isAvailable) {
            try {
                client.sendMessage(DataChannelConfig.getImageInfo(tokenVS));
                if (isBoxChannel) {
                    webRTCClient.sendMessage(DataChannelConfig.getOption(tokenVS));
                }
            } catch (Exception e) {
                this.showNotification(e.getMessage());
            }
        }
    }

    @Override
    public void onReceiveData(WebRTCClient client, String data) {
        try {
            this.processResponseDataChannel(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setDataLand();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setDataPor();
        }
    }
    private int id;
    private String objectID;
    private String camName;
    private String regionId;
    private String model;
    private RelativeLayout editCam;
    private static final String TAG = "CameraActivity";
    private ImageView fullScreen;
    private ZoomLayout zoomLayout;
    private RelativeLayout viewLayout;
    private RelativeLayout settingLayout;
    private SurfaceViewRenderer cameraView;
    private CameraItem cameraItem;
    private TextView nameCamera;
    private RelativeLayout backImage;
    private ImageView thumImage;
    private ProgressBar loading;
    private LightSettingMain lightSettingMain;
    private RelativeLayout touchLayout;
    private JSONObject appearance;
    private JSONObject orientation;
    private boolean wdrValue;
    private String white_balance;
    private String ai_turning;
    private boolean trackReady = false;
    private WebRTCClient webRTCClient;
    private RelativeLayout bar;
    private String Tag = "CameraActivity";
    private String sourceToken;
    private boolean isBoxChannel;
    private TextView noDataPlayback;
    private String tokenVS = "";
    private String mainStream = "";
    private String subStream = "";
    private OrientationEventListener sensorEvent;

    private enum SensorStateChangeActions {
        WATCH_FOR_LANDSCAPE_CHANGES, SWITCH_FROM_LANDSCAPE_TO_STANDARD, WATCH_FOR_POTRAIT_CHANGES, SWITCH_FROM_POTRAIT_TO_STANDARD;
    }

    private SensorStateChangeActions mSensorStateChanges;


    private void setDataLand() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null)
                controller.hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        fullScreen.setVisibility(View.GONE);
        RelativeLayout.LayoutParams paramsViewLayout = (RelativeLayout.LayoutParams) viewLayout.getLayoutParams();
        paramsViewLayout.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsViewLayout.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        viewLayout.setLayoutParams(paramsViewLayout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) settingLayout.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_END);
        params.removeRule(RelativeLayout.BELOW);
        settingLayout.setLayoutParams(params);
        bar.setVisibility(View.GONE);
    }

    private void setDataPor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null)
                controller.show(WindowInsets.Type.statusBars());
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        fullScreen.setVisibility(View.VISIBLE);
        fullScreen.bringToFront();
        viewLayout.getLayoutParams().height = (int) getResources().getDimension(R.dimen._200sdp);
        viewLayout.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) settingLayout.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.BELOW, R.id.viewLayout);
        settingLayout.setLayoutParams(params);
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.camera_detail);
        init();
        action();
        setData(savedInstanceState);
    }
    private void setData(Bundle savedInstanceState){
        fullScreen.bringToFront();
        this.nameCamera.setText(cameraItem.getCameraName());
        this.initWebRTCClient(this.cameraView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setDataLand();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setDataPor();
        }
        if(cameraItem.getSnapShotUrl().length() != 0){
            Picasso.get().load(cameraItem.getSnapShotUrl()).fit().centerCrop()
                    .into(thumImage);
        }

        if (savedInstanceState == null ) {
            Bundle bundle = new Bundle();
            bundle.putInt("ID", id);
            lightSettingMain.setArguments(bundle);
            this.getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.settingFragment, lightSettingMain)
                    .commit();
        }
    }
    private void action() {
        this.fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goFullScreen();
            }
        });
        this.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }


    private int count = 1;
    ArrayList<JSONObject> playbacks = new ArrayList<>();
    private CommonClientOptions clientOptions;

    private void getDataPlaybackCamera(JSONObject data) {
        try {
//            Log.e("xxxxx ", data.getJSONObject("get_video_playback_response").getJSONObject("data").getJSONArray("files").toString());
            JSONArray recordingFiles = data.getJSONObject("get_video_playback_response").getJSONObject("data").getJSONArray("files");
            for (int index = 0; index < recordingFiles.length(); index++) {
                JSONObject playbackItem = recordingFiles.getJSONObject(index);
                playbacks.add(playbackItem);
            }

            if (playbacks.size() > 0) {
                this.lightSettingMain.paintSelectData(playbacks);
            } else {
                noDataPlayback.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startPlaybackCamera(String fileName){
        clientOptions.streamMode("PLAYBACK");
        clientOptions.streamSRC(fileName);
        webRTCClient.restart();
        Log.e("cameraActivity", fileName);
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

    private void init() {
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.getCameraId() == id) {
                this.cameraItem = cameraItem;
            }
        }
//        this.editCam = findViewById(R.id.editCam);
        this.fullScreen = findViewById(R.id.full_screen);
        this.zoomLayout = findViewById(R.id.zoomLayout);
        this.bar = findViewById(R.id.bar);
        this.noDataPlayback = findViewById(R.id.noDataPlayback);
        this.viewLayout = findViewById(R.id.viewLayout);
        this.cameraView = findViewById(R.id.camera_view);
        this.nameCamera = findViewById(R.id.nameCamera);
        this.backImage = findViewById(R.id.backIcon);
        this.thumImage = findViewById(R.id.thumImage);
        this.loading = findViewById(R.id.progressBar);
        this.touchLayout = findViewById(R.id.touchLayout);
        this.settingLayout = findViewById(R.id.setting_layout);
        this.lightSettingMain = new LightSettingMain();


    }

    private void initWebRTCClient(SurfaceViewRenderer cameraView) {
        clientOptions = BrManager.createDefaultClientOptions();
        // TODO update streamSRC

        try {
            isBoxChannel = cameraItem.getBoxId() != 0;
            if (isBoxChannel) {
                JSONObject profileCamera = new JSONObject(this.cameraItem.getCameraInfo());
                tokenVS = profileCamera.getString("TokenVS");
                sourceToken = profileCamera.getJSONArray("ListTokenMP").getJSONObject(0).getString("Token");
                JSONArray profileChannel = profileCamera.getJSONArray("ListTokenMP");
                for (int index = 0; index < profileChannel.length(); index++){
                    if (profileChannel.getJSONObject(index).getString("Name").contains("MainStream")){
                        this.mainStream = profileChannel.getJSONObject(index).getString("Token");
                    } else if (profileChannel.getJSONObject(index).getString("Name").contains("SubStream")){
                        this.subStream = profileChannel.getJSONObject(index).getString("Token");
                    }
                }

                sourceToken = profileChannel.getJSONObject(0).getString("Token");
                clientOptions.streamSRC(sourceToken);
            }
        } catch (JSONException e) {
            Log.e(Tag, e.toString());
            e.printStackTrace();
        }

        this.webRTCClient = new WebRTCClient(BrManager.signalingClient, cameraItem.getPeerID(), clientOptions);
        this.webRTCClient.setClientHandler(this);
        this.webRTCClient.setRemoteVideoView(cameraView);
    }

    private void onTrackReady(boolean isTrackReady) {
        if (isTrackReady){
            this.lightSettingMain.playPlayback();
            this.runOnUiThread(() -> {
                this.loading.setVisibility(View.GONE);
                this.noDataPlayback.setVisibility(View.GONE);
//                getDetailCam(id);Leo1201

            });
        }
        if (isTrackReady == this.trackReady) {
            return;
        }
        this.trackReady = isTrackReady;
        if (this.trackReady) {
            this.runOnUiThread(() -> {
                this.thumImage.setVisibility(View.GONE);
                this.loading.setVisibility(View.GONE);
                this.touchLayout.setVisibility(View.GONE);
//                getDetailCam(id);
            });
//            BrManager.runTask(() -> this.webRTCClient.stopMonitoringTrack());
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

    private void showNotification(String content) {
        this.runOnUiThread(() -> {
            if (content == null || content.isEmpty()) {
                return;
            }
            Toast toast = Toast.makeText(this, content, Toast.LENGTH_LONG);
            toast.show();
        });
    }

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                processDataResponse(isBoxChannel, jsonData);
            }
        });
    }

    private void processDataResponse(boolean isBox, JSONObject jsonData) {
        JSONObject header = null;
        try {
            header = jsonData.getJSONObject("envelope").getJSONObject("header");
            Log.e("response webrtc", jsonData.toString());
            if (!isBox) {
                switch (header.getString("component")) {
                    case ImageSettingFragment.IMAGE_SETTING:
                        JSONObject data = jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("get_imagesetting_response").getJSONObject("data");
                        lightSettingMain.updateUIImageSetting(jsonData.getJSONObject("envelope").getJSONObject("body"));
                        appearance = data.getJSONObject("appearance");
                        wdrValue = data.getBoolean("wdr");
                        white_balance = data.getString("white_balance");
                        orientation = data.getJSONObject("orientation");
                        ai_turning = data.getString("ai_turning");
                        break;
                    case DayNightModeFragment.DAY_NIGHT_SETTING:
                        lightSettingMain.updateUIImageSetting(jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("get_daynight_setting_response"));
                        break;
                    case VideoSettingFragment.VIDEO_SETTING:
                        if (!jsonData.getJSONObject("envelope").getJSONObject("body").isNull("get_video_playback_response")) {
                            getDataPlaybackCamera(jsonData.getJSONObject("envelope").getJSONObject("body"));
                        }
                        lightSettingMain.updateUIImageSetting(jsonData.getJSONObject("envelope").getJSONObject("body"));
                        if (jsonData.getJSONObject("envelope").getJSONObject("body").isNull("put_displaysetting_response")) {
                            if (jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("put_displaysetting_response").getBoolean("status")) {
                                ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                            } else {
                                ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_un_success"), true);
                            }
                        }
                        if (jsonData.getJSONObject("envelope").getJSONObject("body").isNull("put_videosetting_response")) {
                            if (jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("put_videosetting_response").getBoolean("status")) {
                                ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                            } else {
                                ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_un_success"), true);
                            }
                        }
                        break;
                }
            } else {
                switch (header.getString("component")) {
                    case "image_service":
                        if (jsonData.getJSONObject("envelope").getJSONObject("body").get("func").equals("get_options_response")) {
                            lightSettingMain.setMinMaxImage(jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("data").getJSONObject("data"));
                        }

                        if (jsonData.getJSONObject("envelope").getJSONObject("body").get("func").equals("get_options_response")) {
                            lightSettingMain.setMinMaxImage(jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("data").getJSONObject("data"));
                        }
                        JSONObject data = jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("data").getJSONObject("data");
                        lightSettingMain.updateUIImageSetting(data);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initialiseSensor(boolean enable) {
        sensorEvent = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (null != mSensorStateChanges
                        && mSensorStateChanges == SensorStateChangeActions.WATCH_FOR_LANDSCAPE_CHANGES
                        && ((orientation >= 60 && orientation <= 120) || (orientation >= 240 && orientation <= 300))) {
                    mSensorStateChanges = SensorStateChangeActions.SWITCH_FROM_LANDSCAPE_TO_STANDARD;
                } else if (null != mSensorStateChanges
                        && mSensorStateChanges == SensorStateChangeActions.SWITCH_FROM_LANDSCAPE_TO_STANDARD
                        && (orientation <= 40 || orientation >= 320)) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    mSensorStateChanges = null;
                    sensorEvent.disable();
                } else if (null != mSensorStateChanges
                        && mSensorStateChanges == SensorStateChangeActions.WATCH_FOR_POTRAIT_CHANGES
                        && ((orientation >= 300 && orientation <= 359) || (orientation >= 0 && orientation <= 45))) {
                    mSensorStateChanges = SensorStateChangeActions.SWITCH_FROM_POTRAIT_TO_STANDARD;
                } else if (null != mSensorStateChanges
                        && mSensorStateChanges == SensorStateChangeActions.SWITCH_FROM_POTRAIT_TO_STANDARD
                        && ((orientation <= 300 && orientation >= 240) || (orientation <= 130 && orientation >= 60))) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    mSensorStateChanges = null;
                    sensorEvent.disable();
                }
            }
        };
        if (enable)
            sensorEvent.enable();
    }

    public void goFullScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        mSensorStateChanges = SensorStateChangeActions.WATCH_FOR_LANDSCAPE_CHANGES;
        if (null == sensorEvent)
            initialiseSensor(true);
        else
            sensorEvent.enable();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void shrinkToPotraitMode() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorStateChanges = SensorStateChangeActions.WATCH_FOR_POTRAIT_CHANGES;
        if (null == sensorEvent)
            initialiseSensor(true);
        else
            sensorEvent.enable();
    }
    private void getDetailCam(int id){
        @SuppressLint("DefaultLocale") String urlCam = String.format(ApplicationService.URL_GET_CAM_DETAIL, id);
        ApplicationService.requestManager.getCameraDetail(urlCam);
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            if (ApplicationService.CONNECTED_CAMERA == message.what) {
                thumImage.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                touchLayout.setVisibility(View.GONE);
            }
            try {
                if (!webRTCClient.isReadySendData()) {
                    // TODO need update
                    return;
                }
                switch (message.what) {
                    case ApplicationService.CONTROL_PTZ:
                        webRTCClient.sendMessage(DataChannelConfig.control((String) message.obj));
                        break;
                    case ApplicationService.DISPLAY_SETTING_CAMERA:
                        webRTCClient.sendMessage((String) message.obj);
                        break;
                    case ApplicationService.SETTING_FOCUS:
                        webRTCClient.sendMessage(DataChannelConfig.focus((Integer) message.obj));
                        break;
                    case ApplicationService.ZOOM_PTZ:
                        webRTCClient.sendMessage(DataChannelConfig.zoom((Integer) message.obj));
                        break;
                    case ApplicationService.GET_IMAGE_INFO_SETTING:
                        webRTCClient.sendMessage(DataChannelConfig.getImageInfo(tokenVS));
                        break;
                    case ApplicationService.SETTING_WHITE_BALANCE:
                        webRTCClient.sendMessage(DataChannelConfig.settingImage(appearance, orientation, ai_turning, wdrValue, (String) message.obj));
                        break;
                    case ApplicationService.SETTING_ROTATE:
                        webRTCClient.sendMessage(DataChannelConfig.settingImage(appearance, new JSONObject((String) message.obj), ai_turning, wdrValue, white_balance));
                        break;
                    case ApplicationService.SETTING_DAY_NIGHT:
                        webRTCClient.sendMessage(DataChannelConfig.setDayNight((String) message.obj));
                        break;
                    case ApplicationService.GET_DAY_NIGHT_MODE:
                        webRTCClient.sendMessage(DataChannelConfig.getDayNightInfo());
                        break;
                    case ApplicationService.GET_INFO_VIDEO:
                        webRTCClient.sendMessage(DataChannelConfig.getVideoInfor((String) message.obj));
                        break;
                    case ApplicationService.SET_INFO_VIDEO:
                        webRTCClient.sendMessage(DataChannelConfig.settingVideo((String) message.obj));
                        break;
                    case ApplicationService.SETTING_VIDEO:
                        webRTCClient.sendMessage((String) message.obj);
                        break;
                    case ApplicationService.PLAY_VIDEO_PLAYBACK:
                        noDataPlayback.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        JSONObject dataPlayback = new JSONObject((String) message.obj);
                        if (!isBoxChannel) {
                            webRTCClient.sendMessage(DataChannelConfig.selectRecordCamera(cameraItem.getPeerID(),dataPlayback.getString("filePlay"), dataPlayback.getInt("timeSeek")));
                        } else {
                            webRTCClient.sendMessage(DataChannelConfig.selectRecotdBox(dataPlayback.getString("filePlay"), dataPlayback.getInt("timeSeek")));
                        }
                        break;
                    case ApplicationService.START_PLAYBACK_CAMERA:
                          startPlaybackCamera((String) message.obj);
                        break;
                    case ApplicationService.NO_DATA_PLAYBACK:
//                        webRTCClient.stop();
                        noDataPlayback.setVisibility(View.VISIBLE);
                        if (isBoxChannel){
                            webRTCClient.sendMessage(DataChannelConfig.pauVideoPlaybacbk());
                        } else {
                            webRTCClient.sendMessage(DataChannelConfig.pauVideoPlaybacbk(cameraItem.getPeerID()));
                        }
                        break;
                    case ApplicationService.SETTING_IMAGE:
                        if (isBoxChannel) {
                            webRTCClient.sendMessage(DataChannelConfig.settingImageBox(tokenVS, (String) message.obj));
                        } else {
                            webRTCClient.sendMessage(DataChannelConfig.settingImage(new JSONObject((String) message.obj), orientation, ai_turning, wdrValue, white_balance));
                        }
                        break;
                    case ApplicationService.GET_DISPLAY_INFO_SETTING:
                        webRTCClient.sendMessage(DataChannelConfig.getDisplayInfo());
                        break;
                    case ApplicationService.GET_RECORD_JOB_PLAYBACK:
                        webRTCClient.sendMessage(DataChannelConfig.getFilesRecordJob());
                        break;
//                    case ApplicationService.GET_CAM_DETAIL_SUCCESS:
//                        editCam.setVisibility(View.VISIBLE);
//                        JSONObject object = new JSONObject(message.obj.toString());
//                        objectID = object.getString("objectGuid");
//                        camName = object.getString("deviceName");
//                        model = object.getString("serialNumber");
//                        regionId = String.valueOf(object.getInt("regionId"));
//                        break;
//                    case ApplicationService.GET_CAM_DETAIL_FAIL:
//                        editCam.setVisibility(View.GONE);
//                        break;
//                    case ApplicationService.GET_VIDEO_PLAYBACK:
//                        try {
//                            if (isBoxChannel) {
//                                webRTCClient.sendMessage(DataChannelConfig.getFilesRecord(sourceToken));
//                            } else {
//                                webRTCClient.sendMessage(DataChannelConfig.getFilesRecordCamera(cameraItem.getPeerID()));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showNotification(ex.getMessage());
            }
        }
    }
}