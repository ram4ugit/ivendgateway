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
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
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
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.wallet.WalletBlockRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

/**
 * @author RamN
 * 
 */
@Controller
@RequestMapping("/v2/wallet")
public class WalletBlockService {

	@Autowired
	private HttpServletRequest request;
	
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	
//	OperatorDetail operatorDetail = new OperatorDetail();
	
	/*
	 * This Method is used to block wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/blockWalletAmount", method = RequestMethod.POST)
	@ResponseBody
	public String blockWalletAmount(@RequestBody BlockRequest blockRequest, int operatorId) throws JSONException{
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse = new JSONObject();
		
		PaymentController paymentController = new PaymentController();
		boolean isExistOrderId = paymentController.getTransactionExistByOrder(blockRequest.getOrderId());
		if(isExistOrderId){
			finalResponse.put("responseObj", "");
			finalResponse.put("message", "Order id already exist");
			finalResponse.put("status", "Failure");
			return finalResponse.toString();
		} 
		
		
		

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		
		String walletId=blockRequest.getWalletId();
		String walletKey=blockRequest.getWalletKey();
		String walletAccountId=blockRequest.getWalletAccountId();
		String walletAccountKey=blockRequest.getWalletAccountKey();
		
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
			
			WalletBlockRequest walletBlockRequest = new WalletBlockRequest();

			walletBlockRequest.setOrderNo(blockRequest.getOrderId());
			walletBlockRequest.setAmount(Double.parseDouble(blockRequest.getAmount()));
			walletBlockRequest.setWalletAccountId(walletAccountId);
			walletBlockRequest.setSource(blockRequest.getSource());
			
			
			String urlParameters=walletBlockRequest.toString();
			//System.out.println("urlParameters.."+urlParameters);
			
			
			
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/wallet/transaction/block");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",walletId);

				connection.setRequestProperty("WalletKey",walletKey);
				
				connection.setRequestProperty("AccountKey", walletAccountKey);
				
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
			/*} catch (IOException e) {
				System.out.println("Exception in WalletBlock class.."+e);
				e.printStackTrace();
			}*/
			finalResponse = new JSONObject();
//			try{
			JSONObject j = new JSONObject(response.toString());
			JSONObject responseJson = new JSONObject();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				
			JSONObject modelResponseJson = j.getJSONObject("model");
			
			
			responseJson.put("amount", blockRequest.getAmount());
			responseJson.put("TransId", modelResponseJson.getDouble("transactionId"));
			responseJson.put("orderId", blockRequest.getOrderId());
			responseJson.put("userWalletBalance", "");
			
			finalResponse.put("responseObj", responseJson);
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
			
			
			/*******paymentTransaction For Save payment details*********/
			PaymentTransaction paymentTransaction = new PaymentTransaction();
			
			paymentTransaction.setStatus(j.getString("status"));
			paymentTransaction.setStatusCode(j.getString("status"));
			paymentTransaction.setStatusMsg(j.getString("message"));

			paymentTransaction.setAuthAmount(Double.parseDouble(blockRequest.getAmount()));
			paymentTransaction.setSettlementAmount("0");
			paymentTransaction.setOrderId(blockRequest.getOrderId());
			paymentTransaction.setMerchantOrderId(blockRequest.getOrderId());
			paymentTransaction.setPspTransactionId(String.valueOf(modelResponseJson.getLong("transactionId")));
			paymentTransaction.setTransactionType("block");

			paymentTransaction.setServiceType("0");
			paymentTransaction.setPspId(blockRequest.getPspId());
			paymentTransaction.setDeviceId("505505");
			paymentTransaction.setMerchantId("");
			paymentTransaction.setTerminalId("");
			paymentTransaction.setAppId("");
			
			PaymentController transactionController = new PaymentController();
			transactionController.saveTransaction(paymentTransaction);
			
			
		} else {
			finalResponse.put("responseObj", responseJson);
			finalResponse.put("message", j.getString("message"));
			finalResponse.put("status", j.getString("status"));
		} 
			try{
				System.out.println();
				eventLogResponse.setEventType("wallet Block Reponse");
				eventLogRequest.setEventType("wallet Block Request");
				eventLogResponse.setVersion(ResponseCodes.VERSION);
				eventLogRequest.setEventDetails(blockRequest.toString());
				eventLogResponse.setEventDetails(finalResponse.toString());
				
				eventLogRequest.setOrderID("");
				eventLogResponse.setOrderID("");
				
				eventLogRequest.setVersion("v2");
				
				eventDao.saveEventLog(eventLogRequest);
				eventDao.saveEventLog(eventLogResponse);
			} catch (Exception e){
				System.out.println("Error in SaveEventLog WalletRelease... "+e);
			}
			} catch(Exception e){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			}
			System.out.println("Calling wallet block...END");
			return finalResponse.toString();
//		}
		
	}
}
