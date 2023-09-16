package com.bkav.aiotcloud.screen.home.camera.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RotateFragment extends Fragment {

    public static final String DAY_NIGHT_SETTING = "ptz";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_IMAGE_INFO_SETTING);
        View view = inflater.inflate(R.layout.rotate_layout, container, false);
        init(view);
        setData();
//        updateUI(ApplicationService.dataSettingCam);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_IMAGE_INFO_SETTING);
//        updateUI(ApplicationService.dataSettingCam);

    }
    private void setData(){
        this.derectionTx.setText(LanguageManager.getInstance().getValue("rotation"));
        this.mirrorTx.setText(LanguageManager.getInstance().getValue("mirror"));

    }

    private ImageView rotateIamge;
    private ImageView zeroImage;
    private SwitchCompat dayLightSW;

    private TextView mirrorTx;
    private TextView derectionTx;

    private JSONObject currentValue;

    @SuppressLint("ResourceAsColor")
    public void updateUI(JSONObject json) {
        try {
            currentValue = json.getJSONObject("get_imagesetting_response").getJSONObject("data").getJSONObject("orientation");
            this.dayLightSW.setChecked(currentValue.getBoolean("mirror"));
            if (currentValue.getInt("rotate") == 0) {
                this.zeroImage.setImageResource(R.drawable.zero_highlight);
                this.rotateIamge.setImageResource(R.drawable.rotate180);
            } else if (currentValue.getInt("rotate") == 180) {
                this.zeroImage.setImageResource(R.drawable.zero);
                this.rotateIamge.setImageResource(R.drawable.rotate180_highlight);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {

        this.zeroImage = view.findViewById(R.id.zeroImage);
        this.rotateIamge = view.findViewById(R.id.rotateImage);

        this.derectionTx = view.findViewById(R.id.derectionTx);
        this.mirrorTx = view.findViewById(R.id.mirrorTx);

        this.dayLightSW = view.findViewById(R.id.dayLightSW);
        action();
    }

    private void action() {

        this.zeroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    currentValue.put("rotate", 0);

                    Message message = new Message();
                    message.what = ApplicationService.SETTING_ROTATE;
                    message.obj = currentValue.toString();
                    ApplicationService.mainHandler.sendMessage(message);

                    zeroImage.setImageResource(R.drawable.zero_highlight);
                   rotateIamge.setImageResource(R.drawable.rotate180);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.rotateIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    currentValue.put("rotate", 180);
                    Message message = new Message();
                    message.what = ApplicationService.SETTING_ROTATE;
                    message.obj = currentValue.toString();
                    ApplicationService.mainHandler.sendMessage(message);

                    zeroImage.setImageResource(R.drawable.zero);
                    rotateIamge.setImageResource(R.drawable.rotate180_highlight);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        this.dayLightSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    try {
                        currentValue.put("mirror", b);
                        Message message = new Message();
                        message.what = ApplicationService.SETTING_ROTATE;
                        message.obj = currentValue.toString();
                        ApplicationService.mainHandler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }
}