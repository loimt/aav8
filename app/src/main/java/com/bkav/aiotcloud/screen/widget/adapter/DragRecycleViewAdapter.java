package com.bkav.aiotcloud.screen.widget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.notify.detail.NotifyDetailScreen;
import com.bkav.aiotcloud.screen.widget.EventAccessdetectIndays;
import com.bkav.aiotcloud.screen.widget.EventFaceInDays;
import com.bkav.aiotcloud.screen.widget.helper.ItemTouchHelperAdapter;
import com.bkav.aiotcloud.screen.widget.helper.OnStartDragListener;
import com.bkav.aiotcloud.util.Tool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DragRecycleViewAdapter extends RecyclerView.Adapter<DragRecycleViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter, OnStartDragListener {
    private final String TAG = "MyRecycleViewAdapter";
    private Context context;
    private List<WidgetItem> widgetItemList;
    private OnStartDragListener listener;
    private ItemLongClickListener mitemClickListener;

    private ItemClickListener mClickListener;
    public boolean isClickable = true;



    public DragRecycleViewAdapter(Context context, List<WidgetItem> widgetItemList, OnStartDragListener listener) {
        this.context = context;
        this.widgetItemList = widgetItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_card_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WidgetItem widgetItem = widgetItemList.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(20));
        switch (widgetItem.getIndetify()) {
            case ApplicationService.customerrecognize:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_fade));
                holder.avaModel.setImageResource(R.drawable.face_icon);
                break;
            case ApplicationService.accessdetect:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_intru));
                holder.avaModel.setImageResource(R.drawable.intrusion_icon);
                break;
            case ApplicationService.persondetection:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_pede));
                holder.avaModel.setImageResource(R.drawable.pede_widget);
                break;
            case ApplicationService.firedetect:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_fide));
                holder.avaModel.setImageResource(R.drawable.fire_icon_notify_setting);
                break;
            case ApplicationService.licenseplate:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_licen));
                holder.avaModel.setImageResource(R.drawable.license_plate_icon);
                break;
        }


        holder.content.setText(Tool.getStringWithoutFirstEle(widgetItem.getContent(), "\\|", " - "));
        if (widgetItem.getStartAt().equals("") || widgetItem.getStartAt().equals("0")) {
            holder.startAt.setText("");
        } else {
            if(isNumeric(widgetItem.getStartAt())){
                holder.startAt.setText(getTimeLocal(Long.parseLong(widgetItem.getStartAt())));
            }else{
                String date = DateTimeFormat.getTimeFormat(widgetItem.getStartAt(),"yyyy-MM-dd'T'HH:mm:ss","HH:mm:ss - dd/MM/yyyy");
                holder.startAt.setText(date);
            }
        }
        holder.txt_number.setText(String.valueOf(widgetItem.getTotalEvent()));
        if (widgetItem.isSpanned()) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("widgetclick",  widgetItem.getObjectGuide());
                    if( widgetItem.getObjectGuide().length() == 0){
                        return;
                    }
                    Intent intent = new Intent(context, NotifyDetailScreen.class);
                    intent.putExtra(NotifyDetailScreen.OBJGUID, widgetItem.getObjectGuide());
                    intent.putExtra(NotifyDetailScreen.TYPE, "0");
                    context.startActivity(intent);
                }
            });
            holder.txt_number.setVisibility(View.GONE);
            if(widgetItem.getViewType()==1){
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                holder.content.setVisibility(View.VISIBLE);
                holder.startAt.setVisibility(View.VISIBLE);
                holder.imgLastEvent.setVisibility(View.VISIBLE);
                holder.groupImg.setVisibility(View.GONE);
                Glide.with(context)
                        .load(widgetItem.getImagePath())
                        .apply(requestOptions)
                        .into(holder.imgLastEvent);
                holder.avaModel.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen._11sdp);
                holder.avaModel.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen._13sdp);
            }else if(widgetItem.getViewType()==3){
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.conner_layout));
                holder.holverFirstImg.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                holder.holverSecondImg.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                holder.content.setVisibility(View.GONE);
                holder.startAt.setVisibility(View.GONE);
                holder.imgLastEvent.setVisibility(View.GONE);
                holder.groupImg.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(widgetItem.getFirstImg())
                        .apply(requestOptions)
                        .into(holder.firstEventImg);
                Glide.with(context)
                        .load(widgetItem.getSecondImg())
                        .apply(requestOptions)
                        .into(holder.secondEventImg);
                holder.firstContent.setText(Tool.getStringWithoutFirstEle(widgetItem.getFirstContent(), "\\|", " - "));
                holder.secondContent.setText(Tool.getStringWithoutFirstEle(widgetItem.getSecondContent(), "\\|", " - "));
            }
        } else {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (widgetItem.getIndetify().equals(ApplicationService.accessdetect)){
                        Intent intent = new Intent(context, EventAccessdetectIndays.class);
                        context.startActivity(intent);
                    }else if (widgetItem.getIndetify().equals(ApplicationService.customerrecognize)){
                        Intent intent = new Intent(context, EventFaceInDays.class);
                        context.startActivity(intent);
                    }
                }
            });
            holder.avaModel.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen._15sdp);
            holder.avaModel.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen._18sdp);
            if(widgetItem.getViewType()==0){
                holder.txt_number.setVisibility(View.VISIBLE);
                holder.content.setVisibility(View.GONE);
                holder.startAt.setVisibility(View.GONE);
                holder.imgLastEvent.setVisibility(View.GONE);
            }else if(widgetItem.getViewType()==2){

                holder.txt_number.setVisibility(View.GONE);
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                holder.content.setVisibility(View.GONE);
                holder.startAt.setVisibility(View.VISIBLE);
                holder.startAt.setText(Tool.getStringWithoutFirstEle(widgetItem.getContent(), "\\|", " - "));
                holder.imgLastEvent.setVisibility(View.VISIBLE);
                if(widgetItem.getIndetify().equals(ApplicationService.customerrecognize)){
                    if(ApplicationService.countWidgetFari%2==0){
                        Glide.with(context)
                                .load(widgetItem.getFirstImg())
                                .apply(requestOptions)
                                .into(holder.imgLastEvent);
                    }else{
                        Glide.with(context)
                                .load(widgetItem.getSecondImg())
                                .apply(requestOptions)
                                .into(holder.imgLastEvent);
                    }
                }
            }
        }
        if (widgetItem.isDragAble()) {
            onShakeImage(holder.item);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.item.clearAnimation();
            holder.delete.setVisibility(View.GONE);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                widgetItemList.remove(position);
                ArrayList<String> array = new ArrayList<>();
                for(WidgetItem widgetItem : widgetItemList){
                    String identity = widgetItem.getIndetify();
                    int viewType = widgetItem.getViewType();
                    String keyValue = identity+"_"+viewType;
                    array.add(keyValue);
                }
                SharePref.getInstance(context).setWidgetState(String.join(" ", array));
                notifyDataSetChanged();
            }
        });

    }

    private void onShakeImage(RelativeLayout item) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                item.setHasTransientState(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                item.setHasTransientState(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                item.setHasTransientState(true);
            }
        });
        item.setAnimation(shake);
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return widgetItemList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return false;
        }
        WidgetItem item = widgetItemList.remove(fromPosition);
        widgetItemList.add(toPosition, item);
        SharePref.getInstance(context).setWidgetState("");
        for (WidgetItem widgetItem :widgetItemList){
            setStateWidget(widgetItem);
        }
//        Collections.swap(widgetItemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    private void setStateWidget(WidgetItem widgetItem){
        String identify = widgetItem.getIndetify();
        int viewType = widgetItem.getViewType();
        String keyState = identify + "_" + viewType;
        String arrayWidgetState = SharePref.getInstance(context).getWidgetState();
        String newArrayWidgetState = arrayWidgetState + " " + keyState;
        SharePref.getInstance(context).setWidgetState(newArrayWidgetState);
    }

    @Override
    public void onItemDismiss(int position) {
//        widgetItemList.remove(position);
//        notifyItemRemoved(position);
    }

    public void setLongClick(ItemLongClickListener itemClickListener) {
            this.mitemClickListener = itemClickListener;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    public interface ItemLongClickListener {
        void onLongClickListener(View view, int position);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private String getTimeLocal(long time) {
        String pattern = "HH:mm:ss - dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        Timestamp stamp = new Timestamp(time);
        Date date = new Date(stamp.getTime());
        return df.format(date);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView txt_number;
        RelativeLayout item;
        ImageView delete;
        TextView content;
        TextView startAt;
        ImageView avaModel;
        ImageView imgLastEvent;
        LinearLayout groupImg;
        ImageView firstEventImg;
        TextView firstContent;
        ImageView secondEventImg;
        TextView secondContent;
        RelativeLayout holverFirstImg;
        RelativeLayout holverSecondImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txt_number = itemView.findViewById(R.id.txt_number);
            this.item = itemView.findViewById(R.id.item);
            this.delete = itemView.findViewById(R.id.deleteWidget);
            this.content = itemView.findViewById(R.id.content);
            this.startAt = itemView.findViewById(R.id.timeStart);
            this.avaModel = itemView.findViewById(R.id.avaModel);
            this.imgLastEvent = itemView.findViewById(R.id.avaLastEvent);
            this.groupImg = itemView.findViewById(R.id.group_img);
            this.firstEventImg = itemView.findViewById(R.id.firstEventImg);
            this.firstContent = itemView.findViewById(R.id.firstContentEvent);
            this.secondEventImg = itemView.findViewById(R.id.secondEventImg);
            this.secondContent = itemView.findViewById(R.id.secondContentEvent);
            this.holverFirstImg = itemView.findViewById(R.id.holverFirstImg);
            this.holverSecondImg = itemView.findViewById(R.id.holverSecondImg);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (mitemClickListener != null && isClickable)
                mitemClickListener.onLongClickListener(itemView, getBindingAdapterPosition());
            return false;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getBindingAdapterPosition());
        }
    }
}
