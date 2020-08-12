package com.limitlessmobility.iVendGateway.dao;

import java.awt.print.Paper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.limitlessmobility.iVendGateway.psp.model.PSPModel;
//import com.mysql.jdbc.Statement;

public class TerminalPspDaoImpl implements TerminalPspDao {

	private static final Logger logger = Logger
			.getLogger(TerminalPspDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public TerminalPsp checkStatus(TerminalPsp terminalPsp) {

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		if (terminalPsp != null) {
			try {

				if (isConnected) {
					logger.info("Connection Object Created...");

					String query = "SELECT p.psp_mguid FROM psp_merchant p WHERE p.psp_id='"+ terminalPsp.getPspId()+ "' and p.psp_terminal_id ='"+terminalPsp.getPosId()+"' and p.payment_id='"+ terminalPsp.getPaymentId() + "'";
					System.out.println("query.." + query);

					// PreparedStatement preparedStatement =
					// con.prepareStatement(query);
					java.sql.Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()) {

						terminalPsp.setMerchantGuid((rs.getString("psp_mguid")));

						System.out.println(terminalPsp.getMerchantGuid());
					}

					logger.info(terminalPsp.getMerchantGuid());

					if (terminalPsp != null) {
						return terminalPsp;
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
			System.out.println(terminalPsp);
			logger.info(terminalPsp);
		}
		return terminalPsp;

	}

	@Override
	public String getTerminalIdByDeviceID(String deviceid) {

		boolean isConnected = true;
		String terminalId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT t.terminal_id FROM terminal t where t.device_id='"
						+ deviceid + "'";
				System.out.println("query.." + query);

				// PreparedStatement preparedStatement =
				// con.prepareStatement(query);
				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					terminalId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return terminalId;

	}

	@Override
	public List<String> getpspMerchantID(String terminalID) {
		List<String> pspMerchantList = new ArrayList<String>();
		boolean isConnected = true;
		String pspMerchantId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT t.psp_merchant_id FROM terminal_psp t where t.terminal_id='"
						+ terminalID + "'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					pspMerchantId = rs.getString(1);
					pspMerchantList.add(pspMerchantId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return pspMerchantList;
	}

	@Override
	public List<PSPModel> getpspList(String pspMeachantID) {

		boolean isConnected = true;
		List<PSPModel> pspList = new ArrayList<PSPModel>();

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT * FROM psp_merchant p where p.psp_merchant_id='"+pspMeachantID+"'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					PSPModel p = new PSPModel();
					p.setPspID(rs.getString("psp_merchant_id"));
					p.setPaymentId(rs.getString("payment_id"));
					p.setImage(rs.getString("psp_image"));
					
					pspList.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return pspList;
	}

}
