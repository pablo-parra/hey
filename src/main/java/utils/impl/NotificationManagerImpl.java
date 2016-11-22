package utils.impl;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import core.Main;
import utils.api.FileManager;
import utils.api.NetManager;
import utils.api.NotificationManager;
import utils.api.PropertyManager;

@Component("notificationManager")
public class NotificationManagerImpl implements NotificationManager {

	String IMG_OK = "/resources/img/ok.png";
	String IMG_KO = "/resources/img/ko.png";
	private String AVAILABLE = "The site is available.";
	private String UNAVAILABLE = "The site is unavailable";
	private String HEY = "HEY";
	private String URL = "url";
	private String USER_DIR = "user.home";
	
	private AbstractApplicationContext  context;	
	private PropertyManager propertyManager;
	
	public void notifyAvailability() {
		Optional<TrayIcon> heyIcon = getHeyIcon();
		
		if(heyIcon.isPresent()){
			changeHeyIcon(heyIcon.get(), true);
		}else{
			Optional<TrayIcon> newHeyIcon = createHeyIcon(true); 
			heyIcon = newHeyIcon;
		}
		
		showNotification(heyIcon.get(), true);

	}


	public void notifyUnavailability() {
		Optional<TrayIcon> heyIcon = getHeyIcon();
		
		if(heyIcon.isPresent()){
			changeHeyIcon(heyIcon.get(), false);
		}else{
			Optional<TrayIcon> newHeyIcon = createHeyIcon(false); 
			heyIcon = newHeyIcon;
		}
		
		showNotification(heyIcon.get(), false);

	}
	
	
	private Optional<TrayIcon> getHeyIcon(){
		TrayIcon heyIcon = null;
		try {
			SystemTray tray = SystemTray.getSystemTray();
			TrayIcon[] icons = tray.getTrayIcons();
			for (TrayIcon trayIcon : icons) {
				if (trayIcon.getToolTip().equals(HEY)) heyIcon = trayIcon;
			}
			return Optional.of(heyIcon);
			
		} catch (Exception e) {
			//System.out.println("No HEY icon...");
			return Optional.absent();
		}

	}
	
	private Optional<TrayIcon> createHeyIcon(boolean webIsAvailable){
		context = new AnnotationConfigApplicationContext(Main.class);
        propertyManager = (PropertyManager)context.getBean("propertyManager");

		try{
			SystemTray tray = SystemTray.getSystemTray();
			String image = webIsAvailable ? IMG_OK : IMG_KO;
			Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(image));
			//popupmenu
		    PopupMenu trayPopupMenu = new PopupMenu();
	
		    //1t menuitem for popupmenu
		    MenuItem info = new MenuItem("Info");
		    info.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            JOptionPane.showMessageDialog(null, "You are checking the site: " + propertyManager.getProperty(URL));          
		        }
		    });
		    trayPopupMenu.add(info);
		    
		    //2nd menuitem for popupmenu
		    MenuItem configuration = new MenuItem("Configuration");
		    configuration.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            JOptionPane.showMessageDialog(null, "You can change the site to be checked and its interval editing the config.json file in "+System.getProperty(USER_DIR)+"\\.hey\\");          
		        }
		    }); 
		    trayPopupMenu.add(configuration);

		    //3nd menuitem of popupmenu
		    MenuItem close = new MenuItem("Close");
		    close.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            System.exit(0);             
		        }
		    });
		    trayPopupMenu.add(close);
	
		    //setting tray icon
		    TrayIcon trayIcon = new TrayIcon(img, "HEY", trayPopupMenu);
		    trayIcon.setImageAutoSize(true);    
	        tray.add(trayIcon);
	        
	        return Optional.of(trayIcon);
	        
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	        return Optional.absent();
	    }finally{
	    	if (context != null) context.close();
	    }
	    
	    
	}
	
	private void changeHeyIcon(TrayIcon heyIcon, boolean webIsAvailable) {
		String image = webIsAvailable ? IMG_OK : IMG_KO;
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(image));
		heyIcon.setImage(img);
	}

	private void showNotification(TrayIcon heyIcon, boolean available){
		String text = available ? AVAILABLE : UNAVAILABLE;
		heyIcon.displayMessage(HEY, text, MessageType.INFO);
	}
}
