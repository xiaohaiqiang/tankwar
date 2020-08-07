package com.xhq.tank;

import com.xhq.tank.net.Client;

public class Main {
    public static void main(String[] args) {

        TankFrame.INSTANCE.setVisible(true);

        new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TankFrame.INSTANCE.repaint();//repaint()----update()----paint()
            }
        }).start();


        Client.INSTANCE.connect();
    }
}
