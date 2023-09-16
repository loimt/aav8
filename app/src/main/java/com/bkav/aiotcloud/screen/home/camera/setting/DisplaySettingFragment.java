package com.bkav.aiotcloud.screen.home.camera.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.customer.ListTypeCustomer;
import com.bkav.aiotcloud.screen.setting.face.customer.SetColorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisplaySettingFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_setting_layout, container, false);
        init(view);
        setData();
//        updateUI(ApplicationService.dataSettingCam);
        action();
        return view;
    }

    private void setData(){
        this.save.setText(LanguageManager.getInstance().getValue("save"));
    }

    @Override
    public void onResume() {
        super.onResume();
//        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_DAY_NIGHT_MODE);
//        updateUI(ApplicationService.dataSettingCam);
    }

    public void updateUI(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("get_displaysetting_response").getJSONObject("data");
            this.cameraNameInfor = data.getJSONObject("cam_name_info");
            this.dateTimeInfor = data.getJSONObject("date_time_info");
            this.resolution = data.getJSONObject("resolution");

            this.displayNameSW.setChecked(data.getBoolean("cam_name_enable"));
            this.displayTimeSW.setChecked(data.getBoolean("date_time_enable"));
            this.nameCamera.setText(data.getJSONObject("cam_name_info").getString("cam_name"));
            this.nameCamera.setSelection(nameCamera.getText().length());
            JSONArray listSize = data.getJSONArray("size_list");
            listSizeText.clear();
            for (int index = 0; index < listSize.length(); index++) {
                listSizeText.add(listSize.get(index).toString());
            }
            this.spinnerSize.setSelection(listSizeText.indexOf(data.getString("size")));
            this.adapterSize.notifyDataSetChanged();

            this.currentColor = data.getString("color");

            isBasic = false;
            for (String element : colors) {
                if (this.currentColor.equalsIgnoreCase(element)) {
                    isBasic = true;
                }
            }
            if (!isBasic) {
                colors.add(8, currentColor);
            }

            for (int index = 0; index < colors.size(); index++) {
                if (currentColor.equalsIgnoreCase(colors.get(index))) {
                    setCheckedColor(index);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private RelativeLayout c1;
    private RelativeLayout c2;
    private RelativeLayout c3;
    private RelativeLayout c4;
    private RelativeLayout c5;
    private RelativeLayout c6;
    private RelativeLayout c7;
    private RelativeLayout c8;

    private ImageView t1;
    private ImageView t2;
    private ImageView t3;
    private ImageView t4;
    private ImageView t5;
    private ImageView t6;
    private ImageView t7;
    private ImageView t8;

    private List<String> colors;
    private List<RelativeLayout> colorLayout;
    private List<ImageView> listTick;
    private String color;
    private int currentBackground = 0;
    private String currentColor = "";
    private ImageView add;
    private Button save;
    private TextView colorTx;
    private TextView displayTimeTx;
    private TextView displayNameTx;

    private TextView textsizeTx;
    private List<String> listSizeText = new ArrayList<>();
    private boolean isBasic = false;
    private ArrayAdapter<String> adapterSize;
    private Spinner spinnerSize;

    private SwitchCompat displayTimeSW;
    private SwitchCompat displayNameSW;

    private JSONObject dateTimeInfor;
    private JSONObject cameraNameInfor;
    private JSONObject resolution;

    private EditText nameCamera;

    private ImageView tselect;
    private RelativeLayout selectColor;
    private int positionSexSelected = 0;

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        currentColor = data.getExtras().getString(SetColorDialog.COLOR);
                        colors.add(8, currentColor);
                        setCheckedColor(8);
                    }
                }
            });


    private void init(View view) {
        this.save = view.findViewById(R.id.save);
        this.c1 = view.findViewById(R.id.c1);
        this.c2 = view.findViewById(R.id.c2);
        this.c3 = view.findViewById(R.id.c3);
        this.c4 = view.findViewById(R.id.c4);
        this.c5 = view.findViewById(R.id.c5);
        this.c6 = view.findViewById(R.id.c6);
        this.c7 = view.findViewById(R.id.c7);
        this.c8 = view.findViewById(R.id.c8);

        this.t1 = view.findViewById(R.id.t1);
        this.t2 = view.findViewById(R.id.t2);
        this.t3 = view.findViewById(R.id.t3);
        this.t4 = view.findViewById(R.id.t4);
        this.t5 = view.findViewById(R.id.t5);
        this.t6 = view.findViewById(R.id.t6);
        this.t7 = view.findViewById(R.id.t7);
        this.t8 = view.findViewById(R.id.t8);
        this.tselect = view.findViewById(R.id.tselect);
        this.add = view.findViewById(R.id.add);
        this.save = view.findViewById(R.id.save);
        this.selectColor = view.findViewById(R.id.colorSelect);

        this.displayNameSW = view.findViewById(R.id.displayNameSW);
        this.displayTimeSW = view.findViewById(R.id.displayTimeSW);
        this.displayNameTx = view.findViewById(R.id.displayNameTx);
        this.displayTimeTx = view.findViewById(R.id.displayTimeTx);
        TextView textsizeTx = view.findViewById(R.id.textsizeTx);
        TextView colorTx = view.findViewById(R.id.colorTx);;

        displayTimeTx.setText(LanguageManager.getInstance().getValue("display_time"));
        displayNameTx.setText(LanguageManager.getInstance().getValue("display_name"));
        textsizeTx.setText(LanguageManager.getInstance().getValue("text_size"));
        colorTx.setText(LanguageManager.getInstance().getValue("choose_color"));

        this.spinnerSize = view.findViewById(R.id.spinnerSizeText);
        this.nameCamera = view.findViewById(R.id.nameCamera);

        this.colors = Stream.of("#FD413C", "#E0A818", "#4AA541", "#FD7B38", "#3BB7FD", "#818181", "#1C9AAB", "#6D45C0").collect(Collectors.toList());

        this.colorLayout = Stream.of(c1, c2, c3, c4, c5, c6, c7, c8, selectColor).collect(Collectors.toList());
        this.listTick = Stream.of(t1, t2, t3, t4, t5, t6, t7, t8, tselect).collect(Collectors.toList());

        for (int index = 0; index < colors.size(); index++) {
            this.colorLayout.get(index).setBackgroundResource(R.drawable.retangcle);
            GradientDrawable drawable = (GradientDrawable) this.colorLayout.get(index).getBackground();
            drawable.setColor(Color.parseColor(colors.get(index)));
        }

        adapterSize = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listSizeText) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == positionSexSelected) {
                    text.setTextColor(Objects.requireNonNull(getActivity()).getColor(R.color.mainColor));
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

        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapterSize);
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionSexSelected = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void action() {
        for (RelativeLayout layout : colorLayout) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckedColor(colorLayout.indexOf(layout));
                }
            });
        }

        this.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetColorDialog.class);
                intent.setFlags(0);
                if (currentColor.length() == 0) {
                    intent.putExtra(SetColorDialog.COLOR, "#FFFFFF");
                } else {
                    intent.putExtra(SetColorDialog.COLOR, currentColor);
                }
                startActivityIntent.launch(intent);
            }
        });
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.DISPLAY_SETTING_CAMERA;
                message.obj = DataChannelConfig.settingDisplay(dataRequest());
                ApplicationService.mainHandler.sendMessage(message);
            }
        });
    }

    private void setCheckedColor(int position) {
        currentBackground = position;
        for (int index = 0; index < colorLayout.size(); index++) {
            listTick.get(index).setVisibility(View.GONE);
        }
        listTick.get(position).setVisibility(View.VISIBLE);

        if (position == 8) {
//            tselect.setVisibility(View.VISIBLE);
            this.selectColor.setVisibility(View.VISIBLE);
            this.selectColor.setBackgroundResource(R.drawable.retangcle);
            GradientDrawable drawable = (GradientDrawable) this.selectColor.getBackground();
            drawable.setColor(Color.parseColor(colors.get(position)));
        }
        currentColor = colors.get(position);
    }

    private String dataRequest(){
        JSONObject data = new JSONObject();
        try {
            data.put("date_time_enable", displayTimeSW.isChecked());
            data.put("date_time_info", dateTimeInfor);
            data.put("cam_name_enable", displayNameSW.isChecked());
            data.put("cam_name_info", cameraNameInfor);
            data.put("resolution", resolution);
            data.put("size", listSizeText.get(positionSexSelected));
            data.put("color", currentColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}