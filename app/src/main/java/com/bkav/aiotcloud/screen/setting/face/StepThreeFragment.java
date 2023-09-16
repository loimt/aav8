package com.bkav.aiotcloud.screen.setting.face;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SetNationDialog;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.edit.SetDetectConfigDialog;
import com.bkav.aiotcloud.view.CustomEditText;
import com.bkav.aiotcloud.view.GridviewCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StepThreeFragment extends Fragment implements ScheduleAdapter.OnMenuItemClickListener {
    public static final String SENSITIVITY = "sensitivity";
    public static final String FACE = "face";
    public static final String SCHEDULE = "schedule";
    public static final String NATION = "nation";
    public static final String TIME_START = "time_start";
    public static final String TIME_END = "time_end";
    public static final String DAY_CONVERSE = "day_converse";
    public static final String DAYS = "days";
    public static final String ADD_SHEDULE = "add_schedule";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String POSITION = "position";
    public static final String EDIT = "edit";
    public static final String NEW = "new";
    public static final String TIME_WARNING = "timewarning";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.step_three_fragment, container, false);
        init(frag);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setUI(String type) {
        if (type.equals(SettingFragment.FACE_RECOGNIZE)) {
            this.type = SettingFragment.FACE_RECOGNIZE;
            this.faceLayout.setVisibility(View.VISIBLE);
            this.timeWarningLayout.setVisibility(View.GONE);
        } else if (type.equals(SettingFragment.INTRUSION_DETECT)) {
            this.faceLayout.setVisibility(View.GONE);
            this.timeWarningLayout.setVisibility(View.VISIBLE);
        } else if (type.equals(SettingFragment.LICENSE_PLATE)) {
            this.type = SettingFragment.LICENSE_PLATE;
            this.faceLayout.setVisibility(View.GONE);
            this.timeWarningLayout.setVisibility(View.GONE);
            this.nationLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setType(String type) {
        this.type = type;
//        if (type.equals(SettingFragment.INTRUSION_DETECT)){
//            this.faceLayout.setVisibility(View.GONE);
//        }
    }

    public JSONObject getProfileFeatures() {
        JSONObject profileFeatures = new JSONObject();
        try {
            profileFeatures.put("threshold_recognize", currentSensitivity);
            profileFeatures.put("profile_size", currentFace);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileFeatures;
    }

    public JSONObject getdDetectConfig() {
        JSONObject detectConfig = new JSONObject();
        try {
//            detectConfig.put("threshold_recognize", currentSensitivity);
//            detectConfig.put("isWarnAll", true);
//            detectConfig.put("lsObjectDetect", "");
//            detectConfig.put("smallestObjectWarn", 1);
//            detectConfig.put("warnMode", "");
//            detectConfig.put("timeToWarn", 0);
            detectConfig.put("repeated_time", currenTimeRepeatWarn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detectConfig;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataEdit(JSONObject editItem) {
        Log.e("editItem", editItem.toString());
        try {
            JSONArray scheduleDtails = editItem.getJSONArray("scheduleDetails");
            String frequencyType = editItem.getJSONObject("schedule").getString("frequencyType");
            if (type.equals(SettingFragment.FACE_RECOGNIZE)) {
                JSONObject jsonParameter = editItem.getJSONObject("extra");
                currentFace = jsonParameter.getInt("profile_size");
                setFaceValue(currentFace);
                currentSensitivity = jsonParameter.getInt("threshold_recognize");
                setSensitivity(currentSensitivity);
            } else if (type.equals(SettingFragment.INTRUSION_DETECT)) {
//                JSONObject detectConfig = editItem.getJSONObject("detectConfig");
                currenTimeRepeatWarn = editItem.getJSONObject("extra").getInt("repeated_time");
                timeRepeatWarnValue.setText(String.valueOf(currenTimeRepeatWarn));
            } else if (type.equals(SettingFragment.LICENSE_PLATE)) {
//                String parameterValue = editItem.getJSONObject("extra").getString("");
                setValueNation(editItem.getJSONObject("extra").getString("nation"));
            }
            setSchedule(frequencyType, scheduleDtails);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setSchedule(String frequencyType, JSONArray scheduleDtails) {
        try {
            if (frequencyType.equals("daily")) {
                addScheuleLayout.setVisibility(View.GONE);
                isCustomSchedule = false;
            } else {
                isCustomSchedule = true;
                addScheuleLayout.setVisibility(View.VISIBLE);
            }
            scheduleItems.clear();

            if (isCustomSchedule) {
                customSchedule.setText(LanguageManager.getInstance().getValue("setting_handmade"));
                for (int index = 0; index < scheduleDtails.length(); index++) {
                    JSONObject scheuleitem = null;
                    scheuleitem = scheduleDtails.getJSONObject(index);
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
                Log.e("dates xxx",scheduleItems.size()+ "" );
                scheduleAdapter.notifyDataSetChanged();
            } else {
                customSchedule.setText(LanguageManager.getInstance().getValue("alway_active"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCurrenNation() {
        return currenNation;
    }

    public JSONArray getScheduleDetails() {
        JSONArray scheduleDetails = new JSONArray();
        if (isCustomSchedule) {
            try {
                for (ScheduleItem scheduleItem : scheduleItems) {
                    JSONObject scheduleItemJson = new JSONObject();

                    String[] timeStarts = scheduleItem.getTimeStart().split(":");
                    int hourStart = Integer.parseInt(timeStarts[0]);
                    int minuteStart = Integer.parseInt(timeStarts[1]);
                    String valueStart = LocalTime.of(hourStart, minuteStart, 0).format(
                            DateTimeFormatter.ofPattern("HH:mm:ss"));

                    String[] timeEnds = scheduleItem.getTimeEnd().split(":");
                    int hourEnd = Integer.parseInt(timeEnds[0]);
                    int minuteEnd = Integer.parseInt(timeEnds[1]);
                    String valueEnd = LocalTime.of(hourEnd, minuteEnd, 0).format(
                            DateTimeFormatter.ofPattern("HH:mm:ss"));

                    scheduleItemJson.put("scheduleDetailId", 0);
                    scheduleItemJson.put("fromAt", valueStart);
                    scheduleItemJson.put("toAt", valueEnd);
                    scheduleItemJson.put("dayOfWeekOrMonth", scheduleItem.getDays());
                    scheduleItemJson.put("isDelete", 0);

                    scheduleDetails.put(scheduleItemJson);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("scheduleItem",scheduleDetails.length()  + "");

        return scheduleDetails;
    }

    private RelativeLayout sensitivityLayout;
    private RelativeLayout sizeFaceLayout;
    private RelativeLayout sheduleLayout;
    private RelativeLayout faceLayout;
    private RelativeLayout timeWarningLayout;
    private RelativeLayout nationLayout;

    private GridviewCustom listCamera;
    private CustomEditText searchTx;
    private int currentSelected = -1;
    private int currenTimeRepeatWarn = 10;
    private String currenNation = "VN"; // US VN MY
    private RelativeLayout addScheuleLayout;

    private int currentSensitivity = 5;
    private int currentFace = 1;
    private boolean isCustomSchedule = false;
    private String timeStart = "";
    private String timeEnd = "";
    private String days = "";
    private String type;

    private TextView sensitivityValue;
    private TextView faceValue;
    private TextView customSchedule;
    private TextView timeRepeatWarnTx;
    private TextView timeRepeatWarnValue;
    private TextView scheduleTitleTx;
    private TextView nationTx;
    private TextView nationValue;

    private TextView settingTx;
    private TextView sensitivityTx;
    private TextView sizeFaceTx;
    private TextView scheduleTx;


    private ScheduleAdapter scheduleAdapter;
    private RecyclerView listSchedule;
    private ArrayList<ScheduleItem> scheduleItems;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String type = data.getExtras().getString(StepThreeFragment.TYPE);

                        switch (type) {
                            case SENSITIVITY:
                                currentSensitivity = data.getIntExtra(VALUE, 1);
                                setSensitivity(currentSensitivity);
                                break;
                            case FACE:
                                currentFace = data.getIntExtra(VALUE, 0);
                                setFaceValue(currentFace);
                                break;
                            case SCHEDULE:
                                isCustomSchedule = data.getBooleanExtra(VALUE, false);
                                if (isCustomSchedule) {
                                    customSchedule.setText(LanguageManager.getInstance().getValue("setting_handmade"));
                                    addScheuleLayout.setVisibility(View.VISIBLE);
                                } else {
                                    customSchedule.setText(LanguageManager.getInstance().getValue("alway_active"));
                                    addScheuleLayout.setVisibility(View.GONE);
                                }
                                break;
                            case ADD_SHEDULE:
                                int position = data.getIntExtra(POSITION, -1);

                                timeStart = data.getStringExtra(TIME_START);
                                timeEnd = data.getStringExtra(TIME_END);
                                days = data.getStringExtra(DAYS);

                                ScheduleItem scheduleItem = new ScheduleItem(timeStart, timeEnd, days);
                                if (position != -1){
                                    scheduleItems.set(position, scheduleItem);
                                } else {
                                    scheduleItems.add(scheduleItem);

                                }
                                scheduleAdapter.notifyDataSetChanged();
                                scheduleAdapter.closeAllItems();
                                break;
                            case TIME_WARNING:
                                currenTimeRepeatWarn = data.getIntExtra(VALUE, 10);
                                timeRepeatWarnValue.setText(String.valueOf(currenTimeRepeatWarn));
                                break;
                            case NATION:
                                setValueNation(data.getStringExtra(VALUE));
                                break;
                        }
//                        if (type.equals(SENSITIVITY)) {
//                            currentSensitivity = data.getDoubleExtra(VALUE, 0);
//                            setSensitivity(currentSensitivity);
//                        } else if (type.equals(FACE)) {
//                            currentFace = data.getIntExtra(VALUE, 0);
//                            setFaceValue(currentFace);
//                        } else if (type.equals(SCHEDULE)) {
//                            isCustomSchedule = data.getBooleanExtra(VALUE, false);
//                            if (isCustomSchedule) {
//                                customSchedule.setText(LanguageManager.getInstance().getValue("setting_handmade"));
//                                addScheuleLayout.setVisibility(View.VISIBLE);
//                            } else {
//                                customSchedule.setText(LanguageManager.getInstance().getValue("alway_active"));
//                                addScheuleLayout.setVisibility(View.GONE);
//                            }
//                        } else if (type.equals(ADD_SHEDULE)) {
//                            timeStart = data.getStringExtra(TIME_START);
//                            timeEnd = data.getStringExtra(TIME_END);
//                            days = data.getStringExtra(DAYS);
//                            ScheduleItem scheduleItem = new ScheduleItem(timeStart, timeEnd, days);
//                            scheduleItems.add(scheduleItem);
//                            scheduleAdapter.notifyDataSetChanged();
//                        } else if (type.equals(TIME_WARNING)) {
//                            currenTimeRepeatWarn = data.getIntExtra(VALUE, 10);
//                            Log.e("time ", currenTimeRepeatWarn + "");
//                            timeRepeatWarnValue.setText(String.valueOf(currenTimeRepeatWarn));
//                        }
                    }
                }
            });

    private void setSensitivity(int sensitivity) {

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

    private void setFaceValue(int currentFace) {
        if (currentFace == 1) {
            faceValue.setText(LanguageManager.getInstance().getValue("veryBig"));
        } else if (currentFace == 2) {
            faceValue.setText(LanguageManager.getInstance().getValue("big"));
        } else if (currentFace == 3) {
            faceValue.setText(LanguageManager.getInstance().getValue("medium"));
        }else if (currentFace == 4) {
            faceValue.setText(LanguageManager.getInstance().getValue("small"));
        }else if (currentFace == 5) {
            faceValue.setText(LanguageManager.getInstance().getValue("verySmall"));
        }
    }

    private void init(View view) {
        this.sensitivityLayout = view.findViewById(R.id.sensitivityLayout);
        this.sizeFaceLayout = view.findViewById(R.id.sizeFaceLayout);
        this.sheduleLayout = view.findViewById(R.id.scheduleLayout);
        this.addScheuleLayout = view.findViewById(R.id.addScheuleLayout);
        this.scheduleTitleTx = view.findViewById(R.id.scheduleTitleTx);

        this.sensitivityValue = view.findViewById(R.id.sensitivityValue);
        this.faceValue = view.findViewById(R.id.sizeFaceValue);
        this.customSchedule = view.findViewById(R.id.scheduleValue);
        this.customSchedule.setText(LanguageManager.getInstance().getValue("alway_active"));
        this.faceLayout = view.findViewById(R.id.faceLayout);

        this.timeWarningLayout = view.findViewById(R.id.timeWarningLayout);
        this.timeRepeatWarnTx = view.findViewById(R.id.timeWaningTx);
        this.timeRepeatWarnValue = view.findViewById(R.id.timeWarningValue);

        this.listSchedule = view.findViewById(R.id.listSchedule);
        this.listSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        this.scheduleItems = new ArrayList<>();
        this.scheduleAdapter = new ScheduleAdapter(getContext(), scheduleItems, this);
        this.listSchedule.setAdapter(scheduleAdapter);

        this.settingTx = view.findViewById(R.id.settingTx);
        this.sensitivityTx = view.findViewById(R.id.sensitivityTx);
        this.sizeFaceTx = view.findViewById(R.id.sizeFaceTx);
        this.scheduleTx = view.findViewById(R.id.scheduleTx);

        this.nationLayout = view.findViewById(R.id.nationLayout);
        this.nationTx = view.findViewById(R.id.nationTx);
        this.nationValue = view.findViewById(R.id.nationValue);

        this.settingTx.setText(LanguageManager.getInstance().getValue("setting_parameter"));
        this.sensitivityTx.setText(LanguageManager.getInstance().getValue("sensitivity"));
        this.sizeFaceTx.setText(LanguageManager.getInstance().getValue("size_face"));
        this.scheduleTx.setText(LanguageManager.getInstance().getValue("schedule_active"));
        this.scheduleTitleTx.setText(LanguageManager.getInstance().getValue("schedule_active"));
        this.timeRepeatWarnTx.setText(LanguageManager.getInstance().getValue("time_warning"));
        this.nationTx.setText(LanguageManager.getInstance().getValue("nation"));
        this.sensitivityValue.setText(LanguageManager.getInstance().getValue("veryHigh"));
        this.faceValue.setText(LanguageManager.getInstance().getValue("veryBig"));


        ImageView addTime = view.findViewById(R.id.addSchedule);


        this.sensitivityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetSensitivityDialog.class);
                chooserIntent.putExtra(VALUE, currentSensitivity);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        this.sizeFaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetSizeFaceDialog.class);
                chooserIntent.putExtra(VALUE, currentFace);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        this.sheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetScheduleDialog.class);
                chooserIntent.putExtra(VALUE, isCustomSchedule);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        this.nationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetNationDialog.class);
                chooserIntent.putExtra(VALUE, currenNation);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetTimeDetail.class);
                chooserIntent.putExtra(StepThreeFragment.TYPE, StepThreeFragment.NEW);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });

        this.timeWarningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getActivity(), SetDetectConfigDialog.class);
                chooserIntent.putExtra(StepThreeFragment.VALUE, currenTimeRepeatWarn);
                chooserIntent.setFlags(0);
                startActivityIntent.launch(chooserIntent);
            }
        });
    }

    public int checkStatement() {
        return AddOjectFace.SUCCESS;
    }

    private void setValueNation(String value) {
        this.currenNation = value;
        if (value.equals("VN")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("vietnam"));
        } else if (value.equals("USA")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("usa"));
        } else if (value.equals("MY")) {
            this.nationValue.setText(LanguageManager.getInstance().getValue("malaysia"));
        }
    }

    @Override
    public void onMenuItemClick(ScheduleItem scheduleItem, int position, int event) {
        if (event == ScheduleAdapter.EDIT_ZONE_EVENT) {
            Intent chooserIntent = new Intent(getActivity(), SetTimeDetail.class);
            chooserIntent.putExtra(StepThreeFragment.TYPE, StepThreeFragment.EDIT);
            chooserIntent.putExtra(StepThreeFragment.VALUE, scheduleItem);
            chooserIntent.putExtra(StepThreeFragment.POSITION, position);
            chooserIntent.setFlags(0);
            startActivityIntent.launch(chooserIntent);
        } else if (event == ScheduleAdapter.DELETE_ZONE_EVENT) {
            this.scheduleItems.remove(position);
        }
        scheduleAdapter.notifyDataSetChanged();
    }
}