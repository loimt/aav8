package com.bkav.aiotcloud.screen.setting.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;

import java.util.ArrayList;

public class ListDayAdapter extends RecyclerView.Adapter<ListDayAdapter.ViewHolder> {

    private ArrayList<DayItem> dayItems;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ListDayAdapter(Context context, ArrayList<DayItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.dayItems = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.day_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayItem dayItem = dayItems.get(position);
        holder.name.setText(dayItem.getName());

        if (dayItem.isActive()){
            holder.isActive.setVisibility(View.VISIBLE);
        } else {
            holder.isActive.setVisibility(View.GONE);
        }

        holder.dayLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                dayItem.setActive(!dayItem.isActive());
                notifyDataSetChanged();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return dayItems.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final ImageView isActive;
        private final RelativeLayout dayLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.dayTx);
            this.isActive = itemView.findViewById(R.id.tick);
            this.dayLayout = itemView.findViewById(R.id.dayLayout);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    DayItem getItem(int id) {
        return dayItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

}