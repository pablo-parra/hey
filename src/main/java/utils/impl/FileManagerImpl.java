package utils.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import utils.api.FileManager;

public class FileManagerImpl implements FileManager{
	
	final String CONFIG = "config.properties";
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

	public String getAppProperty(String propertyName) {
		String response = null;
		try {
			Properties prop = new Properties();
			InputStream config = null;

				config = new FileInputStream(CONFIG);

				// load a properties file
				prop.load(config);
				response = prop.getProperty(propertyName);
				return response;
			
		} catch (Exception e) {
			System.out.println("ERROR IN getAppProperty: " + e.getMessage());
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
		        String content = "{\"available\":true}";
		          
		        File statusFile = statusPath.resolve(STATUS_FILE).toFile();
		        FileUtils.writeStringToFile(statusFile, content, "UTF-8");
		        System.out.println("Created status file");
			}
		} catch (Exception e) {
			System.out.println("ERROR IN createStatusFile: " + e.getMessage());
		}
	}
	
}
