package com.limitlessmobility.iVendGateway.services.bharatqr;

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
import com.limitlessmobility.iVendGateway.model.bharatqr.BQRTrasanctionRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.services.mqtt.MqttClientConnection;
import com.sun.istack.internal.logging.Logger;

@Controller
@RequestMapping("/payment/v1/transactions/bharatqr")
public class BharatQRTrasanctionService {

	private static final Logger logger = Logger.getLogger(BharatQRTrasanctionService.class);

	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getBharatQRCallback(@RequestBody BQRTrasanctionRequest bqrTrasanctionRequest) throws JSONException {

		logger.info("Callback method is Callig..............");
		System.out.println("Callback method is Callig...............");
		String str="";
		String orderId="";
		String deviceTransactionId="";

		bqrTrasanctionRequest.getMid();
		System.out.println("MID " + bqrTrasanctionRequest.getMid());
		bqrTrasanctionRequest.getMpan();
		System.out.println("MPAN " + bqrTrasanctionRequest.getMpan());
		bqrTrasanctionRequest.getCustomer_name();
		System.out.println("CUSTOMER_NAME " + bqrTrasanctionRequest.getCustomer_name());
		bqrTrasanctionRequest.getTxn_currency();
		System.out.println("TXN_currency " + bqrTrasanctionRequest.getTxn_currency());
		bqrTrasanctionRequest.getTxn_amount();
		System.out.println("TXN_AMOUNT " + bqrTrasanctionRequest.getTxn_amount());
		bqrTrasanctionRequest.getTr_id();
		System.out.println("TR_ID " + bqrTrasanctionRequest.getTr_id());
		bqrTrasanctionRequest.getAuth_code();
		System.out.println("AUTH_CODE " + bqrTrasanctionRequest.getAuth_code());
		bqrTrasanctionRequest.getRef_no();
		System.out.println("REF_NO " + bqrTrasanctionRequest.getRef_no());
		bqrTrasanctionRequest.getPrimary_id();
		System.out.println("PRIMARY_ID " + bqrTrasanctionRequest.getPrimary_id());


		/*String str=bqrTrasanctionRequest.getSecondary_id();
		str=str.replaceFirst(String.valueOf(str.charAt(0)),"").trim();//'0' will replace with "" 
		System.out.println("Secondary_id after trim "+str);*/
		
		bqrTrasanctionRequest.getSettlement_amount();
		System.out.println("SETTLEMENT_AMOUNT " + bqrTrasanctionRequest.getSettlement_amount());
		bqrTrasanctionRequest.getTime_stamp();
		System.out.println("TIME_STAMP " + bqrTrasanctionRequest.getTime_stamp());
		String txntype=bqrTrasanctionRequest.getTransaction_type();
		System.out.println("TRANSACTION_TYPE " + bqrTrasanctionRequest.getTransaction_type());
		bqrTrasanctionRequest.getBank_code();
		System.out.println("BANK_CODE " + bqrTrasanctionRequest.getBank_code());
		bqrTrasanctionRequest.getAggregator_id();
		System.out.println("AGGREGATOR_ID " + bqrTrasanctionRequest.getAggregator_id());
		bqrTrasanctionRequest.getConsumer_pan();
		System.out.println("CONSUMER_PAN " + bqrTrasanctionRequest.getConsumer_pan());
		bqrTrasanctionRequest.getMerchant_vpa();
		System.out.println("MERCHANT_VPA " + bqrTrasanctionRequest.getMerchant_vpa());
		bqrTrasanctionRequest.getCustomer_vpa();
		System.out.println("CUSTOMER_VPA " + bqrTrasanctionRequest.getCustomer_vpa());
		
		
		if (txntype.equals("1")) {
			str=bqrTrasanctionRequest.getSecondary_id();
			System.out.println("Secondary_id without trim "+str);
			}
			else
			{
			str=bqrTrasanctionRequest.getSecondary_id();
			str=str.replaceFirst(String.valueOf(str.charAt(0)),"").trim();//'0' will replace with "" 
			System.out.println("Secondary_id after trim "+str);
			}
		
         
		/*String originalString = "2010-07-14 09:00:02";
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
		String newString = new SimpleDateFormat("H:mm").format(date);*/
		
		System.out.println("Secondary_id "+str);
		
		
		Date date = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(date);
		
		EventLogs eventLogRequest=new EventLogs();
		
		JSONObject reqJson = new JSONObject(bqrTrasanctionRequest);

		PaymentTransaction paymentTransaction = new PaymentTransaction();
		JSONObject obj = new JSONObject(bqrTrasanctionRequest);
		// EventLogs eventLog = new EventLogs();
		
		deviceTransactionId=obj.getString("primary_id");
		orderId=deviceTransactionId.replace(str,"");
		
		if (txntype.equals("1")) {
			try {
				paymentTransaction.setMerchantId("vendiman");
				paymentTransaction.setOrderId(obj.getString("primary_id"));
				paymentTransaction.setPspTransactionId(obj.getString("ref_no"));
				paymentTransaction.setDeviceTransactionId(obj.getString("primary_id"));
				paymentTransaction.setMerchantOrderId(obj.getString("primary_id"));
				paymentTransaction.setTerminalId(str);
				paymentTransaction.setAuthAmount(obj.getDouble("settlement_amount"));
				paymentTransaction.setAuthDate(String.valueOf(obj.getLong("time_stamp")));
				paymentTransaction.setSettlementTime(String.valueOf(obj.getLong("time_stamp")));
				paymentTransaction.setStatus("success");
				paymentTransaction.setServiceType("BharatQR Callback Response");
				paymentTransaction.setPspId("bharatqr");
				paymentTransaction.setDeviceId("D1");
				paymentTransaction.setAuthCode(reqJson.getString("auth_code"));
				paymentTransaction.setCustomerName(reqJson.getString("customer_name"));
				paymentTransaction.setTransactionType(reqJson.getString("transaction_type"));
				paymentTransaction.setPspMerchantId(reqJson.getString("mid"));

				TransactionDao transactionDao = new TransactionDaoImpl();
				transactionDao.saveTransaction(paymentTransaction);

			} catch (Exception e) {
				e.printStackTrace();

			}
			try {
				EventLogDao edao = new eventLogDaoImpl();

				eventLogRequest.setTerminalId(str);
				eventLogRequest.setEventDetails(reqJson.toString());
				eventLogRequest.setEventType("BharatQR static CheckStatus API Request");
				eventLogRequest.setVersion("v1");
	            eventLogRequest.setEventDate(currentTime);
				edao.saveEventLog(eventLogRequest);
				
			} catch (Exception e) {
				System.out.println(e);
			}
		
			/*try{
				MqttClientConnection mqttClientConnection = new MqttClientConnection();
				JSONObject mqttJson = new JSONObject();
				mqttJson.put("TransactionId", paymentTransaction.getOrderId());
				mqttJson.put("Amount", paymentTransaction.getAuthAmount());
				mqttJson.put("TerminalId", paymentTransaction.getTerminalId());
				mqttJson.put("PaymentMode", paymentTransaction.getPspId());
				mqttClientConnection.getMQClient(mqttJson.toString());
			} catch(Exception e) {
				logger.error("Error in PayTM Callback MQTT.."+e);
				System.out.println("Error in PayTM Callback MQTT.."+e);
			}*/
		
		} else if(txntype.equals("2")){
			try {
				paymentTransaction.setMerchantId("vendiman");
				paymentTransaction.setOrderId(orderId);
				paymentTransaction.setPspTransactionId(obj.getString("ref_no"));
				paymentTransaction.setDeviceTransactionId(obj.getString("primary_id"));
				paymentTransaction.setMerchantOrderId(orderId);
				paymentTransaction.setTerminalId(str);
				paymentTransaction.setAuthAmount(obj.getDouble("settlement_amount"));
				paymentTransaction.setAuthDate(String.valueOf(obj.getLong("time_stamp")));
				paymentTransaction.setSettlementTime(String.valueOf(obj.getLong("time_stamp")));
				paymentTransaction.setStatus("success");
				//paymentTransaction.setComments());
				paymentTransaction.setServiceType("UPI Callback Response");
				paymentTransaction.setPspId("bharatqr");
				paymentTransaction.setDeviceId("D1");
				//paymentTransaction.setCustomerName(reqJson.getString("customer_vpa"));
				paymentTransaction.setTransactionType(reqJson.getString("transaction_type"));
				paymentTransaction.setPspMerchantId(reqJson.getString("mid"));

				TransactionDao transactionDao = new TransactionDaoImpl();
				transactionDao.saveTransaction(paymentTransaction);

			} catch (Exception e) {
				e.printStackTrace();

			}
			try {
				EventLogDao edao = new eventLogDaoImpl();

				eventLogRequest.setTerminalId(str);
				eventLogRequest.setEventDetails(reqJson.toString());
				eventLogRequest.setEventType("UPI static CheckStatus API Request");
				eventLogRequest.setVersion("v1");
	            eventLogRequest.setEventDate(currentTime);
				edao.saveEventLog(eventLogRequest);
				
			} catch (Exception e) {
				System.out.println(e);
			}
			
			try{
				MqttClientConnection mqttClientConnection = new MqttClientConnection();
				JSONObject mqttJson = new JSONObject();
				System.out.println("TransactionId "+paymentTransaction.getOrderId());
				mqttJson.put("TransactionId", paymentTransaction.getOrderId());
				mqttJson.put("Amount", paymentTransaction.getAuthAmount());
				mqttJson.put("TerminalId", paymentTransaction.getTerminalId());
				mqttJson.put("PaymentMode", paymentTransaction.getPspId());
				mqttClientConnection.getMQClient(mqttJson.toString());
			} catch(Exception e) {
				/*logger.error("Error in PayTM Callback MQTT.."+e);*/
				System.out.println("Error in PayTM Callback MQTT.."+e);
			}

		}
		else {
			System.out.println("Coming...........else");
		}
		
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", "null");
		jsonObject.put("status", "SUCCESS");

		JSONObject responseObj = new JSONObject();
		responseObj.put("txn_amount",  bqrTrasanctionRequest.getTxn_amount());
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
