package com.bkav.aiotcloud.screen.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.AccessdetectItem;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.entity.notify.NotifyItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.notify.NotifyAdapter;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.FilterCustomer;
import com.bkav.aiotcloud.screen.setting.face.historyObject.ListDates;
import com.bkav.aiotcloud.screen.widget.adapter.AccessDetectAdapter;
import com.bkav.aiotcloud.screen.widget.adapter.CustomerIndayAdapter;
import com.bkav.aiotcloud.view.RecyclerViewMargin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventAccessdetectIndays extends AppCompatActivity {
    public static ArrayList<AccessdetectItem> accessdetectItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.access_detect_in_day);
        init();
        setData();
        EventAccessdetectIndays.accessdetectItems.clear();
        ApplicationService.requestManager.getListIntruIndayDays(payloadStatistic(), ApplicationService.URL_GET_LIST_STATISTIC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    private RecyclerView listNotify;

    private RelativeLayout backIm;
    private AccessDetectAdapter notifyAdapter;
    private static final long MILLIS_IN_TWO_DAY = 1000 * 60 * 60 * 24; // 1 ngay
    private ImageView noData;

    private TextView title;
    private ProgressBar progressBar;
    private Parcelable recyclerViewState;
    private int currentPageindex = 1;

    //    private PullRefreshLayout refreshLayout;
    private void init() {
        ApplicationService.startTime = getTimeStart();
        ApplicationService.endTime = getTimeEnd();
        this.listNotify = findViewById(R.id.listNotify);
        this.progressBar = findViewById(R.id.progressBar);
        this.title = findViewById(R.id.title);
        this.noData = findViewById(R.id.no_data);
        this.backIm = findViewById(R.id.backIm);

//        this.refreshLayout = findViewById(R.id.refreshLayout);

//        payloadStatistic();
    }

    private void setData() {
        this.listNotify.setLayoutManager(new LinearLayoutManager(this));
        this.notifyAdapter = new AccessDetectAdapter(this, accessdetectItems);
        this.listNotify.setAdapter(notifyAdapter);
        this.title.setText(LanguageManager.getInstance().getValue("notification"));
//        this.refreshLayout.setRefreshing(false);
        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        this.refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Load data to your RecyclerView
////                refresh();
//            }
//        });

        this.listNotify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {//1 for down
                    loadmore();
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    //top end reached
//                    Log.e(TAG, "load top");
                }
                if (dy > 0) {
//                    Log.e(TAG, "load down");
                }
            }
        });
    }

    private String getTimeEnd() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + "T23:59:59+07:00";
        return date;
    }

    private String getTimeStart() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + "T00:00:00+07:00";
        return date;
    }

    private JSONObject payloadStatistic() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("appIdentity", ApplicationService.accessdetect);
            payload.put("pageSize", 20);
            payload.put("pageIndex", currentPageindex);
            payload.put("fromDate", getTimeStart());
            payload.put("toDate", getTimeEnd());
            payload.put("listCamera", "");
            payload.put("listDetectType", "");
            payload.put("showType", "day");
            payload.put("optionTime", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private void loadmore() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.currentPageindex++;
        ApplicationService.requestManager.getListIntruIndayDays(payloadStatistic(), ApplicationService.URL_GET_LIST_STATISTIC);
//        payloadStatistic();
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_ACCESS_DETECT_SUCESS:
                    progressBar.setVisibility(View.GONE);
                    notifyAdapter.notifyDataSetChanged();
                    break;

            }
        }
    }

}



