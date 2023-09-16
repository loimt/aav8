package com.bkav.aiotcloud.screen.setting.face;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.entity.aiobject.ZoneAIObject;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class ListZoneAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
    public static final int EDIT_ZONE_EVENT = 1;
    public static final int DELETE_ZONE_EVENT = 2;

    public ListZoneAdapter(Context context, ArrayList<ZoneAIObject> zoneAIObjects, OnMenuItemClickListener listener) {
        this.context = context;
        this.zoneAIObjects = zoneAIObjects;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return zoneAIObjects.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zone_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ZoneAIObject item = zoneAIObjects.get(position);
        ((ViewHolder) viewHolder).swipeLayout.setClickToClose(true);
        ((ViewHolder) viewHolder).nameZone.setText(item.getZoneName());
//
//        ((ViewHolder) viewHolder).edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onMenuItemClick(item, position, EDIT_ZONE_EVENT);
//            }
//        });
        setAction(((ViewHolder) viewHolder), item, position);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    private void setAction(ListZoneAdapter.ViewHolder viewHolder, ZoneAIObject zoneAIObject, int position) {

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuItemClick(zoneAIObject, position, EDIT_ZONE_EVENT);
//                Log.e()
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuItemClick(zoneAIObject, position, DELETE_ZONE_EVENT);
//                Log.e()
            }
        });

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    @Override
    public void closeAllItems() {
        super.closeAllItems();
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(ZoneAIObject zoneAIObject, int position, int id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            swipeLayout = view.findViewById(R.id.swipeLayout);
            nameZone = view.findViewById(R.id.zoneName);
            edit = view.findViewById(R.id.editLayout);
            delete = view.findViewById(R.id.deleteLayout);
            view.setTag(this);
        }

        private SwipeLayout swipeLayout;
        private TextView nameZone;
        private RelativeLayout edit;
        private RelativeLayout delete;
    }

//    class TitleHolder extends RecyclerView.ViewHolder {
//        TitleHolder(View itemView) {
//            super(itemView);
//            this.title = itemView.findViewById(R.id.title);
//            itemView.setTag(this);
//        }
//
//        private TextView title;
//    }

    private Context context;
    private ArrayList<ZoneAIObject> zoneAIObjects;
    private OnMenuItemClickListener listener;

}
