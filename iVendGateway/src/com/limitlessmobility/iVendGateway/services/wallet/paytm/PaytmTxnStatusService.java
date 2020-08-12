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
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmReleaseRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmTxnStatusRequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmTxnStatusService {

	@RequestMapping(value="/paytmtxnstatus")
	public String getTxnStatus(@RequestBody PaytmTxnStatusRequest txnStatusRequest) throws Exception{
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        JSONObject request = new JSONObject();
	        request.put("MID", txnStatusRequest.getMid());
	        request.put("ORDERID", txnStatusRequest.getOrderId());
	        request.put("TXN_TYPE", txnStatusRequest.getTxnType());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID",txnStatusRequest.getMid());
	        paramMap.put("ORDERID",txnStatusRequest.getOrderId());
	        paramMap.put("TXN_TYPE",txnStatusRequest.getTxnType());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(txnStatusRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CHECKSUMHASH", newchecksum);
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"merchant-status/getTxnStatus?JsonData="+request.toString());
//			URL url = new URL("https://securegw-stage.paytm.in/merchant-status/getTxnStatus?JsonData="+request.toString());
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
	  	    	  if(responseJson.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS")){
	  	    		  try{
	  	    			finalResponse.put("message", responseJson.getString("RESPMSG"));
	  		  	      		if(responseJson.has("STATUS")){responseJson.remove("STATUS");}
	  		  	      		if(responseJson.has("RESPCODE")){responseJson.remove("RESPCODE");}
	  		  	      		if(responseJson.has("RESPMSG")){responseJson.remove("RESPMSG");}
	  		  	      	finalResponse.put("status", "Success");
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
	  	    if(responseJson.has("RESPMSG")){finalResponse.put("message", responseJson.getString("RESPMSG"));}
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
	  	     
	  	      
		} catch (IOException e) {
			System.out.println("Exception.."+e);
			finalResponse.put("message", "Error- "+e.getMessage());
  	      	finalResponse.put("status", "Failure");
  	      	finalResponse.put("responseObj", "");
		}
		return finalResponse.toString();
	}
}
