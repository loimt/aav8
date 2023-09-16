package com.bkav.aiotcloud.screen.setting.face;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.entity.aiobject.CameraConfig;
import com.bkav.aiotcloud.entity.aiobject.ZoneAIObject;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.AndroidCameraCapturer;
import com.bkav.aiotcloud.screen.home.camera.CustomPeerConnectionObserver;
import com.bkav.aiotcloud.screen.home.camera.CustomSdpObserver;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.view.PaintView;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class StepTwoFragment extends Fragment implements ListZoneAdapter.OnMenuItemClickListener, ClientHandler {
    public static final String EDIT = "edit";
    public static final String NEW = "new";


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
//                client.sendMessage(DataChannelConfig.getImageInfo(tokenVS));
//                if (isBoxChannel){
//                    webRTCClient.sendMessage(DataChannelConfig.getOption(tokenVS));
//                }
            } catch (Exception e) {
                this.showNotification(e.getMessage());
            }
        }
    }

    @Override
    public void onReceiveData(WebRTCClient client, String data) {
//        try {
//            this.processResponseDataChannel(new JSONObject(data));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.step_two_fragment, container, false);
        init(frag);
        action();
        return frag;
    }

    @Override
    public void onDestroy() {
        BrManager.runTask(() -> {
            if (this.webRTCClient != null) {
                this.webRTCClient.stop();
            }
            this.webRTCClient = null;
        });
        super.onDestroy();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String type = requireArguments().getString(ListCustomerActivity.TYPE);
        if (type.equals(EDIT)){
            setIDCamera(requireArguments().getInt("cameraID"));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (width * 1080 / 1920));
        cameraLayout.setLayoutParams(layoutParams);
    }

    public int checkStatement() {
        if (points.size() != 0 && isTouch) {
            return AddOjectFace.FORGET_SAVE;
        }
        return AddOjectFace.SUCCESS;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void drawZones(AIObject aiObject) {
        float scale = (float) 1920 / width;
        for (ZoneAIObject zoneAIObject : aiObject.getZones()) {
            if (!zoneAIObject.getZoneName().equals("Full screen")) {
                for (Point point : zoneAIObject.getCoordinate()) {
                    point.x = (int) (point.x / scale);
                    point.y = (int) (point.y / scale);
                }
            }
        }
        if (aiObject.getZones().size() == 1 && aiObject.getZones().get(0).getZoneName().equals("Full screen")) {
            return;
        }
        zoneAIObjects.clear();
        zoneAIObjects.addAll(aiObject.getZones());
        listZoneAdapter.notifyDataSetChanged();
        paintView.setNewZone();
        paintView.drawZones(aiObject.getZones());
    }

    public void connectCamera() {
        thumImage.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    public void setIDCamera(int id){
        for (CameraItem cameraItem : ApplicationService.cameraitems){
            if (cameraItem.getCameraId() == id){
                this.cameraItem = cameraItem;
            }
        }
        this.initWebRTCClient(this.cameraView);
        BrManager.runTask(() -> {
            try {
                this.webRTCClient.start();
            } catch (Exception e) {
                e.printStackTrace();
                this.showNotification(e.getMessage());
            }
        });
    }

    public JSONArray getPayload() {
        JSONArray zoneArray = new JSONArray();
        if (zoneAIObjects.size() != 0) {
            for (ZoneAIObject zoneAIObject : zoneAIObjects) {
                conversePoint(zoneAIObject.getCoordinate());
                zoneArray.put(zoneAIObject.getNewPayload());
            }
        }
        return zoneArray;
    }

    private List<Point> points;
    private CameraItem cameraItem;
    private ArrayList<ZoneAIObject> zoneAIObjects;
    private PaintView paintView;
    private SurfaceViewRenderer cameraView;
    private RelativeLayout cameraLayout;
    private RelativeLayout repaint;
    private RelativeLayout save;
    private RecyclerView listZone;
    private ListZoneAdapter listZoneAdapter;
    private ProgressBar loading;
    private PeerConnectionFactory peerConnectionFactory;
    private boolean isTouch = false;
    private int width;
    private String type;
    private boolean isBoxChannel;
    private String tokenVS = "";
    private TextView listZoneName;
    private WebRTCClient webRTCClient;
    private ImageView thumImage;
    private boolean trackReady = false;

    private void onTrackReady(boolean isTrackReady) {
        if (isTrackReady == this.trackReady) {
            return;
        }
        this.trackReady = isTrackReady;
        if (this.trackReady) {
            getActivity().runOnUiThread(() -> {
                this.thumImage.setVisibility(View.GONE);
                this.loading.setVisibility(View.GONE);
            });
//            BrManager.runTask(() -> this.webRTCClient.stopMonitoringTrack());
        }
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
            e.printStackTrace();
        }

        this.webRTCClient = new WebRTCClient(BrManager.signalingClient, cameraItem.getPeerID(), clientOptions);
        this.webRTCClient.setClientHandler(this);
        this.webRTCClient.setRemoteVideoView(cameraView);
    }

    private void showNotification(String content) {
        getActivity().runOnUiThread(() -> {
            if (content == null || content.isEmpty()) {
                return;
            }
            Toast toast = Toast.makeText(getActivity(), content, Toast.LENGTH_LONG);
            toast.show();
        });
    }


    public void setCameraUnConfigItem(String urlThumn, String peer) {
        Picasso.get().load(urlThumn).fit().centerCrop()
                .into(thumImage);
    }

    private void init(View view) {
        this.paintView = view.findViewById(R.id.touchLayout);
        this.cameraLayout = view.findViewById(R.id.cameraLayout);
        this.repaint = view.findViewById(R.id.layoutRepaint);
        this.save = view.findViewById(R.id.layoutSave);
        this.listZone = view.findViewById(R.id.listZone);
        this.listZone.setLayoutManager(new LinearLayoutManager(getContext()));
        this.cameraView = view.findViewById(R.id.camera_view);
        this.loading = view.findViewById(R.id.progressBar);
        this.thumImage = view.findViewById(R.id.thumImage);
        this.listZoneName = view.findViewById(R.id.listZoneName);
        this.listZoneName.setText(LanguageManager.getInstance().getValue("zone_list"));
        this.points = new ArrayList<>();
        this.zoneAIObjects = new ArrayList<>();

        this.listZoneAdapter = new ListZoneAdapter(getContext(), zoneAIObjects, this);
        this.listZone.setAdapter(listZoneAdapter);

        this.paintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        Point point = new Point(x, y);
                        points.add(point);

                        paintView.addPoint(x, y, zoneAIObjects);
                        paintView.invalidate();
                        isTouch = true;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                }
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onMenuItemClick(ZoneAIObject zoneAIObject, int position, int event) {
        if (event == ListZoneAdapter.EDIT_ZONE_EVENT) {
            Intent chooserIntent = new Intent(getActivity(), SetNameZone.class);
            chooserIntent.putExtra(SetNameZone.NAME, zoneAIObject.getZoneName());
            chooserIntent.putExtra(SetNameZone.POSITION, position);
            chooserIntent.putExtra(SetNameZone.TYPE, SetNameZone.EDIT);
            chooserIntent.setFlags(0);
            startActivityIntent.launch(chooserIntent);
        } else if (event == ListZoneAdapter.DELETE_ZONE_EVENT) {
            this.zoneAIObjects.remove(position);
            this.paintView.clearZone(zoneAIObjects);
        }
        listZoneAdapter.notifyDataSetChanged();
        listZoneAdapter.closeAllItems();
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getExtras().getString(SetNameZone.NAME);
                        if (data.getExtras().getString(SetNameZone.TYPE).equals(SetNameZone.NEW)) {
                            List<Point> listPoint = new ArrayList<>();
                            listPoint.addAll(points);
                            ZoneAIObject aiObject = new ZoneAIObject(listPoint, name);
                            zoneAIObjects.add(aiObject);
                            paintView.setNewZone();
                            points.clear();
                            isTouch = false;
                        } else if (data.getExtras().getString(SetNameZone.TYPE).equals(SetNameZone.EDIT)) {
                            zoneAIObjects.get(data.getExtras().getInt(SetNameZone.POSITION)).setZoneName(name);
                        }
                        listZoneAdapter.notifyDataSetChanged();
                    }
                    listZoneAdapter.closeAllItems();
                }
            });


    private List<Point> conversePoint(List<Point> points) {
        for (Point point : points) {
            float scale = (float) width / 1920;
            point.x = (int) (point.x / scale);
            point.y = (int) (point.y / (scale));
        }
        return points;
    }


    private void action() {
        this.save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (points.size() < 3) {
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("toast_short_point"), true );
                    return;
                }
                Intent chooserIntent = new Intent(getActivity(), SetNameZone.class);
                chooserIntent.putExtra(SetNameZone.TYPE, SetNameZone.NEW);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        this.repaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.clearZone(zoneAIObjects);
                points.clear();
                isTouch = false;
                paintView.invalidate();
            }
        });
    }
}
