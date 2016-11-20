package utils.api;

public interface PropertyManager {
	boolean getLastStatus();
	
	void setLastStatus(boolean status);
	
	String getProperty(String key);
	
	void setProperty (String key, String value);
}
