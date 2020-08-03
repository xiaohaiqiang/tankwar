package com.xhq.tank.strategy;

import com.xhq.tank.*;

public class FourDirFireStrategy implements FireStrategy {
    public void fire(Player p) {
        int bX = p.getX() + ResourceMgr.goodTankU.getWidth() / 2 - ResourceMgr.bulletU.getWidth() / 2;
        int bY = p.getY() +ResourceMgr.goodTankU.getHeight() / 2 -ResourceMgr.bulletU.getHeight() / 2;

        Dir[] dirs = Dir.values();
        for(Dir dir : dirs){
            TankFrame.INSTANCE.getGm().add(new Bullet(bX, bY, dir, p.getGroup()));
        }
    }
}
