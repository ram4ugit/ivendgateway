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
import com.limitlessmobility.iVendGateway.model.terminal.TerminalDeviceIdModel;

public class TerminalDeviceIdDaoImpl implements TerminalDeviceIdDao {

	private static final Logger logger = Logger.getLogger(TerminalDeviceIdDaoImpl.class);

	@Override
	public List<TerminalDeviceIdModel> getTerminalDeviceId() {

		List<TerminalDeviceIdModel> deviceList = new ArrayList<TerminalDeviceIdModel>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT id,device_id FROM devices where status='0'";

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {

					TerminalDeviceIdModel t = new TerminalDeviceIdModel();
					t.setId(rs.getString(1));
					t.setDevice_id(rs.getString(2));

					deviceList.add(t);
				}

				System.out.println("devicelist1 " + deviceList);

				Gson gson = new Gson();
				// convert your list to json
				String jsonCartList = gson.toJson(deviceList);

				System.out.println("devicelist2 " + jsonCartList);

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		return deviceList;

	}

}