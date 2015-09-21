package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import org.bukkit.Bukkit;
import skymine.redenergy.vaultwrapper.SkyMineVault;

/**
 * Created by Jamefrus on 21.08.2015.
 */

public class CommonProxy {

    public double getMoneyCount(EntityPlayer player) {
        return SkyMineVault.getEconomy().getBalance(Bukkit.getPlayer(player.getGameProfile().getId()));
    }

    public void preInit(FMLPreInitializationEvent ev, SimpleNetworkWrapper network) {
        network.registerMessage(ServerMessageHandler.class,ResponseMessage.RequestMessage.class,0, Side.SERVER);
    }
}
