package com.bkav.aiotcloud.screen.setting.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;

public class SetNameZone extends Activity {
    public static final String NAME = "name";

    public static final String POSITION = "position";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String TYPE = "type";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_zone_name);
        setFinishOnTouchOutside(false);

        TextView title = findViewById(R.id.titleTx);
        TextView warningTx = findViewById(R.id.warningTx);

        EditText nameZone = findViewById(R.id.nameZoneInput);
        Button cancel = findViewById(R.id.cancel);
        Button ok = findViewById(R.id.ok);

        warningTx.setText(LanguageManager.getInstance().getValue("empty_name_zone"));
        nameZone.setHint(LanguageManager.getInstance().getValue("hint_name_zone"));
        title.setText(LanguageManager.getInstance().getValue("name_zone"));
        cancel.setText(LanguageManager.getInstance().getValue("cancel"));

        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);
        if (type.equals(EDIT)){
            nameZone.setText(intent.getStringExtra(NAME));

        }
//        if (intent.getStringExtra(TYPE).equals(NEW)){
//
//        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameZone.getText().length() == 0){
                    warningTx.setVisibility(View.VISIBLE);
                    return;
                } else {
                    warningTx.setVisibility(View.GONE);
                }
                Intent data = new Intent();
                data.putExtra(TYPE, type);
               if(type.equals(EDIT)){
                   data.putExtra(POSITION, intent.getIntExtra(POSITION,  0));
                }
                data.putExtra(NAME, nameZone.getText().toString());
                setResult(RESULT_OK, data);
                nameZone.onEditorAction(EditorInfo.IME_ACTION_DONE);
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
