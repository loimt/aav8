package com.bkav.aiotcloud.screen.widget.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AccessDetectIndayAdapter extends RecyclerView.Adapter<AccessDetectIndayAdapter.ViewHolder> {

    public AccessDetectIndayAdapter(Context context, ArrayList<CustomerItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.customerItems = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.access_detect_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerItem customerItem = customerItems.get(position);
        holder.type.setText(customerItem.getCustomerTypeName());
        holder.name.setText(customerItem.getFullName());
        holder.backgroud.setBackgroundResource(R.drawable.circle);

        holder.timeDetect.setText(DateTimeFormat.getTimeFormat(customerItem.getTimeDetect(), DateTimeFormat.DATE_ROOTH, DateTimeFormat.TIME_FORMAT));

        GradientDrawable drawable = (GradientDrawable) holder.backgroud.getBackground();
        if (customerItem.getBackGroundColor() != null){
            drawable.setColor(Color.parseColor(customerItem.getBackGroundColor()));
        }

        Glide.with(context)
                .load(customerItem.getThumbnailFilePath())
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
        private final TextView isActive;
        private final ImageView avatar;
        private  final TextView timeDetect;
        private final RelativeLayout backgroud;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nameCustomer);
            type = view.findViewById(R.id.typeCustomer);
            isActive = view.findViewById(R.id.isActive);
            isActive.setVisibility(View.GONE);
            timeDetect = view.findViewById(R.id.timeDetect);
            timeDetect.setVisibility(View.VISIBLE);
            backgroud = view.findViewById(R.id.backgroud);
            avatar = view.findViewById(R.id.avaterImage);
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



