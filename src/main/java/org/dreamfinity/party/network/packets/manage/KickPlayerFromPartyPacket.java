package org.dreamfinity.party.network.packets.manage;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.utils.Localization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.elements.Party;
import org.dreamfinity.party.network.packets.AbstractPacket;

public class KickPlayerFromPartyPacket extends AbstractPacket {

	private String playerNameToKick;
	public KickPlayerFromPartyPacket(){}
	
	public KickPlayerFromPartyPacket(String playerToKick){
		this.playerNameToKick = playerToKick;
	}
	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		var2.writeBytes(playerNameToKick.getBytes());
	}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		this.playerNameToKick = new String(var2.array()).substring(1);
	}

	@Override
	public void handleClientSide(EntityPlayer var1) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Party party = Core.getPartyManager().getPartyWithLeader(player);
		if(party == null){
			player.addChatComponentMessage(new ChatComponentText(Localization.NOT_A_PARTY_LEADER));
			return;
		}
		EntityPlayer playerToKick = party.getPlayerWithNickname(playerNameToKick);
		if(playerToKick == null){
			player.addChatComponentMessage(new ChatComponentText(String.format(Localization.PLAYER_NOT_IN_YOUR_PARTY, playerNameToKick)));
			return;
		}
		party.removePlayer(playerToKick);
		party.sendMessageToMembers(new ChatComponentText(String.format(Localization.PLAYER_KICKED_BY_LEADER, playerToKick.getDisplayName())));
		Core.getPartyManager().cleanPlayerClientParty(playerToKick); //This will destroy party inside player's client 
		Core.getPartyManager().sendSyncPartyPackets(party);
		
		
	}

}
