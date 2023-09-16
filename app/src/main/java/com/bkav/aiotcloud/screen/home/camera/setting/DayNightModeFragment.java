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

public class DayNightModeFragment extends Fragment {

    public static final String DAY_NIGHT_SETTING = "ptz";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_night_ftagment, container, false);
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_DAY_NIGHT_MODE);
        init(view);
//        updateUI(ApplicationService.dataSettingCam);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_DAY_NIGHT_MODE);
//        updateUI(ApplicationService.dataSettingCam);

    }

    private RelativeLayout autoLayout;
    private RelativeLayout dayLayout;
    private RelativeLayout nightLayout;

    private ImageView autoIcon;
    private ImageView dayImage;
    private ImageView nightImage;

    private TextView autoTx;
    private TextView dayTx;
    private TextView nightTx;

    public void updateUI(JSONObject json) {
        try {
            String data = json.getJSONObject("data").getString("day_night_mode");
            updateUI(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("ResourceAsColor")
    private void updateUI(String value) {
        this.dayImage.setImageResource(R.drawable.daylight);
        this.autoIcon.setImageResource(R.drawable.auto_icon);
        this.nightImage.setImageResource(R.drawable.night_mode);

        this.dayTx.setTextColor(getResources().getColor(R.color.white));
        this.autoTx.setTextColor(getResources().getColor(R.color.white));
        this.nightTx.setTextColor(getResources().getColor(R.color.white));

        if (value.equalsIgnoreCase("Day")) {
            this.dayImage.setImageResource(R.drawable.daylight_highlight);
            this.dayTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Auto")) {
            this.autoIcon.setImageResource(R.drawable.auto_icon_highlight);
            this.autoTx.setTextColor(getResources().getColor(R.color.mainColor));
        } else if (value.equalsIgnoreCase("Night")) {
            this.nightImage.setImageResource(R.drawable.night_mode_highlight);
            this.nightTx.setTextColor(getResources().getColor(R.color.mainColor));
        }
    }

    private void init(View view) {
        this.dayLayout = view.findViewById(R.id.dayLayout);
        this.nightLayout = view.findViewById(R.id.nightLayout);
        this.autoLayout = view.findViewById(R.id.autoLayout);

        this.dayImage = view.findViewById(R.id.dayImage);
        this.nightImage = view.findViewById(R.id.nightImage);
        this.autoIcon = view.findViewById(R.id.autoIcon);

        this.autoTx = view.findViewById(R.id.autoTx);
        this.dayTx = view.findViewById(R.id.dayTx);
        this.nightTx = view.findViewById(R.id.nightTx);
        action();
    }

    private void action() {
        this.dayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_DAY_NIGHT;
                message.obj = "Day";
                ApplicationService.mainHandler.sendMessage(message);

                updateUI("Day");
            }
        });

        this.nightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_DAY_NIGHT;
                message.obj = "Night";
                ApplicationService.mainHandler.sendMessage(message);
                updateUI("Night");
            }
        });

        this.autoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_DAY_NIGHT;
                message.obj = "Auto";
                ApplicationService.mainHandler.sendMessage(message);
                updateUI("Auto");
            }
        });
    }
}