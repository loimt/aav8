package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.FlagView;

public class CustomFlag extends FlagView {

    private TextView textView;
    private AlphaTileView alphaTileView;

    @SuppressLint("WrongViewCast")
    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        alphaTileView = findViewById(R.id.flag_color_layout);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        textView.setText("#" + colorEnvelope.getHexCode());
        alphaTileView.setPaintColor(colorEnvelope.getColor());
    }

}