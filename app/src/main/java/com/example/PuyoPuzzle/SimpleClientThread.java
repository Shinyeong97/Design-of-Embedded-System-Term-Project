package com.example.PuyoPuzzle;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Integer.parseInt;

public class SimpleClientThread extends Thread {

    private static String GIVEN_SERVER;
    private static int GIVEN_PORT;
    private static final int TIMEOUT=5000;

    private Socket mSocket;
    private boolean mShouldStop;


    public SimpleClientThread(String addr, int port){
        Log.v("KKT", "SimpleClientThread Constructor()");
        GIVEN_SERVER = addr;
        GIVEN_PORT = port;

        mSocket = null;
        mShouldStop = true;
    }

    @Override
    public void run(){
        try {
            Log.v("KKT", "SimpleClientThread run()");
            Log.d("CLIENT THREAD", "connecting to" + GIVEN_SERVER + ":" + GIVEN_PORT);
            mSocket = new Socket(GIVEN_SERVER, GIVEN_PORT);
            mSocket.setSoTimeout(TIMEOUT);
            Log.v("KKT", "Socket 생성 성공");

//            final BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
//            final PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
//
//            Log.d("CLIENT THREAD", "connected?" + mSocket.isConnected());
//
//            String success;
//            mShouldStop = false;
//            while(true){
//                out.write("imbedded systemmmmmmmmmmm 1" + "\n");
//                out.flush();
//            }
//
//
///*
//            Log.d("CLIENT THEREAD", "read from server");
//            success = in.readLine();
//            Log.d("CLIENT THREAD", "--recv-->" + success);
//
//            int success_i = Integer.parseInt(success);
//            if(success_i == 1){
//
//            }
//*/
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(mSocket != null){
                try{
                    mSocket.close();
                }catch (IOException e){

                }
            }
            mSocket = null;
            mShouldStop = true;
        }
    }

    public void closeConnection() throws IOException{
        mShouldStop = true;
        if(mSocket != null){
            mSocket.close();
        }
        mSocket = null;
    }

}
