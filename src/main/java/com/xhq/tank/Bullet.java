package com.xhq.tank;

import java.awt.*;

public class Bullet extends AbstractGameObject{
    private int x;
    private int y;
    private Dir dir;
    private Group group;
    private boolean live = true;
    private Rectangle rect;
    public static final int w = ResourceMgr.bulletU.getWidth();
    public static final int h = ResourceMgr.bulletU.getHeight();
    public static final int SPEED = 6;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }



    public Bullet(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        rect = new Rectangle(x,y,w, h);
    }

    public void paint(Graphics g) {

        switch (dir){
            case L:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }
        move();

        //update the rect
        rect.x = x;
        rect.y = y;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private void move() {
        switch(dir){
            case L:
                x -= SPEED;
                break;
            case R:
                x += SPEED;
                break;
            case U:
                y -= SPEED;
                break;
            case D:
                y += SPEED;
                break;
        }


        boundsCheck();
    }

    public Rectangle getRect(){
        return rect;
    }

    public void die() {
        this.setLive(false);
    }

    private void boundsCheck() {
        if(x < 0 || y < 30 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT){
            live = false;
        }
    }
}
