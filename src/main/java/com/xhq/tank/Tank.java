package com.xhq.tank;

import com.xhq.tank.net.TankJoinMsg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class Tank extends AbstractGameObject{
    public static final int SPEED = 5;
    private int x;
    private int y;
    private Dir dir;
    private boolean bL, bU, bR, bD;
    private boolean moving = true;
    private Group group;
    private int width, height;

    private int oldX, oldY;
    private Rectangle rect;

    private UUID id;

    public Tank(TankJoinMsg msg) {
        this.x = msg.getX();
        this.y = msg.getY();
        this.dir = msg.getDir();
        this.moving = msg.isMoving();
        this.group = msg.getGroup();
        this.id = msg.getId();

        oldX = x;
        oldY = y;
        this.width = ResourceMgr.goodTankU.getWidth();
        this.height = ResourceMgr.goodTankU.getHeight();

        this.rect = new Rectangle(x,y,width,height);
    }

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

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        oldX = x;
        oldY = y;
        this.width = ResourceMgr.goodTankU.getWidth();
        this.height = ResourceMgr.goodTankU.getHeight();

        this.rect = new Rectangle(x,y,width,height);
    }

    public void paint(Graphics g) {

        if(!this.isLive()) return;

        switch (dir){
            case L:
                g.drawImage(this.group.equals(Group.BAD)? ResourceMgr.badTankL: ResourceMgr.goodTankL, x, y, null);
                break;
            case R:
                g.drawImage(this.group.equals(Group.BAD)? ResourceMgr.badTankR: ResourceMgr.goodTankR, x, y, null);
                break;
            case U:
                g.drawImage(this.group.equals(Group.BAD)? ResourceMgr.badTankU: ResourceMgr.goodTankU, x, y, null);
                break;
            case D:
                g.drawImage(this.group.equals(Group.BAD)? ResourceMgr.badTankD: ResourceMgr.goodTankD, x, y, null);
                break;
        }

        move();

        //update rect
        rect.x = x;
        rect.y = y;
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

        //randomDir();

//        if(r.nextInt(100) > 90){
//            fire();
//        }
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
        int bX = x + width / 2 - Bullet.w / 2;
        int bY = y + height / 2 - Bullet.h / 2;
        TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, dir, group));
    }

    public void die() {
        this.setLive(false);
        TankFrame.INSTANCE.getGm().add(new Explode(x,y));
    }

    private void boundsCheck() {
        if(x < 0 || y < 30 || x + width > TankFrame.GAME_WIDTH || y + height > TankFrame.GAME_HEIGHT){
            this.back();
        }
    }

    public void back() {
        this.x = oldX;
        this.y = oldY;
    }

    public Rectangle getRect() {

        return rect;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }
}
