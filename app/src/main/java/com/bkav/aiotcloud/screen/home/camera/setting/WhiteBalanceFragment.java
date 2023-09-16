package com.bkav.aiotcloud.screen.home.camera.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;

import org.json.JSONException;
import org.json.JSONObject;

public class WhiteBalanceFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.white_balance_fragment, container, false);
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_IMAGE_INFO_SETTING);
        init(view);
//        updateUI(ApplicationService.dataSettingCam);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_IMAGE_INFO_SETTING);
//        updateUI(ApplicationService.dataSettingCam);

    }

    private RelativeLayout incandescentLayout;
    private RelativeLayout fluorescentLayout;
    private RelativeLayout autoLayout;
    private RelativeLayout daylightLayout;
    private RelativeLayout cloudyLayout;

    private ImageView incandescentImage;
    private ImageView fluorescentImage;
    private ImageView autoIcon;
    private ImageView dayLight;
    private ImageView cloudy;

    private TextView incandescentTx;
    private TextView fluorescentTx;
    private TextView autoImageTx;
    private TextView dayLightTx;
    private TextView cloudyTx;

    public void updateUI(JSONObject json) {
        try {
            String data = json.getJSONObject("get_imagesetting_response").getJSONObject("data").getString("white_balance");
            updateImage(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void updateImage(String value) {
        this.incandescentImage.setImageResource(R.drawable.incandescent);
        this.fluorescentImage.setImageResource(R.drawable.fluorescent);
        this.autoIcon.setImageResource(R.drawable.auto_icon);
        this.dayLight.setImageResource(R.drawable.daylight);
        this.cloudy.setImageResource(R.drawable.cloudy);

        this.incandescentTx.setTextColor(getResources().getColor(R.color.white));
        this.fluorescentTx.setTextColor(getResources().getColor(R.color.white));
        this.autoImageTx.setTextColor(getResources().getColor(R.color.white));
        this.dayLightTx.setTextColor(getResources().getColor(R.color.white));
        this.cloudyTx.setTextColor(getResources().getColor(R.color.white));

        if (value.equalsIgnoreCase("Incandescent")) {
            this.incandescentImage.setImageResource(R.drawable.incandescent_highlight);
            this.incandescentTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Fluorescent")) {
            this.fluorescentImage.setImageResource(R.drawable.fluorescent_highlight);
            this.fluorescentTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Auto")) {
            this.autoIcon.setImageResource(R.drawable.auto_icon_highlight);
            this.autoImageTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Daylight")) {
            this.dayLight.setImageResource(R.drawable.daylight_highlight);
            this.dayLightTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Cloudy")) {
            this.cloudy.setImageResource(R.drawable.cloudy_highlight);
            this.cloudyTx.setTextColor(getResources().getColor(R.color.mainColor));
        }
    }

    private void init(View view) {
        this.incandescentLayout = view.findViewById(R.id.incandescentLayout);
        this.fluorescentLayout = view.findViewById(R.id.fluorescentLayout);
        this.autoLayout = view.findViewById(R.id.autoLayout);
        this.daylightLayout = view.findViewById(R.id.daylightLayout);
        this.cloudyLayout = view.findViewById(R.id.cloudyLayout);

        this.incandescentImage = view.findViewById(R.id.incandescentImage);
        this.fluorescentImage = view.findViewById(R.id.fluorescentImage);
        this.autoIcon = view.findViewById(R.id.autoIcon);
        this.dayLight = view.findViewById(R.id.dayLight);
        this.cloudy = view.findViewById(R.id.cloudy);

        this.incandescentTx = view.findViewById(R.id.incandescentTx);
        this.fluorescentTx = view.findViewById(R.id.fluorescentTx);
        this.autoImageTx = view.findViewById(R.id.autoImageTx);
        this.dayLightTx = view.findViewById(R.id.dayLightTx);
        this.cloudyTx = view.findViewById(R.id.cloudyTx);
        action();
    }

    private void action() {
        this.incandescentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_WHITE_BALANCE;
                message.obj = "Incandescent";
                ApplicationService.mainHandler.sendMessage(message);

                updateImage("Incandescent");
            }
        });

        this.fluorescentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_WHITE_BALANCE;
                message.obj = "Fluorescent";
                ApplicationService.mainHandler.sendMessage(message);

                updateImage("Fluorescent");
            }
        });

        this.autoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_WHITE_BALANCE;
                message.obj = "Auto";
                ApplicationService.mainHandler.sendMessage(message);

                updateImage("Auto");
            }
        });

        this.daylightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_WHITE_BALANCE;
                message.obj = "Daylight";
                ApplicationService.mainHandler.sendMessage(message);

                updateImage("Daylight");
            }
        });

        this.cloudyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_WHITE_BALANCE;
                message.obj = "Cloudy";
                ApplicationService.mainHandler.sendMessage(message);

                updateImage("Cloudy");
            }
        });
    }
}