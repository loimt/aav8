package com.bkav.aiotcloud.screen.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bkav.aiotcloud.R;
import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.widget.WidgetItem;
import com.bkav.aiotcloud.language.LanguageManager;
import com.bkav.aiotcloud.main.SharePref;
import com.bkav.aiotcloud.screen.widget.adapter.DragRecycleViewAdapter;
import com.bkav.aiotcloud.screen.widget.helper.MyItemTouchHelperCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class WidgetFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View frag = inflater.inflate(R.layout.third_frag, container, false);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ApplicationService.isUpdateWidget = true;
//        initWidget();
        init(view);
        setData();
        action();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {

        Log.e("widgetF", "onresum");
        ApplicationService.statisticThread.getStatisticEvent();
        ApplicationService.requestManager.getLastEvent();
        if (socket != null){
            connectSocket();
        }
//        getStateWidget();
        if (this.add.getVisibility() == View.VISIBLE) {
            for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
                widgetItem.setDragAble(true);
            }
        }
        this.dragAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("widgetF", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("widgetF", "onDestroyView");
        if (socket != null){
            disconectSocket();
        }
    }



    public void viewButton() {
        this.add.setVisibility(View.VISIBLE);
        this.done.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        this.add.setVisibility(View.GONE);
        this.done.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUI() {
        dragAdapter.notifyDataSetChanged();
    }

    private void setData() {
        this.addImage.setColorFilter(getContext().getResources().getColor(R.color.white));
        this.gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                WidgetItem widgetItem = ApplicationService.listWidgetView.get(position);
                if (widgetItem.isSpanned()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(dragAdapter);
        this.itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void action() {
        this.editWidgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
                    widgetItem.setDragAble(true);
                }
                dragAdapter.notifyDataSetChanged();
                dragAdapter.isClickable = false;
                viewButton();
            }
        });
        this.done.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
                    widgetItem.setDragAble(false);
                }
                dragAdapter.notifyDataSetChanged();
                dragAdapter.isClickable = true;
                hideButton();
            }
        });
        this.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWidgetFeature.class);
                startActivity(intent);
            }
        });
        this.dragAdapter.setLongClick((view, position) -> {
            for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
                widgetItem.setDragAble(true);
            }
            dragAdapter.notifyDataSetChanged();
            dragAdapter.isClickable = false;
            viewButton();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
            widgetItem.setDragAble(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (WidgetItem widgetItem : ApplicationService.listWidgetView) {
            widgetItem.setDragAble(false);
        }
        ApplicationService.isUpdateWidget = false;
    }

    private void init(View view) {
        this.editWidgetBtn = view.findViewById(R.id.editWidgetBtn);
        TextView txEdit = view.findViewById(R.id.txEdit);
        TextView txDone = view.findViewById(R.id.txDone);

        txEdit.setText(LanguageManager.getInstance().getValue("edit"));
        txDone.setText(LanguageManager.getInstance().getValue("done"));
        this.addImage = view.findViewById(R.id.addImage);
        this.add = view.findViewById(R.id.add);
        this.done = view.findViewById(R.id.done);
        this.recyclerView = view.findViewById(R.id.recycler_view);
        this.gridLayoutManager = new GridLayoutManager(getContext(), 2);
        this.dragAdapter = new DragRecycleViewAdapter(getContext(), ApplicationService.listWidgetView, viewHolder -> {
//            itemTouchHelper.startDrag(viewHolder);
        });
        this.callback = new MyItemTouchHelperCallBack(dragAdapter);
        this.itemTouchHelper = new ItemTouchHelper(callback);
    }
    private ImageView addImage;
    private static final String TAG = "DouwnloadFragment";
    private RelativeLayout add, done, editWidgetBtn;
    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;
    private DragRecycleViewAdapter dragAdapter;
    private GridLayoutManager gridLayoutManager;
    private ItemTouchHelper.Callback callback;
    public static final String TYPE = "type";
    public static final String MODELNAME = "modelname";
    public static final String typeWidgetKey = "typeWidgetFeature";
    public static final String addWidgetFeatureKey = "AddWidgetFeature";

    private void connectSocket() {
        socket.disconnect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String content = "{"+"\"tokenLogin\":\""+"Bearer "+ApplicationService.TOKEN +"\"" +"}";
                socket.emit("authenticate", content);
            }
        });
        socket.on("request", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String content = "{"+"\"tokenLogin\":\""+"Bearer "+ApplicationService.TOKEN +"\"" +"}";
                socket.emit("authenticate", content);
            }
        });
        socket.on("notify", onMessageReceived);
        socket.connect();
    }
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try{
                JSONObject jsonObject = new JSONObject(args[0].toString());
                JSONObject data = jsonObject.getJSONObject("data");
                String objGuidEvent =  data.getString("ObjGuidEvent") ;
                String img = "";
                String avaImg = "";
                String timeCreate = "";
                String content = "";
                String identity = jsonObject.getString("identity");
                if(identity.equals(ApplicationService.customerrecognize)){
                    ApplicationService.countWidgetFari +=1;
                    avaImg = data.getString("FilePath");
                    img = new JSONObject(data.getJSONArray("ListImage").get(0).toString()).getString("FilePath");
                    content = data.getString("Content");
                    timeCreate = data.getString("DateOfDetected");
                }else if(identity.equals(ApplicationService.licenseplate)){
                    ApplicationService.countWidgetLincen +=1;
                    avaImg = data.getString("cropImage");
                    img = data.getString("fullImage");
                    content = data.getString("noPlate");
                    timeCreate = data.getString("createAt");
                }else if(identity.equals(ApplicationService.accessdetect)){
                    img = new JSONObject(data.getJSONArray("ListImage").get(0).toString()).getString("FilePath");
                    content = data.getString("Content");
                    timeCreate = data.getString("DateOfDetected");
                }
                setLastEvent(img, timeCreate, content, identity, avaImg, objGuidEvent);
                ApplicationService.mainHandler.sendEmptyMessage(ApplicationService.GET_EVENT_STATISTIC_SUCESS);
//                if (dragAdapter != null){
//                    dragAdapter.notifyDataSetChanged();
//                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    private void setLastEvent(String imagePath, String startAt, String content, String identity, String avaImg, String objectGuid){
        switch (identity) {
            case ApplicationService.customerrecognize:
                ApplicationService.fariWidget.setContent(content);
                ApplicationService.fariWidget.setStartAt(startAt);
                ApplicationService.fariWidget.setImagePath(imagePath);
                ApplicationService.fariWidget.setObjectGuide(objectGuid);
                if(ApplicationService.countWidgetFari%2==0){
                    ApplicationService.fariWidget.setFirstImg(avaImg);

                }else{
                    ApplicationService.fariWidget.setSecondImg(avaImg);
                }
                break;
            case ApplicationService.accessdetect:
                ApplicationService.intrudWidget.setContent(content);
                ApplicationService.intrudWidget.setStartAt(startAt);
                ApplicationService.intrudWidget.setFirstImg(imagePath);
                ApplicationService.intrudWidget.setImagePath(avaImg);
                ApplicationService.intrudWidget.setObjectGuide(objectGuid);
                break;
            case ApplicationService.firedetect:
                ApplicationService.fideWidget.setContent(content);
                ApplicationService.fideWidget.setStartAt(startAt);
                ApplicationService.fideWidget.setFirstImg(imagePath);
                ApplicationService.fideWidget.setObjectGuide(objectGuid);
                break;
            case ApplicationService.persondetection:
                ApplicationService.pedeWidget.setContent(content);
                ApplicationService.fariWidget.setStartAt(startAt);
                ApplicationService.fariWidget.setFirstImg(imagePath);
                ApplicationService.fariWidget.setObjectGuide(objectGuid);
                break;
            case ApplicationService.licenseplate:
                ApplicationService.licenWidget.setContent(content);
                ApplicationService.licenWidget.setStartAt(startAt);
                ApplicationService.licenWidget.setImagePath(imagePath);
                ApplicationService.licenWidget.setObjectGuide(objectGuid);
                if(ApplicationService.countWidgetLincen%2==0){
                    ApplicationService.licenWidget.setFirstImg(avaImg);
                }else{
                    ApplicationService.licenWidget.setSecondImg(avaImg);
                }
                break;

        }
        for(WidgetItem widget : ApplicationService.listWidgetView){
            if(widget.getIndetify().equals(identity)){
                widget.setContent(content);
                widget.setStartAt(startAt);
                widget.setImagePath(imagePath);
                if(identity.equals(ApplicationService.customerrecognize)){
                    if(ApplicationService.countWidgetFari%2==0){
                        widget.setFirstImg(avaImg);
                        widget.setFirstContent(content);
                    }else{
                        widget.setSecondImg(avaImg);
                        widget.setSecondContent(content);
                    }
                }else if(identity.equals(ApplicationService.licenseplate)){
                    if(ApplicationService.countWidgetLincen%2==0){
                        widget.setFirstImg(avaImg);
                        widget.setFirstContent(content);

                    }else{
                        widget.setSecondImg(avaImg);
                        widget.setSecondContent(content);

                    }
                }
            }
        }
    }

//    private void initWidget(){
//        ApplicationService.fariWidget = new WidgetItem(ApplicationService.customerrecognize, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.intrudWidget = new WidgetItem(ApplicationService.accessdetect, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.fideWidget = new WidgetItem(ApplicationService.firedetect, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.pedeWidget = new WidgetItem(ApplicationService.persondetection, 0, "", "", "0",false, 0, false, "", "", "", "");
//        ApplicationService.licenWidget = new WidgetItem(ApplicationService.licenseplate, 0, "", "", "0",false, 0, false, "", "", "", "");
//        if(ApplicationService.listWidget.size() == 0){
//            ApplicationService.listWidget.addAll(Arrays.asList(ApplicationService.fariWidget, ApplicationService.intrudWidget, ApplicationService.licenWidget));
//        }
//    }
//    private void getStateWidget(){
//        String widgetState = SharePref.getInstance(getContext()).getWidgetState().trim();
//        String[] arrayWidgetState = widgetState.split(" ");
//        if(!widgetState.equals("")){
//            for (String s : arrayWidgetState) {
//                String identity = s.split("_")[0];
//                int viewType = Integer.parseInt(s.split("_")[1]);
//                boolean spanned = viewType == 1 || viewType == 3;
//                WidgetItem widgetItem = new WidgetItem(identity, 0, "", "", "0", false, viewType, spanned, "", "", "", "");
//                ApplicationService.listWidgetView.add(widgetItem);
//            }
//        }
//    }

    private void disconectSocket(){
        socket.disconnect();
        socket.off("notify", onMessageReceived);
        socket.off("notify");
        socket.close();
        socket=null;
    }

    public Socket socket;

    {
        try {
            socket = IO.socket(ApplicationService.URL_SOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
