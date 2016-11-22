package core.impl;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import core.Main;
import core.api.Manager;
import utils.api.FileManager;
import utils.api.NetManager;
import utils.api.NotificationManager;
import utils.api.PropertyManager;

@Component("mainManager")
public class ManagerImpl implements Manager{
	private AbstractApplicationContext  context;
	
	private NetManager netManager;
	
	private FileManager fileManager;
	
	private PropertyManager propertyManager;
	
	private NotificationManager notificationManager;
	
	final String URL = "url";
	
	final String TIMER = "timer";
	
	private boolean lastStatus;
	
	private boolean webIsAvailable; 
	
	public void start(){
		try {
			context = new AnnotationConfigApplicationContext(Main.class);
	        fileManager = (FileManager)context.getBean("fileManager");
	        netManager = (NetManager)context.getBean("netManager");
	        propertyManager = (PropertyManager)context.getBean("propertyManager");
	        notificationManager = (NotificationManager)context.getBean("notificationManager");
	        
	        
	        Optional<String> urlProperty = (Optional<String>) fileManager.getConfigProperty(URL);
	        
	        if(urlProperty.isPresent()){
				propertyManager.setProperty(URL, urlProperty.get());
				
				Optional<Long> timerProperty = (Optional<Long>) fileManager.getConfigProperty(TIMER);
				
				if (timerProperty.isPresent()){

					launchHeyProcess(urlProperty.get().toString(), Long.parseLong(timerProperty.get().toString()));

				}else{
					System.out.println("No TIMER parameter defined in the config.json file");
				}

	        }else{
	        	// TODO: implement Logger?
	        	System.out.println("No URL parameter defined in the config.json file");
	        }

			
		} catch (Exception e) {
			// TODO: implement LOG
			System.out.println(e.getMessage());
		}finally{
			if (context != null) context.close();
		}
        
        

        
	}

	private void launchHeyProcess(final String url, long loopInterval) {

		System.out.println("URL TO CHECK: "+ url);
		System.out.println("INTERVAL: " + loopInterval);
		
		//Initial check
		if(!netManager.connectionAvailable(url)){
			notificationManager.notifyUnavailability();
		}
		
		// Loop
	     TimerTask timerTask = new TimerTask() 
	     { 
	         public void run()  
	         { 
	     		lastStatus = propertyManager.getLastStatus();
	    		webIsAvailable = netManager.connectionAvailable(url);	
	    		
	     		if(webIsAvailable != lastStatus){
	     			
		    		System.out.println(LocalDateTime.now() + " | "+url+" | CHANGE REGISTERED --> WebIsAvailable: " + webIsAvailable + " -- LastStatus: " + lastStatus);
		    		
	    			if(webIsAvailable){
	    				notificationManager.notifyAvailability();
	    				lastStatus = true;
	    				
	    			}else{
	    				notificationManager.notifyUnavailability();
	    				lastStatus = false;
	    			}
	    			propertyManager.setLastStatus(lastStatus);
	    		}
	         } 
	     }; 
	     
	     Timer timer = new Timer(); 

	     timer.scheduleAtFixedRate(timerTask, 0, loopInterval);
		
	}

}
