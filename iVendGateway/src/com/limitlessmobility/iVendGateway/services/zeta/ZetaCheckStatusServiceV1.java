package com.limitlessmobility.iVendGateway.services.zeta;

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

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;

@Controller
@RequestMapping("/v1/checkstatus")

public class ZetaCheckStatusServiceV1 {

	private static final Logger logger = Logger.getLogger(ZetaCheckStatusService.class);

	String result = "";

	DbConfigration dbConfig = new DbConfigrationImpl();

	/**
	 * Method to call checkstatus Service
	 * 
	 * @throws JSONException
	 */

	@RequestMapping(value = "/zeta", method = RequestMethod.POST)
	@ResponseBody
	public String checkstatus(@RequestBody String request) throws JSONException {
		logger.info("Service checkstatus Method is calling!!");
		System.out.println("zeta checkstatus api v1 "+request);

		JSONObject jsonObject = new JSONObject(request);

		String status = jsonObject.getString("status");
		String amount = jsonObject.getString("amount");
		String appid = jsonObject.getString("appid");
		String tid = jsonObject.getString("terminalid");
		String pspid = "zeta";
		String deviceId = "";
		String merchantId = "";
		String transactionId = "";
		String pspTid = "";
		String pspMerchantId = "";

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

					// get deviceid and merchantid here

					String query1 = "SELECT device_id,merchant_id FROM terminal WHERE terminal_id='" + tid + "'";

					System.out.println(query1);
					logger.info(query1);

					PreparedStatement preparedStatement1 = con.prepareStatement(query1);

					ResultSet rs1 = preparedStatement1.executeQuery();

					try {
						if (rs1.next()) {

							deviceId = rs1.getString("device_id");

							System.out.println("device_id:  " + deviceId);

							merchantId = rs1.getString("merchant_id");

							System.out.println("merchant_id:  " + merchantId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

					// get psptid and pspmerchantid here

					String query12 = "SELECT psp_tid,psp_merchant_id FROM terminal_psp WHERE terminal_id='" + tid
							+ "' and payment_id='" + pspid + "'";

					System.out.println(query12);
					logger.info(query12);

					PreparedStatement preparedStatement12 = con.prepareStatement(query12);

					ResultSet rs12 = preparedStatement12.executeQuery();

					try {
						if (rs12.next()) {

							pspTid = rs12.getString("psp_tid").trim();

							System.out.println("psp_tid:  " + pspTid);

							pspMerchantId = rs12.getString("psp_merchant_id").trim();

							System.out.println("psp_merchant_id:  " + pspMerchantId);

						}

					} catch (Exception e) {

						System.out.println(e);
					}

					// get maxid here

					/*String query = "SELECT MAX(Id)FROM payment_transactions WHERE  terminal_id='" + pspTid
							+ "' and psp_merchantid='" + pspMerchantId + "'  and     psp_id= '" + pspid
							+ "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC)";*/

					String query = "SELECT MAX(Id)FROM payment_transactions WHERE  terminal_id = '"+pspTid+"'"
					+ " and psp_merchantid='" + pspMerchantId + "'  and     psp_id= '" + pspid
					+ "' AND auth_date > date_sub(now(), interval 1 minute)";
					System.out.println("querry: " + query);
					logger.info("querry: " + query);

					PreparedStatement preparedStatement = con.prepareStatement(query);

					ResultSet rs = preparedStatement.executeQuery();

					if (rs.next()) {

						id = rs.getInt(1);

						System.out.println("max id:  " + id);
						logger.info(id);

					}

					// update here

					String query2 = "UPDATE payment_transactions SET terminal_id='" + tid + "', merchant_id='"
							+ merchantId + "', app_id='" + appid + "', device_id='" + deviceId + "' where Id='" + id
							+ "'";

					PreparedStatement preparedStatement2 = con.prepareStatement(query2);
					int i2 = preparedStatement2.executeUpdate();

					if (i2 > 0) {
						System.out.println("updated successfully");
						logger.info("table updated successfully");
					}

					// get authamount, orderid here

					String query3 = "SELECT auth_amount,order_id FROM payment_transactions WHERE terminal_id='" + tid
							+ "'and psp_id= '" + pspid + "' AND Id ='" + id + "'";

					/*
					 * String query3 =
					 * "SELECT auth_amount,order_id FROM payment_transactions WHERE terminal_id='" +
					 * tid + "'and psp_id= '" + pspid +
					 * "' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_transactions WHERE (auth_date > (NOW() - INTERVAL 40 SECOND)) ORDER BY TIMESTAMP DESC) AND Id = (SELECT MAX(Id)FROM payment_transactions WHERE app_id='"
					 * + appid + "')";
					 */
					System.out.println(query3);
					logger.info(query3);

					PreparedStatement preparedStatement3 = con.prepareStatement(query3);

					ResultSet rs3 = preparedStatement3.executeQuery();

					try {
						if (rs3.next()) {

							amount = rs3.getString("auth_amount");
							System.out.println("amount:  " + amount);

							transactionId = rs3.getString("order_id");
							System.out.println("trn_id:  " + transactionId);

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
		jsonObject2.put("tid", tid);
		jsonObject2.put("trn_id", transactionId);

		System.out.println("zeta checkstatus response v1 "+jsonObject2.toString());
		return jsonObject2.toString();
	}

}
