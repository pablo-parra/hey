package utils.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import utils.api.FileManager;

@Component("fileManager")
public class FileManagerImpl implements FileManager{
	
	final String CONFIG_FILE_RESOURCE = "resources/config/config.json";
	final String CONFIG_FILE = "config.json";
	final String HEY_DIR = ".hey";
	final String USER_DIR = "user.home";
	
	public Optional<Object> getConfigProperty(String propertyName){
		JSONParser parser = new JSONParser();
		Object property = null;
		try {
			File configFile = new File(System.getProperty(USER_DIR) + File.separator + HEY_DIR + File.separator + CONFIG_FILE);
			
			if(!configFile.exists()){
				Optional<File> newConfigFile = createConfigFile();
				if (! newConfigFile.isPresent()){
					throw new FileNotFoundException("The config file could not be created.");
				}
				configFile = newConfigFile.get();
			}
			
			InputStream config = new FileInputStream(configFile);
			String result = IOUtils.toString(config);
	        Object obj = parser.parse(result);
	        JSONObject jsonObject = (JSONObject) obj;
	        property = jsonObject.get(propertyName);
	        return Optional.of(property);
			
		} catch (Exception e) {
			System.out.println("ERROR IN getConfigProperty: " + e.getMessage());
			return Optional.absent();
		}
		
	}
	
	private Optional<File> createConfigFile(){
		InputStream in = null;
		OutputStream out = null;
		
		try {
			File heyDir = new File(System.getProperty(USER_DIR) + File.separator + HEY_DIR);
			if (!heyDir.exists()){
				heyDir.mkdirs();
			}
			
			Path statusPath = heyDir.toPath();
			File configFile = statusPath.resolve(CONFIG_FILE).toFile();
			ClassLoader classLoader = getClass().getClassLoader();
	
			in = classLoader.getResourceAsStream(CONFIG_FILE_RESOURCE);
			out = new FileOutputStream(configFile, true);
			
			IOUtils.copy(in, out);
			
			System.out.println("Config File created");
			
			return Optional.of(configFile);
		}catch(Exception e){
			System.out.println("ERROR IN createConfigFile: " + e.getMessage());
			return Optional.absent();
		}
		finally {
			if (in != null ) IOUtils.closeQuietly(in);
			if (out != null) IOUtils.closeQuietly(out);
		}
		  
	}
	
}
