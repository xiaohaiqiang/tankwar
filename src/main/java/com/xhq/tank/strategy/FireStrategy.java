package com.xhq.tank.strategy;

import com.xhq.tank.Player;
import com.xhq.tank.Tank;

import java.io.Serializable;

public interface FireStrategy extends Serializable {
    public void fire(Player p);
}
