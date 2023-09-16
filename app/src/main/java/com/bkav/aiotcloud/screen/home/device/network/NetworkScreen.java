package com.bkav.aiotcloud.screen.home.device.network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.config.DeviceConfig;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;

public class NetworkScreen extends AppCompatActivity  implements ClientHandler {
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
                this.webRTCClient.sendMessage(DeviceConfig.getTimeZoneBox());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void onReceiveData(WebRTCClient client, String data) {
        try {
            Log.e("data responsex ", data);
            this.processResponseDataChannel(new JSONObject(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//        int id = intent.getIntExtra("ID", 0);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.device_network_setting);
    }

    private CommonClientOptions clientOptions;
    private WebRTCClient webRTCClient;
    private DeviceItem deviceItem;
    private String tokenVS;
    private String sourceToken;


    private boolean isBoxChannel;

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

    private void processResponseDataChannel(JSONObject jsonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject header = null;
                JSONObject body = null;
                try {
                    header = jsonData.getJSONObject("envelope").getJSONObject("header");
                    body = jsonData.getJSONObject("envelope").getJSONObject("body");
                    switch (body.getString("func")) {
                        case "get_time_response":
//                            updateUI(body.getJSONObject("data").getJSONObject("data"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

}
