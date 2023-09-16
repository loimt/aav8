package com.bkav.aiotcloud.network;

import android.util.Log;

import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;

import org.chromium.net.CronetEngine;
import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataProviders;
import org.chromium.net.UrlRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NetworkRequest {
    public static CronetEngine cronetEngine = new CronetEngine.Builder(ApplicationService.getAppContext()).build();
    public static Executor executor = Executors.newSingleThreadExecutor();
    private static String TAG = "NetworkRequest";
    public static UrlRequest.Builder makeRequestBuilderCustom(String url, String httpMethod, UrlRequest.Callback callback) {
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(url, callback, executor);
        requestBuilder.setHttpMethod(httpMethod);
        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("Lang", LanguageManager.getInstance().getValue("header_lang"));
        return requestBuilder;
    }
    public static UrlRequest.Builder makeRequestBuilder(String url, String httpMethod, UrlRequest.Callback callback) {

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(url, callback, executor);
        requestBuilder.setHttpMethod(httpMethod);
//        SharedPreferences authPref = ApplicationService.getAppContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
//        String accessToken = authPref.getString("accessToken", "null");
//        String client = authPref.getString("client", "null");
//        String expiry = Long.toString(authPref.getLong("expiry", 0));
//        String uid = authPref.getString("uid", "null");


        requestBuilder.addHeader("X-TimeZone-Offset", "-420");
        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("Accept", "application/json");
        requestBuilder.addHeader("Lang", LanguageManager.getInstance().getValue("header_lang"));

        if (ApplicationService.TOKEN != null){
            requestBuilder.addHeader("Authorization", "Bearer " + ApplicationService.TOKEN);
            Log.e("TOKEN" ,  ApplicationService.TOKEN);
        }
        return requestBuilder;
    }

    public static UrlRequest.Builder makeRequestFile(String url, String httpMethod, UrlRequest.Callback callback) {

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(url, callback, executor);

        requestBuilder.setHttpMethod(httpMethod);

//        SharedPreferences authPref = ApplicationService.getAppContext().getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE);
//        String accessToken = authPref.getString("accessToken", "null");
//        String client = authPref.getString("client", "null");
//        String expiry = Long.toString(authPref.getLong("expiry", 0));
//        String uid = authPref.getString("uid", "null");

        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("Accept", "application/json");
        if (ApplicationService.TOKEN != null){
            requestBuilder.addHeader("Authorization", "Bearer " + ApplicationService.TOKEN);
        }
        return requestBuilder;
    }




    public static UploadDataProvider generateUploadDataProvider(String payload) {
        byte[] bytes = convertStringToBytes(payload);
        UploadDataProvider uploadDataProvider = UploadDataProviders.create(bytes);

        return uploadDataProvider;
    }

    public static byte[] convertStringToBytes(String payload) {
        byte[] bytes;
        ByteBuffer byteBuffer = ByteBuffer.wrap(payload.getBytes());
        if (byteBuffer.hasArray()) {
            bytes = byteBuffer.array();
        } else {
            bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
        }

        return bytes;
    }



    public static void get(String url, UrlRequest.Callback callback) {
        //You should define your callback from where you called this method from
        UrlRequest.Builder requestBuilder = makeRequestBuilder(url,"GET", callback);
        UrlRequest request = requestBuilder.build();
        request.start();
    }

    public static void put(String url, String payload, UrlRequest.Callback callback) {
        //You should define your callback from where you called this method from
        UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "PUT", callback);

        requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

        UrlRequest request = requestBuilder.build();

        request.start();
    }
    public static void postCustom(String url, String payload, UrlRequest.Callback callback) {
        //You should define your callback from where you called this method from
        UrlRequest.Builder requestBuilder = makeRequestBuilderCustom(url, "POST", callback);

        requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

        UrlRequest request = requestBuilder.build();

        request.start();
    }

    public static void post(String url, String payload, UrlRequest.Callback callback) {
        //You should define your callback from where you called this method from
        UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "POST", callback);

        requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

        UrlRequest request = requestBuilder.build();

        request.start();
    }



//
//    public static void senOfferCustom(JSONObject payload){
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
////        RequestBody body = RequestBody.create(mediaType, "{\n   \"context\": \"signal.webrtc\",\n   \"type\": \"offer.no_trickle\",\n   \"data\": {\n       \"peer_id\": \"62885d749a545d39cb1ad41a\",\n       \"stream_mode\": \"LIVE\",\n       \"stream_src\": \"MAIN\",\n       \"data\": \"eyJ0eXBlIjoib2ZmZXIiLCJzZHAiOiJ2PTBcclxubz0tIDQ0NTUzODY0MzM0OTk5MTc2NjggMiBJTiBJUDQgMTI3LjAuMC4xXHJcbnM9LVxyXG50PTAgMFxyXG5hPWdyb3VwOkJVTkRMRSAwIDFcclxuYT1leHRtYXAtYWxsb3ctbWl4ZWRcclxuYT1tc2lkLXNlbWFudGljOiBXTVNcclxubT12aWRlbyA0NTUzNSBVRFAvVExTL1JUUC9TQVZQRiA5NiA5NyA5OCA5OSAxMDAgMTAxIDEwMiAxMjIgMTI3IDEyMSAxMjUgMTA3IDEwOCAxMDkgMTI0IDEyMCAxMjMgMTE5IDM1IDM2IDM3IDM4IDM5IDQwIDQxIDQyIDExNCAxMTUgMTE2IDExNyAxMTggNDNcclxuYz1JTiBJUDQgMjAzLjE3MS4yMS4xNDVcclxuYT1ydGNwOjkgSU4gSVA0IDAuMC4wLjBcclxuYT1jYW5kaWRhdGU6MzY2NTY2NTczMSAxIHVkcCAyMTEzOTM3MTUxIDA4YmMxMzQ4LTM1YmUtNDdkNC04YjIyLTJlYzAwZGExNjlkMS5sb2NhbCA1NDEzNSB0eXAgaG9zdCBnZW5lcmF0aW9uIDAgbmV0d29yay1jb3N0IDk5OVxyXG5hPWNhbmRpZGF0ZTo4NDIxNjMwNDkgMSB1ZHAgMTY3NzcyOTUzNSAyMjIuMjU0LjM0LjEwMSA1NDEzNSB0eXAgc3JmbHggcmFkZHIgMC4wLjAuMCBycG9ydCAwIGdlbmVyYXRpb24gMCBuZXR3b3JrLWNvc3QgOTk5XHJcbmE9Y2FuZGlkYXRlOjIyMDE3MzMzNzQgMSB1ZHAgMzM1NjIzNjcgMjAzLjE3MS4yMS4xNDUgNDU1MzUgdHlwIHJlbGF5IHJhZGRyIDIyMi4yNTQuMzQuMTAxIHJwb3J0IDU0MTM1IGdlbmVyYXRpb24gMCBuZXR3b3JrLWNvc3QgOTk5XHJcbmE9aWNlLXVmcmFnOkJySkxcclxuYT1pY2UtcHdkOmRCT2FGL1k2MWM1T1RLb3lwRmF3REZKWFxyXG5hPWljZS1vcHRpb25zOnRyaWNrbGVcclxuYT1maW5nZXJwcmludDpzaGEtMjU2IDIzOjdBOjEzOkEyOkMyOkFCOkMwOjhCOkUwOkY1OkU2OjM1OjY3OjQ2OjM2OjQ3OkJGOjRFOjNEOjdGOjZFOjk0OjAzOkE5OjE1OkMyOkZCOkFBOkVFOjdBOkI5OkZCXHJcbmE9c2V0dXA6YWN0cGFzc1xyXG5hPW1pZDowXHJcbmE9ZXh0bWFwOjEgdXJuOmlldGY6cGFyYW1zOnJ0cC1oZHJleHQ6dG9mZnNldFxyXG5hPWV4dG1hcDoyIGh0dHA6Ly93d3cud2VicnRjLm9yZy9leHBlcmltZW50cy9ydHAtaGRyZXh0L2Ficy1zZW5kLXRpbWVcclxuYT1leHRtYXA6MyB1cm46M2dwcDp2aWRlby1vcmllbnRhdGlvblxyXG5hPWV4dG1hcDo0IGh0dHA6Ly93d3cuaWV0Zi5vcmcvaWQvZHJhZnQtaG9sbWVyLXJtY2F0LXRyYW5zcG9ydC13aWRlLWNjLWV4dGVuc2lvbnMtMDFcclxuYT1leHRtYXA6NSBodHRwOi8vd3d3LndlYnJ0Yy5vcmcvZXhwZXJpbWVudHMvcnRwLWhkcmV4dC9wbGF5b3V0LWRlbGF5XHJcbmE9ZXh0bWFwOjYgaHR0cDovL3d3dy53ZWJydGMub3JnL2V4cGVyaW1lbnRzL3J0cC1oZHJleHQvdmlkZW8tY29udGVudC10eXBlXHJcbmE9ZXh0bWFwOjcgaHR0cDovL3d3dy53ZWJydGMub3JnL2V4cGVyaW1lbnRzL3J0cC1oZHJleHQvdmlkZW8tdGltaW5nXHJcbmE9ZXh0bWFwOjggaHR0cDovL3d3dy53ZWJydGMub3JnL2V4cGVyaW1lbnRzL3J0cC1oZHJleHQvY29sb3Itc3BhY2VcclxuYT1leHRtYXA6OSB1cm46aWV0ZjpwYXJhbXM6cnRwLWhkcmV4dDpzZGVzOm1pZFxyXG5hPWV4dG1hcDoxMCB1cm46aWV0ZjpwYXJhbXM6cnRwLWhkcmV4dDpzZGVzOnJ0cC1zdHJlYW0taWRcclxuYT1leHRtYXA6MTEgdXJuOmlldGY6cGFyYW1zOnJ0cC1oZHJleHQ6c2RlczpyZXBhaXJlZC1ydHAtc3RyZWFtLWlkXHJcbmE9cmVjdm9ubHlcclxuYT1ydGNwLW11eFxyXG5hPXJ0Y3AtcnNpemVcclxuYT1ydHBtYXA6OTYgVlA4LzkwMDAwXHJcbmE9cnRjcC1mYjo5NiBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjk2IHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6OTYgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6OTYgbmFja1xyXG5hPXJ0Y3AtZmI6OTYgbmFjayBwbGlcclxuYT1ydHBtYXA6OTcgcnR4LzkwMDAwXHJcbmE9Zm10cDo5NyBhcHQ9OTZcclxuYT1ydHBtYXA6OTggVlA5LzkwMDAwXHJcbmE9cnRjcC1mYjo5OCBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjk4IHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6OTggY2NtIGZpclxyXG5hPXJ0Y3AtZmI6OTggbmFja1xyXG5hPXJ0Y3AtZmI6OTggbmFjayBwbGlcclxuYT1mbXRwOjk4IHByb2ZpbGUtaWQ9MFxyXG5hPXJ0cG1hcDo5OSBydHgvOTAwMDBcclxuYT1mbXRwOjk5IGFwdD05OFxyXG5hPXJ0cG1hcDoxMDAgVlA5LzkwMDAwXHJcbmE9cnRjcC1mYjoxMDAgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjoxMDAgdHJhbnNwb3J0LWNjXHJcbmE9cnRjcC1mYjoxMDAgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6MTAwIG5hY2tcclxuYT1ydGNwLWZiOjEwMCBuYWNrIHBsaVxyXG5hPWZtdHA6MTAwIHByb2ZpbGUtaWQ9MlxyXG5hPXJ0cG1hcDoxMDEgcnR4LzkwMDAwXHJcbmE9Zm10cDoxMDEgYXB0PTEwMFxyXG5hPXJ0cG1hcDoxMDIgVlA5LzkwMDAwXHJcbmE9cnRjcC1mYjoxMDIgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjoxMDIgdHJhbnNwb3J0LWNjXHJcbmE9cnRjcC1mYjoxMDIgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6MTAyIG5hY2tcclxuYT1ydGNwLWZiOjEwMiBuYWNrIHBsaVxyXG5hPWZtdHA6MTAyIHByb2ZpbGUtaWQ9MVxyXG5hPXJ0cG1hcDoxMjIgcnR4LzkwMDAwXHJcbmE9Zm10cDoxMjIgYXB0PTEwMlxyXG5hPXJ0cG1hcDoxMjcgSDI2NC85MDAwMFxyXG5hPXJ0Y3AtZmI6MTI3IGdvb2ctcmVtYlxyXG5hPXJ0Y3AtZmI6MTI3IHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6MTI3IGNjbSBmaXJcclxuYT1ydGNwLWZiOjEyNyBuYWNrXHJcbmE9cnRjcC1mYjoxMjcgbmFjayBwbGlcclxuYT1mbXRwOjEyNyBsZXZlbC1hc3ltbWV0cnktYWxsb3dlZD0xO3BhY2tldGl6YXRpb24tbW9kZT0xO3Byb2ZpbGUtbGV2ZWwtaWQ9NDIwMDFmXHJcbmE9cnRwbWFwOjEyMSBydHgvOTAwMDBcclxuYT1mbXRwOjEyMSBhcHQ9MTI3XHJcbmE9cnRwbWFwOjEyNSBIMjY0LzkwMDAwXHJcbmE9cnRjcC1mYjoxMjUgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjoxMjUgdHJhbnNwb3J0LWNjXHJcbmE9cnRjcC1mYjoxMjUgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6MTI1IG5hY2tcclxuYT1ydGNwLWZiOjEyNSBuYWNrIHBsaVxyXG5hPWZtdHA6MTI1IGxldmVsLWFzeW1tZXRyeS1hbGxvd2VkPTE7cGFja2V0aXphdGlvbi1tb2RlPTA7cHJvZmlsZS1sZXZlbC1pZD00MjAwMWZcclxuYT1ydHBtYXA6MTA3IHJ0eC85MDAwMFxyXG5hPWZtdHA6MTA3IGFwdD0xMjVcclxuYT1ydHBtYXA6MTA4IEgyNjQvOTAwMDBcclxuYT1ydGNwLWZiOjEwOCBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjEwOCB0cmFuc3BvcnQtY2NcclxuYT1ydGNwLWZiOjEwOCBjY20gZmlyXHJcbmE9cnRjcC1mYjoxMDggbmFja1xyXG5hPXJ0Y3AtZmI6MTA4IG5hY2sgcGxpXHJcbmE9Zm10cDoxMDggbGV2ZWwtYXN5bW1ldHJ5LWFsbG93ZWQ9MTtwYWNrZXRpemF0aW9uLW1vZGU9MTtwcm9maWxlLWxldmVsLWlkPTQyZTAxZlxyXG5hPXJ0cG1hcDoxMDkgcnR4LzkwMDAwXHJcbmE9Zm10cDoxMDkgYXB0PTEwOFxyXG5hPXJ0cG1hcDoxMjQgSDI2NC85MDAwMFxyXG5hPXJ0Y3AtZmI6MTI0IGdvb2ctcmVtYlxyXG5hPXJ0Y3AtZmI6MTI0IHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6MTI0IGNjbSBmaXJcclxuYT1ydGNwLWZiOjEyNCBuYWNrXHJcbmE9cnRjcC1mYjoxMjQgbmFjayBwbGlcclxuYT1mbXRwOjEyNCBsZXZlbC1hc3ltbWV0cnktYWxsb3dlZD0xO3BhY2tldGl6YXRpb24tbW9kZT0wO3Byb2ZpbGUtbGV2ZWwtaWQ9NDJlMDFmXHJcbmE9cnRwbWFwOjEyMCBydHgvOTAwMDBcclxuYT1mbXRwOjEyMCBhcHQ9MTI0XHJcbmE9cnRwbWFwOjEyMyBIMjY0LzkwMDAwXHJcbmE9cnRjcC1mYjoxMjMgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjoxMjMgdHJhbnNwb3J0LWNjXHJcbmE9cnRjcC1mYjoxMjMgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6MTIzIG5hY2tcclxuYT1ydGNwLWZiOjEyMyBuYWNrIHBsaVxyXG5hPWZtdHA6MTIzIGxldmVsLWFzeW1tZXRyeS1hbGxvd2VkPTE7cGFja2V0aXphdGlvbi1tb2RlPTE7cHJvZmlsZS1sZXZlbC1pZD00ZDAwMWZcclxuYT1ydHBtYXA6MTE5IHJ0eC85MDAwMFxyXG5hPWZtdHA6MTE5IGFwdD0xMjNcclxuYT1ydHBtYXA6MzUgSDI2NC85MDAwMFxyXG5hPXJ0Y3AtZmI6MzUgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjozNSB0cmFuc3BvcnQtY2NcclxuYT1ydGNwLWZiOjM1IGNjbSBmaXJcclxuYT1ydGNwLWZiOjM1IG5hY2tcclxuYT1ydGNwLWZiOjM1IG5hY2sgcGxpXHJcbmE9Zm10cDozNSBsZXZlbC1hc3ltbWV0cnktYWxsb3dlZD0xO3BhY2tldGl6YXRpb24tbW9kZT0wO3Byb2ZpbGUtbGV2ZWwtaWQ9NGQwMDFmXHJcbmE9cnRwbWFwOjM2IHJ0eC85MDAwMFxyXG5hPWZtdHA6MzYgYXB0PTM1XHJcbmE9cnRwbWFwOjM3IEgyNjQvOTAwMDBcclxuYT1ydGNwLWZiOjM3IGdvb2ctcmVtYlxyXG5hPXJ0Y3AtZmI6MzcgdHJhbnNwb3J0LWNjXHJcbmE9cnRjcC1mYjozNyBjY20gZmlyXHJcbmE9cnRjcC1mYjozNyBuYWNrXHJcbmE9cnRjcC1mYjozNyBuYWNrIHBsaVxyXG5hPWZtdHA6MzcgbGV2ZWwtYXN5bW1ldHJ5LWFsbG93ZWQ9MTtwYWNrZXRpemF0aW9uLW1vZGU9MTtwcm9maWxlLWxldmVsLWlkPWY0MDAxZlxyXG5hPXJ0cG1hcDozOCBydHgvOTAwMDBcclxuYT1mbXRwOjM4IGFwdD0zN1xyXG5hPXJ0cG1hcDozOSBIMjY0LzkwMDAwXHJcbmE9cnRjcC1mYjozOSBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjM5IHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6MzkgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6MzkgbmFja1xyXG5hPXJ0Y3AtZmI6MzkgbmFjayBwbGlcclxuYT1mbXRwOjM5IGxldmVsLWFzeW1tZXRyeS1hbGxvd2VkPTE7cGFja2V0aXphdGlvbi1tb2RlPTA7cHJvZmlsZS1sZXZlbC1pZD1mNDAwMWZcclxuYT1ydHBtYXA6NDAgcnR4LzkwMDAwXHJcbmE9Zm10cDo0MCBhcHQ9MzlcclxuYT1ydHBtYXA6NDEgQVYxLzkwMDAwXHJcbmE9cnRjcC1mYjo0MSBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjQxIHRyYW5zcG9ydC1jY1xyXG5hPXJ0Y3AtZmI6NDEgY2NtIGZpclxyXG5hPXJ0Y3AtZmI6NDEgbmFja1xyXG5hPXJ0Y3AtZmI6NDEgbmFjayBwbGlcclxuYT1ydHBtYXA6NDIgcnR4LzkwMDAwXHJcbmE9Zm10cDo0MiBhcHQ9NDFcclxuYT1ydHBtYXA6MTE0IEgyNjQvOTAwMDBcclxuYT1ydGNwLWZiOjExNCBnb29nLXJlbWJcclxuYT1ydGNwLWZiOjExNCB0cmFuc3BvcnQtY2NcclxuYT1ydGNwLWZiOjExNCBjY20gZmlyXHJcbmE9cnRjcC1mYjoxMTQgbmFja1xyXG5hPXJ0Y3AtZmI6MTE0IG5hY2sgcGxpXHJcbmE9Zm10cDoxMTQgbGV2ZWwtYXN5bW1ldHJ5LWFsbG93ZWQ9MTtwYWNrZXRpemF0aW9uLW1vZGU9MTtwcm9maWxlLWxldmVsLWlkPTY0MDAxZlxyXG5hPXJ0cG1hcDoxMTUgcnR4LzkwMDAwXHJcbmE9Zm10cDoxMTUgYXB0PTExNFxyXG5hPXJ0cG1hcDoxMTYgcmVkLzkwMDAwXHJcbmE9cnRwbWFwOjExNyBydHgvOTAwMDBcclxuYT1mbXRwOjExNyBhcHQ9MTE2XHJcbmE9cnRwbWFwOjExOCB1bHBmZWMvOTAwMDBcclxuYT1ydHBtYXA6NDMgZmxleGZlYy0wMy85MDAwMFxyXG5hPXJ0Y3AtZmI6NDMgZ29vZy1yZW1iXHJcbmE9cnRjcC1mYjo0MyB0cmFuc3BvcnQtY2NcclxuYT1mbXRwOjQzIHJlcGFpci13aW5kb3c9MTAwMDAwMDBcclxubT1hcHBsaWNhdGlvbiA0ODE3OSBVRFAvRFRMUy9TQ1RQIHdlYnJ0Yy1kYXRhY2hhbm5lbFxyXG5jPUlOIElQNCAyMDMuMTcxLjIxLjE0NVxyXG5hPWNhbmRpZGF0ZTozNjY1NjY1NzMxIDEgdWRwIDIxMTM5MzcxNTEgMDhiYzEzNDgtMzViZS00N2Q0LThiMjItMmVjMDBkYTE2OWQxLmxvY2FsIDYyNDkxIHR5cCBob3N0IGdlbmVyYXRpb24gMCBuZXR3b3JrLWNvc3QgOTk5XHJcbmE9Y2FuZGlkYXRlOjg0MjE2MzA0OSAxIHVkcCAxNjc3NzI5NTM1IDIyMi4yNTQuMzQuMTAxIDYyNDkxIHR5cCBzcmZseCByYWRkciAwLjAuMC4wIHJwb3J0IDAgZ2VuZXJhdGlvbiAwIG5ldHdvcmstY29zdCA5OTlcclxuYT1jYW5kaWRhdGU6MjIwMTczMzM3NCAxIHVkcCAzMzU2MjM2NyAyMDMuMTcxLjIxLjE0NSA0ODE3OSB0eXAgcmVsYXkgcmFkZHIgMjIyLjI1NC4zNC4xMDEgcnBvcnQgNjI0OTEgZ2VuZXJhdGlvbiAwIG5ldHdvcmstY29zdCA5OTlcclxuYT1pY2UtdWZyYWc6QnJKTFxyXG5hPWljZS1wd2Q6ZEJPYUYvWTYxYzVPVEtveXBGYXdERkpYXHJcbmE9aWNlLW9wdGlvbnM6dHJpY2tsZVxyXG5hPWZpbmdlcnByaW50OnNoYS0yNTYgMjM6N0E6MTM6QTI6QzI6QUI6QzA6OEI6RTA6RjU6RTY6MzU6Njc6NDY6MzY6NDc6QkY6NEU6M0Q6N0Y6NkU6OTQ6MDM6QTk6MTU6QzI6RkI6QUE6RUU6N0E6Qjk6RkJcclxuYT1zZXR1cDphY3RwYXNzXHJcbmE9bWlkOjFcclxuYT1zY3RwLXBvcnQ6NTAwMFxyXG5hPW1heC1tZXNzYWdlLXNpemU6MjYyMTQ0XHJcbiJ9\"\n   }\n}\n");
//        RequestBody body = RequestBody.create(mediaType,payload.toString());
//        Log.e("payload", payload.toString());
//        Request request = new Request.Builder()
//                .url("https://ppbs.aiview.ai:30004/p2p/api/sample/client/send/offer")
//                .method("POST", body)
//                .addHeader("Authorization", "Basic NjFjZWNjZGVjN2Q4OTU5MThhMmY1MzBmOnlDSklEQWJBaU9TUVRaRFRRZlc4aWNQcDg2MGZ6ZQ==")
//                .addHeader("Content-Type", "application/json")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            Log.e("response ", response.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void delete(String url, String payload, UrlRequest.Callback callback) {
        //You should define your callback from where you called this method from
        UrlRequest.Builder requestBuilder = makeRequestBuilder(url, "DELETE", callback);
        requestBuilder.setUploadDataProvider(generateUploadDataProvider(payload), executor);

        UrlRequest request = requestBuilder.build();

        request.start();
    }

}

