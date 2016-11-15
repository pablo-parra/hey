package core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import core.api.Manager;

@Configuration
@ComponentScan(basePackages = {"utils", "core"})
public class Main {
	
	
	public static void main(String [] arguments){
    	AbstractApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);
    	Manager mainManager = (Manager)context.getBean("mainManager");
		mainManager.start();
	    System.out.println("end");
	    if (context != null) context.close();
	}
}
