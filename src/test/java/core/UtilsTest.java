package core;

import static org.assertj.core.api.Assertions.*;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import utils.api.FileManager;
import utils.impl.FileManagerImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


public class UtilsTest {
	@Inject
	private FileManager fileManager;
	
	String URL;
	
	@Before
	public void init(){
		URL = "url";
		fileManager = new FileManagerImpl();
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
