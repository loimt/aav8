package com.bkav.aiotcloud.screen.home.device.genneralsetting;

import android.view.View;

import me.texy.treeview.TreeNode;

public abstract class CheckableNodeViewBinder extends BaseNodeViewBinder {

    public CheckableNodeViewBinder(View itemView) {
        super(itemView);
    }

    /**
     * Get the checkable view id. MUST BE A Checkable type！
     *
     * @return
     */
    public abstract int getCheckableViewId();

    /**
     * Do something when a node select or deselect（only triggered by clicked）
     *
     * @param treeNode
     * @param selected
     */
    public void onNodeSelectedChanged(TreeNode treeNode, boolean selected) {
        /*empty*/
    }
}
