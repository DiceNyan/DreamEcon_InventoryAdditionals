package org.dreamfinity.party.network.packets.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamfinity.party.client.ClientData;
import org.dreamfinity.party.elements.Party;
import org.dreamfinity.party.elements.SerializableParty;
import org.dreamfinity.party.network.packets.AbstractPacket;

import com.google.gson.Gson;

public class SyncPartyPacket extends AbstractPacket{

	private SerializableParty sparty;
	
	public SyncPartyPacket() {}
	
	public SyncPartyPacket(Party p){
		sparty = SerializableParty.serializeParty(p);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		Gson gson = new Gson();
		String json = gson.toJson(sparty);
		var2.writeBytes(json.getBytes());
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		Gson gson = new Gson();
		String json = new String(var2.array()).substring(1);
		sparty = gson.fromJson(json, SerializableParty.class);
	}

	@Override
	public void handleClientSide(EntityPlayer var1) {
		int id = sparty.id;
		ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
		for(String playerName: sparty.players){
			EntityPlayer newPlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(playerName);
			if(newPlayer != null){
				players.add(newPlayer);	
			}
		}
		EntityPlayer leader = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(sparty.leader);
		if(leader != null){
			ClientData.clientParty = new Party(id, players, leader);
		}
		ClientData.clientParty = new Party(id, players, leader);
		
	}

	@Override
	public void handleServerSide(EntityPlayer var1) {}

}
