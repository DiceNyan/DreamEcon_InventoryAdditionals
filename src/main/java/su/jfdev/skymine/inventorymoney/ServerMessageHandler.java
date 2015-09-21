package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Jamefrus on 21.08.2015.
 */

@SideOnly(Side.SERVER)
public class ServerMessageHandler implements IMessageHandler<ResponseMessage.RequestMessage,ResponseMessage>{
    @Override
    public ResponseMessage onMessage(ResponseMessage.RequestMessage message, MessageContext ctx) {
        if(ctx.side == Side.SERVER){
            return new ResponseMessage(InventoryMoney.proxy.getMoneyCount(ctx.getServerHandler().playerEntity));
        } else return null;
    }
}
