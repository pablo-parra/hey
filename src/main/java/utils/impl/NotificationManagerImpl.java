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

import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import utils.api.NotificationManager;

@Component("notificationManager")
public class NotificationManagerImpl implements NotificationManager {

	String IMG_OK = "/img/ok.png";
	String IMG_KO = "/img/ko.png";
	private String AVAILABLE = "The site is available.";
	private String UNAVAILABLE = "The site is unavailable";
	private String HEY = "HEY";
	
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
			Optional<TrayIcon> newHeyIcon = createHeyIcon(true); 
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
			System.out.println("No HEY icon...");
			return Optional.absent();
		}

	}
	
	private Optional<TrayIcon> createHeyIcon(boolean webIsAvailable){
		try{
			SystemTray tray = SystemTray.getSystemTray();
			String image = webIsAvailable ? IMG_OK : IMG_KO;
			Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(image));
			//popupmenu
		    PopupMenu trayPopupMenu = new PopupMenu();
	
		    //1t menuitem for popupmenu
		    MenuItem action = new MenuItem("Show the url");
		    action.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            JOptionPane.showMessageDialog(null, "Show the url");          
		        }
		    });     
		    trayPopupMenu.add(action);
	
		    //2nd menuitem of popupmenu
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
