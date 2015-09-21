package org.dreamfinity.party.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.manage.KickPlayerFromPartyPacket;
import org.dreamfinity.party.utils.Utilities;

public class GuiPartyPlayerPanel extends Gui{
	
	private GuiButton sendTeleportRequestButton;
	private GuiButton kickPlayerButton;
	private String playerName;
	private EntityPlayer player;
	private int xPos, yPos;
	private int screenWidth, screenHeight;
	private int panelWidth, panelHeight;
	
	public GuiPartyPlayerPanel(EntityPlayer player, int xPos, int yPos, int screenWidth, int screenHeight, int width, int height){
		this.player = player;
		this.playerName = player.getDisplayName();
		this.xPos = xPos;
		this.yPos = yPos;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.panelWidth = width;
		this.panelHeight = height;
		sendTeleportRequestButton = new GuiButton(0, this.xPos + width - 40, this.yPos, 10, 10, "T");
		kickPlayerButton = new GuiButton(1, this.xPos + width - 20, this.yPos, 10, 10, "X");
	}
	
	public void drawPanel(int x, int y, float f){
		this.drawPlayerHead(playerName, xPos, yPos - 3, Minecraft.getMinecraft(), false);
		this.drawString(Minecraft.getMinecraft().fontRenderer, playerName, this.xPos + 20, this.yPos, 0xFFFFFF);
		sendTeleportRequestButton.drawButton(Minecraft.getMinecraft(), x, y);
		kickPlayerButton.drawButton(Minecraft.getMinecraft(), x, y);
	}
	
	public void onUpdate(){
		
	}

	public void mouseClicked(int x, int y, int p_73864_3_) {
		if(sendTeleportRequestButton.mousePressed(Minecraft.getMinecraft(), x, y)){
			sendTeleportRequest();
		} else if(kickPlayerButton.mousePressed(Minecraft.getMinecraft(), x, y)){
			kickPlayer();
		}
	}
	
	private void sendTeleportRequest(){
		System.out.println("Teleport request send to " + this.playerName);
	}
	
	private void kickPlayer(){
		Core.getPacketPipeline().sendToServer(new KickPlayerFromPartyPacket(playerName));
	}
	
	private void drawPlayerHead(String nickname, int xPos, int yPos, Minecraft mc, boolean shadow){
		GL11.glPushMatrix();
		GL11.glColor4d(1, 1, 1, 1);
		ResourceLocation headTexture = AbstractClientPlayer.locationStevePng;
		headTexture = Utilities.getLocationSkull(nickname);
		AbstractClientPlayer.getDownloadImageSkin(headTexture, nickname);
		mc.getTextureManager().bindTexture(headTexture);
		GL11.glScalef(1.0F, 0.5F, 1.0F);
//		GL11.glScalef(1.75F, 0.75F, 1.0F);
		GL11.glScalef(0.4F, 0.4F, 0.5F);
		drawTexturedModalRect((int)(xPos / 0.4F), (int)(yPos / 0.20F), 32, 64, 32, 64);
		if(shadow){
			this.drawGradientRect(xPos, yPos, xPos + 32, yPos + 64, -1072689136, -804253680);
		}
//		GL11.glScalef(1.333333F, 1.333333F, 1.0F);
		GL11.glScalef(1.0F, 2.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
