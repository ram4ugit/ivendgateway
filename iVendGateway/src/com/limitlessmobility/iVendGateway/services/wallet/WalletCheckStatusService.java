package com.limitlessmobility.iVendGateway.services.wallet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.google.gson.Gson;
import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobil.ivendgateway.util.ResponseCodes;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusReponse;
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.wallet.WalletCheckStatusRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletQRRequestModel;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.paytm.pg.merchant.CheckSumServiceHelper;


/**
 * @author RamNarayan
 * @version 1.0 This class is used for check status of PayTM
 */
@Controller
@RequestMapping("/v2/wallet")
public class WalletCheckStatusService {

	/*
	 * This Method is used to check status using transactions using transaction
	 * ID.
	 */
	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String walletCheckStatus(@RequestBody CommonCheckStatusRequest commonCheckStatusRequest) throws Exception {
		System.out.println("Calling PayTM Dynamic QR (createQRCode Method)...START");
		JSONObject finalResponsee = new JSONObject();
		
		
		
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		//This object is used for save request event log in 'event_logs' table
		EventLogs eventLogRequest = new EventLogs();	
		eventLogRequest.setEventDetails(commonCheckStatusRequest.toString());
		
		//eventLogResponse is used for save response logs in 'event_logs' table
		EventLogs eventLogResponse = new EventLogs();	

		WalletCheckStatusRequest walletCheckStatusRequest = new WalletCheckStatusRequest();
		
		String line = "";
		StringBuilder response = new StringBuilder();
		String walletId = "";
		String walletKey="";
		String pspId="";
		String terminalId="";
		try {
//			PspMerchant pspMerchant = new PspMerchant();
			OperatorDetail operatorDetail = new OperatorDetail();
			if(commonCheckStatusRequest.getPosId()!=null ) {
				terminalId= commonCheckStatusRequest.getPosId();
				if(commonCheckStatusRequest.getPspId()!=null) {
					pspId=commonCheckStatusRequest.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, pspId);
					
					
//					pspMerchant = cdao.getPspMerchant(pspId, terminalId);
					walletId = operatorDetail.getMerchantId();
					walletKey = operatorDetail.getMerchantKey();
//					walletAccountId = commonCheckStatusRequest.getWalletAccountId();
//					walletAccountKey = commonCheckStatusRequest.getWalletAccountKey();
				}
			}

	        walletCheckStatusRequest.setReferenceNo(commonCheckStatusRequest.getTxnId());
//	        walletCheckStatusRequest.setWalletAccountId(commonCheckStatusRequest.getAmount());
	        
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String apiURL=Util.apiUrlReader();
			String urlParameters=walletCheckStatusRequest.toString();
			URL url = new URL(apiURL+"api/wallet/Transaction/CheckStatus");
			System.out.println(url);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("WalletId",walletId);
			connection.setRequestProperty("WalletKey",walletKey);
			
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);
	       
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
	              System.out.println("RESPONSE From wallet check status... "+response.toString());
	              System.out.append("\r");

	              }
	              rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(response.toString());
		System.out.println("Walet Check Status Response:- " +j);
		
		try{
			PaymentTransaction paymentTransaction = new PaymentTransaction();
			String responseStatus=j.get("status").toString();
//			JSONObject responseJson = new JSONObject();
			if(responseStatus.equalsIgnoreCase("Success")){
				
				
				/*responseJson.put("amount", commonCheckStatusRequest.getAmount());
				responseJson.put("status", "SUCCESS");
				responseJson.put("message", j.get("message"));
				
				
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.get("message"));
				finalResponse.put("status", HttpStatusModal.OK);*/
				
				
				finalResponsee.put("statusMessage", j.get("message"));
				finalResponsee.put("status", HttpStatusModal.OK);
				finalResponsee.put("statusCode", "200");
				JSONObject resModel = j.getJSONObject("model");
				finalResponsee.put("amount", resModel.get("amount"));
				finalResponsee.put("walletAccountId", resModel.get("walletAccountId"));

				
				
				paymentTransaction.setStatus(responseStatus);
				paymentTransaction.setStatusCode(responseStatus);
				paymentTransaction.setStatusMsg(responseStatus);

				
				
				JSONObject modelResponseJson = j.getJSONObject("model");
				
				/******Saving Payment transaction*********/
				
				paymentTransaction.setOrderId(modelResponseJson.getString("referenceNo"));
				paymentTransaction.setMerchantOrderId(modelResponseJson.getString("referenceNo"));
				paymentTransaction.setPspTransactionId(modelResponseJson.getString("transactionRefNo"));
				paymentTransaction.setAuthAmount(modelResponseJson.getDouble("amount"));
				paymentTransaction.setPaidAmount(modelResponseJson.getDouble("amount"));
				paymentTransaction.setTransactionType("0");

				paymentTransaction.setServiceType("CHECK_TXN_STATUS");
				paymentTransaction.setPspId(pspId);
				paymentTransaction.setDeviceId("");
				paymentTransaction.setMerchantId("");
				paymentTransaction.setTerminalId(terminalId);
				paymentTransaction.setAppId(commonCheckStatusRequest.getAppId());


				PaymentController transactionController = new PaymentController();
				transactionController.saveTransaction(paymentTransaction);
			
			} else{
//				finalResponse.put("responseObj", responseJson);
//				finalResponse.put("message", j.getString("message"));
//				finalResponse.put("status", j.getString("status"));
				
				finalResponsee.put("statusMessage", j.get("message"));
				finalResponsee.put("status", HttpStatusModal.ERROR);
				finalResponsee.put("statusCode", "422");
				finalResponsee.put("amount", commonCheckStatusRequest.getAmount());
			}
			try {
				EventLogDao edao = new eventLogDaoImpl();

				eventLogResponse.setTerminalId(paymentTransaction.getTerminalId());
				eventLogRequest.setTerminalId(paymentTransaction.getTerminalId());
				eventLogResponse.setEventDetails(j.toString());
				eventLogResponse.setEventType("Wallet CheckStatus APIResponse");
				eventLogRequest.setEventType("Wallet CheckStatus APIRequest");
				eventLogResponse.setVersion("v1");
				eventLogRequest.setVersion("v1");

				eventLogRequest.setOrderID(paymentTransaction.getOrderId());
				eventLogResponse.setOrderID(paymentTransaction.getOrderId());

				edao.saveEventLog(eventLogRequest);
				edao.saveEventLog(eventLogResponse);
			} catch (Exception e) {
				System.out.println(e);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		

		
		return finalResponsee.toString();
	}
}
