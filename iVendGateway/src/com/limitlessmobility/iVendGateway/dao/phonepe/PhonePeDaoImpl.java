package com.limitlessmobility.iVendGateway.dao.phonepe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.phonepe.CheckStatusData;
import com.limitlessmobility.iVendGateway.model.phonepe.InitiationDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PSPMerchantDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PaymentDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.TerminalDetail;

public class PhonePeDaoImpl implements PhonePeDao{
	
	private static final Logger logger = Logger.getLogger(BharatQRDaoImpl.class);

	DbConfigration dbConfig = new DbConfigrationImpl();

	@Override
	public PSPMerchantDetail getMerchantDetailsByPspId(String pspId,String terminalId) {
		
		boolean isConnected = true;
		
		PSPMerchantDetail pspMerchantDetail=new PSPMerchantDetail();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
        
		
		try {
			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = " SELECT * FROM psp_merchant WHERE psp_id='"+pspId+"' and psp_terminal_id= '"+terminalId+"'";
				System.out.println("query.." + query);
                
				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
                   
					pspMerchantDetail.setPspMerchantId(rs.getString("psp_merchant_id"));
					System.out.println("MerchantId "+pspMerchantDetail.getPspMerchantId());
					pspMerchantDetail.setPspMerchantKey(rs.getString("psp_merchant_key"));
					System.out.println("MerchantKey "+pspMerchantDetail.getPspMerchantKey());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return pspMerchantDetail;
	}

	@Override
	public String getMerchantOrderIdByTxnId(String txnId) {
		boolean isConnected = true;
		String merchantOrtderId = null;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = " SELECT merchant_order_id FROM payment_transactions WHERE  order_id='" + txnId + "'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					merchantOrtderId = rs.getString(1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return merchantOrtderId;
	}

	@Override
	public int getAmountByTxnId(String txnId) {
		// TODO Auto-generated method stub
		int amount=0;
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT auth_amount FROM payment_transactions where order_id='"+txnId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					amount = result.getInt(1);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
		}

		return amount;
	}

	@Override
	public CheckStatusData getCheckStatusData(String txnId) {
		// TODO Auto-generated method stub
		//int amount=0;
		CheckStatusData checkStatusData=new CheckStatusData();
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT status_msg,status_code FROM payment_transactions WHERE order_id='"+txnId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					checkStatusData.setStatusMessage(result.getString(1));
					System.out.println("STATUS MESSAGE "+checkStatusData.getStatusMessage());
					//amount = result.getInt(1);
					checkStatusData.setStatusCode(result.getString(2));
					System.out.println("STATUS CODE "+checkStatusData.getStatusCode());
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
		}

		return checkStatusData;
	}

	@Override
	public PaymentDetail getPaymentDetail(String txnId) {
    boolean isConnected = true;
		
		PaymentDetail payDetail=new PaymentDetail();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
        
		
		try {
			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT * FROM  payment_transactions WHERE order_id='"+txnId+"'";
				System.out.println("query.." + query);
                
				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
                   
					payDetail.setPspId(rs.getString("psp_id"));
					System.out.println("pspId "+payDetail.getPspId());
					payDetail.setTerminalId(rs.getString("terminal_id"));
					
					payDetail.setType(rs.getString("device_id"));
					System.out.println("TerminalId "+payDetail.getTerminalId());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return payDetail;
	}
	@Override
	public TerminalDetail getTerminalDetail(String terminalId) {
		  boolean isConnected = true;
			
		  TerminalDetail terminalDetail=new TerminalDetail();
			Connection con = dbConfig.getCon();
			if (con == null) {
				isConnected = false;
			}
	        
			
			try {
				if (isConnected) {
					logger.info("Connection Object Created...");

					String query = "SELECT * FROM  terminal WHERE terminal_id='"+terminalId+"'";
					System.out.println("query.." + query);
	                
					java.sql.Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
	                   
						terminalDetail.setMerchantId(rs.getString("merchant_id"));
						System.out.println("MerchantId "+terminalDetail.getMerchantId());
						terminalDetail.setDeviceId(rs.getString("device_id"));
						System.out.println("DeviceId "+terminalDetail.getDeviceId());
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
public InitiationDetail getInitiationDetail(String txnId) {

	  boolean isConnected = true;
		
	  InitiationDetail initiationDetail=new InitiationDetail();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
      
		
		try {
			if (isConnected) {
				logger.info("Connection Object Created...");

				String query = "SELECT * FROM  payment_initiation WHERE order_id='"+txnId+"'";
				System.out.println("query.." + query);
              
				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
                 
					initiationDetail.setTerminalId(rs.getString("terminal_id"));
					System.out.println("TerminalId "+initiationDetail.getTerminalId());
					initiationDetail.setAppId(rs.getString("app_id"));
					System.out.println("DeviceId "+initiationDetail.getAppId());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			//dbConfig.closeConnection(con);
			logger.info("db connection closed");
		}

		return initiationDetail;

}
}
