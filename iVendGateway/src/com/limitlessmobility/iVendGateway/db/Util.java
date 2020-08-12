package com.limitlessmobility.iVendGateway.db;

import java.io.InputStream;
import java.util.Properties;



import org.apache.log4j.Logger;

public class Util  {
	
	public static Logger log = Logger.getLogger(Util.class);
	
	static Properties properties = null;
	
	public static String dbUrlReader() {
		String url = "";
		log.info("Start Reading Database URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			
			
			url = properties.getProperty("db.url").toString().trim();
			configurl.close();
			////System.out.println("url is     "+url);
//			log.info("The Database URL is ["+ url +"]");
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return url;
	}

	public static String dbUserNameReader() {
		String userName = "";
		log.info("Start Reading Database UserName...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			userName = properties.getProperty("db.user").toString().trim();
			//System.out.println("userName is    "+userName);
//			log.info("The Database UserName is ["+ userName +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse userName"+e);
		}
		return userName;
	}
	
	
	public static String dbPasswoeReader() {
		String password = "";
		log.info("Start Reading Database Password");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			password = properties.getProperty("db.pwd").toString().trim();
			//System.out.println("password is    "+password);
//			log.info("The Database UserName is ["+password+"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse password"+e);
		}
		return password;
	}
	
	public static String authKeyReader() {
		String authKey = "";
		log.info("Start Reading Database URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			authKey = properties.getProperty("auth.key").toString();
//			log.info("The auth.key is ["+ authKey +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return authKey;
	}
	
	public static String apiUrlReader() {
		String url = "";
		log.info("Start Reading URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			url = properties.getProperty("api.url").toString().trim();
			//System.out.println("url is    "+url);
//			log.info("The API URL is ["+ url +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return url;
	}
	
	public static String apiUrlReaderPaytm() {
		String url = "";
//		log.info("Start Reading URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			url = properties.getProperty("paytmwallet.url").toString().trim();
//			log.info("The API URL is ["+ url +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return url;
	}
	
	public static String reportdownloadPath() {
		String url = "";
//		log.info("Start Reading URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			url = properties.getProperty("paymenthistoryreport.filepath").toString().trim();
//			log.info("The API URL is ["+ url +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return url;
	}
	
	public static String newFilePathForReport() {
		String url = "";
//		log.info("Start Reading URL...");
		try {
			properties = new Properties();
//			properties.load(Util.class.getClassLoader().getResourceAsStream("config.properties"));
			InputStream configurl = Util.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(configurl);
			url = properties.getProperty("paymenthistoryreport.newfile").toString().trim();
//			log.info("The API URL is ["+ url +"]");
			configurl.close();
		} catch (Exception e) {
			log.error("Exception in reading databse url"+e);
		}

		return url;
	}
}
