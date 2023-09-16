package com.bkav.aiotcloud.screen.forgetpass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.user.IntroApp;
import com.bkav.aiotcloud.screen.user.notify_setting.DetailNotifySetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPassword extends AppCompatActivity {
    private TextView title;
    private TextView noteAccount;
    private TextView alertAccount;
    private EditText inputAcount;
    private Button next;
    private RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.forget_password);
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
        this.title = findViewById(R.id.titleForgetPassword);
        this.noteAccount = findViewById(R.id.noteForgetPass);
        this.alertAccount = findViewById(R.id.alertAccount);
        this.inputAcount = findViewById(R.id.accountForgetPass);
        this.next = findViewById(R.id.nextForgetPass);
        this.back = findViewById(R.id.backButtonForgetPass);
    }
    private void setData(){
        setUnavailableNext();
        this.alertAccount.setText("");
        this.title.setText(LanguageManager.getInstance().getValue("forgetPass"));
        this.noteAccount.setText(LanguageManager.getInstance().getValue("note_account_forgetpass"));
        this.next.setText(LanguageManager.getInstance().getValue("next"));
        this.inputAcount.setHint(LanguageManager.getInstance().getValue("account"));
    }

    private void setAvailableNext(){
        this.next.setAlpha(1);
        this.next.setEnabled(true);
    }
    private void setUnavailableNext(){
        this.next.setAlpha(.5f);
        this.next.setEnabled(false);
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    if(isValidPhone(inputAcount.getText())){
//                        payload.put("mobile", inputAcount.getText());
                        payload.put("mobile", "0942439023");
                        payload.put("email", "");

                    }else if(isValidEmail(inputAcount.getText())){
                        payload.put("email", inputAcount.getText());
                        payload.put("mobile", "");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                ApplicationService.requestManager.recoverPass(payload, ApplicationService.URL_RECOVER_PASS);
            }
        });



       this.inputAcount.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(inputAcount.length() == 0){
                    alertAccount.setText(LanguageManager.getInstance().getValue("not_empty_account"));
                    setUnavailableNext();
                }
                else{
                    if(isNumeric(String.valueOf(s.charAt(0)))){
                        if(isValidPhone(s)){
                            alertAccount.setText("");
                            setAvailableNext();
                        }
                        else if(isValidPhone(s)==false && isNumeric(s.toString())){
                            alertAccount.setText(LanguageManager.getInstance().getValue("wrong_format_phone"));
                            setUnavailableNext();
                        }
                        else{
                            if(isValidEmail(s)){
                                alertAccount.setText("");
                                setAvailableNext();
                            }else{
                                alertAccount.setText(LanguageManager.getInstance().getValue("wrong_format_email"));
                                setUnavailableNext();
                            }
                        }
                    }else{
                        if(isValidEmail(s)){
                            alertAccount.setText("");
                            setAvailableNext();
                        }else{
                            alertAccount.setText(LanguageManager.getInstance().getValue("wrong_format_email"));
                            setUnavailableNext();
                        }
                    }
                }
           }
           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidPhone(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                Patterns.PHONE.matcher(target).matches();
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.RECOVER_PASSWORD_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), ConfirmOTP.class);
                    startActivity(intent);
                    break;
                case ApplicationService.RECOVER_PASSWORD_FAIL:
                    ApplicationService.showToast(message.obj.toString(),  true);
                    break;
            }
        }
    }

    private void postDataUsingVolley(String mobile, String email) {
        // url to post our data
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"mobile\":\"0978563735\",\"email\":\"\"}");
        Request request = new Request.Builder()
                .url("https://baav.aiview.ai/api/AppUserRegister/RecoverPassword")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}