package com.bkav.aiotcloud.screen.setting.face.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.network.VolleyRequestManagement;
import com.bkav.aiotcloud.screen.setting.face.historyObject.ListDates;
import com.bkav.aiotcloud.screen.setting.face.unidenifiedface.UnidenifiedFaceActivity;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AddCustomerActivity extends AppCompatActivity {
    public static String TAG = "AddCustomerActivity";
    public static String TIME = "time";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.add_customer);
        Intent intent = getIntent();
        this.type = intent.getStringExtra(ListCustomerActivity.TYPE);
        init();
        if (this.type.equals(ListCustomerActivity.EDIT)) {
            this.deleteIM.setVisibility(View.VISIBLE);
            this.historyLayout.setVisibility(View.VISIBLE);
            this.historyImage.setColorFilter(getApplicationContext().getResources().getColor(R.color.mainColor));
            this.title.setText(LanguageManager.getInstance().getValue("edit_infor"));
            this.dateOfBirthValue.setHint(LanguageManager.getInstance().getValue("hint_date_of_birth"));
            this.listImageDelete = new JSONArray();
            this.statusLayout.setVisibility(View.VISIBLE);
            this.deleteIM.setVisibility(View.VISIBLE);
            this.position = getIntent().getIntExtra(ListCustomerActivity.ID, -1);
            @SuppressLint("DefaultLocale") String url = String.format(ApplicationService.URL_GET_DETAIL_INFOR_CUSTOMER, ApplicationService.customerItems.get(position).getCustomerId());
            ApplicationService.requestManager.getDetailCustomer(url);
        } else if (this.type.equals(ListCustomerActivity.NEW)) {
            this.dateOfBirthValue.setHint(LanguageManager.getInstance().getValue("hint_date_of_birth"));
            this.deleteIM.setVisibility(View.GONE);
            this.title.setText(LanguageManager.getInstance().getValue("add_new"));
            this.codePeopleInput.setText(generateString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20));
        } else {
            this.position = getIntent().getIntExtra(ListCustomerActivity.ID, -1);
            this.dateOfBirthValue.setHint(LanguageManager.getInstance().getValue("hint_date_of_birth"));
            this.deleteIM.setVisibility(View.GONE);
            this.title.setText(LanguageManager.getInstance().getValue("add_new"));
            Glide.with(this).load(ApplicationService.customerItems.get(position).getAvatarFilePath()).circleCrop().into(avatar);
            this.codePeopleInput.setText(generateString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 20));
        }
        action();

        setAdapter();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        listType.clear();
        getListStyleName();
        adapterType.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateImageThread != null) {
            updateImageThread.interrupt();
        }
    }

    private List<String> listType = new ArrayList<>();
    private List<String> listSex = new ArrayList<>();


    private ImageView avatar;
    private RelativeLayout backIM;
    private RelativeLayout addPhoto;
    private ArrayAdapter<String> adapterType;
    private ArrayAdapter<String> adapterSex;
    private LinearLayout listImage;
    private Uri mCropImageUri;
    private Spinner spinnerType;
    private Spinner spinnerSex;
    private ArrayList<File> images;
    private ArrayList<File> imagesAdd;
    private boolean isAvatar = false;
    private LinearLayout.LayoutParams imageParam;
    private LinearLayout.LayoutParams deleteParam;
    private int positionTypeSelected = 0;
    private int positionSexSelected = 0;
    private Button save;
    private File avatarFile;
    private Thread updateImageThread;
    private String type;
    private String time = "";
    private RelativeLayout statusLayout;

    private TextView title;

    private TextView fullNameTx;
    private EditText fullnameInput;

    private TextView codePeopleTx;
    private EditText codePeopleInput;

    private TextView typePeopleTx;

    private TextView dateOfBirthTx;
    private TextView dateOfBirthValue;

    private TextView emailTx;
    private EditText emailInput;

    private TextView sexTx;
//    private EditText fullnameInput;

    private TextView phoneNumberlTx;
    private EditText phoneNumberInput;

    private TextView addressTx;
    private EditText addressInput;

    private TextView noteTx;
    private EditText noteInput;
    private JSONArray listImageDelete;

    private SwitchCompat swOnOffStatus;
    private TextView statusTx;

    private TextView chooseImageTx;

    private RelativeLayout setTimeLayout;
    private JSONObject editItem;
    private Button deleteIM;
    private RelativeLayout historyLayout;
    private ImageView historyImage;
    private String token;
    private String vsToken;
    private int position;

    private void setDataEdit(JSONObject editItem) {
        try {
            this.fullnameInput.setText(editItem.getString("fullName"));
            this.codePeopleInput.setText(editItem.getString("code"));
            getTypeCustomer();
            if (!editItem.isNull("dateOfBirth")) {
                this.dateOfBirthValue.setText(DateTimeFormat.getTimeFormat(editItem.getString("dateOfBirth"), DateTimeFormat.DATE_ROOTH, DateTimeFormat.DATE_FORMAT));
            }
            this.emailInput.setText(editItem.getString("email"));
            spinnerSex.setSelection(editItem.getInt("gender"));
            this.phoneNumberInput.setText(editItem.getString("phone"));
            this.addressInput.setText(editItem.getString("address"));
            this.noteInput.setText(editItem.getString("description"));
            this.swOnOffStatus.setChecked(editItem.getBoolean("active"));

            for (int index = 0; index < editItem.getJSONArray("listProfileImage").length(); index++) {
                addImage(editItem.getJSONArray("listProfileImage").getJSONObject(index).getString("filePath"));
            }
            Glide.with(this).load(editItem.getJSONObject("profileAvatar").getString("filePath")).circleCrop().into(avatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addImage(String path) {
        try {
            URL url = new URL(path);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(imageParam);
            Glide.with(this).load(path).into(imageView);
            listImage.addView(addView(imageView, path), listImage.getChildCount() - 1);
            File image = new File(url.getFile());
            images.add(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void getTypeCustomer() {
        try {
            for (TypeAIObject typeCustomerItem : ApplicationService.typeCustomerItems) {
                if (typeCustomerItem.getID() == editItem.getInt("customerTypeId")) {
                    spinnerType.setSelection(listType.indexOf(typeCustomerItem.getName()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        adapterType = new ArrayAdapter<String>(AddCustomerActivity.this, android.R.layout.simple_spinner_dropdown_item, listType) {
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
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                positionTypeSelected = index;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterSex = new ArrayAdapter<String>(AddCustomerActivity.this, android.R.layout.simple_spinner_dropdown_item, listSex) {
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

    private View addView(ImageView imageView, String path) {
        RelativeLayout viewChild = new RelativeLayout(this);
        viewChild.setLayoutParams(imageParam);

        ImageView delete = new ImageView(this);
        Drawable res = ContextCompat.getDrawable(this, R.drawable.delete_icon);
        delete.setImageDrawable(res);
        delete.setLayoutParams(deleteParam);
        viewChild.addView(imageView);
        viewChild.addView(delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listImage.removeView(viewChild);
                images.removeIf(file -> path.equals(file.getPath()));
                imagesAdd.removeIf(file -> path.equals(file.getPath()));
                try {
                    for (int index = 0; index < editItem.getJSONArray("listProfileImage").length(); index++) {
                        if (path.equals(editItem.getJSONArray("listProfileImage").getJSONObject(index).getString("filePath"))) {
                            listImageDelete.put(editItem.getJSONArray("listProfileImage").getJSONObject(index).getInt("imageId"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return viewChild;
    }

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
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            getDropboxIMGSize(result.getUri());
            if (resultCode == RESULT_OK) {
//                ((ImageButton) findViewById(R.id.quick_start_cropped_image)).sestatusLayouttImageURI(result.getUri());
                if (isAvatar) {
                    Glide.with(this).load(result.getUri()).circleCrop().into(avatar);
                    this.avatarFile = new File(result.getUri().getPath());
                } else {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(imageParam);
                    Glide.with(this).load(result.getUri()).into(imageView);
                    listImage.addView(addView(imageView, result.getUri().getPath()), listImage.getChildCount() - 1);
                    File image = new File(result.getUri().getPath());
                    images.add(image);
                    imagesAdd.add(image);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getListStyleName() {
        for (TypeAIObject typeCustomerItem : ApplicationService.typeCustomerItems) {
            if (!((TypeCustomerItem) typeCustomerItem).isUnknow()) {
                listType.add(typeCustomerItem.getName());
            }
        }
    }

    public static String generateString(String characters, int length) {
        Random ran = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(ran.nextInt(characters.length()));
        }
        return new String(text);
    }

    private void getDropboxIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1, 1)
                .setRequestedSize(400, 400)
                .start(this);
    }

    private void init() {
        this.historyImage = findViewById(R.id.historyImage);
        this.historyLayout = findViewById(R.id.historyLayout);
        this.avatar = findViewById(R.id.avatarImage);
        this.addPhoto = findViewById(R.id.addPhoto);
        this.listImage = findViewById(R.id.listImage);
        this.backIM = findViewById(R.id.backIm);
        this.setTimeLayout = findViewById(R.id.dateOfBirthLayout);
        this.save = findViewById(R.id.save);
        this.statusLayout = findViewById(R.id.statusLayout);
        this.deleteIM = findViewById(R.id.deleteIM);

        this.title = findViewById(R.id.title);

        this.fullNameTx = findViewById(R.id.fullNameTx);
        this.fullnameInput = findViewById(R.id.fullnameInput);

        this.codePeopleTx = findViewById(R.id.codePeopleTx);
        this.codePeopleInput = findViewById(R.id.codePeopleInput);

        this.typePeopleTx = findViewById(R.id.typePeopleTx);
        this.spinnerType = findViewById(R.id.spinnerType);

        this.dateOfBirthTx = findViewById(R.id.dateOfBirthTx);
        this.dateOfBirthValue = findViewById(R.id.dateOfBirthValue);

        this.emailTx = findViewById(R.id.emailTx);
        this.emailInput = findViewById(R.id.emailInput);

        this.sexTx = findViewById(R.id.sexTx);
        this.spinnerSex = findViewById(R.id.spinnerSex);

        this.phoneNumberInput = findViewById(R.id.phoneNumberInput);
        this.phoneNumberlTx = findViewById(R.id.phoneNumberlTx);

        this.addressTx = findViewById(R.id.addressTx);
        this.addressInput = findViewById(R.id.adressInput);

        this.noteTx = findViewById(R.id.noteTx);
        this.noteInput = findViewById(R.id.noteInput);

        this.swOnOffStatus = findViewById(R.id.swOnOffStatus);
        this.statusTx = findViewById(R.id.statusTx);

        this.chooseImageTx = findViewById(R.id.chooseImageTx);

        this.imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.imageParam.setMargins(15, 0, 0, 0);
        this.deleteParam = new LinearLayout.LayoutParams(40, 40);
        this.deleteParam.setMargins(220, 20, 0, 0);

        this.listSex.add(LanguageManager.getInstance().getValue("male"));
        this.listSex.add(LanguageManager.getInstance().getValue("female"));
        this.listSex.add(LanguageManager.getInstance().getValue("other"));

        this.images = new ArrayList<>();
        this.imagesAdd = new ArrayList<>();


    }


    private void action() {
        this.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListDates.class);
                intent.putExtra(ListDates.POSITION_KEY, position);
                intent.putExtra(ListDates.TYPE, ListDates.CUSTOMER_HISTORY);
                startActivity(intent);
            }
        });
        this.setTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SetTimeDialog.class);
                if (dateOfBirthValue.getText().length() != 0) {
                    intent.putExtra(SetTimeDialog.TIME, dateOfBirthValue.getText().toString());
                } else {
                    intent.putExtra(SetTimeDialog.TIME, LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                intent.setFlags(0);
                startActivityIntent.launch(intent);
            }
        });
        this.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = true;
                onSelectImageClick(view);
            }
        });

        this.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAvatar = false;
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                imageLauncher.launch(intent);
                onSelectImageClick(view);

            }
        });

        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e(TAG, listImageDelete.toString());
                if (fullnameInput.getText().length() == 0) {
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("er_empty_name"), true);
                    return;
                }
                if (!type.equals(ListCustomerActivity.EDIT)) {
                    ApplicationService.requestManager.addUpdateCustomer(getPayload(new JSONObject()), ApplicationService.URL_ADD_UPDATE_CUSTOMER);
                } else {
                    if (editItem != null) {
                        ApplicationService.requestManager.addUpdateCustomer(getPayload(editItem), ApplicationService.URL_ADD_UPDATE_CUSTOMER);
                    }
                }
            }
        });

        this.deleteIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogoutDialog();
            }
        });
        this.save.setText(LanguageManager.getInstance().getValue("save"));
        this.fullNameTx.setText(LanguageManager.getInstance().getValue("fullname"));
        this.chooseImageTx.setText(LanguageManager.getInstance().getValue("select_sample_image"));
        this.codePeopleTx.setText(LanguageManager.getInstance().getValue("code_object"));
        this.fullnameInput.setHint(LanguageManager.getInstance().getValue("hint_fullname"));

        this.codePeopleTx.setText(LanguageManager.getInstance().getValue("code_object"));
        this.codePeopleInput.setHint(LanguageManager.getInstance().getValue("input_code_object"));

        this.typePeopleTx.setText(LanguageManager.getInstance().getValue("type_object"));
        this.dateOfBirthTx.setText(LanguageManager.getInstance().getValue("date_of_birth"));

//        this.dateOfBirthTx.setText(LanguageManager.getInstance().getValue("email"));

        this.emailTx.setHint(LanguageManager.getInstance().getValue("email"));
        this.emailInput.setHint(LanguageManager.getInstance().getValue("hint_email"));
        this.sexTx.setText(LanguageManager.getInstance().getValue("sex"));

        this.phoneNumberlTx.setText(LanguageManager.getInstance().getValue("phone_number"));
        this.phoneNumberInput.setHint(LanguageManager.getInstance().getValue("hint_phone_number"));

        this.addressTx.setText(LanguageManager.getInstance().getValue("address"));
        this.addressInput.setHint(LanguageManager.getInstance().getValue("hint_address"));

        this.noteTx.setText(LanguageManager.getInstance().getValue("note"));
        this.noteInput.setHint(LanguageManager.getInstance().getValue("note_input"));
        this.statusTx.setText(LanguageManager.getInstance().getValue("status"));
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }


    private boolean checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        return checkPermission();
    }

    private TypeCustomerItem getCustomerTypeID(String name) {
        for (TypeAIObject typeCustomerItem : ApplicationService.typeCustomerItems) {
            if (typeCustomerItem.getName().equals(name)) {
                return (TypeCustomerItem)typeCustomerItem;
            }
        }
        return null;
    }

    private JSONObject getPayload(JSONObject payload) {
        try {
            payload.put("fullName", Objects.requireNonNull(fullnameInput.getText().toString()));
            payload.put("customerTypeId", Objects.requireNonNull(getCustomerTypeID(listType.get(positionTypeSelected))).getID());
            payload.put("ownerByUserId", Objects.requireNonNull(getCustomerTypeID(listType.get(positionTypeSelected))).getOwnerByUserId());
            payload.put("code", codePeopleInput.getText().toString());
            if (time.length() == 0) {
                payload.put("dateOfBirth", JSONObject.NULL);
            } else {
                payload.put("dateOfBirth", DateTimeFormat.converseTimeRooth(time, DateTimeFormat.DATE_FORMAT, DateTimeFormat.DATE_ROOTH)); // get dateofbirth
//                Log.e(TAG, DateTimeFormat.converseTimeRooth(time, DateTimeFormat.DATE_FORMAT, DateTimeFormat.DATE_ROOTH));
            }
            payload.put("email", emailInput.getText().toString());
            payload.put("gender", positionSexSelected); // get gender nam : 0; nu 1; khac 2
            payload.put("phone", phoneNumberInput.getText().toString());
            payload.put("description", noteInput.getText().toString());
            payload.put("address", addressInput.getText().toString());
            if (type.equals(ListCustomerActivity.NEW)) {
                payload.put("active", true);
            } else {
                payload.put("active", swOnOffStatus.isChecked());
            }
            payload.put("customerTypeName", JSONObject.NULL);
            if (imagesAdd.size() == 0 && avatarFile == null) {
                payload.put("isUploadImgs", false);
            } else {
                payload.put("isUploadImgs", true);
            }

            if (type.equals(ListCustomerActivity.NEW)) {
                payload.put("profileId", 0);
            } else {
                payload.put("listImageDelete", listImageDelete);
            }


            Log.e(TAG, payload.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payload;
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        time = data.getExtras().getString(TIME).replace("-", "/");
                        dateOfBirthValue.setText(time);
                        Log.e(TAG, DateTimeFormat.converseTimeRooth(time.replace("-", "/"), DateTimeFormat.DATE_FORMAT, DateTimeFormat.DATE_ROOTH));
                    }
                }
            });

    private void uploadImage(JSONObject customerJson) {
        updateImageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplicationService.requestManager.sendFile(imagesAdd, avatarFile, customerJson.getString("objectGuid"), customerJson.getInt("profileId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        updateImageThread.start();
    }

    private void addCustomerFromUnkow(JSONObject customerJson) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("customerId", customerJson.getInt("profileId"));
            JSONArray listEvent = new JSONArray();
            for (int index =0; index <3; index++){
                JSONObject evenItem = new JSONObject();
                evenItem.put("eventId", ApplicationService.customerItems.get(position).getEventId());
                evenItem.put("pathImage", ApplicationService.customerItems.get(position).getAvatarFilePath());
                evenItem.put("imageType", index);
                listEvent.put(evenItem);
            }
            payload.put("listImage", listEvent);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        ApplicationService.volleyRequestManagement.moveImageToUnknownCustomer(payload, ApplicationService.URL_UPLOAD_MOVE_IMAGE_UNKNOWN_CUSTOMER);
    }

    private void openLogoutDialog() {
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

        textViewConfirm.setText(LanguageManager.getInstance().getValue("comfirm_delete_customer"));
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
                @SuppressLint("DefaultLocale") String url = String.format(ApplicationService.URL_DELETE_CUSTOMER, ApplicationService.customerItems.get(position).getCustomerId());
                ApplicationService.requestManager.deleteTypeCustomer(url);
            }
        });
    }

    private class MainHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_UPLOAD_PHOTO_SUCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    if (!type.equals(UnidenifiedFaceActivity.UNKNOW)){
                        if (updateImageThread != null) {
                            updateImageThread.interrupt();
                        }
                        Intent dataBack = new Intent();
                        dataBack.putExtra(ListCustomerActivity.TYPE, ListCustomerActivity.EDIT);
                        setResult(RESULT_OK, dataBack);
                    }
                    finish();
                    break;
                case ApplicationService.DELETE_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent dataBackDelete = new Intent();
                    dataBackDelete.putExtra(ListCustomerActivity.TYPE, ListCustomerActivity.EDIT);
                    setResult(RESULT_OK, dataBackDelete);
                    finish();
                    break;

                case ApplicationService.MESSAGE_ERROR:
                    String er = (String) message.obj;
                    ApplicationService.showToast(er, true);
                    if (updateImageThread != null) {
                        updateImageThread.interrupt();
                    }
                    break;
                case ApplicationService.MESSAGE_UPDATE_CUSTOMER_SUCCESS:
                    String dataItem = (String) message.obj;
                    try {
                        JSONObject customerItem = new JSONObject(dataItem);
                        if (type.equals(UnidenifiedFaceActivity.UNKNOW)){
                            addCustomerFromUnkow(customerItem);
                        } else {
                            uploadImage(customerItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case ApplicationService.MESSAGE_GET_DETAI_CUSTOMER_SUCCESS:
                    try {
                        editItem = new JSONObject((String) message.obj);
                        setDataEdit(editItem);
//                        Log.e(TAG, editItem.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}
