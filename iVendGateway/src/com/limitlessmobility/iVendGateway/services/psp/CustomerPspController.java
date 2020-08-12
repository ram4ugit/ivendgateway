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
import org.springframework.web.bind.annotation.RestController;

import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseStatusReturn;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.psp.CustomerPspServiceImpl;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;

import constants.KeyValueHolder;

@Controller
public class CustomerPspController {

	CustomerPspServiceImpl customerPspService = new CustomerPspServiceImpl();
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/saveCustomerPSP", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
    public ResponseDTO insertCustomerPsp(@RequestBody List<CustomerPspModel> customerPspList){
	
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		
		
			Iterator<CustomerPspModel> itr = customerPspList.iterator();
			while(itr.hasNext()){
				CustomerPspModel customerPspModel = itr.next();
			customerPspModel.setOperatorId(customerPspModel.getOperatorId());
		
		/*if(CommonValidationUtility.isEmpty(customerPspModel.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		if(CommonValidationUtility.isEmpty(customerPspModel.getCustomerId())) {
			errors.put(KeyValueHolder.CUSTOMER_ID_KEY, KeyValueHolder.CUSTOMER_ID_MSG);
		}
		if(CommonValidationUtility.isEmpty(customerPspModel.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(customerPspModel.getIsStatic())) {
			errors.put(KeyValueHolder.IS_STATIC_KEY, KeyValueHolder.IS_STATIC_MSG);
		} */
		
		if(CommonValidationUtility.isEmpty(errors)) {
			boolean saveStatus = customerPspService.saveCustomerPsp(customerPspModel);
			if(saveStatus) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			}else if(!saveStatus){
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("false");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.EXIST_MSG);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			}else { 
				
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
				responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
				return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
			}
		} 
	/*}*/
		}
		
		return responseDTO;
	}
	
	
	@RequestMapping(value="/updateCustomerPSP", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
	public ResponseDTO updateCustomerPsp(@RequestBody List<CustomerPspModel> customerPspList){
	
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
			Iterator<CustomerPspModel> itr = customerPspList.iterator();
			while(itr.hasNext()){
				CustomerPspModel customerPspModel = itr.next();
			customerPspModel.setOperatorId(t.getOperatorId());
		
		if(CommonValidationUtility.isEmpty(customerPspModel.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		if(CommonValidationUtility.isEmpty(customerPspModel.getCustomerId())) {
			errors.put(KeyValueHolder.CUSTOMER_ID_KEY, KeyValueHolder.CUSTOMER_ID_MSG);
		}
		if(CommonValidationUtility.isEmpty(customerPspModel.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		/*if(CommonValidationUtility.isEmpty(customerPspModel.getOperationLocationId()) || customerPspModel.getOperationLocationId()==0) {
			errors.put(KeyValueHolder.OPERATION_LOCATION_ID_KEY, KeyValueHolder.OPERATION_LOCATION_ID_MSG);
		}*/
		
		
		
		
		
		
		if(CommonValidationUtility.isEmpty(errors)) {
			
			if(customerPspService.updateCustomerPsp(customerPspModel)) 
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
			System.out.println(responseDTO.toString());
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
	}}
		System.out.println(responseDTO.toString());
		return responseDTO;
	}
	
	@RequestMapping(value="/getCustomerPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO getOperatorPspList(@RequestBody CustomerPspModel customerPspModel){
	
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		
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
//			customerPspModel.setOperatorId(customerPspModel.getOperatorId());
		try{
		/*if(CommonValidationUtility.isEmpty(customerPspModel.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		}*/
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<CustomerPspModel> customerPspList = customerPspService.getCustomerList(customerPspModel);
				if(customerPspList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(customerPspList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(customerPspList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get operatorList.. "+e);
			}
			
				
		} else { 
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
			System.out.println(responseDTO.toString());
			return ResponseUtility.sendResponse(ResponseStatusCode.VALIDATION_FAILED_CODE, ResponseStatusCode.VALIDATION_FAILED_TEXT, errors, "false");
		}
		
		} catch(Exception e){
			System.out.println("getOperatorPspList controller error.. "+e);
		}
//	}
		System.out.println(responseDTO.toString());
		return responseDTO;
	}
}
