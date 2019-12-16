package com.example.PuyoPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class GameMainActivity extends AppCompatActivity {

    Thread mClientThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        findViewById(R.id.play_1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSinglePlay();
            }
        });

        findViewById(R.id.play_2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startMultiPlay(2);
            }
        });

        findViewById(R.id.play_3).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startMultiPlay(3);
            }
        });

        findViewById(R.id.play_4).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startMultiPlay(4);
            }
        });

        findViewById(R.id.participation_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String addr = ((EditText)findViewById(R.id.etAddress)).getText().toString();
                int port = Integer.parseInt(((EditText)findViewById(R.id.etPort)).getText().toString());

                Log.v("CLIENT ACTIVITY", "start client" + addr + ":" + port);
                mClientThread = new SimpleClientThread(addr, port);
                mClientThread.start();
            }
        });

        findViewById(R.id.btnStopClient).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stopClient();
            }
        });
    }

    private void startMultiPlay(int member) {
        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("member", member);
        startActivity(intent);
    }

    private void startSinglePlay() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("member", 1);
        startActivity(intent);
    }

    private void stopClient(){
        if(mClientThread != null){
            try{
                ((SimpleClientThread)mClientThread).closeConnection();
            }catch(IOException e){
                e.printStackTrace();
            }
            mClientThread = null;
        }
    }
}
