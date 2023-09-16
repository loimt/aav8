package com.bkav.aiotcloud.screen.viewlan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;

public class AlertViewLan extends AppCompatActivity {
    RelativeLayout back;
    TextView title;
    TextView note;
    Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_view_lan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        action();
        setData();
    }
    private void init(){
        this.back = findViewById(R.id.backAlertLan);
        this.note = findViewById(R.id.noteLan);
        this.title = findViewById(R.id.titleScanLan);
        this.confirm = findViewById(R.id.confirmScanLan);
    }
    private void action(){
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewOnLan.class);
                intent.putExtra(ViewOnLan.TYPE, "viewLan");
                startActivity(intent);
            }
        });
    }
    private void setData(){
        this.title.setText(LanguageManager.getInstance().getValue("view_lan"));
        this.note.setText(LanguageManager.getInstance().getValue("alert_Lan"));
        this.confirm.setText(LanguageManager.getInstance().getValue("search"));
    }
}