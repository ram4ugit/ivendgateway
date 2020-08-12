package com.limitlessmobility.iVendGateway.services.bharatqr;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmCallbackService;

@Controller
@RequestMapping("v1")
public class BharatCallBack {

private static final Logger logger = Logger.getLogger(PayTmCallbackService.class);
	
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String getPayTmCallback(@RequestBody String callBackRequest) {
		
		
		PaymentTransaction callbackTransaction = new PaymentTransaction();
		
		try { 
			JSONObject responseJsonObj = new JSONObject(callBackRequest);
	        
	        
//	        JSONObject responseJsonObj = obj.getJSONObject("response");
	        callbackTransaction.setOrderId(responseJsonObj.getString("ref_no"));
	        callbackTransaction.setMerchantOrderId(responseJsonObj.getString("ref_no"));
//	        callbackTransaction.setStatus(obj.getString("status")); 
//	        callbackTransaction.setStatusCode(obj.getString("statusCode"));
//	        callbackTransaction.setStatusMsg(obj.getString("statusMessage"));
	        
	        callbackTransaction.setTerminalId("p");
	        callbackTransaction.setAuthAmount(responseJsonObj.getDouble("settlement_amount"));
	        callbackTransaction.setAuthDate(String.valueOf(responseJsonObj.getLong("time_stamp")));
	        callbackTransaction.setComments(responseJsonObj.getString("comment"));
	        
	        callbackTransaction.setServiceType("callback");
	        callbackTransaction.setPspId("bharatqr");
	        callbackTransaction.setDeviceId("d1");
	        callbackTransaction.setMerchantId("zoneone");
	        
		} catch (JSONException e) {
			e.printStackTrace();
		}
		PaymentController transactionController = new PaymentController();
		String transactionStatus = transactionController.saveTransaction(callbackTransaction);
		return "";
	}
}
