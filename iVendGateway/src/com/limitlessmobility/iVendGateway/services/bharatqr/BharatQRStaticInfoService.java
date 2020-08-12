package com.limitlessmobility.iVendGateway.services.bharatqr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
@Controller
public class BharatQRStaticInfoService {
   
	private static final Logger logger = Logger.getLogger(BharatQRStaticInfoService.class);

	String result = "";

	DbConfigration dbConfig = new DbConfigrationImpl();

	/**
	 * Method to call checkstatus Service
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/v1/checkstatus/bharatqr", method = RequestMethod.POST)
	@ResponseBody
	public String checkstatusV1(@RequestBody String request) throws JSONException {
		logger.info("BharatQR checkstatus Method is calling!!");
		System.out.println("BharatQR checkstatus Method is calling!!");

		JSONObject jsonObject = new JSONObject(request);

		 
		String status = jsonObject.getString("status");
		String amount = jsonObject.getString("amount");
		String appid = jsonObject.getString("appid");
		String tid = jsonObject.getString("terminalid");
		String pspid="bharatqr";
		String deviceId="";
		String txnId="";
		
		logger.info("BharatQR checkstatus request....   "+jsonObject.toString());
		System.out.println("BharatQR checkstatus request.... "+jsonObject.toString());
		
		int id = 0;

		System.out.println(status);
		logger.info(status);
		System.out.println(amount);
		logger.info(amount);
		System.out.println(appid);
		logger.info(appid);
		System.out.println(tid);
		logger.info(tid);
		System.out.println(pspid);
		logger.info(pspid);

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				System.out.println("Connection Object Created...");

				logger.info("Connection Object Created...");

				if (!status.equals("1")) {

					//String query = "SELECT auth_amount FROM payment_transactions WHERE psp_id= 'bharatqr'AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC)  AND Id = (SELECT MAX(Id)FROM payment_transactions WHERE stage='qrscanned' or stage='')";
               
					String query1 = "SELECT device_id FROM terminal WHERE terminal_id='" + tid + "'";

					
					System.out.println(query1);
					logger.info(query1);

					PreparedStatement preparedStatement1 = con.prepareStatement(query1);

					ResultSet rs1 = preparedStatement1.executeQuery();

					try {
						if (rs1.next()) {


							deviceId = rs1.getString("device_id");

							System.out.println("device_id:  " + deviceId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

					// update here

					String query = "SELECT MAX(Id)FROM payment_transactions WHERE terminal_id='"+tid+"' and psp_id= '" + pspid
							+ "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC)";

					PreparedStatement preparedStatement = con.prepareStatement(query);

					ResultSet rs = preparedStatement.executeQuery();

					if (rs.next()) {

						id = rs.getInt(1);

						System.out.println("max id:  " + id);
						logger.info(id);

					}

					String query2 = "UPDATE payment_transactions SET app_id='" + appid + "', device_id='" + deviceId
							+ "' where Id='" + id + "'";

					PreparedStatement preparedStatement2 = con.prepareStatement(query2);
					int i2 = preparedStatement2.executeUpdate();

					if (i2 > 0) {
						System.out.println("updated successfully");
						logger.info("table updated successfully");
					}

					// get here

					String query3 = "SELECT order_id,auth_amount FROM payment_transactions WHERE psp_id= '" + pspid
							+ "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC) AND Id = (SELECT MAX(Id)FROM payment_transactions WHERE app_id='"
							+ appid + "')";

					System.out.println(query3);
					logger.info(query3);

					PreparedStatement preparedStatement3 = con.prepareStatement(query3);

					ResultSet rs3 = preparedStatement3.executeQuery();

					try {
						if (rs3.next()) {

							amount = rs3.getString("auth_amount");

							System.out.println("amount:  " + amount);
							
							txnId = rs3.getString("order_id");

							System.out.println("order_id:  " +txnId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		}

		JSONObject jsonObject2 = new JSONObject();

		jsonObject2.put("amount", amount);
		jsonObject2.put("trn_id", txnId);
		jsonObject2.put("tid", tid);

		return jsonObject2.toString();
	}
	
	
	/**
	 * Method to call checkstatus Service
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/v2/checkstatus/bharatqr", method = RequestMethod.POST)
	@ResponseBody
	public String checkstatusV2(@RequestBody String request) throws JSONException {
		logger.info("BharatQR checkstatus Method is calling!!");
		System.out.println("BharatQR checkstatus Method is calling!!");

		JSONObject jsonObject = new JSONObject(request);

		 
		String status = jsonObject.getString("status");
		String amount = jsonObject.getString("amount");
		String appid = jsonObject.getString("appid");
		String tid = jsonObject.getString("terminalid");
		String pspid="bharatqr";
		String deviceId="";
		String txnId="";
		
		logger.info("BharatQR checkstatus request....   "+jsonObject.toString());
		System.out.println("BharatQR checkstatus request.... "+jsonObject.toString());
		
		int id = 0;

		System.out.println(status);
		logger.info(status);
		System.out.println(amount);
		logger.info(amount);
		System.out.println(appid);
		logger.info(appid);
		System.out.println(tid);
		logger.info(tid);
		System.out.println(pspid);
		logger.info(pspid);

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				System.out.println("Connection Object Created...");

				logger.info("Connection Object Created...");

				if (!status.equals("1")) {

					//String query = "SELECT auth_amount FROM payment_transactions WHERE psp_id= 'bharatqr'AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC)  AND Id = (SELECT MAX(Id)FROM payment_transactions WHERE stage='qrscanned' or stage='')";
               
					String query1 = "SELECT device_id FROM terminal WHERE terminal_id='" + tid + "'";

					
					System.out.println(query1);
					logger.info(query1);

					PreparedStatement preparedStatement1 = con.prepareStatement(query1);

					ResultSet rs1 = preparedStatement1.executeQuery();

					try {
						if (rs1.next()) {


							deviceId = rs1.getString("device_id");

							System.out.println("device_id:  " + deviceId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

					// update here

					String query = "SELECT MAX(Id)FROM payment_transactions WHERE terminal_id='"+tid+"' and psp_id= '" + pspid
							+ "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC)";

					PreparedStatement preparedStatement = con.prepareStatement(query);

					ResultSet rs = preparedStatement.executeQuery();

					if (rs.next()) {

						id = rs.getInt(1);

						System.out.println("max id:  " + id);
						logger.info(id);

					}

					String query2 = "UPDATE payment_transactions SET app_id='" + appid + "', device_id='" + deviceId
							+ "' where Id='" + id + "'";

					PreparedStatement preparedStatement2 = con.prepareStatement(query2);
					int i2 = preparedStatement2.executeUpdate();

					if (i2 > 0) {
						System.out.println("updated successfully");
						logger.info("table updated successfully");
					}

					// get here

					String query3 = "SELECT order_id,auth_amount FROM payment_transactions WHERE psp_id= '" + pspid
							+ "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC) AND Id = (SELECT MAX(Id)FROM payment_transactions WHERE app_id='"
							+ appid + "')";

					System.out.println(query3);
					logger.info(query3);

					PreparedStatement preparedStatement3 = con.prepareStatement(query3);

					ResultSet rs3 = preparedStatement3.executeQuery();

					try {
						if (rs3.next()) {

							amount = rs3.getString("auth_amount");

							System.out.println("amount:  " + amount);
							
							txnId = rs3.getString("order_id");

							System.out.println("order_id:  " +txnId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);

		}

		JSONObject jsonObject2 = new JSONObject();

		jsonObject2.put("amount", amount);
		jsonObject2.put("trn_id", txnId);
		jsonObject2.put("tid", tid);
		
		JSONObject finalResponse = new JSONObject();
		finalResponse.put("responseObj", jsonObject2.toString());
		if(amount==null || amount.trim().isEmpty()){
			finalResponse.put("status", HttpStatusModal.ERROR);
			finalResponse.put("message", HttpStatusModal.ERROR);
		} else{
			finalResponse.put("status", HttpStatusModal.OK);
		}
		return finalResponse.toString();
	}

}