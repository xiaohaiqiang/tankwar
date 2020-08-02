package com.xhq.tank.chainofresponsibility;

import com.xhq.tank.AbstractGameObject;
import com.xhq.tank.Bullet;
import com.xhq.tank.Tank;
import com.xhq.tank.Wall;

public class TankWallCollider implements Collider {
    public boolean collide(AbstractGameObject go1, AbstractGameObject go2) {
        if(go1 instanceof Tank && go2 instanceof Wall){
            Tank t = (Tank) go1;
            Wall w = (Wall) go2;
            if(t.isLive()){
                if(t.getRect().intersects(w.getRect())){
                    t.back();
                }
            }
        }else if(go1 instanceof Wall && go2 instanceof Tank){
            collide(go2, go1);
        }

        return true;
    }
}
