package com.limitlessmobil.ivendgateway.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;

public class MySqlDate {

	DbConfigration dbConfig = new DbConfigrationImpl();
	
	public String getDate(){
		
		String sqlDate="";
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}

				} 
			}catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} 
		return sqlDate;
	}
	
}
