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

public class SetSizeFaceDialog extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_size_face);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.titleTx);
        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.ok);

        this.high = findViewById(R.id.high);
        this.medium = findViewById(R.id.medium);
        this.low = findViewById(R.id.low);
        this.veryBig = findViewById(R.id.veryBig);
        this.verySmall = findViewById(R.id.verySmall);

        this.veryBigTx = findViewById(R.id.veryBigTx);
        this.verySmallTx = findViewById(R.id.verySmallTx);
        this.bigTx = findViewById(R.id.big);
        this.mediumTx = findViewById(R.id.mediumText);
        this.lowTx = findViewById(R.id.smallTx);


        title.setText(LanguageManager.getInstance().getValue("size_face"));
        this.bigTx.setText(LanguageManager.getInstance().getValue("big"));
        this.mediumTx.setText(LanguageManager.getInstance().getValue("medium"));
        this.lowTx.setText(LanguageManager.getInstance().getValue("small"));
        this.veryBigTx.setText(LanguageManager.getInstance().getValue("veryBig"));
        this.verySmallTx.setText(LanguageManager.getInstance().getValue("verySmall"));

        currentValue = getIntent().getIntExtra(StepThreeFragment.VALUE, 1);
        this.high.setChecked(false);
        this.medium.setChecked(false);
        this.low.setChecked(false);
        this.veryBig.setChecked(false);
        this.verySmall.setChecked(false);


        switch (currentValue){
            case 1:
                this.veryBig.setChecked(true);
                break;
            case 2:
                this.high.setChecked(true);
                break;
            case 3:
                this.medium.setChecked(true);
                break;
            case 4:
                this.low.setChecked(true);
                break;
            case 5:
                this.verySmall.setChecked(true);
                break;

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
                data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.FACE);
                data.putExtra(StepThreeFragment.VALUE, currentValue);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        this.veryBig.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 1;
                    high.setChecked(false);
                    medium.setChecked(false);
                    low.setChecked(false);
                    verySmall.setChecked(false);

                }
            }
        });
        this.high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 2;
                    veryBig.setChecked(false);
                    medium.setChecked(false);
                    low.setChecked(false);
                    verySmall.setChecked(false);
                }
            }
        });
        this.verySmall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 5;
                    high.setChecked(false);
                    medium.setChecked(false);
                    low.setChecked(false);
                    veryBig.setChecked(false);
                }
            }
        });

        this.medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 3;
                    high.setChecked(false);
                    verySmall.setChecked(false);
                    low.setChecked(false);
                    veryBig.setChecked(false);
                }
            }
        });

        this.low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    currentValue = 4;
                    high.setChecked(false);
                    medium.setChecked(false);
                    verySmall.setChecked(false);
                    veryBig.setChecked(false);
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

    private RadioButton veryBig;
    private RadioButton verySmall;
    private RadioButton high;
    private RadioButton medium;
    private RadioButton low;


    private TextView veryBigTx;
    private TextView verySmallTx;

    private TextView bigTx;
    private TextView mediumTx;
    private TextView lowTx;

    private int currentValue = 160;
}

