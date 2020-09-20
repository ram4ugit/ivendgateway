package com.limitlessmobility.iVendGateway.dao.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobil.ivendgateway.util.RandomString;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.amazonpay.RefundInitialData;
import com.limitlessmobility.iVendGateway.model.bharatqr.Bharatqrrefundrequest;
import com.limitlessmobility.iVendGateway.model.bharatqr.RefundRequestModel;
import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.model.common.RefundData;
import com.limitlessmobility.iVendGateway.model.common.RefundSatusModel;
import com.limitlessmobility.iVendGateway.model.phonepe.Phoneperefundrequest;
import com.limitlessmobility.iVendGateway.model.sodexo.SodexoRefundModel;
import com.limitlessmobility.iVendGateway.model.zeta.ZetaRefundModel;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.services.amazonpay.AmazonRefundServicevV2;
import com.limitlessmobility.iVendGateway.services.bharatqr.BharatQRRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.common.CommonQRServiceV2;
import com.limitlessmobility.iVendGateway.services.paytm.PaytmRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.payu.PayuRefundRequest;
import com.limitlessmobility.iVendGateway.services.payu.PayuRefundService;
import com.limitlessmobility.iVendGateway.services.phonepe.PhonePeRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.sodexo.SodexoRefundService;
import com.limitlessmobility.iVendGateway.services.sodexo.SodexoRefundServiceV2;
import com.limitlessmobility.iVendGateway.services.wallet.WalletRefundService;
import com.limitlessmobility.iVendGateway.services.zeta.ZetaRefundServiceV2;

public class SavePaymentDaoImpl implements SavePaymentDao{
	
	

	@Override
	public boolean saveRefundData(RefundData reData) {
		

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get SaveRefundData DAO method is calling.. "+reData.toString());
		System.out.println("redata amt : "+reData.getAmount());
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				
				
//				PaymentTransaction paymentTransaction = getPspTxnId(reData.getMerchant_order_id());
				
				String isExistQuery = "SELECT * FROM payment_refund WHERE merchant_order_id='"+reData.getMerchant_order_id()+"'";
				PreparedStatement ps1 = conn.prepareStatement(isExistQuery);
			      java.sql.ResultSet resultSet = ps1.executeQuery();
			      //STEP 5: Extract data from result set
			      if(resultSet.next()){
			    	  System.out.println("Record already exist in payment_refund Table...and it's OrderId is "+reData.getMerchant_order_id());
			    	  return true;
			      } else {
				
			    	  String queryTransaction = "SELECT * FROM payment_transactions WHERE order_id='"+reData.getMerchant_order_id()+"' OR psp_transaction_id='"+reData.getMerchant_order_id()+"' OR device_transaction_id='"+reData.getMerchant_order_id()+reData.getTelemetryId()+"'";		
						System.out.println("queryTransactionThree.. "+queryTransaction);
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
						String amount = reData.getAmount();
						try {
						double dAmount=Double.parseDouble(amount);
						if(dAmount < 1){
							amount = rsTransaction.getString("paid_amount");
							double dAuthAmount=Double.parseDouble(amount);
							if(dAuthAmount<1){
								amount=rsTransaction.getString("auth_amount");
							}
						}} catch(Exception e) {}
						preparedStatement.setString(1, amount);
						
						if(reData.getPsp_id().equalsIgnoreCase("bharatqr")){
							preparedStatement.setString(2, rsTransaction.getString("order_id"));
							preparedStatement.setString(3, rsTransaction.getString("order_id"));
						} else if(reData.getPsp_id().equalsIgnoreCase("sodexo")){
							preparedStatement.setString(2, rsTransaction.getString("order_id"));
							preparedStatement.setString(3, rsTransaction.getString("order_id"));
						} else{
							preparedStatement.setString(2, reData.getMerchant_order_id());
							preparedStatement.setString(3, reData.getMerchant_order_id());
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
						preparedStatement.setString(13, reData.getApp_id());
						preparedStatement.setString(14, reData.getTelemetryId());
						preparedStatement.setString(15, "0");
						preparedStatement.setString(16, reData.getWalletAccountId());
						preparedStatement.setString(17, reData.getWalletAcountKey());
						
						int isInsert = preparedStatement.executeUpdate();

						System.out.println("Record is inserted into payment_refund table!");
						if (isInsert > 0) {
							
							System.out.println("Save Refund Payment Transaction Done.");
							
							
							
							
							
							
							
							
		/*****************************Early Refund Start*****************************************/

							/*String amountCheckQuery = "SELECT * FROM payment_refund where refund_status=0 and psp_id='"+rsTransaction.getString("psp_id")+"'";				
							System.out.println("queryyyyyyyyyyyyyyyyyyyy.. "+amountCheckQuery);
								PreparedStatement pspAmountCheck = conn.prepareStatement(amountCheckQuery);
							      java.sql.ResultSet rsAmountCheck = pspAmountCheck.executeQuery();
							      while (rsAmountCheck.next()) {
							    	  Statement stmt = conn.createStatement();
							    	  String updateAmountQry = "UPDATE payment_refund SET amount = (SELECT auth_amount FROM payment_transactions WHERE order_id='"+rsAmountCheck.getString("merchant_order_id")+"') WHERE merchant_order_id='"+rsAmountCheck.getString("merchant_order_id")+"'";
							    	  stmt.executeUpdate(updateAmountQry);
							      }*/
							 
							List<RefundSatusModel> listStatus = new ArrayList<RefundSatusModel>();
							String refundStatus="failure";
						/*String pspquery = "SELECT DISTINCT psp_id FROM payment_refund WHERE  refund_status=0";				
						System.out.println("queryyyyyyyyyyyyyyyyyyyy.. "+pspquery);
							PreparedStatement pspps = conn.prepareStatement(pspquery);
							String pid="";
						      java.sql.ResultSet psprs = pspps.executeQuery();
						      while (psprs.next()) {
								pid=psprs.getString("psp_id").trim();
								System.out.println("psp_id " +pid);*/
							PaytmRefundRequestModelFinal paytmRefundRequestModelFinal = new PaytmRefundRequestModelFinal();
							String pid=rsTransaction.getString("psp_id");
							
							Phoneperefundrequest phoneperefundrequest=new Phoneperefundrequest();
							Bharatqrrefundrequest bharatqrrefundrequest = new Bharatqrrefundrequest();
							RefundRequestModel refundRequestModelBharatqr = new RefundRequestModel();
							RefundInitialData refundInitialData = new RefundInitialData();
								
								
			                	try{  
								if (pid.contains("paytm")) {
									
									System.out.println("paytm refund......................................... " +pid);
									try{ 		  
									String query = "SELECT * FROM payment_refund WHERE psp_id='PayTM' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";				
									System.out.println("EarlyRefundPaytm "+query);
									PreparedStatement ps = conn.prepareStatement(query);
									
								      java.sql.ResultSet rs = ps.executeQuery();
								      //STEP 5: Extract data from result set
								      if(rs.next()){
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
							                		refundStatuss.setPaymentRefNo("");
							                	}
								    		 
								    	  PaytmRefundRequest paytmRefundRequest = new PaytmRefundRequest();
								    	  paytmRefundRequest=new PaytmRefundRequest();
								    	  paytmRefundRequest.setAmount(rs.getString("amount"));
								    	  paytmRefundRequest.setCurrencyCode("INR");
								    	  paytmRefundRequest.setMerchantOrderId(rs.getString("merchant_order_id"));
								    	  
								    	  RandomString randomString= new RandomString(); 
//								    	  String refundRefId = randomString.generateRandomString(6);
								    	  paytmRefundRequest.setRefundRefId(rs.getString("refund_ref_id"));
								    	  paytmRefundRequest.setTxnGuid(rs.getString("txn_guid"));
								    	  
								    	  paytmRefundRequestModelFinal.setChannel("POS");
								    	  paytmRefundRequestModelFinal.setIpAddress("139.59.73.155");
								    	  paytmRefundRequestModelFinal.setOperationType("REFUND_MONEY");
								    	  paytmRefundRequestModelFinal.setVersion("1.0");
								    	  paytmRefundRequestModelFinal.setPlatformName("PayTM");
								    	  paytmRefundRequestModelFinal.setRequest(paytmRefundRequest);
								    	  
								    	  
//								    	  ObjectMapper mapperObj = new ObjectMapper();
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
								            	  
								            	  CommonQRServiceV2 refundStatusService = new CommonQRServiceV2();
								            	  String refundStatusrequest = "{\"orderId\":\""+rs.getString("merchant_order_id")+"\",\"posId\":\""+rs.getString("terminal_id")+"\",\"amount\":\""+rs.getString("amount")+"\",\"pspId\":\""+rs.getString("psp_id")+"\",\"app_id\":\""+rs.getString("app_id")+"\",\"source\":\"RefundBack\"}";
								            	  try {
								            		  String refundStatusResponse = refundStatusService.refundCheckStatus(refundStatusrequest);
								            		  JSONObject refundStatusJson = new JSONObject(refundStatusResponse);
								            		  if(refundStatusJson.getString("status").equalsIgnoreCase("success")) {
								            			  refundStatuss.setStatusId(2);
								            		  }
								            	  } catch(Exception e) {
								            		  refundStatuss.setStatusId(1);
								            	  }
								            	  
								            	  listStatus.add(refundStatuss);//refund status list
								            	  MySqlDate mySqlDate = new MySqlDate();
								            	   Statement stmt = conn.createStatement();
								                  String updateRefundedSql = "UPDATE payment_refund " +
								                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundResponseJson.getString("orderId")+"'";
								                  System.out.println("Paytm update payment_refund.."+updateRefundedSql);
								                  stmt.executeUpdate(updateRefundedSql);
								                  
								                  
								                  
								                  /*Statement stmt1 = conn.createStatement();
								                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+paytmRefundRequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
								                  System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
								                  stmt1.executeUpdate(updateTerminalLogSql);*/
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
								    	  } catch (Exception e) {
									    	  System.out.println("Save Refund Payment Transaction ERROR paytm..: "+e);
									      }
								} else if(pid.contains("phonepe")) { 
									System.out.println("phonepe refund........................................." +pid);
			                    try{
			                    	String queryCheck = "SELECT * FROM payment_transactions WHERE psp_id='phonepe' and order_id='"+reData.getMerchant_order_id()+"' and device_id='static'";
			                    	PreparedStatement psCheck = conn.prepareStatement(queryCheck);
							        
						             java.sql.ResultSet rsCheck = psCheck.executeQuery();
						             //STEP 5: Extract data from result set
						             if(rsCheck.next()){
						            	 
				                    String query = "SELECT * FROM payment_transactions WHERE psp_id='phonepe' and order_id='"+reData.getMerchant_order_id()+"'";    
							           System.out.println("EarlyRefundPhonepe "+query);
							        PreparedStatement ps = conn.prepareStatement(query);
							        
							             java.sql.ResultSet rs = ps.executeQuery();
							             //STEP 5: Extract data from result set
							             if(rs.next()){
							            	 RefundSatusModel refundStatuss = new RefundSatusModel();
							            	 try{
							                		refundStatuss.setAppId(rs.getString("app_id"));
							                	} catch(Exception e){
							                		refundStatuss.setAppId("");
							                	}
							                	try{
							                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
							                	} catch(Exception e){
							                		refundStatuss.setPaymentRefNo("");
							                	}
							              phoneperefundrequest.setTransactionid(rs.getString("order_id"));
							              phoneperefundrequest.setAmount(Long.parseLong(reData.getAmount()));
							                              
//							              ObjectMapper mapperObj = new ObjectMapper();
							              String jsonStr ="";
							              
							                 try {
							                     // get phoneperefundrequest object as a json string
							                     jsonStr = phoneperefundrequest.toString();
							                     System.out.println(jsonStr);
							                     //PayTmRefundService paytmRefundService = new PayTmRefundService();
							                     PhonePeRefundServiceV2 peRefundService=new PhonePeRefundServiceV2();
							                    // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
							                     String pprefundResponse=peRefundService.refundStatic(jsonStr);
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
							                         
							                         CommonQRServiceV2 refundStatusService = new CommonQRServiceV2();
									            	  String refundStatusrequest = "{\"orderId\":\""+rs.getString("order_id")+"\",\"posId\":\""+rs.getString("terminal_id")+"\",\"amount\":\""+rs.getString("auth_amount")+"\",\"pspId\":\""+rs.getString("psp_id")+"\",\"app_id\":\""+rs.getString("app_id")+"\",\"source\":\"RefundBack\"}";
									            	  
									            	  //have to check checkSTatus of  refund
									            	  try {
									            		  String refundStatusResponse = refundStatusService.refundCheckStatus(refundStatusrequest);
									            		  JSONObject refundStatusJson = new JSONObject(refundStatusResponse);
									            		  if(refundStatusJson.getString("status").equalsIgnoreCase("success")) {
									            			  refundStatuss.setStatusId(2);
									            		  }
									            	  } catch(Exception e) {
									            		  System.out
														        .println(e);
									            		  refundStatuss.setStatusId(1);
									            	  }
									            	  
									            	  
									            	  
									            	  listStatus.add(refundStatuss);//refund status list
							                         /*Statement stmt1 = conn.createStatement();
							                         String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+phoneperefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
							                         System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
							                         stmt1.executeUpdate(updateTerminalLogSql);*/
							                     }
							                     }catch(Exception e){
									            	  System.out.println("Error in update.. "+e);
									              }
							                 } catch (Exception e) {
							                     e.printStackTrace();
							                 }
							                 
							                 
							              
							             } 
							                    } else {
			                    String query = "SELECT * FROM payment_refund WHERE psp_id='phonepe' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";    
			           System.out.println("EarlyRefundPhonepe "+query);
			        PreparedStatement ps = conn.prepareStatement(query);
			        
			             java.sql.ResultSet rs = ps.executeQuery();
			             //STEP 5: Extract data from result set
			             if(rs.next()){
			            	 RefundSatusModel refundStatuss = new RefundSatusModel();
			            	 try{
			                		refundStatuss.setAppId(rs.getString("app_id"));
			                	} catch(Exception e){
			                		refundStatuss.setAppId("");
			                	}
			                	try{
			                		refundStatuss.setPaymentRefNo(rs.getString("merchant_order_id"));
			                	} catch(Exception e){
			                		refundStatuss.setPaymentRefNo("");
			                	}
			              phoneperefundrequest.setTransactionid(rs.getString("merchant_order_id"));
			              phoneperefundrequest.setAmount(rs.getLong("amount"));
			                              
//			              ObjectMapper mapperObj = new ObjectMapper();
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
			                         refundStatuss.setStatusId(1);
			                         
			                         CommonQRServiceV2 refundStatusService = new CommonQRServiceV2();
					            	  String refundStatusrequest = "{\"orderId\":\""+rs.getString("merchant_order_id")+"\",\"posId\":\""+rs.getString("terminal_id")+"\",\"amount\":\""+rs.getString("amount")+"\",\"pspId\":\""+rs.getString("psp_id")+"\",\"app_id\":\""+rs.getString("app_id")+"\",\"source\":\"RefundBack\"}";
					            	  try {
					            		  String refundStatusResponse = refundStatusService.refundCheckStatus(refundStatusrequest);
					            		  JSONObject refundStatusJson = new JSONObject(refundStatusResponse);
					            		  if(refundStatusJson.getString("status").equalsIgnoreCase("success")) {
					            			  refundStatuss.setStatusId(2);
					            		  }
					            	  } catch(Exception e) {
					            		  refundStatuss.setStatusId(1);
					            	  }
					            	  listStatus.add(refundStatuss);//refund status list
			                         /*Statement stmt1 = conn.createStatement();
			                         String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+phoneperefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
			                         System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
			                         stmt1.executeUpdate(updateTerminalLogSql);*/
			                     }
			                     }catch(Exception e){
					            	  System.out.println("Error in update.. "+e);
					              }
			                 } catch (Exception e) {
			                     e.printStackTrace();
			                 }
			                 
			                 
			              
			             } 
			                    }
			              } catch (Exception e) {
			               System.out.println("Save PhonePe Refund Payment Transaction ERROR.: "+e);
			              }
								}else if(pid.contains("zeta")) {
									System.out.println("zeta refund........................................." +pid);
			                        try{ 		  
			                        String query = "SELECT * FROM payment_refund WHERE psp_id='zeta' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";				
			            			System.out.println("EarlyRefundZeta "+query);
			        				PreparedStatement ps = conn.prepareStatement(query);
			        				ZetaRefundModel zetaRefundModel = new ZetaRefundModel();
			        			      java.sql.ResultSet rs = ps.executeQuery();
			        			      //STEP 5: Extract data from result set
			        			      if(rs.next()){
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
			        			    	        			    	  
//			        			    	  String jsonStr ="";
			        			    	  
			        			          try {
			        			              // get paytmRefundRequestModelFinal object as a json string
//			        			              jsonStr = mapperObj.writeValueAsString(zetaRefundModel);
//			        			              System.out.println(jsonStr);
			        			              //PayTmRefundService paytmRefundService = new PayTmRefundService();
			        			              ZetaRefundServiceV2 zetaRefundService = new ZetaRefundServiceV2();
			        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
			        			              String zetaRefundResponse=zetaRefundService.getZetaRefund(zetaRefundModel);
			        			              System.out.println("zetaRefundResponse "+zetaRefundResponse);
			        			              JSONObject refundResponseJson = new JSONObject(zetaRefundResponse);
//			        			              System.out.println("refundResponseJson "+refundResponseJson);
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
								                  refundStatuss.setStatusId(1);
								                  listStatus.add(refundStatuss);//refund status list
								                  /*Statement stmt1 = conn.createStatement();
								                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+zetaRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+zetaRefundModel.getTransactionId()+"'";
								                  System.out.println("zeta update terminal_event_log.."+updateTerminalLogSql);
							                  stmt1.executeUpdate(updateTerminalLogSql);*/
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
								} else if (pid.contains("bharatqr")) {
									System.out.println("bharatqr refund........................................." +pid);
			                        try{ 		  
			                        String query = "SELECT * FROM payment_refund WHERE psp_id='bharatqr' and merchant_order_id='"+rsTransaction.getString("order_id")+"' and refund_status=0";				
			            			System.out.println("EarlyRefundBharatqr "+query);
			        				PreparedStatement ps = conn.prepareStatement(query);
			        				
			        			      java.sql.ResultSet rs = ps.executeQuery();
			        			      //STEP 5: Extract data from result set
			        			      if(rs.next()){
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
			        			                  refundStatuss.setStatusId(1);
			        			                  listStatus.add(refundStatuss);//refund status list
			        			                  /*Statement stmt1 = conn.createStatement();
			        			                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+bharatqrrefundrequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+bharatqrrefundrequest.getTransactionid()+"'";
			        			                  System.out.println("bharatqr update terminal_event_log.."+updateTerminalLogSql);
			        			                  stmt1.executeUpdate(updateTerminalLogSql);*/
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
								} else if(pid.contains("Sodexo")) {
									System.out.println("Sodexo refund........................................." +pid);
									
			                        try{ 		  
			                        String query = "SELECT * FROM payment_refund WHERE psp_id='Sodexo' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";				
			            			System.out.println("EarlyRefundSodexo "+query);
			        				PreparedStatement ps = conn.prepareStatement(query);
			        				SodexoRefundModel sodexoRefundModel	 = new SodexoRefundModel();
			        			      java.sql.ResultSet rs = ps.executeQuery();
			        			      //STEP 5: Extract data from result set
			        			      if(rs.next()){
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
			        			    	  System.out.println("Sodexo Refund Processing!!");
			        			    	  
			        			    	  sodexoRefundModel.setTransactionId(rs.getString("merchant_order_id"));
			        			    	  sodexoRefundModel.setAmount(rs.getString("amount"));
			        			    	        			    	  
//			        			    	  String jsonStr ="";
			        			    	  
			        			          try {
			        			              SodexoRefundServiceV2 sodexoRefundService = new SodexoRefundServiceV2();
			        			             // String refundResponse = paytmRefundService.payTmRefundProcess(jsonStr);
			        			              String sodexoRefundResponse=sodexoRefundService.getSodexoRefund(sodexoRefundModel);
			        			              System.out.println("sodexoRefundResponse "+sodexoRefundResponse);
			        			              JSONObject refundResponseJson = new JSONObject(sodexoRefundResponse);
//			        			              System.out.println("refundResponseJson "+refundResponseJson);
			        			              refundStatus = refundResponseJson.getString("status").trim();
			        			              System.out.println("..........SODEXO REFUND STATUS........."+ refundStatus);
			        			              try{
			        			              if(refundStatus.equalsIgnoreCase("SUCCESS")){
								            	  MySqlDate mySqlDate = new MySqlDate();
								            	   Statement stmt = conn.createStatement();
								                  String updateRefundedSql = "UPDATE payment_refund " +
								                               "SET refund_status = '1', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+sodexoRefundModel.getTransactionId()+"'";
								                  stmt.executeUpdate(updateRefundedSql);
								                  refundStatuss.setStatusId(1);
								                  listStatus.add(refundStatuss);//refund status list
								                  /*Statement stmt1 = conn.createStatement();
								                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+sodexoRefundModel.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+sodexoRefundModel.getTransactionId()+"'";
								                  stmt1.executeUpdate(updateTerminalLogSql);*/
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
								} else if(pid.contains("amazonpay")){
									System.out.println("Amazonpay refund........................................." +pid);
									try{ 
									String query = "SELECT * FROM payment_refund WHERE psp_id='amazonpay' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0"; 
									System.out.println("EarlyRefundAmazonpay "+query);
									PreparedStatement ps = conn.prepareStatement(query);

									java.sql.ResultSet rs = ps.executeQuery();
									//STEP 5: Extract data from result set
									if(rs.next()){
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
									refundStatuss.setStatusId(1);
									
									CommonQRServiceV2 refundStatusService = new CommonQRServiceV2();
					            	  String refundStatusrequest = "{\"orderId\":\""+rs.getString("merchant_order_id")+"\",\"posId\":\""+rs.getString("terminal_id")+"\",\"amount\":\""+rs.getString("amount")+"\",\"pspId\":\""+rs.getString("psp_id")+"\",\"app_id\":\""+rs.getString("app_id")+"\",\"source\":\"RefundBack\"}";
					            	  try {
					            		  String refundStatusResponse = refundStatusService.refundCheckStatus(refundStatusrequest);
					            		  JSONObject refundStatusJson = new JSONObject(refundStatusResponse);
					            		  if(refundStatusJson.getString("status").equalsIgnoreCase("success")) {
					            			  refundStatuss.setStatusId(2);
					            		  }
					            	  } catch(Exception e) {
					            		  refundStatuss.setStatusId(1);
					            	  }
					            	  
									listStatus.add(refundStatuss);//refund status list
									/*Statement stmt1 = conn.createStatement();
									String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+refundInitialData.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundReferenceId+"'";
									System.out.println("AmazonPay update terminal_event_log.."+updateTerminalLogSql);
									stmt1.executeUpdate(updateTerminalLogSql);*/
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
								} else if (pid.contains("payu")) {
									
									System.out.println("payu refund......................................... " +pid);
									try{ 		  
									String query = "SELECT * FROM payment_refund WHERE psp_id='payu' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";				
									System.out.println("EarlyRefundPayu "+query);
									PreparedStatement ps = conn.prepareStatement(query);
									
								      java.sql.ResultSet rs = ps.executeQuery();
								      //STEP 5: Extract data from result set
								      if(rs.next()){
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
							                		refundStatuss.setPaymentRefNo("");
							                	}
								    		 
							              
								    	  PayuRefundRequest payuRefundRequest = new PayuRefundRequest();
								    	  payuRefundRequest.setAmount(amount);
								    	  payuRefundRequest.setOrderId(rs.getString("merchant_order_id"));
//								    	  payuRefundRequest.setCommand(command);
//								    	  payuRefundRequest.setKey(key);
								    	  payuRefundRequest.setPayuId(rs.getString("txn_guid"));
								    	  payuRefundRequest.setPosId(reData.getTelemetryId());
//								    	  payuRefundRequest.setTokenId(tokenId);
								    	  
								    	  /*paytmRefundRequest=new PaytmRefundRequest();
								    	  paytmRefundRequest.setAmount(rs.getString("amount"));
								    	  paytmRefundRequest.setCurrencyCode("INR");
								    	  paytmRefundRequest.setMerchantOrderId(rs.getString("merchant_order_id"));
								    	  
								    	  RandomString randomString= new RandomString(); 
//								    	  String refundRefId = randomString.generateRandomString(6);
								    	  paytmRefundRequest.setRefundRefId(rs.getString("refund_ref_id"));
								    	  paytmRefundRequest.setTxnGuid(rs.getString("txn_guid"));
								    	  
								    	  paytmRefundRequestModelFinal.setChannel("POS");
								    	  paytmRefundRequestModelFinal.setIpAddress("139.59.73.155");
								    	  paytmRefundRequestModelFinal.setOperationType("REFUND_MONEY");
								    	  paytmRefundRequestModelFinal.setVersion("1.0");
								    	  paytmRefundRequestModelFinal.setPlatformName("PayTM");
								    	  paytmRefundRequestModelFinal.setRequest(paytmRefundRequest);*/
								    	  
								    	  
//								    	  ObjectMapper mapperObj = new ObjectMapper();
								    	  String jsonStr ="";
								    	  
								          try {
								              // get paytmRefundRequestModelFinal object as a json string
								              jsonStr = paytmRefundRequestModelFinal.toString();
								              System.out.println(jsonStr);
								              PayuRefundService payuRefundService = new PayuRefundService();
								              String refundResponse =payuRefundService.refund(payuRefundRequest);
								              
								              JSONObject refundResponseJson = new JSONObject(refundResponse);
								              System.out.println("refundStatus "+refundStatus);
								              refundStatus = refundResponseJson.getString("status");
								              System.out.println("refundStatus "+refundStatus);
								              
								              try{
								              if(refundStatus.equalsIgnoreCase("1")){
								            	  
								            	  CommonQRServiceV2 refundStatusService = new CommonQRServiceV2();
								            	  String refundStatusrequest = "{\"orderId\":\""+rs.getString("merchant_order_id")+"\",\"posId\":\""+rs.getString("terminal_id")+"\",\"amount\":\""+rs.getString("amount")+"\",\"pspId\":\""+rs.getString("psp_id")+"\",\"app_id\":\""+rs.getString("app_id")+"\",\"source\":\"RefundBack\"}";
								            	  try {
								            		  String refundStatusResponse = refundStatusService.refundCheckStatus(refundStatusrequest);
								            		  JSONObject refundStatusJson = new JSONObject(refundStatusResponse);
								            		  if(refundStatusJson.getString("status").equalsIgnoreCase("success")) {
								            			  refundStatuss.setStatusId(2);
								            		  } else {
								            			  refundStatuss.setStatusId(1);
								            		  }
								            	  } catch(Exception e) {
								            		  refundStatuss.setStatusId(1);
								            	  }
								            	  
								            	  listStatus.add(refundStatuss);//refund status list
								            	  MySqlDate mySqlDate = new MySqlDate();
								            	   Statement stmt = conn.createStatement();
								                  String updateRefundedSql = "UPDATE payment_refund " +
								                               "SET refund_status = '3', txn_guid='"+refundResponseJson.getString("requestId")+"', refund_date='"+mySqlDate.getDate()+"' WHERE merchant_order_id ='"+refundResponseJson.getString("orderId")+"'";
								                  System.out.println("Paytm update payment_refund.."+updateRefundedSql);
								                  stmt.executeUpdate(updateRefundedSql);
								                  
								                  
								                  
								                  /*Statement stmt1 = conn.createStatement();
								                  String updateTerminalLogSql = "UPDATE terminal_event_log SET is_refund_Done = '1', refund_amount='"+paytmRefundRequest.getAmount()+"', pending_refund_amount='0' WHERE server_trn_id='"+refundResponseJson.getString("orderId")+"'";
								                  System.out.println("phonepe update terminal_event_log.."+updateTerminalLogSql);
								                  stmt1.executeUpdate(updateTerminalLogSql);*/
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
									    	  System.out.println("Save Refund Payment Transaction ERROR paytm..: "+e);
									      }
								} else {
									
									
									System.out.println("wallet refund........................................." +pid);
			                        try{ 		  
			                        String query = "SELECT * FROM payment_refund WHERE psp_id='"+pid+"' and merchant_order_id='"+reData.getMerchant_order_id()+"' and refund_status=0";				
			            			System.out.println("EarlyRefundWallet "+query);
			        				PreparedStatement ps = conn.prepareStatement(query);
			        			      java.sql.ResultSet rs = ps.executeQuery();
			        			      //STEP 5: Extract data from result set
			        			      if(rs.next()){
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
			        			    		  refundRequest.setSource(reData.getSource());
			        			    		  
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
			 					            	   
			        			    			}
			        			    		  } catch(Exception e){
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
								
			                	  
								
							
						      System.out.println("listStatus.. "+listStatus);
						      CommonService.sendRefundStatus(listStatus);
						
		/*****************************Early Refund End******************************************/
							
							
							
							
							
							return true;
						} else{
							System.out.println("Save Refund Payment Transaction Failure.");
							return true;
						}
					      
							} else {
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						String queryInsertRefund = "INSERT INTO payment_refund"
								+ "(amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id,"
								+ "platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id,refund_status, wallet_account_id, wallet_account_key) VALUES"
								+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						
						System.out.println("queryInsertRefund... "+queryInsertRefund);
						PreparedStatement preparedStatement = conn.prepareStatement(queryInsertRefund);
						String amount = reData.getAmount();
						preparedStatement.setString(1, amount);
						
							preparedStatement.setString(2, reData.getMerchant_order_id());
							preparedStatement.setString(3, reData.getMerchant_order_id());
						
						preparedStatement.setString(4, reData.getMerchant_order_id());
						preparedStatement.setString(5, "");
						
						RandomString randomRefundId = new RandomString();
				        String uniqueRefundId = randomRefundId.generateRandomString(16);
						preparedStatement.setString(6, uniqueRefundId);
						preparedStatement.setString(7, "");
						preparedStatement.setString(8, "");
						preparedStatement.setString(9, "");
						preparedStatement.setString(10, "");
						preparedStatement.setString(11, "V2");
						preparedStatement.setString(12, reData.getPsp_id());
						preparedStatement.setString(13, reData.getApp_id());
						preparedStatement.setString(14, reData.getTelemetryId());
						preparedStatement.setString(15, String.valueOf(reData.getRefundStatus()));
						preparedStatement.setString(16, reData.getWalletAccountId());
						preparedStatement.setString(17, reData.getWalletAcountKey());
						
						int isInsert = preparedStatement.executeUpdate();

						System.out.println("Record is inserted into payment_refund table!");
						if (isInsert > 0) {
							
							System.out.println("Save Refund Payment Transaction Done.");
							 return true;
							
						}
			      }
						
						
						
						
						
						
						
						
			      return false;
			}}
		} catch (Exception e) {
			System.out.println("Exception in SaveRefundData Dao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	
	}

	@Override
	public PaymentTransaction getPspTxnId(String merchantOrderId) {
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		String pspTxnId=null;

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get get  psp_transaction_id id using merchant_order_id method is calling!!");

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {

			if (isConnected) {

				String sql = "SELECT *  FROM payment_transactions  WHERE merchant_order_id='"+merchantOrderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);

				while (result.next()) {

					paymentTransaction.setPspTransactionId(result.getString("psp_transaction_id"));
				}

			}
		} catch (Exception e) {
			System.out.println("Exception is.." + e);
		} finally {
			dbConfig.closeConnection(conn);
		}

		return paymentTransaction;
	}

	
}
