package com.bkav.aiotcloud.screen.widget;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.FilterCustomer;
import com.bkav.aiotcloud.screen.setting.face.historyObject.ListDates;
import com.bkav.aiotcloud.screen.widget.adapter.CustomerIndayAdapter;
import com.bkav.aiotcloud.view.RecyclerViewMargin;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventFaceInDays extends AppCompatActivity {
    public static ArrayList<CustomerItem> customerInDays = new ArrayList<>();
    public static final String TYPE = "type";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String FILTER = "filter";
    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String SEX = "sex";
    public static final String LIST_TYPE = "listtype";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.list_customer);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        createPramRequest();
    }

    private CustomerIndayAdapter customerAdapter;
    private RecyclerView listCustomer;
    private TextView title;
    private RelativeLayout backIm;
    private ImageView addIM;
    private RelativeLayout filter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private int currentGender = -1;
    private int currentShowType = 1;
    private int currentPageIndex = 1;
    private String currentListID = "";


    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        currentPageIndex = 1;
                        Intent data = result.getData();
                        if (data.getStringExtra(TYPE).equals(FILTER)) {
                            currentShowType = data.getIntExtra(STATUS, -1);
                            currentGender = data.getIntExtra(SEX, 1);
                            currentListID = data.getStringExtra(LIST_TYPE).replace(" ", "");
                        }
                        ApplicationService.customerItems.clear();
//                        getListCustomerDefaul();
                    }
                }
            });


    private void init() {
        this.listCustomer = findViewById(R.id.gridview);
        this.listCustomer.setLayoutManager(new GridLayoutManager(getApplication(), 2));
        this.customerAdapter = new CustomerIndayAdapter(this, customerInDays);
        this.listCustomer.setAdapter(this.customerAdapter);
        this.refreshLayout = findViewById(R.id.refreshLayout);
        this.refreshLayout.setRefreshing(false);
        RecyclerViewMargin decoration = new RecyclerViewMargin(35, 2);
        listCustomer.addItemDecoration(decoration);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);
        this.addIM = findViewById(R.id.addIM);
        this.filter = findViewById(R.id.filter);
        this.progressBar = findViewById(R.id.progressBar);
        this.title.setText(LanguageManager.getInstance().getValue("face_id_list"));

        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data to your RecyclerView
                refreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.VISIBLE);
//                getListCustomerDefaul();
            }
        });

        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), FilterCustomer.class);
                intent.putExtra(SEX, currentGender);
                intent.putExtra(STATUS, currentShowType);
                intent.putExtra(LIST_TYPE, currentListID);
                startActivityIntent.launch(intent);
            }
        });

//        this.addIM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplication(), AddCustomerActivity.class);
//                intent.putExtra(TYPE, NEW);
//                startActivityIntent.launch(intent);
//            }
//        });

        customerAdapter.setClickListener(new ListDayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), ListDates.class);
                intent.putExtra(ListDates.POSITION_KEY, position);
                intent.putExtra(ListDates.TYPE, ListDates.EVENT_IN_DAY);
                startActivity(intent);
            }
        });

//        getListCustomerDefaul();

        listCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    currentPageIndex++;
//                    getListCustomerDefaul();
                }
            }
        });

//        JSONObject typeCustomer = new JSONObject();
////        ApplicationService.customerItems.clear();
//        try {
//            typeCustomer.put("customerTypeId", JSONObject.NULL);
//            typeCustomer.put("identity", JSONObject.NULL);
//            typeCustomer.put("showType", 1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ApplicationService.requestManager.getListTypeCustomer(typeCustomer, ApplicationService.URL_GET_ALL_TYPE_CUSTOMER);

    }

    private void getListCustomerDefaul() {
        this.progressBar.setVisibility(View.VISIBLE);
        JSONObject payload = new JSONObject();
        try {
            if (currentListID.length() == 0) {
                payload.put("cusTypeId", JSONObject.NULL);
            } else {
                payload.put("cusTypeId", currentListID);
            }
            if (currentGender == -1) {
                payload.put("gender", JSONObject.NULL);
            } else {
                payload.put("gender", currentGender);
            }
            if (currentShowType == -1) {
                payload.put("showType", JSONObject.NULL);
            } else {
                payload.put("showType", currentShowType);

            }
            payload.put("pageSize", 10);
            payload.put("IsUnknow", 0);
            payload.put("OrderType", 0);
            payload.put("pageIndex", currentPageIndex);
            Log.e(this.getPackageName(), payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApplicationService.requestManager.getListCustomer(payload, ApplicationService.URL_GET_ALL_CUSTOMER);
    }
    private void createPramRequest(){
        @SuppressLint("SimpleDateFormat") DateFormat today = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
//        Log.e("today",  today.format(date));


        JSONObject payload = new JSONObject();
        try {
            payload.put("fromDate",   today.format(date) +"T00:00:00+07:00");
            payload.put("toDate",today.format(date) +"T23:59:59+07:00");

            payload.put("appIdentity", "customerrecognize");
            payload.put("pageIndex", 1);
            payload.put("pageSize", "24");
            payload.put("listRegionId", null);
            payload.put("isActive", 1);
            payload.put("IsUnknow", 0);
            payload.put("optionTime", 99);
            payload.put("showType", "day");
            ApplicationService.volleyRequestManagement.getEventsInDay(payload, ApplicationService.URL_GET_DETAIL_EVENT_LAST_DAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        UrlRequestCallback.OnFinishRequest callbackInfo = getListCamCallbackActions();
//        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
//        NetworkRequest.post(url, payload.toString().replace("\"[", "[").replace("]\"","]"), requestCallback);
    }


    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_CUSTOMER_SUCCESS:
                    customerAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    String er = (String) message.obj;
                    ApplicationService.showToast(er, true);
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }
}


