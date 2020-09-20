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

import org.apache.log4j.Logger;
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
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.model.amazonpay.AmazonQRRequestData;
import com.limitlessmobility.iVendGateway.model.amazonpay.CredentialData;
import com.limitlessmobility.iVendGateway.model.amazonpay.RequestToSign;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

@Controller
@RequestMapping("/v2/amazonpay")
public class AmazonPayQRServiceV2{
 
	private static final Logger logger = Logger.getLogger(AmazonPayQRServiceV2.class);
	
	private static final String currencyCode="INR";
	
	private static final String signatureMethod="HmacSHA384";
	
	private static final String signatureVersion="4";
	
    private static final String HTTPRequestMethod="POST";
	
	private static final String hostName ="amazonpay.amazon.in";
	
	private static final String CanonicalURI ="/payment/token/QR_CODE/v1";
	
	
	/*private static final String keyId="18f35507-94e8-45d2-98a6-81bf7fed090c"; 
	private static final String accessKeyId="07ccf898-a2cf-4765-b517-12330e931117";*/
	
	
	/**
	 * This API is Used for get QR of Amazonpay. Called by common QR.
	 * @param amazonQRRequestData
	 * @version 2
	 * @throws Exception
	 */
	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	private String getQr(@RequestBody AmazonQRRequestData amazonQRRequestData) throws Exception{
		
		String amount="";
		String payLoad="";
		String merchantId="";
		String accessKeyId="";
		String walletId = "";
		String walletKey="";
		String pspId="";
		String terminalId="";
		String storeId="";
		String merchantTransactionId="";
		long currentTimeStamp=0;
        String urlparameter="";
        String payloadData="";
        String iv="";
        String key="";
        boolean sandBox = true;
        
        
        String line = "";
    	StringBuilder response = new StringBuilder();
    	CredentialData credentialData = new CredentialData();
		AmazonPayDao amazonPayDao = new AmazonDaoImpl();
    	QRDao qrDao = new QRDaoImpl();
    	EventLogDao eventLogDao = new eventLogDaoImpl();
    	EventLogs eventLogRequest=new EventLogs();
    	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
    	
    	
    	//getting all the credential
    	
    	try {
			OperatorDetail operatorDetail = new OperatorDetail();
			if(amazonQRRequestData.getPosId()!=null ) {
				terminalId= amazonQRRequestData.getPosId();
				System.out.println("POSID " + amazonQRRequestData.getPosId());
				if(amazonQRRequestData.getPspId()!=null) {
					pspId=amazonQRRequestData.getPspId();
					System.out.println("PSPID " + amazonQRRequestData.getPspId());
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
					
					
					merchantId = operatorDetail.getMerchantId().trim();
					System.out.println("merchantId " + merchantId);
					accessKeyId = operatorDetail.getPspMguid().trim();
					System.out.println("accessKeyId " + accessKeyId);
					storeId = commonCredentialDao.getStoreId(terminalId, pspId);
					System.out.println("storeId " + storeId);
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
			
		// Random number generation for transactionId
		RandomString RandomString = new RandomString();
		merchantTransactionId= RandomString.generateRandomString(16);
		System.out.println("Random Number  " + merchantTransactionId);
		
        //convert request data to json format
		JSONObject reqson = new JSONObject(amazonQRRequestData);

		System.out.println("Initial Request Data " + reqson.toString());
        
        
		
		//getting amount from request data
        if (!amazonQRRequestData.getAmount().isEmpty()) {
			
			amount =amazonQRRequestData.getAmount();
			System.out.println("amount "+amount);
		}
        
      
       
		System.out.println("APPID " + amazonQRRequestData.getAppId());
		System.out.println("PRODUCTID " + amazonQRRequestData.getProductId());

		
		
		
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
