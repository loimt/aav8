package com.bkav.aiotcloud.screen.home.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.screen.setting.face.customer.CustomerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {
    private String TAG = "CameraAdapter";

    CameraAdapter(Context context, ArrayList<CameraItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.cameraItems = data;
        this.context = context;
    }

    @Override
    public CameraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.camera_item, parent, false);
        return new CameraAdapter.ViewHolder(view);
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
    public void onBindViewHolder(@NonNull CameraAdapter.ViewHolder holder, int position) {
        CameraItem cameraItem = cameraItems.get(position);
        holder.nameDocument.setText(cameraItem.getCameraName());
        if (cameraItem.getIsConnected() == 0) {
            holder.nosignal.setVisibility(View.VISIBLE);
        } else {
            holder.nosignal.setVisibility(View.GONE);
        }

        if(!cameraItem.getSnapShotUrl().equals("")){
            Picasso.get().load(cameraItem.getSnapShotUrl()).fit().centerCrop()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return cameraItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameDocument;
        private final ImageView imageView;
        private final RelativeLayout nosignal;

        ViewHolder(View view) {
            super(view);
            nameDocument = view.findViewById(R.id.nameCamera);
            imageView = view.findViewById(R.id.imageCamera);
            nosignal = view.findViewById(R.id.cameraNoSignal);
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

    private ArrayList<CameraItem> cameraItems;
    private LayoutInflater mInflater;
    private ListDayAdapter.ItemClickListener mClickListener;
    private Context context;
}
