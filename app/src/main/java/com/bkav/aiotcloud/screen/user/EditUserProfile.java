package com.bkav.aiotcloud.screen.user;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.customer.SetTimeDialog;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditUserProfile extends AppCompatActivity {
    public static final String TAG = EditUserProfile.class.getName();
    public static String TIME = "time";
    private String time = "";
    private File avatarFile;
    private List<String> listSex = new ArrayList<>();
    private ImageView avatar;
    private RelativeLayout backIM;
    private ArrayAdapter<String> adapterSex;
    private Spinner spinnerSex;
    private boolean isAvatar = false;
    private LinearLayout.LayoutParams imageParam;
    private LinearLayout.LayoutParams deleteParam;
    private int positionSexSelected = 0;
    private Button save;
    private EditText fullnameInput;
    private TextView dateOfBirthValue;
    private EditText emailInput;
    private Uri mCropImageUri;
    private Thread updateImageThread;
    private ArrayList<File> images;
    private EditText phoneNumberInput;
    private EditText adressInput;
    private RelativeLayout setTimeLayout;
    private TextView title;
    private TextView fullNameTx;
    private TextView birthTx;
    private TextView sexTx;
    private TextView phoneNumberTx;
    private TextView addressTx;
    private TextView editAvaTx;

    private void init() {
        this.title = findViewById(R.id.titleEditProfile);
        this.setTimeLayout = findViewById(R.id.dateOfBirthLayoutProfile);
        this.backIM = findViewById(R.id.backButton);
        this.avatar = findViewById(R.id.avatarProfile);
        this.save = findViewById(R.id.saveProfile);
        this.fullnameInput = findViewById(R.id.fullnameProfile);
        this.dateOfBirthValue = findViewById(R.id.dateOfBirthProfile);
        this.emailInput = findViewById(R.id.emailProfile);
        this.spinnerSex = findViewById(R.id.spinnerSexProfile);
        this.phoneNumberInput = findViewById(R.id.phoneNumberProfile);
        this.adressInput = findViewById(R.id.adressProfile);
        this.imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.imageParam.setMargins(15, 0, 0, 0);
        this.deleteParam = new LinearLayout.LayoutParams(40, 40);
        this.deleteParam.setMargins(220, 20, 0, 0);
        this.listSex.add(LanguageManager.getInstance().getValue("male"));
        this.listSex.add(LanguageManager.getInstance().getValue("female"));
        this.listSex.add(LanguageManager.getInstance().getValue("other"));
        this.fullNameTx = findViewById(R.id.fullNameTx);
        this.birthTx = findViewById(R.id.dateOfBirthTx);
        this.sexTx = findViewById(R.id.sexTx);
        this.phoneNumberTx = findViewById(R.id.phoneNumberlTx);
        this.addressTx = findViewById(R.id.addressTx);
        this.editAvaTx = findViewById(R.id.editAvaTx);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_edit_user_profile);
        init();
        action();
        setAdapter();
        setData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
//        setData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateImageThread != null) {
            updateImageThread.interrupt();
        }

    }

    private void setData(){
        this.title.setText(LanguageManager.getInstance().getValue("userprofile"));
        this.fullNameTx.setText(LanguageManager.getInstance().getValue("fullname"));
        this.fullnameInput.setHint(LanguageManager.getInstance().getValue("hint_fullname"));
        this.birthTx.setText(LanguageManager.getInstance().getValue("date_of_birth"));
        this.emailInput.setHint(LanguageManager.getInstance().getValue("hint_email"));
        this.sexTx.setText(LanguageManager.getInstance().getValue("gender"));
        this.phoneNumberTx.setText(LanguageManager.getInstance().getValue("phone_number"));
        this.phoneNumberInput.setHint(LanguageManager.getInstance().getValue("hint_phone_number"));
        this.addressTx.setText(LanguageManager.getInstance().getValue("address"));
        this.adressInput.setHint(LanguageManager.getInstance().getValue("hint_address"));
        this.editAvaTx.setText(LanguageManager.getInstance().getValue("edit"));
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        if (ApplicationService.user != null){
            if(ApplicationService.user.getAvatar().equals("null")){
                String fnm = "user_default";
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
                Glide.with(getApplicationContext())
                        .load(imgId)
                        .circleCrop()
                        .into(this.avatar);
            }else{
                Glide.with(getApplicationContext())
                        .load(ApplicationService.user.getAvatar())
                        .circleCrop()
                        .into(this.avatar);
            }
            if(!ApplicationService.user.getFullName().equals("null")){
                fullnameInput.setText(ApplicationService.user.getFullName());
            }else{
                fullnameInput.setText("");
            }
            if(ApplicationService.user.getDateOfBirth().indexOf("T")==-1){
                dateOfBirthValue.setText(ApplicationService.user.getDateOfBirth());
            }else{
                String[] dateArray = ApplicationService.user.getDateOfBirth().split("T")[0].split("-");
                String date = dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
                dateOfBirthValue.setText(date);
            }
            if(!ApplicationService.user.getEmail().equals("null")){
                emailInput.setText(ApplicationService.user.getEmail());
            }else {
                emailInput.setText("");
            }
            spinnerSex.setSelection(ApplicationService.user.getGender());

            if(!ApplicationService.user.getPhone().equals("null")){
                phoneNumberInput.setText(ApplicationService.user.getPhone());
            }else {
                phoneNumberInput.setText("");
            }
            if(!ApplicationService.user.getAddress().equals("null")){
                adressInput.setText(ApplicationService.user.getAddress());
            }else {
                adressInput.setText("");
            }
        }
    }
    private void setAdapter() {
        adapterSex = new ArrayAdapter<String>(EditUserProfile.this, android.R.layout.simple_spinner_dropdown_item, listSex) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == positionSexSelected) {
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
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapterSex);
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionSexSelected = i;
                ((TextView) view).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void action() {
        this.setTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String[] date = dateOfBirthValue.getText().toString().split("/");
                Intent intent = new Intent(getApplication(), SetTimeDialog.class);
//                intent.putExtra("date", date[0]);
//                intent.putExtra("month", date[1]);
//                intent.putExtra("year", date[2]);
                intent.putExtra(SetTimeDialog.TIME, dateOfBirthValue.getText().toString());
                intent.setFlags(0);
                startActivityIntent.launch(intent);
            }
        });
        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = true;
                onSelectImageClick(view);
            }
        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: " + getPayload() );
                ApplicationService.requestManager.updateUserProfile(getPayload(), ApplicationService.URL_UPDATE_USERPROFILE);
            }
        });
    }


    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        time = data.getExtras().getString(TIME);
                        dateOfBirthValue.setText(time.replace("-", "/"));
                    }
                }
            });

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            getDropboxIMGSize(result.getUri());
            if (resultCode == RESULT_OK) {
                if (isAvatar) {
                    Glide.with(this).load(result.getUri()).circleCrop().into(avatar);
                    this.avatarFile = new File(result.getUri().getPath());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .setRequestedSize(400, 400)
                .start(this);
    }
    private void getDropboxIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
    }
    private JSONObject getPayload() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("fullName", fullnameInput.getText().toString());
            if (time.length() == 0){
                Log.e(TAG, "getPayload: " + ApplicationService.user.getDateOfBirth());
                if(ApplicationService.user.getDateOfBirth().contains("T")){
                    payload.put("dateOfBirth",ApplicationService.user.getDateOfBirth()); // get dateofbirth //2012-12-12T00:00:00+07:00
                }else{
                    String[] dateArray =ApplicationService.user.getDateOfBirth().split("/");
                    String updateDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
                    payload.put("dateOfBirth",updateDate  + "T00:00:00+07:00");
                }
            }else {
                String[] dateArray =time.split("-");
                String updateDate = dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0];
                payload.put("dateOfBirth",updateDate  + "T00:00:00+07:00"); // get dateofbirth //2012-12-12T00:00:00+07:00
            }
            if( emailInput.getText().toString().length() == 0){
                payload.put("email", "");

            }else{
                payload.put("email",  emailInput.getText().toString());
            }
            payload.put("gender", positionSexSelected); // get gender nam : 0; nu 1; khac 2
            if(phoneNumberInput.getText().toString().length() == 0){
                payload.put("phone", "");

            }else{
                payload.put("phone", phoneNumberInput.getText().toString());
            }
            if(adressInput.getText().toString().length() == 0){
                payload.put("address", "");

            }else{
                payload.put("address", adressInput.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }
    private void uploadImage() {
        updateImageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplicationService.requestManager.sendFileUserProfile(avatarFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        updateImageThread.start();
    }

    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_UPLOAD_PHOTO_PROFILE_SUCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("update_user_success"), false);
                    if (updateImageThread != null) {
                        updateImageThread.interrupt();
                    }
                    break;
                case ApplicationService.MESSAGE_UPLOAD_FAILE:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                    if (updateImageThread != null) {
                        updateImageThread.interrupt();
                    }
                    break;
                case ApplicationService.MESSAGE_UPDATE_FAIL:
                    try {
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("update_userTime_fail"), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ApplicationService.MESSAGE_UPDATE_USERPROFILE_SUCCESS:
                    try {
                        ApplicationService.user.setAddress(adressInput.getText().toString());
                        ApplicationService.user.setFullName(fullnameInput.getText().toString());
                        ApplicationService.user.setEmail(emailInput.getText().toString());
                        ApplicationService.user.setPhone(phoneNumberInput.getText().toString());
                        ApplicationService.user.setGender(spinnerSex.getSelectedItemPosition());
                        ApplicationService.user.setDateOfBirth(dateOfBirthValue.getText().toString());

                        if(avatarFile==null){
                            ApplicationService.showToast(LanguageManager.getInstance().getValue("update_user_success"), false);
                        }else{
                            uploadImage();
                            ApplicationService.user.setAvatar(avatarFile.toString());
                        }

//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}