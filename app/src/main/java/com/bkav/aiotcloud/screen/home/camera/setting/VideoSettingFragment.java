package com.bkav.aiotcloud.screen.home.camera.setting;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DataChannelConfig;
import com.bkav.aiotcloud.language.LanguageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class VideoSettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String VIDEO_SETTING = "media";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_video, container, false);
        init(view);
        setData();
        action();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Message message = new Message();
        message.what = ApplicationService.GET_INFO_VIDEO;
        message.obj = "Main";
        ApplicationService.mainHandler.sendMessage(message);
    }
    private void setData(){
        bitRateTx.setText(LanguageManager.getInstance().getValue("bitrate"));
        frameRateTx.setText(LanguageManager.getInstance().getValue("frame_rate"));
        resolutionTx.setText(LanguageManager.getInstance().getValue("resolution"));
        selectChannelTx.setText(LanguageManager.getInstance().getValue("select_channel"));
        typeBitRateTx.setText(LanguageManager.getInstance().getValue("bitrate_type"));
        qualityTx.setText(LanguageManager.getInstance().getValue("video_quality"));
        save.setText(LanguageManager.getInstance().getValue("save"));
    }


    private JSONObject currentValue;

    private TextView selectChannelTx;
    private Spinner selectChannelSpiner;
    private List<String> listChannel = new ArrayList<>();
    private ArrayAdapter<String> adapterChannel;
    private int channelSeclect = 0;

    private TextView resolutionTx;
    private Spinner spinerResolution;
    private List<String> listResolution = new ArrayList<>();
    private ArrayAdapter<String> adapterResolution;
    private int resolutionSelect = 0;


    private TextView bitRateTx;
    private Spinner spinerBitRate;
    private List<String> listBitRate = new ArrayList<>();
    private ArrayAdapter<String> adapterBitRate;
    private int bitRateSelect = 0;

    private TextView frameRateTx;
    private Spinner spinerFrameRate;
    private List<String> listFrameRate = new ArrayList<>();
    private ArrayAdapter<String> adapterFrameRate;
    private int frameRateSeclect = 0;

    private TextView codecTx;
    private Spinner spinerCodec;
    private List<String> listCodec = new ArrayList<>();
    private ArrayAdapter<String> adapterCodec;
    private int codecSeclect = 0;

    private TextView typeBitRateTx;
    private Spinner spinerTypeBitRate;
    private List<String> listTypeBiteRate = new ArrayList<>();
    private ArrayAdapter<String> adapterTypeBiteRate;
    private int typeBitRateSeclect = 0;

    private TextView qualityTx;
    private Spinner spinerQuality;
    private List<String> listQuality = new ArrayList<>();
    private ArrayAdapter<String> adapterQuality;
    private int qualitySeclect = 0;

    private SeekBar seekbarIFrame;
    private TextView iFrameValue;
    private Button save;

    @SuppressLint("ResourceAsColor")
    public void updateUI(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("get_videosetting_response").getJSONObject("data");

            // channel
            JSONArray listChannelData = data.getJSONArray("channel_list");
            listChannel.clear();
            for (int index = 0; index < listChannelData.length(); index++) {
                listChannel.add(listChannelData.get(index).toString());
            }
            this.selectChannelSpiner.setSelection(listChannel.indexOf(data.getString("channel")));
            this.adapterChannel.notifyDataSetChanged();

            // resolution
            JSONArray listResolutionData = data.getJSONArray("resolution_list");
            listResolution.clear();
            for (int index = 0; index < listResolutionData.length(); index++) {
                listResolution.add(listResolutionData.get(index).toString());
            }
            this.spinerResolution.setSelection(listResolution.indexOf(data.getString("resolution")));
            this.adapterResolution.notifyDataSetChanged();

            // bit rate
            JSONArray listBitRateData = data.getJSONArray("bitrate_list");
            listBitRate.clear();
            for (int index = 0; index < listBitRateData.length(); index++) {
                listBitRate.add(listBitRateData.get(index).toString());
            }
            this.spinerBitRate.setSelection(listBitRate.indexOf(data.getString("bitrate")));
            this.adapterBitRate.notifyDataSetChanged();

            // frame rate
            JSONArray listFrameRateData = data.getJSONArray("frame_rate_list");
            listFrameRate.clear();
            for (int index = 0; index < listFrameRateData.length(); index++) {
                listFrameRate.add(listFrameRateData.get(index).toString());
            }
            this.spinerFrameRate.setSelection(listFrameRate.indexOf(data.getString("frame_rate")));
            this.adapterFrameRate.notifyDataSetChanged();

            // frame rate
            JSONArray listCodecData = data.getJSONArray("codec_list");
            listCodec.clear();
            for (int index = 0; index < listCodecData.length(); index++) {
                listCodec.add(listCodecData.get(index).toString());
            }
            this.spinerCodec.setSelection(listCodec.indexOf(data.getString("codec")));
            this.adapterCodec.notifyDataSetChanged();

            // type bit rate
            JSONArray listTypeBitRateData = data.getJSONArray("bitrate_type_list");
            listTypeBiteRate.clear();
            for (int index = 0; index < listTypeBitRateData.length(); index++) {
                listTypeBiteRate.add(listTypeBitRateData.get(index).toString());
            }
            this.spinerTypeBitRate.setSelection(listTypeBiteRate.indexOf(data.getString("bitrate_type")));
            this.adapterTypeBiteRate.notifyDataSetChanged();

            // quality video
            JSONArray listQualityData = data.getJSONArray("video_quality_list");
            listQuality.clear();
            for (int index = 0; index < listQualityData.length(); index++) {
                listQuality.add(listQualityData.get(index).toString());
            }
            this.spinerQuality.setSelection(listQuality.indexOf(data.getString("video_quality")));
            this.adapterQuality.notifyDataSetChanged();

            // set iframe
            this.seekbarIFrame.setProgress(data.getInt("compression"));
            this.iFrameValue.setText(String.valueOf(data.getInt("compression")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        this.selectChannelTx = view.findViewById(R.id.selectChannelTx);
        this.selectChannelSpiner = view.findViewById(R.id.selectChannelSpiner);

        this.resolutionTx = view.findViewById(R.id.resolutionTx);
        this.spinerResolution = view.findViewById(R.id.spinerResolution);

        this.bitRateTx = view.findViewById(R.id.bitRateTx);
        this.spinerBitRate = view.findViewById(R.id.spinerBitRate);

        this.frameRateTx = view.findViewById(R.id.frameRateTx);
        this.spinerFrameRate = view.findViewById(R.id.spinerFrameRate);

        this.codecTx = view.findViewById(R.id.codecTx);
        this.spinerCodec = view.findViewById(R.id.spinerCodec);

        this.typeBitRateTx = view.findViewById(R.id.typeBitRateTx);
        this.spinerTypeBitRate = view.findViewById(R.id.spinerTypeBitRate);

        this.selectChannelTx = view.findViewById(R.id.selectChannelTx);
        this.selectChannelSpiner = view.findViewById(R.id.selectChannelSpiner);

        this.qualityTx = view.findViewById(R.id.qualityTx);
        this.spinerQuality = view.findViewById(R.id.spinerQuality);

        this.seekbarIFrame = view.findViewById(R.id.seekbarIFrame);
        this.iFrameValue = view.findViewById(R.id.txSeekBar);
        this.seekbarIFrame.setMin(0);
        this.seekbarIFrame.setMax(100);
        this.save = view.findViewById(R.id.save);
        setupSpinner();
    }

    private void action() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue();
            }
        });

        this.seekbarIFrame.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                iFrameValue.setText(String.valueOf(seekBar.getProgress()));
            }
        });
    }

    private void setupSpinner(){

        // channel
        adapterChannel = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listChannel) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == channelSeclect) {
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

        adapterChannel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectChannelSpiner.setAdapter(adapterChannel);
        selectChannelSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                channelSeclect = i;
                if (listChannel.size() > 0){
                    Message message = new Message();
                    message.what = ApplicationService.GET_INFO_VIDEO;
                    message.obj = listChannel.get(channelSeclect);
                    ApplicationService.mainHandler.sendMessage(message);
                }
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // resolution
        adapterResolution = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listResolution) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == resolutionSelect) {
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

        adapterResolution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerResolution.setAdapter(adapterResolution);
        spinerResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resolutionSelect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // bit rate
        adapterBitRate = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listBitRate) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == bitRateSelect) {
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

        adapterBitRate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerBitRate.setAdapter(adapterBitRate);
        spinerBitRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bitRateSelect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // bit rate
        adapterFrameRate = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listFrameRate) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == frameRateSeclect) {
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

        adapterFrameRate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerFrameRate.setAdapter(adapterFrameRate);
        spinerFrameRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                frameRateSeclect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // codec
        adapterCodec = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCodec) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == codecSeclect) {
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

        adapterCodec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCodec.setAdapter(adapterCodec);
        spinerCodec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                codecSeclect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // type bit rate
        adapterTypeBiteRate = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listTypeBiteRate) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == typeBitRateSeclect) {
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

        adapterTypeBiteRate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerTypeBitRate.setAdapter(adapterTypeBiteRate);
        spinerTypeBitRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeBitRateSeclect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // quality video
        adapterQuality = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listQuality) {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                if (position == qualitySeclect) {
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

        adapterQuality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerQuality.setAdapter(adapterQuality);
        spinerQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qualitySeclect = i;
                if (((TextView) view) != null) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setValue(){
        JSONObject data = new JSONObject();
        try {
            data.put("channel", listChannel.get(channelSeclect));
            data.put("resolution", listResolution.get(resolutionSelect));
            data.put("bitrate", listBitRate.get(bitRateSelect));
            data.put("frame_rate", Integer.valueOf(listFrameRate.get(frameRateSeclect)));
            data.put("bitrate_type", listTypeBiteRate.get(typeBitRateSeclect));
            data.put("compression", seekbarIFrame.getProgress());
            data.put("codec", listCodec.get(codecSeclect));
            data.put("video_quality", listQuality.get(qualitySeclect));

            Message message = new Message();
            message.what = ApplicationService.SET_INFO_VIDEO;
            message.obj = data.toString();
            ApplicationService.mainHandler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}