package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.terminal.TerminalMerchantIdModel;

public class TerminalMerchantIdDaoImpl implements TerminalMerchantIdDao {

	private static final Logger logger = Logger.getLogger(TerminalMerchantIdDaoImpl.class);

	@Override
	public List<TerminalMerchantIdModel> getTerminalMerchantId() {

		List<TerminalMerchantIdModel> terminalMerchantList = new ArrayList<TerminalMerchantIdModel>();

		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT id,merchant_id FROM merchants";

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {

					TerminalMerchantIdModel t = new TerminalMerchantIdModel();
					t.setId(rs.getString(1));
					t.setMerchantName(rs.getString(2));

					terminalMerchantList.add(t);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return terminalMerchantList;
	}

}
