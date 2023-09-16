package com.bkav.aiotcloud.screen.setting.face.historyObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.customer.CustomerEvent;
import com.bkav.aiotcloud.entity.customer.CustomerItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListDateAdapter extends RecyclerView.Adapter<ListDateAdapter.MyViewHolder> {
    private final String TAG = "ListWidgetAdapter";
    private Context context;
    private ArrayList<CustomerEvent> customerEvents;
    private ItemClickListener mitemClickListener;
    public ListDateAdapter(Context context, ArrayList<CustomerEvent> customerEvents){
        this.context = context;
        this.customerEvents = customerEvents;
    }

    @NonNull
    @Override
    public ListDateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListDateAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_date, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ListDateAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomerEvent customerEvent = customerEvents.get(position);
        String date = DateTimeFormat.getTimeFormat(customerEvent.getStartedAt(),"yyyy-MM-dd'T'HH:mm:ss","HH:mm:ss");
        holder.dateTx.setText(date);
        holder.locationTx.setText(customerEvent.getCameraName());
        holder.locationTx.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.locationTx.setSelected(true);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(30));
        Glide.with(context).load(customerEvent.getFilePath()).apply(requestOptions).into(holder.imageEvent);
    }
    @Override
    public int getItemCount() {
        return customerEvents.size();
    }


    public void setOnClick(ListDateAdapter.ItemClickListener itemClickListener){
        this.mitemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickListener(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dateTx;
        TextView locationTx;
        ImageView imageEvent;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            this.dateTx = itemView.findViewById(R.id.dateTx);
            this.locationTx = itemView.findViewById(R.id.locationTx);
            this.imageEvent = itemView.findViewById(R.id.eventImg);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(mitemClickListener != null) mitemClickListener.onClickListener(itemView, getBindingAdapterPosition());
        }
    }
}
