package com.bkav.aiotcloud.screen.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.config.DateTimeFormat;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.util.Tool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private Context context;
    private List<WidgetItem> widgetItemList;

    public ViewPagerAdapter(Context context, List<WidgetItem> widgetItemList) {
        this.context = context;
        this.widgetItemList = widgetItemList;
    }


    @Override
    public int getCount() {
        return widgetItemList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_type_widget, container, false);
        TextView summaryInfo = view.findViewById(R.id.summaryInfo);
        TextView contenInfo = view.findViewById(R.id.contentInfo);
        TextView txt_number = view.findViewById(R.id.txt_number);
        RelativeLayout item = view.findViewById(R.id.item);
        TextView content = view.findViewById(R.id.content);
        TextView startAt = view.findViewById(R.id.timeStart);
        ImageView avaModel = view.findViewById(R.id.avaModel);
        ImageView imgLastEvent = view.findViewById(R.id.avaLastEvent);
        LinearLayout groupImg = view.findViewById(R.id.group_img);
        ImageView firstEventImg = view.findViewById(R.id.firstEventImg);
        TextView firstContent = view.findViewById(R.id.firstContentEvent);
        ImageView secondEventImg = view.findViewById(R.id.secondEventImg);
        TextView secondContent = view.findViewById(R.id.secondContentEvent);
        RelativeLayout holverFirstImg = view.findViewById(R.id.holverFirstImg);
        RelativeLayout holverSecondImg = view.findViewById(R.id.holverSecondImg);
        WidgetItem widgetItem = widgetItemList.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new RoundedCorners(20));
        switch (widgetItem.getIndetify()) {
            case ApplicationService.customerrecognize:
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_fade));
                avaModel.setImageResource(R.drawable.face_icon);
                break;
            case ApplicationService.accessdetect:
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_intru));
                avaModel.setImageResource(R.drawable.intrusion_icon);
                break;
            case ApplicationService.persondetection:
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_pede));
                avaModel.setImageResource(R.drawable.pede_widget);
                break;
            case ApplicationService.firedetect:
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_fide));
                avaModel.setImageResource(R.drawable.fire_icon_notify_setting);
                break;
            case ApplicationService.licenseplate:
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.color_licen));
                avaModel.setImageResource(R.drawable.license_plate_icon);
                break;
        }
        content.setText(Tool.getStringWithoutFirstEle(widgetItem.getContent(), "\\|", " - "));
        if (widgetItem.getStartAt().equals("") || widgetItem.getStartAt().equals("0")) {
            startAt.setText("");
        } else {
            if(isNumeric(widgetItem.getStartAt())){
                startAt.setText(getTimeLocal(Long.parseLong(widgetItem.getStartAt())));
            }else{
                String date = DateTimeFormat.getTimeFormat(widgetItem.getStartAt(),"yyyy-MM-dd'T'HH:mm:ss","HH:mm:ss - dd/MM/yyyy");
                startAt.setText(date);
            }
        }
        txt_number.setText(String.valueOf(widgetItem.getTotalEvent()));
        if (widgetItem.isSpanned()) {
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            summaryInfo.setText(LanguageManager.getInstance().getValue("widget_last_event"));
            contenInfo.setText(LanguageManager.getInstance().getValue("widget_last_event_content"));
            txt_number.setVisibility(View.GONE);
            if(widgetItem.getViewType()==1){
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                content.setVisibility(View.VISIBLE);
                startAt.setVisibility(View.VISIBLE);
                imgLastEvent.setVisibility(View.VISIBLE);
                groupImg.setVisibility(View.GONE);

                Glide.with(context)
                        .load(widgetItem.getImagePath())
                        .apply(requestOptions)
                        .into(imgLastEvent);
                avaModel.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen._11sdp);
                avaModel.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen._13sdp);
            }else if(widgetItem.getViewType()==3){
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.conner_layout));
                holverFirstImg.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                holverSecondImg.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                content.setVisibility(View.GONE);
                startAt.setVisibility(View.GONE);
                imgLastEvent.setVisibility(View.GONE);
                groupImg.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(widgetItem.getFirstImg())
                        .apply(requestOptions)
                        .into(firstEventImg);
                Glide.with(context)
                        .load(widgetItem.getSecondImg())
                        .apply(requestOptions)
                        .into(secondEventImg);
                firstContent.setText(Tool.getStringWithoutFirstEle(widgetItem.getFirstContent(), "\\|", " - "));
                secondContent.setText(Tool.getStringWithoutFirstEle(widgetItem.getSecondContent(), "\\|", " - "));
            }
        } else {
            item.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen._150sdp);
            avaModel.getLayoutParams().width = (int) context.getResources().getDimension(R.dimen._15sdp);
            avaModel.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen._18sdp);
            if(widgetItem.getViewType()==0){
                summaryInfo.setText(LanguageManager.getInstance().getValue("widget_general_information"));
                contenInfo.setText(LanguageManager.getInstance().getValue("widget_general_content"));
                txt_number.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                startAt.setVisibility(View.GONE);
                imgLastEvent.setVisibility(View.GONE);
            }else if(widgetItem.getViewType()==2){
                summaryInfo.setText(LanguageManager.getInstance().getValue("widget_last_event"));
                contenInfo.setText(LanguageManager.getInstance().getValue("widget_last_event_content"));
                avaModel.setVisibility(View.GONE);
                item.setBackground(ContextCompat.getDrawable(context, R.drawable.black_conner));
                txt_number.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                startAt.setVisibility(View.VISIBLE);
                startAt.setText(Tool.getStringWithoutFirstEle(widgetItem.getContent(), "\\|", " - "));
                imgLastEvent.setVisibility(View.VISIBLE);
                if(widgetItem.getIndetify().equals(ApplicationService.customerrecognize)){
                    if(ApplicationService.countWidgetFari%2==0){
                        Glide.with(context)
                                .load(widgetItem.getFirstImg())
                                .apply(requestOptions)
                                .into(imgLastEvent);
                    }else{
                        Glide.with(context)
                                .load(widgetItem.getSecondImg())
                                .apply(requestOptions)
                                .into(imgLastEvent);
                    }
                }
            }
        }
        container.addView(view);
        return view;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private String getTimeLocal(long time){
        String pattern = "HH:mm:ss - dd/MM/yyyy ";
        DateFormat df = new SimpleDateFormat(pattern);
        Timestamp stamp = new Timestamp(time);
        Date date = new Date(stamp.getTime());
        return df.format(date);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
