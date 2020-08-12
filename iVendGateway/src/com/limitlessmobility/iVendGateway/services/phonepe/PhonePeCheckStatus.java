package com.limitlessmobility.iVendGateway.services.phonepe;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDao;
import com.limitlessmobility.iVendGateway.dao.phonepe.PhonePeDaoImpl;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.phonepe.CheckStatusData;
import com.limitlessmobility.iVendGateway.model.phonepe.PhonePeCheckStatusModel;
import com.limitlessmobility.iVendGateway.model.phonepe.TerminalDetail;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

@Controller
@RequestMapping("/v1/phonepe")
public class PhonePeCheckStatus {
	
	private static final Logger logger = Logger.getLogger(PhonePeCheckStatus.class);
	public static final String VERSION = "v1";
	
	/*public static final String PHONEPEKEY = "8485ce39-393a-4bae-be2b-359c8307fc0f";
	public static final String MERCHANTID = "VENDIMANVILEPARLE";
	public static final String QRSTR="/v3/transaction";*/
	
	DbConfigration dbConfig = new DbConfigrationImpl();
	PhonePeDao pdao=new PhonePeDaoImpl();
	
	@RequestMapping(value = "/checkStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getPhonePeCheckStatus(@RequestBody  PhonePeCheckStatusModel peCheckStatusModel) throws Exception {
		
		
		
		logger.info("posId.." + peCheckStatusModel.getPosId() + "...txnId..." + peCheckStatusModel.getTxnId() + "...appId..." + peCheckStatusModel.getAppId()+"...pspId..."+peCheckStatusModel.getPspId()+"...amount..."+peCheckStatusModel.getAmount());
		System.out.println("posId.." + peCheckStatusModel.getPosId() + "...txnId..." + peCheckStatusModel.getTxnId() + "...appId..." + peCheckStatusModel.getAppId()+"...pspId..."+peCheckStatusModel.getPspId()+"...amount..."+peCheckStatusModel.getAmount());
		
		JSONObject jsonObject=new JSONObject(peCheckStatusModel);
		
		System.out.println("Request Data"+jsonObject.toString());
		
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		PhonePeDao peDao=new PhonePeDaoImpl();
		
		TerminalDetail detail=new TerminalDetail();
		
		
		int amount=0;
		
		String txnId=peCheckStatusModel.getTxnId();
		System.out.println("txnId "+txnId);
		String appId=peCheckStatusModel.getAppId();
		System.out.println("AppId "+appId);
		String terminalId=peCheckStatusModel.getPosId();
		System.out.println("terminalId "+terminalId);
		
		
		detail=peDao.getTerminalDetail(terminalId);
		
		String merchantId=detail.getMerchantId();
		String deviceId=detail.getDeviceId();
		
		try {
			amount=pdao.getAmountByTxnId(txnId);
			
			System.out.println("amount "+amount);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Amount Not Available "+e);
		}
		
		
		
		boolean isConnected = true;
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
		
			if (isConnected) {
				
				System.out.println("Connection Object Created...");
				String query2 = "UPDATE payment_transactions SET app_id='" + appId + "', terminal_id='" + terminalId+"', merchant_id='" + merchantId+"', device_id='" + deviceId+ "' where order_id='" + txnId + "'";

				PreparedStatement preparedStatement2 = con.prepareStatement(query2);
				int i2 = preparedStatement2.executeUpdate();
			}
			
		/*	if (i2 > 0) {
				System.out.println("updated successfully");
				logger.info("table updated successfully");
			}
		*/
		
			/*{
				  "responseObj": {
				    "amount": 5,
				    "statusMessage": "SUCCESS",
				    "status": "SUCCESS",
				    "statusCode": "SS_001"
				  },
				  "message": "null",
				  "status": "SUCCESS"
				}*/
			CheckStatusData checkStatusData1=new CheckStatusData();
			checkStatusData1=pdao.getCheckStatusData(txnId);
			System.out.println("stmsg "+checkStatusData1.getStatusMessage());
			System.out.println("stcod "+checkStatusData1.getStatusCode());
			JSONObject parentJson=new JSONObject();
			JSONObject childJson=new JSONObject();
			
			if (amount == 0) {
				//JSONObject childJson=new JSONObject();
				childJson.put("amount", amount);
				childJson.put("statusMessage", checkStatusData1.getStatusMessage());
				childJson.put("status", "FAILURE");
				childJson.put("statusCode", checkStatusData1.getStatusCode());
				
				
				/*parentJson.put("responseObj", childJson);
				parentJson.put("message", "null");
				parentJson.put("status", "FAILURE");*/
			} else {
				//JSONObject childJson=new JSONObject();
				childJson.put("amount", amount);
				childJson.put("statusMessage", checkStatusData1.getStatusMessage());
				childJson.put("status", "success");
				childJson.put("statusCode", checkStatusData1.getStatusCode());
				
				//JSONObject parentJson=new JSONObject();
				/*parentJson.put("responseObj", childJson);
				parentJson.put("message", "null");
				parentJson.put("status", "SUCCESS");*/
			}
			
			
		
		/*JSONObject  resdata=new JSONObject();
		
		
		resdata.put("amount", amount);
		resdata.put("status", "success");*/

		//return parentJson.toString();
			return childJson.toString();
	}

}
