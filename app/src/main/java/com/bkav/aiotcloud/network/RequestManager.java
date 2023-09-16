package com.bkav.aiotcloud.network;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.application.Constant;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.UserItem;
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.entity.aiobject.AccessdetectItem;
import com.bkav.aiotcloud.entity.aiobject.CameraUnConfigItem;
import com.bkav.aiotcloud.entity.customer.CustomerEvent;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.entity.notify.NotifyItem;
import com.bkav.aiotcloud.entity.notifySetting.NotifySettingItem;
import com.bkav.aiotcloud.entity.region.Region;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.screen.StatisticThread;
import com.bkav.aiotcloud.screen.widget.EventAccessdetectIndays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.PeerConnection;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestManager {
    private static String TAG = "RequestManager";

    public void login(String user, String pass, String firebaseID, String url) {
        Log.e("responseBody ", url);
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", user);
            payload.put("password", pass);
            payload.put("os", 0);
            payload.put("firebaseID", firebaseID);
            payload.put("clientID",  ApplicationService.CLIENT_ID);
            payload.put("deviceID", ApplicationService.DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = loginCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void resetpassword(String mobile, String email) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("mobile", mobile);
            Log.e("reset", payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = recoverPassCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(ApplicationService.URL_RECOVER_PASS, payload.toString(), requestCallback);
    }
    public void logout(String firebaseID,  String url) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("firebaseID", firebaseID);
            payload.put("deviceID", ApplicationService.DEVICE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = logoutCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest logoutCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.LOGOUT_SUCCESS);
                        }
                    } else {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.LOGOUT_FAIL);
                    }
                    if (statusCode < 399) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void recoverPass(JSONObject payload, String url) {
        Log.e("statusCode"," " + payload);
        UrlRequestCallback.OnFinishRequest callbackInfo = recoverPassCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);

    }
    public UrlRequestCallback.OnFinishRequest recoverPassCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.RECOVER_PASSWORD_SUCCESS);
                            JSONObject object = new JSONObject(responseBody.getString("object"));
                            ApplicationService.OBJECTGUID = object.getString("objectGuid");
                        }
                    } else {
                        Message message = new Message();
                        message.obj = responseBody.getString("object");
                        message.what = ApplicationService.RECOVER_PASSWORD_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                    if (statusCode < 399) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void resendRecoverPass(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = resendRecoverPassCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest resendRecoverPassCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.RESEND_RECOVER_PASSWORD_SUCCESS);
                        }
                    } else {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.RESEND_RECOVER_PASSWORD_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void checkStatusCam(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = checkStatusCamCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest checkStatusCamCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.CHECK_STATUS_CAM_SUCCESS);
                        }
                    } else {
                        Message message = new Message();
                        message.obj = responseBody.getString("object");
                        message.what = ApplicationService.CHECK_STATUS_CAM_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void registerCam(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = registerCamCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest registerCamCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                Message message = new Message();
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            message.obj = responseBody.getString("object");
                            message.what = ApplicationService.REGISTER_CAM_SUCCESS;
                            ApplicationService.mainHandler.sendMessage(message);
                        }
                    } else {
                        message.what = ApplicationService.REGISTER_CAM_FAIL;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApplicationService.mainHandler.sendMessage(message);
            }
        };
    }
    public void updateCam(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = updateCamCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest updateCamCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                Message message = new Message();
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            message.what = ApplicationService.UPDATE_CAM_SUCCESS;
                        }
                    } else {
                        message.what = ApplicationService.UPDATE_CAM_FAIL;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApplicationService.mainHandler.sendMessage(message);
            }
        };
    }
    public void deleteCam(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = deleteCamCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest deleteCamCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                Message message = new Message();
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            message.what = ApplicationService.DELETE_CAM_SUCCESS;
                        }
                    } else {
                        message.what = ApplicationService.DELETE_CAM_FAIL;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApplicationService.mainHandler.sendMessage(message);
            }
        };
    }
    public void confirmOTPrecoverPass(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = confirmOTPrecoverPassCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest confirmOTPrecoverPassCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            JSONObject object = new JSONObject(responseBody.getString("object"));
                            ApplicationService.OBJECTGUID = object.getString("otpGuid");
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.CONFIRM_RECOVER_PASSWORD_OTP_SUCCESS);
                        }
                    } else {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.CONFIRM_RECOVER_PASSWORD_OTP_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void recoverChangePass(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = recoverChangePassCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest recoverChangePassCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.RECOVER_PASSWORD_SUCCESS);
                        }
                    } else {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.RECOVER_PASSWORD_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getLastEvent(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getLastEventCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getLastEventCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_LAST_EVENT_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Log.e(TAG, "objGuidEvent: " +  responseBody.getString("object"));

                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray listImage = object.getJSONArray("listImage");
                        String imagePath = "";
                        if(listImage.length() > 0){
                            JSONObject imageObject = listImage.getJSONObject(0);
                            imagePath = imageObject.getString("filePath");
                        }
                        String startAt = object.getString("startedAt");
                        String content = object.getString("content");
                        String identity = object.getString("identity");
                        String objGuidEvent = "";
                        if (!object.isNull("objGuidEvent")){
                             objGuidEvent = object.getString("objGuidEvent");
                            Log.e(TAG, "objGuidEvent 2: " + objGuidEvent);
                        }
                        Message message = new Message();
//                        message.obj = detailEvent;
                        message.what = ApplicationService.GET_EVENT_STATISTIC_SUCESS;
                        switch (identity) {
                            case ApplicationService.customerrecognize:
                                setLastEvent(ApplicationService.fariWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                            case ApplicationService.accessdetect:
                                setLastEvent(ApplicationService.intrudWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                            case ApplicationService.firedetect:
                                setLastEvent(ApplicationService.fideWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                            case ApplicationService.persondetection:
                                setLastEvent(ApplicationService.pedeWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                            case ApplicationService.licenseplate:
                                setLastEvent(ApplicationService.licenWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                            default:
                                setLastEvent(ApplicationService.fideWidget, imagePath, startAt, content, objGuidEvent);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getAllRegions(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getAllRegionsCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getAllRegionsCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    ApplicationService.notifySettingItems.clear();
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_ALL_REGIONS_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONArray listRegions = responseBody.getJSONArray("object");
                        for (int index = 0; index < listRegions.length(); index++) {
                            JSONObject obj = listRegions.getJSONObject(index);
                            Region region = new Region(obj);
                            ApplicationService.regions.add(region);
                        }
                        Message message = new Message();
                        message.what = ApplicationService.GET_ALL_REGIONS_SUCCESS;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void configCamera(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = configCameraCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void addUpdateCustomerType(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = addUpdateCustomerTypeCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.put(url, payload.toString(), requestCallback);
    }

    public void deleteTypeCustomer(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = deleteCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.delete(url, "{}", requestCallback);
    }
    public void changePasswordUser(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = changePasswordUserCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public UrlRequestCallback.OnFinishRequest changePasswordUserCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_CHANGE_PASS_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_CHANGE_PASS_SUCCESS;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void updateUserProfile(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = updateUserProfileCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public UrlRequestCallback.OnFinishRequest updateUserProfileCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_UPDATE_USERPROFILE_FAIL;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_UPDATE_USERPROFILE_SUCCESS;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getListNotifySetting(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getListNotifySettingCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getListNotifySettingCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    ApplicationService.notifySettingItems.clear();
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_GET_LIST_NOTIFYSETTING_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        ApplicationService.isActiveIdentitySetting = object.getBoolean("isActive");
                        ApplicationService.identitySetting = object.getString("identity");
                        JSONArray listNotifySetting = object.getJSONArray("data");
                        for (int index = 0; index < listNotifySetting.length(); index++) {
                            JSONObject obj = listNotifySetting.getJSONObject(index);
                            NotifySettingItem notifySettingItemItem = new NotifySettingItem(obj);
                            ApplicationService.notifySettingItems.add(notifySettingItemItem);
                        }
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_GET_LIST_NOTIFYSETTING_SUCCESS;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getStatisticLicen(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getStatisticLicenCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getStatisticLicenCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_EVENT_STATISTIC_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray info = object.getJSONArray("data");
                        int count = info.length();
                        Message message = new Message();
                        setTotal(ApplicationService.licenWidget, count);
                        message.what = ApplicationService.GET_EVENT_STATISTIC_SUCESS;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getStatisticsPede(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getStatisticsPedeCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getStatisticsPedeCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    Log.e("getStatisticsPedeCallbackActions", responseBody.toString());
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_EVENT_STATISTIC_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray info = object.getJSONArray("data");
                        int count = info.length();
                        Message message = new Message();
                        setTotal(ApplicationService.pedeWidget, count);
                        message.what = ApplicationService.GET_EVENT_STATISTIC_SUCESS;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getStatisticsIntru(JSONObject payload, String url) {
        Log.e("getStatisticsIntruCallbackActions", payload.toString());
        UrlRequestCallback.OnFinishRequest callbackInfo = getStatisticsIntruCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void getListIntruIndayDays(JSONObject payload, String url) {
        Log.e("xxxxxx",  " " + payload.toString() );
        UrlRequestCallback.OnFinishRequest callbackInfo = getIntruInDaysCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public UrlRequestCallback.OnFinishRequest getIntruInDaysCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_EVENT_STATISTIC_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        Log.e("getStatisticsIntruCallbackActionsxxxxxx",  " " + object.toString() );
                        JSONArray listIntruction = object.getJSONArray("data");
                        for (int index = 0; index < listIntruction.length(); index++) {
                            JSONObject obj = listIntruction.getJSONObject(index);
                            AccessdetectItem accessdetectItem = new AccessdetectItem(obj);
                            EventAccessdetectIndays.accessdetectItems.add(accessdetectItem);
                        }

                            Message message = new Message();
                            message.what = ApplicationService.GET_LIST_ACCESS_DETECT_SUCESS;
                            ApplicationService.mainHandler.sendMessage(message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public UrlRequestCallback.OnFinishRequest getStatisticsIntruCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_EVENT_STATISTIC_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray info = object.getJSONArray("data");
                        Log.e("getStatisticsIntruCallbackActions", object.toString());
                        if (info.length() > 0){
                            int count = info.getJSONObject(0).getInt("totalDetect");
                            Message message = new Message();
                            setTotal(ApplicationService.intrudWidget, count);
                            message.what = ApplicationService.GET_EVENT_STATISTIC_SUCESS;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getStatisticsFide(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getStatisticsFireCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getStatisticsFireCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
//                    Log.e(TAG, responseBody.toString());
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_EVENT_STATISTIC_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray info = object.getJSONArray("data");
                        int count = info.length();
                        Message message = new Message();
                        setTotal(ApplicationService.fideWidget, count);
                        message.what = ApplicationService.GET_EVENT_STATISTIC_SUCESS;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getStatisticsCustomer(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getStatisticsCustomerCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getStatisticsCustomerCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_EVENT_STATISTIC_FAIL);
                    } else if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray info = object.getJSONArray("info");
                        List<String> listProfile = new ArrayList<>();
                        for(int i = 0; i < info.length(); i++){
                            JSONObject jsonObject = info.getJSONObject(i);
                            String listProfileStr = jsonObject.getString("listProfile");
                            String[] listProfileItem = listProfileStr.split(",");
                            listProfile.addAll(Arrays.asList(listProfileItem));
                        }
                        int customerCount = listProfile.size();
                        setTotal(ApplicationService.fariWidget, customerCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void updateNotifySetting(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = updateNotifySettingCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest updateNotifySettingCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_UPDATE_NOTIFYSETTING_FAIL;
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_UPDATE_NOTIFYSETTING_SUCCESS;
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void addUpdateCustomer(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = addUpdateCustomerCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.put(url, payload.toString(), requestCallback);
    }

    public void getListNotify(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getListNotifyCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void refreshNotify(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = refreshNotifyCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }


    public void getUserInfor(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getInforUserCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);
    }

    public void getListCustomer(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCustomerCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void getListTypeCustomer(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getListTypeCustomerCallback();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void getInforAIDetail(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getInfoAIDtailCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public void deleteAIConfig(JSONObject payload, String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = deleteCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.delete(url, payload.toString(), requestCallback);
    }

    public void getDetaiEvent(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getDetaiEventCallback();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);

    }

    public void getDetailCustomer(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getDetailCustomerCallback();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);

    }

    public UrlRequestCallback.OnFinishRequest getDetailCustomerCallback() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400 || statusCode == 403) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_GET_DETAI_CUSTOMER_SUCCESS;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getInfoCamLan(String url, JSONObject payload) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getInfoCamLanCallback();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.postCustom(url, payload.toString(), requestCallback);

    }

    public UrlRequestCallback.OnFinishRequest getInfoCamLanCallback() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("code", 400);
                    errorMessage.put("message", "Not Authorized");
                    Message message = new Message();
                    if(!responseBody.toString().equals(errorMessage.toString())) {
                        message.what = ApplicationService.GET_CAM_LAN_INFO_SUCCESS;
                        JSONObject envelope = responseBody.getJSONObject("Envelope");
                        JSONObject body = envelope.getJSONObject("Body");
                        message.obj = body.getJSONObject("GetDeviceInformationResponse");
                    } else {
                        message.what = ApplicationService.GET_CAM_LAN_INFO_FAIL;
                    }
                    ApplicationService.mainHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void configCamLan(String url, JSONObject payload) {
        UrlRequestCallback.OnFinishRequest callbackInfo = configCamLanCallback();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.postCustom(url, payload.toString(), requestCallback);

    }
    public UrlRequestCallback.OnFinishRequest configCamLanCallback() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    JSONObject successData = new JSONObject();
                    JSONObject body = new JSONObject();
                    JSONObject envelope = new JSONObject();
                    body.put("ContinuousMoveResponse", "");
                    envelope.put("Header","");
                    envelope.put("Body", body);
                    successData.put("Envelope", envelope);
                    Message message = new Message();
                    if(successData.toString().equals(responseBody.toString())) {
                        message.what = ApplicationService.CONFIG_CAMERA_SUCCESS;
                    } else {
                        message.what = ApplicationService.CONFIG_CAMERA_FAIL;
                    }
                    ApplicationService.mainHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getDetaiEventCallback() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_GET_DETAI_EVENT_SUCCESS;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getListTypeCustomerCallback() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {

                        JSONArray object = new JSONArray(responseBody.getString("object"));
                        ApplicationService.typeCustomerItems.clear();
                        for (int index = 0; index < object.length(); index++) {
                            JSONObject obj = object.getJSONObject(index);
                            TypeCustomerItem typeCustomerItem = new TypeCustomerItem(obj);
                            ApplicationService.typeCustomerItems.add(typeCustomerItem);
                        }
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_LIST_TYPE_CUSTOMER_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getListCustomerCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    JSONObject object = new JSONObject(responseBody.getString("object"));
                    if (statusCode == 400) {

                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONArray listcam = object.getJSONArray("listCustomer");
//                        if (listcam.length() >= 0) {
//                            ApplicationService.customerItems.clear();
//                        }
                        for (int index = 0; index < listcam.length(); index++) {
                            JSONObject obj = listcam.getJSONObject(index);
                            CustomerItem customerItem = new CustomerItem(obj);
                            ApplicationService.customerItems.add(customerItem);
                        }
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_LIST_CUSTOMER_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest deleteCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());

                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.DELETE_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getInfoAIDtailCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                    } else if (statusCode == 200) {
                        JSONObject responseObject = new JSONObject(responseBody.getString("object"));
                        Message message = new Message();
                        message.what = ApplicationService.INFOR_AI_CONFIG_DETAIL;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest getInforUserCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    JSONObject userJson = responseBody.getJSONObject("object").getJSONObject("userInfo");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
//                        Message message = new Message();
//                        message.what = ApplicationService.MESSAGE_ERROR;
//                        message.obj = responseBody.getString("object");
//                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        ApplicationService.user = new UserItem(userJson);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest configCameraCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.CONFIG_CAMERA_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest addUpdateCustomerTypeCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.ADD_UPDATE_TYPE_CUSTOMER_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest getListNotifyCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    JSONObject object = new JSONObject(responseBody.getString("object"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONArray listNotify = object.getJSONArray("data");
                        for (int index = 0; index < listNotify.length(); index++) {
                            JSONObject obj = listNotify.getJSONObject(index);
                            NotifyItem notifyItem = new NotifyItem(obj);
                            ApplicationService.notifyItems.add(notifyItem);
                        }
//                        Log.e(TAG, "getListNotifyCallbackActions " + ApplicationService.notifyItems.size() );
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.MESSAGE_GET_LIST_NOTIFY_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest refreshNotifyCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
//                    String headers = data.getString("headers");
                    JSONObject object = new JSONObject(responseBody.getString("object"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        JSONArray listNotify = object.getJSONArray("data");
                        ApplicationService.notifyItems.clear();
                        for (int index = 0; index < listNotify.length(); index++) {
                            JSONObject obj = listNotify.getJSONObject(index);
                            NotifyItem notifyItem = new NotifyItem(obj);
                            ApplicationService.notifyItems.add(notifyItem);
                        }
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.MESSAGE_GET_LIST_NOTIFY_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest addUpdateCustomerCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
//                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    Log.e(TAG, responseBody.toString());
                    if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_UPDATE_CUSTOMER_SUCCESS;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getListCameraConfig(String appIdentity) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("appIdentity", appIdentity);
//            Log.e("appIdentity", payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCameraConfigCallBack();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(ApplicationService.URL_GET_LIST_CAMERA_CONFIG, payload.toString(), requestCallback);
    }

    public void getListCameraUnConfig(String appIdentity) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("appIdentity", appIdentity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCameraUnConfigCallBack();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(ApplicationService.URL_GET_LIST_CAMERA_UN_CONFIG, payload.toString(), requestCallback);
    }

    public void getListCam(String url, Integer cameraStatus, List<Integer> listRegion, int pageIndex, int pageSize, String searchCam) {
        JSONObject payload = new JSONObject();
        try {

            payload.put("cameraStatus", null);
            payload.put("listRegion","");
            payload.put("pageIndex", pageIndex);
            payload.put("pageSize", pageSize);
            payload.put("searchText", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCamCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString().replace("\"[", "[").replace("]\"","]"), requestCallback);
    }

    public void getListDevice(String url, Integer cameraStatus, List<Integer> listRegion, int pageIndex, int pageSize, String searchCam) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("cameraStatus", null);
            payload.put("listRegion", listRegion != null ? listRegion : null);
            payload.put("pageIndex", pageIndex);
            payload.put("pageSize", pageSize);
            payload.put("searchText", searchCam !=null ? searchCam : null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = getListDeviceCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString().replace("\"[", "[").replace("]\"","]"), requestCallback);
    }

    public void getListCamLoadmore(String url, Integer cameraStatus, List<Integer> listRegion, int pageIndex, int pageSize) {

        JSONObject payload = new JSONObject();
        try {
            payload.put("cameraStatus", null);
            payload.put("listRegion", null);
            payload.put("pageIndex", pageIndex);
            payload.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCamLoadmoreCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }

    public UrlRequestCallback.OnFinishRequest getListCameraConfigCallBack() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    Log.e("camera config ", responseBody.toString());
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {
                        JSONArray listObject = responseBody.getJSONArray("object");
                        if (listObject.length() >= 0) {
                            ApplicationService.cameraConfigs.clear();
                        }
                        for (int index = 0; index < listObject.length(); index++) {
                            JSONObject obj = listObject.getJSONObject(index);
                            AIObject aiObject = new AIObject(obj);
                            ApplicationService.cameraConfigs.add(aiObject);
                        }
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_LIST_CAMERA_CONFIG_SUCCESS);

                    } else if (statusCode == 400) {
                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest getListCameraUnConfigCallBack() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");

                    if (statusCode == 200) {

                        JSONArray listObject = responseBody.getJSONArray("object");
                        if (listObject.length() >= 0) {
                            ApplicationService.cameraUnConfigItems.clear();
                        }
                        for (int index = 0; index < listObject.length(); index++) {
                            JSONObject obj = listObject.getJSONObject(index);
                            CameraUnConfigItem cameraUnConfigItem = new CameraUnConfigItem(obj);
                            ApplicationService.cameraUnConfigItems.add(cameraUnConfigItem);
                        }
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_LIST_CAMERA_UN_CONFIG);
//                        Log.e("size object ", ApplicationService.cameraUnConfigItems.size() + "");

                    } else if (statusCode == 400) {
//                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.LOGIN_FAIL);
                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest loginCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    Log.e("responseBody login", responseBody.toString());
                    if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONObject userToken = new JSONObject(object.getString("userToken"));
                        JSONObject user = new JSONObject(object.getString("user"));
                        ApplicationService.TOKEN = userToken.getString("token");
                        ApplicationService.PARENT_ID = userToken.getInt("parentId");

                        ApplicationService.USERNAME = user.getString("userName");
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.LOGIN_SUCCESS);
                        }
                        getListCam(ApplicationService.URL_GET_LIST_CAM, null, null, 1, 10, null);
                        getListDevice(ApplicationService.URL_GET_LIST_DEVICE, null, null, 1, 10, null);
                        getUserInfor(ApplicationService.URL_GET_USER_INFOR);
                        ApplicationService.statisticThread = new StatisticThread();
                        ApplicationService.statisticThread.start();
                    } else {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.LOGIN_FAIL);
                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public UrlRequestCallback.OnFinishRequest getListDeviceCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
                        JSONArray listcam = object.getJSONArray("listCamera");
                        if (listcam.length() >= 0) {
                            ApplicationService.deviceItems.clear();
                        }
                        for (int index = 0; index < listcam.length(); index++) {
                            JSONObject obj = listcam.getJSONObject(index);
                            DeviceItem deviceItem = new DeviceItem(obj);
//                            if(!cameraItem.isAiBox()){
                                ApplicationService.deviceItems.add(deviceItem);
//                            }
                        }
//                        Log.e("device size", ApplicationService.deviceItems.size() + "");
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.UPDATE_LIST_DEVICE);
                        }

                    } else if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("er", e.getMessage());
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getListCamCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 200) {
                        JSONObject object = new JSONObject(responseBody.getString("object"));
//                        Log.e("camerazzzz ", object+ "");
                        JSONArray listcam = object.getJSONArray("listCamera");
                        if (listcam.length() >= 0) {
                            ApplicationService.cameraitems.clear();
                        }
                        for (int index = 0; index < listcam.length(); index++) {
                            JSONObject obj = listcam.getJSONObject(index);
                            CameraItem cameraItem = new CameraItem(obj);
                            if(!cameraItem.isAiBox()){
                                ApplicationService.cameraitems.add(cameraItem);
                            }

                        }
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.UPDATE_LIST_CAMERA);
                        }

                    } else if (statusCode == 400) {
                        Message message = new Message();
                        message.what = ApplicationService.MESSAGE_ERROR;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("er", e.getMessage());
                }
            }
        };
    }


    public UrlRequestCallback.OnFinishRequest getListCamLoadmoreCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
//                    String headers = data.getString("headers");
                    int statusCode = responseBody.getInt("statusCode");
                    JSONObject object = new JSONObject(String.valueOf(responseBody.getString("object")));
                    if (statusCode == 200) {
                        JSONArray listcam = object.getJSONArray("listCamera");

                        for (int index = 0; index < listcam.length(); index++) {
                            JSONObject obj = listcam.getJSONObject(index);
                            CameraItem cameraItem = new CameraItem(obj);
                            if(!cameraItem.isAiBox()  && !checkObjectInList(cameraItem, ApplicationService.cameraitems)){
                                ApplicationService.cameraitems.add(cameraItem);
                            }
                        }
//                        Log.e("CameraFragment", "down" +   ApplicationService.cameraitems.size());
                        if (ApplicationService.mainHandler != null) {
                            ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.UPDATE_LIST_CAMERA);
                        }

                    } else if (statusCode == 400) {

                    }
                    if (statusCode < 399) {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("er", e.getMessage());
                }
            }
        };
    }

    public void sendFileUserProfile(File avatar) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        MultipartBody.Builder buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), avatar))
                .addFormDataPart("thumbnail", avatar.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), avatar));
        RequestBody requestBody = buildernew.build();



        Request request = new Request.Builder()
                .url(ApplicationService.URL_UPLOAD_IMAGE_USERPROFILE)
                .method("POST", requestBody)
                .addHeader("Authorization", "Bearer " + ApplicationService.TOKEN)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responString = response.body().string();
            try {
                JSONObject data = new JSONObject(responString);
                int code = data.getInt("statusCode");
                if (code == 200) {
                    ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.MESSAGE_UPLOAD_PHOTO_PROFILE_SUCESS);
                } else {
                    Message message = new Message();
                    message.what = ApplicationService.MESSAGE_UPLOAD_FAILE;
                    message.obj = data.getString("object");
                    ApplicationService.mainHandler.sendMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendFile(ArrayList<File> images, File avatar, String objectGuid, int profileId) {


        Log.e("file", "null " + avatar.exists());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        MultipartBody.Builder buildernew = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("objectGuid", objectGuid)
                .addFormDataPart("profileId", String.valueOf(profileId));

        if (avatar != null) {
            buildernew.addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), avatar));
            buildernew.addFormDataPart("thumbnail", avatar.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), avatar));
        }
        for (File file : images) {
            buildernew.addFormDataPart("images", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }


        RequestBody requestBody = buildernew.build();

        Request request = new Request.Builder()
                .url(ApplicationService.URL_UPLOAD_IMAGE)
                .method("POST", requestBody)
                .addHeader("Authorization", "Bearer " + ApplicationService.TOKEN)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responString = response.body().string();
            Log.e("image ressponse", responString);
            try {
                JSONObject data = new JSONObject(responString);
                int code = data.getInt("statusCode");
                if (code == 200) {
                    ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.MESSAGE_UPLOAD_PHOTO_SUCESS);
                } else {
                    Message message = new Message();
                    message.what = ApplicationService.MESSAGE_UPLOAD_FAILE;
                    message.obj = data.getString("object");
                    ApplicationService.mainHandler.sendMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCameraDetail(String url) {
        UrlRequestCallback.OnFinishRequest callbackInfo = getCameraDetailCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.get(url, requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getCameraDetailCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    int statusCode = responseBody.getInt("statusCode");
                    if (statusCode == 400) {
                        ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_CAM_DETAIL_FAIL);
                    } else if (statusCode == 200) {
                        Message message = new Message();
                        message.what = ApplicationService.GET_CAM_DETAIL_SUCCESS;
                        message.obj = new JSONObject(responseBody.getString("object"));
                        ApplicationService.mainHandler.sendMessage(message);
//                        Log.e(TAG, "onFinishRequest: " + "Đã gửi" );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public void getListCustomerEvent(JSONObject payload, String url) {
        Log.e(TAG, "GET_CUSTOMER_EVENTS_SUCCESS: " + payload.toString());
        UrlRequestCallback.OnFinishRequest callbackInfo = getListCustomerEventCallbackActions();
        UrlRequestCallback requestCallback = new UrlRequestCallback(callbackInfo);
        NetworkRequest.post(url, payload.toString(), requestCallback);
    }
    public UrlRequestCallback.OnFinishRequest getListCustomerEventCallbackActions() {
        return new UrlRequestCallback.OnFinishRequest() {
            @Override
            public void onFinishRequest(Object result) {
                try {
                    JSONObject data = new JSONObject(result.toString());
                    JSONObject responseBody = new JSONObject(data.getString("body"));
                    JSONObject object = new JSONObject(responseBody.getString("object"));
                    int statusCode = responseBody.getInt("statusCode");


                    Message message = new Message();
                    if (statusCode == 400) {
                        message.what = ApplicationService.GET_CUSTOMER_EVENTS_FAIL;
                        message.obj = responseBody.getString("object");
                        ApplicationService.mainHandler.sendMessage(message);
                    } else if (statusCode == 200) {
                        ApplicationService.customerEvents.clear();
                        message.what = ApplicationService.GET_CUSTOMER_EVENTS_SUCCESS;
                        message.obj = object.getJSONArray("listDateDetect");

                        JSONArray customerEvents = object.getJSONArray("data");
                        for (int index = 0; index < customerEvents.length(); index++) {
                            JSONObject obj = customerEvents.getJSONObject(index);
                            CustomerEvent customerEvent = new CustomerEvent(obj);
                            ApplicationService.customerEvents.add(customerEvent);
                        }
                        Log.e(TAG, "GET_CUSTOMER_EVENTS_SUCCESSx: " + ApplicationService.customerEvents.size());
                    }
                    ApplicationService.mainHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void getLastEvent() {
        @SuppressLint("DefaultLocale") String urlCustomer = String.format(ApplicationService.URL_GET_LAST_EVENT, ApplicationService.customerrecognize);
        @SuppressLint("DefaultLocale") String urlPerson = String.format(ApplicationService.URL_GET_LAST_EVENT, ApplicationService.persondetection);
        @SuppressLint("DefaultLocale") String urlAcess = String.format(ApplicationService.URL_GET_LAST_EVENT, ApplicationService.accessdetect);
        @SuppressLint("DefaultLocale") String urlFire = String.format(ApplicationService.URL_GET_LAST_EVENT, ApplicationService.firedetect);
        @SuppressLint("DefaultLocale") String urlLicen = String.format(ApplicationService.URL_GET_LAST_EVENT, ApplicationService.licenseplate);
        getLastEvent(urlCustomer);
        getLastEvent(urlPerson);
        getLastEvent(urlAcess);
        getLastEvent(urlFire);
        getLastEvent(urlLicen);
    }

    private String getTimeEnd() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime()) + " 23:59";
        return date;
    }

    private String getTimeStart() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = dateFormat.format(Calendar.getInstance().getTime()) + " 00:00";
        return result;
    }
    public void getStatisticFR(){
        try{
            JSONObject payload = new JSONObject();
            payload.put("appIdentity", ApplicationService.customerrecognize);
            payload.put("pageSize", 10000);
            payload.put("pageIndex", 1);
            payload.put("fromDate", getTimeStart() );
            payload.put("toDate", getTimeEnd());
            payload.put("showType", "day");
            payload.put("isActive", 1);
            payload.put("optionTime", 4);
            payload.put("IsUnknow", 0);
            getStatisticsCustomer(payload, ApplicationService.URL_GET_LIST_STATISTIC_CUSTOMER);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void setLastEvent(WidgetItem widgetItem, String imagePath, String startAt, String content, String objectGuid){
        widgetItem.setContent(content);
        widgetItem.setStartAt(startAt);
        widgetItem.setImagePath(imagePath);
        for(WidgetItem widget : ApplicationService.listWidgetView){
            if(widget.getIndetify().equals(widgetItem.getIndetify())){
                widget.setContent(content);
                widget.setStartAt(startAt);
                widget.setImagePath(imagePath);
                widget.setObjectGuide(objectGuid);
            }
        }
        for(WidgetItem widget : ApplicationService.listWidgetType){
            if(widget.getIndetify().equals(widgetItem.getIndetify())){
                widget.setContent(content);
                widget.setStartAt(startAt);
                widget.setImagePath(imagePath);
                widget.setObjectGuide(objectGuid);
            }
        }
    }
    private void setTotal(WidgetItem widgetItem, int count){
        widgetItem.setTotalEvent(count);
        for(WidgetItem widget : ApplicationService.listWidgetView){
            if(widget.getIndetify().equals(widgetItem.getIndetify())){
                widget.setTotalEvent(count);
            }
        }
        for(WidgetItem widget : ApplicationService.listWidgetType){
            if(widget.getIndetify().equals(widgetItem.getIndetify())){
                widget.setTotalEvent(count);
            }
        }
    }
    private boolean checkObjectInList(CameraItem cameraItem, List<CameraItem> cameraItemList){
        for(CameraItem cameraItem1 : cameraItemList){
            if(cameraItem.getCameraName().equals(cameraItem1.getCameraName())){
                return true;
            }
        }
        return false;
    }

    private void processMessage(JSONObject jsonObject, int tag, Handler handler) {
        switch (tag){
            case Constant.GET_EVENTS_OF_DAY:

        }
    }


}
