package com.limitlessmobility.iVendGateway.services.wallet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
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
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.CommonQRRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.wallet.WalletQRRequestModel;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

/**
 * @author RamN
 * @version 2.0
 * This class is used for get dynamic QR of Wallet
 */
@Controller
@RequestMapping(value="v2/wallet")
public class WalletQRService {

//	private static final Logger logger = Logger.getLogger(walletQRService.class);

	CommonDao cdao = new CommonDaoImpl();
	QRDao qrDao = new QRDaoImpl();
	
	
	/*
	 * This Method is used for creating wallet QR
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String createQRCode(@RequestBody CommonQRRequest requestQR) throws Exception {
		System.out.println("Calling wallet Dynamic QR (createQRCode Method)...START");
		System.out.println("Dynamic QR request..Common...."+requestQR);
//		JSONObject finalResponse = new JSONObject();
		
		JSONObject respodata=new JSONObject();
		
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		//This object is used for save request event log in 'event_logs' table
		EventLogs eventLogRequest = new EventLogs();	
		eventLogRequest.setEventDetails(requestQR.toString());
		
		//Generate Random Order Id (Order id from our side)
		RandomString randomOrderId = new RandomString();
        String uniqueOrderId = randomOrderId.generateRandomString(16);
        
        //This object used for save data in DB
		PaymentInitiation paymentInitiation = new PaymentInitiation();
		
		//eventLogResponse is used for save response logs in 'event_logs' table
		EventLogs eventLogResponse = new EventLogs();	
		

		WalletQRRequestModel walletQRRequest = new WalletQRRequestModel();
		
		String line = "";
		StringBuilder response = new StringBuilder();
		String walletId = "";
		String walletKey="";
		String pspId="";
		String terminalId="";
		try {
//			PspMerchant pspMerchant = new PspMerchant();
			OperatorDetail operatorDetail = new OperatorDetail();
			if(requestQR.getPosId()!=null ) {
				terminalId= requestQR.getPosId();
				if(requestQR.getPspId()!=null) {
					pspId=requestQR.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, pspId);
					
					
//					pspMerchant = cdao.getPspMerchant(pspId, terminalId);
					walletId = operatorDetail.getMerchantId();
					walletKey = operatorDetail.getMerchantKey();
					if(walletId == null && walletId.trim().isEmpty()){
//						
						respodata.put("qrData", "");
						  respodata.put("orderId","");
//						  respodata.put("trId", jrespObject.getString("trId"));
						  respodata.put("encryptedData", "");
						return respodata.toString();
					} else if(walletKey == null && walletKey.trim().isEmpty()){
//						respodata.put("qrData", "");
						  respodata.put("orderId","");
//						  respodata.put("trId", jrespObject.getString("trId"));
						  respodata.put("encryptedData", "");
						return respodata.toString();
					}
				}
			}

	        walletQRRequest.setOrderNo(uniqueOrderId);
	        walletQRRequest.setAmount(String.valueOf(requestQR.getAmount()));
	        paymentInitiation.setOrderId(uniqueOrderId);
	        paymentInitiation.setAuthAmount(String.valueOf(requestQR.getAmount()));
	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
	        paymentInitiation.setTerminalId(terminalId);
	        paymentInitiation.setCurrency("INR");
	        paymentInitiation.setProductName(requestQR.getProductId());
	        paymentInitiation.setProductPrice(String.valueOf(requestQR.getAmount()));
	        paymentInitiation.setDeviceId(requestQR.getAppId());
	        paymentInitiation.setPspId(pspId);
	        paymentInitiation.setMerchantId(operatorDetail.getMerchantId());
	        paymentInitiation.setAppId(requestQR.getAppId());
	        
	        eventLogResponse.setTerminalId(terminalId);
	        eventLogRequest.setTerminalId(terminalId);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		
//		String jsonRequest = gson.toJson(walletQRRequest);
//		System.out.println("Converted QR Request .."+jsonRequest);
		String urlParameters=walletQRRequest.toString();
		System.out.println("urlParameters.."+urlParameters);
		
		paymentInitiation.setOrderId(uniqueOrderId);
		
		try{
			//Saving Payment Initiation
			qrDao.saveQRRequest(paymentInitiation);
		} catch(Exception e) {
			System.out.println("Exception in Saving QR payment initiation"+e);
		}
		
		EventLogDao eventDao = new eventLogDaoImpl();
		
		try {
			String apiURL=Util.apiUrlReader();
			URL url = new URL(apiURL+"api/Wallet/Transaction/generateqr");
			System.out.println(url);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("walletId",walletId);

			connection.setRequestProperty("walletKey",walletKey);
			
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
	              
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From wallet..."+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(response.toString());
		System.out.println("Response.. "+response.toString());
		JSONObject responseJson = new JSONObject();
		
		if(j.getString("status").equalsIgnoreCase("SUCCESS")){
		JSONObject modelResponseJson = j.getJSONObject("model");
		
		/*responseJson.put("orderId", modelResponseJson.getString("referenceNo"));
		responseJson.put("qrData", modelResponseJson.getString("qrCode"));
		responseJson.put("encryptedData", modelResponseJson.getString("qrCode"));
		
//		finalResponse.put("responseObj", responseJson);
		finalResponse.put("message", "null");
		finalResponse.put("status", HttpStatusModal.OK);*/
		
		
		
		  
		  //return response.toString();
		
		  respodata.put("qrData", modelResponseJson.getString("qrCode"));
		  respodata.put("orderId",modelResponseJson.getString("referenceNo"));
//		  respodata.put("trId", jrespObject.getString("trId"));
		  respodata.put("encryptedData", modelResponseJson.getString("qrCode"));
		
		
		
	} else {
		/*finalResponse.put("responseObj", responseJson);
		finalResponse.put("message", j.getString("message"));
		finalResponse.put("status", j.getString("status"));*/
		
		respodata.put("qrData", "");
		  respodata.put("orderId","");
//		  respodata.put("trId", jrespObject.getString("trId"));
		  respodata.put("encryptedData", "");
	}
		
		System.out.println();
		eventLogResponse.setEventType("wallet Dynamic QR Reponse");
		eventLogResponse.setVersion(ResponseCodes.VERSION);
		eventLogResponse.setEventDetails(respodata.toString());
		
		eventLogRequest.setOrderID(paymentInitiation.getOrderId());
		eventLogResponse.setOrderID(paymentInitiation.getOrderId());
		
		eventLogRequest.setEventType("wallet Dynamic QR Request");
		eventLogRequest.setVersion("v1");
		
		eventDao.saveEventLog(eventLogRequest);
		eventDao.saveEventLog(eventLogResponse);
		
		System.out.println("Calling wallet Dynamic QR (createQRCode Method)...END");
		return respodata.toString();
	}
	
	/*@RequestMapping(value = "/machine/walletQRCode", method = RequestMethod.POST)
	@ResponseBody
	public String createMacineQRCode(@RequestBody String requestQR) throws Exception {
		logger.info("Calling wallet Dynamic Machine QR...START");
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
		walletFinalQRRequest walletFinalQRRequest= new walletFinalQRRequest();
		walletQRRequestType walletQRRequestType = new walletQRRequestType();
		
		//walletDao walletDao=new walletDaoImpl();
		String line = "";
		StringBuilder response = new StringBuilder();
		try {
			
			jsonObj = new JSONObject(requestQR);
//			if(String.valueOf(jsonObj.getString("price").toString())!=null) {
				String priceHex = jsonObj.getString("price");
				HexadecimaToInteger hexToInt = new HexadecimaToInteger();
				walletQRRequestType.setAmount(hexToInt.getHexToInt(priceHex));
//			}
			if(jsonObj.getString("deviceId")!=null) {
				walletQRRequestType.setDeviceId(jsonObj.getString("deviceId"));
			}
			if(jsonObj.getString("productId")!=null) {
				walletQRRequestType.setProductId(jsonObj.getString("productId"));
			}
			walletFinalQRRequest.setOperationType("QR_CODE");
			walletFinalQRRequest.setIpAddress("139.59.73.155");
			walletFinalQRRequest.setPlatformName("wallet");
			

			walletQRRequestType.setRequestType("QR_ORDER");
	        walletQRRequestType.setMerchantGuid(merchantGuid);
	        walletQRRequestType.setOrderId(uniqueOrderId);
	        walletQRRequestType.setValidity("7");
	        walletQRRequestType.setIndustryType("RETAIL");
	        walletQRRequestType.setOrderDetails("Us Order");

			jsonObj.put("platformName", "wallet");
			jsonObj.put("operationType", "QR_CODE");
			
//	        JSONObject requestFromJson = jsonObj.getJSONObject("request");
	        requestFromJson.put("requestType", "QR_ORDER");
	        requestFromJson.put("merchantGuid", merchantGuid);
	        requestFromJson.put("orderId", uniqueOrderId);
	        requestFromJson.put("validity", validity);
	        requestFromJson.put("industryType", "RETAIL");
	      //  String deviceId=walletQRRequestType.getDeviceId();
	       // String terminalId=walletDao.getTerminalIdByDeviceId(deviceId);
	        paymentInitiation.setOrderId(uniqueOrderId);
	        paymentInitiation.setAuthAmount(walletQRRequestType.getAmount());
	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
	        paymentInitiation.setTerminalId(walletQRRequestType.getDeviceId());
	        //paymentInitiation.setTerminalId(terminalId);
	        paymentInitiation.setCurrency("INR");
	        paymentInitiation.setProductName(walletQRRequestType.getProductId());
	        paymentInitiation.setProductPrice(walletQRRequestType.getAmount());
	        paymentInitiation.setDeviceId(walletQRRequestType.getDeviceId());
	      //paymentInitiation.setDeviceId(deviceId);
	        paymentInitiation.setPspId(walletFinalQRRequest.getPlatformName());
	        paymentInitiation.setMerchantId(merchantGuid);
	        paymentInitiation.setComments(walletFinalQRRequest.getOperationType());
	        
	        eventLogResponse.setTerminalId(walletQRRequestType.getDeviceId());
	        eventLogRequest.setTerminalId(walletQRRequestType.getDeviceId());
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		JSONObject jsonRequest =  (JSONObject) jsonObj.get("request");
		
        jsonRequest.remove("orderId");
        jsonRequest.put("orderId", uniqueOrderId);
        
//        jsonObj.remove("request");
//        jsonObj.put("request", jsonRequest);
		walletFinalQRRequest.setRequest(walletQRRequestType);
		Gson gson = new Gson();
		
		String jsonRequest = gson.toJson(walletFinalQRRequest);
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
			URL url = new URL("https://wallet.wallet.in/wallet-merchant/v2/createQRCode");
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
		
		eventLogResponse.setEventType("wallet Dynamic QR Response");
		eventLogResponse.setVersion("v1");
		eventLogResponse.setEventDetails(j.toString());
		logger.info("MACHINE QR RESPONSE----"+j.toString());
		
		eventLogRequest.setEventType("wallet Dynamic QR Request");
		eventLogRequest.setVersion("v1");
		
		eventDao.saveEventLog(eventLogRequest);
		eventDao.saveEventLog(eventLogResponse);
		
		
		return j.toString();
	}*/
}
