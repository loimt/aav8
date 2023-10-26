package com.bkav.aiotcloud.screen.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.widget.adapter.ViewPagerAdapter;

import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class TypeWidget extends AppCompatActivity {
    private String typeModel;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ViewPagerAdapter viewPagerAdapter;
//    private List<WidgetItem> listWidgetType;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_widget);
        setLayout();
        init();
        setData();
        action();

    }

    private void init() {
        this.typeModel = getIntent().getStringExtra(WidgetFragment.MODELNAME);
        this.save = findViewById(R.id.confirmAddWidget);
        this.viewPager = findViewById(R.id.viewPager);
        this.circleIndicator = findViewById(R.id.circleIndicator);
        setListWidgetType(ApplicationService.listWidgetType);
        this.viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), ApplicationService.listWidgetType);

    }
    public void updateUI(){
        this.viewPagerAdapter.notifyDataSetChanged();
    }
    private void setData() {
        this.viewPager.setAdapter(viewPagerAdapter);
        this.circleIndicator.setViewPager(viewPager);
        this.viewPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private void action() {
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationService.listWidgetView.add(ApplicationService.listWidgetType.get(viewPager.getCurrentItem()));
                setStateWidget(ApplicationService.listWidgetType.get(viewPager.getCurrentItem()));
                finish();
            }
        });

    }
    private void setStateWidget(WidgetItem widgetItem){
        String identify = widgetItem.getIndetify();
        int viewType = widgetItem.getViewType();
        String keyState = identify + "_" + viewType;
        String arrayWidgetState = SharePref.getInstance(getApplicationContext()).getWidgetState();
        String newArrayWidgetState = arrayWidgetState + " " + keyState;
        SharePref.getInstance(getApplicationContext()).setWidgetState(newArrayWidgetState);
    }

    private void setListWidgetType(List<WidgetItem> listWidget) {
//        ApplicationService.listWidgetType.clear();
        listWidget.clear();
        WidgetItem widget1;
        WidgetItem widget2;
        WidgetItem widget3;
        WidgetItem widget4;
        switch (typeModel) {
            case ApplicationService.customerrecognize:
                widget1 = WidgetType_1(ApplicationService.fariWidget);
                widget2 = WidgetType_2(ApplicationService.fariWidget);
                widget3 = WidgetType_3(ApplicationService.fariWidget);
                widget4 = WidgetType_4(ApplicationService.fariWidget);
                listWidget.addAll(Arrays.asList(widget1, widget2, widget3, widget4));
                break;
            case ApplicationService.accessdetect:
                widget1 = WidgetType_1(ApplicationService.intrudWidget);
                widget2 = WidgetType_2(ApplicationService.intrudWidget);
                listWidget.addAll(Arrays.asList(widget1, widget2));
                break;
            case ApplicationService.persondetection:
                widget1 = WidgetType_1(ApplicationService.pedeWidget);
                widget2 = WidgetType_2(ApplicationService.pedeWidget);
                listWidget.addAll(Arrays.asList(widget1, widget2));
                break;
            case ApplicationService.firedetect:
                widget1 = WidgetType_1(ApplicationService.fideWidget);
                widget2 = WidgetType_2(ApplicationService.fideWidget);
                listWidget.addAll(Arrays.asList(widget1, widget2));
                break;
            case ApplicationService.licenseplate:
                widget1 = WidgetType_1(ApplicationService.licenWidget);
                widget2 = WidgetType_2(ApplicationService.licenWidget);
                widget3 = WidgetType_3(ApplicationService.licenWidget);
                widget4 = WidgetType_4(ApplicationService.licenWidget);
                listWidget.addAll(Arrays.asList(widget1, widget2, widget3, widget4));
                break;
        }
    }
    private WidgetItem WidgetType_1(WidgetItem sampleWidget){
        return new WidgetItem(sampleWidget.getIndetify(), sampleWidget.getTotalEvent(), sampleWidget.getImagePath(), sampleWidget.getContent(), sampleWidget.getStartAt(), sampleWidget.isDragAble(), 0, false, sampleWidget.getFirstImg(), sampleWidget.getSecondImg(), sampleWidget.getFirstContent(), sampleWidget.getSecondContent());
    }
    private WidgetItem WidgetType_2(WidgetItem sampleWidget){
        return new WidgetItem(sampleWidget.getIndetify(), sampleWidget.getTotalEvent(), sampleWidget.getImagePath(), sampleWidget.getContent(), sampleWidget.getStartAt(), sampleWidget.isDragAble(), 1, true, sampleWidget.getFirstImg(), sampleWidget.getSecondImg(), sampleWidget.getFirstContent(), sampleWidget.getSecondContent());
    }
    private WidgetItem WidgetType_3(WidgetItem sampleWidget){
        return new WidgetItem(sampleWidget.getIndetify(), sampleWidget.getTotalEvent(), sampleWidget.getImagePath(), sampleWidget.getContent(), sampleWidget.getStartAt(), sampleWidget.isDragAble(), 2, false, sampleWidget.getFirstImg(), sampleWidget.getSecondImg(), sampleWidget.getFirstContent(), sampleWidget.getSecondContent());
    }
    private WidgetItem WidgetType_4(WidgetItem sampleWidget){
        return new WidgetItem(sampleWidget.getIndetify(), sampleWidget.getTotalEvent(), sampleWidget.getImagePath(), sampleWidget.getContent(), sampleWidget.getStartAt(), sampleWidget.isDragAble(), 3, true, sampleWidget.getFirstImg(), sampleWidget.getSecondImg(), sampleWidget.getFirstContent(), sampleWidget.getSecondContent());
    }
    private void setLayout() {
        Window window = this.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        setFinishOnTouchOutside(true);
    }



}