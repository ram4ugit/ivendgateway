package com.limitlessmobility.iVendGateway.services.wallet.paytm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limitlessmobil.ivendgateway.util.Base64Encoder;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.LogoutRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmOTPRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.ValidateOTPRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.ValidateTokenRequest;

import org.apache.commons.codec.binary.Base64;


//@RestController
//@RequestMapping(value="/v2")
public class OTPService {

//	@RequestMapping(value="/sendOTP")
	public String sendOTP(@RequestBody PaytmOTPRequest otpRequest){
		
		String requestAPI = otpRequest.toString();
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"signin/otp");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type","application/json");
	        connection.setRequestProperty("Content-Length", Integer.toString(requestAPI.getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	       
	       try (DataOutputStream wr = new DataOutputStream (connection.getOutputStream())) {
	    	   
	    	   wr.writeBytes(requestAPI);
	       }
	       
	       int responseCode = connection.getResponseCode();
	       InputStream is;
	       if(responseCode == HttpURLConnection.HTTP_OK){
	    	   is = connection.getInputStream();
	       }else {
	    	   is = connection.getErrorStream();
	       }
	       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	       line="";
	      while((line = rd.readLine()) != null) {
		      response.append(line);
		      System.out.println("RESPONSE From PayTm..."+response.toString());
		      System.out.append("\r");
	      }
	      rd.close(); is.close();
	      
	      	JSONObject responseJson = new JSONObject(response.toString());
	      	
			finalResponse.put("message", responseJson.getString("message"));
			finalResponse.put("status", responseJson.getString("status"));
			
			responseJson.remove("status");
	      	responseJson.remove("message");
	      	
	      	finalResponse.put("responseObj", responseJson);
	      
		} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return finalResponse.toString();
	}
	
	
//	@RequestMapping(value="/validateOTP")
	public String validateOTP(@RequestBody ValidateOTPRequest requestValidate){
		
		
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			String requestAPI = requestValidate.toString();
			
			JSONObject requestJson = new JSONObject(requestAPI);
			requestJson.remove("clientId");
			requestJson.remove("clientSecret");
			
			String authToken = requestValidate.getClientId()+":"+requestValidate.getClientSecret();
//			byte[] bytesEncoded = Base64.getEncoder().encode(authToken.getBytes());
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"signin/validate/otp");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type","application/json");
	        String token = "Basic "+Base64Encoder.getBase64(authToken);
	        connection.setRequestProperty("Authorization", token);
	        connection.setRequestProperty("Content-Length", Integer.toString(requestJson.toString().getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	       
	       try (DataOutputStream wr = new DataOutputStream (connection.getOutputStream())) {
	    	   
	    	   wr.writeBytes(requestAPI);
	       }
	       
	       int responseCode = connection.getResponseCode();
	       InputStream is;
	       if(responseCode == HttpURLConnection.HTTP_OK){
	    	   is = connection.getInputStream();
	       }else {
	    	   is = connection.getErrorStream();
	       }
	       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	       line="";
	      while((line = rd.readLine()) != null) {
		      response.append(line);
		      System.out.println("RESPONSE From PayTm..."+response.toString());
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
			finalResponse.put("message", "");
			finalResponse.put("status", "Success");
			
//			responseJson.remove("status");
//	      	responseJson.remove("message");
			if(responseJson.has("expires")){
				String expiresDate = String.valueOf(responseJson.getLong("expires"));
				Date expiry = new Date(Long.parseLong(expiresDate));
				responseJson.remove("expires");
				responseJson.put("expires", expiry);
			}
	      	finalResponse.put("responseObj", responseJson);
	      
		} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return finalResponse.toString();
	}

	@RequestMapping(value="/validateToken")
	public String validateToken(@RequestBody ValidateTokenRequest requestValidate){
		
		
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"user/details");
//			URL url = new URL("https://accounts.paytm.com/user/details");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
	        connection.setRequestProperty("session_token", requestValidate.getSessionToken());
//	        connection.setRequestProperty("Content-Length", Integer.toString(requestJson.toString().getBytes().length));
	        
	       
	       int responseCode = connection.getResponseCode();
	       InputStream is;
	       if(responseCode == HttpURLConnection.HTTP_OK){
	    	   is = connection.getInputStream();
	       }else {
	    	   is = connection.getErrorStream();
	       }
	       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	       line="";
	      while((line = rd.readLine()) != null) {
		      response.append(line);
		      System.out.println("RESPONSE From PayTm..."+response.toString());
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
				finalResponse.put("message", "");
				finalResponse.put("status", "Success");
				
//				responseJson.remove("status");
//		      	responseJson.remove("message");
		      	
		      	finalResponse.put("responseObj", responseJson);
	      
		} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return finalResponse.toString();
	}
	
	@RequestMapping(value="/logoutPaytmWallet")
	public String logoutPaytmWallet(@RequestBody LogoutRequest requestLogout){
		
		System.out.println("logoutPaytmWallet request "+requestLogout.toString());
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			JSONObject requestJson = new JSONObject();
			/*requestJson.remove("Authorization");
			requestJson.remove("clientId");
			requestJson.remove("clientSecret");*/
//			requestJson.put("session_token", requestLogout.getSessionToken());
			
			String authToken = requestLogout.getClientId()+":"+requestLogout.getClientSecret();
//			byte[] bytesEncoded = Base64.getEncoder().encode(authToken.getBytes());
			String apiURL=Util.apiUrlReaderPaytm();
			URL url = new URL(apiURL+"oauth2/accessToken/"+requestLogout.getSessionToken());
//			URL url = new URL("https://accounts.paytm.com/oauth2/accessToken/"+requestLogout.getSessionToken());
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("DELETE");
	        String token = "Basic "+Base64Encoder.getBase64(authToken);
	        connection.setRequestProperty("Authorization", token);
	        connection.setRequestProperty("Content-Length", Integer.toString(requestJson.toString().getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	       
	       try (DataOutputStream wr = new DataOutputStream (connection.getOutputStream())) {
	    	   
	    	   wr.writeBytes(requestJson.toString());
	       }
	       
	       int responseCode = connection.getResponseCode();
	       InputStream is;
	       if(responseCode == HttpURLConnection.HTTP_OK){
	    	   is = connection.getInputStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Logged Out");
				finalResponse.put("status", "Success");
	       } else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
	    	   is = connection.getInputStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Logged Out");
				finalResponse.put("status", "Success");
	       } else {
	    	   is = connection.getErrorStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Failure");
				finalResponse.put("status", "Failure");
	       }
	       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	       line="";
	      while((line = rd.readLine()) != null) {
		      response.append(line);
		      System.out.println("logoutPaytmWallet RESPONSE From PayTm..."+response.toString());
		      System.out.append("\r");
	      }
	      rd.close(); is.close();
	      
	      	/*JSONObject responseJson = new JSONObject(response.toString());
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
			finalResponse.put("message", "");
			finalResponse.put("status", "Success");
			
//			responseJson.remove("status");
//	      	responseJson.remove("message");
	      	
	      	finalResponse.put("responseObj", responseJson);*/
	      
		} catch(Exception e){
			try{
		    	   finalResponse.put("responseObj", "");
					finalResponse.put("message", "Logged Out");
					finalResponse.put("status", "Success");
		       } catch(Exception ee){ee.printStackTrace();}
		}
		
		return finalResponse.toString();
	}
}
