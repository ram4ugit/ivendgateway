package com.limitlessmobility.iVendGateway.services.amazonpay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.controller.amazonpay.SignatureUtil;
import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.amazonpay.AmazonPayDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonQRRequestData;
import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.RequestToSign;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

/**
 * @author Amar
 *
 */
@Controller
@RequestMapping("/v1/amazonpay")
public class AmazonPayQRServiceV1{

	
	private static final String currencyCode="INR";
	
	
	private static final String signatureMethod="HmacSHA384";
	
	private static final String signatureVersion="4";
	
	
	private static final String HTTPRequestMethod="POST";
	
	private static final String hostName ="amazonpay.amazon.in";
	
	private static final String CanonicalURI ="/payment/token/QR_CODE/v1";
	
	
	
	
	/**
	 * This API is used to get QR of Amazonpay. 
	 * @version 1
	 * @param amazonQRRequestData
	 * @throws Exception
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	private String getQr(@RequestBody AmazonQRRequestData amazonQRRequestData) throws Exception{
		
		CredentialData credentialData = new CredentialData();
		AmazonPayDao amazonPayDao = new AmazonDaoImpl();
		String amount="";
		String payLoad="";
		String merchantId="";
		String accessKeyId="";
		String merchantTransactionId="";
		String storeId="";
		long currentTimeStamp=0;
        String urlparameter="";
        String payloadData="";
        String iv="";
        String key="";
        String pspId="";
        String terminalId="";
        
        String line = "";
    	StringBuilder response = new StringBuilder();
    	QRDao qrDao = new QRDaoImpl();
    	EventLogDao eventLogDao = new eventLogDaoImpl();
    	EventLogs eventLogRequest=new EventLogs();
    	
        
        //convert request data to json format
        
		JSONObject reqson = new JSONObject(amazonQRRequestData);

		System.out.println("Initial Request Data " + reqson.toString());
        
        
		// Random number generation for transactionId
		RandomString RandomString = new RandomString();
		merchantTransactionId = RandomString.generateRandomString(16);
		System.out.println("Random Number  " + merchantTransactionId);
		
		//getting amount from request data
        if (!amazonQRRequestData.getAmount().isEmpty()) {
			
			amount =amazonQRRequestData.getAmount();
			System.out.println("amount "+amount);
		}
        
       if (!amazonQRRequestData.getPspId().isEmpty()) {
			
    	   pspId=amazonQRRequestData.getPspId();
			System.out.println("pspId "+pspId);
		}
       
       if (!amazonQRRequestData.getPosId().isEmpty()) {
			
    	   terminalId=amazonQRRequestData.getPosId();
			System.out.println("terminalId "+terminalId);
		}
		
		//gettingg all the credential
		 try {
		 		credentialData = amazonPayDao.getAllCredentialDetail(amazonQRRequestData.getPspId()	, amazonQRRequestData.getPosId());
		 		 merchantId = credentialData.getMerchantId().trim();
		 		System.out.println("merchantId " + merchantId);
		 		accessKeyId = credentialData.getSecretKey().trim();
		 		System.out.println("accessKeyId " + accessKeyId);
		 		 /*storeId = credentialData.getImage().trim();
		 		System.out.println("storeId " + storeId);*/
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Problem while fetching credential from Database... "+e	);
			}		
        
		 try {
				storeId = amazonPayDao.getStoreIdForTerminal(pspId, terminalId);
				System.out.println("storeId " + storeId);
			} catch (Exception e) {
				// TODO: handle exception
			}
        
	    /*timestamp in milliseconds*/
		/*epochTime = System.currentTimeMillis();
		System.out.println("In milliseconds  "+epochTime); // prints a Unix timestamp in milliseconds
		System.out.println("In seconds "+epochTime/ 1000); // prints the same Unix timestamp in seconds
*/		
		
		 Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("signatureMethod", signatureMethod);
	        requestBody.put("signatureVersion", signatureVersion);
	        requestBody.put("merchantId", merchantId);
	        requestBody.put("accessKeyId", accessKeyId);
	        requestBody.put("amount", amount);
	        requestBody.put("currencyCode", currencyCode);
	        requestBody.put("merchantTransactionId", merchantTransactionId);
	        currentTimeStamp = Instant.now().toEpochMilli();
	        System.out.println("tsss-1 "+currentTimeStamp);
	        requestBody.put("timeStamp", String.valueOf(currentTimeStamp));
	        Map<String, Object> storeDetails = new HashMap<>();
	        storeDetails.put("cashierMobileNo", "9491868411");
	        storeDetails.put("storeId", storeId);
	        storeDetails.put("storeIdType", "AMAZON_STORE_ID");
	        requestBody.put("storeDetail", storeDetails);
	        
	        try {
				requestBody.put("signature", SignatureUtil.generateSignatures(getRequestToSign(requestBody), CommonService.getSecretKey(merchantId)));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        try {
            Map<String, String> map = CryptoForMerchant.encrypt(requestBody);
            System.out.println(map);
            payloadData=map.get("payload");
            
            System.out.println("payload "+payloadData);
            
             iv=map.get("iv");
            
            System.out.println("iv "+iv);
            
             key=map.get("key");
            
            System.out.println("key "+key);

        } catch (Exception e) {
              //  tring errorResponse = ((HttpClientErrorException) e).getResponseBodyAsString();
              // logger.error("Error Response is ----> {}", errorResponse);
            e.printStackTrace();
        }
        
		/*String jsonFormattedString = payloadinfo.toString().replaceAll("\\\\", "");
		System.out.println("final Request "+jsonFormattedString);*/
		
		JSONObject requestData=new JSONObject();
		requestData.put("payload", payloadData);
		requestData.put("iv", iv);
		requestData.put("key", key);
		
		urlparameter= requestData.toString();
		
		System.out.println("request Info "+urlparameter.toString().trim());
		
		try {
			URL url = new URL("https://amazonpay.amazon.in/payment/token/QR_CODE/v1");
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("merchantId", merchantId);
			String timeEpoch = String.valueOf(currentTimeStamp);
			System.out.println("tsss-2 "+timeEpoch);
			connection.setRequestProperty("timeStamp", timeEpoch);
			connection.setRequestProperty("attributableProgram", "S2S_PAY");
			connection.setRequestProperty("isSandbox", "true");
			System.out.println("ts "+connection.getRequestProperty("timeStamp")+"\n merchantId "+connection.getRequestProperty("merchantId")+"\n attributableProgram"+connection.getRequestProperty("attributableProgram")+"\n isSandbox"+connection.getRequestProperty("isSandbox"));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(

				connection.getOutputStream())) {
				wr.writeBytes(urlparameter);
			}

			int responseCode = connection.getResponseCode();
			System.out.println("responseCode "+responseCode);

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

				System.out.println("Amazonpay Response..." + response.toString());
				System.out.append("\r");

			}
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.." + e);
			e.printStackTrace();
		}
		
		JSONObject apresponse=new JSONObject(response.toString());
		JSONObject finalresponse=new JSONObject();
		
		try {
		JSONObject innerResponse=apresponse.getJSONObject("response");
		payLoad=innerResponse.getString("payLoad");
		
			if (innerResponse.getString("status").equalsIgnoreCase("SUCCESS")) {
				// This object used for save data in DB
				PaymentInitiation paymentInitiation = new PaymentInitiation();

				paymentInitiation.setPspId(pspId);
				paymentInitiation.setPspTransactionId(merchantTransactionId);
				paymentInitiation.setOrderId(merchantTransactionId);
				paymentInitiation.setDeviceId("AMAZONPAY-DEVICE");
				paymentInitiation.setAppId(amazonQRRequestData.getAppId());
				paymentInitiation.setStatus("Blank");
				paymentInitiation.setStatus("Blank");
				paymentInitiation.setTerminalId(terminalId);
				paymentInitiation.setMerchantId("zoneone");

				try {
					// Saving Payment Initiation
					qrDao.saveQRRequest(paymentInitiation);
				} catch (Exception e) {
					System.out.println("Exception in Saving QR payment initiation" + e);
				}
				
				try {
					eventLogRequest.setEventDetails(amazonQRRequestData.toString());
					eventLogRequest.setTerminalId(terminalId);
					eventLogRequest.setEventType("AmazonPay Dynamic QR Request");
					eventLogRequest.setVersion("v1");
					eventLogRequest.setOrderID(merchantTransactionId);
					eventLogDao.saveEventLog(eventLogRequest);
				} catch (Exception e) {
					System.out.println("Exception in Saving AP  data in Eveentlogs" + e);
					
				}
				
				finalresponse.put("qrData", payLoad);
				finalresponse.put("orderId", innerResponse.get("merchantTransactionId"));
				finalresponse.put("encryptedData", payLoad);
			    return finalresponse.toString();
		}else if(innerResponse.getString("reasonCode").equalsIgnoreCase("04-01")){
    	    finalresponse.put("qrData", "Invalid accessKeyId for signature authentication");
			finalresponse.put("orderId", "");
			finalresponse.put("encryptedData", "");
			return finalresponse.toString();
     } else{
    	    finalresponse.put("qrData", "");
			finalresponse.put("orderId", "FAILURE");
			finalresponse.put("encryptedData", "");
			return finalresponse.toString();
     }
		}catch (Exception e) {
    	    finalresponse.put("qrData", e);
			finalresponse.put("orderId", "");
			finalresponse.put("encryptedData", "");
			return finalresponse.toString();
		}
		
	}
	 private static RequestToSign getRequestToSign(Map<String, Object> fields) {
	        RequestToSign requestToSign = new RequestToSign();
	        requestToSign.setMethod(HTTPRequestMethod);
	        requestToSign.setHost(hostName);
	        requestToSign.setuRI(CanonicalURI);
	       // requestToSign.setTimeStamp(System.currentTimeMillis());
	        requestToSign.setParameters(fields);
	        return requestToSign;
	    }
	
	
}
