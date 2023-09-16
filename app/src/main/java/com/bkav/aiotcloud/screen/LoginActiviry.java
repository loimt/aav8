package com.bkav.aiotcloud.screen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.Language;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.forgetpass.ConfirmNewPass;
import com.bkav.aiotcloud.screen.forgetpass.ForgetPassword;
import com.bkav.aiotcloud.screen.user.EditUserProfile;
import com.bkav.aiotcloud.screen.viewlan.AlertViewLan;
import com.bkav.aiotcloud.screen.viewlan.CameraDetailLan;
import com.bkav.aiotcloud.screen.viewlan.ViewOnLan;
import com.bkav.aiotcloud.view.WrongPassDialog;

import java.util.ArrayList;

import static com.bkav.aiotcloud.util.FontManager.TAG;

public class LoginActiviry extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.login);
        init();
        action();
        ArrayList<Language> languages = LanguageManager.getInstance().getLanguageList();
        for (int index = 0; index < languages.size(); index++) {
            Language language = languages.get(index);
            if (language.isDefault()) {
                currentLanguage = index;
                if (currentLanguage == 1){
                    radioVN.setChecked(true);
                } else {
                    radioUNK.setChecked(true);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private boolean showLanguage = false;
    private boolean isShowPassword = false;

    private RelativeLayout curentLayoutLanguage;
    private RelativeLayout layoutLanguage;
    private int currentLanguage = 0;


    private EditText txUser;
    private EditText txPass;
    private TextView txForgetPass;
    private TextView txHintLogin;
    private TextView txViewOnLAN;
    private TextView txCurrentLanguage;

    private RadioButton radioUNK;
    private RadioButton radioVN;

    private Button btLogin;

    private ProgressBar loadingLogin;

    private ImageView imCurrentFlag;
    private ImageView showPass;

    private TextView vnText;
    private TextView enText;

    private void init() {
        this.curentLayoutLanguage = findViewById(R.id.currentLanguagelayout);
        this.layoutLanguage = findViewById(R.id.layoutLanguage);
        this.txUser = findViewById(R.id.user);
        this.txPass = findViewById(R.id.pass);
        this.txHintLogin = findViewById(R.id.hintLogin);
        this.txForgetPass = findViewById(R.id.forgetPass);
        this.txViewOnLAN = findViewById(R.id.viewOnLAN);
        this.txCurrentLanguage = findViewById(R.id.currentLanguage);

        this.imCurrentFlag = findViewById(R.id.currentFlag);
        this.radioUNK = findViewById(R.id.radioUNK);
        this.radioVN = findViewById(R.id.radioVN);

        this.btLogin = findViewById(R.id.loginBT);
        this.loadingLogin = findViewById(R.id.progressBar);
        this.showPass = findViewById(R.id.eye);

        this.vnText = findViewById(R.id.vietnameseText);
        this.enText = findViewById(R.id.UNKText);

        this.txPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        updateLanguage();
        ApplicationService.mainHandler = new MainHandler();
    }

    @Override
    protected void onResume() {
        ApplicationService.mainHandler = new MainHandler();
        super.onResume();
    }

    private void hideKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void action() {
        this.txForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivity(intent);
            }
        });

        this.curentLayoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showLanguage) {
                    layoutLanguage.setVisibility(View.GONE);
                    showLanguage = false;
                } else {
                    layoutLanguage.setVisibility(View.VISIBLE);
                    showLanguage = true;
                }
            }
        });

        this.radioVN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    radioUNK.setChecked(false);
                    currentLanguage = 1;
                    changeLanguage(currentLanguage);
                }
            }
        });

        this.radioUNK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if ( compoundButton.isChecked()){
                    radioVN.setChecked(false);
                    currentLanguage = 0;
                    changeLanguage(currentLanguage);
                }
            }
        });

        this.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ApplicationService.requestManager.login(txUser.getText().toString(), txPass.getText().toString(), ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
//                ApplicationService.requestManager.login("0332387813", "abcd@1234", ApplicationService.URL_LOGIN);
//                ApplicationService.requestManager.login("Bkav-cuongndc", "123456@A", ApplicationService.URL_LOGIN);
//                ApplicationService.requestManager.login("0919910019", "Tnthiefz12$", ApplicationService.URL_LOGIN);
                hideKeyBoard();
                loadingLogin.setVisibility(View.VISIBLE);
            }
        });

        this.showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowPassword) {
                    //password is visible
                    txPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.eye);
                    isShowPassword = true;

                } else {
                    //password gets hided
                    txPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setImageResource(R.drawable.showpass);
                    isShowPassword = false;
                }
                txPass.setSelection(txPass.getText().length());
            }
        });
        this.txPass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showPass.setClickable(true);
                    if(isShowPassword){
                        showPass.setImageResource(R.drawable.eye);

                    }else{
                        showPass.setImageResource(R.drawable.showpass);
                    }
                }else{
                    showPass.setClickable(false);
                    showPass.setImageResource(0);
                }
            }
        });

        this.txViewOnLAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlertViewLan.class);
                startActivity(intent);
            }
        });
    }


    private void changeLanguage(int position) {
        if (position == 1) {
            this.imCurrentFlag.setImageResource(R.drawable.vietnam_flag_cercle);
        } else {
            this.imCurrentFlag.setImageResource(R.drawable.en_flag_cercle);
        }
        LanguageManager.getInstance().changeLanguage(LanguageManager.getInstance().getLanguage(currentLanguage));
        SharePref.getInstance(getApplicationContext()).setDefaultLanguage(currentLanguage);
        this.txCurrentLanguage.setText(LanguageManager.getInstance().getValue("languageLogin"));
        updateLanguage();
    }

    private void updateLanguage() {
        SpannableString content = new SpannableString(LanguageManager.getInstance().getValue("viewOnLAN"));
        content.setSpan(new UnderlineSpan(), 0, LanguageManager.getInstance().getValue("viewOnLAN").length(), 0);
        txViewOnLAN.setText(content);
        this.txUser.setHint(LanguageManager.getInstance().getValue("user"));
        this.txPass.setHint(LanguageManager.getInstance().getValue("password"));
        this.txHintLogin.setText(Html.fromHtml(LanguageManager.getInstance().getValue("hintLogin")));
        this.txForgetPass.setText(LanguageManager.getInstance().getValue("forgetPass"));
        this.txViewOnLAN.setText(LanguageManager.getInstance().getValue("view_lan"));
        this.btLogin.setText(LanguageManager.getInstance().getValue("login"));
        this.enText.setText(LanguageManager.getInstance().getValue("Eng_language_setting"));
        this.vnText.setText(LanguageManager.getInstance().getValue("VN_language_setting"));
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Log.e(TAG, "handleMessage: " + message.what);
            switch (message.what) {
                case ApplicationService.LOGIN_SUCCESS:
                    loadingLogin.setVisibility(View.GONE);
                    SharePref.getInstance(getApplicationContext()).setUserPassword(txUser.getText().toString(), txPass.getText().toString());
                    SharePref.getInstance(getApplicationContext()).setIsLogin(true);
                    Intent intent = new Intent(getApplication(), MainScreen.class);
                    intent.putExtra(MainScreen.TYPE, "main");
                    startActivity(intent);
                    break;
                case ApplicationService.LOGIN_FAIL:
                    loadingLogin.setVisibility(View.GONE);
                    Intent loginFail = new Intent(getApplication(), WrongPassDialog.class);
                    startActivity(loginFail);
                    break;
            }
        }
    }
}
