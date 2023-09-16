package com.bkav.aiotcloud.screen.user;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.notifySetting.NotifySettingItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

public class SettingNotifyAdapter extends RecyclerView.Adapter<SettingNotifyAdapter.ViewHolder> {
    private String TAG = "SettingNotifyAdapter";
    private ArrayList<NotifySettingItem> notifySettingItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;
    private CompoundButton.OnCheckedChangeListener mCheckedListener;
    private Context context;

    public SettingNotifyAdapter(Context context, ArrayList<NotifySettingItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.notifySettingItems = data;
        this.context = context;
    }

    @Override
    public SettingNotifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notify_setting_item, parent, false);
        return new SettingNotifyAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull SettingNotifyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NotifySettingItem notifyItem = notifySettingItems.get(position);
        holder.title.setText(notifyItem.getNotifyContent());
        holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.title.setSelected(true);
        holder.swNotifyItem.setChecked(notifyItem.isActive());
        if(ApplicationService.identitySetting.equals("customerrecognize")){
            Log.e(TAG, "onBindViewHolder: " );
            holder.circleImage.setBackgroundResource(R.drawable.circle);
            GradientDrawable drawable = (GradientDrawable) holder.circleImage.getBackground();
            drawable.setColor(Color.parseColor(notifyItem.getColor()));
        }else{
            holder.backgroud.setVisibility(View.GONE);
//            holder.circleImage.setVisibility(View.GONE);
        }
        if(ApplicationService.isActiveIdentitySetting){
            holder.swNotifyItem.setClickable(true);
            holder.swNotifyItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    JSONArray dataArray = new JSONArray();
                    JSONObject itemObject = new JSONObject();
                    ApplicationService.notifySettingItems.get(position).setActive(isChecked);
                    try {
                        itemObject.put("id", notifyItem.getId());
                        itemObject.put("notifyContent", notifyItem.getNotifyContent());
                        itemObject.put("isActive", isChecked);
                        itemObject.put("color", notifyItem.getColor());
                        itemObject.put("parentId", notifyItem.getParentId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject payload = new JSONObject();
                    try {
                        payload.put("identity", ApplicationService.identitySetting);
                        payload.put("data", dataArray.put(itemObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ApplicationService.requestManager.updateNotifySetting(payload, ApplicationService.URL_UPDATE_NOTIFY_SETTING);
                }
            });
        }else {
            holder.swNotifyItem.setClickable(false);
        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return notifySettingItems.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final SwitchCompat swNotifyItem;
        private final ImageView circleImage;
        private final RelativeLayout backgroud;
        ViewHolder(View itemView) {
            super(itemView);
            this.circleImage = itemView.findViewById(R.id.circleNotifySetting);
            this.title = itemView.findViewById(R.id.titleNotifySettingItem);
            this.swNotifyItem = itemView.findViewById(R.id.swOnOffNotifySettingItem);
            this.backgroud = itemView.findViewById(R.id.backgroudNotifySettingDetail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public SwitchCompat getSwNotifyItem() {
            return swNotifyItem;
        }
    }

    // convenience method for getting data at click position
    NotifySettingItem getItem(int id) {
        return notifySettingItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    void setCheckedListener(CompoundButton.OnCheckedChangeListener itemCheckedListener) {
        this.mCheckedListener = itemCheckedListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
