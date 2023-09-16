package com.bkav.aiotcloud.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bkav.aiotcloud.R;

public class WrongPassDialog extends Activity {

    public static final String TEXT = "text";
    public static final String TITLE = "title";
    public static final String TYPE = "type";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notice);

        TextView title = findViewById(R.id.noticeTitle);
        TextView text = findViewById(R.id.text);
        Button close = findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
