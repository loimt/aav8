package com.bkav.aiotcloud.screen.notify;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.FilterCustomer;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NotifyFragment extends Fragment {
    private RecyclerView listNotify;
    private NotifyAdapter notifyAdapter;
    private ProgressBar progressBar;
    private int currentPageindex = 1;
    private String TAG = "NotifyFragment";
    private PullRefreshLayout refreshLayout;
    private ImageView filterImage;
    private TextView title;
    private RelativeLayout filter;
    private static final long MILLIS_IN_TWO_DAY = 1000 * 60 * 60 * 24 * 6; // 7 ngay
    private ImageView noData;
    private Parcelable recyclerViewState;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragview = inflater.inflate(R.layout.notify_fragment, container, false);
        init(fragview);
        setData();
        action();
        return fragview;
    }

    private void init(View fragview) {
        ApplicationService.startTime = getTimeStart();
        ApplicationService.endTime = getTimeEnd();
        this.listNotify = fragview.findViewById(R.id.listNotify);
        this.progressBar = fragview.findViewById(R.id.progressBar);
        this.refreshLayout = fragview.findViewById(R.id.refreshLayout);
        this.title = fragview.findViewById(R.id.title);
        this.filter = fragview.findViewById(R.id.filter);
        this.filter.setVisibility(View.VISIBLE);
        this.filterImage = fragview.findViewById(R.id.filterIm);
        this.noData = fragview.findViewById(R.id.no_data);
        createPlayload(ApplicationService.startTime, ApplicationService.endTime, 0);
    }

    private void setData() {
        this.listNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        this.notifyAdapter = new NotifyAdapter(getContext(), ApplicationService.notifyItems);
        this.listNotify.setAdapter(notifyAdapter);
        this.refreshLayout.setRefreshing(false);
        this.title.setText(LanguageManager.getInstance().getValue("notification"));
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = listNotify.getLayoutManager().onSaveInstanceState();
    }

    private void action() {
        this.refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data to your RecyclerView
                refresh(ApplicationService.startTime, ApplicationService.endTime);
            }
        });
        this.listNotify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {//1 for down
                    loadmore();
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    //top end reached
                    Log.e(TAG, "load top");
                }
                if (dy > 0) {
                    Log.e(TAG, "load down");
                }
            }
        });
        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FilterNotifyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUI() {
        if (notifyAdapter != null){
            this.notifyAdapter.notifyDataSetChanged();
            this.progressBar.setVisibility(View.GONE);
            this.refreshLayout.setRefreshing(false);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        setData();
        if(ApplicationService.notifyItems.size() == 0){
            setNodata();
        }else{
            unSetNoData();
        }
        if (recyclerViewState != null) {
            Objects.requireNonNull(listNotify.getLayoutManager()).onRestoreInstanceState(recyclerViewState);
        }
    }

    public void setNodata() {
        this.noData.setVisibility(View.VISIBLE);
    }
    public void unSetNoData() {
        this.noData.setVisibility(View.GONE);
    }

    private void createPlayload(String getTimeStart, String getTimeEnd, int lastID) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("fromDate", getTimeStart);
            payload.put("toDate", getTimeEnd);
            payload.put("applicationId", ApplicationService.applicationId);
            payload.put("pageSize", 20); // default items return
            payload.put("pageIndex", currentPageindex);
            payload.put("listCameraId", ApplicationService.listCameraId);
            payload.put("optionTime", 1);
            payload.put("eventId", lastID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getListNotify(payload, ApplicationService.URL_GET_LIST_NOTIFY);
    }

    private void refresh(String getTimeStart, String getTimeEnd) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("fromDate", getTimeStart + "+7:00");
            payload.put("toDate", getTimeEnd + "+7:00");
            payload.put("listCameraId", ApplicationService.listCameraId);
            payload.put("pageSize", 10);
            payload.put("pageIndex", currentPageindex);
            payload.put("optionTime", 1);
            payload.put("applicationId", ApplicationService.applicationId);
            payload.put("eventId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.refreshNotify(payload, ApplicationService.URL_GET_LIST_NOTIFY);
    }


    private String getTimeEnd() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + " 23:59";
        return date;
    }

    private String getTimeStart() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date oneDayBefore = new Date(date.getTime() - MILLIS_IN_TWO_DAY);
        String result = dateFormat.format(oneDayBefore) + " 00:00";
        return result;
    }

    private void loadmore() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.currentPageindex++;
        createPlayload(ApplicationService.startTime, ApplicationService.endTime, ApplicationService.notifyItems.get(ApplicationService.notifyItems.size() - 1).getEventId());
    }
}