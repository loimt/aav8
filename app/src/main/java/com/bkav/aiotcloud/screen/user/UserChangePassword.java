package com.bkav.aiotcloud.screen.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class UserChangePassword extends AppCompatActivity {
    public static final String TAG = UserChangePassword.class.getName();
    private EditText nowPasswordInput;
    private EditText newPasswordInput;
    private EditText confirmPasswordInput;
    private Button savePassWord;
    private RelativeLayout backIM;
    private ImageView showNowPass;
    private ImageView showNewPass;
    private ImageView showConfirmPass;
    private boolean isShowNowPass = false;
    private boolean isShowNewPass = false;
    private boolean isShowConfirmPass = false;
    private TextView title;
    private TextView nowPassTx;
    private TextView newPassTx;
    private TextView confirmNewPassTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_change_password);
        init();
        setData();
        action();

    }

    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("change_password"));
        this.nowPassTx.setText(LanguageManager.getInstance().getValue("now_password"));
        this.nowPasswordInput.setHint(LanguageManager.getInstance().getValue("hint_now_password"));
        this.newPassTx.setText(LanguageManager.getInstance().getValue("new_password"));
        this.newPasswordInput.setHint(LanguageManager.getInstance().getValue("hint_new_password"));
        this.confirmNewPassTx.setText(LanguageManager.getInstance().getValue("confirm_new_password"));
        this.confirmPasswordInput.setHint(LanguageManager.getInstance().getValue("hint_confirm_password"));
        this.savePassWord.setText(LanguageManager.getInstance().getValue("confirm"));
    }

    private void action() {
        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.savePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new String(String.valueOf(newPasswordInput.getText())).equals(String.valueOf(confirmPasswordInput.getText()))){
                    ApplicationService.requestManager.changePasswordUser(getPayload(), ApplicationService.URL_CHANGE_PASS);

                }else{
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("changePass_user_notSame"), true);
                }
            }
        });

        nowPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNowPass.setClickable(true);

                    if(isShowNowPass){
                        showNowPass.setImageResource(R.drawable.eye);
                    }else{
                        showNowPass.setImageResource(R.drawable.showpass);

                    }
                }else{
                    showNowPass.setClickable(false);
                    showNowPass.setImageResource(0);
                }
            }
        });
        newPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
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
        confirmPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
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
        this.showNowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowNowPass) {
                    showNowPass.setImageResource(R.drawable.eye);

                    //password is visible
                    nowPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowNowPass = true;
                } else {
                    //password gets hided
                    showNowPass.setImageResource(R.drawable.showpass);

                    nowPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    isShowNowPass = false;
                }
                nowPasswordInput.setSelection(nowPasswordInput.getText().length());
            }
        });

        this.showNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowNewPass) {
                    showNewPass.setImageResource(R.drawable.eye);

                    //password is visible
                    newPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowNewPass = true;
                } else {
                    //password gets hided
                    showNewPass.setImageResource(R.drawable.showpass);

                    newPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    isShowNewPass = false;
                }
                newPasswordInput.setSelection(newPasswordInput.getText().length());
            }
        });
        this.showConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowConfirmPass) {
                    showConfirmPass.setImageResource(R.drawable.eye);
                    //password is visible
                    confirmPasswordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowConfirmPass = true;
                } else {
                    //password gets hided
                    confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showConfirmPass.setImageResource(R.drawable.showpass);

                    isShowConfirmPass = false;
                }
                confirmPasswordInput.setSelection(confirmPasswordInput.getText().length());

            }
        });
    }
    private JSONObject getPayload() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("oldPassword", nowPasswordInput.getText().toString());
            payload.put("newPassword", newPasswordInput.getText().toString());
            Log.e(TAG, "getPayload: "+payload.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
    private void init() {
        this.confirmNewPassTx = findViewById(R.id.confirmNewPassWordlTx);
        this.newPassTx = findViewById(R.id.newPassWordTx);
        this.nowPassTx = findViewById(R.id.nowPassWordTx);
        this.title = findViewById(R.id.titlePassword);
        this.nowPasswordInput = findViewById(R.id.nowPassWord);
        this.newPasswordInput = findViewById(R.id.newPassWord);
        this.confirmPasswordInput = findViewById(R.id.newConfirmPassWord);
        this.savePassWord = findViewById(R.id.savePassword);
        this.backIM = findViewById(R.id.backButtonPass);
        this.showNowPass = findViewById(R.id.eyeNowPass);
        this.showNewPass = findViewById(R.id.eyeNewPass);
        this.showConfirmPass = findViewById(R.id.eyeNewConfirmPassWord);
        this.nowPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.newPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.confirmPasswordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }
    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new UserChangePassword.MainHandler();
    }
    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_CHANGE_PASS_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("changePass_user_success"), false);
                    break;
                case ApplicationService.MESSAGE_UPDATE_USERPROFILE_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("changePass_user_fail"), true);
                    break;
            }
        }
    }

}