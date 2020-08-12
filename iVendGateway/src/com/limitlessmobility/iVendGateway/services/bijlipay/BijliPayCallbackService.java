package com.limitlessmobility.iVendGateway.services.bijlipay;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.model.bijlipay.BijliPayCallbackData;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.sun.istack.internal.logging.Logger;

	@Controller
	@RequestMapping("/payment/v1/transactions")
	public class BijliPayCallbackService {

		private static final Logger logger = Logger.getLogger(BijliPayCallbackService.class);

		@RequestMapping(value = "/bijlipay/initiate", method = RequestMethod.POST)
		@ResponseBody
		public String getBijliPayCallback(@RequestBody BijliPayCallbackData bijliPayCallbackData) throws JSONException {

			logger.info("Bijlipay Callback method is Callig..............");
			System.out.println("Bijlipay Callback method is Callig...............");

			bijliPayCallbackData.getTid();
			System.out.println("TID " + bijliPayCallbackData.getTid());
			bijliPayCallbackData.getAmount();
			System.out.println("AMOUNT " + bijliPayCallbackData.getAmount());
			bijliPayCallbackData.getRrn();
			System.out.println("RRN " + bijliPayCallbackData.getRrn());
			bijliPayCallbackData.getMessage();
			System.out.println("MESSAGES " + bijliPayCallbackData.getMessage());
			
	         
			
			Date date = new Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(date);
			
			EventLogs eventLogRequest=new EventLogs();
			
			JSONObject reqJson = new JSONObject(bijliPayCallbackData);

			PaymentTransaction paymentTransaction = new PaymentTransaction();
			JSONObject obj = new JSONObject(bijliPayCallbackData);
			// EventLogs eventLog = new EventLogs();
				try {
					paymentTransaction.setMerchantId("vendiman");
					paymentTransaction.setOrderId(obj.getString("rrn"));
					paymentTransaction.setPspTransactionId(obj.getString("rrn"));
					paymentTransaction.setMerchantOrderId(obj.getString("rrn"));
					paymentTransaction.setTerminalId(bijliPayCallbackData.getTid());
					paymentTransaction.setAuthAmount(obj.getDouble("amount"));
					//paymentTransaction.setAuthDate(String.valueOf(obj.getLong("time_stamp")));
					//paymentTransaction.setSettlementTime(String.valueOf(obj.getLong("time_stamp")));
					paymentTransaction.setStatus("success");
					paymentTransaction.setServiceType("Bijlipay Callback Response");
					paymentTransaction.setPspId("Bijlipay");
					paymentTransaction.setDeviceId("B-D1");
				 //	paymentTransaction.setAuthCode(reqJson.getString("auth_code"));
				//	paymentTransaction.setCustomerName(reqJson.getString("customer_name"));
			   //	paymentTransaction.setTransactionType(reqJson.getString("transaction_type"));
			  //	paymentTransaction.setPspMerchantId(reqJson.getString("mid"));
					TransactionDao transactionDao = new TransactionDaoImpl();
					transactionDao.saveTransaction(paymentTransaction);

				} catch (Exception e) {
					e.printStackTrace();

				}
				try {
					EventLogDao edao = new eventLogDaoImpl();

					eventLogRequest.setTerminalId(bijliPayCallbackData.getTid());
					eventLogRequest.setEventDetails(reqJson.toString());
					eventLogRequest.setEventType("BijliPay static CheckStatus API Request");
					eventLogRequest.setVersion("v1");
		            eventLogRequest.setEventDate(currentTime);
					edao.saveEventLog(eventLogRequest);
					
				} catch (Exception e) {
					System.out.println(e);
				}
			
			
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "null");
			jsonObject.put("status", "SUCCESS");

			JSONObject responseObj = new JSONObject();
			responseObj.put("txn_amount",  bijliPayCallbackData.getAmount());
			responseObj.put("statusMessage", "SUCCESS");
			responseObj.put("status", "SUCCESS");
			responseObj.put("statusCode", "SS_200");

			//jsonObject.put("responseObj", responseObj).toString();
			jsonObject.put("responseObj", responseObj);

			System.out.println("RES   " + jsonObject.toString());

			return jsonObject.toString();
			
			//return reqJson.toString();
		}
		
		

	}


