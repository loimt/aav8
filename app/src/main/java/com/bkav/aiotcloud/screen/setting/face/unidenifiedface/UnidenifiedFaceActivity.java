package com.bkav.aiotcloud.screen.setting.face.unidenifiedface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.AddOjectFace;
import com.bkav.aiotcloud.screen.setting.face.FaceObjectAdapter;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity;
import com.bkav.aiotcloud.screen.setting.face.customer.AddCustomerActivity;
import com.bkav.aiotcloud.screen.setting.face.customer.CustomerAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.FilterCustomer;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.screen.setting.face.edit.InfoAIScreen;
import com.bkav.aiotcloud.view.CustomEditText;
import com.bkav.aiotcloud.view.GridviewCustom;
import com.bkav.aiotcloud.view.RecyclerViewMargin;

import org.json.JSONException;
import org.json.JSONObject;

public class UnidenifiedFaceActivity extends AppCompatActivity {
    public static final String TYPE = "type";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String UNKNOW = "unkow";
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
    }

    private AdapterUnknowCustomer customerAdapter;
    private RecyclerView listCustomer;
    private TextView title;
    private RelativeLayout backIm;

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
                        getListCustomerDefaul();
                    }
                }
            });


    private void init() {
        this.listCustomer = findViewById(R.id.gridview);
        this.listCustomer.setLayoutManager(new GridLayoutManager(getApplication(), 2));
        this.customerAdapter = new AdapterUnknowCustomer(this, ApplicationService.customerItems);
        this.listCustomer.setAdapter(this.customerAdapter);
        this.refreshLayout = findViewById(R.id.refreshLayout);
        this.refreshLayout.setRefreshing(false);
        RecyclerViewMargin decoration = new RecyclerViewMargin(35, 2);
        listCustomer.addItemDecoration(decoration);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);
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
                getListCustomerDefaul();
            }
        });


        customerAdapter.setClickListener(new ListDayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomerItem customerItem = ApplicationService.customerItems.get(position);
                Intent intent = new Intent(getApplication(), AddCustomerActivity.class);
                intent.putExtra(TYPE, UNKNOW);
                intent.putExtra(ID, position);
//                intent.putExtra("image", customerItem.getAvatarFilePath());
                startActivityIntent.launch(intent);
            }
        });

        getListCustomerDefaul();

        listCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    currentPageIndex++;
                    getListCustomerDefaul();
                }
            }
        });

        JSONObject typeCustomer = new JSONObject();
        ApplicationService.customerItems.clear();
        try {
            typeCustomer.put("customerTypeId", JSONObject.NULL);
            typeCustomer.put("identity", JSONObject.NULL);
            typeCustomer.put("showType", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getListTypeCustomer(typeCustomer, ApplicationService.URL_GET_ALL_TYPE_CUSTOMER);

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
            payload.put("IsUnknow", 1);
            payload.put("OrderType", 0);
            payload.put("pageIndex", currentPageIndex);
            Log.e(this.getPackageName(), payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApplicationService.requestManager.getListCustomer(payload, ApplicationService.URL_GET_ALL_CUSTOMER);
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
