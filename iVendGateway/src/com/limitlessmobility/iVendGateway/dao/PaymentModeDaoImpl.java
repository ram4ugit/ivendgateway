package com.limitlessmobility.iVendGateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.limitlessmobil.ivendgateway.util.MySqlDate;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.psp.model.PSPModel;

public class PaymentModeDaoImpl implements PaymentModeDao{

	@Override
	public List<PaymentModeModel> getPaymentModeList() {

		List<PaymentModeModel> paymentModeList = new ArrayList<PaymentModeModel>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getPaymentModeList method is calling!!");
//		log.info("Form is going to Save Transaction Details");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM payment_mode where status='1'";
				 
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				int count = 0;
				 
				while (result.next()){
					
					PaymentModeModel paymentMode = new PaymentModeModel();
					paymentMode.setPaymentModeId(result.getString(1));
					paymentMode.setPaymentModelName(result.getString(2));
					paymentMode.setIsChild(result.getString("is_child"));
					paymentMode.setType(Integer.parseInt(result.getString("type")));
					paymentMode.setIsStatic(Integer.parseInt(result.getString("is_static")));
					
					boolean isEmpty = result.getString("refund_msg") == null || result.getString("refund_msg").trim().length() == 0;
					if (isEmpty) {
						paymentMode.setMessage("null");
					} else {
						paymentMode.setMessage(result.getString("refund_msg"));
					}
					
					
					int isStatic = paymentMode.getIsStatic();
					if(isStatic==1){
						String paymentModeName = paymentMode.getPaymentModelName();
						if(paymentModeName.equalsIgnoreCase("Zeta Meal Card")){
							paymentModeName="zeta";
						}
						String sqlQr = "SELECT * FROM terminal_psp where payment_id='"+paymentModeName+"'";
						 
						Statement statementQr = conn.createStatement();
						ResultSet resultQr = statementQr.executeQuery(sqlQr);
						if(resultQr.next()){
							paymentMode.setQrCode(resultQr.getString("psp_qrcode"));
						}
					}
					
				    paymentModeList.add(paymentMode);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return paymentModeList;
	}

	@Override
	public List<PaymentModeModel> getPSPList(PaymentModeModel paymentMode) {

		List<PaymentModeModel> paymentModeList = new ArrayList<PaymentModeModel>();
//		List<PspDetailModal> pspDetailList = new ArrayList<PspDetailModal>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getPSPList method is calling!!");

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM terminal_psp where terminal_id='"+paymentMode.getTerminalId()+"' and status='1'";
				 System.out.println(sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				if(!result.isBeforeFirst())
				{
					sql = "SELECT * FROM terminal_psp where payment_id = 'paytm' LIMIT 1";
					 System.out.println(sql);
					statement = conn.createStatement();
					result = statement.executeQuery(sql);
					
				}
				int count = 0;
				 
				while (result.next()){
					paymentMode=new PaymentModeModel();
//					PaymentModeModel paymentMode = new PaymentModeModel();
					paymentMode.setPaymentModeId(result.getString(1));
					paymentMode.setPaymentModelName(result.getString("payment_id"));
					paymentMode.setIsChild(result.getString("is_child"));
					paymentMode.setType(Integer.parseInt(result.getString("type")));
					paymentMode.setIsStatic(Integer.parseInt(result.getString("is_static")));
					paymentMode.setKeyboardType(result.getString("keyboard_type"));
					
					paymentMode.setProductSelectionTime(result.getInt("product_selection_time"));
					paymentMode.setPaymentProcessTime(result.getInt("payment_process_time"));
					paymentMode.setVendingTime(result.getInt("vending_time"));
					paymentMode.setWalletAlertTime(result.getInt("wallet_alert_time"));
					paymentMode.setPspListRefreshTime(result.getInt("psplist_refresh_time"));
					paymentMode.setCheckStatusTime(result.getInt("check_status_time"));
					
					boolean ismsg = result.getString("message1") == null || result.getString("message1").trim().length() == 0;
					if (ismsg) {
						paymentMode.setMsg1("null");
					} else {
						paymentMode.setMsg1(result.getString("message1"));
					}
					
					boolean ismsg2 = result.getString("message2") == null || result.getString("message2").trim().length() == 0;
					if (ismsg2) {
						paymentMode.setMsg2("null");
					} else {
						paymentMode.setMsg2(result.getString("message2"));
					}
					
					boolean ismsg3 = result.getString("message3") == null || result.getString("message3").trim().length() == 0;
					if (ismsg3) {
						paymentMode.setMsg3("null");
					} else {
						paymentMode.setMsg3(result.getString("message3"));
					}
					
					
					paymentMode.setTransactionPostTime(result.getInt("transaction_post_time"));
					paymentMode.setImage(result.getBoolean("isImage"));
					
					boolean dnt = result.getString("DateTime") == null || result.getString("DateTime").trim().length() == 0;
					if (dnt) {
						paymentMode.setDateTime("null");
					} else {
						paymentMode.setDateTime(result.getString("DateTime"));
					}
					
					boolean isEmpty = result.getString("refund_msg") == null || result.getString("refund_msg").trim().length() == 0;
					if (isEmpty) {
						paymentMode.setMessage("null");
					} else {
						paymentMode.setMessage(result.getString("refund_msg"));
					}
					
					
					
					int isStatic = paymentMode.getIsStatic();
					if(isStatic==1){
						
							paymentMode.setQrCode(result.getString("psp_qrcode"));
						
					}
					
				    paymentModeList.add(paymentMode);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
        
		return paymentModeList;
	}
	/*
	 * This is old backup
	 * @Override
	public List<PspDetailModal> getPSPList(PaymentModeModel paymentMode) {

		List<PspDetailModal> pspDetailList = new ArrayList<PspDetailModal>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getPSPList method is calling!!");

		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM psp_details where payment_mode_id='"+paymentMode.getPaymentModeId()+"'";
				 System.out.println(sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				int count = 0;
				 
				while (result.next()){
					
					PspDetailModal pspDetail = new PspDetailModal();
					pspDetail.setId(result.getString(1));
					pspDetail.setPspName(result.getString(2));
					
					
					String sqlTerminal = "SELECT * FROM terminal_psp where payment_id='"+pspDetail.getPspName()+"' and is_static!='1'";
					 System.out.println(sqlTerminal);
					Statement statementTerminal = conn.createStatement();
					ResultSet resultTerminal = statementTerminal.executeQuery(sqlTerminal);
					 
					int countTerminal = 0;
					pspDetail.setIsStatic("0");
					pspDetail.setQrCode("null");
					while (resultTerminal.next()){
						pspDetail.setIsStatic(resultTerminal.getString("is_static"));
						pspDetail.setQrCode(resultTerminal.getString("psp_qrcode"));
						pspDetailList.add(pspDetail);
					}
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return pspDetailList;
	}*/

	@Override
	public PspDetailModal getpspDetailByPspId(String pspId) {

		PspDetailModal pspDetailModal = new PspDetailModal();
		boolean isConnected = true;
		DbConfigration dbConfig = new DbConfigrationImpl();
		Connection con = dbConfig.getCon();
		if (con == null) {
			isConnected = false;
		}
		try {

			if (isConnected) {
				String query = "SELECT * FROM psp_details p where psp_id='"+pspId+"'";
				System.out.println("query.." + query);

				java.sql.Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					
					pspDetailModal.setPspApiUrl(rs.getString("psp_api_url"));
				} else{
					String queryWallet = "SELECT * FROM psp_details p where psp_id='wallet'";
					System.out.println("query.." + query);

					java.sql.Statement stmtWallet = con.createStatement();

					ResultSet rsWallet = stmtWallet.executeQuery(queryWallet);
					
					if (rsWallet.next()) {
						
						pspDetailModal.setPspApiUrl(rsWallet.getString("psp_api_url"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbConfig.closeConnection(con);
		}

		return pspDetailModal;
	}
}
