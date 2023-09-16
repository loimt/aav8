package com.bkav.aiotcloud.screen.setting.face.edit;

import static com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity.AI_ITEM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.entity.aiobject.CameraConfig;
import com.bkav.aiotcloud.entity.aiobject.ZoneAIObject;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.AndroidCameraCapturer;
import com.bkav.aiotcloud.screen.home.camera.CustomPeerConnectionObserver;
import com.bkav.aiotcloud.screen.home.camera.CustomSdpObserver;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.ScheduleAdapter;
import com.bkav.aiotcloud.screen.setting.face.ScheduleItem;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;
import com.bkav.aiotcloud.view.PaintView;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.List;

public class InfoAIScreen extends AppCompatActivity implements ScheduleAdapter.OnMenuItemClickListener, ClientHandler {
    public static final String EDIT_DONE = "edit_done";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.edit_screen_ai);
        this.position = getIntent().getIntExtra(AI_ITEM, 0);
        aiObject = ApplicationService.cameraConfigs.get(position);
        aiObject.getZones();
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.getCameraId() == aiObject.getCameraId()) {
                this.cameraItem = cameraItem;
            }
        }

        Intent intent = getIntent();
        type = intent.getStringExtra(SettingFragment.TYPE);
        init();
        if (!type.equals(SettingFragment.FACE_RECOGNIZE)){
            this.sensitivityLayout.setVisibility(View.GONE);
            this.faceLayout.setVisibility(View.GONE);
        }

        if (type.equals(SettingFragment.LICENSE_PLATE)){
            this.nationLayout.setVisibility(View.VISIBLE);
        }
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
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();

        getInforAIObject();
    }

    private TextView stateAI;
    private SwitchCompat swOnOffAI;

    private TextView locate;
    private TextView locateValue;

    private TextView faceSize;
    private TextView faceSizeValue;


    private TextView sensitivityTx;
    private TextView sensitivityValue;

    private TextView schedule;
    private TextView scheduleValue;
    private TextView title;
    private Boolean isTouched = false;
    private Boolean isBoxChannel;
    private WebRTCClient webRTCClient;
    private PaintView paintView;
    private SurfaceViewRenderer cameraView;
    private ProgressBar loading;
    private int width;
    private String type;
    private JSONObject aiItem;
    private int isActive;
    private RelativeLayout settingIM;

    private ImageView thumImage;
    private AIObject aiObject;
    private CameraItem cameraItem;
    private RelativeLayout sensitivityLayout;
    private RelativeLayout faceLayout;
    private String Tag = "InfoAIScreen";

    private RelativeLayout nationLayout;
    private TextView nationTx;
    private TextView nationValue;

    private ScheduleAdapter scheduleAdapter;
    private RecyclerView listSchedule;
    private String tokenVS = "";
    private ArrayList<ScheduleItem> scheduleItems;
    private RelativeLayout addScheuleLayout;
    private boolean trackReady = false;
    private int position;

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                processDataResponse(isBoxChannel, jsonData);
            }
        });
    }


    private void initWebRTCClient(SurfaceViewRenderer cameraView) {
        CommonClientOptions clientOptions = BrManager.createDefaultClientOptions();
        // TODO update streamSRC

        try {

            isBoxChannel = cameraItem.getBoxId() != 0;
            if (isBoxChannel) {
                JSONObject profileCamera = new JSONObject(this.cameraItem.getCameraInfo());
                tokenVS = profileCamera.getString("TokenVS");
                String data = profileCamera.getJSONArray("ListTokenMP").getJSONObject(0).getString("Token");
                clientOptions.streamSRC(data);
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
        if (isTrackReady == this.trackReady) {
            return;
        }
        this.trackReady = isTrackReady;
        if (this.trackReady) {
            this.runOnUiThread(() -> {
                this.thumImage.setVisibility(View.GONE);
                this.loading.setVisibility(View.GONE);
            });
//            BrManager.runTask(() -> this.webRTCClient.stopMonitoringTrack());
        }
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

    @SuppressLint("NotifyDataSetChanged")
    public void setDataEdit(JSONObject editItem) {
        Log.e("ai infor responseBody", editItem.toString());
        try {

            String parameterValue = editItem.getString("extra");
            JSONArray scheduleDtails = editItem.getJSONArray("scheduleDetails");
            String frequencyType = editItem.getJSONObject("schedule").getString("frequencyType");
            this.isActive = editItem.getInt("isActiveAI");
            if (this.isActive == 1){
                this.swOnOffAI.setChecked(true);
            } else {
                this.swOnOffAI.setChecked(false);
            }
            if (frequencyType.equals("daily")) {
                addScheuleLayout.setVisibility(View.GONE);
                this.scheduleValue.setText(LanguageManager.getInstance().getValue("alway_active"));
            } else {
                this.scheduleValue.setText(LanguageManager.getInstance().getValue("setting_handmade"));
                scheduleItems.clear();
                addScheuleLayout.setVisibility(View.VISIBLE);
                for (int index = 0; index < scheduleDtails.length(); index++) {
                    JSONObject scheuleitem = scheduleDtails.getJSONObject(index);

                    String dates = scheuleitem.getString("dayOfWeekOrMonth");
                    String timeStart = scheuleitem.getString("fromAt");
                    timeStart = timeStart.substring(0, timeStart.length() - 3);
                    String timeEnd = scheuleitem.getString("toAt");
                    timeEnd = timeEnd.substring(0, timeEnd.length() - 3);
                    ScheduleItem scheduleItem = new ScheduleItem(timeStart, timeEnd, dates);
                    if (!dates.equals("[]")){
                        scheduleItems.add(scheduleItem);
                    }
                }
                scheduleAdapter.notifyDataSetChanged();
            }

            if (type.equals(SettingFragment.LICENSE_PLATE)){
                JSONObject jsonParameter = new JSONObject(parameterValue);
                setValueNation(jsonParameter.getString("nation"));
            }
            if (type.equals(SettingFragment.FACE_RECOGNIZE)){
                JSONObject jsonParameter = new JSONObject(parameterValue);
                int currentFace = jsonParameter.getInt("profile_size");
                if (currentFace == 1) {
                    faceSizeValue.setText(LanguageManager.getInstance().getValue("veryBig"));
                } else if (currentFace == 2) {
                    faceSizeValue.setText(LanguageManager.getInstance().getValue("big"));
                } else if (currentFace == 3) {
                    faceSizeValue.setText(LanguageManager.getInstance().getValue("medium"));
                }else if (currentFace == 4) {
                    faceSizeValue.setText(LanguageManager.getInstance().getValue("small"));
                }else if (currentFace == 5) {
                    faceSizeValue.setText(LanguageManager.getInstance().getValue("verySmall"));
                }

                int sensitivity = jsonParameter.getInt("threshold_recognize");

                switch (sensitivity) {
                    case 1:
                        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("veryLow"));
                        break;
                    case 2:
                        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("low"));
                        break;
                    case 3:
                        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("medium"));
                        break;
                    case 4:
                        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("high"));
                        break;
                    case 5:
                        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("veryHigh"));
                        break;
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setValueNation(String value) {
        if (value.equals("VN")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("vietnam"));
        } else if (value.equals("USA")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("usa"));
        } else if (value.equals("MY")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("malaysia"));
        }
    }

    private void getInforAIObject() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("cameraId", aiObject.getCameraId());
            payload.put("monitorGuid", aiObject.getMonitorId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getInforAIDetail(payload, ApplicationService.URL_GET_INFO_AI_CONFIG);
    }


    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String type = data.getExtras().getString(StepThreeFragment.TYPE);
                        if (type.equals(EDIT_DONE)) {
                            finish();
                        }

                    }
                }
            });


    private void drawZones(AIObject aiObject) {
        ArrayList<ZoneAIObject> zoneAIObjects = new ArrayList<>();
        float scale = (float) 1920 / width;
        for (ZoneAIObject zoneAIObject : aiObject.getZones()) {
            List<Point> points = new ArrayList<>();
            if (!zoneAIObject.getZoneName().equals("Full screen")) {
                for (Point point : zoneAIObject.getCoordinate()) {
                    Point pointClone = new Point();
                    pointClone.x = (int) (point.x / scale);
                    pointClone.y = (int) (point.y / scale);
                    points.add(pointClone);
                }
                ZoneAIObject zoneItem = new ZoneAIObject(points, zoneAIObject.getZoneName());
                zoneAIObjects.add(zoneItem);
            }
        }
        paintView.drawZones(zoneAIObjects);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        this.paintView = findViewById(R.id.touchLayout);
//        this.cameraLayout = findViewById(R.id.cameraLayout);
        this.cameraView = findViewById(R.id.camera_view);
        this.loading = findViewById(R.id.progressBar);
        this.thumImage = findViewById(R.id.thumImage);

        this.stateAI = findViewById(R.id.stateCameraTx);
        this.swOnOffAI = findViewById(R.id.swOnOffAI);

        this.locate = findViewById(R.id.locateTx);
        this.locateValue = findViewById(R.id.locateValue);
        this.title = findViewById(R.id.title);

        this.faceSize = findViewById(R.id.sizeFaceTx);
        this.faceSizeValue = findViewById(R.id.sizeFaceValue);

        this.sensitivityTx = findViewById(R.id.sensitivityTx);
        this.sensitivityValue = findViewById(R.id.sensitivityValue);

        this.schedule = findViewById(R.id.scheduleTitleTx);
        this.scheduleValue = findViewById(R.id.scheduleValue);

        this.sensitivityLayout = findViewById(R.id.sensitivityLayout);
        this.faceLayout = findViewById(R.id.faceLayout);

        this.addScheuleLayout = findViewById(R.id.addScheuleLayout);
        TextView textSetting = findViewById(R.id.textSetting);
        this.settingIM = findViewById(R.id.settingIM);
        RelativeLayout back = findViewById(R.id.backIm);

        this.listSchedule = findViewById(R.id.listSchedule);
        this.listSchedule.setLayoutManager(new LinearLayoutManager(this));
        this.scheduleItems = new ArrayList<>();
        this.scheduleAdapter = new ScheduleAdapter(this, scheduleItems, this);
        this.listSchedule.setAdapter(scheduleAdapter);
        this.locateValue.setText(aiObject.getRegionName());

        this.nationLayout = findViewById(R.id.nationLayout);
        this.nationTx = findViewById(R.id.nationTx);
        this.nationValue = findViewById(R.id.nationValue);

        this.stateAI.setText(LanguageManager.getInstance().getValue("ai_state"));
        this.locate.setText(LanguageManager.getInstance().getValue("location"));
        this.sensitivityTx.setText(LanguageManager.getInstance().getValue("sensitivity"));
        this.faceSize.setText(LanguageManager.getInstance().getValue("size_face"));
        this.schedule.setText(LanguageManager.getInstance().getValue("schedule_active"));
        this.nationTx.setText(LanguageManager.getInstance().getValue("nation"));
        textSetting.setText(LanguageManager.getInstance().getValue("setting_parameter"));

        this.title.setText(aiObject.getCameraName());
        this.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.title.setSelected(true);
        // play camera rtsp
//        getUserMedia();
//        createPeerConnection(aiObject.getPeerID());
        Picasso.get().load(aiObject.getSnapShotUrl()).fit().centerCrop()
                .into(thumImage);

        this.initWebRTCClient(this.cameraView);
        // vẽ zone

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (width * 1080 / 1920));
        cameraView.setLayoutParams(layoutParams);
        drawZones(aiObject);

        this.swOnOffAI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouched) {
                    isTouched = false;
                    try {
//                        JSONObject processor = aiItem.getJSONObject("processor");
                        aiItem.put("isActiveAI", swOnOffAI.isChecked() ? 1 : 0);
//                        aiItem.put("processor", processor);
//                        Log.e("ai item", aiItem.toString());
                        ApplicationService.requestManager.configCamera(aiItem, ApplicationService.URL_CONFIG_CAMERA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        swOnOffAI.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouched = true;
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        settingIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DialogEditAI.class);
                intent.putExtra(AI_ITEM, position);
                intent.putExtra(SettingFragment.TYPE, type);
                intent.setFlags(0);
                startActivityIntent.launch(intent);

            }
        });
    }

    private void connectCamera() {
        thumImage.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onMenuItemClick(ScheduleItem scheduleItem, int position, int id) {

    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.CONNECTED_CAMERA:
                    connectCamera();
                    break;
                case ApplicationService.CONFIG_CAMERA_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                case ApplicationService.INFOR_AI_CONFIG_DETAIL:
                    String data = (String) message.obj;
                    try {
                        aiItem = new JSONObject(data);
                        setDataEdit(aiItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
