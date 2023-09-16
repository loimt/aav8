package com.bkav.aiotcloud.screen.widget.helper;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.widget.WidgetItem;

public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;
    private static final String TAG = "MyItemTouchHelperCallBack";
    public final ItemTouchHelperAdapter adapter;

    public MyItemTouchHelperCallBack(ItemTouchHelperAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled(){
        return true;
    }


    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof ItemTouchHelperViewHolder){
                ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemTouchHelperViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        if(viewHolder.getItemViewType() != target.getItemViewType()){
            return false;
        }
        else
            adapter.onItemMove(viewHolder.getBindingAdapterPosition(),target.getBindingAdapterPosition());
            return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
            widgetItem.setDragAble(true);
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        if(viewHolder instanceof ItemTouchHelperViewHolder){
            ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemTouchHelperViewHolder.onItemClear();
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        adapter.onItemDismiss(viewHolder.getBindingAdapterPosition());
    }
}
