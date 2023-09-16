package com.bkav.aiotcloud.screen.setting.face.historyObject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.screen.notify.detail.NotifyDetailScreen;
import com.bkav.aiotcloud.screen.widget.EventFaceInDays;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListDates extends AppCompatActivity {
    private static final String TAG = "ListDates";
    public static final String TYPE = "type";
    public static final String CUSTOMER_HISTORY = "cutomer";
    public static final String EVENT_IN_DAY = "event_in_day";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_list_dates);
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        if(ApplicationService.customerEvents.size() == 0){
            ApplicationService.requestManager.getListCustomerEvent(
                    getCustomerEvent(currentCustomer, getTimeStart(), getTimeEnd()),
                    ApplicationService.URL_GET_CUSTOMER_EVENTS
            );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationService.customerEvents.clear();
    }

    private void setData(){
        this.title.setText(dateNow);
        this.progressBar.setVisibility(View.VISIBLE);
        this.pullLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        this.listDatesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.listDatesView.setAdapter(listDateAdapter);
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationService.customerEvents.clear();
                finish();
            }
        });
        this.pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApplicationService.requestManager.getListCustomerEvent(
                        getCustomerEvent(currentCustomer, getTimeStart(), getTimeEnd()),
                        ApplicationService.URL_GET_CUSTOMER_EVENTS
                );
                title.setText(dateNow);
            }
        });
        this.listDateAdapter.setOnClick(new ListDateAdapter.ItemClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), NotifyDetailScreen.class);
                if (getIntent().getStringExtra(TYPE).equals(CUSTOMER_HISTORY)){
                    intent.putExtra(NotifyDetailScreen.TYPE, "2");
                    intent.putExtra(NotifyDetailScreen.ID, ApplicationService.customerEvents.get(position).getEventId());
                    intent.putExtra(NotifyDetailScreen.CURRENT_POSITION, currentPosition);
                } else  if (getIntent().getStringExtra(TYPE).equals(EVENT_IN_DAY)){
                    intent.putExtra(NotifyDetailScreen.TYPE, "3");
                    intent.putExtra(NotifyDetailScreen.ID, ApplicationService.customerEvents.get(position).getEventId());
                    intent.putExtra(NotifyDetailScreen.CURRENT_POSITION, currentPosition);
                }
                startActivityIntent.launch(intent);
            }
        });
        this.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarBottomSheet.class);
                intent.putExtra(CalendarBottomSheet.CURRENT_DATE, getTimeStart().split("T")[0]);
                intent.putExtra(CalendarBottomSheet.CURRENT_POSITION_CUSTOMER, currentPosition);
                intent.putExtra(TYPE, getIntent().getStringExtra(TYPE));
                startActivityIntent.launch(intent);
            }
        });
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
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String selectedDate = data.getStringExtra(SELECTED_DATE);
                        String timeStart = selectedDate + "T00:00+07:00";
                        String timeEnd = selectedDate + "T23:59+07:00";
                        Log.e(TAG, "onActivityResult: " + getCustomerEvent(currentCustomer, timeStart, timeEnd));
                        ApplicationService.requestManager.getListCustomerEvent(
                                getCustomerEvent(currentCustomer, timeStart, timeEnd),
                                ApplicationService.URL_GET_CUSTOMER_EVENTS
                        );
                        String titleNew = selectedDate.split("-")[2] + "-" + selectedDate.split("-")[1] + "-" + selectedDate.split("-")[0];
                        title.setText(titleNew);
                    }
                }
            });
    private String getTimeEnd() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + "T23:59+07:00";
        return date;
    }

    private String getTimeStart() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + "T00:00+07:00";
        return date;
    }

    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_CUSTOMER_EVENTS_SUCCESS:
                    Log.e(TAG, "GET_CUSTOMER_EVENTS_SUCCESS: " + ApplicationService.customerEvents.size());
                    pullLayout.setRefreshing(false);
                    listDateAdapter.notifyDataSetChanged();
                    if(ApplicationService.customerEvents.size() == 0){
                        noData.setVisibility(View.VISIBLE);
                    }else{
                        noData.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                    break;
                case ApplicationService.GET_CUSTOMER_EVENTS_FAIL:
                    ApplicationService.showToast("Get customer event fail", true);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }
    private void init(){
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        this.dateNow = df.format(Calendar.getInstance().getTime());
        this.listDateAdapter = new ListDateAdapter(getApplicationContext(), ApplicationService.customerEvents);
        this.currentPosition = getIntent().getIntExtra(POSITION_KEY,-1);
        if (getIntent().getStringExtra(TYPE).equals(CUSTOMER_HISTORY)){
            this.currentCustomer = ApplicationService.customerItems.get(currentPosition);
        } else  if (getIntent().getStringExtra(TYPE).equals(EVENT_IN_DAY)){
            this.currentCustomer = EventFaceInDays.customerInDays.get(currentPosition);
        }

        this.title = findViewById(R.id.title);
        this.back = findViewById(R.id.back);
        this.calendar = findViewById(R.id.calendarViewLayout);
        this.pullLayout = findViewById(R.id.pullRefresh);
        this.listDatesView = findViewById(R.id.listDates);
        this.noData = findViewById(R.id.no_data);
        this.progressBar = findViewById(R.id.progressBar);
    }

    public static String POSITION_KEY = "customerTypeID";
    public static String SELECTED_DATE = "slectDate";

    private int currentPosition;
    private CustomerItem currentCustomer;
    private ListDateAdapter listDateAdapter;
    private String dateNow;


    private TextView title;
    private RelativeLayout back;
    private RelativeLayout calendar;
    private PullRefreshLayout pullLayout;
    private RecyclerView listDatesView;
    private ImageView noData;
    private ProgressBar progressBar;
}