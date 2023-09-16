package com.bkav.aiotcloud.screen.notify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.util.LanguageConfig;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SetDateTimeDialog extends AppCompatActivity {
    private CalendarView calendarView;
    private TimePicker timePicker;
    private Button save;
    private String type;
    private String date;
    private String time;
    private final String parttenDate = "yyyy-MM-dd";
    private final String parttenDecimal = "00";
    private final String parttenDateTime = "yyyy-MM-dd HH:mm";
    private String compareDate;
    private final DecimalFormat decimalFormat = new DecimalFormat(parttenDecimal);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(parttenDate);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(parttenDateTime);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setdatetime_dialog);
        setLayout();
        init();
        setData();
        action();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LanguageConfig.changeLanguage(newBase, LanguageManager.getInstance().getValue("datetime_picker"));
        super.attachBaseContext(context);
    }
    private void init(){
        Intent intent = getIntent();
        this.date = intent.getStringExtra("date");
        this.time = intent.getStringExtra("time");
        this.type = intent.getStringExtra("type");
        this.compareDate = intent.getStringExtra("compareDate");
        this.save = findViewById(R.id.save);
        this.calendarView = findViewById(R.id.calendarView);
        this.timePicker = findViewById(R.id.timePicker);
    }
    private void setLayout(){
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFinishOnTouchOutside(true);
    }
    private void setData(){
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        LanguageConfig.changeLanguage(getApplicationContext(), LanguageManager.getInstance().getValue("datetime_picker"));
        this.timePicker.setIs24HourView(true);
        colorizeDatePicker(timePicker);
        setDateTime();

    }
    private void action(){
        this.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = decimalFormat.format(year) + "-" + decimalFormat.format(month+1) + "-" + decimalFormat.format(dayOfMonth);
            }
        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String selectedTime = decimalFormat.format(timePicker.getHour()) + ":" + decimalFormat.format(timePicker.getMinute());
                    String selectedDateTime = date + " " + selectedTime;
                    Date dateCompare = dateTimeFormat.parse(compareDate);
                    Date timeSelected = dateTimeFormat.parse(selectedDateTime);
                    if(type.equals("starttime")){
                        assert timeSelected != null;
                        if(timeSelected.compareTo(dateCompare) > 0 || timeSelected.compareTo(dateCompare) == 0){
                            ApplicationService.showToast("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc", true);
                        }else{
                            commit(selectedDateTime);
                        }
                    }else if(type.equals("endtime")){
                        assert timeSelected != null;
                        if(timeSelected.compareTo(dateCompare) < 0 || timeSelected.compareTo(dateCompare) == 0){
                            ApplicationService.showToast("Thời gian kết thúc phải lớn hơn thời gian bắt đầu", true);
                        }else{
                            commit(selectedDateTime);
                        }
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void commit(String selectedDateTime){
        Intent dataBack = new Intent();
        dataBack.putExtra("selectedDateTime", selectedDateTime);
        dataBack.putExtra("type", type);
        setResult(RESULT_OK, dataBack);
        finish();
    }
    @SuppressLint("SimpleDateFormat")
    private void setDateTime(){

        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        try {
            this.calendarView.setDate(dateFormat.parse(date).getTime(), true, true);
            this.timePicker.setHour(hour);
            this.timePicker.setMinute(minute);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    public static void colorizeDatePicker(TimePicker timePicker) {
        Resources system = Resources.getSystem();
        int hourId = system.getIdentifier("hour", "id", "android");
        int minutesid = system.getIdentifier("minute", "id", "android");
        NumberPicker dayPicker = (NumberPicker) timePicker.findViewById(hourId);
        NumberPicker monthPicker = (NumberPicker) timePicker.findViewById(minutesid);
        setDividerColor(dayPicker);
        setDividerColor(monthPicker);
    }

    private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                @SuppressLint("SoonBlockedPrivateApi") Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.mainColor, null));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }

        try{
            @SuppressLint("SoonBlockedPrivateApi") Field selectorWheelPaintField = picker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) Objects.requireNonNull(selectorWheelPaintField.get(picker))).setColor(picker.getResources().getColor(R.color.white, null));
        }
        catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException e){
            Log.w("setNumberPickerTextColor", e);
        }

        final int countIndex = picker.getChildCount();
        for(int i = 0; i < countIndex; i++){
            View child = picker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(picker.getResources().getColor(R.color.white, null));
        }
        picker.invalidate();
    }

}
