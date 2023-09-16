package com.bkav.aiotcloud.screen.widget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.language.LanguageManager;

import java.util.List;

public class ListWidgetAdapter extends RecyclerView.Adapter<ListWidgetAdapter.MyViewHolder> {
    private final String TAG = "ListWidgetAdapter";
    private Context context;
    private List<WidgetItem> widgetItemList;
    private ItemClickListener mitemClickListener;

    public ListWidgetAdapter(Context context, List<WidgetItem> widgetItemList) {
        this.context = context;
        this.widgetItemList = widgetItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_widget_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WidgetItem widgetItem = widgetItemList.get(position);
        switch (widgetItem.getIndetify()) {
            case ApplicationService.customerrecognize:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_widget_fade));
                holder.avaModel.setImageResource(R.drawable.fade_widget_icon);
                break;
            case ApplicationService.accessdetect:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_widget_intru));
                holder.avaModel.setImageResource(R.drawable.intru_widget_icon);
                break;
            case ApplicationService.persondetection:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_widget_pede));
                holder.avaModel.setImageResource(R.drawable.pede_widget_icon);
                break;
            case ApplicationService.firedetect:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_widget_fide));
                holder.avaModel.setImageResource(R.drawable.fide_widget_icon);
                break;
            case ApplicationService.licenseplate:
                holder.item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_widget_licen));
                holder.avaModel.setImageResource(R.drawable.licen_widget_icon);
                break;
        }
        holder.identity.setText(LanguageManager.getInstance().getValue(widgetItem.getIndetify()));
        holder.identity.bringToFront();
    }
    @Override
    public int getItemCount() {
        return widgetItemList.size();
    }


    public void setOnClick(ItemClickListener itemClickListener){
        this.mitemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickListener(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RelativeLayout item;
        ImageView avaModel;
        TextView identity;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            this.item = itemView.findViewById(R.id.item);
            this.avaModel = itemView.findViewById(R.id.avaModel);
            this.identity = itemView.findViewById(R.id.identity);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if(mitemClickListener != null) mitemClickListener.onClickListener(itemView, getBindingAdapterPosition());
        }
    }
}
