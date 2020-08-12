package com.limitlessmobility.iVendGateway.services.paytm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.AppResponse;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;
import com.paytm.pg.merchant.CheckSumServiceHelper;


/**
 * @author RamNarayan
 * @version 1.0 This class is used for check status of PayTM
 */
@Controller
@RequestMapping("/v2")
public class PayTmCheckStatusV2 {

	private static final Logger logger = Logger.getLogger(PayTmCheckStatus.class);
	public static final String VERSION = "v1";
	CommonDao cdao = new CommonDaoImpl();

	/*
	 * This Method is used to check status using transactions using transaction
	 * ID.
	 */
//	@RequestMapping(value = "/paymentCheckStatus", method = RequestMethod.POST)
//	@ResponseBody
	public String getPaytmCheckStatus(@RequestBody String request, String pspId, String appId, String orderId, String posId, String amount) throws Exception {

		System.out.println("getPaytmCheckStatus Method START..request.." + request + "...orderid..." + orderId + "...terminalID..." + posId);
		logger.info("request.." + request + "...orderid..." + orderId + "...terminalID..." + posId);
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails("request.." + request + "...orderid..." + orderId + "...terminalID..." + posId);
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		JSONObject superjson = new JSONObject(request);
		JSONObject requestjson = superjson.getJSONObject("request");
		String urlParameters = request.toString();
		String line = "";
		StringBuilder response = new StringBuilder();
		String deviceId = "";
//		String pspId="";
		String merchantId = "";
		OperatorDetail operatorDetail = new OperatorDetail();
		try {
//			PspMerchant pspMerchant = new PspMerchant();
				
			
			int operatorId = commonCredentialDao.getOperatorId(posId, pspId);
			operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, pspId);
			
			merchantId = operatorDetail.getMerchantId();
			requestjson.put("merchantGuid", operatorDetail.getPspMguid());
			
			superjson.remove("request");
			superjson.put("request", requestjson);
			urlParameters = superjson.toString();
					
			
			
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(operatorDetail.getMerchantKey(), urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/checkStatus");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

//			connection.setRequestProperty("mid", "ae322099-8e13-4245-a425-fd7daba6865f");
			connection.setRequestProperty("mid", operatorDetail.getPspMguid().trim());

			connection.setRequestProperty("Content-Type", "application/json");
			// connection.setRequestProperty("merchantGuid",
			// "d6c635da-fefc-4e4e-9039-4580a9a2dea2");

			connection.setRequestProperty("checksumhash", CHECKSUMHASH.trim());

			System.out.println("My checksumhash is = " + CHECKSUMHASH.toString());

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
			connection.setUseCaches(false);

			connection.setDoOutput(true);
			// response.setContentType(“application/json”);
			System.out.println("urlParameters.....CheckStatus..."+urlParameters);
			try (DataOutputStream wr = new DataOutputStream(

					connection.getOutputStream())) {
				wr.writeBytes(urlParameters);
			}

			int responseCode = connection.getResponseCode();

			System.out.println(responseCode + "\tSuccess");

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = connection.getInputStream();

			} else {

				is = connection.getErrorStream(); 

			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				System.out.append(line);
				System.out.println("line.." + line);
				response.append(line);
				System.out.println("RESPONSE..." + response.toString());
				System.out.append("\r");

			}
			rd.close();

		} catch (IOException e) {
			System.out.println("Exception in Paytm check Status class.." + e);
			e.printStackTrace();
		}
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		AppResponse appResponse = new AppResponse();

		// JSONObject requestJson = new JSONObject(urlParameters.toString());
		JSONObject checkStatusModelJson = new JSONObject(response.toString());
		EventLogs eventLogResponse = new EventLogs();

		JSONObject responeArrayJson = new JSONObject();
		JSONArray txnListJsonArr = new JSONArray();
		JSONObject txnListJson = new JSONObject();

		/*
		 * RandomString msr = new RandomString(); String uniqueOrderId =
		 * msr.generateRandomString();
		 * System.out.println("uniqueOrderId.."+uniqueOrderId);
		 * jsonRequest.remove("orderId"); jsonRequest.put("orderId",
		 * uniqueOrderId);
		 */

		String checkResponse = checkStatusModelJson.getString("status");
		if (checkResponse.equalsIgnoreCase("SUCCESS")) {
		JSONObject responseInternalJson = checkStatusModelJson.getJSONObject("response");	
			if (checkStatusModelJson.isNull("response")) {} else{

			JSONObject responseStrJson = checkStatusModelJson.getJSONObject("response");
			
			JSONArray txnListArr = responseStrJson.getJSONArray("txnList");
			
			JSONObject txnJson = txnListArr.getJSONObject(0);
			
			if(txnJson.getString("message").toString().equalsIgnoreCase("SUCCESS")) {
				
			responeArrayJson = checkStatusModelJson.getJSONObject("response");
			txnListJsonArr = responeArrayJson.getJSONArray("txnList");
			txnListJson = txnListJsonArr.getJSONObject(0);

			paymentTransaction.setTerminalId(posId);
			paymentTransaction.setAppId(appId);
			paymentTransaction.setStatus(checkStatusModelJson.getString("status"));
			paymentTransaction.setStatusCode(checkStatusModelJson.getString("statusCode"));
			paymentTransaction.setStatusMsg(checkStatusModelJson.getString("statusMessage"));

			paymentTransaction.setAuthAmount(txnListJson.getDouble("txnAmount"));
			paymentTransaction.setPaidAmount(txnListJson.getDouble("txnAmount"));
			paymentTransaction.setOrderId(txnListJson.getString("merchantOrderId"));
			paymentTransaction.setMerchantOrderId(txnListJson.getString("merchantOrderId"));
			paymentTransaction.setPspTransactionId(txnListJson.getString("txnGuid"));
			paymentTransaction.setTransactionType("0");

			paymentTransaction.setServiceType("CHECK_TXN_STATUS");
			paymentTransaction.setPspId(pspId);
			paymentTransaction.setDeviceId(deviceId);
			paymentTransaction.setMerchantId(merchantId);
			paymentTransaction.setTerminalId(posId);
			paymentTransaction.setAppId(appId);

			appResponse.setStatus(checkStatusModelJson.getString("status"));
			appResponse.setStatusCode(checkStatusModelJson.getString("statusCode"));
			appResponse.setStatusMsg(txnListJson.getString("message"));

			PaymentController transactionController = new PaymentController();
			String transactionStatus = transactionController.saveTransaction(paymentTransaction);
			}
		}
		}

		/*
		 * paymentTransaction.setTerminalId(responseJsonObj.getString("posId"));
		 * paymentTransaction.setAuthAmount(responseJsonObj.getDouble(
		 * "txnAmount"));
		 * paymentTransaction.setAuthDate(String.valueOf(responseJsonObj.getLong
		 * ("timestamp")));
		 * paymentTransaction.setComments(responseJsonObj.getString("comment"));
		 */

		// JSONObject responseJsonObj = new JSONObject(response.toString());

		JSONObject responseJsonObj = new JSONObject();

	 String status=	appResponse.getStatus();
	 String statuscode=	appResponse.getStatusCode();
	 String statusmsg=	appResponse.getStatusMsg();


		responseJsonObj.put("orderId", appResponse.getOrderId());

		responseJsonObj.put("statusMessage", statusmsg);

		System.out.println("STSUSSSSSSSSSSSSS.."+status);
		if(status!=null){
			responseJsonObj.put("status", status);
		} else {
			responseJsonObj.put("status", "FAILURE");
		}
		responseJsonObj.put("statusCode", statuscode);
		responseJsonObj.put("amount", paymentTransaction.getAuthAmount());

		try {
			EventLogDao edao = new eventLogDaoImpl();

			eventLogResponse.setTerminalId(paymentTransaction.getTerminalId());
			eventLogRequest.setTerminalId(paymentTransaction.getTerminalId());
			eventLogResponse.setEventDetails(responseJsonObj.toString());
			eventLogResponse.setEventType("PayTM CheckStatus APIResponse");
			eventLogRequest.setEventType("PayTM CheckStatus APIRequest");
			eventLogResponse.setVersion(VERSION);
			eventLogRequest.setVersion(VERSION);

			eventLogRequest.setOrderID(paymentTransaction.getOrderId());
			eventLogResponse.setOrderID(paymentTransaction.getOrderId());

			edao.saveEventLog(eventLogRequest);
			edao.saveEventLog(eventLogResponse);
		} catch (Exception e) {
			System.out.println(e);
		}

		return responseJsonObj.toString();
	}

	/*
	 * This Method is used for check status of paytm refund
	 */
	@RequestMapping(value = "/refundCheckStatuss", method = RequestMethod.POST)
	@ResponseBody
	public String refundCheckStatus(@RequestBody String request, String orderId, int operatorId) throws Exception {

		System.out.println("request.." + request + ".." + "orderid..." + orderId);
		logger.info("request.." + request + ".." + "orderid..." + orderId);
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails("request.." + request + ".." + "orderid..." + orderId);

		String urlParameters = request.toString();
		String line = "";
		StringBuilder response = new StringBuilder();
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		try {
			OperatorDetail operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, "paytm");
			// String CHECKSUMHASH =
			// CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!",
			// urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(operatorDetail.getMerchantKey(), urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/checkStatus");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("mid", operatorDetail.getPspMguid().trim());

			connection.setRequestProperty("Content-Type", "application/json");
			// connection.setRequestProperty("merchantGuid",
			// "d6c635da-fefc-4e4e-9039-4580a9a2dea2");

			connection.setRequestProperty("checksumhash", CHECKSUMHASH.trim());

			System.out.println("My checksumhash is =" + CHECKSUMHASH.toString());

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
			connection.setUseCaches(false);

			connection.setDoOutput(true);
			// response.setContentType(“application/json”);

			try (DataOutputStream wr = new DataOutputStream(

					connection.getOutputStream())) {
				wr.writeBytes(urlParameters);
			}

			int responseCode = connection.getResponseCode();

			System.out.println(responseCode + "\tSuccess");

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = connection.getInputStream();

			} else {

				is = connection.getErrorStream();

			}
			/*
			 * try (BufferedReader rd = new BufferedReader(new
			 * InputStreamReader(is))) { response = new StringBuilder(); // or
			 * StringBuffer if not Java 5+ String line = ""; while((line =
			 * rd.readLine()) != null) {
			 * 
			 * System.out.println("line.."+line); response.append(line);
			 * 
			 * response.append('\r');
			 * 
			 * } }
			 */ // or StringBuffer if not Java 5+

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				System.out.append(line);
				System.out.println("line.." + line);
				response.append(line);
				System.out.println("RESPONSE..." + response.toString());
				System.out.append("\r");

			}
			rd.close();

		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLogResponse = new EventLogs();
		JSONObject checkStatusModelJson = new JSONObject(response.toString());

		JSONObject responeArrayJson = new JSONObject();
		JSONArray txnListJsonArr = new JSONArray();
		JSONObject txnListJson = new JSONObject();

		/*
		 * RandomString msr = new RandomString(); String uniqueOrderId =
		 * msr.generateRandomString();
		 * System.out.println("uniqueOrderId.."+uniqueOrderId);
		 * jsonRequest.remove("orderId"); jsonRequest.put("orderId",
		 * uniqueOrderId);
		 */
		
		JSONObject responseJsonObj = new JSONObject(response.toString());
		responseJsonObj.put("orderId", orderId);

		

		return responseJsonObj.toString();
	}
	
	/*
	 * This Method is used for check status of paytm refund
	 */
	@RequestMapping(value = "/refundCheckStatusDirect", method = RequestMethod.POST)
	@ResponseBody
	public String refundCheckStatusDirect(@RequestBody String refundOrderId) throws Exception {

		
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails("request.." + refundOrderId + ".." + "orderid..." + refundOrderId);

		
		String line = "";
		StringBuilder response = new StringBuilder();
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		try {
			int operatorId = 1;
			OperatorDetail operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, "paytm");
			
			
			
			JSONObject request = new JSONObject();
			request.put("requestType", "refundreftxnid");
			request.put("txnType", "normalrefund");
			request.put("merchantGuid", operatorDetail.getPspMguid());
			request.put("txnId", refundOrderId);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("platformName", "PayTM");
			jsonObj.put("operationType", "CHECK_TXN_STATUS");
			
			jsonObj.put("request", request);
			
			String urlParameters = jsonObj.toString();
			// String CHECKSUMHASH =
			// CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!",
			// urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(operatorDetail.getMerchantKey(), urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/checkStatus");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("mid", operatorDetail.getPspMguid().trim());

			connection.setRequestProperty("Content-Type", "application/json");
			// connection.setRequestProperty("merchantGuid",
			// "d6c635da-fefc-4e4e-9039-4580a9a2dea2");

			connection.setRequestProperty("checksumhash", CHECKSUMHASH.trim());

			System.out.println("My checksumhash is =" + CHECKSUMHASH.toString());

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
			connection.setUseCaches(false);

			connection.setDoOutput(true);
			// response.setContentType(“application/json”);

			try (DataOutputStream wr = new DataOutputStream(

					connection.getOutputStream())) {
				wr.writeBytes(urlParameters);
			}

			int responseCode = connection.getResponseCode();

			System.out.println(responseCode + "\tSuccess");

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = connection.getInputStream();

			} else {

				is = connection.getErrorStream();

			}
			/*
			 * try (BufferedReader rd = new BufferedReader(new
			 * InputStreamReader(is))) { response = new StringBuilder(); // or
			 * StringBuffer if not Java 5+ String line = ""; while((line =
			 * rd.readLine()) != null) {
			 * 
			 * System.out.println("line.."+line); response.append(line);
			 * 
			 * response.append('\r');
			 * 
			 * } }
			 */ // or StringBuffer if not Java 5+

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				System.out.append(line);
				System.out.println("line.." + line);
				response.append(line);
				System.out.println("RESPONSE..." + response.toString());
				System.out.append("\r");

			}
			rd.close();

		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLogResponse = new EventLogs();
		JSONObject checkStatusModelJson = new JSONObject(response.toString());

		JSONObject responeArrayJson = new JSONObject();
		JSONArray txnListJsonArr = new JSONArray();
		JSONObject txnListJson = new JSONObject();

		/*
		 * RandomString msr = new RandomString(); String uniqueOrderId =
		 * msr.generateRandomString();
		 * System.out.println("uniqueOrderId.."+uniqueOrderId);
		 * jsonRequest.remove("orderId"); jsonRequest.put("orderId",
		 * uniqueOrderId);
		 */
		
		JSONObject responseJsonObj = new JSONObject(response.toString());
		responseJsonObj.put("orderId", refundOrderId);

		

		return responseJsonObj.toString();
	}
}
