package com.limitlessmobility.iVendGateway.services.paytm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@Controller
@RequestMapping(value="v2")
public class PaytmRefundServiceV2 {
	
	private static final Logger logger = Logger.getLogger(PayTmCallbackService.class);
	private static final String VERSION="v1";
	
	@RequestMapping(value = "/refundprocess", method = RequestMethod.POST)
	@ResponseBody
	public String getPayTmRefund(@RequestBody String refundRequest) {
		logger.info("refundTransactionRecord Method is calling!!");
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails(refundRequest);
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		
		try {
			JSONObject obj = new JSONObject(refundRequest);
	        
	     
			JSONObject responseJsonObj = obj.getJSONObject("response");
	        paymentTransaction.setTerminalId(responseJsonObj.getString("posId"));
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PaymentController transactionController = new PaymentController();
		String transactionStatus = transactionController.saveTransaction(paymentTransaction);
		
		return "Success";
	}
	
	@RequestMapping(value = "paytmRefundDynamic", method = RequestMethod.POST)
	@ResponseBody
	public String payTmRefundProcess(@RequestBody String refundRequest, String posId) throws Exception {
		logger.info("payTmRefundProcess Method ..."+refundRequest);
		System.out.println("payTmRefundProcess Method ..."+refundRequest);
		CommonDao cdao = new CommonDaoImpl();
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails(refundRequest);
		
		/*String merchantGuid = "ae322099-8e13-4245-a425-fd7daba6865f";*/
		String merchantGuid = "";
		String pspId="";
		String merchantKey="";
		
//		String mid = "Zoneon62132477850670";
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLogResponse = new EventLogs();
		
		JSONObject jsonObj = new JSONObject(refundRequest);
		OperatorDetail operatorDetail = new OperatorDetail();
		
		PspMerchant pspMerchant = new PspMerchant();
		if(jsonObj.getString("platformName")!=null) {
			
			pspId=jsonObj.getString("platformName");
			/*pspMerchant = cdao.getPspMerchant(pspId, posId);
			
			merchantGuid = pspMerchant.getPspMguid();*/
			
			int operatorId = commonCredentialDao.getOperatorId(posId, pspId);
			
			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
			
			
//			pspMerchant = cdao.getPspMerchant(pspId, terminalId);
			/*walletId = operatorDetail.getMerchantId();
			walletKey = operatorDetail.getMerchantKey();*/
			merchantKey = operatorDetail.getMerchantKey();
			merchantGuid = operatorDetail.getPspMguid();
		}
		/*if(jsonObj.getString("posId")!=null) {
			
			deviceId = cdao.getDeviceId(terminalId);
			paytmQRRequestType.setDeviceId(deviceId);
			mid = pspMerchant.getPspMerchantId();
		}*/
		
		jsonObj.put("platformName", pspId);
		jsonObj.put("operationType", "REFUND_MONEY");
//		jsonObj.put("version", "1.0");
		
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		requestJsonObj.put("merchantGuid", merchantGuid);
		
		RandomString RandomString = new RandomString();
//		String refundId = RandomString.generateRandomString(6); 
		
		requestJsonObj.put("refundRefId", requestJsonObj.getString("refundRefId"));
		
		System.out.println("refundId..."+requestJsonObj.getString("refundRefId"));
		JSONObject requestFromJson = jsonObj.getJSONObject("request");
		
		/*paymenttransaction.setOrderId(requestJsonObj.getString("merchantOrderId"));
		paymenttransaction.setAuthAmount(requestFromJson.getString("amount"));
		paymenttransaction.setMerchantOrderId(requestJsonObj.getString("merchantOrderId"));
		paymenttransaction.setTerminalId("440975811");
		paymenttransaction.setCurrency(requestFromJson.getString("currencyCode"));
		paymenttransaction.setProductName(requestFromJson.getString("productId"));*/
		
		
		
		
        paymentTransaction.setTerminalId(posId);
		
		String urlParameters=jsonObj.toString();
		
		System.out.println("URL Parameter.."+urlParameters);
		
		
		String line = "";
		StringBuilder response = new StringBuilder();
		try {
//			String CHECKSUMHASH = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!", urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(merchantKey, urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/refundWalletTxn");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("mid",merchantGuid);

	        connection.setRequestProperty("Content-Type","application/json");
//	        connection.setRequestProperty("merchantGuid", "ae322099-8e13-4245-a425-fd7daba6865f");


	        connection.setRequestProperty("checksumhash",CHECKSUMHASH.trim());


	   System.out.println("My checksumhash is =" +CHECKSUMHASH.toString());
			
		   connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
	       connection.setUseCaches(false);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);

	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
	    		}
	       
	       
	       int responseCode = connection.getResponseCode();

	        System.out.println(responseCode+"\tSuccess");

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = connection.getInputStream();

	        }else {

	        is = connection.getErrorStream();

	        }
	              /*try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
	              {

	           System.out.println("line.."+line);
	              response.append(line);

	              response.append('\r');

	          }           }*/ // or StringBuffer if not Java 5+
	              
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              System.out.append(line);
	              System.out.println("line.."+line);
	              response.append(line);
	              System.out.println("RESPONSE..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
			
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		JSONObject responseJson = new JSONObject(response.toString());
		try {
		responseJson.put("refundCheckStatusId", jsonObj.getString("refundRefId"));
		} catch(Exception e) {
			
		}
		String checkResponse = responseJson.getString("status");
		if(checkResponse.equalsIgnoreCase("SUCCESS")) {
			try{
			JSONObject requestArr = (JSONObject) responseJson.get("response");
			paymentTransaction.setPspTransactionId(requestArr.getString("refundTxnGuid"));
			paymentTransaction.setAuthAmount(Double.valueOf(requestJsonObj.getString("amount")));
			paymentTransaction.setOrderId(responseJson.getString("orderId"));
			paymentTransaction.setStatus(responseJson.getString("status")); 
	        paymentTransaction.setStatusCode(responseJson.getString("statusCode"));
	        paymentTransaction.setStatusMsg(responseJson.getString("statusMessage"));
	        
//	        paymentTransaction.setAuthAmount(txnListJson.getDouble("txnAmount"));
	        paymentTransaction.setOrderId(responseJson.getString("orderId"));
//	        paymentTransaction.setMerchantOrderId(txnListJson.getString("merchantOrderId"));
	        paymentTransaction.setServiceType("Refund Dynamic API");
	        paymentTransaction.setPspId(pspId);
	        paymentTransaction.setDeviceId("d1");
	        paymentTransaction.setMerchantId("zoneone");
			
	        PaymentController transactionController = new PaymentController();
//	        String transactionStatus = transactionController.saveTransaction(paymentTransaction);  
	        DbConfigration dbConfig = new DbConfigrationImpl();
	        Connection conn = dbConfig.getCon();
	        Statement stmt = conn.createStatement();
            String updateRefundedSql = "UPDATE payment_refund " +
                         "SET refund_status = '1', refund_ref_id='"+requestJsonObj.getString("refundRefId")+"' WHERE merchant_order_id ='"+requestJsonObj.getString("merchantOrderId")+"'";
            System.out.println("Paytm update payment_refund.."+updateRefundedSql);
            stmt.executeUpdate(updateRefundedSql);
            try{dbConfig.closeConnection(conn);}catch(Exception e) {}
			} catch(Exception e) {
				System.out.println("Paytm Refund Service Error..."+e);
			}
		}
		
		
		try {
			
			
        	EventLogDao edao = new eventLogDaoImpl();
	        
	        eventLogResponse.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogRequest.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogResponse.setEventDetails(responseJson.toString());
	        eventLogResponse.setEventType("PayTM Refund Dynamic API Response");
	        eventLogRequest.setEventType("PayTM Refund Dynamic API Request");
	        eventLogResponse.setVersion(VERSION);
	        eventLogRequest.setVersion(VERSION);
	        eventLogRequest.setOrderID(paymentTransaction.getOrderId());
	        eventLogResponse.setOrderID(paymentTransaction.getOrderId());
	        edao.saveEventLog(eventLogResponse);
	        edao.saveEventLog(eventLogRequest);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return responseJson.toString();
	}
	
	
	/*@RequestMapping(value = "paytmRefundDynamicManual", method = RequestMethod.POST)
	@ResponseBody
	public String payTmRefundDynamicDirect(@RequestBody String refundRequest) throws Exception {
		logger.info("payTmRefundProcess Method ..."+refundRequest);
		System.out.println("payTmRefundProcess Method ..."+refundRequest);
		CommonDao cdao = new CommonDaoImpl();
		
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails(refundRequest);
		
		String merchantGuid = "ae322099-8e13-4245-a425-fd7daba6865f";
		String merchantGuid = "";
		String pspId="";
		
//		String mid = "Zoneon62132477850670";
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLogResponse = new EventLogs();
		
		JSONObject jsonObj = new JSONObject(refundRequest);
		
		
		PspMerchant pspMerchant = new PspMerchant();
		if(jsonObj.getString("platformName")!=null) {
			
			pspId=jsonObj.getString("platformName");
			pspMerchant = cdao.getPspMerchant(pspId, posId);
			
			merchantGuid = pspMerchant.getPspMguid();
		}
		if(jsonObj.getString("posId")!=null) {
			
			deviceId = cdao.getDeviceId(terminalId);
			paytmQRRequestType.setDeviceId(deviceId);
			mid = pspMerchant.getPspMerchantId();
		}
		
		jsonObj.put("platformName", pspId);
		jsonObj.put("operationType", "REFUND_MONEY");
//		jsonObj.put("version", "1.0");
		
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		requestJsonObj.put("merchantGuid", merchantGuid);
		
		RandomString RandomString = new RandomString();
		String refundId = RandomString.generateRandomString(6); 
		requestJsonObj.put("refundRefId", refundId);
		
		System.out.println("refundId..."+refundId);
		JSONObject requestFromJson = jsonObj.getJSONObject("request");
		
		paymenttransaction.setOrderId(requestJsonObj.getString("merchantOrderId"));
		paymenttransaction.setAuthAmount(requestFromJson.getString("amount"));
		paymenttransaction.setMerchantOrderId(requestJsonObj.getString("merchantOrderId"));
		paymenttransaction.setTerminalId("440975811");
		paymenttransaction.setCurrency(requestFromJson.getString("currencyCode"));
		paymenttransaction.setProductName(requestFromJson.getString("productId"));
		
		
		
		
        paymentTransaction.setTerminalId(posId);
		
		String urlParameters=jsonObj.toString();
		
		System.out.println("URL Parameter.."+urlParameters);
		
		
		String line = "";
		StringBuilder response = new StringBuilder();
		try {
//			String CHECKSUMHASH = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!", urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(cdao.getPspMerchantKey(pspId,posId), urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/refundWalletTxn");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("mid",merchantGuid);

	        connection.setRequestProperty("Content-Type","application/json");
//	        connection.setRequestProperty("merchantGuid", "ae322099-8e13-4245-a425-fd7daba6865f");


	        connection.setRequestProperty("checksumhash",CHECKSUMHASH.trim());


	   System.out.println("My checksumhash is =" +CHECKSUMHASH.toString());
			
		   connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
	       connection.setUseCaches(false);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);

	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
	    		}
	       
	       
	       int responseCode = connection.getResponseCode();

	        System.out.println(responseCode+"\tSuccess");

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = connection.getInputStream();

	        }else {

	        is = connection.getErrorStream();

	        }
	              try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
	              {

	           System.out.println("line.."+line);
	              response.append(line);

	              response.append('\r');

	          }           } // or StringBuffer if not Java 5+
	              
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              System.out.append(line);
	              System.out.println("line.."+line);
	              response.append(line);
	              System.out.println("RESPONSE..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
			
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		JSONObject responseJson = new JSONObject(response.toString());
		responseJson.put("refundCheckStatusId", refundId);
		
		String checkResponse = responseJson.getString("status");
		if(checkResponse.equalsIgnoreCase("SUCCESS")) {
			try{
			JSONObject requestArr = (JSONObject) responseJson.get("response");
			paymentTransaction.setPspTransactionId(requestArr.getString("refundTxnGuid"));
			paymentTransaction.setAuthAmount(Double.valueOf(requestJsonObj.getString("amount")));
			paymentTransaction.setOrderId(responseJson.getString("orderId"));
			paymentTransaction.setStatus(responseJson.getString("status")); 
	        paymentTransaction.setStatusCode(responseJson.getString("statusCode"));
	        paymentTransaction.setStatusMsg(responseJson.getString("statusMessage"));
	        
//	        paymentTransaction.setAuthAmount(txnListJson.getDouble("txnAmount"));
	        paymentTransaction.setOrderId(responseJson.getString("orderId"));
//	        paymentTransaction.setMerchantOrderId(txnListJson.getString("merchantOrderId"));
	        paymentTransaction.setServiceType("Refund Dynamic API");
	        paymentTransaction.setPspId(pspId);
	        paymentTransaction.setDeviceId("d1");
	        paymentTransaction.setMerchantId("zoneone");
			
	        PaymentController transactionController = new PaymentController();
//	        String transactionStatus = transactionController.saveTransaction(paymentTransaction);  
			} catch(Exception e) {
				System.out.println("Paytm Refund Service Error..."+e);
			}
		}
		
		
		try {
			
			
        	EventLogDao edao = new eventLogDaoImpl();
	        
	        eventLogResponse.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogRequest.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogResponse.setEventDetails(responseJson.toString());
	        eventLogResponse.setEventType("PayTM Refund Dynamic API Response");
	        eventLogRequest.setEventType("PayTM Refund Dynamic API Request");
	        eventLogResponse.setVersion(VERSION);
	        eventLogRequest.setVersion(VERSION);
	        eventLogRequest.setOrderID(paymentTransaction.getOrderId());
	        eventLogResponse.setOrderID(paymentTransaction.getOrderId());
	        edao.saveEventLog(eventLogResponse);
	        edao.saveEventLog(eventLogRequest);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return responseJson.toString();
	}*/
	
	@RequestMapping(value = "paytmRefundStatic", method = RequestMethod.POST)
	@ResponseBody
	public String payTmStaticRefundProcess(@RequestBody String refundRequest) throws Exception {
		logger.info("payTmRefundProcess Method ...."+refundRequest);
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails(refundRequest);
		
		String merchantGuid = "ae322099-8e13-4245-a425-fd7daba6865f";
				
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLogResponse = new EventLogs();
        paymentTransaction.setTerminalId("440975811");

		JSONObject jsonObj = new JSONObject(refundRequest);
		jsonObj.put("platformName", "PayTM");
		jsonObj.put("operationType", "REFUND_MONEY");
		jsonObj.put("version", "1.0");
		
		JSONObject requestJsonObj = jsonObj.getJSONObject("request");
		requestJsonObj.put("merchantGuid", merchantGuid);
		
		RandomString RandomString = new RandomString();
		String refundId = RandomString.generateRandomString(6); 
		requestJsonObj.put("refundRefId", refundId);
		
		
		
		String urlParameters=jsonObj.toString();
		
		String line = "";
		StringBuilder response = new StringBuilder();
		try {
//			String CHECKSUMHASH = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!", urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum("bc7TaII#8xYe%_D9", urlParameters);
			URL url = new URL("https://trust.paytm.in/wallet-web/refundWalletTxn");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("mid",merchantGuid);

	        connection.setRequestProperty("Content-Type","application/json");
//	        connection.setRequestProperty("merchantGuid", "ae322099-8e13-4245-a425-fd7daba6865f");


	        connection.setRequestProperty("checksumhash",CHECKSUMHASH.trim());


	   System.out.println("My checksumhash is =" +CHECKSUMHASH.toString());
			
		   connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length).trim());
	       connection.setUseCaches(false);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);

	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
	    		}
	       
	       
	       int responseCode = connection.getResponseCode();

	        System.out.println(responseCode+"\tSuccess");

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = connection.getInputStream();

	        }else {

	        is = connection.getErrorStream();

	        }
	              /*try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
	              {

	           System.out.println("line.."+line);
	              response.append(line);

	              response.append('\r');

	          }           }*/ // or StringBuffer if not Java 5+
	              
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              System.out.append(line);
	              System.out.println("line.."+line);
	              response.append(line);
	              System.out.println("RESPONSE..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
			
		} catch (IOException e) {
			System.out.println("Exception in paytm ststic refund class.."+e);
			e.printStackTrace();
		}
		JSONObject responseJson = new JSONObject(response.toString());
		responseJson.put("refundCheckStatusId", refundId);
		
		String checkResponse = responseJson.getString("status");
		if(checkResponse.equalsIgnoreCase("SUCCESS")) {
			try{
			JSONObject requestArr = (JSONObject) responseJson.get("response");
			paymentTransaction.setPspTransactionId(requestArr.getString("refundTxnGuid"));
			paymentTransaction.setAuthAmount(Double.valueOf(requestJsonObj.getString("amount")));
			paymentTransaction.setOrderId(responseJson.getString("orderId"));
			paymentTransaction.setStatus(responseJson.getString("status")); 
	        paymentTransaction.setStatusCode(responseJson.getString("statusCode"));
	        paymentTransaction.setStatusMsg(responseJson.getString("statusMessage"));
	        
//	        paymentTransaction.setAuthAmount(txnListJson.getDouble("txnAmount"));
	        paymentTransaction.setOrderId(responseJson.getString("orderId"));
//	        paymentTransaction.setMerchantOrderId(txnListJson.getString("merchantOrderId"));
	        paymentTransaction.setServiceType("Refund Dynamic API");
	        paymentTransaction.setPspId("paytm");
	        paymentTransaction.setDeviceId("d1");
	        paymentTransaction.setMerchantId("zoneone");
			
	        PaymentController transactionController = new PaymentController();
	        String transactionStatus = transactionController.saveTransaction(paymentTransaction);
			} catch(Exception e) {
				System.out.println("Paytm Refund Service Error..."+e);
			}
		}
		
		try {
        	EventLogDao edao = new eventLogDaoImpl();
	        
	        eventLogResponse.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogRequest.setTerminalId(paymentTransaction.getTerminalId());
	        eventLogResponse.setEventDetails(response.toString());
	        eventLogResponse.setEventType("PayTM Refund Static API Response");
	        eventLogRequest.setEventType("PayTM Refund Static API Response");
	        eventLogResponse.setVersion(VERSION);
	        eventLogRequest.setVersion(VERSION);
	        eventLogRequest.setOrderID(paymentTransaction.getOrderId());
	        eventLogResponse.setOrderID(paymentTransaction.getOrderId());
	        edao.saveEventLog(eventLogResponse);
	        edao.saveEventLog(eventLogRequest);
		} catch (Exception e) {
			System.out.println(e);
		}
		return response.toString();
	}
	
	/**
	 * This Method is used for automatic refund
	 * @param refundRequest
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value = "/autorefundprocess", method = RequestMethod.POST)
	@ResponseBody
	public String getAutoRefundPaytm(@RequestBody String refundRequest) throws  IOException {
		logger.info("refundTransactionRecord Method is calling!!");
//		ObjectMapper mapper = new ObjectMapper();
		EventLogs eventLogRequest = new EventLogs();
		eventLogRequest.setEventDetails(refundRequest);
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		
		try{
			
			
		} catch(Exception e) {
			logger.info("");
		}
		
		
		try {
			PaytmRefundRequest request = new PaytmRefundRequest();
			PaytmRefundRequestModelFinal finalRequestObj = new PaytmRefundRequestModelFinal();
			String convertedToString = String.valueOf(finalRequestObj); 
			String jsonInString = finalRequestObj.toString();
			System.out.println(convertedToString);
			System.out.println("String.."+jsonInString);
			
			JSONObject obj = new JSONObject(refundRequest);
	        
	        JSONObject responseJsonObj = obj.getJSONObject("response");
	        paymentTransaction.setTerminalId(responseJsonObj.getString("posId"));
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PaymentController transactionController = new PaymentController();
		String transactionStatus = transactionController.saveTransaction(paymentTransaction);
		
		return "Success";
	}

}
