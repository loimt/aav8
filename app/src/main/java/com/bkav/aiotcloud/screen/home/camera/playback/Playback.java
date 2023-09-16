package com.bkav.aiotcloud.screen.home.camera.playback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.home.camera.setting.TimePartItem;
import com.bkav.aiotcloud.screen.setting.face.customer.FilterCustomer;
import com.bkav.aiotcloud.screen.setting.face.historyObject.CalendarBottomSheet;
import com.bkav.aiotcloud.view.TimeRuleView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
//import com.kevalpatel2106.rulerpicker.RulerValuePicker;
//import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Playback extends Fragment {
    public static final String PLAYBACK = "playback_service";
    public static Handler playbackHandle = null;
    public static final int ACTION_UP = 1;
    public static final int REPAINT = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playback_layout, container, false);
        init(view);

        this.id = requireArguments().getInt("ID");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private int id;

    private Button btHD;
    private Button btSD;

    private ImageView redSD;
    private ImageView redHD;
    private CameraItem currentCamera;

    private void init(View view){
        this.btHD = view.findViewById(R.id.btHD);
        this.btSD = view.findViewById(R.id.btSD);

        for (CameraItem cameraItem : ApplicationService.cameraitems){
            if (id == cameraItem.getCameraId()){
                currentCamera = cameraItem;
            }
        }
        if (currentCamera.getCameraInfo() != null){
            Log.e("mediazz " , currentCamera.getCameraInfo());
        }

        this.redSD = view.findViewById(R.id.redSD);
        this.redHD = view.findViewById(R.id.redHD);

        btHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });
    }
}