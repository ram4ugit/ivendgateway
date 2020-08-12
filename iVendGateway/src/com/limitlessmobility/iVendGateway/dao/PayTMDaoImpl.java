package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;

public class PayTMDaoImpl implements PayTMDao {
	
	private static final Logger logger = Logger.getLogger(PayTMDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();
    @Override
    public String getTerminalIdByDeviceId(String deviceId) {
    	boolean isConnected = true;
		String terminalId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection  Created...");

				String query = "SELECT t.terminal_id FROM terminal t where t.device_id='" + deviceId + "'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					terminalId = rs.getString(1);

				}
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return terminalId;

    }
}
