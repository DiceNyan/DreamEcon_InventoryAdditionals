package skymine.redenergy.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cpw.mods.fml.relauncher.Side;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProp {
	
	String name() default "";
	
	String info() default "";
	
	ConfigSide side() default ConfigSide.ANY;

	public static enum ConfigSide {
		CLIENT,
		SERVER,
		ANY;
	}
}
