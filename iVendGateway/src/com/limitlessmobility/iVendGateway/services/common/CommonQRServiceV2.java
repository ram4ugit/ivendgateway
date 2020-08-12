package com.limitlessmobility.iVendGateway.services.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JsonValidator;
import com.limitlessmobility.iVendGateway.controller.paymentmode.PaymentModeController;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.RefundInitialData;
import com.limitlessmobility.iVendGateway.model.common.CommonQRRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.model.phonepe.PPRefundInitialData;
import com.limitlessmobility.iVendGateway.services.amazonpay.AmazonPayRefundStatusV2;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCheckStatusV2;
import com.limitlessmobility.iVendGateway.services.phonepe.PhonePeRefundStatus;

import constants.KeyValueHolder;

@Controller
@RequestMapping(value="/v2")
public class CommonQRServiceV2 {
	
	PaymentModeController paymentModeController = new PaymentModeController();
//	private static final Logger logger = Logger.getLogger(CommonQRService.class);
	
	/*
	 * This API is used for get QR for all.
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String commonQRCode(@RequestBody CommonQRRequest commonQrResuest) throws Exception {
		System.out.println("CommonQRCode Api Start. Request:- "+commonQrResuest);
		JSONObject qrRequestJson = new JSONObject(commonQrResuest);
		Map<String, Object> errors = new HashMap<>();
		
		String pspId = qrRequestJson.getString("pspId");
		if(CommonValidationUtility.isEmpty(commonQrResuest.getPosId())) {
			errors.put(KeyValueHolder.POS_ID_KEY, KeyValueHolder.POS_ID_KEY_MSG);
		} 
		if(CommonValidationUtility.isEmpty(commonQrResuest.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		try{
			if(commonQrResuest.getAmount()<0.01) {
				errors.put(KeyValueHolder.AMOUNT, KeyValueHolder.AMOUNT_MSG);
			}
		} catch(Exception e){
			errors.put("Amount", "Please send correct amount");
		}
		
		if(CommonValidationUtility.isEmpty(errors)) {} else{
			JSONObject finalResponse = new JSONObject();
			finalResponse.put("responseObj", "");
			finalResponse.put("message", errors);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
			
		}
		PspDetailModal pspDetailModal = new PspDetailModal();
		pspDetailModal = paymentModeController.getPspDetailById(pspId);
		
		String qrRequest = qrRequestJson.toString();
		String line="";
		StringBuilder response=new StringBuilder();
		String uriV1 = pspDetailModal.getPspApiUrl();
		String uriV2 = uriV1.replace("v1","v2");  
		String uri = uriV2+"getqr";
//		String uri = "http://localhost:8080/iVendGateway/v2/paytm/getqr";

		try{
//		System.out.println("URL.."+uri);
		
			URL url = new URL(uri);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type","application/json");
	        connection.setRequestProperty("Content-Length", Integer.toString(qrRequest.getBytes().length));
	        connection.setUseCaches(false);
	        connection.setDoOutput(true);
       
       try (DataOutputStream wr = new DataOutputStream (
    		   	connection.getOutputStream())) {
    		  	wr.writeBytes(qrRequest);
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
            	  System.out.append("\r");
              }
              rd.close(); is.close();     
	} catch (IOException e) {
//		System.out.println("Exception in DynamicQRService class.."+e);
		System.out.println("Exception in DynamicQRService class.. "+e);
		e.printStackTrace();
	}
//		 System.out.println("RESPONSE..."+response.toString());
		JSONObject finalResponse = new JSONObject();
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
	
	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String commonCheckStatus(@RequestBody String checkStatusRequest) throws Exception {
		System.out.println("====commonCheckStatus===");
		JSONObject qrRequestJson = new JSONObject(checkStatusRequest);
		
		String pspId = qrRequestJson.getString("pspId");
		System.out.println("====commonCheckStatus..pspId==="+pspId);
		PspDetailModal pspDetailModal = new PspDetailModal();
			pspDetailModal = paymentModeController.getPspDetailById(pspId);
		
		String qrRequest = qrRequestJson.toString();
		String line="";
		StringBuilder response=new StringBuilder();
		String uriV1 = pspDetailModal.getPspApiUrl();
		String uriV2 = uriV1.replace("v1","v2");  
		String uri = uriV2+"checkStatus";
//		String uri ="http://localhost:8080/iVendGateway/v2/wallet/checkStatus";

		try{
			System.out.println("URL.."+uri);
		URL url = new URL(uri);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type","application/json");
		
        connection.setRequestProperty("Content-Length", Integer.toString(qrRequest.getBytes().length));
        connection.setUseCaches(false);

        connection.setDoOutput(true);
       
       try (DataOutputStream wr = new DataOutputStream (

    		      connection.getOutputStream())) {
    		  wr.writeBytes(qrRequest);
    		}
       
       
       int responseCode = connection.getResponseCode();

        //System.out.println(responseCode+"\tSuccess");

        InputStream is;

        if(responseCode == HttpURLConnection.HTTP_OK){

        is = connection.getInputStream();

        }else {

        is = connection.getErrorStream();

        }
              /*try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) { response = new StringBuilder(); // or StringBuffer if not Java 5+ String line = ""; while((line = rd.readLine()) != null) 
              {

           //System.out.println("line.."+line);
              response.append(line);

              response.append('\r');

          }           }*/ // or StringBuffer if not Java 5+
              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
              //System.out.append(line);
              //System.out.println("line.."+line);
              response.append(line);
              
//              System.out.println("RESPONSE..."+response.toString());
              System.out.append("\r");

              }
              rd.close(); is.close();     
	} catch (IOException e) {
		System.out.println("Exception in DynamicQRService class.."+e);
		e.printStackTrace();
	}
		JSONObject finalResponse = new JSONObject();
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		
//		System.out.println("Response Error"+response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			if (responseObj.has("walletAccountId")) {} else {
				responseObj.put("walletAccountId", "");
			}
			
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
			
			
			
			JSONObject checkEmptyJson = new JSONObject(response.toString());
			System.out.println(checkEmptyJson);
			if(checkEmptyJson.getString("status").toString().equalsIgnoreCase("FAILURE")){
				finalResponse.put("responseObj", "null");
				finalResponse.put("message", "Transaction Not Found");
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
			
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
	
	@RequestMapping(value = "/refundCheckStatus", method = RequestMethod.POST)
	@ResponseBody
	public String refundCheckStatus(@RequestBody String checkStatusRequest) throws Exception {
		System.out.println("====commonCheckStatus===");
		JSONObject responseToReturn = new JSONObject();
		JSONObject qrRequestJson = new JSONObject(checkStatusRequest);
		String responseStr = null;
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		PayTmCheckStatusV2 paytmCheckStatusService = new PayTmCheckStatusV2();
		OperatorDetail operatorDetail = new OperatorDetail();
		
		String pspId = qrRequestJson.getString("pspId");
	
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		
		if (pspId.contains("paytm")) {
			
			int operatorId = commonCredentialDao.getOperatorId(qrRequestJson.getString("posId"), pspId);
			
			operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
			
//			String merchantKey = operatorDetail.getMerchantKey();
			String merchantGuid = operatorDetail.getPspMguid();
			
			System.out.println("paytm refund Status......................................... " +pspId);
			try{ 		  
			String query = "SELECT * FROM payment_refund WHERE psp_id='PayTM' and merchant_order_id='"+qrRequestJson.getString("orderId")+"'";				
			System.out.println("EarlyRefundPaytm "+query);
			PreparedStatement ps = conn.prepareStatement(query);
			
		      java.sql.ResultSet rs = ps.executeQuery();
		      //STEP 5: Extract data from result set
		      if(rs.next()){
		    	  
			
			String req = "{\"request\":{\"requestType\":\"refundreftxnid\",\"txnType\":\"normalrefund\",\"merchantGuid\":\""+merchantGuid+"\",\"txnId\":\""+rs.getString("refund_ref_id")+"\"},\"platformName\":\"PayTM\",\"operationType\":\"CHECK_TXN_STATUS\"}";
			String response = paytmCheckStatusService.refundCheckStatus(req, qrRequestJson.getString("orderId"), operatorId);
			
			JSONObject responseJson = new JSONObject(response);
			responseToReturn.put("responseObj", "");
			responseToReturn.put("status", responseJson.getString("status"));
			responseToReturn.put("message", responseJson.getString("statusMessage"));
			
			
			return responseToReturn.toString();
		}
		} catch(Exception e) {
			System.out.println(e);
		}
		
	} else if (pspId.contains("amazonpay")) {
		AmazonPayRefundStatusV2 apRefundStatus = new AmazonPayRefundStatusV2();
		RefundInitialData refundInitialData = new RefundInitialData();
		
		refundInitialData.setAmount(qrRequestJson.getString("amount"));
		refundInitialData.setTxnId(qrRequestJson.getString("orderId"));
		
		String responseAmazonStr = apRefundStatus.getRefundStatus(refundInitialData);
		System.out.println(responseAmazonStr);
		JSONObject responseJsonAmazon = new JSONObject(responseAmazonStr);
		JSONObject jResponse = responseJsonAmazon.getJSONObject("response");
		if(jResponse.has("refundReferenceId")) {
			responseToReturn.put("responseObj", "");
			responseToReturn.put("status", jResponse.getString("status"));
			responseToReturn.put("message", "Refunded Successfully");
			return responseToReturn.toString();
		} else {
			responseToReturn.put("responseObj", "");
			responseToReturn.put("status", "Failure");
			if(jResponse.has("reasonDescription")){
				responseToReturn.put("message", jResponse.getString("reasonDescription"));
			} else {
				responseToReturn.put("message", "Not Refunded");
			}
			return responseToReturn.toString();
		}
	} else if (pspId.contains("phonepe")) {
		
		
		PhonePeRefundStatus phonePeRefundStatus = new PhonePeRefundStatus();
		PPRefundInitialData requestData = new PPRefundInitialData();
		requestData.setAmount(qrRequestJson.getString("amount"));
		requestData.setTransactionid(qrRequestJson.getString("orderId"));
		String responsePhonepeStr = phonePeRefundStatus.getPhonepeRefundStatus(requestData);
		
		
		JSONObject jResponse = new JSONObject(responsePhonepeStr);
		if(jResponse.has("status")) {
			if(jResponse.getString("status").equalsIgnoreCase("success")) {
			responseToReturn.put("responseObj", "");
			responseToReturn.put("status", "SUCCESS");
			responseToReturn.put("message", "Refunded Successfully");
			return responseToReturn.toString();
			} else {
				responseToReturn.put("responseObj", "");
				responseToReturn.put("status", "Initiated");
				responseToReturn.put("message", "Refund Initiated Successfully");
				return responseToReturn.toString();
			}
		} else {
			responseToReturn.put("responseObj", "");
			responseToReturn.put("status", "Failure");
			if(jResponse.has("reasonDescription")){
				responseToReturn.put("message", jResponse.getString("reasonDescription"));
			} else {
				responseToReturn.put("message", "Not Refunded");
			}
			return responseToReturn.toString();
		}
	}
		responseToReturn.put("responseObj", "");
		responseToReturn.put("status", "Failure");
		responseToReturn.put("message", "Invalid TransactionId");
		return  responseToReturn.toString();
	}
	
}
