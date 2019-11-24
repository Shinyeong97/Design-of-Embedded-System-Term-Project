package com.example.PuyoPuzzle;

public class User{
    private int userX = 3, userY = 1;
    private int subX,subY;
    public int userCentColor = 1;
    public int userSubColor = 2;
    int subState = 0; // sub puyo rotation state value  e.g defalut = 0 : top
    int[][] nextBuffer = new int[2][2];
    int saveUserStateX,saveUserStateY,saveSubX,saveSubY,saveRotate;

    User(int startX, int startYbuffer){
        userX = startX;
        userY = startYbuffer;
        subX = startX;
        subY = startYbuffer-1;
        userCentColor = 1;
        userSubColor = 2;

        //Buffer Initialization
        nextBuffer[0][0] = (int)(Math.random() * 4)+1;
        nextBuffer[0][1] = (int)(Math.random() * 4)+1;
        nextBuffer[1][0] = (int)(Math.random() * 4)+1;
        nextBuffer[1][1] = (int)(Math.random() * 5)+1;
    }

    public void refreshSub(){
        switch(subState){
            case 0: //top
                subX = userX;
                subY = userY-1;
                break;
            case 2: // bottom
                subX = userX;
                subY = userY+1;
                break;
            case 3: // left
                subX = userX-1;
                subY = userY;
                break;
            case 1: // right
                subX = userX+1;
                subY = userY;
        }
    }

    public void setUserX(int x){ saveCor(); this.userX = x;}
    public void setUserY(int y){ saveCor(); this.userY = y;}
    public void setSubX(int x){ saveCor(); this.subX = x;}
    public void setSubY(int y){ saveCor(); this.subY = y;}

    public int getUserX(){ return this.userX;}
    public int getUserY(){ return this.userY;}
    public int getSubX(){ return this.subX;}
    public int getSubY(){ return this.subY;}

    private void saveCor(){
        saveUserStateX = this.userX;
        saveUserStateY = this.userY;
        saveSubX = this.subX;
        saveSubY = this.subY;
        saveRotate = this.subState;
    }

    public void rollBackXY(){ // rollback to post coordinate if Collision dectection
        this.userX = saveUserStateX;
        this.userY = saveUserStateY;
        this.subX = saveSubX;
        this.subY = saveSubY;
        this.subState = saveRotate;
    }

    public void nextPuyo(){
        userX = 3;
        userY = 1;
        subState = 0;
        refreshSub();
        userCentColor = nextBuffer[0][0];
        userSubColor = nextBuffer[0][1];
        nextBuffer[0][0] = nextBuffer[1][0];
        nextBuffer[0][1] = nextBuffer[1][1];
        nextBuffer[1][0] = (int)(Math.random() * 4)+1;  // new buffer
        nextBuffer[1][1] = (int)(Math.random() * 5)+1;  // new buffer
    }
}
