package su.jfdev.skymine.inventorymoney;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.reflect.FieldUtils;
import skymine.redenergy.core.api.annotations.SkyMineMod;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Jamefrus on 21.08.2015.
 */

@SkyMineMod
@Mod(modid = "InventoryMoney")
public class InventoryMoney {

    private static Logger logger = Logger.getLogger("InventoryMoney");


    @SidedProxy(clientSide = "su.jfdev.skymine.inventorymoney.ClientProxy", serverSide = "su.jfdev.skymine.inventorymoney.CommonProxy")
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;
    public static final boolean inDevEnv;

    static {
        boolean dev;
        try {
            dev = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        } catch (IOException e) {
            dev = false;
        }
        inDevEnv = dev;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("inventory_money");
        proxy.preInit(ev, network);
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiEnabled(GuiOpenEvent ev) {
        if (Minecraft.getMinecraft().isSingleplayer() || !(ev.gui instanceof GuiInventory)) return;
        InventoryMoney.network.sendToServer(new ResponseMessage.RequestMessage());
    }

    private static final ResourceLocation LOCATION = new ResourceLocation("invmoney", "gui/money.png");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextMoney(GuiScreenEvent.DrawScreenEvent ev) {
        if (
//                Minecraft.getMinecraft().isSingleplayer() ||
                !(ev.gui instanceof GuiInventory)) return;
        try {
            int guiLeft = (Integer) FieldUtils.readField(ev.gui, inDevEnv ? "guiLeft" : "field_147003_i", true);
            int guiTop = (Integer) FieldUtils.readField(ev.gui, inDevEnv ? "guiTop" : "field_147009_r", true);
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            double count = proxy.getMoneyCount(Minecraft.getMinecraft().thePlayer);
            String str = count > 9999999.99D ? String.valueOf((long) count) : String.valueOf(count);
            glDisable(GL_LIGHTING);
            guiTop -= 2;
            Minecraft.getMinecraft().getTextureManager().bindTexture(LOCATION);
            int r = 12;
            guiLeft += 8;
            int stringWidth = fr.getStringWidth(str);
            if (stringWidth > 60) return;
            this.drawTexturedModalRect(guiLeft + 92, guiTop + 68, 0, 0, r);
            this.drawTexturedModalRect(r + guiLeft + 84, guiTop + 67, stringWidth + 5, 14, 0);
            fr.drawString(str, r + guiLeft + 87, guiTop + 70, (Color.DARK_GRAY.getRGB()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void drawTexturedModalRect(int i, int i1, int i4, int i5, int r) {
        glPushMatrix();
        RenderHelper.disableStandardItemLighting();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        if (r != 0) {
            i -= r;

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (i), (double) (i1 + r), 1.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double) (i + r), (double) (i1 + r), 1.0D, 0.34D, 1.0D);
            tessellator.addVertexWithUV((double) (i + r), (double) (i1), 1.0D, 0.34D, 0.0D);
            tessellator.addVertexWithUV((double) (i), (double) (i1), 1.0D, 0.0D, 0.0D);
            tessellator.draw();
        } else {
            i--;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (i), (double) (i1 + i5), 0.0D, 0.46D, 1.0D);
            tessellator.addVertexWithUV((double) (i + 5), (double) (i1 + i5), 0.0D, 0.563D, 1.0D);
            tessellator.addVertexWithUV((double) (i + 5), (double) (i1), 0.0D, 0.563D, 0.0D);
            tessellator.addVertexWithUV((double) (i), (double) (i1), 0.0D, 0.46D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (i + 5), (double) (i1 + i5), 0.0D, 0.563D, 1.0D);
            tessellator.addVertexWithUV((double) (i + i4 - 2), (double) (i1 + i5), 0.0D, 0.9375D, 1.0D);
            tessellator.addVertexWithUV((double) (i + i4 - 2), (double) (i1), 0.0D, 0.9375D, 0.0D);
            tessellator.addVertexWithUV((double) (i + 5), (double) (i1), 0.0D, 0.563D, 0.0D);
            tessellator.draw();
            i += i4 - 2;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double) (i), (double) (i1 + i5), 0.0D, 0.9375D, 1.0D);
            tessellator.addVertexWithUV((double) (i + 3), (double) (i1 + i5), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double) (i + 3), (double) (i1), 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) (i), (double) (i1), 0.0D, 0.9375D, 0.0D);
            tessellator.draw();
        }
        glPopMatrix();
    }
}
