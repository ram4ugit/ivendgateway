package com.limitlessmobility.iVendGateway.dao.common;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryRequestModel;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryResponseData;
import com.limitlessmobility.iVendGateway.model.common.PaymentHistoryResponseModel;
import com.limitlessmobility.iVendGateway.model.common.TransactionFilterResponse;

public class TransactionFilterDao {

	public List<TransactionFilterResponse> getTransactionByOrder(String orderId, String operatorId){

		List<TransactionFilterResponse> listResponse = new ArrayList<TransactionFilterResponse>(); 
		
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		String returnStatus="False";

		if (con == null) {
			isConnected = false;
		}

//		ProductPriceDetail productPriceDetail = new ProductPriceDetail();
		try {

			if (isConnected) {

				String query = "SELECT * FROM payment_transactions where order_id='"+orderId+"'";

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {
					TransactionFilterResponse transactionFilterResponse = new TransactionFilterResponse();
					if(rs.getString("paid_amount")==null){
						transactionFilterResponse.setAmount(0);
					} else {
						transactionFilterResponse.setAmount(Float.parseFloat(rs.getString("paid_amount").trim()));
					}
					transactionFilterResponse.setDate(rs.getString("auth_date"));
					String pspId = rs.getString("psp_id");
					try{
						String pspName = CommonService.getPspNameById(operatorId, pspId);
						
						if(CommonValidationUtility.isEmpty(pspName)) {
							transactionFilterResponse.setMode(rs.getString("psp_id"));
						} else{
							transactionFilterResponse.setMode(pspName);
						}
						
						if(transactionFilterResponse.getMode().contains("paytm")){
							transactionFilterResponse.setMode("Paytm");
						}
						
					} catch(Exception e){System.out.println(e);}
					transactionFilterResponse.setOrderId(rs.getString("order_id"));
					transactionFilterResponse.setRefNo(rs.getString("psp_transaction_id"));
					listResponse.add(transactionFilterResponse);
					
				}
			}

		} catch (Exception e) {
			System.out.println("paytm preauth transation details error "+e);

		} finally {
			dbConfig.closeConnection(con);
		}
		return listResponse;

	}
	
	public PaymentHistoryResponseModel getTransactionReport(PaymentHistoryRequestModel paymentHistoryRequestModel, String operatorID){

		System.out.println("getTransactionReport is calling");
		PaymentHistoryResponseModel response = new PaymentHistoryResponseModel();  
		response.setResponseType(paymentHistoryRequestModel.getDatatype());
		List<PaymentHistoryResponseData> data = new ArrayList<>();
		
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		Connection con = dbConfig.getCon();

		String returnStatus="False";

		if (con == null) {
			isConnected = false;
		}

//		ProductPriceDetail productPriceDetail = new ProductPriceDetail();
		try {

			if (isConnected) {
				StringBuilder queryBuilder = new StringBuilder();
				
				queryBuilder.append("SELECT tx.auth_amount as auth_amount, tx.auth_date as auth_date, tx.psp_id as psp_id, tx.order_id as psp_transaction_id, tx.terminal_id as terminal_id, tx.order_id as order_id, rf.amount as amount, rf.refund_status as refund_status, rf.refund_date as refund_date, rf.refund_ref_id as refund_ref_id FROM payment_transactions tx LEFT JOIN payment_refund rf ON tx.order_id = rf.merchant_order_id where tx.id!='1'");
				String offsetQry = "";
				
				
				if(paymentHistoryRequestModel.getPosId() != null && !paymentHistoryRequestModel.getPosId().isEmpty()) {
					queryBuilder.append(" and tx.terminal_id ='"+paymentHistoryRequestModel.getPosId()+"'");
					
					if(paymentHistoryRequestModel.getFromDate() != null && !paymentHistoryRequestModel.getFromDate().isEmpty()) {
						queryBuilder.append(" and DATE(tx.auth_date) between '"+paymentHistoryRequestModel.getFromDate()+"' and '"+paymentHistoryRequestModel.getToDate()+"'");
					}
					if(paymentHistoryRequestModel.getPaymentModeId() != null && !paymentHistoryRequestModel.getPaymentModeId().isEmpty()) {
						queryBuilder.append(" and tx.psp_id ='"+paymentHistoryRequestModel.getPaymentModeId()+"'");
					}
				} else {
				if(paymentHistoryRequestModel.getTransactionRefNo() != null && !paymentHistoryRequestModel.getTransactionRefNo().isEmpty()) {
					queryBuilder.append(" and tx.psp_transaction_id ='"+paymentHistoryRequestModel.getTransactionRefNo()+"'");
					
				} else {
					
				if(paymentHistoryRequestModel.getFromDate() != null && !paymentHistoryRequestModel.getFromDate().isEmpty()) {
					queryBuilder.append(" and DATE(tx.auth_date) between '"+paymentHistoryRequestModel.getFromDate()+"' and '"+paymentHistoryRequestModel.getToDate()+"'");
				}
				if(paymentHistoryRequestModel.getPaymentModeId() != null && !paymentHistoryRequestModel.getPaymentModeId().isEmpty()) {
					queryBuilder.append(" and tx.psp_id ='"+paymentHistoryRequestModel.getPaymentModeId()+"'");
				}
				
				
				boolean isTerminal=false;
				Set<String> terminalLists = new HashSet<String>();
				if(paymentHistoryRequestModel.getCustomerId()>0) {
					isTerminal=true;
					terminalLists = CommonService.getTerminalByCustomer(paymentHistoryRequestModel.getCustomerId());
					if(paymentHistoryRequestModel.getCustomerLocationId()>0) {
							terminalLists = CommonService.getTerminalByCustomerAndLoc(paymentHistoryRequestModel.getCustomerId(), paymentHistoryRequestModel.getCustomerLocationId());
					}
					if(paymentHistoryRequestModel.getMachineId() != null && !paymentHistoryRequestModel.getMachineId().isEmpty()) {
						terminalLists = CommonService.getTerminalByCustomerAndLocAndMachine(paymentHistoryRequestModel.getCustomerId(), paymentHistoryRequestModel.getCustomerLocationId(), paymentHistoryRequestModel.getMachineId());
					}
				} else if(paymentHistoryRequestModel.getCustomerLocationId()>0) {
					isTerminal=true;
							terminalLists = CommonService.getTerminalByCustomerLocation(paymentHistoryRequestModel.getCustomerLocationId());
						
					if(paymentHistoryRequestModel.getMachineId() != null && !paymentHistoryRequestModel.getMachineId().isEmpty()) {
						terminalLists = CommonService.getTerminalByCustomerLocAndMachine(paymentHistoryRequestModel.getCustomerLocationId(), paymentHistoryRequestModel.getMachineId());
					}
				} else if(paymentHistoryRequestModel.getMachineId() != null && !paymentHistoryRequestModel.getMachineId().isEmpty()) {
					isTerminal=true;
						terminalLists = CommonService.getTerminalByMachineId(paymentHistoryRequestModel.getMachineId());
				} else {
					isTerminal=true;
					terminalLists = CommonService.getTerminalByOperatorV2(operatorID);
				}
				
				
				if(isTerminal) {
					if(terminalLists.size()==0) {
						queryBuilder.append(" and tx.terminal_id ='RamTerminal'");
					}
				}
				if(terminalLists.size()>1) {
					String citiesCommaSeparated = String.join("','", terminalLists);
					String terminalListt = "'" + citiesCommaSeparated + "'";
					queryBuilder.append(" and tx.terminal_id in("+terminalListt+")");
				} else if(terminalLists.size()>0){
					queryBuilder.append(" and tx.terminal_id ='"+terminalLists.iterator().next()+"'");
				}
				}
				
				}
				
				int offsetVal = 1;
				if(paymentHistoryRequestModel.getPageNo()==1) {
					offsetVal=0;
				} else {
					
					offsetVal = (paymentHistoryRequestModel.getPageNo()-1)*paymentHistoryRequestModel.getPageSize();
				}
				offsetQry=" OFFSET "+offsetVal;
				
				int totalRecord = 0;
				if(paymentHistoryRequestModel.getDatatype().equalsIgnoreCase("list")) {
				String countQry = queryBuilder.toString();
				
				
				int firstPos = countQry.indexOf("SELECT") + "SELECT".length();
				int lastPos = countQry.indexOf("FROM", firstPos);
				String updatedQry = countQry.substring(0,firstPos) + " count(*) " + countQry.substring(lastPos);
				
				
				//String updatedQry = countQry.replace("*", "count(*)");
				System.out.println("updatedQry.. "+updatedQry);
				Statement stmtCount = con.createStatement();

				ResultSet rsCount = stmtCount.executeQuery(updatedQry);
				
				while (rsCount.next()) {
					totalRecord = rsCount.getInt(1);
				}
				
				
				
				Statement stmtPsp = con.createStatement();

//				ResultSet rsPsp = stmtPsp.executeQuery("SELECT psp_id,psp_name FROM machine_config where operatorId='"+operatorID+"' GROUP BY psp_name,psp_id");
				
				ResultSet rsPsp = stmtPsp.executeQuery("SELECT psp_id,psp_name FROM operator_psp where operatorId='"+operatorID+"' GROUP BY psp_name,psp_id");
				
				HashMap<String, String> hash_map = new HashMap<String, String>(); 
				
				while (rsPsp.next()) {
					hash_map.put(rsPsp.getString("psp_id"), rsPsp.getString("psp_name"));
				}
				
				queryBuilder.append(" order by tx.id desc LIMIT "+paymentHistoryRequestModel.getPageSize()+ offsetQry);
				System.out.println("queryBuilder qry "+queryBuilder.toString());
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(queryBuilder.toString());
		        
				while (rs.next()) {
					System.out.println("while");
					PaymentHistoryResponseData d = new PaymentHistoryResponseData();
					d.setPaidAmount(Double.parseDouble(rs.getString("auth_amount")));
					d.setDate(rs.getString("auth_date"));
					d.setPaymentModeName(rs.getString("psp_id"));
					d.setRefundReferenceNo(rs.getString("refund_ref_id"));


					/*try 
			        { 
			            // checking valid integer using parseInt() method 
			            Integer.parseInt(d.getPaymentModeName()); 
			            
			            if(d.getPaymentModeName().equalsIgnoreCase("188")) {
			            	d.setPaymentModeName("Vendiman Wallet");
			            } else {
			            	d.setPaymentModeName(CommonService.getPspNameById(operatorID, d.getPaymentModeName()));
			            }
			        }  
			        catch (NumberFormatException e)  
			        { 
			        	
			        } */
					
					
					if(CommonUtil.isInteger(d.getPaymentModeName())) {
						d.setPaymentModeName(hash_map.get(d.getPaymentModeName()));
//						d.setPaymentModeName(CommonService.getPspNameById(operatorID, d.getPaymentModeName()));
						
						/*if(d.getPaymentModeName() != null && !d.getPaymentModeName().isEmpty()) {} else {
							d.setPaymentModeName(CommonService.getPspNameById(operatorID, rs.getString("psp_id")));
							hash_map.put(rsPsp.getString("psp_id"), d.getp);
						}*/
					}
					d.setPaymentReferenceNo(rs.getString("psp_transaction_id"));
					
					
					d.setPosName(rs.getString("terminal_id"));
					//RefundDbModel refundModel = CommonService.getRefundDetails(rs.getString("order_id"));
					if(rs.getString("amount")!=null){
						d.setRefundAmount(Double.parseDouble(rs.getString("amount")));
						d.setRefundedOn(rs.getString("refund_date"));
						d.setRefundStatus(1);
						
					} else{
						d.setRefundAmount(0);
						d.setRefundedOn("NA");
						d.setRefundStatus(0);
					}
					
					data.add(d);
				}
				response.setTotalCount(totalRecord);
				response.setData(data);
				

				
				
				
				
				
				
				
				
				
				
				
				
				
				} else {
					Statement stmtPsp = con.createStatement();
					ResultSet rsPsp = stmtPsp.executeQuery("SELECT psp_id,psp_name FROM machine_config GROUP BY psp_name,psp_id");
					HashMap<String, String> hash_map = new HashMap<String, String>(); 
					while (rsPsp.next()) {
						hash_map.put(rsPsp.getString("psp_id"), rsPsp.getString("psp_name"));
					}
					
					
					Statement stmt = con.createStatement();
					System.out.println(queryBuilder.toString());
					String qryBuilderString = queryBuilder.toString();
					qryBuilderString.replace("OFFSET", " order by tx.id desc OFFSET ");
					
					ResultSet rs = stmt.executeQuery(queryBuilder.toString());
			        
			        FileInputStream inputStream = new FileInputStream(Util.newFilePathForReport());
			        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
			        inputStream.close();
			        
			        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template); 
			        wb.setCompressTempFiles(true);

			        SXSSFSheet sh = (SXSSFSheet) wb.getSheetAt(0);
			        sh.setRandomAccessWindowSize(100);// keep 100 rows in memory, exceeding rows will be flushed to disk
			        
			        Row rowHeader = sh.createRow(0);
			            Cell cellHeader = rowHeader.createCell(0);
			            cellHeader.setCellValue("posName");
			            
			            Cell cellHeader1 = rowHeader.createCell(1);
			            cellHeader1.setCellValue("paymentReferenceNo");
			            
			            Cell cellHeader2 = rowHeader.createCell(2);
			            cellHeader2.setCellValue("paymentModeName");
			            
			            Cell cellHeader3 = rowHeader.createCell(3);
			            cellHeader3.setCellValue("paidAmount");
			            
			            Cell cellHeader4 = rowHeader.createCell(4);
			            cellHeader4.setCellValue("refundStatus");
			            
			            Cell cellHeader5 = rowHeader.createCell(5);
			            cellHeader5.setCellValue("refundAmount");
			            
			            Cell cellHeader6 = rowHeader.createCell(6);
			            cellHeader6.setCellValue("refundedOn");
			            
			            Cell cellHeader7 = rowHeader.createCell(7);
			            cellHeader7.setCellValue("date");
			            
			            Cell cellHeader8 = rowHeader.createCell(8);
			            cellHeader8.setCellValue("refundReferenceNo");
			            
			        
				    int i = 1;
					while (rs.next()) {
						//System.out.println("while "+i);
						/*PaymentHistoryResponseData d = new PaymentHistoryResponseData();
						d.setPaidAmount(Double.parseDouble(rs.getString("auth_amount")));
						d.setDate(rs.getString("auth_date"));
						d.setPaymentModeName(rs.getString("psp_id"));
						
						if(CommonUtil.isInteger(d.getPaymentModeName())) {
							d.setPaymentModeName(hash_map.get(d.getPaymentModeName()));
//							d.setPaymentModeName(CommonService.getPspNameById(operatorID, d.getPaymentModeName()));
						}
						d.setPaymentReferenceNo(rs.getString("psp_transaction_id"));
						d.setPosName(rs.getString("terminal_id"));
						//RefundDbModel refundModel = CommonService.getRefundDetails(rs.getString("order_id"));
						if(rs.getString("amount")!=null){
							d.setRefundAmount(Double.parseDouble(rs.getString("amount")));
							d.setRefundedOn(rs.getString("refund_date"));
							d.setRefundStatus(rs.getInt("refund_status"));
							
						} else{
							d.setRefundAmount(0);
							d.setRefundedOn("NA");
							d.setRefundStatus(0);
						}
						
						data.add(d);*/
						
						Row row = sh.createRow(i);
				        //for(int cellnum = 0; cellnum < 10; cellnum++){
				            Cell cell = row.createCell(0);
				            cell.setCellValue(rs.getString("terminal_id"));
				            
				            Cell cell1 = row.createCell(1);
				            //System.out.println(rs.getString("psp_transaction_id"));
				            cell1.setCellValue(rs.getString("psp_transaction_id"));
				            
				            String pspName = rs.getString("psp_id");
				            if(CommonUtil.isInteger(pspName)) {
				            	pspName = hash_map.get(rs.getString("psp_id"));
//								d.setPaymentModeName(CommonService.getPspNameById(operatorID, d.getPaymentModeName()));
							}
				            
				            
				            Cell cell2 = row.createCell(2);
				            cell2.setCellValue(pspName);
				            
				            Cell cell3 = row.createCell(3);
				            cell3.setCellValue(Double.parseDouble(rs.getString("auth_amount")));
				            
				            String refundStatus="Not Refunded";
				            String refundAmount="0";
				            String refundedOn = "NA";
				            if(rs.getString("amount")!=null){
				            	refundAmount = rs.getString("amount");
				            	refundedOn = rs.getString("refund_date");
				            	refundStatus = "Refunded";
								
							}
				            
				            
				            Cell cell4 = row.createCell(4);
				            cell4.setCellValue(refundStatus);
				            
				            Cell cell5 = row.createCell(5);
				            cell5.setCellValue(refundAmount);
				            
				            Cell cell6 = row.createCell(6);
				            cell6.setCellValue(refundedOn);
				            
				            
				            Cell cell7 = row.createCell(7);
				            cell7.setCellValue(rs.getString("auth_date"));
				            
				            Cell cell8 = row.createCell(8);
				            cell8.setCellValue(rs.getString("refund_ref_id"));
				            
				        //}
				        i++;
					}
					response.setTotalCount(totalRecord);
					response.setData(data);
					
					try {
				       /* FileOutputStream out =  new FileOutputStream(new File("D://formulaDemo.xlsx"));
				        workbook.write(out);*/
						
						FileOutputStream out = new FileOutputStream(Util.reportdownloadPath());
					    wb.write(out);
				        out.close();
				        System.out.println("Excel with foumula cells written successfully");
				          
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				    } catch (IOException e) {
				        e.printStackTrace();
				    }
				}
			}
		} catch (Exception e) {
			System.out.println("paytm preauth transation details error "+e);

		} finally {
			dbConfig.closeConnection(con);
		}
		return response;

	}
}
