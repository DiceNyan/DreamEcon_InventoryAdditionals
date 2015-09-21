package skymine.redenergy.core.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Objects;

/**
 * @author RedEnergy
 */
public class SkyMineLogger {
	public static Logger logger;
	public static boolean debugEnabled;
	public static final String PREFIX = "SkyMine";
	
	public static void setLogger(Logger log){
		logger = log;
	}
	
	public static void setDebugEnabled(boolean deb){
		debugEnabled = deb;
	}
	
	public static void info(String message, Object ... objects){
		info(String.format(message, objects));
	}
	
	public static void debug(String message, Object ... objects){
		debug(String.format(message, objects));
	}
	
	public static void error(String message, Object ... objects){
		error(String.format(message, objects));
	}
	
	public static void info(String message){
		if(logger != null){
			logger.log(Level.INFO, new StringBuffer().append(" ").append(message).toString());
		}
	}
	
	public static void debug(String message){
		if(debugEnabled && logger != null){
			logger.log(Level.INFO, new StringBuffer().append("[DEBUG]").append(" ").append(message).toString());
		}
	}
	
	public static void error(String message){
		if(logger != null){
			logger.log(Level.INFO, new StringBuffer().append("[ERROR]").append(" ").append(message).toString());
		}
	}
}
