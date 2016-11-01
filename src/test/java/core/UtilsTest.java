package core;

import static org.assertj.core.api.Assertions.*;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import utils.api.FileManager;
import utils.impl.FileManagerImpl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UtilsTest {
	//@Inject
	private FileManager fileManager;
	
	String URL;
	
	@Before
	public void init(){
		URL = "url";
		
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
		
	}

	@Test
	public void getConfigProperty(){
		assertThat(fileManager.getConfigProperty(URL)).isNotNull();
	}
	
	@Test
	public void getConfigProperty_Fail(){
		assertThat(fileManager.getConfigProperty("asdfasdfasdffasdffasdfareasdf")).isNull();
	}
}
