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
import com.bkav.aiotcloud.entity.aiobject.AIObject;
import com.bkav.aiotcloud.language.LanguageManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FaceObjectAdapter  extends ArrayAdapter<AIObject> {

    public FaceObjectAdapter(Context context, ArrayList<AIObject> aiObjects) {
        super(context, 0, aiObjects);
        this.aiObjects = aiObjects;
    }


    @SuppressLint({"ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        AIObject aiObject = aiObjects.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.ai_object_item, null);
        TextView nameDocument = view.findViewById(R.id.nameCamera);
        TextView isActive = view.findViewById(R.id.isActive);
        ImageView imageView = view.findViewById(R.id.imageCamera);
        RelativeLayout nosignal = view.findViewById(R.id.cameraNoSignal);
        nameDocument.setText(aiObject.getCameraName());
        if (aiObject.getIsActive() == 0){
            nosignal.setVisibility(View.VISIBLE);
        } else {
            nosignal.setVisibility(View.GONE);
        }
        if (aiObject.getStatusProcess() != 0){
            isActive.setText(LanguageManager.getInstance().getValue("activated"));
            isActive.setTextColor(getContext().getResources().getColor(R.color.stateActive));
        } else {
            isActive.setText(LanguageManager.getInstance().getValue("inactivated"));
            isActive.setTextColor(getContext().getResources().getColor(R.color.mainColor));
        }
        if(aiObject.getSnapShotUrl() != null && !aiObject.getSnapShotUrl().equals("")){
            Picasso.get().load(aiObject.getSnapShotUrl()).fit().centerCrop()
                    .into(imageView);
        }
        return view;
    }

    private ArrayList<AIObject> aiObjects;
}

