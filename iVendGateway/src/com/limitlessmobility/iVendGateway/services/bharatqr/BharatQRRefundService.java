package com.limitlessmobility.iVendGateway.services.bharatqr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDao;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDaoImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;
import com.limitlessmobility.iVendGateway.model.bharatqr.RefundRequestModel;

@Controller
@RequestMapping(value="/v1/bharatqr")
public class BharatQRRefundService {
	

	private static final Logger logger = Logger.getLogger(BharatQRRefundService.class);
	private static final String VERSION="v1";
	
	
	BharatQRDao bharatQRDao=new BharatQRDaoImpl();
	PspTerminal pspTerminal=new PspTerminal();
	
	private static final String FROM_ENTITY = "APP";

	private static final String REFUND_REASON = "NA";
	
	private static final String MOBILE_NUMBER = "9643779983";
	
	
	
	
	@RequestMapping(value="/refund" ,method=RequestMethod.POST)
	@ResponseBody
	public String getPaymentrefund(@RequestBody RefundRequestModel refundRequestModel) throws JSONException {
		
		System.out.println("rrn...... "+refundRequestModel.getRrn());
		System.out.println("rrn...... "+refundRequestModel.getRefundAmount());
		
		
		
		JSONObject jsonReq=new JSONObject(refundRequestModel);
		
		
		logger.info("Refund request data for Android..."+jsonReq.toString());
		
		System.out.println("Refund request data for Android...." +jsonReq.toString());
		
		String pspId="";
		String amount="";
		String refundId="";
		String authCode="";
		String txnType="";
		String bankCode="";
		String txnId="";
		String refNo="";
		String tId="";
		
		if (refundRequestModel.getRrn()!=null) {
			txnId=refundRequestModel.getRrn();
			logger.info("TxnId..."+txnId);
		} 
		
        try {
        	if (txnId!=null) {
        	refNo = bharatQRDao.getRefNoByTxnId(txnId);
    		System.out.println("RRN Number .....  " + refNo);
    		logger.info("REFNO..." + refNo);
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("Amount "+refundRequestModel.getRefundAmount());
		logger.info("AMOUNT..."+refundRequestModel.getRefundAmount());
		
		/*String pspId=bharatQRDao.getPspIdByPSPTxnId(refNo);
		logger.info("PSPID..."+pspId);
     	System.out.println("pspId "+pspId);*/
		
		RandomString RandomString = new RandomString();
		refundId = RandomString.generateRandomString(10); 
		System.out.println("Random Number  "+refundId);
		
	    amount =refundRequestModel.getRefundAmount();
	    logger.info("amount..."+amount);
	    try {
	    	bankCode=bharatQRDao.getBankCodeByPspId(pspId);
			logger.info("bankCode..."+bankCode);
			pspTerminal=bharatQRDao.getTransactionDetails(txnId);
			pspId=pspTerminal.getPspId();
			logger.info("PSPID..."+pspId);
			System.out.println("pspId "+pspId);
			tId=bharatQRDao.getTerminalId(pspId).trim();
			logger.info("TID..."+tId);
			if (pspTerminal.getAuthCode()!=null) {
				authCode=pspTerminal.getAuthCode();
				logger.info("authCode..."+authCode);
				if (pspTerminal.getTxnType()!=null) {
					txnType=pspTerminal.getTxnType();
					logger.info("txnType..."+txnType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    System.out.println("authCode   "+authCode);
		System.out.println("txnType   "+txnType);
		System.out.println("BankCode   "+bankCode);
		
		JSONObject parent = new JSONObject();
		String line = "";
		StringBuilder response = new StringBuilder();
		
			parent.put("fromEntity", FROM_ENTITY);
			parent.put("bankCode", bankCode);
			
			JSONObject child = new JSONObject();
			child.put("txnType", txnType);
			child.put("TID", tId);
			child.put("rrn", refNo);
			if (txnType.equalsIgnoreCase("1")) {
				child.put("authCode", authCode);
			} 
			child.put("refundAmount", amount);
			child.put("refundReason",REFUND_REASON);
			child.put("mobileNumber", MOBILE_NUMBER);
			child.put("refundId", refundId);
			parent.put("data", child);
			
			String urlParameters = "parm=" + parent.toString();
			
			logger.info("BharatQR Refund Request..."+urlParameters);
			System.out.println("BharatQR Refund Request..." + urlParameters);
			
			try {
				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;
				String request = "https://qrcode.in.worldline.com/bharatqr/qr/refund";
				URL url = new URL(request);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setInstanceFollowRedirects(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("parm", urlParameters);
				conn.setRequestProperty("charset", "utf-8");
				conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				conn.setUseCaches(false);
				try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
					wr.writeBytes(urlParameters);
				}

				int responseCode = conn.getResponseCode();

				InputStream is;

				if (responseCode == HttpURLConnection.HTTP_OK) {

					is = conn.getInputStream();

				} else {

					is = conn.getErrorStream();

				}

				System.out.println("Response Code : " + responseCode);

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				line = "";

				while ((line = rd.readLine()) != null) {
					response.append(line);
					System.out.println("RESPONSE..." + response.toString());
					response.append('\r');
				}
				rd.close();
			} catch (IOException e) {
				System.out.println("Exception in Refund Service class.." + e);
				e.printStackTrace();
			}
			System.out.println(response.toString());
			
			logger.info("BharatQR Refund Response..."+response.toString());

		
		return response.toString();
	}
}
