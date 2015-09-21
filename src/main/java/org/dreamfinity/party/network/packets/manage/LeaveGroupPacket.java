package org.dreamfinity.party.network.packets.manage;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.AbstractPacket;
import org.dreamfinity.party.utils.Localization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.elements.Party;

public class LeaveGroupPacket extends AbstractPacket {

	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void handleClientSide(EntityPlayer var1) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Party party = Core.getPartyManager().getPartyWithPlayer(player);
		if(party == null){
			player.addChatComponentMessage(new ChatComponentText(Localization.YOU_RE_NOT_IN_PARTY));
			return;
		}
		party.removePlayer(player);
		Core.getPartyManager().cleanPlayerClientParty(player);
		Core.getPartyManager().sendSyncPartyPackets(party);
		party.sendMessageToMembers(new ChatComponentText(String.format(Localization.PLAYER_LEFT_THE_PARTY, player.getDisplayName())));
	}

}
