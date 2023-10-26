package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_customer);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        currentStatus = intent.getIntExtra(ListCustomerActivity.STATUS, -1);
        currentSex = intent.getIntExtra(ListCustomerActivity.SEX, -1);
//        String listGroup = intent.getStringExtra(ListCustomerActivity.LIST_TYPE);

        init();
        action();
        setCurrent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //
    private final int all = -1;
    private final int male = 0;
    private final int female = 1;
    private final int other = 2;

    private final int active = 1;
    private final int inactive = 0;
    private final int allStatus = -1;
    //
    private TextView titleTx;
    private TextView sexTx;

    private TextView allSexTx;
    private RadioButton allCheck;


    private TextView maleTx;
    private RadioButton maleCheck;

    private TextView femaleTx;
    private RadioButton femaleCheck;

    private TextView otherTx;
    private RadioButton otherCheck;

    private TextView statusTx;
    private TextView allStatusTx;
    private RadioButton allStatusCheck;

    private TextView activingTx;
    private RadioButton activingCheck;

    private TextView inactiveTx;
    private RadioButton inacticeCheck;

    private RelativeLayout reload;

    private TextView typeObjectTx;
    private Button save;


    private RecyclerView listType;
    private TypeObjectAdapter typeCustomerAdapter;

    private void setCurrent(){
        switch (currentSex) {
            case all:
                allCheck.setChecked(true);
                break;
            case male:
                maleCheck.setChecked(true);
                break;
            case female:
                femaleCheck.setChecked(true);
                break;
            case other:
                otherCheck.setChecked(true);
                break;
        }

        switch (currentStatus) {
            case allStatus:
                allStatusCheck.setChecked(true);
                break;
            case active:
                activingCheck.setChecked(true);
                break;
            case inactive:
                inacticeCheck.setChecked(true);
                break;
        }

//        if (listGroup.length() > 0){
//            String[] list = listGroup.split(",");
//            for (TypeCustomerItem typeCustomerItem : ApplicationService.typeCustomerItems){
//                for (int index : )
//                if (typeCustomerItem.isChoose()){
//                    listId.add(typeCustomerItem.getCustomerTypeId());
//                }
//            }
//        }
    }


    private void init() {
        this.titleTx = findViewById(R.id.titleTx);
        this.sexTx = findViewById(R.id.sexTx);

        this.allSexTx = findViewById(R.id.allSexTx);
        this.allCheck = findViewById(R.id.allCheck);

        this.maleTx = findViewById(R.id.maleTx);
        this.maleCheck = findViewById(R.id.maleCheck);

        this.femaleTx = findViewById(R.id.femaleTx);
        this.femaleCheck = findViewById(R.id.femaleCheck);


        this.otherCheck = findViewById(R.id.otherCheck);
        this.otherTx = findViewById(R.id.otherTx);

        this.statusTx = findViewById(R.id.statusTx);

        this.allStatusCheck = findViewById(R.id.allStatusCheck);
        this.allStatusTx = findViewById(R.id.allStatusTx);

        this.activingTx = findViewById(R.id.activingTx);
        this.activingCheck = findViewById(R.id.activingCheck);


        this.inactiveTx = findViewById(R.id.inactiveTx);
        this.inacticeCheck = findViewById(R.id.inacticeCheck);

        this.reload = findViewById(R.id.reload);
        this.save = findViewById(R.id.save);
        this.typeObjectTx = findViewById(R.id.typeObjectTx);

        this.listType = findViewById(R.id.listType);
        listType.setLayoutManager(new LinearLayoutManager(this));
        typeCustomerAdapter = new TypeObjectAdapter(this, ApplicationService.typeCustomerItems);
        listType.setAdapter(typeCustomerAdapter);
        listType.setNestedScrollingEnabled(false);

        this.allCheck.setChecked(true);
        this.activingCheck.setChecked(true);

        this.titleTx.setText(LanguageManager.getInstance().getValue("filter"));
        this.allSexTx.setText(LanguageManager.getInstance().getValue("all"));
        this.allStatusTx.setText(LanguageManager.getInstance().getValue("all"));
        this.activingTx.setText(LanguageManager.getInstance().getValue("activing"));
        this.inactiveTx.setText(LanguageManager.getInstance().getValue("inactive"));
        this.statusTx.setText(LanguageManager.getInstance().getValue("status"));

        this.sexTx.setText(LanguageManager.getInstance().getValue("sex"));
        this.maleTx.setText(LanguageManager.getInstance().getValue("male"));
        this.femaleTx.setText(LanguageManager.getInstance().getValue("female"));
        this.otherTx.setText(LanguageManager.getInstance().getValue("other"));
        this.typeObjectTx.setText(LanguageManager.getInstance().getValue("type_object"));

    }

    private int currentStatus = 1;
    private int currentSex = -1;
    private void action() {
        this.allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckSex(all);
                    }
                }
            }
        });

        this.maleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckSex(male);
                    }
                }
            }
        });

        this.femaleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckSex(female);
                    }
                }
            }
        });

        this.otherCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckSex(other);
                    }
                }
            }
        });


        this.allStatusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(all);
                    }
                }
            }
        });

        this.activingCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(active);
                    }
                }
            }
        });

        this.inacticeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                {
                    if (compoundButton.isChecked()) {
                        resetCheckStatus(inactive);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataBack = new Intent();
                dataBack.putExtra(ListCustomerActivity.TYPE, ListCustomerActivity.FILTER);
                dataBack.putExtra(ListCustomerActivity.STATUS, currentStatus);
                dataBack.putExtra(ListCustomerActivity.SEX, currentSex);
                dataBack.putExtra(ListCustomerActivity.LIST_TYPE, getListType());
                setResult(RESULT_OK, dataBack);
                finish();
            }
        });

        this.reload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                allCheck.setChecked(true);
                activingCheck.setChecked(true);
                resetCheckSex(all);
                resetCheckStatus(active);
                for (TypeAIObject typeCustomerItem : ApplicationService.typeCustomerItems){
                    ((TypeCustomerItem) typeCustomerItem).setChoose(false);
                }
                typeCustomerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void resetCheckSex(int value) {
        this.currentSex = value;
        if (value == all) {
            maleCheck.setChecked(false);
            femaleCheck.setChecked(false);
            otherCheck.setChecked(false);
        } else if (value == male) {
            femaleCheck.setChecked(false);
            allCheck.setChecked(false);
            otherCheck.setChecked(false);
        } else if (value == female) {
            maleCheck.setChecked(false);
            allCheck.setChecked(false);
            otherCheck.setChecked(false);
        } else if (value == other) {
            maleCheck.setChecked(false);
            femaleCheck.setChecked(false);
            allCheck.setChecked(false);
        }
    }

    private void resetCheckStatus(int value) {
        currentStatus = value;
        if (value == all) {
            activingCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == active) {
            allStatusCheck.setChecked(false);
            inacticeCheck.setChecked(false);
        } else if (value == inactive) {
            allStatusCheck.setChecked(false);
            activingCheck.setChecked(false);
        }
    }

    private String getListType(){
        ArrayList<Integer> listId = new ArrayList<>();
        for (TypeAIObject typeCustomerItem : ApplicationService.typeCustomerItems){
            if (((TypeCustomerItem) typeCustomerItem).isChoose()){
                listId.add(typeCustomerItem.getID());
            }
        }
        return listId.toString().replace("[", "").replace("]", "");
    }
}


