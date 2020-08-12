package com.limitlessmobility.iVendGateway.dao.sodexo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class SodexoCallbackTransactionDaoImpl implements SodexoCallbackTransactionDao {

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

					System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);

					System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}

				String query = "INSERT INTO payment_transactions"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_name,product_price,"
						+ "psp_transaction_id,device_transaction_id,terminal_transaction_id,auth_amount,auth_date,settlement_amount,"
						+ "location_id,location_lat,location_lng,settlement_date,"
						+ "status,comments,status_code,status_msg,refund_amount,stage,order_id,merchant_order_id,service_type,app_id,psp_merchantid) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductName());
				preparedStatement.setString(6, callbackTransactions.getProductPrice());
				preparedStatement.setString(7, callbackTransactions.getPspTransactionId());
				preparedStatement.setString(8, "");
				preparedStatement.setString(9, "");
				preparedStatement.setDouble(10, callbackTransactions.getAuthAmount());
				preparedStatement.setString(11, sqlDate.toString());
				preparedStatement.setString(12, "");
				preparedStatement.setString(13, "");
				preparedStatement.setString(14, "");
				preparedStatement.setString(15, "");
				preparedStatement.setString(16, "");
				preparedStatement.setString(17, callbackTransactions.getStatus());
				preparedStatement.setString(18, callbackTransactions.getComments());
				preparedStatement.setString(19, callbackTransactions.getStatusCode());
				preparedStatement.setString(20, callbackTransactions.getStatusMsg());
				preparedStatement.setString(21, "");
				preparedStatement.setString(22, "qrscanned");
				preparedStatement.setString(23, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(24, callbackTransactions.getMerchantOrderId());
				preparedStatement.setString(25, callbackTransactions.getServiceType());
				preparedStatement.setString(26, "");
				preparedStatement.setString(27, callbackTransactions.getPspMerchantId());

				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into payment_transactions table!");
				if (isInsert > 0) {
					return true;
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

}
