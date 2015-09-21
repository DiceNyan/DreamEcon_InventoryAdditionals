package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Jamefrus on 21.08.2015.
 */

@SideOnly(Side.CLIENT)
public class ClientMessageHandler implements IMessageHandler<ResponseMessage,IMessage> {
    @Override
    public IMessage onMessage(ResponseMessage message, MessageContext ctx) {
        if(ctx.side == Side.CLIENT){
            ClientProxy.money = message.money;
        }
        return null;
    }
}
