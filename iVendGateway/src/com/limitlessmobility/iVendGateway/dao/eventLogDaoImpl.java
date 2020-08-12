package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.paytm.model.EventLogs;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaytmRefundRequestModelFinal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalEventModel;
//import com.mysql.jdbc.ResultSet;

public class eventLogDaoImpl implements EventLogDao{

	@Override
	public boolean saveEventLog(EventLogs eventLog) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("saveEventLog method is calling!!");
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
//				log.info("Connection Object Created..");
				
				String query = "INSERT INTO event_logs"
						+ "(terminal_id,event_date,event_type,event_details,event_location,version,orderid) VALUES"
						+ "(?,?,?,?,?,?,?)";
				
				
				PreparedStatement preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, eventLog.getTerminalId());
				
				preparedStatement.setString(2, timestamp.toString());
				preparedStatement.setString(3, eventLog.getEventType());
				preparedStatement.setString(4, eventLog.getEventDetails());
				preparedStatement.setString(5, eventLog.getEventLocation());
				preparedStatement.setString(6, eventLog.getVersion());
				preparedStatement.setString(7, eventLog.getOrderID());
				
				
				
				int isInsert = preparedStatement.executeUpdate();

				System.out.println("Record is inserted into table!");
				if (isInsert > 0) {
					return true;
				}

				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return false;
	}

	@Override
	public boolean saveTerminalEventLog(TerminalEventModel terminalEvent) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("save TerminalEventLog method is calling!!");
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
//				log.info("Connection Object Created..");
				
				String query = "SELECT payment_mode FROM terminal_event_log WHERE app_id='"+terminalEvent.getAppId()+"'";				
				
				PreparedStatement ps = conn.prepareStatement(query);
				
//				String sql = "SELECT * FROM terminal_event_log where app_id='"+terminalEvent.getAppId()+"'";
			      java.sql.ResultSet rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			    	  PreparedStatement update = conn.prepareStatement
			    			    ("UPDATE terminal_event_log SET terminal_id = '"+terminalEvent.getTerminalId()+"', telemetry_id = '"+terminalEvent.getTelemetryId()+"', "
			    			    		+ "trx_id = '"+terminalEvent.getTrxId()+"', payment_mode = '"+terminalEvent.getPaymentMode()+"', "
			    			    		+ "payment_code='"+terminalEvent.getPaymentCode()+"', vmc_payment_sel_code='"+terminalEvent.getVmcPaymentSelCode()+"', vmc_payment_sel_response='"+terminalEvent.getVmcPaymentSelResponse()+"',payment_selection_date='"+terminalEvent.getPaymentSelectionDate()+"', product_code='"+terminalEvent.getProductCode()+"', "
			    			    		+ "price='"+terminalEvent.getPrice()+"', vmc_product_sel_code='"+terminalEvent.getVmcProductSelCode()+"', vmc_product_sel_res='"+terminalEvent.getVmcProductSelRes()+"', product_selection_date='"+terminalEvent.getProductSelectionDate()+"', server_trn_id='"+terminalEvent.getServerTrnId()+"', status='"+terminalEvent.getStatus()+"', "
			    			    		+ "is_barcode_generated='"+terminalEvent.getIsBarcodeGenerated()+"', is_payment_done='"+terminalEvent.getIsPaymentDone()+"', vend_status='"+terminalEvent.getVmcProductVendStatus()+"', is_refund='"+terminalEvent.getIsRefund()+"', error_code='"+terminalEvent.getErrorCode()+"', "
			    			    		+ "txn_guid='"+terminalEvent.getTxnGuid()+"',paid_amount='"+terminalEvent.getPaidAmount()+"' WHERE app_id = '"+terminalEvent.getAppId()+"'");

			    			

			    			update.executeUpdate();
			    			System.out.println("paid Amount "+terminalEvent.getPaidAmount());
			    			System.out.println("Updated Successfully into table!");
			    			return true;                                  
			      } else {
			    	  String insertTableSQL = "INSERT INTO terminal_event_log"
			    				+ "(terminal_id, telemetry_id, trx_id, payment_mode,payment_code,vmc_payment_sel_code, vmc_payment_sel_response,"
			    				+ "payment_selection_date, product_code, price, vmc_product_sel_code, vmc_product_sel_res, product_selection_date, server_trn_id, "
			    				+ "status, is_barcode_generated, is_payment_done, vend_status, is_refund, error_code, txn_guid,app_id,paid_amount) VALUES"
			    				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			    		PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			    		preparedStatement.setString(1, terminalEvent.getTerminalId());
			    		preparedStatement.setString(2, terminalEvent.getTelemetryId());
			    		preparedStatement.setString(3, terminalEvent.getTrxId());
			    		preparedStatement.setString(4, terminalEvent.getPaymentMode());
			    		preparedStatement.setString(5, terminalEvent.getPaymentCode());
			    		preparedStatement.setString(6, terminalEvent.getVmcPaymentSelCode());
			    		preparedStatement.setString(7, terminalEvent.getVmcPaymentSelResponse());
			    		preparedStatement.setString(8, terminalEvent.getPaymentSelectionDate());
			    		preparedStatement.setString(9, terminalEvent.getProductCode());
			    		preparedStatement.setString(10, terminalEvent.getPrice());
			    		preparedStatement.setString(11, terminalEvent.getVmcProductSelCode());
			    		preparedStatement.setString(12, terminalEvent.getVmcProductSelRes());
			    		preparedStatement.setString(13, terminalEvent.getProductSelectionDate());
			    		preparedStatement.setString(14, terminalEvent.getServerTrnId());
			    		preparedStatement.setLong(15, terminalEvent.getStatus());
			    		preparedStatement.setInt(16, terminalEvent.getIsBarcodeGenerated());
			    		preparedStatement.setLong(17, terminalEvent.getIsPaymentDone());
			    		preparedStatement.setString(18, terminalEvent.getVmcProductVendStatus());
			    		preparedStatement.setLong(19, terminalEvent.getIsRefund());
			    		preparedStatement.setString(20, terminalEvent.getErrorCode());
			    		preparedStatement.setString(21, terminalEvent.getTxnGuid());
			    		preparedStatement.setString(22, terminalEvent.getAppId());
			    		preparedStatement.setString(23, terminalEvent.getPaidAmount());
//			    		preparedStatement.setInt(24, terminalEvent.getPendingRefundAmount());
			    		
			    		// execute insert SQL stetement
			    		int isInsert = preparedStatement.executeUpdate();

						System.out.println("Record is inserted into table.. "+isInsert);
						if (isInsert > 0) {
							return true;
						}
			      }
			      rs.close();

				System.out.println("Record is not inserted into table!");

				return false;
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return false;
	}


}
