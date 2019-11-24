package com.example.test;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    private static Handler mRenderHandler, mStackHandler;
    Queue<Point> queue = new LinkedList<Point>();
    ArrayList<Point> delete = new ArrayList<>();
    int [][] serchTable = new int[15][8];

    // gird setting
    ImageView[][] grid = new ImageView[15][8];
    Integer[][] gridID = {{0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,R.id.imageView,R.id.imageView2,R.id.imageView3, R.id.imageView4,R.id.imageView5,R.id.imageView6,0},
            {0,R.id.imageView7,R.id.imageView8,R.id.imageView9, R.id.imageView10,R.id.imageView11,R.id.imageView12,0},
            {0,R.id.imageView13,R.id.imageView14,R.id.imageView15, R.id.imageView16,R.id.imageView17,R.id.imageView18,0},
            {0,R.id.imageView19,R.id.imageView20,R.id.imageView21, R.id.imageView22,R.id.imageView23,R.id.imageView24,0},
            {0,R.id.imageView25,R.id.imageView26,R.id.imageView27, R.id.imageView28,R.id.imageView29,R.id.imageView30,0},
            {0,R.id.imageView31,R.id.imageView32,R.id.imageView33,R.id.imageView34,R.id.imageView35,R.id.imageView36,0},
            {0,R.id.imageView37, R.id.imageView38,R.id.imageView39,R.id.imageView40,R.id.imageView41,R.id.imageView42,0},
            {0,R.id.imageView43, R.id.imageView44,R.id.imageView45,R.id.imageView46,R.id.imageView47,R.id.imageView48,0},
            {0,R.id.imageView49, R.id.imageView50,R.id.imageView51,R.id.imageView52, R.id.imageView53,R.id.imageView54,0},
            {0,R.id.imageView55, R.id.imageView56,R.id.imageView57,R.id.imageView58, R.id.imageView59,R.id.imageView60,0},
            {0,R.id.imageView61, R.id.imageView62,R.id.imageView63,R.id.imageView64, R.id.imageView65,R.id.imageView66,0},
            {0,R.id.imageView67, R.id.imageView68,R.id.imageView69,R.id.imageView70,R.id.imageView71,R.id.imageView72,0},
            {0,0,0,0,0,0,0,0}};
    int[][] gridState = new int[15][8];
    int[][] shapeState = new int[15][8];

    User user = new User(3,1);   // Single Pattern Object

    //Controll setting
    Button rotateBt,rightBt,leftBt,downBt,resetBt;
    Controller mController;

    static {
//        System.loadLibrary("7segment");
//        System.loadLibrary("lcd");
        System.loadLibrary("pbutton");
        //System.loadLibrary("dotmatrix");
    }

//    public native int SSegmentWrite(int data);
//    public native int LcdWrite(String first, String second);
    public native int PbuttonRead();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.board_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.frame1, new OtherPlayerFrg()).commit();
        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
        fragmentTransaction2.replace(R.id.frame2, new OtherPlayerFrg2()).commit();
        FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
        fragmentTransaction3.replace(R.id.frame3, new OtherPlayerFrg3()).commit();

        //--------------View initialization------------------------------------------
        for(int i=2;i<14;i++)
            for(int j=1;j<7;j++)
                grid[i][j] = (ImageView) findViewById(gridID[i][j]);

        mRenderHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                rendering(false);
            }
        };
        mStackHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                rendering(true);
            }
        };

        //---------- Control initialization ------------------------------------------
        mController = new Controller(this,user);
        rotateBt = (Button)findViewById(R.id.rotate);
        rightBt = (Button)findViewById(R.id.btright);
        leftBt = (Button)findViewById(R.id.btleft);
        downBt = (Button)findViewById(R.id.down);
        rotateBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);
        rightBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);

        resetBt = (Button)findViewById(R.id.button2);   // 임시 리셋버튼
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<15;i++)
                    for(int j=0;j<8;j++) {
                        if(i!=14) {
                            if (j == 0 || j == 7)
                                gridState[i][j] = 7;
                            else
                                gridState[i][j] = 0;
                        }
                        else
                            gridState[i][j] = 7;
                    }
            }
        });

        //--------- Game Main Thread ----------------------------
        GameLoop thread = new GameLoop();
        thread.start();
    }

    private class GameLoop extends Thread {
        public GameLoop() {
            for (int i=0;i<15;i++) // State Initialization
                for(int j=0;j<8;j++) {
                    if(i!=14) {
                        if (j == 0 || j == 7)
                            gridState[i][j] = 7;
                        else
                            gridState[i][j] = 0;
                    }
                    else
                        gridState[i][j] = 7;
                }
        }

        public void run() {
            while (true) {
                if(!mController.CollisionCheck(mController.CMD_DOWN,user.getUserX(),user.getUserY()+1,user.getSubX(),user.getSubY()+1)){
                    user.setUserY(user.getUserY()+1);
                    user.refreshSub();
                }
                else{
                    gridState[user.getUserY()][user.getUserX()] = user.userCentColor;
                    gridState[user.getSubY()][user.getSubX()] = user.userSubColor;     // 유저의 손을 벗어남.
                    stacking();
                    checkClear();
                    user.nextPuyo();
                }
                mRenderHandler.sendEmptyMessage(0);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void rendering(boolean stackmode){
        if(!stackmode) {
            for (int i = 0; i < 14; i++)   // remove post user graphic
                for (int j = 1; j < 7; j++)
                    if (gridState[i][j] == 6)
                        gridState[i][j] = 0;

            if (user.getUserY() >= 0)  // current user position
                gridState[user.getUserY()][user.getUserX()] = 6;
            if (user.getSubY() >= 2 && user.getSubY() <= 13)
                gridState[user.getSubY()][user.getSubX()] = 6;
        }

        for(int i=2;i<14;i++)   // render current graphic
            for(int j=1;j<7;j++){
                switch (gridState[i][j]){
                    case 0:
                        grid[i][j].setImageResource(R.drawable.non);
                        break;
                    case 1:
                        grid[i][j].setImageResource(R.drawable.p1);
                        break;
                    case 2:
                        grid[i][j].setImageResource(R.drawable.p2);
                        break;
                    case 3:
                        grid[i][j].setImageResource(R.drawable.p3);
                        break;
                    case 4:
                        grid[i][j].setImageResource(R.drawable.p4);
                        break;
                    case 5:
                        grid[i][j].setImageResource(R.drawable.p5);
                        break;
                    case 6:
                        if(i == user.getUserY() && j == user.getUserX() && user.userCentColor == 1 )
                            grid[i][j].setImageResource(R.drawable.p1);
                        if(i == user.getUserY() && j == user.getUserX() && user.userCentColor == 2 )
                            grid[i][j].setImageResource(R.drawable.p2);
                        if(i == user.getUserY() && j == user.getUserX() && user.userCentColor == 3 )
                            grid[i][j].setImageResource(R.drawable.p3);
                        if(i == user.getUserY() && j == user.getUserX() && user.userCentColor == 4 )
                            grid[i][j].setImageResource(R.drawable.p4);
                        if(i == user.getUserY() && j == user.getUserX() && user.userCentColor == 5 )
                            grid[i][j].setImageResource(R.drawable.p5);
                        if(i == user.getSubY() && j == user.getSubX() && user.userSubColor == 1 )
                            grid[i][j].setImageResource(R.drawable.p1);
                        if(i == user.getSubY() && j == user.getSubX() && user.userSubColor == 2 )
                            grid[i][j].setImageResource(R.drawable.p2);
                        if(i == user.getSubY() && j == user.getSubX() && user.userSubColor == 3 )
                            grid[i][j].setImageResource(R.drawable.p3);
                        if(i == user.getSubY() && j == user.getSubX() && user.userSubColor == 4 )
                            grid[i][j].setImageResource(R.drawable.p4);
                        if(i == user.getSubY() && j == user.getSubX() && user.userSubColor == 5 )
                            grid[i][j].setImageResource(R.drawable.p5);
                        break;
                }
            }


    }

    public void stacking(){
        boolean stack = true;
        int targetX=0,targetY=0;
        rightBt.setOnClickListener(null);
        leftBt.setOnClickListener(null);
        rotateBt.setOnClickListener(null);
        downBt.setOnClickListener(null);

        while(stack) {
            stack = false;
            for (int i = 12; i >=2; i--)
                for (int j = 1; j <= 6; j++)
                    if (gridState[i][j] != 0 && gridState[i + 1][j] == 0) {  // 아래 빈공간 발견
                        stack = true;
                        targetX = j;
                        targetY = i;
                    }

            //melt down
            if(stack && targetY >=2) {
                for (int i = targetY; i < 13; i++) {
                    if(gridState[i+1][targetX] ==0) { //아래가 비어있다면
                        for(int j = i+1;j>=2;j--)
                            gridState[j][targetX] = gridState[j - 1][targetX]; //흘러내린다.

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mStackHandler.sendEmptyMessage(0);
                    }
                }
            }
        }

        rotateBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);
        rightBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);
    }

    public void checkClear(){
        rightBt.setOnClickListener(null);
        leftBt.setOnClickListener(null);
        rotateBt.setOnClickListener(null);
        downBt.setOnClickListener(null);

        for(int i=0;i<15;i++)  //  탐색여부 배열 초기화
            for(int j=0;j<8;j++)
                serchTable[i][j] = 0;

        for(int y=2;y<=13;y++){
            for(int x=1;x<=6;x++){
                delete.clear();
                queue.clear(); // 안해도될듯??
                if(gridState[y][x] == 0)
                    continue;
                else {
                    queue.add(new Point(x,y));
                    bfs(gridState[y][x]);

                    if(delete.size() >=4) {
                        for (int d = 0; d < delete.size(); d++) {
                            gridState[delete.get(d).y][delete.get(d).x] = 0; // 스택값 제거
                            stacking();
                        }
                        checkClear(); // 재귀로 연쇄 제거
                    }
                }
            }
        }

        rotateBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);
        rightBt.setOnClickListener(mController);
        leftBt.setOnClickListener(mController);
    }

    private void bfs(int color){
        if(queue.size() == 0)
            return;

        Point p = queue.poll();  //꺼내
        delete.add(new Point(p.x,p.y));
        serchTable[p.y][p.x] = 1;

        if((p.x-1) >=1 && gridState[p.y][p.x-1] == color && serchTable[p.y][p.x-1] == 0)   // 왼쪽
            queue.add(new Point(p.x-1,p.y));
        if((p.y-1) >=2 && gridState[p.y-1][p.x] == color && serchTable[p.y-1][p.x] == 0)  //위
            queue.add(new Point(p.x,p.y-1));
        if((p.x+1) <= 7 && gridState[p.y][p.x+1] == color && serchTable[p.y][p.x+1] == 0) //오른쪽
            queue.add(new Point(p.x+1,p.y));
        if((p.y+1) <=14 && gridState[p.y+1][p.x] ==color && serchTable[p.y+1][p.x] == 0) //아래
            queue.add(new Point(p.x,p.y+1));
        bfs(color);
    }
}
