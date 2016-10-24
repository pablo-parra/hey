package hey;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String [] arguments){
		
	    //checking for support
	    if(!SystemTray.isSupported()){
	        System.out.println("System tray is not supported !!! ");
	        return ;
	    }
	    //get the systemTray of the system
	    SystemTray systemTray = SystemTray.getSystemTray();

	    //get default toolkit
	    //Toolkit toolkit = Toolkit.getDefaultToolkit();
	    //get image 
	    //Toolkit.getDefaultToolkit().getImage("src/resources/busylogo.jpg");
	    Image image = Toolkit.getDefaultToolkit().getImage("D:\\DEV\\hey\\src\\main\\resources\\img\\green_ball.png");

	    //popupmenu
	    PopupMenu trayPopupMenu = new PopupMenu();

	    //1t menuitem for popupmenu
	    MenuItem action = new MenuItem("Action");
	    action.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(null, "Action Clicked");          
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
	    TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", trayPopupMenu);
	    //adjust to default size as per system recommendation 
	    trayIcon.setImageAutoSize(true);

	    try{
	        systemTray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
	    System.out.println("end of main");
		
	}
}
