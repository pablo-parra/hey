package core.impl;

import javax.inject.Inject;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import core.Main;
import core.api.Manager;
import utils.api.FileManager;
import utils.api.NetManager;
import utils.api.PropertyManager;

public class ManagerImpl implements Manager{
	
	private NetManager netManager;
	
	private FileManager fileManager;
	
	private PropertyManager propertyManager;
	
	final String URL = "url";
	
	public void start(){
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);
        
        //Byname Autowiring
        fileManager = (FileManager)context.getBean("fileManager");
        netManager = (NetManager)context.getBean("NetManager");
        propertyManager = (PropertyManager)context.getBean("propertyManager");
        
		String url = fileManager.getConfigProperty(URL);
		boolean lastStatus = propertyManager.getLastStatus();
		boolean webIsAvailable = netManager.connectionAvailable(url);
		
		if(webIsAvailable != lastStatus){
			if(webIsAvailable){
				//notifyAvailability
				
				//change tray icon
			}else{
				//notifyUnavailability
				//change try icon
			}
		}
	}
}
