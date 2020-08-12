package com.limitlessmobility.iVendGateway.dao.zeta;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;

public class ZetaRefundDaoImpl implements ZetaRefundDao{
	
	private static final Logger logger = Logger.getLogger(ZetaRefundDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public PspTerminal getTransactionDetails(String pspTxnId) {


        
		boolean isConnected = true;
		PspTerminal detail = new PspTerminal();
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
			
		}

		return detail;
	
	}

	@Override
	public PspTerminal getTransactionDetailsZeta(String pspTxnId) {


        
		boolean isConnected = true;
		PspTerminal detail = new PspTerminal();
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {
			if (isConnected) {
				String query = "SELECT * FROM payment_transactions WHERE psp_transaction_id='"
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
			
		}

		return detail;
	
	}

	@Override
    public String getOrderId(String pspTxnId) {

		DbConfigration dbConfig = new DbConfigrationImpl();

		String orderId = "null";
        
		boolean isConnected = true;
		PspTerminal detail = new PspTerminal();
		Connection con = dbConfig.getCon();

		if (con == null) {
			isConnected = false;
		}

		try {
			if (isConnected) {
				String query = "SELECT * FROM payment_transactions WHERE psp_transaction_id='"
				        + pspTxnId + "'";
				System.out.println("query.." + query);
				java.sql.Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					orderId = rs.getString("order_id");
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}

		return orderId;
	
	}
}
