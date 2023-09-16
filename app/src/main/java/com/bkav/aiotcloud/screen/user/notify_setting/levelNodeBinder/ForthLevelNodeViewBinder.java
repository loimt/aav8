package com.bkav.aiotcloud.screen.user.notify_setting.levelNodeBinder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkav.aiotcloud.R;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.CheckableNodeViewBinder;

public class ForthLevelNodeViewBinder extends CheckableNodeViewBinder {
    TextView textView;
    ImageView imageView;
    public ForthLevelNodeViewBinder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.node_name_view);
        imageView = (ImageView) itemView.findViewById(R.id.arrow_img);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_forth_level;
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public void bindView(final TreeNode treeNode) {
        textView.setText(treeNode.getValue().toString());
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
        imageView.setVisibility(treeNode.hasChild() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }
}
