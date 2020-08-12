package com.limitlessmobility.iVendGateway.services.amazonpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.controller.amazonpay.SignatureUtil;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.RefundInitialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.RequestToSign;
import com.limitlessmobility.iVendGateway.model.amazonpay.TerminalDetail;

@Controller
@RequestMapping(value="/v1/amazonpay")
public class AmazonPayRefundServiceV1 {
	
	
	private static final Logger logger = Logger.getLogger(AmazonPayRefundServiceV1.class);
	
    private static final String transactionIdType ="AMAZON_ORDER_ID";
	
	private static final String signatureMethod ="HmacSHA384";
	
	private static final String signatureVersion ="4";
	
	private static final String CanonicalURI="/payment/refund/v1";
	
	
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String getPaymentRefund(@RequestBody RefundInitialData initialData) throws JSONException {

	
	logger.info("Amount " + initialData.getAmount() + "\n TxnId "
	        + initialData.getTxnId());
	
	String merchantId="";
	String merchantOrderId="";
	String accessKey = "";
	String refundReferenceId ="";
	long currentTimeStamp=0;
	String payloadData="";
	String iv="";
	String key="";
	String urlparameter="";
	String line="";
	StringBuilder response= new StringBuilder();
	
	
	   // Random number generation for transactionId
	   RandomString RandomString = new RandomString();
	   refundReferenceId = RandomString.generateRandomString(16);
	   System.out.println("Random Number  " + refundReferenceId);
	
	AmazonPayDao amazonPayDao=new AmazonDaoImpl();
	CredentialData credentialData=new CredentialData();
	TerminalDetail terminalDetail = new TerminalDetail();
	try {
		terminalDetail = amazonPayDao.getTerminalDetail(initialData.getTxnId());

		credentialData = amazonPayDao.getAllCredentialDetail(terminalDetail.getPspId(), terminalDetail.getTerminalId());
		/* Amazon seller credential detail */
		 merchantId = credentialData.getMerchantId().trim();
		 accessKey = credentialData.getSecretKey().trim();

	} catch (Exception e) {
		// TODO: handle exception
	}	
	try {
		merchantOrderId=amazonPayDao.getMerchantOrderId(initialData.getTxnId());
		System.out.println("merchantOrderId "+merchantOrderId);
	} catch (Exception e) {
		// TODO: handle exception
	}
	 
	 
	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("amazonTransactionId", merchantOrderId);
        requestBody.put("amazonTransactionIdType", transactionIdType);
        requestBody.put("refundReferenceId", refundReferenceId);
        requestBody.put("amount", initialData.getAmount());
        requestBody.put("currencyCode", "INR");
        requestBody.put("signatureMethod", signatureMethod);
        requestBody.put("signatureVersion", signatureVersion);
       // requestBody.put("accessKeyId", "18f35507-94e8-45d2-98a6-81bf7fed090c");
        requestBody.put("accessKeyId", accessKey);
       // requestBody.put("merchantId", "A1IPHQV2ALSME6");
        requestBody.put("merchantId", merchantId);
        currentTimeStamp = Instant.now().toEpochMilli();
        System.out.println("tsss-1 "+currentTimeStamp);
        requestBody.put("timeStamp", String.valueOf(currentTimeStamp));
        
	
	
        
        try {
			requestBody.put("signature", SignatureUtil.generateSignatures(getRequestToSign(requestBody), CommonService.getSecretKey(merchantId)));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   
        System.out.println("PayLoad : "+requestBody);

    try {
        Map<String, String> map = CryptoForMerchant.encrypt(requestBody);
        System.out.println(map);
        payloadData=map.get("payload");
        
        System.out.println("payload "+payloadData);
        
         iv=map.get("iv");
        
        System.out.println("iv "+iv);
        
         key=map.get("key");
        
        System.out.println("key "+key);

    } catch (Exception e) {
          //  tring errorResponse = ((HttpClientErrorException) e).getResponseBodyAsString();
          // logger.error("Error Response is ----> {}", errorResponse);
        e.printStackTrace();
    }

	 
    JSONObject requestData=new JSONObject();
    try {
		requestData.put("payload", payloadData);
		requestData.put("iv", iv);
		requestData.put("key", key);
	} catch (Exception e) {
		// TODO: handle exception
	}
	urlparameter= requestData.toString().trim();
	
	System.out.println("request Info "+urlparameter.toString().trim());
	
	try {
		String path="https://amazonpay.amazon.in/payment/refund/v1";
		URL url = new URL(path);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("merchantId", merchantId);
		/*connection.setRequestProperty("isSandbox", "true");*/
		//System.out.println("merchantId "+connection.getRequestProperty("merchantId")+"\n isSandbox"+connection.getRequestProperty("isSandbox"));
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
        
		try (DataOutputStream wr = new DataOutputStream(

				connection.getOutputStream())) {
				wr.writeBytes(urlparameter);
			}
		int responseCode = connection.getResponseCode();
		System.out.println("responseCode "+responseCode);

		InputStream is;

		if (responseCode == HttpURLConnection.HTTP_OK) {

			is = connection.getInputStream();

		} else {

			is = connection.getErrorStream();

		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		line = "";
		while ((line = rd.readLine()) != null) {
			response.append(line);

			System.out.println("Amazonpay Response..." + response.toString());
			System.out.append("\r");

		}
		rd.close();
	} catch (IOException e) {
		System.out.println("Exception in DynamicQRService class.." + e);
		e.printStackTrace();
	}
	
        JSONObject apResponse=new JSONObject(response.toString());
        
        return apResponse.toString();
	    
	    /*JSONObject innerResponse=apResponse.getJSONObject("response");
	
	    
	    
	    
	
	    JSONObject finalresponse =new JSONObject();
	    try {
	     if (!innerResponse.isNull("status")) {
	    	 if(innerResponse.getString("status").equalsIgnoreCase("PENDING")){
	            finalresponse.put("statusMessage", "statusMessage");
	            finalresponse.put("status", "SUCCESS");
	            finalresponse.put("statusCode", "SS_001");
	            finalresponse.put("amount", initialData.getAmount());
	    	 }
	    	 return finalresponse.toString();
	     }else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02")){
	    	    finalresponse.put("statusMessage", "Bad Request.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }else if(innerResponse.getString("reasonCode").equalsIgnoreCase("09-01")){
	    	    finalresponse.put("statusMessage", "Amazon has rejected the refund.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }else if(innerResponse.getString("reasonCode").equalsIgnoreCase("99")){
	    	    finalresponse.put("statusMessage", "Unexpected/Undefined state");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("04")){
	    	    finalresponse.put("statusMessage", "Request authentication failed because of invalid signature.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02-08")){
	    	    finalresponse.put("statusMessage", "Bad Encryption.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02-04")){
	    	    finalresponse.put("statusMessage", "Invalid amazon order id.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02-02")){
	    	    finalresponse.put("statusMessage", "Bad Request: Request contains different merchant ids in header and body.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("09-02")){
	    	    finalresponse.put("statusMessage", "Amazon could not process the refund.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02-05")){
	    	    finalresponse.put("statusMessage", "Invalid amazon refund id.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02-07")){
	    	    finalresponse.put("statusMessage", "Requested amount is more than eligible amount.");
				finalresponse.put("status", "FAILURE");
				finalresponse.put("statusCode", "");
				finalresponse.put("amount", 0);
				return finalresponse.toString();
	     }
	     else{
		finalresponse.put("statusMessage", "");
		finalresponse.put("status", "FAILURE");
		finalresponse.put("statusCode", "");
		finalresponse.put("amount", 0);
		return finalresponse.toString();
	     }
	    } catch (Exception e) {
	    	finalresponse.put("statusMessage", e);
			finalresponse.put("status", "FAILURE");
			finalresponse.put("statusCode", "");
			finalresponse.put("amount", 0);
			return finalresponse.toString();
		}*/
	

	}
	 private static RequestToSign getRequestToSign(Map<String, Object> fields) {
		    RequestToSign requestToSign = new RequestToSign();
	        requestToSign.setMethod("POST");
	        requestToSign.setHost("amazonpay.amazon.in");
	        requestToSign.setuRI(CanonicalURI);
	        requestToSign.setParameters(fields);
	        return requestToSign;
	    }

}
