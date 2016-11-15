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

import org.junit.Before;
import org.junit.Test;

import utils.api.FileManager;
import utils.api.PropertyManager;
import utils.impl.FileManagerImpl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BaseTest {
	//@Inject
	private FileManager fileManager;
	private PropertyManager propertyManager;
	
	String URL = "url";;
	String IMG_OK = "/img/ok.png";
	String IMG_KO = "/img/ko.png";
	
	@Before
	public void init(){
		//1
		//fileManager = new FileManagerImpl();
		
		//2
//		ApplicationContext context = new ClassPathXmlApplicationContext("config/META-INF/beans.xml");
//	    BeanFactory factory = context;
//	    fileManager = (FileManager)factory.getBean("fm");
		
		//3
        AbstractApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);
        
        //Byname Autowiring
        fileManager = (FileManager)context.getBean("fileManager");
        propertyManager = (PropertyManager)context.getBean("propertyManager");
		
	}

	@Test
	public void getConfigProperty(){
		assertThat(fileManager.getConfigProperty(URL)).isNotNull();
	}
	
	@Test
	public void getConfigProperty_Fail(){
		assertThat(fileManager.getConfigProperty("asdfasdfasdffasdffasdfareasdf")).isNull();
	}
	
	@Test
	public void getSystemProperty(){
		System.setProperty("hey.test", "ok");
		assertThat(System.getProperty("hey.test")).isEqualTo("ok");
	}
	
	@Test
	public void getSystemProperty_Fail(){
		assertThat(System.getProperty("notExistingProperty")).isNull();
	}
	
//	@Test
//	public void getLastStatusAsFalse(){
//		System.setProperty("hey.status", null);
//		assertThat(propertyManager.getLastStatus()).isFalse();
//	}
	
	@Test
	public void getLastStatusAsTrue(){
		System.setProperty("hey.status", "true");
		assertThat(propertyManager.getLastStatus()).isTrue();
	}
	
	@Test
	public void getImagesFromResources(){
		Object img_ko = getClass().getResource(IMG_KO);
		Object img_ok = getClass().getResource(IMG_OK);
		assertThat(img_ko).isNotNull();
		assertThat(img_ok).isNotNull();
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
	    TrayIcon trayIcon = new TrayIcon(img_ok, "AVAILABLE", trayPopupMenu);
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

	}
}
