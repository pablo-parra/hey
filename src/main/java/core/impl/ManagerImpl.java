package core.impl;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import core.Main;
import core.api.Manager;
import utils.api.FileManager;
import utils.api.NetManager;
import utils.api.NotificationManager;
import utils.api.PropertyManager;

import com.google.common.base.Optional;

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
			
	        //Byname Autowiring
	        fileManager = (FileManager)context.getBean("fileManager");
	        netManager = (NetManager)context.getBean("netManager");
	        propertyManager = (PropertyManager)context.getBean("propertyManager");
	        notificationManager = (NotificationManager)context.getBean("notificationManager");
	        
	        
	        Optional<String> urlProperty = (Optional<String>) fileManager.getConfigProperty(URL);
	        
	        if(urlProperty.isPresent()){

//				lastStatus = propertyManager.getLastStatus();
//				webIsAvailable = netManager.connectionAvailable(urlProperty.get().toString());
				
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
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally{
			if (context != null) context.close();
		}
        
        

        
	}

	private void launchHeyProcess(final String url, long loopInterval) {

		
	     TimerTask timerTask = new TimerTask() 
	     { 
	         public void run()  
	         { 
	     		lastStatus = propertyManager.getLastStatus();
	    		webIsAvailable = netManager.connectionAvailable(url);	
	    		
	     		if(webIsAvailable != lastStatus){
	     			
		    		System.out.println(LocalDateTime.now() + " | "+url+" | CHANGE REGISTERED --> WebIsAvailable: " + webIsAvailable + " -- LastStatus: " + lastStatus);
		    		
	    			if(webIsAvailable){

	    				System.out.println("IS AVAILABLE");
	    				notificationManager.notifyAvailability();
	    				lastStatus = true;
	    				//change tray icon
	    			}else{

	    				System.out.println("NOOOOOOO");
	    				notificationManager.notifyUnavailability();
	    				lastStatus = false;
	    				//change try icon
	    			}
	    			propertyManager.setLastStatus(lastStatus);
	    		}
	         } 
	     }; 
	     
	     Timer timer = new Timer(); 

	     timer.scheduleAtFixedRate(timerTask, 0, loopInterval);
		
	}

}
