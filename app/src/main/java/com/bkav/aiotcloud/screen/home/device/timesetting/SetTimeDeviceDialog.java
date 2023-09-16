package com.bkav.aiotcloud.screen.home.device.timesetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.util.LanguageConfig;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SetTimeDeviceDialog extends AppCompatActivity {
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_date_hour_dialog);
        setLayout();
        init();
        setData();
        setTime();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LanguageConfig.changeLanguage(newBase, LanguageManager.getInstance().getValue("datetime_picker"));
        super.attachBaseContext(context);
    }

    private static final String TAG = "SetTimeDeviceDialog";
    private MaterialCalendarView calendarView;

    private int currentDate;
    private int currentMonth;
    private int currentYear;

    private int hour;

    private int minute;

    private TimePicker timePicker;
    private String time;
    private Button save;


    private void init() {
        this.calendarView = findViewById(R.id.calendarView);
        this.timePicker = findViewById(R.id.timePicker);
        this.timePicker.setIs24HourView(true);

        this.time = getIntent().getStringExtra(DeviceTimeSettingActivity.DATE_TIME);
        this.save = findViewById(R.id.save);

    }

    public void setTime() {
        if (this.time.length() == 0) {
            return;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(DateTimeFormat.TIME_FORMAT);
        isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Date date = null;
        try {
            date = isoFormat.parse(time);
            Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);
            hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
            minute = calendar.get(Calendar.MINUTE);
            currentDate = calendar.get(Calendar.DAY_OF_MONTH) ;
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            Log.e(TAG, currentYear + " " + currentMonth + " " + currentDate + " " + hour + " " + minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.calendarView.setDateSelected(date, true);
        this.timePicker.setHour(hour);
        this.timePicker.setMinute(minute);

    }

    private void setLayout() {
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFinishOnTouchOutside(true);
    }

    private void setData() {
        this.calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        LanguageConfig.changeLanguage(getApplicationContext(), LanguageManager.getInstance().getValue("datetime_picker"));
    }

    private void action() {
        this.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat isoFormat = new SimpleDateFormat(DateTimeFormat.TIME_FORMAT);
//                isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

                Calendar calendar = date.getCalendar();
                currentDate = calendar.get(Calendar.DAY_OF_MONTH);
                currentMonth = calendar.get(Calendar.MONTH) + 1;
                currentYear = calendar.get(Calendar.YEAR);
                Log.e("Timeeee", "timeStartValue"+  currentDate + " " + currentMonth);

            }
        });

       this.save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent data = new Intent();

               data.putExtra(HOUR, timePicker.getHour());
               data.putExtra(MINUTE, timePicker.getMinute());
               data.putExtra(DAY,currentDate);
               data.putExtra(MONTH, currentMonth);
               data.putExtra(YEAR, currentYear);
               setResult(RESULT_OK, data);

//               Log.e("Timeeee", "timeStartValue"+  timeStartValue + " " + timeEndValue);
               finish();
           }
       });

    }


}