package com.limitlessmobility.iVendGateway.dao.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.common.ConfigDetail;
import com.limitlessmobility.iVendGateway.model.common.OperatorDetail;
import com.limitlessmobility.iVendGateway.model.common.RefundDbModel;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;


public class CommonCredentialDaoImpl implements CommonCredentialDao{
	
	
	@Override
	public OperatorDetail getPspConfigDetail(int operatorId,String pspId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		OperatorDetail operatorDetail=new OperatorDetail();

		Connection connection = dbConfig.getCon();
		if (connection == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");
				String qry = "";
				if(pspId.equalsIgnoreCase("zeta") || pspId.equalsIgnoreCase("zeta")){
					qry = "and payment_type='Static'";
				} else{
					qry = "and payment_type='Dynamic'";
				}

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"'  "+qry;
				System.out.println("query.." + query);

				Statement stmt = connection.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					operatorDetail.setOperatorId(rs.getInt("operatorId"));
					operatorDetail.setMerchantId(rs.getString("psp_merchant_id"));
					operatorDetail.setMerchantKey(rs.getString("psp_merchant_key"));
					operatorDetail.setPspMguid(rs.getString("psp_mguid"));
					operatorDetail.setPspId(rs.getString("psp_id"));
					try{
						if(pspId.equalsIgnoreCase("AmazonPay") || pspId.equalsIgnoreCase("AmazonPay")){
							operatorDetail.setStoreId(rs.getString("store_id"));
						}
						
					} catch(Exception e){}
					try {stmt.close();rs.close();}catch(Exception e) {}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			try{dbConfig.closeConnection(connection);}catch(Exception e) {}
//			logger.info("db connection closed");
		}

		return operatorDetail;
	}
	
	@Override
		public ConfigDetail getConfigDetail(String telemetryId, String pspId) {
		  boolean isConnected = true;
		  
		  DbConfigration dbConfig = new DbConfigrationImpl();
			ConfigDetail detail=new ConfigDetail();
			Connection connnn = dbConfig.getCon();
			if (connnn == null) {
				isConnected = false;
			}
	        
			
			try {
				if (isConnected) {
//					logger.info("Connection Object Created...");

					String query = " SELECT OperatorId,is_static,psp_qrcode FROM machine_config WHERE telemetryId='"+telemetryId+"' and psp_id= '"+pspId+"'";
					System.out.println("query.." + query);
	                
					java.sql.Statement stmt = connnn.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					while (rs.next()) {
	                   
						detail.setIsStatic(Integer.parseInt(rs.getString("is_static")));
						System.out.println("is_static "+detail.getIsStatic());
						detail.setOperatorId(Integer.parseInt(rs.getString("OperatorId")));
						System.out.println("OperatorId "+detail.getOperatorId());
						detail.setPsp_qrcode((rs.getString("psp_qrcode")));
						System.out.println("Psp_qrcode "+detail.getPsp_qrcode());
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.error(e);
			} finally {
				try{dbConfig.closeConnection(connnn);}catch(Exception e) {}
//				logger.info("db connection closed");
			}

			return detail;
		}

	@Override
	public int getOperatorId(String telemetryId, String pspId) {
		  boolean isConnected = true;
		  int operatorId = 0;
		  DbConfigration dbConfig = new DbConfigrationImpl();
			Connection con = dbConfig.getCon();
			if (con == null) {
				isConnected = false;
			}
	        
			
			try {
				if (isConnected) {
//					logger.info("Connection Object Created...");

					String query = "SELECT * FROM machine_config WHERE telemetryId='"+telemetryId+"' and psp_id= '"+pspId+"'";
					System.out.println("query.." + query);
	                
					java.sql.Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()) {
	                   operatorId = rs.getInt("OperatorId");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.error(e);
			} finally {
				try{dbConfig.closeConnection(con);}catch(Exception e) {}
//				logger.info("db connection closed");
			}

			return operatorId;
		}

	@Override
    public OperatorDetail getPspConfigDetailForWallet(int operatorId,
            String pspId) {
		boolean isConnected = true;
		OperatorDetail operatorDetail=new OperatorDetail();
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection connectionn = dbConfig.getCon();
		if (connectionn == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"' and payment_type='Dynamic'";
				System.out.println("query.." + query);

				Statement stmt = connectionn.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					operatorDetail.setOperatorId(rs.getInt("operatorId"));
					operatorDetail.setMerchantId(rs.getString("psp_merchant_id"));
					operatorDetail.setMerchantKey(rs.getString("psp_merchant_key"));
					operatorDetail.setPspMguid(rs.getString("psp_mguid"));
					operatorDetail.setPspId(rs.getString("psp_id"));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			try{dbConfig.closeConnection(connectionn);}catch(Exception e) {}
//			logger.info("db connection closed");
		}

		return operatorDetail;
	}

	@Override
	public String getStoreId(String telemetryId, String pspId) {
		  boolean isConnected = true;
		  String operatorId = "0";
		  DbConfigration dbConfig = new DbConfigrationImpl();
		  PspMerchant PspMerchantdetail = new PspMerchant();
			Connection con = dbConfig.getCon();
			if (con == null) {
				isConnected = false;
			}
	        
			
			try {
				if (isConnected) {
//					logger.info("Connection Object Created...");

					String query = "SELECT psp_mid FROM machine_config WHERE telemetryId='"+telemetryId+"' and psp_id= '"+pspId+"'";
					System.out.println("query.." + query);
	                
					java.sql.Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()) {
	                   operatorId = rs.getString("psp_mid");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.error(e);
			} finally {
				try{dbConfig.closeConnection(con);}catch(Exception e) {}
//				logger.info("db connection closed");
			}

			return operatorId;
		}

	@Override
	public RefundDbModel getRefundDetailsByOrderId(String orderId) {
		RefundDbModel refundDbModel = new RefundDbModel();/*
		  boolean isConnected = true;
		  DbConfigration dbConfig = new DbConfigrationImpl();
		  PspMerchant PspMerchantdetail = new PspMerchant();
			Connection con = dbConfig.getCon();
			if (con == null) {
				isConnected = false;
			}
	        
			
			try {
				if (isConnected) {
//					logger.info("Connection Object Created...");

					String query = "SELECT * FROM payment_refund WHERE merchant_order_id='"+orderId+"'";
					System.out.println("query.." + query);
	                
					java.sql.Statement stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()) {
						refundDbModel.set
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
//				logger.error(e);
			} finally {
				try{dbConfig.closeConnection(con);}catch(Exception e) {}
//				logger.info("db connection closed");
			}
*/
			return refundDbModel;
		}

	@Override
	public OperatorDetail getPspConfigPhonepeStatic(int operatorId,
	        String pspId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean isConnected = true;
		OperatorDetail operatorDetail=new OperatorDetail();

		Connection connection = dbConfig.getCon();
		if (connection == null) {
			isConnected = false;
		}

		try {

			if (isConnected) {
//				logger.info("Connection Object Created...");
				String qry = "";
					qry = "and payment_type='Static'";

				String query = "SELECT * FROM operator_psp WHERE operatorId='"+operatorId+"' and psp_id= '"+pspId+"'  "+qry;
				System.out.println("query.." + query);

				Statement stmt = connection.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					operatorDetail.setOperatorId(rs.getInt("operatorId"));
					operatorDetail.setMerchantId(rs.getString("psp_merchant_id"));
					operatorDetail.setMerchantKey(rs.getString("psp_merchant_key"));
					operatorDetail.setPspMguid(rs.getString("psp_mguid"));
					operatorDetail.setPspId(rs.getString("psp_id"));
					try{
						if(pspId.equalsIgnoreCase("AmazonPay") || pspId.equalsIgnoreCase("AmazonPay")){
							operatorDetail.setStoreId(rs.getString("store_id"));
						}
						
					} catch(Exception e){}
					try {stmt.close();rs.close();}catch(Exception e) {}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(e);
		} finally {
			try{dbConfig.closeConnection(connection);}catch(Exception e) {}
//			logger.info("db connection closed");
		}

		return operatorDetail;
	}


}
