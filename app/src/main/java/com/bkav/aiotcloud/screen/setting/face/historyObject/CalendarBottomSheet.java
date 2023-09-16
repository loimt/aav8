package com.bkav.aiotcloud.screen.setting.face.historyObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.screen.widget.EventFaceInDays;
import com.bkav.aiotcloud.util.LanguageConfig;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class CalendarBottomSheet extends AppCompatActivity {
    private static final String TAG = "CalendarBottomSheet";
    private MaterialCalendarView calendarView;
    private RelativeLayout progressBarLayout;
    private JSONArray listdays;
    private RelativeLayout calendarViewLayout;

    public static String CURRENT_DATE = "currentDate";
    public static String CURRENT_POSITION_CUSTOMER = "positon";

    private String currentDate;
    private String currentMoth;

    private String currentYear;
    private int currentPosition;
    private CustomerItem customerItem;

    private final String parttenDate = "yyyy-MM-dd";
    private final String parttenDecimal = "00";
    private final String parttenDateTime = "yyyy-MM-dd HH:mm";
    private final DecimalFormat decimalFormat = new DecimalFormat(parttenDecimal);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(parttenDate);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(parttenDateTime);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_bottom_sheet);
        setLayout();
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        try {
            Date givenDate = dateFormat.parse(currentDate);
            getDateDetected(givenDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = LanguageConfig.changeLanguage(newBase, LanguageManager.getInstance().getValue("datetime_picker"));
        super.attachBaseContext(context);
    }
    private void init() {
        this.currentDate = getIntent().getStringExtra(CURRENT_DATE);
        this.currentMoth = currentDate.split("-")[1];
        this.currentYear = currentDate.split("-")[0];
        this.currentPosition = getIntent().getIntExtra(CURRENT_POSITION_CUSTOMER, -1);
//        ApplicationService.
        if (getIntent().getStringExtra(ListDates.TYPE).equals(ListDates.CUSTOMER_HISTORY)){
            this.customerItem = ApplicationService.customerItems.get(this.currentPosition);
        }else  if (getIntent().getStringExtra(ListDates.TYPE).equals(ListDates.EVENT_IN_DAY)){
            this.customerItem = EventFaceInDays.customerInDays.get(currentPosition);
        }

        this.calendarView = findViewById(R.id.calendarView);
        this.progressBarLayout = findViewById(R.id.progressBarLayout);
        this.calendarViewLayout = findViewById(R.id.calendarViewLayout);

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
        this.calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        LanguageConfig.changeLanguage(getApplicationContext(), LanguageManager.getInstance().getValue("datetime_picker"));
    }
    private void action(){
        this.calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                getDateDetected(date.getDate());
            }
        });
        this.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String dateSelected = dateFormat.format(date.getDate());
                if(isFounded(dateSelected)){
                    Intent dataBack = new Intent();
                    dataBack.putExtra(ListDates.SELECTED_DATE, dateSelected);
                    setResult(RESULT_OK, dataBack);
                    finish();
                }else{
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("no_infor"), true);
                }
            }
        });

    }
    private boolean isFounded(String dateSelected){
        for(int i = 0; i < listdays.length(); i++){
            try {
                String dateIndex = listdays.get(i).toString().split("T")[0];
                if(dateIndex.equals(dateSelected)){
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }
    private void getDateDetected(Date givenDate){
        pending();
        Calendar cal = Calendar.getInstance();
        cal.setTime(givenDate);
        int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDate = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        int currentMoth = cal.get(Calendar.MONTH) +1;
        int currentYear = cal.get(Calendar.YEAR);
        String timeStart = currentYear + "-" + currentMoth + "-" + firstDate + "T00:00+07:00";
        String timeEnd = currentYear + "-" + currentMoth + "-" +  lastDate + "T23:59+07:00";
        ApplicationService.requestManager.getListCustomerEvent(
                getCustomerEvent(customerItem, timeStart, timeEnd),
                ApplicationService.URL_GET_CUSTOMER_EVENTS
        );
    }
    private JSONObject getCustomerEvent(CustomerItem customerItem, String fromDate, String toDate){
        JSONObject payload = new JSONObject();
        try {
            payload.put("profileId", customerItem.getCustomerId());
            payload.put("customerTypeId", String.valueOf(customerItem.getCustomerTypeId()));
            payload.put("isActive", 1);
            payload.put("appIdentity", ApplicationService.customerrecognize);
            payload.put("fromDate", fromDate);
            payload.put("toDate", toDate);
            payload.put("pageIndex", 1);
            payload.put("pageSize", 24);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return payload;
    }
    private void setDetectedCaledar(JSONArray listDays){
        HashSet<CalendarDay> listDaysMap = new HashSet<>();
        try {
            for(int i=0; i < listDays.length(); i++){
                String date = listDays.get(i).toString().split("T")[0];
                int day = Integer.parseInt(date.split("-")[2]);
                int month = Integer.parseInt(date.split("-")[1]);
                int year = Integer.parseInt(date.split("-")[0]);
                listDaysMap.add(CalendarDay.from(year, month - 1, day));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.calendarView.addDecorator(new EventDecorator(getApplicationContext().getResources().getColor(R.color.mainColor), listDaysMap, getApplicationContext()));
    }
    private void pending(){
        this.calendarView.setEnabled(false);
        this.calendarView.setAlpha(.5f);
        this.calendarView.setClickable(false);
        this.progressBarLayout.setVisibility(View.VISIBLE);
    }
    private void unPending(){
        this.calendarView.setEnabled(true);
        this.calendarView.setAlpha(1);
        this.calendarView.setClickable(true);
        this.progressBarLayout.setVisibility(View.GONE);
    }

    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_CUSTOMER_EVENTS_SUCCESS:
                    try {
                        listdays = new JSONArray(message.obj.toString());
                        setDetectedCaledar(listdays);
                        unPending();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ApplicationService.GET_CUSTOMER_EVENTS_FAIL:
                    ApplicationService.showToast("Get customer event fail", true);
                    unPending();
                    break;
            }
        }
    }
}