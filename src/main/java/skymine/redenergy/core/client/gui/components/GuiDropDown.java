package skymine.redenergy.core.client.gui.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiDropDown extends Gui
{
    /**
     * The element map of the dropdownList
     */
    protected Map<String, DropdownListElement> data = new TreeMap<String, DropdownListElement>();

    protected final Minecraft minecraft;
    protected int xPosition;
    protected int yPosition;
    protected int width;
    protected int height;
    protected String displayString = null;

    protected String selectedItem = null;
    protected String hoverItem = null;
    protected final int stringWidth;
    
    protected boolean isUnroll = false;

    /**
     * Specify if it would drawn the dropdownList
     */
    public boolean drawdropdownList = true;
    
    /**
     * If the dropdownList isn't enabled you can't interact whith
     */
    public boolean enabled = true;

    public GuiDropDown(Minecraft minecraft, int x, int y)
    {
        this(minecraft, x, y, 75);
    }
    
    public GuiDropDown(Minecraft minecraft, int x, int y, int width)
    {
        this.minecraft = minecraft;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width >= 20 ? width : 20;
        this.height = 12;
        this.stringWidth = this.width - 14;
    }
    
    /**
     * Set the string to display under the dropdownList
     * @param displayString
     * @return
     */
    public GuiDropDown setDisplayString(String displayString)
    {
        this.displayString = displayString;
        return this;
    }
    
    /**
     * Add an item to the dropdownList and set him to default selected item
     * @param value
     */
    public void addItemAndSetDefault(Object value)
    {
        this.addItemAndSetDefault(String.valueOf(value), value);
    }
    
    /**
     * Add an item to the dropdownList and set him to default selected item
     * @param name
     * @param value
     */
    public void addItemAndSetDefault(String name, Object value)
    {
        this.addItem(name, value);
        this.setDefaultItem(String.valueOf(name));
    }
    
    /**
     * Add an item to the dropdownList
     * @param value
     */
    public void addItem(Object value)
    {
        this.addItem(String.valueOf(value), value);
    }
    
    /**
     * Add an item to the dropdownList
     * @param value
     */
    public void addItem(String name, Object value)
    {
        this.data.put(name, new DropdownListElement(this.data.size(), value, name));
    }
    
    /**
     * Set the default selected item of the list
     * Throw an exception if the item in argument is not present in the dropdownList
     * @param name
     */
    private void setDefaultItem(String name)
    {
        if(this.data.containsKey(name))
        {
        	this.selectedItem = name;        	
        }
    }
    
    /**
     * Remove an item from the dropdownList
     * Throw an exception if the item in argument is not present in the dropdownList
     * @param name
     */
    public void removeItem(String name)
    {
        if(!this.data.containsKey(name))
        {
        	return;
        }
        
        this.data.remove(name);
    }
    
    /**
     * Clear all items in the dropdownList
     */
    public void clearItems()
    {
        this.data.clear();
        this.isUnroll = false;
        this.selectedItem = null;
    }
    
    /**
     * Return the current selected item of the dropdownList
     * If they are no selected item return a empy item
     * @return
     */
    public DropdownListElement getSelectedItem()
    {
        return this.data.containsKey(this.selectedItem) ? this.data.get(this.selectedItem) : new DropdownListElement(-1, -1, "");
    }
    
    /**
     * Draw the dropdownList to the screen
     * @param mouseX
     * @param mouseY
     * @param partialTick
     */
    public void drawScreen(int mouseX, int mouseY, float partialTick)
    {
        if(this.data.isEmpty())
        {
            this.enabled = false;
        }
        
        if(this.drawdropdownList )
        {
            boolean isHover = mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition && mouseY < this.yPosition + this.height;
            
            this.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
            this.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width - 13, this.yPosition + this.height, -16777216);

            GL11.glPushMatrix();
            GL11.glColor4f(1F, 1F, 1F, 1F);
            this.minecraft.renderEngine.bindTexture(new ResourceLocation("textures/gui/widgets.png"));
            this.drawTexturedModalRect(this.xPosition + this.width - 12, this.yPosition, 0, this.enabled ? isHover ? 86 : 66 : 46, 6, 6);
            this.drawTexturedModalRect(this.xPosition + this.width - 6, this.yPosition, 194, this.enabled ? isHover ? 86 : 66 : 46, 6, 6);
            this.drawTexturedModalRect(this.xPosition + this.width - 12, this.yPosition + 6, 0, this.enabled ? isHover ? 100 : 80 : 60, 6, 6);
            this.drawTexturedModalRect(this.xPosition + this.width - 6, this.yPosition + 6, 194, this.enabled ? isHover ? 100 : 80 : 60, 6, 6);
            
            GL11.glTranslated(0D, 0D, 0D);
            GL11.glRotatef(0F, 0F, 1F, 0F);
            this.drawCenteredString(this.minecraft.fontRenderer, this.isUnroll ? "<" : ">", this.xPosition + this.width - 6, this.yPosition + this.height / 8, 0xffffffff);
            GL11.glPopMatrix();
            
            if(this.isUnroll)
            {            
                ArrayList<String> entrySet = this.getDataKey();
                
                int unrollLenght = entrySet.size() * this.height;
            
                this.drawRect(this.xPosition - 1, this.yPosition + this.height, this.xPosition + this.width + 1, this.yPosition + this.height + unrollLenght + 1, -6250336);//-6250336);
                this.drawRect(this.xPosition, this.yPosition + this.height + 1, this.xPosition + this.width, this.yPosition + this.height + unrollLenght, -16777216);//-6250336);

                boolean isHoverList = mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition + this.height && mouseY < this.yPosition+ this.height + unrollLenght + 1;
                
                for(int index = 0; index < entrySet.size(); index++)
                {
                    String itemDisplayString = entrySet.get(index);
                    int xPosition = this.xPosition;
                    int yPosition = this.yPosition + this.height + (this.height / 8) + (index * 12);
                    
                    boolean isHoverItem = mouseX >= xPosition && mouseX < xPosition + width && mouseY >= yPosition && mouseY < yPosition + 12;
                    
                    this.hoverItem = isHoverItem ? String.valueOf(itemDisplayString) : null;
    
                    if(this.hoverItem != null && this.hoverItem.equals(itemDisplayString))
                    {
                        this.drawRect(xPosition, yPosition, xPosition + this.width, yPosition + 11, 0xffffffff);
                        this.minecraft.fontRenderer.drawString(this.minecraft.fontRenderer.trimStringToWidth(itemDisplayString, this.width - 2), xPosition + 2, yPosition + (11 / 8), 0);
                    }
                    else
                    {
                        this.minecraft.fontRenderer.drawString(this.minecraft.fontRenderer.trimStringToWidth(itemDisplayString, this.width - 2), xPosition + 2, yPosition + (11 / 8), 0xffffffff);
                        
                        if(!isHoverList)
                        {
                            if(this.selectedItem != null && this.selectedItem.equals(itemDisplayString))
                            {
                                this.drawRect(xPosition, yPosition, xPosition + this.width, yPosition + 11, 0xffffffff);
                                this.minecraft.fontRenderer.drawString(this.minecraft.fontRenderer.trimStringToWidth(itemDisplayString, this.width - 2), xPosition + 2, yPosition + (11 / 8), 0);
                            }
                        }
                    }
                }
            }
            
            if(this.isItemSelected())
            {
                String selectedItemName = this.minecraft.fontRenderer.trimStringToWidth(this.selectedItem, stringWidth);
                this.drawString(this.minecraft.fontRenderer, selectedItemName, this.xPosition + 2, this.yPosition + this.height / 8, 0xffffffff);
            }
            
            if(this.displayString != null)
            {
                this.drawString(this.minecraft.fontRenderer, this.displayString + " :", this.xPosition, this.yPosition - 11, 0xffffffff);
            }
        }
    }
    
    /**
     * Fired when mouse is clickin. Call it from your guiscreen
     * @param mouseX
     * @param mouseY
     * @param button
     */
    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        if(this.enabled)
        {
            boolean isHoverComponent = this.isUnroll ? mouseX >= this.xPosition - 1 && mouseX < this.xPosition + this.width + 1 && mouseY >= this.yPosition -1 && mouseY < this.yPosition + this.height + (this.getDataKey().size() * 12) - 1 : mouseX >= this.xPosition - 1 && mouseX < this.xPosition + this.width + 1 && mouseY >= this.yPosition - 1&& mouseY < this.yPosition + this.height + 1;
    
            if(!isHoverComponent)
            {
                this.isUnroll = false;
            }
            else
            {
                boolean isHover = mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition && mouseY < this.yPosition + this.height;
                
                if(isHover && !this.data.isEmpty())
                {
                    this.isUnroll = !this.isUnroll;
                }
                
                if(this.isUnroll)
                {
                    ArrayList<String> entrySet = this.getDataKey();
                                    
                    for(int index = 0; index < entrySet.size(); index++)
                    {
                        int xPosition = this.xPosition;
                        int yPosition = this.yPosition + this.height + (this.height / 8) + (index * 12);
                        
                        boolean isHoverItem = mouseX >= xPosition && mouseX < xPosition + width && mouseY >= yPosition && mouseY < yPosition + 12;
                        
                        if(isHoverItem)
                        {
                            this.selectedItem = entrySet.get(index);
                            this.isUnroll = false;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Return true if they are a selected item
     * @return
     */
    public boolean isItemSelected()
    {
        return this.selectedItem != null;
    }
    
    private ArrayList<String> getDataKey()
    {
        return new ArrayList(this.data.keySet());
    }
    
    public class DropdownListElement
    {
        private final int itemIndex;
        private final Object itemValue;
        private final String itemName;
        
        public DropdownListElement(int itemIndex, Object itemValue, String itemName)
        {
            this.itemIndex = itemIndex;
            this.itemValue = itemValue;
            this.itemName = itemName;
        }
        
        /**
         * Return the value of this element in the dropdownList
         * @return
         */
        public Object getItemValue()
        {
            return this.itemValue;
        }
        
        /**
         * Return the index of this element in the dropdownList
         * @return
         */
        public int getItemIndex()
        {
            return this.itemIndex;
        }
        
        /**
         * Return the name of this element in the dropdownList
         * @return
         */
        public String getItemName()
        {
            return this.itemName;
        }
    }
}