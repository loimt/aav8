package com.bkav.aiotcloud.screen.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.screen.widget.adapter.ListWidgetAdapter;

public class AddWidgetFeature extends AppCompatActivity {
    private RecyclerView listWidget;
    private ListWidgetAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_widget_feature);
        setLayout();
        init();
        setData();
        action();

    }

    private void init() {
        this.listWidget = findViewById(R.id.listWidget);
    }

    private void setData() {
        this.gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        this.adapter = new ListWidgetAdapter(getApplicationContext(), ApplicationService.listWidget);
        this.listWidget.setLayoutManager(gridLayoutManager);
        this.listWidget.setAdapter(adapter);
    }

    private void action() {
        adapter.setOnClick(new ListWidgetAdapter.ItemClickListener() {
            @Override
            public void onClickListener(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), TypeWidget.class);
                intent.putExtra(WidgetFragment.TYPE, WidgetFragment.addWidgetFeatureKey);
                intent.putExtra(WidgetFragment.MODELNAME, ApplicationService.listWidget.get(position).getIndetify());
                startActivity(intent);
                finish();
            }
        });
    }

    private void setLayout() {
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        setFinishOnTouchOutside(true);
    }

}