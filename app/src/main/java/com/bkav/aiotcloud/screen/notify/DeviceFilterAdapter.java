package com.bkav.aiotcloud.screen.notify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.CameraItem;

import java.util.ArrayList;

public class DeviceFilterAdapter  extends RecyclerView.Adapter<DeviceFilterAdapter.ViewHolder> {
    private ArrayList<CameraItem> cameraItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public DeviceFilterAdapter(Context context, ArrayList<CameraItem> cameraItems) {
        this.cameraItems = cameraItems;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DeviceFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_object_item, parent, false);
        return new DeviceFilterAdapter.ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull DeviceFilterAdapter.ViewHolder holder, int position) {
        CameraItem cameraItem = cameraItems.get(position);
        holder.name.setText(cameraItem.getCameraName());
        holder.checkBox.setChecked(cameraItem.isChoose());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cameraItem.setChoose(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cameraItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final CheckBox checkBox;
        private final RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.nameDevice);
            this.checkBox = itemView.findViewById(R.id.checkboxDevice);
            this.layout = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    CameraItem getItem(int id) {
        return cameraItems.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
