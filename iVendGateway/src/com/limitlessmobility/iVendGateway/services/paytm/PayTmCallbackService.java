package com.limitlessmobility.iVendGateway.services.paytm;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.services.mqtt.MqttClientConnection;

@Controller
@RequestMapping("/payment/v1/transactions/paytm")
public class PayTmCallbackService {

	private static final Logger logger = Logger.getLogger(PayTmCallbackService.class);
	
	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getPayTmCallback(@RequestBody String paymentTransactionRequest) {
		
		logger.info("CallBack Method is calling!!");
		logger.info("CallBack Response..."+paymentTransactionRequest);
//		Get ip address, time, date
//		placeHolder to call for sevice call login
		System.out.println("MQTT CallBack Method is calling!!");
		System.out.println("MQTT CallBack resposne!!"+paymentTransactionRequest);
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLog = new EventLogs();
		
		try {
			JSONObject obj = new JSONObject(paymentTransactionRequest);
			
			JSONObject responseObj = obj.getJSONObject("response");
			
//			Long datetimeJson = responseObj.getLong("timestamp");
			Long datetimeJson = responseObj.getLong("timestamp");
			
			System.out.println("Callback Response datetime exact  "+datetimeJson);
	        
	        JSONObject responseJsonObj = obj.getJSONObject("response");
	        paymentTransaction.setAppId("8d4c832f-a5d1-43cf-b48e-0973b58e305d");
	        paymentTransaction.setOrderId(obj.getString("orderId"));
	        paymentTransaction.setMerchantOrderId(obj.getString("orderId"));
	        paymentTransaction.setStatus(obj.getString("status")); 
	        paymentTransaction.setStatusCode(obj.getString("statusCode"));
	        paymentTransaction.setStatusMsg(obj.getString("statusMessage"));
	        
	        paymentTransaction.setTerminalId(responseJsonObj.getString("posId"));
	        paymentTransaction.setAuthAmount(responseJsonObj.getDouble("txnAmount"));
//	        paymentTransaction.setAuthDate();
	        try {
	        	paymentTransaction.setComments(responseJsonObj.getString("comment"));
	        } catch(Exception e){
	        	paymentTransaction.setComments("");
	        }
	        paymentTransaction.setServiceType("callback");
	        paymentTransaction.setPspId("paytm");
	        paymentTransaction.setPspTransactionId(responseObj.getString("walletSystemTxnId"));
	        paymentTransaction.setDeviceId("d1");
	        paymentTransaction.setMerchantId("zoneone");
	        
			Timestamp dtStartDT = new Timestamp(datetimeJson);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			String finalDate = simpleDateFormat.format(dtStartDT);
			System.out.println("After Converted datetime "+finalDate);
			paymentTransaction.setAuthDate(finalDate);
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PaymentController transactionController = new PaymentController();
		transactionController.saveTransaction(paymentTransaction);
		
		try {
        	EventLogDao edao = new eventLogDaoImpl();
	        
	        eventLog.setTerminalId(paymentTransaction.getTerminalId());
	        eventLog.setEventDetails(paymentTransactionRequest.toString());
	        eventLog.setEventType("PayTM Callback API");
	        eventLog.setVersion("v1");
	        edao.saveEventLog(eventLog);
		} catch (Exception e) {
			System.out.println(e);
		}
		try{
			System.out.println("mqtt start");
			MqttClientConnection mqttClientConnection = new MqttClientConnection();
			JSONObject mqttJson = new JSONObject();
			mqttJson.put("TransactionId", paymentTransaction.getOrderId());
			mqttJson.put("Amount", paymentTransaction.getAuthAmount());
			mqttJson.put("TerminalId", paymentTransaction.getTerminalId());
			mqttJson.put("PaymentMode", paymentTransaction.getPspId());
			mqttClientConnection.getMQClient(mqttJson.toString());
			System.out.println("mqtt end");
		} catch(Exception e) {
			System.out.println("Error in PayTM Callback MQTT.."+e);
		}
		return "Success";
	}
	
	@RequestMapping(value = "/callbackMqttDemo", method = RequestMethod.GET)
	@ResponseBody
	public String getPayTmCallback() {
		
		try{
			MqttClientConnection mqttClientConnection = new MqttClientConnection();
			JSONObject mqttJson = new JSONObject();
			mqttJson.put("TransactionId", "Txn1");
			mqttJson.put("Amount", "1");
			mqttJson.put("TerminalId", "87264823764");
			mqttJson.put("PaymentMode", "paytm");
			mqttClientConnection.getMQClient(mqttJson.toString());
			return mqttJson.toString();
		} catch(Exception e) {
			logger.error("Error in PayTM Callback MQTT.."+e);
		}
		return null;
	}
}
