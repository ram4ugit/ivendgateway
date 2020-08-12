package com.limitlessmobility.iVendGateway.services.common;

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
import com.limitlessmobil.ivendgateway.dto.ResponseDTOObj;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseStatusReturn;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.TransactionFilterDao;
import com.limitlessmobility.iVendGateway.model.common.TransactionFilterRequest;
import com.limitlessmobility.iVendGateway.model.common.TransactionFilterResponse;

@Controller
@RequestMapping(value = "/v2")
public class TransactionFilterService {

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value = "/getTransactionByOrderId", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDTOObj commonCheckStatus(@RequestBody TransactionFilterRequest transactionFilterRequest){
		System.out.println("getTransactionByOrderId "+transactionFilterRequest.toString());
		Map<String, Object> errors = new HashMap<>();
		ResponseDTOObj responseDTO = new ResponseDTOObj();
		
		TransactionFilterDao tdao = new TransactionFilterDao();
		
		
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
			return responseDTO;
		} else {
		/*String orderId = transactionFilterResponse.getOrderId();
		boolean isEmpty = orderId == null || orderId.length() == 0;*/
		List<TransactionFilterResponse> transactionFilterResponse = tdao.getTransactionByOrder(transactionFilterRequest.getOrderId(), t.getOperatorId());
		if (transactionFilterResponse.size()>0) {

			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
			responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
			responseDTO.setMessage(null);
			responseDTO.setResponseObj(transactionFilterResponse);
		} else {
			responseDTO.setErrors(errors);
			responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
			responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
			responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
			responseDTO.setResponseObj(null);
		}}
		return responseDTO;
	}
	
}
