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

import BlockAmountRequest.PaytmWithdrawRequest;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmReleaseRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmWithdrawService {

	@RequestMapping(value="/paytmwithdrawamount")
	public String withdrawAmount(@RequestBody PaytmWithdrawRequest withdrawRequest) throws Exception{
		System.out.println("paytmwithdrawamount request "+ withdrawRequest.toString());
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", withdrawRequest.getMid());
	        request.put("PREAUTH_ID", withdrawRequest.getPreAuthId());
	        request.put("ReqType", withdrawRequest.getReqType());
	        request.put("TxnAmount", withdrawRequest.getTxnAmount());
	        request.put("AppIP", withdrawRequest.getAppIP());
	        request.put("OrderId", withdrawRequest.getOrderId());
	        request.put("Currency", withdrawRequest.getCurrency());
	        request.put("DeviceId", withdrawRequest.getDeviceId());
	        request.put("SSOToken", withdrawRequest.getSsoToken());
	        request.put("PaymentMode", withdrawRequest.getPaymentMode());
	        request.put("CustId", withdrawRequest.getCustId());
	        request.put("IndustryType", withdrawRequest.getIndustryType());
	        request.put("Channel", withdrawRequest.getChannel());
	        request.put("AuthMode", withdrawRequest.getAuthMode());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID", withdrawRequest.getMid());
	        paramMap.put("PREAUTH_ID", withdrawRequest.getPreAuthId());
	        paramMap.put("ReqType", withdrawRequest.getReqType());
	        paramMap.put("TxnAmount", withdrawRequest.getTxnAmount());
	        paramMap.put("AppIP", withdrawRequest.getAppIP());
	        paramMap.put("OrderId", withdrawRequest.getOrderId());
	        paramMap.put("Currency", withdrawRequest.getCurrency());
	        paramMap.put("DeviceId", withdrawRequest.getDeviceId());
	        paramMap.put("SSOToken", withdrawRequest.getSsoToken());
	        paramMap.put("PaymentMode", withdrawRequest.getPaymentMode());
	        paramMap.put("CustId", withdrawRequest.getCustId());
	        paramMap.put("IndustryType", withdrawRequest.getIndustryType());
	        paramMap.put("Channel", withdrawRequest.getChannel());
	        paramMap.put("AuthMode", withdrawRequest.getAuthMode());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(withdrawRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CheckSum", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
//			URL url = new URL(apiURL+"paymentservices/HANDLER_FF/withdrawScw?JsonData="+request.toString());
			URL url = new URL("https://securegw.paytm.in/paymentservices/HANDLER_FF/withdrawScw?JsonData="+request.toString());
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
	              System.out.println("paytmwithdrawamount RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	          rd.close(); is.close();
	          JSONObject responseJson = new JSONObject(response.toString());
	  	      if(responseJson.has("Status")){
	  	    	  if(responseJson.getString("Status").equalsIgnoreCase("TXN_FAILURE")){
	  	    		  try{
	  					finalResponse.put("responseObj", "");
	  					finalResponse.put("message", responseJson.getString("ResponseMessage"));
	  					finalResponse.put("status", "Failure");
	  					
	  					return finalResponse.toString();
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	    	if(responseJson.getString("Status").equalsIgnoreCase("TXN_SUCCESS")){
	  	    		  try{
	  	    			finalResponse.put("message", responseJson.getString("ResponseMessage"));
//	  		  	      	if(responseJson.has("Status")){responseJson.remove("Status");responseJson.remove("ResponseMessage");responseJson.remove("ResponseCode");}
	  		  	      finalResponse.put("status", "Success");
	  		  	      
	  		  	      JSONObject responsee = new JSONObject();
	  		  	      responsee.put("pendingBalance", "");
	  		  	      responsee.put("orderId", responseJson.getString("OrderId"));
	  		  	  	  responsee.put("usedBalance", responseJson.getString("TxnAmount"));
	  		  		  responsee.put("status", "Success");
	  		  		  responsee.put("transactionRefNo", responseJson.getString("TxnId"));
	  				  responsee.put("CustId", responseJson.getString("CustId"));
	  				  responsee.put("BankTxnId", responseJson.getString("BankTxnId"));
	  		  	      
	  		  	      finalResponse.put("responseObj", responsee);
	  		  	      
	  		  	  /*******paymentTransaction For Save payment details*********/
	  		  	    PaymentController transactionController = new PaymentController();
	  	  	      	PaymentTransaction paymentTransaction = new PaymentTransaction();
	  				paymentTransaction.setStatus(finalResponse.getString("status"));
	  				paymentTransaction.setStatusCode("");
	  				paymentTransaction.setStatusMsg("");

	  				paymentTransaction.setPaidAmount(new Double(responseJson.getString("TxnAmount")));
	  				paymentTransaction.setSettlementAmount("0");
	  				paymentTransaction.setOrderId(responseJson.getString("OrderId"));
	  				paymentTransaction.setMerchantOrderId(responseJson.getString("OrderId"));
	  				paymentTransaction.setPspTransactionId(responseJson.getString("TxnId"));
	  				paymentTransaction.setTransactionType("settle");

	  				paymentTransaction.setServiceType("settle");
	  				paymentTransaction.setStatusMsg(responseJson.getString("ResponseMessage"));
	  				paymentTransaction.setPspId("paytm");
	  				paymentTransaction.setDeviceId("505505");
	  				paymentTransaction.setMerchantId("");
	  				paymentTransaction.setTerminalId("");
	  				paymentTransaction.setAppId("");
	  				
	  				if(withdrawRequest.getReqType().equalsIgnoreCase("WITHDRAW")){
	  					PaymentController paymentController = new PaymentController();
	  					paymentController.payPendingSaveTransaction(paymentTransaction);
	  				} else{
	  					transactionController.updateTransaction(paymentTransaction);
	  				}
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      if(responseJson.has("STATUS")){
	  	    	  if(responseJson.getString("STATUS").equalsIgnoreCase("TXN_FAILURE")){
	  	    		  try{
	  	    			  finalResponse.put("responseObj", "");
	  	    			  finalResponse.put("message", responseJson.getString("ResponseMessage"));
	  	    			  finalResponse.put("status", "Failure");
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      try{
	  	    	  
	  	    	  if(responseJson.has("ErrorMessage")){
	  	    		finalResponse.put("message", responseJson.get("ErrorMessage"));
		  	      	finalResponse.put("status", "Failure");
		  	      	finalResponse.put("responseObj", "");
		  	      	return finalResponse.toString();
	  	    	  }
	  	      finalResponse.put("message", "");
	  	      	if(responseJson.has("Status")){responseJson.remove("Status");}
	  	      finalResponse.put("status", "Success");
	  	      finalResponse.put("responseObj", responseJson);
	  	      
	  	    
	  	      } catch(Exception e){
	  	    	  return finalResponse.toString();
	  	      }
		} catch (IOException e) {
			System.out.println("Exception.."+e);
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		System.out.println("paytmwithdrawamount response "+finalResponse.toString());
		return finalResponse.toString();
	}
	
	@RequestMapping(value="/paytmPayPendingamount")
	public String payPendingAmount(@RequestBody PaytmWithdrawRequest withdrawRequest, String orderId) throws Exception{
		System.out.println("paytmPayPendingamount req "+withdrawRequest +" and oder id "+orderId);
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", withdrawRequest.getMid());
	        request.put("PREAUTH_ID", withdrawRequest.getPreAuthId());
	        request.put("ReqType", withdrawRequest.getReqType());
	        request.put("TxnAmount", withdrawRequest.getTxnAmount());
	        request.put("AppIP", withdrawRequest.getAppIP());
	        request.put("OrderId", withdrawRequest.getOrderId());
	        request.put("Currency", withdrawRequest.getCurrency());
	        request.put("DeviceId", withdrawRequest.getDeviceId());
	        request.put("SSOToken", withdrawRequest.getSsoToken());
	        request.put("PaymentMode", withdrawRequest.getPaymentMode());
	        request.put("CustId", withdrawRequest.getCustId());
	        request.put("IndustryType", withdrawRequest.getIndustryType());
	        request.put("Channel", withdrawRequest.getChannel());
	        request.put("AuthMode", withdrawRequest.getAuthMode());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID", withdrawRequest.getMid());
	        paramMap.put("PREAUTH_ID", withdrawRequest.getPreAuthId());
	        paramMap.put("ReqType", withdrawRequest.getReqType());
	        paramMap.put("TxnAmount", withdrawRequest.getTxnAmount());
	        paramMap.put("AppIP", withdrawRequest.getAppIP());
	        paramMap.put("OrderId", withdrawRequest.getOrderId());
	        paramMap.put("Currency", withdrawRequest.getCurrency());
	        paramMap.put("DeviceId", withdrawRequest.getDeviceId());
	        paramMap.put("SSOToken", withdrawRequest.getSsoToken());
	        paramMap.put("PaymentMode", withdrawRequest.getPaymentMode());
	        paramMap.put("CustId", withdrawRequest.getCustId());
	        paramMap.put("IndustryType", withdrawRequest.getIndustryType());
	        paramMap.put("Channel", withdrawRequest.getChannel());
	        paramMap.put("AuthMode", withdrawRequest.getAuthMode());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(withdrawRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CheckSum", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
//			URL url = new URL(apiURL+"paymentservices/HANDLER_FF/withdrawScw?JsonData="+request.toString());
			URL url = new URL("https://securegw.paytm.in/paymentservices/HANDLER_FF/withdrawScw?JsonData="+request.toString());
//			          https://securegw.paytm.in/paymentservices/HANDLER_FF/withdrawScw

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
	              System.out.println("paytmPayPendingamount RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	          rd.close(); is.close();
	          JSONObject responseJson = new JSONObject(response.toString());
	  	      if(responseJson.has("Status")){
	  	    	  if(responseJson.getString("Status").equalsIgnoreCase("TXN_FAILURE")){
	  	    		  try{
	  					finalResponse.put("responseObj", "");
	  					finalResponse.put("message", responseJson.getString("ResponseMessage"));
	  					finalResponse.put("status", "Failure");
	  					
	  					return finalResponse.toString();
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	    	if(responseJson.getString("Status").equalsIgnoreCase("TXN_SUCCESS")){
	  	    		  try{
	  	    			finalResponse.put("message", responseJson.getString("ResponseMessage"));
//	  		  	      	if(responseJson.has("Status")){responseJson.remove("Status");responseJson.remove("ResponseMessage");responseJson.remove("ResponseCode");}
	  		  	      finalResponse.put("status", "Success");
	  		  	      
	  		  	      JSONObject responsee = new JSONObject();
	  		  	      responsee.put("pendingBalance", "");
	  		  	      responsee.put("orderId", responseJson.getString("OrderId"));
	  		  	  	  responsee.put("usedBalance", responseJson.getString("TxnAmount"));
	  		  		  responsee.put("status", "Success");
	  		  		  responsee.put("transactionRefNo", responseJson.getString("TxnId"));
	  				  responsee.put("CustId", responseJson.getString("CustId"));
	  				  responsee.put("BankTxnId", responseJson.getString("BankTxnId"));
	  		  	      
	  		  	      finalResponse.put("responseObj", responsee);
	  		  	      
	  		  	  /*******paymentTransaction For Save payment details*********/
	  		  	    PaymentController transactionController = new PaymentController();
	  	  	      	PaymentTransaction paymentTransaction = new PaymentTransaction();
	  				paymentTransaction.setStatus(finalResponse.getString("status"));
	  				paymentTransaction.setStatusCode("");
	  				paymentTransaction.setStatusMsg("");

	  				paymentTransaction.setPaidAmount(new Double(responseJson.getString("TxnAmount")));
	  				paymentTransaction.setSettlementAmount("0");
	  				paymentTransaction.setOrderId(orderId);
	  				paymentTransaction.setMerchantOrderId(responseJson.getString("OrderId"));
	  				paymentTransaction.setPspTransactionId(responseJson.getString("TxnId"));
	  				paymentTransaction.setTransactionType("settle");

	  				paymentTransaction.setServiceType("settle");
	  				paymentTransaction.setStatusMsg(responseJson.getString("ResponseMessage"));
	  				paymentTransaction.setPspId("paytm");
	  				paymentTransaction.setDeviceId("505505");
	  				paymentTransaction.setMerchantId("");
	  				paymentTransaction.setTerminalId("");
	  				paymentTransaction.setAppId("");
	  				
	  					PaymentController paymentController = new PaymentController();
	  					paymentController.payPendingSaveTransaction(paymentTransaction);
	  				
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      if(responseJson.has("STATUS")){
	  	    	  if(responseJson.getString("STATUS").equalsIgnoreCase("TXN_FAILURE")){
	  	    		  try{
	  	    			  finalResponse.put("responseObj", "");
	  	    			  finalResponse.put("message", responseJson.getString("ResponseMessage"));
	  	    			  finalResponse.put("status", "Failure");
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  }
	  	      }
	  	      try{
	  	      finalResponse.put("message", "");
	  	      	if(responseJson.has("Status")){responseJson.remove("Status");}
	  	      finalResponse.put("status", "Success");
	  	      finalResponse.put("responseObj", responseJson);
	  	      
	  	    
	  	      } catch(Exception e){
	  	    	  return finalResponse.toString();
	  	      }
		} catch (IOException e) {
			System.out.println("Exception.."+e);
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		System.out.println("paytmPayPendingamount response "+finalResponse.toString());
		return finalResponse.toString();
	}
}
