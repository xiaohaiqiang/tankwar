package com.xhq.tank.chainofresponsibility;

import com.xhq.tank.AbstractGameObject;
import com.xhq.tank.Bullet;
import com.xhq.tank.Tank;

import java.awt.*;

public class TankTankCollider implements Collider{
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if(go1 != go2 && go1 instanceof Tank && go2 instanceof Tank){
            Tank t1 = (Tank) go1;
            Tank t2 = (Tank) go2;

            if(t1.isLive() || t2.isLive()){
                if(t1.getRect().intersects(t2.getRect())){
                    t1.back();
                    t2.back();
                }
            }
        }

        return true;
    }
}
