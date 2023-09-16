package com.bkav.aiotcloud.screen.setting.face.edit;

import static com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity.AI_ITEM;
import static com.bkav.aiotcloud.screen.setting.face.edit.InfoAIScreen.EDIT_DONE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogEditAI extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_edit_ai);
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(true);

        int position = getIntent().getIntExtra(ListFaceObjectActivity.AI_ITEM, 0);
        AIObject aiObject = ApplicationService.cameraConfigs.get(position);

        TextView editTitle = findViewById(R.id.editTitle);
        TextView editDetail = findViewById(R.id.editDetail);
        TextView deleteTitle = findViewById(R.id.deleteTitle);
        TextView deleteDetail = findViewById(R.id.deleteDetail);

        RelativeLayout deleteLayout = findViewById(R.id.deleteLayout);
        RelativeLayout editLayout = findViewById(R.id.editLayout);
        String type = getIntent().getStringExtra(SettingFragment.TYPE);

        editTitle.setText(LanguageManager.getInstance().getValue("edit"));
        editDetail.setText(LanguageManager.getInstance().getValue("edit_ai_info"));

        deleteTitle.setText(LanguageManager.getInstance().getValue("delete"));
        deleteDetail.setText(LanguageManager.getInstance().getValue("delete_ai_info"));
        ApplicationService.mainHandler = new MainHandler();

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), EditObjectFace.class);
                intent.putExtra(SettingFragment.TYPE, type);
                intent.putExtra(AI_ITEM, position);
                intent.setFlags(0);
                startActivityIntent.launch(intent);
            }
        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("monitorGuid", aiObject.getMonitorId());
                    payload.put("cameraId", aiObject.getCameraId());
                    payload.put("appIdentity", type);
                    payload.put("identity", type);
                    ApplicationService.requestManager.deleteAIConfig(payload, ApplicationService.URL_DELETE_AI_CONFIG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String type = data.getExtras().getString(StepThreeFragment.TYPE);
                        if (type.equals(EDIT_DONE)) {
                            Intent dataEdit = new Intent();
                            dataEdit.putExtra(StepThreeFragment.TYPE, InfoAIScreen.EDIT_DONE);
                            setResult(RESULT_OK, dataEdit);
                            finish();
                        }

                    }
                }
            });

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                    finish();
                    break;
                case ApplicationService.DELETE_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent data = new Intent();
                    data.putExtra(StepThreeFragment.TYPE, InfoAIScreen.EDIT_DONE);
                    setResult(RESULT_OK, data);
                    finish();
//                    Intent dataEdit = new Intent();
//                    dataEdit.putExtra(StepThreeFragment.TYPE, InfoAIScreen.EDIT_DONE);
//                    setResult(RESULT_OK, dataEdit);
                    break;
            }
        }
    }
}


