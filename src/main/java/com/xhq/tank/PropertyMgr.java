package com.xhq.tank;

import org.junit.Test;


import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
    private static Properties props;

    static{
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return (String)props.get(key);
    }

    @Test
    public void test(){
        System.out.println(PropertyMgr.get("initTankCount"));
    }

}
