package com.bkav.aiotcloud.screen.home.camera.setting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.home.camera.CameraActivity;
import com.bkav.aiotcloud.screen.home.camera.playback.Playback;

import org.json.JSONObject;

import java.util.ArrayList;

public class LightSettingMain extends Fragment {

    public static final int DISPLAY_IMAGE = 0;
    public static final int WHITE_BALANCE = 1;
    public static final int NIGHT_MODE = 2;
    public static final int ROTATE = 3;
    public static final int FOCUS = 4;
    public static final int VIDEO = 5;
    public static final int PTZ = 6;
    public static final int DISPLAY_SETTING = 7;
    public static final int PLAYBACK = 8;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(currentPosition != LightSettingMain.PLAYBACK){
                this.backSetting.setVisibility(View.GONE);
                this.horizontalScrollView.setVisibility(View.GONE);
                setDataLand();
            }else{
                this.backSetting.setVisibility(View.VISIBLE);
                setLanPlayback();
            }
            actionLan();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.backSetting.setVisibility(View.GONE);
            setDataPor();
            action();
        }
    }


    public void paintSelectData(ArrayList<JSONObject> recordingFiles) {
//        playback.paintSelectData(recordingFiles);
    }

    public void paintSelectDataBox(ArrayList<JSONObject> recordingFiles) {
//        playback.paintSelectDataBox(recordingFiles);
    }

    public void playPlayback() {
        if (currentPosition == PLAYBACK){
//            playback.updateImgPerSec();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button even
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_POSITION, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.light_setting_fragment, container, false);
        id = requireArguments().getInt("ID");
        init(view);

//        if (ApplicationService.PARENT_ID != 0){
//            horizontalScrollView.setVisibility(View.GONE);
//        }
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.getCameraId() == id) {
                cameraSelect = cameraItem;
            }
        }
        updateViewPTZ(cameraSelect);
        orientation = getResources().getConfiguration().orientation;
        if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            setDataLand();
            actionLan();
        } else if (Configuration.ORIENTATION_PORTRAIT == orientation) {
            setDataPor();
            action();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateUIImageSetting(JSONObject currentStatement) {
        if (currentPosition == DISPLAY_IMAGE) {
            displayImageFragment.updateUI(currentStatement);
        } else if (currentPosition == WHITE_BALANCE) {
            whiteBalanceFragment.updateUI(currentStatement);
        } else if (currentPosition == NIGHT_MODE) {
            dayNightModeFragment.updateUI(currentStatement);
        } else if (currentPosition == ROTATE) {
            rotateFragment.updateUI(currentStatement);
        } else if (currentPosition == FOCUS) {
            rotateFragment.updateUI(currentStatement);
        } else if (currentPosition == VIDEO) {
            videoSettingFragment.updateUI(currentStatement);
        } else if (currentPosition == DISPLAY_SETTING) {
            displaySettingFragment.updateUI(currentStatement);
        }
    }

    public void setMinMaxImage(JSONObject jsonObject) {
        if (currentPosition == DISPLAY_IMAGE) {
            displayImageFragment.setMinMax(jsonObject);
        }
    }

    private CameraItem cameraSelect;
    public static final String KEY_POSITION = "key";
    private HorizontalScrollView horizontalScrollView;
    private RelativeLayout bar;
    private TextView nameCamera;
    private RelativeLayout back;
    private RelativeLayout layoutSetting;
    private RelativeLayout background;
    private TextView title;
    private ImageView displayImage;
    private ImageView whiteBalance;
    private ImageView nightMode;
    private ImageView rotate;
    private ImageView focus;
    private ImageView video;
    private ImageView ptz;
    private ImageView playbackIcon;
    private ImageView displaySetting;
    private FrameLayout frameLayout;
    private int currentPosition = -1;
    private ImageSettingFragment displayImageFragment;
    private WhiteBalanceFragment whiteBalanceFragment;
    private DayNightModeFragment dayNightModeFragment;
    private VideoSettingFragment videoSettingFragment;
    private RotateFragment rotateFragment;
    private FocusFragment focusFragment;
    private PTZMainFragment ptzMainFragment;
    private Playback playback;
    private DisplaySettingFragment displaySettingFragment;
    private int id;
    private int layoutSettingHeight;
    private int layoutSettingWidth;
    private int orientation;
    private RelativeLayout detailSettingGroup;
    private int shortAnimationDuration;
    private boolean showSetting = true;
    private Animation slideUp, slideDown, slideShowDown, slideShowUp;
    private String TAG = "LightSettingMain";
    private RelativeLayout backSetting;

    @SuppressLint("ResourceType")
    private void init(View view) {
        this.slideShowUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_show_up);
        this.slideShowDown = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_show_down);
        this.slideUp = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);
        this.slideDown = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);
        this.shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        this.detailSettingGroup = view.findViewById(R.id.detailSettingGroup);
        this.background = view.findViewById(R.id.cLayout1);
        this.horizontalScrollView = view.findViewById(R.id.horizontalScrollView);

//        if (ApplicationService.PARENT_ID != 0){
//            this.horizontalScrollView.setVisibility(View.GONE);
//        }
        this.bar = view.findViewById(R.id.bar);
        this.nameCamera = view.findViewById(R.id.nameCamera);
        this.back = view.findViewById(R.id.back);
        this.layoutSetting = view.findViewById(R.id.layoutSetting);
        this.layoutSettingHeight = this.layoutSetting.getLayoutParams().height;
        this.layoutSettingWidth = this.layoutSetting.getLayoutParams().width;
        this.title = view.findViewById(R.id.title);
        this.title.setText(LanguageManager.getInstance().getValue("title_setting_cam"));
        this.displayImage = view.findViewById(R.id.displaySetting);
        this.whiteBalance = view.findViewById(R.id.whiteBalance);
        this.nightMode = view.findViewById(R.id.nightMode);
        this.rotate = view.findViewById(R.id.rotate);
        this.focus = view.findViewById(R.id.focus);
        this.video = view.findViewById(R.id.video);
        this.ptz = view.findViewById(R.id.ptz);
        this.playbackIcon = view.findViewById(R.id.playbackIcon);
        this.displaySetting = view.findViewById(R.id.display);
        this.frameLayout = view.findViewById(R.id.layout_setting_detail);
        this.displayImageFragment = new ImageSettingFragment();
        this.whiteBalanceFragment = new WhiteBalanceFragment();
        this.dayNightModeFragment = new DayNightModeFragment();
        this.videoSettingFragment = new VideoSettingFragment();
        this.rotateFragment = new RotateFragment();
        this.focusFragment = new FocusFragment();
        this.ptzMainFragment = new PTZMainFragment();
        this.displaySettingFragment = new DisplaySettingFragment();
        this.playback = new Playback();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        this.ptzMainFragment.setArguments(bundle);
        this.displayImageFragment.setArguments(bundle);
        this.playback.setArguments(bundle);
        this.backSetting = view.findViewById(R.id.backSetting);
    }

    public void addFrament(int valueFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (valueFragment == DISPLAY_IMAGE) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_image"));
            transaction.replace(R.id.layout_setting_detail, displayImageFragment);
        } else if (valueFragment == WHITE_BALANCE) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_white_balance"));
            transaction.replace(R.id.layout_setting_detail, whiteBalanceFragment);
        } else if (valueFragment == NIGHT_MODE) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_day_night"));
            transaction.replace(R.id.layout_setting_detail, dayNightModeFragment);
        } else if (valueFragment == ROTATE) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_rotate"));
            transaction.replace(R.id.layout_setting_detail, rotateFragment);
        } else if (valueFragment == VIDEO) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_video"));
            transaction.replace(R.id.layout_setting_detail, videoSettingFragment);
        } else if (valueFragment == FOCUS) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_focus"));
            transaction.replace(R.id.layout_setting_detail, focusFragment);
        } else if (valueFragment == DISPLAY_SETTING) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_display"));
            transaction.replace(R.id.layout_setting_detail, displaySettingFragment);
            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_DISPLAY_INFO_SETTING);
        } else if (valueFragment == PTZ) {
            this.title.setText(LanguageManager.getInstance().getValue("title_setting_ptz"));
            transaction.replace(R.id.layout_setting_detail, ptzMainFragment);
        } else if (valueFragment == PLAYBACK) {
            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_RECORD_JOB_PLAYBACK);
            this.title.setText("");
            transaction.replace(R.id.layout_setting_detail, playback);
//            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_VIDEO_PLAYBACK);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void lightSettingAction(int value) {
//        Log.e(TAG, "action: " + value);
        if (value == this.currentPosition) {
            return;
        }
        this.displayImage.setImageResource(R.drawable.display_setting);
        this.whiteBalance.setImageResource(R.drawable.white_balance);
        this.nightMode.setImageResource(R.drawable.night_mode);
        this.rotate.setImageResource(R.drawable.rotate);
        this.focus.setImageResource(R.drawable.focus);
        this.video.setImageResource(R.drawable.video);
        this.ptz.setImageResource(R.drawable.ptz_icon);
        this.displaySetting.setImageResource(R.drawable.display_video);
        this.playbackIcon.setImageResource(R.drawable.playback_icon);
        if (value == LightSettingMain.DISPLAY_IMAGE) {
            this.displayImage.setImageResource(R.drawable.display_setting_highlight);
        } else if (value == LightSettingMain.WHITE_BALANCE) {
            this.whiteBalance.setImageResource(R.drawable.white_balance_highlight);
        } else if (value == LightSettingMain.NIGHT_MODE) {
            this.nightMode.setImageResource(R.drawable.night_mode_highlight);
        } else if (value == LightSettingMain.ROTATE) {
            this.rotate.setImageResource(R.drawable.rotate_highlight);
        } else if (value == LightSettingMain.FOCUS) {
            this.focus.setImageResource(R.drawable.focus_higlight);
        } else if (value == LightSettingMain.VIDEO) {
            this.video.setImageResource(R.drawable.video_highlight);
        } else if (value == LightSettingMain.PTZ) {
            this.ptz.setImageResource(R.drawable.ptz_icon_highlight);
        } else if (value == LightSettingMain.DISPLAY_SETTING) {
            this.displaySetting.setImageResource(R.drawable.display_video_highlight);
        } else if (value == LightSettingMain.PLAYBACK) {
            this.playbackIcon.setImageResource(R.drawable.playback_icon_highlight);
        }
        this.currentPosition = value;
        if(currentPosition != LightSettingMain.PLAYBACK){
            this.backSetting.setVisibility(View.GONE);
        }else{
            this.backSetting.setVisibility(View.VISIBLE);
        }
        addFrament(value);
    }

    private void updateViewPTZ(CameraItem cameraItem) {
        if (cameraItem.isPtzDevice() || cameraItem.isZoom()) {
            ptz.setVisibility(View.VISIBLE);
            lightSettingAction(PTZ);

        } else {
            ptz.setVisibility(View.GONE);
            lightSettingAction(DISPLAY_IMAGE);
        }

        if (cameraItem.getBoxId() != 0) {
            this.whiteBalance.setVisibility(View.GONE);
            this.nightMode.setVisibility(View.GONE);
            this.rotate.setVisibility(View.GONE);
            this.displaySetting.setVisibility(View.GONE);
            this.video.setVisibility(View.GONE);
        } else {
            this.playbackIcon.setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void actionLan() {
        this.backSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) getActivity()).shrinkToPotraitMode();
            }
        });
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CameraActivity) getActivity()).shrinkToPotraitMode();
            }
        });
        this.background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) {
                if (getResources().getConfiguration().orientation == 2) {
                    if(currentPosition != LightSettingMain.PLAYBACK){
                        if (showSetting) {
                            bar.startAnimation(slideUp);
                            bar.setVisibility(View.GONE);
//                            horizontalScrollView.setVisibility(View.GONE);
                            if (ApplicationService.PARENT_ID == 0){
                                horizontalScrollView.startAnimation(slideDown);
                            }
                            showSetting = false;
                        } else {
                            bar.startAnimation(slideShowDown);
                            bar.setVisibility(View.VISIBLE);
                            if (ApplicationService.PARENT_ID == 0){
                                horizontalScrollView.startAnimation(slideShowUp);
                                horizontalScrollView.setVisibility(View.VISIBLE);
                            }
                            showSetting = true;
                        }
                    }else{
                        setViewBar(bar);
                        setViewSetting(layoutSetting);
                    }
                }
                return false;
            }
        });


        this.layoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + "layout setting" );
                if (getResources().getConfiguration().orientation == 2) {
                    if(currentPosition != LightSettingMain.PLAYBACK){
                        setViewBar(bar);
                        if (ApplicationService.PARENT_ID == 0){

                        setViewSetting(horizontalScrollView);
                        }
                        setGone(layoutSetting);
                    }else{
                        setGoneBar(bar);
                        horizontalScrollView.setVisibility(View.GONE);
                        setGoneSetting(layoutSetting);
                    }
                }
            }
        });
        this.displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(DISPLAY_IMAGE);
            }
        });

        this.playbackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout.LayoutParams detailSettingParams = (RelativeLayout.LayoutParams) detailSettingGroup.getLayoutParams();
                detailSettingParams.height = (int) getResources().getDimension(R.dimen._100sdp);
                detailSettingParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                detailSettingParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
                detailSettingParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                detailSettingParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                detailSettingGroup.setLayoutParams(detailSettingParams);
                detailSettingGroup.setBackground(ContextCompat.getDrawable(getContext(), R.color.barColor));
                detailSettingGroup.getBackground().setAlpha(180);
                horizontalScrollView.setVisibility(View.GONE);
                setView(layoutSetting);
                lightSettingAction(PLAYBACK);
            }
        });

        this.whiteBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(WHITE_BALANCE);
            }
        });

        this.nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(NIGHT_MODE);
            }
        });

        this.rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(ROTATE);
            }
        });

        this.focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(FOCUS);
            }
        });

        this.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(VIDEO);
            }
        });

        this.ptz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(PTZ);
            }
        });
        this.displaySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(layoutSetting);
                setGoneBar(bar);
                setGoneSetting(horizontalScrollView);
                lightSettingAction(DISPLAY_SETTING);
            }
        });
    }

    private void action() {
        this.displayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(DISPLAY_IMAGE);
            }
        });

        this.whiteBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(WHITE_BALANCE);
            }
        });

        this.nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(NIGHT_MODE);
            }
        });

        this.rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(ROTATE);
            }
        });

        this.focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(FOCUS);
            }
        });

        this.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(VIDEO);
            }
        });

        this.ptz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(PTZ);
            }
        });
        this.displaySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(DISPLAY_SETTING);
            }
        });
        this.playbackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightSettingAction(PLAYBACK);
            }
        });
    }

    private void setGone(View view) {
        view.animate()
                .translationX(view.getWidth())
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void setView(View view) {
        view.animate()
                .translationX(0)
                .alpha(1.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                        view.bringToFront();
                    }
                });
    }

    private void setGoneBar(View view) {
//        view.setVisibility(View.GONE);
        view.animate()
                .translationY(-view.getHeight())
                .alpha(0.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void setViewBar(View view) {
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void setGoneSetting(View view) {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void setViewSetting(View view) {
        view.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }
    private void setLanPlayback(){
        bar.setVisibility(View.VISIBLE);
        nameCamera.setText(cameraSelect.getCameraName());
        background.setBackgroundResource(0);
        horizontalScrollView.setBackgroundResource(0);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutSetting.getLayoutParams();
        params.removeRule(RelativeLayout.ABOVE);
        layoutSetting.setLayoutParams(params);
        RelativeLayout.LayoutParams detailSettingParams = (RelativeLayout.LayoutParams) detailSettingGroup.getLayoutParams();
        detailSettingParams.height = (int) getResources().getDimension(R.dimen._100sdp);
        detailSettingParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        detailSettingParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        detailSettingParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        detailSettingParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        detailSettingGroup.setLayoutParams(detailSettingParams);
        detailSettingGroup.setBackground(ContextCompat.getDrawable(getContext(), R.color.barColor));
        detailSettingGroup.getBackground().setAlpha(180);
        setGoneSetting(horizontalScrollView);
    }

    private void setDataLand() {
        bar.setVisibility(View.VISIBLE);
        nameCamera.setText(cameraSelect.getCameraName());
        background.setBackgroundResource(0);
        horizontalScrollView.setBackgroundResource(0);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutSetting.getLayoutParams();
        params.removeRule(RelativeLayout.ABOVE);
        layoutSetting.setLayoutParams(params);
        RelativeLayout.LayoutParams detailSettingParams = (RelativeLayout.LayoutParams) detailSettingGroup.getLayoutParams();
        detailSettingParams.width = (int) getResources().getDimension(R.dimen._300sdp);
        detailSettingParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        detailSettingGroup.setLayoutParams(detailSettingParams);
        detailSettingGroup.setBackground(ContextCompat.getDrawable(getContext(), R.color.barColor));
        detailSettingGroup.getBackground().setAlpha(180);
        setGoneSetting(horizontalScrollView);
        setGoneBar(bar);

        if (ApplicationService.PARENT_ID != 0){
            horizontalScrollView.setVisibility(View.GONE);
            detailSettingGroup.setVisibility(View.GONE);
        }
    }

    private void setDataPor() {
        bar.setVisibility(View.GONE);
        nameCamera.setText("");
        background.setBackground(ContextCompat.getDrawable(getContext(), R.color.background));
        horizontalScrollView.setBackground(ContextCompat.getDrawable(getContext(), R.color.barColor));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutSetting.getLayoutParams();
        params.addRule(RelativeLayout.ABOVE, R.id.horizontalScrollView);
        layoutSetting.setLayoutParams(params);
        RelativeLayout.LayoutParams detailSettingParams = (RelativeLayout.LayoutParams) detailSettingGroup.getLayoutParams();
        detailSettingParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        detailSettingParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        detailSettingParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        detailSettingParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        detailSettingGroup.setLayoutParams(detailSettingParams);
        detailSettingGroup.setBackgroundResource(0);
        setView(layoutSetting);

        if (ApplicationService.PARENT_ID != 0){
            horizontalScrollView.setVisibility(View.GONE);
            layoutSetting.setVisibility(View.GONE);
            detailSettingGroup.setVisibility(View.GONE);
        } else {
            setViewSetting(horizontalScrollView);
        }
    }
}