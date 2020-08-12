package com.limitlessmobility.iVendGateway.services.psp;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseStatusReturn;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.dao.psp.MachineConfigDao;
import com.limitlessmobility.iVendGateway.dao.psp.MachineConfigDaoImpl;
import com.limitlessmobility.iVendGateway.dao.psp.OperatorPspDao;
import com.limitlessmobility.iVendGateway.dao.psp.OperatorPspDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;
import com.limitlessmobility.iVendGateway.psp.model.CustomerlocationPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigRequest;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspAll;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.PspListRequest;

import codes.ResponseStatusCode;
import constants.KeyValueHolder;

@Controller
public class CommonPspConfig {

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/saveCommonPSP", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseDTO commonPspController(@RequestBody CommonPspModel commonPspModel) {
		
		System.out.println("saveCommonPSP Request is "+commonPspModel.toString());
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			TokenAuthModel t = new TokenAuthModel();
			Map<String, Object> errors = new HashMap<>();
			
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
//			if(false){
				System.out.println("token authentication failed!!");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
				responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
//				return ResponseUtility.sendResponse(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
			} else {
				String operatorId = t.getOperatorId();
//				String operatorId = "1";
			if(commonPspModel.getLabel().equalsIgnoreCase("customer")) {
				
				List<CustomerPspModel> customerPspModelList = new ArrayList<CustomerPspModel>();
				CustomerPspController customerPspController = new CustomerPspController();
				
				Iterator<CommonPspModelChild> itr = commonPspModel.getData().iterator();
				while(itr.hasNext()) {
					CommonPspModelChild commonPspModelChild = itr.next();
					CustomerPspModel customerPspModel = new CustomerPspModel();
					
					customerPspModel.setCustomerId(commonPspModel.getEntityId());
					customerPspModel.setIsActive(commonPspModelChild.getIsActive());
					
					customerPspModel.setOperationLocationId("0");
					customerPspModel.setOperatorId(operatorId);
					customerPspModel.setPspId(commonPspModelChild.getPspId());
					customerPspModel.setPspName(commonPspModelChild.getPspName());
					customerPspModel.setSequence(commonPspModelChild.getSequence());
					String isStatic=commonPspModelChild.getIsStatic();
					try{
						if(commonPspModelChild.getIsStatic().equalsIgnoreCase("static")) {
							isStatic="1";
						} else if(commonPspModelChild.getIsStatic().equalsIgnoreCase("dynamic")) {
							isStatic="0";
						}
					} catch (Exception e) {System.out.println();}
					
					customerPspModel.setIsStatic(isStatic);
					customerPspModelList.add(customerPspModel);
				}

				
				responseDTO = customerPspController.insertCustomerPsp(customerPspModelList);
				
			} else if(commonPspModel.getLabel().equalsIgnoreCase("customerLocation")) {
				List<CustomerlocationPspEntity> customerLocationModelList = new ArrayList<CustomerlocationPspEntity>();
				CustomerLocationPspController customerLocationPspController = new CustomerLocationPspController();
				
				Iterator<CommonPspModelChild> itr = commonPspModel.getData().iterator();
				while(itr.hasNext()) {
					CommonPspModelChild commonPspModelChild = itr.next();
					CustomerlocationPspEntity customerLocation = new CustomerlocationPspEntity();
					
					customerLocation.setCustomerLocationId(Integer.parseInt(commonPspModel.getEntityId()));
					customerLocation.setCustomerId(commonPspModel.getParentId());
					customerLocation.setIsActive(commonPspModelChild.getIsActive());
					String isStatic = "0";
					try {
						isStatic=commonPspModelChild.getIsStatic();
						if(isStatic.equalsIgnoreCase("static")) {
							isStatic="1";
						} else if(isStatic.equalsIgnoreCase("dynamic")) {
							isStatic="0";
						}
					} catch(Exception e) {}
					customerLocation.setIsStatic(Integer.parseInt(isStatic));
					customerLocation.setOperationLocationId(0);
					customerLocation.setOperatorId(Integer.parseInt(operatorId));
					customerLocation.setPspId(commonPspModelChild.getPspId());
					customerLocation.setPspName(commonPspModelChild.getPspName());
					customerLocation.setSequence(commonPspModelChild.getSequence());
					 
					customerLocationModelList.add(customerLocation);
				}
				
				responseDTO = customerLocationPspController.insertCustomerPsp(customerLocationModelList);
				
			} else if(commonPspModel.getLabel().equalsIgnoreCase("machine")) {
				List<MachineConfigRequest> customerLocationModelList = new ArrayList<MachineConfigRequest>();
				
				MachineConfigController machineConfigController = new MachineConfigController();
				MachineConfigDao machineConfigDao = new MachineConfigDaoImpl();
				MachineConfigRequest machineConfigRequest = new MachineConfigRequest();
				List<MachineConfigPaymentMode> listPaymentMode= new ArrayList<MachineConfigPaymentMode>();
				Iterator<CommonPspModelChild> itr = commonPspModel.getData().iterator();
				while(itr.hasNext()) {
					CommonPspModelChild commonPspModelChild = itr.next();
					
					MachineConfigPaymentMode paymentMode = new MachineConfigPaymentMode();
					
					paymentMode.setIsStatic(Integer.parseInt(commonPspModelChild.getIsStatic()));
					paymentMode.setMethodType(commonPspModelChild.getMethodType());
					paymentMode.setPspId(commonPspModelChild.getPspId());
					paymentMode.setPspName(commonPspModelChild.getPspName());
					paymentMode.setPspQrcode(commonPspModelChild.getPspQrcode());
					paymentMode.setPspTid(commonPspModelChild.getPspTid());
					paymentMode.setRefundMsg(commonPspModelChild.getRefundMsg());
					paymentMode.setSelected(commonPspModelChild.isSelected());
					paymentMode.setType(commonPspModelChild.getType());
					paymentMode.setSequence(commonPspModelChild.getSequence());
					paymentMode.setWalletType(commonPspModelChild.getWalletType());
					paymentMode.setIsActive(Integer.parseInt(commonPspModelChild.getIsActive()));
					
					machineConfigRequest.setCashboxId(commonPspModelChild.getCashboxId());
					machineConfigRequest.setCustomerId(commonPspModelChild.getCustomerId());
					machineConfigRequest.setCustomerLocationId(Integer.parseInt(commonPspModel.getParentId()));
					machineConfigRequest.setKeyboardType(commonPspModelChild.getKeyboardType());
					machineConfigRequest.setMachineId(Integer.parseInt(commonPspModel.getEntityId()));
					machineConfigRequest.setOperationLocationId(0);
					machineConfigRequest.setPspName(commonPspModelChild.getPspName());
					machineConfigRequest.setTelemetryId(commonPspModelChild.getTelemetryId());
					machineConfigRequest.setVposId(commonPspModelChild.getVposId());
//					machineConfigRequest.setSequence(commonPspModelChild.getSequence());
					machineConfigRequest.setCustomerId(commonPspModelChild.getCustomerId());
					machineConfigRequest.setOperatorId(Integer.parseInt(operatorId));
					listPaymentMode.add(paymentMode);
					
				}
				machineConfigRequest.setPaymentMode(listPaymentMode);
				machineConfigDao.deleteMachineConfigBeforeInsert(machineConfigRequest, operatorId);
				responseDTO = machineConfigController.insertMachineConfig(machineConfigRequest);
				
			}
			}} catch(Exception e) {}
		return responseDTO;
	}

	@RequestMapping(value="/getCommonPsp", method = RequestMethod.POST)
	@ResponseBody
    public ResponseDTO getPspList(@RequestBody CommonPspGetRequest commonPspGet){
		ResponseDTO responseDTO = new ResponseDTO();
		try {
			TokenAuthModel t = new TokenAuthModel();
			Map<String, Object> errors = new HashMap<>();
			
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
//			if(false){
				System.out.println("token authentication failed!!");
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.UNAUTHORISED_ACCESS);
				responseDTO.setMessage(ResponseStatusCode.UNAUTHORISED_ACCESS_MSG);
				responseDTO.setStatus(ResponseStatusCode.FAILURE_TEXT);
//				return ResponseUtility.sendResponse(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
			} else {
				String operatorId = t.getOperatorId();
//				String operatorId = "1";
				
			if(commonPspGet.getLabel().equalsIgnoreCase("customer")) {
				CommonPspFinalModel dataa = new CommonPspFinalModel();
				List<CommonPspFinalModel> dataListtt = new ArrayList<CommonPspFinalModel>();
//				List<CommonPspGetResponse> customerList = new ArrayList<CommonPspGetResponse>();
				if(commonPspGet.getCustomerId().equalsIgnoreCase("")) {
					List<CommonPspGetResponse> customerListt = new ArrayList<CommonPspGetResponse>();
					OperatorPspDao operatorDao = new OperatorPspDaoImpl();
					List<OperatorPspEntity> listOperator = operatorDao.getOperatorPspListPromo(operatorId);
					Iterator<OperatorPspEntity> itr = listOperator.iterator();
					while(itr.hasNext()) {
						OperatorPspEntity op = itr.next();
						CommonPspGetResponse availableCustomer = new CommonPspGetResponse();
						availableCustomer.setPspId(op.getPspId());
						availableCustomer.setSequence(op.getSequence());
						availableCustomer.setIsStatic(op.getPaymentType());
						availableCustomer.setIsActive(op.getIsActive());
						availableCustomer.setOperationLocationId(String.valueOf(op.getOperationLocationId()));
						availableCustomer.setImagePath(op.getImagePath());
						availableCustomer.setPspName(op.getPspName());
						customerListt.add(availableCustomer);
						dataa.setAvailable(customerListt);
						dataa.setSelected(null);
						
						
				    }
					dataListtt.add(dataa);
					
					
//					if()
					responseDTO.setStatusCode(200);					
					responseDTO.setStatus("success");
					responseDTO.setResponseData(dataListtt);
					return responseDTO;
				}
				CustomerPspController customerPspController = new CustomerPspController();
				
				CustomerPspModel customerPspModel = new CustomerPspModel();
				customerPspModel.setCustomerId(commonPspGet.getCustomerId());
				customerPspModel.setIsActive(commonPspGet.getIsActive());
				customerPspModel.setPspId(commonPspGet.getPspId());
				customerPspModel.setOperationLocationId(commonPspGet.getOperationLocationId());
				customerPspModel.setOperatorId(operatorId);
					
				
				responseDTO = customerPspController.getOperatorPspList(customerPspModel);
				ObjectMapper mapper = new ObjectMapper();
				 
				List<CommonPspFinalModel> dataList = new ArrayList<CommonPspFinalModel>();
				//Convert POJO to JSON
				String json = mapper.writeValueAsString(responseDTO.getResponseData());
				org.json.JSONArray jsonArr = new org.json.JSONArray(json);
				
				CommonPspFinalModel data = new CommonPspFinalModel();
				List<CommonPspGetResponse> customerList = new ArrayList<CommonPspGetResponse>();
				List<String> selectedList = new ArrayList<String>();
				for (int i = 0, size = jsonArr.length(); i < size; i++)
			    {
					
					JSONObject jsonObj = jsonArr.getJSONObject(i);
//					boolean available = jsonObj.getBoolean("available");
					
//					if(available) {
						CommonPspGetResponse selected = new CommonPspGetResponse();
						selected.setPspId(jsonObj.getString("pspId"));
						selected.setSequence(jsonObj.getInt("sequence"));
						selected.setIsStatic(jsonObj.getString("isStatic"));
						selected.setIsActive(jsonObj.getString("isActive"));
						selected.setOperationLocationId(jsonObj.getString("operationLocationId"));
//						availableCustomer.setIsActive(isActive);
						String isstatic="Dynamic";
						if(selected.getIsStatic().equalsIgnoreCase("1")) {
							isstatic="static";
						}
						selected.setIsStatic(isstatic);
						OperatorPspEntity op = CommonService.getPspImage(Integer.parseInt(operatorId), selected.getPspId());
						selected.setImagePath(op.getImagePath());
						selected.setPspName(jsonObj.getString("pspName"));
						
						String pspName = CommonUtil.lowerCaseString(jsonObj.getString("pspName"));
						
						selectedList.add(pspName);
						customerList.add(selected);
						data.setSelected(customerList);
						
//					} 
						
						
					}
				List<CommonPspGetResponse> customerListt = new ArrayList<CommonPspGetResponse>();
				OperatorPspDao operatorDao = new OperatorPspDaoImpl();
				List<OperatorPspEntity> listOperator = operatorDao.getOperatorPspListForCustomer(operatorId);
				Iterator<OperatorPspEntity> itr = listOperator.iterator();
				while(itr.hasNext()) {
					OperatorPspEntity op = itr.next();
					CommonPspGetResponse availableCustomer = new CommonPspGetResponse();
					availableCustomer.setPspId(op.getPspId());
					availableCustomer.setSequence(op.getSequence());
					if(op.getPspName().equalsIgnoreCase("Bharatqr Static")) {
						System.out.println(op.getPspName());
					}
					availableCustomer.setIsStatic(op.getPaymentType());
					/*String isstatic="Dynamic";
					if(availableCustomer.getIsStatic().equalsIgnoreCase("1")) {
						isstatic="static";
					}*/
					availableCustomer.setIsStatic(op.getPaymentType());
					availableCustomer.setIsActive(op.getIsActive());
					availableCustomer.setOperationLocationId(String.valueOf(op.getOperationLocationId()));
					availableCustomer.setImagePath(op.getImagePath());
					availableCustomer.setPspName(op.getPspName());
					
					
					
					String pspName = CommonUtil.lowerCaseString(op.getPspName());
					if(selectedList.contains(pspName)) {
						
					} else {
						customerListt.add(availableCustomer);
						data.setAvailable(customerListt);
					}
			    }
				dataList.add(data);
				
				
//				if()
				
				responseDTO.setResponseData(dataList);
				
			} else if(commonPspGet.getLabel().equalsIgnoreCase("customerLocation")) {
				
				
				
				
				
				
				CustomerLocationPspController customerPspController = new CustomerLocationPspController();
				
				
				PspListRequest pspList = new PspListRequest();
				pspList.setCustomerId(commonPspGet.getCustomerId());
				pspList.setCustomerLocationId(commonPspGet.getCustomerLocationId());
				pspList.setMachineId(commonPspGet.getMachineId());
				pspList.setType(commonPspGet.getType());
				pspList.setOperatorId(operatorId);
				
				CustomerlocationPspEntity  customerlocationModel = new CustomerlocationPspEntity();
				customerlocationModel.setCustomerId(commonPspGet.getCustomerId());
				customerlocationModel.setCustomerLocationId(Integer.parseInt(commonPspGet.getCustomerLocationId()));
				customerlocationModel.setIsActive(commonPspGet.getIsActive());
				customerlocationModel.setIsStatic(0);
//				if(commonPspGet.getIsStatic().equalsIgnoreCase("static")) {commonPspGet.setIsStatic("1");} else if(commonPspGet.getIsStatic().equalsIgnoreCase("dynamic")) {commonPspGet.setIsStatic("0");} else {customerlocationModel.setIsStatic(0);}
//				customerlocationModel.setIsStatic();
				customerlocationModel.setOperationLocationId(0);
				customerlocationModel.setOperatorId(Integer.parseInt(operatorId));
				customerlocationModel.setPspId(commonPspGet.getPspId());
				customerlocationModel.setPspName(commonPspGet.getPspId());
					
				
				responseDTO = customerPspController.getCustomerLocationList(customerlocationModel);
				ObjectMapper mapper = new ObjectMapper();
				 
				List<CommonPspFinalModel> dataList = new ArrayList<CommonPspFinalModel>();
				//Convert POJO to JSON
				String json = mapper.writeValueAsString(responseDTO.getResponseData());
				org.json.JSONArray jsonArr = new org.json.JSONArray(json);
				
				CommonPspFinalModel data = new CommonPspFinalModel();
				List<CommonPspGetResponse> customerList = new ArrayList<CommonPspGetResponse>();
				List<String> selectedList = new ArrayList<>();
				for (int i = 0, size = jsonArr.length(); i < size; i++)
			    {
					
					JSONObject jsonObj = jsonArr.getJSONObject(i);
//					boolean selectedd = jsonObj.getBoolean("selected");
					
//					if(selectedd) {
						CommonPspGetResponse selected = new CommonPspGetResponse();
						selected.setPspId(jsonObj.getString("pspId"));
						selected.setKeyboardType("");
						selected.setRefundMsg("");
						selected.setIsActive(jsonObj.getString("isActive"));
						selected.setPspName(jsonObj.getString("pspName"));
						
						selected.setPspQrcode("");
						selected.setSequence(jsonObj.getInt("sequence"));
						
						
						selected.setIsStatic(String.valueOf(jsonObj.getInt("isStatic")));
						String isstatic="Dynamic";
						if(selected.getIsStatic().equalsIgnoreCase("1")) {
							isstatic="static";
						}
						selected.setIsStatic(isstatic);
						selectedList.add(selected.getPspName());
						OperatorPspEntity op = CommonService.getPspImage(Integer.parseInt(operatorId), selected.getPspId());
						selected.setImagePath(op.getImagePath());
						if(jsonObj.getString("pspName").equalsIgnoreCase(null)) {
							selected.setPspName("");
						} else {
							selected.setPspName(jsonObj.getString("pspName"));
						}
						
						
//						customerList.add(selected);
						
						/*selected.setKeyboardType(jsonObj.getString("keyboardType"));
						selected.setRefundMsg(jsonObj.getString("refundMsg"));
						selected.setIsStatic(jsonob);*/
						customerList.add(selected);
						data.setSelected(customerList);
						
						
//					} else {
						
//					}
					
					
					
			    }
				List<CommonPspGetResponse> customerList1 = new ArrayList<CommonPspGetResponse>();
				OperatorPspDao operatorDao = new OperatorPspDaoImpl();
				List<CustomerPspModel> listOperator = operatorDao.getOperatorPspListForcustLocation(operatorId, Integer.parseInt(commonPspGet.getCustomerId().trim()));
				Iterator<CustomerPspModel> itr = listOperator.iterator();
				while(itr.hasNext()) {
					
					CustomerPspModel op = itr.next();
					
				CommonPspGetResponse availableCustomerr = new CommonPspGetResponse();
				availableCustomerr.setPspId(op.getPspId());
				availableCustomerr.setKeyboardType("");
				availableCustomerr.setPspName(op.getPspName());
				availableCustomerr.setIsActive(op.getIsActive());
				availableCustomerr.setIsStatic(op.getIsStatic());
				
				String isstatic="Dynamic";
				if(availableCustomerr.getIsStatic().equalsIgnoreCase("1")) {
					isstatic="static";
				}
				availableCustomerr.setIsStatic(isstatic);
				availableCustomerr.setRefundMsg("");
//				availableCustomerr.setIsStatic(op.getPaymentType());
				availableCustomerr.setPspQrcode("");
				availableCustomerr.setSequence(op.getSequence());
				
				OperatorPspEntity opp = CommonService.getPspImage(Integer.parseInt(operatorId), availableCustomerr.getPspId());
				availableCustomerr.setImagePath(opp.getImagePath());
				availableCustomerr.setPspName(op.getPspName());
				
				if(selectedList.contains(availableCustomerr.getPspName())) {
					
				} else {
					customerList1.add(availableCustomerr);
					data.setAvailable(customerList1);
				}
				OperatorPspEntity op1 = CommonService.getPspImage(Integer.parseInt(operatorId), availableCustomerr.getPspId());
				availableCustomerr.setImagePath(op1.getImagePath());
				}
				dataList.add(data);
//				if()
				
				responseDTO.setResponseData(dataList);
				
			} else if(commonPspGet.getLabel().equalsIgnoreCase("machine")) {
				CustomerLocationPspController customerPspController = new CustomerLocationPspController();
				
				
				PspListRequest pspList = new PspListRequest();
				pspList.setCustomerId(commonPspGet.getCustomerId());
				pspList.setCustomerLocationId(commonPspGet.getCustomerLocationId());
				pspList.setMachineId(commonPspGet.getMachineId());
				pspList.setType(commonPspGet.getType());
				pspList.setOperatorId(operatorId);
				
				CustomerlocationPspEntity  customerlocationModel = new CustomerlocationPspEntity();
				customerlocationModel.setCustomerId(commonPspGet.getCustomerId());
				customerlocationModel.setCustomerLocationId(Integer.parseInt(commonPspGet.getCustomerLocationId()));
				customerlocationModel.setIsActive(commonPspGet.getIsActive());
//				customerlocationModel.setIsStatic(0);
//				if(commonPspGet.getIsStatic().equalsIgnoreCase("static")) {commonPspGet.setIsStatic("1");} else if(commonPspGet.getIsStatic().equalsIgnoreCase("dynamic")) {commonPspGet.setIsStatic("0");} else {customerlocationModel.setIsStatic(0);}
//				customerlocationModel.setIsStatic();
				customerlocationModel.setOperationLocationId(0);
				customerlocationModel.setOperatorId(Integer.parseInt(operatorId));
				customerlocationModel.setPspId(commonPspGet.getPspId());
				customerlocationModel.setPspName(commonPspGet.getPspId());
					
				
				responseDTO = customerPspController.getCustLocationList(pspList);
				ObjectMapper mapper = new ObjectMapper();
				 
				List<CommonPspFinalModel> dataList = new ArrayList<CommonPspFinalModel>();
				//Convert POJO to JSON
				String json = mapper.writeValueAsString(responseDTO.getResponseData());
				org.json.JSONArray jsonArr = new org.json.JSONArray(json);
				
				CommonPspFinalModel data = new CommonPspFinalModel();
				List<CommonPspGetResponse> customerList = new ArrayList<CommonPspGetResponse>();
				List<CommonPspGetResponse> customerList1 = new ArrayList<CommonPspGetResponse>();
				List<String> selectedList = new ArrayList<String>();
				for (int i = 0, size = jsonArr.length(); i < size; i++)
			    {
					
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					boolean selectedd = jsonObj.getBoolean("selected");
					
					if(selectedd) {
						CommonPspGetResponse selected = new CommonPspGetResponse();
						selected.setPspId(jsonObj.getString("pspId"));
						selected.setKeyboardType("");
						selected.setRefundMsg("");
						selected.setPspName(jsonObj.getString("pspName"));
						selected.setIsStatic(String.valueOf(jsonObj.getInt("isStatic")));
						selected.setPspQrcode(jsonObj.getString("pspQrcode"));
						selected.setPspTid(jsonObj.getString("pspTid"));
						selected.setIsActive(String.valueOf(jsonObj.getInt("isActive")));
						
						String isstatic="Dynamic";
						if(selected.getIsStatic().equalsIgnoreCase("1")) {
							isstatic="static";
						}
						OperatorPspEntity op = CommonService.getPspImage(Integer.parseInt(operatorId), selected.getPspId());
						selected.setImagePath(op.getImagePath());
						selected.setPspName(jsonObj.getString("pspName"));
						
						/*selected.setKeyboardType(jsonObj.getString("keyboardType"));
						selected.setRefundMsg(jsonObj.getString("refundMsg"));
						selected.setIsStatic(jsonob);*/
						selectedList.add(selected.getPspName());
						customerList.add(selected);
						data.setSelected(customerList);
						
					} else {
						CommonPspGetResponse avalable = new CommonPspGetResponse();
						avalable.setPspId(jsonObj.getString("pspId"));
						avalable.setKeyboardType("");
						avalable.setRefundMsg("");
						avalable.setPspName(jsonObj.getString("pspName"));
						avalable.setIsStatic(String.valueOf(jsonObj.getInt("isStatic")));
						avalable.setPspQrcode("");
						avalable.setIsActive(String.valueOf(jsonObj.getInt("isActive")));
						
						String isstatic="Dynamic";
						if(avalable.getIsStatic().equalsIgnoreCase("1")) {
							isstatic="static";
						}
						OperatorPspEntity op = CommonService.getPspImage(Integer.parseInt(operatorId), avalable.getPspId());
						avalable.setImagePath(op.getImagePath());
						avalable.setPspName(jsonObj.getString("pspName"));
						
						/*selected.setKeyboardType(jsonObj.getString("keyboardType"));
						selected.setRefundMsg(jsonObj.getString("refundMsg"));
						selected.setIsStatic(jsonob);*/
						
						if(selectedList.contains(avalable.getPspName())) {
							
						} else {
							customerList1.add(avalable);
							data.setAvailable(customerList1);
						}
						
						
						
					}
				/*OperatorPspDao operatorDao = new OperatorPspDaoImpl();
				List<OperatorPspEntity> listOperator = operatorDao.getOperatorPspListPromo(operatorId);
				Iterator<OperatorPspEntity> itr = listOperator.iterator();
				while(itr.hasNext()) {
					
					OperatorPspEntity op = itr.next();
					
				CommonPspGetResponse availableCustomerr = new CommonPspGetResponse();
				availableCustomerr.setPspId(op.getPspId());
				availableCustomerr.setKeyboardType("");
				availableCustomerr.setImagePath(op.getImagePath());
				availableCustomerr.setPspName(op.getPspName());
				availableCustomerr.setIsActive(op.getIsActive());
				availableCustomerr.setIsStatic(op.getPaymentType());
				availableCustomerr.setRefundMsg("");
				availableCustomerr.setIsStatic(op.getPaymentType());
				availableCustomerr.setPspQrcode("");
				customerList1.add(availableCustomerr);
				data.setAvailable(customerList1);
				dataList.add(data);
				}*/
				
//				if()
				
				
			    }
				dataList.add(data);
				responseDTO.setResponseData(dataList);
			}
			}} catch(Exception e) {
				System.out.println(e);
			}
		return responseDTO;
	}
}
