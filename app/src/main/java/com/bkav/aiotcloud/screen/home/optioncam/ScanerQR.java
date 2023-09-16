package com.bkav.aiotcloud.screen.home.optioncam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.screen.home.optioncam.CheckStatusCam;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanerQR extends AppCompatActivity {
    private CodeScanner codeScanner;
    private CodeScannerView scannerView;
    private RelativeLayout back;
    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scaner_qr);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
        setData();
        action();
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();

    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void init(){
        this.scannerView = findViewById(R.id.scanner_view);
        this.back = findViewById(R.id.backButton);
        this.codeScanner = new CodeScanner(this, scannerView);
        checkPermission();
    }
    private void setData(){
        this.back.bringToFront();
    }
    private void action(){
       this.back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }
    private void runCodeScanner(){
        this.codeScanner.setAutoFocusEnabled(true);
        this.codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        this.codeScanner.setScanMode(ScanMode.CONTINUOUS);
        this.codeScanner.setFlashEnabled(false);
        this.codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            String dataString = result.getText();
                            JSONObject json = new JSONObject(dataString);
                            Log.e("TAG", "run: " + json);
                            String serial = json.getString("serial");
                            String model = json.getString("model");
                            if(serial.equals("") && model.equals("")){
                                ApplicationService.showToast("Mã serial không hợp lệ", true);
                            }else{
                                Intent intent = new Intent(getApplicationContext(), CheckStatusCam.class);
                                intent.putExtra(CheckStatusCam.SERIAL_KEY, serial);
                                intent.putExtra(CheckStatusCam.MODEL_KEY, model);
                                startActivity(intent);
                            }
                        }catch (JSONException e){
//                            e.printStackTrace();
                            ApplicationService.showToast("Mã serial không hợp lệ", true);
                        }

                    }
                });
            }
        });
    }
    private void checkPermission(){
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA
        };
        if(!hasPermissons(this, PERMISSIONS))
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        else
            runCodeScanner();
    }
    public static boolean hasPermissons(Context context, String... permissons){
        if(context!= null && permissons != null){
            for(String permisson : permissons){
                if(ActivityCompat.checkSelfPermission(context, permisson) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}