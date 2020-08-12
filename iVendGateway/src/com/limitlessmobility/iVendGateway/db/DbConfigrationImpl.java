package com.limitlessmobility.iVendGateway.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DbConfigrationImpl implements DbConfigration{
	
Connection con = null;
public static Logger log = Logger.getLogger(DbConfigrationImpl.class);
	
	
	public DbConfigrationImpl() {
		super();
	}
	

	@Override
	public boolean isConnected(Connection connection) {
		log.info("Closing Connection");
		try {
			if(!connection.isClosed())
				return true;
		} catch (Exception e) {
			log.error("Connetion Closing Failed.");
		}
		return false;
	}

	@Override
	public Connection getCon() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Loading Failed...  " + e);
		}
		try {
			if(con == null)
				con = DriverManager.getConnection(Util.dbUrlReader(),Util.dbUserNameReader(), Util.dbPasswoeReader());
			else
				return con;
		} catch (Exception e) {
			System.out.println("Connection creating failed."+e);
			log.error("Connection creating failed."+e);
		}
		return con;
	}

	@Override
	public void closeConnection(Connection connection) {
			log.info("Closing Connection..");
		try {
			connection.close();
		} catch (Exception e) {
			log.error("Closing Connetion Failed.."+e);
		}
	}

	
	public static void main(String[] args) {
		System.out.println();
	}
}
