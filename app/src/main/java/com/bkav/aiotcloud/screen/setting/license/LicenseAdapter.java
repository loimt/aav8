package com.bkav.aiotcloud.screen.setting.license;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.bkav.aiotcloud.entity.aiobject.TypeAIObject;
import com.bkav.aiotcloud.entity.license.LicenseGroupItem;
import com.bkav.aiotcloud.entity.license.LicenseItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;

import java.util.ArrayList;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {

    public LicenseAdapter(Context context, ArrayList<LicenseItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.licenseItems = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.license_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LicenseItem licenseItem = licenseItems.get(position);
        holder.name.setText(licenseItem.getNoPlate());

        for(TypeAIObject lsg : ApplicationService.licenseGroupItems){
            if (licenseItem.getLicensePlateTypeId() == lsg.getID()){
                holder.type.setText(lsg.getName());
                GradientDrawable drawable = (GradientDrawable) holder.typeLayout.getBackground();
                if (licenseItem.getLicensePlateTypeColor() != null){
                    drawable.setColor(Color.parseColor(lsg.getBackground()));
                }
            }
        }
        setImage(licenseItem.getObjectDetectTypeIdentity(), holder.avatar);
        holder.avatar.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return licenseItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView type;
        private final ImageView avatar;
        private final RelativeLayout typeLayout;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.license);
            type = view.findViewById(R.id.type);
            avatar = view.findViewById(R.id.image);
            typeLayout = view.findViewById(R.id.typeLayout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    // convenience method for getting data at click position
    LicenseItem getItem(int id) {
        return licenseItems.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ListDayAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private ArrayList<LicenseItem> licenseItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;
    private Context context;

    public void setImage(String type, ImageView image) {
        Log.e("getListLicense12", type+ "");
        switch (type) {
            case "motor":
                image.setImageResource(R.drawable.license_motor);
                break;
            case "unknown":
                image.setImageResource(R.drawable.license_unknown);
                break;
            case "car":
                image.setImageResource(R.drawable.license_car);
                break;
            case "bicycle":
                image.setImageResource(R.drawable.license_bicycle);
                break;
            case "bus":
                image.setImageResource(R.drawable.license_bus);
                break;
            case "van":
                image.setImageResource(R.drawable.license_van);
                break;
            case "ambulance":
                image.setImageResource(R.drawable.license_ambulance);
                break;
            case "firetruck":
                image.setImageResource(R.drawable.license_firestruck);
                break;
            case "container":
                image.setImageResource(R.drawable.license_container);
                break;
            default:
                image.setImageResource(R.drawable.license_unknown);
                break;
        }
    }
}


