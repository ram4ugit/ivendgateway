package com.limitlessmobility.iVendGateway.services.paymentmode;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JsonValidator;
import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobility.iVendGateway.controller.paymentmode.PaymentModeController;
import com.limitlessmobility.iVendGateway.controller.terminal.EventController;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;
import com.limitlessmobility.iVendGateway.services.terminal.TerminalCheckStatusService;

/**
 * This class is used for get Payment mode details
 * @author RamN
 *
 */
@Controller
@RequestMapping(value="/v1")
public class PaymentModeServices {

	private static final Logger logger = Logger.getLogger(PaymentModeServices.class);
	
	PaymentModeController paymentModeController = new PaymentModeController();
	
	
	/**
	 * This API is used for get payment mode list
	 * @return paymentModeList
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/getPaymentMode", method = RequestMethod.GET)
	@ResponseBody
	public String getPaymentMode() throws JSONException {
		List<PaymentModeModel> paymentModes = new ArrayList<PaymentModeModel>();
		JSONObject response = new JSONObject();
		
		try{
			paymentModes = paymentModeController.getPaymentModes();
			
			response.put("paymentModes", paymentModes);
			
		} catch(Exception e) {
			logger.info("Error in get PaymentModes Services..."+e);
		}
		
		JSONObject finalResponse = new JSONObject();
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("message", "null");
			finalResponse.put("status", HttpStatusModal.OK);
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("message", HttpStatusModal.ERROR_FORMAT);
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
	
	/**
	 * This API is used for get psp list
	 * @return pspDetailList
	 * @throws JSONException 
	 */
	@RequestMapping(value = "/getPSPList", method = RequestMethod.POST)
	@ResponseBody
	public String getPSPList(@RequestBody PaymentModeModel paymentMode) throws JSONException {
//		System.out.println(paymentMode.getPaymentModeId());
		MySqlDate d = new MySqlDate();
		List<PaymentModeModel> paymentModeList = new ArrayList<PaymentModeModel>();
		JSONObject response = new JSONObject();
		
		try{
			paymentModeList = paymentModeController.getPspDetails(paymentMode);
			
			response.put("pspList", paymentModeList);
			
		} catch(Exception e) {
			logger.info("Error in get PaymentModes Services..."+e);
		}
		
		JSONObject finalResponse = new JSONObject();
		finalResponse.put("message", d.getDate());
		
		boolean responseValidator = JsonValidator.isJSONValid(response.toString());
		if(responseValidator){
			JSONObject responseObj = new JSONObject(response.toString());
			finalResponse.put("responseObj", responseObj);
			finalResponse.put("status", HttpStatusModal.OK);
			return finalResponse.toString();
		} else {
			finalResponse.put("responseObj", "null");
			finalResponse.put("status", HttpStatusModal.ERROR);
			return finalResponse.toString();
		}
	}
}
