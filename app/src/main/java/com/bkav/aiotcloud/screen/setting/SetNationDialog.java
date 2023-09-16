package com.bkav.aiotcloud.screen.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;

public class SetNationDialog extends Activity {
    public static final String VALUE = "value";
    public static final String NEW = "new";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_nation);
        setFinishOnTouchOutside(false);

        Window window = this.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        this.title = findViewById(R.id.titleTx);

        this.cancel = findViewById(R.id.cancel);
        this.ok = findViewById(R.id.ok);

        this.layoutUSA = findViewById(R.id.layoutUSA);
        this.usaTx = findViewById(R.id.usaTx);
        this.tickUSA = findViewById(R.id.tickUSA);

        this.layoutVN = findViewById(R.id.layoutVN);
        this.vnTx = findViewById(R.id.vnTx);
        this.tickVN = findViewById(R.id.tickVN);

        this.layoutMalay = findViewById(R.id.layoutMalay);
        this.malayTx = findViewById(R.id.malayTx);
        this.tickMalay = findViewById(R.id.tickMalay);

        setValueNation(getIntent().getStringExtra(VALUE));
        setLanguage();
        action();

    }

    private void action() {
        this.layoutVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValueNation("VN");
            }
        });
        this.layoutMalay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValueNation("MY");
            }
        });
        this.layoutUSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValueNation("USA");
            }
        });

        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(StepThreeFragment.TYPE, StepThreeFragment.NATION);
                data.putExtra(StepThreeFragment.VALUE, currentNation);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void setLanguage() {
        this.title.setText(LanguageManager.getInstance().getValue("nation"));
        this.usaTx.setText(LanguageManager.getInstance().getValue("usa"));
        this.vnTx.setText(LanguageManager.getInstance().getValue("vietnam"));
        this.malayTx.setText(LanguageManager.getInstance().getValue("malaysia"));
    }

    private void setValueNation(String value) {
        Log.e("nation ", value);
        this.currentNation = value;
        if (value.equals("VN")) {
            this.tickVN.setVisibility(View.VISIBLE);
            this.tickMalay.setVisibility(View.GONE);
            this.tickUSA.setVisibility(View.GONE);
        } else if (value.equals("USA")) {
            this.tickVN.setVisibility(View.GONE);
            this.tickMalay.setVisibility(View.GONE);
            this.tickUSA.setVisibility(View.VISIBLE);
        } else if (value.equals("MY")) {
            this.tickVN.setVisibility(View.GONE);
            this.tickMalay.setVisibility(View.VISIBLE);
            this.tickUSA.setVisibility(View.GONE);
        }
    }

    private RelativeLayout layoutUSA;
    private TextView usaTx;
    private ImageView tickUSA;
    private TextView title;

    private RelativeLayout layoutVN;
    private TextView vnTx;
    private ImageView tickVN;

    private RelativeLayout layoutMalay;
    private TextView malayTx;
    private ImageView tickMalay;

    private String currentNation;
    private Button cancel;
    private Button ok;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

