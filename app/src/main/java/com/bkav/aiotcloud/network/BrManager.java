package com.bkav.aiotcloud.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.bai.bridge.client.SignalingClient;
import com.bkav.bai.bridge.client.SignalingClientState;
import com.bkav.bai.bridge.common.SignalingContext;
import com.bkav.bai.bridge.config.ClientConfig;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.bkav.bai.bridge.transportserver.ConnectData;
import com.bkav.bai.bridge.webrtc.RTCSignalingChannel;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrManager {

    public static class Define {
        public final static String STREAM_MODE_LIVE = "LIVE";
        public final static String STREAM_SRC_MAIN = "MAIN";
        public final static String STREAM_SRC_SUB = "SUB";
    }

    public static class BrConfig {

        public BrConfig(int configResourceId, @NonNull String signalingConnectDataSaveKey, boolean isTrickle) {
            this.configResourceId = configResourceId;
            this.signalingConnectDataSaveKey = signalingConnectDataSaveKey;
            this.isTrickle = isTrickle;
        }

        public String signalingConnectDataSaveKey() {
            return this.signalingConnectDataSaveKey;
        }

        public boolean isTrickle() {
            return this.isTrickle;
        }

        public void setTrickle(boolean enabled) {
            this.isTrickle = enabled;
        }

        private final int configResourceId;
        private final String signalingConnectDataSaveKey;
        private boolean isTrickle;
    }

    public static SignalingClient signalingClient;
    public static BrConfig currentConfig;

    public static final BrConfig QCConfig;
    public static final BrConfig PRConfig;
    public static final BrConfig DevConfig;

    static {
        QCConfig = new BrConfig(
                R.raw.signaling_config_qc,
                "qc.saveKey.signaling.connect.data",
                true
        );

        DevConfig = new BrConfig(
                R.raw.signaling_config_dev,
                "dev.saveKey.signaling.connect.data",
                true
        );
        PRConfig = new BrConfig(
                R.raw.signaling_config_produce,
                "pr.saveKey.signaling.connect.data",
                true
        );
        currentConfig = PRConfig;
        // TODO load trickle from saved
    }

    public static void runTask(Runnable task) {
        if (executorService == null) {
            return;
        }
        executorService.submit(task);
    }

    public static void start() {
        Log.v(TAG, "start");
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
    }

    public static void stop() {
        Log.v(TAG, "stop");
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (signalingClient != null && signalingClient.isConnected()) {
            try {
                signalingClient.disconnect();
            } catch (Exception ex) {
                Log.e(TAG, "stop:" + ex.getMessage());
            }
        }
        signalingClient = null;
    }

    public static void updateTrickleMode(boolean isTrickle) {
        if (isTrickle == currentConfig.isTrickle()) {
            return;
        }
        currentConfig.setTrickle(isTrickle);
        if (currentConfig.isTrickle()) {
            // TODO check signalingClient exist and connect
            BrManager.runTask(() -> {
                if (signalingClient == null) {
                    BrManager.loadSignalingClient();
                    if (signalingClient == null) {
                        return; // throw error
                    }
                }
                if (!signalingClient.isConnected()) {
                    try {
                        signalingClient.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static SignalingClient loadSignalingClient() {
        if (BrManager.signalingClient != null) {
            return BrManager.signalingClient;
        }
        try {
            Context appContext = getApplicationContext();
            Reader reader = new InputStreamReader(appContext.getResources().openRawResource(currentConfig.configResourceId));
            ClientConfig clientConfig = ClientConfig.loadFromReader(reader);
            SignalingContext context = SignalingContext.init(clientConfig);
            ConnectData connectData = null;
            String connectDataRaw = getSaveData(currentConfig.signalingConnectDataSaveKey);
            if (connectDataRaw != null) {
                connectData = ConnectData.fromRawData(connectDataRaw);
                // TODO check targetAuth expireTime
            }
            BrManager.signalingClient = context.createClient(connectData);
            BrManager.signalingClient.setCreateChannelHandler(RTCSignalingChannel::createRTC);
            BrManager.signalingClient.addChangeStateHandler((client, state) -> {
                if (state == SignalingClientState.Connected) {
                    if (BrManager.getSaveData(BrManager.currentConfig.signalingConnectDataSaveKey()) == null) {
                        ConnectData serverConnectData = client.serverChannel().connectData();
                        if (serverConnectData.targetAuth() != null) {
                            serverConnectData.targetAuth().updateTimeNow();
                        }
                        BrManager.saveData(BrManager.currentConfig.signalingConnectDataSaveKey(), serverConnectData.connectDataRaw());
                    }
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return BrManager.signalingClient;
    }

    public static Context getApplicationContext() {
        return ApplicationService.getAppContext();
    }

    public static void saveData(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSaveData(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static CommonClientOptions createDefaultClientOptions() {
        CommonClientOptions clientOptions = WebRTCClient.getDefaultClientOptions()
                .context(getApplicationContext())
                .isTrickle(BrManager.currentConfig.isTrickle())
                .streamMode(BrManager.Define.STREAM_MODE_LIVE)
                .streamSRC(Define.STREAM_SRC_MAIN)
                .isAudioSupport(false)
                .isVideoSupport(true)
                .isStartTrackMonitoring(true);
        Log.e("createDefaultClientOptions:", clientOptions.isTrickle() + "");
        return clientOptions;
    }

    private static final String TAG = BrManager.class.getSimpleName();
    private static ExecutorService executorService;

    private static SharedPreferences getSharedPreferences() {
        Context context = getApplicationContext();
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
}
