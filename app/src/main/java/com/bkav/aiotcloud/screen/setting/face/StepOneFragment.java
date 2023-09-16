package com.bkav.aiotcloud.screen.setting.face;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.CameraUnConfigItem;
import com.bkav.aiotcloud.view.CustomEditText;
import com.bkav.aiotcloud.view.GridviewCustom;

public class StepOneFragment  extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.step_one_fragment, container, false);
        init(frag);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public CameraUnConfigItem getCurrentSelected() {
        return ApplicationService.cameraUnConfigItems.get(currentSelected);
    }

    private CameraUnconfigAdapter cameraUnconfigAdapter;
    private GridviewCustom listCamera;
    private CustomEditText searchTx;
    private int currentSelected = -1;
    private boolean isCheck = false;

    private void init(View view){
        this.listCamera = view.findViewById(R.id.gridview);
        this.cameraUnconfigAdapter = new CameraUnconfigAdapter(getContext(), ApplicationService.cameraUnConfigItems);
        this.listCamera.setAdapter(this.cameraUnconfigAdapter);
//        this.searchTx = view.findViewById(R.id.searchEditText);

        this.listCamera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isCheck = true;
                cameraUnconfigAdapter.updateSelect(position);
                cameraUnconfigAdapter.notifyDataSetChanged();
                currentSelected = position;
            }
        });

    }

    public void updateListCamera(){
       cameraUnconfigAdapter.notifyDataSetChanged();
    }

    public int checkStatement(){
        if (!isCheck){
//            Toast yourToast = Toast.makeText(getContext(), "YOURTEXT", Toast.LENGTH_SHORT);
//            yourToast.show();
            return AddOjectFace.NOT_CHECK;
        }
        return  AddOjectFace.SUCCESS;
    }
}