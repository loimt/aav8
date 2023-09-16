package com.bkav.aiotcloud.screen.setting.face.edit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.AIConfig;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.aiotcloud.screen.setting.face.ListFaceObjectActivity;
import com.bkav.aiotcloud.screen.setting.face.StepThreeFragment;
import com.bkav.aiotcloud.screen.setting.face.StepTwoFragment;
import com.bkav.aiotcloud.screen.setting.face.customer.ListCustomerActivity;
import com.bkav.aiotcloud.view.ViewPagerNonSwipe;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class EditObjectFace extends AppCompatActivity {

    public static final int FORGET_SAVE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.add_config_ai);
        position = getIntent().getIntExtra(ListFaceObjectActivity.AI_ITEM, 0);
        type = getIntent().getStringExtra(SettingFragment.TYPE);
        aiObject = ApplicationService.cameraConfigs.get(position);
        aiObject.getZones();
        this.tabLayout = findViewById(R.id.tab_layout);
        init();
    }

    private void getInforAIObject() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("cameraId", aiObject.getCameraId());
            payload.put("monitorGuid", aiObject.getMonitorId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApplicationService.requestManager.getInforAIDetail(payload, ApplicationService.URL_GET_INFO_AI_CONFIG);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private TabLayout tabLayout;

    private ImageView stepIm2;
    private TextView stepTx2;
    private String type;
    private ImageView stepIm3;
    private TextView stepTx3;
    private AIObject aiObject;
    private RelativeLayout backIm;

    private StepTwoFragment stepTwoFragment;
    private StepThreeFragment stepThreeFragment;
    private JSONObject itemEdit;
    private Button next;
    private TextView title;
    private int position;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        ViewPagerNonSwipe viewPager = findViewById(R.id.pager);
        next = findViewById(R.id.nextBt);
        this.backIm = findViewById(R.id.backIm);
        this.title = findViewById(R.id.title);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.disableScroll(true);
        stepTwoFragment = new StepTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ListCustomerActivity.TYPE, StepTwoFragment.EDIT);
        bundle.putInt("cameraID",aiObject.getCameraId());
        stepTwoFragment.setArguments(bundle);

        stepThreeFragment = new StepThreeFragment();
        stepThreeFragment.setType(type);
        this.next.setText(LanguageManager.getInstance().getValue("next"));
        this.title.setText(LanguageManager.getInstance().getValue("config"));
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
            if (position == 1){
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
            @SuppressLint({"ObsoleteSdkInt", "SupportAnnotationUsage"})
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    if (stepTwoFragment.checkStatement() == FORGET_SAVE) {
                        ApplicationService.showToast(LanguageManager.getInstance().getValue("toast_not_save_polygon"), true);
                        return;
                    }
                } else if (viewPager.getCurrentItem() == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      JSONObject payload = null;

                        if (type.equals(SettingFragment.FACE_RECOGNIZE)){
                            payload = AIConfig.editAIFaceConfig(itemEdit, stepTwoFragment.getPayload(), stepThreeFragment.getProfileFeatures(), stepThreeFragment.getScheduleDetails());
                        } else if (type.equals(SettingFragment.INTRUSION_DETECT)){
                            payload = AIConfig.editAccessDetectConfig(itemEdit, stepTwoFragment.getPayload(), stepThreeFragment.getdDetectConfig(), stepThreeFragment.getScheduleDetails());
                        } else if (type.equals(SettingFragment.LICENSE_PLATE)){
                            payload = AIConfig.editLicensePlate(itemEdit, stepTwoFragment.getPayload(), stepThreeFragment.getScheduleDetails(), stepThreeFragment.getCurrenNation());
                        }
                        ApplicationService.requestManager.configCamera(payload, ApplicationService.URL_CONFIG_CAMERA);
                    }
                }
                if (viewPager.getCurrentItem() < 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    updateTabView(viewPager.getCurrentItem());
                }
            }
        });

        new CountDownTimer(300, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                stepTwoFragment.setCameraUnConfigItem(aiObject.getSnapShotUrl(), aiObject.getPeerID());
                stepTwoFragment.drawZones(aiObject);
                ApplicationService.mainHandler = new MainHandler();
                getInforAIObject();
            }

        }.start();

//        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
//        for (int index = 0; index < tabStrip.getChildCount(); index++) {
//            tabStrip.getChildAt(index).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        }
    }

    @SuppressLint("InflateParams")
    private void setupTabIcons() {
        View step2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.step_tab, null);
        this.stepIm2 = step2.findViewById(R.id.stepIm);
        this.stepIm2.setImageResource(R.drawable.step1);
        this.stepTx2 = step2.findViewById(R.id.stepTx);
        this.stepTx2.setText(LanguageManager.getInstance().getValue("step2"));
        this.stepTx2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textHint));
        this.tabLayout.getTabAt(0).setCustomView(step2);

        View step3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.step_tab, null);
        this.stepIm3 = step3.findViewById(R.id.stepIm);
        this.stepIm3.setImageResource(R.drawable.step2);

        this.stepTx3 = step3.findViewById(R.id.stepTx);
        this.stepTx3.setText(LanguageManager.getInstance().getValue("step3"));
        this.stepTx3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textHint));
        Objects.requireNonNull(this.tabLayout.getTabAt(1)).setCustomView(step3);
    }

    private void updateTabView(int position) {
        if (position == 0) {
            this.stepIm2.setImageResource(R.drawable.step1_selected);
            this.stepTx2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            this.stepIm3.setImageResource(R.drawable.step2);
            this.stepTx3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textHint));
        } else if (position == 1) {
//            this.stepIm1.setImageResource(R.drawable.selected_icon);
//            this.stepTx1.setTextColor(getResources().getColor(R.color.mainColor));

            this.stepIm2.setImageResource(R.drawable.selected_icon);
            this.stepTx2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.mainColor));

            this.stepIm3.setImageResource(R.drawable.step2_selected);
            this.stepTx3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
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
                fragment = stepTwoFragment;
            } else {
                fragment = stepThreeFragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    @SuppressLint("HandlerLeak")
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
                    break;
                case ApplicationService.CONFIG_CAMERA_SUCCESS:
                    ApplicationService.showToast(LanguageManager.getInstance().getValue("add_config_success"), false);
                    Intent data = new Intent();
                    data.putExtra(StepThreeFragment.TYPE, InfoAIScreen.EDIT_DONE);
                    setResult(RESULT_OK, data);
                    finish();
                    break;

                case ApplicationService.INFOR_AI_CONFIG_DETAIL:
                    String dataInfor = (String) message.obj;
                    Log.e("xxxxxxxx", dataInfor);
                    try {
                        itemEdit = new JSONObject(dataInfor);
                        stepThreeFragment.setUI(type);
                        stepThreeFragment.setDataEdit(itemEdit);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                    Intent dataBack = new Intent();
                    dataBack.putExtra(StepThreeFragment.TYPE, InfoAIScreen.EDIT_DONE);
                    setResult(RESULT_OK, dataBack);
                    finish();
                    break;

            }
        }
    }
}
