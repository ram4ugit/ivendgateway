package com.limitlessmobility.iVendGateway.services.wallet.paytm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmReleaseRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmReleaseAmountService {

	@RequestMapping(value="/paytmreleaseamount")
	public String releaseAmount(@RequestBody PaytmReleaseRequest releaseRequest, String orderId) throws Exception{
		System.out.println("paytmreleaseamount reqest "+releaseRequest.toString() +" and orderid "+orderId);
		JSONObject finalResponse = new JSONObject();
		try {
//			RandomString randomOrderId = new RandomString();
//	        String uniqueOrderId = randomOrderId.generateRandomString(16);
//	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", releaseRequest.getMid());
	        request.put("PREAUTH_ID", releaseRequest.getPreAuthId());
	        request.put("TOKEN", releaseRequest.getToken());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID",releaseRequest.getMid());
	        paramMap.put("PREAUTH_ID",releaseRequest.getPreAuthId());
	        paramMap.put("TOKEN",releaseRequest.getToken());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(releaseRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CHECKSUM", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
//			URL url = new URL(apiURL+"paymentservices/HANDLER_INTERNAL/release?JsonData="+request.toString());
			URL url = new URL("https://securegw.paytm.in/paymentservices/HANDLER_INTERNAL/release?JsonData="+request.toString());
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			int responseCode = connection.getResponseCode();
	        InputStream is;
	        if(responseCode == HttpURLConnection.HTTP_OK){
	        	is = connection.getInputStream();
	        }else {
	        	is = connection.getErrorStream();
	        }
              String line="";
              StringBuilder response = new StringBuilder();
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("Release RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	          rd.close(); is.close();
	          JSONObject responseJson = new JSONObject(response.toString());
	  	      if(responseJson.has("status")){
	  	    	  if(responseJson.getString("status").equalsIgnoreCase("FAILURE")){
	  	    		  try{
	  					finalResponse.put("responseObj", "");
	  					finalResponse.put("message", responseJson.getString("message"));
	  					finalResponse.put("status", responseJson.getString("status"));
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      if(responseJson.has("STATUS")){
	  	    	  if(responseJson.getString("STATUS").equalsIgnoreCase("TXN_FAILURE")){
	  	    		  try{
	  	    			  finalResponse.put("responseObj", "");
	  	    			  finalResponse.put("message", responseJson.getString("STATUSMESSAGE"));
	  	    			  finalResponse.put("status", "Failure");
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      finalResponse.put("message", "Amount relased successfully");
	  	      finalResponse.put("status", "Success");
	  	      JSONObject responsee = new JSONObject();
	  	      responsee.put("releaseRefTransactionId", responseJson.getString("PREAUTH_ID"));
	  	      
	  	      finalResponse.put("responseObj", responsee);
	  	      
	  	    /*******paymentTransaction For Save payment details*********/
	  	    PaymentTransaction paymentTransaction = new PaymentTransaction();
				paymentTransaction.setStatus("Success");
				paymentTransaction.setStatusCode("Success");
				paymentTransaction.setStatusMsg("Amount relased successfully");
				paymentTransaction.setOrderId(orderId);

				paymentTransaction.setPspTransactionId(releaseRequest.getPreAuthId());
				paymentTransaction.setTransactionType("Release");

				paymentTransaction.setServiceType("Release");
				paymentTransaction.setPspId("paytm");
				paymentTransaction.setDeviceId("505505");
				paymentTransaction.setMerchantId(releaseRequest.getMid());
				paymentTransaction.setTerminalId("");
				paymentTransaction.setAppId("");
				
				PaymentController transactionController = new PaymentController();
				transactionController.updateReleaseTransaction(paymentTransaction);
		} catch (IOException e) {
			System.out.println("Exception.."+e);
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		System.out.println("paytm release "+finalResponse.toString());
		return finalResponse.toString();
	}
}