package com.bkav.aiotcloud.screen.home;

import static com.bkav.aiotcloud.screen.MainScreen.currentFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.home.camera.CameraFragment;
import com.bkav.aiotcloud.screen.home.camera.FilterCamera;
import com.bkav.aiotcloud.screen.home.device.DeviceFragment;
import com.bkav.aiotcloud.screen.home.optioncam.ManualAdd;
import com.bkav.aiotcloud.screen.home.optioncam.ScanerQR;
import com.bkav.aiotcloud.screen.viewlan.ViewOnLan;

public class HomeFragment extends Fragment {

    public static final int CAMERA = 0; // Màn hình camera
    public static final int DEVICE = 1; // Màn hình mặt bằng
    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        init(view);
        setData();
        action();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        changeScreen();
    }

    public void updateUI(){
        if (currentFragment == CAMERA){
            if (cameraFragment != null){
                cameraFragment.updateUI();
            }
        } else {
            if (deviceFragment != null){
                deviceFragment.updateUI();
            }
        }

    }
    private void init(View view){
        this.shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        this.addIM = view.findViewById(R.id.addIM);
        this.optionAddCam = view.findViewById(R.id.optionAddCam);
        this.filterCamLayout = view.findViewById(R.id.filterCamLayout);
        this.scanLan =view.findViewById(R.id.scanLanLayout);
        this.scanQR = view.findViewById(R.id.scanQRLayout);
        this.manualAdd = view.findViewById(R.id.manualAddLayout);
        this.scanQRTx = view.findViewById(R.id.scanQRTx);
        this.scanLanTx = view.findViewById(R.id.scanLanTx);
        this.manualTx = view.findViewById(R.id.manualTx);
        this.frameLayout = view.findViewById(R.id.frameLayout);
        this.title = view.findViewById(R.id.title);
        this.qrImage = view.findViewById(R.id.qrImage);
        this.lanImage = view.findViewById(R.id.lanImage);
        this.manualImage = view.findViewById(R.id.manualImage);

        this.btCameras = view.findViewById(R.id.btCamera);
        this.btDevices = view.findViewById(R.id.btDevices);

        this.btDevices.setText(LanguageManager.getInstance().getValue("devices"));
    }
    private void setData(){
        this.title.setText(LanguageManager.getInstance().getValue("cameras"));
        this.scanLanTx.setText(LanguageManager.getInstance().getValue("scan_lan"));
        this.scanQRTx.setText(LanguageManager.getInstance().getValue("scan_QR"));
        this.manualTx.setText(LanguageManager.getInstance().getValue("manual_add"));
        this.qrImage.setColorFilter(getContext().getResources().getColor(R.color.mainColor));
        this.lanImage.setColorFilter(getContext().getResources().getColor(R.color.mainColor));
        this.manualImage.setColorFilter(getContext().getResources().getColor(R.color.mainColor));
        this.cameraFragment = new CameraFragment();
        this.deviceFragment = new DeviceFragment();
        setGone(scanQR);
        setGone(scanLan);
        setGone(manualAdd);
        changeScreen(cameraFragment);
    }

    private void action(){
        this.addIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showOptionAddCam){
                    setGone(scanQR);
                    setGone(scanLan);
                    setGone(manualAdd);
                    addIM.animate().rotation(0).start();
                    sendViewToBack(optionAddCam);
                    frameLayout.setAlpha(1);
                    showOptionAddCam = false;
                }else{
                    addIM.animate().rotation(-45).start();
                    frameLayout.setAlpha(0.5f);
                    optionAddCam.bringToFront();
                    setView(scanQR);
                    setView(scanLan);
                    setView(manualAdd);
                    showOptionAddCam = true;
                }
            }
        });
        this.optionAddCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showOptionAddCam){
                    addIM.animate().rotation(0).start();
                    frameLayout.setAlpha(1);
                    setGone(scanQR);
                    setGone(scanLan);
                    setGone(manualAdd);
                    showOptionAddCam = false;
                    sendViewToBack(optionAddCam);
                }
            }
        });
        this.scanLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewOnLan.class);
                intent.putExtra(ViewOnLan.TYPE, "addCam");
                startActivity(intent);
            }
        });
        this.scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanerQR.class);
                startActivity(intent);
            }
        });
        this.manualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManualAdd.class);
                startActivity(intent);
            }
        });
        this.filterCamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FilterCamera.class);
                startActivity(intent);
            }
        });

        this.btCameras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable cameraDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.camera_tab_selected);
                btCameras.setBackground(cameraDrawable);
                Drawable deviceDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.groud_tab);
                btDevices.setBackground(deviceDrawable);
                if (currentFragment != CAMERA){
                    changeScreen(cameraFragment);
                    currentFragment = CAMERA;
                }
            }
        });

        this.btDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable cameraDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.camera_tab);
                btCameras.setBackground(cameraDrawable);

                Drawable deviceDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.groud_tab_selected);
                btDevices.setBackground(deviceDrawable);
                if (currentFragment != DEVICE){
                    changeScreen(deviceFragment);
                    currentFragment = DEVICE;
                }
            }
        });
    }

    private void changeScreen(){
        Log.e("resume", " " + cameraFragment);
        if (currentFragment == DEVICE){
            Drawable cameraDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.camera_tab);
            btCameras.setBackground(cameraDrawable);
            Drawable deviceDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.groud_tab_selected);
            btDevices.setBackground(deviceDrawable);
                changeScreen(deviceFragment);
        } else {
            Drawable cameraDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.camera_tab_selected);
            btCameras.setBackground(cameraDrawable);
            Drawable deviceDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.groud_tab);
            btDevices.setBackground(deviceDrawable);
            changeScreen(cameraFragment);
        }
    }

    private void changeScreen(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
    private void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    private FrameLayout frameLayout;
    private TextView title;
    private CameraFragment cameraFragment;
    private DeviceFragment deviceFragment;
    private RelativeLayout optionAddCam;
    private RelativeLayout filterCamLayout;
    private RelativeLayout scanQR;
    private RelativeLayout scanLan;
    private RelativeLayout manualAdd;
    private TextView scanQRTx;
    private TextView scanLanTx;
    private TextView manualTx;
    private ImageView qrImage;
    private ImageView lanImage;
    private ImageView manualImage;
    private boolean showOptionAddCam = false;
    private TextView btCameras;
    private TextView btDevices;
    private ImageView addIM;
    private int shortAnimationDuration;
}

