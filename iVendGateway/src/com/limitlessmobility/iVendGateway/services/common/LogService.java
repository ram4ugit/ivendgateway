package com.limitlessmobility.iVendGateway.services.common;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.print.DocFlavor.STRING;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;

@Controller
//@RequestMapping(value="/v1")
public class LogService {
	
//	@RequestMapping(value="/logTest")
	public void exportLogg() {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get saveCustomerPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				// Blank workbook 
		        XSSFWorkbook workbook = new XSSFWorkbook(); 
		  
		        // Create a blank sheet 
		        XSSFSheet sheet = workbook.createSheet("student Details"); 
		        
		        // This data needs to be written (Object[]) 
		        Map<String, Object[]> data = new TreeMap<String, Object[]>(); 
		        data.put("1", new Object[]{"machine_name", "operator_identifier", "currency", "card_string", "seValue", "product", "HW_serial", "payment_method_descr", "recognition_descr", "card_first4digits", "card_last4digits", "transaction_id", "auTime", "auValue", "PayServTransid", "cancelType", "is_Multivend", "settlement_failed", "auth_DateAndTime", "product_name", "display_card_string", "is_refund_card", "payment_method_id_enc" });
		        
//				String sql = "SELECT t1.terminal_id, t2.terminal_address, t1.server_trn_id, t1.paid_amount, t1.product_code, t1.payment_code, t2.UID as uid  FROM terminal_event_log t1, terminal t2 WHERE t1.terminal_id = t2.terminal_id;";
		        
		        String sql = "SELECT t1.terminal_id, t1.payment_selection_date, t2.terminal_address, t1.server_trn_id, t1.paid_amount as amount, t1.product_code, t1.payment_code , t2.UID as uid FROM terminal_event_log t1 INNER JOIN terminal t2 ON t1.terminal_id = t2.terminal_id AND t1.error_code IS NULL";
		        
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				int i = 2;
				while(result.next()){
					String telemetryId = result.getString("terminal_id");
					String orderId = result.getString("server_trn_id");
					String product = result.getString("product_code");
					String uid = result.getString("UID");
					String machineName = result.getString("terminal_address");
					String amount = result.getString("amount");
					String pspId = result.getString("payment_code");
//					String dateTime = result.getString("payment_selection_date");
					Timestamp timestamp = result.getTimestamp("payment_selection_date");
					
					
					String txnId="";
					try{txnId = CommonService.getTransactionId(orderId);} catch(Exception e) {}
					
					
					data.put(String.valueOf(i), new Object[]{machineName, uid, "INR", "", amount, product, telemetryId, pspId, "", "", "", orderId, timestamp, "", txnId, "", "", "", "", product, "", "", "" }); 
					
					String sqlUpdate = "UPDATE terminal_event_log SET error_code='1' where server_trn_id='"+orderId+"'";
					Statement updateStmt = conn.createStatement();
					updateStmt.executeUpdate(sqlUpdate);
					try {updateStmt.close();}catch(Exception e) {}
					i++;
				}
		  
		        // Iterate over data and write to sheet 
		        Set<String> keyset = data.keySet(); 
		        int rownum = 0; 
		        for (String key : keyset) { 
		            // this creates a new row in the sheet 
		            Row row = sheet.createRow(rownum++); 
		            Object[] objArr = data.get(key); 
		            int cellnum = 0; 
		            for (Object obj : objArr) { 
		                // this line creates a cell in the next column of that row 
		                Cell cell = row.createCell(cellnum++); 
		                if (obj instanceof String) 
		                    cell.setCellValue((String)obj); 
		                else if (obj instanceof Integer) 
		                    cell.setCellValue((Integer)obj); 
		            } 
		        } 
		        try { 
		            // this Writes the workbook gfgcontribute 
		            FileOutputStream out = new FileOutputStream(new File("D://gfgcontribute.xlsx")); 
		            workbook.write(out); 
		            out.close(); 
		            System.out.println("gfgcontribute.xlsx written successfully on disk."); 
		        } 
		        catch (Exception e) { 
		            e.printStackTrace(); 
		        } 
			}
		} catch (Exception e) {
			System.out.println("Exception in saveCustomerPspDao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
	}

}
