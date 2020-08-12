package com.limitlessmobility.iVendGateway.services.paytm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobil.ivendgateway.util.ResponseCodes;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.CommonQRRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmFinalQRRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmQRRequestType;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;
import com.paytm.pg.merchant.CheckSumServiceHelper;

/**
 * @author RamNarayan
 * @version 1.0
 * This class is used for get dynamic QR of PayTM
 */
@Controller
@RequestMapping(value="v2/paytm")
public class PayTmQRServiceV2 {

	private static final Logger logger = Logger.getLogger(PayTmQRService.class);

	CommonDao cdao = new CommonDaoImpl();
	QRDao qrDao = new QRDaoImpl();
	
	
	/*
	 * This Method is used for generate PayTM QR
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String createQRCode(@RequestBody CommonQRRequest requestQR) throws Exception {
		System.out.println("Calling PayTM Dynamic QR (createQRCode Method)...START... "+requestQR);
		//This object is used for save request event log in DB
		EventLogs eventLogRequest = new EventLogs();	
		eventLogRequest.setEventDetails(requestQR.toString());
		
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		//Generate Random Order Id
		RandomString randomOrderId = new RandomString();
        String uniqueOrderId = randomOrderId.generateRandomString(16);
        
		PaymentInitiation paymentInitiation = new PaymentInitiation(); //This object used for save QR data in DB
		
		EventLogs eventLogResponse = new EventLogs(); //used for save response logs in DB
		
		
		PaytmFinalQRRequest paytmFinalQRRequest= new PaytmFinalQRRequest(); //Final request for Paytm QR
		PaytmQRRequestType paytmQRRequestType = new PaytmQRRequestType(); //This is used in paytm QR request
		String line = "";
		StringBuilder response = new StringBuilder();
		String merchantGuid = "";
		String deviceId = "";
		String pspId="";
		String terminalId="";
		String merchantId = "";
		OperatorDetail operatorDetail = new OperatorDetail();
		
		try {
			
			if(requestQR.getPspId()!=null) {
				terminalId= requestQR.getPosId();
				pspId=requestQR.getPspId();
				int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
				
				operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, pspId);
				merchantId = operatorDetail.getMerchantId();
				merchantGuid = operatorDetail.getPspMguid();
				
				deviceId = cdao.getDeviceId(terminalId);
				paytmQRRequestType.setDeviceId(deviceId);
			}
			if(requestQR.getProductId()!=null) {
				paytmQRRequestType.setProductId(requestQR.getProductId());
			}
			paytmFinalQRRequest.setOperationType("QR_CODE");
			paytmFinalQRRequest.setIpAddress("139.59.73.155");
			paytmFinalQRRequest.setPlatformName("PayTM");
			
			paytmQRRequestType.setRequestType("QR_ORDER");
	        paytmQRRequestType.setMerchantGuid(merchantGuid);
	        paytmQRRequestType.setOrderId(uniqueOrderId);
	        paytmQRRequestType.setValidity("7");
	        paytmQRRequestType.setIndustryType("RETAIL");
	        paytmQRRequestType.setOrderDetails(paytmQRRequestType.getProductId());
	        paytmQRRequestType.setAmount(String.valueOf(requestQR.getAmount()));

	        
	        paymentInitiation.setOrderId(uniqueOrderId);
	        paymentInitiation.setAuthAmount(paytmQRRequestType.getAmount());
	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
	        paymentInitiation.setTerminalId(terminalId);
	        paymentInitiation.setCurrency("INR");
	        paymentInitiation.setProductName(paytmQRRequestType.getProductId());
	        paymentInitiation.setProductPrice(paytmQRRequestType.getAmount());
	        paymentInitiation.setDeviceId(deviceId);
	        paymentInitiation.setPspId(pspId);
	        paymentInitiation.setMerchantId(merchantId);
	        paymentInitiation.setComments(paytmFinalQRRequest.getOperationType());
	        paymentInitiation.setAppId(requestQR.getAppId());
	        
	        eventLogResponse.setTerminalId(terminalId);
	        eventLogRequest.setTerminalId(terminalId);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		paytmFinalQRRequest.setRequest(paytmQRRequestType);
		Gson gson = new Gson();
		
		String jsonRequest = gson.toJson(paytmFinalQRRequest);
		System.out.println("Converted QR Request .."+jsonRequest);
		String urlParameters=jsonRequest;
		//System.out.println("urlParameters.."+urlParameters);
		
		paymentInitiation.setOrderId(uniqueOrderId);
		
		try{
			//Saving Payment Initiation
			qrDao.saveQRRequest(paymentInitiation);
		} catch(Exception e) {
			System.out.println("Exception in Saving QR payment initiation"+e);
		}
		
		EventLogDao eventDao = new eventLogDaoImpl();
		
		try {
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
//			System.out.println(cdao.getPspMerchantKey(pspId,terminalId));
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(operatorDetail.getMerchantKey(), urlParameters);
			URL url = new URL("https://wallet.paytm.in/wallet-merchant/v2/createQRCode");
			
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("mid",merchantId.trim());
	        connection.setRequestProperty("Content-Type","application/json");
	        connection.setRequestProperty("merchantGuid", merchantGuid);
	        connection.setRequestProperty("checksumhash",CHECKSUMHASH);
	        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
	       	connection.setUseCaches(false);
	       	connection.setDoOutput(true);
	       
	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
	    		}
	       
	       int responseCode = connection.getResponseCode();

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = connection.getInputStream();

	        }else {

	        is = connection.getErrorStream();

	        }
	              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	          rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(response.toString());
		JSONObject finalResponse = new JSONObject(j.get("response").toString());
		
		finalResponse.remove("path");
		finalResponse.put("orderId", uniqueOrderId);
		System.out.println();
		eventLogResponse.setEventType("PayTM Dynamic QR Reponse");
		eventLogResponse.setVersion(ResponseCodes.VERSION);
		eventLogResponse.setEventDetails(finalResponse.toString());
		
		eventLogRequest.setOrderID(paymentInitiation.getOrderId());
		eventLogResponse.setOrderID(paymentInitiation.getOrderId());
		
		eventLogRequest.setEventType("PayTM Dynamic QR Request");
		eventLogRequest.setVersion("v1");
		
		eventDao.saveEventLog(eventLogRequest);
		eventDao.saveEventLog(eventLogResponse);
		
		System.out.println("Calling PayTM Dynamic QR (createQRCode Method)...END");
		return finalResponse.toString();
	}
	
	/*@RequestMapping(value = "/machine/paytmQRCode", method = RequestMethod.POST)
	@ResponseBody
	public String createMacineQRCode(@RequestBody String requestQR) throws Exception {
		logger.info("Calling PayTM Dynamic Machine QR...START");
		logger.info("request..."+requestQR);
		System.out.println("Dynamic QR request..."+requestQR);
		//This object is used for save request event log in DB
		EventLogs eventLogRequest = new EventLogs();	
		eventLogRequest.setEventDetails(requestQR);
		
		String merchantGuid = "ae322099-8e13-4245-a425-fd7daba6865f";
		logger.info("merchantGuid..ae322099-8e13-4245-a425-fd7daba6865f");
		
		String validity="7";
		
		//Generate Random Order Id
		RandomString msr = new RandomString();
        String uniqueOrderId = msr.generateRandomString(16);
        logger.info("OrderID..."+uniqueOrderId);
        
        //This object used for save data in DB
		PaymentInitiation paymentInitiation = new PaymentInitiation();
		
		//eventLog is used for save response logs in DB
		EventLogs eventLogResponse = new EventLogs();	
		JSONObject jsonObj=null;
		PaytmFinalQRRequest paytmFinalQRRequest= new PaytmFinalQRRequest();
		PaytmQRRequestType paytmQRRequestType = new PaytmQRRequestType();
		
		//PayTMDao payTMDao=new PayTMDaoImpl();
		String line = "";
		StringBuilder response = new StringBuilder();
		try {
			
			jsonObj = new JSONObject(requestQR);
//			if(String.valueOf(jsonObj.getString("price").toString())!=null) {
				String priceHex = jsonObj.getString("price");
				HexadecimaToInteger hexToInt = new HexadecimaToInteger();
				paytmQRRequestType.setAmount(hexToInt.getHexToInt(priceHex));
//			}
			if(jsonObj.getString("deviceId")!=null) {
				paytmQRRequestType.setDeviceId(jsonObj.getString("deviceId"));
			}
			if(jsonObj.getString("productId")!=null) {
				paytmQRRequestType.setProductId(jsonObj.getString("productId"));
			}
			paytmFinalQRRequest.setOperationType("QR_CODE");
			paytmFinalQRRequest.setIpAddress("139.59.73.155");
			paytmFinalQRRequest.setPlatformName("PayTM");
			

			paytmQRRequestType.setRequestType("QR_ORDER");
	        paytmQRRequestType.setMerchantGuid(merchantGuid);
	        paytmQRRequestType.setOrderId(uniqueOrderId);
	        paytmQRRequestType.setValidity("7");
	        paytmQRRequestType.setIndustryType("RETAIL");
	        paytmQRRequestType.setOrderDetails("Us Order");

			jsonObj.put("platformName", "PayTM");
			jsonObj.put("operationType", "QR_CODE");
			
//	        JSONObject requestFromJson = jsonObj.getJSONObject("request");
	        requestFromJson.put("requestType", "QR_ORDER");
	        requestFromJson.put("merchantGuid", merchantGuid);
	        requestFromJson.put("orderId", uniqueOrderId);
	        requestFromJson.put("validity", validity);
	        requestFromJson.put("industryType", "RETAIL");
	      //  String deviceId=paytmQRRequestType.getDeviceId();
	       // String terminalId=payTMDao.getTerminalIdByDeviceId(deviceId);
	        paymentInitiation.setOrderId(uniqueOrderId);
	        paymentInitiation.setAuthAmount(paytmQRRequestType.getAmount());
	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
	        paymentInitiation.setTerminalId(paytmQRRequestType.getDeviceId());
	        //paymentInitiation.setTerminalId(terminalId);
	        paymentInitiation.setCurrency("INR");
	        paymentInitiation.setProductName(paytmQRRequestType.getProductId());
	        paymentInitiation.setProductPrice(paytmQRRequestType.getAmount());
	        paymentInitiation.setDeviceId(paytmQRRequestType.getDeviceId());
	      //paymentInitiation.setDeviceId(deviceId);
	        paymentInitiation.setPspId(paytmFinalQRRequest.getPlatformName());
	        paymentInitiation.setMerchantId(merchantGuid);
	        paymentInitiation.setComments(paytmFinalQRRequest.getOperationType());
	        
	        eventLogResponse.setTerminalId(paytmQRRequestType.getDeviceId());
	        eventLogRequest.setTerminalId(paytmQRRequestType.getDeviceId());
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		JSONObject jsonRequest =  (JSONObject) jsonObj.get("request");
		
        jsonRequest.remove("orderId");
        jsonRequest.put("orderId", uniqueOrderId);
        
//        jsonObj.remove("request");
//        jsonObj.put("request", jsonRequest);
		paytmFinalQRRequest.setRequest(paytmQRRequestType);
		Gson gson = new Gson();
		
		String jsonRequest = gson.toJson(paytmFinalQRRequest);
		System.out.println("Request.."+jsonRequest);
		logger.info("COnverted Machine QR Request .."+jsonRequest);
		String urlParameters=jsonRequest;
		//System.out.println("urlParameters.."+urlParameters);
		
		paymentInitiation.setOrderId(uniqueOrderId);
		
		try{
			//Saving Payment Initiation
			qrDao.saveQRRequest(paymentInitiation);
		} catch(Exception e) {
			System.out.println("Exception in Saving QR payment initiation"+e);
		}
		
		EventLogDao eventDao = new eventLogDaoImpl();
		
		try {
//			String CHECKSUMHASH = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("TC#1Bkm&PWDv3Vz!", urlParameters);
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum("5SArH9vtuQIfbxb!", urlParameters);
			URL url = new URL("https://wallet.paytm.in/wallet-merchant/v2/createQRCode");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			
			connection.setRequestProperty("mid","Zoneon62132477850670");

	        connection.setRequestProperty("Content-Type","application/json");
	        connection.setRequestProperty("merchantGuid", merchantGuid);

	        connection.setRequestProperty("checksumhash",CHECKSUMHASH);

	   //System.out.println("My checksumhash is =" +CHECKSUMHASH.toString());
			
		   connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
	       connection.setUseCaches(false);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);
	       
	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
	    		}
	       
	       
	       int responseCode = connection.getResponseCode();

	        //System.out.println(responseCode+"\tSuccess");

	        InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = connection.getInputStream();

	        }else {

	        is = connection.getErrorStream();

	        }
	              try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
	              {

	           //System.out.println("line.."+line);
	              response.append(line);

	              response.append('\r');

	          }           } // or StringBuffer if not Java 5+
	              
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              //System.out.append(line);
	              //System.out.println("line.."+line);
	              response.append(line);
	              
	              System.out.println("RESPONSE..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
//		String responseString = response.toString();
		JSONObject j = new JSONObject(response.toString());
		j.remove("orderId");
		j.put("orderId", uniqueOrderId);
		
		eventLogResponse.setEventType("PayTM Dynamic QR Response");
		eventLogResponse.setVersion("v1");
		eventLogResponse.setEventDetails(j.toString());
		logger.info("MACHINE QR RESPONSE----"+j.toString());
		
		eventLogRequest.setEventType("PayTM Dynamic QR Request");
		eventLogRequest.setVersion("v1");
		
		eventDao.saveEventLog(eventLogRequest);
		eventDao.saveEventLog(eventLogResponse);
		
		
		return j.toString();
	}*/
}
