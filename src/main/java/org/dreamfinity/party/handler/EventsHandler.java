package org.dreamfinity.party.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.elements.Party;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EventsHandler {
	
	
	private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	//TODO
//	@SideOnly(Side.SERVER)
//	@SubscribeEvent
//	public void onPlayerDeath(LivingDeathEvent e){
//		if(e.entityLiving instanceof EntityPlayer){
//			Core.getPartyManager();
//		}
//	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e){
		final Party party = Core.getPartyManager().getPartyWithPlayer(e.player);
		if(party != null){
			//We must add delay because client need time to load players on client side :(
			exec.schedule(new Runnable(){
				@Override
				public void run(){
					Core.getPartyManager().sendSyncPartyPackets(party);
				}
			}, 2, TimeUnit.SECONDS);
		}
			
	}
	
}
