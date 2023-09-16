package com.bkav.aiotcloud.screen.setting.face;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SetTimeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_time_ai_detail);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.titleTx);
        TextView timeTitle = findViewById(R.id.timeTitle);
        TextView repeat = findViewById(R.id.repeat);

         timeStartTx = findViewById(R.id.timeStart);
         timeEndTx = findViewById(R.id.timeEnd);

        RelativeLayout timeStartLayout = findViewById(R.id.timeStartLayout);
        RelativeLayout timeEndLayout = findViewById(R.id.timeEndLayout);

        this.listDay = findViewById(R.id.listDay);
        listDay.setLayoutManager(new LinearLayoutManager(this));
        this.names = new ArrayList<>();
        this.dayItems = new ArrayList<>();
        this.names.add(LanguageManager.getInstance().getValue("sun"));
        this.names.add(LanguageManager.getInstance().getValue("mon"));
        this.names.add(LanguageManager.getInstance().getValue("tue"));
        this.names.add(LanguageManager.getInstance().getValue("wed"));
        this.names.add(LanguageManager.getInstance().getValue("thu"));
        this.names.add(LanguageManager.getInstance().getValue("fri"));
        this.names.add(LanguageManager.getInstance().getValue("sat"));

        for (int index = 0; index < 7; index++) {
            DayItem dayItem = new DayItem(this.names.get(index));
            dayItems.add(dayItem);
        }

        listDayAdapter = new ListDayAdapter(this, dayItems);
        listDay.setAdapter(listDayAdapter);

        type = getIntent().getStringExtra(StepThreeFragment.TYPE);
        position = getIntent().getIntExtra(StepThreeFragment.POSITION, -1);
        if (type.equals(StepThreeFragment.EDIT)){
            ScheduleItem scheduleItem = (ScheduleItem) getIntent().getSerializableExtra(StepThreeFragment.VALUE);
            String value = scheduleItem.getDays().substring(1, scheduleItem.getDays().length() - 1).replace(" ", "");;
            String[] days = value.split(",");
            for (int indexDays = 0; indexDays < days.length; indexDays++) {
                dayItems.get(Integer.parseInt(days[indexDays])).setActive(true);
            }

            this.timeStartValue = scheduleItem.getTimeStart();
            this.timeEndValue = scheduleItem.getTimeEnd();
            timeStartTx.setText(this.timeStartValue);
            timeEndTx.setText(this.timeEndValue );
        } else if (type.equals(StepThreeFragment.NEW)){

        }


        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.ok);

        title.setText(LanguageManager.getInstance().getValue("add_schedule"));
        timeTitle.setText(LanguageManager.getInstance().getValue("time"));
        repeat.setText(LanguageManager.getInstance().getValue("repeat"));
        cancel.setText(LanguageManager.getInstance().getValue("cancel"));


//        timeStartLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeStartTx.getText().length() == 0 || timeEndTx.getText().length() == 0){
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("forget_set_time"), true );
                    return;
                }
                String value = getvalue();
                if (!isChooseDay){
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("forget_choose_day"), true );
                    return;
                }
                Intent data = new Intent();
                data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.ADD_SHEDULE);
                if (type.equals(StepThreeFragment.EDIT)){
                    data.putExtra(StepThreeFragment.POSITION, position);
                } else {
                    data.putExtra(StepThreeFragment.POSITION, -1);
                }
                data.putExtra(StepThreeFragment.TIME_START, timeStartValue);
                data.putExtra(StepThreeFragment.TIME_END, timeEndValue);
                data.putExtra(StepThreeFragment.DAYS, value);
                data.putExtra(StepThreeFragment.DAY_CONVERSE, dayConverse);
                setResult(RESULT_OK, data);


                finish();
            }
        });

        timeStartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getApplication(), SetHourDialog.class);
                chooserIntent.setFlags(0);
                chooserIntent.putExtra(StepThreeFragment.TYPE, StepThreeFragment.TIME_START);
                startActivityIntent.launch(chooserIntent);
            }
        });

        timeEndLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooserIntent = new Intent(getApplication(), SetHourDialog.class);
                chooserIntent.setFlags(0);
                chooserIntent.putExtra(StepThreeFragment.TYPE, StepThreeFragment.TIME_END);
                startActivityIntent.launch(chooserIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    ActivityResultLauncher<Intent> startActivityIntent =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                assert data != null;
                                String type = data.getExtras().getString(StepThreeFragment.TYPE);
                                if (type.equals(StepThreeFragment.TIME_START)){
                                     timeStartValue = data.getStringExtra(StepThreeFragment.VALUE);
//                                     timeStartTx.setText(timeStartValue);
                                     String[] time = timeStartValue.split(":");
                                    timeStartTx.setText(LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1])).format(
                                            // using a desired pattern
                                            DateTimeFormatter.ofPattern("HH:mm")));
                                } else if (type.equals(StepThreeFragment.TIME_END)){
                                    timeEndValue = data.getStringExtra(StepThreeFragment.VALUE);
                                    String[] time = timeEndValue.split(":");
                                    timeEndTx.setText(LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1])).format(
                                            // using a desired pattern
                                            DateTimeFormatter.ofPattern("HH:mm")));
                                }
                            }
                        }
                    });


    private ListDayAdapter listDayAdapter;
    private RecyclerView listDay;

    private String type;
    private int position;
    private ArrayList<DayItem> dayItems;
    private List<String> names;
    private TextView timeStartTx;
    private TextView timeEndTx;
    private String timeStartValue;
    private String timeEndValue;
    private String dayConverse = "";
    private boolean isChooseDay = false;

    private String getvalue() {
        List<Integer> days = new ArrayList<>();
        for (int index = 0; index < 7; index++) {
            DayItem dayItem = dayItems.get(index);
            if (dayItem.isActive()) {
                days.add(index);
                dayConverse = dayConverse.concat(names.get(index)+", ");
                isChooseDay = true;
            }
        }
        if (dayConverse.length() != 0){
            dayConverse = dayConverse.substring(0, dayConverse.length() -2);
        }
        return days.toString();
    }
}


