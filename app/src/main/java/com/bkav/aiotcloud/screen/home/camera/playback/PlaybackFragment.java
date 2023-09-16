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

public class PlaybackFragment extends Fragment {
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        playbackHandle = new MainHandler();
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

    public void paintSelectDataBox(ArrayList<JSONObject> recordingFiles) {
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
//        Log.e("sizzzzz 2", timePartItems.size() + "");

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

        timeRuleView.setCurrentTime(timeDatasCalculated.get(0).getStartTime());
        Message message = new Message();
        message.what = ApplicationService.START_PLAYBACK_CAMERA;
        message.obj = timePartItems.get(timePartItems.size() - 1).getTokenFile();
        ApplicationService.mainHandler.sendMessage(message);

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

    private int count = 0;
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

    private void repaintData() {
        timeDatasCalculated.clear();
        int valueMilestones = timePartItems.get(timePartItems.size() - 1).getEndTime();
        TimePartItem lastRecord = new TimePartItem(timePartItems.get(timePartItems.size() - 1).getStartTime(),
                timePartItems.get(timePartItems.size() - 1).getEndTime(), timePartItems.get(timePartItems.size() - 1).getTokenFile());
        timeDatasCalculated.add(lastRecord);
        for (int index = timePartItems.size() - 1; index >= 0; index--) {
            // check dữ liệu 3 ngày cuối vẽ lên thước
//            Log.e("value ", -(timePartItems.get(index).getStartTime() - valueMilestones)%(24*3600) + "");
            if (Math.abs(TimeRuleView.MIN_TIME_VALUE) > -(timePartItems.get(index).getStartTime() - valueMilestones)) {
                if (timePartItems.get(index).getStartTime() - timePartItems.get(index - 1).getEndTime() <= 1) {
                    timeDatasCalculated.get(timeDatasCalculated.size() - 1).setStartTime(timePartItems.get(index - 1).getStartTime());
                } else {
//                    Log.e("sizzzzz 2", timeDatasCalculated.get(timeDatasCalculated.size() - 1).getStartTime() + " " + timePartItems.get(index).getEndTime() + " ");
                    TimePartItem timePartItem = new TimePartItem(timePartItems.get(index - 1).getStartTime(), timePartItems.get(index - 1).getEndTime(), timePartItems.get(index - 1).getTokenFile());
                    timeDatasCalculated.add(timePartItem);
                }
            }
        }

        timeRuleView.setTimePartList(timeDatasCalculated);

        // lấy ngày có dữ liệu

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

    @SuppressLint("ResourceAsColor")
    private void init(View view) {
        this.timeNow = view.findViewById(R.id.tvTime);
        this.filter = view.findViewById(R.id.filter);
        this.timeRuleView = view.findViewById(R.id.timeRuler);
        this.calendarView = view.findViewById(R.id.calendarView);

        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterPlayback.class);
                intent.putExtra(FilterPlayback.DATE_HAVE_DATE, dateHasData.toString());
                intent.putExtra(FilterPlayback.CURRENT_DATE, timeRuleView.getCurrentTime() + maxValue);
                startActivityIntent.launch(intent);
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
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.NO_DATA_PLAYBACK);
        return false;
    }

    public void updateImgPerSec() {
        this.timeRuleView.setCurrentTime(timeRuleView.getCurrentTime() + 1);
        if (timeRuleView.isUp() && timeRuleView.getCurrentTime() + maxValue + 1 > 24 * 3600) {
            timeNow.setText(DateTimeFormat.converseSecondToTime(timeRuleView.getCurrentTime() + maxValue + 1, DateTimeFormat.TIME_FORMAT));
        }
    }

    private void playFile(int timeSeek, String filePlay) {
        Message message = new Message();
        JSONObject object = new JSONObject();
        try {
            object.put("timeSeek", timeSeek);
            object.put("filePlay", filePlay);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        message.what = ApplicationService.PLAY_VIDEO_PLAYBACK;
        message.obj = object.toString();
        ApplicationService.mainHandler.sendMessage(message);
    }

    private void repainData(int time) {

        timeDatasCalculated.clear();
        for (int index = 0; index < timePartItems.size(); index++) {

            // check dữ liệu 2 ngày cuối vẽ lên thước
            int valueMilestones = timePartItems.get(timePartItems.size() - 1).getEndTime();
            if (Math.abs(TimeRuleView.MIN_TIME_VALUE) > -(timePartItems.get(index).getStartTime() - valueMilestones)) {
                if (index <= 0) {
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
            }
        }
        timeRuleView.setTimePartList(timeDatasCalculated);
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ACTION_UP:
                    playTime(timeRuleView.getCurrentTime() + maxValue);
                    break;
            }
        }
    }

}
