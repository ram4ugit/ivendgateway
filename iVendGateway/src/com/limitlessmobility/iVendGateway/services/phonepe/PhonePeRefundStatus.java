package com.limitlessmobility.iVendGateway.services.phonepe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.common.RefundDbModel;
import com.limitlessmobility.iVendGateway.model.phonepe.PPRefundInitialData;
import com.limitlessmobility.iVendGateway.model.phonepe.PSPMerchantDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PaymentDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.TerminalDetail;

@Controller
@RequestMapping("/v1/phonepe")
public class PhonePeRefundStatus {

	private static final Logger logger = Logger.getLogger(PhonePeRefundStatus.class);
	public static final String VERSION = "v1";
	/*public static final String PHONEPEKEY = "8289e078-be0b-484d-ae60-052f117f8deb";
	public static final String MERCHANTID = "M2306160483220675579140";*/
//	public static final String PHONEPEKEY = "8289e078-be0b-484d-ae60-052f117f8deb";
//	public static final String MERCHANTID = "UATMERCHANT9";
	public static final String QRSTR = "/v3/transaction";

	DbConfigration dbConfig = new DbConfigrationImpl();
	PhonePeDao pdao = new PhonePeDaoImpl();

	@RequestMapping(value = "/refundStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getPhonepeRefundStatus(@RequestBody PPRefundInitialData ppRefundInitialData ) throws JSONException {

		logger.info("Transactionid " + ppRefundInitialData.getTransactionid() + "...Amount..." + ppRefundInitialData.getAmount());

		String line = "";
		StringBuilder response = new StringBuilder();
		String merchantId="";
		String merchantKey="";
		String pspId="";
		String terminalId="";
		boolean isexist=true;
		String appId="";
		double amount=0;
		
		PhonePeDao ppDao = new PhonePeDaoImpl();
		PaymentDetail detail=new PaymentDetail();
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		
		//converting initial request in json format
		JSONObject initialJsonReq = new JSONObject(ppRefundInitialData);
		System.out.println("TrasanctionId " + initialJsonReq.getString("transactionid")+"\nAmount " +initialJsonReq.getString("amount"));
		String transactionId = initialJsonReq.getString("transactionid");
		double amountt = Double.parseDouble(initialJsonReq.getString("amount"));
		amount = amountt*100;
		try {
			detail=ppDao.getPaymentDetail(transactionId);
			
			if(detail.getType().equalsIgnoreCase("static")) {
				return phonepeRefundStatusStatic(ppRefundInitialData);
			}
			
			pspId=detail.getPspId();
			System.out.println("pspsid "+pspId);
			terminalId=detail.getTerminalId();
			System.out.println("terminalId "+terminalId);
			
		//Getting All credential from database on behalf of pspId and terminalId
		
			PSPMerchantDetail merchantDetail=new PSPMerchantDetail();
			/*if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				System.out.println("POSID " + detail.getTerminalId());
				if(detail.getPspId()!=null) {
					pspId=detail.getPspId();
					System.out.println("PSPID " + detail.getPspId());
					merchantDetail = ppDao.getMerchantDetailsByPspId(pspId,terminalId);
					merchantId = merchantDetail.getPspMerchantId().trim();
//					merchantId=MERCHANTID.trim();
					System.out.println("merchantId " + merchantId);
					merchantKey = merchantDetail.getPspMerchantKey().trim();
//					merchantKey=PHONEPEKEY.trim();
					System.out.println("merchantKey " + merchantKey);
				}
			}*/
			
			OperatorDetail operatorDetail = new OperatorDetail();
			if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				if(detail.getPspId()!=null) {
					pspId=detail.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
					
					
					merchantId = operatorDetail.getMerchantId();
					merchantKey = operatorDetail.getMerchantKey();
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

		


		//Appending url format with merchantId and transactionId
		String urlformat = QRSTR+"/"+merchantId+"/"+transactionId+"/status";
		System.out.println("urlformat " + urlformat);
		
		//Appending updated url format with merchantkey or saltkey 
		String updatedUrl=urlformat.concat(merchantKey);
		System.out.println("updatedUrl "+updatedUrl);

		//Encoding updated url string  to sha256hex
		String sha256hex = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(updatedUrl)).toUpperCase();
		System.out.println("sha256hex " + sha256hex);

		//Adding sha256 converted string with ### and 1
		String XVERIFY = sha256hex.concat("###").concat("1");
		System.out.println("XVERIFY " + XVERIFY);
		
		
		try {
			//URL url = new URL("https://mercury-t2.phonepe.com/v3/qr/init");  //live URL
			String urlParameters  = "https://mercury-t2.phonepe.com/v3/transaction/"+merchantId+"/"+transactionId+"/status";
			System.out.println("urlParameters "+urlParameters);
			URL url = new URL(urlParameters);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("x-verify", XVERIFY);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);


			int responseCode = connection.getResponseCode();
			System.out.println("responseCode is "+responseCode);

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				logger.info("responseCode: " + responseCode);
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);
				System.out.append("\r");
			}
			System.out.println("PHONEPE REFUND_STATUS RESPONSE..." + response.toString());
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in Checkstatus Service class.." + e);
			e.printStackTrace();
		}

		    JSONObject ppResponse=new JSONObject(response.toString());
		    
		      JSONObject innerResponse=ppResponse.getJSONObject("data");
		

		JSONObject finalResponse=new JSONObject();
		
		try {
			if (innerResponse.getString("payResponseCode").equalsIgnoreCase("SUCCESS")) {
				finalResponse.put("amount", amount);
				finalResponse.put("statusMessage", ppResponse.getString("message"));
				finalResponse.put("status", "success");
				finalResponse.put("statusCode", ppResponse.getString("code"));
				
			} else {
				finalResponse.put("amount", amount);
				finalResponse.put("statusMessage", "");
				finalResponse.put("status", "FAILURE");
				finalResponse.put("statusCode","");
			}
		} catch (Exception e) {
			finalResponse.put("amount", amount);
			finalResponse.put("statusMessage", e);
			finalResponse.put("status", "FAILURE");
			finalResponse.put("statusCode","");
		}
		
		/* Final response */
		return finalResponse.toString();

	}
	
	@RequestMapping(value = "/refundStatusStatic", method = RequestMethod.POST)
	@ResponseBody
	public String phonepeRefundStatusStatic(@RequestBody PPRefundInitialData ppRefundInitialData ) throws JSONException {

		logger.info("Transactionid " + ppRefundInitialData.getTransactionid() + "...Amount..." + ppRefundInitialData.getAmount());

		String line = "";
		StringBuilder response = new StringBuilder();
		String merchantId="";
		String merchantKey="";
		String pspId="";
		String terminalId="";
		boolean isexist=true;
		String appId="";
		double amount=0;
		
		PhonePeDao ppDao = new PhonePeDaoImpl();
		PaymentDetail detail=new PaymentDetail();
		CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
		
		
		//converting initial request in json format
		JSONObject initialJsonReq = new JSONObject(ppRefundInitialData);
		System.out.println("TrasanctionId " + initialJsonReq.getString("transactionid")+"\nAmount " +initialJsonReq.getString("amount"));
		String transactionId = initialJsonReq.getString("transactionid");
		double amountt = Double.parseDouble(initialJsonReq.getString("amount"));
		amount = amountt*100;
		try {
			detail=ppDao.getPaymentDetail(transactionId);
			pspId=detail.getPspId();
			System.out.println("pspsid "+pspId);
			terminalId=detail.getTerminalId();
			System.out.println("terminalId "+terminalId);
			
		//Getting All credential from database on behalf of pspId and terminalId
		
			PSPMerchantDetail merchantDetail=new PSPMerchantDetail();
			/*if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				System.out.println("POSID " + detail.getTerminalId());
				if(detail.getPspId()!=null) {
					pspId=detail.getPspId();
					System.out.println("PSPID " + detail.getPspId());
					merchantDetail = ppDao.getMerchantDetailsByPspId(pspId,terminalId);
					merchantId = merchantDetail.getPspMerchantId().trim();
//					merchantId=MERCHANTID.trim();
					System.out.println("merchantId " + merchantId);
					merchantKey = merchantDetail.getPspMerchantKey().trim();
//					merchantKey=PHONEPEKEY.trim();
					System.out.println("merchantKey " + merchantKey);
				}
			}*/
			
			OperatorDetail operatorDetail = new OperatorDetail();
			if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				if(detail.getPspId()!=null) {
					pspId=detail.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigPhonepeStatic(operatorId, pspId);
					
					
					merchantId = operatorDetail.getMerchantId();
					merchantKey = operatorDetail.getMerchantKey();
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		RefundDbModel refundDbModel = CommonService.getRefundTxnId(transactionId);
		transactionId=refundDbModel.getTxnGuid();

		//Appending url format with merchantId and transactionId
		String urlformat = QRSTR+"/"+merchantId+"/"+transactionId+"/status";
		System.out.println("urlformat " + urlformat);
		
		//Appending updated url format with merchantkey or saltkey 
		String updatedUrl=urlformat.concat(merchantKey);
		System.out.println("updatedUrl "+updatedUrl);

		//Encoding updated url string  to sha256hex
		String sha256hex = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(updatedUrl)).toUpperCase();
		System.out.println("sha256hex " + sha256hex);

		//Adding sha256 converted string with ### and 1
		String XVERIFY = sha256hex.concat("###").concat("1");
		System.out.println("XVERIFY " + XVERIFY);
		
		
		try {
			
			//URL url = new URL("https://mercury-t2.phonepe.com/v3/qr/init");  //live URL
			String urlParameters  = "https://mercury-uat.phonepe.com/v3/transaction/"+merchantId+"/"+transactionId+"/status";
			System.out.println("urlParameters "+urlParameters);
			URL url = new URL(urlParameters);
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("x-verify", XVERIFY);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);


			int responseCode = connection.getResponseCode();
			System.out.println("responseCode is "+responseCode);

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				logger.info("responseCode: " + responseCode);
				is = connection.getInputStream();
			} else {
				is = connection.getErrorStream();
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);
				System.out.append("\r");
			}
			System.out.println("PHONEPE REFUND_STATUS RESPONSE..." + response.toString());
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in Checkstatus Service class.." + e);
			e.printStackTrace();
		}

		    JSONObject ppResponse=new JSONObject(response.toString());
		    
		      JSONObject innerResponse=ppResponse.getJSONObject("data");
		

		JSONObject finalResponse=new JSONObject();
		
		try {
			if (innerResponse.getString("payResponseCode").equalsIgnoreCase("SUCCESS")) {
				finalResponse.put("amount", amount);
				finalResponse.put("statusMessage", ppResponse.getString("message"));
				finalResponse.put("status", "success");
				finalResponse.put("statusCode", ppResponse.getString("code"));
				
			} else {
				finalResponse.put("amount", amount);
				finalResponse.put("statusMessage", "");
				finalResponse.put("status", "FAILURE");
				finalResponse.put("statusCode","");
			}
		} catch (Exception e) {
			finalResponse.put("amount", amount);
			finalResponse.put("statusMessage", e);
			finalResponse.put("status", "FAILURE");
			finalResponse.put("statusCode","");
		}
		
		/* Final response */
		return finalResponse.toString();

	}
}
