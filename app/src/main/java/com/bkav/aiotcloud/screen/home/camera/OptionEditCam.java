package com.bkav.aiotcloud.screen.home.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.LoginActiviry;
import com.bkav.aiotcloud.screen.MainScreen;
import com.bkav.aiotcloud.screen.home.optioncam.EditCamProfile;

import org.json.JSONException;
import org.json.JSONObject;

public class OptionEditCam extends AppCompatActivity {
    public static String ID_KEY = "idcam";
    public static String OBID_KEY = "obidcam";
    public static String CAMNAME_KEY = "camname";
    public static String REGION_KEY = "region";
    public static String MODEL_KEY = "model";

    private int id;
    private String camID;
    private String camName;
    private String regionID;
    private String modelCam;
    private RelativeLayout editCam;
    private RelativeLayout deleteCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_edit_cam);
        setLayout();
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    private void setLayout() {
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        setFinishOnTouchOutside(true);
    }
    private void init(){
        Intent intent = getIntent();
        this.id = intent.getIntExtra(ID_KEY, 0);
        this.camID = intent.getStringExtra(OBID_KEY);
        this.camName = intent.getStringExtra(CAMNAME_KEY);
        this.regionID = intent.getStringExtra(REGION_KEY);
        this.modelCam = intent.getStringExtra(MODEL_KEY);
        this.editCam = findViewById(R.id.editCamLayout);
        this.deleteCam = findViewById(R.id.deleteCamLayout);
    }
    private void setData(){

    }
    private void action(){
        this.editCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditCamProfile.class);
                intent.putExtra(EditCamProfile.REGION_ID, regionID);
                intent.putExtra(EditCamProfile.CAMNAME, camName);
                intent.putExtra(EditCamProfile.OBJECTGUID, camID);
                intent.putExtra(EditCamProfile.MODEL, modelCam);
                intent.putExtra(EditCamProfile.TYPE, "editCam");
                startActivity(intent);
            }
        });
        this.deleteCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openLogoutDialog();
            }
        });
    }
    private void openLogoutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_logout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        RelativeLayout btnCancel = dialog.findViewById(R.id.cancelLayoutLogout);
        RelativeLayout btnConfirm = dialog.findViewById(R.id.confirmLayoutLogout);
        TextView textViewConfirm = dialog.findViewById(R.id.contentConfirmText);
        TextView cancel = dialog.findViewById(R.id.titleCancleLogout);
        TextView confirm = dialog.findViewById(R.id.titleConfirmLogout);

        textViewConfirm.setText(LanguageManager.getInstance().getValue("make_sure_delete_cam"));
        cancel.setText(LanguageManager.getInstance().getValue("cancel"));
        confirm.setText(LanguageManager.getInstance().getValue("confirm"));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject payload = new JSONObject();
                    payload.put("camId", id);
                    ApplicationService.requestManager.deleteCam(payload, ApplicationService.URL_DELETE_CAM);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.DELETE_CAM_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra(MainScreen.TYPE, "main");
                    ApplicationService.cameraitems.clear();
                    ApplicationService.requestManager.getListCam(ApplicationService.URL_GET_LIST_CAM, null, null, 1, 20, null);
                    startActivity(intent);
                    break;
                case ApplicationService.DELETE_CAM_FAIL:
                    ApplicationService.showToast("Lỗi xóa cam", true);
                    break;
            }
        }
    }
}