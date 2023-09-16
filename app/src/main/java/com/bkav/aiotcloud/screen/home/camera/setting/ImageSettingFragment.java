package com.bkav.aiotcloud.screen.home.camera.setting;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ImageSettingFragment extends Fragment {
    public static final String IMAGE_SETTING = "image";
    public static final int CONTRAST = 0;
    public static final int BRIGHTNESS = 1;
    public static final int DEFINITION = 2;
    public static final int SATURATION = 3;
    private String TAG = "DisplaySettingFragment";
    private TextView txContrast;
    private TextView txBrightness;
    private TextView txDefinition;
    private TextView txSaturation;

    private SeekBar sbContrast;
    private SeekBar sbBrightness;
    private SeekBar sbDefinition;
    private SeekBar sbSaturation;

    private TextView txValueContrast;
    private TextView txValueBrightness;
    private TextView txValueDefinition;
    private TextView txValueSaturation;

    private int contrast;
    private int brightness;
    private int definition;
    private int saturation;

    private CameraItem cameraItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_setting_fragment, container, false);
        init(view);
        setData();
        action();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int id = requireArguments().getInt("ID");
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.getCameraId() == id) {
                this.cameraItem = cameraItem;
            }
        }
        // check co phai laf channel box hay k
        if (cameraItem.getBoxId() == 0){
            this.sbContrast.setMax(10);
            this.sbContrast.setMin(0);

            this.sbBrightness.setMax(10);
            this.sbBrightness.setMin(0);

            this.sbDefinition.setMax(6);
            this.sbDefinition.setMin(0);

            this.sbSaturation.setMax(10);
            this.sbSaturation.setMin(0);
        }
    }

    private void setData(){
        this.txContrast.setText(LanguageManager.getInstance().getValue("contrast"));
        this.txBrightness.setText(LanguageManager.getInstance().getValue("brightness"));
        this.txDefinition.setText(LanguageManager.getInstance().getValue("sharpness"));
        this.txSaturation.setText(LanguageManager.getInstance().getValue("saturation"));
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_IMAGE_INFO_SETTING);
//        updateUI(ApplicationService.dataSettingCam);

    }

    public void setMinMax(JSONObject jsonObject){
        try {
            Log.e("option", jsonObject.toString());
            this.sbBrightness.setMax(jsonObject.getJSONObject("brightness").getInt("max"));
            this.sbContrast.setMax(jsonObject.getJSONObject("contrast").getInt("max"));
            this.sbDefinition.setMax(jsonObject.getJSONObject("sharpness").getInt("max"));
            this.sbSaturation.setMax(jsonObject.getJSONObject("colorSaturation").getInt("max"));

            this.sbBrightness.setMin(jsonObject.getJSONObject("brightness").getInt("min"));
            this.sbContrast.setMin(jsonObject.getJSONObject("contrast").getInt("min"));
            this.sbDefinition.setMin(jsonObject.getJSONObject("sharpness").getInt("min"));
            this.sbSaturation.setMin(jsonObject.getJSONObject("colorSaturation").getInt("min"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUI(JSONObject json){
        try {
            Log.e(TAG, json.toString());
            if (cameraItem.getBoxId() != 0){
                this.contrast = json.getInt("contrast");
                if (this.sbContrast == null) {
                    return;
                }
                this.sbContrast.setProgress(this.contrast);
                this.txValueContrast.setText(String.valueOf(this.contrast));


                this.brightness = json.getInt("brightness");
                this.sbBrightness.setProgress(this.brightness);
                this.txValueBrightness.setText(String.valueOf(this.brightness));

                this.definition = json.getInt("sharpness");
                this.sbDefinition.setProgress(this.definition);
                this.txValueDefinition.setText(String.valueOf(this.definition));

                this.saturation = json.getInt("colorSaturation");
                this.sbSaturation.setProgress(this.saturation);
                this.txValueSaturation.setText(String.valueOf(this.saturation));

            } else {
                JSONObject data = json.getJSONObject("get_imagesetting_response").getJSONObject("data").getJSONObject("appearance");
                this.contrast = data.getInt("contrast");
                if (this.sbContrast == null) {
                    return;
                }
                this.sbContrast.setProgress(this.contrast);
                this.txValueContrast.setText(String.valueOf(this.contrast));

                this.brightness = data.getInt("brightness");
                this.sbBrightness.setProgress(this.brightness);
                this.txValueBrightness.setText(String.valueOf(this.brightness));

                this.definition = data.getInt("sharpness");
                this.sbDefinition.setProgress(this.definition);
                this.txValueDefinition.setText(String.valueOf(this.definition));

                this.saturation = data.getInt("saturation");
                this.sbSaturation.setProgress(this.saturation);
                this.txValueSaturation.setText(String.valueOf(this.saturation));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(View view){
        this.txContrast = view.findViewById(R.id.txContrast);
        this.txBrightness = view.findViewById(R.id.txBrightness);
        this.txDefinition = view.findViewById(R.id.txDefinition);
        this.txSaturation = view.findViewById(R.id.txSaturation);

        this.sbContrast = view.findViewById(R.id.sbContrast);
        this.sbBrightness = view.findViewById(R.id.sbBrightness);
        this.sbDefinition = view.findViewById(R.id.sbDefinition);
        this.sbSaturation = view.findViewById(R.id.sbSaturation);

        this.txValueContrast = view.findViewById(R.id.txValueContrast);
        this.txValueBrightness = view.findViewById(R.id.txValueBrightness);
        this.txValueDefinition = view.findViewById(R.id.txValueDefinition);
        this.txValueSaturation = view.findViewById(R.id.txValueSaturation);
    }

    private  JSONObject sendAppearance(int value, int key) {
        JSONObject appearance = new JSONObject();
        try {
            if (key == CONTRAST){
                appearance.put("contrast", value);
                appearance.put("sharpness", definition);
                appearance.put("saturation", saturation);
                appearance.put("brightness", brightness);
            } else if (key == BRIGHTNESS){
                appearance.put("contrast", contrast);
                appearance.put("sharpness", definition);
                appearance.put("saturation", saturation);
                appearance.put("brightness", value);
            }else if (key == DEFINITION){
                appearance.put("contrast", contrast);
                appearance.put("sharpness", value);
                appearance.put("saturation", saturation);
                appearance.put("brightness", brightness);
            }else if (key == SATURATION){
                appearance.put("contrast", contrast);
                appearance.put("sharpness", definition);
                appearance.put("saturation", value);
                appearance.put("brightness", brightness);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return appearance;
    }

    private void action(){
        this.sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Message message = new Message();
                message.what = ApplicationService.SETTING_IMAGE;
                if (cameraItem.getBoxId() == 0){
                    message.obj = sendAppearance(seekBar.getProgress(), CONTRAST).toString();
                } else {
                    message.obj = getDataControlBox();
                }
                ApplicationService.mainHandler.sendMessage(message);
                txValueContrast.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        this.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_IMAGE;
                if (cameraItem.getBoxId() == 0){
                    message.obj = sendAppearance(seekBar.getProgress(), BRIGHTNESS).toString();
                } else {
                    message.obj = getDataControlBox();
                }
                ApplicationService.mainHandler.sendMessage(message);
                txValueBrightness.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        this.sbDefinition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_IMAGE;
                if (cameraItem.getBoxId() == 0){
                    message.obj = sendAppearance(seekBar.getProgress(), DEFINITION).toString();
                } else {
                    message.obj = getDataControlBox();
                }
                ApplicationService.mainHandler.sendMessage(message);

                txValueDefinition.setText(String.valueOf(seekBar.getProgress()));
            }
        });

        this.sbSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_IMAGE;
                if (cameraItem.getBoxId() == 0){
                    message.obj = sendAppearance(seekBar.getProgress(), SATURATION).toString();
                } else {
                    message.obj = getDataControlBox();
                }
                ApplicationService.mainHandler.sendMessage(message);
                txValueSaturation.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    private String formartControl = "{\n" +
            "                                \"contrast\":%d,\n" +
            "                                \"sharpness\":%d,\n" +
            "                                \"brightness\":%d,\n" +
            "                                \"colorSaturation\":%d\n" +
            "                            }";
    private String getDataControlBox(){
        return String.format(formartControl, sbContrast.getProgress(), sbDefinition.getProgress(), sbBrightness.getProgress(), sbSaturation.getProgress());
    }
}