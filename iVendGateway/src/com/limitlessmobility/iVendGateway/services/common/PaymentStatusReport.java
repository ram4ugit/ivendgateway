package com.limitlessmobility.iVendGateway.services.common;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONObject;
import com.limitlessmobility.iVendGateway.dao.common.PaymentStatusDao;
import com.limitlessmobility.iVendGateway.dao.common.PaymentStatusDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.PaymentStatusRequest;

@RestController
@RequestMapping(value="v2")
public class PaymentStatusReport {

	@RequestMapping(value="paymentStatus")
	public String paymentStatus(@RequestBody PaymentStatusRequest request){
		System.out.println("paymentStatus reportt.. "+request.toString());
		JSONObject finalResponse = new JSONObject();
		try{
			PaymentStatusDao p = new PaymentStatusDaoImpl();
			String responseSrt = p.paymentStatus(request);
			JSONObject responseJson = new JSONObject(responseSrt);
			if(responseJson.getString("status").equalsIgnoreCase("success")){
				JSONObject j = new JSONObject();
				j.put("amount", responseJson.getString("amount"));
				finalResponse.put("responseObj", j);
				
				finalResponse.put("message", "Payment Done");
				finalResponse.put("status", "Success");
			} else{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Payment Not Done");
				finalResponse.put("status", "Failure");
			}
		} catch(Exception e){
			
		}
		System.out.println("paymentStatus Responsee.. "+finalResponse.toString());
		return finalResponse.toString();
	}
}
