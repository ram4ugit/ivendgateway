package com.limitlessmobility.iVendGateway.services.common;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import BlockAmountRequest.PaytmWithdrawRequest;
import codes.ResponseStatusCode;

import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.common.PayPendingRequest;
import com.limitlessmobility.iVendGateway.model.common.ReleaseAmountRequest;
import com.limitlessmobility.iVendGateway.model.common.SettleAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.CaptureCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.SettleCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.VoidTransactionRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmReleaseRequest;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.services.wallet.WalletPayPendingService;
import com.limitlessmobility.iVendGateway.services.wallet.WalletReleaseAmountService;
import com.limitlessmobility.iVendGateway.services.wallet.WalletSettleAmountService;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.PaytmReleaseAmountService;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.PaytmWithdrawService;

@RestController
@RequestMapping(value="/v2")
public class CommonSettleAmountService {
	
	@Autowired
	private HttpServletRequest request;

	/*
	 * This Method is used to block wallet amount
	 */
	@RequestMapping(value = "/commonSettle", method = RequestMethod.POST)
	@ResponseBody
	public String settleAmount(@RequestBody SettleAmountRequest settleAmountRequest) throws JSONException{
		System.out.println("Request commonSettle.. "+settleAmountRequest.toString());
		JSONObject finalResponse = new JSONObject();
		String response = new String();
		try{
			/*TokenAuthModel t = new TokenAuthModel();
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
			t = JwtTokenDecode.validateJwt(token);
			if(CommonValidationUtility.isEmpty(t.getOperatorId())){
				System.out.println("token authentication failed!!");
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Access Denied");
				finalResponse.put("status", "Failure");
			} else {*/
				int operatorId = settleAmountRequest.getOperatorId();
				
			String pspId = settleAmountRequest.getPspId();
			if(pspId.equalsIgnoreCase("paytm")){
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, pspId, "wallet");
				
				PaytmWithdrawRequest p = new PaytmWithdrawRequest();
				p.setAppIP("1.1.1.1");
				p.setAuthMode("USRPWD");
				p.setChannel("WAP");
				p.setCurrency("INR");
				p.setCustId(settleAmountRequest.getCustId());
				p.setDeviceId(settleAmountRequest.getMobileNo());
				p.setIndustryType("Retail");
				p.setMerchantKey(op.getPspMerchantKey());
				p.setMid(op.getPspMerchantId());
				p.setOrderId(settleAmountRequest.getOrderId());
				p.setPaymentMode("PPI");
	
				p.setPreAuthId(CommonService.getTransactionId(settleAmountRequest.getOrderId()));
				p.setReqType("CAPTURE");
				p.setSsoToken(settleAmountRequest.getWalletAccountKey());
				p.setTxnAmount(String.valueOf(settleAmountRequest.getAmount()));
				
				PaytmWithdrawService settleService = new PaytmWithdrawService();
				response = settleService.withdrawAmount(p);
				return response;
			}else{
				WalletSettleAmountService w = new WalletSettleAmountService();
				
				response = w.settleWalletAmount(settleAmountRequest);
				return response;
			}
			
//			}
		} catch(Exception e){
			
		}
		
		return finalResponse.toString();
	}
	
	/*
	 * This Method is used to block wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/commonPay", method = RequestMethod.POST)
	@ResponseBody
	public String payAmount(@RequestBody SettleAmountRequest settleAmountRequest) throws JSONException{
		System.out.println("Request commonPay.. "+settleAmountRequest.toString());
		JSONObject finalResponse = new JSONObject();
		String response = new String();
		try{
			TokenAuthModel t = new TokenAuthModel();
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
			try{
				t = JwtTokenDecode.validateJwt(token);
			} catch(Exception e){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Access Denied");
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			}
			if(CommonValidationUtility.isEmpty(t.getOperatorId())){
				System.out.println("token authentication failed!!");
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Access Denied");
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			} else {
				int operatorId = Integer.parseInt(t.getOperatorId());
				
			String pspId = settleAmountRequest.getPspId();
			PaymentController paymentController = new PaymentController();
			boolean isExistOrderId = paymentController.getTransactionExistByOrder(settleAmountRequest.getOrderId());
			if(!isExistOrderId){
				finalResponse.put("responseObj", "");
				finalResponse.put("message", "Order id doesn't exist");
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			} 
			if(pspId.equalsIgnoreCase("paytm")){
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, pspId, "wallet");
				
				PaytmWithdrawRequest p = new PaytmWithdrawRequest();
				p.setAppIP("1.1.1.1");
				p.setAuthMode("USRPWD");
				p.setChannel("WAP");
				p.setCurrency("INR");
				p.setCustId(settleAmountRequest.getCustId());
				p.setDeviceId(settleAmountRequest.getMobileNo());
				p.setIndustryType("Retail");
				p.setMerchantKey(op.getPspMerchantKey());
				p.setMid(op.getPspMerchantId());
				p.setOrderId(CommonService.getTransactionId(settleAmountRequest.getOrderId()));
				p.setPaymentMode("PPI");
	
				p.setPreAuthId(CommonService.getTransactionId(settleAmountRequest.getOrderId()));
				p.setReqType("WITHDRAW");
				p.setSsoToken(settleAmountRequest.getWalletAccountKey());
				p.setTxnAmount(String.valueOf(settleAmountRequest.getAmount()));
				
				PaytmWithdrawService settleService = new PaytmWithdrawService();
				response = settleService.payPendingAmount(p, settleAmountRequest.getOrderId());
				return response;
			}else{
				WalletPayPendingService payService = new WalletPayPendingService();
				PayPendingRequest pp = new PayPendingRequest();
				pp.setAmount(settleAmountRequest.getAmount());
				pp.setOrderId(settleAmountRequest.getOrderId());
				pp.setPspId(pspId);
				pp.setSource(settleAmountRequest.getSource());
				pp.setTransactionRefNo(CommonService.getTransactionId(settleAmountRequest.getOrderId()));
				pp.setWalletAccountId(settleAmountRequest.getWalletAccountId());
				pp.setWalletAccountKey(settleAmountRequest.getWalletAccountKey());
				pp.setWalletId(settleAmountRequest.getWalletId());
				pp.setWalletKey(settleAmountRequest.getWalletKey());
				response = payService.payPendingWalletAmount(pp, operatorId);
				return response;
			}
			
			}
		} catch(Exception e){
			
		}
		
		return finalResponse.toString();
	}

	/*
	 * This Method is used to block wallet amount
	 * ID.
	 */
	/*@RequestMapping(value = "/settleAll", method = RequestMethod.POST)
	@ResponseBody
	public String settleAll(@RequestBody SettleCardRequest settleRequest) throws JSONException{
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse = new JSONObject();
		try{
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
				try {
		            return ResponseUtility.sendResponseString(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
	            } catch (JSONException e) {
	            	try {
		                finalResponse.put("responseObj", "null");
		            finalResponse.put("message", "Token Validate Error in CardValidateService");
					finalResponse.put("status","ERROR");
					return finalResponse.toString();
	            	} catch (JSONException e1) {}
	            }
			} else {
		if(requestModel.getStatus().equalsIgnoreCase("Success")){
			CaptureCardRequest captureRequest = new CaptureCardRequest();
			captureRequest.setAmount(requestModel.getAmount());
			captureRequest.setRefNo(requestModel.getRefNo());
			captureRequest.setTerminalId(requestModel.getTerminalId());
			return captureCard(captureRequest);
		} else if(requestModel.getStatus().equalsIgnoreCase("Fail")){
			VoidTransactionRequest voidRequest = new VoidTransactionRequest();
			voidRequest.setRefId(requestModel.getRefNo());
			voidRequest.setTerminalId(requestModel.getTerminalId());
			return voidTransactionUpdate(voidRequest);
		} }} catch(Exception e){
			
		}
		
		return "";
		}*/
}
