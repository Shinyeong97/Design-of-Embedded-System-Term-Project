package com.example.PuyoPuzzle;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OtherPlayerFrg extends Fragment {
    private static Handler mHandler ;
    View v;

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

    int revUserX=0, revUserY=0,revUserSubX=0,revUserSubY=0;
    int revUserCentC=0,revUserSubC=0;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.other1, container, false);

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
        for(int i=2;i<14;i++)
            for(int j=1;j<7;j++)
                grid[i][j] = (ImageView) v.findViewById(gridID[i][j]);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                rendering(false);
            }
        };

        ViewThread thread = new ViewThread();
        thread.start();
        return v;
    }

    private class ViewThread extends Thread {


        public ViewThread() {

        }

        public void run() {

            while (true) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mHandler.sendEmptyMessage(0) ;
            }
        }
    }

    public void rendering(boolean stackmode){
        try { // 데이터 수신부
            Bundle data = getActivity().getIntent().getExtras();
            int[] revdata = data.getIntArray("state");
            int k = 0;
            for (int i = 0; i < 15; i++)
                for (int j = 0; j < 8; j++)
                    gridState[i][j] = revdata[k++];
            revUserX = data.getInt("userX");
            revUserY = data.getInt("userY");
            revUserSubX = data.getInt("subX");
            revUserSubY = data.getInt("subY");
            revUserCentC = data.getInt("centC");
            revUserSubC = data.getInt("subC");
        }catch(Exception e){
            //
        }


        if(!stackmode) {
            for (int i = 0; i < 14; i++)   // remove post user graphic
                for (int j = 1; j < 7; j++)
                    if (gridState[i][j] == 6)
                        gridState[i][j] = 0;

            if (revUserY >= 0)  // current user position
                gridState[revUserY][revUserX] = 6;
            if (revUserY >= 2 && revUserX <= 13)
                gridState[revUserSubY][revUserSubX] = 6;
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
                        if(i == revUserY && j == revUserX && revUserCentC == 1 )
                            grid[i][j].setImageResource(R.drawable.p1);
                        if(i == revUserY && j == revUserX && revUserCentC == 2 )
                            grid[i][j].setImageResource(R.drawable.p2);
                        if(i == revUserY && j == revUserX && revUserCentC == 3 )
                            grid[i][j].setImageResource(R.drawable.p3);
                        if(i == revUserY && j == revUserX && revUserCentC == 4 )
                            grid[i][j].setImageResource(R.drawable.p4);
                        if(i == revUserY && j == revUserX && revUserCentC == 5 )
                            grid[i][j].setImageResource(R.drawable.p5);
                        if(i == revUserSubY && j == revUserSubX && revUserSubC == 1 )
                            grid[i][j].setImageResource(R.drawable.p1);
                        if(i == revUserSubY && j == revUserSubX && revUserSubC == 2 )
                            grid[i][j].setImageResource(R.drawable.p2);
                        if(i == revUserSubY && j == revUserSubX && revUserSubC == 3 )
                            grid[i][j].setImageResource(R.drawable.p3);
                        if(i == revUserSubY && j == revUserSubX && revUserSubC == 4 )
                            grid[i][j].setImageResource(R.drawable.p4);
                        if(i == revUserSubY && j == revUserSubX && revUserSubC == 5 )
                            grid[i][j].setImageResource(R.drawable.p5);
                        break;
                }
            }
        //tScore.setText("Score : "+score);
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
