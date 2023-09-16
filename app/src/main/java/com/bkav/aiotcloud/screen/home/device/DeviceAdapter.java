package com.bkav.aiotcloud.screen.home.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.device.DeviceItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private String TAG = "CameraAdapter";

    DeviceAdapter(Context context, ArrayList<DeviceItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.deviceItems = data;
        this.context = context;
    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_item, parent, false);
        return new DeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceItem deviceItem = deviceItems.get(position);
        holder.nameDocument.setText(deviceItem.getCameraName());
        holder.locate.setText(deviceItem.getRegionName());
        if (deviceItem.getIsConnected() == 0) {
            holder.nosignal.setVisibility(View.VISIBLE);
        } else {
            holder.nosignal.setVisibility(View.GONE);
        }

        if(!deviceItem.getSnapShotUrl().equals("")){
            Picasso.get().load(deviceItem.getSnapShotUrl()).fit().centerCrop()
                    .into(holder.imageView);
        }

        if(!deviceItem.getAvatar().equals("")){
            Picasso.get().load(deviceItem.getAvatar()).fit().centerCrop()
                    .into(holder.avartar);
        }
    }


//    @SuppressLint("ViewHolder")
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//        CameraItem cameraItem = cameraItems.get(position);
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        assert inflater != null;
//        view = inflater.inflate(R.layout.camera_item, null);
//        TextView nameDocument = view.findViewById(R.id.nameCamera);
//        ImageView imageView = view.findViewById(R.id.imageCamera);
//        RelativeLayout nosignal = view.findViewById(R.id.cameraNoSignal);
//        if (cameraItem.getIsConnected() == 0){
//            nosignal.setVisibility(View.VISIBLE);
//        } else {
//           nosignal.setVisibility(View.GONE);
//        }
//        nameDocument.setText(cameraItem.getCameraName());
//        if(!cameraItem.getSnapShotUrl().equals("")){
//            Picasso.get().load(cameraItem.getSnapShotUrl()).fit().centerCrop()
//                    .into(imageView);
//        }
//
//        return view;
//    }

    @Override
    public int getItemCount() {
        return deviceItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameDocument;
        private final TextView locate;
        private final ImageView imageView;
        private final ImageView avartar;
        private final RelativeLayout nosignal;

        ViewHolder(View view) {
            super(view);
            nameDocument = view.findViewById(R.id.nameCamera);
            imageView = view.findViewById(R.id.imageCamera);
            nosignal = view.findViewById(R.id.cameraNoSignal);
            avartar = view.findViewById(R.id.avatar);
            locate = view.findViewById(R.id.locate);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<DeviceItem> deviceItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;
    private Context context;
}
