package com.example.PuyoPuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

public class RoomActivity extends AppCompatActivity implements Serializable {
    private  static int PORT = 58082;

    private Thread mServerThread = null;
    private TextView mTVMember;
    private TextView mTVAddress;
    private TextView mTVPort;
    private int member;
    private boolean  isFull = false;
    private ArrayList<Socket> sockets = new ArrayList<>();


    private Handler handler;
    private AppCompatActivity currentActivity;


    private ServerSocket mServerSocket;
    private int currentMember = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        mTVMember = (TextView) findViewById(R.id.member);
//        findViewById(R.id.btnStopServer).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                stopServer();
//            }
//        });

        Log.v("KKT", "RoomActivity onCreate()");
        Intent intent1 = getIntent();
        member = intent1.getExtras().getInt("member");

        mTVAddress = (TextView) findViewById(R.id.tvAddress);
        mTVPort = (TextView)findViewById(R.id.tvPort);


//        currentActivity = this;
//        handler = new Handler() {
//            //Looper는 메세지큐에 메세지가 있으면 그 메세지를 꺼내서 핸들러에게
//            // 전달하며 핸덜리의 아래 콜백 메소드를 호출하게 된다.
//            @Override
//            public void handleMessage(Message msg) {
//
//
//                //전달 받은 Message 오브젝트를 가지고 관련된 작업을 수행 할 수 있다.
//                // 예를 들어 textView의 텍스트를 업데이트 한다든가...
//                // 이번예제 에서는 msg의 arg1에 1을 넣었음으로
//                // msg.arg1을 통하여 숫자 1을 얻을 수 있다.
//                finish();
//                Intent intent2 = new Intent(currentActivity, MainActivity.class);
//                intent2.putExtra("sockets", sockets);
//                intent2.putExtra("member", member);
//                startActivity(intent2);
//            }
//        };




        startServer();



//        Log.v("KKT", "아직 액티비티 바꾸기 전");
//        Log.v("KKT", "isFull : " + isFull);
//        if(isFull){
//            Log.v("KKT", "아직 액티비티 바꾼다");
//            Log.v("KKT", "isFull : " + isFull);
//
//            Intent intent2 = new Intent(this, MainActivity.class);
//            intent2.putExtra("sockets", sockets);
//            intent2.putExtra("member", member);
//        }
//
//        Log.v("KKT", "이거 실행 될까?");
//        Log.v("KKT", "isFull : " + isFull);
    }

    private void startServer(){
        if(mServerThread == null){
            Log.v("KKT", "startServer() before SimpleServerThread()");
            mServerThread = new SimpleServerThread(){
                @Override
                public void run(){
                    Log.v("KKT", "SimpleServerThread run()"+ PORT);
                    try{
                        mServerSocket = new ServerSocket(PORT);
                        while(true){

                            mTVMember.setText((member-currentMember) + "명 대기중");

                            if(currentMember >= member)
                                break;

                            Log.v("KKT", "SimpleServerThread run() before accept");

                            Socket connection = mServerSocket.accept();
                            sockets.add(connection);

                            currentMember = currentMember + 1;

                            Log.v("KKT", "SimpleServerThread run() after accept");
                        }
                        MyApplication myApp = (MyApplication) getApplication();
                        myApp.setSockets(sockets);
                        myApp.setCurrentMember(member);
//            Message message = Message.obtain();
//            message.arg1 = 1;
//            handler.sendMessage(message);


            /*
            for(int i = 0; i<currentMember-1; i++){
                final PrintWriter out = new PrintWriter(sockets.get(i).getOutputStream(), true);
                out.write("1" + "\n");
                out.flush();
            }
//            */
//            Log.v("KKT", "success");
//            priorActivity.setFull(true);


                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    finish();
                    Intent intent = new Intent(RoomActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            };
            Log.v("KKT", "startServer() after SimpleServerThread()");
            mServerThread.start();
        }
        Log.v("KKT", "setLocalIPAddress()");
        setLocalIPAddress();
    }

//    private void stopServer(){
//        try{
//            if(mServerThread != null){
//                ((SimpleServerThread) mServerThread).closeServer();
//            }
//            mServerThread = null;
//        }catch(IOException e){
//            e.printStackTrace();
//        }finally{
//            mTVAddress.setText(String.format(Locale.US, "%s", "STOPPED"));
//        }
//    }

    private void setLocalIPAddress(){
        Log.v("KKT", "RoomActivity setLocalIPAddress()");
        new Thread(){
            public void run(){
                Socket socket = null;
                try{
                    socket = new Socket("192.168.0.1", 80);
                    Log.v("KKT", "RoomActivity setLocalIPAddress() socket 생성 성공");
                    final Socket finalSocket = socket;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTVAddress.setText(String.format(Locale.US, "IP: %s", finalSocket.getLocalAddress()));
                            Log.v("KKT", finalSocket.getLocalAddress().toString());
                        }
                    });
                }catch (UnknownHostException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }.start();
        mTVPort.setText(String.format(Locale.US, "PORT: %d", PORT));
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
