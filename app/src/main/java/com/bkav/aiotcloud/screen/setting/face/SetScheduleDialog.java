package com.bkav.aiotcloud.screen.setting.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;

public class SetScheduleDialog extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_schedule);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.titleTx);

        TextView alwayActiveTx = findViewById(R.id.alwayActiveTx);
        TextView settingHandmadeTx = findViewById(R.id.settingHandmadeTx);
        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.ok);

        this.settingHandmade = findViewById(R.id.settingHandmade);
        this.alwayActive = findViewById(R.id.alwayActive);

        title.setText(LanguageManager.getInstance().getValue("schedule_active"));
        alwayActiveTx.setText(LanguageManager.getInstance().getValue("alway_active"));
        settingHandmadeTx.setText(LanguageManager.getInstance().getValue("setting_handmade"));
        cancel.setText(LanguageManager.getInstance().getValue("cancel"));

        currentValue = getIntent().getBooleanExtra(StepThreeFragment.VALUE, false);
        this.settingHandmade.setChecked(false);
        this.alwayActive.setChecked(false);
        if (!currentValue){
            this.alwayActive.setChecked(true);
        } else {
            this.settingHandmade.setChecked(true);
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.SCHEDULE);
                data.putExtra(StepThreeFragment.VALUE, currentValue);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        this.settingHandmade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = true;
                    alwayActive.setChecked(false);
                }
            }
        });

        this.alwayActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = false;
                    settingHandmade.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private RadioButton settingHandmade;
    private RadioButton alwayActive;
    private boolean currentValue = false;
}

