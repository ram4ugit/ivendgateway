package com.limitlessmobility.iVendGateway.services.common;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.util.Base64Encoder;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.common.CommonValidateOtpRequest;
import com.limitlessmobility.iVendGateway.model.common.OTPRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.LogoutRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmOTPRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.ValidateOTPRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.ValidateTokenRequest;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.OTPService;

@RestController
@RequestMapping(value="/v2")
public class CommonOTPService {
	
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value="/sendOTP")
	public String sendOTP(@RequestBody OTPRequest commonOtpRequest){
		System.out.println("commonOtpRequest.. "+commonOtpRequest);
		String responsne = "";
		String line="";
		StringBuilder response = new StringBuilder();
		JSONObject finalResponse = new JSONObject();
		try{
			TokenAuthModel t = new TokenAuthModel();
			String token = request.getHeader("Authorization");
			String header = request.getHeader(HEADER_STRING);
			if (header != null && header.startsWith(TOKEN_PREFIX)) {
				token = header.replace(TOKEN_PREFIX,"");
			} else{
				header=request.getParameter(HEADER_STRING);
			    if (header != null && header.startsWith(TOKEN_PREFIX)) {
			    	token = header.replace(TOKEN_PREFIX,"");
			    }
			}
			t = JwtTokenDecode.validateJwt(token);
			if(CommonValidationUtility.isEmpty(t.getOperatorId())){
				System.out.println("token authentication failed!!");
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Access Denied");
				finalResponse.put("status", "Failure");
			} else {
				int operatorId = Integer.parseInt(t.getOperatorId());
				
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, commonOtpRequest.getPspid(), "wallet");
				
				PaytmOTPRequest paytmOTPRequest = new PaytmOTPRequest();
				
				List<String> clientDetails = Arrays.asList(Pattern.compile("\\|").split(op.getPspMguid()));
				paytmOTPRequest.setClientId(clientDetails.get(0));
				paytmOTPRequest.setEmail(commonOtpRequest.getEmail());
				paytmOTPRequest.setPhone(commonOtpRequest.getPhone());
				paytmOTPRequest.setScope("wallet");
				paytmOTPRequest.setResponseType("token");
				String requestAPI = paytmOTPRequest.toString();
				OTPService otpService = new OTPService();
				responsne = otpService.sendOTP(paytmOTPRequest);
				
				/*URL url = new URL("https://accounts.paytm.com/signin/otp");
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
		      	
		      	finalResponse.put("responseObj", responseJson);*/
			}
		} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return responsne;
	}
	
	
	@RequestMapping(value="/validateOTP")
	public String validateOTP(@RequestBody CommonValidateOtpRequest commonValidateRequest){
		
		String responsne = "";
		String line="";
		StringBuilder response = new StringBuilder();
		JSONObject finalResponse = new JSONObject();
		try{
			TokenAuthModel t = new TokenAuthModel();
			String token = request.getHeader("Authorization");
			String header = request.getHeader(HEADER_STRING);
			if (header != null && header.startsWith(TOKEN_PREFIX)) {
				token = header.replace(TOKEN_PREFIX,"");
			} else{
				header=request.getParameter(HEADER_STRING);
			    if (header != null && header.startsWith(TOKEN_PREFIX)) {
			    	token = header.replace(TOKEN_PREFIX,"");
			    }
			}
			t = JwtTokenDecode.validateJwt(token);
			if(CommonValidationUtility.isEmpty(t.getOperatorId())){
				System.out.println("token authentication failed!!");
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Access Denied");
				finalResponse.put("status", "Failure");
			} else {
				int operatorId = Integer.parseInt(t.getOperatorId());
				
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, commonValidateRequest.getPspid(), "wallet");
				
				ValidateOTPRequest validateOTPRequest = new ValidateOTPRequest();
				
				List<String> clientDetails = Arrays.asList(Pattern.compile("\\|").split(op.getPspMguid()));
				validateOTPRequest.setClientId(clientDetails.get(0));
				validateOTPRequest.setClientSecret(clientDetails.get(1));
				validateOTPRequest.setOtp(commonValidateRequest.getOtp());
				validateOTPRequest.setState(commonValidateRequest.getState());
				String requestAPI = validateOTPRequest.toString();
				OTPService otpService = new OTPService();
				responsne = otpService.validateOTP(validateOTPRequest);
				
			}
		} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return responsne;
	}

	@RequestMapping(value="/validateToken")
	public String validateToken(@RequestBody ValidateTokenRequest requestValidate){
		
		
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			
			URL url = new URL("https://accounts.paytm.com/user/details");
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
		
		
		String line="";
		StringBuilder response = new StringBuilder();
		
		JSONObject finalResponse = new JSONObject();
		try{
			
			/*try{
				TokenAuthModel t = new TokenAuthModel();
				String tokenn = request.getHeader("Authorization");
				String header = request.getHeader(HEADER_STRING);
				if (header != null && header.startsWith(TOKEN_PREFIX)) {
					tokenn = header.replace(TOKEN_PREFIX,"");
				} else{
					header=request.getParameter(HEADER_STRING);
				    if (header != null && header.startsWith(TOKEN_PREFIX)) {
				    	tokenn = header.replace(TOKEN_PREFIX,"");
				    }
				}
				t = JwtTokenDecode.validateJwt(tokenn);
				if(CommonValidationUtility.isEmpty(t.getOperatorId())){
					System.out.println("token authentication failed!!");
					finalResponse.put("responseObj", "");
					finalResponse.put("message", "Access Denied");
					finalResponse.put("status", "Failure");
				} else {
					int operatorId = Integer.parseInt(t.getOperatorId());
					
					OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, requestLogout.getPspId(), "wallet");
					
					ValidateOTPRequest validateOTPRequest = new ValidateOTPRequest();
					
					List<String> clientDetails = Arrays.asList(Pattern.compile("\\|").split(op.getPspMguid()));
					validateOTPRequest.setClientId(clientDetails.get(0));
					validateOTPRequest.setClientSecret(clientDetails.get(1));*/
			
//			JSONObject requestJson = new JSONObject();
			/*requestJson.remove("Authorization");
			requestJson.remove("clientId");
			requestJson.remove("clientSecret");*/
//			requestJson.put("session_token", requestLogout.getSessionToken());
			
			String authToken = requestLogout.getClientId()+":"+requestLogout.getClientSecret();
//			byte[] bytesEncoded = Base64.getEncoder().encode(authToken.getBytes());
			URL url = new URL("https://accounts.paytm.com/oauth2/accessToken/"+requestLogout.getSessionToken());
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("DELETE");
	        String token = "Basic "+Base64Encoder.getBase64(authToken);
	        connection.setRequestProperty("Authorization", token);
//	        connection.setRequestProperty("Content-Length", Integer.toString(requestJson.toString().getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
	       
	      /* try (DataOutputStream wr = new DataOutputStream (connection.getOutputStream())) {
	    	   
	    	   wr.writeBytes(requestJson.toString());
	       }*/
	       
	       int responseCode = connection.getResponseCode();
	       InputStream is;
	       if(responseCode == HttpURLConnection.HTTP_OK){
	    	   is = connection.getInputStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Success");
				finalResponse.put("status", "Success");
	       } else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
	    	   is = connection.getInputStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Unautherized");
				finalResponse.put("status", "Failure");
	       } else {
	    	   is = connection.getErrorStream();
	    	   finalResponse.put("responseObj", "");
				finalResponse.put("message", "Alredy Logged Out");
				finalResponse.put("status", "Failure");
	       }
	       /*BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	       line="";
	      while((line = rd.readLine()) != null) {
		      response.append(line);
		      System.out.println("RESPONSE From PayTm..."+response.toString());
		      System.out.append("\r");
	      }
	      rd.close(); is.close();*/
	      
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
	      
				/*}
			} catch(Exception e){
				try{
					finalResponse.put("responseObj", "");
					finalResponse.put("message", e.getMessage());
					finalResponse.put("status", "Failure");
				} catch(Exception ee){ee.printStackTrace();}
			}*/
			} catch(Exception e){
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			} catch(Exception ee){ee.printStackTrace();}
		}
		
		return finalResponse.toString();
	}
}
