package utils.impl;

import java.net.HttpURLConnection;
import java.net.URL;

import utils.api.Net;

public class NetImpl implements Net{

	public boolean connectionAvailable(String url) {
		boolean result = false;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			if (connection.getResponseCode() == 200){
				System.out.println("OK");
				result = true;
			}else {
				System.out.println("NOOOO");
			}
		} catch (Exception e) {
			// TODO: implement LOG
			System.out.println("ERROR: " + e.getMessage());
		}
		return result;
	}

}
