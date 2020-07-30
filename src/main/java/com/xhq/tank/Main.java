package com.xhq.tank;

public class Main {
    public static void main(String[] args) {

        TankFrame.INSTANCE.setVisible(true);

        while(true){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TankFrame.INSTANCE.repaint();//repaint()----update()----paint()
        }
    }
}
