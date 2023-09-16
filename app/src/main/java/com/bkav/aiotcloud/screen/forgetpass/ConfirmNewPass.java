package com.bkav.aiotcloud.screen.forgetpass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.LoginActiviry;
import com.ybs.passwordstrengthmeter.PasswordStrength;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmNewPass extends AppCompatActivity {
    private RelativeLayout back;
    private TextView title;
    private TextView newPassTitle;
    private EditText inputNewPass;
    private EditText inputConfirmPass;
    private TextView confirmPassTitle;
    private TextView alertNotSame;
    private TextView notePass;
    private TextView weakPass;
    private TextView mediumPass;
    private TextView strongPass;
    private Button sendNewPass;
    private ImageView showNewPass;
    private ImageView showConfirmPass;
    private boolean isShowNewPass = false;
    private boolean isShowConfirmPass = false;
    private boolean strongPassActive = false;
    private boolean isSamePass = false;

    private PasswordStrength checkStrongPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.confirm_new_pass);
        init();
        setData();
        action();
    }
    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }
    private void init(){
        this.back = findViewById(R.id.backButtonConfirmPass);
        this.title = findViewById(R.id.titleConfirmPass);
        this.newPassTitle = findViewById(R.id.newPassWordTx);
        this.inputNewPass = findViewById(R.id.newPassWordInput);
        this.confirmPassTitle = findViewById(R.id.confirmNewPassWordlTx);
        this.inputConfirmPass = findViewById(R.id.newConfirmPassWordInput);
        this.alertNotSame = findViewById(R.id.alertConfirmPassTxt);
        this.notePass = findViewById(R.id.noteNewPass);
        this.weakPass = findViewById(R.id.weakPassTxt);
        this.mediumPass = findViewById(R.id.mediumPassTxt);
        this.strongPass = findViewById(R.id.strongPassTxt);
        this.sendNewPass = findViewById(R.id.sendNewPass);
        this.showNewPass = findViewById(R.id.eyeNewPass);
        this.showConfirmPass = findViewById(R.id.eyeNewConfirmPassWord);
    }
    private void setData(){
        disableNext();
        this.title.setText(LanguageManager.getInstance().getValue("hint_new_password"));
        this.newPassTitle.setText(LanguageManager.getInstance().getValue("new_password"));
        this.inputNewPass.setHint(LanguageManager.getInstance().getValue("hint_new_password"));
        this.confirmPassTitle.setText(LanguageManager.getInstance().getValue("confirm_new_password"));
        this.inputConfirmPass.setHint(LanguageManager.getInstance().getValue("hint_confirm_password"));
        this.notePass.setText(LanguageManager.getInstance().getValue("note_pass"));
        this.weakPass.setText(LanguageManager.getInstance().getValue("weak_pass"));
        this.mediumPass.setText(LanguageManager.getInstance().getValue("medium_pass"));
        this.strongPass.setText(LanguageManager.getInstance().getValue("strong_pass"));
        this.sendNewPass.setText(LanguageManager.getInstance().getValue("send_new_pass"));

    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.sendNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("objectGuid", ApplicationService.OBJECTGUID);
                    payload.put("password", inputNewPass.getText());
                    payload.put("confirmPassword", inputConfirmPass.getText());
                }catch (JSONException e){
                    e.printStackTrace();
                }
             ApplicationService.requestManager.recoverChangePass(payload, ApplicationService.URL_RECOVER_CHANGE_PASS);
            }
        });

        this.inputNewPass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNewPass.setClickable(true);
                    if(isShowNewPass){
                        showNewPass.setImageResource(R.drawable.eye);

                    }else{
                        showNewPass.setImageResource(R.drawable.showpass);

                    }
                }else{
                    showNewPass.setClickable(false);
                    showNewPass.setImageResource(0);
                }
            }
        });
        this.inputConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showConfirmPass.setClickable(true);

                    if(isShowConfirmPass){
                        showConfirmPass.setImageResource(R.drawable.eye);

                    }else{
                        showConfirmPass.setImageResource(R.drawable.showpass);
                    }
                }else{
                    showConfirmPass.setClickable(false);
                    showConfirmPass.setImageResource(0);
                }
            }
        });
        this.inputConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputConfirmPass.getText().length() > 0){
                    if(!inputConfirmPass.getText().toString().equals(inputNewPass.getText().toString())){
                        alertNotSame.setText(LanguageManager.getInstance().getValue("alert_notsame"));
                        isSamePass=false;
                    }else {
                        alertNotSame.setText("");
                        isSamePass=true;
                    }
                }else {
                    alertNotSame.setText("");
                }
                validPassWord();
            }
        });

        this.inputNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(inputNewPass.getText().length()>0){
                    checkStrongPass = PasswordStrength.calculateStrength(s.toString());
                    if(checkStrongPass.getText(getApplicationContext()).equals("Weak")){
                        strongPassActive=false;
                        weakPass.setTextColor(getResources().getColor(R.color.mainColor, null));
                        mediumPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        strongPass.setTextColor(getResources().getColor(R.color.grayTitle, null));

                    }else if(checkStrongPass.getText(getApplicationContext()).equals("Medium")){
                        strongPassActive=true;
                        weakPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        mediumPass.setTextColor(getResources().getColor(R.color.mainColor, null));
                        strongPass.setTextColor(getResources().getColor(R.color.grayTitle, null));

                    }else if(checkStrongPass.getText(getApplicationContext()).equals("Strong")){
                        strongPassActive=true;
                        weakPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        mediumPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        strongPass.setTextColor(getResources().getColor(R.color.mainColor, null));
                    }else{
                        strongPassActive=true;
                        weakPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        mediumPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                        strongPass.setTextColor(getResources().getColor(R.color.mainColor, null));
                    }
                }else{
                    strongPassActive=false;
                    weakPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                    mediumPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                    strongPass.setTextColor(getResources().getColor(R.color.grayTitle, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                validPassWord();
            }
        });

        this.showNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowNewPass) {
                    inputNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowNewPass = true;
                    showNewPass.setImageResource(R.drawable.eye);
                } else {
                    showNewPass.setImageResource(R.drawable.showpass);

                    inputNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowNewPass = false;
                }
                inputNewPass.setSelection(inputNewPass.getText().length());
            }
        });
        this.showConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowConfirmPass) {
                    showConfirmPass.setImageResource(R.drawable.eye);
                    //password is visible
                    inputConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowConfirmPass = true;
                } else {
                    showConfirmPass.setImageResource(R.drawable.showpass);

                    //password gets hided
                    inputConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowConfirmPass = false;
                }
                inputConfirmPass.setSelection(inputConfirmPass.getText().length());
            }
        });
    }
    private void validPassWord(){
        if(isSamePass && strongPassActive){
            enableNext();
        }else {
            disableNext();
        }
    }
    private void disableNext(){
        this.sendNewPass.setAlpha(.5f);
        this.sendNewPass.setEnabled(false);
    }
    private void enableNext(){
        this.sendNewPass.setAlpha(1);
        this.sendNewPass.setEnabled(true);
    }
    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.RECOVER_PASSWORD_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("reset_pass_fail"), true);
                    break;
                case ApplicationService.RECOVER_PASSWORD_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("reset_pass_success"), false);
                    Intent intent = new Intent(getApplicationContext(), LoginActiviry.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}