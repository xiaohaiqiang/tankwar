package com.xhq.tank;

import com.xhq.tank.chainofresponsibility.BulletTankCollider;
import com.xhq.tank.chainofresponsibility.BulletWallCollider;
import com.xhq.tank.chainofresponsibility.Collider;
import com.xhq.tank.chainofresponsibility.ColliderChain;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TankFrame extends Frame {

    public static final TankFrame INSTANCE = new TankFrame();

    private GameModel gm = new GameModel();

    Explode e = new Explode(150,150);



    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    private TankFrame(){
        this.setTitle("com.xhq.tank.Tank War");
        this.setLocation(400, 100);
        this.setSize(GAME_WIDTH,GAME_HEIGHT);

        this.addKeyListener(new TankKeyListener());



    }


    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }



    @Override
    public void paint(Graphics g) {
        gm.paint(g);
    }

    private class TankKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_S){
                //save
                save();
            }else if(key == KeyEvent.VK_L){
                load();
            }else{
                gm.getMyTank().keyPressed(e);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gm.getMyTank().keyReleased(e);
        }
    }

    private void load() {
        File f = new File("c:/Code/Tank/tank.dat");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(f));
            this.gm = (GameModel)ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void save() {
        File f = new File("c:/Code/Tank/tank.dat");
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(gm);

            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public GameModel getGm(){
        return this.gm;
    }
}
