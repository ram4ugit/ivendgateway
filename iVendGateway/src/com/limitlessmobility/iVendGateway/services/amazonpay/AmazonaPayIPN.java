package com.limitlessmobility.iVendGateway.services.amazonpay;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayDao;
import com.limitlessmobility.iVendGateway.model.amazonpay.IPNRequestModel;
import com.limitlessmobility.iVendGateway.model.amazonpay.Message;
import com.limitlessmobility.iVendGateway.model.amazonpay.Player;
import com.limitlessmobility.iVendGateway.model.amazonpay.Status;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping("/payment/ipn/transactions/amazonpay")
public class AmazonaPayIPN {

	/*
	 * This API is used to store callback data of Amazonpay in Database. This API is called by Amazonpay.
	 */
	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getInstantNotification(@RequestBody String requestInfo)
	        throws JSONException {

		String response = "";
		String jsonFormattedString = "";
		String message = "";
		String respone2 = "";
		boolean isexist = true;
		String terminalId="";
		String deviceId="";

		String jsonFormattedString2 = "";

		EventLogs eventLogs = new EventLogs();
		AmazonPayDao amazonPayDao = new AmazonDaoImpl();
		Message message1= new Message();;
		Status status = new Status();;
		IPNRequestModel ipnRequestModel =new IPNRequestModel();;
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		TransactionDao transactionDao = new TransactionDaoImpl();

		System.out.println("AmazonPay Response" + requestInfo);

		Gson g = new Gson();

		Player p = g.fromJson(requestInfo, Player.class);

		response = g.toJson(p);

		System.out.println("Gson to String " + response);

		JSONObject obj = new JSONObject(response);

		jsonFormattedString = obj.toString();

		System.out.println("String to Json  " + jsonFormattedString);

		message = obj.getString("Message");
		message = message.trim();

		System.out.println("message " + message);

		Gson g1 = new Gson();

		Message m = g1.fromJson(message, Message.class);

		respone2 = g1.toJson(m);

		JSONObject obj1 = new JSONObject(respone2);

		jsonFormattedString2 = obj1.toString().trim();
		System.out.println("ToString " + jsonFormattedString2);

		try {
			if (obj1.has("Timestamp")) {
				ipnRequestModel.setTimestamp(obj1.getString("Timestamp"));
				System.out.println("Timestamp " + ipnRequestModel.getTimestamp());
			}
			 
			if (obj1.has("storeId")) {
				message1.setStoreId(obj1.getString("storeId"));
				System.out.println("storeId " + message1.getStoreId());
			}

			if (obj1.has("amazonOrderId")) {
				message1.setAmazonOrderId(obj1.getString("amazonOrderId"));
				System.out.println(
				        "amazonOrderId " + message1.getAmazonOrderId());
			}
			
			if (obj1.has("sellerOrderId")) {
				message1.setSellerOrderId(obj1.getString("sellerOrderId"));
				System.out.println(
				        "sellerOrderId " + message1.getSellerOrderId());
			}

			if (obj1.has("orderTotalAmount")) {
				message1.setOrderTotalAmount(
				        (float) obj1.getDouble("orderTotalAmount"));
				System.out.println(
				        "orderTotalAmount " + message1.getOrderTotalAmount());
			}

			if (obj1.has("transactionDate")) {
				message1.setTransactionDate(
				        (float) (obj1.getDouble("transactionDate")));
				System.out.println(
				        "transactionDate " + message1.getTransactionDate());
			}
			
			if (obj1.has("merchantId")) {
				message1.setMerchantId(obj1.getString("merchantId"));
				System.out.println(
				        "merchantId " + message1.getMerchantId());
			}

			JSONObject msg = (JSONObject) obj1.get("status");
			
			if (msg.has("status")) {
				status.setStatus(msg.getString("status"));
				System.out.println("status " + status.getStatus());
			}
			if (msg.has("reasonCode")) {
				status.setReasonCode(msg.getString("reasonCode"));
				System.out.println("reasonCode " + status.getReasonCode());
			}
			if (msg.has("description")) {
				status.setDescription(msg.getString("description"));
				System.out.println("description " + status.getDescription());
			}
		} catch (Exception b) {
			System.out.println("Error fek diya bhai");
		}
		
		
		try {
			if (!message1.getStoreId().isEmpty()) {
				terminalId=amazonPayDao.getTerminalIdByStroreId(message1.getStoreId());
				System.out.println("terminalId " + terminalId);
				deviceId=amazonPayDao.getDeviceIDByTerminalId(terminalId);
				System.out.println("deviceId " + deviceId);
				
			}
		} catch (Exception e) {
			System.out.println("Exception.. "+e);
		}
		

		/* Save to Transaction table............ */

		
		  try{ 
			  isexist =amazonPayDao.checkTxnId(message1.getSellerOrderId().trim()); 
		         if(isexist) {
		        	 System.out.println(message1.getAmazonOrderId()+" Already Exist Previously " );
		         } else {
				  paymentTransaction.setMerchantId("vendiman");
				  System.out.println("MerchantId "+paymentTransaction.getMerchantId());
				  paymentTransaction.setPspMerchantId(message1.getMerchantId());
				  System.out.println("PspMerchantId "+paymentTransaction.getPspMerchantId()); 
				  paymentTransaction.setOrderId(message1.getSellerOrderId().trim());//sellerOrderId
				  paymentTransaction.setMerchantOrderId(message1.getAmazonOrderId().trim());
				  System.out.println("MerchantOrderId "+paymentTransaction.getMerchantOrderId());
				  paymentTransaction.setAuthAmount(Double.valueOf(message1.getOrderTotalAmount()));
				  System.out.println("Amount "+paymentTransaction.getAuthAmount());
				  paymentTransaction.setTransactionType("0");
				  paymentTransaction.setStatus(status.getStatus());
				  paymentTransaction.setServiceType("CHECK_TXN_STATUS-IPN");
				  System.out.println("ServiceType "+paymentTransaction.getServiceType()); 
				  paymentTransaction.setPspId("amazonpay");
				  System.out.println("PspId "+paymentTransaction.getPspId()); //
				  paymentTransaction.setTransactionType(paymentTransaction.getTransactionType());
				  if (!deviceId.isEmpty()) {
					  paymentTransaction.setDeviceId(deviceId);
					  System.out.println("DeviceId "+paymentTransaction.getDeviceId());
				  }
				  if (!terminalId.isEmpty()) {
					  paymentTransaction.setTerminalId(terminalId);
					  System.out.println("TerminalId "+paymentTransaction.getTerminalId());
				  }
				  paymentTransaction.setMerchantId("zoneone");
				  paymentTransaction.setAuthDate(ipnRequestModel.getTimestamp());
				  System.out.println("AuthDate "+paymentTransaction.getAuthDate()); //
				  paymentTransaction.setPspTransactionId(message1.getSellerOrderId());
				  System.out.println("PspTransactionId "+paymentTransaction.getPspTransactionId());
				  
				  if (!message1.getStoreId().isEmpty()) {
					  paymentTransaction.setLocationId(message1.getStoreId());
					  System.out.println("StoreId "+paymentTransaction.getLocationId());
				  }
				  
				  
				  transactionDao.saveTransaction(paymentTransaction);
				  
				  } 
		         }catch (Exception pte) { 
		        	 System.out.println("Exception iss "+pte);
		         }
				 
		
		  try {
			  eventLogs.setEventDetails(requestInfo);
				EventLogDao dao=new eventLogDaoImpl();
				 dao.saveEventLog(eventLogs);
			} catch (Exception e) {
				System.out.println("Exception iss "+e);
			}
		  

		    JSONObject finalresponse = new JSONObject();
		    finalresponse.put("message", "null");
		    finalresponse.put("status", "SUCCESS");
		 
			JSONObject responseObj = new JSONObject();
			responseObj.put("txn_amount",  paymentTransaction.getAuthAmount());
			responseObj.put("statusMessage", "SUCCESS");
			responseObj.put("status", "SUCCESS");
			responseObj.put("statusCode", "SS_200");

			finalresponse.put("responseObj", responseObj);

			System.out.println("RES   " + finalresponse.toString());

			return finalresponse.toString();
	}

}
