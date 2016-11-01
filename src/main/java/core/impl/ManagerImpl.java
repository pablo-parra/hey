package core.impl;

import javax.inject.Inject;

import core.api.Manager;
import utils.api.FileManager;
import utils.api.Net;

public class ManagerImpl implements Manager{
	
	@Inject
	private Net net;
	
	@Inject
	private FileManager fileManager;
	
	final String URL = "url";
	
	public void start(){
		String url = fileManager.getConfigProperty(URL);
		boolean lastStatus = fileManager.getLastStatus();
		if(net.connectionAvailable(url)){
			
		}
	}
}
