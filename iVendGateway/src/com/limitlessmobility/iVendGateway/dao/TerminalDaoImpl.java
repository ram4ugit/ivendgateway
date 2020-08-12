package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public class TerminalDaoImpl implements TerminalDao {

	private static final Logger logger = Logger.getLogger(TerminalDaoImpl.class);

	String result;

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public String terminalRegistration(Terminal terminal) {

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		// boolean isInsert = false;

		if (terminal != null) {
			try {
				if (isConnected) {
					logger.info("Connection Object Created...");

					String query = "select terminal_id FROM terminal where terminal_id='" + terminal.getTerminalId()
							+ "'";

					PreparedStatement preparedStatement = con.prepareStatement(query);

					ResultSet rs = preparedStatement.executeQuery(query);

					if (rs.next()) {

						System.out.println("primary key concern");

						JSONObject jsonObject = new JSONObject();
						result = jsonObject.put("msg", "Duplicate TerminalId Not Allowed").toString();
						return result;

					}

					else {

						PreparedStatement stmt = con.prepareStatement(
								"insert into terminal (terminal_id,merchant_id,device_id,IMEI,terminal_mac,terminal_address,terminal_country,terminal_state,terminal_city,terminal_lat,terminal_lng,status) values(?,?,?,?,?,?,?,?,?,?,?,?)");

						logger.info(terminal.getDeviceId());
						logger.info(terminal.getMerchantId());

						stmt.setString(1, terminal.getTerminalId());
						stmt.setString(2, terminal.getMerchantId());
						stmt.setString(3, terminal.getDeviceId());
						stmt.setString(4, terminal.getImei());
						stmt.setString(5, terminal.getTerminalMac());
						stmt.setString(6, terminal.getTerminalAddress());
						stmt.setString(7, terminal.getTerminalCountry());
						stmt.setString(8, terminal.getTerminalState());
						stmt.setString(9, terminal.getTerminalCity());
						stmt.setString(10, terminal.getTerminalLat());
						stmt.setString(11, terminal.getTerminalLng());
						stmt.setString(12, terminal.getStatus());

						int i = stmt.executeUpdate();

						if (i > 0) {
							return "Success";
						}
						return "Fail";
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			} finally {
				dbConfig.closeConnection(con);
				logger.info("db connection closed");
			}

		} else {
			System.out.println(terminal);
			logger.info(terminal);
		}
		return "Success";

	}

}