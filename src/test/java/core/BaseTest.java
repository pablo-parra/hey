package core;

import static org.assertj.core.api.Assertions.*;

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

import javax.inject.Inject;
import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.api.FileManager;
import utils.api.NetManager;
import utils.api.PropertyManager;
import utils.impl.FileManagerImpl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.base.Optional;


public class BaseTest {
	//@Inject
	private FileManager fileManager;
	private PropertyManager propertyManager;
	private NetManager netManager;
	
	private final String URL = "url";;
	private final String IMG_OK = "/img/ok.png";
	private final String IMG_KO = "/img/ko.png";
	private final String TEST_PROPERTY = "testPropertyForHeyApp";
			
	AbstractApplicationContext  context;
	
	@Before
	public void init(){
         context = new AnnotationConfigApplicationContext(Main.class);

        fileManager = (FileManager)context.getBean("fileManager");
        propertyManager = (PropertyManager)context.getBean("propertyManager");
        netManager = (NetManager)context.getBean("netManager");
		
	}
	
	@After
	public void end(){
		if (context != null) context.close();
	}

	@Test
	public void propertiesManagement(){
		propertyManager.setProperty(TEST_PROPERTY, "qwerty");
		assertThat(propertyManager.getProperty(TEST_PROPERTY)).isEqualTo("qwerty");
	}
	
	@Test
	public void getSystemProperty_Fail(){
		assertThat(System.getProperty("notExistingProperty")).isNull();
	}
	
	@Test
	public void getImagesFromResources(){
		Object img_ko = getClass().getResource(IMG_KO);
		Object img_ok = getClass().getResource(IMG_OK);
		assertThat(img_ko).isNotNull();
		assertThat(img_ok).isNotNull();
	}
	
	@Test
	public void availableUrlReturnsTrue(){
		assertThat(netManager.connectionAvailable("http://google.com")).isTrue();
	}
	
	@Test
	public void notAvailableUrlReturnsFalse(){
		assertThat(netManager.connectionAvailable("asdfasdfasdf")).isFalse();
	}
	
	@Test
	public void heyIconIsNotInTray(){
		boolean result = true;
		SystemTray tray = SystemTray.getSystemTray();
		TrayIcon[] icons = tray.getTrayIcons();
		for (TrayIcon trayIcon : icons) {
			if (trayIcon.getToolTip().equals("HEY")) result = false;
		}
		assertThat(result).isTrue();
	}
	
	@Test
	public void heyIconIsInTray(){
		boolean iconIsInTray = false;
		int iconMenuCount = 0;
		SystemTray tray = SystemTray.getSystemTray();
		Image img_ok = Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMG_OK));
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
	    TrayIcon trayIcon = new TrayIcon(img_ok, "HEY", trayPopupMenu);
	    
	    try{
	        tray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
	    
		TrayIcon[] icons = tray.getTrayIcons();
		for (TrayIcon icon : icons) {
			if (icon.getToolTip().equals("HEY")){
				iconIsInTray = true;
				iconMenuCount = icon.getPopupMenu().getItemCount();
			}
			
		}
		assertThat(iconIsInTray).isTrue();
		assertThat(iconMenuCount).isEqualTo(2);
		
		tray.remove(trayIcon);
	}
	
	@Test
	public void addToTray(){
		Image img_ok = Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMG_OK));
	    Image img_ko = Toolkit.getDefaultToolkit().getImage(getClass().getResource(IMG_KO)); 
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
	    TrayIcon trayIcon = new TrayIcon(img_ok, "HEY", trayPopupMenu);
	    SystemTray tray = SystemTray.getSystemTray();
	    trayIcon.setImageAutoSize(true);

	    try{
	        tray.add(trayIcon);
	    }catch(AWTException awtException){
	        awtException.printStackTrace();
	    }
		
		TrayIcon[] icons = tray.getTrayIcons();
		assertThat(icons.length).isEqualTo(1);
		
		icons[0].displayMessage("Voy a cambiar el Icono", "sabes o qué?", MessageType.INFO);
		icons[0].setImage(img_ko);
		TrayIcon[] iconsAgain = tray.getTrayIcons();
		assertThat(iconsAgain.length).isEqualTo(1);

		tray.remove(trayIcon);
	}
}
