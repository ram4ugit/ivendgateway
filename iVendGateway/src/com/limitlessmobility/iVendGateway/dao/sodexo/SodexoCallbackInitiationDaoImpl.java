package com.limitlessmobility.iVendGateway.dao.sodexo;

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
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class SodexoCallbackInitiationDaoImpl implements SodexoCallbackInitiationDao {

	

	String sqlDate = "";

	String line = "";
	StringBuilder response = new StringBuilder();

	String jsonString = "";
	String output = "";

	String pspId = "";

	String apiKey = "";

	@Override
	public boolean saveTransaction(PaymentTransaction callbackTransactions) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("saveTransaction method is calling!!");
		// log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
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

				PreparedStatement preparedStatementtime = con.prepareStatement(querytime);

				ResultSet resultSettime = preparedStatementtime.executeQuery();

				if (resultSettime.next()) {

					System.out.println("zeta transaction daoimpl1: " + sqlDate);

					sqlDate = resultSettime.getString(1);

					System.out.println("zeta transaction daoimpl2: " + sqlDate);

				}

				String query = "INSERT INTO payment_initiation"
						+ "(psp_id,device_id,terminal_id,merchant_id,product_price,psp_transaction_id,auth_amount,auth_date,status,comments,status_code,status_msg,payment_stage) VALUES"
						+ "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

				System.out.println("5");

				PreparedStatement preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, callbackTransactions.getPspId());
				preparedStatement.setString(2, callbackTransactions.getDeviceId());
				preparedStatement.setString(3, callbackTransactions.getTerminalId());
				preparedStatement.setString(4, callbackTransactions.getMerchantId());
				preparedStatement.setString(5, callbackTransactions.getProductPrice());
				preparedStatement.setString(6, callbackTransactions.getPspTransactionId());
				preparedStatement.setDouble(7, callbackTransactions.getAuthAmount());
				preparedStatement.setString(8, sqlDate.toString());
				preparedStatement.setString(9, callbackTransactions.getStatus());
				preparedStatement.setString(10, callbackTransactions.getComments());
				preparedStatement.setString(11, callbackTransactions.getStatusCode());
				preparedStatement.setString(12, callbackTransactions.getStatusMsg());
				preparedStatement.setString(13, "qrscanned");

				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into payment_initiation table!");
				if (isInsert > 0) {

					String querry = "SELECT psp_transaction_id FROM payment_initiation  WHERE psp_id= 'zeta' AND auth_date=(SELECT MAX(auth_date) AS TIMESTAMP FROM payment_initiation ORDER BY TIMESTAMP DESC) AND Id = (SELECT MAX(Id)FROM payment_initiation WHERE payment_stage='qrscanned')";

					PreparedStatement preparedStatement2 = con.prepareStatement(querry);

					ResultSet resultSet2 = preparedStatement2.executeQuery();

					while (resultSet2.next()) {

						pspId = resultSet2.getString("psp_transaction_id");

					}

					System.out.println("pspId: " + pspId);

					RandomString RandomString = new RandomString();

					String requestId = RandomString.generateRandomString(8);

					System.out.println("requestId: " + requestId);

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("captureRequestId", requestId);
					jsonObject.put("purchaseTransactionId", pspId);

					String reqdata = jsonObject.toString();

					try {
						byte[] postData = reqdata.getBytes(StandardCharsets.UTF_8);
						int postDataLength = postData.length;
						String request = "https://pay.gw.zetapay.in/v1.0/zeta/transactions/capture";
						URL url = new URL(request);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.setInstanceFollowRedirects(false);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/json");

						// dynamicly get key using psp id from pspmrchnt

						String querry22 = "SELECT psp_merchant_key FROM psp_merchant WHERE psp_id='sodexo'";

						PreparedStatement preparedStatement22 = con.prepareStatement(querry22);

						ResultSet resultSet22 = preparedStatement22.executeQuery();

						while (resultSet22.next()) {

							apiKey = resultSet22.getString("psp_merchant_key");

						}

						System.out.println("apiKey: " + apiKey);

						conn.setRequestProperty("apiKey", apiKey);

						// conn.setRequestProperty("apiKey",
						// "WeAJ1I6kcCKFar9rlAj3teeBfQzQGo81vW9Na8l3xMBDQgD7e4PHooKS7nMZypCz");
						//
						conn.setRequestProperty("charset", "utf-8");
						conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
						conn.setUseCaches(false);
						try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
							wr.writeBytes(reqdata);
						}

						int responseCode = conn.getResponseCode();

						InputStream is;

						if (responseCode == HttpURLConnection.HTTP_OK) {

							is = conn.getInputStream();

						} else {

							is = conn.getErrorStream();

						}

						System.out.println("Response Code : " + responseCode);

						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

						line = "";

						while ((line = rd.readLine()) != null) {
							response.append(line);
							System.out.println("RESPONSE..." + response.toString());
							response.append('\r');
						}

						JSONObject jsonObject2 = new JSONObject(response.toString());

						String cid = jsonObject2.getString("captureRequestId");
						System.out.println("cid: " + cid);

						String ptid = jsonObject2.getString("purchaseTransactionId");
						System.out.println(ptid);

						String prid = jsonObject2.getString("purchaseRequestId");
						System.out.println(prid);

						String ts = jsonObject2.getString("transactionState");
						System.out.println(ts);

						JSONObject jsonObject3 = jsonObject2.getJSONObject("merchantInfo");

						String name = jsonObject3.getString("name");
						System.out.println("name: " + name);

						String tid = jsonObject3.getString("tid");
						System.out.println("tid: " + tid);

						String mid = jsonObject3.getString("mid");
						System.out.println("mid: " + mid);

						// String aid = jsonObject3.getString("aid");
						// jsonObject3.getString("aid: " + aid);

						JSONObject jsonObject4 = jsonObject2.getJSONObject("amount");
						String cur = jsonObject4.getString("currency");
						System.out.println(cur);

						String val = jsonObject4.getString("value");
						System.out.println(val);

						// JSONArray jsonArray = jsonObject2.getJSONArray("purposes");
						// JSONObject purpose = jsonArray.getJSONObject(0);
						// jsonArray.getJSONObject(0);
						// System.out.println("purpose " + purpose);
						// JSONObject jsonamount = new JSONObject("amount");
						// String cuurr = jsonamount.getString("currency");
						// System.out.println("cuurr " + cuurr);
						// String valuee = jsonamount.getString("value");
						// System.out.println("valuee " + valuee);
						// jsonamount.put("currency", cuurr);
						// jsonamount.put("value", valuee);
						// JSONObject newwww = new JSONObject();
						// newwww.put("amount", jsonamount);
						// jsonArray.getJSONObject(1);
						// JSONObject jsonObject4 = new JSONObject();
						// jsonObject4.put("purpose", jsonObject2.getString("purpose"));
						// jsonArray.put(0, jsonObject4);

						try {
							PaymentTransaction paymentTransaction = new PaymentTransaction();

							// paymentTransaction.setAppId(aid);
							paymentTransaction.setPspId("sodexo");

							paymentTransaction.setDeviceId("zd2");

							paymentTransaction.setTerminalId(tid);
							paymentTransaction.setMerchantId("zoneone");
							paymentTransaction.setPspMerchantId(mid);
							paymentTransaction.setProductPrice(val);
							paymentTransaction.setPspTransactionId(ptid);
							double dval = Double.parseDouble(val);
							paymentTransaction.setAuthAmount(dval);
							paymentTransaction.setStatus(ts);
							paymentTransaction.setOrderId(prid);
							paymentTransaction.setMerchantOrderId(ptid);
							paymentTransaction.setServiceType("CHECK_TXN_STATUS");

							SodexoCallbackTransactionDao zetaCallbackTransactionDao = new SodexoCallbackTransactionDaoImpl();

							zetaCallbackTransactionDao.saveTransaction(paymentTransaction);

						} catch (Exception e) {
							System.out.println(e);
						}

						rd.close();
					} catch (IOException e) {
						System.out.println("Exception in ZetaCallbackInitiationDaoImpl Service class.." + e);
						e.printStackTrace();
					}

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
