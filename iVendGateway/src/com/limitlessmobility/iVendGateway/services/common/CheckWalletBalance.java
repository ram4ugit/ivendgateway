package com.limitlessmobility.iVendGateway.services.common;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

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
import com.limitlessmobility.iVendGateway.model.common.CheckBalanceRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.BlockAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.CheckStatusBody;
import com.limitlessmobility.iVendGateway.model.wallet.paytm.CheckStatusHead;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;
import com.limitlessmobility.iVendGateway.services.wallet.WalletBlockService;
import com.limitlessmobility.iVendGateway.services.wallet.paytm.PaytmBlockAmountService;
import com.paytm.pg.merchant.CheckSumServiceHelper;

@RestController
@RequestMapping(value="v2")
public class CheckWalletBalance {

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value = "/checkBalance", method = RequestMethod.POST)
	@ResponseBody
	public String checkBalance(@RequestBody CheckBalanceRequest checkBalanceRequest){
		JSONObject finalResponse = new JSONObject();
//		String response = new String();
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
				
			String pspId = checkBalanceRequest.getPspId();
			if(pspId.equalsIgnoreCase("paytm")){
				
				OperatorPspEntity op = CommonService.getOperatorDetails(operatorId, pspId, "wallet");
				
				CheckStatusHead head = new CheckStatusHead();
				head.setClientId("merchant-vendiman-staging");//merchant-vendiman-staging|f34NgiNbvzP1gqxueqbBYeae6Ekyrt77
				head.setVersion("v2");
				
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				head.setRequestTimestamp(String.valueOf(timestamp.getTime()));
				head.setChannelId("WEB");
				
				TreeMap<String,String>paramMap=new TreeMap<String, String>();
		        paramMap.put("clientId",head.getClientId());
		        paramMap.put("version", head.getVersion());
		        paramMap.put("requestTimestamp",head.getRequestTimestamp());
		        paramMap.put("channelId",head.getChannelId());
//		        paramMap.put("DURATIONHRS",blockRequest.getDuration());
			
				CheckSumServiceHelper checksumHelper = CheckSumServiceHelper.getCheckSumServiceHelper();
				String CHECKSUMHASH = checksumHelper.genrateCheckSum(op.getPspMerchantKey(), paramMap);
				String newchecksum=URLEncoder.encode(CHECKSUMHASH);
				
				head.setSignature(newchecksum);
				
				CheckStatusBody body = new CheckStatusBody();
				body.setUserToken(checkBalanceRequest.getUserToken());
				body.setTotalAmount(checkBalanceRequest.getTotalAmount());
				
				com.limitlessmobility.iVendGateway.model.wallet.paytm.CheckBalanceRequest c = new com.limitlessmobility.iVendGateway.model.wallet.paytm.CheckBalanceRequest(); 
				c.setHead(head);
				c.setBody(body);
				System.out.println(c.toString());
				
				
				URL url = new URL("https://securegw-stage.paytm.in/paymentservice/pay/consult");
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				connection.setDoOutput(true);
				
				
				
				connection.setRequestProperty("Content-Length", Integer.toString(c.toString().getBytes().length));
			       connection.setUseCaches(false);
			
			       connection.setDoOutput(true);
//						response.setContentType(“application/json”);
			       
			       try (DataOutputStream wr = new DataOutputStream (

			    		      connection.getOutputStream())) {
			    		  wr.writeBytes(c.toString());
			    		}
			       
			       
				int responseCode = connection.getResponseCode();
		        InputStream is;
		        if(responseCode == HttpURLConnection.HTTP_OK){
		        	is = connection.getInputStream();
		        }else {
		        	is = connection.getErrorStream();
		        }
	              String line="";
	              StringBuilder response = new StringBuilder();
	              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	              line="";
	              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From PayTm..."+response.toString());
		              System.out.append("\r");
	              }
		          rd.close(); is.close();
			}else{}
			
			}
		} catch(Exception e){
			
		}
		
		return finalResponse.toString();
	}
}
