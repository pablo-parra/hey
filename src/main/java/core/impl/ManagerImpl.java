package core.impl;

import java.awt.event.ActionListener;
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
import utils.api.PropertyManager;

@Component("mainManager")
public class ManagerImpl implements Manager{
	private AbstractApplicationContext  context;
	
	private NetManager netManager;
	
	private FileManager fileManager;
	
	private PropertyManager propertyManager;
	
	final String URL = "url";
	
	final String TIMER = "timer";
	
	private boolean lastStatus;
	
	private boolean webIsAvailable; 
	
	public void start(){
		try {
			context = new AnnotationConfigApplicationContext(Main.class);
			
	        //Byname Autowiring
	        fileManager = (FileManager)context.getBean("fileManager");
	        netManager = (NetManager)context.getBean("NetManager");
	        propertyManager = (PropertyManager)context.getBean("propertyManager");
	        
			String url = fileManager.getConfigProperty(URL);
			lastStatus = propertyManager.getLastStatus();
			webIsAvailable = netManager.connectionAvailable(url);
			int loopTime = Integer.parseInt(fileManager.getConfigProperty(TIMER));
			
		    // Clase en la que está el código a ejecutar 
		     TimerTask timerTask = new TimerTask() 
		     { 
		         public void run()  
		         { 
		     		if(webIsAvailable != lastStatus){
		    			if(webIsAvailable){
		    				//notifyAvailability
		    				System.out.println("IS AVAILABLE");
		    				//change tray icon
		    			}else{
		    				//notifyUnavailability
		    				System.out.println("NOOOOOOO");
		    				//change try icon
		    			}
		    		}
		         } 
		     }; 


		      // Aquí se pone en marcha el timer cada segundo. 
		     Timer timer = new Timer(); 
		     // Dentro de 0 milisegundos avísame cada 1000 milisegundos 
		     timer.scheduleAtFixedRate(timerTask, 0, loopTime);
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if (context != null) context.close();
		}
        
        

        
	}
}
