package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.Terminal;

public class TerminalIdDaoImpl implements TerminalIdDao {

	private static final Logger logger = Logger.getLogger(TerminalIdDaoImpl.class);

	public List<Terminal> getDetails(Terminal terminal) {

		System.out.println("3: in impl");

		List<Terminal> detailList = new ArrayList<Terminal>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");
				System.out.println("Connection Object Created...");
				String query = "SELECT * FROM terminal WHERE terminal_id='" + terminal.getTerminalId() + "'";

				System.out.println(query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {

					Terminal t = new Terminal();
					t.setId(rs.getInt("Id"));
					t.setTerminalId(rs.getString("terminal_id"));
					t.setMerchantId(rs.getString("merchant_id"));
					t.setDeviceId(rs.getString("device_id"));
					t.setImei(rs.getString("IMEI"));
					t.setTerminalMac(rs.getString("terminal_mac"));
					t.setTerminalAddress(rs.getString("terminal_address"));
					t.setTerminalCountry(rs.getString("terminal_country"));
					t.setTerminalState(rs.getString("terminal_state"));
					t.setTerminalCity(rs.getString("terminal_city"));
					t.setTerminalLat(rs.getString("terminal_lat"));
					t.setTerminalLng(rs.getString("terminal_lng"));
					t.setStatus(rs.getString("status"));

					detailList.add(t);
				}
				Gson gson = new Gson();
				String json = gson.toJson(detailList);

				System.out.println("detaillist1 " + detailList);

				System.out.println("detaillist2 " + json);
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		return detailList;

	}
}
