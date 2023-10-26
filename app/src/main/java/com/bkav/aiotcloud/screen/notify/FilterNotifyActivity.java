package com.bkav.aiotcloud.screen.notify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FilterNotifyActivity extends AppCompatActivity {
    private static final String TAG = "FilterNotifyActivity";
    private TextView title;
    private RelativeLayout reloadLayout;
    private TextView periodTitle;
    private RelativeLayout startTimeLayout;
    private RelativeLayout endTimeLayout;
    private TextView startTimeTx;
    private TextView endTimeTx;
    private TextView listAITitle;
    private TextView accessTitle;
    private TextView customerTitle;
    private TextView licenseTx;
    private TextView statusDeviceTitle;
    private RadioButton accessCheck;
    private RadioButton customerCheck;
    private RadioButton licenseCheck;
    private RadioButton statusDeviceCheck;
    private TextView listDeciveTitle;
    private RecyclerView listDevice;
    private DeviceFilterAdapter deviceFilterAdapter;
    private String currentModel;
    private ImageView rotateImage;
    private Button confirm;
    private final String accessDetect = "3";
    private final String faceRecog = "2";
    private final String licenseDetect = "6";
    private final String deviceActivity = "7";
    private final String reload = "-1";
    private ProgressBar progressBar;

    private void init() {
        this.progressBar = findViewById(R.id.progressBar);
        this.title = findViewById(R.id.title);
        this.reloadLayout = findViewById(R.id.reload);
        this.periodTitle = findViewById(R.id.timeTitle);
        this.startTimeTx = findViewById(R.id.timeStart);
        this.endTimeTx = findViewById(R.id.timeEnd);
        this.listAITitle = findViewById(R.id.listAITitle);
        this.accessTitle = findViewById(R.id.accessTx);
        this.customerTitle = findViewById(R.id.customerRecogTx);
        this.licenseTx = findViewById(R.id.licenseTx);
        this.statusDeviceTitle = findViewById(R.id.statusDeviceTx);
        this.accessCheck = findViewById(R.id.accessCheck);
        this.customerCheck = findViewById(R.id.customerCheck);
        this.licenseCheck = findViewById(R.id.licenseCheck);
        this.statusDeviceCheck = findViewById(R.id.statusDeviceCheck);
        this.listDeciveTitle = findViewById(R.id.listDeviceTx);
        this.listDevice = findViewById(R.id.listDevice);
        this.startTimeLayout = findViewById(R.id.timeStartLayout);
        this.endTimeLayout = findViewById(R.id.timeEndLayout);
        this.rotateImage = findViewById(R.id.rotateImage);
        this.confirm = findViewById(R.id.confirmFilter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.filter_notify_fragment);
        setLayout();
        init();
        setData();
        action();
        ApplicationService.mainHandler = new MainHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setLayout() {
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wlp.gravity = Gravity.BOTTOM;
        setFinishOnTouchOutside(false);
    }

    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("filter"));
        this.periodTitle.setText(LanguageManager.getInstance().getValue("period_time"));
        this.listDeciveTitle.setText(LanguageManager.getInstance().getValue("list_device"));
        this.listAITitle.setText(LanguageManager.getInstance().getValue("ai_list"));
        this.accessTitle.setText(LanguageManager.getInstance().getValue("accessdetect"));
        this.customerTitle.setText(LanguageManager.getInstance().getValue("customerrecognize"));
        this.licenseTx.setText(LanguageManager.getInstance().getValue("licenseplate"));
        this.statusDeviceTitle.setText(LanguageManager.getInstance().getValue("deviceactivity"));
        this.startTimeTx.setText(ApplicationService.startTime);
        this.endTimeTx.setText(ApplicationService.endTime);
        if (ApplicationService.applicationId != null) {
            resetCheck(ApplicationService.applicationId);
        }
        if (ApplicationService.listCameraId != null) {
            processListCam(ApplicationService.listCameraId);
        }
        this.listDevice.setLayoutManager(new LinearLayoutManager(this));
        this.deviceFilterAdapter = new DeviceFilterAdapter(this, ApplicationService.cameraitems);
        this.listDevice.setAdapter(deviceFilterAdapter);
        this.listDevice.setNestedScrollingEnabled(false);

    }

    private void action() {
        this.startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetDateTimeDialog.class);
                String[] dateTimeStart = ApplicationService.startTime.split(" ");
                intent.putExtra("type", "starttime");
                intent.putExtra("time", dateTimeStart[1]);
                intent.putExtra("date", dateTimeStart[0]);
                intent.putExtra("compareDate", ApplicationService.endTime);
                intent.setFlags(0);
                startActivityIntent.launch(intent);
            }
        });
        this.endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetDateTimeDialog.class);
                String[] dateTimeEnd = ApplicationService.endTime.split(" ");
                intent.putExtra("type", "endtime");
                intent.putExtra("time", dateTimeEnd[1]);
                intent.putExtra("date", dateTimeEnd[0]);
                intent.putExtra("compareDate", ApplicationService.startTime);
                intent.setFlags(0);
                startActivityIntent.launch(intent);
            }
        });
        this.accessCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(accessCheck, accessDetect);
            }
        });
        this.customerCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(customerCheck, faceRecog);
            }
        });
        this.licenseCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(licenseCheck, licenseDetect);
            }
        });
        this.statusDeviceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(statusDeviceCheck, deviceActivity);
            }
        });
        this.reloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeDefault();
                resetCheck(reload);
                resetDevice();
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setInterpolator(new LinearInterpolator());
                rotateImage.startAnimation(rotate);
            }
        });
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationService.listCameraId = getListDevice();
                JSONObject payload = new JSONObject();
                try {
                    payload.put("fromDate", ApplicationService.startTime + "+7:00");
                    payload.put("toDate", ApplicationService.endTime + "+7:00");
                    payload.put("listCameraId", ApplicationService.listCameraId);
                    payload.put("pageSize", 10);
                    payload.put("pageIndex", 1);
                    payload.put("optionTime", 1);
                    payload.put("applicationId", ApplicationService.applicationId);
                    payload.put("eventId", 0);
                    ApplicationService.notifyItems.clear();
                    ApplicationService.requestManager.getListNotify(payload, ApplicationService.URL_GET_LIST_NOTIFY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTimeDefault() {
        String pattern = "yyyy-MM-dd HH:mm";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar currentCalen = Calendar.getInstance();
        ApplicationService.endTime = simpleDateFormat.format(currentCalen.getTime());
        currentCalen.add(Calendar.DATE, -6);
        ApplicationService.startTime = simpleDateFormat.format(currentCalen.getTime());
        this.startTimeTx.setText(ApplicationService.startTime);
        this.endTimeTx.setText(ApplicationService.endTime);
    }

    private void resetCheck(String value) {
        switch (value) {
            case accessDetect:
                accessCheck.setChecked(true);
                accessCheck.setSelected(true);
                licenseCheck.setChecked(false);
                licenseCheck.setSelected(false);
                customerCheck.setChecked(false);
                customerCheck.setSelected(false);
                statusDeviceCheck.setChecked(false);
                statusDeviceCheck.setSelected(false);
                ApplicationService.applicationId = value;
                break;
            case faceRecog:
                customerCheck.setChecked(true);
                customerCheck.setSelected(true);
                accessCheck.setChecked(false);
                accessCheck.setSelected(false);
                licenseCheck.setChecked(false);
                licenseCheck.setSelected(false);
                statusDeviceCheck.setChecked(false);
                statusDeviceCheck.setSelected(false);
                ApplicationService.applicationId = value;
                break;
            case licenseDetect:
                licenseCheck.setChecked(true);
                licenseCheck.setSelected(true);
                accessCheck.setChecked(false);
                accessCheck.setSelected(false);
                customerCheck.setChecked(false);
                customerCheck.setSelected(false);
                statusDeviceCheck.setChecked(false);
                statusDeviceCheck.setSelected(false);
                ApplicationService.applicationId = value;
                break;
            case deviceActivity:
                statusDeviceCheck.setChecked(true);
                statusDeviceCheck.setSelected(true);
                accessCheck.setChecked(false);
                accessCheck.setSelected(false);
                licenseCheck.setChecked(false);
                licenseCheck.setSelected(false);
                customerCheck.setChecked(false);
                customerCheck.setSelected(false);
                ApplicationService.applicationId = value;
                break;
            case reload:
                setAllCheck(false);
                ApplicationService.applicationId = null;
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetDevice() {
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.isChoose()) {
                cameraItem.setChoose(false);
            }
        }
        deviceFilterAdapter.notifyDataSetChanged();
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
                        String type = data.getExtras().getString("type");
                        if (type.equals("starttime")) {
                            ApplicationService.startTime = data.getExtras().getString("selectedDateTime");
                            startTimeTx.setText(ApplicationService.startTime);
                        } else {
                            ApplicationService.endTime = data.getExtras().getString("selectedDateTime");
                            endTimeTx.setText(ApplicationService.endTime);
                        }
                    }
                }
            });

    private String getListDevice() {
        ArrayList<Integer> listId = new ArrayList<>();
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
//            Log.e(TAG, "getListDevice: " + cameraItem.getCameraName() + " " + cameraItem.isChoose());
            if (cameraItem.isChoose()) {
                listId.add(cameraItem.getCameraId());
            }
        }
        return listId.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    private void processListCam(String listCamId) {
        String[] listID = listCamId.split(",");
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            for (String s : listID) {
                if (String.valueOf(cameraItem.getCameraId()).equals(s)) {
                    cameraItem.setChoose(true);
                }
            }
        }
    }

    private void setCheck(RadioButton radioButton, String modelName) {
        if (!radioButton.isSelected()) {
            resetCheck(modelName);
        } else {
            radioButton.setChecked(false);
            radioButton.setSelected(false);
            resetCheck(reload);
        }
    }

    private void setAllCheck(boolean ischeck) {
        accessCheck.setChecked(ischeck);
        licenseCheck.setChecked(ischeck);
        customerCheck.setChecked(ischeck);
        statusDeviceCheck.setChecked(ischeck);
        accessCheck.setSelected(ischeck);
        licenseCheck.setSelected(ischeck);
        customerCheck.setSelected(ischeck);
        statusDeviceCheck.setSelected(ischeck);
    }
    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == ApplicationService.MESSAGE_GET_LIST_NOTIFY_SUCCESS) {
                finish();
            }
        }
    }
}