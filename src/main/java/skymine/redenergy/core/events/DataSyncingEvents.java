package skymine.redenergy.core.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import skymine.redenergy.core.Core;
import skymine.redenergy.core.network.packets.SyncPlayerPropsMessage;
import skymine.redenergy.core.player.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DataSyncingEvents {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			if (ExtendedPlayer.get((EntityPlayer) event.entity) == null){
				EntityPlayer p = (EntityPlayer) event.entity;
				ExtendedPlayer.register((EntityPlayer) event.entity);
			}
		}
	}
	
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.entity instanceof EntityPlayerMP){
			Core.getNetworkHandler().sendTo(new SyncPlayerPropsMessage((EntityPlayer) event.entity), (EntityPlayerMP) event.entity);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
			ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			ExtendedPlayer.get(player).onUpdate();
		}
	}
}
