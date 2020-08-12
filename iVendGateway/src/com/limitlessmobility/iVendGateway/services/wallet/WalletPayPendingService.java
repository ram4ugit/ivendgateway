package com.limitlessmobility.iVendGateway.services.wallet;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseCodes;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.BlockRequest;
import com.limitlessmobility.iVendGateway.model.common.PayPendingRequest;
import com.limitlessmobility.iVendGateway.model.wallet.PayFromCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletBlockRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletPayPandingRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

/**
 * @author RamN
 */
@Controller
@RequestMapping("/v2/wallet")
public class WalletPayPendingService {

	@Autowired
	private HttpServletRequest request;
	
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	
//	OperatorDetail operatorDetail = new OperatorDetail();
	
	/*
	 * This Method is used to Pay pending wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/payPendingWalletAmount", method = RequestMethod.POST)
	@ResponseBody
	public String payPendingWalletAmount(@RequestBody PayPendingRequest payPendingRequest, int operatorId) throws JSONException{
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse = new JSONObject();
		
		PaymentController paymentController = new PaymentController();
		boolean isExistOrderId = paymentController.getTransactionExistByOrder(payPendingRequest.getOrderId());
		if(!isExistOrderId){
			finalResponse.put("responseObj", "");
			finalResponse.put("message", "Order id doesn't exist");
			finalResponse.put("status", "Failure");
			return finalResponse.toString();
		} 
		
		PaymentTransaction p = paymentController.getTransaction(payPendingRequest.getOrderId());
		payPendingRequest.setTransactionRefNo(p.getPspTransactionId().trim());
		

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		
		String walletId=payPendingRequest.getWalletId();
		String walletKey=payPendingRequest.getWalletKey();
		String walletAccountId=payPendingRequest.getWalletAccountId();
		String walletAccountKey=payPendingRequest.getWalletAccountKey();
		
		StringBuilder response = new StringBuilder();
		TokenAuthModel t = new TokenAuthModel();
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
//			int operatorId = Integer.parseInt(t.getOperatorId());
			
//			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, blockRequest.getPspId());
			
			WalletPayPandingRequest walletPayPandingRequest = new WalletPayPandingRequest();

			walletPayPandingRequest.setAmount(payPendingRequest.getAmount());
			walletPayPandingRequest.setWalletAccountId(walletAccountId);
			walletPayPandingRequest.setReferenceNo(payPendingRequest.getTransactionRefNo().trim());
			walletPayPandingRequest.setSource(payPendingRequest.getSource());
			
			
			
			/*walletPayPandingRequest.setAmount(payFromCardRequest.getAmount());
			walletPayPandingRequest.setReferenceNo(payFromCardRequest.getReferenceNo());
			walletPayPandingRequest.setWalletAccountId(payFromCardRequest.getWalletAccountId());
			walletPayPandingRequest.setSource(payFromCardRequest.getSource());*/
			
			
			
			
			
			
			String urlParameters=walletPayPandingRequest.toString();
			//System.out.println("urlParameters.."+urlParameters);
			
			
			
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/Transaction/pay");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",walletId);

				connection.setRequestProperty("WalletKey",walletKey);
				
//				connection.setRequestProperty("AccountKey", walletAccountKey);
				
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

		        }
		              
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			finalResponse = new JSONObject();
			try{
			JSONObject j = new JSONObject(response.toString());
			JSONObject responseJson = new JSONObject();
			PaymentTransaction paymentTransaction = new PaymentTransaction();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				
//			JSONObject modelResponseJson = j.getJSONObject("model");
			
			
			responseJson.put("amount", payPendingRequest.getAmount());
			responseJson.put("TransId", payPendingRequest.getTransactionRefNo());
			responseJson.put("orderId", payPendingRequest.getOrderId());
			
			finalResponse.put("responseObj", "");
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
			
			
			/*******paymentTransaction For Save payment details*********/
			paymentTransaction.setStatus(j.getString("status"));
			paymentTransaction.setStatusCode(j.getString("status"));
			paymentTransaction.setStatusMsg(j.getString("message"));

			paymentTransaction.setSettlementAmount("0");
			paymentTransaction.setOrderId(payPendingRequest.getOrderId());
			paymentTransaction.setMerchantOrderId(payPendingRequest.getOrderId());
			paymentTransaction.setPspTransactionId(payPendingRequest.getTransactionRefNo());
			paymentTransaction.setTransactionType("pay");

			paymentTransaction.setServiceType("pay");
			paymentTransaction.setStatusMsg(j.getString("message"));
			paymentTransaction.setPspId(payPendingRequest.getPspId());
			paymentTransaction.setDeviceId("505505");
			paymentTransaction.setMerchantId("");
			paymentTransaction.setTerminalId("");
			paymentTransaction.setAppId("");
			paymentTransaction.setPaidAmount(payPendingRequest.getAmount());
			
			
			paymentController.payPendingSaveTransaction(paymentTransaction);
			
			
		} else {
			finalResponse.put("responseObj", responseJson);
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
		} 
			System.out.println();
			eventLogResponse.setEventType("wallet Pay Service Request");
			eventLogResponse.setVersion(ResponseCodes.VERSION);
			eventLogRequest.setEventDetails(payPendingRequest.toString());
			eventLogResponse.setEventDetails(finalResponse.toString());
			
			eventLogRequest.setOrderID("");
			eventLogResponse.setOrderID("");
			
			eventLogRequest.setEventType("wallet Pay Service Response");
			eventLogRequest.setVersion("v1");
			
			eventDao.saveEventLog(eventLogRequest);
			eventDao.saveEventLog(eventLogResponse);
			} catch(Exception e){
				
			}
			return finalResponse.toString();
//		}
		
	}
	
	/*@RequestMapping(value = "/payPendingWalletAmount", method = RequestMethod.POST)
	@ResponseBody*/
	public String payAmountFromCard(PayFromCardRequest payFromCardRequest, String terminalID) throws JSONException{
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse = new JSONObject();
		
		PaymentController paymentController = new PaymentController();
		
		
		

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		int walletId=payFromCardRequest.getWalletId();
		String walletKey=payFromCardRequest.getWalletKey();
		String walletAccountId = payFromCardRequest.getWalletAccountId();
		
		StringBuilder response = new StringBuilder();
			
//			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, blockRequest.getPspId());
			
			WalletPayPandingRequest walletPayPandingRequest = new WalletPayPandingRequest();

			walletPayPandingRequest.setAmount(payFromCardRequest.getAmount());
			walletPayPandingRequest.setReferenceNo(payFromCardRequest.getReferenceNo());
			walletPayPandingRequest.setWalletAccountId(payFromCardRequest.getWalletAccountId());
			walletPayPandingRequest.setSource(payFromCardRequest.getSource());
			
			String urlParameters=walletPayPandingRequest.toString();
			
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/Transaction/pay");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",String.valueOf(walletId));

				connection.setRequestProperty("WalletKey",walletKey);
				
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

		        }
		              
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			finalResponse = new JSONObject();
			try{
			JSONObject j = new JSONObject(response.toString());
			JSONObject responseJson = new JSONObject();
			PaymentTransaction paymentTransaction = new PaymentTransaction();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				
			JSONObject modelResponseJson = j.getJSONObject("model");
			
			
			responseJson.put("amount", payFromCardRequest.getAmount());
			responseJson.put("TransId", payFromCardRequest.getOrderId());
			responseJson.put("orderId", payFromCardRequest.getOrderId());
			
			finalResponse.put("responseObj", "");
			finalResponse.put("referenceNo", modelResponseJson.getString("referenceNo"));
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
			
			
			/*******paymentTransaction For Save payment details*********/
			paymentTransaction.setStatus(j.getString("status"));
			paymentTransaction.setStatusCode(j.getString("status"));
			paymentTransaction.setStatusMsg(j.getString("message"));

			paymentTransaction.setSettlementAmount("0");
			paymentTransaction.setOrderId(payFromCardRequest.getOrderId());
			paymentTransaction.setMerchantOrderId(payFromCardRequest.getOrderId());
			paymentTransaction.setPspTransactionId(modelResponseJson.getString("referenceNo"));
			paymentTransaction.setTransactionType("pay");

			paymentTransaction.setServiceType("pay");
			paymentTransaction.setStatusMsg(j.getString("message"));
			paymentTransaction.setPspId(String.valueOf(payFromCardRequest.getWalletId()));
			paymentTransaction.setDeviceId("505505");
			paymentTransaction.setMerchantId("");
			paymentTransaction.setTerminalId(terminalID);
			paymentTransaction.setAppId("");
			paymentTransaction.setPaidAmount(payFromCardRequest.getAmount());
			
			
			paymentController.payPendingSaveTransaction(paymentTransaction);
			
			
		} else {
			finalResponse.put("responseObj", responseJson);
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
		} 
			System.out.println();
			eventLogResponse.setEventType("wallet Pay Service Request");
			eventLogResponse.setVersion(ResponseCodes.VERSION);
			eventLogRequest.setEventDetails(payFromCardRequest.toString());
			eventLogResponse.setEventDetails(finalResponse.toString());
			
			eventLogRequest.setOrderID("");
			eventLogResponse.setOrderID("");
			
			eventLogRequest.setEventType("wallet Pay Service Response");
			eventLogRequest.setVersion("v1");
			
			eventDao.saveEventLog(eventLogRequest);
			eventDao.saveEventLog(eventLogResponse);
			} catch(Exception e){
				
			}
			return finalResponse.toString();
		}
}
