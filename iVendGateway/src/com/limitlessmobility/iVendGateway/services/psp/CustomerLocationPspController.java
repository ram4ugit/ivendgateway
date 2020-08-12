package com.limitlessmobility.iVendGateway.services.psp;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.util.ArrayList;
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
import com.limitlessmobility.iVendGateway.controller.psp.CustomerLocationPspServiceImpl;
import com.limitlessmobility.iVendGateway.controller.psp.OperatorPspServiceImpl;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.psp.model.CustomerlocationPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.PspListRequest;

import constants.KeyValueHolder;

@Controller
public class CustomerLocationPspController {

	CustomerLocationPspServiceImpl customerLocationPspService = new CustomerLocationPspServiceImpl();
	
	OperatorPspServiceImpl operatorPspService = new OperatorPspServiceImpl();
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/saveCustomerLocationPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO insertCustomerPsp(@RequestBody List<CustomerlocationPspEntity> customerlocationPspList){
	
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
			Iterator<CustomerlocationPspEntity> itr = customerlocationPspList.iterator();
			while(itr.hasNext()){
				CustomerlocationPspEntity customerlocationPspEntity = itr.next();
			customerlocationPspEntity.setOperatorId(customerlocationPspEntity.getOperatorId());
			
		/*if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getOperatorId()) || customerlocationPspEntity.getOperatorId()==0) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getCustomerLocationId()) || customerlocationPspEntity.getCustomerLocationId()==0) {
			errors.put(KeyValueHolder.CUSTOMER_LOCATION_ID_KEY, KeyValueHolder.CUSTOMER_LOCATION_ID_MGS);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getCustomerId())) {
			errors.put(KeyValueHolder.CUSTOMER_ID_KEY, KeyValueHolder.CUSTOMER_ID_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getIsStatic())) {
			errors.put(KeyValueHolder.IS_STATIC_KEY, KeyValueHolder.IS_STATIC_MSG);
		} */
		
		if(CommonValidationUtility.isEmpty(errors)) {
			
			if(customerLocationPspService.saveCustomerLocationPsp(customerlocationPspEntity)) 
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
		
	}
//	}
		return responseDTO;
	}
	
	@RequestMapping(value="/updateCustomerLocationPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO updateCustomerPsp(@RequestBody List<CustomerlocationPspEntity> customerlocationPspList){
	
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
			Iterator<CustomerlocationPspEntity> itr = customerlocationPspList.iterator();
			while(itr.hasNext()){
				CustomerlocationPspEntity customerlocationPspEntity = itr.next();
			customerlocationPspEntity.setOperatorId(Integer.parseInt(t.getOperatorId()));
			
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getOperatorId()) || customerlocationPspEntity.getOperatorId()==0) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getCustomerLocationId()) || customerlocationPspEntity.getCustomerLocationId()==0) {
			errors.put(KeyValueHolder.CUSTOMER_LOCATION_ID_KEY, KeyValueHolder.CUSTOMER_LOCATION_ID_MGS);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getPspId())) {
			errors.put(KeyValueHolder.PSP_ID_KEY, KeyValueHolder.PSP_ID_KEY_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getCustomerId())) {
			errors.put(KeyValueHolder.CUSTOMER_ID_KEY, KeyValueHolder.CUSTOMER_ID_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(customerlocationPspEntity.getIsStatic())) {
			errors.put(KeyValueHolder.IS_STATIC_KEY, KeyValueHolder.IS_STATIC_MSG);
		} 
		
		if(CommonValidationUtility.isEmpty(errors)) {
			boolean updateStatus = customerLocationPspService.updateCustomerLocationPsp(customerlocationPspEntity);
			if(updateStatus) 
			{
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("true");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			} else if(!updateStatus){
				ResponseStatusReturn responseStatus = new ResponseStatusReturn();
				responseStatus.setStatus("false");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.EXIST_NOT_MSG);
				responseDTO.setMessage(null);
				responseDTO.setResponseData(responseStatus);
			}else { 
				
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.VALIDATION_FAILED_CODE);
				responseDTO.setMessage(ResponseStatusCode.VALIDATION_FAILED_TEXT);
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
		
	}
	}
		return responseDTO;
	}
	
	
	@RequestMapping(value="/getCustLocationPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO getCustLocationList(@RequestBody PspListRequest pspListRequest){
	
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
			Integer operatorId = Integer.parseInt(pspListRequest.getOperatorId());
			
		try{
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<MachineConfigPaymentMode> pspListModelList = new ArrayList<MachineConfigPaymentMode>();
				List<MachineConfigPaymentMode> machineConfigPaymentModeList=new ArrayList<MachineConfigPaymentMode>();
				
				
					machineConfigPaymentModeList = customerLocationPspService.getCustLocationPspListt(pspListRequest.getCustomerId(), pspListRequest.getCustomerLocationId(), pspListRequest.getMachineId(), operatorId);
				
				if(machineConfigPaymentModeList.size()>0){
					Iterator<MachineConfigPaymentMode> itr = machineConfigPaymentModeList.iterator();
					while(itr.hasNext()){
						MachineConfigPaymentMode machineConfigPaymentMode = itr.next();
						

//				        StringTokenizer pspIdWithottComma = new StringTokenizer(machineConfigPaymentMode.getPspId(), ",");
				        
						
						OperatorPspEntity operatorPspEntity = new OperatorPspEntity();
						operatorPspEntity.setPspId(machineConfigPaymentMode.getPspId());
						operatorPspEntity.setOperatorId(operatorId);
						
						if(machineConfigPaymentMode.getMethodType().equalsIgnoreCase("new")){
						
						List<OperatorPspEntity> operatorPspList = operatorPspService.getOperatorList(operatorPspEntity);
						Iterator<OperatorPspEntity> operatorPspListItr = operatorPspList.iterator();
						
						
						
						while(operatorPspListItr.hasNext()){
							MachineConfigPaymentMode pspModel = new MachineConfigPaymentMode();
							pspModel.setPspId(machineConfigPaymentMode.getPspId());
							
							
							if(machineConfigPaymentMode.getType()==null){machineConfigPaymentMode.setType("");};
							pspModel.setType(machineConfigPaymentMode.getType());
							
							if(machineConfigPaymentMode.getPspTid()==null){machineConfigPaymentMode.setPspTid("");};
							pspModel.setPspTid(machineConfigPaymentMode.getPspTid());
							
							if(machineConfigPaymentMode.getRefundMsg()==null){machineConfigPaymentMode.setRefundMsg("");};
							pspModel.setRefundMsg(machineConfigPaymentMode.getRefundMsg());
							
							if(machineConfigPaymentMode.getPspQrcode()==null){machineConfigPaymentMode.setPspQrcode("");};
							pspModel.setPspQrcode(machineConfigPaymentMode.getPspQrcode());
							
							OperatorPspEntity operatorPsp = operatorPspListItr.next();
							
							pspModel.setWalletType(operatorPsp.getWalletType());
							pspModel.setSelected(machineConfigPaymentMode.isSelected());
							pspModel.setWalletType(machineConfigPaymentMode.getWalletType());
							
							/*if(machineConfigPaymentMode.getIsStatic()!=null){
								if(machineConfigPaymentMode.getIsStatic()==1){
									pspModel.setIsStatic(1);
								} else {
									pspModel.setIsStatic(0);
								}
							} else{*/
								String paymentType = operatorPsp.getPaymentType();
								
								if(paymentType.equalsIgnoreCase("dynamic")){
									pspModel.setIsStatic(0);
								} else {
									pspModel.setIsStatic(1);
								}
//							}
							pspModel.setPspName(machineConfigPaymentMode.getPspName());
							pspListModelList.add(pspModel);
						}
					}
						else  {
							
//							List<OperatorPspEntity> operatorPspList = operatorPspService.getOperatorList(operatorPspEntity);
//							Iterator<OperatorPspEntity> operatorPspListItr = operatorPspList.iterator();

							MachineConfigPaymentMode pspModel = new MachineConfigPaymentMode();
							pspModel.setPspId(machineConfigPaymentMode.getPspId());
							
							if(machineConfigPaymentMode.getType()==null){machineConfigPaymentMode.setType("");};
							pspModel.setType(machineConfigPaymentMode.getType());
							
							if(machineConfigPaymentMode.getPspTid()==null){machineConfigPaymentMode.setPspTid("");};
							pspModel.setPspTid(machineConfigPaymentMode.getPspTid());
							
							pspModel.setIsActive(machineConfigPaymentMode.getIsActive());
							
							if(machineConfigPaymentMode.getRefundMsg()==null){machineConfigPaymentMode.setRefundMsg("");};
							pspModel.setRefundMsg(machineConfigPaymentMode.getRefundMsg());
							
							if(machineConfigPaymentMode.getPspQrcode()==null){machineConfigPaymentMode.setPspQrcode("");};
							pspModel.setPspQrcode(machineConfigPaymentMode.getPspQrcode());
							
							pspModel.setPspName(machineConfigPaymentMode.getPspName());
							
							
							pspModel.setIsStatic(machineConfigPaymentMode.getIsStatic());
							
//							OperatorPspEntity operatorPsp = operatorPspListItr.next();
							
//							pspModel.setWalletType(operatorPsp.getWalletType());
//							pspModel.setSelected(true);
							pspModel.setSelected(machineConfigPaymentMode.isSelected());
							pspModel.setWalletType(machineConfigPaymentMode.getWalletType());
								/*String paymentType = machineConfigPaymentMode.getType();
								
								if(paymentType.equalsIgnoreCase("dynamic")){
									pspModel.setIsStatic(0);
								} else {
									pspModel.setIsStatic(1);
								}*/
							pspListModelList.add(pspModel);
						
							
							
							
							
						}
				        }
					}
				
				
				if(pspListModelList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage("");
					responseDTO.setResponseData(pspListModelList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(pspListModelList);
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
		}
//	}
		return responseDTO;
	}
	
	@RequestMapping(value="/getCustomerLocationPSP", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO getCustomerLocationList(@RequestBody CustomerlocationPspEntity customerLocationPsp){
	
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
			customerLocationPsp.setOperatorId(customerLocationPsp.getOperatorId());
		try{
		if(CommonValidationUtility.isEmpty(customerLocationPsp.getOperatorId())) {
			errors.put(KeyValueHolder.OPERATOR_ID_KEY, KeyValueHolder.OPERATOR_ID_MSG);
		}
		
		if(CommonValidationUtility.isEmpty(errors)) {
			try {
				List<CustomerlocationPspEntity> customerLocationPspList = customerLocationPspService.getCustomerLocationPspList(customerLocationPsp);
				if(customerLocationPspList.size()>0){
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(null);
					responseDTO.setResponseData(customerLocationPspList);
				} else {
					responseDTO.setErrors(errors);
					responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
					responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
					responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
					responseDTO.setResponseData(customerLocationPspList);
				}
			} catch (Exception e) {
				System.out.println("Some issue in get customerLocationPspList.. "+e);
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
			System.out.println("customerLocationPspList controller error.. "+e);
		}
//	}
		System.out.println(responseDTO.toString());
		return responseDTO;
	}
}
