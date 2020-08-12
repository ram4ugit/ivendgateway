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

import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDao;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonCredentialDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonDao;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.BQRRequestData;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping("/v2/bharatqr")
public class BharatQRCheckStatusV2 {
	
	private static final Logger logger = Logger.getLogger(BharatQRCheckStatus.class);
	
	CommonDao cdao = new CommonDaoImpl();
	CommonCredentialDao commonCredentialDao = new CommonCredentialDaoImpl();
	boolean isexist=true;
	
	public static final String VERSION = "v1";

	public static String fromEntity = "AG1";
	
	public static String PAYMENTID="2";
	
	PaymentInitiation initiation=new PaymentInitiation();

	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getBharatQRCheckStatus(@RequestBody BQRRequestData bqrCheckStatus) throws IOException, JSONException {

		System.out.println("Calling getBharatQRCheckStatus.....");
		
		
		String walletId = "";
		String walletKey="";
		String txnId="";
		String pspid="";
		String terminalId="";
		String txnType="";
		String trId="";
		
		
		try {
			OperatorDetail operatorDetail = new OperatorDetail();
			if(bqrCheckStatus.getPosId()!=null ) {
				terminalId= bqrCheckStatus.getPosId();
				if(bqrCheckStatus.getPspId()!=null) {
					pspid=bqrCheckStatus.getPspId();
					
					int operatorId = commonCredentialDao.getOperatorId(terminalId, pspid);
					
					operatorDetail = commonCredentialDao.getPspConfigDetail(operatorId, pspid);
					
					
					walletId = operatorDetail.getMerchantId();
					
					walletKey = operatorDetail.getMerchantKey();
					
				}
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		String bankCode = cdao.getBankCode(bqrCheckStatus.getPspId());
		
		EventLogs eventLogRequest=new EventLogs();

		JSONObject parent = new JSONObject();

		parent.put("fromEntity", fromEntity);

		parent.put("bankCode", bankCode);
		
		BharatQRDao  bharatQRDao1=new BharatQRDaoImpl();
		
		String procuctName=bharatQRDao1.getProductByTxnId(bqrCheckStatus.getTxnId());
		
		System.out.println("ProductName "+procuctName);
		
		
		/*isexist=bharatQRDao1.checkTxnId(bqrCheckStatus.getTxnId());
        
   	    System.out.println("isexist "+isexist);
        if (isexist) {
       	 bharatQRDao1.updateTerminalDevice(bqrCheckStatus.getTxnId(),  bqrCheckStatus.getPosId());
			
		}*/
		
		JSONObject child = new JSONObject();
		
		child.put("TID", walletKey);
		
		System.out.println("TID "+walletKey);
		child.put("amount", bqrCheckStatus.getAmount());
		System.out.println("Amount " +bqrCheckStatus.getAmount());
		child.put("txnId", bqrCheckStatus.getTxnId());
		System.out.println("TXN ID" +bqrCheckStatus.getTxnId());
		
		try {
			if (bqrCheckStatus.getTxnId()!=null) {
				txnId=bqrCheckStatus.getTxnId();
				trId=bharatQRDao1.getTrIDByTxnId(txnId);
				System.out.println("TRID "+trId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		child.put("trId", trId);
		
		parent.put("data", child);

		String urlParameters = "parm=" + parent.toString();
		
		
		logger.info("BharatQR CheckStatus Request..."+urlParameters);
		System.out.println("BharatQR CheckStatus Request Format  :  " +urlParameters);
		String line = "";

		StringBuilder response = new StringBuilder();

		try {

			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			//String request = "https://te1.in.worldline.com:8443/bharatqr/qr/txnEnquiry";
			String request = "https://qrcode.in.worldline.com/bharatqr/qr/txnEnquiry";
			URL url = new URL(request);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);

			conn.setInstanceFollowRedirects(false);

			conn.setRequestMethod("POST");
			
			conn.setConnectTimeout(1000*10);
			conn.setReadTimeout(10000);

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			conn.setRequestProperty("parm", urlParameters);

			conn.setRequestProperty("charset", "utf-8");

			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));

			conn.setUseCaches(false);

			int responseCode = conn.getResponseCode();
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.writeBytes(urlParameters);
			}

			

			System.out.println(responseCode + "\tSuccess");

			InputStream is;

			if (responseCode == HttpURLConnection.HTTP_OK) {

				is = conn.getInputStream();

			} else {

				is = conn.getErrorStream();
			}

			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			line = "";
			while ((line = rd.readLine()) != null) {
				System.out.append(line);
				System.out.println("line.." + line);
				response.append(line);
				System.out.println("RESPONSE..." + response.toString());
				System.out.append("\r");

			}
			rd.close(); is.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("BharatQR CheckStatus Response..."+response.toString());
		System.out.println("BharatQR CheckStatus Response..."+response.toString());
		
		JSONObject checkStatusResponseJson = new JSONObject(response.toString());
		EventLogs eventLogResponse = new EventLogs();
		PaymentTransaction callbackTransactions = new PaymentTransaction();
		JSONObject jrespObject=null;
		if (checkStatusResponseJson.get("responseObject")!=null) {
			 jrespObject =new JSONObject(checkStatusResponseJson.get("responseObject").toString());
			 System.out.println("jrespObject "+jrespObject);
		}else if (checkStatusResponseJson.get("responseObject").toString().isEmpty()==true) {
			checkStatusResponseJson.put("responseObject", "null");
			System.out.println("else if");
		} 
		else {
			checkStatusResponseJson.put("responseObject", "null");
			System.out.println("else responseObject ");
		}
		
		
	     
		
		System.out.println("jrespObject "+jrespObject);
		
		try {
			if (jrespObject.getString("transaction_type")!=null) {
				txnType=jrespObject.getString("transaction_type");
				if (txnType.equalsIgnoreCase("2")) {
					isexist=bharatQRDao1.checkTxnId(trId);
					 if (isexist) {
				       	 bharatQRDao1.updateTerminalDevice(trId,  terminalId);
						}
				}
				else {
					isexist=bharatQRDao1.checkTxnId(txnId);
					System.out.println("isexist "+isexist);
					 if (isexist) {
				       	 bharatQRDao1.updateTerminalDevice(txnId,  terminalId);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
     
		
		try {
			String checkResponse = checkStatusResponseJson.getString("status");
			if (checkResponse.equalsIgnoreCase("SUCCESS")) {

				
                callbackTransactions.setAppId(bqrCheckStatus.getAppId());
                callbackTransactions.setPspId(bqrCheckStatus.getPspId());
                callbackTransactions.setTerminalId(bqrCheckStatus.getPosId());
                callbackTransactions.setMerchantId("zoneone");
                callbackTransactions.setProductName(procuctName);
                callbackTransactions.setProductPrice(jrespObject.getString("txn_amount"));
                callbackTransactions.setPspTransactionId(jrespObject.getString("ref_no"));
                callbackTransactions.setAuthAmount(jrespObject.getDouble("txn_amount"));
                if(txnType.equalsIgnoreCase("1")){
                	callbackTransactions.setAuthCode(jrespObject.getString("auth_code"));
                	System.out.println("authCode "+callbackTransactions.getAuthCode());
                	callbackTransactions.setCustomerName(jrespObject.getString("customer_name"));
                	System.out.println("customer_name "+callbackTransactions.getCustomerName());
                }
                callbackTransactions.setTransactionType(jrespObject.getString("transaction_type"));
                callbackTransactions.setSettlementAmount(jrespObject.getString("settlement_amount"));
                callbackTransactions.setStatus(checkStatusResponseJson.getString("status"));
				callbackTransactions.setMerchantOrderId(bqrCheckStatus.getTxnId());
				callbackTransactions.setOrderId(bqrCheckStatus.getTxnId());
				callbackTransactions.setServiceType("Check_Transaction_Status");
				
				
				TransactionDao transactionDao = new TransactionDaoImpl();
			    transactionDao.saveTransaction(callbackTransactions);
			}

		} catch (Exception e) {
			System.out.println("BharatQR Check Status Error:" + e);
		}
		
		
	
		jrespObject.remove("bank_code");
		jrespObject.remove("aggregator_id");
		jrespObject.remove("mpan");
		jrespObject.remove("time_stamp");
		jrespObject.remove("mid");
		jrespObject.remove("customer_vpa");
		jrespObject.remove("transaction_type");
		jrespObject.remove("auth_code");
		
		jrespObject.remove("consumer_pan");
		jrespObject.remove("txn_currency");
		jrespObject.remove("secondary_id");
		jrespObject.remove("settlement_amount");
		jrespObject.remove("primary_id");
		jrespObject.remove("customer_name");
		jrespObject.remove("txn_amount");
		jrespObject.remove("response_code");
		
		System.out.println("BharatQR merch_vpa");
		jrespObject.remove("merch_vpa");
		jrespObject.remove("merchant_vpa");
		System.out.println("BharatQR merchant_vpa");
		
		
		jrespObject.put("statusMessage", checkStatusResponseJson.getString("status"));
		jrespObject.put("status", checkStatusResponseJson.getString("status"));
		jrespObject.put("statusCode", "SS_001");
		jrespObject.put("amount", bqrCheckStatus.getAmount());
		
		logger.info("BharatQR CheckStatus final  Response..."+jrespObject.toString());
		System.out.println("BharatQR CheckStatus final  Response..."+jrespObject.toString());
		
		
		

		try {
			EventLogDao edao = new eventLogDaoImpl();

			eventLogResponse.setTerminalId(bqrCheckStatus.getPosId());
			eventLogRequest.setTerminalId(bqrCheckStatus.getPosId());
			eventLogRequest.setEventDetails(urlParameters);
			eventLogResponse.setEventDetails(checkStatusResponseJson.toString());
			eventLogResponse.setEventType("BharatQR trnx CheckStatus API Response");
			eventLogRequest.setEventType("BharatQR trnx CheckStatus API Request");
			eventLogResponse.setVersion(VERSION);
			eventLogRequest.setVersion(VERSION);

			edao.saveEventLog(eventLogRequest);
			
			edao.saveEventLog(eventLogResponse);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return jrespObject.toString();
	}
}
