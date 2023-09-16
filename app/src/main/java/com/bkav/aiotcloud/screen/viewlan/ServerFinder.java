package com.bkav.aiotcloud.screen.viewlan;

import android.annotation.SuppressLint;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bkav.aiotcloud.application.ApplicationService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ServerFinder extends Thread {
    private static final String TAG = "ServerFinder";
    InetAddress broadcast;
    int port;
    private boolean running;
    ViewOnLan.MainHandler handler;
    DatagramSocket socket;
    InetAddress address;
    String dataUDP;
    public static final int MAX_COUNT = 5;
    private ReceiveThread receiveThread;
    private final android.os.Handler handlerCore;

    @SuppressLint("HandlerLeak")
    public ServerFinder(ViewOnLan.MainHandler handler, String data) {
        super();
        setDaemon(true);
        this.port = 3702;
        dataUDP = data;
        this.handler = handler;
        this.handlerCore = new android.os.Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==ApplicationService.GET_UDP_DATA_SUCCESS){
                    DatagramPacket recivePackage = (DatagramPacket) msg.obj;
                    if (recivePackage == null) {
                        return;
                    }
                    processData(recivePackage);
                }
            }
        };
    }

    private void sendState(int state){
        ApplicationService.mainHandler.sendEmptyMessage(state);
    }
    private byte[] data = new byte[128];
    private DatagramPacket sendPacket;
    @Override
    public void run() {
        sendState(ApplicationService.CONNECT_CLIENT_CONNECTING);
        running = true;
        try {
            getBroadcastAddress();


            int count = 0;
            while (!Thread.interrupted()) {
                // check 5 lan
                try {
                    if (this.socket == null) {
                        this.socket = new DatagramSocket();
                        this.socket.setBroadcast(true);
                        this.socket.setSoTimeout(30 * 1000);
                        if (this.receiveThread == null) {
                            this.receiveThread = new ReceiveThread(this.socket, this.handler);
                            this.receiveThread.start();
                        }
                    }
                    byte[] data = dataUDP.getBytes();
                    sendPacket = new DatagramPacket(dataUDP.getBytes(StandardCharsets.UTF_8), dataUDP.getBytes(StandardCharsets.UTF_8).length, broadcast, port);
                    this.socket.send(sendPacket);
//                    sendPacket = new DatagramPacket(data, data.length);
//                    Arrays.fill(this.data, (byte) 0);
//                    socket.receive(sendPacket);
//                    String line = new String(sendPacket.getData(), 0, sendPacket.getLength());
//                    Log.e("receive: ", line);
                    Thread.sleep(15000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            this.close();
//            if (ApplicationService.UDP_DATA != null) {
//                Log.e("Loading", "SUCCESS_FIND_SERVER: " );
//                handler.sendEmptyMessage(ApplicationService.CONNECT_CLIENT_SUCCESS);
//            } else {
//                Log.e("Loading", "NO_FIND_SERVER: " );
//                handler.sendEmptyMessage(ApplicationService.CONNECT_CLIENT_FAIL);
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void close() {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
        }
        if (receiveThread != null) {
            receiveThread.interrupt();
        }
        this.socket = null;
    }

    public void stopFinder() {
        this.close();
        this.interrupt();
    }
    private void processData(DatagramPacket recivePackage) {
        String data = new String(recivePackage.getData());
        Log.e("ServerFinder", data);
        ApplicationService.UDP_DATA = data;
//        ApplicationService.isComplete = true;
    }
    private void getBroadcastAddress() {
        try {
            this.broadcast = InetAddress.getByName("239.255.255.250");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
