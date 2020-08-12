package com.limitlessmobility.iVendGateway.services.wallet.paytm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.paytm.pg.merchant.CheckSumServiceHelper;

public class Refund {

	public static void main(String[] args) throws JSONException {
	//	excutePost("https://pguat.paytm.com/oltp/HANDLER_INTERNAL/REFUND?");
	  excutePost("https://securegw.paytm.in/refund/HANDLER_INTERNAL/REFUND");
		//excutePost("https://securegw-stage.paytm.in/refund/HANDLER_INTERNAL/REFUND");
	  //excutePost("https://pguat.paytm.com/oltp/HANDLER_INTERNAL/REFUND");
	
	}
	public static String excutePost(String targetURL) throws JSONException {
		HttpURLConnection connection = null;
		JSONObject jsonRequestObj1 = new JSONObject(); 
                        jsonRequestObj1 = getJsonRequestObject();
                try {
			
              targetURL=targetURL.concat("?JsonData=");
                    
                   String targetURL1 = targetURL.concat(jsonRequestObj1.toString());
                    
                
			URL url = new URL(targetURL1);
			
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			String urlParameters=jsonRequestObj1.toString();
				
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
		      
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream (
			connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              		StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+ 
			String line= new String("hello");
                        
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
                        System.out.println("response is =" +response);
			rd.close();
                       
			return response.toString();
		}  catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			if(connection != null) {
				connection.disconnect(); 
			}
		}
		return "";
	}
	
	public static JSONObject getJsonRequestObject() throws JSONException {
		Random rand = new Random();
		int  n = rand.nextInt(50) + 1;

        String MID="Vendim69261175597160";
        String ORDERID="Paytm74db45b0f4ed4f2caec2475b88c0b131";
        String TXNID="20190924111212800110168489189141062";
        String REFUNDAMOUNT="0.01";
        String TXNTYPE="REFUND";
        int REFID=n;
        String newchecksum="";
        String CHECKSUM="";
		TreeMap<String,String>paramMap=new TreeMap<String, String>();
            paramMap.put("MID",MID);
	        paramMap.put("ORDERID",ORDERID);
	        paramMap.put("TXNID",TXNID);
	        paramMap.put("REFUNDAMOUNT",REFUNDAMOUNT);
	        paramMap.put("TXNTYPE",TXNTYPE);
	        
        try{
            CHECKSUM=CheckSumServiceHelper.getCheckSumServiceHelper().genrateRefundCheckSum("eiqC64xTwuQ0KQU4",paramMap);
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