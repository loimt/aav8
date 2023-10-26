package com.bkav.aiotcloud.screen.setting.face;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.AIConfig;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.view.ViewPagerNonSwipe;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

public class AddOjectFace extends AppCompatActivity {

    public static final int SUCCESS = 0;
    public static final int FORGET_SAVE = 1;
    public static final int SHORT_POINT = 2;
    public static final int NOT_CHECK = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.add_config_ai);
        type = getIntent().getStringExtra(SettingFragment.TYPE);
        this.tabLayout = findViewById(R.id.tab_layout);
        init();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
        ApplicationService.requestManager.getListCameraUnConfig(type);
    }

    private TextView title;
    private TabLayout tabLayout;

    private ImageView stepIm1;
    private TextView stepTx1;

    private ImageView stepIm2;
    private TextView stepTx2;

    private ImageView stepIm3;
    private TextView stepTx3;
    private RelativeLayout backIm;

    private StepOneFragment stepOneFragment;
    private StepTwoFragment stepTwoFragment;
    private StepThreeFragment stepThreeFragment;
    private String type;
    private Button next;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setData() {
        title.setText(LanguageManager.getInstance().getValue("camera_setting"));
        next.setText(LanguageManager.getInstance().getValue("next"));
    }

    private void init() {
        this.title = findViewById(R.id.title);
        ViewPagerNonSwipe viewPager = findViewById(R.id.pager);
        next = findViewById(R.id.nextBt);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.disableScroll(true);
        stepOneFragment = new StepOneFragment();
        stepTwoFragment = new StepTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ListCustomerActivity.TYPE, StepTwoFragment.NEW);
        stepTwoFragment.setArguments(bundle);
        stepThreeFragment = new StepThreeFragment();
        backIm = findViewById(R.id.backIm);
        StepPagerAdapter stepPagerAdapter = new StepPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(stepPagerAdapter);
        setupTabIcons();
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    stepTwoFragment.setCameraUnConfigItem(stepOneFragment.getCurrentSelected().getSnapShotUrl(), stepOneFragment.getCurrentSelected().getPeerID());
                } else if (position == 2) {
                    stepThreeFragment.setUI(type);
                    next.setText(LanguageManager.getInstance().getValue("save"));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.backIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (viewPager.getCurrentItem() == 0) {
                    if (stepOneFragment.checkStatement() == AddOjectFace.NOT_CHECK) {
//                        showToast(LanguageManager.getInstance().getValue("toast_not_choose_cam"));
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("toast_not_choose_cam"), true);
                        return;
                    }
                    stepTwoFragment.setIDCamera(stepOneFragment.getCurrentSelected().getCameraId());

                } else if (viewPager.getCurrentItem() == 1) {
                    if (stepTwoFragment.checkStatement() == FORGET_SAVE) {
//                        showToast(LanguageManager.getInstance().getValue("toast_not_save_polygon"));
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("toast_not_save_polygon"), true);
                        return;
                    }
                } else if (viewPager.getCurrentItem() == 2) {
                    JSONObject payload = null;
                    next.setText(LanguageManager.getInstance().getValue("save"));
                    if (type.equals(SettingFragment.FACE_RECOGNIZE)) {
                        payload = AIConfig.createNewConfigFace(stepOneFragment.getCurrentSelected(), stepTwoFragment.getPayload(), stepThreeFragment.getProfileFeatures(), stepThreeFragment.getScheduleDetails());
                    } else if (type.equals(SettingFragment.INTRUSION_DETECT)) {
                        payload = AIConfig.createNewAccessdetect(stepOneFragment.getCurrentSelected(), stepTwoFragment.getPayload(), stepThreeFragment.getdDetectConfig(), stepThreeFragment.getScheduleDetails());
                    } else if (type.equals(SettingFragment.LICENSE_PLATE)) {
                        payload = AIConfig.createNewLicensePlate(stepOneFragment.getCurrentSelected(), stepTwoFragment.getPayload(), stepThreeFragment.getScheduleDetails(), stepThreeFragment.getCurrenNation());
                    }
                    ApplicationService.requestManager.configCamera(payload, ApplicationService.URL_CONFIG_CAMERA);
                }
                if (viewPager.getCurrentItem() < 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    updateTabView(viewPager.getCurrentItem());
                }
            }
        });


        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int index = 0; index < tabStrip.getChildCount(); index++) {
            tabStrip.getChildAt(index).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    @SuppressLint("InflateParams")
    private void setupTabIcons() {
        View step1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.step_tab, null);
        this.stepIm1 = step1.findViewById(R.id.stepIm);
        this.stepIm1.setImageResource(R.drawable.step1_selected);
        this.stepTx1 = step1.findViewById(R.id.stepTx);
        this.stepTx1.setText(LanguageManager.getInstance().getValue("step1"));
        this.stepTx1.setTextColor(getResources().getColor(R.color.white));
        this.tabLayout.getTabAt(0).setCustomView(step1);

        View step2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.step_tab, null);
        this.stepIm2 = step2.findViewById(R.id.stepIm);
        this.stepIm2.setImageResource(R.drawable.step2);
        this.stepTx2 = step2.findViewById(R.id.stepTx);
        this.stepTx2.setText(LanguageManager.getInstance().getValue("step2"));
        this.stepTx2.setTextColor(getResources().getColor(R.color.textHint));
        this.tabLayout.getTabAt(1).setCustomView(step2);

        View step3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.step_tab, null);
        this.stepIm3 = step3.findViewById(R.id.stepIm);
        this.stepIm3.setImageResource(R.drawable.step3);

        this.stepTx3 = step3.findViewById(R.id.stepTx);
        this.stepTx3.setText(LanguageManager.getInstance().getValue("step3"));
        this.stepTx3.setTextColor(getResources().getColor(R.color.textHint));
        this.tabLayout.getTabAt(2).setCustomView(step3);
    }

    private void updateTabView(int position) {
        if (position == 0) {
            this.stepIm1.setImageResource(R.drawable.step1_selected);
            this.stepTx1.setTextColor(getResources().getColor(R.color.white));

            this.stepIm2.setImageResource(R.drawable.step2);
            this.stepTx2.setTextColor(getResources().getColor(R.color.textHint));

            this.stepIm3.setImageResource(R.drawable.step3);
            this.stepTx3.setTextColor(getResources().getColor(R.color.textHint));
        } else if (position == 1) {
            this.stepIm1.setImageResource(R.drawable.selected_icon);
            this.stepTx1.setTextColor(getResources().getColor(R.color.mainColor));

            this.stepIm2.setImageResource(R.drawable.step2_selected);
            this.stepTx2.setTextColor(getResources().getColor(R.color.white));

            this.stepIm3.setImageResource(R.drawable.step3);
            this.stepTx3.setTextColor(getResources().getColor(R.color.textHint));
        } else if (position == 2) {
            this.stepIm1.setImageResource(R.drawable.selected_icon);
            this.stepTx1.setTextColor(getResources().getColor(R.color.mainColor));

            this.stepIm2.setImageResource(R.drawable.selected_icon);
            this.stepTx2.setTextColor(getResources().getColor(R.color.mainColor));

            this.stepIm3.setImageResource(R.drawable.step3_selected);
            this.stepTx3.setTextColor(getResources().getColor(R.color.white));
        }
    }

    class StepPagerAdapter extends FragmentStatePagerAdapter {
        public StepPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment;

            if (index == 0) {
                fragment = stepOneFragment;
            } else if (index == 1) {
                fragment = stepTwoFragment;
            } else {
                fragment = stepThreeFragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
//                case ApplicationService.ANSWER_OFFER:
//                    JSONObject responseBody = (JSONObject) message.obj;
//                    stepTwoFragment.setOffer(responseBody);
//                    break;
                case ApplicationService.CONNECTED_CAMERA:
                    stepTwoFragment.connectCamera();
//                    touchLayout.setVisibility(View.GONE);
                    break;
                case ApplicationService.GET_LIST_CAMERA_UN_CONFIG:
                    stepOneFragment.updateListCamera();
                    break;
                case ApplicationService.CONFIG_CAMERA_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    finish();
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;

                    ApplicationService.showToast(messageER, true);
                    finish();
                    break;

            }
        }
    }
}
