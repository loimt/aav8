package com.bkav.aiotcloud.screen.home.optioncam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;

public class ManualAdd extends AppCompatActivity {
    private RelativeLayout back;
    private TextView title;
    private TextView note;
    private EditText inputSerial;
    private Button buttomConfirm;
    private TextView alert;
    private static final String TAG = "ManualAdd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        setData();
        action();
    }

    private void init (){
        this.back = findViewById(R.id.back);
        this.title = findViewById(R.id.title);
        this.note = findViewById(R.id.noteText);
        this.inputSerial = findViewById(R.id.serialInput);
        this.buttomConfirm = findViewById(R.id.confirm);
        this.alert = findViewById(R.id.alertTx);
    }
    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("manual_add"));
        this.note.setText(LanguageManager.getInstance().getValue("note_manual_add"));
        this.inputSerial.setHint(LanguageManager.getInstance().getValue("note_manual_hint"));
        this.buttomConfirm.setText(LanguageManager.getInstance().getValue("next"));
        disableConfirm();
    }
    private void action() {
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.buttomConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serialStr = inputSerial.getText().toString();
                Intent intent = new Intent(getApplicationContext(), CheckStatusCam.class);
                intent.putExtra(CheckStatusCam.SERIAL_KEY, serialStr);
                startActivity(intent);
            }
        });

        this.inputSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 8){
                    disableConfirm();
                    alert.setText(LanguageManager.getInstance().getValue("invalid_serial"));
                }else {
                    enableConfirm();
                    alert.setText("");
                }

            }
        });
    }

    private void disableConfirm(){
        this.buttomConfirm.setEnabled(false);
        this.buttomConfirm.setClickable(false);
        this.buttomConfirm.setAlpha(.5f);
    }
    private void enableConfirm(){
        this.buttomConfirm.setEnabled(true);
        this.buttomConfirm.setClickable(true);
        this.buttomConfirm.setAlpha(1);
    }
}