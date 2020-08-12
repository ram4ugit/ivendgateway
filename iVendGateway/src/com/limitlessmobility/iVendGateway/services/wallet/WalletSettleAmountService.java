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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.MySqlDate;
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
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.common.SettleAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletBlockRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletSettleAmountRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

/**
 * @author RamN
 * @version 1.0 This class is used for check status of PayTM
 */
@Controller
@RequestMapping("/v2/wallet")
public class WalletSettleAmountService {

	@Autowired
	private HttpServletRequest request;
	
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	
//	OperatorDetail operatorDetail = new OperatorDetail();
	
	/*
	 * This Method is used to settle wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/settleWalletAmount", method = RequestMethod.POST)
	@ResponseBody
	public String settleWalletAmount(@RequestBody SettleAmountRequest settleAmountRequest) throws JSONException{
		
		JSONObject finalResponse = new JSONObject();
		PaymentController transactionController = new PaymentController();
		boolean isExistOrderId = transactionController.getTransactionExistByOrder(settleAmountRequest.getOrderId());
		if(!isExistOrderId){
			finalResponse.put("responseObj", "");
			finalResponse.put("message", "Order id not generated");
			finalResponse.put("status", "Failure");
			return finalResponse.toString();
		}
		PaymentTransaction p = transactionController.getTransaction(settleAmountRequest.getOrderId());
		settleAmountRequest.setTransactionRefNo(p.getPspTransactionId().trim());
		
		
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject responseJson = new JSONObject();
		PaymentTransaction paymentTransaction = new PaymentTransaction();

		EventLogs eventLogRequest = new EventLogs();
		EventLogs eventLogResponse = new EventLogs();
		String line = "";
		
		String walletId=settleAmountRequest.getWalletId();
		String walletKey=settleAmountRequest.getWalletKey();
		Integer walletAccountId=Integer.parseInt(settleAmountRequest.getWalletAccountId());
		String walletAccountKey=settleAmountRequest.getWalletAccountKey();
		
		StringBuilder response = new StringBuilder();
		/*TokenAuthModel t = new TokenAuthModel();
		try{
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
//		if(CommonValidationUtility.isEmpty(t.getOperatorId())){
//			System.out.println("token authentication failed!!");
//			responseDTO.setErrors(errors);
//			responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
//			responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
//			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
//			return ResponseUtility.sendResponseObj(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
//		} else {
//			int operatorId = Integer.parseInt(t.getOperatorId());
			
//			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, "wallet");
			
			WalletSettleAmountRequest walletSettleAmountRequest = new WalletSettleAmountRequest();

			walletSettleAmountRequest.setWalletAccountId(walletAccountId);
			walletSettleAmountRequest.setTransactionRefNo(settleAmountRequest.getTransactionRefNo().trim());
			walletSettleAmountRequest.setAmount(settleAmountRequest.getAmount());
			walletSettleAmountRequest.setSource(settleAmountRequest.getSource());
			
			String urlParameters=walletSettleAmountRequest.toString();
			EventLogDao eventDao = new eventLogDaoImpl();
			
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/wallet/transaction/settle?version=2.0");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				
				connection.setRequestProperty("WalletId",walletId);

				connection.setRequestProperty("WalletKey",walletKey);
				
//				connection.setRequestProperty("api-version", "2.0");
				
				
				
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
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			
			try{*/
			JSONObject j = new JSONObject(response.toString());
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			JSONObject modelResponseJson = j.getJSONObject("model");
			
				
			
				responseJson.put("usedBalance", modelResponseJson.getDouble("usedBalance"));
				responseJson.put("pendingBalance", modelResponseJson.getDouble("pendingBalance"));
				responseJson.put("status", j.getString("status"));
				responseJson.put("transactionRefNo", settleAmountRequest.getTransactionRefNo());
				responseJson.put("orderId", settleAmountRequest.getOrderId());
				
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
				try{
				/*******paymentTransaction For Save payment details*********/
					paymentTransaction.setStatus(j.getString("status"));
					paymentTransaction.setStatusCode(j.getString("status"));
					paymentTransaction.setStatusMsg(j.getString("message"));
	
					paymentTransaction.setPaidAmount(modelResponseJson.getDouble("usedBalance"));
					paymentTransaction.setSettlementAmount(String.valueOf(modelResponseJson.getDouble("pendingBalance")));
					paymentTransaction.setOrderId(settleAmountRequest.getOrderId());
					paymentTransaction.setMerchantOrderId(settleAmountRequest.getOrderId());
					paymentTransaction.setPspTransactionId(settleAmountRequest.getTransactionRefNo());
					paymentTransaction.setTransactionType("settle");
	
					paymentTransaction.setServiceType("settle");
					paymentTransaction.setStatusMsg(j.getString("message"));
					paymentTransaction.setPspId(settleAmountRequest.getPspId());
					paymentTransaction.setDeviceId("505505");
					paymentTransaction.setMerchantId("");
					paymentTransaction.setTerminalId("");
					paymentTransaction.setAppId("");
					
					
					transactionController.updateTransaction(paymentTransaction);
				} catch(Exception e){
					System.out.println("Error in Wallet Settle Amount Save.. "+e);
				}
			} else {
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", j.getString("status"));
			}
			System.out.println();
			try{
				eventLogResponse.setEventType("wallet Settlement Service Response");
				eventLogResponse.setVersion(ResponseCodes.VERSION);
				eventLogRequest.setEventDetails(settleAmountRequest.toString());
				eventLogResponse.setEventDetails(finalResponse.toString());
				
				eventLogRequest.setOrderID("");
				eventLogResponse.setOrderID("");
				
				eventLogRequest.setEventType("wallet Settlement Service Request");
				eventLogRequest.setVersion("v2");
				
				eventDao.saveEventLog(eventLogRequest);
				eventDao.saveEventLog(eventLogResponse);
			} catch(Exception e){
				System.out.println("Error in saveEventLog WalletSettle  "+e);
			}
			} catch(Exception e){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", "Failure");
			}
			return finalResponse.toString();
//		}
		
	}
}
