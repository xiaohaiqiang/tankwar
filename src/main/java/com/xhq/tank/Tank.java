package com.xhq.tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Tank {
    private int x;
    private int y;
    private Dir dir;
    private boolean bL, bU, bR, bD;
    private boolean moving = false;
    private Group group;

    public static final int SPEED = 5;

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }

    public void paint(Graphics g) {

        if(this.group == Group.GOOD){
            switch (dir){
                case L:
                    g.drawImage(ResourceMgr.goodTankL, x, y, null);
                    break;
                case R:
                    g.drawImage(ResourceMgr.goodTankR, x, y, null);
                    break;
                case U:
                    g.drawImage(ResourceMgr.goodTankU, x, y, null);
                    break;
                case D:
                    g.drawImage(ResourceMgr.goodTankD, x, y, null);
                    break;
            }
        }

        if(this.group == Group.BAD){
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
        if(!bL && !bR && !bD && !bU){
            moving = false;
        }else{
            moving = true;
        }

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

    private void fire() {
        TankFrame.INSTANCE.add(new Bullet(x, y, dir, group));
    }
}
