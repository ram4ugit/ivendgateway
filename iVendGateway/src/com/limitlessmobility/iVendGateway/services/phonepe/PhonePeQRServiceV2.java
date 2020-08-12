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
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PSPMerchantDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PhonePeQRRequestData;
import com.limitlessmobility.iVendGateway.model.phonepe.PhonePeQRRequestDataV2;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

@Controller
@RequestMapping("/v2/phonepe")
public class PhonePeQRServiceV2 {

	private static final Logger logger = Logger.getLogger(PhonePeQRService.class);
	
	/*public static final String PHONEPEKEY = "8289e078-be0b-484d-ae60-052f117f8deb";
	public static final String MERCHANTID = "UATMERCHANT9";*/
	public static final String QRSTR="/v3/qr/init";

	PhonePeDao ppDao = new PhonePeDaoImpl();
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();

	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String createQRCode(@RequestBody PhonePeQRRequestDataV2 phonePeQRRequestModel) throws JSONException {

		logger.info("Calling PhonePe createQR...");

		System.out.println("createQRCode.....");
        
		double amount=0;
		String walletId = "";
		String walletKey="";
		String pspid="";
		String terminalId="";
		
		EventLogs eventLogRequest = new EventLogs();

		System.out.println("POSID " + phonePeQRRequestModel.getPosId());
		System.out.println("PSPID " + phonePeQRRequestModel.getPspId());
		System.out.println("AMOUNT " + phonePeQRRequestModel.getAmount());
		System.out.println("APPID " + phonePeQRRequestModel.getAppId());
		System.out.println("PRODUCTID " + phonePeQRRequestModel.getProductId());
		
		
		try {
			OperatorDetail operatorDetail = new OperatorDetail();
			if(phonePeQRRequestModel.getPosId()!=null ) {
				terminalId= phonePeQRRequestModel.getPosId();
				if(phonePeQRRequestModel.getPspId()!=null) {
					pspid=phonePeQRRequestModel.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspid);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspid);
					
					
					walletId = operatorDetail.getMerchantId();
					walletKey = operatorDetail.getMerchantKey();
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		pspid=phonePeQRRequestModel.getPspId();
		
		
		String s=phonePeQRRequestModel.getAmount();
	    double i =  Double.parseDouble(s);
//	    long l = (new Double(i)).longValue(); 
	    
		amount= (new Double(i))*100;
		System.out.println("amount "+amount);
		QRDao qrDao = new QRDaoImpl();
		
		JSONObject andData = new JSONObject(phonePeQRRequestModel);
		String req = andData.toString();
		logger.info("Android Request data phonepe..." + req);
		System.out.println("Android Request data for phonepe " + req);

		 terminalId = phonePeQRRequestModel.getPosId();

		RandomString RandomString = new RandomString();
		String txnId = RandomString.generateRandomString(16);
		System.out.println("Random Number  " + txnId);

		/*merchantDetail = ppDao.getMerchantDetailsByPspId(pspid,terminalId);
		String merchantID=merchantDetail.getPspMerchantId();
		System.out.println("merchantID "+merchantID);
		String merchantKey=merchantDetail.getPspMerchantKey();
		System.out.println("merchantKey "+merchantKey);*/
		eventLogRequest.setTerminalId(terminalId);
		eventLogRequest.setEventType("PhonePe Dynamic QR Request");
		eventLogRequest.setVersion("v1");
		eventLogRequest.setOrderID(txnId);
		

		EventLogs eventLogResponse = new EventLogs();
		JSONObject jsonObject = null;
		String line = "";
		StringBuilder response = new StringBuilder();

		jsonObject = new JSONObject();
		jsonObject.put("merchantId", walletId);
		//jsonObject.put("subMerchantId", "LLMS");
		jsonObject.put("storeId", terminalId);
		jsonObject.put("terminalId", "T1");
		jsonObject.put("transactionId", txnId);
		jsonObject.put("amount", amount);
		jsonObject.put("merchantOrderId", txnId);
		jsonObject.put("message", "QR-Generated");
		jsonObject.put("expiresIn", 300);

		String str = jsonObject.toString();
		
		eventLogRequest.setEventDetails(str);
		EventLogDao eventLogDao = new eventLogDaoImpl();
		eventLogDao.saveEventLog(eventLogRequest);
		
		System.out.println("Before encode  "+str);

		
		String encodVal =  new String(new Base64().encode(str.getBytes()));

		System.out.println("Encode value " + encodVal);
		
		String decodVal =  new String(new Base64().decode(encodVal.getBytes()));

		System.out.println("Decode value " + decodVal);
		
		JSONObject jsonObject2=new JSONObject();
		
		jsonObject2.put("request", encodVal);
		
		String urlparameter=jsonObject2.toString();
		
		System.out.println("urlparameter "+urlparameter);
		
		// This object used for save data in DB
		PaymentInitiation paymentInitiation = new PaymentInitiation();
		
		
		System.out.println("PhonePe QR Request..." + jsonObject2.toString());
		logger.info("PhonePe QR Request..." + jsonObject2.toString());

		//String qrstr = "/v3/qr/init";

		//String phonekey = "8289e078-be0b-484d-ae60-052f117f8deb";

		String xveri = encodVal.concat(QRSTR).concat(walletKey);
		//String xveri1 = encodVal+QRSTR+PHONEPEKEY;
		

		System.out.println("xverify   " + xveri);
		
		String sha256hex = (org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri)).toUpperCase();
		
		System.out.println("sha256hex "+sha256hex);
		
		String XVERIFY = sha256hex.concat("###").concat("1");
		
		//String XVERIFY1=((org.apache.commons.codec.digest.DigestUtils.sha256Hex(xveri1)).toUpperCase())+"###"+1;
		
		System.out.println("XVERIFY  " + XVERIFY);

		try {
			URL url = new URL("https://mercury-t2.phonepe.com/v3/qr/init");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-VERIFY", XVERIFY);
			connection.setRequestProperty("X-CALLBACK-URL", "http://139.59.73.155/iVendGateway/payment/v1/transactions/phonepe/initiate");
			connection.setRequestProperty("X-CALL-MODE", "POST");
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

				System.out.println("PHONEPE RESPONSE..." + response.toString());
				System.out.append("\r");

			}
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		
		JSONObject jrespObject= new JSONObject(response.toString());
		
		
		JSONObject jsonData=jrespObject.getJSONObject("data");
		
		paymentInitiation.setPspTransactionId(jsonData.getString("transactionId"));
		
		paymentInitiation.setOrderId(jsonData.getString("transactionId"));
		
		paymentInitiation.setAuthAmount(String.valueOf(amount/100));
		System.out.println("amount in mul of 100 "+paymentInitiation.getAuthAmount());

		System.out.println("TXNID " + jsonData.getString("transactionId"));
		
		paymentInitiation.setDeviceId("P_D");

		System.out.println("DeviceID " + paymentInitiation.getDeviceId());

		paymentInitiation.setAppId(phonePeQRRequestModel.getAppId());

		System.out.println("AppID " + paymentInitiation.getAppId());

		paymentInitiation.setStatus(jrespObject.getString("code"));

		System.out.println("Status " + jrespObject.getString("code"));

		paymentInitiation.setPspId("PhonePe");
		
		paymentInitiation.setTerminalId(phonePeQRRequestModel.getPosId());
		
		paymentInitiation.setCurrency("INR");
		
		paymentInitiation.setProductName(phonePeQRRequestModel.getProductId());
		
		paymentInitiation.setProductPrice(String.valueOf(phonePeQRRequestModel.getAmount()));

		System.out.println("PSPID " + paymentInitiation.getPspId());
		
		paymentInitiation.setComments("QR_CODE");
		
		paymentInitiation.setStatus(Boolean.toString(jrespObject.getBoolean("success")));
		System.out.println("Status Message= "+paymentInitiation.getStatus());
		
		paymentInitiation.setStatusMsg(jrespObject.getString("message"));
		System.out.println("Status Message= "+paymentInitiation.getStatusMsg());

		paymentInitiation.setMerchantId("zoneone");
		//paymentInitiation.setpspMerchantId(merchantDetail.getPspMerchantId());

		try {
			// Saving Payment Initiation
			qrDao.saveQRRequest(paymentInitiation);
		} catch (Exception e) {
			System.out.println("Exception in Saving PhonePe QR payment initiation" + e);
		}
		
		JSONObject resData=new JSONObject();
		resData.put("qrData", jsonData.getString("qrString"));
		resData.put("orderId", jsonData.getString("transactionId"));
		resData.put("encryptedData", jsonData.getString("qrString"));
			

		return resData.toString();
		//return encodVal.toString();
		
		
		
	}

}
