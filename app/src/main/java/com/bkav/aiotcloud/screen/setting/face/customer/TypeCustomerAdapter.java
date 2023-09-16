package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TypeCustomerAdapter extends ArrayAdapter<TypeCustomerItem> {

    public TypeCustomerAdapter(Context context, ArrayList<TypeCustomerItem> typeCustomerItems) {
        super(context, 0, typeCustomerItems);
        this.typeCustomerItems = typeCustomerItems;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TypeCustomerItem typeCustomerItem = typeCustomerItems.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(R.layout.type_customer_item, null);

        TextView name = view.findViewById(R.id.nameType);
        TextView descrip = view.findViewById(R.id.desciption);
        TextView status = view.findViewById(R.id.status);
        RelativeLayout background = view.findViewById(R.id.layoutName);
        background.setBackgroundResource(R.drawable.retangcle);
        // set background
        GradientDrawable drawable = (GradientDrawable) background.getBackground();
        drawable.setColor(Color.parseColor(typeCustomerItem.getBackGroundColor()));

        name.setText(typeCustomerItem.getCustomerTypeName());
        descrip.setText(typeCustomerItem.getDescription());

        if (typeCustomerItem.isActive()){
            status.setTextColor(ContextCompat.getColor(getContext(), R.color.stateActive));
            status.setText(LanguageManager.getInstance().getValue("activated"));
        } else {
            status.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            status.setText(LanguageManager.getInstance().getValue("inactivated"));
        }
        ImageView sound = view.findViewById(R.id.sound);
        ImageView popup = view.findViewById(R.id.popup);
        ImageView blink = view.findViewById(R.id.blink);

        if (typeCustomerItem.getIdentity().contains("popup")){
            popup.setImageResource(R.drawable.popup_highlight);
        } else {
            popup.setImageResource(R.drawable.popup_icon);
        }

        if (typeCustomerItem.getIdentity().contains("sound")){
            sound.setImageResource(R.drawable.sound_highlight);
        } else {
            sound.setImageResource(R.drawable.sound_icon);
        }

        if (typeCustomerItem.getIdentity().contains("flicker")){
            blink.setImageResource(R.drawable.blink_highlight);
        } else {
            blink.setImageResource(R.drawable.blink_icon);
        }
        return view;
    }

    private ArrayList<TypeCustomerItem> typeCustomerItems;
}
