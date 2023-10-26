package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DetailTypeObject extends AppCompatActivity {
    public static String TAG = "DtailTypeObject";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        this.type = intent.getStringExtra(ListTypeAI.TYPE);
        setContentView(R.layout.type_object_detail);
        init();
        setDataLan();
        if (this.type.equals(ListCustomerActivity.EDIT)) {
            this.typeCustomerItem = ApplicationService.typeCustomerItems.get(intent.getIntExtra(ListTypeAI.ID, 0));
            this.title.setText(LanguageManager.getInstance().getValue("edit"));
            setData();
        } else if (type.equals(ListTypeAI.NEW)){
            setCheckedColor(0);
            this.deleteIM.setVisibility(View.GONE);
            this.title.setText(LanguageManager.getInstance().getValue("add_new"));
        }
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }
    private void setDataLan(){
        this.nameTx.setText(LanguageManager.getInstance().getValue("profile_type_name"));
        this.nameInput.setHint(LanguageManager.getInstance().getValue("profile_type_name_hint"));
        this.colorTx.setText(LanguageManager.getInstance().getValue("profile_type_color"));
        this.descripTx.setText(LanguageManager.getInstance().getValue("profile_type_describe"));
        this.descripInput.setHint(LanguageManager.getInstance().getValue("profile_type_describe_hint"));
        this.statusTx.setText(LanguageManager.getInstance().getValue("status"));
        this.selectWarningTx.setText(LanguageManager.getInstance().getValue("profile_type_notify"));
        this.soundCheck.setText(LanguageManager.getInstance().getValue("profile_type_sound"));
        this.blinkCheck.setText(LanguageManager.getInstance().getValue("profile_type_flicker"));
        this.save.setText(LanguageManager.getInstance().getValue("save"));
    }

    private void setData() {

        this.nameInput.setText(this.typeCustomerItem.getName());
        this.descripInput.setText(this.typeCustomerItem.getDesciption());
        if (typeCustomerItem.getIdentify().contains("popup")) {
            popupCheck.setChecked(true);
        }

        if (typeCustomerItem.getIdentify().contains("sound")) {
            soundCheck.setChecked(true);
        }

        if (typeCustomerItem.getIdentify().contains("flicker")) {
            blinkCheck.setChecked(true);
        }

        swOnOffStatus.setChecked(typeCustomerItem.isActive());

//        Log.e(TAG, typeCustomerItem.getBackGroundColor());

        for (String element : colors) {
            isBasic = false;
            if (typeCustomerItem.getBackground().equalsIgnoreCase(element)) {
                isBasic = true;
            }
        }
        if (!isBasic) {
            colors.add(8, typeCustomerItem.getBackground());
        }

        for (int index = 0; index < colors.size(); index++) {
            if (typeCustomerItem.getBackground().equalsIgnoreCase(colors.get(index))) {
                setCheckedColor(index);
            }
        }

        this.deleteIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("DefaultLocale") String url = String.format(ApplicationService.URL_DELETE_TYPE_CUSTOMER, typeCustomerItem.getID());
                ApplicationService.requestManager.deleteTypeCustomer(url);
            }
        });
    }

    private void init() {
        this.c1 = findViewById(R.id.c1);
        this.c2 = findViewById(R.id.c2);
        this.c3 = findViewById(R.id.c3);
        this.c4 = findViewById(R.id.c4);
        this.c5 = findViewById(R.id.c5);
        this.c6 = findViewById(R.id.c6);
        this.c7 = findViewById(R.id.c7);
        this.c8 = findViewById(R.id.c8);

        this.t1 = findViewById(R.id.t1);
        this.t2 = findViewById(R.id.t2);
        this.t3 = findViewById(R.id.t3);
        this.t4 = findViewById(R.id.t4);
        this.t5 = findViewById(R.id.t5);
        this.t6 = findViewById(R.id.t6);
        this.t7 = findViewById(R.id.t7);
        this.t8 = findViewById(R.id.t8);
        this.tselect = findViewById(R.id.tselect);

        this.selectColor = findViewById(R.id.colorSelect);

        this.colors = Stream.of("#FD413C", "#E0A818", "#4AA541", "#FD7B38", "#3BB7FD", "#818181", "#1C9AAB", "#6D45C0").collect(Collectors.toList());

        this.colorLayout = Stream.of(c1, c2, c3, c4, c5, c6, c7, c8, selectColor).collect(Collectors.toList());
        this.listTick = Stream.of(t1, t2, t3, t4, t5, t6, t7, t8, tselect).collect(Collectors.toList());

        for (int index = 0; index < colors.size(); index++) {
            this.colorLayout.get(index).setBackgroundResource(R.drawable.retangcle);
            GradientDrawable drawable = (GradientDrawable) this.colorLayout.get(index).getBackground();
            drawable.setColor(Color.parseColor(colors.get(index)));
        }

        this.nameTx = findViewById(R.id.nameTx);
        this.nameInput = findViewById(R.id.nameInput);

        this.descripTx = findViewById(R.id.descripTx);
        this.descripInput = findViewById(R.id.descripInput);

        this.statusTx = findViewById(R.id.statusTx);
        this.swOnOffStatus = findViewById(R.id.swOnOffStatus);

        this.statusTx = findViewById(R.id.statusTx);
        this.swOnOffStatus = findViewById(R.id.swOnOffStatus);
        this.statusTx = findViewById(R.id.statusTx);
        this.swOnOffStatus = findViewById(R.id.swOnOffStatus);

        this.selectWarningTx = findViewById(R.id.selectWarningTx);
        this.popupCheck = findViewById(R.id.popupCheck);
        this.soundCheck = findViewById(R.id.soundCheck);
        this.blinkCheck = findViewById(R.id.blinkCheck);

        this.save = findViewById(R.id.save);
        this.colorTx = findViewById(R.id.colorTx);
        this.add = findViewById(R.id.add);
        this.selectColor = findViewById(R.id.colorSelect);
        this.backIM = findViewById(R.id.backIm);

        this.title = findViewById(R.id.title);
        this.deleteIM = findViewById(R.id.deleteIM);

        this.nameTx.setText(LanguageManager.getInstance().getValue("name_object"));
        this.colorTx.setText(LanguageManager.getInstance().getValue("select_color"));
        this.descripTx.setText(LanguageManager.getInstance().getValue("description"));
        this.statusTx.setText(LanguageManager.getInstance().getValue("status"));
        this.selectWarningTx.setText(LanguageManager.getInstance().getValue("select_type_warning"));
        this.popupCheck.setText(LanguageManager.getInstance().getValue("popup"));
        this.soundCheck.setText(LanguageManager.getInstance().getValue("sound"));
        this.blinkCheck.setText(LanguageManager.getInstance().getValue("blink"));

    }

    private String currentProfile = "";
    private RelativeLayout c1;
    private RelativeLayout c2;
    private RelativeLayout c3;
    private RelativeLayout c4;
    private RelativeLayout c5;
    private RelativeLayout c6;
    private RelativeLayout c7;
    private RelativeLayout c8;

    private ImageView t1;
    private ImageView t2;
    private ImageView t3;
    private ImageView t4;
    private ImageView t5;
    private ImageView t6;
    private ImageView t7;
    private ImageView t8;

    private ImageView tselect;
    private RelativeLayout selectColor;
    private ImageView add;
    private String type;
    private TypeAIObject typeCustomerItem;
    private boolean isBasic = false;

    private TextView title;
    private RelativeLayout deleteIM;

    private TextView nameTx;
    private EditText nameInput;

    private TextView descripTx;
    private EditText descripInput;

    private TextView statusTx;
    private SwitchCompat swOnOffStatus;

    private TextView selectWarningTx;
    private CheckBox popupCheck;
    private CheckBox soundCheck;
    private CheckBox blinkCheck;

    private Button save;
    private RelativeLayout backIM;

    private TextView colorTx;

    private List<String> colors;
    private List<RelativeLayout> colorLayout;
    private List<ImageView> listTick;
    private String color;
    private int currentBackground = 0;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        color = data.getExtras().getString(SetColorDialog.COLOR);
                        colors.add(8, color);
                        setCheckedColor(8);
                    }
                }
            });

    private void setCheckedColor(int position) {
        currentBackground = position;
        for (int index = 0; index < colorLayout.size(); index++) {
            listTick.get(index).setVisibility(View.GONE);
        }
        listTick.get(position).setVisibility(View.VISIBLE);

        if (position == 8) {
//            tselect.setVisibility(View.VISIBLE);
            this.selectColor.setVisibility(View.VISIBLE);
            this.selectColor.setBackgroundResource(R.drawable.retangcle);
            GradientDrawable drawable = (GradientDrawable) this.selectColor.getBackground();
            try {
                drawable.setColor(Color.parseColor(colors.get(position)));
            } catch (Exception e){

            }
        }
    }

    private void action() {
        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for (RelativeLayout layout : colorLayout) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckedColor(colorLayout.indexOf(layout));
                }
            });
        }

        this.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SetColorDialog.class);
                intent.setFlags(0);
                if (type.equals(ListTypeAI.EDIT)) {
                    intent.putExtra(SetColorDialog.COLOR, typeCustomerItem.getBackground());
                } else {
                    intent.putExtra(SetColorDialog.COLOR, "#ffffff");
                }
                startActivityIntent.launch(intent);
            }
        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify()){
                    ApplicationService.requestManager.addUpdateCustomerType(getPayload(), ApplicationService.URL_ADD_UPDATE_CUSTOMER_TYPE);
                }
            }
        });
    }

    private boolean verify(){
        if (nameInput.getText().length() == 0){
            ApplicationService.showToast(LanguageManager.getInstance().getValue("empty_name_object"), true);
            return false;
        }
        return true;
    }

    private JSONObject getPayload() {
        JSONObject payload = new JSONObject();
        JSONArray handerTypeNotification = new JSONArray();
        if (popupCheck.isChecked()) {
            handerTypeNotification.put("popup");
        }
        if (blinkCheck.isChecked()) {
            handerTypeNotification.put("flicker");
        }
        if (soundCheck.isChecked()) {
            handerTypeNotification.put("sound");
        }
        try {
            payload.put("backGroundColor", colors.get(currentBackground));
            payload.put("customerTypeName", nameInput.getText());
            payload.put("description", descripInput.getText());
            payload.put("filePathBackGroundImage", "string");
            payload.put("handerTypeNotification", handerTypeNotification);
            payload.put("active", swOnOffStatus.isChecked());
            if (type.equals(ListTypeAI.NEW)) {
                payload.put("customerTypeId", 0);
            } else if (type.equals(ListTypeAI.EDIT)) {
                payload.put("customerTypeId", typeCustomerItem.getID());
                if (currentProfile.equals(ListTypeAI.FACE))
                payload.put("isUnknow", ((TypeCustomerItem) typeCustomerItem).isUnknow());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.ADD_UPDATE_TYPE_CUSTOMER_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent dataBack = new Intent();
                    dataBack.putExtra(ListCustomerActivity.TYPE, ListTypeAI.EDIT);
                    setResult(RESULT_OK, dataBack);
                    finish();
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    String er = (String) message.obj;
                    ApplicationService.showToast(er, true);
                    finish();
                    break;
                case ApplicationService.DELETE_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent datadelete = new Intent();
                    datadelete.putExtra(ListCustomerActivity.TYPE, ListTypeAI.EDIT);
                    setResult(RESULT_OK, datadelete);
                    finish();
            }
        }
    }
}