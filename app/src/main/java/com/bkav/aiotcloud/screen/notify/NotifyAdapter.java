package com.bkav.aiotcloud.screen.notify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.notify.NotifyItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.notify.detail.NotifyDetailScreen;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
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
    public NotifyAdapter(Context context, ArrayList<NotifyItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.notifyItems = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notify_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifyItem notifyItem = notifyItems.get(position);

        holder.content.setText(notifyItem.getContent());
//        holder.time.setText(DateTimeFormat.getTimeFormat(notifyItem.getCreatedAt(), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));
        holder.time.setText(DateTimeFormat.getTimeFormatRelative(notifyItem.getCreatedAt(), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));
        if (notifyItem.isSeen()) {
            holder.seen.setVisibility(View.GONE);
        } else {
            holder.seen.setVisibility(View.VISIBLE);
        }
        holder.backgroud.setBackgroundResource(R.drawable.circle);
        Log.e("event identity", notifyItem.getIdentity());
        if (notifyItem.getIdentity().equals(faceReconizerType)) {
            String[] contens = notifyItem.getContent().split(" " + "\\|" + " ");
            holder.title.setText(LanguageManager.getInstance().getValue("face_recognize"));
            if (contens[0].equals(customer)) {
                holder.content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_content_format"), contens[1], contens[2], contens[3], contens[4]));
            } else if (contens[0].equals(unknow)) {
//                Log.e("xxxxx", contens[1] + contens[2] );
//                holder.title.setText(LanguageManager.getInstance().getValue("stranger_detect"));
                holder.content.setText(String.format(LanguageManager.getInstance().getValue("ai_fr_unknow_format"), contens[1], contens[2]));
            }
//            holder.content.setText(notifyItem.getContent());
            Glide.with(context)
                    .load(notifyItem.getIconNoti())
                    .placeholder(R.drawable.user_default)
                    .circleCrop()
                    .into(holder.avatar);
            GradientDrawable drawable = (GradientDrawable) holder.backgroud.getBackground();
            drawable.setColor(Color.parseColor(notifyItem.getColor()));
        } else if (notifyItem.getIdentity().equals(statusDevice)) {
            holder.title.setText(LanguageManager.getInstance().getValue("deviceactivity"));
            holder.avatar.getLayoutParams().height = 70;
            holder.avatar.getLayoutParams().width = 70;
//            holder.avatar.reqe
            Glide.with(context)
                    .load(R.drawable.camera_icon)
                    .placeholder(R.drawable.camera_icon)
                    .into(holder.avatar);
            if (!notifyItem.getColor().equals("#fff")) {
                GradientDrawable drawable = (GradientDrawable) holder.backgroud.getBackground();
                drawable.setColor(Color.parseColor(notifyItem.getColor()));
            }
           holder.content.setText(String.format(LanguageManager.getInstance().getValue("device_content_format"),  notifyItem.getCameraName(), notifyItem.getContent()));

//            if (notifyItem.getContent().equals(statusCameraActive)) {
//                holder.content.setText(String.format(LanguageManager.getInstance().getValue("device_active_content_format"), notifyItem.getCameraName()));
//            } else if (notifyItem.getContent().equals(statusCameraNoActive)) {
//                holder.content.setText(String.format(LanguageManager.getInstance().getValue("device_inactive_content_format"), notifyItem.getCameraName()));
//            }
        } else if (notifyItem.getIdentity().equals(intrusion)) {
            holder.title.setText(LanguageManager.getInstance().getValue("intrusion_detect"));
            if (notifyItem.getContent().length() != 0){
                String[] contens = notifyItem.getContent().split("\\|");
//                String[] contens = notifyItem.getContent().split("\\|");
                if (contens.length > 1){
                    holder.content.setText(String.format(LanguageManager.getInstance().getValue("instrusion_content_format"), contens[1], contens[2]));
                }
            }
            holder.avatar.getLayoutParams().height = 70;
            holder.avatar.getLayoutParams().width = 70;
//            holder.avatar.reqe
            Glide.with(context)
                    .load(R.drawable.intrusion_icon)
                    .placeholder(R.drawable.intrusion_icon)
                    .into(holder.avatar);
            GradientDrawable drawable = (GradientDrawable) holder.backgroud.getBackground();
            drawable.setColor(ContextCompat.getColor(context, R.color.mainColor));
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetailNotify(notifyItem, context);
            }
        });
    }
    private void viewDetailNotify(NotifyItem notifyItem, Context mContext){
        Intent intent = new Intent(mContext, NotifyDetailScreen.class);
        notifyItem.setSeen(true);
        intent.putExtra(NotifyDetailScreen.OBJGUID, notifyItem.getObjGuid());
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
        private final TextView title;
        private final TextView time;
        private final ImageView avatar;
        private final RelativeLayout backgroud;
        private final ImageView seen;
        private final RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            this.seen = itemView.findViewById(R.id.seen);
            this.avatar = itemView.findViewById(R.id.avaterImage);
            this.backgroud = itemView.findViewById(R.id.backgroud);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.time = itemView.findViewById(R.id.time);
            this.layout = itemView.findViewById(R.id.layoutNotify);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    // convenience method for getting data at click position
    NotifyItem getItem(int id) {
        return notifyItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<NotifyItem> notifyItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
}