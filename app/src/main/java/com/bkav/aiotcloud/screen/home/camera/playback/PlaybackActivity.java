package com.bkav.aiotcloud.screen.home.camera.playback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.Language;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.CameraActivity;
import com.bkav.aiotcloud.screen.home.camera.OptionEditCam;
import com.bkav.aiotcloud.screen.home.camera.setting.DayNightModeFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.ImageSettingFragment;
import com.bkav.aiotcloud.screen.home.camera.setting.LightSettingMain;
import com.bkav.aiotcloud.screen.home.camera.setting.TimePartItem;
import com.bkav.aiotcloud.screen.home.camera.setting.VideoSettingFragment;
import com.bkav.aiotcloud.view.TimeRuleView;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.shopgun.android.zoomlayout.ZoomLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.SurfaceViewRenderer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PlaybackActivity extends AppCompatActivity implements ClientHandler {
    public static final int ACTION_UP = 1;
    public static Handler playbackHandle = null;
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
    private TextView timeNow;
    private TimeRuleView timeRuleView;
    private RelativeLayout filter;
    private MaterialCalendarView calendarView;
    private CountDownTimer countDownTimer;
    private int maxValue;
    private boolean isPlaying = false;

    private ArrayList<TimePartItem> timePartItems = new ArrayList<>();
    private ArrayList<TimePartItem> timeDatas = new ArrayList<>();
    private ArrayList<TimePartItem> timeDatasCalculated = new ArrayList<>();
    private JSONArray dateHasData;
    private ImageView fullScreen;
    private RelativeLayout viewLayout;
    private SurfaceViewRenderer cameraView;
    private CameraItem cameraItem;
    private TextView nameCamera;
    private RelativeLayout backImage;
    private ImageView thumImage;
    private ProgressBar loading;
//    private RelativeLayout touchLayout;
    private boolean trackReady = false;
    private WebRTCClient webRTCClient;
    private RelativeLayout backLand;
    private TextView nameLand;
    private RelativeLayout barLand;
    private RelativeLayout bar;
    private String Tag = "CameraActivity";
    private String sourceToken;
    private RelativeLayout controlLayout;

    private RelativeLayout timeLayout;
    private boolean showSetting = true;
    private boolean isBoxChannel;
    private TextView noDataPlayback;
    private int shortAnimationDuration;

    private ZoomLayout zoomLayout;
    private String tokenVS = "";
    private OrientationEventListener sensorEvent;

    private Animation slideUp, slideDown, slideShowDown, slideShowUp;

    private enum SensorStateChangeActions {
        WATCH_FOR_LANDSCAPE_CHANGES, SWITCH_FROM_LANDSCAPE_TO_STANDARD, WATCH_FOR_POTRAIT_CHANGES, SWITCH_FROM_POTRAIT_TO_STANDARD;
    }

    private SensorStateChangeActions mSensorStateChanges;

    private void setGone(View view) {
        view.animate()
                .translationX(view.getWidth())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void setView(View view) {
        view.animate()
                .translationX(0)
                .alpha(1.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                        view.bringToFront();
                    }
                });
    }


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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) controlLayout.getLayoutParams();
        params.removeRule(RelativeLayout.ALIGN_PARENT_END);
        params.removeRule(RelativeLayout.BELOW);
        controlLayout.setLayoutParams(params);
        bar.setVisibility(View.GONE);
        this.barLand.setVisibility(View.VISIBLE);
    }

    private void setDataPor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null)
                controller.show(WindowInsets.Type.statusBars());
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        this.barLand.setVisibility(View.GONE);
        fullScreen.setVisibility(View.VISIBLE);
        fullScreen.bringToFront();
        viewLayout.getLayoutParams().height = (int) getResources().getDimension(R.dimen._200sdp);
        viewLayout.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) controlLayout.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.BELOW, R.id.viewLayout);
        controlLayout.setLayoutParams(params);
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
        setContentView(R.layout.playback_activity);
        init();
        action();
        setData(savedInstanceState);
    }
    private void setData(Bundle savedInstanceState){
        fullScreen.bringToFront();
        this.nameCamera.setText(cameraItem.getCameraName());
        this.nameLand.setText(cameraItem.getCameraName());
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

    }
    @SuppressLint("ClickableViewAccessibility")
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

        this.backLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             shrinkToPotraitMode();
            }
        });

            this.controlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.e(" xxxxxxx", getResources().getConfiguration().orientation + " " + showSetting);
                    if (getResources().getConfiguration().orientation == 2) {
                        if (showSetting) {
                            barLand.startAnimation(slideUp);
                            barLand.setVisibility(View.GONE);
//                            timeLayout.startAnimation(slideDown);
//                            timeLayout.setVisibility(View.GONE);
//                            timeRuleView.startAnimation(slideDown);
//                            timeRuleView.setVisibility(View.GONE);
                            showSetting = false;
                        } else {
                            barLand.startAnimation(slideShowDown);
                            barLand.setVisibility(View.VISIBLE);
//                            timeLayout.setVisibility(View.VISIBLE);
//                            timeRuleView.setVisibility(View.VISIBLE);
//                            timeRuleView.startAnimation(slideShowUp);
//                            timeLayout.startAnimation(slideUp);
                            showSetting = true;
                        }

                    }
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        playbackHandle = new MainHandler();
    }


    private int count = 1;
    ArrayList<JSONObject> playbacks = new ArrayList<>();
    private CommonClientOptions clientOptions;

    private void getDataPlaybackBox(JSONObject data) {
        Log.e("respone data playback", data.toString());
        try {
            JSONArray recordingFiles = data.getJSONArray("recordingFiles");
            for (int index = 0; index < recordingFiles.length(); index++) {
                JSONObject playbackItem = recordingFiles.getJSONObject(index);
                playbacks.add(playbackItem);
            }

            if (playbacks.size() > 0){
                this.loading.setVisibility(View.VISIBLE);
            }
            if (playbacks.size() % 100 == 0 && playbacks.size() > 0) {
                Log.e("length last", playbacks.size() + " " + playbacks.get(playbacks.size() -1).getLong("earliestRecording"));
                webRTCClient.sendMessage(DataChannelConfig.getFilesRecord(sourceToken, playbacks.get(playbacks.size() -1).getLong("earliestRecording")));
                return;
            }
            // sap xep tat ca cac file tra ve
            Log.e("length last 1", playbacks.size() + " " + playbacks.get(playbacks.size() -1).getLong("earliestRecording"));
            // lay xong tat ca file record
            if (playbacks.size() != 0) {
                this.paintSelectDataBox(playbacks);
            }
            else {
                noDataPlayback.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void paintSelectDataBox(ArrayList<JSONObject> recordingFiles) {
        try {
            timePartItems.clear();
            timeDatas.clear();
            for (int index = 0; index < recordingFiles.size(); index++) {
                int start = (int) TimeUnit.MILLISECONDS.toSeconds(recordingFiles.get(index).getLong("earliestRecording")); // converse to seconds
                int end = (int) TimeUnit.MILLISECONDS.toSeconds(recordingFiles.get(index).getLong("latestRecording"));
                String filename = recordingFiles.get(index).getString("token");
                TimePartItem timePartItem = new TimePartItem(start, end, filename);
                timePartItems.add(timePartItem);
            }
            timePartItems.sort(Comparator.comparing(TimePartItem::getStartTime));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        int valueMilestones = timePartItems.get(timePartItems.size() - 1).getEndTime();
        TimePartItem lastRecord = new TimePartItem(timePartItems.get(timePartItems.size() - 1).getStartTime(),
                timePartItems.get(timePartItems.size() - 1).getEndTime(), timePartItems.get(timePartItems.size() - 1).getTokenFile());
        timeDatasCalculated.add(lastRecord);
        dateHasData = new JSONArray();
        for (int index = timePartItems.size() - 1; index > 0; index--) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timePartItems.get(index).getStartTime() * 1000L);
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            long mDay = calendar.get(Calendar.DAY_OF_MONTH);
            try {
                if (dateHasData.length() == 0 || dateHasData.getJSONObject(dateHasData.length() - 1).getInt("day") != mDay) {
                    JSONObject time = new JSONObject();
                    time.put("year", mYear);
                    time.put("month", mMonth);
                    time.put("day", mDay);
                    dateHasData.put(time);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // check dữ liệu 3 ngày cuối vẽ lên thước
//            if (Math.abs(TimeRuleView.MIN_TIME_VALUE) > -(timePartItems.get(index).getStartTime() - valueMilestones)) {
            if (timePartItems.get(index).getStartTime() - timePartItems.get(index - 1).getEndTime() <= 1) {
                timeDatasCalculated.get(timeDatasCalculated.size() - 1).setStartTime(timePartItems.get(index - 1).getStartTime());
            } else {
//                    Log.e("sizzzzz 2", timeDatasCalculated.get(timeDatasCalculated.size() - 1).getStartTime() + " " + timePartItems.get(index).getEndTime() + " ");
                TimePartItem timePartItem = new TimePartItem(timePartItems.get(index - 1).getStartTime(), timePartItems.get(index - 1).getEndTime(), timePartItems.get(index - 1).getTokenFile());
                timeDatasCalculated.add(timePartItem);
            }
//            }
        }

        timeDatasCalculated.sort(Comparator.comparing(TimePartItem::getStartTime));
        Date date = new Date(timePartItems.get(timePartItems.size() - 1).getStartTime() * 1000L);
        Calendar cal = Calendar.getInstance(); // creates calendar
        Calendar cal2 = Calendar.getInstance(); // creates calendar
        cal.setTime(date);               // sets calendar time/date
        cal.getTime();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MIN);
        Date date2 = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        cal2.setTime(date2);
        maxValue = (int) TimeUnit.MILLISECONDS.toSeconds(date2.getTime());

        for (TimePartItem timePartItem : timeDatasCalculated) {
            timePartItem.setStartTime(timePartItem.getStartTime() - maxValue);
            timePartItem.setEndTime(timePartItem.getEndTime() - maxValue);
        }

        Log.e("sizzzzz 2", timeDatasCalculated.size() + "");
        startPlaybackCamera(timePartItems.get(timePartItems.size() - 1).getTokenFile());

        timeRuleView.setTimePartList(timeDatasCalculated);
        timeRuleView.setCurrentTime(timePartItems.get(timePartItems.size() - 1).getStartTime() - maxValue);
        filter.setVisibility(View.VISIBLE);
        this.timeRuleView.setOnTimeChangedListener(new TimeRuleView.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int newTimeValue) {
                timeNow.setText(DateTimeFormat.converseSecondToTime(timeRuleView.getCurrentTime() + maxValue, DateTimeFormat.TIME_FORMAT));
                if (timeRuleView.isStopScroll()) {
                    if (timeRuleView.isUp()) {
                        playTime(timeRuleView.getCurrentTime() + maxValue);
                    }
                }
            }
        });
    }

    private boolean playTime(int time) {
        for (TimePartItem timePartItem : timePartItems) {
            if (time >= timePartItem.getStartTime() && time < timePartItem.getEndTime()) {
                playFile(time - timePartItem.getStartTime(), timePartItem.getTokenFile());
                return true;
            }
        }

        noDataPlayback.setVisibility(View.VISIBLE);
        try {

            if (isBoxChannel){
                webRTCClient.sendMessage(DataChannelConfig.pauVideoPlaybacbk());
            } else {
                webRTCClient.sendMessage(DataChannelConfig.pauVideoPlaybacbk(cameraItem.getPeerID()));
            }
        }catch (Exception exception){
        exception.printStackTrace();
        }
        return false;
    }

    private void playFile(int timeSeek, String filePlay) {
        Log.e("file play", filePlay);
        Message message = new Message();
//        JSONObject object = new JSONObject();
//        try {
//            object.put("timeSeek", timeSeek);
//            object.put("filePlay", filePlay);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        noDataPlayback.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
//        JSONObject dataPlayback = null;
        try {
//            dataPlayback = new JSONObject((String) message.obj);
            if (!isBoxChannel) {
                webRTCClient.sendMessage(DataChannelConfig.selectRecordCamera(cameraItem.getPeerID(),filePlay, timeSeek));
            } else {
                webRTCClient.sendMessage(DataChannelConfig.selectRecotdBox(filePlay, timeSeek));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        message.what = ApplicationService.PLAY_VIDEO_PLAYBACK;
//        message.obj = object.toString();
//        ApplicationService.mainHandler.sendMessage(message);
    }



    private void getDataPlaybackCamera(JSONObject data) {
        try {
//            Log.e("xxxxx ", data.getJSONObject("get_video_playback_response").getJSONObject("data").getJSONArray("files").toString());
            JSONArray recordingFiles = data.getJSONObject("get_video_playback_response").getJSONObject("data").getJSONArray("files");
            for (int index = 0; index < recordingFiles.length(); index++) {
                JSONObject playbackItem = recordingFiles.getJSONObject(index);
                playbacks.add(playbackItem);
            }

            if (playbacks.size() > 0) {
                this.paintSelectData(playbacks);
            } else {
                noDataPlayback.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void paintSelectData(ArrayList<JSONObject> recordingFiles) {
        try {
            timePartItems.clear();
            timeDatas.clear();
            for (int index = 0; index < recordingFiles.size(); index++) {
                int start = recordingFiles.get(index).getInt("start");
                int end = recordingFiles.get(index).getInt("end");
                String filename = recordingFiles.get(index).getString("filename");
                TimePartItem timePartItem = new TimePartItem(start, end, filename);
                timePartItems.add(timePartItem);
            }
            timePartItems.sort(Comparator.comparing(TimePartItem::getStartTime));
//            int valueMilestones = timePartItems.get(timePartItems.size() - 1).getEndTime();
            for (int index = 0; index < recordingFiles.size(); index++) {

                // check dữ liệu 2 ngày cuối vẽ lên thước
//                if (Math.abs(TimeRuleView.MIN_TIME_VALUE) > -(timePartItems.get(index).getStartTime() - valueMilestones)) {
                if (index == 0) {
                    TimePartItem timePartItem = new TimePartItem(timePartItems.get(index).getStartTime(), timePartItems.get(index).getEndTime(), timePartItems.get(index).getTokenFile());
                    timeDatasCalculated.add(timePartItem);
                } else {
                    if (timePartItems.get(index).getStartTime() - timePartItems.get(index - 1).getEndTime() <= 1) {
                        timeDatasCalculated.get(timeDatasCalculated.size() - 1).setEndTime(timePartItems.get(index).getEndTime());
                    } else {
                        TimePartItem timePartItem = new TimePartItem(timePartItems.get(index).getStartTime(), timePartItems.get(index).getEndTime(), timePartItems.get(index).getTokenFile());
                        timeDatasCalculated.add(timePartItem);
                    }
                }
//                }
            }

            Date date = new Date(timeDatasCalculated.get(0).getStartTime() * 1000L);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
//            int mYear = calendar.get(Calendar.YEAR) - 1970;
//            int mMonth = calendar.get(Calendar.MONTH);Leo1201
//            long mDay = TimeUnit.MILLISECONDS.toDays(date.getTime());
            Calendar cal = Calendar.getInstance(); // creates calendar
            Calendar cal2 = Calendar.getInstance(); // creates calendar
            cal.setTime(date);               // sets calendar time/date
            cal.getTime();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            LocalDateTime endOfDay = localDateTime.with(LocalTime.MIN);
            Date date2 = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
            cal2.setTime(date2);
            maxValue = (int) TimeUnit.MILLISECONDS.toSeconds(date2.getTime());

            for (TimePartItem timePartItem : timeDatasCalculated) {
                timePartItem.setStartTime(timePartItem.getStartTime() - maxValue);
                timePartItem.setEndTime(timePartItem.getEndTime() - maxValue);
            }

            // vẽ lên thước
            timeRuleView.setTimePartList(timeDatasCalculated);
            // Start Play
            //
            timeRuleView.setCurrentTime(timeDatasCalculated.get(0).getStartTime());
            Message message = new Message();
            message.what = ApplicationService.START_PLAYBACK_CAMERA;
            message.obj = timeDatasCalculated.get(0).getTokenFile();
            ApplicationService.mainHandler.sendMessage(message);

//            Log.e("time 2", timeDatasCalculated.size() + " " + timeDatasCalculated.get(0).getStartTime() + " " + timeDatasCalculated.get(timeDatasCalculated.size() - 1).getEndTime());
            this.timeRuleView.setOnTimeChangedListener(new TimeRuleView.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(int newTimeValue) {
                    timeNow.setText(DateTimeFormat.converseSecondToTime(timeRuleView.getCurrentTime() + maxValue, DateTimeFormat.TIME_FORMAT));
//                    repainData(timeRuleView.getCurrentTime());
                    if (timeRuleView.isUp() && timeRuleView.isStopScroll()) {
                        playTime(timeRuleView.getCurrentTime() + maxValue);
                    }
                }
            });

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

        this.slideShowUp = AnimationUtils.loadAnimation(this,
                R.anim.slide_show_up);
        this.slideShowDown = AnimationUtils.loadAnimation(this,
                R.anim.slide_show_down);
        this.slideUp = AnimationUtils.loadAnimation(this,
                R.anim.slide_up);
        this.slideDown = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
       this.timeLayout = findViewById(R.id.timeLayout);
        this.shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        this.backLand = findViewById(R.id.backLand);
        this.nameLand = findViewById(R.id.nameCameraLand);
        this.barLand = findViewById(R.id.barLand);
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
//        this.touchLayout = findViewById(R.id.touchLayout);
        this.timeNow = findViewById(R.id.tvTime);
        this.filter = findViewById(R.id.filter);
        this.timeRuleView = findViewById(R.id.timeRuler);
        this.calendarView = findViewById(R.id.calendarView);
        this.controlLayout = findViewById(R.id.controlLayout);
        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FilterPlayback.class);
                intent.putExtra(FilterPlayback.DATE_HAVE_DATE, dateHasData.toString());
                intent.putExtra(FilterPlayback.CURRENT_DATE, timeRuleView.getCurrentTime() + maxValue);
                startActivityIntent.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        long selectedDate = data.getLongExtra(FilterPlayback.SELECT_DATE, 0);
                        Log.e("start time ", " " + selectedDate);
                            playAtMidnight((int) selectedDate);
                    }
                }
            });
    private void playAtMidnight(int timeSelected) {
        boolean isHaveData = false;
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.setTimeInMillis(timeSelected * 1000L);

        for (TimePartItem timePartItem : timePartItems) {
            if (timeSelected >= timePartItem.getStartTime() && timeSelected < timePartItem.getEndTime()) {
                playFile(timeSelected - timePartItem.getStartTime(), timePartItem.getTokenFile());
                isHaveData = true;
            }
        }

        Log.e("timeSelected", timeSelected + " " + isHaveData);
        Log.e("timeSelected", selectedDay.get(Calendar.DAY_OF_MONTH) + " " + selectedDay.get(Calendar.MONTH) + " " + selectedDay.get(Calendar.YEAR));

        if (!isHaveData){
            for (int index = 0; timePartItems.size() > index; index++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timePartItems.get(index).getStartTime() * 1000L);
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                long mDay = calendar.get(Calendar.DAY_OF_MONTH);
//                Log.e("timeSelected", mDay + " " + mMonth);
                if (mDay == selectedDay.get(Calendar.DAY_OF_MONTH) &&
                        mMonth == selectedDay.get(Calendar.MONTH) && mYear == selectedDay.get(Calendar.YEAR) && !isHaveData){
                    playFile(0, timePartItems.get(index).getTokenFile());
                    timeRuleView.setCurrentTime(timePartItems.get(index).getStartTime() - maxValue);
                    isHaveData = true;
                }
            }
        }

        if (!isHaveData){
            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.NO_DATA_PLAYBACK);
        }
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
//        this.getVideoPlayback();
        if (this.playbacks.size()  == 0){
            this.getVideoPlayback();
        }
        if (isTrackReady){
            updateImgPerSec(); // update time in video
            this.runOnUiThread(() -> {
                this.loading.setVisibility(View.GONE);
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
//                this.touchLayout.setVisibility(View.GONE);
            });
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

    private void updateImgPerSec() {
        this.timeRuleView.setCurrentTime(timeRuleView.getCurrentTime() + 1);
        if (timeRuleView.isUp() && timeRuleView.getCurrentTime() + maxValue + 1 > 24 * 3600) {
            timeNow.setText(DateTimeFormat.converseSecondToTime(timeRuleView.getCurrentTime() + maxValue + 1, DateTimeFormat.TIME_FORMAT));
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

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("xxxxx", jsonData.toString());
                processDataResponse(isBoxChannel, jsonData);
            }
        });
    }

    private void processDataResponse(boolean isBox, JSONObject jsonData) {
        JSONObject header = null;
        JSONObject body = null;
        try {
            header = jsonData.getJSONObject("envelope").getJSONObject("header");
            body = jsonData.getJSONObject("envelope").getJSONObject("body");

            if (!isBox) {
                switch (header.getString("component")) {

                }
            } else {
                switch (header.getString("component")) {
                    case Playback.PLAYBACK:
                        getDataPlaybackBox(jsonData.getJSONObject("envelope").getJSONObject("body").getJSONObject("data").getJSONObject("data"));
                        break;
                    case "ps_service":
                        if (body.getString("func").equals("post_control_record_response") && body.isNull("data")){
                            Log.e("response ", "nodata");
                            this.noDataPlayback.setVisibility(View.VISIBLE);
                        }
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

    private void getVideoPlayback(){
        try {
            if (isBoxChannel) {
                webRTCClient.sendMessage(DataChannelConfig.getFilesRecord(sourceToken));
            } else {
                webRTCClient.sendMessage(DataChannelConfig.getFilesRecordCamera(cameraItem.getPeerID()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

//            Log.e("response message 12",message.what + "");

            if (ApplicationService.CONNECTED_CAMERA == message.what) {
                thumImage.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
//                touchLayout.setVisibility(View.GONE);
            }
            try {
                if (!webRTCClient.isReadySendData()) {
                    // TODO need update
                    return;
                }
                switch (message.what) {
//                    case ApplicationService.PLAY_VIDEO_PLAYBACK:
//                        noDataPlayback.setVisibility(View.GONE);
//                        loading.setVisibility(View.VISIBLE);
//                        JSONObject dataPlayback = new JSONObject((String) message.obj);
//                        if (!isBoxChannel) {
//                            webRTCClient.sendMessage(DataChannelConfig.selectRecordCamera(cameraItem.getPeerID(),dataPlayback.getString("filePlay"), dataPlayback.getInt("timeSeek")));
//                        } else {
//                            webRTCClient.sendMessage(DataChannelConfig.selectRecotdBox(dataPlayback.getString("filePlay"), dataPlayback.getInt("timeSeek")));
//                        }
//                        break;
                    case ApplicationService.GET_CAM_DETAIL_SUCCESS:
                        JSONObject object = new JSONObject(message.obj.toString());
                        objectID = object.getString("objectGuid");
                        camName = object.getString("deviceName");
                        model = object.getString("serialNumber");
                        regionId = String.valueOf(object.getInt("regionId"));
                        break;
                    case ACTION_UP:
                        playTime(timeRuleView.getCurrentTime() + maxValue);
                        break;
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