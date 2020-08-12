package com.limitlessmobility.iVendGateway.services.wallet;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.ResponseCodes;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.ReleaseAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletReleaseRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

/**
 * @author RamN
 * @version 1.0 This class is used for check status of PayTM
 */
@Controller
@RequestMapping("/v2/wallet")
public class WalletReleaseAmountService {

	/*@Autowired
	private HttpServletRequest request;*/
	
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	
//	OperatorDetail operatorDetail = new OperatorDetail();
	
	/*
	 * This Method is used to release wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/releaseWalletAmount", method = RequestMethod.POST)
	@ResponseBody
	public String releaseWalletAmount(@RequestBody ReleaseAmountRequest releaseAmountRequest) throws JSONException{
		
		JSONObject finalResponse = new JSONObject();
		PaymentController transactionController = new PaymentController();
		boolean isExistOrderId = transactionController.getTransactionExistByOrder(releaseAmountRequest.getOrderId());
		if(!isExistOrderId){
			finalResponse.put("responseObj", "");
			finalResponse.put("message", "Order id not generated");
			finalResponse.put("status", "Failure");
			return finalResponse.toString();
		}
		PaymentTransaction p = transactionController.getTransaction(releaseAmountRequest.getOrderId());
		releaseAmountRequest.setTransactionRefNo(p.getPspTransactionId().trim());
		
		JSONObject responseJson = new JSONObject();
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		
		String walletId=releaseAmountRequest.getWalletId();
		String walletKey=releaseAmountRequest.getWalletKey();
		String walletAccountId=releaseAmountRequest.getWalletAccountId();
		String walletAccountKey=releaseAmountRequest.getWalletAccountKey();
		
		
		StringBuilder response = new StringBuilder();
//		TokenAuthModel t = new TokenAuthModel();
		/*try{
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
			 System.out.println("TOKENNNN===== "+token);
				t = JwtTokenDecode.validateJwt(token);
			} catch(Exception e){
				System.out.println(e);
			}*/
		/*if(CommonValidationUtility.isEmpty(t.getOperatorId())){
			System.out.println("token authentication failed!!");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
			responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponseObj(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
		} else {*/
			
//			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, "wallet");
			
			WalletReleaseRequest walletReleaseRequest = new WalletReleaseRequest();

			walletReleaseRequest.setWalletAccountId(walletAccountId);
			walletReleaseRequest.setRefTransactionId(releaseAmountRequest.getTransactionRefNo());
			walletReleaseRequest.setSource(releaseAmountRequest.getSource());
			
			String urlParameters=walletReleaseRequest.toString();
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/wallet/transaction/release?version=2.0");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",walletId);

				connection.setRequestProperty("WalletKey",walletKey);
				
//				connection.setRequestProperty("AccountKey", walletAccountKey);
//					connection.setRequestProperty("api-version", "2.0");
				
				
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches(false);
		
		       connection.setDoOutput(true);
//					response.setContentType(“application/json”);
		       
		       try (DataOutputStream wr = new DataOutputStream (

		    		      connection.getOutputStream())) {
		    		  wr.writeBytes(urlParameters);
		    		}
		       
		       
		       int responseCode = connection.getResponseCode();

		        //System.out.println(responseCode+"\tSuccess");

		        InputStream is;

		        if(responseCode == HttpURLConnection.HTTP_OK){

		        is = connection.getInputStream();

		        }else {

		        is = connection.getErrorStream();
		        finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", "Order id doesn't exist");
				finalResponse.put("status", "Falilure");
				return finalResponse.toString();

		        }
		              
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			/*} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			
			try{*/
			JSONObject j = new JSONObject(response.toString());
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			JSONObject modelResponseJson = j.getJSONObject("model");
			
			
				responseJson.put("releaseRefTransactionId", modelResponseJson.getLong("transactionId"));
				
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
				
				
				try{
					/*******paymentTransaction For Save payment details*********/
						paymentTransaction.setStatus(j.getString("status"));
						paymentTransaction.setStatusCode(j.getString("status"));
						paymentTransaction.setStatusMsg(j.getString("message"));
						paymentTransaction.setOrderId(releaseAmountRequest.getOrderId());
		
						paymentTransaction.setPspTransactionId(String.valueOf(modelResponseJson.getLong("transactionId")));
						paymentTransaction.setTransactionType("Release");
		
						paymentTransaction.setServiceType("Release");
						paymentTransaction.setStatusMsg(j.getString("message"));
						paymentTransaction.setPspId(releaseAmountRequest.getPspId());
						paymentTransaction.setDeviceId("505505");
						paymentTransaction.setMerchantId("");
						paymentTransaction.setTerminalId("");
						paymentTransaction.setAppId("");
						
						
						transactionController.updateReleaseTransaction(paymentTransaction);
					} catch(Exception e){
						System.out.println("Error in Wallet Release Amount Save.. "+e);
					}
			} else {
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
			}
			System.out.println();
			eventLogRequest.setEventType("wallet Release Service Request");
			eventLogResponse.setEventType("wallet Release Service Response");
			eventLogResponse.setVersion(ResponseCodes.VERSION);
			eventLogRequest.setEventDetails(releaseAmountRequest.toString());
			eventLogResponse.setEventDetails(finalResponse.toString());
			
			eventLogRequest.setOrderID("");
			eventLogResponse.setOrderID("");
			
			eventLogRequest.setVersion("v2");
			
			eventDao.saveEventLog(eventLogRequest);
			eventDao.saveEventLog(eventLogResponse);
			} catch(Exception e){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			}
			return finalResponse.toString();
//		}
		
	}

	/*
	 * This Method is used to release wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/voidTransaction", method = RequestMethod.POST)
	@ResponseBody
	public String voidTransaction(@RequestBody ReleaseAmountRequest releaseAmountRequest) throws JSONException{
		
		JSONObject finalResponse = new JSONObject();
		PaymentController transactionController = new PaymentController();
		boolean isExistOrderId = transactionController.getTransactionExistByOrder(releaseAmountRequest.getOrderId());
		if(!isExistOrderId){
			finalResponse.put("responseObj", "");
			finalResponse.put("message", "Order id not generated");
			finalResponse.put("status", "Failure");
			return finalResponse.toString();
		}
		PaymentTransaction p = transactionController.getTransaction(releaseAmountRequest.getOrderId());
		releaseAmountRequest.setTransactionRefNo(p.getPspTransactionId().trim());
		
		JSONObject responseJson = new JSONObject();
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		
		String walletId=releaseAmountRequest.getWalletId();
		String walletKey=releaseAmountRequest.getWalletKey();
		String walletAccountId=releaseAmountRequest.getWalletAccountId();
		
		
		StringBuilder response = new StringBuilder();
			
			WalletReleaseRequest walletReleaseRequest = new WalletReleaseRequest();

			walletReleaseRequest.setWalletAccountId(walletAccountId);
			walletReleaseRequest.setRefTransactionId(releaseAmountRequest.getTransactionRefNo());
			walletReleaseRequest.setSource(releaseAmountRequest.getSource());
			
			String urlParameters=walletReleaseRequest.toString();
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/wallet/transaction/release?version=2.0");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",walletId);

				connection.setRequestProperty("WalletKey",walletKey);
				
//				connection.setRequestProperty("AccountKey", walletAccountKey);
//					connection.setRequestProperty("api-version", "2.0");
				
				System.out.println("void transacion url "+url);
				System.out.println("void transacion req "+urlParameters);
				System.out.println("void transacion header WalletId: "+walletId+" WalletKey: "+walletKey);
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches(false);
		
		       connection.setDoOutput(true);
//					response.setContentType(“application/json”);
		       
		       try (DataOutputStream wr = new DataOutputStream (

		    		      connection.getOutputStream())) {
		    		  wr.writeBytes(urlParameters);
		    		}
		       
		       
		       int responseCode = connection.getResponseCode();

		        //System.out.println(responseCode+"\tSuccess");

		        InputStream is;

		        if(responseCode == HttpURLConnection.HTTP_OK){

		        is = connection.getInputStream();

		        }else {

		        is = connection.getErrorStream();
		        finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", "Order id doesn't exist");
				finalResponse.put("status", "Falilure");
				return finalResponse.toString();

		        }
		              
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			/*} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			
			try{*/
			JSONObject j = new JSONObject(response.toString());
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS") || j.getString("message").equalsIgnoreCase("Already released")){
			JSONObject modelResponseJson = j.getJSONObject("model");
			
			
				responseJson.put("releaseRefTransactionId", modelResponseJson.getLong("transactionId"));
				
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
				
				
				try{
					/*******paymentTransaction For Save payment details*********/
						paymentTransaction.setStatus(j.getString("status"));
						paymentTransaction.setStatusCode(j.getString("status"));
						paymentTransaction.setStatusMsg(j.getString("message"));
						paymentTransaction.setOrderId(releaseAmountRequest.getOrderId());
		
						paymentTransaction.setPspTransactionId(String.valueOf(modelResponseJson.getLong("transactionId")));
						paymentTransaction.setTransactionType("Release");
		
						paymentTransaction.setServiceType("Release");
						paymentTransaction.setStatusMsg(j.getString("message"));
						paymentTransaction.setPspId(releaseAmountRequest.getPspId());
						paymentTransaction.setDeviceId("505505");
						paymentTransaction.setMerchantId("");
						paymentTransaction.setTerminalId("");
						paymentTransaction.setAppId("");
						
						
						transactionController.updateVoidTransaction(paymentTransaction);
					} catch(Exception e){
						System.out.println("Error in Wallet Release Amount Save.. "+e);
					}
			} else {
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
				
				return finalResponse.toString();
			}
			System.out.println();
			eventLogRequest.setEventType("wallet Release Service Request");
			eventLogResponse.setEventType("wallet Release Service Response");
			eventLogResponse.setVersion(ResponseCodes.VERSION);
			eventLogRequest.setEventDetails(releaseAmountRequest.toString());
			eventLogResponse.setEventDetails(finalResponse.toString());
			
			eventLogRequest.setOrderID("");
			eventLogResponse.setOrderID("");
			
			eventLogRequest.setVersion("v2");
			
			eventDao.saveEventLog(eventLogRequest);
			eventDao.saveEventLog(eventLogResponse);
			} catch(Exception e){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			}
			return finalResponse.toString();
//		}
		
	}
}

