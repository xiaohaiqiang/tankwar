package com.xhq.tank;

import com.xhq.tank.net.Client;
import com.xhq.tank.net.TankStartMovingMsg;
import com.xhq.tank.net.TankStopMsg;
import com.xhq.tank.strategy.DefaultFireStrategy;
import com.xhq.tank.strategy.FireStrategy;
import com.xhq.tank.strategy.FourDirFireStrategy;
import com.xhq.tank.strategy.LeftRightFireStrategy;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.UUID;

public class Player extends AbstractGameObject {
    public static final int SPEED = 5;

    private int x;
    private int y;

    private Dir dir;
    private boolean bL, bU, bR, bD;
    private boolean moving = false;
    private Group group;
    private boolean live = true;

    private UUID id = UUID.randomUUID();

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
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


    public Player(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        //init fire strategy from config file
        this.initFireStrategy();
    }

    public void paint(Graphics g) {

        if(!this.isLive()) return;

        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString(), x, y -10);
        g.setColor(c);

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
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }

        setMainDir();

    }

    private void setMainDir() {

        boolean oldMoving = moving;

        if(!bL && !bR && !bD && !bU){
            moving = false;
            Client.INSTANCE.send(new TankStopMsg(this.id, this.x, this.y));
        }else{
            moving = true;

            if(bL && !bR && !bD && !bU){
                dir = Dir.L;
            }
            if(!bL && bR && !bD && !bU){
                dir = Dir.R;
            }
            if(!bL && !bR && bD && !bU){
                dir = Dir.D;
            }
            if(!bL && !bR && !bD && bU){
                dir = Dir.U;
            }
        }

        if(!oldMoving){
            Client.INSTANCE.send(new TankStartMovingMsg(this.id,this.x,this.y,this.dir));
        }
    }

    private void move() {

        if(!moving) return ;

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

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
        }
        setMainDir();
    }

    private FireStrategy strategy = null;
    private void initFireStrategy(){

        String className = PropertyMgr.get("tankFireStrategy");
        try {
            Class c = Class.forName(className);
            strategy = (FireStrategy) c.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fire() {

        strategy.fire(this);
    }

    public void die() {
        this.setLive(false);
    }
}
