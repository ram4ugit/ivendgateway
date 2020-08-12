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
import com.limitlessmobility.iVendGateway.model.wallet.paytm.BlockAmountRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmBlockAmountService {

	@RequestMapping(value="/paytmblockamount")
	public String blockAmount(@RequestBody BlockAmountRequest blockRequest) throws Exception{
		System.out.println("paytm paytmblockamount req "+blockRequest.toString());
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", blockRequest.getMid());
	        request.put("ORDER_ID", blockRequest.getOrderId());
	        request.put("TXN_AMOUNT", blockRequest.getTxnAmount());
	        request.put("TOKEN", blockRequest.getToken());
//	        request.put("DURATIONHRS", blockRequest.getDuration());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID",blockRequest.getMid());
	        paramMap.put("ORDER_ID",blockRequest.getOrderId());
	        paramMap.put("TOKEN",blockRequest.getToken());
	        paramMap.put("TXN_AMOUNT",blockRequest.getTxnAmount());
//	        paramMap.put("DURATIONHRS",blockRequest.getDuration());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(blockRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CHECKSUM", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
//			URL url = new URL(apiURL+"paymentservices/HANDLER_INTERNAL/preAuth?JsonData="+request.toString());
			URL url = new URL("https://securegw.paytm.in/paymentservices/HANDLER_INTERNAL/preAuth?JsonData="+request.toString());
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
	              System.out.println("paytm block RESPONSE From PayTm..."+response.toString());
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
	  	      } else if(responseJson.has("ErrorMessage")){
	  	    	finalResponse.put("responseObj", "");
    			  finalResponse.put("message", responseJson.getString("ErrorMessage"));
    			  finalResponse.put("status", "Failure");
    			  return finalResponse.toString();
	  	      }
	  	    PaymentTransaction paymentTransaction = new PaymentTransaction();
	  	    
	  	    JSONObject responsee = new JSONObject();
	  	      finalResponse.put("message", responseJson.getString("STATUSMESSAGE"));
	  	      finalResponse.put("status", "Success");
	  	      	responsee.put("amount", responseJson.getString("BLOCKEDAMOUNT"));
	  	      	responsee.put("orderId", responseJson.getString("ORDER_ID"));
	  	      	responsee.put("userWalletBalance", responseJson.getString("USER_WALLET_BALANCE"));
	  	      	responsee.put("transId", responseJson.getString("PREAUTH_ID"));
	  	      finalResponse.put("responseObj", responsee);
	  	      
	  	      /******Save Data in Tranaction Table*****/
			paymentTransaction.setAuthAmount(Double.parseDouble(responseJson.getString("BLOCKEDAMOUNT")));
			paymentTransaction.setSettlementAmount("0");
			paymentTransaction.setOrderId(blockRequest.getOrderId());
			paymentTransaction.setMerchantOrderId(blockRequest.getOrderId());
			paymentTransaction.setPspTransactionId(responseJson.getString("PREAUTH_ID"));
			paymentTransaction.setTransactionType("block");

			paymentTransaction.setServiceType("0");
			paymentTransaction.setPspId("paytm");
			paymentTransaction.setDeviceId("505505");
			paymentTransaction.setMerchantId(blockRequest.getMid());
			paymentTransaction.setTerminalId("");
			paymentTransaction.setAppId("");
			
			PaymentController transactionController = new PaymentController();
			transactionController.saveTransaction(paymentTransaction);
	  	      
		} catch (IOException e) {
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		System.out.println("paytm block amount response "+finalResponse.toString());
		return finalResponse.toString();
	}
}