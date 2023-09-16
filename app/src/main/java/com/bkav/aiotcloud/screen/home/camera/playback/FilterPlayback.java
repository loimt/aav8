package com.bkav.aiotcloud.screen.home.camera.playback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.screen.notify.FilterNotifyActivity;
import com.bkav.aiotcloud.screen.setting.face.historyObject.EventDecorator;
import com.bkav.aiotcloud.screen.setting.face.historyObject.ListDates;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class FilterPlayback extends AppCompatActivity {
    public static String DATE_HAVE_DATE = "date_have_data";
    public static String CURRENT_DATE = "current_date";
    public static String SELECT_DATE = "select_date";

    @SuppressLint("AppCompatMethod")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_playback);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            // Portrait Mode
            wlp.gravity = Gravity.BOTTOM;
        } else {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            // Landscape Mode
            wlp.gravity = Gravity.TOP;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Intent intent = getIntent();
        String dateHasData = intent.getStringExtra(DATE_HAVE_DATE);
        int currentDate = intent.getIntExtra(CURRENT_DATE, 0);
        try {
            dates = new JSONArray(dateHasData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setFinishOnTouchOutside(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentDate * 1000L);
//        calendarView.setDateSelected(date, true);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//        calendarView.setSelectionColor(ContextCompat.getColor(this, R.color.mainColor));

//        calendarView.setDateSelected(CalendarDay.from(mYear, mMonth, mDay), true);
        dayChoose = CalendarDay.from(mYear, mMonth, mDay);
        calendarView.setSelectedDate(dayChoose);
//        calendarView.set

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private MaterialCalendarView calendarView;
    private Button ok;

    private void init() {
        this.calendarView = findViewById(R.id.calendarView);
        this.ok = findViewById(R.id.ok);
        if (dates.length() != 0) {
            HashSet<CalendarDay> listDaysMap = new HashSet<>();
            try {
                for (int index = 0; index < dates.length(); index++) {
                    listDaysMap.add(CalendarDay.from(dates.getJSONObject(index).getInt("year"),
                            dates.getJSONObject(index).getInt("month"), dates.getJSONObject(index).getInt("day")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.calendarView.addDecorator(new EventDecorator(getApplicationContext().getResources().getColor(R.color.green), listDaysMap, getApplicationContext()));
        }

        this.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
              dayChoose = date;
            }
        });

        this.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataBack = new Intent();
                dataBack.putExtra(SELECT_DATE, dayChoose.getCalendar().getTimeInMillis()/1000);
                Log.e("start time ", " " +dayChoose.getCalendar().getTimeInMillis()/1000);
                setResult(RESULT_OK, dataBack);
                finish();
            }
        });
    }

    private JSONArray dates;
    private CalendarDay dayChoose;
}
