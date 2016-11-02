package core.impl;

import javax.inject.Inject;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import core.Main;
import core.api.Manager;
import utils.api.FileManager;
import utils.api.NetManager;

public class ManagerImpl implements Manager{
	
	private NetManager netManager;
	
	private FileManager fileManager;
	
	final String URL = "url";
	
	public void start(){
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);
        
        //Byname Autowiring
        fileManager = (FileManager)context.getBean("fileManager");
        netManager = (NetManager)context.getBean("NetManager");
        
		String url = fileManager.getConfigProperty(URL);
		boolean lastStatus = fileManager.getLastStatus();
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
