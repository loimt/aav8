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

public class SetSensitivityDialog extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_sensitivity);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.titleTx);
        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.ok);


        this.veryHigh = findViewById(R.id.veryHigh);
        this.veryLow = findViewById(R.id.veryLow);
        this.high = findViewById(R.id.high);
        this.medium = findViewById(R.id.medium);
        this.low = findViewById(R.id.low);

        this.veryHighTx = findViewById(R.id.veryHighText);
        this.veryLowTx = findViewById(R.id.veryLowText);
        this.highTx = findViewById(R.id.highText);
        this.mediumTx = findViewById(R.id.mediumText);
        this.lowTx = findViewById(R.id.lowText);

        cancel.setText(LanguageManager.getInstance().getValue("cancel"));

        this.highTx.setText(LanguageManager.getInstance().getValue("high"));
        this.mediumTx.setText(LanguageManager.getInstance().getValue("medium"));
        this.lowTx.setText(LanguageManager.getInstance().getValue("low"));

        this.veryHighTx.setText(LanguageManager.getInstance().getValue("veryHigh"));
        this.veryLowTx.setText(LanguageManager.getInstance().getValue("veryLow"));

        currentValue = getIntent().getIntExtra(StepThreeFragment.VALUE, 1);
        this.high.setChecked(false);
        this.medium.setChecked(false);
        this.low.setChecked(false);
        this.veryHigh.setChecked(false);
        this.veryLow.setChecked(false);
//        if (currentValue == 0.82){
//            this.high.setChecked(true);
//        } else if (currentValue == 0.78){
//            this.medium.setChecked(true);
//        }else if (currentValue == 0.74){
//            this.low.setChecked(true);
//        }

        switch (currentValue) {
            case 1:
                this.veryLow.setChecked(true);
                break;
            case 2:
                this.low.setChecked(true);
                break;
            case 3:
                this.medium.setChecked(true);
                break;
            case 4:
                this.high.setChecked(true);
                break;
            case 5:
                this.veryHigh.setChecked(true);
                break;
        }
            title.setText(LanguageManager.getInstance().getValue("sensitivity"));


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
                data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.SENSITIVITY);
                data.putExtra(StepThreeFragment.VALUE, currentValue);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        this.veryLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 1;

                    low.setChecked(false);
                    medium.setChecked(false);
                    high.setChecked(false);
                    veryHigh.setChecked(false);
                }
            }
        });
        this.low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 2;

                    veryLow.setChecked(false);
                    medium.setChecked(false);
                    high.setChecked(false);
                    veryHigh.setChecked(false);
                }
            }
        });

        this.medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 3;

                    veryLow.setChecked(false);
                    low.setChecked(false);
                    high.setChecked(false);
                    veryHigh.setChecked(false);
                }
            }
        });

        this.high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 4;

                    low.setChecked(false);
                    medium.setChecked(false);
                    high.setChecked(false);
                    veryHigh.setChecked(false);
                }
            }
        });

        this.veryHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 5;

                    veryLow.setChecked(false);
                    medium.setChecked(false);
                    high.setChecked(false);
                    low.setChecked(false);
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

    private RadioButton veryHigh;
    private RadioButton veryLow;
    private RadioButton high;
    private RadioButton medium;
    private RadioButton low;

    private TextView veryLowTx;
    private TextView veryHighTx;
    private TextView highTx;
    private TextView mediumTx;
    private TextView lowTx;

    private int currentValue = 0;
}

