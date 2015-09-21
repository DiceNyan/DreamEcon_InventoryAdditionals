package org.dreamfinity.party;

import org.dreamfinity.party.handler.EventsHandler;
import org.dreamfinity.party.network.packets.handshake.PartyCleanPacket;
import org.dreamfinity.party.network.packets.handshake.SyncPartyPacket;
import org.dreamfinity.party.network.packets.invite.RejectPartyInvitationPacket;
import org.dreamfinity.party.network.packets.manage.DisbandGroupPacket;
import net.minecraftforge.common.MinecraftForge;
import org.dreamfinity.party.main.PartyManager;
import org.dreamfinity.party.network.PacketPipeline;
import org.dreamfinity.party.network.packets.invite.AcceptPartyInvitationPacket;
import org.dreamfinity.party.network.packets.invite.AddPlayerToPartyPacket;
import org.dreamfinity.party.network.packets.invite.SuggestJoinPartyPacket;
import org.dreamfinity.party.network.packets.manage.KickPlayerFromPartyPacket;
import org.dreamfinity.party.network.packets.manage.LeaveGroupPacket;
import org.dreamfinity.party.proxy.ServerProxy;
import org.dreamfinity.party.utils.Refs;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Refs.MOD_ID, name = Refs.MOD_TITLE, version = Refs.MOD_VERSION)
public class Core {

	/**
	 * TODO: Fix buttons
	 * TODO: Add party cloud container
	 * TODO: Add party item transfer
	 */
	private static PacketPipeline network;
	private static PartyManager partyManager;
	
	@Mod.Instance
	public Core instance;

	public Core getInstance(){;
		return instance;
	}

	@SidedProxy(clientSide = Refs.CLIENT_PROXY, serverSide = Refs.SERVER_PROXY)
	public static ServerProxy proxy;
	
	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent e){
		network = new PacketPipeline();
		network.initialise();
		network.registerPacket(AddPlayerToPartyPacket.class);
		network.registerPacket(SuggestJoinPartyPacket.class);
		network.registerPacket(AcceptPartyInvitationPacket.class);
		network.registerPacket(RejectPartyInvitationPacket.class);
		network.registerPacket(SyncPartyPacket.class);
		network.registerPacket(KickPlayerFromPartyPacket.class);
		network.registerPacket(PartyCleanPacket.class);
		network.registerPacket(DisbandGroupPacket.class);
		network.registerPacket(LeaveGroupPacket.class);
		proxy.initProxy();
		if(e.getSide() == Side.SERVER){
			partyManager = new PartyManager();
		}
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
		FMLCommonHandler.instance().bus().register(new EventsHandler());
	}
	
	public static PacketPipeline getPacketPipeline(){
		return network;
	}
	
	public static PartyManager getPartyManager(){
		return partyManager;
	}
	
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent e){
		network.postInitialise();
	}
}
