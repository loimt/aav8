package com.bkav.aiotcloud.screen.setting.license;

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
import com.bkav.aiotcloud.entity.license.LicenseGroupItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileLicenseManage  extends AppCompatActivity {
    public static final String TYPE = "type";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String FILTER = "filter";
    public static final String ID = "id";
    public static String LICENSE = "LICENSE";

    public static String GROUP = "GROUP";

    public static String STATUS = "STATUS";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.license_profile_list);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ApplicationService.licenseItems.clear();
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.licenseGroupItems.clear();
        ApplicationService.volleyRequestManagement.getListLicenseGroup("true", "");
    }

    private LicenseAdapter licenseAdapter;
    private RecyclerView listGroupLicense;

    private String licenseCurrent = "";
    private TextView title;
    private RelativeLayout backIm;
    private ImageView addIM;
    private RelativeLayout filter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private int currentStatus = 1;
    private int currentPageIndex = 1;
    private String licenseName = "";

    private int currentGroupPos = -1;


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
                            currentStatus = data.getIntExtra(STATUS, -1);
                            licenseName = data.getStringExtra(LICENSE);
                            Log.e("searchLicense", licenseName);
                            currentGroupPos = data.getIntExtra(GROUP,-1);
                        }
                        ApplicationService.licenseItems.clear();
                        getListLicenseDefaul();
                    }
                }
            });


    private void init() {
        this.listGroupLicense = findViewById(R.id.gridview);
        this.listGroupLicense.setLayoutManager(new GridLayoutManager(getApplication(), 1));
        this.licenseAdapter = new LicenseAdapter(this, ApplicationService.licenseItems);
        this.listGroupLicense.setAdapter(this.licenseAdapter);
        this.refreshLayout = findViewById(R.id.refreshLayout);
        this.refreshLayout.setRefreshing(false);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);
        this.addIM = findViewById(R.id.addIM);
        this.addIM.setVisibility(View.VISIBLE);
        this.filter = findViewById(R.id.filter);
        this.filter.setVisibility(View.VISIBLE);
        this.progressBar = findViewById(R.id.progressBar);
        this.title.setText(LanguageManager.getInstance().getValue("license_plate_list"));

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
                getListLicenseDefaul();
            }
        });

        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), FilterLicense.class);
                intent.putExtra(STATUS, currentStatus);
                intent.putExtra(GROUP, currentGroupPos);
                intent.putExtra(LICENSE, licenseName);
                startActivityIntent.launch(intent);
            }
        });

        this.addIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), AddLicense.class);
//                Intent intent = new Intent(getApplication(), TestCropImage.class);
                intent.putExtra(TYPE, NEW);
                startActivityIntent.launch(intent);
            }
        });

        licenseAdapter.setClickListener(new ListDayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                LicenseItem licenseItem = ApplicationService.licenseItems.get(position);
                Intent intent = new Intent(getApplication(), AddLicense.class);
                intent.putExtra(TYPE, EDIT);
                intent.putExtra(ID, position);
                startActivityIntent.launch(intent);
            }
        });

//        getListLicenseDefaul();

        listGroupLicense.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    currentPageIndex++;
                    getListLicenseDefaul();

                }
            }
        });

        JSONObject payload = new JSONObject();
        ApplicationService.licenseItems.clear();
        try {
            payload.put("licensePlateTypeGuid", "");
            payload.put("currentPage", 1);
            payload.put("pageSize", 24);
            payload.put("searchText", "");
            payload.put("isActive", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.volleyRequestManagement.getListLicense(payload, ApplicationService.URL_GET_LIST_LICENSE);

    }

    private void getListLicenseDefaul() {
        this.progressBar.setVisibility(View.VISIBLE);
        JSONObject payload = new JSONObject();
        try {

            if (currentStatus == -1) {
                payload.put("isActive", JSONObject.NULL);
            } else if (currentStatus == 1){
                payload.put("isActive", true);
            }else if (currentStatus == 0){
                payload.put("isActive", false);
            }
            if (currentGroupPos == -1){
                payload.put("licensePlateTypeGuid", "");
            }else {
                payload.put("licensePlateTypeGuid", ((LicenseGroupItem)ApplicationService.licenseGroupItems.get(currentGroupPos)).getObjectGuid());

            }

            payload.put("searchText", licenseName);
            payload.put("pageSize", 24);
            payload.put("currentPage", currentPageIndex);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApplicationService.volleyRequestManagement.getListLicense(payload, ApplicationService.URL_GET_LIST_LICENSE);
    }

    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_LICENSE_SUCCESS:
                    licenseAdapter.notifyDataSetChanged();
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
