package com.bkav.aiotcloud.util;

import android.widget.ImageView;

import com.bkav.aiotcloud.R;

public class MapIdentity {
    public static void mapImageCam(String model, ImageView view){
        if(model.contains("C100")){
            view.setBackgroundResource(R.drawable.c100);
        }else if(model.contains("C110")){
            view.setBackgroundResource(R.drawable.c110);
        }else if(model.contains("C300")){
            view.setBackgroundResource(R.drawable.c300);
        }else if(model.contains("P200")){
            view.setBackgroundResource(R.drawable.p200);
        }else if(model.contains("P400")){
            view.setBackgroundResource(R.drawable.p400);
        }else if(model.contains("P450")){
            view.setBackgroundResource(R.drawable.p450);
        }else if(model.contains("S200")){
            view.setBackgroundResource(R.drawable.s200);
        }else if(model.contains("S201")){
            view.setBackgroundResource(R.drawable.s201);
        }else if(model.contains("S500")){
            view.setBackgroundResource(R.drawable.s500);
        }else if(model.contains("C310")){
            view.setBackgroundResource(R.drawable.c310);
        }else{
            view.setBackgroundResource(R.drawable.ava_cam_defautl);
        }
    }
}