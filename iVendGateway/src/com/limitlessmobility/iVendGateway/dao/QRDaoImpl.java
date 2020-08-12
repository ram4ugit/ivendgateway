package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

public class QRDaoImpl implements QRDao{

	@Override
	public boolean saveQRRequest(PaymentInitiation paymentInitiation) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("saveQRRequest method is calling!!");
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
//				log.info("Connection Object Created..");
				
				String query = "INSERT INTO payment_initiation"
						+ "(order_id,merchant_order_id,psp_id,device_id,terminal_id,merchant_id,auth_amount,currency,auth_date,product_name,comments,product_price,app_id,psp_transaction_id) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				
				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, paymentInitiation.getOrderId());
				preparedStatement.setString(2, paymentInitiation.getOrderId());
				preparedStatement.setString(3, paymentInitiation.getPspId());
				preparedStatement.setString(4, paymentInitiation.getDeviceId());
				preparedStatement.setString(5, paymentInitiation.getTerminalId());
				System.out.println("MerchantId Dao--- "+paymentInitiation.getMerchantId());
				preparedStatement.setString(6, paymentInitiation.getMerchantId());
				preparedStatement.setString(7, paymentInitiation.getAuthAmount());
//				preparedStatement.setString(8, paymentInitiation.getMerchantOrderId());
				preparedStatement.setString(8, paymentInitiation.getCurrency());
				preparedStatement.setString(9, timestamp.toString());
				preparedStatement.setString(10, paymentInitiation.getProductName());
				preparedStatement.setString(11, paymentInitiation.getComments());
				preparedStatement.setString(12, paymentInitiation.getProductPrice());
				preparedStatement.setString(13, paymentInitiation.getAppId());
				preparedStatement.setString(14, paymentInitiation.getPspTransactionId());
				
				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into table!");
				if (isInsert > 0) {
					return true;
				}

				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return false;
	}

	/*@Override
	public PaymentInitiation getPaymentInitiationByTxnId(String appId, String orderId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("saveQRRequest method is calling!!");
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			String sql = "SELECT * FROM payment_initiation where app_id='"+appId+"' and order_id='"+orderId+"'";
			 
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			 
			int count = 0;
			 
			while (result.next()){
			    String name = result.getString(2);
			    String pass = result.getString(3);
			    String fullname = result.getString("fullname");
			    String email = result.getString("email");
			 
			    String output = "User #%d: %s - %s - %s - %s";
			    System.out.println(String.format(output, ++count, name, pass, fullname, email));
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return false;
	}*/

}
