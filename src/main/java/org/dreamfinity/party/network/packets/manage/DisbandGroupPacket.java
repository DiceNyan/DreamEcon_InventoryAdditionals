package org.dreamfinity.party.network.packets.manage;

import org.dreamfinity.party.utils.Localization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.Core;
import org.dreamfinity.party.elements.Party;
import org.dreamfinity.party.network.packets.AbstractPacket;

public class DisbandGroupPacket extends AbstractPacket {

	
	
	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void handleClientSide(EntityPlayer var1) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Party party = Core.getPartyManager().getPartyWithLeader(player);
		if(party == null){
			player.addChatComponentMessage(new ChatComponentText(Localization.NOT_A_PARTY_LEADER));
			return;
		}
		Core.getPartyManager().disbandParty(party);

	}

}
