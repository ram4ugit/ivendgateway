package com.limitlessmobility.iVendGateway.services.wallet.paytm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmTxnStatusRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmWalletRefundRequest;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="/v2")
public class PaytmWalletRefundService {
	private static String URL = "https://securegw-stage.Paytm.in";
	@RequestMapping(value="/paytmwalletrefund")
	public String paytmWalletRefund(@RequestBody PaytmWalletRefundRequest refundRequest) throws Exception{
		JSONObject finalResponse = new JSONObject();
		try {
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        System.out.println("OrderID..."+uniqueOrderId);
	        /*JSONObject request = new JSONObject();
	        request.put("MID", refundRequest.getMid());
	        request.put("TXNID", refundRequest.getTxnId());
	        request.put("ORDERID", refundRequest.getOrderId());
	        request.put("REFUNDAMOUNT", refundRequest.getRefundAmount());
	        request.put("TXNTYPE", refundRequest.getTxnType());
	        request.put("REFID", refundRequest.getRefId());
        
	        TreeMap<String,String>paramMap=new TreeMap<String, String>();
	        paramMap.put("MID", refundRequest.getMid());
	        paramMap.put("TXNID", refundRequest.getTxnId());
	        paramMap.put("ORDERID", refundRequest.getOrderId());
	        paramMap.put("REFUNDAMOUNT", refundRequest.getRefundAmount());
	        paramMap.put("TXNTYPE", refundRequest.getTxnType());
//	        paramMap.put("REFID", refundRequest.getRefId());
		
			CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
			String CHECKSUMHASH = checksumHelper.genrateCheckSum(refundRequest.getMerchantKey(), paramMap);
			String newchecksum=URLEncoder.encode(CHECKSUMHASH);
			request.put("CHECKSUM", newchecksum);*/
	        
	        JSONObject requestData = getJsonRequestObject(refundRequest.getMid(), refundRequest.getMerchantKey(), refundRequest.getOrderId(), refundRequest.getTxnId(), refundRequest.getRefundAmount(), refundRequest.getTxnType(),refundRequest.getRefId());
	        
//			URL url = new URL("https://securegw-stage.Paytm.in/refund/HANDLER_INTERNAL/REFUND?JsonData="+request.toString());
	        String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL("https://securegw.paytm.in/refund/HANDLER_INTERNAL/REFUND?JsonData="+requestData);
//			URL url = new URL(URL+"/refund/HANDLER_INTERNAL/REFUND?JsonData="+requestData);
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
	  	    	  } else if(responseJson.getString("STATUS").equalsIgnoreCase("PENDING")){
	  	    		  try{
	  	    			finalResponse.put("message", responseJson.getString("RESPMSG"));
	  		  	      		if(responseJson.has("STATUS")){responseJson.remove("STATUS");}
	  		  	      		if(responseJson.has("RESPCODE")){responseJson.remove("RESPCODE");}
	  		  	      		if(responseJson.has("RESPMSG")){responseJson.remove("RESPMSG");}
	  		  	      	finalResponse.put("status", "Pending");
	  		  	      	finalResponse.put("responseObj", responseJson);
	  	    		  } catch(Exception ee){ee.printStackTrace();}
	  	    		  return finalResponse.toString();
	  	    	  } else{
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
	public JSONObject getJsonRequestObject(String mid, String merchantKey, String orderid, String txnId, String refundAmount, String txnType, String refId) throws JSONException {
		/*Random rand = new Random();
		int  n = rand.nextInt(50) + 1;
*/
        String MID=mid;
        String ORDERID=orderid;
        String TXNID=txnId;
        String REFUNDAMOUNT=refundAmount;
        String TXNTYPE=txnType;
        String REFID=refId;
        String newchecksum="";
        String CHECKSUM="";
		TreeMap<String,String>paramMap=new TreeMap<String, String>();
            paramMap.put("MID",MID);
	        paramMap.put("ORDERID",ORDERID);
	        paramMap.put("TXNID",TXNID);
	        paramMap.put("REFUNDAMOUNT",REFUNDAMOUNT);
	        paramMap.put("TXNTYPE",TXNTYPE);
	        
        try{
            CHECKSUM=CheckSumServiceHelper.getCheckSumServiceHelper().genrateRefundCheckSum(merchantKey,paramMap);
            System.out.println(paramMap);
            newchecksum=URLEncoder.encode(CHECKSUM);
            System.out.println(CHECKSUM);
        }
        catch(Exception e)
        {
            System.out.println("sdsd" +e);
        }
			
      JSONObject jsonRequestObj = new JSONObject();
            jsonRequestObj.put("MID",MID);
            jsonRequestObj.put("ORDERID", ORDERID);
            jsonRequestObj.put("TXNID", TXNID);
            jsonRequestObj.put("REFUNDAMOUNT", REFUNDAMOUNT);
            jsonRequestObj.put("TXNTYPE", TXNTYPE);
            
            jsonRequestObj.put("CHECKSUM", newchecksum);
	
            jsonRequestObj.put("REFID", REFID);
            System.out.println(jsonRequestObj);
            return jsonRequestObj;
    	}
}
