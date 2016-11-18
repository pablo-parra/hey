package utils.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import utils.api.FileManager;

@Component("fileManager")
public class FileManagerImpl implements FileManager{
	
	final String CONFIG_FILE = "config/config.json";
	final String HEY_DIR = ".hey";
	//final String STATUS_FILE = "status.json";
	final String USER_DIR = "user.home";
	//final String STATUS_PROPERTY = "available";

	

//	public boolean getLastStatus() {
//		File statusFile = getStatusFile();
//		if (statusFile.exists()){
//			return getStatus(statusFile);
//		}else{
//			createStatusFile();
//			return false;
//		}
//	}
	

	
//	public boolean getStatus(File statusFile){
//		try {
//			JSONParser parser = new JSONParser();
//			InputStream is = new FileInputStream(statusFile);
//	        Object obj = parser.parse(IOUtils.toString(is));
//	        JSONObject jsonObject = (JSONObject) obj;
//	        String status = (String) jsonObject.get(STATUS_PROPERTY);
//	        return Boolean.parseBoolean(status); 
//		} catch (Exception e) {
//			System.out.println("ERROR IN getStatus: " + e.getMessage());
//			return false;
//		}
//	}
	
//	private File getStatusFile(){
//		try {
//			String currentUserHomeDir = System.getProperty(USER_DIR);
//			return new File(currentUserHomeDir + File.separator + HEY_DIR + File.separator + STATUS_FILE);
//			
//		} catch (Exception e) {
//			System.out.println("ERROR IN getStatusFile: " + e.getMessage());
//			return null;
//		}
//
//	}
	
	public Optional<Object> getConfigProperty(String propertyName){
		JSONParser parser = new JSONParser();
		Object property = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String result = IOUtils.toString(classLoader.getResourceAsStream(CONFIG_FILE));
	        Object obj = parser.parse(result);
	        JSONObject jsonObject = (JSONObject) obj;
	        property = jsonObject.get(propertyName);
	        return Optional.of(property);
			
		} catch (Exception e) {
			System.out.println("ERROR IN getConfigProperty: " + e.getMessage());
			return Optional.absent();
		}
		
	}
	
//	private void createStatusFile(){
//		try {
//			String currentUserHomeDir = System.getProperty(USER_DIR);
//			File heyDir = new File(currentUserHomeDir + File.separator + HEY_DIR);
//			if(!heyDir.exists()){
//				heyDir.mkdirs();	
//				Path statusPath = heyDir.toPath();
//		        String content = "{\"available\":false}";
//		          
//		        File statusFile = statusPath.resolve(STATUS_FILE).toFile();
//		        FileUtils.writeStringToFile(statusFile, content, "UTF-8");
//		        System.out.println("Created status file");
//			}
//		} catch (Exception e) {
//			System.out.println("ERROR IN createStatusFile: " + e.getMessage());
//		}
//	}
	
}
