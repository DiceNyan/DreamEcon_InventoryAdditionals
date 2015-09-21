package skymine.redenergy.core.client.gui.components;

import net.minecraft.client.gui.Gui;

public abstract class GuiComponent extends Gui {
	
	public abstract void draw(int mouseX, int mouseY, float partialTicks);
	
	public abstract void keyTyped(char typedChar, int typedIndex);
	
	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
}
