package com.bkav.aiotcloud.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bkav.aiotcloud.application.ApplicationService;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectRequest {
    private void getJson(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.ipify.org/?format=json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "JsonObjectRequest onResponse: " + response.getString("ip").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "JsonObjectRequest onErrorResponse: " + error.getMessage());
            }
        });


    }
}
