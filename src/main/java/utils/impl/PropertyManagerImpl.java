package utils.impl;

import org.springframework.stereotype.Component;

import utils.api.PropertyManager;

@Component("propertyManager")
public class PropertyManagerImpl implements PropertyManager{

	final String STATUS = "hey.status";
	
	public boolean getLastStatus(){
		String status = System.getProperty(STATUS);
		if(status == null){
			System.setProperty(STATUS, "false");
			return false;
		}else{
			return Boolean.parseBoolean(status);
		}
	}

	public void setLastStatus(boolean statusUpdated) {	
		System.setProperty(STATUS, statusUpdated ? "true" : "false");
	}

	public void setProperty(String key, String value) {
		System.setProperty(key, value);
		
	}

	public String getProperty(String key) {
		return System.getProperty(key);
	}

}
