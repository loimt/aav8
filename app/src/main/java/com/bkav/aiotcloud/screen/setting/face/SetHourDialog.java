package com.bkav.aiotcloud.screen.setting.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bkav.aiotcloud.R;

public class SetHourDialog  extends Activity {
    public static final String NAME = "name";

    public static final String POSITION = "position";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String TYPE = "type";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_hour);

        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);
        Button ok = findViewById(R.id.ok);
        TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

//        if (intent.getStringExtra(TYPE).equals(NEW)){
//
//        }

        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                if (type.equals(StepThreeFragment.TIME_END)){
                    data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.TIME_END);
                } else  if (type.equals(StepThreeFragment.TIME_START)){
                    data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.TIME_START);
                }

                data.putExtra(StepThreeFragment.VALUE, timePicker.getHour() + ":"+ timePicker.getMinute());
                setResult(RESULT_OK, data);
                finish();
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
}
