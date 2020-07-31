package com.xhq.tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Tank {
    private int x;
    private int y;
    private Dir dir;
    private boolean bL, bU, bR, bD;
    private boolean moving = true;
    private Group group;
    private int width, height;

    private int oldX, oldY;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live = true;

    public static final int SPEED = 5;

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        oldX = x;
        oldY = y;
        this.width = ResourceMgr.goodTankU.getWidth();
        this.height = ResourceMgr.goodTankU.getHeight();
    }

    public void paint(Graphics g) {

        if(!this.isLive()) return;

        switch (dir){
            case L:
                g.drawImage(ResourceMgr.badTankL, x, y, null);
                break;
            case R:
                g.drawImage(ResourceMgr.badTankR, x, y, null);
                break;
            case U:
                g.drawImage(ResourceMgr.badTankU, x, y, null);
                break;
            case D:
                g.drawImage(ResourceMgr.badTankD, x, y, null);
                break;
        }

        move();
    }

    private void move() {

        if(!moving) return;

        oldX = x;
        oldY = y;

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

        randomDir();
        if(r.nextInt(100) > 90){
            fire();
        }
    }

    private Random r = new Random();

    private void randomDir() {
        if(r.nextInt(100) < 90) return;
        this.dir = Dir.randomDir();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private void fire() {
        int bX = x + ResourceMgr.goodTankU.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = y +ResourceMgr.goodTankU.getHeight() / 2 -ResourceMgr.bulletU.getHeight() / 2;
        TankFrame.INSTANCE.add(new Bullet(bX, bY, dir, group));
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.add(new Explode(x,y));
    }

    private void boundsCheck() {
        if(x < 0 || y < 30 || x + width > TankFrame.GAME_WIDTH || y + height > TankFrame.GAME_HEIGHT){
            this.back();
        }
    }

    private void back() {
        this.x = oldX;
        this.y = oldY;
    }
}
