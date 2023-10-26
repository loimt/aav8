package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.view.GridviewCustom;

import org.json.JSONException;
import org.json.JSONObject;

public class ListTypeAI extends AppCompatActivity {
    public static final String PROFILE = "PROFILE";
    public static final String FACE = "FACE";
    public static final String LICENSE = "LICENSE";




    public static final String TYPE = "type";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String ID = "id";
    public static final String FILTER = "filter";
    public static final String TYPE_SHOW = "show_type";
    public static final String STATUS = "status";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        currentProfile = getIntent().getStringExtra(PROFILE);
        setContentView(R.layout.type_customer_screen);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    //    private CameraAdapter cameraAdapter;
    private GridviewCustom gridview;
    private RelativeLayout backIM;
    private TextView title;

    private String currentProfile = "";

    private RelativeLayout filter;
    private TypeAIAdapter typeAIAdapter;
    private int currentStatus = 1;
    private String currentIdentity = "all";


    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getStringExtra(TYPE).equals(FILTER)){
                            currentStatus = data.getIntExtra(STATUS, 1);
                            currentIdentity = data.getStringExtra(TYPE_SHOW);

                            JSONObject type = new JSONObject();
                            try {
                                type.put("customerTypeId", JSONObject.NULL);
                                if (currentStatus == -1) {
                                    type.put("showType", JSONObject.NULL);
                                } else {
                                    type.put("showType", currentStatus);
                                }
                                if (currentIdentity.equals("all")) {
                                    type.put("identity",  JSONObject.NULL);
                                } else {
                                    type.put("identity", currentIdentity);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ApplicationService.requestManager.getListTypeCustomer(type, ApplicationService.URL_GET_ALL_TYPE_CUSTOMER);

                        } else {
                            ApplicationService.typeCustomerItems.clear();
                            getListTypeCustomerDefault();
                        }

                    }
                }
            });

    private void init() {
        this.gridview = findViewById(R.id.gridview);
        this.title = findViewById(R.id.title);
        this.backIM = findViewById(R.id.backIm);
        if (currentIdentity.equals(FACE)){
            this.typeAIAdapter = new TypeAIAdapter(this, ApplicationService.typeCustomerItems);
        } else {
            this.typeAIAdapter = new TypeAIAdapter(this, ApplicationService.licenseGroupItems);
        }
        this.gridview.setAdapter(this.typeAIAdapter);
        this.filter = findViewById(R.id.filter);

        ImageView addIM = findViewById(R.id.addIM);

        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), FilterTypeCustomer.class);
                intent.putExtra(FilterTypeCustomer.STATUS, currentStatus);
                intent.putExtra(FilterTypeCustomer.IDENTITY, currentIdentity);
                startActivityIntent.launch(intent);
            }
        });

        this.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplication(), DetailTypeObject.class);
                intent.putExtra(PROFILE, currentProfile);
                intent.putExtra(TYPE, EDIT);
                intent.putExtra(ID, position);
                startActivityIntent.launch(intent);
            }
        });

        addIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), DetailTypeObject.class);
                intent.putExtra(PROFILE, currentProfile);
                intent.putExtra(TYPE, NEW);
                startActivityIntent.launch(intent);
            }
        });

        this.title.setText(LanguageManager.getInstance().getValue("manage_face_group"));

      getListTypeCustomerDefault();
    }

    private void getListTypeCustomerDefault(){
        JSONObject type = new JSONObject();
        try {
            type.put("customerTypeId", JSONObject.NULL);
            if (currentIdentity.equals("all")){
                type.put("identity", JSONObject.NULL);
            } else {
                type.put("identity", currentIdentity);
            }
            type.put("showType", currentStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getListTypeCustomer(type, ApplicationService.URL_GET_ALL_TYPE_CUSTOMER);
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_TYPE_CUSTOMER_SUCCESS:
                    typeAIAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}
