package org.dreamfinity.party.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import org.dreamfinity.party.client.ClientData;
import org.dreamfinity.party.utils.Utilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiInGame extends Gui{

	private static final ResourceLocation texture = new ResourceLocation("party", "textures/gui/bar.png");
	public FontRenderer font;
	
	@SubscribeEvent
	public void drawScreen(RenderGameOverlayEvent.Post event){
		Minecraft mc = Minecraft.getMinecraft();
		if(event.type != ElementType.EXPERIENCE){return;}
		if(ClientData.clientParty == null){return;}
		int additionalY = 0;
		font = mc.fontRenderer;
		int xPos = 100;
		int yPos = 100;
		for(EntityPlayer player: ClientData.clientParty.getPlayers()){
			this.drawPlayerInfo(mc, player, xPos, yPos, additionalY);
			additionalY += 38;
		}
	
	}
	
	private void drawPlayerInfo(Minecraft mc, EntityPlayer player, int xPos, int yPos, int additionalY){
		float healthvalue = player.getHealth();
		drawPlayerHead(player.getDisplayName(), xPos - 34, yPos - 9 + additionalY, mc, healthvalue <= 0.0);
		mc.getTextureManager().bindTexture(texture);
		drawHeadBorder(xPos - 1, yPos - 3, additionalY);
		drawHealth(healthvalue, player.getMaxHealth(), xPos, yPos, additionalY);
		drawArmor(player.getTotalArmorValue(), 20, xPos, yPos, additionalY);
		if(player == ClientData.clientParty.getLeader()){
			drawCrown(xPos - 4, yPos - 3);
		}
		font.drawStringWithShadow(player.getDisplayName(), xPos, yPos - 8 + additionalY, 0xFFFFFF);
	}
	
	private void drawHealth(float currentHealth, float maxHealth, int xPos, int yPos, int additionalY){
		drawTexturedModalRect(xPos + 9, yPos + 3+ additionalY, 0, 0, 56, 9);
		int healthbarwidht = (int)(( currentHealth / maxHealth) * 49);
		drawTexturedModalRect(xPos + 12, yPos + 6 + additionalY, 0, 12, healthbarwidht, 3);
		drawTexturedModalRect(xPos, yPos + 4 + additionalY, 0, 47, 7, 7);
		if(currentHealth <= 0.0){
			drawTexturedModalRect(xPos - 27, yPos - 7 + additionalY, 0, 55, 17, 22);
		}
	}
	
	private void drawHeadBorder(int xPos, int yPos, int additionalY){
		GL11.glPushMatrix();
		GL11.glScalef(1.05F, 1.03F, 1F);
		drawTexturedModalRect((int)((xPos - 34) / 1.05F ), (int)((yPos- 7 + additionalY) / 1.03F), 0, 15, 33, 32);
		GL11.glPopMatrix();
	}
	private void drawArmor(int currentArmor, int maxArmor, int xPos, int yPos, int additionalY){
		if(!(currentArmor <= 0)){
			drawTexturedModalRect(xPos + 9, yPos + 13 + additionalY, 0, 0, 56, 9);
			int armorbarwidth = (int)(((float) currentArmor / maxArmor) * 49);
			drawTexturedModalRect(xPos + 12, yPos + 16 + additionalY, 0, 9, armorbarwidth, 3);
			drawTexturedModalRect(xPos - 1, yPos + 13 + additionalY, 7, 47, 9, 9);
		}
	}
	
	private void drawCrown(int xPos, int yPos){
		drawTexturedModalRect(xPos - 21, yPos - 17, 0, 79, 19, 7);
	}
	
	private void drawPlayerHead(String nickname, int xPos, int yPos, Minecraft mc, boolean shadow){
		GL11.glPushMatrix();
		ResourceLocation headTexture = AbstractClientPlayer.locationStevePng;
		headTexture = Utilities.getLocationSkull(nickname);
		AbstractClientPlayer.getDownloadImageSkin(headTexture, nickname);
		mc.getTextureManager().bindTexture(headTexture);
		GL11.glScalef(0.95F, 0.475F, 1.0F);
		drawTexturedModalRect((int)(xPos / 0.95F), (int)(yPos / 0.475F), 32, 64, 32, 64);
		if(shadow){
			this.drawGradientRect((int)(xPos / 0.95F), (int)(yPos / 0.475F), (int)(xPos / 0.95F) + 32, (int)(yPos / 0.475F) + 64, -1072689136, -804253680);
		}
		GL11.glPopMatrix();
	}
		
}
