package com.bkav.aiotcloud.screen.setting.face;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.edit.InfoAIScreen;
import com.bkav.aiotcloud.view.CustomEditText;
import com.bkav.aiotcloud.view.GridviewCustom;

public class ListFaceObjectActivity extends AppCompatActivity {
    public static final String AI_ITEM = "ai_item";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.list_face_object);
        ApplicationService.cameraConfigs.clear();
        Intent intent = getIntent();
        type = intent.getStringExtra(SettingFragment.TYPE);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.requestManager.getListCameraConfig(type);
    }

    private FaceObjectAdapter aiObjectAdapter;
    private GridviewCustom listCamera;
    private CustomEditText searchTx;
    private TextView title;
    private RelativeLayout backIm;
    private ImageView addIM;
    private String type;


    private void init(){
        this.listCamera = findViewById(R.id.gridview);
        this.aiObjectAdapter = new FaceObjectAdapter(this, ApplicationService.cameraConfigs);
        this.listCamera.setAdapter(this.aiObjectAdapter);
//        this.searchTx = findViewById(R.id.searchEditText);
        this.title = findViewById(R.id.title);
        this.backIm = findViewById(R.id.backIm);
        this.addIM = findViewById(R.id.addIM);

        this.title.setText(LanguageManager.getInstance().getValue("list_ai_object"));

        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.addIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), AddOjectFace.class);
                intent.putExtra(SettingFragment.TYPE, type);
                startActivity(intent);
            }
        });

        this.listCamera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if (ApplicationService.cameraConfigs.get(position).getIsActive() == 0){
//                    showToast(LanguageManager.getInstance().getValue("camera_off_line"), true);
//                    return;
//                }
                Intent intent = new Intent(getApplication(), InfoAIScreen.class);
                intent.putExtra(AI_ITEM, position);
                intent.putExtra(SettingFragment.TYPE, type);
                startActivity(intent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showToast(String message, boolean isEr) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        if (isEr) {
            view.getBackground().setColorFilter(getColor(R.color.background), PorterDuff.Mode.SRC_IN);
            text.setTextColor((getColor(R.color.red)));
        } else {
            view.getBackground().setColorFilter(getColor(R.color.stateActive), PorterDuff.Mode.SRC_IN);
            text.setTextColor((getColor(R.color.white)));
        }

        toast.show();
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.GET_LIST_CAMERA_CONFIG_SUCCESS:
                    aiObjectAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
