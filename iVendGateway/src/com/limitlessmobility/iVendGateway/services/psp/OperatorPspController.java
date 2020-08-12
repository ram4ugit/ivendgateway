package com.limitlessmobility.iVendGateway.services.psp;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseStatusReturn;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.psp.OperatorPspServiceImpl;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspAll;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspSequenceModel;

import constants.KeyValueHolder;

@Controller
public class OperatorPspController {

	OperatorPspServiceImpl operatorPspService = new OperatorPspServiceImpl();
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/saveOperatorPsp", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO insertOperatorPsp(@RequestBody List<OperatorPspEntity> operatorPspEntityList){
		System.out.println("Save Operator Request: "+operatorPspEntityList);
	
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
			Iterator<OperatorPspEntity> itr = operatorPspEntityList.iterator();
			while(itr.hasNext()){
				OperatorPspEntity operatorPspEntity = itr.next();
			operatorPspEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getOperatorId()) || operatorPspEntity.getOperatorId()==0) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspMerchantId())) {
			errors.put(KeyValueHolder.PSP_MERCHANT_ID_KEY, KeyValueHolder.PSP_MERCHANT_ID_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(errors)) {
			boolean saveStatus = operatorPspService.saveOperatorPsp(operatorPspEntity);
			if(saveStatus) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			} else if(saveStatus==false) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("false");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.EXIST_MSG);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			} else {
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.ERROR_CODE);
				responseDTO.setMessage(ResponseStatusCode.ERROR_MSG);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
				return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
			
			}
			
		} else { 
			
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		}}
		
		return responseDTO;
	}
	
	@RequestMapping(value="/updateOperatorPsp", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO updateOperatorPsp(@RequestBody List<OperatorPspEntity> operatorPspEntityList){
		System.out.println("Update Operator Request: "+operatorPspEntityList);
	
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
			Iterator<OperatorPspEntity> itr = operatorPspEntityList.iterator();
			while(itr.hasNext()){
				OperatorPspEntity operatorPspEntity = itr.next();
			operatorPspEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getOperatorId()) || operatorPspEntity.getOperatorId()==0) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspMerchantId())) {
			errors.put(KeyValueHolder.PSP_MERCHANT_ID_KEY, KeyValueHolder.PSP_MERCHANT_ID_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(errors)) {
			
			if(operatorPspService.updateOperatorPsp(operatorPspEntity)) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			} else{
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.ERROR_CODE);
				responseDTO.setMessage(ResponseStatusCode.ERROR_MSG);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
				return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
			
			}
			
		} else { 
			
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		}}
		
		return responseDTO;
	}
	
	@RequestMapping(value="/getOperatorPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO getOperatorPspList(@RequestBody OperatorPspEntity operatorPspEntity){
	
		System.out.println("getOperatorPSP Request: "+operatorPspEntity);
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
			operatorPspEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		try{
		if(CommonValidationUtility.isEmpty(t.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<OperatorPspEntity> operatorList = operatorPspService.getOperatorList(operatorPspEntity);
				System.out.println("operatorList size.. "+operatorList.size());
				if(operatorList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(operatorList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(operatorList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get operatorList.. "+e);
			}
			
				
		} else { 
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		
		} catch(Exception e){
			System.out.println("getOperatorPspList controller error.. "+e);
		}}
		return responseDTO;
	}

	@RequestMapping(value="/getAllPsp", method = RequestMethod.GET)
	@ResponseBody
    public ResponseDTO getAllPsp(){
	
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
			
//			operatorPspEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
		try{
		if(CommonValidationUtility.isEmpty(t.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<OperatorPspAll> operatorList = operatorPspService.getAllPsp(t.getOperatorId());
				System.out.println("operatorList size.. "+operatorList.size());
				if(operatorList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(operatorList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(operatorList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get operatorList.. "+e);
			}
			
				
		} else { 
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		
		} catch(Exception e){
			System.out.println("getOperatorPspList controller error.. "+e);
		}}
		return responseDTO;
	}

	@RequestMapping(value="/updateOperatorSequence", method = RequestMethod.PUT)
	@ResponseBody
    public ResponseDTO updateOperatorSequence(@RequestBody List<OperatorPspSequenceModel> operatorPspEntityList){
		System.out.println("Update Operator Request: "+operatorPspEntityList);
	
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
			Iterator<OperatorPspSequenceModel> itr = operatorPspEntityList.iterator();
			while(itr.hasNext()){
				OperatorPspSequenceModel sequenceModel = itr.next();
				OperatorPspEntity operatorPspEntityy = new OperatorPspEntity();
				
				operatorPspEntityy.setPspId(sequenceModel.getPspId());
				operatorPspEntityy.setIsActive(sequenceModel.getIsActive());
				operatorPspEntityy.setPaymentId(sequenceModel.getPaymentId());
				operatorPspEntityy.setPaymentType(sequenceModel.getPaymentType());
				operatorPspEntityy.setSequence(sequenceModel.getSequence());
			operatorPspEntityy.setOperatorId(Integer.parseInt(t.getOperatorId()));
		if(CommonValidationUtility.isEmpty(operatorPspEntityy.getOperatorId()) || operatorPspEntityy.getOperatorId()==0) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		
		
		if(CommonValidationUtility.isEmpty(operatorPspEntityy.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(errors)) {
			
			if(operatorPspService.updateOperatorSequence(operatorPspEntityy)) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			} else{
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.ERROR_CODE);
				responseDTO.setMessage(ResponseStatusCode.ERROR_MSG);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
				return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
			
			}
			
		} else { 
			
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		}}
		
		return responseDTO;
	}
}
