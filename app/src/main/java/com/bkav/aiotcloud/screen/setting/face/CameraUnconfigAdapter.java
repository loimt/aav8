package com.bkav.aiotcloud.screen.setting.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.aiobject.CameraUnConfigItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CameraUnconfigAdapter extends ArrayAdapter<CameraUnConfigItem> {

    public CameraUnconfigAdapter(Context context, ArrayList<CameraUnConfigItem> cameraUnConfigItems) {
        super(context, 0, cameraUnConfigItems);
        this.cameraUnConfigItems = cameraUnConfigItems;
    }


    @SuppressLint({"ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CameraUnConfigItem cameraUnConfigItem = cameraUnConfigItems.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.camera_item, null);
        TextView nameDocument = view.findViewById(R.id.nameCamera);
        ImageView imageView = view.findViewById(R.id.imageCamera);
        ImageView checkBox = view.findViewById(R.id.checkbox);

        RelativeLayout nosignal = view.findViewById(R.id.cameraNoSignal);
        if (cameraUnConfigItem.getIsActive() == 0){
            nosignal.setVisibility(View.VISIBLE);
        } else {
            nosignal.setVisibility(View.GONE);
        }
        if (currentSelected == position){
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }
        nameDocument.setText(cameraUnConfigItem.getCameraName());
        if (cameraUnConfigItem.getSnapShotUrl().length() != 0)
        Picasso.get().load(cameraUnConfigItem.getSnapShotUrl()).fit().centerCrop()
                .into(imageView);
        return view;
    }

    public void updateSelect(int positionSelected){
            currentSelected = positionSelected;
    }

    private ArrayList<CameraUnConfigItem> cameraUnConfigItems;
    private int currentSelected = -1;
}


