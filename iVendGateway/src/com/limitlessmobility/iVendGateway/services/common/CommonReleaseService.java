package com.limitlessmobility.iVendGateway.services.common;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

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

import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.common.ReleaseAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.PaytmReleaseRequest;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.services.wallet.WalletReleaseAmountService;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.PaytmReleaseAmountService;

@RestController
@RequestMapping(value="/v2")
public class CommonReleaseService {
	
	@Autowired
	private HttpServletRequest request;

	/*
	 * This Method is used to block wallet amount
	 * ID.
	 */
	@RequestMapping(value = "/commonRelease", method = RequestMethod.POST)
	@ResponseBody
	public String releaseWalletAmount(@RequestBody ReleaseAmountRequest releaseAmountRequest) throws JSONException{
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
			} else {*/
				int operatorId = releaseAmountRequest.getOperatorId();
				
			String pspId = releaseAmountRequest.getPspId();
			if(pspId.equalsIgnoreCase("paytm")){
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, pspId, "wallet");
				
				PaytmReleaseRequest paytmReleaseRequest = new PaytmReleaseRequest();
//				paytmBlockRequest.setDuration(blockRequest.get);
				paytmReleaseRequest.setMid(op.getPspMerchantId());
				paytmReleaseRequest.setMerchantKey(op.getPspMerchantKey());
				paytmReleaseRequest.setPreAuthId(CommonService.getTransactionId(releaseAmountRequest.getOrderId()));
				paytmReleaseRequest.setToken(releaseAmountRequest.getWalletAccountKey());
				
				PaytmReleaseAmountService p = new PaytmReleaseAmountService();
				response = p.releaseAmount(paytmReleaseRequest, releaseAmountRequest.getOrderId());
				return response;
			}else{
				WalletReleaseAmountService w = new WalletReleaseAmountService();
				response = w.releaseWalletAmount(releaseAmountRequest);
				return response;
			}
			
//			}
		} catch(Exception e){
			
		}
		
		return finalResponse.toString();
	}
}
