package com.limitlessmobility.iVendGateway.dao.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.RefundInitialData;
import com.limitlessmobility.iVendGateway.model.bharatqr.Bharatqrrefundrequest;
import com.limitlessmobility.iVendGateway.model.bharatqr.RefundRequestModel;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.model.common.RefundSatusModel;
import com.limitlessmobility.iVendGateway.model.phonepe.Phoneperefundrequest;
import com.limitlessmobility.iVendGateway.model.sodexo.SodexoRefundModel;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaRefundModel;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;
import com.limitlessmobility.iVendGateway.services.amazonpay.AmazonPayRefundServiceV1;
import com.limitlessmobility.iVendGateway.services.amazonpay.AmazonRefundServicevV2;
import com.limitlessmobility.iVendGateway.services.bharatqr.BharatQRRefundService;
import com.limitlessmobility.iVendGateway.services.bharatqr.BharatQRRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.common.CommonQRService;
import com.limitlessmobility.iVendGateway.services.common.CommonQRServiceV2;
import com.limitlessmobility.iVendGateway.services.paytm.PayTmRefundService;
import com.limitlessmobility.iVendGateway.services.paytm.PaytmRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.phonepe.PhonePeRefundService;
import com.limitlessmobility.iVendGateway.services.phonepe.PhonePeRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.sodexo.SodexoRefundService;
import com.limitlessmobility.iVendGateway.services.sodexo.SodexoRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.wallet.WalletRefundService;
import com.limitlessmobility.iVendGateway.services.zeta.ZetaRefundService;
import com.limitlessmobility.iVendGateway.services.zeta.ZetaRefundServiceV2;

public class CommonDaoImpl implements CommonDao{

	@Override
	public void getTerminalForRefund() {
		
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("paytmRefundRequest method is calling!!");
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				System.out.println("timestamp.."+timestamp);
			      
			      try{
			    	  String checkFixedRefundQuery = "SELECT * FROM terminal_event_log WHERE vend_status!='00' and is_barcode_generated='1' and is_payment_done='1' and is_refund='1' and is_refund_Done NOT IN ('1', '2') and vmc_payment_sel_response IS NULL";		
						
						System.out.println("checkFixedRefundQuery.."+checkFixedRefundQuery);
						
						PreparedStatement ps = conn.prepareStatement(checkFixedRefundQuery);
						
//						String sql = "SELECT * FROM terminal_event_log where app_id='"+terminalEvent.getAppId()+"'";
					      java.sql.ResultSet rs = ps.executeQuery(checkFixedRefundQuery);
					      //STEP 5: Extract data from result set
					      while(rs.next()){
					    	  try{
					    	  String appId=rs.getString("app_id");
					    	  String transactionId = rs.getString("server_trn_id");
					    	  String refundAmount = rs.getString("paid_amount");
					    	  boolean saveStatus = saveRefundLog(appId, transactionId, refundAmount, "V2");
					    	  if(saveStatus){
					    		  try {
					    		  Statement stmt = conn.createStatement();
				                  String updateTerminalLogSql = "UPDATE terminal_event_log SET vmc_payment_sel_response='1', is_refund_Done = '1', refund_amount='"+refundAmount+"', pending_refund_amount='"+refundAmount+"' WHERE app_id ='"+appId+"' and server_trn_id='"+transactionId+"'";
				                  stmt.executeUpdate(updateTerminalLogSql);
					    		  } catch(Exception e) {}
					    	  }
						  }catch(Exception e){
							  System.out.println("CatchException.... "+e);
						  }
					      }
			      } catch(Exception e) {
			    	  
			      }
			      
			      try{
				      String checkStatusRefundQuery = "SELECT LG.* FROM  payment_transactions PT INNER JOIN  terminal_event_log LG ON PT.order_id = LG.server_trn_id WHERE LG.is_payment_done = '0' AND LG.payment_selection_date > '2020-04-15 00:00:00'";				
						
				      System.out.println("checkFixedRefundQuery.."+checkStatusRefundQuery);
				      
				      PreparedStatement psStatusRefundQuery = conn.prepareStatement(checkStatusRefundQuery);
						
				      java.sql.ResultSet rsStatusRefundQuery = psStatusRefundQuery.executeQuery();
				      
					      //STEP 5: Extract data from result set
					      while(rsStatusRefundQuery.next()){
					    	  try{
					    	  String appId=rsStatusRefundQuery.getString("app_id");
					    	  String transactionId = rsStatusRefundQuery.getString("server_trn_id");
					    	  String refundAmount = rsStatusRefundQuery.getString("paid_amount");
					    	  String checkTransaction = "SELECT * FROM payment_transactions where order_id='"+transactionId+"'";				
						      PreparedStatement psTransactionQuery = conn.prepareStatement(checkTransaction);
						      java.sql.ResultSet rsTransactionQuery = psTransactionQuery.executeQuery();
						      
							      //STEP 5: Extract data from result set
							      if(rsTransactionQuery.next()){
						    		  try {
							    	  boolean saveStatus = saveRefundLog(appId, transactionId, refundAmount, "V1");
						    		  
							    	  if(saveStatus){
							    		  Statement stmt = conn.createStatement();
						                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_payment_done='1', vmc_payment_sel_response='1', is_refund_Done='1', refund_amount='"+refundAmount+"', pending_refund_amount='"+refundAmount+"' WHERE app_id ='"+appId+"' and server_trn_id='"+transactionId+"'";
						                  int updateCount = stmt.executeUpdate(updateTerminalLogSql);
						                  
						                  if(updateCount<1) {
						                	  Statement stmtt = conn.createStatement();
							                  String updateTerminalLogSqll = "UPDATE terminal_event_log SET is_payment_done='2' WHERE server_trn_id='"+transactionId+"'";
							                  stmtt.executeUpdate(updateTerminalLogSqll);  
							                  try { if (stmtt != null) stmtt.close();} catch (Exception e) {};
						                  }
						                  try { if (stmt != null) stmt.close();} catch (Exception e) {};
							    	  } /*else {
							    		  Statement stmt = conn.createStatement();
						                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_payment_done='1' WHERE server_trn_id='"+transactionId+"'";
						                  stmt.executeUpdate(updateTerminalLogSql);
							    	  }*/
						    		  } catch(Exception e) {}
						    	  }
							      try { if (rsTransactionQuery != null) rsTransactionQuery.close(); } catch (Exception e) {};
					    	  
					      }catch(Exception e){
							  System.out.println("checkFixedRefundQuery..Exception.... "+e);
						  } 
					    	
					      }
				      } catch(Exception e){
				    	  System.out.println("checkFixedRefundQuery Error.. "+e);
				      }
			      try{
//			    	  String staticRefundQuery = "SELECT app_id, paid_amount, price, server_trn_id FROM terminal_event_log WHERE (CAST(paid_amount as DECIMAL(9,2)) > CAST(price as DECIMAL(9,2))) and (CAST(paid_amount as DECIMAL(9,2))>0) and is_refund_Done!='1' and vmc_payment_sel_response IS NULL";
			    	  String staticRefundQuery = "SELECT app_id, paid_amount, price, payment_selection_date, server_trn_id FROM terminal_event_log WHERE (CAST(paid_amount as DECIMAL(9,2)) > CAST(price as DECIMAL(9,2))) and (CAST(paid_amount as DECIMAL(9,2))>0) and is_refund_Done!='1' and price!='' and payment_selection_date > '2019-08-07 00:00:00' and vmc_payment_sel_response IS NULL";
						System.out.println("staticRefundQuery.."+staticRefundQuery);
						
						PreparedStatement ps = conn.prepareStatement(staticRefundQuery);
						
					      java.sql.ResultSet rs = ps.executeQuery(staticRefundQuery);
					      //STEP 5: Extract data from result set
					      while(rs.next()){
					    	  try{
					    	  System.out.println("test..1");
					    	  String appId=rs.getString("app_id");
					    	  System.out.println("test..2");
					    	  /*if(appId.equalsIgnoreCase("88ea3d78-6321-4d61-bf77-5ad794b04204")){
					    		  System.out.println("");
					    		  System.out.println("test..2.1");
					    	  }*/
					    	  String transactionId = rs.getString("server_trn_id");
					    	  /*if(transactionId.equalsIgnoreCase("txn_f25f45e9-5305-4111-b2db-c42ccf51ad43")){
					    		  System.out.println("Have to check.. "+transactionId);
					    	  }*/
					    	  String paidAmount = rs.getString("paid_amount");
					    	  if(Double.parseDouble(paidAmount)<0.001) {
					    		  paidAmount=rs.getString("paid_amount");
					    	  }
					    	  String price = rs.getString("price");
					    	  double refundAmount=0;
					    	  boolean isEmpty = price == null || price.trim().length() == 0;
					    	  double paidAmt = Double.parseDouble(paidAmount);
					    	  if (isEmpty) {
					    	      // handle the validation
					    	  } else {
					    		  double productPrice = Double.parseDouble(price);
						    	  refundAmount = paidAmt-productPrice;
					    	  }
					    	  
					    	  
					    	  /*int productPrice = Integer.parseInt(price);
					    	  int refundAmount = paidAmt-productPrice;*/
//					    	  System.out.println("refundAmount @ saveRefundLog (amount)..."+refundAmount);
					    	  System.out.println("test..3");
					    	  boolean saveStatus = saveRefundLog(appId, transactionId, String.valueOf(refundAmount), "V2");
					    	  System.out.println("test..4");
					    	  if(saveStatus){
					    		  System.out.println("test..4.1");
					    		  Statement stmt = conn.createStatement();
				                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_payment_done='1',vmc_payment_sel_response='1', is_refund_Done = '1', refund_amount='"+String.valueOf(refundAmount)+"', pending_refund_amount='"+String.valueOf(refundAmount)+"' WHERE app_id ='"+appId+"' and server_trn_id='"+transactionId+"'";
				                  
				                  System.out.println("test..4.2");
				                  stmt.executeUpdate(updateTerminalLogSql);
				                  
				                  try { if (stmt != null) stmt.close(); } catch (Exception e) {};
					    	  }
					      }catch(Exception e){
							  System.out.println("checkFixedRefundQuery..Exceptionn.... "+e);
						  } 
					      }
					      try { if (rs != null) rs.close(); } catch (Exception e) {};
			      } catch(Exception e) {
			    	  System.out.println("Pakra gya.."+e);
			      }
			      
			      try{
			    	  String zetaQry = "select * from payment_transactions where psp_id IN('zeta', 'Sodexo') and app_id='' and length(terminal_id) < 6 order by Id Desc";		
						
						System.out.println("zetaQry.. "+zetaQry);
						
						PreparedStatement ps = conn.prepareStatement(zetaQry);
						
//						String sql = "SELECT * FROM terminal_event_log where app_id='"+terminalEvent.getAppId()+"'";
					      java.sql.ResultSet rs = ps.executeQuery(zetaQry);
					      //STEP 5: Extract data from result set
					      while(rs.next()){
					    	  try{
//					    	  String appId=rs.getString("app_id");
					    	  String transactionId = rs.getString("order_id");
					    	  String refundAmount = rs.getString("auth_amount");
					    	  boolean saveStatus = saveRefundLog("", transactionId, refundAmount, "V2");
					    	  if(saveStatus){
					    		  Statement stmt = conn.createStatement();
					    		  System.out.println("Zeta transaction update");
				                  String updateTerminalLogSql = "UPDATE payment_transactions SET app_id = 'done' WHERE order_id ='"+transactionId+"'";
				                  stmt.executeUpdate(updateTerminalLogSql);
				                  
				                  try { if (stmt != null) stmt.close(); } catch (Exception e) {};
					    	  }
						  }catch(Exception e){
							  System.out.println("Exceptionnn.... "+e);
						  }
					      }
					      try { if (rs != null) rs.close(); } catch (Exception e) {};
			      } catch(Exception e) {
			    	  
			      }
			}
			try{
//				commonRefundProcess();
			} catch(Exception e) {
				System.out.println("Error in commonRefundProcess.. "+e);
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			
			dbConfig.closeConnection(conn);
		}
		
	}

	private boolean saveRefundLog(String appId, String transactionId, String refundAmount, String version){
		if(transactionId.equalsIgnoreCase("txn_44881886-310c-489f-a891-9f17bddf31d6")) {
			System.out.println(transactionId);
		}
		boolean response=false;
		DbConfigration dbConfig = new DbConfigrationImpl();
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;
		Connection connnection = dbConfig.getCon();
		if (connnection == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String queryTransaction = "SELECT * FROM payment_transactions WHERE order_id='"+transactionId+"' OR app_id='"+transactionId+"'";		
				System.out.println("queryTransactionONE.. "+queryTransaction);
				PreparedStatement psTransaction = connnection.prepareStatement(queryTransaction);
				java.sql.ResultSet rsTransaction = psTransaction.executeQuery(queryTransaction);
  	  
				if(rsTransaction.next()){
					
					
					
					
					if(rsTransaction.getString("transaction_type").equalsIgnoreCase("2")){
					
					System.out.println("found records for refund else part");
					PaytmRefundRequestModelFinal p = new PaytmRefundRequestModelFinal();
					PaytmRefundRequest refundRequest = new PaytmRefundRequest(); 
					//Generate Random Refund Id
					RandomString randomRefundId = new RandomString();
			        String uniqueRefundId = randomRefundId.generateRandomString(16);
			        System.out.println("RefundID else part..."+uniqueRefundId);
			        
			        System.out.println("else test 1");
			        refundRequest.setRefundRefId(uniqueRefundId);
			        System.out.println("else test 2");
					refundRequest.setAmount(refundAmount);
					System.out.println("else test 3");
					refundRequest.setMerchantOrderId(rsTransaction.getString("order_id"));
					System.out.println("else test 4");
					refundRequest.setTxnGuid(rsTransaction.getString("psp_transaction_id"));
					System.out.println("else test 5");
				try{
					String query = "SELECT * FROM payment_refund WHERE merchant_order_id='"+refundRequest.getTxnGuid()+"' OR txn_guid='"+refundRequest.getTxnGuid()+"'";
					System.out.println("Check Refund Exist Qry "+query);
					PreparedStatement ps = connnection.prepareStatement(query);
					java.sql.ResultSet rs = ps.executeQuery();
				    //STEP 5: Extract data from result set
				    if(rs.next()){
				    	System.out.println("Record is already existing!.. IFFFFFFFFFFFFFFF");
				    } else {
				    	System.out.println("ELSEEEEEEEEEEEEEEEEEEEE");
				    	String queryInsertRefund = "INSERT INTO payment_refund"
							+ "(amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id,"
							+ "platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id,refund_status) VALUES"
							+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					System.out.println("queryInsertRefund... "+queryInsertRefund);
					PreparedStatement preparedStatement = connnection.prepareStatement(queryInsertRefund);
					preparedStatement.setString(1, refundAmount);
					preparedStatement.setString(2, refundRequest.getMerchantOrderId());
					preparedStatement.setString(3, transactionId);
					preparedStatement.setString(4, refundRequest.getTxnGuid());
					preparedStatement.setString(5, "");
					preparedStatement.setString(6, refundRequest.getRefundRefId());
					preparedStatement.setString(7, p.getPlatformName());
					preparedStatement.setString(8, p.getIpAddress());
					preparedStatement.setString(9, p.getOperationType());
					preparedStatement.setString(10, p.getChannel());
					preparedStatement.setString(11, version);
					preparedStatement.setString(12, rsTransaction.getString("psp_id"));
					preparedStatement.setString(13, appId);
					preparedStatement.setString(14, rsTransaction.getString("terminal_id"));
					preparedStatement.setString(15, "1");
					
					int isInsert = preparedStatement.executeUpdate();

					System.out.println("Record is inserted into payment_refund table!");
					if (isInsert > 0) {
						
						System.out.println("Save Refund Payment Transaction Done.");
						response = true;
					} else{
						System.out.println("Save Refund Payment Transaction Failure.");
						response = false;
					}
				      }
				    	  } catch (Exception e) {
					    	  System.out.println("Save Refund Payment Transaction ERROR.: "+e);
					      }
					
					
					
					
					}else{
					
					
					
					
					
					
					System.out.println("found records for refund!!");
					PaytmRefundRequestModelFinal p = new PaytmRefundRequestModelFinal();
					PaytmRefundRequest refundRequest = new PaytmRefundRequest(); 
					//Generate Random Refund Id
					RandomString randomRefundId = new RandomString();
			        String uniqueRefundId = randomRefundId.generateRandomString(16);
			        System.out.println("RefundID..."+uniqueRefundId);
			        
			        refundRequest.setRefundRefId(uniqueRefundId);
					refundRequest.setAmount(refundAmount);
					refundRequest.setMerchantOrderId(rsTransaction.getString("order_id"));
					refundRequest.setTxnGuid(rsTransaction.getString("psp_transaction_id"));
				try{
					String query = "SELECT * FROM payment_refund WHERE merchant_order_id='"+refundRequest.getTxnGuid()+"' OR txn_guid='"+refundRequest.getTxnGuid()+"'";
					System.out.println("payment_refund check queryyyyyyyyyyyyy.. "+query);
					PreparedStatement ps = connnection.prepareStatement(query);
					java.sql.ResultSet rs = ps.executeQuery();
				    //STEP 5: Extract data from result set
				    if(rs.next()){
				    	System.out.println("Record is already existing!.. IFFFFFFFFFFFFFFF");
				    } else {
				    	System.out.println("ELSEEEEEEEEEEEEEEEEEEEE");
				    	String queryInsertRefund = "INSERT INTO payment_refund"
							+ "(amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id,"
							+ "platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id) VALUES"
							+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					System.out.println("queryInsertRefund... "+queryInsertRefund);
					PreparedStatement preparedStatement = connnection.prepareStatement(queryInsertRefund);
					preparedStatement.setString(1, refundAmount);
					preparedStatement.setString(2, refundRequest.getMerchantOrderId());
					preparedStatement.setString(3, transactionId);
					preparedStatement.setString(4, refundRequest.getTxnGuid());
					preparedStatement.setString(5, "");
					preparedStatement.setString(6, refundRequest.getRefundRefId());
					preparedStatement.setString(7, p.getPlatformName());
					preparedStatement.setString(8, p.getIpAddress());
					preparedStatement.setString(9, p.getOperationType());
					preparedStatement.setString(10, p.getChannel());
					preparedStatement.setString(11, p.getVersion());
					preparedStatement.setString(12, rsTransaction.getString("psp_id"));
					preparedStatement.setString(13, appId);
					preparedStatement.setString(14, rsTransaction.getString("terminal_id"));
					
					int isInsert = preparedStatement.executeUpdate();
				    try { if (preparedStatement != null) preparedStatement.close(); } catch (Exception e) {};

					System.out.println("Record is inserted into payment_refund table!");
					if (isInsert > 0) {
						
						System.out.println("Save Refund Payment Transaction Done.");
						response = true;
					} else{
						System.out.println("Save Refund Payment Transaction Failure.");
						response = false;
					}
				      }
				    	  } catch (Exception e) {
					    	  System.out.println("Save Refund Payment Transaction ERROR.: "+e);
					      }
				}
	    	  
  	  }
				try { if (rsTransaction != null) rsTransaction.close(); } catch (Exception e) {};
				
			}
		} catch(Exception e) {
			System.out.println("ERROR IN SAVE REFUND LOG.. "+e);
		} finally {
			try{dbConfig.closeConnection(connnection);}catch(Exception e) {}
		}
		
		return response;
	}
	
	@Override
	public String commonRefundProcess(){
		DbConfigration dbConfig = new DbConfigrationImpl();
//	log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
		
		Phoneperefundrequest phoneperefundrequest=new Phoneperefundrequest();
		Bharatqrrefundrequest bharatqrrefundrequest = new Bharatqrrefundrequest();
		RefundRequestModel refundRequestModelBharatqr = new RefundRequestModel();
		RefundInitialData refundInitialData = new RefundInitialData();   
		if (conn == null) {
			isConnected = false;
		}
		String refundStatus="failure";
		try {
			
			if (isConnected) {
		
				String amountCheckQuery = "SELECT * FROM payment_refund where refund_status=0 and (amount='0' or amount is null or amount='')";				
				System.out.println("amount zero query.. "+amountCheckQuery);
					PreparedStatement pspAmountCheck = conn.prepareStatement(amountCheckQuery);
				      java.sql.ResultSet rsAmountCheck = pspAmountCheck.executeQuery();
				      while (rsAmountCheck.next()) {
				    	  Statement stmt = conn.createStatement();
				    	  String updateAmountQry = "UPDATE payment_refund SET amount = (SELECT auth_amount FROM payment_transactions WHERE order_id='"+rsAmountCheck.getString("merchant_order_id")+"') WHERE merchant_order_id='"+rsAmountCheck.getString("merchant_order_id")+"'";
				    	  stmt.executeUpdate(updateAmountQry);
				    	  try { if (stmt != null) stmt.close(); } catch (Exception e) {};
				      }
				      try { if (rsAmountCheck != null) rsAmountCheck.close(); } catch (Exception e) {};
			
			
			String pspquery = "SELECT DISTINCT psp_id FROM payment_refund WHERE  refund_status=0";				
			System.out.println("Refund queryy.. "+pspquery);
				PreparedStatement pspps = conn.prepareStatement(pspquery);
				String pid="";
			      java.sql.ResultSet psprs = pspps.executeQuery();
			      while (psprs.next()) {
					pid=psprs.getString("psp_id").trim();
					System.out.println("psp_id " +pid);
					
					if (pid.contains("paytm")) {
						System.out.println("paytm refund......................................... " +pid);
						try{ 		  
						String query = "SELECT * FROM payment_refund WHERE psp_id='PayTM' and refund_status=0";				
						System.out.println("Paytm Refund queryy.. "+query);
						PreparedStatement ps = conn.prepareStatement(query);
						
					      java.sql.ResultSet rs = ps.executeQuery();
					      //STEP 5: Extract data from result set
					      while(rs.next()){
					    	  try{
					    	  PaytmRefundRequest paytmRefundRequest = new PaytmRefundRequest();
					    	  paytmRefundRequest=new PaytmRefundRequest();
					    	  paytmRefundRequest.setAmount(rs.getString("amount"));
					    	  paytmRefundRequest.setCurrencyCode("INR");
					    	  paytmRefundRequest.setMerchantOrderId(rs.getString("merchant_order_id"));
					    	  
					    	  RandomString randomString= new RandomString(); 
//					    	  String refundRefId = randomString.generateRandomString(6);
					    	  paytmRefundRequest.setRefundRefId(rs.getString("refund_ref_id"));
					    	  paytmRefundRequest.setTxnGuid(rs.getString("txn_guid"));
					    	  
					    	  paytmRefundRequestModelFinal.setChannel("POS");
					    	  paytmRefundRequestModelFinal.setIpAddress("139.59.73.155");
					    	  paytmRefundRequestModelFinal.setOperationType("REFUND_MONEY");
					    	  paytmRefundRequestModelFinal.setVersion("1.0");
					    	  paytmRefundRequestModelFinal.setPlatformName("PayTM");
					    	  paytmRefundRequestModelFinal.setRequest(paytmRefundRequest);
					    	  
					    	  
//					    	  ObjectMapper mapperObj = new ObjectMapper();
					    	  String jsonStr ="";
					    	  
					          try {
					              // get paytmRefundRequestModelFinal object as a json string
					              jsonStr = paytmRefundRequestModelFinal.toString();
					              System.out.println(jsonStr);
					              PayTmRefundService paytmRefundService = new PayTmRefundService();
					              String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr, rs.getString("terminal_id"));
					              JSONObject refundResponseJson = new JSONObject(refundResponse);
					              System.out.println("refundStatus "+refundStatus);
					              refundStatus = refundResponseJson.getString("status");
					              System.out.println("refundStatus "+refundStatus);
					              
					              try{
					              if(refundStatus.equalsIgnoreCase("SUCCESS")){
					            	  MySqlDate mySqlDate = new MySqlDate();
					            	   Statement stmt = conn.createStatement();
					                  String updateRefundedSql = "UPDATE payment_refund " +
					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundResponseJson.getString("orderId")+"'";
					                  System.out.println("Paytm update payment_refund.."+updateRefundedSql);
					                  stmt.executeUpdate(updateRefundedSql);
					                  try { if (stmt != null) stmt.close(); } catch (Exception e) {};
					                  
					                  Statement stmt1 = conn.createStatement();
					                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+paytmRefundRequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
					                  System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
					                  stmt1.executeUpdate(updateTerminalLogSql);
					                  try { if (stmt1 != null) stmt.close(); } catch (Exception e) {};
					              }
					              }catch(Exception e){
					            	  System.out.println("Error in update.. "+e);
					              }
					          } catch (IOException e) {
					              e.printStackTrace();
					          }
					          
					      }catch(Exception e){
							  System.out.println("Exception.... "+e);
						  }  
					    	  
					      } 
					      try { if (rs != null) rs.close(); } catch (Exception e) {};
					    	  } catch (Exception e) {
						    	  System.out.println("Save Refund Payment Transaction ERROR paytm..: "+e);
						      }
					} else if(pid.contains("phonepe")) {      
						System.out.println("phonepe refund........................................." +pid);
                    try{     
                    String query = "SELECT * FROM payment_refund WHERE psp_id='phonepe' and refund_status=0";    
           System.out.println("Phonepe Refund queryy..  "+query);
        PreparedStatement ps = conn.prepareStatement(query);
        
             java.sql.ResultSet rs = ps.executeQuery();
             //STEP 5: Extract data from result set
             while(rs.next()){
              
              phoneperefundrequest.setTransactionid(rs.getString("merchant_order_id"));
              phoneperefundrequest.setAmount(rs.getLong("amount"));
                              
//              ObjectMapper mapperObj = new ObjectMapper();
              String jsonStr ="";
              
                 try {
                     // get phoneperefundrequest object as a json string
                     jsonStr = phoneperefundrequest.toString();
                     System.out.println(jsonStr);
                     //PayTmRefundService paytmRefundService = new PayTmRefundService();
                     PhonePeRefundService peRefundService=new PhonePeRefundService();
                    // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
                     String pprefundResponse=peRefundService.getPaymentrefund(jsonStr);
                     System.out.println("pprefundResponse "+pprefundResponse);
                     JSONObject refundResponseJson = new JSONObject(pprefundResponse);
                     JSONObject statusresponse=refundResponseJson.getJSONObject("data");
                     System.out.println("statusresponse "+statusresponse);
                     System.out.println("refundResponseJson "+statusresponse.getString("status"));
                     refundStatus = statusresponse.getString("status").trim();
                     System.out.println("..........PHONEPE REFUND STATUS........."+ refundStatus);
                     System.out.println("old trasanction Id "+phoneperefundrequest.getTransactionid());
                     try{
                     if(refundStatus.equalsIgnoreCase("SUCCESS")){
                      System.out.println("if........");
                      MySqlDate mySqlDate = new MySqlDate();
                       Statement stmt = conn.createStatement();
                         String updateRefundedSql = "UPDATE payment_refund " +
                                      "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+statusresponse.getString("transactionId")+"' WHERE merchant_order_id ='"+phoneperefundrequest.getTransactionid()+"'";
                         System.out.println("phonepe update payment_refund.."+updateRefundedSql);
                         stmt.executeUpdate(updateRefundedSql);
                         Statement stmt1 = conn.createStatement();
                         String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+phoneperefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
                         System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
                         stmt1.executeUpdate(updateTerminalLogSql);
                     }
                     }catch(Exception e){
		            	  System.out.println("Error in update.. "+e);
		              }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 
                 
              
             } 
              } catch (Exception e) {
               System.out.println("Save PhonePe Refund Payment Transaction ERROR.: "+e);
              }
					}else if(pid.contains("zeta")) {
						System.out.println("zeta refund........................................." +pid);
                        try{ 		  
                        String query = "SELECT * FROM payment_refund WHERE psp_id='zeta' and refund_status=0";				
            			System.out.println("Zeta Refund qry "+query);
        				PreparedStatement ps = conn.prepareStatement(query);
        				ZetaRefundModel zetaRefundModel = new ZetaRefundModel();
        			      java.sql.ResultSet rs = ps.executeQuery();
        			      //STEP 5: Extract data from result set
        			      while(rs.next()){
        			    	  
        			    	  System.out.println("Zeta Refund Processing!!");
        			    	  
        			    	  zetaRefundModel.setTransactionId(rs.getString("merchant_order_id"));
        			    	  zetaRefundModel.setAmount(rs.getString("amount"));
        			    	        			    	  
//        			    	  String jsonStr ="";
        			    	  
        			          try {
        			              // get paytmRefundRequestModelFinal object as a json string
//        			              jsonStr = mapperObj.writeValueAsString(zetaRefundModel);
//        			              System.out.println(jsonStr);
        			              //PayTmRefundService paytmRefundService = new PayTmRefundService();
        			              ZetaRefundService zetaRefundService = new ZetaRefundService();
        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
        			              String zetaRefundResponse=zetaRefundService.getZetaRefund(zetaRefundModel);
        			              System.out.println("zetaRefundResponse "+zetaRefundResponse);
        			              JSONObject refundResponseJson = new JSONObject(zetaRefundResponse);
//        			              System.out.println("refundResponseJson "+refundResponseJson);
        			              refundStatus = refundResponseJson.getString("status").trim();
        			              System.out.println("..........ZETA REFUND STATUS........."+ refundStatus);
        			              try{
        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
					            	  MySqlDate mySqlDate = new MySqlDate();
					            	   Statement stmt = conn.createStatement();
					                  String updateRefundedSql = "UPDATE payment_refund " +
					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+zetaRefundModel.getTransactionId()+"'";
					                  System.out.println("zeta update payment_refund.."+updateRefundedSql);
					                  stmt.executeUpdate(updateRefundedSql);
					                  
					                  Statement stmt1 = conn.createStatement();
					                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+zetaRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+zetaRefundModel.getTransactionId()+"'";
					                  System.out.println("zeta update terminal_event_log.."+updateTerminalLogSql);
				                  stmt1.executeUpdate(updateTerminalLogSql);
					              }
        			              }catch(Exception e){
					            	  System.out.println("Error in update.. "+e);
					              }
        			          } catch (Exception e) {
        			              e.printStackTrace();
        			          }
        			          
        			          
        			    	  
        			      } 
        			    	  } catch (Exception e) {
        				    	  System.out.println("Save Refund Payment Transaction ERROR zeta.: "+e);
        				      }
					}
					else if (pid.contains("bharatqr")) {
						System.out.println("bharatqr refund........................................." +pid);
                        try{ 		  
                        String query = "SELECT * FROM payment_refund WHERE psp_id='bharatqr' and refund_status=0";				
            			System.out.println("Bharatqr refund qry "+query);
        				PreparedStatement ps = conn.prepareStatement(query);
        				
        			      java.sql.ResultSet rs = ps.executeQuery();
        			      //STEP 5: Extract data from result set
        			      while(rs.next()){
        			    	  try{
        			    	  bharatqrrefundrequest.setTransactionid(rs.getString("merchant_order_id"));
        			    	  bharatqrrefundrequest.setAmount(rs.getString("amount"));
        			    	        			    	  
        			    	  String jsonStr ="";
        			    	  
        			          try {
        			              // Get bharatqrrefundrequest object as a json string
        			             // jsonStr = mapperObj.writeValueAsString(bharatqrrefundrequest);
        			              refundRequestModelBharatqr.setRrn(bharatqrrefundrequest.getTransactionid());
        			              refundRequestModelBharatqr.setRefundAmount(bharatqrrefundrequest.getAmount());
        			              System.out.println(jsonStr);
        			              BharatQRRefundService bharatQRRefundService  =new BharatQRRefundService();
        			              String bqrrefundResponse=bharatQRRefundService.getPaymentrefund(refundRequestModelBharatqr);
        			              System.out.println("bqrrefundResponse "+bqrrefundResponse);
        			              JSONObject refundResponseJson = new JSONObject(bqrrefundResponse);
        			              JSONObject internaljson=refundResponseJson.getJSONObject("responseObject");
        			              refundStatus = refundResponseJson.getString("status").trim();
        			              System.out.println("..........BHARATQR REFUND STATUS........."+ refundStatus);
        			              System.out.println("internaljson "+internaljson);
        			              System.out.println(" trasanction Id "+bharatqrrefundrequest.getTransactionid());
        			              try{
        			              if(refundStatus.equalsIgnoreCase("Success")){
        			            	  System.out.println("if........");
        			            	  MySqlDate mySqlDate = new MySqlDate();
        			            	  Statement stmt = conn.createStatement();
        			                  String updateRefundedSql = "UPDATE payment_refund " +
        			                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+internaljson.getString("rrn")+"' WHERE merchant_order_id ='"+bharatqrrefundrequest.getTransactionid()+"'";
        			                  System.out.println("bharatqr update payment_refund.."+updateRefundedSql);
        			                  stmt.executeUpdate(updateRefundedSql);
        			                  
        			                  Statement stmt1 = conn.createStatement();
        			                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+bharatqrrefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+bharatqrrefundrequest.getTransactionid()+"'";
        			                  System.out.println("bharatqr update terminal_event_log.."+updateTerminalLogSql);
        			                  stmt1.executeUpdate(updateTerminalLogSql);
        			              }
        			              }catch(Exception e){
					            	  System.out.println("Error in update.. "+e);
					              }
        			          } catch (Exception e) {
        			              e.printStackTrace();
        			          }
        			          
        			      }catch(Exception e){
							  System.out.println("Exception.... "+e);
						  } 
        			    	  
        			      } 
        			    	  } catch (Exception e) {
        				    	  System.out.println("Save BharatQR Refund Payment Transaction ERROR bharatqr.: "+e);
        				      }
					}else if(pid.contains("Sodexo")) {
						System.out.println("Sodexo refund........................................." +pid);
                        try{ 		  
                        String query = "SELECT * FROM payment_refund WHERE psp_id='Sodexo' and refund_status=0";				
            			System.out.println("Sodexo Refund qry "+query);
        				PreparedStatement ps = conn.prepareStatement(query);
        				SodexoRefundModel sodexoRefundModel	 = new SodexoRefundModel();
        			      java.sql.ResultSet rs = ps.executeQuery();
        			      //STEP 5: Extract data from result set
        			      while(rs.next()){
        			    	  try{
        			    	  System.out.println("Sodexo Refund Processing!!");
        			    	  
        			    	  sodexoRefundModel.setTransactionId(rs.getString("merchant_order_id"));
        			    	  sodexoRefundModel.setAmount(rs.getString("amount"));
        			    	        			    	  
//        			    	  String jsonStr ="";
        			    	  
        			          try {
        			              SodexoRefundService sodexoRefundService = new SodexoRefundService();
        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
        			              String sodexoRefundResponse=sodexoRefundService.getSodexoRefund(sodexoRefundModel);
        			              System.out.println("sodexoRefundResponse "+sodexoRefundResponse);
        			              JSONObject refundResponseJson = new JSONObject(sodexoRefundResponse);
//        			              System.out.println("refundResponseJson "+refundResponseJson);
        			              refundStatus = refundResponseJson.getString("status").trim();
        			              System.out.println("..........SODEXO REFUND STATUS........."+ refundStatus);
        			              try{
        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
					            	  MySqlDate mySqlDate = new MySqlDate();
					            	   Statement stmt = conn.createStatement();
					                  String updateRefundedSql = "UPDATE payment_refund " +
					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+sodexoRefundModel.getTransactionId()+"'";
					                  stmt.executeUpdate(updateRefundedSql);
					                  
					                  Statement stmt1 = conn.createStatement();
					                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+sodexoRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+sodexoRefundModel.getTransactionId()+"'";
					                  stmt1.executeUpdate(updateTerminalLogSql);
					              }
        			              }catch(Exception e){
					            	  System.out.println("Error in update.. "+e);
					              }
        			          } catch (Exception e) {
        			              e.printStackTrace();
        			          }
        			          
        			      }catch(Exception e){
							  System.out.println("Exception.... "+e);
						  }  
        			    	  
        			      } 
        			    	  } catch (Exception e) {
        				    	  System.out.println("Save Refund Payment Transaction ERROR sodexo.: "+e);
        				      }
					}
					
					//
					
					else if(pid.contains("amazonpay")){

						System.out.println("Amazonpay refund version 1 ......................................... " +pid);
						try{ 
						String query = "SELECT * FROM payment_refund WHERE psp_id='amazonpay' and refund_status=0"; 
						System.out.println("queryyyyyyyyyyyyyyyyyyyy v1 .. "+query);
						PreparedStatement ps = conn.prepareStatement(query);

						java.sql.ResultSet rs = ps.executeQuery();
						//STEP 5: Extract data from result set
						while(rs.next()){

						refundInitialData.setTxnId(rs.getString("merchant_order_id"));
						refundInitialData.setAmount(rs.getString("amount"));

						//ObjectMapper mapperObj = new ObjectMapper();
						String jsonStr ="";


						try {
						// get refundInitialData object as a json string
						// jsonStr = mapperObj.writeValueAsString(refundInitialData);
						AmazonPayRefundServiceV1 apRefundService=new AmazonPayRefundServiceV1();
						// String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
						System.out.println("amznpy refund orderid v1 "+refundInitialData.getTxnId());
						System.out.println("amznpy refund request v1 = "+refundInitialData);
						String aprefundResponse=apRefundService.getPaymentRefund(refundInitialData);
						System.out.println("aprefundResponse v1 "+aprefundResponse);


						/*JSONObject jsonObject1=new JSONObject(aprefundResponse);
						System.out.println("jsonObject1 "+jsonObject1.toString());
						JSONArray array=jsonObject1.getJSONArray("details");
						System.out.println("array "+array.toString());
						//JSONObject jo=array.getJSONObject(0);
						JSONObject refstatatat=array.getJSONObject(0);
						String refundReferenceId=refstatatat.getString("refundReferenceId");
						System.out.println("refstatus...... " +refundReferenceId);
						JSONObject refundStatusdata=array.getJSONObject(0).getJSONObject("refundStatus");
						System.out.println("refundStatus...... " +refundStatusdata.toString());
						refundStatus=refundStatusdata.getString("state").trim();
						System.out.println("status "+refundStatus.trim());
						String amazonRefundId=refstatatat.getString("amazonRefundId");
						System.out.println("amazonRefundId = "+amazonRefundId);
						System.out.println("..........AMAZONPAY REFUND STATUS........."+ refundStatus);
						System.out.println("old trasanction Id "+refundInitialData.getTxnId());*/
						
						JSONObject apresponse=new JSONObject(aprefundResponse);
						System.out.println("Amazonpay Response :- "+apresponse.toString());
						JSONObject innerresponse=apresponse.getJSONObject("response");
						String refundReferenceId=innerresponse.getString("refundReferenceId");
						System.out.println("refundReferenceId...... " +refundReferenceId);
//						System.out.println("refundStatus...... " +refundStatusdata.toString());
						refundStatus=innerresponse.getString("status").trim();
						System.out.println("status "+refundStatus);
						String amazonRefundId=innerresponse.getString("amazonRefundId");
						System.out.println("amazonRefundId = "+amazonRefundId);
						System.out.println("..........AMAZONPAY REFUND STATUS........."+ refundStatus);
						System.out.println("old trasanction Id "+refundInitialData.getTxnId());
						
						try{
						if(refundStatus.equalsIgnoreCase("Pending")){
						System.out.println("if........");
						MySqlDate mySqlDate = new MySqlDate();
						Statement stmt = conn.createStatement();
						String updateRefundedSql = "UPDATE payment_refund " +
						"SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+amazonRefundId+"' WHERE merchant_order_id ='"+refundInitialData.getTxnId()+"'";
						System.out.println("AmazonPay update payment_refund.."+updateRefundedSql);
						stmt.executeUpdate(updateRefundedSql);
						Statement stmt1 = conn.createStatement();
						String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+refundInitialData.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundReferenceId+"'";
						System.out.println("AmazonPay update terminal_event_log.."+updateTerminalLogSql);
						stmt1.executeUpdate(updateTerminalLogSql);
						}
						}catch(Exception e){
						System.out.println("Error in update.. "+e);
						}
						} catch (JSONException je) {
						je.printStackTrace();
						}



						} 
						} catch (Exception e) {
						System.out.println("Save AmazonPay Refund Payment Transaction ERROR.: "+e);
						}
					}
					else {
						System.out.println("wallet refund........................................." +pid);
                        try{ 		  
                        String query = "SELECT * FROM payment_refund WHERE psp_id='"+pid+"' and refund_status=0";				
            			System.out.println("Wallet Refund Qry "+query);
        				PreparedStatement ps = conn.prepareStatement(query);
        			      java.sql.ResultSet rs = ps.executeQuery();
        			      //STEP 5: Extract data from result set
        			      while(rs.next()){
        			    	  try{
        			    		  WalletRefundService walletRefundService = new WalletRefundService();
        			    		  ManualRefundRequest refundRequest = new ManualRefundRequest();
        			    		  refundRequest.setAmount(Double.parseDouble(rs.getString("amount")));
        			    		  refundRequest.setPosId(rs.getString("terminal_id"));
        			    		  refundRequest.setPspId(rs.getString("psp_id"));
        			    		  refundRequest.setPspTransactionId(rs.getString("txn_guid"));
        			    		  refundRequest.setTransactionId(rs.getString("merchant_order_id"));
        			    		  try{
        			    			String response = walletRefundService.walletManualRefund(refundRequest);
        			    			JSONObject responseJson = new JSONObject(response);
        			    			String walletRefundStatus = responseJson.getString("status");
        			    			if(walletRefundStatus.equalsIgnoreCase("SUCCESS")){
        			    				MySqlDate mySqlDate = new MySqlDate();
 					            	   	Statement stmt = conn.createStatement();
 					            	   	String updateRefundedSql = "UPDATE payment_refund " +
 					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundRequest.getTransactionId()+"'";
 					            	   	stmt.executeUpdate(updateRefundedSql);
 					            	   try { if (stmt != null) stmt.close(); } catch (Exception e) {};
        			    			}
        			    		  } catch(Exception e){
        			    			  System.out.println("CommonDaoImpl line no 802 wallet Refund.. "+e);
        			    		  }
        			    	  }catch(Exception e){
							  System.out.println("Exception.... "+e);
						  }  
        			    	  
        			      } 
        			      try { if (rs != null) rs.close(); } catch (Exception e) {};
        			    	  } catch (Exception e) {
        				    	  System.out.println("Save Refund Payment Transaction ERROR sodexo.: "+e);
        				      }
					}//end
				}
			}
		}catch(Exception e) {
			System.out.println("eeeeeeeeeee.. "+e);
		} finally {
			dbConfig.closeConnection(conn);
		}
		return refundStatus;
	}

	@Override
	public PspMerchant getPspMerchant(String pspid, String terminalId) {

		PspMerchant pspMerchant = new PspMerchant();
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection connectionn = dbConfig.getCon();
		if (connectionn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM psp_merchant where psp_id='"+pspid+"' and psp_terminal_id='"+terminalId+"'";
				Statement statement = connectionn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					pspMerchant.setPspMerchantId(result.getString("psp_merchant_id"));
					pspMerchant.setPspMerchantKey(result.getString("psp_merchant_key"));
					pspMerchant.setPspMguid(result.getString("psp_mguid"));
				}
				try { if (result != null) result.close(); } catch (Exception e) {};
				try { if (statement != null) statement.close(); } catch (Exception e) {};
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			try{dbConfig.closeConnection(connectionn);}catch(Exception e) {}
		}

		return pspMerchant;
	}

	@Override
	public String getDeviceId(String terminalId) {

		String deviceId="null";
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection connn = dbConfig.getCon();
		if (connn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT device_id FROM terminal where terminal_id='"+terminalId+"'";
				Statement statement = connn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					deviceId = result.getString(1);
				}
				try { if (result != null) result.close(); } catch (Exception e) {};
				try { if (statement != null) statement.close(); } catch (Exception e) {};
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			try{dbConfig.closeConnection(connn);}catch(Exception e) {}
		}

		return deviceId;
	}

	@Override
	public String getMid(String deviceId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		String machineId = null;

		Connection conection = dbConfig.getCon();
		System.out.println("con..."+conection);
		if (conection == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
				
				Statement stmt = conection.createStatement();

				String query = "SELECT t.machine_id FROM devices t where t.device_id='"+ deviceId +"'";
				System.out.println("query.." + query);

				//Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {

					machineId = rs.getString(1);

				}
				try { if (rs != null) rs.close(); } catch (Exception e) {};
				try { if (stmt != null) stmt.close(); } catch (Exception e) {};
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{dbConfig.closeConnection(conection);}catch(Exception e) {}
		}

		return machineId;
	}

	@Override
	public String getPspMerchantKey(String pspid, String terminalId) {

		String merchantKey="null";
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT psp_merchant_key FROM psp_merchant where psp_id='"+pspid+"' and psp_terminal_id='"+terminalId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					merchantKey = result.getString(1);
				}
				try { if (result != null) result.close(); } catch (Exception e) {};
				try { if (statement != null) statement.close(); } catch (Exception e) {};
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			try{dbConfig.closeConnection(conn);}catch(Exception e) {}
		}

		return merchantKey;
	}

	@Override
	public String getBankCode(String pspId) {

		String bankCode="null";
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT psp_bankcode FROM psp_details where psp_id='"+pspId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					bankCode = result.getString(1);
				}
				try { if (result != null) result.close(); } catch (Exception e) {};
				try { if (statement != null) statement.close(); } catch (Exception e) {};
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			try{dbConfig.closeConnection(conn);}catch(Exception e) {}
		}

		return bankCode;
	}

	@Override
	public void autoCheckStatusForRefund() {
//		CommonQRService commonService = new CommonQRService();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("paytmRefundRequest method is calling!!");
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				java.util.Date date = new java.util.Date();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
				System.out.println("timestamp.."+timestamp);
			      
			      try{
			    	  String checkStatusRefundQuery = "SELECT * FROM terminal_event_log AS t WHERE vend_status!='00' and is_barcode_generated='1' AND payment_selection_date > '2019-08-08 00:00:00' and is_refund_Done NOT IN('1','2')";		
			    	  
			    	  //For testing only
//			    	  String checkFixedRefundQuery = "SELECT * FROM terminal_event_log WHERE server_trn_id='rJxCA2qVizez4bga'";
						
			    	  
			    	  System.out.println("checkStatusRefundQuery ..."+checkStatusRefundQuery);
						
						PreparedStatement ps = conn.prepareStatement(checkStatusRefundQuery);
						
//						String sql = "SELECT * FROM terminal_event_log where app_id='"+terminalEvent.getAppId()+"'";
					      java.sql.ResultSet rs = ps.executeQuery(checkStatusRefundQuery);
					      //STEP 5: Extract data from result set
					      while(rs.next()){
					    	  String transactionId = rs.getString("server_trn_id");
					    	  if(transactionId.equalsIgnoreCase("WFWfNvzs8SbHvrUE")){
					    		  System.out.println("trnid.. "+transactionId);
					    	  }
					    	  try{
					    	  String sqlQr = "SELECT * FROM payment_initiation where order_id='"+transactionId+"'";
								Statement statementQr = conn.createStatement();
								ResultSet resultQr = statementQr.executeQuery(sqlQr);
								
								if(resultQr.next()){
								
								CommonQRService commonQRService = new CommonQRService();
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("posId", resultQr.getString("terminal_id"));
								jsonObj.put("amount", resultQr.getString("auth_amount"));
								jsonObj.put("txnId", transactionId);
								jsonObj.put("appId", resultQr.getString("app_id"));
								jsonObj.put("pspId", resultQr.getString("psp_id"));
								
								commonQRService.commonCheckStatus(jsonObj.toString());
								CommonQRServiceV2 commonQRServiceV2 = new CommonQRServiceV2();
								commonQRServiceV2.commonCheckStatus(jsonObj.toString());
								}
								try { if (resultQr != null) resultQr.close(); } catch (Exception e) {};
								} catch(Exception e){
									System.out.println("refund check status excetion... "+e);
								}
					    	  String checkExistTransaction = "SELECT * FROM payment_transactions WHERE order_id='"+transactionId+"'";		
								
								System.out.println("checkExistTransactionQuery.."+checkExistTransaction);
								
								PreparedStatement psCheckExist = conn.prepareStatement(checkExistTransaction);
								java.sql.ResultSet rsCheckExist = psCheckExist.executeQuery(checkExistTransaction);
					    	  if(rsCheckExist.next()){
					    	  
					    	  String appId=rs.getString("app_id");
					    	  
					    	  String amountTxn = rsCheckExist.getString("auth_amount");
//					    	  if(CommonService.isExistOrder(transactionId)) {
//					    		  String amountTxn = CommonService.getAmount(transactionId);
					    		  boolean saveStatus = saveRefundLog(appId, transactionId, amountTxn, "V2");
						    	  if(saveStatus){
						    		  Statement stmt = conn.createStatement();
					                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+amountTxn+"', pending_refund_amount='0' WHERE app_id ='"+appId+"' and server_trn_id='"+transactionId+"'";
					                  stmt.executeUpdate(updateTerminalLogSql);
						    	  }
//					    	  }
					    	  
					      }
					    	  try { if (rsCheckExist != null) rsCheckExist.close(); } catch (Exception e) {};
					    	    try { if (psCheckExist != null) psCheckExist.close(); } catch (Exception e) {};
					    	  try{
						    	  Statement stmt = conn.createStatement();
				                  String updateTerminalLogSql = "UPDATE terminal_event_log SET vmc_payment_sel_response = '1', is_refund_Done='2' WHERE app_id ='"+rs.getString("app_id")+"' and server_trn_id='"+rs.getString("server_trn_id")+"' and is_refund_Done!='1'";
				                  stmt.executeUpdate(updateTerminalLogSql);
				                  try { if (stmt != null) stmt.close(); } catch (Exception e) {};
					    	  } catch(Exception e){
					    		  
					    	  }
					      
					      }
			      } catch(Exception e) {
			    	System.out.println(e);  
			      }
			}
			try{
//				commonRefundProcess();
			} catch(Exception e) {
				System.out.println("Error in commonRefundProcess.. "+e);
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			
			dbConfig.closeConnection(conn);
		}
		
	}
	
	@Override
	public String manualRefund(ManualRefundRequest refundRequest) {
		DbConfigration dbConfig = new DbConfigrationImpl();
//		log.info("Form is going to Save Transaction Details");
			boolean isConnected = true;
			Connection conn = dbConfig.getCon();
			/*PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
			
			Phoneperefundrequest phoneperefundrequest=new Phoneperefundrequest();
			Bharatqrrefundrequest bharatqrrefundrequest = new Bharatqrrefundrequest();
			RefundRequestModel refundRequestModelBharatqr = new RefundRequestModel();*/
			if (conn == null) {
				isConnected = false;
			}
//			String refundStatus="failure";
			JSONObject finalResponse=new JSONObject();
			try {
				
				if (isConnected) {
					JSONObject refundStatusJson = new JSONObject();
					
//					PaymentTransaction paymentTransaction = getPspTxnId(reData.getMerchant_order_id());
					
					String isExistQuery = "SELECT * FROM payment_refund WHERE merchant_order_id='"+refundRequest.getTransactionId()+"'";
					PreparedStatement ps1 = conn.prepareStatement(isExistQuery);
				      java.sql.ResultSet resultSet = ps1.executeQuery();
				      //STEP 5: Extract data from result set
				      if(resultSet.next()){
				    	  System.out.println("Record already exist in Payment_Transaction Table...and it's OrderId is "+refundRequest.getTransactionId());
				      } else {
					
				    	  String queryTransaction = "SELECT * FROM payment_transactions WHERE order_id='"+refundRequest.getTransactionId()+"' OR psp_transaction_id='"+refundRequest.getTransactionId()+"' OR device_transaction_id='"+refundRequest.getTransactionId()+refundRequest.getPosId()+"'";		
							System.out.println("queryTransactionTWO .. "+queryTransaction);
							PreparedStatement psTransaction = conn.prepareStatement(queryTransaction);
							java.sql.ResultSet rsTransaction = psTransaction.executeQuery(queryTransaction);
							if(rsTransaction.next()){
								

						    	System.out.println("ELSEEEEEEEEEEEEEEEEEEEE");
						    	String queryInsertRefund = "INSERT INTO payment_refund"
									+ "(amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id,"
									+ "platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id,refund_status, wallet_account_id, wallet_account_key) VALUES"
									+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							
							System.out.println("queryInsertRefund... "+queryInsertRefund);
							PreparedStatement preparedStatement = conn.prepareStatement(queryInsertRefund);
							String amount = Double.toString(refundRequest.getAmount());
							if(amount == null || amount.isEmpty() || amount=="0"){
								amount = rsTransaction.getString("paid_amount");
								if(amount.equalsIgnoreCase("0.0")){
									amount=rsTransaction.getString("auth_amount");
								}
							}
							preparedStatement.setString(1, amount);
							
							if(refundRequest.getPspId().equalsIgnoreCase("bharatqr")){
								preparedStatement.setString(2, rsTransaction.getString("order_id"));
								preparedStatement.setString(3, rsTransaction.getString("order_id"));
							} else{
								preparedStatement.setString(2, refundRequest.getTransactionId());
								preparedStatement.setString(3, refundRequest.getTransactionId());
							}
							preparedStatement.setString(4, rsTransaction.getString("psp_transaction_id"));
							preparedStatement.setString(5, "");
							
							RandomString randomRefundId = new RandomString();
					        String uniqueRefundId = randomRefundId.generateRandomString(16);
							preparedStatement.setString(6, uniqueRefundId);
							preparedStatement.setString(7, "");
							preparedStatement.setString(8, "");
							preparedStatement.setString(9, "");
							preparedStatement.setString(10, "");
							preparedStatement.setString(11, "V2");
							preparedStatement.setString(12, rsTransaction.getString("psp_id"));
							preparedStatement.setString(13, refundRequest.getPosId());
							preparedStatement.setString(14, refundRequest.getPosId());
							preparedStatement.setString(15, "0");
							preparedStatement.setString(16, "");
							preparedStatement.setString(17, "");
							
							int isInsert = preparedStatement.executeUpdate();

							System.out.println("Record is inserted into payment_refund table!");
							if (isInsert > 0) {} else{
								System.out.println("Save Refund Payment Transaction Failure.");
								finalResponse.put("responseObj", refundStatusJson);
		            			finalResponse.put("message", refundStatusJson.getString("message"));
		            			finalResponse.put("status", refundStatusJson.getString("status"));
							
							return finalResponse.toString();
							}
						      
								}else{
							finalResponse.put("responseObj", "");
	            			finalResponse.put("message", "Failure");
	            			finalResponse.put("status", "Failure");
						
						return finalResponse.toString();}
				}
				    /*To do Manual Refund*/  
				}
			}catch(Exception e) {
				System.out.println("eeeeeeeeeee.. "+e);
			} finally {
				try{dbConfig.closeConnection(conn);}catch(Exception e) {}
			}
			return finalResponse.toString();
		}
	
	@Override
	public String manualRefundProcess(ManualRefundRequest refundRequest) {
		DbConfigration dbConfig = new DbConfigrationImpl();
//		log.info("Form is going to Save Transaction Details");
			boolean isConnected = true;
			Connection conn = dbConfig.getCon();
			if (conn == null) {
				isConnected = false;
			}
			String refundStatus="failure";
			try {
				
				if (isConnected) {
					
					String checkTransaction = "SELECT * FROM payment_refund where merchant_order_id='"+refundRequest.getTransactionId()+"'";				
				    PreparedStatement psTransactionQuery = conn.prepareStatement(checkTransaction);
				    java.sql.ResultSet rsTransactionQuery = psTransactionQuery.executeQuery();
					      //STEP 5: Extract data from result set
					      if(rsTransactionQuery.next()){
					    	  return "exist";
					      } else{

				    		  boolean saveStatus = saveRefundLog("", refundRequest.getTransactionId(), String.valueOf(refundRequest.getAmount()), refundRequest.getVersion());
					    	  /*if(saveStatus){
//					    		  Statement stmt = conn.createStatement();
//				                  String updateTerminalLogSql = "UPDATE terminal_event_log SET refund_amount='"+refundRequest.getAmount()+"', pending_refund_amount='"+refundRequest.getAmount()+"' WHERE server_trn_id='"+refundRequest.getTransactionId()+"'";
//				                  stmt.executeUpdate(updateTerminalLogSql);
					    	  }*/
					    	  if (saveStatus==true){
									return "success";
								} else if (saveStatus==false){
									return "failure";
								}
					      }
				}
				}catch(Exception e) {
				System.out.println("eeeeeeeeeee.. "+e);
			}
			return refundStatus;
		}

	@Override
    public String commonRefundProcessV2() {
		DbConfigration dbConfig = new DbConfigrationImpl();
//		log.info("Form is going to Save Transaction Details");
			boolean isConnected = true;
			Connection conn = dbConfig.getCon();
			PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
			
			Phoneperefundrequest phoneperefundrequest=new Phoneperefundrequest();
			Bharatqrrefundrequest bharatqrrefundrequest = new Bharatqrrefundrequest();
			RefundRequestModel refundRequestModelBharatqr = new RefundRequestModel();
			RefundInitialData refundInitialData = new RefundInitialData();   
			if (conn == null) {
				isConnected = false;
			}
			String refundStatus="failure";
			try {
				
				if (isConnected) {
//					String amountCheckQuery = "SELECT * FROM payment_refund where refund_status=0 and (amount='0' or amount is null or amount='')";
					String amountCheckQuery = "SELECT * FROM payment_refund where refund_status=0 and (amount<1 or amount is null or amount='')";
					System.out.println("amount zero null qry "+amountCheckQuery);
						PreparedStatement pspAmountCheck = conn.prepareStatement(amountCheckQuery);
					      java.sql.ResultSet rsAmountCheck = pspAmountCheck.executeQuery();
					      while (rsAmountCheck.next()) {
					    	 String qryAmt = "SELECT auth_amount FROM payment_transactions WHERE order_id='"+rsAmountCheck.getString("merchant_order_id")+"'";
					    	 PreparedStatement txnAmountCheck = conn.prepareStatement(qryAmt); 
					    	 java.sql.ResultSet rsTxnAmountCheck = txnAmountCheck.executeQuery();
						      if (rsTxnAmountCheck.next()) {
						    	  
						      
					    	 
					    	  String updateAmountQry = "UPDATE payment_refund SET amount = '"+rsTxnAmountCheck.getString("auth_amount")+"' WHERE merchant_order_id='"+rsAmountCheck.getString("merchant_order_id")+"'";
					    	  PreparedStatement stmt = conn.prepareStatement(updateAmountQry);
//					    	  int updateCount = stmt.executeUpdate(updateAmountQry);
					    	  stmt.executeUpdate(updateAmountQry);
						      } else{
						    	  String qryy = "SELECT id FROM payment_refund ORDER BY id DESC LIMIT 0, 1";
						    	  PreparedStatement txn = conn.prepareStatement(qryy); 
							    	 java.sql.ResultSet rss = txn.executeQuery();
								      if (rss.next()) {
								    	  
								     int idd = rss.getInt("id"); 
								     int updatedId = idd-100;
					    		  Statement stmtt = conn.createStatement();
						    	  String updateAmountQryy = "DELETE FROM payment_refund WHERE merchant_order_id='"+rsAmountCheck.getString("merchant_order_id")+"' and id < '"+updatedId+"'";
						    	  stmtt.executeUpdate(updateAmountQryy);
								      }
					    	  }
					      }
					      
					List<RefundSatusModel> listStatus = new ArrayList<RefundSatusModel>();
				
				String pspquery = "SELECT DISTINCT psp_id FROM payment_refund WHERE  refund_status=0";				
				System.out.println("Refund Status zero qry "+pspquery);
					PreparedStatement pspps = conn.prepareStatement(pspquery);
					String pid="";
				      java.sql.ResultSet psprs = pspps.executeQuery();
				      while (psprs.next()) {
						pid=psprs.getString("psp_id").trim();
						System.out.println("psp_id " +pid);
						
						
						
						
	                	try{  
						if (pid.contains("paytm")) {
							
							System.out.println("paytm refund......................................... " +pid);
							try{ 		  
							String query = "SELECT * FROM payment_refund WHERE psp_id='PayTM' and refund_status=0";				
							System.out.println("Paytm v2 refund qry "+query);
							PreparedStatement ps = conn.prepareStatement(query);
							
						      java.sql.ResultSet rs = ps.executeQuery();
						      //STEP 5: Extract data from result set
						      while(rs.next()){
						    	  RefundSatusModel refundStatuss = new RefundSatusModel();
						    	  try{
						    		  try{
					                		refundStatuss.setAppId(rs.getString("app_id"));
					                	} catch(Exception e){
					                		refundStatuss.setAppId("");
					                	}
					                	try{
					                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
					                		System.out.println("paytm refund FOR......................................... " +rs.getString("merchant_order_id"));
					                	} catch(Exception e){
					                		refundStatuss.setAppId("");
					                	}
						    		 
						    	  PaytmRefundRequest paytmRefundRequest = new PaytmRefundRequest();
						    	  paytmRefundRequest=new PaytmRefundRequest();
						    	  paytmRefundRequest.setAmount(rs.getString("amount"));
						    	  paytmRefundRequest.setCurrencyCode("INR");
						    	  paytmRefundRequest.setMerchantOrderId(rs.getString("merchant_order_id"));
						    	  
//						    	  RandomString randomString= new RandomString(); 
//						    	  String refundRefId = randomString.generateRandomString(6);
						    	  paytmRefundRequest.setRefundRefId(rs.getString("refund_ref_id"));
						    	  paytmRefundRequest.setTxnGuid(rs.getString("txn_guid"));
						    	  
						    	  paytmRefundRequestModelFinal.setChannel("POS");
						    	  paytmRefundRequestModelFinal.setIpAddress("139.59.73.155");
						    	  paytmRefundRequestModelFinal.setOperationType("REFUND_MONEY");
						    	  paytmRefundRequestModelFinal.setVersion("1.0");
						    	  paytmRefundRequestModelFinal.setPlatformName("PayTM");
						    	  paytmRefundRequestModelFinal.setRequest(paytmRefundRequest);
						    	  
						    	  
//						    	  ObjectMapper mapperObj = new ObjectMapper();
						    	  String jsonStr ="";
						    	  
						          try {
						              // get paytmRefundRequestModelFinal object as a json string
						              jsonStr = paytmRefundRequestModelFinal.toString();
						              System.out.println(jsonStr);
						              PaytmRefundServiceV2 paytmRefundService = new PaytmRefundServiceV2();
						              String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr, rs.getString("terminal_id"));
						              JSONObject refundResponseJson = new JSONObject(refundResponse);
						              System.out.println("refundStatus "+refundStatus);
						              refundStatus = refundResponseJson.getString("status");
						              System.out.println("refundStatus "+refundStatus);
						              
						              try{
						              if(refundStatus.equalsIgnoreCase("SUCCESS")){
						            	  refundStatuss.setStatusId(2);
						            	  listStatus.add(refundStatuss);//refund status list
						            	  MySqlDate mySqlDate = new MySqlDate();
						            	   Statement stmt = conn.createStatement();
						                  String updateRefundedSql = "UPDATE payment_refund " +
						                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundResponseJson.getString("orderId")+"'";
						                  System.out.println("Paytm update payment_refund.."+updateRefundedSql);
						                  stmt.executeUpdate(updateRefundedSql);
						                  
						                  
						                  
						                  Statement stmt1 = conn.createStatement();
						                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+paytmRefundRequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
						                  System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
						                  stmt1.executeUpdate(updateTerminalLogSql);
						              } else {
						            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						              }
						              }catch(Exception e){
						            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						            	  System.out.println("Error in update.. "+e);
						              }
						          } catch (IOException e) {
						        	  refundStatuss.setStatusId(0);
					            	  listStatus.add(refundStatuss);//refund status list
						              e.printStackTrace();
						          }
						          
						      }catch(Exception e){
								  System.out.println("Exception.... "+e);
							  }  
						    	  
						      } 
						    	  } catch (Exception e) {
							    	  System.out.println("Save Refund Payment Transaction ERROR paytm..: "+e);
							      }
						} else if(pid.contains("phonepe")) { 
							System.out.println("phonepe refund........................................." +pid);
	                    try{     
	                    String query = "SELECT * FROM payment_refund WHERE psp_id='phonepe' and refund_status=0";    
	           System.out.println("Phonepe v2 qry "+query);
	        PreparedStatement ps = conn.prepareStatement(query);
	        
	             java.sql.ResultSet rs = ps.executeQuery();
	             //STEP 5: Extract data from result set
	             while(rs.next()){
	            	 RefundSatusModel refundStatuss = new RefundSatusModel();
	            	 try{
	                		refundStatuss.setAppId(rs.getString("app_id"));
	                	} catch(Exception e){
	                		refundStatuss.setAppId("");
	                	}
	                	try{
	                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
	                	} catch(Exception e){
	                		refundStatuss.setAppId("");
	                	}
	              phoneperefundrequest.setTransactionid(rs.getString("merchant_order_id"));
	              phoneperefundrequest.setAmount(rs.getLong("amount"));
	                              
//	              ObjectMapper mapperObj = new ObjectMapper();
	              String jsonStr ="";
	              
	                 try {
	                     // get phoneperefundrequest object as a json string
	                     jsonStr = phoneperefundrequest.toString();
	                     System.out.println(jsonStr);
	                     //PayTmRefundService paytmRefundService = new PayTmRefundService();
	                     PhonePeRefundServiceV2 peRefundService=new PhonePeRefundServiceV2();
	                    // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
	                     String pprefundResponse=peRefundService.getPaymentrefund(jsonStr);
	                     System.out.println("pprefundResponse "+pprefundResponse);
	                     JSONObject refundResponseJson = new JSONObject(pprefundResponse);
	                     JSONObject statusresponse=refundResponseJson.getJSONObject("data");
	                     System.out.println("statusresponse "+statusresponse);
	                     System.out.println("refundResponseJson "+statusresponse.getString("status"));
	                     refundStatus = statusresponse.getString("status").trim();
	                     System.out.println("..........PHONEPE REFUND STATUS........."+ refundStatus);
	                     System.out.println("old trasanction Id "+phoneperefundrequest.getTransactionid());
	                     try{
	                     if(refundStatus.equalsIgnoreCase("SUCCESS")){
	                      System.out.println("if........");
	                      MySqlDate mySqlDate = new MySqlDate();
	                       Statement stmt = conn.createStatement();
	                         String updateRefundedSql = "UPDATE payment_refund " +
	                                      "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+statusresponse.getString("transactionId")+"' WHERE merchant_order_id ='"+phoneperefundrequest.getTransactionid()+"'";
	                         System.out.println("phonepe update payment_refund.."+updateRefundedSql);
	                         stmt.executeUpdate(updateRefundedSql);
	                         refundStatuss.setStatusId(2);
			            	  listStatus.add(refundStatuss);//refund status list
	                         Statement stmt1 = conn.createStatement();
	                         String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+phoneperefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
	                         System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
	                         stmt1.executeUpdate(updateTerminalLogSql);
	                     } else {
			            	  refundStatuss.setStatusId(0);
			            	  listStatus.add(refundStatuss);//refund status list
			              }
	                     }catch(Exception e){
	                    	 refundStatuss.setStatusId(0);
			            	  listStatus.add(refundStatuss);//refund status list
			            	  System.out.println("Error in update.. "+e);
			              }
	                 } catch (Exception e) {
	                	 refundStatuss.setStatusId(0);
		            	  listStatus.add(refundStatuss);//refund status list
	                     e.printStackTrace();
	                 }
	                 
	                 
	              
	             } 
	              } catch (Exception e) {
	               System.out.println("Save PhonePe Refund Payment Transaction ERROR.: "+e);
	              }
						}else if(pid.contains("zeta")) {
							System.out.println("zeta refund........................................." +pid);
	                        try{ 		  
	                        String query = "SELECT * FROM payment_refund WHERE psp_id='zeta' and refund_status=0";				
	            			System.out.println("Zeta v2 qry "+query);
	        				PreparedStatement ps = conn.prepareStatement(query);
	        				ZetaRefundModel zetaRefundModel = new ZetaRefundModel();
	        			      java.sql.ResultSet rs = ps.executeQuery();
	        			      //STEP 5: Extract data from result set
	        			      while(rs.next()){
	        			    	  RefundSatusModel refundStatuss = new RefundSatusModel();
	        			    	  
	        			    	  try{
				                		refundStatuss.setAppId(rs.getString("app_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
				                	try{
				                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
	        			    	  System.out.println("Zeta Refund Processing!!");
	        			    	  
	        			    	  zetaRefundModel.setTransactionId(rs.getString("merchant_order_id"));
	        			    	  zetaRefundModel.setAmount(rs.getString("amount"));
	        			    	        			    	  
//	        			    	  String jsonStr ="";
	        			    	  
	        			          try {
	        			              // get paytmRefundRequestModelFinal object as a json string
//	        			              jsonStr = mapperObj.writeValueAsString(zetaRefundModel);
//	        			              System.out.println(jsonStr);
	        			              //PayTmRefundService paytmRefundService = new PayTmRefundService();
	        			              ZetaRefundServiceV2 zetaRefundService = new ZetaRefundServiceV2();
	        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
	        			              String zetaRefundResponse=zetaRefundService.getZetaRefund(zetaRefundModel);
	        			              System.out.println("zetaRefundResponse "+zetaRefundResponse);
	        			              JSONObject refundResponseJson = new JSONObject(zetaRefundResponse);
//	        			              System.out.println("refundResponseJson "+refundResponseJson);
	        			              refundStatus = refundResponseJson.getString("status").trim();
	        			              System.out.println("..........ZETA REFUND STATUS........."+ refundStatus);
	        			              try{
	        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
						            	  MySqlDate mySqlDate = new MySqlDate();
						            	   Statement stmt = conn.createStatement();
						                  String updateRefundedSql = "UPDATE payment_refund " +
						                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+zetaRefundModel.getTransactionId()+"'";
						                  System.out.println("zeta update payment_refund.."+updateRefundedSql);
						                  stmt.executeUpdate(updateRefundedSql);
						                  refundStatuss.setStatusId(2);
						                  listStatus.add(refundStatuss);//refund status list
						                  Statement stmt1 = conn.createStatement();
						                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+zetaRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+zetaRefundModel.getTransactionId()+"'";
						                  System.out.println("zeta update terminal_event_log.."+updateTerminalLogSql);
					                  stmt1.executeUpdate(updateTerminalLogSql);
						              } else {
						            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						              }
	        			              }catch(Exception e){
	        			            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						            	  System.out.println("Error in update.. "+e);
						              }
	        			          } catch (Exception e) {
	        			        	  refundStatuss.setStatusId(0);
					            	  listStatus.add(refundStatuss);//refund status list
	        			              e.printStackTrace();
	        			          }
	        			          
	        			          
	        			    	  
	        			      } 
	        			    	  } catch (Exception e) {
	        				    	  System.out.println("Save Refund Payment Transaction ERROR zeta.: "+e);
	        				      }
						}
						else if (pid.contains("bharatqr")) {
							System.out.println("bharatqr refund........................................." +pid);
	                        try{ 		  
	                        String query = "SELECT * FROM payment_refund WHERE psp_id='bharatqr' and refund_status=0";				
	            			System.out.println("Bharatqr v2 qry "+query);
	        				PreparedStatement ps = conn.prepareStatement(query);
	        				
	        			      java.sql.ResultSet rs = ps.executeQuery();
	        			      //STEP 5: Extract data from result set
	        			      while(rs.next()){
	        			    	  RefundSatusModel refundStatuss = new RefundSatusModel();
	        			    	  try{
				                		refundStatuss.setAppId(rs.getString("app_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
				                	try{
				                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
	        			    	  try{
	        			    	  bharatqrrefundrequest.setTransactionid(rs.getString("merchant_order_id"));
	        			    	  bharatqrrefundrequest.setAmount(rs.getString("amount"));
	        			    	        			    	  
	        			    	  String jsonStr ="";
	        			    	  
	        			          try {
	        			              // Get bharatqrrefundrequest object as a json string
	        			             // jsonStr = mapperObj.writeValueAsString(bharatqrrefundrequest);
	        			              refundRequestModelBharatqr.setRrn(bharatqrrefundrequest.getTransactionid());
	        			              refundRequestModelBharatqr.setRefundAmount(bharatqrrefundrequest.getAmount());
	        			              System.out.println(jsonStr);
	        			              BharatQRRefundServiceV2 bharatQRRefundService  =new BharatQRRefundServiceV2();
	        			              String bqrrefundResponse=bharatQRRefundService.getPaymentrefund(refundRequestModelBharatqr);
	        			              System.out.println("bqrrefundResponse "+bqrrefundResponse);
	        			              JSONObject refundResponseJson = new JSONObject(bqrrefundResponse);
	        			              JSONObject internaljson=refundResponseJson.getJSONObject("responseObject");
	        			              refundStatus = refundResponseJson.getString("status").trim();
	        			              System.out.println("..........BHARATQR REFUND STATUS........."+ refundStatus);
	        			              System.out.println("internaljson "+internaljson);
	        			              System.out.println(" trasanction Id "+bharatqrrefundrequest.getTransactionid());
	        			              try{
	        			              if(refundStatus.equalsIgnoreCase("Success")){
	        			            	  System.out.println("if........");
	        			            	  MySqlDate mySqlDate = new MySqlDate();
	        			            	  Statement stmt = conn.createStatement();
	        			                  String updateRefundedSql = "UPDATE payment_refund " +
	        			                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+internaljson.getString("rrn")+"' WHERE merchant_order_id ='"+bharatqrrefundrequest.getTransactionid()+"'";
	        			                  System.out.println("bharatqr update payment_refund.."+updateRefundedSql);
	        			                  stmt.executeUpdate(updateRefundedSql);
	        			                  refundStatuss.setStatusId(2);
	        			                  listStatus.add(refundStatuss);//refund status list
	        			                  Statement stmt1 = conn.createStatement();
	        			                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+bharatqrrefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+bharatqrrefundrequest.getTransactionid()+"'";
	        			                  System.out.println("bharatqr update terminal_event_log.."+updateTerminalLogSql);
	        			                  stmt1.executeUpdate(updateTerminalLogSql);
	        			              } else {
						            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						              }
	        			              }catch(Exception e){
	        			            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						            	  System.out.println("Error in update.. "+e);
						              }
	        			          } catch (Exception e) {
	        			              e.printStackTrace();
	        			          }
	        			          
	        			      }catch(Exception e){
								  System.out.println("Exception.... "+e);
							  } 
	        			    	  
	        			      } 
	        			    	  } catch (Exception e) {
	        				    	  System.out.println("Save BharatQR Refund Payment Transaction ERROR bharatqr.: "+e);
	        				      }
						}else if(pid.contains("Sodexo")) {
							System.out.println("Sodexo refund........................................." +pid);
							
	                        try{ 		  
	                        String query = "SELECT * FROM payment_refund WHERE psp_id='Sodexo' and refund_status=0";				
	            			System.out.println("Sodexo v2 qry "+query);
	        				PreparedStatement ps = conn.prepareStatement(query);
	        				SodexoRefundModel sodexoRefundModel	 = new SodexoRefundModel();
	        			      java.sql.ResultSet rs = ps.executeQuery();
	        			      //STEP 5: Extract data from result set
	        			      while(rs.next()){
	        			    	  String orderId = CommonService.getOrderIdByTxnId(rs.getString("merchant_order_id"));
	        			    	  if(orderId.equalsIgnoreCase("0")) {
	        			    		  orderId = rs.getString("merchant_order_id");
	        			    	  }
	        			    	  RefundSatusModel refundStatuss = new RefundSatusModel();
	        			    	  try{
				                		refundStatuss.setAppId(rs.getString("app_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
				                	try{
				                		refundStatuss.setPaymentRefNo(orderId);
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
	        			    	  try{
	        			    	  System.out.println("Sodexo Refund Processing!!");
	        			    	  
	        			    	  sodexoRefundModel.setTransactionId(rs.getString("merchant_order_id"));
	        			    	  sodexoRefundModel.setAmount(rs.getString("amount"));
	        			    	        			    	  
//	        			    	  String jsonStr ="";
	        			    	  
	        			          try {
	        			              SodexoRefundService sodexoRefundService = new SodexoRefundService();
	        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
	        			              String sodexoRefundResponse=sodexoRefundService.getSodexoRefund(sodexoRefundModel);
	        			              System.out.println("sodexoRefundResponse "+sodexoRefundResponse);
	        			              JSONObject refundResponseJson = new JSONObject(sodexoRefundResponse);
//	        			              System.out.println("refundResponseJson "+refundResponseJson);
	        			              refundStatus = refundResponseJson.getString("status").trim();
	        			              System.out.println("..........SODEXO REFUND STATUS........."+ refundStatus);
	        			              try{
	        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
						            	  MySqlDate mySqlDate = new MySqlDate();
						            	   Statement stmt = conn.createStatement();
						                  String updateRefundedSql = "UPDATE payment_refund " +
						                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+sodexoRefundModel.getTransactionId()+"'";
						                  stmt.executeUpdate(updateRefundedSql);
						                  refundStatuss.setStatusId(2);
						                  listStatus.add(refundStatuss);//refund status list
						                  Statement stmt1 = conn.createStatement();
						                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+sodexoRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+sodexoRefundModel.getTransactionId()+"'";
						                  stmt1.executeUpdate(updateTerminalLogSql);
						              } else if(!refundStatus.equalsIgnoreCase("SUCCESS")){
						            	  /*
						            	   * For Version 2
						            	   */
						            	  SodexoRefundServiceV2 sodexoRefundServiceV2 = new SodexoRefundServiceV2();
			        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
			        			              String sodexoRefundResponseV2=sodexoRefundServiceV2.getSodexoRefund(sodexoRefundModel);
			        			              System.out.println("sodexoRefundResponse "+sodexoRefundResponseV2);
			        			              JSONObject refundResponseJsonV2 = new JSONObject(sodexoRefundResponseV2);
//			        			              System.out.println("refundResponseJson "+refundResponseJson);
			        			              refundStatus = refundResponseJsonV2.getString("status").trim();
			        			              System.out.println("..........SODEXO REFUND STATUS........."+ refundStatus);
			        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
								            	  MySqlDate mySqlDate = new MySqlDate();
								            	   Statement stmt = conn.createStatement();
								                  String updateRefundedSql = "UPDATE payment_refund " +
								                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+sodexoRefundModel.getTransactionId()+"'";
								                  stmt.executeUpdate(updateRefundedSql);
								                  refundStatuss.setStatusId(2);
								                  listStatus.add(refundStatuss);//refund status list
								                  Statement stmt1 = conn.createStatement();
								                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+sodexoRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+sodexoRefundModel.getTransactionId()+"'";
								                  stmt1.executeUpdate(updateTerminalLogSql);
								              }
						              } else {
						            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						              }
	        			              }catch(Exception e){
	        			            	  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
						            	  System.out.println("Error in Sodexo refund.. "+e);
						              }
	        			          } catch (Exception e) {
	        			        	  refundStatuss.setStatusId(0);
					            	  listStatus.add(refundStatuss);//refund status list
	        			              e.printStackTrace();
	        			          }
	        			          
	        			      }catch(Exception e){
								  System.out.println("Exception.... "+e);
							  }  
	        			    	  
	        			      } 
	        			    	  } catch (Exception e) {
	        				    	  System.out.println("Save Refund Payment Transaction ERROR sodexo.: "+e);
	        				      }
						}
						
						//
						
						else if(pid.contains("amazonpay")){
							System.out.println("Amazonpay refund........................................." +pid);
							try{ 
							String query = "SELECT * FROM payment_refund WHERE psp_id='amazonpay' and refund_status=0"; 
							System.out.println("amazonpay v2 qry "+query);
							PreparedStatement ps = conn.prepareStatement(query);

							java.sql.ResultSet rs = ps.executeQuery();
							//STEP 5: Extract data from result set
							while(rs.next()){
								RefundSatusModel refundStatuss = new RefundSatusModel();
								try{
			                		refundStatuss.setAppId(rs.getString("app_id"));
			                	} catch(Exception e){
			                		refundStatuss.setAppId("");
			                	}
			                	try{
			                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
			                	} catch(Exception e){
			                		refundStatuss.setAppId("");
			                	}
							refundInitialData.setTxnId(rs.getString("merchant_order_id"));
							refundInitialData.setAmount(rs.getString("amount"));

							//ObjectMapper mapperObj = new ObjectMapper();
							String jsonStr ="";


							try {
							// get refundInitialData object as a json string
							// jsonStr = mapperObj.writeValueAsString(refundInitialData);
								AmazonRefundServicevV2 apRefundService=new AmazonRefundServicevV2();
							// String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
							String aprefundResponse=apRefundService.getPaymentRefund(refundInitialData);
							
							
							JSONObject apresponse=new JSONObject(aprefundResponse);
							System.out.println("Amazonpay Response :- "+apresponse.toString());
							JSONObject innerresponse=apresponse.getJSONObject("response");
							String refundReferenceId=innerresponse.getString("refundReferenceId");
							System.out.println("refundReferenceId...... " +refundReferenceId);
							refundStatus=innerresponse.getString("status").trim();
							System.out.println("status "+refundStatus);
							String amazonRefundId=innerresponse.getString("amazonRefundId");
							System.out.println("amazonRefundId = "+amazonRefundId);
							System.out.println("..........AMAZONPAY REFUND STATUS........."+ refundStatus);
							System.out.println("old trasanction Id "+refundInitialData.getTxnId());
							try{
							if(refundStatus.equalsIgnoreCase("Pending") || refundStatus.equalsIgnoreCase("SUCCESS")){
							System.out.println("if........");
							MySqlDate mySqlDate = new MySqlDate();
							Statement stmt = conn.createStatement();
							String updateRefundedSql = "UPDATE payment_refund " +
							"SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"',txn_guid='"+amazonRefundId+"' WHERE merchant_order_id ='"+refundInitialData.getTxnId()+"'";
							System.out.println("AmazonPay update payment_refund.."+updateRefundedSql);
							stmt.executeUpdate(updateRefundedSql);
							refundStatuss.setStatusId(2);
							listStatus.add(refundStatuss);//refund status list
							Statement stmt1 = conn.createStatement();
							String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+refundInitialData.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundReferenceId+"'";
							System.out.println("AmazonPay update terminal_event_log.."+updateTerminalLogSql);
							stmt1.executeUpdate(updateTerminalLogSql);
							} else {
				            	  refundStatuss.setStatusId(0);
				            	  listStatus.add(refundStatuss);//refund status list
				              }
							}catch(Exception e){
								refundStatuss.setStatusId(0);
				            	  listStatus.add(refundStatuss);//refund status list
							System.out.println("Error in update.. "+e);
							}
							} catch (JSONException je) {
							je.printStackTrace();
							}



							} 
							} catch (Exception e) {
							System.out.println("Save AmazonPay Refund Payment Transaction ERROR.: "+e);
							}
						}
						else {
							
							
							System.out.println("wallet refund........................................." +pid);
	                        try{ 		  
	                        String query = "SELECT * FROM payment_refund WHERE psp_id='"+pid+"' and refund_status=0";				
	            			System.out.println("wallet v2 qry "+query);
	        				PreparedStatement ps = conn.prepareStatement(query);
	        			      java.sql.ResultSet rs = ps.executeQuery();
	        			      //STEP 5: Extract data from result set
	        			      while(rs.next()){
	        			    	  RefundSatusModel refundStatuss = new RefundSatusModel();
	        			    	  try{
				                		refundStatuss.setAppId(rs.getString("app_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
				                	try{
				                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
				                	} catch(Exception e){
				                		refundStatuss.setAppId("");
				                	}
	        			    	  try{
	        			    		  WalletRefundService walletRefundService = new WalletRefundService();
	        			    		  ManualRefundRequest refundRequest = new ManualRefundRequest();
	        			    		  refundRequest.setAmount(Double.parseDouble(rs.getString("amount")));
	        			    		  refundRequest.setPosId(rs.getString("terminal_id"));
	        			    		  refundRequest.setPspId(rs.getString("psp_id"));
	        			    		  refundRequest.setPspTransactionId(rs.getString("txn_guid"));
	        			    		  refundRequest.setTransactionId(rs.getString("merchant_order_id"));
	        			    		  
	        			    		  try{
	        			    			String response = walletRefundService.walletManualRefund(refundRequest);
	        			    			JSONObject responseJson = new JSONObject(response);
	        			    			String walletRefundStatus = responseJson.getString("status");
	        			    			String walletRefundMessage = responseJson.getString("message");
	        			    			if(walletRefundStatus.equalsIgnoreCase("SUCCESS")){
	        			    				MySqlDate mySqlDate = new MySqlDate();
	 					            	   	Statement stmt = conn.createStatement();
	 					            	   	String updateRefundedSql = "UPDATE payment_refund " +
	 					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundRequest.getTransactionId()+"'";
	 					            	   	stmt.executeUpdate(updateRefundedSql);
	 					            	   refundStatuss.setStatusId(2);
	 					            	  listStatus.add(refundStatuss);//refund status list
	        			    			} else if(walletRefundMessage.equalsIgnoreCase("Refund is already processed")){
	        			    				refundStatuss.setStatusId(2);
		 					            	listStatus.add(refundStatuss);//refund status list
	        			    				MySqlDate mySqlDate = new MySqlDate();
	 					            	   	Statement stmt = conn.createStatement();
	 					            	   	String updateRefundedSql = "UPDATE payment_refund " +
	 					                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundRequest.getTransactionId()+"'";
	 					            	   	stmt.executeUpdate(updateRefundedSql);
	 					            	   
	        			    			} else {
							            	  refundStatuss.setStatusId(0);
							            	  listStatus.add(refundStatuss);//refund status list
							              }
	        			    		  } catch(Exception e){
	        			    			  refundStatuss.setStatusId(0);
						            	  listStatus.add(refundStatuss);//refund status list
	        			    			  System.out.println("CommonDaoImpl line no 802 wallet Refund.. "+e);
	        			    		  }
	        			    	  }catch(Exception e){
								  System.out.println("Exception.... "+e);
							  }  
	        			    	  
	        			      } 
	        			    	  } catch (Exception e) {
	        				    	  System.out.println("Save Refund Payment Transaction ERROR sodexo.: "+e);
	        				      }
						}//end
	                	} catch(Exception e){
	                		System.out.println("Refund ERROR.. "+e);
	                	}
						System.out.println("Trying to send refund Status");
						
	                	  
						
					}
				      System.out.println("listStatus.. "+listStatus);
				      CommonService.sendRefundStatus(listStatus);
				}
				
			}catch(Exception e) {
				System.out.println("eeeeeeeeeee.. "+e);
			} finally {
				dbConfig.closeConnection(conn);
			}
			return refundStatus;
		}

	@Override
    public void directRefund() {

		System.out.println("zeta special efund calling");
		DbConfigration dbConfig = new DbConfigrationImpl();

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_transactions where length(terminal_id)<4 and app_id!='done' and terminal_id != '' and psp_id IN('zeta','Sodexo') and terminal_id IS NOT NULL";
				System.out.println(sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				
				if(result.next()){
					ZetaRefundModel zetaRefundModel = new ZetaRefundModel();
					String amount = result.getString("auth_amount")+".00";
					zetaRefundModel.setAmount(amount);
					zetaRefundModel.setTransactionId(result.getString("order_id"));
					ZetaRefundService z = new ZetaRefundService();
					String refundResponse = z.getZetaRefund(zetaRefundModel);
					System.out.println("refundResponse.. "+refundResponse);
					JSONObject responseJson = new JSONObject();
					String refundStatus = responseJson.getString("status");
					
					List<RefundSatusModel> listStatus = new ArrayList<RefundSatusModel>();
					RefundSatusModel refundStatuss = new RefundSatusModel();
					if (refundStatus.equalsIgnoreCase("success")){
					
					refundStatuss.setAppId("NA");
					refundStatuss.setPaymentRefNo(result.getString("order_id"));
					refundStatuss.setStatusId(2);
						
					String sqll = "SELECT * FROM payment_refund WHERE merchant_order_id='"+zetaRefundModel.getTransactionId()+"'";
					System.out.println("Exist query.. "+sqll);
					Statement stmt = conn.createStatement();
					ResultSet resultt = stmt.executeQuery(sqll);
					if(resultt.next()){
						String sqlUpdate = "UPDATE payment_refund " +
				                   "SET refund_status = '1' WHERE merchant_order_id='"+zetaRefundModel.getTransactionId()+"'";
						System.out.println("Update Query "+sqlUpdate);
						stmt.executeUpdate(sqlUpdate);
				} else{ 
		    	String queryInsertRefund = "INSERT INTO payment_refund"
					+ "(amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id,"
					+ "platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id,refund_status) VALUES"
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			System.out.println("queryInsertRefund... "+queryInsertRefund);
			PreparedStatement ps = conn.prepareStatement(queryInsertRefund);
			ps.setString(1, zetaRefundModel.getAmount());
			ps.setString(2, zetaRefundModel.getTransactionId());
			ps.setString(3, zetaRefundModel.getTransactionId());
			ps.setString(4, "");
			ps.setString(5, "");
			RandomString randomString= new RandomString(); 
	    	  String refundRefId = randomString.generateRandomString(6);
			ps.setString(6, refundRefId);
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setString(9, "");
			ps.setString(10, "");
			ps.setString(11, "");
			ps.setString(12, "");
			ps.setString(13, "");
			ps.setString(14, result.getString("terminal_id"));
			ps.setString(15, "1");
			ps.executeUpdate();
			
			String updateTxnSql = "UPDATE payment_transactions SET app_id = 'done' WHERE merchant_order_id='"+zetaRefundModel.getTransactionId()+"'";
            stmt.executeUpdate(updateTxnSql);
            
            CommonService.sendRefundStatus(listStatus);
				}
					
				
			} else {
				refundStatuss.setAppId("NA");
				refundStatuss.setPaymentRefNo(result.getString("order_id"));
          	  refundStatuss.setStatusId(0);
          	  listStatus.add(refundStatuss);//refund status list
            }
			}
			}
			} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
		
			dbConfig.closeConnection(conn);
		}
		
	}

}