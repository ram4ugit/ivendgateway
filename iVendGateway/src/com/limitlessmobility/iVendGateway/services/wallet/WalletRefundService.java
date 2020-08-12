package com.limitlessmobility.iVendGateway.services.wallet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.wallet.CardRefundRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;

@Controller
@RequestMapping("/v2/wallet")
public class WalletRefundService {

	@RequestMapping(value = "/manualRefund", method = RequestMethod.POST)
	@ResponseBody
	public String walletManualRefund(@RequestBody ManualRefundRequest manualRefundRequest) throws Exception {
		System.out.println("Calling walletManualRefund...START  "+manualRefundRequest.toString());
		JSONObject finalResponse = new JSONObject();
		
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		//This object is used for save request event log in 'event_logs' table
		EventLogs eventLogRequest = new EventLogs();	
		eventLogRequest.setEventDetails(manualRefundRequest.toString());
		
		//eventLogResponse is used for save response logs in 'event_logs' table
		EventLogs eventLogResponse = new EventLogs();	

		WalletRefundRequest walletRefundRequest = new WalletRefundRequest();
		
		String line = "";
		StringBuilder response = new StringBuilder();
		String walletId = "";
		String walletKey="";
		String pspId="";
		String terminalId="";
		try {
//			PspMerchant pspMerchant = new PspMerchant();    
			OperatorDetail operatorDetail = new OperatorDetail();
			if(manualRefundRequest.getPosId()!=null ) {
				terminalId= manualRefundRequest.getPosId();
				if(manualRefundRequest.getPspId()!=null) {
					pspId=manualRefundRequest.getPspId();
					 
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
					
					
//					pspMerchant = cdao.getPspMerchant(pspId, terminalId);
					walletId = operatorDetail.getMerchantId();
					walletKey = operatorDetail.getMerchantKey();
				}
			}
	        walletRefundRequest.setRefTransactionId(Long.parseLong(manualRefundRequest.getPspTransactionId()));
	        walletRefundRequest.setAmount(manualRefundRequest.getAmount());
	        walletRefundRequest.setSource(manualRefundRequest.getSource());
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String apiURL=Util.apiUrlReader();
			String urlParameters=walletRefundRequest.toString().trim();
			URL url = new URL(apiURL+"api/Wallet/Transaction/refund");
			System.out.println(url);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("WalletId",walletId);
			connection.setRequestProperty("WalletKey",walletKey);
			
			System.out.println("WID  "+walletId+" - "+walletKey);
			
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);
			System.out.println("Request... "+urlParameters);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);
	       
	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
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
	              System.out.println("RESPONSE From wallet..."+response.toString());
	              System.out.append("\r");
	              }
	              rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in walletRefundManual class.. "+e);
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(response.toString());
		try{
			JSONObject responseJson = new JSONObject();
			String responseStatus=j.getString("status").toString();
			if(responseStatus.equalsIgnoreCase("Success")){
				/*
				 * Save data in Refund table
				 */
				responseJson.put("status", HttpStatusModal.OK);
				responseJson.put("statusMessage", HttpStatusModal.OK);
				
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", HttpStatusModal.OK);
			} else {
				finalResponse.put("responseObj", responseJson);
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
			try {
				
				EventLogDao edao = new eventLogDaoImpl();
				eventLogResponse.setTerminalId(terminalId);
				eventLogRequest.setTerminalId(terminalId);
				eventLogResponse.setEventDetails(j.toString());
				eventLogResponse.setEventType("Wallet Refund APIResponse");
				eventLogRequest.setEventType("Wallet Refund APIRequest");
				eventLogResponse.setVersion("v2");
				eventLogRequest.setVersion("v2");
				eventLogRequest.setOrderID(manualRefundRequest.getTransactionId());
				eventLogResponse.setOrderID(manualRefundRequest.getTransactionId());
				edao.saveEventLog(eventLogRequest);
				edao.saveEventLog(eventLogResponse);
			} catch (Exception e) {
				System.out.println(e);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return finalResponse.toString();
	}

	public String walletRefund(int walletAccountId, int transactionId, String remark, int pspId, int operatorId) throws Exception {
		JSONObject finalResponse = new JSONObject();
		
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		//This object is used for save request event log in 'event_logs' table
		EventLogs eventLogRequest = new EventLogs();	
		
		//eventLogResponse is used for save response logs in 'event_logs' table
		EventLogs eventLogResponse = new EventLogs();	

		CardRefundRequest cardRefundRequest = new CardRefundRequest();
		
		String line = "";
		StringBuilder response = new StringBuilder();
		String walletId = "";
		String walletKey="";
		String terminalId="";
		try {
//			PspMerchant pspMerchant = new PspMerchant();    
			OperatorDetail operatorDetail = new OperatorDetail();
					 
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, String.valueOf(pspId));
					
					
//					pspMerchant = cdao.getPspMerchant(pspId, terminalId);
					walletId = operatorDetail.getMerchantId();
					walletKey = operatorDetail.getMerchantKey();
					
					
					cardRefundRequest.setTransactionId(transactionId);
					cardRefundRequest.setRemark(remark);
					cardRefundRequest.setWalletAccountId(walletAccountId);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String apiURL=Util.apiUrlReader();
			String urlParameters=cardRefundRequest.toString().trim();
			URL url = new URL(apiURL+"api/Wallet/Transaction/refund?version=2.0");
			System.out.println(url);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			connection.setRequestProperty("WalletId",walletId);
			connection.setRequestProperty("WalletKey",walletKey);
			
			System.out.println("WID  "+walletId+" - "+walletKey);
			
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);
			System.out.println("Request... "+urlParameters);
	
	       connection.setDoOutput(true);
//				response.setContentType(“application/json”);
	       
	       try (DataOutputStream wr = new DataOutputStream (

	    		      connection.getOutputStream())) {
	    		  wr.writeBytes(urlParameters);
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
	              System.out.println("RESPONSE From wallet..."+response.toString());
	              System.out.append("\r");
	              }
	              rd.close(); is.close();     
		} catch (IOException e) {
			System.out.println("Exception in walletRefundManual class.. "+e);
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(response.toString());
		try{
			JSONObject responseJson = new JSONObject();
			String responseStatus=j.getString("status").toString();
			if(responseStatus.equalsIgnoreCase("Success")){
				return"success";
			} else {
				return"failure";
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return "failure";
	}
}
