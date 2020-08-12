package com.limitlessmobility.iVendGateway.services.phonepe;

import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
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
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.model.phonepe.Data;
import com.limitlessmobility.iVendGateway.model.phonepe.InitiationDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PPCallbackRequestData;
import com.limitlessmobility.iVendGateway.model.phonepe.TerminalDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.TransactionContext;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.services.mqtt.MqttClientConnection;
import com.sun.istack.internal.logging.Logger;

@Controller
@RequestMapping("/payment/static/transactions/phonepe")
public class PhonePeCallbackStatic {
	
	private static final Logger logger = Logger.getLogger(PhonePeTrasanctionService.class);
	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getPhonePeCallback(@RequestBody String ppCallbackRequest) throws JSONException  {
		
		//logger.info("Callback method Calling.........");
		//logger.info("Callback method Calling........." +ppCallbackRequest);
		System.out.println("PhonePe Static CallBack Request First "+ppCallbackRequest);
		JSONObject mainData=new JSONObject(ppCallbackRequest);
		System.out.println("Main Data PhonePe Static "+mainData);
		
//		String actualData= mainData.getString("response");
		
	//	System.out.println("ActualData "+actualData.toString());
		
		
//		String encodData=actualData.toString().trim();
		
		

		
		
		Date date = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(date);
		EventLogs eventLogRequest=new EventLogs();
		
		PhonePeDao peDao=new PhonePeDaoImpl();
//		InitiationDetail detail=new InitiationDetail();
		TerminalDetail detail2=new TerminalDetail();
		PPCallbackRequestData callbackRequestData=new PPCallbackRequestData();
		callbackRequestData.setSuccess(mainData.getBoolean("success"));
		System.out.println("success PhonePe Static "+callbackRequestData.getSuccess());
		callbackRequestData.setCode(mainData.getString("code"));
		System.out.println("code PhonePe Static "+callbackRequestData.getCode());
		callbackRequestData.setMessage(mainData.getString("message"));
		System.out.println("message PhonePe Static "+callbackRequestData.getMessage());
		
		JSONObject jdata=mainData.getJSONObject("data");
		Data data=new Data();
		data.setTransactionId(jdata.getString("transactionId"));
		System.out.println("transactionId PhonePe Static "+data.getTransactionId());
		data.setMerchantId(jdata.getString("merchantId"));
		System.out.println("merchantId PhonePe Static "+data.getMerchantId());
		data.setProviderReferenceId(jdata.getString("providerReferenceId"));
		System.out.println("providerReferenceId PhonePe Static "+data.getProviderReferenceId());
		data.setAmount(jdata.getInt("amount"));
		System.out.println("amount PhonePe Static "+data.getAmount());
		data.setPaymentState(jdata.getString("paymentState"));
		System.out.println("paymentState "+data.getPaymentState());
		data.setPayResponseCode(jdata.getString("payResponseCode"));
		System.out.println("payResponseCode "+data.getPayResponseCode());
		
		TransactionContext context=new TransactionContext();
	/*	if (jdata.getJSONObject("transactionContext")!=null) {
			JSONObject jcontext=jdata.getJSONObject("transactionContext");
			
			if (jcontext.getString("qrCodeId")!=null) {
				context.setQrCodeId(jcontext.getString("qrCodeId"));
			} else{
				context.setQrCodeId("");
			}
			if(jcontext.getString("storeId")!=null) {
				context.setStoreId(jcontext.getString("storeId"));
			}
			else{
				context.setStoreId("");
			}
			if(jcontext.getString("terminalId")!=null) {
				context.setStoreId(jcontext.getString("terminalId"));
			}
			else{
				context.setTerminalId("");
			}
			
		} else {
             data.setTransactionContext(context);
		}*/
		
		 // data.getTransactionContext();		
		
		/*if (jdata.getJSONArray("paymentModes")!=null) {
			JSONArray jsonArray=jdata.getJSONArray("paymentModes");
			JSONObject jaObject=jsonArray.getJSONObject(0);
			List<PaymentMode> paymentModes=new ArrayList<PaymentMode>();
	        PaymentMode pmode=new PaymentMode();
	        pmode.setMode(jaObject.getString("mode"));
	        pmode.setAmount(jaObject.getInt("amount"));
	        pmode.setUtr(jaObject.getString("utr"));
	        paymentModes.add(pmode);
		} else {

		}*/
		
		/*System.out.println("payResponseCode "+pmode.getMode());
		System.out.println("payResponseCode "+pmode.getAmount());
		System.out.println("payResponseCode "+pmode.getUtr());*/
		/*System.out.println("payResponseCode "+data.getTransactionContext());*/
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		EventLogs eventLog = new EventLogs();
		
		try {
			String checkResponse = mainData.getString("code");
			System.out.println("CheckResponse "+checkResponse);
			if (checkResponse.equalsIgnoreCase("PAYMENT_SUCCESS")) {
			JSONObject obj = new JSONObject(callbackRequestData);
	        
	      //  JSONObject responseJsonObj = obj.getJSONObject(data);
	       // paymentTransaction.setAppId("8d4c832f-a5d1-43cf-b48e-0973b58e305d");
	        paymentTransaction.setOrderId(data.getTransactionId());
	        System.out.println("OrderID "+paymentTransaction.getOrderId());
	        paymentTransaction.setMerchantOrderId(data.getProviderReferenceId());
	        System.out.println("MerchantOrderId "+paymentTransaction.getMerchantOrderId());
	        //paymentTransaction.setStatus(obj.getString("status")); 
	        paymentTransaction.setStatusCode(callbackRequestData.getCode());
	        System.out.println("StatusCode "+paymentTransaction.getStatusCode());
	      //paymentTransaction.setStatusMsg(obj.getString("statusMessage"));
	        paymentTransaction.setPspTransactionId(data.getTransactionId());
	        String txnId=data.getTransactionId();
	        
	        System.out.println("txnId "+txnId);
	        
//	        detail=peDao.getInitiationDetail(txnId);
	        
	        System.out.println("PspTransactionId "+paymentTransaction.getPspTransactionId());
//	        paymentTransaction.setTerminalId(detail.getTerminalId());
//	        paymentTransaction.setAppId(detail.getAppId());
	        
	        String terminalId=jdata.getString("terminalId");
	        paymentTransaction.setTerminalId(terminalId);
	        
	        System.out.println("terminalId "+terminalId);
	        
	        detail2=peDao.getTerminalDetail(terminalId);
	        
	      //paymentTransaction.setAuthAmount(pmode.getAmount());
	        paymentTransaction.setComments(callbackRequestData.getMessage());
	        System.out.println("Comments "+paymentTransaction.getComments());
	        paymentTransaction.setStatusMsg(callbackRequestData.getMessage());
	        System.out.println("Status Message "+paymentTransaction.getComments());
	        paymentTransaction.setPspMerchantId(data.getMerchantId());
	        System.out.println("PspMerchantId "+paymentTransaction.getPspMerchantId());
	        paymentTransaction.setServiceType("callback");
	        paymentTransaction.setPspId("phonepe");
	        paymentTransaction.setDeviceId(detail2.getDeviceId());
	        paymentTransaction.setMerchantId(detail2.getMerchantId());
	        paymentTransaction.setAuthAmount((data.getAmount())/100);
	        paymentTransaction.setPaidAmount(paymentTransaction.getAuthAmount());
	        paymentTransaction.setSettlementAmount(Integer.toString(data.getAmount()/100));
	        paymentTransaction.setTransactionType("0");
	        paymentTransaction.setDeviceId("static");
	        System.out.println("All OK");
	        TransactionDao transactionDao = new TransactionDaoImpl();
			transactionDao.saveTransaction(paymentTransaction);
			
			
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
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			EventLogDao edao = new eventLogDaoImpl();

			eventLogRequest.setTerminalId("T1");
			eventLogRequest.setEventDetails(mainData.toString());
			eventLogRequest.setEventType("PhonePe Callback API Request");
			eventLogRequest.setVersion("v1");
            eventLogRequest.setEventDate(currentTime);
			edao.saveEventLog(eventLogRequest);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		return mainData.toString();
		
		
	}
}
