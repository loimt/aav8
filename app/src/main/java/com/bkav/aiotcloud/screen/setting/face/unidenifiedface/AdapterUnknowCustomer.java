package com.bkav.aiotcloud.screen.setting.face.unidenifiedface;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.CustomerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterUnknowCustomer extends RecyclerView.Adapter<AdapterUnknowCustomer.ViewHolder> {

    public AdapterUnknowCustomer(Context context, ArrayList<CustomerItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.customerItems = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.unknow_face_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerItem customerItem = customerItems.get(position);
        holder.type.setText(LanguageManager.getInstance().getValue("unknow_face"));
        holder.name.setText(LanguageManager.getInstance().getValue("unknow_face_name"));
        holder.backgroud.setBackgroundResource(R.drawable.circle);

        GradientDrawable drawable = (GradientDrawable) holder.backgroud.getBackground();
        if (customerItem.getBackGroundColor() != null){
            drawable.setColor(Color.parseColor("#808080"));
        }

        if (customerItem.getTimeDetect() != null){
            holder.timeEvent.setText(DateTimeFormat.getTimeFormat(customerItem.getTimeDetect(), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));
        }

        Glide.with(context)
                .load(customerItem.getAvatarFilePath())
                .placeholder(R.drawable.user_default)
                .circleCrop()
                .into(holder.avatar);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return customerItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;

        private final TextView type;
        private final TextView timeEvent;
        private final ImageView avatar;
        private final RelativeLayout backgroud;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nameCustomer);
            type = view.findViewById(R.id.typeCustomer);
            backgroud = view.findViewById(R.id.backgroud);
            avatar = view.findViewById(R.id.avaterImage);
            timeEvent = view.findViewById(R.id.timeEvent);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    // convenience method for getting data at click position
    CustomerItem getItem(int id) {
        return customerItems.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<CustomerItem> customerItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;
    private Context context;
}

