package com.bkav.aiotcloud.screen.notify.detail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.MainScreen;
import com.bkav.aiotcloud.screen.notify.NotifyAdapter;
import com.bkav.aiotcloud.screen.widget.EventFaceInDays;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class NotifyDetailScreen extends AppCompatActivity {
    public static String TAG = "NotifyDetailScreen";
    public static String OBJGUID = "objGuid";
    public static String ID = "id";
    public static String CURRENT_POSITION = "position";
    public static String TYPE = "type";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.notify_detail);
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    private void init() {
        this.type = getIntent().getStringExtra(TYPE);
        this.backIM = findViewById(R.id.backIm);
        this.title = findViewById(R.id.title);
        this.avaterImage = findViewById(R.id.avaterImage);
        this.nameContent = findViewById(R.id.nameContent);
        this.content = findViewById(R.id.content);
        this.locateTx = findViewById(R.id.locateTx);
        this.nameCamera = findViewById(R.id.nameCamera);
        this.timeTx = findViewById(R.id.timeTx);
        this.imageSelected = findViewById(R.id.imageSelect);
        this.event1 = findViewById(R.id.event1);
        this.eventImage1 = findViewById(R.id.eventImage1);
        this.event2 = findViewById(R.id.event2);
        this.eventImage2 = findViewById(R.id.eventImage2);
        this.backgroud = findViewById(R.id.background);

        this.nameContent.setText(LanguageManager.getInstance().getValue("face_recognize"));
    }

    private void setData() {
        this.title.setText(LanguageManager.getInstance().getValue("detail_event"));
        this.event1.setBackgroundResource(R.drawable.retangcle);
        this.event2.setBackgroundResource(R.drawable.retangcle);
        this.backgroud.setBackgroundResource(R.drawable.circle);
        getDetailEvent();
    }

    private void action() {
        this.backIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("0") || type.equals("2") || type.equals("3")) {
                    finish();
                } else {
                    Intent newintent = new Intent(getApplicationContext(), MainScreen.class);
                    newintent.putExtra(MainScreen.TYPE, "notify");
                    startActivity(newintent);
                }
            }
        });
        changeColorSelect();
    }

    private void getDetailEvent() {
        if (type.equals("2") || type.equals("3")) {
            String id = getIntent().getStringExtra(ID);
            String url = String.format(ApplicationService.URL_GET_DETAIL_EVENT_BYID, id);
            ApplicationService.requestManager.getDetaiEvent(url);
        } else {
            String objGuid = getIntent().getStringExtra(OBJGUID);
//            if (ApplicationService.TOKEN == null || ApplicationService.TOKEN.length() == 0){
//                ApplicationService.requestManager.login(SharePref.getInstance(getApplicationContext()).getUser(),
//                        SharePref.getInstance(getApplicationContext()).getPassword(), ApplicationService.FIREBASE_ID, ApplicationService.URL_LOGIN);
//            }
//            Log.e("typee", this.type + " " + objGuid );
            String url = String.format(ApplicationService.URL_GET_DETAIL_EVENT, objGuid);
            ApplicationService.requestManager.getDetaiEvent(url);
        }
    }

    private void changeColorSelect() {
        event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable1 = (GradientDrawable) event1.getBackground();
                drawable1.setColor(ContextCompat.getColor(getApplication(), R.color.mainColor));

                GradientDrawable drawable2 = (GradientDrawable) event2.getBackground();
                drawable2.setColor(ContextCompat.getColor(getApplication(), R.color.barColor));
                try {
                    if (type.equals("2") || type.equals("3")) {
                        Glide.with(getApplication())
                                .load(listPath.getJSONObject(0).getString("filePath")).into(imageSelected);
                    } else {
                        Glide.with(getApplication())
                                .load(listPath.getJSONObject(0).getString("path")).into(imageSelected);
                    }
//                    Glide.with(getApplication())
//                            .load(listPath.getJSONObject(0).getString("path")).into(imageSelected);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        event2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable1 = (GradientDrawable) event2.getBackground();
                drawable1.setColor(ContextCompat.getColor(getApplication(), R.color.mainColor));

                GradientDrawable drawable2 = (GradientDrawable) event1.getBackground();
                drawable2.setColor(ContextCompat.getColor(getApplication(), R.color.barColor));

                try {
                    if (type.equals("2") || type.equals("3")) {
                        Glide.with(getApplication())
                                .load(listPath.getJSONObject(1).getString("filePath")).into(imageSelected);
                    } else {
                        Glide.with(getApplication())
                                .load(listPath.getJSONObject(1).getString("path")).into(imageSelected);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.imageSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ImagesInEvent.class);
                intent.putExtra("path", listPath.toString());
                startActivity(intent);
            }
        });


    }

    private void updateUIbyGuid(JSONObject notifyItem) {
        Log.e("notify item ", notifyItem.toString());
        try {
            this.listPath = notifyItem.getJSONArray("listPath");


            if (notifyItem.getString("iconNotify").length() != 0) {
                Glide.with(this)
                        .load(notifyItem.getString("iconNotify")).circleCrop().into(avaterImage);
            }
            //path Image
            if (listPath.length() == 0) {
                event1.setVisibility(View.GONE);
                event2.setVisibility(View.GONE);
            }
            if (listPath.length() != 0) {
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("path")).circleCrop().into(imageSelected);
                GradientDrawable drawable1 = (GradientDrawable) event1.getBackground();
                drawable1.setColor(ContextCompat.getColor(getApplication(), R.color.mainColor));
            }
            if (listPath.length() == 1) {
                event2.setVisibility(View.GONE);
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("path")).into(eventImage1);
            }
            if (listPath.length() >= 2) {
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("path")).into(eventImage1);
                Glide.with(this)
                        .load(listPath.getJSONObject(1).getString("path")).into(eventImage2);

                GradientDrawable drawable2 = (GradientDrawable) event2.getBackground();
                drawable2.setColor(ContextCompat.getColor(getApplication(), R.color.barColor));
            }
            GradientDrawable drawable;
            String[] contens;
            switch (notifyItem.getString("identity")) {
                case NotifyAdapter.statusDevice:
                    content.setText(String.format(LanguageManager.getInstance().getValue("device_content_format"), notifyItem.getString("cameraName"), notifyItem.getString("content")));
                    nameContent.setText(LanguageManager.getInstance().getValue("deviceactivity"));
                    avaterImage.getLayoutParams().height = 100;
                    avaterImage.getLayoutParams().width = 100;
                    Glide.with(getApplication())
                            .load(R.drawable.camera_icon)
                            .placeholder(R.drawable.camera_icon)
                            .into(avaterImage);
                    drawable = (GradientDrawable) backgroud.getBackground();
                    drawable.setColor(Color.parseColor(notifyItem.getString("backgroundColor")));

                    this.event1.setVisibility(View.GONE);
                    break;
                case NotifyAdapter.faceReconizerType:
                    contens = notifyItem.getString("content").split(" " + "\\|" + " ");
                    if (contens[0].equals(NotifyAdapter.customer)) {
                        content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_content_format"), contens[1], contens[2], contens[3], contens[4]));
                    } else {
                        content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_unknow_format"), contens[1], contens[2]));
                    }
                    drawable = (GradientDrawable) backgroud.getBackground();
                    drawable.setColor(Color.parseColor(notifyItem.getString("backgroundColor")));
                    break;
                case NotifyAdapter.intrusion:
                    if (!notifyItem.isNull("content")) {
                        Log.e(TAG, notifyItem.getString("content"));
                        contens = notifyItem.getString("content").split("\\|");
                        content.setText(String.format(LanguageManager.getInstance().getValue("instrusion_content_format"), contens[1], contens[2]));
                    }
                    nameContent.setText(LanguageManager.getInstance().getValue("intrusion_detect"));
                    avaterImage.getLayoutParams().height = 100;
                    avaterImage.getLayoutParams().width = 100;
                    Glide.with(getApplication())
                            .load(R.drawable.intrusion_icon)
                            .placeholder(R.drawable.intrusion_icon)
                            .into(avaterImage);
                    drawable = (GradientDrawable) backgroud.getBackground();
                    drawable.setColor(ContextCompat.getColor(getApplication(), R.color.mainColor));
                    break;
                case NotifyAdapter.licenseplate:
                    contens = notifyItem.getString("content").split(" " + "\\|" + " ");
                    nameContent.setText(LanguageManager.getInstance().getValue("lable_detect"));
                    content.setText(String.format(LanguageManager.getInstance().getValue("ai_license_content_format"), contens[0], contens[2], contens[1]));
                    drawable = (GradientDrawable) backgroud.getBackground();
                    drawable.setColor(Color.parseColor(notifyItem.getString("backgroundColor")));
                    break;
                default:
                    break;
            }

            this.locateTx.setText(notifyItem.getString("address"));
            this.nameCamera.setText(notifyItem.getString("cameraName"));
            this.timeTx.setText(DateTimeFormat.getTimeFormat(notifyItem.getString("createdAt"), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));

            Glide.with(this)
                    .load(listPath.getJSONObject(0).getString("path")).into(imageSelected);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUIbyID(JSONObject notifyItem) {
        try {
            listPath = notifyItem.getJSONArray("eventFiles");

            if (listPath.length() == 0) {
                event1.setVisibility(View.GONE);
                event2.setVisibility(View.GONE);
            }
            if (listPath.length() != 0) {
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("filePath")).into(imageSelected);
                GradientDrawable drawable1 = (GradientDrawable) event1.getBackground();
                drawable1.setColor(ContextCompat.getColor(getApplication(), R.color.mainColor));
            }
            if (listPath.length() == 1) {
                event2.setVisibility(View.GONE);
                Log.e("NotifyDetailScreen", listPath.getJSONObject(0).getString("filePath"));
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("filePath")).into(eventImage1);
            }
            if (listPath.length() >= 2) {
                Glide.with(this)
                        .load(listPath.getJSONObject(0).getString("filePath")).into(eventImage1);
                Glide.with(this)
                        .load(listPath.getJSONObject(1).getString("filePath")).into(eventImage2);

                GradientDrawable drawable2 = (GradientDrawable) event2.getBackground();
                drawable2.setColor(ContextCompat.getColor(getApplication(), R.color.barColor));
            }

            int position = getIntent().getIntExtra(CURRENT_POSITION, -1);
            String avaPath = "";
            if (type.equals("3")) {
                avaPath = EventFaceInDays.customerInDays.get(position).getThumbnailFilePath();
            } else {
                avaPath = ApplicationService.customerItems.get(position).getAvatarFilePath();
            }
            Glide.with(this)
                    .load(avaPath).circleCrop().into(avaterImage);
            String[] contens = notifyItem.getString("content").split("-");
            String locationCustom = "";
            if (!notifyItem.getString("content").equals("")) {
                if (contens.length == 4) {
                    locationCustom = String.join(" ", Arrays.copyOfRange(contens[3].split(" "), 1, contens[3].split(" ").length));
                    this.content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_content_format"), contens[0], contens[1], locationCustom, contens[2]).trim());
                } else if (contens.length == 3) {
                    content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_unknow_format"), contens[1], contens[2]));
                }

            } else {
                this.content.setText("");
            }
//            this.nameContent.setText(LanguageManager.getInstance().getValue("face_customer"));
            this.locateTx.setText(notifyItem.getString("allRegionName"));
            this.nameCamera.setText(notifyItem.getString("cameraName"));
            this.timeTx.setText(DateTimeFormat.getTimeFormat(notifyItem.getString("startedAt"), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                    break;
                case ApplicationService.MESSAGE_GET_DETAI_EVENT_SUCCESS:
                    String notifyItem = (String) message.obj;
                    try {
                        if (type.equals("2") || type.equals("3")) {
                            updateUIbyID(new JSONObject(notifyItem));
                        } else {
                            updateUIbyGuid(new JSONObject(notifyItem));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    private RelativeLayout backIM;
    private TextView title;
    private ImageView avaterImage;
    private TextView nameContent;
    private TextView content;
    private TextView locateTx;
    private TextView nameCamera;
    private TextView timeTx;
    private ImageView imageSelected;
    private ImageView eventImage1;
    private ImageView eventImage2;
    private RelativeLayout backgroud;
    private RelativeLayout event1;
    private RelativeLayout event2;
    private JSONArray listPath;
    private String type;

    private String paths;

}
