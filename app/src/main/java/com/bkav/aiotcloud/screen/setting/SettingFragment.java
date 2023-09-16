package com.bkav.aiotcloud.screen.setting;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.FaceDetectionActivity;
import com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity;

public class SettingFragment extends Fragment {
    public static final String TYPE = "type";
    public static final String INTRUSION_DETECT = "accessdetect";
    public static final String LICENSE_PLATE = "licenseplate";
    public static final String FACE_RECOGNIZE = "customerrecognize";
    public static final String FIRE_DETECT = "firedetect";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragview = inflater.inflate(R.layout.setting_fragment, container, false);

        this.customerLayout = fragview.findViewById(R.id.customerLayout);
        this.intrusionLayout = fragview.findViewById(R.id.intrusionLayout);
        this.fireLayout = fragview.findViewById(R.id.fireLayout);
        this.licenseLayout = fragview.findViewById(R.id.licenseLayout);

        this.faceDetectionCustomertx = fragview.findViewById(R.id.faceDetectionCustomertx);
        this.intrusionTx = fragview.findViewById(R.id.intrusionTx);
        this.fireTx = fragview.findViewById(R.id.fireTx);
        this.lableTx = fragview.findViewById(R.id.lableTx);
        this.title = fragview.findViewById(R.id.title);

        this.faceDetectionCustomertx.setText(LanguageManager.getInstance().getValue("face_recognize"));
        this.intrusionTx.setText(LanguageManager.getInstance().getValue("intrusion_detect"));
        this.fireTx.setText(LanguageManager.getInstance().getValue("fire_detect"));
        this.lableTx.setText(LanguageManager.getInstance().getValue("lable_detect"));
        this.title.setText(LanguageManager.getInstance().getValue("setting_ai"));

        action();
        return fragview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void action(){
        this.customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FaceDetectionActivity.class);
                startActivity(intent);
            }
        });

        this.intrusionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListFaceObjectActivity.class);
                intent.putExtra(TYPE, INTRUSION_DETECT);
                startActivity(intent);
            }
        });

        this.licenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListFaceObjectActivity.class);
                intent.putExtra(TYPE, LICENSE_PLATE);
                startActivity(intent);
            }
        });
    }

    private RelativeLayout customerLayout;
    private RelativeLayout intrusionLayout;
    private RelativeLayout fireLayout;
    private RelativeLayout licenseLayout;

    private TextView faceDetectionCustomertx;
    private TextView intrusionTx;
    private TextView fireTx;
    private TextView lableTx;
    private TextView title;
}