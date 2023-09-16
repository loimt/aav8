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
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
    public static final int EDIT_ZONE_EVENT = 1;
    public static final int DELETE_ZONE_EVENT = 2;

    public ScheduleAdapter(Context context, ArrayList<ScheduleItem> scheduleItems, ScheduleAdapter.OnMenuItemClickListener listener) {
        this.context = context;
        this.scheduleItems = scheduleItems;
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
        return scheduleItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ScheduleItem item = scheduleItems.get(position);
        ((ViewHolder) viewHolder).swipeLayout.setClickToClose(true);
        ((ViewHolder) viewHolder).hour.setText(String.format("%s - %s",item.getTimeStart(),  item.getTimeEnd()));
        ((ViewHolder) viewHolder).days.setText(item.getDateConverse());
//
//        ((ViewHolder) viewHolder).edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onMenuItemClick(item, position, EDIT_ZONE_EVENT);
//            }
//        });
        setAction(((ScheduleAdapter.ViewHolder) viewHolder), item, position);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    private void setAction(ScheduleAdapter.ViewHolder viewHolder, ScheduleItem scheduleItem, int position) {

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuItemClick(scheduleItem, position, EDIT_ZONE_EVENT);
//                Log.e()
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMenuItemClick(scheduleItem, position, DELETE_ZONE_EVENT);
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
        void onMenuItemClick(ScheduleItem scheduleItem, int position, int id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
            swipeLayout = view.findViewById(R.id.swipeLayout);
            hour = view.findViewById(R.id.hourTx);
            days = view.findViewById(R.id.days);
            edit = view.findViewById(R.id.editLayout);
            delete = view.findViewById(R.id.deleteLayout);
            view.setTag(this);
        }

        private SwipeLayout swipeLayout;
        private TextView hour;
        private TextView days;
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
    private ArrayList<ScheduleItem> scheduleItems;
    private ScheduleAdapter.OnMenuItemClickListener listener;

}
