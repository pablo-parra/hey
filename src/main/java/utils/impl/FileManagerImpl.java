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

import utils.api.FileManager;

//@Named("fm")
@Component("fileManager")
public class FileManagerImpl implements FileManager{
	
	final String CONFIG_FILE = "config/config.json";
	final String HEY_DIR = ".hey";
	final String STATUS_FILE = "status.json";
	final String USER_DIR = "user.home";
	

	public boolean getLastStatus() {
		if (statusFileExist()){
			return true;
		}else{
			createStatusFile();
			return false;
		}
	}
	
	private boolean statusFileExist(){
		try {
			String currentUserHomeDir = System.getProperty(USER_DIR);
			File statusFile = new File(currentUserHomeDir + File.separator + HEY_DIR + File.separator + STATUS_FILE);
			return statusFile.exists();
		} catch (Exception e) {
			System.out.println("ERROR IN statusFileExist: " + e.getMessage());
			return false;
		}

	}
	
	public String getConfigProperty(String propertyName){
		JSONParser parser = new JSONParser();
		
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String result = IOUtils.toString(classLoader.getResourceAsStream(CONFIG_FILE));
	        Object obj = parser.parse(result);
	        JSONObject jsonObject = (JSONObject) obj;
	        return (String) jsonObject.get(propertyName);
			
		} catch (Exception e) {
			System.out.println("ERROR IN getConfigProperty: " + e.getMessage());
			return null;
		}

	}
	
	private void createStatusFile(){
		try {
			String currentUserHomeDir = System.getProperty(USER_DIR);
			File heyDir = new File(currentUserHomeDir + File.separator + HEY_DIR);
			if(!heyDir.exists()){
				heyDir.mkdirs();	
				Path statusPath = heyDir.toPath();
		        String content = "{\"available\":false}";
		          
		        File statusFile = statusPath.resolve(STATUS_FILE).toFile();
		        FileUtils.writeStringToFile(statusFile, content, "UTF-8");
		        System.out.println("Created status file");
			}
		} catch (Exception e) {
			System.out.println("ERROR IN createStatusFile: " + e.getMessage());
		}
	}
	
}
