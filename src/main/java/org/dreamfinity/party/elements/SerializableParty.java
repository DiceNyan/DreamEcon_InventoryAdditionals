package org.dreamfinity.party.elements;

import java.util.ArrayList;
import java.util.List;

import org.dreamfinity.party.network.packets.handshake.SyncPartyPacket;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This is a lite version of {@link Party} used in {@link SyncPartyPacket SyncPartyPacket}
 * @author Dreamfinity.org
 *
 */
public class SerializableParty {
	public int id;
	public List<String> players;
	public String leader;
	
	/**
	 * @param id
	 * @param players
	 * @param leader
	 */
	public SerializableParty(int id, List<String> players, String leader){
		this.id = id;
		this.players = players;
		this.leader = leader;
	}
	
	/**
	 * Converts {@link Party} to {@link SerializableParty}
	 * @param Party for serialize
	 * @return Serialized party
	 */
	public static SerializableParty serializeParty(Party party){
		int id = party.getId();
		List<String> players = new ArrayList<String>();
		for(EntityPlayer player: party.getPlayers()){
			players.add(player.getDisplayName());
		}
		String leader = party.getLeader().getDisplayName();
		return new SerializableParty(id, players, leader);
	}
}
