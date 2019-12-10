package com.example.PuyoPuzzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class GameMainActivity extends AppCompatActivity {
    ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        Handler hd = new Handler();
        hd.postDelayed(new splash(), 3000);

        next = (ImageView)findViewById(R.id.next);
        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent gotoNext = new Intent(getApplication(), MainActivity.class);
               startActivity(gotoNext);
            }
        });
    }

    private class splash implements Runnable{
        public void run(){
            next.setVisibility(View.VISIBLE);
        }
    }
}
