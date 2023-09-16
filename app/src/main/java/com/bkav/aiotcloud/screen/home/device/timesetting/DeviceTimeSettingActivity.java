package com.bkav.aiotcloud.screen.home.device.timesetting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.config.DeviceConfig;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.camera.playback.FilterPlayback;
import com.bkav.aiotcloud.screen.home.device.DeviceActivity;
import com.bkav.aiotcloud.screen.notify.SetDateTimeDialog;
import com.bkav.bai.bridge.rtc.ClientHandler;
import com.bkav.bai.bridge.rtc.CommonClientOptions;
import com.bkav.bai.bridge.rtc.TrackStatReport;
import com.bkav.bai.bridge.rtc.WebRTCClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceTimeSettingActivity extends AppCompatActivity implements ClientHandler {
    public static final String DATE_TIME = "date_time";

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
        int id = intent.getIntExtra(DeviceActivity.ID, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.time_setting_device);
        for (DeviceItem deviceItem : ApplicationService.deviceItems) {
            if (deviceItem.getCameraId() == id) {
                this.deviceItem = deviceItem;
            }
        }

        this.init();
        this.action();
        this.initWebRTCClient();
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
//        ApplicationService.mainHandler = new MainHandler();

    }

    private DeviceItem deviceItem;
    private CommonClientOptions clientOptions;
    private WebRTCClient webRTCClient;

    private boolean isBoxChannel;
    private String tokenVS;
    private String sourceToken;

    private String currentMode = "Auto";
    private String currentTimeZone = "";
    private String currentDateTime = "";
    private String currentServe = "";
    private TextView title;

    private Button save;
    private String objectGuid = "";

    private TextView autoSynchronized;
    private SwitchCompat swOnOff;

    private TextView dateTx;
    private TextView dateInput;
    private RelativeLayout dateLayout;
    private RelativeLayout setDateHour;
    private TextView timezoneTx;

    private TextView NTPTx;
    private TextView NTPInput;
    private int positionTypeSelected = 0;
    private Spinner spinnerTimeZone;
    private ArrayAdapter<String> adapterTimeZone;
    private List<String> listTimezone = new ArrayList<>();

    private RelativeLayout backIm;

    private int currentDate;
    private int currentMonth;
    private int currentYear;

    private JSONObject editItem;
    private int hour;

    private int minute;

    private Calendar calendar;

    private String getDateAndHour(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(DateTimeFormat.TIME_FORMAT);
        return  isoFormat.format(getDate());
    }

    private Date getDate() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.DAY_OF_MONTH, currentDate);
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.YEAR, currentYear);
        return calendar.getTime();
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
                            updateUI(body.getJSONObject("data").getJSONObject("data"));
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

        this.save = findViewById(R.id.save);

        this.autoSynchronized = findViewById(R.id.autoSynchronized);
        this.swOnOff = findViewById(R.id.swOnOff);
        this.dateTx = findViewById(R.id.dateTx);
        this.dateInput = findViewById(R.id.dateInput);
        this.dateLayout = findViewById(R.id.dateLayout);
        this.timezoneTx = findViewById(R.id.timezoneTx);
        this.NTPTx = findViewById(R.id.NTPTx);
        this.NTPInput = findViewById(R.id.NTPInput);
        this.spinnerTimeZone = findViewById(R.id.spinnerType);
        this.title.setText(LanguageManager.getInstance().getValue("time_setting"));
        this.dateTx.setText(LanguageManager.getInstance().getValue("date_and_time"));
        this.autoSynchronized.setText(LanguageManager.getInstance().getValue("automatic_sync"));
        this.timezoneTx.setText(LanguageManager.getInstance().getValue("time_zone"));


        adapterTimeZone = new ArrayAdapter<String>(DeviceTimeSettingActivity.this, android.R.layout.simple_spinner_dropdown_item, listTimezone) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == positionTypeSelected) {
                    text.setTextColor(getColor(R.color.mainColor));
                } else {
                    text.setTextColor(Color.WHITE);
                }
                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        adapterTimeZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeZone.setAdapter(adapterTimeZone);
        spinnerTimeZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                positionTypeSelected = index;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        this.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        setDateHour = findViewById(R.id.setDateHour);

        setDateHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetTimeDeviceDialog.class);
                intent.putExtra(DATE_TIME, currentDateTime);
//                intent.putExtra(FilterPlayback.CURRENT_DATE, timeRuleView.getCurrentTime() + maxValue);
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
//                finish();

                    try {
                        if (swOnOff.isActivated()) {
                            editItem.put("mode", "Auto");
                        } else {
                            editItem.put("mode", "Manual");
                        }
                        editItem.put("timezone", listTimezone.get(positionTypeSelected));
                        editItem.put("ntpServerAddress", NTPInput.getText());

//                        Log.e("settime", DeviceConfig.setTimeZoneBox(editItem.toString()));
                        webRTCClient.sendMessage(DeviceConfig.setTimeZoneBox(editItem.toString()));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
        });

        this.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                setAutoMode(ischecked);
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
                        hour = data.getIntExtra(SetTimeDeviceDialog.HOUR, 0);
                        minute = data.getIntExtra(SetTimeDeviceDialog.MINUTE, 0);
                        currentDate = data.getIntExtra(SetTimeDeviceDialog.DAY, 0);
                        currentMonth = data.getIntExtra(SetTimeDeviceDialog.MONTH, 0);
                        currentYear = data.getIntExtra(SetTimeDeviceDialog.YEAR, 0);
                        dateInput.setText(getDateAndHour());
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        TimeZone zone = TimeZone.getTimeZone(ZoneId.systemDefault());
                        format.setTimeZone(zone);

//                        editItem.putExtra("datetime", format.format(getDate()));
//                        Log.e("Timeeee", " "+ format.format(getDate()));
                        try {
                            editItem.put("datetime", format.format(getDate()));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

    private void updateUI(JSONObject data) {
        try {
            editItem = data;
            this.currentMode = data.getString("mode");
            if (this.currentMode.equals("Auto")) {
                this.swOnOff.setChecked(true);
            } else if (this.currentMode.equals("Manual")) {
                this.swOnOff.setChecked(false);
            }
            this.currentDateTime = DateTimeFormat.getTimeFormat(data.getString("datetime"), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT);
            this.dateInput.setText(this.currentDateTime);
            getListTimeZone(data.getJSONArray("timezone_list"));
            this.currentTimeZone = data.getString("timezone");
            this.currentServe = data.getString("ntpServerAddress");
            this.NTPInput.setHint(this.currentServe);
            spinnerTimeZone.setSelection(listTimezone.indexOf(currentTimeZone));

//           Log.e("timesss", DateTimeFormat.getTimeFormat(data.getString("datetime"), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAutoMode(boolean isAuto) {
        if (isAuto) {
            this.setDateHour.setClickable(false);
            this.spinnerTimeZone.setEnabled(false);
            this.NTPInput.setEnabled(false);
        } else {
            this.setDateHour.setClickable(true);
            this.spinnerTimeZone.setEnabled(true);
            this.NTPInput.setEnabled(true);
        }
    }

    private void getListTimeZone(JSONArray timezone) {
        if (timezone != null) {
            for (int i = 0; i < timezone.length(); i++) {
                try {
                    listTimezone.add(timezone.getString(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            adapterTimeZone.notifyDataSetChanged();
        }
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            switch (message.what) {
                case ApplicationService.GET_CAM_DETAIL_SUCCESS:
                    JSONObject object = null;
//                    try {
//                        object = new JSONObject(message.obj.toString());
//                        nameDeviceInput.setText(object.getString("deviceName"));
//                        branchInput.setText(object.getString("regionName"));
//                        objectGuid = object.getString("objectGuid");
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
                    break;
//                case ApplicationService.UPDATE_CAM_SUCCESS:
//                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
//                    finish();
//                    break;
//                case ApplicationService.UPDATE_CAM_FAIL:
//                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_un_success"), true);
//                    finish();
//                    break;


            }
        }
    }
}
