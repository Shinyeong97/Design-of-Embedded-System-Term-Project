package com.example.puyopuzzle;
import android.view.View;

public class Controller implements View.OnClickListener{
    MainActivity mainPointer;
    User user;
    static final int TOP = 0 ,RIGHT = 1, BOTTOM = 2, LEFT = 3;
    static final int CMD_RIGHT = 0, CMD_LEFT =1, CMD_DOWN = 2, CMD_ROTATE = 3;

    Thread pushClick;
    int button_num =0;

    Controller(MainActivity pm, User pu){
        mainPointer = pm;
        user = pu;

        //pushClick = new PushThread(); //--------------------- board
        //pushClick.start(); //--------------------- board
    }
    @Override
    public void onClick(View v) {
        int color_tmp;
        switch (v.getId()) {
            case R.id.rotate:
                if(user.subState == BOTTOM && mainPointer.gridState[user.getUserY()][user.getUserX()-1] !=0) {
                    if( mainPointer.gridState[user.getUserY()][user.getUserX()+1] ==0 ) {  //튕겨나옴
                        user.setUserX(user.getUserX()+1);
                        user.subState+=1;
                        user.subState%=4;
                        user.refreshSub();
                    }
                    else if(mainPointer.gridState[user.getUserY()][user.getUserX()+1] !=0) { // 꼼짝못함
                        color_tmp = user.userCentColor;
                        user.userCentColor = user.userSubColor;
                        user.userSubColor = color_tmp;
                    }
                    mainPointer.rendering(false);
                    break;
                }
                if(user.subState == TOP && mainPointer.gridState[user.getUserY()][user.getUserX()+1] !=0) {
                    if( mainPointer.gridState[user.getUserY()][user.getUserX()-1] ==0 ) {  //튕겨나옴
                        user.setUserX(user.getUserX()-1);
                        user.subState+=1;
                        user.subState%=4;
                        user.refreshSub();
                    }
                    else if(mainPointer.gridState[user.getUserY()][user.getUserX()-1] !=0) { //꼼짝못함
                        color_tmp = user.userCentColor;
                        user.userCentColor = user.userSubColor;
                        user.userSubColor = color_tmp;
                    }
                    mainPointer.rendering(false);
                    break;
                }
                if(user.subState == RIGHT && mainPointer.gridState[user.getUserY()+1][user.getUserX()] !=0)
                    break;
                user.subState+=1;
                user.subState%=4;
                user.refreshSub();
                if(CollisionCheck(CMD_ROTATE,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))  // 없어도 될듯
                    user.rollBackXY();
                else
                    mainPointer.rendering(false);
                break;
            case R.id.down:
                user.setUserY(user.getUserY()+1);
                user.refreshSub();
                if(CollisionCheck(CMD_DOWN,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                    user.rollBackXY();
                else
                    mainPointer.rendering(false);
                break;
            case R.id.btleft:
                user.setUserX(user.getUserX()-1);
                user.refreshSub();
                if(CollisionCheck(CMD_LEFT,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                    user.rollBackXY();
                else
                    mainPointer.rendering(false);
                break;
            case R.id.btright:
                user.setUserX(user.getUserX()+1);
                user.refreshSub();
                if(CollisionCheck(CMD_RIGHT,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                    user.rollBackXY();
                else
                    mainPointer.rendering(false);
        }
    }

    private class PushThread extends Thread{
        int color_tmp;
        @Override
        public void run() {
            while (true) {
                //button_num = mainPointer.PbuttonRead();   //--------------------- board
                switch (button_num) {
                    case 2:
                        if(user.subState == BOTTOM && mainPointer.gridState[user.getUserY()][user.getUserX()-1] !=0) {
                            if( mainPointer.gridState[user.getUserY()][user.getUserX()+1] ==0 ) {  //튕겨나옴
                                user.setUserX(user.getUserX()+1);
                                user.subState+=1;
                                user.subState%=4;
                                user.refreshSub();
                            }
                            else if(mainPointer.gridState[user.getUserY()][user.getUserX()+1] !=0) { // 꼼짝못함
                                color_tmp = user.userCentColor;
                                user.userCentColor = user.userSubColor;
                                user.userSubColor = color_tmp;
                            }
                            mainPointer.rendering(false);
                            break;
                        }
                        if(user.subState == TOP && mainPointer.gridState[user.getUserY()][user.getUserX()+1] !=0) {
                            if( mainPointer.gridState[user.getUserY()][user.getUserX()-1] ==0 ) {  //튕겨나옴
                                user.setUserX(user.getUserX()-1);
                                user.subState+=1;
                                user.subState%=4;
                                user.refreshSub();
                            }
                            else if(mainPointer.gridState[user.getUserY()][user.getUserX()-1] !=0) { //꼼짝못함
                                color_tmp = user.userCentColor;
                                user.userCentColor = user.userSubColor;
                                user.userSubColor = color_tmp;
                            }
                            mainPointer.rendering(false);
                            break;
                        }
                        if(user.subState == RIGHT && mainPointer.gridState[user.getUserY()+1][user.getUserX()] !=0)
                            break;
                        user.subState+=1;
                        user.subState%=4;
                        user.refreshSub();
                        if(CollisionCheck(CMD_ROTATE,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))  // 없어도 될듯
                            user.rollBackXY();
                        else
                            mainPointer.rendering(false);
                        break;
                    case 8:
                        user.setUserY(user.getUserY()+1);
                        user.refreshSub();
                        if(CollisionCheck(CMD_DOWN,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                            user.rollBackXY();
                        else
                            mainPointer.rendering(false);
                        break;
                    case 4:
                        user.setUserX(user.getUserX()-1);
                        user.refreshSub();
                        if(CollisionCheck(CMD_LEFT,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                            user.rollBackXY();
                        else
                            mainPointer.rendering(false);
                        break;
                    case 6:
                        user.setUserX(user.getUserX()+1);
                        user.refreshSub();
                        if(CollisionCheck(CMD_RIGHT,user.getUserX(),user.getUserY(),user.getSubX(),user.getSubY()))
                            user.rollBackXY();
                        else
                            mainPointer.rendering(false);
                }

                try {
                    Thread.sleep(100);  // 0.5초간 Thread sleep
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean CollisionCheck(int cmd,int unextX, int unextY, int snextX, int snextY) {
        switch (user.subState) {
            case TOP:
                if (cmd == CMD_RIGHT || cmd == CMD_LEFT)
                    if (mainPointer.gridState[unextY][unextX] != 0 || mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_DOWN) {
                    if (mainPointer.gridState[unextY][unextX] != 0)
                        return true;
                    else
                        return false;
                }
                break;
            case RIGHT:
                if (cmd == CMD_RIGHT)
                    if (mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_LEFT)
                    if (mainPointer.gridState[unextY][unextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_DOWN)
                    if (mainPointer.gridState[unextY][unextX] != 0 || mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                break;
            case BOTTOM:
                if (cmd == CMD_RIGHT || cmd == CMD_LEFT)
                    if (mainPointer.gridState[unextY][unextX] != 0 || mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_DOWN)
                    if (mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                break;
            case LEFT:
                if (cmd == CMD_RIGHT)
                    if (mainPointer.gridState[unextY][unextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_LEFT)
                    if (mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                else if (cmd == CMD_DOWN)
                    if (mainPointer.gridState[unextY][unextX] != 0 || mainPointer.gridState[snextY][snextX] != 0)
                        return true;
                    else
                        return false;
                break;
        }

        if(cmd == CMD_ROTATE)   // 없어도 될듯
            if (mainPointer.gridState[snextY][snextX] != 0)
                return true;
            else
                return false;
        return true;
    }
}
