package com.bkav.aiotcloud.screen.home.camera.setting;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;

public class FocusFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.focus_layout, container, false);
        init(view);
        action();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private RelativeLayout in;
    private RelativeLayout out;

    private void init(View view){
        this.in = view.findViewById(R.id.in);
        this.out = view.findViewById(R.id.out);
    }

    private void action() {
        this.in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.what = ApplicationService.SETTING_FOCUS;
                message.obj = "Incandescent";
                ApplicationService.mainHandler.sendMessage(message);

            }
        });

        this.in.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    sendFocus(-1);
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    sendFocus(0);
                }
                return true;
            }
        });

        this.out.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    sendFocus(1);
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    sendFocus(0);
                }
                return true;
            }
        });

    }

    private void sendFocus(int remote){
        Message message = new Message();
        message.what = ApplicationService.SETTING_FOCUS;
        message.obj = remote;
        ApplicationService.mainHandler.sendMessage(message);
    }
}
