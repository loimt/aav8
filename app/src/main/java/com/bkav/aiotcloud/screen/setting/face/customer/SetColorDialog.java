package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.user.EditUserProfile;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;

public class SetColorDialog extends Activity {
    public static final String TYPE = "type";
    public static final String COLOR = "COLOR";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_color_dialog);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        color = getIntent().getStringExtra(COLOR);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.title);

        ColorPickerView colorPickerView = findViewById(R.id.colorPickerView);
        BubbleFlag bubbleFlag = new BubbleFlag(this);
        bubbleFlag.setFlagMode(FlagMode.ALWAYS);
        colorPickerView.setFlagView(bubbleFlag);

        TextView colorTX = findViewById(R.id.colorTx);
        Button save = findViewById(R.id.save);
        title.setText(LanguageManager.getInstance().getValue("select_color"));
        save.setText(LanguageManager.getInstance().getValue("save"));


        colorPickerView.setInitialColor(Color.parseColor(color));
//        colorPickerView.setPureColor(Color.parseColor(color));
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                color = "#" + envelope.getHexCode().substring(2);
                colorTX.setText(color);
                colorTX.setTextColor(envelope.getColor());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataBack = new Intent();
                dataBack.putExtra(COLOR,color);
                setResult(RESULT_OK, dataBack);
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

    private String color;
}
