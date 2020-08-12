package com.limitlessmobility.iVendGateway.services.phonepe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PPRefundRequestModel;
import com.limitlessmobility.iVendGateway.model.phonepe.PSPMerchantDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PaymentDetail;

@Controller
@RequestMapping(value = "/v2/phonepe")
public class PhonePeRefundServiceV2 {

	private static final Logger logger = Logger.getLogger(PhonePeRefundService.class);
	private static final String VERSION = "v1";

	/*public static final String PHONEPEKEY = "8289e078-be0b-484d-ae60-052f117f8deb";
	public static final String MERCHANTID = "UATMERCHANT9";*/
	public static final String QRSTR = "/v3/credit/backToSource";
	public static final String SUBMERCHANT = "NA";

	PhonePeDao ppDao = new PhonePeDaoImpl();
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();

	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String getPaymentrefund(@RequestBody String refundData) throws JSONException {
        
		
		String pspid="";
		String terminalId="";
		String mid="";
		String key="";
		JSONObject jsonReq = new JSONObject(refundData);
		PaymentDetail detail=new PaymentDetail();
		PSPMerchantDetail merchantDetail=new PSPMerchantDetail();

		System.out.println("TrasanctionId " + jsonReq.getString("transactionid"));
		System.out.println("Amount " +jsonReq.getLong("amount"));

		//String txnId = jsonReq.getString("originalTransactionId");
		String txnId = jsonReq.getString("transactionid");
		long amount = (jsonReq.getLong("amount"))*100;
		System.out.println("amount "+amount);
		
		detail=ppDao.getPaymentDetail(txnId);
		
		
		
		try {
			OperatorDetail operatorDetail = new OperatorDetail();
			if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				System.out.println("pspsid "+pspid);
				if(detail.getPspId()!=null) {
					pspid=detail.getPspId();
					System.out.println("terminalId "+terminalId);
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspid);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspid);
					
					
					mid = operatorDetail.getMerchantId();
					System.out.println("mid "+mid);
					key = operatorDetail.getMerchantKey();
					System.out.println("key "+key);
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		logger.info("Refund Initial request data ..." + jsonReq.toString());

		System.out.println("Refund Initial request data ...." + jsonReq.toString());

		RandomString RandomString = new RandomString();
		String rtxnId = RandomString.generateRandomString(16);
		System.out.println("Random Number  " + rtxnId);

		String merchtOderId = ppDao.getMerchantOrderIdByTxnId(txnId);

		PPRefundRequestModel ppRefundRequestModel = new PPRefundRequestModel();

		String line = "";
		StringBuilder response = new StringBuilder();
		
		JSONObject reqJsonData = new JSONObject();
		reqJsonData.put("merchantId", mid);
		reqJsonData.put("transactionId", rtxnId);
		reqJsonData.put("originalTransactionId", txnId);
		reqJsonData.put("amount", amount);
		reqJsonData.put("merchantOrderId", merchtOderId);
		reqJsonData.put("subMerchant", SUBMERCHANT);
		reqJsonData.put("message", "I want My Money back");

		String str = reqJsonData.toString();

		System.out.println("R Before encode  " + str);

		String encodVal = new String(new Base64().encode(str.getBytes()));

		System.out.println("R Encode value " + encodVal);

		String decodVal = new String(new Base64().decode(encodVal.getBytes()));

		System.out.println("R Decode value " + decodVal);

		JSONObject jsonObject2 = new JSONObject();

		jsonObject2.put("request", encodVal);

		String urlparameter = jsonObject2.toString();

		//System.out.println("urlparameter " + urlparameter);
		
		
		System.out.println("PhonePe QR Request..." + urlparameter);
		logger.info("PhonePe QR Request..." +urlparameter);
		
		
		String xveri = encodVal.concat(QRSTR).concat(key);
		//String xveri1 = encodVal+QRSTR+PHONEPEKEY;
		

		System.out.println("xverify   " + xveri);
		
		String sha256hex = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri)).toUpperCase();
		
		System.out.println("sha256hex "+sha256hex);
		
		String XVERIFY = sha256hex.concat("###").concat("1");
		
		//String XVERIFY1=((org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri1)).toUpperCase())+"###"+1;
		
		System.out.println("XVERIFY  " + XVERIFY);
		
		try {
			URL url = new URL("https://mercury-t2.phonepe.com/v3/credit/backToSource");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-VERIFY", XVERIFY);
			connection.setRequestProperty("Content-Length", Integer.toString(urlparameter.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(

					connection.getOutputStream())) {
				wr.writeBytes(urlparameter);
			}

			int responseCode = connection.getResponseCode();

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = connection.getInputStream();

			} else {

				is = connection.getErrorStream();

			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);

				System.out.println("PHONEPE RESPONSE REFUND..." + response.toString());
				System.out.append("\r");

			}
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		
		JSONObject jrespObject= new JSONObject(response.toString());
		


		return jrespObject.toString();

	}
	
	@RequestMapping(value = "/refundStatic", method = RequestMethod.POST)
	@ResponseBody
	public String refundStatic(@RequestBody String refundData) throws JSONException {
        
		
		String pspid="";
		String terminalId="";
		String mid="";
		String key="";
		JSONObject jsonReq = new JSONObject(refundData);
		PaymentDetail detail=new PaymentDetail();
		PSPMerchantDetail merchantDetail=new PSPMerchantDetail();

		System.out.println("TrasanctionId " + jsonReq.getString("transactionid"));
		System.out.println("Amount " +jsonReq.getLong("amount"));

		//String txnId = jsonReq.getString("originalTransactionId");
		String txnId = jsonReq.getString("transactionid");
		long amount = (jsonReq.getLong("amount"))*100;
		System.out.println("amount "+amount);
		
		detail=ppDao.getPaymentDetail(txnId);
		
		
		
		try {
			OperatorDetail operatorDetail = new OperatorDetail();
			if(detail.getTerminalId()!=null ) {
				terminalId= detail.getTerminalId();
				System.out.println("pspsid "+pspid);
				if(detail.getPspId()!=null) {
					pspid=detail.getPspId();
					System.out.println("terminalId "+terminalId);
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspid);
					
					operatorDetail = commonCredentialDao.getPspConfigPhonepeStatic(operatorId, pspid);
					
					
					mid = operatorDetail.getMerchantId();
					System.out.println("mid "+mid);
					key = operatorDetail.getMerchantKey();
//					key = "f03b691d-7f11-4ba9-81fb-286b48638f93";
					System.out.println("key "+key);
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		logger.info("Refund Initial request data ..." + jsonReq.toString());

		System.out.println("Refund Initial request data ...." + jsonReq.toString());

		RandomString RandomString = new RandomString();
		String rtxnId = RandomString.generateRandomString(16);
		System.out.println("Random Number  " + rtxnId);

		String merchtOderId = ppDao.getMerchantOrderIdByTxnId(txnId);

		PPRefundRequestModel ppRefundRequestModel = new PPRefundRequestModel();

		String line = "";
		StringBuilder response = new StringBuilder();
		
		JSONObject reqJsonData = new JSONObject();
		reqJsonData.put("merchantId", mid);
		reqJsonData.put("transactionId", rtxnId);
		reqJsonData.put("originalTransactionId", txnId);
		reqJsonData.put("amount", amount);
		reqJsonData.put("merchantOrderId", merchtOderId);
		//reqJsonData.put("subMerchant", SUBMERCHANT);
		reqJsonData.put("message", "refund for cancelled order");

		String str = reqJsonData.toString();

		System.out.println("R Before encode  " + str);

		String encodVal = new String(new Base64().encode(str.getBytes()));

		System.out.println("R Encode value " + encodVal);

		String decodVal = new String(new Base64().decode(encodVal.getBytes()));

		System.out.println("R Decode value " + decodVal);

		JSONObject jsonObject2 = new JSONObject();

		jsonObject2.put("request", encodVal);

		String urlparameter = jsonObject2.toString();

		//System.out.println("urlparameter " + urlparameter);
		
		
		System.out.println("PhonePe QR Request..." + urlparameter);
		logger.info("PhonePe QR Request..." +urlparameter);
		
		
		String xveri = encodVal.concat(QRSTR).concat(key);
		//String xveri1 = encodVal+QRSTR+PHONEPEKEY;
		

		System.out.println("xverify   " + xveri);
		
		String sha256hex = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri)).toUpperCase();
		
		System.out.println("sha256hex "+sha256hex);
		
		String XVERIFY = sha256hex.concat("###").concat("1");
		
		//String XVERIFY1=((org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri1)).toUpperCase())+"###"+1;
		
		System.out.println("XVERIFY  " + XVERIFY);
		
		try {
			URL url = new URL("https://mercury-uat.phonepe.com/v3/credit/backToSource");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-VERIFY", XVERIFY);
			connection.setRequestProperty("Content-Length", Integer.toString(urlparameter.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(

					connection.getOutputStream())) {
				wr.writeBytes(urlparameter);
			}

			int responseCode = connection.getResponseCode();

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = connection.getInputStream();

			} else {

				is = connection.getErrorStream();

			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);

				System.out.println("PHONEPE RESPONSE REFUND..." + response.toString());
				System.out.append("\r");

			}
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		
		JSONObject jrespObject= new JSONObject(response.toString());
		


		return jrespObject.toString();

	}

}
