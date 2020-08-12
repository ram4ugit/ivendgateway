package com.limitlessmobility.iVendGateway.dao.bharatqr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;

public class BharatQRDaoImpl implements BharatQRDao {

	private static final Logger logger = Logger
	        .getLogger(BharatQRDaoImpl.class);

//	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public String getDeviceIDByTerminalId(String terminalId) {
		boolean isConnected = true;
		String deviceId = null;

		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT t.device_id FROM terminal t where t.terminal_id='"
				        + terminalId + "'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					deviceId = rs.getString(1);

				}
				rs.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			 dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return deviceId;

	}

	@Override
	public String getMachineIdByDeviceId(String deviceId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		String machineId = null;

		Connection con = dbConfig.getCon();
		System.out.println("con..." + con);
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				Statement stmt = con.createStatement();

				String query = "SELECT t.machine_id FROM devices t where t.device_id='"
				        + deviceId + "'";
				System.out.println("query.." + query);

				// Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					machineId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return machineId;
	}

	@Override
	public String getMIDByPspId(String pspId) {
		boolean isConnected = true;
		String MID = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_merchant_id FROM psp_merchant t where t.psp_id='"
				        + pspId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					MID = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return MID;
	}

	@Override
	public String getTrIDByTxnId(String txnId) {
		boolean isConnected = true;
		String trId = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_transaction_id FROM payment_initiation WHERE  order_id='"
				        + txnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					trId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			 dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return trId;
	}

	@Override
	public String getProductByTxnId(String txnId) {
		boolean isConnected = true;
		String productName = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT product_name FROM payment_initiation WHERE  order_id='"
				        + txnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					productName = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return productName;
	}

	@Override
	public String getTerminalId() {
		boolean isConnected = true;
		String termialId = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT terminal_id FROM terminal ";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					termialId = rs.getString(1);

					System.out.println("termialId " + termialId);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return termialId;

	}

	@Override
	public String getTerminalId(String pspId) {

		System.out.println("PSP_TRXN_ID " + pspId);
		boolean isConnected = true;
		String terminalId = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_merchant_key FROM psp_merchant WHERE psp_id='"
				        + pspId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					terminalId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("TermialId  " + terminalId);
		return terminalId;

	}

	/*@Override
	public String getAmount(String pspTxnId) {

		System.out.println("PSP_TRXN_ID " + pspTxnId);
		boolean isConnected = true;
		String amount = null;
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT auth_amount FROM payment_transactions WHERE  psp_transaction_id='"
				        + pspTxnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					amount = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("Amount  " + amount);
		return amount;

	}*/

	@Override
	public String getPspIdByPSPTxnId(String pspTxnId) {

		System.out.println("PSP_TRXN_ID " + pspTxnId);
		boolean isConnected = true;
		String pspId = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_id FROM payment_transactions WHERE device_transaction_id='"
				        + pspTxnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					pspId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("BankCode  " + pspId);
		return pspId;
	}

	@Override
	public String getBankCodeByPspId(String pspId) {

		System.out.println("PSP_TRXN_ID " + pspId);
		boolean isConnected = true;
		String bankCode = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_bankcode FROM psp_details WHERE psp_id='"
				        + pspId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					bankCode = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("BankCode  " + bankCode);
		return bankCode;
	}

	@Override
	public String getAuthCodeByPspId(String pspTxnId) {

		System.out.println("PSP_TRXN_ID " + pspTxnId);
		boolean isConnected = true;
		String authCode = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT auth_code FROM payment_transactions WHERE psp_transaction_id='"
				        + pspTxnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					authCode = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("AuthCode  " + authCode);
		return authCode;
	}

	@Override
	public String getTxnTypeByPspTxnId(String txnId) {

		System.out.println("ORDER_ID " + txnId);
		boolean isConnected = true;
		String txnType = null;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT transaction_type FROM payment_transactions WHERE order_id='"
				        + txnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					txnType = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			// dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("txnType  " + txnType);
		return txnType;
	}

	@Override
	public String getTIDByPspTerminalPaymentId(String pspId,
	        String termainalId, String paymentId) {

		boolean isConnected = true;
		String TID = null;
		DbConfigration dbConfig = new DbConfigrationImpl();

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				Statement stmt = con.createStatement();

				String query = "SELECT t.psp_merchant_key FROM psp_merchant t where t.psp_id='"
				        + pspId
				        + "'and psp_terminal_id='"
				        + termainalId
				        + "' and payment_id='" + paymentId + "'";

				System.out.println("query.." + query);

				// Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					TID = rs.getString("psp_merchant_key");

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			logger.info("db connection closed");
		}

		return TID;

	}

	@Override
	public String getMIDPspTerminalPaymentId(String pspId, String termainalId,
	        String paymentId) {
		boolean isConnected = true;
		String MID = null;

		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		System.out.println("con..." + con);
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				Statement stmt = con.createStatement();

				String query = "SELECT t.psp_merchant_id FROM psp_merchant t where t.psp_id='"
				        + pspId
				        + "'and psp_terminal_id='"
				        + termainalId
				        + "' and payment_id='" + paymentId + "'";

				System.out.println("query.." + query);

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					MID = rs.getString("psp_merchant_id");

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return MID;
	}

	@Override
	public boolean checkTxnId(String txnId) {
		System.out.println("TRXN_ID " + txnId);
		boolean isConnected = true;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "select * from  payment_transactions where device_transaction_id='"
				        + txnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {

					return true;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			 dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("NoRecord found...  ");
		return false;
	}

	@Override
	public boolean updateTerminalDevice(String txnId, String terminalId) {

		System.out.println("TXN_ID " + txnId +  " TerminalId " + terminalId);
		boolean isConnected = true;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query2 = "UPDATE payment_transactions SET  terminal_id='" + terminalId
				        + "' where device_transaction_id='" + txnId + "'";

				System.out.println("query.." + query2);

				PreparedStatement preparedStatement2 = con
				        .prepareStatement(query2);
				int i2 = preparedStatement2.executeUpdate();

				if (i2 > 0) {
					System.out.println("updated successfully");
					logger.info("table updated successfully");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			 dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		return true;

	}

	@Override
	public PspTerminal getTransactionDetails(String pspTxnId) {

		boolean isConnected = true;
		PspTerminal detail = new PspTerminal();
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {
			if (isConnected) {
				String query = "SELECT * FROM payment_transactions WHERE order_id='"
				        + pspTxnId + "'";
				System.out.println("query.." + query);
				java.sql.Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					detail.setTerminal((rs.getString("terminal_id")));
					System.out.println("terminal_id " + detail.getTerminal());
					detail.setPspId((rs.getString("psp_id")));
					System.out.println("psp_id " + detail.getPspId());
					detail.setAuthCode((rs.getString("auth_code")));
					System.out.println("auth_code " + detail.getAuthCode());
					detail.setTxnType((rs.getString("transaction_type")));
					System.out.println("transaction_type "
					        + detail.getTxnType());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 dbConfig.closeConnection(con);
			// logger.info("db connection closed");
		}

		return detail;
	}
	
	@Override
	public String getRefNoByTxnId(String txnId) {


		boolean isConnected = true;
		String refNo = null;

		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				Statement stmt = con.createStatement();

				String query = "SELECT psp_transaction_id FROM payment_transactions t where t.order_id='"
				        + txnId+"'";

				System.out.println("query.." + query);

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					refNo = rs.getString("psp_transaction_id");

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			logger.info("db connection closed");
		}
		return refNo;

	}

}
