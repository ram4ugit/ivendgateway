package com.limitlessmobility.iVendGateway.services.phonepe;

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

import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.services.mqtt.MqttClientConnection;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCallbackService;

@Controller
@RequestMapping("/v2/phonepe")
public class PhonepeCallback {

	private static final Logger logger = Logger.getLogger(PayTmCallbackService.class);
	
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String getPhonepeCallback(@RequestBody String paymentTransactionRequest) {
		
		logger.info("Phonepe CallBack Method is calling!!");
		System.out.println("Phonepe CallBack Method is calling.. "+paymentTransactionRequest);
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

			MySqlDate date = new MySqlDate();
	        
	        JSONObject responseJsonObj = obj.getJSONObject("response");
	        JSONObject responseDataJsonObj = responseJsonObj.getJSONObject("data");
	        paymentTransaction.setAppId("8d4c832f-a5d1-43cf-b48e-0973b58e305d");
	        paymentTransaction.setOrderId(responseDataJsonObj.getString("providerReferenceId"));
	        paymentTransaction.setMerchantOrderId(responseDataJsonObj.getString("providerReferenceId"));
	        
	        if(responseJsonObj.getBoolean("success")){
	        	paymentTransaction.setStatus("success");
	        } else {
	        	paymentTransaction.setStatus("failure");
	        }
	         
	        //paymentTransaction.setStatusCode(obj.getString("statusCode"));
	        paymentTransaction.setStatusMsg(responseJsonObj.getString("message"));
	        
	        //paymentTransaction.setTerminalId(responseJsonObj.getString("posId"));
	        int amountResponse = responseDataJsonObj.getInt("amount");
	        
	        
	        paymentTransaction.setAuthAmount(amountResponse/100);
//	        paymentTransaction.setAuthDate();
	        try {
	        	paymentTransaction.setComments(responseJsonObj.getString("comment"));
	        } catch(Exception e){
	        	paymentTransaction.setComments("");
	        }
	        paymentTransaction.setServiceType("callback");
	        paymentTransaction.setPspId("paytm");
	        paymentTransaction.setPspTransactionId(responseDataJsonObj.getString("transactionId"));
	        paymentTransaction.setDeviceId("d1");
	        paymentTransaction.setMerchantId("zoneone");
	        
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
			paymentTransaction.setAuthDate(date.getDate());
	        
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
