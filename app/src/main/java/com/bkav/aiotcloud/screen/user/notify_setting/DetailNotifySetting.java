package com.bkav.aiotcloud.screen.user.notify_setting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.entity.region.RegionGroup;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.LoginActiviry;
import com.bkav.aiotcloud.screen.user.SettingNotifyAdapter;
import com.bkav.aiotcloud.view.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;
import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;

public class DetailNotifySetting extends AppCompatActivity {
    private CustomEditText inputSearch;
    private TextView titleNotifySettingDetail;
    private SwitchCompat switchAll;
    private RecyclerView listNotifySetting;
    private SettingNotifyAdapter adapterNotifySetting;
    private RelativeLayout backIM;
    private String identity;
    private String TAG = "NotifySettingDetail";
    private TextView introContent;
    private TextView titleNotify;
    private RelativeLayout listNotifySettingLayout;
    private TextView list_device;
    private ImageView filterIM;
    private TreeNode root;
    private TreeView tView;
    private PullRefreshLayout pullLayout;


    private void init() {
        this.identity = getIntent().getStringExtra("identity_notifySetting");
        this.titleNotify = findViewById(R.id.titleNotify);
        this.introContent = findViewById(R.id.introContent);
        this.inputSearch = findViewById(R.id.searchNotifySetting);
        this.titleNotifySettingDetail = findViewById(R.id.titleDetailNotifySetting);
        this.switchAll = findViewById(R.id.swOnOffNotifyAll);
        this.listNotifySetting = findViewById(R.id.listNotifySetting);
        this.backIM = findViewById(R.id.backImAccessSetting);
        this.listNotifySettingLayout = findViewById(R.id.layoutListNotifySetting);
        this.list_device = findViewById(R.id.list_device);
        this.filterIM = findViewById(R.id.filterIM);
        this.pullLayout = findViewById(R.id.pullRefresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.access_notify_setting);
        init();
        setData();
        action();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ApplicationService.identitySetting != null){
            ApplicationService.identitySetting.isEmpty();
        }
        if(ApplicationService.notifySettingItems.size() > 0){
            ApplicationService.notifySettingItems.clear();
        }
        if(ApplicationService.regions.size() > 0){
            ApplicationService.regions.clear();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void action() {
        this.pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotifySetting(identity);
            }
        });

        this.filterIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        this.switchAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        JSONObject payload = new JSONObject();
                        try {
                            payload.put("identity", identity);
                            payload.put("data", new JSONArray() );
                            payload.put("isActive", isChecked);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApplicationService.requestManager.updateNotifySetting(payload, ApplicationService.URL_UPDATE_NOTIFY_SETTING);
                        if(isChecked){
                            ApplicationService.isActiveIdentitySetting = true;
                            adapterNotifySetting.notifyDataSetChanged();
                            listNotifySetting.setAlpha(1);
                        }else{
                            adapterNotifySetting.notifyDataSetChanged();
                            listNotifySetting.setAlpha(.5f);
                            ApplicationService.isActiveIdentitySetting = false;
                        }
                    }
                });
                return false;
            }
        });


        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ApplicationService.regions.size() > 0){
                    ApplicationService.regions.clear();
                }
                finish();
            }
        });

       this.inputSearch.setOnKeyListener(new View.OnKeyListener() {
           @Override
           public boolean onKey(View v, int keyCode, KeyEvent event) {
               if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == 66) { //SEARCH
                   searchNotify(identity, inputSearch.getText().toString());
                   return true;
               }
               return false;
           }
       });

       this.inputSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    getNotifySetting(identity);
                }
           }
       });

    }

    private void setData() {
        this.pullLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        this.introContent.setText(LanguageManager.getInstance().getValue("introNotifySetting"));
        this.inputSearch.setHint(LanguageManager.getInstance().getValue("search"));

        this.titleNotifySettingDetail.setText(LanguageManager.getInstance().getValue(identity));
        this.titleNotify.setText(LanguageManager.getInstance().getValue("allow_notify"));
        this.listNotifySetting.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.adapterNotifySetting = new SettingNotifyAdapter(getApplicationContext(), ApplicationService.notifySettingItems);
        this.listNotifySetting.setAdapter(adapterNotifySetting);
        this.list_device.setText(LanguageManager.getInstance().getValue("list_device"));
        if(identity.equals("customerrecognize")){
            this.filterIM.setVisibility(View.GONE);
            this.filterIM.setClickable(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getNotifySetting(identity);
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.requestManager.getAllRegions(ApplicationService.URL_GET_ALL_REGIONS);

    }

    private void searchNotify(String identityNotify, String notifyContent) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("identity", identityNotify);
            payload.put("customerTypeId", 0);
            payload.put("pageSize", 100);
            payload.put("pageIndex", 1);
            payload.put("search", notifyContent);
            payload.put("listRegion", "");
            payload.put("userId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getListNotifySetting(payload, ApplicationService.URL_GET_LIST_NOTIFY_SETTING);
    }
    private void getNotifySetting(String identityNotify) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("identity", identityNotify);
            payload.put("customerTypeId", 0);
            payload.put("pageSize", 100);
            payload.put("pageIndex", 1);
            payload.put("search", "");
            payload.put("listRegion", "");
            payload.put("userId", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getListNotifySetting(payload, ApplicationService.URL_GET_LIST_NOTIFY_SETTING);
    }
    private TreeNode generateTree(List<Region> listAll){
        TreeNode root = TreeNode.root();
        for (int i = 0; i < listAll.size(); i++){
            if(listAll.get(i).getParentId()==0){
                TreeNode treeNode = new TreeNode(listAll.get(i).getRegionName());
                treeNode.setLevel(0);
                root.addChild(treeNode);
            }
        }
        root.addChild(getChildren(root.getChildren().get(0), listAll));
        return root;
    }
    private TreeNode getChildren(TreeNode treeNode, List<Region> listAll) {
        for(int i =0; i < listAll.size(); i++){
            Region objectIndex = listAll.stream().filter(region -> treeNode.getValue().equals(region.getRegionName())).findAny().orElse(null);
            if(objectIndex.getId() == listAll.get(i).getParentId() && objectIndex.getLevel() + 1 == listAll.get(i).getLevel()){
                TreeNode childrenNode = new TreeNode(listAll.get(i).getRegionName());
                childrenNode.setLevel(listAll.get(i).getLevel() - 1);
                treeNode.addChild(getChildren(childrenNode,listAll));
            }
        }
        return treeNode;
    }

    private void openFilterDialog() {
        Dialog dialog = new Dialog(DetailNotifySetting.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_filter_notify_setting);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ViewGroup containerView = dialog.findViewById(R.id.container_treeview);
        tView = new TreeView(root, this, new MyNodeViewFactory());
        View view = tView.getView();
        containerView.addView(view);
        Button confirm = dialog.findViewById(R.id.confirmFilterNotifySetting);
        confirm.setText(LanguageManager.getInstance().getValue("confirm"));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder listRegionId = new StringBuilder();
                List<TreeNode> selectedNodes = tView.getSelectedNodes();
                for(int i = 0; i < selectedNodes.size(); i++){
                    String nameRegionIndex = selectedNodes.get(i).getValue().toString();
                    Region objectIndex = ApplicationService.regions.stream().filter(region -> nameRegionIndex.equals(region.getRegionName())).findAny().orElse(null);
                    listRegionId.append(objectIndex.getId() + ",");
                }
                StringBuffer listRegionString= new StringBuffer(listRegionId);
                if(listRegionString.length() > 0){
                    listRegionString.deleteCharAt(listRegionString.length()-1);
                }
                JSONObject payload = new JSONObject();
                try {
                    payload.put("identity", identity);
                    payload.put("customerTypeId", 0);
                    payload.put("pageSize", 100);
                    payload.put("pageIndex", 1);
                    payload.put("search", "");
                    payload.put("listRegion", listRegionString);
                    payload.put("userId", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                ApplicationService.requestManager.getListNotifySetting(payload, ApplicationService.URL_GET_LIST_NOTIFY_SETTING);
            }
        });
    }


    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_GET_LIST_NOTIFYSETTING_SUCCESS:
                    pullLayout.setRefreshing(false);
                    switchAll.setChecked(ApplicationService.isActiveIdentitySetting);
                    if(ApplicationService.isActiveIdentitySetting){
                        listNotifySetting.setAlpha(1);
                    }else{
                        listNotifySetting.setAlpha(.5f);
                    }
                    adapterNotifySetting.notifyDataSetChanged();
                    break;
                case ApplicationService.MESSAGE_GET_LIST_NOTIFYSETTING_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("get_list_notifysetting_fail"), true);
                    break;
                case ApplicationService.MESSAGE_UPDATE_NOTIFYSETTING_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("update_notifysetting_fail"), true);
                    break;
                case ApplicationService.MESSAGE_UPDATE_NOTIFYSETTING_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("update_notifysetting_sucess"), false);
                    break;
                case ApplicationService.GET_ALL_REGIONS_SUCCESS:
                    root = generateTree(ApplicationService.regions);
                    root.removeChild(root.getChildren().get(0));
                    break;
                case ApplicationService.GET_ALL_REGIONS_FAIL:
                    ApplicationService.showToast("Lỗi lấy thông tin region", true);
                    break;
            }
        }
    }
}