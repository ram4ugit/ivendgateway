package com.limitlessmobility.iVendGateway.services.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.dto.ResponseDTOObj;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.TransactionFilterDao;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryFileData;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryRequestModel;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryResponseData;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryResponseModel;
import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import codes.ResponseStatusCode;

	@Controller
	@RequestMapping(value = "/v2")
	public class PaymentHistoryReport {

		@Autowired
		private HttpServletRequest request;
		
		@RequestMapping(value = "/paymentHistoryReport", method = RequestMethod.POST)
		@ResponseBody
		public ResponseDTOObj paymentHistoryReport(@RequestBody PaymentHistoryRequestModel paymentHistoryRequestModel){
			System.out.println("paymentHistoryReport request "+paymentHistoryRequestModel.toString());
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
			
			PaymentHistoryResponseModel transactionFilterResponse = tdao.getTransactionReport(paymentHistoryRequestModel, t.getOperatorId());
			
			
			if (paymentHistoryRequestModel.getDatatype().equalsIgnoreCase("list")) {

				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(null);
				responseDTO.setResponseObj(transactionFilterResponse);
			} else if (paymentHistoryRequestModel.getDatatype().equalsIgnoreCase("file")) {

				try
	            {   
			            responseDTO.setErrors(errors);
						responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
						responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
						responseDTO.setMessage(null);
						
						PaymentHistoryFileData paymentHistoryFileData = new PaymentHistoryFileData();
						paymentHistoryFileData.setPath("http://139.59.73.155/iVendGateway/download/report");
//						paymentHistoryFileData.setPath("http://139.59.7.72/iVendGateway/download/report");
						responseDTO.setResponseObj(paymentHistoryFileData);
			           
			       } 
			       catch (Exception e) 
			       {
			           e.printStackTrace();
			       }
			}
			else {
				responseDTO.setErrors(errors);
				responseDTO.setStatusCode(ResponseStatusCode.OK_CODE);
				responseDTO.setStatus(ResponseStatusCode.OK_TEXT);
				responseDTO.setMessage(ResponseStatusCode.NO_RECORD);
				responseDTO.setResponseObj(null);
			}
			}
			return responseDTO;
		}
		
		
		
	}
