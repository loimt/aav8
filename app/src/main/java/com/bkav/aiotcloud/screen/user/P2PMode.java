package com.bkav.aiotcloud.screen.user;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.Language;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.network.BrManager;

import java.util.ArrayList;

public class P2PMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.p2p_mode);
        init();

    }

    private TextView title;
    private TextView fastConnection;
    private SwitchCompat swFastConnection;
    private RelativeLayout backButton;
   private void init(){
        this.title = findViewById(R.id.title);
        this.fastConnection = findViewById(R.id.fastConnection);
        this.swFastConnection = findViewById(R.id.swFastConnection);
        this.backButton = findViewById(R.id.backButton);
        this.title.setText(LanguageManager.getInstance().getValue("p2p_mode"));
        this.fastConnection.setText(LanguageManager.getInstance().getValue("fast_connection"));

        this.swFastConnection.setChecked( SharePref.getInstance(getApplicationContext()).getTrickle());

        this.swFastConnection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // BrManager.currentConfig.setTrickle(isChecked);
                BrManager.updateTrickleMode(isChecked);
                SharePref.getInstance(getApplicationContext()).setTrickle(isChecked);
            }
        });

        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

   }
}