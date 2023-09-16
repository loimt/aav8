package com.bkav.aiotcloud.screen.home.optioncam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewAdapter;
import com.amrdeveloper.treeview.TreeViewHolderFactory;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.MainScreen;
import com.bkav.aiotcloud.screen.user.notify_setting.DetailNotifySetting;
import com.bkav.aiotcloud.screen.user.notify_setting.MyNodeViewFactory;
import com.bkav.aiotcloud.util.MapIdentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditCamProfile extends AppCompatActivity {
    private static final String TAG = "EditCamProfile";
    private TextView title;
    private ImageView avatarCam;
    private TextView camNameTx;
    private EditText cameNameInput;
    private TextView regionTx;
    private EditText regionInput;
    private Button save;
    private RelativeLayout back;
    public static final String TYPE = "type";
    public static final String OBJECTGUID = "objectID";
    public static final String MODEL = "model";
    public static final String CAMNAME = "camname";
    public static final String REGION_ID = "regionname";
    private String objectID;
    private String model;
    private String camName;
    private String regionId;
    private String type;
    private TreeViewAdapter treeViewAdapter;
    private List<TreeNode> root = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_cam_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        setData();
        action();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(root.size() == 0){
            ApplicationService.requestManager.getAllRegions(ApplicationService.URL_GET_ALL_REGIONS);
        }
        ApplicationService.mainHandler = new MainHandler();
    }

    private void init(){
        Intent intent = getIntent();
        this.type = intent.getStringExtra(TYPE);
        this.objectID = intent.getStringExtra(OBJECTGUID);
        this.model = intent.getStringExtra(MODEL);
        this.camName = intent.getStringExtra(CAMNAME);
        this.regionId = intent.getStringExtra(REGION_ID);
        this.title = findViewById(R.id.title);
        this.avatarCam = findViewById(R.id.avatarCam);
        this.camNameTx = findViewById(R.id.camNameTx);
        this.cameNameInput = findViewById(R.id.camNameInput);
        this.regionTx = findViewById(R.id.regionTx);
        this.regionInput = findViewById(R.id.inputRegion);
        this.save = findViewById(R.id.confirm);
        this.back = findViewById(R.id.back);
    }
    private void checkValid(){
        if(cameNameInput.getText().length() > 0 && regionInput.getText().length() > 0){
            enableConfirm();
        }else{
            disableConfirm();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ApplicationService.regions.size() > 0){
            ApplicationService.regions.clear();
        }
    }

    private void setData(){
        if(type.equals("editCam")){
            this.title.setText(LanguageManager.getInstance().getValue("edit_camprofile"));
            this.back.setVisibility(View.VISIBLE);
        }else{
            this.title.setText(LanguageManager.getInstance().getValue("complete"));
            this.back.setVisibility(View.GONE);
        }
        this.camNameTx.setText(LanguageManager.getInstance().getValue("camera_name"));
        this.cameNameInput.setHint(LanguageManager.getInstance().getValue("camera_name_hint"));
        this.regionTx.setText(LanguageManager.getInstance().getValue("region"));
        this.regionInput.setHint(LanguageManager.getInstance().getValue("region_hint"));
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        checkValid();
        if(model!=null){
            MapIdentity.mapImageCam(model, avatarCam);
        }else{
            avatarCam.setImageResource(R.drawable.ava_cam_defautl);
        }
        this.cameNameInput.setText(camName);
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ApplicationService.regions.clear();
            }
        });
        this.regionInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegionTree();
            }
        });
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nameRegion = regionInput.getText().toString();
                    Region objectIndex = ApplicationService.regions.stream().filter(region -> nameRegion.equals(region.getRegionName())).findAny().orElse(null);
                    int idRegion = objectIndex.getId();
                    JSONObject payload = new JSONObject();
                    payload.put("objectGuidCamera", objectID);
                    payload.put("cameraName", cameNameInput.getText().toString());
                    payload.put("regionId", idRegion);
                    ApplicationService.requestManager.updateCam(payload, ApplicationService.URL_UPDATE_CAM);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        this.cameNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkValid();
            }
        });
        regionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkValid();
            }
        });

    }
    private void setRegion(){
        Region regionIndex = ApplicationService.regions.stream().filter(region -> regionId.equals(String.valueOf(region.getId()))).findAny().orElse(null);
        regionInput.setText(regionIndex.getRegionName());
    }

    private void disableConfirm(){
        this.save.setEnabled(false);
        this.save.setClickable(false);
        this.save.setAlpha(.5f);
    }
    private void enableConfirm(){
        this.save.setEnabled(true);
        this.save.setClickable(true);
        this.save.setAlpha(1);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }

    private void openRegionTree() {
        Dialog dialog = new Dialog(EditCamProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tree_view_custom);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        RecyclerView recyclerView = dialog.findViewById(R.id.files_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        TreeViewHolderFactory factory = (v, layout) -> new ItemViewHolder(v, dialog, regionInput, regionInput.getText().toString());
        treeViewAdapter = new TreeViewAdapter(factory);
        recyclerView.setAdapter(treeViewAdapter);
        treeViewAdapter.updateTreeNodes(root);
//        TreeNode treeNode = new TreeNode(regionInput.getText().toString(), R.layout.item_tree_custom);
//        treeViewAdapter.expandNode(root.);
        treeViewAdapter.expandAll();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                treeViewAdapter.collapseAll();
            }
        });
    }
    private List<TreeNode> generateTree(List<Region> listAll){
        List<TreeNode> root = new ArrayList<>();
        for (int i = 0; i < listAll.size(); i++){
            if(listAll.get(i).getParentId()==0){
                TreeNode treeNode = new TreeNode(listAll.get(i).getRegionName(), R.layout.item_tree_custom);
                treeNode.setLevel(0);
                root.add(treeNode);
            }
        }
        root.add(getChildren(root.get(0), listAll));
        return root;
    }
    private TreeNode getChildren(TreeNode treeNode, List<Region> listAll) {
        for(int i =0; i < listAll.size(); i++){
            Region objectIndex = listAll.stream().filter(region -> treeNode.getValue().equals(region.getRegionName())).findAny().orElse(null);
            if(objectIndex.getId() == listAll.get(i).getParentId() && objectIndex.getLevel() + 1 == listAll.get(i).getLevel()){
                TreeNode childrenNode = new TreeNode(listAll.get(i).getRegionName(), R.layout.item_tree_custom);
                childrenNode.setLevel(listAll.get(i).getLevel() - 1);
                treeNode.addChild(getChildren(childrenNode,listAll));
            }
        }
        return treeNode;
    }
    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_ALL_REGIONS_SUCCESS:
//                    ApplicationService.showToast("Lấy thông tin region thành công", false);
                    root = generateTree(ApplicationService.regions);
                    root.remove(root.get(0));
                    Log.e(TAG, "handleMessage: " + root.size());
                    setRegion();
                    break;
                case ApplicationService.GET_ALL_REGIONS_FAIL:
                    ApplicationService.showToast("Lỗi lấy thông tin region", true);
                    break;
                case ApplicationService.UPDATE_CAM_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra(MainScreen.TYPE, "main");
                    ApplicationService.cameraitems.clear();
                    ApplicationService.regions.clear();
                    ApplicationService.requestManager.getListCam(ApplicationService.URL_GET_LIST_CAM, null, null, 1, 20, null);
                    startActivity(intent);
                    break;
                case ApplicationService.UPDATE_CAM_FAIL:
                    ApplicationService.showToast("Lỗi update thông tin cam", true);
                    break;
            }
        }
    }
}