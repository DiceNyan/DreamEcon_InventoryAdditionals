package skymine.redenergy.core.client.render;

import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ConfigurableEffect;
import org.newdawn.slick.font.effects.EffectUtil;
import org.newdawn.slick.opengl.TextureImpl;


import scala.actors.threadpool.Arrays;

/**
 * <b>Unstable</b> 
 * @author RedEnergy
 */
@Deprecated
public class SkyMineFont {
	
	private  Font font;
	private HashMap<Float, UnicodeFont> fonts;
	public float defaultFontSize;
	public Color defaultColor;
	
	public SkyMineFont(int fontType, float fontSizeByDefault, Color colorByDefault, ConfigurableEffect[] effects, ResourceLocation fontLocation, float ... sizes){
		fonts = new HashMap<Float, UnicodeFont>();
		try{
			font = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
		} catch(Exception e){
			e.printStackTrace();
		}
		for(float f : sizes){
			this.fonts.put(f, initFont(font, f * 4F, Arrays.asList(effects)));
		}
		if(this.fonts.get(fontSizeByDefault) == null){
			this.fonts.put(fontSizeByDefault, initFont(font, fontSizeByDefault * 4F, Arrays.asList(effects)));
		}
		defaultFontSize = fontSizeByDefault * 4F;
		defaultColor = colorByDefault;
	}
	
	public void renderString(String string, float x, float y){
		renderString(string, x, y, defaultFontSize);
	}
	
	public void renderString(String string, float x, float y, float size){
		renderString(string, x, y, size, defaultColor);
	}
	
	public void renderString(String string, float x, float y, float size, Color color){
		renderString(string, x, y, size, color, false, false);
	}
	
	public void renderString(String string, float x, float y, float size, Color color, boolean centered){
		renderString(string, x, y, size, color, centered, false);
	}
	public void renderString(String string, float x, float y, float size, Color color, boolean centered, boolean shadowed){
		if(fonts != null){
			TextureImpl.bindNone();
			GL11.glPushMatrix();
			GL11.glScalef(0.25F, 0.25F, 0.25F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if(fonts.get(size) != null){
				if(centered){
					x = x - ((fonts.get(size).getWidth(string) / 2) / 4F);
				}
				if(shadowed){
					fonts.get(size).drawString((x + 0.5F) * 4F, (y + 0.5F) * 4F, string, Color.black);
				}
				fonts.get(size).drawString(x * 4F, y * 4F, string, color);
			} else {
				System.out.println("RENDERING HELPER ERROR: NOT SUCH SIZE HAS BEEN INITIALIZED - " + String.valueOf(size));
			}
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glScalef(2.0F, 2.0F, 2.0F);
			GL11.glPopMatrix();
		}
	}
	
	
//	private void blur(BufferedImage image) {
//		float[] matrix = GAUSSIAN_BLUR_KERNELS[blurKernelSize - 1];
//		Kernel gaussianBlur1 = new Kernel(matrix.length, 1, matrix);
//		Kernel gaussianBlur2 = new Kernel(1, matrix.length, matrix);
//		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
//		ConvolveOp gaussianOp1 = new ConvolveOp(gaussianBlur1, ConvolveOp.EDGE_NO_OP, hints);
//		ConvolveOp gaussianOp2 = new ConvolveOp(gaussianBlur2, ConvolveOp.EDGE_NO_OP, hints);
//		BufferedImage scratchImage = EffectUtil.getScratchImage();
//		for (int i = 0; i < blurPasses; i++) {
//			gaussianOp1.filter(image, scratchImage);
//			gaussianOp2.filter(scratchImage, image);
//		}
//	}
	private static UnicodeFont initFont(Font fontToLoad, float size, List<ConfigurableEffect> effects){
		UnicodeFont font = new UnicodeFont(fontToLoad.deriveFont(size));
		font.addAsciiGlyphs();
		font.addGlyphs(32, 1200);
//		font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		font.getEffects().addAll(effects);
		try{
			font.loadGlyphs();
		} catch(SlickException e){
			e.printStackTrace();
		}
		return font;
	}
}
