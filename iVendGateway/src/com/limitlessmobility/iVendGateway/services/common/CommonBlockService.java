package com.limitlessmobility.iVendGateway.services.common;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.common.BlockRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.BlockAmountRequest;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.services.wallet.WalletBlockService;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.PaytmBlockAmountService;

@RestController
@RequestMapping(value="/v2")
public class CommonBlockService {
	
	@Autowired
	private HttpServletRequest request;

	/*
	 * This Method is used to block wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/commonBlockAmount", method = RequestMethod.POST)
	@ResponseBody
	public String blockWalletAmount(@RequestBody BlockRequest blockRequest) throws JSONException{
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
				
			String pspId = blockRequest.getPspId();
			if(pspId.equalsIgnoreCase("paytm")){
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, blockRequest.getPspId(), "wallet");
				
				BlockAmountRequest paytmBlockRequest = new BlockAmountRequest();
//				paytmBlockRequest.setDuration(blockRequest.get);
				paytmBlockRequest.setMid(op.getPspMerchantId());
				paytmBlockRequest.setMerchantKey(op.getPspMerchantKey());
				paytmBlockRequest.setOrderId(blockRequest.getOrderId());
				paytmBlockRequest.setTxnAmount(blockRequest.getAmount());
				paytmBlockRequest.setToken(blockRequest.getWalletAccountKey());
				PaytmBlockAmountService p = new PaytmBlockAmountService();
				response = p.blockAmount(paytmBlockRequest);
				return response;
			}else{
				WalletBlockService w = new WalletBlockService();
				response = w.blockWalletAmount(blockRequest, operatorId);
				return response;
			}
			
			}
		} catch(Exception e){
			
		}
		
		return finalResponse.toString();
	}
	
}
