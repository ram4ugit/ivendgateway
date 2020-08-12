package com.limitlessmobility.iVendGateway.dao.amazonpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonPayCallbackModel;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class AmazonPayInitiationDaoImpl implements AmazonPayInitiationDao {

	DbConfigration dbConfig = new DbConfigrationImpl();

	String sqlDate = "";

	String line = "";
	StringBuilder response = new StringBuilder();

	String jsonString = "";
	String output = "";

	String pspId = "";

	String apiKey = "";

	@Override
	public boolean saveTransaction(AmazonPayCallbackModel amazonPayCallbackModel) {
		System.out.println("i am in AmazonPayInitiationDaoImpl");
		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				String querytime = "SELECT NOW()";

				PreparedStatement preparedStatementtime = con.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					System.out.println("amazonpay transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);

					System.out.println("amazonpay transaction daoimpl2: " + sqlDate);

				}

				String query = "INSERT INTO payment_initiation"
						+ "(psp_id,device_id,merchant_order_id,terminal_id,merchant_id,product_price,psp_transaction_id,auth_amount,auth_date,status,comments,"
						+ "status_code,status_msg,payment_stage) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				/*order_id,,,currency,auth_date,product_name,app_id,*/

				PreparedStatement preparedStatement = con.prepareStatement(query);
				 preparedStatement.setString(1, amazonPayCallbackModel.getPspId());
				 preparedStatement.setString(2, amazonPayCallbackModel.getDeviceId());
				 preparedStatement.setString(3, amazonPayCallbackModel.getMerchantId());
				 preparedStatement.setString(4, amazonPayCallbackModel.getProductName());
				 preparedStatement.setString(5, amazonPayCallbackModel.getServiceType());
				 preparedStatement.setString(6, amazonPayCallbackModel.getPspId());
				 /*preparedStatement.setDouble(7, amazonPayCallbackModel.getAuthAmount());*/
				 preparedStatement.setString(7, sqlDate.toString());
				 preparedStatement.setString(8, "qrscanned");
				  preparedStatement.setString(9, "");
				 preparedStatement.setString(10, "");
				 preparedStatement.setString(11,"");
				 preparedStatement.setString(12,"");
				 preparedStatement.setString(13,"");
				 preparedStatement.setString(14,"");				 

				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into payment_initiation table!");
				if (isInsert > 0) {

					return true;

					}
				   return false;

			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
			// log.error("Saving failed...."+e);
		} finally {
			dbConfig.closeConnection(con);
		}

		return false;
	}

}
