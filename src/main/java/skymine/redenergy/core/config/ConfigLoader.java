package skymine.redenergy.core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import skymine.redenergy.core.config.ConfigProp.ConfigSide;
import skymine.redenergy.core.logging.SkyMineLogger;


public class ConfigLoader {

	protected boolean updateConfig = false;
	protected final File dir;
	protected final String fileName;
	protected final Class configClass;
	protected final List<Field> configFields;
	
	public ConfigLoader(Class classToLoad, File dir, String fileName) {
		this.dir = dir;
		if(!this.dir.exists()){
			this.dir.mkdir();
		}
		
		this.configClass = classToLoad;
		this.fileName = fileName + ".cfg";
		List<Field> fields = Arrays.asList(this.configClass.getDeclaredFields());
		configFields = new ArrayList<Field>();
		fields.forEach(f -> {
			if(f.isAnnotationPresent(ConfigProp.class)){
				ConfigProp prop = f.getAnnotation(ConfigProp.class);
				if(prop.side() == ConfigSide.ANY || prop.side().ordinal() == FMLCommonHandler.instance().getEffectiveSide().ordinal()){
					configFields.add(f);
					SkyMineLogger.debug("Prop %s has been successfully loaded on %s side", f.getName(), FMLCommonHandler.instance().getEffectiveSide().name());
				} else {
					SkyMineLogger.debug("Prop %s can't be loaded on %s side, skiped.", f.getName(), FMLCommonHandler.instance().getEffectiveSide().name());
				}
			}
		});
	}
	
	public void loadConfig(){
		try{
			File file = new File(this.dir, this.fileName);
			HashMap types = new HashMap();
			this.configFields.forEach(f -> {
				ConfigProp type = f.getAnnotation(ConfigProp.class);
				types.put(type.name().isEmpty() ? f.getName() : type.name(), f);
			});
			
			if(file.exists()){
				HashMap props = parseConfig(file, types);
				props.forEach((key, value) -> {
					try{
						((Field) types.get(key)).set((Object)null, value);
					} catch(Exception ex){
						ex.printStackTrace();
					}});
				
				types.forEach((key, value) ->{
					if(!props.containsKey(key)){
						this.updateConfig = true;
					}
				});
			} else {
				this.updateConfig = true;
			}
		} catch(Exception ex){
			this.updateConfig = true;
			ex.printStackTrace();
		}
		
		if(this.updateConfig){
			this.updateConfig();
		}
		this.updateConfig = false;
	}
	
	protected HashMap parseConfig(File file, HashMap types) throws IOException{
		HashMap config = new HashMap();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.lines().forEach(line -> {
			if(!line.startsWith("#") && !(line.length() <= 0)){
				if(line.indexOf("=") > 0 && line.indexOf("=") != line.length()){
					String[] partsOfLine = StringUtils.split(line, '=');
					String name = partsOfLine[0];
					String value = partsOfLine[1];
					if(!types.containsKey(name)){
						updateConfig = true;
					} else {
						Class clazz = ((Field)types.get(name)).getType();
						Object obj = getObjectDependingOnClass(clazz, value);
						if(obj != null){
							config.put(name, obj);
						}
					}
				} else {
					this.updateConfig = true;
				}
			}
		});
		reader.close();
		return config;
		
	}
	
	private static Object getObjectDependingOnClass(Class clazz, String value){
		Object obj = null;
	    if(clazz.isAssignableFrom(String.class)) {
            obj = value;
         } else if(clazz.isAssignableFrom(Integer.TYPE)) {
            obj = Integer.valueOf(Integer.parseInt(value));
         } else if(clazz.isAssignableFrom(Short.TYPE)) {
            obj = Short.valueOf(Short.parseShort(value));
         } else if(clazz.isAssignableFrom(Byte.TYPE)) {
            obj = Byte.valueOf(Byte.parseByte(value));
         } else if(clazz.isAssignableFrom(Boolean.TYPE)) {
            obj = Boolean.valueOf(Boolean.parseBoolean(value));
         } else if(clazz.isAssignableFrom(Float.TYPE)) {
            obj = Float.valueOf(Float.parseFloat(value));
         } else if(clazz.isAssignableFrom(Double.TYPE)) {
            obj = Double.valueOf(Double.parseDouble(value));
         } else if(clazz.isAssignableFrom(Map.class)){
        	 obj = new Gson().fromJson(value, Map.class);
         } else if(clazz.isAssignableFrom(List.class)){
        	 obj = new Gson().fromJson(value, List.class);
         }
	    return obj;
	}
	
	
	private static String getValueDependingOnClass(Class clazz, Field field) throws IllegalArgumentException, IllegalAccessException{
		String value = "null";
		if(clazz.isAssignableFrom(Map.class)){
			value = new Gson().toJson(field.get((Object)null));
		} else if(clazz.isAssignableFrom(List.class)){
			value = new Gson().toJson(field.get((Object)null));
		} else {
			value = field.get((Object)null).toString();
		}
		return value;
	}
	public void updateConfig(){
		File file = new File(this.dir, this.fileName);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			this.configFields.forEach(field -> {
				try {
				ConfigProp prop = field.getAnnotation(ConfigProp.class);
				if(!prop.info().isEmpty()){
					writer.write("#" + prop.info() + System.getProperty("line.separator"));
				}
				String name = prop.name().isEmpty() ? field.getName() : prop.name();
				String value = getValueDependingOnClass(field.getType(), field);
				writer.write(name + "=" + value + System.getProperty("line.separator"));
				writer.write(System.getProperty("line.separator"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

}
