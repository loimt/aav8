package com.bkav.aiotcloud.screen.notify.detail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bkav.aiotcloud.R;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import me.relex.circleindicator.CircleIndicator;

public class ImagesInEvent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.images_of_day);
        String path = getIntent().getStringExtra("path");
        try {
             listPath = new  JSONArray(path);
            Log.e("listpart ",  listPath.length() + "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        init();
        action();
    }

    private  JSONArray listPath;

    private void init(){
        ViewPager vpPager =  findViewById(R.id.pager);
        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), listPath);
        vpPager.setAdapter(imageAdapter);
        CircleIndicator circleIndicator = findViewById(R.id.circleIndicator);
        circleIndicator.setViewPager(vpPager);
        imageAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        ImageView close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (listPath.length() == 1){
            circleIndicator.setVisibility(View.GONE);
        }

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
//        tabLayout.setupWithViewPager(vpPager, true);
    }

    private void action(){

    }

    public static class ImageAdapter extends PagerAdapter {
        private Context mContext;
        LayoutInflater mLayoutInflater;
        private JSONArray mResources;

        public ImageAdapter(Context context, JSONArray resources) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResources = resources;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = mLayoutInflater.inflate(R.layout.image_item,container,false);
            ImageView imageView = itemView.findViewById(R.id.img);
            try {
//                imageView.setImageResource(mResources.getString(position));
                if (!mResources.getJSONObject(position).isNull("path")){
                    Glide.with(mContext)
                            .load(mResources.getJSONObject(position).getString("path")).into(imageView);
                    Log.e("listpart", mResources.getString(position));
                } else  if (!mResources.getJSONObject(position).isNull("filePath")){
                    Glide.with(mContext)
                            .load(mResources.getJSONObject(position).getString("filePath")).into(imageView);
                    Log.e("listpart", mResources.getString(position));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mResources.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
