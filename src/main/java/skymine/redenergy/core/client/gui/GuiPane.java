package skymine.redenergy.core.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import skymine.redenergy.core.client.gui.components.GuiComponent;

/**
 * Not yet implemented
 * @author RedEnergy
 */
@Deprecated
public class GuiPane extends GuiScreen {

	public List<GuiComponent> guiComponents;
	public boolean shouldPauseGame = false;
	
	public GuiPane(){
		guiComponents = new ArrayList();
	}
	
	public void attach(GuiComponent component){
		this.guiComponents.add(component);
	}
	
	public List<GuiComponent> getComponents(){
		return Collections.unmodifiableList(guiComponents);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		getComponents().forEach(c -> c.draw(mouseX, mouseY, renderPartialTicks));
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int typedIndex) {
		getComponents().forEach(c -> c.keyTyped(typedChar, typedIndex));
		super.keyTyped(typedChar, typedIndex);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		getComponents().forEach(c -> c.mouseClicked(mouseX, mouseY, mouseButton));
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		// TODO Auto-generated method stub
		super.actionPerformed(p_146284_1_);
	}

	@Override
	public void setWorldAndResolution(Minecraft p_146280_1_, int p_146280_2_, int p_146280_3_) {
		// TODO Auto-generated method stub
		super.setWorldAndResolution(p_146280_1_, p_146280_2_, p_146280_3_);
	}

	@Override
	public void initGui() {
		// TODO Auto-generated method stub
		super.initGui();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		super.handleInput();
	}

	@Override
	public void handleMouseInput() {
		// TODO Auto-generated method stub
		super.handleMouseInput();
	}

	@Override
	public void handleKeyboardInput() {
		// TODO Auto-generated method stub
		super.handleKeyboardInput();
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		super.updateScreen();
	}

	@Override
	public void onGuiClosed() {
		// TODO Auto-generated method stub
		super.onGuiClosed();
	}

	@Override
	public void drawDefaultBackground() {
		// TODO Auto-generated method stub
		super.drawDefaultBackground();
	}

	@Override
	public void drawWorldBackground(int p_146270_1_) {
		// TODO Auto-generated method stub
		super.drawWorldBackground(p_146270_1_);
	}

	@Override
	public void drawBackground(int p_146278_1_) {
		// TODO Auto-generated method stub
		super.drawBackground(p_146278_1_);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return shouldPauseGame;
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		// TODO Auto-generated method stub
		super.confirmClicked(p_73878_1_, p_73878_2_);
	}

}
