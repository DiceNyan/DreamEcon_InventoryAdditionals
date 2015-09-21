package org.dreamfinity.party.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class Utilities {
	public static ResourceLocation getLocationSkull(String par0Str)
	{
		return new ResourceLocation("skull/" + StringUtils.stripControlCodes(par0Str));
	}
}
