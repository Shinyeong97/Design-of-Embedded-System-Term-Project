package com.example.PuyoPuzzle;

import android.app.Application;

import java.net.Socket;
import java.util.ArrayList;

public class MyApplication extends Application
{
    private ArrayList<Socket> sockets = new ArrayList<>();
    private int currentMember;

    public ArrayList<Socket> getSockets() {
        return sockets;
    }

    public void setSockets(ArrayList<Socket> sockets) {
        this.sockets = sockets;
    }

    public int getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(int currentMember) {
        this.currentMember = currentMember;
    }
}