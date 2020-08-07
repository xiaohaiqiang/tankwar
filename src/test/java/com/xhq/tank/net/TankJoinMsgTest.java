package com.xhq.tank.net;


import com.xhq.nettycodec.TankMsgDecoder;
import com.xhq.tank.Dir;
import com.xhq.tank.Group;
import com.xhq.tank.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

public class TankJoinMsgTest {


    @Test
    public void encode() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgEncoder());

        Player p = new Player(50, 100, Dir.R, Group.BAD);
        TankJoinMsg tjm = new TankJoinMsg(p);

        ch.writeOutbound(tjm);

        ByteBuf buf = ch.readOutbound();

        int length = buf.readInt();

        int x = buf.readInt();
        int y = buf.readInt();
        Dir dir = Dir.values()[buf.readInt()];
        boolean moving = buf.readBoolean();
        Group group = Group.values()[buf.readInt()];
        UUID id = new UUID(buf.readLong(), buf.readLong());

        assertEquals(33,length);

        assertEquals(50,x);
        assertEquals(100,y);
        assertEquals(Dir.R,dir);
        assertFalse(moving);
        assertEquals(Group.BAD,group);
        assertEquals(p.getId(),id);
    }

    @Test
    void decode(){
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgDecoder());


    }
}
