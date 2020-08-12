package com.limitlessmobility.iVendGateway.services.wallet;

import static com.limitless.uvm.model.Constants.HEADER_STRING;
import static com.limitless.uvm.model.Constants.TOKEN_PREFIX;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import codes.ResponseStatusCode;

import com.google.gson.JsonObject;
import com.limitlessmobil.ivendgateway.dto.ResponseDTO;
import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobil.ivendgateway.util.HttpStatusModal;
import com.limitlessmobil.ivendgateway.util.JwtTokenDecode;
import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobil.ivendgateway.util.ResponseCodes;
import com.limitlessmobil.ivendgateway.util.ResponseUtility;
import com.limitlessmobil.ivendgateway.util.TokenAuthModel;
import com.limitlessmobility.iVendGateway.controller.paytm.PaymentController;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.BlockRequest;
import com.limitlessmobility.iVendGateway.model.common.CommonCheckStatusReponse;
import com.limitlessmobility.iVendGateway.model.common.PspDetail;
import com.limitlessmobility.iVendGateway.model.common.RefundDbModel;
import com.limitlessmobility.iVendGateway.model.common.ReleaseAmountRequest;
import com.limitlessmobility.iVendGateway.model.common.SettleAmountRequest;
import com.limitlessmobility.iVendGateway.model.wallet.CaptureCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.CardValidateRequest;
import com.limitlessmobility.iVendGateway.model.wallet.PayFromCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.SettleCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.TransactionDetailsCard;
import com.limitlessmobility.iVendGateway.model.wallet.VoidTransactionRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletBlockRequest;
import com.limitlessmobility.iVendGateway.model.wallet.WalletPayPandingRequest;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping("/v2/wallet")
public class CardService {

	@Autowired
	private HttpServletRequest request;

	
	@RequestMapping(value="/validateCard", method=RequestMethod.POST)
	@ResponseBody
	public String validateCard(@RequestBody CardValidateRequest cardValidateRequest){
		System.out.println("cardValidateRequestttt  "+cardValidateRequest.toString());
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse=new JSONObject();
		CommonCheckStatusReponse commoncheckstatusReasponse = new CommonCheckStatusReponse();
		
		
		String line = "";
		
		StringBuilder response = new StringBuilder();
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
			return ResponseUtility.sendResponseObj(ResponseStatusCode.UNAUTHORISED_ACCESS, ResponseStatusCode.UNAUTHORISED_ACCESS_MSG, errors, "false");
		} else {
			int operatorId = Integer.parseInt(t.getOperatorId());
		
			if(CommonService.isCardEnable(cardValidateRequest.getTerminalId(), operatorId)){
			} else {
				try {
		            finalResponse.put("responseObj", "null");
		            finalResponse.put("message", "Cards not enabled");
					finalResponse.put("status", HttpStatusModal.ERROR);
					
					return finalResponse.toString();
	            } catch (JSONException e) {
		            e.printStackTrace();
	            }
			}
				
			
		if(CommonService.isExistOrder(cardValidateRequest.getOrderId())){
			try {
	            finalResponse.put("responseObj", "null");
	            finalResponse.put("message", "Already in process");
				finalResponse.put("status", HttpStatusModal.ERROR);
				
				return finalResponse.toString();
            } catch (JSONException e) {
	            e.printStackTrace();
            }
		} else{
		
			String urlParameters="";
			try{
				JSONObject requestJson = new JSONObject();
				requestJson.put("cardNo", cardValidateRequest.getCardNo());
				requestJson.put("amount", cardValidateRequest.getAmount());
				urlParameters = requestJson.toString();
			} catch(Exception e){
				
			}
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/WalletAccount/validatecard");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches(false);
		       connection.setDoOutput(true);
		       
		       try (DataOutputStream wr = new DataOutputStream (
		    		   	connection.getOutputStream())) {
		    	   		wr.writeBytes(urlParameters);
		    		}
		       int responseCode = connection.getResponseCode();
		        InputStream is;
		        if(responseCode == HttpURLConnection.HTTP_OK){
		        	is = connection.getInputStream();
		        }else {
		        is = connection.getErrorStream();
		        }
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			try{
			JSONObject j = new JSONObject(response.toString());
//			JSONObject responseJson = new JSONObject();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				JSONObject modelResponseJson = j.getJSONObject("model");
				
				PayFromCardRequest payFromCardRequest = new PayFromCardRequest();
				payFromCardRequest.setAmount(cardValidateRequest.getAmount());
				payFromCardRequest.setOrderId(cardValidateRequest.getOrderId());
				payFromCardRequest.setReferenceNo(cardValidateRequest.getOrderId());
				payFromCardRequest.setSource("");
				payFromCardRequest.setWalletId(modelResponseJson.getInt("walletId"));
				int accountId = modelResponseJson.getInt("walletAccountId");
				payFromCardRequest.setWalletAccountId(Integer.toString(accountId));
				
				if(CommonService.validateCard(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"), cardValidateRequest.getTerminalId())){
				
					PspDetail pspDetail = CommonService.getWalletDetailsByID(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"));
					
					payFromCardRequest.setWalletKey(pspDetail.getPspMerchantKey());
					
					WalletPayPendingService w = new WalletPayPendingService();
					String responsee = w.payAmountFromCard(payFromCardRequest, cardValidateRequest.getTerminalId());
					
					JSONObject jj = new JSONObject(responsee);
					if(jj.getString("status").equalsIgnoreCase("Success")){
						
						commoncheckstatusReasponse.setAmount(cardValidateRequest.getAmount());
						commoncheckstatusReasponse.setStatus(HttpStatusModal.OK);
						commoncheckstatusReasponse.setStatusCode("200");
						commoncheckstatusReasponse.setStatusMessage(HttpStatusModal.OK);
						
						JSONObject subResponse = new JSONObject();
						subResponse.put("amount", cardValidateRequest.getAmount());
						subResponse.put("statusMessage", HttpStatusModal.OK);
						subResponse.put("statusCode", "200");
						subResponse.put("status", HttpStatusModal.OK);
						subResponse.put("refNo", jj.getString("referenceNo"));
						
						finalResponse.put("responseObj", subResponse);
						finalResponse.put("message", jj.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
						
					} else{
						finalResponse.put("responseObj", "null");
						finalResponse.put("message", jj.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
					}
				} else{
					finalResponse.put("responseObj", "null");
					finalResponse.put("message", "Invalid Card");
					finalResponse.put("status", HttpStatusModal.ERROR);
				}
				
			} else{
				finalResponse.put("responseObj", "null");
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
		
			} catch(Exception e){
				System.out.println(e);
			}
		}}
		return finalResponse.toString();
	}
	
	@RequestMapping(value="/validateWalletCard", method=RequestMethod.POST)
	@ResponseBody
	public String validateWalletCard(@RequestBody CardValidateRequest cardValidateRequest){
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse=new JSONObject();
		CommonCheckStatusReponse commoncheckstatusReasponse = new CommonCheckStatusReponse();
		
		
		String line = "";
		
		StringBuilder response = new StringBuilder();
		
		if(CommonService.isExistOrder(cardValidateRequest.getOrderId())){
			try {
	            finalResponse.put("responseObj", "null");
	            finalResponse.put("message", "Already in process");
				finalResponse.put("status", HttpStatusModal.ERROR);
				
				return finalResponse.toString();
            } catch (JSONException e) {
	            e.printStackTrace();
            }
		} else{
		
			String urlParameters="";
			try{
				JSONObject requestJson = new JSONObject();
				requestJson.put("cardNo", cardValidateRequest.getCardNo());
//				requestJson.put("amount", cardValidateRequest.getAmount());
				urlParameters = requestJson.toString();
			} catch(Exception e){
				
			}
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/WalletAccount/validateprepaidcard");
				System.out.println(url);
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setUseCaches(false);
		       connection.setDoOutput(true);
		       
		       try (DataOutputStream wr = new DataOutputStream (
		    		   	connection.getOutputStream())) {
		    	   		wr.writeBytes(urlParameters);
		    		}
		       int responseCode = connection.getResponseCode();
		        InputStream is;
		        if(responseCode == HttpURLConnection.HTTP_OK){
		        	is = connection.getInputStream();
		        }else {
		        is = connection.getErrorStream();
		        }
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			try{
			JSONObject j = new JSONObject(response.toString());
//			JSONObject responseJson = new JSONObject();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				JSONObject modelResponseJson = j.getJSONObject("model");
				
				PayFromCardRequest payFromCardRequest = new PayFromCardRequest();
				payFromCardRequest.setAmount(cardValidateRequest.getAmount());
				payFromCardRequest.setOrderId(cardValidateRequest.getOrderId());
				payFromCardRequest.setReferenceNo(cardValidateRequest.getOrderId());
				payFromCardRequest.setSource("");
				payFromCardRequest.setWalletId(modelResponseJson.getInt("walletId"));
				int accountId = modelResponseJson.getInt("walletAccountId");
				payFromCardRequest.setWalletAccountId(Integer.toString(accountId));
				
				if(CommonService.validateCard(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"), cardValidateRequest.getTerminalId())){
				
					PspDetail pspDetail = CommonService.getWalletDetailsByID(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"));
					
					payFromCardRequest.setWalletKey(pspDetail.getPspMerchantKey());
					
					WalletPayPendingService w = new WalletPayPendingService();
					String responsee = w.payAmountFromCard(payFromCardRequest, cardValidateRequest.getTerminalId());
					
					JSONObject jj = new JSONObject(responsee);
					if(jj.getString("status").equalsIgnoreCase("Success")){
						
						commoncheckstatusReasponse.setAmount(cardValidateRequest.getAmount());
						commoncheckstatusReasponse.setStatus(HttpStatusModal.OK);
						commoncheckstatusReasponse.setStatusCode("200");
						commoncheckstatusReasponse.setStatusMessage(HttpStatusModal.OK);
						
						JSONObject subResponse = new JSONObject();
						subResponse.put("amount", cardValidateRequest.getAmount());
						subResponse.put("statusMessage", HttpStatusModal.OK);
						subResponse.put("statusCode", "200");
						subResponse.put("status", HttpStatusModal.OK);
						subResponse.put("refNo", jj.getString("referenceNo"));
						
						finalResponse.put("responseObj", subResponse);
						finalResponse.put("message", jj.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
						
					} else{
						finalResponse.put("responseObj", "null");
						finalResponse.put("message", jj.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
					}
				} else{
					finalResponse.put("responseObj", "null");
					finalResponse.put("message", "Invalid Card");
					finalResponse.put("status", HttpStatusModal.ERROR);
				}
				
			} else{
				finalResponse.put("responseObj", "null");
				finalResponse.put("message", j.getString("message"));
				finalResponse.put("status", HttpStatusModal.ERROR);
			}
		
			} catch(Exception e){
				System.out.println(e);
			}
		}
		return finalResponse.toString();
	}
	
	@RequestMapping(value="/cardValidate", method=RequestMethod.POST)
	@ResponseBody
	public String validateWalletCardNew(@RequestBody CardValidateRequest cardValidateRequest){
		System.out.println("cardValidate start  "+cardValidateRequest.toString());
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse=new JSONObject();
		
		String line = "";
		
		StringBuilder response = new StringBuilder();
		
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
		
		/*if(CommonService.isExistOrder(cardValidateRequest.getOrderId())){
			try {
	            finalResponse.put("responseObj", "null");
	            finalResponse.put("message", "Already in process");
				finalResponse.put("status", HttpStatusModal.ERROR);
				
				return finalResponse.toString();
            } catch (JSONException e) {
	            e.printStackTrace();
            }
		} else{*/
		
			String urlParameters="";
			try{
				JSONObject requestJson = new JSONObject();
				requestJson.put("cardNo", cardValidateRequest.getCardNo());
				urlParameters = requestJson.toString();
			} catch(Exception e){
				
			}
			int operatorId = Integer.parseInt(t.getOperatorId());
			
			if(CommonService.isCardEnable(cardValidateRequest.getTerminalId(), operatorId)){
			} else {
				try {
		            finalResponse.put("responseObj", "null");
		            finalResponse.put("message", "Wallet Inactive");
					finalResponse.put("status", HttpStatusModal.ERROR);
					
					return finalResponse.toString();
	            } catch (JSONException e) {
		            e.printStackTrace();
	            }
			}
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/WalletAccount/validateprepaidcard");
				System.out.println("validate card url: "+url);
				
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				System.out.println("validate card urlParameters: "+urlParameters);
				connection.setUseCaches(false);
		       connection.setDoOutput(true);
		       
		       try (DataOutputStream wr = new DataOutputStream (
		    		   	connection.getOutputStream())) {
		    	   		wr.writeBytes(urlParameters);
		    		}
		       int responseCode = connection.getResponseCode();
		        InputStream is;
		        if(responseCode == HttpURLConnection.HTTP_OK){
		        	is = connection.getInputStream();
		        }else {
		        is = connection.getErrorStream();
		        }
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			try{
			JSONObject j = new JSONObject(response.toString());
//			JSONObject responseJson = new JSONObject();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
			
				JSONObject modelResponseJson = j.getJSONObject("model");
				if(CommonService.prepaidCardValidate(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"), cardValidateRequest.getTerminalId())){
				
					PspDetail pspDetail = CommonService.getWalletDetailsByID(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"));
					
					PaymentTransaction paymentTransaction = new PaymentTransaction();
					paymentTransaction.setStatus("Pending");
					String refNo = String.valueOf(modelResponseJson.getInt("refTransactionId"));
					paymentTransaction.setPspTransactionId(refNo);
					paymentTransaction.setOrderId(refNo);
					paymentTransaction.setMerchantOrderId(refNo);
					
					paymentTransaction.setDeviceId(String.valueOf(modelResponseJson.getInt(("operatorId"))));
					paymentTransaction.setCustomerName(String.valueOf(modelResponseJson.getInt("customerId")));
					paymentTransaction.setMerchantId(String.valueOf(modelResponseJson.getInt("walletId")));
					paymentTransaction.setPspId(String.valueOf(modelResponseJson.getInt("walletId")));
					paymentTransaction.setPspMerchantId(String.valueOf(modelResponseJson.getInt("walletAccountId")));
					paymentTransaction.setTerminalId(cardValidateRequest.getTerminalId());
					paymentTransaction.setCustomerName(cardValidateRequest.getCardNo());
					TransactionDao tdao = new TransactionDaoImpl();
					tdao.saveTransaction(paymentTransaction);
					
						JSONObject responseObj = new JSONObject();
						responseObj.put("refNo", refNo);
						responseObj.put("cardDisplayNo", pspDetail.getPspName());
						responseObj.put("balance", String.valueOf(modelResponseJson.getDouble("availableBalance")));
						
						responseObj.put("statusMessage", modelResponseJson.getString("statusMessage"));
						
						
						
						finalResponse.put("responseObj", responseObj);
						finalResponse.put("message", j.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
						return finalResponse.toString();
						
				} else{
						finalResponse.put("responseObj", "");
						finalResponse.put("message", "Invalid Card");
						finalResponse.put("status", HttpStatusModal.OK);
						
						try {
						//to do release card 
						PspDetail pspDetail = CommonService.getWalletDetailsByID(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"));
						ReleaseAmountRequest releaseRequest = new ReleaseAmountRequest();
						releaseRequest.setOperatorId(modelResponseJson.getInt(("operatorId")));
						releaseRequest.setOrderId(String.valueOf(modelResponseJson.getInt("refTransactionId")));
						releaseRequest.setPspId(String.valueOf(modelResponseJson.getInt("walletId")));
						releaseRequest.setSource("PaymentMode");
						releaseRequest.setTransactionRefNo(releaseRequest.getOrderId());
						releaseRequest.setWalletAccountId(String.valueOf(modelResponseJson.getInt("walletAccountId")));
						releaseRequest.setWalletAccountKey(String.valueOf(modelResponseJson.getInt("walletAccountId")));
						releaseRequest.setWalletId(String.valueOf(modelResponseJson.getInt("walletId")));
						releaseRequest.setWalletKey(pspDetail.getPspMerchantKey());
						
						
						
						WalletReleaseAmountService releaseService = new WalletReleaseAmountService();
						String releseResponse = releaseService.voidTransaction(releaseRequest);
						System.out.println("releseResponse auto ... "+releseResponse);
						} catch(Exception e) {
							System.out.println();
						}
						return finalResponse.toString();
					}
				} else {
					finalResponse.put("responseObj", "null");
					finalResponse.put("message", j.getString("message"));
					finalResponse.put("status", HttpStatusModal.ERROR);
					return finalResponse.toString();
				}
			} 
		
			catch(Exception e){
				System.out.println(e);
			}
		}
//		}
		System.out.println("cardValidate end");
		return finalResponse.toString();
	}

	@RequestMapping(value="/voidTransactionUpdate", method=RequestMethod.POST)
	@ResponseBody
	public String voidTransactionUpdate(@RequestBody VoidTransactionRequest requestModel){
		System.out.println("voidTransactionUpdate APi request  "+requestModel.toString());
		JSONObject finalResponse = new JSONObject();
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
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
			if(CommonService.isCardTransactionOpen(requestModel.getTerminalId(), requestModel.getRefId())){
				
				TransactionDetailsCard transaction = CommonService.getTransactionByRefId(requestModel.getRefId(), requestModel.getTerminalId());
				PspDetail pspDetail = CommonService.getWalletDetailsByID(transaction.getOperatorId(), Integer.parseInt(transaction.getWalletId()));
				
				
				
				ReleaseAmountRequest releaseRequest = new ReleaseAmountRequest();
				releaseRequest.setOperatorId(transaction.getOperatorId());
				releaseRequest.setOrderId(requestModel.getRefId());
				releaseRequest.setPspId(transaction.getWalletId());
				releaseRequest.setSource("PaymentMode");
				releaseRequest.setTransactionRefNo(releaseRequest.getOrderId());
				releaseRequest.setWalletAccountId(transaction.getWalletAccountId());
				releaseRequest.setWalletAccountKey(transaction.getWalletAccountId());
				releaseRequest.setWalletId(pspDetail.getPspId());
				releaseRequest.setWalletKey(pspDetail.getPspMerchantKey());
				
				
				
				WalletReleaseAmountService releaseService = new WalletReleaseAmountService();
				String releseResponse = releaseService.voidTransaction(releaseRequest);
//			String status = CommonService.voidTransactionUpdate(requestModel.getTerminalId(), requestModel.getRefId());
			JSONObject response = new JSONObject(releseResponse);
			String status = response.getString("status");
			if(status.equalsIgnoreCase("success")){
				JSONObject responseObject = new JSONObject();
				responseObject.put("status", "Transaction Updated");
				finalResponse.put("responseObj", responseObject);
				finalResponse.put("message", response.getString("message"));
				finalResponse.put("status", HttpStatusModal.OK);
				return finalResponse.toString();
			} else if(status.equalsIgnoreCase("Failure")){
				JSONObject responseObject = new JSONObject();
				responseObject.put("status", "");
				finalResponse.put("responseObj", responseObject);
				finalResponse.put("message", response.getString("message"));
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			}
			}}
		} catch(Exception e){
			
		}
		return finalResponse.toString();
	}
	@RequestMapping(value="/captureCard", method=RequestMethod.POST)
	@ResponseBody
	public String captureCard(@RequestBody CaptureCardRequest requestModel){
		JSONObject finalResponse = new JSONObject();
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();

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
			TransactionDetailsCard transaction = CommonService.getTransactionByRefId(requestModel.getRefNo(), requestModel.getTerminalId());
			PspDetail pspDetail = CommonService.getWalletDetailsByID(transaction.getOperatorId(), Integer.parseInt(transaction.getWalletId()));
			
			if(transaction.getStatus().equalsIgnoreCase("success")){
				/*PayFromCardRequest payRequest = new PayFromCardRequest();
				
				payRequest.setAmount(request.getAmount());
				payRequest.setOrderId(request.getRefNo());
				payRequest.setReferenceNo(request.getRefNo());
				payRequest.setSource("paymentmodeSystem");
				payRequest.setWalletAccountId(transaction.getWalletAccountId());
				payRequest.setWalletId(Integer.parseInt(transaction.getWalletId()));
				payRequest.setWalletKey(pspDetail.getPspMerchantKey());
				
				WalletPayPendingService payService = new WalletPayPendingService();
				String responseStr = payService.payAmountFromCard(payRequest, request.getTerminalId());
				JSONObject responseJson = new JSONObject(responseStr);*/
				
				SettleAmountRequest settleRequest = new SettleAmountRequest();
				
				settleRequest.setAmount(requestModel.getAmount());
				settleRequest.setCustId(transaction.getCustomerId());
				settleRequest.setOperatorId(transaction.getOperatorId());
				settleRequest.setPspId(transaction.getWalletId());
				settleRequest.setSource("paymentMode");
				settleRequest.setTransactionRefNo(requestModel.getRefNo());
				settleRequest.setWalletAccountId(transaction.getWalletAccountId());
				settleRequest.setWalletId(pspDetail.getPspMerchantId());
				settleRequest.setWalletKey(pspDetail.getPspMerchantKey());
				settleRequest.setOrderId(requestModel.getRefNo());
				
				WalletSettleAmountService settleService = new WalletSettleAmountService();
				String responseStr = settleService.settleWalletAmount(settleRequest);
				
				JSONObject responseJson = new JSONObject(responseStr);
				
				if(responseJson.getString("status").equalsIgnoreCase("Success")){
					
					
					
					JSONObject responseObj = new JSONObject();
					responseObj.put("status", "Paid Successfully");
					finalResponse.put("responseObj", responseObj);
					finalResponse.put("message", HttpStatusModal.OK);
					finalResponse.put("status", responseJson.getString("status"));
					return finalResponse.toString();
				} else{
					finalResponse.put("responseObj", "");
					finalResponse.put("message", responseJson.getString("message"));
					finalResponse.put("status", "Failure");
					return finalResponse.toString();
				}
			} else if(transaction.getStatus().equalsIgnoreCase("Failure")){
				JSONObject responseObject = new JSONObject();
				responseObject.put("status", "No Records Found");
				finalResponse.put("responseObj", responseObject);
				finalResponse.put("message", "Failure");
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			}
			
		} } catch(Exception e){
			
		}
		return finalResponse.toString();
	}
	@RequestMapping(value="/settleCard", method=RequestMethod.POST)
	@ResponseBody
	public String settleCard(@RequestBody SettleCardRequest requestModel){
		System.out.println("settleCard API request "+requestModel.toString());
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
		/*
		JSONObject finalResponse = new JSONObject();
		try{
			TransactionDetailsCard transaction = CommonService.getTransactionByRefId(request.getRefNo(), request.getTerminalId());
			PspDetail pspDetail = CommonService.getWalletDetailsByID(transaction.getOperatorId(), Integer.parseInt(transaction.getWalletId()));
			
			if(transaction.getStatus().equalsIgnoreCase("success")){
				SettleAmountRequest settleRequest = new SettleAmountRequest();
				
				settleRequest.setAmount(request.getAmount());
				settleRequest.setCustId(transaction.getCustomerId());
				settleRequest.setOperatorId(transaction.getOperatorId());
				settleRequest.setPspId(transaction.getWalletId());
				settleRequest.setSource("paymentMode");
				settleRequest.setTransactionRefNo(request.getRefNo());
				settleRequest.setWalletAccountId(transaction.getWalletAccountId());
				settleRequest.setWalletId(pspDetail.getPspMerchantId());
				settleRequest.setWalletKey(pspDetail.getPspMerchantKey());
				
				WalletSettleAmountService settleService = new WalletSettleAmountService();
				String responseStr = settleService.settleWalletAmount(settleRequest);
				JSONObject responseJson = new JSONObject(responseStr);
				if(responseJson.getString("status").equalsIgnoreCase("Success")){
				
					JSONObject responseObj = new JSONObject();
					responseObj.put("status", "Settled Successfully");
					finalResponse.put("responseObj", responseObj);
					finalResponse.put("message", HttpStatusModal.OK);
					finalResponse.put("status", responseJson.getString("status"));
					return finalResponse.toString();
				} else{
					finalResponse.put("responseObj", "");
					finalResponse.put("message", responseJson.getString("message"));
					finalResponse.put("status", "Failure");
					return finalResponse.toString();
				}
			} else if(transaction.getStatus().equalsIgnoreCase("Failure")){
				JSONObject responseObject = new JSONObject();
				responseObject.put("status", "No Records Found");
				finalResponse.put("responseObj", responseObject);
				finalResponse.put("message", "Failure");
				finalResponse.put("status", "Failure");
				return finalResponse.toString();
			}
			
		} catch(Exception e){
			
		}
		return finalResponse.toString();
	*/}

	@RequestMapping(value="/cardValidateService", method=RequestMethod.POST)
	@ResponseBody
	public String cardValidateNew(@RequestBody CardValidateRequest cardValidateRequest){
		System.out.println("cardValidateService start  "+cardValidateRequest.toString());
		Map<String, Object> errors = new HashMap<>();
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject finalResponse=new JSONObject();
		
		String line = "";
		
		StringBuilder response = new StringBuilder();
		
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
		
		/*if(CommonService.isExistOrder(cardValidateRequest.getOrderId())){
			try {
	            finalResponse.put("responseObj", "null");
	            finalResponse.put("message", "Already in process");
				finalResponse.put("status", HttpStatusModal.ERROR);
				
				return finalResponse.toString();
            } catch (JSONException e) {
	            e.printStackTrace();
            }
		} else{*/
		
			String urlParameters="";
			try{
				JSONObject requestJson = new JSONObject();
				requestJson.put("cardNo", cardValidateRequest.getCardNo());
				requestJson.put("referenceNo", cardValidateRequest.getOrderId());
				requestJson.put("amount", cardValidateRequest.getAmount());
				
				urlParameters = requestJson.toString();
			} catch(Exception e){
				
			}
			int operatorId = Integer.parseInt(t.getOperatorId());
			
			if(CommonService.isCardEnable(cardValidateRequest.getTerminalId(), operatorId)){
			} else {
				try {
		            finalResponse.put("responseObj", "null");
		            finalResponse.put("message", "Wallet Inactive");
					finalResponse.put("status", HttpStatusModal.ERROR);
					
					return finalResponse.toString();
	            } catch (JSONException e) {
		            e.printStackTrace();
	            }
			}
			try {
				String apiURL=Util.apiUrlReader();
				URL url = new URL(apiURL+"api/Wallet/Transaction/pay?version=2.0"); //have to change URL
				System.out.println("validate card url: "+url);
				
				HttpURLConnection connection = null;
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				System.out.println("validate card urlParameters: "+urlParameters);
				connection.setUseCaches(false);
		       connection.setDoOutput(true);
		       
		       try (DataOutputStream wr = new DataOutputStream (
		    		   	connection.getOutputStream())) {
		    	   		wr.writeBytes(urlParameters);
		    		}
		       int responseCode = connection.getResponseCode();
		        InputStream is;
		        if(responseCode == HttpURLConnection.HTTP_OK){
		        	is = connection.getInputStream();
		        }else {
		        is = connection.getErrorStream();
		        }
		              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		              line="";
		              while((line = rd.readLine()) != null) {
		              response.append(line);
		              System.out.println("RESPONSE From wallet..."+response.toString());
		              System.out.append("\r");

		              }
		              rd.close(); is.close();     
			} catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
			try{
			JSONObject j = new JSONObject(response.toString());
//			JSONObject responseJson = new JSONObject();
			
			if(j.getString("status").equalsIgnoreCase("SUCCESS")){
				JSONObject modelResponseJson = j.getJSONObject("model");
				
				
//					PspDetail pspDetail = CommonService.getWalletDetailsByID(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"));
					
					PaymentTransaction paymentTransaction = new PaymentTransaction();
					paymentTransaction.setStatus(j.getString("status"));
					String refNo = String.valueOf(modelResponseJson.getInt("transactionId"));
					paymentTransaction.setPspTransactionId(String.valueOf(modelResponseJson.getInt("transactionId")));
					paymentTransaction.setOrderId(cardValidateRequest.getOrderId());
					paymentTransaction.setMerchantOrderId(cardValidateRequest.getOrderId());
					paymentTransaction.setAuthAmount(cardValidateRequest.getAmount());
					paymentTransaction.setPaidAmount(cardValidateRequest.getAmount());
					paymentTransaction.setDeviceId(String.valueOf(modelResponseJson.getInt("operatorId")));
					paymentTransaction.setCustomerName(String.valueOf(modelResponseJson.getInt("customerId")));
					paymentTransaction.setMerchantId(String.valueOf(modelResponseJson.getInt("walletId")));
					paymentTransaction.setPspId(String.valueOf(modelResponseJson.getInt("walletId")));
					paymentTransaction.setPspMerchantId(String.valueOf(modelResponseJson.getInt("walletAccountId")));
					paymentTransaction.setTerminalId(cardValidateRequest.getTerminalId());
					paymentTransaction.setCustomerName(cardValidateRequest.getCardNo());
					TransactionDao tdao = new TransactionDaoImpl();
					tdao.saveTransaction(paymentTransaction);
					
					if(CommonService.prepaidCardValidate(modelResponseJson.getInt("operatorId"), modelResponseJson.getInt("walletId"), cardValidateRequest.getTerminalId())){
					
						JSONObject responseObj = new JSONObject();
//						responseObj.put("amount", cardValidateRequest.getAmount());
						responseObj.put("refNo", modelResponseJson.getInt("transactionId"));
						responseObj.put("cardNo", modelResponseJson.getString("cardNo"));
						responseObj.put("statusMessage", modelResponseJson.getString("statusMessage"));
						try {
						responseObj.put("availableBalance", modelResponseJson.getDouble("availableBalance"));
						} catch(Exception e) {
							try {
								responseObj.put("availableBalance", modelResponseJson.getInt("availableBalance"));
							} catch(Exception ee) {
								
							}
						}
						finalResponse.put("responseObj", responseObj);
						finalResponse.put("message", j.getString("message"));
						finalResponse.put("status", HttpStatusModal.OK);
						return finalResponse.toString();
						
				
				} else{
					finalResponse.put("responseObj", "");
					finalResponse.put("message", "Invalid Card");
					finalResponse.put("status", HttpStatusModal.ERROR);
					
					try {
					//refund card 
						WalletRefundService walletRefundService = new WalletRefundService();
						walletRefundService.walletRefund(modelResponseJson.getInt("walletAccountId"), modelResponseJson.getInt("transactionId"), "Invalid Card", modelResponseJson.getInt("walletId"), modelResponseJson.getInt("operatorId"));
					// have to save details
						try {
						RefundDbModel refundModel = new RefundDbModel();
						refundModel.setTerminalId(cardValidateRequest.getTerminalId());
						refundModel.setPspId(String.valueOf(modelResponseJson.getInt("walletId")));
						refundModel.setAmount(String.valueOf(cardValidateRequest.getAmount()));
						refundModel.setMerchantOrderId(cardValidateRequest.getOrderId());
						MySqlDate mySqlDate = new MySqlDate();
						refundModel.setRefundDate(mySqlDate.getDate());
						CommonService.saveRefund(refundModel); 
						} catch(Exception e) {
							System.out.println("WalletRefund Save Error "+e);
						}
						
					} catch(Exception e) {
						System.out.println();
					}
					return finalResponse.toString();
				}
				} else {
					
					
					
					finalResponse.put("responseObj", "null");
					try {
						finalResponse.put("message", j.getString("message"));
					} catch(Exception e) {
						finalResponse.put("message", HttpStatusModal.ERROR);
					}
					
					JSONObject responseObj = new JSONObject();
					try {
						JSONObject modelResponseJson = j.getJSONObject("model");
					try {
						responseObj.put("availableBalance", modelResponseJson.getDouble("availableBalance"));
						
						} catch(Exception e) {
							try {
								
								responseObj.put("availableBalance", modelResponseJson.getInt("availableBalance"));
								
							} catch(Exception ee) {
								
							}
						}
					finalResponse.put("responseObj", responseObj);
					} catch(Exception e) {
						System.out.println("CardService 1146 line error "+e);
					}
					
					finalResponse.put("status", HttpStatusModal.ERROR);
					return finalResponse.toString();
				}
			} 
		
			catch(Exception e){
				System.out.println(e);
			}
		}
//		}
		System.out.println("cardValidate end");
		return finalResponse.toString();
	}
}
