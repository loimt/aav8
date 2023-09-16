package com.bkav.aiotcloud.screen.home.device.genneralsetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.customer.AddCustomerActivity;
import com.bkav.aiotcloud.screen.user.EditUserProfile;
import com.bkav.aiotcloud.view.CustomEditText;

import java.util.LinkedList;
import java.util.List;

import me.texy.treeview.TreeNode;

public class RegionList extends AppCompatActivity {
    private TreeNode root;
    private TreeView tView;
    private ViewGroup containerView;
    private Button confirm;
//    private CustomEditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.region_list);
        setLayout();
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.requestManager.getAllRegions(ApplicationService.URL_GET_ALL_REGIONS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ApplicationService.regions.size() > 0){
            ApplicationService.regions.clear();
        }
    }

    private void setLayout(){
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        wlp.gravity = Gravity.BOTTOM;
        setFinishOnTouchOutside(true);
    }
    private void init(){
//        this.inputSearch = findViewById(R.id.searchCam);
        this.containerView = findViewById(R.id.container_treeview);
        this.confirm = findViewById(R.id.confirmFilterCam);

    }
    private void setData(){
//        this.inputSearch.setHint(LanguageManager.getInstance().getValue("search"));
        this.confirm.setText(LanguageManager.getInstance().getValue("confirm"));
    }
    private void action(){
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> listRegionId = new LinkedList<Integer>();
                List<TreeNode> selectedNodes = tView.getSelectedNodes();
                for(int i = 0; i < selectedNodes.size(); i++){
                    String nameRegionIndex = selectedNodes.get(i).getValue().toString();
                    Region objectIndex = ApplicationService.regions.stream().filter(region -> nameRegionIndex.equals(region.getRegionName())).findAny().orElse(null);
                    listRegionId.add(objectIndex.getId());
                }
                Region regionSelect = null;
                if(listRegionId.size() != 0){
                    for( Region region : ApplicationService.regions) {
                        if(region.getId() == listRegionId.get(0) ) {
                            //found it!
                            regionSelect = region;

                        }
                    }
                    Intent dataBack = new Intent();
                    dataBack.putExtra(GenneralInformation.ID, regionSelect.getId());
                    dataBack.putExtra(GenneralInformation.NAME_REGION, regionSelect.getRegionName());
                    Log.e("GenneralInformation" , regionSelect.getRegionName());
                    setResult(RESULT_OK, dataBack);
                    finish();
                } else {
                    finish();
                }

//                ApplicationService.cameraitems.clear();
//                ApplicationService.requestManager.getListCam(ApplicationService.URL_GET_LIST_CAM, null, listRegionId, 1, 20, inputSearch.getText().toString());
            }
        });
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
    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_ALL_REGIONS_SUCCESS:
                    root = generateTree(ApplicationService.regions);
                    root.removeChild(root.getChildren().get(0));
                    tView = new TreeView(root, getApplicationContext(), new MyNodeViewFactory());
                    View view = tView.getView();
                    containerView.addView(view);
                    break;
                case ApplicationService.GET_ALL_REGIONS_FAIL:
                    ApplicationService.showToast("Lỗi lấy thông tin region", true);
                    break;
                case ApplicationService.UPDATE_LIST_CAMERA:
                    finish();
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    ApplicationService.showToast(message.obj.toString(), true);
                    break;
            }
        }
    }
}