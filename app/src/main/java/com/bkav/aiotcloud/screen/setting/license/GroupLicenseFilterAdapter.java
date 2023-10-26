package com.bkav.aiotcloud.screen.setting.license;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.customer.TypeCustomerItem;
import com.bkav.aiotcloud.entity.license.LicenseGroupItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;

import java.util.ArrayList;

public class GroupLicenseFilterAdapter extends RecyclerView.Adapter<GroupLicenseFilterAdapter.ViewHolder> {

    private ArrayList<TypeAIObject> licenseGroupItems;

    private ArrayList<Integer> selectCheck = new ArrayList<>();
    private LayoutInflater mInflater;

    public int getCurrentGroupSlected() {
        return currentGroupSlected;
    }

    public void setCurrentGroupSlected(int currentGroupSlected) {
        this.currentGroupSlected = currentGroupSlected;
    }

    private int currentGroupSlected = -1;
    private ListDayAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    GroupLicenseFilterAdapter(Context context, ArrayList<TypeAIObject> typeCustomerItems) {
        this.mInflater = LayoutInflater.from(context);
        this.licenseGroupItems = typeCustomerItems;
        for (int i = 0; i < typeCustomerItems.size(); i++) {
            selectCheck.add(0);
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.group_license_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TypeAIObject licenseGroupItem = licenseGroupItems.get(position);
        holder.name.setText(licenseGroupItem.getName());

        if (selectCheck.get(position) == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<selectCheck.size(); k++) {
                    if(k==position) {
                        selectCheck.set(k,1);
                    } else {
                        selectCheck.set(k,0);
                    }
                }
                notifyDataSetChanged();

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true){
                    currentGroupSlected = position;
                    //Do whatever you want to do with selected value
                }
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return licenseGroupItems.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final RadioButton checkBox;
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
    TypeAIObject getItem(int id) {
        return licenseGroupItems.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setCurrentSelect(int index){
        if (index == -1){
            for (int i = 0; i < selectCheck.size(); i++){
                selectCheck.set(i,0);
            }
        } else {
            selectCheck.set(index, 1);
        }
        currentGroupSlected = index;
}


}
