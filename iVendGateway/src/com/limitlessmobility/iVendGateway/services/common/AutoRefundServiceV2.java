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

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobility.iVendGateway.controller.common.CommonController;
import com.limitlessmobility.iVendGateway.controller.common.CommonControllerV2;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundResponse;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;

@Controller
@RequestMapping(value="/v2/commonRefund")
public class AutoRefundServiceV2 {
	CommonControllerV2 commonController = new CommonControllerV2();
	
	@RequestMapping(value="/autoRefund", method=RequestMethod.POST)
	@ResponseBody
	public String getRefundList(){
		PaytmRefundRequestModelFinal prm = new PaytmRefundRequestModelFinal();
		commonController.getRefundList();
		return "Success";
	}
	
	@RequestMapping(value="/autoRefundProcess", method=RequestMethod.POST)
	@ResponseBody
	public String autoRefundProcess(){
		commonController.autoRefundProcessV2();
		return "Success";
	}
	@RequestMapping(value="/checkStatusRefundProcess", method=RequestMethod.POST)
	@ResponseBody
	public String checkStatusRefundProcess(){
		commonController.checkStatusRefundProcess();
		return "Success";
	}
	
	@RequestMapping(value="/manualRefund", method=RequestMethod.POST)
	@ResponseBody
	public ResponseDTO manualRefundProcesss(@RequestBody ManualRefundRequest refundRequest){
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> errors = new HashMap<>();
		
		ManualRefundResponse response = new ManualRefundResponse();
		
		String refundStatus = commonController.manualRefundProcess(refundRequest);
		if(refundStatus.equalsIgnoreCase("success")){
			
			String refundStatuss = commonController.manualRefund(refundRequest);
			try{
			JSONObject refundJson = new JSONObject(refundStatuss);
			
			response.setStatus(refundJson.get("status").toString());
			response.setStatusMessage(refundJson.get("statusMessage").toString());
			
			System.out.println("Manual Refund Response.. " +refundStatuss);
//			ResponseStatusReturn responseStatus = new ResponseStatusReturn();
//			responseStatus.setStatus("true");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
			responseDTO.setStatus(refundJson.get("status").toString());
			responseDTO.setMessage("");
			responseDTO.setResponseData(response);
			
			} catch (Exception e) {
				System.out.println("manualRefundProcess error.. "+e);
			}
//			response.put("status", arg1)
			
			
		} else if(refundStatus.equalsIgnoreCase("exist")) {
			System.out.println("Exist Manual Refund");
			String refundStatuss = commonController.manualRefund(refundRequest);
			try{
			JSONObject refundJson = new JSONObject(refundStatuss);
			
			response.setStatus(refundJson.get("status").toString());
			response.setStatusMessage(refundJson.get("statusMessage").toString());
			
			System.out.println("Manual Refund Response.. " +refundStatuss);
//			ResponseStatusReturn responseStatus = new ResponseStatusReturn();
//			responseStatus.setStatus("true");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
			responseDTO.setStatus(refundJson.get("status").toString());
			responseDTO.setMessage("");
			responseDTO.setResponseData(response);
			
			} catch (Exception e) {
				System.out.println("manualRefundProcess error.. "+e);
			}
//			response.put("status", arg1)
			
			
		}else{
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
			responseDTO.setMessage("Transaction doesn't exist");
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, responseDTO.getMessage(), errors, "false");
		}
		
		return responseDTO;
	}

	public void directRefund(){
		commonController.directRefund();
	}
}
