package com.limitlessmobility.iVendGateway.dao.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONException;
import org.json.JSONObject;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.common.PaymentStatusRequest;
import com.limitlessmobility.iVendGateway.model.common.PaymentStatusResponse;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;
import com.limitlessmobility.iVendGateway.services.common.CommonQRServiceV2;
import com.limitlessmobility.iVendGateway.services.common.PaymentStatusReport;

public class PaymentStatusDaoImpl implements PaymentStatusDao{

	@Override
    public String paymentStatus(PaymentStatusRequest p) {

		JSONObject responseReturn = new JSONObject();
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		Statement statement =null;
		ResultSet result=null;
		
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_transactions where order_id='"+p.getOrderId()+"' or psp_transaction_id='"+p.getOrderId()+"' order by Id desc";
				statement = conn.createStatement();
				result = statement.executeQuery(sql);
				
				if(result.next()){
					responseReturn.put("status", "success");
					double amount = result.getDouble("paid_amount");
//					double amountt = amount;
					if(amount>0){} else{
						amount = result.getDouble("auth_amount"); 
					}
					if(amount<0.01){
						responseReturn.put("status", "failure");
						responseReturn.put("amount", "0");
						return responseReturn.toString();
					}
					responseReturn.put("amount", String.valueOf(amount));
					
					try {statement.close();result.close();}catch(Exception e) {}
					
					return responseReturn.toString();
				} else{
					Statement statementQr = null;
					ResultSet resultQr=null;
					
					String sqlQr = "SELECT * FROM payment_initiation where order_id='"+p.getOrderId()+"'";
					statementQr = conn.createStatement();
					resultQr = statementQr.executeQuery(sqlQr);
					
					if(resultQr.next()){
					
					CommonQRServiceV2 commonQRService = new CommonQRServiceV2();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("posId", resultQr.getString("terminal_id"));
					jsonObj.put("amount", "1");
					jsonObj.put("txnId", p.getOrderId());
					jsonObj.put("appId", resultQr.getString("app_id"));
					jsonObj.put("pspId", resultQr.getString("psp_id"));
					String responsee = commonQRService.commonCheckStatus(jsonObj.toString());
					JSONObject responseJson = new JSONObject(responsee);
					String statuss = responseJson.getString("status");
					/*if(statuss.equalsIgnoreCase("SUCCESS")){
						
						responseReturn.put("status", "success");
						responseReturn.put("amount", result.getString(""));
						return responseReturn.toString();
					} else{*/
//						String sqll = "SELECT * FROM payment_transactions where order_id='"+p.getOrderId()+"'";
						Statement statementt = conn.createStatement();
						ResultSet resultt = statementt.executeQuery(sql);
						
						if(resultt.next()){
							responseReturn.put("status", "success");
							responseReturn.put("amount", resultt.getString("auth_amount"));
							return responseReturn.toString();
						}
//					}
						try {statementQr.close();resultQr.close();}catch(Exception e) {}
					} 
				}
				
			}
			responseReturn.put("status", "failure");
			responseReturn.put("amount", "0");
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
			try {
				responseReturn.put("status", "Something went wrong");
				responseReturn.put("amount", "0");
				return responseReturn.toString();
			} catch(Exception ee){
				
			}
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		
		return responseReturn.toString();
	}

	
}

