package com.bkav.aiotcloud.screen.setting.license;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.license.LicenseGroupItem;
import com.bkav.aiotcloud.entity.license.LicenseItem;
import com.bkav.aiotcloud.entity.license.LicenseTypeItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddLicense extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.add_license);
        init();
        action();
        this.type = getIntent().getStringExtra(ListCustomerActivity.TYPE);
        if (this.type.equals(ProfileLicenseManage.EDIT)){
            int pos = getIntent().getIntExtra(ListCustomerActivity.ID, 0);
            licenseItem = ApplicationService.licenseItems.get(pos);
            this.licenseInput.setText(licenseItem.getNoPlate());
            this.noteInput.setText(licenseItem.getDescription());
            this.delete.setVisibility(View.VISIBLE);
            this.title.setText(LanguageManager.getInstance().getValue("edit_infor"));
        } else {
            // type new
            this.title.setText(LanguageManager.getInstance().getValue("add_new"));
        }
    }

    private LicenseItem licenseItem;
    private String type;
    private TextView licenseTx;
    private EditText licenseInput;

    private TextView noteTx;
    private TextView title;
    private EditText noteInput;


    private ArrayAdapter<String> adapterGroupLicense;

    private List<String> listType = new ArrayList<>();
    private List<String> listGroup = new ArrayList<>();
    private TextView groupLicenseTx;
    private Spinner spinnerGroupLicense;

    private ArrayAdapter<String> adapterTypelicense;
    private Spinner spinnerTypeVehicle;
    private TextView vehicleTypeTx;

    private Button delete;
    private Button save;
    private RelativeLayout backIm;

    private int positionTypeSelected = 0;
    private int positionGroupSelected = 0;


    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.licenseTypeItems.clear();
        ApplicationService.licenseGroupItems.clear();
        ApplicationService.volleyRequestManagement.getListLicenseGroup("true", "");
        ApplicationService.volleyRequestManagement.getListLicenseType( "licenseplate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void init(){
        licenseTx = findViewById(R.id.licenseTx);
        licenseInput = findViewById(R.id.licenseInput);

        noteTx = findViewById(R.id.noteTx);
        noteInput = findViewById(R.id.noteInput);

        groupLicenseTx = findViewById(R.id.groupLicenseTx);
        spinnerGroupLicense = findViewById(R.id.spinnerGroupLicense);
        spinnerTypeVehicle = findViewById(R.id.spinnerTypeVehicle);
        vehicleTypeTx = findViewById(R.id.vehicleTypeTx);

        delete = findViewById(R.id.delete);
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        backIm = findViewById(R.id.backIm);



        licenseTx.setText(LanguageManager.getInstance().getValue("license"));
        noteTx.setText(LanguageManager.getInstance().getValue("description"));
        groupLicenseTx.setText(LanguageManager.getInstance().getValue("group_license"));
        vehicleTypeTx.setText(LanguageManager.getInstance().getValue("transportation"));

        delete.setText(LanguageManager.getInstance().getValue("delete"));
        save.setText(LanguageManager.getInstance().getValue("save"));

        adapterGroupLicense = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listGroup) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == positionGroupSelected) {
                    text.setTextColor(getColor(R.color.mainColor));
                } else {
                    text.setTextColor(Color.WHITE);
                }
                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };


        adapterGroupLicense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupLicense.setAdapter(adapterGroupLicense);
        spinnerGroupLicense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                positionGroupSelected = index;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        adapterTypelicense = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listType) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == positionTypeSelected) {
                    text.setTextColor(getColor(R.color.mainColor));
                } else {
                    text.setTextColor(Color.WHITE);
                }
                return view;

            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        spinnerTypeVehicle.setDropDownVerticalOffset(100);

                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

                return view;
            }
        };


        adapterTypelicense.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeVehicle.setAdapter(adapterTypelicense);
        spinnerTypeVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                positionTypeSelected = index;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void action(){

        backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (licenseInput.getText().length() != 0){
                    addUpdateLicense();
                } else {
                    ApplicationService.showToast("dien bien so", true);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLicense();
            }
        });
    }

    private void deleteLicense() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_logout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        RelativeLayout btnCancel = dialog.findViewById(R.id.cancelLayoutLogout);
        RelativeLayout btnConfirm = dialog.findViewById(R.id.confirmLayoutLogout);
        TextView textViewConfirm = dialog.findViewById(R.id.contentConfirmText);
        TextView cancel = dialog.findViewById(R.id.titleCancleLogout);
        TextView confirm = dialog.findViewById(R.id.titleConfirmLogout);

        textViewConfirm.setText(String.format(LanguageManager.getInstance().getValue("delete_license_question"),licenseItem.getNoPlate()));
        cancel.setText(LanguageManager.getInstance().getValue("cancel"));
        confirm.setText(LanguageManager.getInstance().getValue("confirm"));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                @SuppressLint("DefaultLocale") String url = String.format(ApplicationService.URL_DELETE_LICENSE,
                        licenseItem.getLicensePlateGuid());
                ApplicationService.volleyRequestManagement.deleteLicense(url);
            }
        });
    }


    private void addUpdateLicense(){
        JSONObject payload = new JSONObject();

        try {
            if(type.equals(ProfileLicenseManage.EDIT)){
                payload.put("licensePlateGuid", licenseItem.getLicensePlateGuid());
            } else {
                payload.put("licensePlateGuid","");
            }
            payload.put("active", true);
            payload.put("description", noteInput.getText());
            payload.put("noPlate", licenseInput.getText());
            payload.put("objectDetectTypeId",  ApplicationService.licenseTypeItems.get(positionTypeSelected).getObjectDetectTypeId());
            payload.put("licensePlateTypeGuid",((LicenseGroupItem) ApplicationService.licenseGroupItems.get(positionGroupSelected)).getObjectGuid());

            Log.e("addlicensexxxxxxx", payload.toString() );
            ApplicationService.volleyRequestManagement.addUPdateLicense(ApplicationService.URL_ADD_UPDATE_LICENSE, payload);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private class MainHandler extends Handler {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_LICENSE_GROUP_SUCCESS:
                    listGroup.clear();
                    for (TypeAIObject licenseGroupItem : ApplicationService.licenseGroupItems){
                        listGroup.add(licenseGroupItem.getName());
                        if(type.equals(ProfileLicenseManage.EDIT)){
                            if (licenseGroupItem.getName().equals(licenseItem.getLicensePlateTypeName())) {
                                spinnerGroupLicense.setSelection( ApplicationService.licenseGroupItems.indexOf(licenseGroupItem));
                            }
                        }
                    }
                    adapterGroupLicense.notifyDataSetChanged();
                    break;
                case ApplicationService.GET_LIST_LICENSE_TYPE_SUCCESS:
                    listType.clear();
                    for (LicenseTypeItem licenseTypeItem : ApplicationService.licenseTypeItems){
                        listType.add(licenseTypeItem.getObjectDetectTypeName());
                        if(type.equals(ProfileLicenseManage.EDIT)){
                            if (licenseTypeItem.getObjectDetectTypeName().equals(licenseItem.getObjectDetectTypeName())){
                                spinnerTypeVehicle.setSelection(ApplicationService.licenseTypeItems.indexOf(licenseTypeItem));
                            }
                        }
                    }
                    adapterTypelicense.notifyDataSetChanged();
                    break;

                case ApplicationService.ADD_UPDATE_LICENSE_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent dataBackDelete = new Intent();
                    dataBackDelete.putExtra(ListCustomerActivity.TYPE, type);
                    setResult(RESULT_OK, dataBackDelete);
                    finish();
                case ApplicationService.UPDATE_CAM_FAILE:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_un_success"), true);
                    finish();
                    break;
            }
        }
    }

    private class spinnerTypeVehicle {
    }
}
