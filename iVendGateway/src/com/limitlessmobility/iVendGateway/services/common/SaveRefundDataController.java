package com.limitlessmobility.iVendGateway.services.common;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobility.iVendGateway.controller.common.SaveRefundDataService;
import com.limitlessmobility.iVendGateway.controller.common.SaveRefundDataServiceImpl;
import com.limitlessmobility.iVendGateway.controller.psp.OperatorPspServiceImpl;
import com.limitlessmobility.iVendGateway.model.common.RefundData;

@Controller
@RequestMapping("/v2")
public class SaveRefundDataController {

	SaveRefundDataService saveRefundDataService = new SaveRefundDataServiceImpl();

	OperatorPspServiceImpl operatorPspService = new OperatorPspServiceImpl();


	@RequestMapping(value = "/saveRefundData", method = RequestMethod.POST)
	@ResponseBody
	public String insertRefundData(@RequestBody RefundData refundDatas) {

		JSONObject finalResponse = new JSONObject();
//		ResponseDTO responseDTO = new ResponseDTO();

		/* TokenAuthModel t = new TokenAuthModel(); */
		try {
			if (saveRefundDataService.saveRefundData(refundDatas)) {
				/*responseDTO.setMessage(ResponseStatusCode.SAVE_SUCCESS_MSG);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setErrors(null);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setResponseData("");
				responseDTO.setErrors(errors);*/
				
				finalResponse.put("responseObj", "");
				finalResponse.put("message", ResponseStatusCode.SAVE_SUCCESS_MSG);
				finalResponse.put("status", HttpStatusModal.OK);
			}else {
				/*responseDTO.setMessage(ResponseStatusCode.EXIST_MSG);
				responseDTO.setStatus("");*/
				
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Transaction not found");
				finalResponse.put("status", HttpStatusModal.ERROR);
				return finalResponse.toString();
			}
			
		} catch (Exception e) {
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", HttpStatusModal.ERROR);
				System.out.println(e);
			} catch (Exception ee){
				System.out.println(ee);
			}
		}
		return finalResponse.toString();
	}
	
	@RequestMapping(value = "/refundManualll", method = RequestMethod.POST)
	@ResponseBody
	public String manualRefund(@RequestBody RefundData refundDatas) {

		 Map<String, Object> errors = new HashMap<>();
		JSONObject finalResponse = new JSONObject();
//		ResponseDTO responseDTO = new ResponseDTO();

		/* TokenAuthModel t = new TokenAuthModel(); */
		try {
			if (saveRefundDataService.saveRefundData(refundDatas)) {
				/*responseDTO.setMessage(ResponseStatusCode.SAVE_SUCCESS_MSG);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setErrors(null);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setResponseData("");
				responseDTO.setErrors(errors);*/
				
				finalResponse.put("responseObj", "");
				finalResponse.put("message", ResponseStatusCode.SAVE_SUCCESS_MSG);
				finalResponse.put("status", HttpStatusModal.OK);
			}else {
				/*responseDTO.setMessage(ResponseStatusCode.EXIST_MSG);
				responseDTO.setStatus("");*/
				
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Invalid Refund Data");
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
			
		} catch (Exception e) {
			try{
				finalResponse.put("responseObj", "");
				finalResponse.put("message", e.getMessage());
				finalResponse.put("status", HttpStatusModal.ERROR);
				System.out.println(e);
			} catch (Exception ee){
				System.out.println(ee);
			}
		}
		return finalResponse.toString();
	}
}
