package com.limitlessmobility.iVendGateway.services.payu;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.CommonQRRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping(value="v2/payu")
public class PayuCallbackService {

	
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public void callbackPayU(@RequestBody String callbackRequest) {
		System.out.println("Payu Callback Request :- "+callbackRequest);
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		try {
			String[] couple = callbackRequest.split("&");
			Map<String, String> testMap = new HashMap<String, String>();
			 for(int i =0; i < couple.length ; i++) {
				 try {
			    String[] items =couple[i].split("=");
			    testMap.put(items[0], items[1]);
			    System.out.println(items[0] +" = "+items[1]);
				 } catch(Exception e) {
					 
				 }
			 }
				 if(testMap.get("status").equalsIgnoreCase("success")) {
				 try {
					 
					 MySqlDate date = new MySqlDate();
						String datetimeJson = date.getDate();
						
						System.out.println("Callback Response datetime exact  "+datetimeJson);
				        
						PaymentInitiation paymentInitiation = CommonService.GetQRDetailsFromDB(testMap.get("txnid"));
				        paymentTransaction.setAppId(paymentInitiation.getAppId());
				        paymentTransaction.setOrderId(testMap.get("txnid"));
				        paymentTransaction.setMerchantOrderId(testMap.get("txnid"));
				        paymentTransaction.setStatus(testMap.get("status")); 
				        paymentTransaction.setStatusCode(testMap.get("status"));
				        paymentTransaction.setStatusMsg(testMap.get("status"));
				        
				        
				        paymentTransaction.setTerminalId(paymentInitiation.getTerminalId());
				        paymentTransaction.setAuthAmount(Double.parseDouble(testMap.get("amount")));
				        paymentTransaction.setPaidAmount(Double.parseDouble(testMap.get("amount")));
//				        paymentTransaction.setAuthDate();
				        try {
				        	paymentTransaction.setComments("");
				        } catch(Exception e){
				        	paymentTransaction.setComments("");
				        }
				        paymentTransaction.setServiceType("callback");
				        paymentTransaction.setPspId("payu");
				        paymentTransaction.setPspTransactionId(testMap.get("mihpayid"));
				        paymentTransaction.setDeviceId("d1");
				        paymentTransaction.setMerchantId("zoneone");
				        
						/*Timestamp dtStartDT = new Timestamp(datetimeJson);
						
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
						String finalDate = simpleDateFormat.format(dtStartDT);
						System.out.println("After Converted datetime "+finalDate);*/
						paymentTransaction.setAuthDate(date.getDate());
				        
					} catch (Exception e) {
						e.printStackTrace();
					}
					PaymentController transactionController = new PaymentController();
					transactionController.saveTransaction(paymentTransaction);
				 }
		} catch(Exception e) {
			System.out.println("callbackPayU "+e);
		}
	}
}
