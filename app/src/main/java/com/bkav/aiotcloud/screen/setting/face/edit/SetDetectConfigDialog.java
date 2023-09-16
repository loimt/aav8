package com.bkav.aiotcloud.screen.setting.face.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;

public class SetDetectConfigDialog extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_detect_config);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);

        int currentValue = getIntent().getIntExtra(StepThreeFragment.VALUE, 10);
        TextView title = findViewById(R.id.titleTx);
        EditText timeInput = findViewById(R.id.timeWarningInput);
        timeInput.setText(String.valueOf(currentValue));

        Button ok = findViewById(R.id.ok);
        Button cacel = findViewById(R.id.cancel);
        cacel.setText(LanguageManager.getInstance().getValue("cancel"));
        title.setText(LanguageManager.getInstance().getValue("time_warning"));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeInput.getText().length() == 0) {
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("forget_input_time"), true);
                    return;
                } else {
                    Intent data = new Intent();
                    data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.TIME_WARNING);
                    data.putExtra(StepThreeFragment.VALUE, Integer.parseInt(timeInput.getText().toString()));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}


