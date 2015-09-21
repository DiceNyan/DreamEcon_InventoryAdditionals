package org.dreamfinity.party.main;

import java.util.ArrayList;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.handshake.PartyCleanPacket;
import org.dreamfinity.party.network.packets.handshake.SyncPartyPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.elements.Party;

public class PartyManager {
	
	
	private ArrayList<Party> parties;
	
	public PartyManager(){
		parties = new ArrayList<Party>();
	}
	
	public void addParty(Party party){
		if(party != null){
			parties.add(party);
		}
	}
	
	public void removeParty(Party party){
		if(party != null){
			parties.remove(parties.indexOf(party));
		}
	}
	
	/**
	 * Loops through all parties and searches for the party with the given leader
	 * @param leader
	 * @return Party with the given leader or <i><b>null</b></i> if party with this leader doesn't exists 
	 */
	public Party getPartyWithLeader(EntityPlayer leader){
		Party party = null;
		if(!parties.isEmpty()){
			for(Party currentParty: parties){
				if(currentParty != null){
					if(currentParty.getLeader().getDisplayName().equals(leader.getDisplayName())){
						party = currentParty;
						break;
					}
				}
			}
		}
		return party;
	}
	
	/**
	 * Loops thought all parties and searches for the party with the given id
	 * @param id
	 * @return Party with the given id or <i><b>null</b></i> if party with this id doesn't exists 
	 */
	public Party getPartyById(int id){
		Party party = null;
		for(Party currentParty: parties){
			if(currentParty != null){
				if(currentParty.getId() == id){
					party = currentParty;
					break;
				}
			}
		}
		return party;
	}
	
	/**
	 * Loops thought all parties and checks if given player exists in player list of party
	 * @param player
	 * @return Party in which given player contains or <i><b>null</b></i> 
	 */
	public Party getPartyWithPlayer(EntityPlayer player){
		Party party = null;
		if(!parties.isEmpty()){
			for(Party p: parties){
				if(p.getPlayers().contains(player)){
					party = p;
					break;
				}
			}
		}
		return party;
	}
	
	
	/**
	 * For each player in party it gets his EntityPlayerMP representation and sends him a {@link SyncPartyPacket SyncPartyPacket}
	 * @param party
	 */
	public void sendSyncPartyPackets(Party party){
		for(EntityPlayer player: party.getPlayers()){
			EntityPlayerMP playermp = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player.getDisplayName());
			if(playermp != null){
				Core.getPacketPipeline().sendTo(new SyncPartyPacket(party), playermp);
			}
		}
	}
	
	/**
	 * Searches for player's party and sends him a sync packet
	 */
	public void syncPlayerParty(EntityPlayer player){
		Party party = getPartyWithPlayer(player);
		EntityPlayerMP playermp = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player.getDisplayName());
		if(playermp != null){
			if(party != null){
				Core.getPacketPipeline().sendTo(new SyncPartyPacket(party), playermp);
			} else {
				Core.getPacketPipeline().sendTo(new PartyCleanPacket(), playermp);
			}
		}
	}

	public void cleanPlayerClientParty(EntityPlayer player) {
		EntityPlayerMP playermp = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player.getDisplayName());
		if(playermp != null){
			Core.getPacketPipeline().sendTo(new PartyCleanPacket(), playermp);
		}
	}

	
	public void cleanPlayersInParty(Party party){
		if(party != null){
			for(EntityPlayer player: party.getPlayers()){
				EntityPlayerMP playermp = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player.getDisplayName());
				Core.getPacketPipeline().sendTo(new PartyCleanPacket(), playermp);
			}
		}
	}
	public void disbandParty(Party party){
		if(party != null){
			party.sendMessageToMembers(new ChatComponentText("Party has been disbanded by leader"));
			removeParty(party);
			cleanPlayersInParty(party);
		}
	}
	
	
	
	
	
}
