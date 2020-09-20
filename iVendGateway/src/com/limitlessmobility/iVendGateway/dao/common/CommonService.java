package com.limitlessmobility.iVendGateway.dao.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.db.Util;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.common.PspDetail;
import com.limitlessmobility.iVendGateway.model.common.RefundDbModel;
import com.limitlessmobility.iVendGateway.model.common.RefundSatusModel;
import com.limitlessmobility.iVendGateway.model.common.TransactionModelDB;
import com.limitlessmobility.iVendGateway.model.wallet.CaptureCardRequest;
import com.limitlessmobility.iVendGateway.model.wallet.TransactionDetailsCard;
import com.limitlessmobility.iVendGateway.model.wallet.VoidTransactionRequest;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonService {

	public static String getPspNameById(String operatorId, String pspId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		String pspName = "";
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT psp_name FROM machine_config WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					pspName = rs.getString("psp_name");
				} else {
					String queryy = "SELECT psp_name FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"'";
					System.out.println("queryy.." + queryy);

					Statement stmtt = con.createStatement();

					ResultSet rss = stmtt.executeQuery(query);
					if (rss.next()) {
						pspName = rss.getString("psp_name");
					}
				}
				
				if(pspName==""){
					pspName=pspId;
				}
				if(pspName==null){
					pspName=pspId;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return pspName;
	}
	
public static String getWalletType(int operatorId, String pspId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		String walletType = "";
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					walletType = rs.getString("wallet_type");
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return walletType;
	}
	

public static String getOrderByTerminal(String terminalID, String walletId){
	String orderId="";
	DbConfigration dbConfig = new DbConfigrationImpl();
	boolean isConnected = true;
	PspDetail pspDetail = new PspDetail();
	
	Connection con = dbConfig.getCon();
	if (con == null) {
		isConnected = false;
	}

	try {

		if (isConnected) {
//			logger.info("Connection Object Created...");

			String query = "SELECT * FROM payment_transactions WHERE terminal_id='"+terminalID+"' and psp_id= "+walletId+" and status='Pending' ORDER  BY Id DESC";
			System.out.println("query.." + query);

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				orderId=rs.getString("order_id");
			}
			
			/*if(pspDetail.getPspName()==""){
				pspDetail.setPspName(rs.getString("psp_name"));
			}*/

		}
	} catch (Exception e) {
		e.printStackTrace();
//		logger.error(e);
	} finally {
		dbConfig.closeConnection(con);
//		logger.info("db connection closed");
	}

	return orderId;
}
	public static PspDetail getWalletDetailsByID(Integer operatorId, Integer walletId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_merchant_id= "+walletId+"";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					pspDetail.setPspId(rs.getString("psp_id"));
					pspDetail.setPspName(rs.getString("psp_name"));
					pspDetail.setPspMerchantId(rs.getString("psp_merchant_id"));
					pspDetail.setPspMerchantKey(rs.getString("psp_merchant_key"));
				}
				
				/*if(pspDetail.getPspName()==""){
					pspDetail.setPspName(rs.getString("psp_name"));
				}*/

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return pspDetail;
	}
	public static boolean prepaidCardValidate(Integer operatorId, Integer walletId, String telemetryId){
		System.out.println("Prepaid Card Validate-operatorid-"+operatorId+"-walletid-"+walletId+"-telemetryid-"+telemetryId);
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= "+walletId+"";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					String queryMachine = "SELECT * FROM machine_config WHERE operatorId='"+operatorId+"' and telemetryId= '"+telemetryId+"' and psp_id='"+walletId+"' and isActive=1";
					System.out.println("queryMachine.." + queryMachine);

					Statement stmtMachine = con.createStatement();

					ResultSet rsMachine = stmtMachine.executeQuery(queryMachine);
					if (rsMachine.next()) {
						
						return true;
					}
				} else{
					return false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return false;
	}
	public static boolean saveRefund(RefundDbModel refundModel){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String queryInsertRefund = "INSERT INTO payment_refund (amount,merchant_order_id,merchant_guid,txn_guid,currency_code,refund_ref_id, platform_name,ip_address,operation_type,channel,version,psp_id,app_id,terminal_id,refund_status, refund_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				System.out.println("queryInsertRefund... "+queryInsertRefund);
				PreparedStatement ps = con.prepareStatement(queryInsertRefund);
				ps.setString(1, refundModel.getAmount());
				ps.setString(2, refundModel.getMerchantOrderId());
				ps.setString(3, refundModel.getPspOrderId());
				ps.setString(4, "");
				ps.setString(5, "");
				ps.setString(6, refundModel.getMerchantOrderId());
				ps.setString(7, "");
				ps.setString(8, "");
				ps.setString(9, "");
				ps.setString(10, "");
				ps.setString(11, "V2");
				ps.setString(12, refundModel.getPspId());
				ps.setString(13, refundModel.getAppId());
				ps.setString(14, refundModel.getTerminalId());
				ps.setString(15, "1");
				ps.setString(16, refundModel.getRefundDate());
				int count = ps.executeUpdate();
				
				if (count > 0) {
					
					Statement stmt1 = con.createStatement();
	                  String updateTerminalLogSql = "UPDATE payment_transactions SET service_type='refund', status='refund', status_msg='Invalid Card',  refund_amount='"+refundModel.getAmount()+"' WHERE order_id='"+refundModel.getMerchantOrderId()+"'";
	                  System.out.println("update transaction after wallet refund.."+updateTerminalLogSql);
	                  stmt1.executeUpdate(updateTerminalLogSql);
	                  
					return true;
				} else{
					return false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return false;
	}
	public static boolean validateCard(Integer operatorId, Integer walletId, String telemetryId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= "+walletId+"";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					return true;
				} else{
					return false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return false;
	}
	
	public static boolean isExistOrder(String orderId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM payment_transactions WHERE order_id='"+orderId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					return true;
				} else{
					return false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return false;
	}
	public static boolean isWalletEnable(String telemetryId, int pspId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		boolean status=false;
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				
				
					String query1 = "SELECT * FROM machine_config WHERE psp_id='"+pspId+"' and telemetryId='"+telemetryId+"' and isActive='1'";
					System.out.println("query.." + query1);

					Statement stmt1 = con.createStatement();

					ResultSet rs1 = stmt1.executeQuery(query1);
					if (rs1.next()) {
						status= true;
					} else{
						status = false;
					}
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return status;
	}
	public static boolean isCardEnable(String telemetryId, int operatorId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		boolean status=false;
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				
				
				String query = "SELECT * FROM operator_psp WHERE OperatorId='"+operatorId+"' and psp_name='Prepaid Cards' and isActive='1'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					String query1 = "SELECT * FROM machine_config WHERE OperatorId='"+operatorId+"' and telemetryId='"+telemetryId+"' and psp_name='Prepaid Cards' and isActive='1'";
					System.out.println("query.." + query1);

					Statement stmt1 = con.createStatement();

					ResultSet rs1 = stmt1.executeQuery(query1);
					if (rs1.next()) {
						status= true;
					} else{
						status = false;
					}
				} else{
					status = false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return status;
	}
	public static TransactionDetailsCard getTransactionByRefId(String orderId, String terminalId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
		TransactionDetailsCard transaction = new TransactionDetailsCard();
		transaction.setStatus("failure");
		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM payment_transactions WHERE order_id='"+orderId+"' and terminal_id='"+terminalId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					transaction.setWalletId(rs.getString("merchant_id"));
					transaction.setWalletAccountId(rs.getString("psp_merchantid"));
					if(CommonUtil.isNullOrEmpty(rs.getString("device_id"))){
						transaction.setOperatorId(0);
					} else{
						transaction.setOperatorId(Integer.parseInt(rs.getString("device_id")));
					}
					transaction.setCustomerId(rs.getString("customer_id"));
					transaction.setStatus("success");
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return transaction;
	}
	
	public static boolean isCardTransactionOpen(String terminalId, String orderId){
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		PspDetail pspDetail = new PspDetail();
		
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM payment_transactions WHERE order_id='"+orderId+"' and terminal_id='"+terminalId+"'";
				System.out.println("query.." + query);

				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					return true;
				} else{
					return false;
				}
				
				

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			dbConfig.closeConnection(con);
//			logger.info("db connection closed");
		}

		return false;
	}
	
	public static OperatorPspEntity getOperatorDetails(Integer operatorId, String pspId, String type){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+operatorId+"' and psp_id='"+pspId+"' and payment_type='"+type+"'";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					System.out.println("String setPaymentId.. "+ result.getInt("payment_id"));
					operatorEntity.setPaymentId(result.getInt("payment_id"));
					System.out.println("String payment_type.. "+ result.getString("payment_type"));
					operatorEntity.setPaymentType(result.getString("payment_type"));
					System.out.println("String wallet_type.. "+ result.getString("wallet_type"));
					operatorEntity.setWalletType(result.getString("wallet_type"));
					
					operatorEntity.setImagePath(result.getString("image_path"));
					operatorEntity.setPspName(result.getString("psp_name"));
					
					if(result.getString("customerId")==null){
						operatorEntity.setCustomerId("");
					} else{
						operatorEntity.setCustomerId(result.getString("customerId"));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return operatorEntity;
	}
	
	public static OperatorPspEntity getPspImage(Integer operatorId, String pspId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+operatorId+"' and psp_id='"+pspId+"' and image_path !=''";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					System.out.println("String setPaymentId.. "+ result.getInt("payment_id"));
					operatorEntity.setPaymentId(result.getInt("payment_id"));
					System.out.println("String payment_type.. "+ result.getString("payment_type"));
					operatorEntity.setPaymentType(result.getString("payment_type"));
					System.out.println("String wallet_type.. "+ result.getString("wallet_type"));
					operatorEntity.setWalletType(result.getString("wallet_type"));
					
					operatorEntity.setImagePath(result.getString("image_path"));
					operatorEntity.setPspName(result.getString("psp_name"));
					
					if(result.getString("customerId")==null){
						operatorEntity.setCustomerId("");
					} else{
						operatorEntity.setCustomerId(result.getString("customerId"));
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return operatorEntity;
	}
	public static String getSecretKey(String mid){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		String secretKey="";
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM merchant_secret_key where mid='"+mid+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					
					secretKey=result.getString("secret_key");
					
				}
				System.out.println("mid - secretKey :- "+mid+" - "+secretKey);
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return secretKey;
	}
	public static String getTransactionId(String orderId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		String transId="";
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_transactions where order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					transId=result.getString("psp_transaction_id");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return transId;
	}
	public static String getOrderIdByTxnId(String txnId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		String orderId="0";
		try {
			
			if (isConnected) {
				
				String sql = "SELECT order_id FROM payment_transactions where psp_transaction_id='"+txnId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					orderId=result.getString("order_id");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return orderId;
	}
	public static String getAmount(String orderId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		String amount="";
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_transactions where order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					amount=result.getString("auth_amount");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return amount;
	}
	
	public static Set<String> getTerminalByCustomer(int customerId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where customerId='"+customerId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalListUnique.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalListUnique;
	}
	
	public static Set<String> getTerminalByOperatorV2(String operatorId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where OperatorId='"+operatorId+"' and telemetryId like '000%'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalListUnique.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalListUnique;
	}
	
	public static Set<String> getTerminalByCustomerAndLoc(int customerId, int customerLocation){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where customerId='"+customerId+"' and customerLocationId='"+customerLocation+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalListUnique.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalListUnique;
	}
	public static RefundDbModel getRefundDetails(String orderId){

		RefundDbModel refundDbModel = new RefundDbModel();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_refund where merchant_order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					refundDbModel.setAmount(result.getString("amount"));
					refundDbModel.setRefundDate(result.getString("refund_date"));
					refundDbModel.setRefundStatus(result.getInt("refund_status"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return refundDbModel;
	}
	
	public static RefundDbModel getRefundTxnId(String orderId){

		RefundDbModel refundDbModel = new RefundDbModel();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_refund where merchant_order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					refundDbModel.setTxnGuid(result.getString("txn_guid"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return refundDbModel;
	}
	
	public static Set<String> getTerminalByCustomerAndLocAndMachine(int customerId, int customerLocation, String machineId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where customerId='"+customerId+"' and customerLocationId='"+customerLocation+"' and machineId='"+machineId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalListUnique.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalListUnique;
	}
	public static Set<String> getTerminalByCustomerLocAndMachine(int customerLocation, String machineId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalListUnique = new HashSet<String>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where customerLocationId='"+customerLocation+"' and machineId='"+machineId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalListUnique.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalListUnique;
	}
	public static Set<String> getTerminalByCustomerLocation(int customerLocationId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalList= new HashSet<>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where customerLocationId='"+customerLocationId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalList.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalList;
	}
	public static Set<String> getTerminalByMachineId(String machineId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		Set<String> terminalList= new HashSet<>();
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM machine_config where machineId='"+machineId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){ 
					terminalList.add(result.getString("telemetryId"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return terminalList;
	}
	public static String getAppId(String orderId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		OperatorPspEntity operatorEntity = new OperatorPspEntity();

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		String appId="";
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_refund where merchant_order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					appId=result.getString("app_id");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return appId;
	}
	
	public static boolean getIsStatic(int customerLocation, String pspId, int isStatic){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM customerlocation_psp where customerLocationId="+customerLocation+" and psp_id="+pspId+" and is_static="+isStatic+"";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}
	
	public static int getIsStaticNo(int customerLocation, String pspId){

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		int isStatic=0;
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM customerlocation_psp where customerLocationId="+customerLocation+" and psp_id="+pspId+"";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					isStatic = result.getInt("is_static");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return isStatic;
	}
	
	public static PaymentInitiation GetQRDetailsFromDB(String orderId){

		PaymentInitiation paymentInitiation = new PaymentInitiation();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get PaymentInitiation method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_initiation where merchant_order_id='"+orderId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()){ 
					paymentInitiation.setTerminalId(result.getString("terminal_id"));
					paymentInitiation.setAppId(result.getString("app_id"));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return paymentInitiation;
	}

	public static boolean sendRefundStatus(List<RefundSatusModel> listStatus){
		try{
			System.out.println("sendRefundStatus is calling.."+listStatus);
      	  String line=""; 
      	  StringBuilder response=new StringBuilder();
      	  
//      	  URL url = new URL("http://vendiman.limitlessmobil.com:802/UVMGateWay/api/Order/ChangePaymentStatus");
      	  URL url = new URL("http://45.116.117.110:5559/MySqlGatewayStaging/api/Order/ChangePaymentStatus");
//      	String apiURL=Util.apiUrlReader();
//		URL url = new URL(apiURL+"api/Order/ChangePaymentStatus");
//		System.out.println("url "+url);
      	
      	
    		  HttpURLConnection connection = null;
    		  connection = (HttpURLConnection)url.openConnection();
    		  connection.setRequestMethod("PUT");
    		  connection.setRequestProperty("Content-Type","application/json");
    		  connection.setRequestProperty("Content-Length", Integer.toString(listStatus.toString().getBytes().length));
    		  connection.setUseCaches(false);
    		  connection.setDoOutput(true);
           try (DataOutputStream wr = new DataOutputStream (
        		      connection.getOutputStream())) {
        		  wr.writeBytes(listStatus.toString());
        		}
           int responseCode = connection.getResponseCode();
            InputStream is;
            if(responseCode == HttpURLConnection.HTTP_OK){
            is = connection.getInputStream();
            }else {
            is = connection.getErrorStream();
            }
                  BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                  line="";
                  while((line = rd.readLine()) != null) {
                  //System.out.append(line);
                  //System.out.println("line.."+line);
                  response.append(line);
                  
                  System.out.println("Status Update RESPONSE... "+response.toString());
                  System.out.append("\r");

                  }
                  rd.close(); is.close();
        } catch(Exception e){
      	 System.out.println("resfund status report api "+e);
        } 
		
		return false;
	}
	
	public static String voidTransactionUpdate(String terminalId, String refNo){
		try{
			DbConfigration dbConfig = new DbConfigrationImpl();
			System.out.println("Get getOperatorPspList DAO method is calling!!");
			boolean isConnected = true;
			OperatorPspEntity operatorEntity = new OperatorPspEntity();

			Connection conection = dbConfig.getCon();
			if (conection == null) {
				isConnected = false;
			}
			String transId="";
			try {
				
				if (isConnected) {
					
					String sql = "SELECT * FROM payment_transactions where terminal_id='"+terminalId+"' and order_id='"+refNo+"'";
					Statement statement = conection.createStatement();
					ResultSet result = statement.executeQuery(sql);
					if (result.next()){ 
						String status=result.getString("status");
//						transId=result.getString("psp_transaction_id");
						if(status.equalsIgnoreCase("Pending")){
							String sqlUpdate = "UPDATE payment_transactions " +
					                   "SET status = 'close' WHERE terminal_id='"+terminalId+"' and order_id='"+refNo+"'";
							System.out.println("UPDATE oid Transaction  "+ sqlUpdate);
					        int i = statement.executeUpdate(sqlUpdate);
					        return "True";
						} else if(status.equalsIgnoreCase("close")){
							return "exist";
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Exception is.."+e);
			}finally{
				dbConfig.closeConnection(conection);
			}
		}catch(Exception e){
			System.out.println(e);
		} 
		return "No";
	}

	public static void main(String[] args) {
		RefundSatusModel r = new RefundSatusModel();
		r.setAppId("isudbgc");
		r.setPaymentRefNo("ksdbc");
		r.setStatusId(1);
		List<RefundSatusModel> listStatus = new ArrayList<RefundSatusModel>();
		listStatus.add(r);
		sendRefundStatus(listStatus);
//		System.out.println(getAppId("be5fcf11818d42e9"));
    }
}
