package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Jamefrus on 21.08.2015.
 */

public class ClientProxy extends CommonProxy {

    public static double money;

    @Override
    public double getMoneyCount(EntityPlayer player) {
        return money;
    }

    @Override
    public void preInit(FMLPreInitializationEvent ev, SimpleNetworkWrapper network) {
        network.registerMessage(ClientMessageHandler.class, ResponseMessage.class, 0, Side.CLIENT);
    }
}
