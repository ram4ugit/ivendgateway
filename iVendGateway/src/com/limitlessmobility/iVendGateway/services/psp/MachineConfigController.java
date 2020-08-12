package com.limitlessmobility.iVendGateway.services.psp;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseStatusReturn;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.psp.MachineConfigServiceImpl;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.model.paymentmode.GetMachinePspResponse;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigRequest;
import com.limitlessmobility.iVendGateway.psp.model.PspListModel;

import constants.KeyValueHolder;

@RestController
public class MachineConfigController {

	MachineConfigServiceImpl machineConfigService = new MachineConfigServiceImpl();
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/saveMachineConfig", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO insertMachineConfig(@RequestBody MachineConfigRequest machineConfigRequest){
	
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		Integer operatorId=null;
		/*TokenAuthModel t = new TokenAuthModel();
		
		try{
			 String token = request.getHeader("Authorization");
			 
			 
			 String header = request.getHeader(HEADER_STRING);
		        if (header != null && header.startsWith(TOKEN_PREFIX)) {
		        	token = header.replace(TOKEN_PREFIX,"");
			 
		        } else{
		        	header=request.getParameter(HEADER_STRING);
		        	if (header != null && header.startsWith(TOKEN_PREFIX)) {
			        	token = header.replace(TOKEN_PREFIX,"");
			        }
		        }
			 System.out.println("TOKENNNN===== "+token);
				t = JwtTokenDecode.validateJwt(token);
			} catch(Exception e){
				System.out.println(e);
			}
		if(CommonValidationUtility.isEmpty(t.getOperatorId())){
			System.out.println("token authentication failed!!");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
			responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
		} else {*/
			operatorId = machineConfigRequest.getOperatorId();
			
			/*if(CommonValidationUtility.isEmpty(machineConfigRequest.getCustomerLocationId()) || machineConfigRequest.getCustomerLocationId()==0) {
				errors.put(KeyValueHolder.CUSTOMER_LOCATION_ID_KEY, KeyValueHolder.CUSTOMER_LOCATION_ID_MGS);
			} 
			if(CommonValidationUtility.isEmpty(t.getOperatorId())) {
				errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
			} 
			if(CommonValidationUtility.isEmpty(machineConfigRequest.getCustomerId())) {
				errors.put(KeyValueHolder.CUSTOMER_ID_KEY, KeyValueHolder.CUSTOMER_ID_MSG);
			}
			if(CommonValidationUtility.isEmpty(machineConfigRequest.getPspId())) {
				errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
			}
			if(CommonValidationUtility.isEmpty(machineConfigRequest.getMachineId())) {
				errors.put(KeyValueHolder.MACHINE_ID_KEY, KeyValueHolder.MACHINE_ID_MSG);
			}
			if(CommonValidationUtility.isEmpty(machineConfigRequest.getTelemetryId())) {
				errors.put(KeyValueHolder.TELEMETRY_ID_KEY, KeyValueHolder.TELEMETRY_ID_MSG);
			}
			if(CommonValidationUtility.isEmpty(machineConfigRequest.getVposId())) {
				errors.put(KeyValueHolder.V_POS_ID_KEY, KeyValueHolder.V_POS_ID_MSG);
			}*/
		
		if(CommonValidationUtility.isEmpty(errors)) {
			
			if(machineConfigService.saveMachineConfig(machineConfigRequest, operatorId)) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			}
			
		} else { 
			
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
//	}
		
		return responseDTO;
	}
	
	@RequestMapping(value="/getMachineConfig", method = RequestMethod.POST)
    public ResponseDTO getMachineConfigList(@RequestBody MachineConfigEntity machineConfigEntity){
	
		System.out.println("getmachineConfigEntity Request: "+machineConfigEntity);
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();

		TokenAuthModel t = new TokenAuthModel();
		try{
			 String token = request.getHeader("Authorization");
			 
			 
			 String header = request.getHeader(HEADER_STRING);
		        if (header != null && header.startsWith(TOKEN_PREFIX)) {
		        	token = header.replace(TOKEN_PREFIX,"");
			 
		        } else{
		        	header=request.getParameter(HEADER_STRING);
		        	if (header != null && header.startsWith(TOKEN_PREFIX)) {
			        	token = header.replace(TOKEN_PREFIX,"");
			        }
		        }
			 System.out.println("TOKENNNN===== "+token);
				t = JwtTokenDecode.validateJwt(token);
			} catch(Exception e){
				System.out.println(e);
			}
		if(CommonValidationUtility.isEmpty(t.getOperatorId())){
			System.out.println("token authentication failed!!");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
			responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
		} else {
			machineConfigEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		try{
		if(CommonValidationUtility.isEmpty(t.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<MachineConfigEntity> machineConfigEntityList = machineConfigService.getMachineConfig(machineConfigEntity);
				if(machineConfigEntityList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(machineConfigEntityList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(machineConfigEntityList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get machineConfigEntityList.. "+e);
			}
			
				
		} else { 
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		
		} catch(Exception e){
			System.out.println("machineConfigEntityList controller error.. "+e);
		}}
		return responseDTO;
	}
	
	@RequestMapping(value="/getMachinePSP", method = RequestMethod.POST)
    public ResponseDTO getMachinePSP(@RequestBody MachineConfigEntity machineConfigEntity){
	
		System.out.println("getMachinePSP Request: "+machineConfigEntity);
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();

		TokenAuthModel t = new TokenAuthModel();
		try{
			 String token = request.getHeader("Authorization");
			 
			 
			 String header = request.getHeader(HEADER_STRING);
		        if (header != null && header.startsWith(TOKEN_PREFIX)) {
		        	token = header.replace(TOKEN_PREFIX,"");
			 
		        } else{
		        	header=request.getParameter(HEADER_STRING);
		        	if (header != null && header.startsWith(TOKEN_PREFIX)) {
			        	token = header.replace(TOKEN_PREFIX,"");
			        }
		        }
			 System.out.println("TOKENNNN===== "+token);
				t = JwtTokenDecode.validateJwt(token);
			} catch(Exception e){
				System.out.println(e);
			}
		if(CommonValidationUtility.isEmpty(t.getOperatorId())){
			System.out.println("token authentication failed!!");
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
			responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
		} else {
			machineConfigEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		try{
		if(CommonValidationUtility.isEmpty(t.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		if(CommonValidationUtility.isEmpty(machineConfigEntity.getTelemetryId())) {
			errors.put(KeyValueHolder.TELEMETRY_ID_KEY, KeyValueHolder.TELEMETRY_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<PspListModel> machinePSPList = machineConfigService.getMachinePspList(machineConfigEntity.getTelemetryId(), machineConfigEntity.getOperatorId());
				if(machinePSPList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(machinePSPList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(machinePSPList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get machineConfigEntityList.. "+e);
			}
			
				
		} else { 
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		
		} catch(Exception e){
			System.out.println("machineConfigEntityList controller error.. "+e);
		}}
		return responseDTO;
	}
	
	
	
	 @RequestMapping(value="/getMachinePSPList", method = RequestMethod.POST)
	 public String getMachinePSPList(@RequestBody MachineConfigEntity machineConfigEntity){
		 
		 System.out.println("getMachinePSP Request: "+machineConfigEntity);
			Map<String, Object> errors = new HashMap<>();
//			ResponseDTOObj responseDTO = new ResponseDTOObj();
		
		 JSONObject finalResponse = new JSONObject();
			
			try{
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<GetMachinePspResponse> machinePSPList = machineConfigService.getMachinePspListNew(machineConfigEntity.getTelemetryId(), machineConfigEntity.getOperatorId(), machineConfigEntity.getMachineId());
				if(machinePSPList.size()>0){
					JSONObject j = new JSONObject();
					j.put("pspList", machinePSPList.toArray());
	
					finalResponse.put("responseObj", j);
					finalResponse.put("status", HttpStatusModal.OK);
					finalResponse.put("message", "");
				} else {
					JSONObject j = new JSONObject();
					j.put("pspList", machinePSPList.toArray());
	
					finalResponse.put("responseObj", j);
					finalResponse.put("status", HttpStatusModal.OK);
					finalResponse.put("message", ResponseStatusCode.NO_RECORD);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get machineConfigEntityList.. "+e);
			}
			
				
		} else { 
			finalResponse.put("responseObj", "");
			finalResponse.put("status", HttpStatusModal.OK);
			finalResponse.put("message", ResponseStatusCode.NO_RECORD);
		}
		
		} catch(Exception e){
			System.out.println("machineConfigEntityList controller error.. "+e);
		}
			return finalResponse.toString();
		 
	 }
	
}
