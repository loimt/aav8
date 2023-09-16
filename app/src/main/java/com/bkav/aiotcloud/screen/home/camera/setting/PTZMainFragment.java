package com.bkav.aiotcloud.screen.home.camera.setting;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.CameraItem;
import com.bkav.aiotcloud.language.LanguageManager;

public class PTZMainFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        this.roomItem = HomeDataService.getInstance().getCurrentRoom();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       int id = requireArguments().getInt("ID");
        for (CameraItem cameraItem : ApplicationService.cameraitems) {
            if (cameraItem.getCameraId() == id) {
                this.cameraItem = cameraItem;
            }
        }

        if (cameraItem.isPtzDevice()) {
            ptzLayout.setVisibility(View.VISIBLE);
        } else {
            ptzLayout.setVisibility(View.GONE);
        }

        if (cameraItem.isZoom()) {
            zoomLayout.setVisibility(View.VISIBLE);
        } else {
            zoomLayout.setVisibility(View.GONE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ptz_fragment, container, false);
        assert this.getArguments() != null;
        init(view);
        action();
        return view;
    }
    private String TAG = "PTZFragment";
//    private TextView title;
    private CameraItem cameraItem;
    private ImageView zoomIn;
    private ImageView zoomOut;
    private RelativeLayout ptz1;
    private RelativeLayout ptz2;
    private RelativeLayout ptz3;
    private RelativeLayout ptz4;
    private RelativeLayout ptz5;
    private RelativeLayout ptz6;
    private RelativeLayout ptz7;
    private RelativeLayout ptz8;
    private LinearLayout ptzLayout;
    private RelativeLayout zoomLayout;


    private void init(View view) {
//        this.title = view.findViewById(R.id.title);
//        this.title.setText(LanguageManager.getInstance().getValue("title_ptz"));
        this.ptz1 = view.findViewById(R.id.ptz1);
        this.ptz2 = view.findViewById(R.id.ptz2);
        this.ptz3 = view.findViewById(R.id.ptz3);
        this.ptz4 = view.findViewById(R.id.ptz4);
        this.ptz5 = view.findViewById(R.id.ptz5);
        this.ptz6 = view.findViewById(R.id.ptz6);
        this.ptz7 = view.findViewById(R.id.ptz7);
        this.ptz8 = view.findViewById(R.id.ptz8);
        this.ptzLayout = view.findViewById(R.id.ptzLayout);
        this.zoomLayout = view.findViewById(R.id.zoomLayout);

        this.zoomIn = view.findViewById(R.id.zoomIn);
        this.zoomOut = view.findViewById(R.id.zoomOut);
    }

    private void action() {
        this.ptz1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("left_up");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("up");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("right_up");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("left");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("right");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("left_down");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("down");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.ptz8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendControl("right_down");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendControl("stop");
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.zoomOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendZoom(1);
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendZoom(0);
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });

        this.zoomIn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.e("TouchTest", motionEvent.getAction() + "");
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
//                    Log.e("TouchTest", cameraItem.getPeerID());
                    sendZoom(-1);
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "down" );
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
//                    Log.e("TouchTest", "Touch up");
                    sendZoom(0);
//                    ApplicationService.requestManager.control("put_ptz_continue", cameraItem.getPeerID(), "stop" );
                }
                return true;
            }
        });




    }

    private void sendZoom(int remote){
        Message message = new Message();
        message.what = ApplicationService.ZOOM_PTZ;
        message.obj = remote;
        ApplicationService.mainHandler.sendMessage(message);
    }

    private void sendControl(String remote){
        Message message = new Message();
        message.what = ApplicationService.CONTROL_PTZ;
        message.obj = remote;
        ApplicationService.mainHandler.sendMessage(message);
    }
}

