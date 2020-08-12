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
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;
import com.limitlessmobility.iVendGateway.model.bharatqr.RefundRequestModel;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;

@Controller
@RequestMapping(value = "/v2/bharatqr")
public class BharatQRRefundServiceV2 {

	private static final Logger logger = Logger.getLogger(BharatQRRefundServiceV2.class);
	private static final String VERSION = "v1";

	BharatQRDao bharatQRDao = new BharatQRDaoImpl();
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();

	private static final String FROM_ENTITY = "APP";

	private static final String REFUND_REASON = "NA";

	private static final String MOBILE_NUMBER = "9643779983";

	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String getPaymentrefund(
	        @RequestBody RefundRequestModel refundRequestModel)
	        throws JSONException {

		System.out.println("BharatQR Refund API is calling....... ");

		JSONObject jsonReq = new JSONObject(refundRequestModel);

		logger.info("Refund request InitialData..." + jsonReq.toString());
		System.out
		        .println("Refund request InitialData...." + jsonReq.toString());

		String refNo = "";
		String pspId = "";
		String tId = "";
		String amount = "";
		String bankCode = "";
		String authCode = "";
		String txnType = "";
		String terminalId = "";
		String line = "";
		String txnId="";
		
		if (refundRequestModel.getRrn()!=null) {
			txnId=refundRequestModel.getRrn();
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

		amount = refundRequestModel.getRefundAmount();
		System.out.println(
		        "Amount .....  " + refundRequestModel.getRefundAmount());
		logger.info("AMOUNT..." + amount);

		try {
			OperatorDetail operatorDetail = new OperatorDetail();
			PspTerminal pspTerminal = new PspTerminal();
			pspTerminal = bharatQRDao.getTransactionDetails(refundRequestModel.getRrn());
			if (pspTerminal.getTerminal() != null) {
				terminalId = pspTerminal.getTerminal();
				logger.info("TERMINALID..." + terminalId);
				System.out.println("TERMINALID " + terminalId);
				if (pspTerminal.getPspId() != null) {
					pspId = pspTerminal.getPspId();
					logger.info("PSPID..." + pspId);
					System.out.println("pspId " + pspId);

					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspId);

					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspId);
					bankCode = bharatQRDao.getBankCodeByPspId(pspId);
					System.out.println("BankCode Number .. " + bankCode);
					tId = operatorDetail.getMerchantKey();
					System.out.println("MerchantKey .. " + tId);
					authCode = pspTerminal.getAuthCode();
					System.out.println("AuthCode ..  " + authCode);
					txnType = pspTerminal.getTxnType();
					System.out.println("TxnType ..  " + txnType);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Unique random number for RefundOrderId */
		RandomString RandomString = new RandomString();
		String refundId = RandomString.generateRandomString(10);
		System.out.println("Random Number  " + refundId);

		//Request format for refund in JSON
		JSONObject parent = new JSONObject();
		StringBuilder response = new StringBuilder();
			System.out.println("BankCode   " + bankCode);
			parent.put("fromEntity", FROM_ENTITY);
			parent.put("bankCode", bankCode);

			JSONObject child = new JSONObject();
			child.put("txnType", txnType);
			child.put("TID", tId);
			child.put("rrn", refNo);
			if (txnType.equalsIgnoreCase("1")) {
				child.put("authCode", authCode);
				System.out.println("BharatQR Refund  ");
			} 
			child.put("refundAmount", amount);
			child.put("refundReason", REFUND_REASON);
			child.put("mobileNumber", MOBILE_NUMBER);
			child.put("refundId", refundId);
			parent.put("data", child);

			String urlParameters = "parm=" + parent.toString();
			logger.info("BharatQR Refund Payload Format... " + urlParameters);
			System.out.println(
			        "BharatQR Refund Payload Format... " + urlParameters);

			try {
				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;
				String request = "https://qrcode.in.worldline.com/bharatqr/qr/refund";
				URL url = new URL(request);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setInstanceFollowRedirects(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				conn.setRequestProperty("parm", urlParameters);
				conn.setRequestProperty("charset", "utf-8");
				conn.setRequestProperty("Content-Length",
				        Integer.toString(postDataLength));
				conn.setUseCaches(false);
				try (DataOutputStream wr = new DataOutputStream(
				        conn.getOutputStream())) {
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

				BufferedReader rd = new BufferedReader(
				        new InputStreamReader(conn.getInputStream()));

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
			logger.info("BharatQR Refund Response..." + response.toString());


		return response.toString();
	}
}
