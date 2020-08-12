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
import com.limitlessmobility.iVendGateway.model.bharatqr.UPITransactionRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.sun.istack.internal.logging.Logger;

@Controller
@RequestMapping("/payment/v1/transactions/upi")
public class UPITransactionService {

	private static final Logger logger = Logger.getLogger(BharatQRTrasanctionService.class);

	@RequestMapping(value = "/initiate", method = RequestMethod.POST)
	@ResponseBody
	public String getUPICallback(@RequestBody UPITransactionRequest upiTrasanctionRequest) throws JSONException {

		logger.info("UPI Callback method is Callig..............");
		System.out.println("UPI Callback method is Callig...............");

		upiTrasanctionRequest.getTxn_currency();
		System.out.println("txn_currency " + upiTrasanctionRequest.getTxn_currency());
		upiTrasanctionRequest.getTxn_amount();
		System.out.println("txn_amount " + upiTrasanctionRequest.getTxn_amount());
		upiTrasanctionRequest.getRef_no();
		System.out.println("ref_no " + upiTrasanctionRequest.getRef_no());
		upiTrasanctionRequest.getPrimary_id();
		System.out.println("primary_id " + upiTrasanctionRequest.getPrimary_id());
		upiTrasanctionRequest.getSecondary_id();
		System.out.println("secondary_id " + upiTrasanctionRequest.getSecondary_id());
		upiTrasanctionRequest.getSettlement_amount();
		System.out.println("settlement_amount " + upiTrasanctionRequest.getSettlement_amount());
		upiTrasanctionRequest.getRef_no();
		System.out.println("REF_NO " + upiTrasanctionRequest.getRef_no());
		upiTrasanctionRequest.getPrimary_id();
		System.out.println("PRIMARY_ID " + upiTrasanctionRequest.getPrimary_id());
		upiTrasanctionRequest.getSecondary_id();
		System.out.println("SECONDARY_ID " + upiTrasanctionRequest.getSecondary_id());
		upiTrasanctionRequest.getSettlement_amount();
		System.out.println("SETTLEMENT_AMOUNT " + upiTrasanctionRequest.getSettlement_amount());
		upiTrasanctionRequest.getTime_stamp();
		System.out.println("time_stamp " + upiTrasanctionRequest.getTime_stamp());
		upiTrasanctionRequest.getTransaction_type();
		System.out.println("transaction_type " + upiTrasanctionRequest.getTransaction_type());
		upiTrasanctionRequest.getBank_code();
		System.out.println("bank_code " + upiTrasanctionRequest.getBank_code());
		upiTrasanctionRequest.getAggregator_id();
		System.out.println("aggregator_id " + upiTrasanctionRequest.getAggregator_id());
		upiTrasanctionRequest.getMerchant_vpa();
		System.out.println("merchant_vpa " + upiTrasanctionRequest.getMerchant_vpa());
		upiTrasanctionRequest.getCustomer_vpa();
		System.out.println("customer_vpa " + upiTrasanctionRequest.getCustomer_vpa()); 
		
		
		
		Date date = new Date();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(date);
		
		EventLogs eventLogRequest=new EventLogs();
		
		JSONObject reqJson = new JSONObject(upiTrasanctionRequest);

		PaymentTransaction paymentTransaction = new PaymentTransaction();
		JSONObject obj = new JSONObject(upiTrasanctionRequest);
		// EventLogs eventLog = new EventLogs();

		try {
			paymentTransaction.setMerchantId("zoneone");
			paymentTransaction.setOrderId(obj.getString("ref_no"));
			paymentTransaction.setPspTransactionId(obj.getString("ref_no"));
			paymentTransaction.setTerminalId("ABCD");
			paymentTransaction.setAuthAmount(obj.getDouble("txn_amount"));
			paymentTransaction.setAuthDate(String.valueOf(obj.getLong("time_stamp")));
			paymentTransaction.setSettlementTime(String.valueOf(obj.getLong("time_stamp")));
			paymentTransaction.setStatus("success");
			//paymentTransaction.setComments());
			paymentTransaction.setServiceType("UPI Callback Response");
			paymentTransaction.setPspId("BharatUPI");
			paymentTransaction.setDeviceId("D1");
			paymentTransaction.setCustomerName(reqJson.getString("customer_vpa"));
			paymentTransaction.setTransactionType(reqJson.getString("transaction_type"));
			paymentTransaction.setPspMerchantId(reqJson.getString("mid"));

			TransactionDao transactionDao = new TransactionDaoImpl();
			transactionDao.saveTransaction(paymentTransaction);

		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			EventLogDao edao = new eventLogDaoImpl();

			eventLogRequest.setTerminalId("6dc43ea74734033d");
			eventLogRequest.setEventDetails(reqJson.toString());
			eventLogRequest.setEventType("UPI static CheckStatus API Request");
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
		responseObj.put("txn_amount",  upiTrasanctionRequest.getTxn_amount());
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
