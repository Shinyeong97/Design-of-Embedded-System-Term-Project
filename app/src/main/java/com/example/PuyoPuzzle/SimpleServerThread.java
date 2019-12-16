package com.example.PuyoPuzzle;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleServerThread extends Thread {
//    private static int GIVEN_PORT;
//    private ServerSocket mServerSocket;
//    private int maxMember;
//    private int currentMember = 1;
//    private TextView mTVMember;
//    private ArrayList<Socket> sockets;
//    private RoomActivity priorActivity;
//
//
////    private Handler handler;
//
//    public SimpleServerThread(int port, int member, TextView mTVMember, ArrayList<Socket> sockets, RoomActivity priorActivity){
//        Log.v("KKT", "SimpleServerThread constructor() : port " + port + ' ' + member);
//        GIVEN_PORT = port;
//        maxMember = member;
//        this.mTVMember = mTVMember;
//        this.sockets = sockets;
//        this.priorActivity = priorActivity;
//
////        this.handler = handler;
//    }

    @Override
    public void run(){
//        Log.v("KKT", "SimpleServerThread run()"+ GIVEN_PORT);
//        try{
//            mServerSocket = new ServerSocket(GIVEN_PORT);
//            while(true){
//
//                mTVMember.setText((maxMember-currentMember) + "명 대기중");
//
//                if(currentMember >= maxMember)
//                    break;
//
//                Log.v("KKT", "SimpleServerThread run() before accept");
//
//                Socket connection = mServerSocket.accept();
//                sockets.add(connection);
//
//                currentMember = currentMember + 1;
//
//                Log.v("KKT", "SimpleServerThread run() after accept");
//            }
//
////            Message message = Message.obtain();
////            message.arg1 = 1;
////            handler.sendMessage(message);
//
//
//            /*
//            for(int i = 0; i<currentMember-1; i++){
//                final PrintWriter out = new PrintWriter(sockets.get(i).getOutputStream(), true);
//                out.write("1" + "\n");
//                out.flush();
//            }
////            */
////            Log.v("KKT", "success");
////            priorActivity.setFull(true);
//
//
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
////        priorActivity.finish();
////        Intent intent = new Intent(priorActivity, MainActivity.class);
////        intent.putExtra("sockets", sockets);
////        intent.putExtra("member", currentMember);
////        priorActivity.startActivity(intent);
    }

//    public void closeServer() throws IOException {
//        if (mServerSocket != null && !mServerSocket.isClosed()) {
//            mServerSocket.close();
//        }
//        mServerSocket = null;
//    }
}
