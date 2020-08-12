package com.limitlessmobility.iVendGateway.dao.amazonpay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.TerminalDetail;

public class AmazonDaoImpl implements AmazonPayDao {

	private static final Logger logger = Logger.getLogger(AmazonDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();


	@Override
	public String getMerchantOrderId(String txnId) {
		boolean isConnected = true;
		String merchantOrderId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = " SELECT merchant_order_id FROM payment_transactions WHERE  order_id='" + txnId + "'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					merchantOrderId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return merchantOrderId;
	}

	@Override
	public  CredentialData getAllCredentialDetail(String pspId , String terminalId){
		

		
		boolean isConnected = true;
		
		CredentialData credentialData=new CredentialData();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
        
		try {
			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = " SELECT * FROM psp_merchant WHERE psp_id='"+pspId+"' and psp_terminal_id= '"+terminalId+"'";
				System.out.println("query.." + query);
                
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
                   
					credentialData.setMerchantId(rs.getString("psp_merchant_id"));
					System.out.println("MerchantId "+credentialData.getMerchantId());
					credentialData.setMerchantKey(rs.getString("psp_merchant_key"));
					System.out.println("MerchantKey "+credentialData.getMerchantKey());
					credentialData.setSecretKey(rs.getString("psp_mguid"));
					System.out.println("SecretKey "+credentialData.getSecretKey());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return credentialData;
	
	}

	@Override
	public TerminalDetail getTerminalDetail(String txnId) {
		
		boolean isConnected = true;
		TerminalDetail terminalDetail = new TerminalDetail();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT * FROM  payment_transactions WHERE order_id='"+txnId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					terminalDetail.setPspId(rs.getString("psp_id"));
					System.out.println("pspId "+terminalDetail.getPspId());
					terminalDetail.setTerminalId(rs.getString("terminal_id"));
					System.out.println("TerminalId "+terminalDetail.getTerminalId());
				
					

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		
		return terminalDetail;
	}

	@Override
	public boolean checkTxnId(String txnId) {

		System.out.println("TRXN_ID "+txnId);
		boolean isConnected = true;
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "select * from  payment_transactions where order_id='" + txnId + "'";
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
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		System.out.println("NoRecord found...  ");
		return false;
	
	}

	@Override
	public String getDeviceIDByTerminalId(String terminalId) {

		boolean isConnected = true;
		String deviceId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT t.device_id FROM terminal t where t.terminal_id='" + terminalId + "'";
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
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return deviceId;

	
	}
	
	@Override
	public boolean updateTerminalDevice(String txnId, String deviceId,
	        String terminalId,String appId) {


		System.out.println("TXN_ID "+txnId+" DeviceID "+deviceId+" TerminalId "+terminalId);
		boolean isConnected = true;
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");
				
				
				
				String query2 = "UPDATE payment_transactions SET device_id='" + deviceId + "', terminal_id='" + terminalId+ "',app_id='"+appId+"' where order_id='" + txnId + "'";
				
				System.out.println("query.." + query2);

				PreparedStatement preparedStatement2 = con.prepareStatement(query2);
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
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}
		return true;
	
	
	}
	
	@Override
	public String getTerminalIdByStroreId(String storeId) {


		boolean isConnected = true;
		String terminal_id = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT psp_terminal_id FROM psp_merchant WHERE psp_image='"+storeId+"'";
				
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					terminal_id = rs.getString("psp_terminal_id");

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

		return terminal_id;

	
	
	}

	@Override
	public String getStoreIdForTerminal(String pspId,
	        String terminalId) {

		boolean isConnected = true;
		
		String StroreId = null;
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
        
		try {
			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = " SELECT psp_image FROM psp_merchant WHERE psp_id='"+pspId+"' and psp_terminal_id= '"+terminalId+"'";
				System.out.println("query.." + query);
                
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
                   
					StroreId=rs.getString("psp_image");
					System.out.println("StroreId "+StroreId);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return StroreId;
	
	}
}
