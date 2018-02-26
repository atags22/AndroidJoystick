package com.example.ataglieri.AndroidJoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener{

    Thread udpConnect;
    ClientSend dataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_main);

        dataHolder = new ClientSend(this);
        udpConnect = new Thread(dataHolder);
        udpConnect.start();

    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch (id)
        {
            case R.id.joystickRight:
                Log.d("Right Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                if(yPercent != 0) { //hacky solution to dumb problem
                    dataHolder.tickR(floatToMotor(yPercent));
                }
                break;

            case R.id.joystickLeft:
                Log.d("Left Joystick", "X percent: " + xPercent + " Y percent: " + yPercent);
                if (yPercent != 0) {//hacky solution to dumb problem
                    dataHolder.tickL(floatToMotor(yPercent));
                }
                break;
        }
    }
    int floatToMotor(float pwr){
        int toReturn = 180-((int)(pwr * 90) + 90);
        return toReturn;
    }

}
