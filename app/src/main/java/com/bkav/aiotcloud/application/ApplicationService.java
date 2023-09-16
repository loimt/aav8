package com.bkav.aiotcloud.application;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.UserItem;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.entity.aiobject.CameraUnConfigItem;
import com.bkav.aiotcloud.entity.aiobject.LanDevice;
import com.bkav.aiotcloud.entity.customer.CustomerEvent;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.entity.notify.NotifyItem;
import com.bkav.aiotcloud.entity.notifySetting.NotifySettingItem;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.network.BrManager;
import com.bkav.aiotcloud.network.RequestManager;
import com.bkav.aiotcloud.network.VolleyRequestManagement;
import com.bkav.aiotcloud.screen.StatisticThread;
import com.bkav.aiotcloud.util.CustomToast;
import com.bkav.bai.bridge.rtc.WebRTCClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.webrtc.PeerConnection;

import android.provider.Settings.Secure;

public class ApplicationService extends MultiDexApplication {
    public static final String CHANNEl_ID = "push_notification_id";
    private static final String TAG = ApplicationService.class.getName();
    public static final String KEY_PREFERENCES = "AI View Cloud";
    public static final String KEY_HOME_FOLDER = "homeFolder";


    public static final String FOLDER_SELECTED = "folderSelected";

    public static final String customerrecognize = "customerrecognize";
    public static final String accessdetect = "accessdetect";
    public static final String firedetect = "firedetect";
    public static final String persondetection = "persondetection";
    public static final String licenseplate = "licenseplate";


    public static final String HOST = "https://baav.aiview.ai"; // product
    public static final String CLIENT_ID = "61ceccdec7d895918a2f530f"; // product
    public static final String URL_SOCKET = "https://sav.aiview.ai"; // product

//    public static final String HOST = "https://psapidev.hcdt.vn"; // dev
//    public static final String CLIENT_ID = "612f533b4ea3809d6647ce97"; // dev
//    public static final String URL_SOCKET = "https://psemsdatacenter1dev.hcdt.vn"; // dev

//    public static final String HOST = "https://baavqc.aiview.ai"; // qc
//    public static final String CLIENT_ID = "635f73487d4dd6664b5644b0"; // qc
//    public static final String URL_SOCKET = "https://savqc.aiview.ai"; // qc

    public static final String URL_LOGIN = HOST + "/api/AppUserRegister/Login";
    public static final String URL_LOGOUT = HOST + "/api/AppUserRegister/Logout";
    public static final String URL_GET_LIST_CAM = HOST + "/api/cameraservice/getFilterDevice";
    public static final String URL_SETTING = HOST + "/CameraControl";

    public static final String URL_GET_LIST_CAMERA_CONFIG = HOST + "/api/cameraservice/getListCameraConfig";
    public static final String URL_GET_LIST_CAMERA_UN_CONFIG = HOST + "/api/cameraservice/getListCameraUnconfig";
    public static final String URL_CONFIG_CAMERA = HOST + "/api/cameraservice/configCamera";
    public static final String URL_GET_INFO_AI_CONFIG = HOST + "/api/cameraservice/getDetailCameraConfig";
    public static final String URL_DELETE_AI_CONFIG = HOST + "/api/cameraservice/removeConfigCamera";
    public static final String URL_GET_USER_INFOR = HOST + "/api/userservice/getUserInfo";
    public static final String URL_GET_ALL_CUSTOMER = HOST + "/api/customerservice/getListCustomerByFilter";
    public static final String URL_GET_ALL_TYPE_CUSTOMER = HOST + "/api/customertypeservice/getListCustomerTypeByFilter";

    public static final String URL_GET_DETAIL_EVENT_LAST_DAY = HOST + "/api/customerservice/statisticsCustomerChartSelected";
    public static final String URL_UPLOAD_IMAGE = HOST + "/api/customerservice/uploadImageByGuid";
    public static final String URL_UPLOAD_IMAGE_USERPROFILE = HOST + "/api/customerservice/uploadImageUser";






    public static final String URL_ADD_UPDATE_CUSTOMER = HOST + "/api/customerservice/addOrUpdateCustomer";

    public static final String URL_UPDATE_USERPROFILE = HOST + "/api/userservice/updateUser";
    public static final String URL_CHANGE_PASS = HOST + "/api/AppUserDetails/ChangePassword";
    public static final String URL_GET_LIST_NOTIFY = HOST + "/api/notificationservice/getListNotification";
    public static final String URL_GET_DETAIL_EVENT = HOST + "/api/notificationservice/getDetailNotification?objGuid=%s";
    public static final String URL_GET_DETAIL_EVENT_BYID = HOST + "/api/eventservice/getDetailById?eventId=%s";
    public static final String URL_GET_DETAIL_INFOR_CUSTOMER = HOST + "/api/customerservice/getCustomerByCustomerId?customerId=%d";
    public static final String URL_GET_LIST_NOTIFY_SETTING = HOST + "/api/applicationservice/getNotifySetting";
    public static final String URL_UPDATE_NOTIFY_SETTING = HOST + "/api/applicationservice/updateNotifySetting";
    public static final String URL_DELETE_TYPE_CUSTOMER = HOST + "/api/customertypeservice/deleteCustomerType?customerTypeId=%d";
    public static final String URL_ADD_UPDATE_CUSTOMER_TYPE = HOST + "/api/customertypeservice/addOrUpdateCustomerType";
    public static final String URL_RECOVER_PASS = HOST + "/api/AppUserRegister/RecoverPassword";
    public static final String URL_RESEND_RECOVER_PASS = HOST + "/api/AppUserRegister/ResendOTPRecoverPassword";
    public static final String URL_CONFIRM_OTP_RECOVER_PASS = HOST + "/api/AppUserRegister/confirmOTPRecoverPassword";
    public static final String URL_RECOVER_CHANGE_PASS = HOST + "/api/AppUserRegister/recoverchangepass";
    public static final String URL_DELETE_CUSTOMER = HOST + "/api/customerservice/deleteCustomer?customerId=%d";
    public static final String URL_GET_LIST_DEVICE = HOST + "/api/cameraservice/getFilterCamera";

    public static final String URL_GET_ALL_REGIONS = HOST + "/api/regionservice/getallregions";

    public static final String URL_CHECK_STATUS_CAM = HOST + "/api/cameraservice/checkCameraStatusBySerial";
    public static final String URL_REGISTER_CAM = HOST + "/api/cameraservice/registerCamera";
    public static final String URL_UPDATE_CAM = HOST + "/api/cameraservice/updateCamera";

    public static final String URL_GET_LIST_STATISTIC_CUSTOMER = HOST + "/api/customerservice/statisticsCustomer";
    public static final String URL_GET_LIST_STATISTIC = HOST + "/api/eventservice/detectStatisticEvent";
    public static final String URL_GET_TOTAL_EVENT_IN_DAY = HOST + "/api/eventservice/detectStatisticChart";

    public static final String URL_GET_LAST_EVENT = HOST + "/api/eventservice/getLastestByCamera?cameraId=0&identity=%s";
    public static final String URL_GET_CAM_DETAIL = HOST + "/api/cameraservice/getDeviceInfor?deviceid=%s";
    public static final String URL_DELETE_CAM = HOST + "/api/cameraservice/deleteCamera";

    public static final String URL_GET_CUSTOMER_EVENTS = HOST + "/api/customerservice/statisticsCustomerInfoIncoming";

    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_FAIL = 1;
    public static final int ANSWER_OFFER = 2;
    public static final int ICE_SERVER = 3;
    public static final int CONTROL_PTZ = 4;
    public static final int ZOOM_PTZ = 5;
    public static final int CONNECTED_CAMERA = 6;
    public static final int GET_IMAGE_INFO_SETTING = 7;
    public static final int SETTING_IMAGE = 8;
    public static final int UPDATE_LIST_CAMERA = 9;
    public static final int SETTING_WHITE_BALANCE = 10;
    public static final int GET_DAY_NIGHT_MODE = 11;
    public static final int SETTING_DAY_NIGHT = 12;
    public static final int SETTING_ROTATE = 13;
    public static final int SETTING_FOCUS = 14;
    public static final int GET_INFO_VIDEO = 15;
    public static final int SETTING_VIDEO = 16;
    public static final int GET_LIST_CAMERA_UN_CONFIG = 17;
    public static final int MESSAGE_ERROR = 18;
    public static final int CONFIG_CAMERA_SUCCESS = 19;
    public static final int INFOR_AI_CONFIG_DETAIL = 20;
    public static final int GET_LIST_CAMERA_CONFIG_SUCCESS = 21;
    public static final int DELETE_SUCCESS = 22;
    public static final int MESSAGE_ERROR_DELETE_AI = 23;
    public static final int GET_LIST_CUSTOMER_SUCCESS = 24;
    public static final int MESSAGE_UPLOAD_PHOTO_SUCESS = 25;
    public static final int MESSAGE_UPLOAD_FAILE = 26;
    public static final int MESSAGE_UPDATE_CUSTOMER_SUCCESS = 27;
    public static final int MESSAGE_GET_LIST_NOTIFY_SUCCESS = 28;
    public static final int MESSAGE_GET_DETAI_EVENT_SUCCESS = 29;
    public static final int LOGOUT_SUCCESS = 29;
    public static final int LOGOUT_FAIL = 30;
    public static final int MESSAGE_UPDATE_USERPROFILE_SUCCESS = 31;
    public static final int MESSAGE_UPDATE_USERPROFILE_FAIL = 32;
    public static final int MESSAGE_UPLOAD_PHOTO_PROFILE_SUCESS = 33;
    public static final int MESSAGE_CHANGE_PASS_FAIL = 34;
    public static final int MESSAGE_CHANGE_PASS_SUCCESS = 35;
    public static final int MESSAGE_GET_DETAI_CUSTOMER_SUCCESS = 36;
    public static final int MESSAGE_GET_LIST_NOTIFYSETTING_SUCCESS = 37;
    public static final int MESSAGE_GET_LIST_NOTIFYSETTING_FAIL = 38;
    public static final int MESSAGE_UPDATE_NOTIFYSETTING_SUCCESS = 39;
    public static final int MESSAGE_UPDATE_NOTIFYSETTING_FAIL = 40;
    public static final int GET_LIST_TYPE_CUSTOMER_SUCCESS = 41;
    public static final int ADD_UPDATE_TYPE_CUSTOMER_SUCCESS = 42;
    public static final int RECOVER_PASSWORD_SUCCESS = 43;
    public static final int RECOVER_PASSWORD_FAIL = 44;
    public static final int RESEND_RECOVER_PASSWORD_SUCCESS = 45;
    public static final int RESEND_RECOVER_PASSWORD_FAIL = 46;
    public static final int CONFIRM_RECOVER_PASSWORD_OTP_SUCCESS = 47;
    public static final int CONFIRM_RECOVER_PASSWORD_OTP_FAIL = 48;
    public static final int GET_ALL_REGIONS_SUCCESS = 49;
    public static final int GET_ALL_REGIONS_FAIL = 50;
    public static final int GET_UDP_DATA_SUCCESS = 51;
    public static final int GET_UDP_DATA_FAIL = 52;
    public static final int CONNECT_CLIENT_SUCCESS = 53;
    public static final int CONNECT_CLIENT_FAIL = 54;
    public static final int CONNECT_CLIENT_CONNECTING = 55;
    public static final int CONNECT_CLIENT_END = 56;
    public static final int GET_CAM_LAN_INFO_SUCCESS = 57;
    public static final int GET_CAM_LAN_INFO_FAIL = 58;
    public static final int CONFIG_CAMERA_FAIL = 59;
    public static final int CHECK_STATUS_CAM_SUCCESS = 60;
    public static final int CHECK_STATUS_CAM_FAIL = 61;
    public static final int REGISTER_CAM_SUCCESS = 62;
    public static final int REGISTER_CAM_FAIL = 63;
    public static final int UPDATE_CAM_SUCCESS = 64;
    public static final int UPDATE_CAM_FAIL = 65;
    public static final int GET_DISPLAY_INFO_SETTING = 66;
    public static final int DISPLAY_SETTING_CAMERA = 67;
    public static final int SET_INFO_VIDEO = 68;
    public static final int GET_LAST_EVENT_FAIL = 69;
    public static final int GET_EVENT_STATISTIC_SUCESS = 70;
    public static final int GET_EVENT_STATISTIC_FAIL = 71;

    public static final int GET_CAM_DETAIL_SUCCESS = 72;
    public static final int GET_CAM_DETAIL_FAIL = 73;

    public static final int DELETE_CAM_SUCCESS = 74;
    public static final int DELETE_CAM_FAIL = 75;

    public static final int GET_CUSTOMER_EVENTS_SUCCESS = 76;
    public static final int GET_CUSTOMER_EVENTS_FAIL = 77;


    public static final int GET_VIDEO_PLAYBACK = 78;
    public static final int PLAY_VIDEO_PLAYBACK = 79;
    public static final int NO_DATA_PLAYBACK = 80;
    public static final int SEEK_DATA_PLAYBACK = 81;
    public static final int START_PLAYBACK_CAMERA = 82;
    public static final int UPDATE_LIST_DEVICE = 83;

    public static final int GET_LIST_ACCESS_DETECT_SUCESS = 84;

    public static final int GET_RECORD_JOB_PLAYBACK = 85;


    public static int screenHeight;
    public static int screenWidth;

    public static JSONObject dataSettingCam;
    public static String TOKEN = null;
    public static String USERNAME = null;
    public static String FIREBASE_ID;
    public static String DEVICE_ID = "";

    public static int PARENT_ID = 0;
    public static String OBJECTGUID = "";
    public static String startTime = "";
    public static String endTime = "";
    public static String listCameraId = "";
    public static String applicationId = "";
    public static int countWidgetLincen = 1;
    public static int countWidgetFari = 1;

    public int VERSION_CODE;
    public String VERSION_NAME = null;
    public static JSONObject securityObject;
    public static String UDP_DATA = null;
    public static boolean isLogin = false;
    public static List<PeerConnection.IceServer> peerIceServers;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BrManager.stop();
        WebRTCClient.release();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
        instance = this;
        init();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        FIREBASE_ID = task.getResult();
                        // Log and toast
                        Log.e(TAG, FIREBASE_ID);
                    }
                });
        getSizeScreen();

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }

    private void createChannelNotification() {
        NotificationChannel channel = new NotificationChannel(CHANNEl_ID, "PushNotification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }


    public static Context getAppContext() {
        return instance;
    }

    public static Handler mainHandler = null;
    public static SharedPreferences homePreferences;
    private static ApplicationService instance;
    public static String currentFolder = "";
    public static final String HOME = "home";
    public static UserItem user = null;
    public static Boolean isUpdateWidget = false;

    public static String identitySetting = null;
    public static Boolean isActiveIdentitySetting;

    public static ArrayList<CustomerEvent> customerEvents;
    public static ArrayList<CameraItem> cameraitems;
    public static ArrayList<DeviceItem> deviceItems;
    public static ArrayList<CameraItem> cameraitemsFilter;
    public static StatisticThread statisticThread;
    public static ArrayList<AIObject> cameraConfigs;
    public static ArrayList<CustomerItem> customerItems;
    public static ArrayList<CameraUnConfigItem> cameraUnConfigItems;
    public static ArrayList<TypeCustomerItem> typeCustomerItems;
    public static ArrayList<NotifyItem> notifyItems;
    public static ArrayList<NotifySettingItem> notifySettingItems;
    public static ArrayList<Region> regions;
    public static ArrayList<LanDevice> listLanDevices;
    public static ArrayList<WidgetItem> listWidgetView, listWidget, listWidgetType;
    public static WidgetItem fariWidget, intrudWidget, fideWidget, pedeWidget, licenWidget, fariWidgetSpan, intrudWidgetSpan, fideWidgetSpan, pedeWidgetSpan, licenWidgetSpan;
    public static RequestManager requestManager;
    public static VolleyRequestManagement volleyRequestManagement;

    @SuppressLint("HardwareIds")
    private void init() {
        customerEvents = new ArrayList<>();
        listWidgetType = new ArrayList<>();
        listWidgetView = new ArrayList<>();
        listWidget = new ArrayList<>();
        requestManager = new RequestManager();
        volleyRequestManagement = new VolleyRequestManagement();
        cameraitems = new ArrayList<>();
        deviceItems = new ArrayList<>();
        cameraitemsFilter = new ArrayList<>();
        peerIceServers = new ArrayList<>();
        cameraConfigs = new ArrayList<>();
        cameraUnConfigItems = new ArrayList<>();
        customerItems = new ArrayList<>();
        typeCustomerItems = new ArrayList<>();
        notifyItems = new ArrayList<>();
        notifySettingItems = new ArrayList<>();
        regions = new ArrayList<>();
        listLanDevices = new ArrayList<>();
        DEVICE_ID = Secure.getString(getAppContext().getContentResolver(),
                Secure.ANDROID_ID);
        ;
        VERSION_NAME = BuildConfig.VERSION_NAME;
        VERSION_CODE = BuildConfig.VERSION_CODE;
        securityObject = new JSONObject();
        dataSettingCam = new JSONObject();
        initWidget();
//        getStateWidget();
    }

    public static void clearData() {
        cameraitems.clear();
        deviceItems.clear();
        peerIceServers.clear();
        cameraConfigs.clear();
        cameraUnConfigItems.clear();
        customerItems.clear();
        typeCustomerItems.clear();
        notifyItems.clear();
        notifySettingItems.clear();
        listWidgetView.clear();
        listCameraId = null;
        applicationId = null;
        TOKEN = null;
        statisticThread = null;
    }

    private void getSizeScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    private void initWidget() {
        ApplicationService.fariWidget = new WidgetItem(ApplicationService.customerrecognize, 0, "", "", "0", false, 0, false, "", "", "", "");
        ApplicationService.intrudWidget = new WidgetItem(ApplicationService.accessdetect, 0, "", "", "0", false, 0, false, "", "", "", "");
        ApplicationService.fideWidget = new WidgetItem(ApplicationService.firedetect, 0, "", "", "0", false, 0, false, "", "", "", "");
        ApplicationService.pedeWidget = new WidgetItem(ApplicationService.persondetection, 0, "", "", "0", false, 0, false, "", "", "", "");
        ApplicationService.licenWidget = new WidgetItem(ApplicationService.licenseplate, 0, "", "", "0", false, 0, false, "", "", "", "");
        if (ApplicationService.listWidget.size() == 0) {
            ApplicationService.listWidget.addAll(Arrays.asList(ApplicationService.fariWidget, ApplicationService.intrudWidget, ApplicationService.licenWidget));
        }
    }



    public static void showToast(String message, boolean isEr) {
        CustomToast.makeText(getAppContext(), message, isEr);
    }
}
