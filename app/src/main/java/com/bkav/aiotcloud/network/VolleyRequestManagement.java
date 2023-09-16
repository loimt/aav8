package com.bkav.aiotcloud.network;

import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.application.Constant;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.widget.EventFaceInDays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolleyRequestManagement {
    public void login(String user, String pass, String firebaseID, String url) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", user);
            payload.put("password", pass);
            payload.put("os", 0);
            payload.put("firebaseID", firebaseID);
            payload.put("clientID", ApplicationService.CLIENT_ID);
            payload.put("deviceID", ApplicationService.DEVICE_ID);
            sendRequest(Request.Method.POST, url, payload, Constant.LOGIN, ApplicationService.mainHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getEventsOfDay(String user, String pass, String firebaseID, String url) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("username", user);
            payload.put("password", pass);
            payload.put("os", 0);
            payload.put("firebaseID", firebaseID);
            payload.put("clientID", ApplicationService.CLIENT_ID);
            payload.put("deviceID", ApplicationService.DEVICE_ID);
            sendRequest(Request.Method.GET, url, payload, Constant.LOGIN, ApplicationService.mainHandler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
       sendRequest(Request.Method.POST, url, payload, Constant.LOGIN, ApplicationService.mainHandler);
    }

    public void getEventsInDay(JSONObject payload, String url) {
        Log.e("TAG", "request: " + payload.toString());
        sendRequest(Request.Method.POST, url, payload, Constant.GET_EVENTS_OF_DAY, ApplicationService.mainHandler);
    }

    private void sendRequest(int method, String url, JSONObject param, int tag, android.os.Handler handlerDerect) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "JsonObjectRequest onResponse: " + response.toString());
                        processMessage(response, tag, handlerDerect);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "JsonObjectRequest onErrorResponse: " + error.getMessage());
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("token", ACCESS_TOKEN);

                params.put("X-TimeZone-Offset", "-420");
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Lang", LanguageManager.getInstance().getValue("header_lang"));

                if (ApplicationService.TOKEN != null){
                    params.put("Authorization", "Bearer " + ApplicationService.TOKEN);
                    Log.e("TOKEN" ,  ApplicationService.TOKEN);
                }
                return params;
            }
        };

        VolleySingleton.getInstance(ApplicationService.getAppContext()).getRequestQueue().add(jsonObjectRequest);
    }

    private void processMessage(JSONObject response, int tag, Handler handlerDerect) {
        switch (tag) {
            case Constant.GET_EVENTS_OF_DAY:

                try {
                    if (response.getInt("statusCode") == Constant.STATUS_OK){
                        EventFaceInDays.customerInDays.clear();
                        JSONArray listCustomer = response.getJSONObject("object").getJSONArray("data");
                        for (int index = 0; index < listCustomer.length(); index++) {
                            JSONObject obj = listCustomer.getJSONObject(index);
                            CustomerItem customerItem = new CustomerItem(obj);
                            Log.e("TAG", "JsonObjectRequest onResponse: " +customerItem.getFullName() );
                            EventFaceInDays.customerInDays.add(customerItem);
                        }
                    }

                    handlerDerect.sendEmptyMessage(ApplicationService.GET_LIST_CUSTOMER_SUCCESS);
                    break;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}
