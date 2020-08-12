package com.limitlessmobility.iVendGateway.services.payu;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.CommonQRRequest;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

@Controller
@RequestMapping(value="/v2/payu")
public class PayuRefundService {


	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String refund(@RequestBody PayuRefundRequest payuRefundRequest) throws NoSuchAlgorithmException {
		String line="";
		StringBuilder response=new StringBuilder();
		OperatorDetail operatorDetail = new OperatorDetail();
		try {
//			String urlParameters = "{\"transactionId\":\"1234qwer\",\"transactionAmount\":\"1\"}";
//			String urlParameters = qrRequest;
			
			//Generate Random Order Id
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        
	        CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	        int operatorId = commonCredentialDao.getOperatorId(payuRefundRequest.getPosId(), "payu");
			
			operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, "payu");
			
			String merchantId = operatorDetail.getMerchantId();
			String merchantKey = operatorDetail.getMerchantKey();
			String salt = operatorDetail.getPspMguid();
			
			String input=""+merchantKey+"|cancel_refund_transaction|"+payuRefundRequest.getPayuId()+"|"+salt+"";
//			String input="2f8SnB|generate_dynamic_bharat_qr|{\"transactionId\":\""+uniqueOrderId+"\",\"transactionAmount\":\""+commonQrResuest.getAmount()+"\",\"outputType\":\"string\"}|LJR1BcnK";
			
			
			//String urlParameters = "key=2f8SnB&command=generate_dynamic_bharat_qr&hash=1a492065340c71b07d262ac1b4724a1ec53de6acbe3c43d8e73b6e779a8766515440c5b4f7c6928d3d367e6766779ece03fd341476e372565260a146340a64bb&var1={\"transactionId\":\"zcjS1hQ1iG\",\"transactionAmount\":\"1.0\",\"outputType\":\"string\"}";
			String urlParameters = "key="+merchantKey+"&command=cancel_refund_transaction&hash="+PayUHash.getHash(input)+"&var1="+payuRefundRequest.getPayuId()+"&var2="+uniqueOrderId+"&var3="+payuRefundRequest.getAmount()+"";
			
			
			
			
//			System.out.println(cdao.getPspMerchantKey(pspId,terminalId));
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			URL url = new URL("https://info.payu.in/merchant/postservice.php?form=2");
			
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			
			conn.setRequestProperty("key", merchantKey);
			conn.setRequestProperty("outputType", "string");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			conn.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			   wr.write( postData );
			}
			
			int responseCode = conn.getResponseCode();

			InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = conn.getInputStream();

	        }else {

	        is = conn.getErrorStream();

	        }
	              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From PayU..."+response.toString());
	              System.out.append("\r");
              }
              
            try {
            	JSONObject responseJson = new JSONObject(response.toString());
            	JSONObject responseReturn = new JSONObject();
            	if(responseJson.getInt("status")==1) {
            		responseReturn.put("status", "1");
            		responseReturn.put("message", responseJson.getString("msg"));
            		responseReturn.put("orderId", payuRefundRequest.getOrderId());
            		responseReturn.put("requestId", responseJson.getString("request_id"));
            		return responseReturn.toString();
            	} else {

            		responseReturn.put("status", "0");
            		responseReturn.put("message", "Refund Request Failed");
            		return responseReturn.toString();
            	
            	}
            } catch(Exception e) {
            	
            }
	              
	     // resize smaller by 50%
//            double percent = 0.5;
//            ImageResizer.resize(imagePath, imagePath, percent);
            
              rd.close(); is.close();    
	       
            return null;
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		System.out.println(response);
		return response.toString();
	}
	@RequestMapping(value = "/cancelTransaction", method = RequestMethod.POST)
	@ResponseBody
	public String cancelTransaction(@RequestBody PayuRefundRequest payuRefundRequest) throws NoSuchAlgorithmException {
		String line="";
		StringBuilder response=new StringBuilder();
		OperatorDetail operatorDetail = new OperatorDetail();
		try {
//			String urlParameters = "{\"transactionId\":\"1234qwer\",\"transactionAmount\":\"1\"}";
//			String urlParameters = qrRequest;
			
			//Generate Random Order Id
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        
	        CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	        int operatorId = commonCredentialDao.getOperatorId(payuRefundRequest.getPosId(), "payu");
			
			operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, "payu");
			
			String merchantId = operatorDetail.getMerchantId();
			String merchantKey = operatorDetail.getMerchantKey();
			String salt = operatorDetail.getPspMguid();
			
			String input=""+merchantKey+"|cancel_qr_payment|{\"transactionId\":\""+payuRefundRequest.getPayuId()+"\"}|"+salt+"";
			
//			String input="2f8SnB|generate_dynamic_bharat_qr|{\"transactionId\":\""+uniqueOrderId+"\",\"transactionAmount\":\""+commonQrResuest.getAmount()+"\",\"outputType\":\"string\"}|LJR1BcnK";
			
			
			String urlParameters = "key="+merchantKey+"&command=cancel_qr_payment&hash="+PayUHash.getHash(input)+"&var1={\"transactionId\":\""+payuRefundRequest.getPayuId()+"\"}";
			
			//String urlParameters = "key=2f8SnB&command=generate_dynamic_bharat_qr&hash=1a492065340c71b07d262ac1b4724a1ec53de6acbe3c43d8e73b6e779a8766515440c5b4f7c6928d3d367e6766779ece03fd341476e372565260a146340a64bb&var1={\"transactionId\":\"zcjS1hQ1iG\",\"transactionAmount\":\"1.0\",\"outputType\":\"string\"}";
//			String urlParameters = "key="+merchantKey+"&command=cancel_refund_transaction&hash="+PayUHash.getHash(input)+"&var1="+payuRefundRequest.getPayuId()+"&var2="+uniqueOrderId+"&var3="+payuRefundRequest.getAmount()+"";
			
			
			
			
//			System.out.println(cdao.getPspMerchantKey(pspId,terminalId));
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			URL url = new URL("https://info.payu.in/merchant/postservice.php?form=2");
			
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			
			conn.setRequestProperty("key", merchantKey);
			conn.setRequestProperty("outputType", "string");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			conn.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			   wr.write( postData );
			}
			
			int responseCode = conn.getResponseCode();

			InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = conn.getInputStream();

	        }else {

	        is = conn.getErrorStream();

	        }
	              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From PayU..."+response.toString());
	              System.out.append("\r");
              }
              
            try {  
            JSONObject responseFromAPI = new JSONObject(response.toString());
            if (responseFromAPI.has("qrString")) {
            	JSONObject responseObj = new JSONObject();
            	responseObj.put("qrData", responseFromAPI.getString("qrString"));
            	responseObj.put("orderId", uniqueOrderId);
            	responseObj.put("encryptedData", responseFromAPI.getString("qrString"));
            	
            	/*PaymentInitiation paymentInitiation = new PaymentInitiation();
            	paymentInitiation.setOrderId(uniqueOrderId);
    	        paymentInitiation.setAuthAmount(String.valueOf(commonQrResuest.getAmount()));
    	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
    	        paymentInitiation.setTerminalId(commonQrResuest.getPosId());
    	        paymentInitiation.setCurrency("INR");
    	        paymentInitiation.setProductName(commonQrResuest.getProductId());
    	        paymentInitiation.setProductPrice(String.valueOf(commonQrResuest.getAmount()));
    	        paymentInitiation.setDeviceId(commonQrResuest.getPosId());
    	        paymentInitiation.setPspId("payu");
    	        paymentInitiation.setMerchantId(merchantId);
    	        paymentInitiation.setComments("");
    	        paymentInitiation.setAppId(commonQrResuest.getAppId());
    	        
    	        paymentInitiation.setOrderId(uniqueOrderId);
    			
    	        QRDao qrDao = new QRDaoImpl();
    				//Saving Payment Initiation
    				qrDao.saveQRRequest(paymentInitiation);*/
            	
            	return responseObj.toString();
            }
            } catch (Exception e) {
				// TODO: handle exception
			}
	              
	     // resize smaller by 50%
//            double percent = 0.5;
//            ImageResizer.resize(imagePath, imagePath, percent);
            
              rd.close(); is.close();    
	       
            return null;
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		System.out.println(response);
		return response.toString();
	}
	
	@RequestMapping(value = "/refundStatus", method = RequestMethod.POST)
	@ResponseBody
	public String refundStatus(@RequestBody PayuRefundRequest payuRefundRequest) throws NoSuchAlgorithmException {
		String line="";
		StringBuilder response=new StringBuilder();
		OperatorDetail operatorDetail = new OperatorDetail();
		try {
//			String urlParameters = "{\"transactionId\":\"1234qwer\",\"transactionAmount\":\"1\"}";
//			String urlParameters = qrRequest;
			
			//Generate Random Order Id
			RandomString randomOrderId = new RandomString();
	        String uniqueOrderId = randomOrderId.generateRandomString(16);
	        
	        CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	        int operatorId = commonCredentialDao.getOperatorId(payuRefundRequest.getPosId(), "payu");
			
			operatorDetail = commonCredentialDao.getPspConfigDetailForWallet(operatorId, "payu");
			
			String merchantId = operatorDetail.getMerchantId();
			String merchantKey = operatorDetail.getMerchantKey();
			String salt = operatorDetail.getPspMguid();
			
			String input=""+merchantKey+"|check_action_status|"+payuRefundRequest.getPayuId()+"|"+salt+"";
			
//			String input="2f8SnB|generate_dynamic_bharat_qr|{\"transactionId\":\""+uniqueOrderId+"\",\"transactionAmount\":\""+commonQrResuest.getAmount()+"\",\"outputType\":\"string\"}|LJR1BcnK";
			
			
//			String urlParameters = "key="+merchantKey+"&command=check_action_status&hash="+PayUHash.getHash(input)+"&var1={\"requestId\":\""+payuRefundRequest.getPayuId()+"\"}";
			String urlParameters = "key="+merchantKey+"&command=check_action_status&hash="+PayUHash.getHash(input)+"&var1="+payuRefundRequest.getPayuId()+"";
			
			//String urlParameters = "key=2f8SnB&command=generate_dynamic_bharat_qr&hash=1a492065340c71b07d262ac1b4724a1ec53de6acbe3c43d8e73b6e779a8766515440c5b4f7c6928d3d367e6766779ece03fd341476e372565260a146340a64bb&var1={\"transactionId\":\"zcjS1hQ1iG\",\"transactionAmount\":\"1.0\",\"outputType\":\"string\"}";
//			String urlParameters = "key="+merchantKey+"&command=cancel_refund_transaction&hash="+PayUHash.getHash(input)+"&var1="+payuRefundRequest.getPayuId()+"&var2="+uniqueOrderId+"&var3="+payuRefundRequest.getAmount()+"";
			
			
			
			
//			System.out.println(cdao.getPspMerchantKey(pspId,terminalId));
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			URL url = new URL("https://info.payu.in/merchant/postservice.php?form=2");
			
//			URL url = new URL("https://mobiletest.payu.in/merchant/postservice.php");
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			conn.setRequestProperty("charset", "utf-8");
			
			conn.setRequestProperty("key", merchantKey);
			conn.setRequestProperty("outputType", "string");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			conn.setUseCaches(false);
			try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
			   wr.write( postData );
			}
			
			int responseCode = conn.getResponseCode();

			InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK){

	        is = conn.getInputStream();

	        }else {

	        is = conn.getErrorStream();

	        }
	              
              BufferedReader rd = new BufferedReader(new InputStreamReader(is));
              line="";
              while((line = rd.readLine()) != null) {
	              response.append(line);
	              System.out.println("RESPONSE From PayU..."+response.toString());
	              System.out.append("\r");
              }
              
            try {  
            JSONObject responseFromAPI = new JSONObject(response.toString());
            if (responseFromAPI.has("qrString")) {
            	JSONObject responseObj = new JSONObject();
            	responseObj.put("qrData", responseFromAPI.getString("qrString"));
            	responseObj.put("orderId", uniqueOrderId);
            	responseObj.put("encryptedData", responseFromAPI.getString("qrString"));
            	
            	/*PaymentInitiation paymentInitiation = new PaymentInitiation();
            	paymentInitiation.setOrderId(uniqueOrderId);
    	        paymentInitiation.setAuthAmount(String.valueOf(commonQrResuest.getAmount()));
    	        paymentInitiation.setMerchantOrderId(uniqueOrderId);
    	        paymentInitiation.setTerminalId(commonQrResuest.getPosId());
    	        paymentInitiation.setCurrency("INR");
    	        paymentInitiation.setProductName(commonQrResuest.getProductId());
    	        paymentInitiation.setProductPrice(String.valueOf(commonQrResuest.getAmount()));
    	        paymentInitiation.setDeviceId(commonQrResuest.getPosId());
    	        paymentInitiation.setPspId("payu");
    	        paymentInitiation.setMerchantId(merchantId);
    	        paymentInitiation.setComments("");
    	        paymentInitiation.setAppId(commonQrResuest.getAppId());
    	        
    	        paymentInitiation.setOrderId(uniqueOrderId);
    			
    	        QRDao qrDao = new QRDaoImpl();
    				//Saving Payment Initiation
    				qrDao.saveQRRequest(paymentInitiation);*/
            	
            	return responseObj.toString();
            }
            } catch (Exception e) {
				// TODO: handle exception
			}
	              
	     // resize smaller by 50%
//            double percent = 0.5;
//            ImageResizer.resize(imagePath, imagePath, percent);
            
              rd.close(); is.close();    
	       
            return null;
		} catch (IOException e) {
			System.out.println("Exception in DynamicQRService class.."+e);
			e.printStackTrace();
		}
		System.out.println(response);
		return response.toString();
	}
}

