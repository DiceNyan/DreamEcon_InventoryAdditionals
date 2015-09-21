package org.dreamfinity.party.network.packets.invite;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.utils.Localization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.elements.Party;
import org.dreamfinity.party.network.packets.AbstractPacket;

public class AddPlayerToPartyPacket extends AbstractPacket{

	private String playerToAddNickname;
	
	public AddPlayerToPartyPacket() {}
	
	public AddPlayerToPartyPacket(EntityPlayer player){
		playerToAddNickname = player.getDisplayName();
	}
	public AddPlayerToPartyPacket(String player){
		playerToAddNickname = player;
	}
	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		var2.writeBytes(playerToAddNickname.getBytes());
	}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		playerToAddNickname = new String(var2.array());
		if(playerToAddNickname.length() > 1){
			playerToAddNickname = playerToAddNickname.substring(1);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer var1) {}

	@Override
	public void handleServerSide(EntityPlayer sender) {
		Party party = Core.getPartyManager().getPartyWithLeader(sender);
		EntityPlayerMP target = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerToAddNickname);
		if(target == null){
			sender.addChatMessage(new ChatComponentText(String.format(Localization.PLAYER_WITH_NAME_NOT_FOUND, playerToAddNickname)));
			return;
		}
		if(party != null){
			if(!party.getPlayers().contains(target)){
				Core.getPacketPipeline().sendTo(new SuggestJoinPartyPacket(party.getId(), sender), target);
			} else {
				sender.addChatComponentMessage(new ChatComponentText(String.format(Localization.PLAYER_ALREADY_IN_PARTY, target.getDisplayName())));
			}
		} else {
			ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
			players.add(sender);
			Party newParty = new Party(players, sender);
			Core.getPartyManager().addParty(newParty);
			Core.getPacketPipeline().sendTo(new SuggestJoinPartyPacket(newParty.getId(), sender), target);
		}
	}

}
