package com.bkav.aiotcloud.screen.viewlan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.util.ArrayList;


public class CameraDetailLan extends AppCompatActivity implements IVLCVout.Callback {
    @SuppressLint("AuthLeak")
    private String SAMPLE_URL=null;
    private static final String TAG = "PlayerFragment";
    SurfaceView cameraView;
    ProgressBar progressBar;
    private MediaPlayer mMediaPlayer = null;
    private String linklan;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private String RTSP_URL;
    private String PTZ_Support;
    private LibVLC mLibVLC;
    private String account;
    private String pass;
    private String linkPTZ;
    private String PROFILE_TOKEN_ZOOM = "PROFILE_468971848";
    private String PROFILE_TOKEN_PTZ = "PROFILE_000";
    private String valuePositive = "1";
    private String valueNegetive = "-1";
    private String valueZero = "0";
    private String keyZoom = "Zoom";
    private String keyTitl = "Tilt";
    private String keyPan = "Pan";

    RelativeLayout backIcon;
    TextView title;
    RelativeLayout ptz1;
    RelativeLayout ptz2;
    RelativeLayout ptz3;
    RelativeLayout ptz4;
    RelativeLayout ptz5;
    RelativeLayout ptz6;
    RelativeLayout ptz7;
    RelativeLayout ptz8;

    ImageView zoomIn;
    ImageView zoomOut;
    LinearLayout ptzLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.camera_detail_lan);
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void action() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        zoomIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyZoom, valuePositive);
                        requestAction(PROFILE_TOKEN_ZOOM, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyZoom, valueZero);
                        requestAction(PROFILE_TOKEN_ZOOM, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        zoomOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyZoom, valueNegetive);
                        requestAction(PROFILE_TOKEN_ZOOM, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyZoom, valueZero);
                        requestAction(PROFILE_TOKEN_ZOOM, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyTitl, valuePositive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyTitl, valueNegetive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valueNegetive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valuePositive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valueNegetive);
                        velocity.put(keyTitl, valuePositive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valuePositive);
                        velocity.put(keyTitl, valuePositive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valueNegetive);
                        velocity.put(keyTitl, valueNegetive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
        ptz8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                JSONObject velocity = new JSONObject();
                try {
                    if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        velocity.put(keyPan, valuePositive);
                        velocity.put(keyTitl, valueNegetive);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                    else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                        velocity.put(keyPan, valueZero);
                        velocity.put(keyTitl, valueZero);
                        requestAction(PROFILE_TOKEN_PTZ, velocity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }


    private void init(){
        Intent intent = getIntent();
        this.linklan = intent.getStringExtra("linkLan");
        this.manufacturer = intent.getStringExtra("Manufacturer");
        this.model = intent.getStringExtra("Model");
        this.firmwareVersion = intent.getStringExtra("FirmwareVersion");
        this.RTSP_URL = intent.getStringExtra("RTSP_URL");
        this.PTZ_Support = intent.getStringExtra("PTZ_Support");
        this.account = intent.getStringExtra("username");
        this.pass = intent.getStringExtra("password");

        this.cameraView = findViewById(R.id.camera_view);
        this.progressBar = findViewById(R.id.progressBar);
        this.cameraView = findViewById(R.id.camera_view);
        this.backIcon = findViewById(R.id.backIcon);
        this.title = findViewById(R.id.nameCamera);
        this.ptz1 = findViewById(R.id.ptz1);
        this.ptz2 = findViewById(R.id.ptz2);
        this.ptz3 = findViewById(R.id.ptz3);
        this.ptz4 = findViewById(R.id.ptz4);
        this.ptz5 = findViewById(R.id.ptz5);
        this.ptz6 = findViewById(R.id.ptz6);
        this.ptz7 = findViewById(R.id.ptz7);
        this.ptz8 = findViewById(R.id.ptz8);
        this.zoomIn = findViewById(R.id.zoomIn);
        this.zoomOut = findViewById(R.id.zoomOut);
        this.ptzLayout = findViewById(R.id.ptzLayout);
    }
    private void setData(){
        this.title.setText(model);
        if(PTZ_Support.equals("False")){
            ptzLayout.setVisibility(View.GONE);
        }
        processLinkRTSP(account, pass, RTSP_URL);
        setVideoData();
        linkPTZ = linklan + "/api/PTZConfiguration";

    }
    private void processLinkRTSP(String account, String pass, String rtsp_link){
        String[] arrayRTSP = rtsp_link.split("//");
        String rtspLinkConvert = arrayRTSP[0] + "//" + account + ":" + pass + "@" + arrayRTSP[1];
        SAMPLE_URL = rtspLinkConvert;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }
    @Override
    public void onStart() {
        super.onStart();
        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        vlcVout.setVideoView(cameraView);
        vlcVout.attachViews();
        mMediaPlayer.getVLCVout().addCallback(this);
        Media media = new Media(mLibVLC, Uri.parse(SAMPLE_URL));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.play();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.getVLCVout().detachViews();
        mMediaPlayer.getVLCVout().removeCallback(this);
    }

    private void setVideoData(){
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        mLibVLC = new LibVLC(getApplicationContext(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                switch (event.type){
                    case MediaPlayer.Event.Buffering:
                        Log.d(TAG, "onEvent: Buffering");
                        if(event.getBuffering() == 100){
                            progressBar.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
    }
    private void requestAction(String profileToken, JSONObject velocity){
        try {
            JSONObject payload = new JSONObject();
            JSONObject envelope = new JSONObject();
            JSONObject conti = new JSONObject();
            JSONObject body = new JSONObject();
            JSONObject security = new JSONObject();
            conti.put("ProfileToken",profileToken);
            conti.put("Velocity", velocity);
            body.put("ContinuousMove", conti);
            security.put("Security", ApplicationService.securityObject);
            envelope.put("Header", security);
            envelope.put("Body", body);
            payload.put("Envelope", envelope);

            ApplicationService.requestManager.configCamLan(linkPTZ, payload);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {

    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {

    }
    public class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.CONFIG_CAMERA_SUCCESS:
                    break;
                case ApplicationService.CONFIG_CAMERA_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("config_cam_error"), false);
                    break;
            }
        }


    }

}