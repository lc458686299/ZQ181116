package com.szboanda.reptile.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;



public class Property {
	public  void loadProperty(){
		Properties prop = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("config.properties"));
			prop.load(in);
			Config.DRIVER_CLASS = prop.getProperty("DRIVER_CLASS");
			Config.CONNECTION_URL = prop.getProperty("CONNECTION_URL");
			Config.HOME_URL = prop.getProperty("HOME_URL");
			Config.USERNAME = prop.getProperty("USERNAME");
			Config.PASSWORD = prop.getProperty("PASSWORD");
			Config.PDF_ZB_PATH = prop.getProperty("FDF_ZB_PATH");
			Config.PDF_FB_PATH = prop.getProperty("PDF_FB_PATH");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void main(String[] args) {
		new Property().loadProperty();
	}
}
