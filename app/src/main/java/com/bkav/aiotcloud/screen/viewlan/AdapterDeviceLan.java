package com.bkav.aiotcloud.screen.viewlan;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.aiobject.LanDevice;
import com.bkav.aiotcloud.screen.user.SettingNotifyAdapter;
import com.bkav.aiotcloud.util.MapIdentity;

import java.util.ArrayList;

public class AdapterDeviceLan extends RecyclerView.Adapter<AdapterDeviceLan.ViewHolder> {
    private String TAG = "AdapterDeviceLan";
    private ArrayList<LanDevice> lanDevices;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private SettingNotifyAdapter adapterNotifySetting;


    public AdapterDeviceLan(Context context, ArrayList<LanDevice> data) {
        this.mInflater = LayoutInflater.from(context);
        this.lanDevices = data;
        this.context = context;
    }
    @Override
    public AdapterDeviceLan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_lan_item, parent, false);
        return new AdapterDeviceLan.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDeviceLan.ViewHolder holder, int position) {
        LanDevice lanDevice = lanDevices.get(position);
        holder.title.setText(lanDevice.getSerialNumber());
        holder.ip.setText(processAddress(lanDevice.getAddrs()));
        MapIdentity.mapImageCam(lanDevice.getModel(), holder.circleImage);
    }
    private String processAddress(String address){
        String addressElement = address.split("//")[1];
        String addressconvert = addressElement.split("/")[0];
        String addressCore = addressconvert.split(":")[0];
        return addressCore;
    }

    @Override
    public int getItemCount() {
        return lanDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView ip;
        private final ImageView circleImage;
        ViewHolder(View itemView) {
            super(itemView);
            this.circleImage = itemView.findViewById(R.id.avatarCam);
            this.title = itemView.findViewById(R.id.titleDeviceLan);
            this.ip = itemView.findViewById(R.id.ipDevice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // convenience method for getting data at click position
    LanDevice getItem(int id) {
        return lanDevices.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
