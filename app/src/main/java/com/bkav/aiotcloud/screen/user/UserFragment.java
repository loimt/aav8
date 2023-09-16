package com.bkav.aiotcloud.screen.user;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.Language;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.LoginActiviry;
import com.bkav.aiotcloud.screen.MainScreen;
import com.bkav.aiotcloud.screen.setting.face.FaceDetectionActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragview = inflater.inflate(R.layout.user_fragment, container, false);
        this.dialog  = new Dialog(getContext());
        this.avatar = fragview.findViewById(R.id.userImage);
        this.title = fragview.findViewById(R.id.titleUserTx);
        this.fullName = fragview.findViewById(R.id.userName);
        this.logout = fragview.findViewById(R.id.logoutLayout);
        this.infoUserLayout = fragview.findViewById(R.id.infoUserLayout);
        this.changePassLayout = fragview.findViewById(R.id.changePassLayout);
        this.notifySettinglayout = fragview.findViewById(R.id.notifySettingLayout);
        this.currentLanguageTxt = fragview.findViewById(R.id.currentLanguageTxt);
        this.currentFlag = fragview.findViewById(R.id.bigFlag);
        this.infoApp = fragview.findViewById(R.id.infoLayout);
        this.languageSettingLayout = fragview.findViewById(R.id.languageLayout);
        this.userprofileLayoutTxt = fragview.findViewById(R.id.userprofileLayoutTx);
        this.changePass = fragview.findViewById(R.id.changePass);
        this.supportTx = fragview.findViewById(R.id.supportTx);
        this.inforTx = fragview.findViewById(R.id.inforTx);
        this.languageTx = fragview.findViewById(R.id.languageTx);
        this.notifyTx = fragview.findViewById(R.id.notifyTx);
        this.logoutTx = fragview.findViewById(R.id.logoutTx);
        this.p2pTx = fragview.findViewById(R.id.p2pTx);
        this.p2pSettingLayout = fragview.findViewById(R.id.p2pSettingLayout);


        setData();
        action();
        return fragview;
    }


    private void action() {
        this.languageSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LanguageSetting.class);
                startActivity(intent);
            }
        });
        this.infoApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IntroApp.class);
                startActivity(intent);
            }
        });

        this.infoUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUserProfile.class);
                startActivity(intent);
            }
        });

        this.p2pSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), P2PMode.class);
                startActivity(intent);
            }
        });

        this.changePassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserChangePassword.class);
                startActivity(intent);
            }
        });
        this.notifySettinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotifySetting.class);
                startActivity(intent);
            }
        });

        this.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogoutDialog();
//                Intent intent = new Intent(getContext(), LogOutDialog.class);
//                startActivity(intent);

            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
         setData();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static final String TAG = UserFragment.class.getName();
    private RelativeLayout changePassLayout;
    private RelativeLayout infoUserLayout;
    private RelativeLayout notifySettinglayout;
    private RelativeLayout languageSettingLayout;
    private RelativeLayout infoApp;
    private ImageView avatar;
    private TextView title;
    private TextView fullName;
    private RelativeLayout logout;
    private int currentLanguage = 0;
    private TextView currentLanguageTxt;
    private ImageView currentFlag;
    private TextView userprofileLayoutTxt;
    private TextView changePass;
    private TextView supportTx;
    private TextView inforTx;
    private TextView languageTx;
    private TextView notifyTx;
    private TextView logoutTx;
    private TextView tv_test;

    private TextView p2pTx;
    private RelativeLayout p2pSettingLayout;
    private Dialog dialog;


    private void setData() {
        this.logoutTx.setText(LanguageManager.getInstance().getValue("logout"));
        this.notifyTx.setText(LanguageManager.getInstance().getValue("notify_setting"));
        this.languageTx.setText(LanguageManager.getInstance().getValue("language"));
        this.inforTx.setText(LanguageManager.getInstance().getValue("intro_app"));
        this.supportTx.setText(LanguageManager.getInstance().getValue("help"));
        this.changePass.setText(LanguageManager.getInstance().getValue("change_password"));
        this.p2pTx.setText(LanguageManager.getInstance().getValue("p2p_mode"));
        this.userprofileLayoutTxt.setText(LanguageManager.getInstance().getValue("userprofile"));
        this.title.setText(LanguageManager.getInstance().getValue("userprofile"));
        if (ApplicationService.user != null){
            if(ApplicationService.user.getAvatar().equals("null")){
                String fnm = "user_default";
                String PACKAGE_NAME = getContext().getPackageName();
                int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                Glide.with(getContext())
                        .load(imgId)
                        .circleCrop()
                        .into(this.avatar);
            }else{
                Glide.with(getContext())
                        .load(ApplicationService.user.getAvatar())
                        .circleCrop()
                        .into(this.avatar);
            }
            if(!ApplicationService.user.getFullName().equals("null")){
                fullName.setText(ApplicationService.user.getFullName());
            }else{
                fullName.setText(ApplicationService.user.getPhone());
            }
        }

        ArrayList<Language> languages = LanguageManager.getInstance().getLanguageList();
        for (int index = 0; index < languages.size(); index++) {
            Language language = languages.get(index);
            if (language.isDefault()) {
                currentLanguage = index;
                if (currentLanguage == 1){
                    this.currentFlag.setImageResource(R.drawable.flag_vietnam_fullsize);
                    this.currentLanguageTxt.setText("Tiếng Việt");
                } else {
                    this.currentFlag.setImageResource(R.drawable.flag_uk_fullsize);
                    this.currentLanguageTxt.setText("English");
                }
            }
        }
    }

    private void openLogoutDialog() {
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

        textViewConfirm.setText(LanguageManager.getInstance().getValue("make_sure_logout"));
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
                ApplicationService.requestManager.logout(ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGOUT);
            }
        });
    }

}