package com.limitlessmobility.iVendGateway.services.amazonpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
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

import com.limitlessmobility.iVendGateway.controller.amazonpay.SignatureUtil;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.amazonpay.CheckStatusInitialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.RequestToSign;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping(value="/v1/amazonpay")
public class AmazonPayCheckStatusV1 {
	
	private static final Logger logger = Logger.getLogger(AmazonPayCheckStatusV1.class);
	
	private static final String CanonicalURI="/payment/charge/status/v1";
	
	/*
	 * This API is used to get check status of Amazonppay transaction. Version 1
	 */
	@RequestMapping(value="/checkStatus",method=RequestMethod.POST)
	@ResponseBody
	public  String getAmazonTransactionstatus(@RequestBody CheckStatusInitialData checkStatusInitialData) throws JSONException{

		
		logger.info("posId.." + checkStatusInitialData.getPosId()
        + "...transactionId..." + checkStatusInitialData.getTxnId()
        + "...appId..." + checkStatusInitialData.getAppId()
        + "...pspId..." + checkStatusInitialData.getPspId()
        + "...amount..." + checkStatusInitialData.getAmount());
		
		String merchantId="";
		String merchantKey="";
		String accessKeyId="";
		/*long currentTimeStamp=1559899014404L;*/
		long currentTimeStamp=0;
		String payloadData="";
		String iv="";
		String key="";
		String urlparameter="";
		String line="";
		StringBuilder response= new StringBuilder();
		String device_id1="";
		String terminalId="";
		boolean isexist=true;
		
		AmazonPayDao amazonPayDao=new AmazonDaoImpl();
		CredentialData credentialData=new CredentialData();
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		 try {
		 		credentialData = amazonPayDao.getAllCredentialDetail(checkStatusInitialData.getPspId()	, checkStatusInitialData.getPosId());
		 		 merchantId = credentialData.getMerchantId().trim();
		 		System.out.println("merchantId " + merchantId);
		 		 merchantKey = credentialData.getMerchantKey().trim();
		 		System.out.println("merchantKey " + merchantKey);
		 		accessKeyId = credentialData.getSecretKey().trim();
		 		System.out.println("accessKeyId " + accessKeyId);
			} catch (Exception e) {
				System.out.println("Problem while fetching credential from Database... "+e	);
			}	
		   if (checkStatusInitialData.getPosId()!=null) {
			   terminalId= checkStatusInitialData.getPosId();
		    }
		 
		    Map<String, Object> requestBody = new HashMap<>();
			
	        //requestBody.put("merchantId", "A1IPHQV2ALSME6");
		    requestBody.put("merchantId", merchantId);
	        requestBody.put("transactionIdType","MERCHANT_TXN_ID");
	        requestBody.put("transactionId", checkStatusInitialData.getTxnId());
	        requestBody.put("signatureMethod", "HmacSHA384");
	        requestBody.put("signatureVersion", "4");
	       /* requestBody.put("accessKeyId", "18f35507-94e8-45d2-98a6-81bf7fed090c");*/
	        requestBody.put("accessKeyId", accessKeyId);
	        currentTimeStamp = Instant.now().toEpochMilli();
	        System.out.println("tsss-1 "+currentTimeStamp);
	        requestBody.put("timeStamp", String.valueOf(currentTimeStamp));
	        
		
		
	        
	        try {
				requestBody.put("signature", SignatureUtil.generateSignatures(getRequestToSign(requestBody), CommonService.getSecretKey(merchantId)));
			} catch (Exception e1) {
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
		}
		urlparameter= requestData.toString();
		
		System.out.println("request Info "+urlparameter.toString().trim());
		
		urlparameter="?payload="+payloadData+"&iv="+iv+"&key="+key;
		
		//urlparameter="?payload=gZ6SKZ5ppaHOtiEi3F6P4NibDf7NTBoeAv-FA7r5zJEg3A90zvE3mhjDoQbE8BlxdBFOQ2jkHDfxw0xx5Dn6mG6SgFpvzix40qNtw2td6asY3GzvGSne7s-X81hi-hsRrFuY-Aa3ElYUvhRJtd4r9zPqRrAwb-epB4KgWiZj5UqH3dEbgKa-Lw31q3pxC0aj0KbNIU8-s5OuusmIFTdmjJJGUTNKVpM3wgboh4UuRpAkk9y77TcfuLXw1OARaDwgE6RLeLcAjWv_ivjNO75d8FJSawI_7EVUqsMorpyzRnZGgsRQ_CwWFi1BkVUhppnnWsfiTs0d_2zuxXCG1mfTZK3j_9rqSMgaFiVAPXBwb-HTfNu7qsLCj4XoI-o4AKCvr8WG2DyZYoyLUvGQaNqT8jiVhPoXwbuT3rILwQ5EwkxHl3-jM8I7jNUjIg==&iv=ACHUAOr5TfJgURXPeMxdQw==&key=WQu8CgLuP9loQGsHsUpjXXxltCongimMiT_58iPNhbOGeJskIS5P1ksU7cdSPMPlBmF-o1CiBdJisgiNQqQ1UCqRJgo0SV8Umij5FYm4IxCXoCyHNB9bxD9ot0TT4F-stveycvLcu1YGiI1tefBj4kePK0nPFnneOWNF_Nv-DAPvI0qnWEWBS5_la5c4GJBwUAP-oQBElIiglqkAX-eE0fNLsLDtokr_73R5IQl_JO2wD3OIBYz8rs45fcaIdrzdQy3sZQWm7r2KY8_j0UZgZlKqj0pF5kgU4M4L1KaooDkSTd3mfi3gQVotr9Vc5YzUKKZ4XI-3QDhK4B0S2E21Ig==";
		
		try {
			String path="https://amazonpay.amazon.in/payment/charge/status/v1";
			URL url = new URL(path+urlparameter);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("merchantId", merchantId);
			String timeEpoch = String.valueOf(currentTimeStamp);
			System.out.println("tsss-2 "+timeEpoch);
			connection.setRequestProperty("timeStamp", timeEpoch);
			System.out.println("ts "+connection.getRequestProperty("timeStamp")+"\n merchantId "+connection.getRequestProperty("merchantId")+"\n attributableProgram"+connection.getRequestProperty("attributableProgram")+"\n isSandbox"+connection.getRequestProperty("isSandbox"));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

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
		    
		    JSONObject innerResponse=apResponse.getJSONObject("response");
		    
		    Long timestam=Long.valueOf(innerResponse.getString("timeStamp"));
		    Timestamp timestamp = new Timestamp(timestam);
			Date date1=new Date(timestamp.getTime());
		    System.out.println("date1 "+date1);
		    
		    
		    
		    try {
				

		        device_id1=amazonPayDao.getDeviceIDByTerminalId(checkStatusInitialData.getPosId());
				
				System.out.println("device_id1 "+device_id1);
				
				
				isexist=amazonPayDao.checkTxnId(checkStatusInitialData.getTxnId());
		        
		   	    System.out.println("isexist "+isexist);
		        if (isexist) {
		        	amazonPayDao.updateTerminalDevice(checkStatusInitialData.getTxnId(), device_id1, checkStatusInitialData.getPosId(), checkStatusInitialData.getAppId());
					
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		    
		    
		
		    JSONObject finalresponse =new JSONObject();
		    try {
		        if (innerResponse.getString("status").equalsIgnoreCase("SUCCESS")) {
		    	
		    	 paymentTransaction.setMerchantId("vendiman");
				 System.out.println("MerchantId "+paymentTransaction.getMerchantId());
				 paymentTransaction.setPspMerchantId(merchantId);
				 System.out.println("PspMerchantId "+paymentTransaction.getPspMerchantId());
				 paymentTransaction.setOrderId(checkStatusInitialData.getTxnId());
				 paymentTransaction.setMerchantOrderId(innerResponse.getString("amazonTransactionId"));
				 System.out.println("MerchantOrderId "+paymentTransaction.getMerchantOrderId());
				 paymentTransaction.setAuthAmount(Double.valueOf(innerResponse.getString("amount")));
				 paymentTransaction.setTransactionType("0");
				 paymentTransaction.setStatus(innerResponse.getString("status"));
				 paymentTransaction.setServiceType("CHECK_TXN_STATUS");
				 System.out.println("ServiceType "+paymentTransaction.getServiceType());
				 paymentTransaction.setPspId("amazonpay");
				 System.out.println("PspId "+paymentTransaction.getPspId());
				 paymentTransaction.setDeviceId(device_id1);
				 System.out.println("DeviceId "+paymentTransaction.getDeviceId());
				 paymentTransaction.setTerminalId(terminalId);
				 System.out.println("TerminalId "+paymentTransaction.getTerminalId());
				 paymentTransaction.setMerchantId("zoneone");
				 paymentTransaction.setAuthDate(date1.toString());
				 System.out.println("AuthDate "+paymentTransaction.getAuthDate());
				 paymentTransaction.setPspTransactionId(checkStatusInitialData.getTxnId());
				 System.out.println("PspTransactionId "+paymentTransaction.getPspTransactionId());
				 
				 
				 TransactionDao transactionDao = new TransactionDaoImpl();
				 transactionDao.saveTransaction(paymentTransaction);
		    finalresponse.put("statusMessage", "statusMessage");
		    finalresponse.put("status", "SUCCESS");
		    finalresponse.put("statusCode", "SS_001");
		    finalresponse.put("amount", innerResponse.getString("amount"));
		    return finalresponse.toString();

		     }else if(innerResponse.getString("reasonCode").equalsIgnoreCase("11-01")){
		    	    finalresponse.put("statusMessage", "No charge request found for specified order.");
					finalresponse.put("status", "FAILURE");
					finalresponse.put("statusCode", "");
					finalresponse.put("amount", 0);
					return finalresponse.toString();
		     }else if(innerResponse.getString("reasonCode").equalsIgnoreCase("14-03")){
		    	    finalresponse.put("statusMessage", "Order could not be procccessed due to time-out. Please resend data to customer");
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
		     else if(innerResponse.getString("reasonCode").equalsIgnoreCase("02")){
		    	    finalresponse.put("statusMessage", "Bad Request.");
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
			}
		
	}
	
	 private static RequestToSign getRequestToSign(Map<String, Object> fields) {
		    RequestToSign requestToSign = new RequestToSign();
	        requestToSign.setMethod("GET");
	        requestToSign.setHost("amazonpay.amazon.in");
	        requestToSign.setuRI(CanonicalURI);
	       // requestToSign.setTimeStamp(System.currentTimeMillis());
	        requestToSign.setParameters(fields);
	        return requestToSign;
	    }

}

