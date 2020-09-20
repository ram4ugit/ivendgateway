package com.limitlessmobility.iVendGateway.dao.psp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonDaoImpl;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.model.paymentmode.GetMachinePspResponse;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigRequest;
import com.limitlessmobility.iVendGateway.psp.model.PspListModel;

public class MachineConfigDaoImpl implements MachineConfigDao{
	
	CommonDaoImpl commonDao = new CommonDaoImpl();
	
	@Override
	public boolean saveMachineConfig(MachineConfigRequest machineConfigRequest, Integer opertaorId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get saveMachineConfig DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		int status=0;
		try {
			if (isConnected) {
				
				List<MachineConfigPaymentMode> paymentModeList = machineConfigRequest.getPaymentMode();
				
				Iterator<MachineConfigPaymentMode> itr = paymentModeList.iterator();
				while(itr.hasNext()){
				
				MachineConfigPaymentMode paymentMode = itr.next();
				
				
				
				
				PreparedStatement psExist = conn.prepareStatement("select count(*) from machine_config where OperatorId='"+opertaorId+"' "
								+ " and machineId='"+machineConfigRequest.getMachineId()+"'"
								+ " and is_static='"+paymentMode.getIsStatic()+"'"
								+ " and psp_id='"+paymentMode.getPspId()+"'");
				ResultSet rs = psExist.executeQuery();
				int n = 0;
				
				if ( rs.next() ) {
					System.out.println(rs.getInt(1));
				    n = rs.getInt(1);
				}
				if ( n > 0 ) {
				   // row exists
//					boolean isSelected = paymentMode.isSelected();
					/*int isActive = 0; 
					if(isSelected){
						isActive=1;
					}*/
					StringBuilder updateMachineSQLTest = new StringBuilder();
					updateMachineSQLTest.append("UPDATE machine_config"
							+ " SET sequence='"+paymentMode.getSequence()+"', psp_mid = '"+paymentMode.getPspTid()+"', isActive='"+paymentMode.getIsActive()+"'");
					if(machineConfigRequest.getTelemetryId().isEmpty() || machineConfigRequest.getTelemetryId()==null) {} else {
						updateMachineSQLTest.append(", telemetryId = '"+machineConfigRequest.getTelemetryId()+"'");
					}
					if(machineConfigRequest.getCustomerId().isEmpty() || machineConfigRequest.getCustomerId()==null) {} else {
						updateMachineSQLTest.append(", customerId = '"+machineConfigRequest.getCustomerId()+"'");
					}
					if(machineConfigRequest.getCustomerLocationId()>0) {
						updateMachineSQLTest.append(", customerLocationId = '"+machineConfigRequest.getCustomerLocationId()+"'");
					}
					if(paymentMode.getPspTid()==null) {
						
					} else {
					if(paymentMode.getPspTid().isEmpty() || paymentMode.getPspTid()==null) {} else {
						updateMachineSQLTest.append(", psp_tid = '"+paymentMode.getPspTid()+"'");
					}
					}
					if(paymentMode.getPspQrcode().isEmpty() || paymentMode.getPspQrcode()==null) {} else {
						updateMachineSQLTest.append(", psp_qrcode = '"+paymentMode.getPspQrcode()+"'");
					}
					
					updateMachineSQLTest.append(" where OperatorId='"+opertaorId+"' "
								+ " and machineId='"+machineConfigRequest.getMachineId()+"'"
								+ " and is_static='"+paymentMode.getIsStatic()+"'"
								+ " and psp_id='"+paymentMode.getPspId()+"'");
					
					System.out.println(updateMachineSQLTest);
					String updateMachineSQLL = "UPDATE machine_config"
							+ " SET sequence='"+paymentMode.getSequence()+"', psp_tid = '"+paymentMode.getPspTid()+"', psp_mid = '"+paymentMode.getPspTid()+"', telemetryId='"+machineConfigRequest.getTelemetryId()+"', isActive='"+paymentMode.getIsActive()+"', psp_qrcode='"+paymentMode.getPspQrcode()+"'"
							+ " where OperatorId='"+opertaorId+"' "
								+ " and machineId='"+machineConfigRequest.getMachineId()+"'"
								+ " and is_static='"+paymentMode.getIsStatic()+"'"
								+ " and psp_id='"+paymentMode.getPspId()+"'";
					
					Statement statement = conn.createStatement();

//					System.out.println(updateMachineSQL);

					// execute update SQL stetement
					statement.execute(updateMachineSQLTest.toString());

					System.out.println("Record is updated to machine_config table!");
					
					status=1;
					
					
				} else {
				System.out.println(paymentMode.getPspId()+" - "+paymentMode.getIsActive());
				if(paymentMode.getIsActive()==0) {
					
				} else {
				
				PreparedStatement ps = conn.prepareStatement("INSERT INTO machine_config VALUES (NULL, ?, ?, ?, ?, ? ,? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		        ps.setInt(1, machineConfigRequest.getCustomerLocationId());
		        ps.setInt(2, opertaorId);
		        ps.setString(3, machineConfigRequest.getCustomerId());
		        ps.setString(4, paymentMode.getPspId());
		        
		       /* boolean isActive = paymentMode.isSelected();
		        if(isActive){
		        	ps.setInt(5, 1);
		        } else{*/
		        	ps.setInt(5, 1);
//		        }
		        ps.setInt(6, machineConfigRequest.getOperationLocationId());
		        ps.setInt(7, machineConfigRequest.getMachineId());
		        ps.setString(8, machineConfigRequest.getTelemetryId());
		        ps.setString(9, machineConfigRequest.getVposId());
		        ps.setString(10, machineConfigRequest.getCashboxId());
		        ps.setString(11, paymentMode.getPspTid());
		        
		        if(paymentMode.getPspId().trim().equalsIgnoreCase("cards") || paymentMode.getPspId().trim().equalsIgnoreCase("Prepaid Cards") || paymentMode.getPspId().trim().equalsIgnoreCase("Credit/Debit Cards")){
		        	ps.setString(12, "200");
		        } else{
		        	ps.setString(12, "100");
		        }
		        
		        System.out.println("Save PSP isStatic : "+paymentMode.getPspId()+"  :"+paymentMode.getIsStatic());
		        if(CommonService.getIsStatic(machineConfigRequest.getCustomerLocationId(), paymentMode.getPspId(), paymentMode.getIsStatic())) {
		        	ps.setInt(13, paymentMode.getIsStatic());
		        } else {
		        	ps.setInt(13, CommonService.getIsStaticNo(machineConfigRequest.getCustomerLocationId(), paymentMode.getPspId()));
		        }
		        ps.setString(14, paymentMode.getPspQrcode());
		        ps.setString(15, paymentMode.getRefundMsg());
		        ps.setString(16, machineConfigRequest.getKeyboardType());
		        ps.setString(17, paymentMode.getPspName());
		        
		        if(paymentMode.getPspId().trim().equalsIgnoreCase("188")){
		        	ps.setString(17, "Vendiman Wallet");
		        }else if(paymentMode.getPspId().trim().equalsIgnoreCase("Cards")){
		        	ps.setString(17, "Credit/Debit Cards");
		        }
		        
		        ps.setString(18, paymentMode.getPspTid());
		        ps.setInt(19, paymentMode.getSequence());
		         status = ps.executeUpdate();
				}
				}
				}
				/*Commented because not use of below code*/
		      /*if(status == 1) {
		    	  String updateMachine = "UPDATE machine_config set isActive='0' where "
							+ "(machineId='"+machineConfigRequest.getMachineId()+"' and customerLocationId != '"+machineConfigRequest.getCustomerLocationId()+"')  "
									+ " OR (machineId='"+machineConfigRequest.getMachineId()+"' and OperatorId != '"+opertaorId+"')  "
									+ " OR (machineId='"+machineConfigRequest.getMachineId()+"' and operationLocationId != '"+machineConfigRequest.getOperationLocationId()+"')";
					
					Statement st = conn.createStatement();
					System.out.println(updateMachine);
					st.execute(updateMachine);
		        return true;
		      }*/
				if(status == 1) {
					return true;
			      }
			}
		} catch (Exception e) {
			System.out.println("Exception in saveMachineConfig.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}

	@Override
	public List<MachineConfigEntity> getMachineConfig(
			MachineConfigEntity machineConfigEntity) {

		List<MachineConfigEntity> machineConfigList = new ArrayList<MachineConfigEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getMachineConfigEntity DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String qry="";
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getCustomerId())) {
					
				} else{
					
					qry += " and customerId='"+machineConfigEntity.getCustomerId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getCustomerLocationId()) || machineConfigEntity.getCustomerLocationId()==0) {
					
				} else{
					
					qry += " and customerLocationId='"+machineConfigEntity.getCustomerLocationId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getMachineId()) || machineConfigEntity.getMachineId()==0) {
					
				} else{
					
					qry += " and machineId='"+machineConfigEntity.getMachineId()+"'";
				}
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getTelemetryId())) {
					
				} else{
					
					qry += " and telemetryId='"+machineConfigEntity.getTelemetryId()+"'";
				}
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getPspId())) {
					
				} else{
					
					qry += " and psp_id='"+machineConfigEntity.getPspId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getOperationLocationId())) {
					
				} else{
					
					qry += " and operationLocationId='"+machineConfigEntity.getOperationLocationId()+"'";
				}
				if(CommonValidationUtility.isEmpty(machineConfigEntity.getIsActive())) {
					
				} else{
					
					qry += " and isActive='"+machineConfigEntity.getIsActive()+"'";
				}
				
				String sql = "SELECT * FROM machine_config where OperatorId='"+machineConfigEntity.getOperatorId()+"'"+qry;
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					MachineConfigEntity machineConfig = new MachineConfigEntity();
					machineConfig.setCustomerLocationId(result.getInt("customerLocationId"));
					machineConfig.setOperatorId(result.getInt("operatorId"));
					machineConfig.setCustomerId(result.getString("customerId"));
					machineConfig.setPspId(result.getString("psp_id"));
					machineConfig.setIsActive(result.getString("isActive"));
					machineConfig.setOperationLocationId(result.getInt("operationLocationId"));
					machineConfig.setMachineId(result.getInt("machineId"));
					machineConfig.setTelemetryId(result.getString("telemetryId"));
					machineConfig.setVposId(result.getString("vposId"));
					machineConfig.setCashboxId(result.getString("cashboxId"));
					machineConfig.setPspTid(result.getString("psp_tid"));
					machineConfig.setType(result.getString("type"));
					machineConfig.setIsStatic(result.getInt("is_static"));
					machineConfig.setPspQrcode(result.getString("psp_qrcode"));
					machineConfig.setRefundMsg(result.getString("refund_msg"));
					machineConfig.setKeyboardType(result.getString("keyboard_type"));
					machineConfig.setPspName(result.getString("psp_name"));
					
					machineConfigList.add(machineConfig);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return machineConfigList;
	}

	@Override
	public List<PspListModel> getMachinePspList(String telimetryId, Integer operatorId) {

		List<PspListModel> psplist = new ArrayList<PspListModel>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getMachinePspList DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String qry="";
				if(CommonValidationUtility.isEmpty(telimetryId)) {
					
				} else{
					
					qry += " and telemetryId='"+telimetryId+"'";
				}
				
				String sql = "SELECT * FROM machine_config where OperatorId='"+operatorId+"'"+qry;
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					PspListModel pspListModel = new PspListModel();
					pspListModel.setPspId(result.getString("psp_id"));
					pspListModel.setIsStatic(result.getInt("is_static"));
					pspListModel.setPspQrcode(result.getString("psp_qrcode"));
					pspListModel.setRefundMsg(result.getString("refund_msg"));
					pspListModel.setKeyboardType(result.getString("keyboard_type"));
					
						psplist.add(pspListModel);
					
			}
				}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return psplist;
	}

	@Override
    public List<GetMachinePspResponse> getMachinePspListNew(String telimetryId,
            Integer operatorId, Integer machineId) {

		List<GetMachinePspResponse> paymentModeList = new ArrayList<GetMachinePspResponse>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getMachinePspList DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String qry="";
				if(CommonValidationUtility.isEmpty(telimetryId)) {
					
				} else{
					
					qry += " and telemetryId='"+telimetryId+"'";
				}
				
				String sql = "SELECT * FROM machine_config where OperatorId='"+operatorId+"' and machineId="+machineId+" and isActive=1 "+qry+" order by sequence, psp_name";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				boolean isSequenceConfigure=false;
				while (result.next()){
					if(result.getInt("sequence")>0) {
						isSequenceConfigure=true;
					}
					GetMachinePspResponse paymentMode = new GetMachinePspResponse();
					paymentMode.setPaymentModelName(result.getString("psp_id").toLowerCase());
					boolean isEmptyType = result.getString("type") == null || result.getString("type").trim().length() == 0;
					if (isEmptyType) {
						paymentMode.setType(0);
					} else {
						paymentMode.setType(Integer.parseInt(result.getString("type")));
					}
					paymentMode.setIsStatic(Integer.parseInt(result.getString("is_static")));
					paymentMode.setKeyboardType(result.getString("keyboard_type"));
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
					
					paymentMode.setPspName(result.getString("psp_name"));
					
					String pspId =result.getString("psp_id");
					int operatorIdd = result.getInt("OperatorId");
					
					try{
						String sql1 = "SELECT * FROM operator_psp where psp_id='"+pspId+"' and OperatorId='"+operatorIdd+"'";
						Statement statement1 = conn.createStatement();
						ResultSet result1 = statement1.executeQuery(sql1);
						if (result1.next()){
							paymentMode.setImagePath(result1.getString("image_path"));
						} else{
							paymentMode.setImagePath("");
						}
					} catch(Exception e){
						
					}
					
					paymentModeList.add(paymentMode);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return paymentModeList;
	}

	@Override
	public boolean deleteMachineConfigBeforeInsert(
	        MachineConfigRequest machineConfigRequest, String opertaorId) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get saveMachineConfig DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				
				
				Statement stmt = conn.createStatement();
				// creating Query String
				String deleteQry = "DELETE FROM machine_config where machineId='"+machineConfigRequest.getMachineId()+"'";
				// Updating Table
				stmt.executeUpdate(deleteQry);
				
					return true;
			}
		} catch (Exception e) {
			System.out.println("Exception in deleteMachineConfig.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}


}
