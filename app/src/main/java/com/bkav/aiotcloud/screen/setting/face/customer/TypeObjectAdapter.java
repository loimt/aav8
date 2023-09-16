package com.bkav.aiotcloud.screen.setting.face.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.screen.setting.face.DayItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;

import java.util.ArrayList;

public class TypeObjectAdapter extends RecyclerView.Adapter<TypeObjectAdapter.ViewHolder> {

    private ArrayList<TypeCustomerItem> typeCustomerItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    TypeObjectAdapter(Context context, ArrayList<TypeCustomerItem> typeCustomerItems) {
        this.mInflater = LayoutInflater.from(context);
        this.typeCustomerItems = typeCustomerItems;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.type_object_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TypeCustomerItem typeCustomerItem = typeCustomerItems.get(position);
        holder.name.setText(typeCustomerItem.getCustomerTypeName());
        holder.checkBox.setChecked(typeCustomerItem.isChoose());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                typeCustomerItem.setChoose(b);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return typeCustomerItems.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final CheckBox checkBox;
        private final RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.layout = itemView.findViewById(R.id.layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    TypeCustomerItem getItem(int id) {
        return typeCustomerItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
