package com.bkav.aiotcloud.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.screen.home.HomeFragment;
import com.bkav.aiotcloud.screen.notify.NotifyFragment;
import com.bkav.aiotcloud.screen.user.UserFragment;
import com.bkav.aiotcloud.screen.widget.WidgetFragment;
import com.bkav.aiotcloud.screen.setting.SettingFragment;
import com.bkav.bai.bridge.client.SignalingClient;
import com.bkav.bai.bridge.client.SignalingClientState;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainScreen extends AppCompatActivity {
    public static boolean isWidgetScreen = false;
    private String TAG = "MainScreen";
    public static String TYPE = "type";
    private DateTimeFormatter dtf;
    private LocalDateTime dateNow;
    private String startTime;
    private String endTime;
    public static int  currentFragment = 0;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> fragments;
    private HomeFragment homeFragment;
    private NotifyFragment notifyFragment;
    private WidgetFragment widgetFragment;
    private UserFragment userFragment;
    private SettingFragment settingFragment;
    private ImageView homeIcon;
    private ImageView searchIcon;
    private ImageView downloadIcon;
    private ImageView notifyIcon;
    private ImageView settingIcon;
    public  Socket socket;

    {
        try {
            socket = IO.socket(ApplicationService.URL_SOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initWidget();
        BrManager.start();
        WebRTCClient.setUp(this);
        // TODO load current config
        BrManager.currentConfig.setTrickle(SharePref.getInstance(getApplicationContext()).getTrickle());
        loadSignalingClient(BrManager.currentConfig.isTrickle());
        setContentView(R.layout.main_screen);
        tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
//        connectSocket();
        widgetFragment = new WidgetFragment();
        homeFragment = new HomeFragment();
        notifyFragment = new NotifyFragment();
        settingFragment = new SettingFragment();
        userFragment = new UserFragment();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        DemoCollectionPagerAdapter demoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(demoCollectionPagerAdapter);
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                updateTabView(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setupTabIcons();
        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);
        if (type.equals("notify")) {
            viewPager.setCurrentItem(3);
            updateTabView(3);
        } else {
//            getStateWidget();
            viewPager.setCurrentItem(0);
        }

//        ApplicationService.requestManager.getListCam(ApplicationService.URL_GET_LIST_CAM, null, null, 1, 20);
        getStateWidget();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ApplicationService.mainHandler = new MainHandler();
    }

    public void getStateWidget() {
        String widgetState = SharePref.getInstance(getApplicationContext()).getWidgetState().trim();
        Log.e("widgetState", widgetState);
        String[] arrayWidgetState = widgetState.split(" ");
        if (!widgetState.equals("")) {
            for (String s : arrayWidgetState) {
                String identity = s.split("_")[0];
                int viewType = Integer.parseInt(s.split("_")[1]);
                boolean spanned = viewType == 1 || viewType == 3;
                WidgetItem widgetItem = new WidgetItem(identity, 0, "", "", "0", false, viewType, spanned, "", "", "", "");
                ApplicationService.listWidgetView.add(widgetItem);
            }
        }
    }

    class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment;
            if (index == 0) {
                fragment = homeFragment;
            } else if (index == 1) {
                fragment = settingFragment;
            } else if (index == 2) {
                fragment = widgetFragment;
            } else if (index == 3) {
                fragment = notifyFragment;
            } else {
                fragment = userFragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @SuppressLint("InflateParams")
    private void setupTabIcons() {
        View tabAll = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_tab, null);
        this.homeIcon = tabAll.findViewById(R.id.tabIcon);
        this.homeIcon.setImageResource(R.drawable.home_highlight);
//        tabAll.setSelected(true);
        this.tabLayout.getTabAt(0).setCustomView(tabAll);

        View tabSearch = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_tab, null);
        this.searchIcon = tabSearch.findViewById(R.id.tabIcon);
        this.searchIcon.setImageResource(R.drawable.user_icon);
//        tabAll.setSelected(true);
        this.tabLayout.getTabAt(4).setCustomView(tabSearch);

        View tabDownload = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_tab, null);
        this.downloadIcon = tabDownload.findViewById(R.id.tabIcon);
        this.downloadIcon.setImageResource(R.drawable.widget_icon);
        this.downloadIcon.setColorFilter(getApplicationContext().getResources().getColor(R.color.grayTitle));
        this.tabLayout.getTabAt(2).setCustomView(tabDownload);

        View tabNotify = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_tab, null);
        this.notifyIcon = tabNotify.findViewById(R.id.tabIcon);
        this.notifyIcon.setImageResource(R.drawable.notify);
//        tabAll.setSelected(true);
        this.tabLayout.getTabAt(3).setCustomView(tabNotify);

        View tabSetting = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_tab, null);
        this.settingIcon = tabSetting.findViewById(R.id.tabIcon);
        settingIcon.setImageResource(R.drawable.setting);
//        tabAll.setSelected(true);
        this.tabLayout.getTabAt(1).setCustomView(tabSetting);
    }
    private void updateTabView(int position) {
        this.homeIcon.setImageResource(R.drawable.home);
        this.searchIcon.setImageResource(R.drawable.user_icon);
        this.downloadIcon.setImageResource(R.drawable.widget_icon);
        this.downloadIcon.setColorFilter(getApplicationContext().getResources().getColor(R.color.grayTitle));
        this.notifyIcon.setImageResource(R.drawable.notify);
        this.settingIcon.setImageResource(R.drawable.setting);
        isWidgetScreen = false;
        if (position == 0) {
            homeIcon.setImageResource(R.drawable.home_highlight);
        } else if (position == 4) {
            searchIcon.setImageResource(R.drawable.user_icon_highlight);
        } else if (position == 2) {
            this.downloadIcon.setColorFilter(getApplicationContext().getResources().getColor(R.color.mainColor));
            downloadIcon.setImageResource(R.drawable.widget_icon);
            isWidgetScreen = true;
        } else if (position == 3) {
            notifyIcon.setImageResource(R.drawable.notify_highlight);
        } else if (position == 1) {
            settingIcon.setImageResource(R.drawable.setting_highlight);
        }
    }


//    private void connectSocket() {
//        socket.disconnect();
//        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String content = "{"+"\"tokenLogin\":\""+"Bearer "+ApplicationService.TOKEN +"\"" +"}";
//                socket.emit("authenticate", content);
//            }
//        });
//       socket.on("request", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                String content = "{"+"\"tokenLogin\":\""+"Bearer "+ApplicationService.TOKEN +"\"" +"}";
//                socket.emit("authenticate", content);
//            }
//        });
//        socket.on("notify", onMessageReceived);
//        socket.connect();
//    }
//    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            try{
//                JSONObject jsonObject = new JSONObject(args[0].toString());
//                JSONObject data = jsonObject.getJSONObject("data");
//                String img = "";
//                String avaImg = "";
//                String timeCreate = "";
//                String content = "";
//                String identity = jsonObject.getString("identity");
//                if(identity.equals(ApplicationService.customerrecognize)){
//                    ApplicationService.countWidgetFari +=1;
//                    avaImg = data.getString("FilePath");
//                    img = new JSONObject(data.getJSONArray("ListImage").get(0).toString()).getString("FilePath");
//                    content = data.getString("Content");
//                    timeCreate = data.getString("DateOfDetected");
//                }else if(identity.equals(ApplicationService.licenseplate)){
//                    ApplicationService.countWidgetLincen +=1;
//                    avaImg = data.getString("cropImage");
//                    img = data.getString("fullImage");
//                    content = data.getString("noPlate");
//                    timeCreate = data.getString("createAt");
//                }else if(identity.equals(ApplicationService.accessdetect)){
//                    img = new JSONObject(data.getJSONArray("ListImage").get(0).toString()).getString("FilePath");
//                    content = data.getString("Content");
//                    timeCreate = data.getString("DateOfDetected");
//                }
//                setLastEvent(img, timeCreate, content, identity, avaImg);
//                ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_EVENT_STATISTIC_SUCESS);
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//    };

//    private void setLastEvent(String imagePath, String startAt, String content, String identity, String avaImg){
//        switch (identity) {
//            case ApplicationService.customerrecognize:
//                ApplicationService.fariWidget.setContent(content);
//                ApplicationService.fariWidget.setStartAt(startAt);
//                ApplicationService.fariWidget.setImagePath(imagePath);
//                if(ApplicationService.countWidgetFari%2==0){
//                    ApplicationService.fariWidget.setFirstImg(avaImg);
//
//                }else{
//                    ApplicationService.fariWidget.setSecondImg(avaImg);
//                }
//                break;
//            case ApplicationService.accessdetect:
//                ApplicationService.intrudWidget.setContent(content);
//                ApplicationService.intrudWidget.setStartAt(startAt);
//                ApplicationService.intrudWidget.setFirstImg(imagePath);
//                ApplicationService.intrudWidget.setImagePath(avaImg);
//                break;
//            case ApplicationService.firedetect:
//                ApplicationService.fideWidget.setContent(content);
//                ApplicationService.fideWidget.setStartAt(startAt);
//                ApplicationService.fideWidget.setFirstImg(imagePath);
//                break;
//            case ApplicationService.persondetection:
//                ApplicationService.pedeWidget.setContent(content);
//                ApplicationService.fariWidget.setStartAt(startAt);
//                ApplicationService.fariWidget.setFirstImg(imagePath);
//                break;
//            case ApplicationService.licenseplate:
//                ApplicationService.licenWidget.setContent(content);
//                ApplicationService.licenWidget.setStartAt(startAt);
//                ApplicationService.licenWidget.setImagePath(imagePath);
//                if(ApplicationService.countWidgetLincen%2==0){
//                    ApplicationService.licenWidget.setFirstImg(avaImg);
//                }else{
//                    ApplicationService.licenWidget.setSecondImg(avaImg);
//                }
//                break;
//
//        }
//        for(WidgetItem widget : ApplicationService.listWidgetView){
//            if(widget.getIndetify().equals(identity)){
//                widget.setContent(content);
//                widget.setStartAt(startAt);
//                widget.setImagePath(imagePath);
//                if(identity.equals(ApplicationService.customerrecognize)){
//                    if(ApplicationService.countWidgetFari%2==0){
//                        widget.setFirstImg(avaImg);
//                        widget.setFirstContent(content);
//                    }else{
//                        widget.setSecondImg(avaImg);
//                        widget.setSecondContent(content);
//                    }
//                }else if(identity.equals(ApplicationService.licenseplate)){
//                    if(ApplicationService.countWidgetLincen%2==0){
//                        widget.setFirstImg(avaImg);
//                        widget.setFirstContent(content);
//
//                    }else{
//                        widget.setSecondImg(avaImg);
//                        widget.setSecondContent(content);
//
//                    }
//                }
//            }
//        }
//    }

//    private void initWidget(){
//        ApplicationService.fariWidget = new WidgetItem(ApplicationService.customerrecognize, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.intrudWidget = new WidgetItem(ApplicationService.accessdetect, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.fideWidget = new WidgetItem(ApplicationService.firedetect, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.pedeWidget = new WidgetItem(ApplicationService.persondetection, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.licenWidget = new WidgetItem(ApplicationService.licenseplate, 0, "", "", "0",false, 0, false, "", "", "", "");
//        if(ApplicationService.listWidget.size() == 0){
//            ApplicationService.listWidget.addAll(Arrays.asList(ApplicationService.fariWidget, ApplicationService.intrudWidget, ApplicationService.licenWidget));
//        }
//    }

//    private void disconectSocket(){
//        socket.disconnect();
//        socket.off("notify", onMessageReceived);
//        socket.off("notify");
//        socket.close();
//        socket=null;
//    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case ApplicationService.MESSAGE_ERROR:
                    String messageER = (String) message.obj;
                    ApplicationService.showToast(messageER, true);
                    break;
                case ApplicationService.UPDATE_LIST_CAMERA:
                    homeFragment.updateUI();
                    break;
                case ApplicationService.MESSAGE_GET_LIST_NOTIFY_SUCCESS:
                    Log.e(TAG, "handleMessage: " + ApplicationService.notifyItems.size() );
                    if(ApplicationService.notifyItems.size() == 0){
                        notifyFragment.setNodata();
                    }else{
                        notifyFragment.unSetNoData();
                    }
                    notifyFragment.updateUI();
                    break;
                case ApplicationService.GET_LAST_EVENT_FAIL:
                    ApplicationService.showToast("Fail get last event", true);
                    if (widgetFragment != null){
                        widgetFragment.updateUI();
                    }
                    break;
                case ApplicationService.LOGOUT_SUCCESS:
//                    disconectSocket();
                    SharePref.getInstance(getApplicationContext()).removeAllKey();
                    ApplicationService.statisticThread.interrupt();
                    ApplicationService.clearData();
//                    Log.e(TAG, "handleMessage: " + "Logout success" );
                    Intent intent = new Intent(getApplicationContext(), LoginActiviry.class);
                    startActivity(intent);
                    break;
                case ApplicationService.GET_EVENT_STATISTIC_SUCESS:
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(ApplicationService.isUpdateWidget){
                                widgetFragment.updateUI();
                            }                        }
                    }, 2000);
                    break;
            }
        }
    }

    private void loadSignalingClient(boolean connectNow) {
        BrManager.runTask(() -> {
            SignalingClient signalingClient = BrManager.loadSignalingClient();
            if (signalingClient == null) {
                return; // throw error
            }
            signalingClient.addChangeStateHandler((client, state) -> {
                if (state == SignalingClientState.Connected) {
//                    getApplicationContext().runOnUiThread(() -> Toast.makeText(this, "Connect Ready", Toast.LENGTH_LONG).show());
                }
            });
            if (connectNow) {
                try {
                    signalingClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}