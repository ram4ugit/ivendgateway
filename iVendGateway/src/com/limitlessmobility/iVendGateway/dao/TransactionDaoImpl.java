package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class TransactionDaoImpl implements TransactionDao {

	DbConfigration dbConfig = new DbConfigrationImpl();

	String sqlDate = "";

	@Override
	public boolean saveTransaction(PaymentTransaction callbackTransactions) {
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String isExistQuery = "SELECT * FROM payment_transactions WHERE psp_transaction_id='"+callbackTransactions.getPspTransactionId()+"'";
				PreparedStatement ps = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+callbackTransactions.getOrderId());
			      } else {
					
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid,paid_amount) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, callbackTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
//				if(CommonUtil.isNullOrEmpty(callbackTransactions.getAuthDate())){
					preparedStatement.setString(11, sqlDate.toString());
				/*} else {
					preparedStatement.setString(11, callbackTransactions.getAuthDate());
				}*/
				
				preparedStatement.setString(12, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(13, callbackTransactions.getLocationId());//author- Amar
				preparedStatement.setString(14, callbackTransactions.getAuthCode());
				preparedStatement.setString(15, callbackTransactions.getTransactionType());
				preparedStatement.setString(16, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(17, callbackTransactions.getStatus());
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, callbackTransactions.getOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, callbackTransactions.getAppId());
				preparedStatement.setString(27, callbackTransactions.getAuthCode());
				preparedStatement.setString(28, callbackTransactions.getCustomerName());
				preparedStatement.setString(29, callbackTransactions.getTransactionType());
				preparedStatement.setString(30, callbackTransactions.getPspMerchantId());
				preparedStatement.setString(31, String.valueOf(callbackTransactions.getPaidAmount()));
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
				}
			    }
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
	public boolean updateBlockedTransaction(PaymentTransaction callbackTransactions) {
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String isExistQuery = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";
				PreparedStatement ps = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+callbackTransactions.getOrderId());
			    	  Statement stmt=conn.createStatement();
			    	  stmt = conn.createStatement();
						String sql = "UPDATE payment_transactions " +
				                   "SET psp_transaction_id = '"+callbackTransactions.getPspTransactionId()+"', service_type='"+callbackTransactions.getServiceType()+"', transaction_type='"+callbackTransactions.getTransactionType()+"', paid_amount="+callbackTransactions.getPaidAmount()+",status_msg='"+callbackTransactions.getStatusMsg()+"',settlement_amount='"+callbackTransactions.getSettlementAmount()+"', settlement_date='"+sqlDate.toString()+"' WHERE order_id='"+callbackTransactions.getOrderId()+"'";
						System.out.println("UPDATE payment_transactions "+ sql);
				        int i = stmt.executeUpdate(sql);
			    	  
			    	  
			      } else {
					
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, callbackTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, callbackTransactions.getAuthCode());
				preparedStatement.setString(15, callbackTransactions.getTransactionType());
				preparedStatement.setString(16, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(17, callbackTransactions.getStatus());
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, callbackTransactions.getOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, callbackTransactions.getAuthCode());
				preparedStatement.setString(28, callbackTransactions.getCustomerName());
				preparedStatement.setString(29, callbackTransactions.getTransactionType());
				preparedStatement.setString(30, callbackTransactions.getPspMerchantId());
				//preparedStatement.setString(31, callbackTransactions.getPaidAmount());
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
				}
			    }
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
	@Override
    public PaymentTransaction getTransactionByOrder(String orderId) {
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		PaymentTransaction paymentTransaction = new PaymentTransaction();
		
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				
				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String query = "SELECT * FROM payment_transactions WHERE order_id='"+orderId+"'";
				PreparedStatement ps = conn.prepareStatement(query);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  paymentTransaction.setPspTransactionId(rs.getString("psp_transaction_id"));
			      } 
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return paymentTransaction;
	}
	@Override
    public boolean updateReleaseTransaction(PaymentTransaction callbackTransactions) {
		System.out.println("Release method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String isExistQuery = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";
				PreparedStatement ps = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+callbackTransactions.getOrderId());
			    	  Statement stmt=conn.createStatement();
			    	  stmt = conn.createStatement();
						String sql = "UPDATE payment_transactions " +
				                   "SET merchant_order_id = '"+callbackTransactions.getPspTransactionId()+"', service_type='"+callbackTransactions.getServiceType()+"', transaction_type='"+callbackTransactions.getTransactionType()+"', settlement_amount="+callbackTransactions.getSettlementAmount()+",status_msg='"+callbackTransactions.getStatusMsg()+"', settlement_date='"+sqlDate.toString()+"' WHERE order_id='"+callbackTransactions.getOrderId()+"'";
						System.out.println("UPDATE payment_transactions "+ sql);
				        int i = stmt.executeUpdate(sql);
			    	  
			    	  
			      } else {
					
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, callbackTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, callbackTransactions.getAuthCode());
				preparedStatement.setString(15, callbackTransactions.getTransactionType());
				preparedStatement.setString(16, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(17, "Close");
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, callbackTransactions.getOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, callbackTransactions.getAuthCode());
				preparedStatement.setString(28, callbackTransactions.getCustomerName());
				preparedStatement.setString(29, callbackTransactions.getTransactionType());
				preparedStatement.setString(30, callbackTransactions.getPspMerchantId());
				//preparedStatement.setString(31, callbackTransactions.getPaidAmount());
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
				}
			    }
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
	@Override
    public boolean getTransactionExistByOrder(String orderId) {
		boolean responseStatus=false;
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		PaymentTransaction paymentTransaction = new PaymentTransaction();
		
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				
				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String query = "SELECT * FROM payment_transactions WHERE order_id='"+orderId+"'";
				PreparedStatement ps = conn.prepareStatement(query);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  responseStatus=true;
			      } 
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return responseStatus;
	}
	@Override
    public boolean updatependingTransaction(PaymentTransaction callbackTransactions) {
		System.out.println("Release method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String isExistQuery = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";
				PreparedStatement ps = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+callbackTransactions.getOrderId());
			    	  Statement stmt=conn.createStatement();
			    	  stmt = conn.createStatement();
						String sql = "UPDATE payment_transactions " +
				                   "service_type='"+callbackTransactions.getServiceType()+"', transaction_type='"+callbackTransactions.getTransactionType()+"', settlement_amount="+callbackTransactions.getSettlementAmount()+",status_msg='"+callbackTransactions.getStatusMsg()+"', settlement_date='"+sqlDate.toString()+"' WHERE order_id='"+callbackTransactions.getOrderId()+"'";
						System.out.println("UPDATE payment_transactions "+ sql);
				        int i = stmt.executeUpdate(sql);
			    	  
			    	  
			      } else {
					
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, callbackTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, callbackTransactions.getAuthCode());
				preparedStatement.setString(15, callbackTransactions.getTransactionType());
				preparedStatement.setString(16, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(17, callbackTransactions.getStatus());
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, callbackTransactions.getOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, callbackTransactions.getAuthCode());
				preparedStatement.setString(28, callbackTransactions.getCustomerName());
				preparedStatement.setString(29, callbackTransactions.getTransactionType());
				preparedStatement.setString(30, callbackTransactions.getPspMerchantId());
				//preparedStatement.setString(31, callbackTransactions.getPaidAmount());
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
				}
			    }
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
	@Override
    public boolean payPendingSaveTransaction(
            PaymentTransaction paymentTransactions) {
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				
			      //STEP 5: Extract data from result set
				try{
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid,paid_amount) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, paymentTransactions.getPspId());
				preparedStatement.setString(2, paymentTransactions.getDeviceId());
				preparedStatement.setString(3, paymentTransactions.getTerminalId());
				preparedStatement.setString(4, paymentTransactions.getMerchantId());
				preparedStatement.setString(5, paymentTransactions.getProductName());
				preparedStatement.setString(6, paymentTransactions.getProductPrice());
				preparedStatement.setString(7, paymentTransactions.getPspTransactionId());
				preparedStatement.setString(8, paymentTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, paymentTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, paymentTransactions.getSettlementAmount());
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, paymentTransactions.getAuthCode());
				preparedStatement.setString(15, paymentTransactions.getTransactionType());
				preparedStatement.setString(16, paymentTransactions.getSettlementAmount());
				preparedStatement.setString(17, paymentTransactions.getStatus());
				preparedStatement.setString(18, paymentTransactions.getComments());
				preparedStatement.setString(19, paymentTransactions.getStatusCode());
				preparedStatement.setString(20, paymentTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, paymentTransactions.getOrderId());
				preparedStatement.setString(24, paymentTransactions.getMerchantOrderId());
				preparedStatement.setString(25, paymentTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, paymentTransactions.getAuthCode());
				preparedStatement.setString(28, paymentTransactions.getCustomerName());
				preparedStatement.setString(29, paymentTransactions.getTransactionType());
				preparedStatement.setString(30, paymentTransactions.getPspMerchantId());
				preparedStatement.setString(31, String.valueOf(paymentTransactions.getPaidAmount()));
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
			    }
				return false;
				} catch(Exception e){
					System.out.println(e);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
	
	@Override
    public boolean updateVoidTransaction(PaymentTransaction callbackTransactions) {
		System.out.println("VoidDao method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				// java.sql.Timestamp sqlDate = new java.sql.Timestamp(new
				// java.util.Date().getTime());

				// java.util.Date date = new java.util.Date();
				// java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				// log.info("Connection Object Created..");

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = conn.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					//System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);
					
					//System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}
				
				/*String queryIsExist = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(queryIsExist);*/
				
				String isExistQuery = "SELECT * FROM payment_transactions WHERE order_id='"+callbackTransactions.getOrderId()+"'";
				PreparedStatement ps = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+callbackTransactions.getOrderId());
			    	  Statement stmt=conn.createStatement();
			    	  stmt = conn.createStatement();
						String sql = "UPDATE payment_transactions " +
				                   "SET transaction_type='voidTransaction', settlement_date='"+sqlDate.toString()+"', status='close' WHERE order_id='"+callbackTransactions.getOrderId()+"'";
						System.out.println("UPDATE payment_transactions "+ sql);
				        int i = stmt.executeUpdate(sql);
			    	  
			    	  
			      } /*else {
					
				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,auth_code,customer_id,transaction_type,psp_merchantid) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, callbackTransactions.getDeviceTransactionId());
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, callbackTransactions.getAuthCode());
				preparedStatement.setString(15, callbackTransactions.getTransactionType());
				preparedStatement.setString(16, callbackTransactions.getSettlementAmount());
				preparedStatement.setString(17, "Close");
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "Payment Done");
				preparedStatement.setString(23, callbackTransactions.getOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, callbackTransactions.getAuthCode());
				preparedStatement.setString(28, callbackTransactions.getCustomerName());
				preparedStatement.setString(29, callbackTransactions.getTransactionType());
				preparedStatement.setString(30, callbackTransactions.getPspMerchantId());
				//preparedStatement.setString(31, callbackTransactions.getPaidAmount());
				

				int isInsert = preparedStatement.executeUpdate();

				
				if (isInsert > 0) {
					System.out.println("Record is inserted into payment_transactions table!");
					return true;
				}
			    }*/
				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return false;
	}
}
