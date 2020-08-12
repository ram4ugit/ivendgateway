package com.limitlessmobility.iVendGateway.services.zeta;

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

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.zeta.ZetaRefundDao;
import com.limitlessmobility.iVendGateway.dao.zeta.ZetaRefundDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaRefundModel;

@Controller
@RequestMapping("/v2/zeta")
public class ZetaRefundServiceV2 {

	private static final Logger logger = Logger.getLogger(ZetaRefundService.class);

	DbConfigration dbConfig = new DbConfigrationImpl();
	ZetaRefundDao refundDao=new ZetaRefundDaoImpl();
	CommonCredentialDao credentialDao=new CommonCredentialDaoImpl();
	OperatorDetail operatorDetail=new OperatorDetail();
	PspTerminal pspTerminal=new PspTerminal();

	String line = "";
	StringBuilder response = new StringBuilder();

	String jsonString = "";
	String output = "";
	String apiKey = "";
	String purchaseTransactionId1 = "";
	JSONObject resObgJson = new JSONObject();
	String terminalId="";
	String pspId="";
	

	@RequestMapping(value = "/refundprocess", method = RequestMethod.POST)
	@ResponseBody
	public String getZetaRefund(@RequestBody ZetaRefundModel zetaRefundModel) throws JSONException {

		boolean isConnected = true;

		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {

				RandomString RandomString = new RandomString();

				String requestId = RandomString.generateRandomString(8);

				System.out.println("requestId: " + requestId);

				String amount = zetaRefundModel.getAmount();

				System.out.println("amount: " + amount);

				String transactionId = zetaRefundModel.getTransactionId();

				System.out.println("transactionid: " + transactionId);

				JSONObject jsonObject = new JSONObject(zetaRefundModel);

				System.out.println("request data: " + jsonObject.toString());

				JSONObject jsonObject2 = new JSONObject();

				jsonObject2.put("requestId", requestId);

				jsonObject2.put("transactionId", refundDao.getOrderId(transactionId));

				JSONObject jsonObject3 = new JSONObject();
				jsonObject3.put("currency", "INR");
				jsonObject3.put("value", amount);

				jsonObject2.put("amount", jsonObject3);

				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject4 = new JSONObject();
				jsonObject4.put("purpose", "FOOD");

				JSONObject jsonObject5 = new JSONObject();
				jsonObject5.put("currency", "INR");
				jsonObject5.put("value", amount);
				jsonObject4.put("amount", jsonObject5);

				jsonArray.put(0, jsonObject4);

				jsonObject2.put("purposes", jsonArray);

				System.out.println("req given data:" + jsonObject2.toString());

				String reqdata = jsonObject2.toString();

				try {

					// dynamicly get key using psp id from pspmrchnt
					
					pspTerminal=refundDao.getTransactionDetailsZeta(transactionId);
					
					if (pspTerminal.getTerminal() != null) {
					   terminalId=pspTerminal.getTerminal();
					   logger.info("TERMINALID..." + terminalId);
					if (pspTerminal.getPspId() != null) {
						pspId = pspTerminal.getPspId();
					    logger.info("PSPID..." + pspId);
					    System.out.println("pspId " + pspId);
					    
					    int operatorId = credentialDao.getOperatorId(terminalId,pspId);
					    operatorDetail = credentialDao.getPspConfigDetail(operatorId, pspId);
					    
	                    apiKey  = operatorDetail.getMerchantKey();
					    
					    }
					
					}
					
					byte[] postData = reqdata.getBytes(StandardCharsets.UTF_8);
					int postDataLength = postData.length;
					String request = "https://pay.gw.zetapay.in/v1.0/zeta/transactions/refund";
					URL url = new URL(request);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setInstanceFollowRedirects(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json");
					System.out.println("apiKey: " + apiKey);

					conn.setRequestProperty("apiKey", apiKey);

					conn.setRequestProperty("charset", "utf-8");
					conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
					conn.setUseCaches(false);
					try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
						wr.writeBytes(reqdata);
					}

					int responseCode = conn.getResponseCode();
					String responseBody = conn.getResponseMessage();

					System.out.println("Response Code : " + responseCode);
					System.out.println("Response Body : " + responseBody);

					InputStream is;

					if (responseCode == HttpURLConnection.HTTP_OK) {

						is = conn.getInputStream();
						System.out.println("when response code HTTP_OK then input stream is: " + is);

						/***********/

						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

						line = "";

						while ((line = rd.readLine()) != null) {
							response.append(line);
							System.out.println("RESPONSE..." + response.toString());
							response.append('\r');

						}

						System.out.println(response.toString());
						rd.close();

						/***********/

						JSONObject jsonObject1 = new JSONObject(response.toString());

						resObgJson.put("status", "success");
						resObgJson.put("data", jsonObject1);

					} else {

						is = conn.getErrorStream();
						System.out.println("when response code Error then input stream is: " + is);

						/*******/

						BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
						line = "";
						while ((line = rd.readLine()) != null) {
							response.append(line);
							response.append('\r');
						}
						System.out.println("RESPONSE..." + response.toString());
						rd.close();

						/*******/

						JSONObject jsonObject12 = new JSONObject(response.toString());

						resObgJson.put("status", "failure");
						resObgJson.put("data", jsonObject12);

					}
				} catch (IOException e) {
					System.out.println("Exception in Refund Service class.." + e);
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resObgJson.toString();
	}
}
