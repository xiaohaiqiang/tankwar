package com.xhq.tank.chainofresponsibility;

import com.xhq.tank.AbstractGameObject;
import com.xhq.tank.PropertyMgr;

import java.util.ArrayList;
import java.util.List;

public class ColliderChain implements Collider{

    private List<Collider> colliders;

    public ColliderChain() {
        initColliders();
    }

    private void initColliders() {
        colliders = new ArrayList<Collider>();
        String[] collidersName = PropertyMgr.get("colliders").split(",");
        for(String name : collidersName){
            try {
                Class c = Class.forName(name);
                Collider collider = (Collider)c.getConstructor().newInstance();
                colliders.add(collider);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean collide(AbstractGameObject go1, AbstractGameObject go2){
        for(Collider collider : colliders){
            if(!collider.collide(go1, go2)){
                return false;
            }
        }
        return true;
    }
}
