package com.bkav.aiotcloud.screen.viewlan;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bkav.aiotcloud.application.ApplicationService;
import com.bkav.aiotcloud.entity.aiobject.LanDevice;
import com.bkav.aiotcloud.screen.MainScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ReceiveThread extends Thread{
    private final DatagramSocket socket;
    private final Handler handler;
    private final byte[] data;
    private final DatagramPacket recivePackage;
    public ReceiveThread(DatagramSocket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
        this.data = new byte[512];
        this.recivePackage = new DatagramPacket(this.data, this.data.length);
    }
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {

                this.socket.setSoTimeout(30 * 1000);
                Arrays.fill(this.data, (byte) 0);
                this.socket.receive(this.recivePackage);
                Message message = new Message();
                message.what = ApplicationService.GET_UDP_DATA_SUCCESS;
                message.obj = this.recivePackage;
                String line = new String(recivePackage.getData(), 0, recivePackage.getLength());
                JSONObject deviceJson = new JSONObject(line);
                JSONObject envelope = new JSONObject(deviceJson.getString("Envelope"));
                JSONObject body = new JSONObject(envelope.getString("Body"));
                JSONObject probeMatches = new JSONObject(body.getString("ProbeMatches"));
                JSONObject probeMatch = new JSONObject(probeMatches.getString("ProbeMatch"));
                LanDevice lanDevice = new LanDevice(probeMatch);
                if(ApplicationService.listLanDevices.size() == 0){
                    ApplicationService.listLanDevices.add(lanDevice);
                } else {
                    if(ApplicationService.listLanDevices.stream().filter(lanDevice1 -> lanDevice.getSerialNumber().equals(lanDevice1.getSerialNumber())).findAny().orElse(null) == null){
                        ApplicationService.listLanDevices.add(lanDevice);
                    }
                }
                Log.e("Receive", "run: " + ApplicationService.listLanDevices.get(0).getModel());
                this.handler.sendMessage(message);
                Thread.sleep(1000);
            } catch (IOException | NullPointerException | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
