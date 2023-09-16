package com.bkav.aiotcloud.screen.forgetpass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.user.notify_setting.DetailNotifySetting;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ConfirmOTP extends AppCompatActivity {
    private RelativeLayout back;
    private TextView title;
    private EditText code1;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;
    private TextView arlertOTP;
    private TextView resendOTP;
    private TextView notifyOTP;
    private Button next;
    private static final long START_TIME_IN_MILLIS = 180000;

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftinMillis = START_TIME_IN_MILLIS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.confirm_otp_pass);
        init();
        setData();
        action();
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    private void action() {
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code1.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.KEYCODE_CLEAR)) {
                            code1.requestFocus();
                            return true;
                        }else{
                            code2.requestFocus();
                        }
                        return false;
                    }
                });
                checkAvaible();

            }
        });
        this.code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code2.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.KEYCODE_CLEAR)) {
                            code2.requestFocus();
                            return true;
                        }else{
                            code3.requestFocus();
                        }
                        return false;
                    }
                });

            }
        });
        this.code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code3.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.KEYCODE_CLEAR)) {
                            code3.requestFocus();
                            return true;
                        }else{
                            code4.requestFocus();
                        }
                        return false;
                    }
                });

            }
        });
        this.code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code4.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.KEYCODE_CLEAR)) {
                            code4.requestFocus();
                            return true;
                        }else{
                            code5.requestFocus();
                        }
                        return false;
                    }
                });
                checkAvaible();
            }
        });
        this.code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                code5.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.KEYCODE_CLEAR)) {
                            code5.requestFocus();
                            return true;
                        }else{
                            code6.requestFocus();
                        }
                        return false;
                    }
                });
                checkAvaible();
            }
        });



        this.code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAvaible();
            }
        });
        this.resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableResend();
                JSONObject payload = new JSONObject();
                try {
                    payload.put("objectGuid", ApplicationService.OBJECTGUID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApplicationService.requestManager.resendRecoverPass(payload, ApplicationService.URL_RESEND_RECOVER_PASS);

            }
        });
        this.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                String OTP = code1.getText().toString()
                        + code2.getText().toString()
                        + code3.getText().toString()
                        + code4.getText().toString()
                        + code5.getText().toString()
                        + code6.getText().toString();
                try {
                    payload.put("objectGuid", ApplicationService.OBJECTGUID);
                    payload.put("otpCode", OTP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApplicationService.requestManager.confirmOTPrecoverPass(payload, ApplicationService.URL_CONFIRM_OTP_RECOVER_PASS) ;
            }
        });
    }

    private void setData() {
        code1.requestFocus();
        code1.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(code1, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);
        disableResend();
        startTimer();
        disableNext();
        this.title.setText(LanguageManager.getInstance().getValue("enter_otp_code"));
        this.notifyOTP.setText(LanguageManager.getInstance().getValue("notify_OTP_code"));
        this.next.setText(LanguageManager.getInstance().getValue("next"));
    }
    private void disableResend(){
        this.resendOTP.setText("");
        this.resendOTP.setClickable(false);
    }
    private void enableResend(){
        this.resendOTP.setText(LanguageManager.getInstance().getValue("resend_OTP"));
        this.resendOTP.setClickable(true);
    }
    private void disableNext(){
        this.next.setAlpha(.5f);
        this.next.setEnabled(false);
    }
    private void enableNext(){
        this.next.setAlpha(1);
        this.next.setEnabled(true);
    }
    private void checkAvaible(){
        if(!code1.getText().toString().matches( "")
                && !code2.getText().toString().matches( "")
                && !code3.getText().toString().matches( "")
                && !code4.getText().toString().matches( "")
                && !code5.getText().toString().matches( "")
                && !code6.getText().toString().matches( "")){
            enableNext();
        }else{
            disableNext();
        }
    }
    private void init(){
        this.back = findViewById(R.id.backButtonConfirmOTP);
        this.title = findViewById(R.id.titleConfirmOTP);
        this.code1 = findViewById(R.id.code1Txt);
        this.code2 = findViewById(R.id.code2Txt);
        this.code3 = findViewById(R.id.code3Txt);
        this.code4 = findViewById(R.id.code4Txt);
        this.code5 = findViewById(R.id.code5Txt);
        this.code6 = findViewById(R.id.code6Txt);
        this.arlertOTP = findViewById(R.id.otpAlertTxt);
        this.resendOTP = findViewById(R.id.resendOTP);
        this.notifyOTP = findViewById(R.id.notifyOTP);
        this.next = findViewById(R.id.nextConfirmOTP);
    }
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftinMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                enableResend();
                arlertOTP.setText(LanguageManager.getInstance().getValue("expired_OTP"));
            }
        }.start();

    }
    private void resetTimer(){
        mTimeLeftinMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        startTimer();
    }

    private void updateCountDownText() {
        int seconds = (int)(mTimeLeftinMillis/1000);
        this.arlertOTP.setText(LanguageManager.getInstance().getValue("alert_otp_code")+ seconds + LanguageManager.getInstance().getValue("seconds"));
    }

    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.RESEND_RECOVER_PASSWORD_SUCCESS:
                    resetTimer();
                    break;
                case ApplicationService.RESEND_RECOVER_PASSWORD_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("wrong_resend_OTP"), true);
                    break;
                case ApplicationService.CONFIRM_RECOVER_PASSWORD_OTP_FAIL:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("wrong_OTP"), true);
                    break;
                case ApplicationService.CONFIRM_RECOVER_PASSWORD_OTP_SUCCESS:
                    Intent intent = new Intent(getApplicationContext(), ConfirmNewPass.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}