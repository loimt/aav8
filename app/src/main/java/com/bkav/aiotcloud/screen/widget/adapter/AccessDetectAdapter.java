package com.bkav.aiotcloud.screen.widget.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.aiobject.AccessdetectItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.notify.NotifyAdapter;
import com.bkav.aiotcloud.screen.notify.detail.NotifyDetailScreen;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AccessDetectAdapter extends RecyclerView.Adapter<AccessDetectAdapter.ViewHolder> {
    private String TAG = "NotifyAdapter";
    public static final String statusCameraType = "Camera Status";
    public static final String statusDevice = "deviceactivity";
    public static final String statusBoxType = "AI Box Status";
    public static final String statusCameraNoActive = "Inactive";
    public static final String statusCameraActive = "Active";
    public static final String intrusion = "accessdetect";
    public static final String faceReconizerType = "customerrecognize";
    public static final String customer = "Customer";
    public static final String unknow = "Unknow";

    // data is passed into the constructor
    public AccessDetectAdapter(Context context, ArrayList<AccessdetectItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.notifyItems = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.access_detect_widget_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccessDetectAdapter.ViewHolder holder, int position) {
        AccessdetectItem accessdetectItem = notifyItems.get(position);

        holder.content.setText(String.format(LanguageManager.getInstance().getValue("content_access_detect"), accessdetectItem.getCameraName(),accessdetectItem.getAddress()));
        holder.time.setText(DateTimeFormat.getTimeFormat(accessdetectItem.getCreatedAt(), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));

                Glide.with(ApplicationService.getAppContext())
                .load(accessdetectItem.getFullImage()).into(holder.imageEvent);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetailNotify(accessdetectItem, context);
            }
        });
    }
    private void viewDetailNotify(AccessdetectItem accessdetectItem, Context mContext){
        Intent intent = new Intent(mContext, NotifyDetailScreen.class);
        intent.putExtra(NotifyDetailScreen.OBJGUID, accessdetectItem.getObjGuidEvent());
        intent.putExtra(NotifyDetailScreen.TYPE, "0");
        mContext.startActivity(intent);
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return notifyItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView content;
        private final TextView time;
        private final ImageView imageEvent;
        private final RelativeLayout layout;
        ViewHolder(View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.content);
            this.imageEvent = itemView.findViewById(R.id.imgEvent);
            this.time = itemView.findViewById(R.id.timeStart);
            this.layout = itemView.findViewById(R.id.layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    // convenience method for getting data at click position
    AccessdetectItem getItem(int id) {
        return notifyItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(NotifyAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<AccessdetectItem> notifyItems;
    private LayoutInflater mInflater;
    private NotifyAdapter.ItemClickListener mClickListener;
    private Context context;
}