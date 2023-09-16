package com.bkav.aiotcloud.screen.home.device;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.screen.home.camera.CameraActivity;
import com.bkav.aiotcloud.screen.setting.face.ListDayAdapter;
import com.bkav.aiotcloud.view.RecyclerViewMargin;

public class DeviceFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);
        init(view);
        setData();
        action();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUI(){
        if (deviceAdapter != null){
            this.deviceAdapter.notifyDataSetChanged();
            this.pullRefreshLayout.setRefreshing(false);
        }
    }


    private void init(View view){
        this.pullRefreshLayout = view.findViewById(R.id.pullRefresh);
        this.listCamera = view.findViewById(R.id.gridview);
        this.deviceAdapter = new DeviceAdapter(getActivity(), ApplicationService.deviceItems);
        this.decoration = new RecyclerViewMargin(30, 2);
        this.listCamera.setAdapter(this.deviceAdapter);
        this.listCamera.setLayoutManager(new GridLayoutManager(getContext(), 2));
        this.listCamera.addItemDecoration(this.decoration);
    }
    private void setData(){
        this.pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        this.listCamera.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    currentPageIndex++;
                    ApplicationService.requestManager.getListCamLoadmore(ApplicationService.URL_GET_LIST_CAM, null, null, currentPageIndex, 5);
                }
            }
        });
    }
    private void action(){
        this.deviceAdapter.setClickListener(new ListDayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (ApplicationService.cameraitems.get(position).getIsConnected() == 0){
//                    ApplicationService.showToast(LanguageManager.getInstance().getValue("camera_off_line"), true);
//                    return;
//                }
                Intent intent = new Intent(getContext(), DeviceActivity.class);
                intent.putExtra("ID", ApplicationService.deviceItems.get(position).getCameraId());
                startActivity(intent);
            }
        });
        this.pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApplicationService.requestManager.getListCam(ApplicationService.URL_GET_LIST_CAM, null, null, 1, 10, null);
            }
        });
    }
    private DeviceAdapter deviceAdapter;
    private RecyclerView listCamera;
    private int currentPageIndex = 1;
    private RecyclerViewMargin decoration;
    private PullRefreshLayout pullRefreshLayout;
}


