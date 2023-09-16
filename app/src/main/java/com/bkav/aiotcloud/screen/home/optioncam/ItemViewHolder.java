package com.bkav.aiotcloud.screen.home.optioncam;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewHolder;
import com.bkav.aiotcloud.R;


public class ItemViewHolder extends TreeViewHolder{
    private EditText regionInput;
    private Dialog dialog;
    private TextView itemName;
    private ImageView stateIcon;
    private RelativeLayout checkBoxLayout;
    private AppCompatCheckBox checkBox;
    private String regionName;
    public ItemViewHolder(@NonNull View itemView, Dialog dialog, EditText regionInput, String regionName) {
        super(itemView);
        this.regionInput = regionInput;
        this.dialog = dialog;
        this.regionName = regionName;
        initView();
    }
    private void initView(){
        this.itemName = itemView.findViewById(R.id.node_name);
        this.checkBox = itemView.findViewById(R.id.checkBox);
        this.stateIcon = itemView.findViewById(R.id.arrow_treeview);
        this.checkBoxLayout = itemView.findViewById(R.id.checkBoxLayout);
    }

    @Override
    public void bindTreeNode(TreeNode node) {
        super.bindTreeNode(node);
        this.checkBox.setClickable(false);
        String itemNameStr = node.getValue().toString();
        if(node.getValue().toString().equals(regionName)){
            this.checkBox.setChecked(true);
        }
        this.itemName.setText(itemNameStr);
        if (node.getChildren().isEmpty()) {
            this.stateIcon.setVisibility(View.GONE);
        } else {
            this.stateIcon.setVisibility(View.VISIBLE);
            if(node.isExpanded()){
                this.stateIcon.animate().rotation(90).setDuration(200).start();
            }else{
                this.stateIcon.animate().rotation(0).setDuration(200).start();
            }
        }
        this.checkBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
                String idChoose = node.getValue().toString();
                regionInput.setText(idChoose);
                dialog.dismiss();
            }
        });
//        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                node.getParent().setExpanded(false);
//                String idChoose = node.getValue().toString();
//                regionInput.setText(idChoose);
//                dialog.dismiss();
//            }
//        });
    }
}
