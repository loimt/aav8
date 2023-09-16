package com.bkav.aiotcloud.screen.home.device.genneralsetting;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import me.texy.treeview.TreeNode;

/**
 * Created by zxy on 17/4/23.
 */

public abstract class BaseNodeViewBinder extends RecyclerView.ViewHolder {
    /**
     * This reference of TreeView make BaseNodeViewBinder has the ability
     * to expand node or select node.
     */
    protected TreeView treeView;

    public BaseNodeViewBinder(View itemView) {
        super(itemView);
    }

    public void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }

    /**
     * Get node item layout id
     */
    public abstract int getLayoutId();

    /**
     * Bind your data to view,you can get the data from treeNode by getValue()
     *
     * @param treeNode Node data
     */
    public abstract void bindView(TreeNode treeNode);

    /**
     * if you do not want toggle the node when click whole item view,then you can assign a view to
     * trigger the toggle action
     *
     * @return The assigned view id to trigger expand or collapse.
     */
    public int getToggleTriggerViewId() {
        return 0;
    }

    /**
     * Callback when a toggle action happened (only by clicked)
     *
     * @param treeNode The toggled node
     * @param expand   Expanded or collapsed
     */
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        //empty
    }
}
