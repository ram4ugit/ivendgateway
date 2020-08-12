package com.limitlessmobility.iVendGateway.services.amazonpay;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
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
public class AmazonPayRefundStatus {
	
	private static final Logger logger = Logger.getLogger(AmazonPayRefundStatus.class);
	
   /* private static final String transactionIdType ="AMAZON_ORDER_ID";
	
	private static final String signatureMethod ="HmacSHA384";
	
	private static final String signatureVersion ="4";*/
	
	private static final String CanonicalURI="/payment/refund/status/v1";
	
	

	/* This will always be OrderReferenceId */
	public static String amazonTransactionIdType = "OrderReferenceId";;

	/* Currency code for the transaction. This will always be INR */
	public static String refundCurrencyCode = "INR";

	
	@ResponseBody
	@RequestMapping(value="/refundStatus" ,method=RequestMethod.POST)
	public String getRefundStatus(@RequestBody RefundInitialData refundInitialData){

		String merchantId = "";
		String accessKey = "";
		String refundReferenceId = "";
		long currentTimeStamp = 0;
		String payloadData = "";
		String iv = "";
		String key = "";
		String urlparameter = "";
		String line = "";
		StringBuilder response = new StringBuilder();
		String accessKeyId ="";
		String refundId="";
		
		/*
		 * For testing in Sandbox mode add IS_SANDBOX field, Remove IS_SANDBOX when
		 * going live
		 */
		boolean isSandbox = true;
		JSONObject finalresponse;
		
		AmazonPayDao amazonPayDao = new AmazonDaoImpl();
		TerminalDetail terminalDetail = new TerminalDetail();
		CredentialData credentialData = new CredentialData();

		logger.info("Amount " + refundInitialData.getAmount() + "\n TxnId "
		        + refundInitialData.getTxnId());

		// Random number generation for transactionId
		RandomString RandomString = new RandomString();
		refundReferenceId = RandomString.generateRandomString(16);
		System.out.println("Random Number  " + refundReferenceId);
		

		System.out.println("amazonTransactionIdType " + amazonTransactionIdType);
		//refundId=amazonPayDao.getRefundId(refundInitialData.getTxnId());
		refundId="P04-0121366-1747054-R063985";
		System.out.println("Amazon refundId  "+refundId);
		
		try {
			terminalDetail = amazonPayDao.getTerminalDetail(refundInitialData.getTxnId());

			credentialData = amazonPayDao.getAllCredentialDetail(terminalDetail.getPspId(), terminalDetail.getTerminalId());
			/* Amazon seller credential detail */
			merchantId = credentialData.getMerchantId().trim();  //merchantId
			accessKey = credentialData.getMerchantKey().trim();
			 accessKeyId = credentialData.getSecretKey().trim();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		/* if (checkStatusInitialData.getPosId()!=null) {
			   terminalId= checkStatusInitialData.getPosId();
		    }*/
		
		    Map<String, Object> requestBody = new HashMap<>();
			
		    requestBody.put("amazonRefundId", refundId);
	        requestBody.put("signatureMethod", "HmacSHA384");
	        requestBody.put("signatureVersion", "4");
	        requestBody.put("accessKeyId", accessKeyId);
	        requestBody.put("merchantId", merchantId);
	        currentTimeStamp = Instant.now().toEpochMilli();
	        System.out.println("tsss-1 "+currentTimeStamp);
	        requestBody.put("timeStamp", String.valueOf(currentTimeStamp));
	        
	        try {
	    				requestBody.put("signature", SignatureUtil.generateSignatures(getRequestToSign(requestBody),CommonService.getSecretKey(merchantId)));
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
	    		urlparameter= requestData.toString();
	    		
	    		System.out.println("request Info "+urlparameter.toString().trim());
	    		
	    		urlparameter="?payload="+payloadData+"&iv="+iv+"&key="+key;
	    		
	    		//urlparameter="?payload=gZ6SKZ5ppaHOtiEi3F6P4NibDf7NTBoeAv-FA7r5zJEg3A90zvE3mhjDoQbE8BlxdBFOQ2jkHDfxw0xx5Dn6mG6SgFpvzix40qNtw2td6asY3GzvGSne7s-X81hi-hsRrFuY-Aa3ElYUvhRJtd4r9zPqRrAwb-epB4KgWiZj5UqH3dEbgKa-Lw31q3pxC0aj0KbNIU8-s5OuusmIFTdmjJJGUTNKVpM3wgboh4UuRpAkk9y77TcfuLXw1OARaDwgE6RLeLcAjWv_ivjNO75d8FJSawI_7EVUqsMorpyzRnZGgsRQ_CwWFi1BkVUhppnnWsfiTs0d_2zuxXCG1mfTZK3j_9rqSMgaFiVAPXBwb-HTfNu7qsLCj4XoI-o4AKCvr8WG2DyZYoyLUvGQaNqT8jiVhPoXwbuT3rILwQ5EwkxHl3-jM8I7jNUjIg==&iv=ACHUAOr5TfJgURXPeMxdQw==&key=WQu8CgLuP9loQGsHsUpjXXxltCongimMiT_58iPNhbOGeJskIS5P1ksU7cdSPMPlBmF-o1CiBdJisgiNQqQ1UCqRJgo0SV8Umij5FYm4IxCXoCyHNB9bxD9ot0TT4F-stveycvLcu1YGiI1tefBj4kePK0nPFnneOWNF_Nv-DAPvI0qnWEWBS5_la5c4GJBwUAP-oQBElIiglqkAX-eE0fNLsLDtokr_73R5IQl_JO2wD3OIBYz8rs45fcaIdrzdQy3sZQWm7r2KY8_j0UZgZlKqj0pF5kgU4M4L1KaooDkSTd3mfi3gQVotr9Vc5YzUKKZ4XI-3QDhK4B0S2E21Ig==";
	    		
	    		try {
	    			String path="https://amazonpay.amazon.in/payment/refund/status/v1";
	    			URL url = new URL(path+urlparameter);
	    			HttpURLConnection connection = null;
	    			connection = (HttpURLConnection) url.openConnection();
	    			connection.setRequestMethod("GET");
	    			connection.setRequestProperty("merchantId", merchantId);
	    			//connection.setRequestProperty("isSandbox", "true");
	    			System.out.println("\n merchantId "+connection.getRequestProperty("merchantId"));
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
		
		return response.toString();
	    		//return urlparameter.toString();
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
