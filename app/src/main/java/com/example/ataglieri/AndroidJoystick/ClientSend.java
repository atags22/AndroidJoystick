package com.example.ataglieri.AndroidJoystick;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by alex on 2/5/18.
 */
public class ClientSend implements Runnable {

    int port = 44444;
    //String ip = "130.215.13.23";
    String ip = "192.168.4.1";
    //String ip = "0.0.0.0";
    //String ip = "127.0.0.1";

    int powerR = 90; //0 to 180, with 90 stopped
    int powerL = 90;
    boolean sendRight = false;


    Context context;

    public ClientSend(Context ctx){
        this.context = ctx;
    }

    @Override
    public void run() {
        boolean run = true;
        try {
            DatagramSocket udpSocket = new DatagramSocket(port);
            InetAddress serverAddr = InetAddress.getByName(ip);
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String name = wifiInfo.getSSID();
            if (name == "esp32tag") {
                Log.d("alex", name);
                while (run) {

                    if (sendRight) {
                        byte[] buf = ("R: [" + powerR + "]").getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
                        udpSocket.send(packet);
                        sendRight = false;
                    } else { //sendLeft
                        byte[] buf = ("L: [" + powerL + "]").getBytes();
                        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, port);
                        udpSocket.send(packet);
                        sendRight = true;
                    }
                    Thread.sleep(5);
                }
            }
            } catch(SocketException e){
                Log.e("Udp:", "Socket Error:", e);
                run = false;
            } catch(IOException e){
                Log.e("Udp Send:", "IO Error:", e);
                run = false;
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

    public void tickR(int power){
        this.powerR = power;
        Log.d("Alex","powerR = " + this.powerR);
        return;
    }
    public void tickL(int power){
        this.powerL = power;
        return;
    }
}