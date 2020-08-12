package com.limitlessmobility.iVendGateway.services.wallet.paytm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmWalletRefundRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmWalletRefundStatusRequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmRefundStatusService {

	@RequestMapping(value="/paytmwalletrefundstatus")
	public String paytmwalletrefundstatus(@RequestBody PaytmWalletRefundStatusRequest refundStatusRequest) throws Exception{
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", refundStatusRequest.getMid());
	        request.put("ORDERID", refundStatusRequest.getOrderId());
	        request.put("REFID", refundStatusRequest.getRefId());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID", refundStatusRequest.getMid());
	        paramMap.put("ORDERID", refundStatusRequest.getOrderId());
	        paramMap.put("REFID", refundStatusRequest.getRefId());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(refundStatusRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CHECKSUMHASH", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"refund/HANDLER_INTERNAL/getRefundStatus?JsonData="+request.toString());
//			URL url = new URL("https://securegw-stage.Paytm.in/refund/HANDLER_INTERNAL/getRefundStatus?JsonData="+request.toString());
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
	              System.out.println("RESPONSE From PayTm..."+response.toString());
	              System.out.append("\r");
              }
	          rd.close(); is.close();
	          JSONObject responseJson = new JSONObject(response.toString());
	  	      if(responseJson.has("STATUS")){
	  	    	  if(responseJson.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS") || responseJson.getString("STATUS").equalsIgnoreCase("PENDING")){
	  	    		  try{
	  	    			finalResponse.put("message", responseJson.getString("RESPMSG"));
	  		  	      		if(responseJson.has("STATUS")){responseJson.remove("STATUS");}
	  		  	      		if(responseJson.has("RESPCODE")){responseJson.remove("RESPCODE");}
	  		  	      		if(responseJson.has("RESPMSG")){responseJson.remove("RESPMSG");}
	  		  	      	finalResponse.put("status", responseJson.getString("STATUS"));
	  		  	      	finalResponse.put("responseObj", responseJson);
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  } else {
		  	    		finalResponse.put("message", responseJson.getString("RESPMSG"));
			  	      	finalResponse.put("status", "Failure");
			  	      	finalResponse.put("responseObj", "");
			  	      	return finalResponse.toString();
	  	    	  }
	  	      }
	  	      
	  	    if(responseJson.has("REFUND_LIST")){
	  	    	JSONArray refundArray = responseJson.getJSONArray("REFUND_LIST");
	  	    	JSONObject refundJson = refundArray.getJSONObject(0);
	  	    	  if(refundJson.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS")){
	  	    		  try{
	  	    			finalResponse.put("message", refundJson.getString("RESPMSG"));
	  		  	      		if(responseJson.has("STATUS")){refundJson.remove("STATUS");}
	  		  	      		if(responseJson.has("RESPCODE")){refundJson.remove("RESPCODE");}
	  		  	      		if(responseJson.has("RESPMSG")){refundJson.remove("RESPMSG");}
	  		  	      	finalResponse.put("status", refundJson.getString("STATUS"));
	  		  	      	finalResponse.put("responseObj", refundJson);
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  } else {
		  	    		finalResponse.put("message", refundJson.getString("RESPMSG"));
			  	      	finalResponse.put("status", "Failure");
			  	      	finalResponse.put("responseObj", "");
			  	      	return finalResponse.toString();
	  	    	  }
	  	      }
	  	    if(responseJson.has("RESPMSG")){finalResponse.put("message", responseJson.getString("RESPMSG"));}
	  	  if(responseJson.has("ErrorMsg")){finalResponse.put("message", "ErrorMsg- "+responseJson.getString("ErrorMsg"));}
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
	  	     
	  	      
		} catch (IOException e) {
			System.out.println(e);
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		return finalResponse.toString();
	}
}
