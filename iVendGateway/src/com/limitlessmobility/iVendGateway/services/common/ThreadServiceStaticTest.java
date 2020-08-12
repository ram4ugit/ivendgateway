package com.limitlessmobility.iVendGateway.services.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.RefundInitialData;
import com.limitlessmobility.iVendGateway.model.bharatqr.Bharatqrrefundrequest;
import com.limitlessmobility.iVendGateway.model.bharatqr.RefundRequestModel;
import com.limitlessmobility.iVendGateway.model.phonepe.Phoneperefundrequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmRefundService;
import com.limitlessmobility.iVendGateway.services.paytm.PaytmRefundServiceV2;

@Controller
public class ThreadServiceStaticTest {

	@RequestMapping(value = "/staticRefundAll", method = RequestMethod.POST)
	@ResponseBody
	public String staticRefundAll() throws Exception {
		
		PayTmRefundService paytmRefundService = new PayTmRefundService();
		
		DbConfigration dbConfig = new DbConfigrationImpl();
			boolean isConnected = true;
			Connection conn = dbConfig.getCon();
			PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
			
			if (conn == null) {
				isConnected = false;
			}
			String refundStatus="failure";
			try {
				
				if (isConnected) {
					String amountCheckQuery = "SELECT * FROM payment_transactions order by id desc";				
					System.out.println("amount zero null qry "+amountCheckQuery);
						PreparedStatement pspAmountCheck = conn.prepareStatement(amountCheckQuery);
					      java.sql.ResultSet rsAmountCheck = pspAmountCheck.executeQuery();
					      while (rsAmountCheck.next()) {
					    	  try {
					    	  String request = "{\"request\":{\"amount\":\""+rsAmountCheck.getString("auth_amount")+"\",\"merchantOrderId\":\""+rsAmountCheck.getString("order_id")+"\",\"merchantGuid\":\"\",\"txnGuid\":\""+rsAmountCheck.getString("psp_transaction_id")+"\",\"currencyCode\":\"INR\",\"refundRefId\":\"\"},\"platformName\":\"\",\"ipAddress\":\"139.59.73.155\",\"operationType\":\"\",\"channel\":\"POS\",\"version\":\"\"}";
					    	  String response = paytmRefundService.payTmStaticRefundProcess(request);
					    	  System.out.println(response);
					    	  } catch (Exception e) {
					    		  System.out.println(e);
					    	  }
					      }
				}
			} catch(Exception e) {
				
			}
			return "done";
	}
}
