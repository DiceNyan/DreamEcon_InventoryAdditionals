package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by Jamefrus on 21.08.2015.
 */

public class ResponseMessage implements IMessage{

    public ResponseMessage() {
    }

    public ResponseMessage(double money) {
        this.money = money;
    }
    public double money;

    @Override
    public void fromBytes(ByteBuf buf) {
        if(buf.capacity() != 0)
        money = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(money);
    }

    public static class RequestMessage implements IMessage {


        @Override
        public void fromBytes(ByteBuf buf) {
        }

        @Override
        public void toBytes(ByteBuf buf) {

        }
    }
}
