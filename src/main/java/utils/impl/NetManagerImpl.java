package utils.impl;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import utils.api.NetManager;

@Component("netManager")
public class NetManagerImpl implements NetManager{

	public boolean connectionAvailable(String url) {
		boolean result = false;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

			if (connection.getResponseCode() == 200){
				result = true;
			}else {
				System.out.println(url + " NOT AVAILABLE");
			}
		} catch (Exception e) {
			// TODO: implement LOG
			System.out.println("ERROR: " + e.getMessage());
		}
		return result;
	}

}
