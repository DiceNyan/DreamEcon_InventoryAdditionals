package skymine.redenergy.core.client.keyboard;

import java.util.HashMap;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.settings.KeyBinding;

/**
 * Handles KeyInputEvent and executes buttons
 * @author RedEnergy
 */
public class KeyboardHandler {
	
	/**
	 * Map with key bindings
	 */
	private HashMap<KeyBinding, KeyboardRunnable> keys;
	
	public KeyboardHandler(){
		keys = new HashMap<KeyBinding, KeyboardRunnable>();
	}
	
	/**
	 * @param key - key code
	 * @param descr - description of a button
	 * @param action - runnable, which will be executed on key pressed
	 */
	public void registerKeyBinding(int key, String descr, KeyboardRunnable action){
		setKeyBindingAction(buildKeyBinding(key, descr), action);
	}
	
	/**
	 * @param key
	 * @param descr
	 * @return KeyBinding object
	 */
	public KeyBinding buildKeyBinding(int key, String descr){
		return new KeyBinding(descr, key, "SkyMine");
	}
	
	/**
	 * @param keybinding
	 * @param action which would be executed on <b>keybinding</b> pressed
	 */
	public void setKeyBindingAction(KeyBinding keybinding, KeyboardRunnable action){
		keys.put(keybinding, action);
	}
	
	
	/**
	 * Registers all key binds in ClientRegistry
	 */
	public void initialise(){
		for(KeyBinding bind : keys.keySet()){
			ClientRegistry.registerKeyBinding(bind);
		}
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent e){
		for(KeyBinding bind : keys.keySet()){
			if(bind.getIsKeyPressed()){
				KeyboardRunnable run = keys.get(bind);
				if(run != null){
					run.onKeyInput(e);
				}
			}
		}
	}
	
	@FunctionalInterface
	public interface KeyboardRunnable{
		/**
		 * @param event KeyInputEvent (remember, it cannot be canceled)
		 */
		void onKeyInput(KeyInputEvent event);
	}
}
