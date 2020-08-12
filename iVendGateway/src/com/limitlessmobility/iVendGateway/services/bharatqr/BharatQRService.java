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
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.EventLogDao;
import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.eventLogDaoImpl;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDao;
import com.limitlessmobility.iVendGateway.dao.bharatqr.BharatQRDaoImpl;
import com.limitlessmobility.iVendGateway.model.bharatqr.BCreateQRRequestData;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

@Controller
@RequestMapping("/v1/bharatqr")
public class BharatQRService {
	
	private static final Logger logger = Logger.getLogger(BharatQRService.class);

	public static String FROMENTITY = "AG1";

	public static String BANKCODE = "00031";

	//public static String MID = "000022600018181";
	//public static String QRTYPE = "2";
	//public static String ADDDATA = "2485cd530fe2351c";
	public static String QRTYPE = "2";
	public static String PRGTYPE = "2";
	public static String PAYMENTID="2";
	
	QRDao qrDao = new QRDaoImpl();

	@RequestMapping(value = "/getqr", method = RequestMethod.POST)
	@ResponseBody
	public String createQRCode(@RequestBody BCreateQRRequestData bharatQRRequestModel) throws Exception {

		logger.info("Calling BharatQR createQR...");
		
		System.out.println("createQRCode.....");
		EventLogs eventLogRequest = new EventLogs();
		

		System.out.println("POSID " + bharatQRRequestModel.getPosId());
		System.out.println("PSPID " + bharatQRRequestModel.getPspId());
		System.out.println("AMOUNT " + bharatQRRequestModel.getAmount());
		System.out.println("APPID " + bharatQRRequestModel.getAppId());
		System.out.println("PRODUCTID " + bharatQRRequestModel.getProductId());
		
	   JSONObject andData=new JSONObject(bharatQRRequestModel);
	   String req= andData.toString();
	   
	   logger.info("BharatQR CreateQR Android Request data..."+req);
	   System.out.println("BharatQR CreateQR Android Request data "+req);
       
	   eventLogRequest.setEventDetails(req);
       eventLogRequest.setTerminalId(andData.getString("posId"));
       eventLogRequest.setEventType("BhratQR Dynamic QR Request");
       eventLogRequest.setVersion("v1");
       eventLogRequest.setOrderID("NA");
       EventLogDao eventLogDao=new eventLogDaoImpl();
       eventLogDao.saveEventLog(eventLogRequest);
		
       EventLogs eventLogResponse = new EventLogs();
		JSONObject parent = new JSONObject();
		String line = "";
		StringBuilder response = new StringBuilder();
		
		parent.put("fromEntity", FROMENTITY);

		parent.put("bankCode", BANKCODE);
		
		BharatQRDao bharatQRDao=new BharatQRDaoImpl();
		
		String device_id=bharatQRDao.getDeviceIDByTerminalId(bharatQRRequestModel.getPosId());
  
		System.out.println("device_id " +device_id);
		
//	    String mid=bharatQRDao.getMachineIdByDeviceId(device_id);
		//String mid="30371900";
		String tId=bharatQRDao.getTIDByPspTerminalPaymentId(bharatQRRequestModel.getPspId(), bharatQRRequestModel.getPosId(), PAYMENTID).trim();
		
		System.out.println("mid   "+tId);
		
		//String merchant_id=bharatQRDao.getMIDByPspId(bharatQRRequestModel.getPspId()).trim();
		String merchant_id=bharatQRDao.getMIDPspTerminalPaymentId(bharatQRRequestModel.getPspId(), bharatQRRequestModel.getPosId(), PAYMENTID).trim();
		
		System.out.println("TID "+tId);
		JSONObject child = new JSONObject();
		child.put("MID", merchant_id);
		child.put("TID", tId);
		child.put("amount", bharatQRRequestModel.getAmount());
		child.put("qrType", QRTYPE);
		child.put("prgType", PRGTYPE);
		child.put("addData", bharatQRRequestModel.getPosId());

		parent.put("data", child);

		String urlParameters = "parm=" + parent.toString();
		
		//This object used for save data in DB
		PaymentInitiation paymentInitiation = new PaymentInitiation();

		logger.info("BharatQR CreateQR Request data...  "+urlParameters);
		System.out.println("BharatQR CreateQR Request data...  " + urlParameters);

		  try 
		  {
		  byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8); 
		  int postDataLength = postData.length; 
		  String request ="https://qrcode.in.worldline.com/bharatqr/qr/getQR";
		  URL url = new URL(request);
		  HttpURLConnection conn = (HttpURLConnection)
		  url.openConnection();
		  conn.setDoOutput(true);
		  conn.setInstanceFollowRedirects(false);
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		  conn.setRequestProperty("parm", urlParameters);
		  conn.setRequestProperty("charset", "utf-8");
		  conn.setRequestProperty("Content-Length",Integer.toString(postDataLength));
		  conn.setUseCaches(false);
		  try 
		  (DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) 
		  { 
			  wr.writeBytes(urlParameters);
			  }
		 
		  int responseCode = conn.getResponseCode();
		  
		  InputStream is;

	        if(responseCode == HttpURLConnection.HTTP_OK)
	        {

	        is = conn.getInputStream();

	        }else {

	        is = conn.getErrorStream();

	        }
		  
		  System.out.println("Response Code : " + responseCode);
		  logger.info("Response Code..."+responseCode);
		  
		  BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  
		  line="";
		  
		  while ((line = rd.readLine()) != null) {
		  response.append(line); 
		  System.out.println("RESPONSE..."+response.toString());
		  response.append('\r'); 
		  } 
		  rd.close();
		  }
		  catch (IOException e) {
				System.out.println("Exception in DynamicQRService class.."+e);
				e.printStackTrace();
			}
		  
		  System.out.println("BharatQR CreateQR Response Data..."+response.toString());
		  logger.info("BharatQR CreateQR Response Data..."+response.toString());
		  
		  JSONObject jsonresponse = new JSONObject(response.toString());
		  
		  JSONObject jrespObject =new JSONObject(jsonresponse.get("responseObject").toString());
		  
		  paymentInitiation.setPspTransactionId(jrespObject.getString("trId"));
		  
		  System.out.println("TRID "+jrespObject.getString("trId"));
		  
		  paymentInitiation.setOrderId(jrespObject.getString("txnId"));
		  
		  System.out.println("TXNID "+jrespObject.getString("txnId"));
		  
		  paymentInitiation.setDeviceId(device_id);
		  
		  System.out.println("DeviceID "+paymentInitiation.getDeviceId());
		  
		  paymentInitiation.setAppId(bharatQRRequestModel.getAppId());
		  
		  System.out.println("AppID "+bharatQRRequestModel.getAppId());
		  
		  paymentInitiation.setStatus(jsonresponse.getString("status"));
		  
		  System.out.println("Status "+jsonresponse.getString("status"));
		  
		  paymentInitiation.setStatus(jsonresponse.getString("status"));;
		  
		  paymentInitiation.setPspId(bharatQRRequestModel.getPspId());
		  
		  System.out.println("PSPID "+paymentInitiation.getPspId());
		  
		  paymentInitiation.setTerminalId(bharatQRRequestModel.getPosId());
		  
		  paymentInitiation.setMerchantId("zoneone");
		  
			
			try{
				//Saving Payment Initiation
				qrDao.saveQRRequest(paymentInitiation);
			} catch(Exception e) {
				System.out.println("Exception in Saving QR payment initiation"+e);
			}
		  
		  jrespObject.remove("prgType");
		  
		  JSONObject respodata=new JSONObject();
		  
		  //return response.toString();
		  respodata.put("qrData", jrespObject.getString("qrString"));
		  respodata.put("orderId", jrespObject.getString("txnId"));
//		  respodata.put("trId", jrespObject.getString("trId"));
		  respodata.put("encryptedData", "null");
		  
		  //System.out.println("Response Data..."+jrespObject.toString());
		  System.out.println("BharatQR CreateQR final Response Data..."+respodata.toString());
		  logger.info("BharatQR CreateQR final Response Data..."+respodata.toString());
		  
		  //return jrespObject.toString();
		   return respodata.toString();

		//return urlParameters;
		//return parent.toString();
	}

}
