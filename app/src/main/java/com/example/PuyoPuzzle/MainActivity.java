package com.example.PuyoPuzzle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    static final int TOUCHMODE = 1, BOARDMODE =2;
    int mode = BOARDMODE;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction1,fragmentTransaction2,fragmentTransaction3;
    private static Handler mRenderHandler;
    Queue<Point> queue = new LinkedList<Point>();
    ArrayList<Point> delete = new ArrayList<>();
    int [][] serchTable = new int[15][8];

    // gird setting
    TextView tScore;
    ImageView nextImg11,nextImg12,nextImg21,nextImg22,gameoverimg;
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
    Integer[][] imageChipset = { //b,l,t,r,bt,lb,lt,rb,rl,rt,lbt,rlb,rlt,rtb,all
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {R.drawable.p11b,R.drawable.p11l,R.drawable.p11t,R.drawable.p11r,R.drawable.p12bt,R.drawable.p12lb,R.drawable.p12lt,R.drawable.p12rb,R.drawable.p12rl,R.drawable.p12rt,R.drawable.p13lbt,R.drawable.p13rlb,R.drawable.p13rlt,R.drawable.p13rtb,R.drawable.p14},
            {R.drawable.p21b,R.drawable.p21l,R.drawable.p21t,R.drawable.p21r,R.drawable.p22bt,R.drawable.p22lb,R.drawable.p22lt,R.drawable.p22rb,R.drawable.p22rl,R.drawable.p22rt,R.drawable.p23lbt,R.drawable.p23rlb,R.drawable.p23rlt,R.drawable.p23rtb,R.drawable.p24},
            {R.drawable.p31b,R.drawable.p31l,R.drawable.p31t,R.drawable.p31r,R.drawable.p32bt,R.drawable.p32lb,R.drawable.p32lt,R.drawable.p32rb,R.drawable.p32rl,R.drawable.p32rt,R.drawable.p33lbt,R.drawable.p33rlb,R.drawable.p33rlt,R.drawable.p33rtb,R.drawable.p34},
            {R.drawable.p41b,R.drawable.p41l,R.drawable.p41t,R.drawable.p41r,R.drawable.p42bt,R.drawable.p42lb,R.drawable.p42lt,R.drawable.p42rb,R.drawable.p42rl,R.drawable.p42rt,R.drawable.p43lbt,R.drawable.p43rlb,R.drawable.p43rlt,R.drawable.p43rtb,R.drawable.p44},
            {R.drawable.p51b,R.drawable.p51l,R.drawable.p51t,R.drawable.p51r,R.drawable.p52bt,R.drawable.p52lb,R.drawable.p52lt,R.drawable.p52rb,R.drawable.p52rl,R.drawable.p52rt,R.drawable.p53lbt,R.drawable.p53rlb,R.drawable.p53rlt,R.drawable.p53rtb,R.drawable.p54},
    };
    Integer[] normalChipset = {0,R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5};
    int[][] gridState = new int[15][8];
    int score = 0;
    boolean gameOver = false;

    User user = new User(3,1);   // Single Pattern Object

    //Controll setting
    Button rotateBt,rightBt,leftBt,downBt,resetBt;
    Controller mController;

    static {
        System.loadLibrary("7segment");
        System.loadLibrary("lcd");
        System.loadLibrary("led");
        System.loadLibrary("pbutton");
        System.loadLibrary("dotmatrix");
    }

    public native void SSegmentWrite(int data);
    public native void LcdWrite(String first, String second);
    public native void LedWrite(int data);
    public native void DotWrite(int data);
    public native int PbuttonRead();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(mode == TOUCHMODE)
            setContentView(R.layout.activity_main);
        if(mode == BOARDMODE) {
            setContentView(R.layout.board_main);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        fragmentManager = getFragmentManager();
        fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.frame1, new OtherPlayerFrg(),"frag1").commit();
        fragmentTransaction2 = fragmentManager.beginTransaction();
        fragmentTransaction2.replace(R.id.frame2, new OtherPlayerFrg2(),"frag2").commit();
        fragmentTransaction3 = fragmentManager.beginTransaction();
        fragmentTransaction3.replace(R.id.frame3, new OtherPlayerFrg3(),"frag3").commit();
        //--------------View initialization------------------------------------------
        for(int i=2;i<14;i++)
            for(int j=1;j<7;j++)
                grid[i][j] = (ImageView) findViewById(gridID[i][j]);

        mRenderHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0)
                    rendering(false);
                else if(msg.what == 1)
                    rendering(true);
            }
        };

        tScore = (TextView)findViewById(R.id.score);
        nextImg11 = (ImageView)findViewById(R.id.nextpuyo11);
        nextImg12 = (ImageView)findViewById(R.id.nextpuyo12);
        nextImg21 = (ImageView)findViewById(R.id.nextpuyo21);
        nextImg22 = (ImageView)findViewById(R.id.nextpuyo22);
        gameoverimg = (ImageView)findViewById(R.id.gameover);

        //---------- Control initialization ------------------------------------------
        mController = new Controller(this,user);
        if(mode == TOUCHMODE) {
            rotateBt = (Button) findViewById(R.id.rotate);
            rightBt = (Button) findViewById(R.id.btright);
            leftBt = (Button) findViewById(R.id.btleft);
            leftBt = (Button) findViewById(R.id.btleft);
            downBt = (Button) findViewById(R.id.down);
            rotateBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
            rightBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
        }

        resetBt = (Button) findViewById(R.id.button2);   // 임시 리셋버튼
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 15; i++)
                    for (int j = 0; j < 8; j++) {
                        if (i != 14) {
                            if (j == 0 || j == 7)
                                gridState[i][j] = 7;
                            else
                                gridState[i][j] = 0;
                        } else
                            gridState[i][j] = 7;
                    }
                gameOver = false;
            }
        });

        //--------- Game Main Thread ----------------------------
        LcdWrite("1-player mode", "player1");       //----------------- 수정 요망!!!!!
        LedWrite(0);
        SSegmentWrite(0);
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
                if((gridState[3][3] != 0 && gridState[3][3] !=6) && (gridState[2][3] != 0 && gridState[2][3] !=6)){   // 사망
                    if(gameOver!=true)
                        DotWrite(-2);
                    gameOver = true;
                    mRenderHandler.sendEmptyMessage(0);
                    continue;
                }

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
        //data send to another user
        int[] senddata = new int[120];
        int k=0;
        for(int i=0;i<15;i++)
            for(int j=0;j<8;j++)
                senddata[k++] = gridState[i][j];
        Bundle data = new Bundle();
        data.putIntArray("state",senddata);
        data.putInt("userX",user.getUserX());
        data.putInt("userY",user.getUserY());
        data.putInt("subX",user.getSubX());
        data.putInt("subY",user.getSubY());
        data.putInt("centC",user.userCentColor);
        data.putInt("subC",user.userSubColor);
        getIntent().putExtras(data);

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

        //int shape = 0;
        for(int i=2;i<14;i++)   // render current graphic
            for(int j=1;j<7;j++){
                switch (gridState[i][j]){
                    case 0:
                        grid[i][j].setImageResource(R.drawable.non);
                        break;
                    case 1:
                        grid[i][j].setImageResource(puyoShapeCheck(gridState[i][j],i,j));
                        break;
                    case 2:
                        grid[i][j].setImageResource(puyoShapeCheck(gridState[i][j],i,j));
                        break;
                    case 3:
                        grid[i][j].setImageResource(puyoShapeCheck(gridState[i][j],i,j));
                        break;
                    case 4:
                        grid[i][j].setImageResource(puyoShapeCheck(gridState[i][j],i,j));
                        break;
                    case 5:
                        grid[i][j].setImageResource(puyoShapeCheck(gridState[i][j],i,j));
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
        try {
            tScore.setText("Score :"+score);
        }catch (Exception e){}
        //nextPuyo preview
        nextImg11.setImageResource(normalChipset[user.nextBuffer[0][1]]);
        nextImg12.setImageResource(normalChipset[user.nextBuffer[0][0]]);
        nextImg21.setImageResource(normalChipset[user.nextBuffer[1][1]]);
        nextImg22.setImageResource(normalChipset[user.nextBuffer[1][0]]);

        if(gameOver) {
            gameoverimg.setVisibility(View.VISIBLE);
            //finishActivity(0);
            //Handler hd = new Handler();
            //hd.postDelayed(new splash(), 5000);
        }
        else
            gameoverimg.setVisibility(View.INVISIBLE);
    }

    private class splash implements Runnable{
        public void run(){
            finishActivity(0);
        }
    }

    public void stacking(){
        if(mode == TOUCHMODE) {
            rightBt.setOnClickListener(null);
            leftBt.setOnClickListener(null);
            rotateBt.setOnClickListener(null);
            downBt.setOnClickListener(null);
        }

        boolean stack = true;
        int targetX=0,targetY=0;

        while(stack) {
            stack = false;
            for (int i = 12; i >=2; i--)
                for (int j = 1; j <= 6; j++)
                    if (gridState[i][j] != 0 && gridState[i + 1][j] == 0) {  // 아래 빈공간 발견
                        stack = true;
                        targetX = j;
                        targetY = i;
                    }

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
                        mRenderHandler.sendEmptyMessage(1);
                    }
                }
            }
        }
        if(mode == TOUCHMODE) {
            rotateBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
            rightBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
        }
    }

    public void checkClear(){
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
                        score += delete.size();
                        Thread ssegmentTread = new Thread(){
                            public void run(){
                                SSegmentWrite(score);
                            }
                        };
                        Thread ledTread = new Thread(){
                            public void run(){
                                LedWrite(-1);
                            }
                        };
                        Thread dotTread = new Thread(){
                            public void run(){
                                DotWrite(-3);
                            }
                        };
                        ssegmentTread.start();
                        ledTread.start();
                        dotTread.start();
                        for (int d = 0; d < delete.size(); d++)
                            gridState[delete.get(d).y][delete.get(d).x] = 0; // 스택값 제거
                        stacking();
                        checkClear(); // 재귀로 연쇄 제거
                    }
                }
            }
        }
        if(mode == TOUCHMODE) {
            rightBt.setOnClickListener(null);
            leftBt.setOnClickListener(null);
            rotateBt.setOnClickListener(null);
            downBt.setOnClickListener(null);
            rotateBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
            rightBt.setOnClickListener(mController);
            leftBt.setOnClickListener(mController);
        }
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
        if((p.x+1) < 7 && gridState[p.y][p.x+1] == color && serchTable[p.y][p.x+1] == 0) //오른쪽
            queue.add(new Point(p.x+1,p.y));
        if((p.y+1) <14 && gridState[p.y+1][p.x] ==color && serchTable[p.y+1][p.x] == 0) //아래
            queue.add(new Point(p.x,p.y+1));
        bfs(color);
    }

    Integer puyoShapeCheck(int color,int y,int x){
        boolean r=false,l=false,b=false,t=false;
        if(gridState[y-1][x] == color)
            t=true;
        if(gridState[y+1][x] == color)
            b =true;
        if(gridState[y][x-1] == color)
            l = true;
        if(gridState[y][x+1] == color)
            r = true;

        //b,l,t,r,bt,lb,lt,rb,rl,rt,lbt,rlb,rlt,rtb,all
        if(b && l==false && t==false && r==false)
            return imageChipset[color][0];
        else if(l && r==false && t==false && b==false)
            return imageChipset[color][1];
        else if(t && l==false && r==false && b==false)
            return imageChipset[color][2];
        else if(r && l==false && t==false && b==false)
            return imageChipset[color][3];
        else if(b && t && l==false && r==false)
            return imageChipset[color][4];
        else if(l && b && t==false && r==false)
            return imageChipset[color][5];
        else if(l && t && b==false && r==false)
            return imageChipset[color][6];
        else if(r && b && t==false && l==false)
            return imageChipset[color][7];
        else if(r && l && t==false && b==false)
            return imageChipset[color][8];
        else if(r && t && b==false && l==false)
            return imageChipset[color][9];
        else if(l && b && t && r==false)
            return imageChipset[color][10];
        else if(r && l && b && t==false)
            return imageChipset[color][11];
        else if(r && l && t && b==false)
            return imageChipset[color][12];
        else if(r && b && t && l==false)
            return imageChipset[color][13];
        else if(r && b && t && l)
            return imageChipset[color][14];

        return normalChipset[color];
    }
}