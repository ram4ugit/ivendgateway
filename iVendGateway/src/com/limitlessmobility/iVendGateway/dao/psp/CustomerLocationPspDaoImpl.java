package com.limitlessmobility.iVendGateway.dao.psp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.dao.common.CommonService;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerlocationPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;

public class CustomerLocationPspDaoImpl implements CustomerLocationPspDao{

	@Override
	public boolean saveCustomerLocationPsp(CustomerlocationPspEntity customerLocationPsp) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get saveCustomerLocationPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
					String isExistCheckSql="Select * from customerlocation_psp WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
					Statement stmtisExistCheckSql = conn.createStatement();
					ResultSet resultisExistCheckSql = stmtisExistCheckSql.executeQuery(isExistCheckSql);
					if(resultisExistCheckSql.next()){
						
						String sqlUpdate = "UPDATE customerlocation_psp " +
				                   "SET sequence="+customerLocationPsp.getSequence()+", is_static="+customerLocationPsp.getIsStatic()+", isActive="+customerLocationPsp.getIsActive()+" "
				                   		+ "WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
						System.out.println("UPDATE customerlocation_psp.. "+ sqlUpdate);
				        int i = stmtisExistCheckSql.executeUpdate(sqlUpdate);
				        
				        if(customerLocationPsp.getIsActive().equalsIgnoreCase("1")){} else{
				        	String sqlUpdateMachinConfig = "UPDATE machine_config " +
				                   "SET is_static="+customerLocationPsp.getIsStatic()+", isActive="+customerLocationPsp.getIsActive()+" "
				                   		+ "WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
				        	System.out.println("UPDATE sqlUpdateMachinConfig.. "+ sqlUpdateMachinConfig);
				        	stmtisExistCheckSql.executeUpdate(sqlUpdateMachinConfig);
				        }
				        
						return true;
					} else{
						PreparedStatement ps = conn.prepareStatement("INSERT INTO customerlocation_psp VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				        ps.setInt(1, customerLocationPsp.getCustomerLocationId());
				        ps.setInt(2, customerLocationPsp.getOperatorId());
				        ps.setString(3, customerLocationPsp.getCustomerId());
				        ps.setString(4, customerLocationPsp.getPspId());
				        ps.setInt(5, 1);
				        ps.setInt(6, customerLocationPsp.getOperationLocationId());
				        ps.setInt(7, customerLocationPsp.getIsStatic());
				        ps.setString(8, customerLocationPsp.getPspName());
				        ps.setInt(9, customerLocationPsp.getSequence());
				        int i = ps.executeUpdate();
				      if(i == 1) {
				        return true;
				      }
			}
		} 
			}catch (Exception e) {
			System.out.println("Exception in saveCustomerLocationPsp Dao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}
	
	
	@Override
	public boolean updateCustomerLocationPsp(CustomerlocationPspEntity customerLocationPsp) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get updateCustomerLocationPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		Statement stmt=null;
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String isExistCheckSql="Select * from customerlocation_psp WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
				Statement stmtisExistCheckSql = conn.createStatement();
				ResultSet resultisExistCheckSql = stmtisExistCheckSql.executeQuery(isExistCheckSql);
				if(resultisExistCheckSql.next()){
				stmt = conn.createStatement();
				String sql = "UPDATE customerlocation_psp " +
		                   "SET is_static="+customerLocationPsp.getIsStatic()+", isActive="+customerLocationPsp.getIsActive()+" "
		                   		+ "WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
				System.out.println("UPDATE customerlocation_psp.. "+ sql);
		        int i = stmt.executeUpdate(sql);
		      
		        if(customerLocationPsp.getIsActive().equalsIgnoreCase("1")){} else{
		        	String sqlUpdateMachinConfig = "UPDATE machine_config " +
		                   "SET is_static="+customerLocationPsp.getIsStatic()+", isActive="+customerLocationPsp.getIsActive()+" "
		                   		+ "WHERE customerLocationId = '"+customerLocationPsp.getCustomerLocationId()+"' and customerId='"+customerLocationPsp.getCustomerId()+"' and OperatorId='"+customerLocationPsp.getOperatorId()+"' and psp_id='"+customerLocationPsp.getPspId()+"' and is_static='"+customerLocationPsp.getIsStatic()+"'";
		        	System.out.println("UPDATE sqlUpdateMachinConfig.. "+ sqlUpdateMachinConfig);
		        	stmt.executeUpdate(sqlUpdateMachinConfig);
		        }
		        return true;
		      } else{
		    	  PreparedStatement ps = conn.prepareStatement("INSERT INTO customerlocation_psp VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");
			        ps.setInt(1, customerLocationPsp.getCustomerLocationId());
			        ps.setInt(2, customerLocationPsp.getOperatorId());
			        ps.setString(3, customerLocationPsp.getCustomerId());
			        ps.setString(4, customerLocationPsp.getPspId());
			        ps.setInt(5, 1);
			        ps.setInt(6, customerLocationPsp.getOperationLocationId());
			        ps.setInt(7, customerLocationPsp.getIsStatic());
			        ps.setString(8, customerLocationPsp.getPspName());
			        int j = ps.executeUpdate();
			      if(j == 1) {
			        return true;
			      }
		      }
			}
		} catch (Exception e) {
			System.out.println("Exception in updateCustomerLocationPsp Dao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}

	@Override
	public List<CustomerlocationPspEntity> getCustomerLocationPsp(CustomerlocationPspEntity customerlocationPsp) {

		List<CustomerlocationPspEntity> customerPspModelList = new ArrayList<CustomerlocationPspEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String qry="";
				if(CommonValidationUtility.isEmpty(customerlocationPsp.getCustomerLocationId()) || customerlocationPsp.getCustomerLocationId()==0) {
					
				} else{
					
					qry += " and customerLocationId='"+customerlocationPsp.getCustomerLocationId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(customerlocationPsp.getCustomerId())) {
					
				} else{
					
					qry += " and customerId='"+customerlocationPsp.getCustomerId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(customerlocationPsp.getPspId())) {
					
				} else{
					
					qry += " and psp_id='"+customerlocationPsp.getPspId()+"'";
				}
				if(CommonValidationUtility.isEmpty(customerlocationPsp.getIsActive())) {
					
				} else{
					
					qry += " and isActive='"+customerlocationPsp.getIsActive()+"'";
//					qry += " and isActive='1'";
				}
				
				String sql = "SELECT * FROM customerlocation_psp where isActive='1' and OperatorId='"+customerlocationPsp.getOperatorId()+"'"+qry+"  order by sequence";
				
				System.out.println("GetCustomerLocation... "+sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					CustomerlocationPspEntity customerlocationPspEntity = new CustomerlocationPspEntity();
					customerlocationPspEntity.setCustomerLocationId(result.getInt("customerLocationId"));
					customerlocationPspEntity.setOperatorId(result.getInt("operatorId"));
					customerlocationPspEntity.setCustomerId(result.getString("customerId"));
					customerlocationPspEntity.setPspId(result.getString("psp_id"));
					customerlocationPspEntity.setIsStatic(result.getInt("is_static"));
					customerlocationPspEntity.setIsActive(result.getString("isActive"));
					customerlocationPspEntity.setOperationLocationId(result.getInt("operationLocationId"));
					customerlocationPspEntity.setPspName(result.getString("psp_name"));
					customerlocationPspEntity.setSequence(result.getInt("sequence"));
					customerPspModelList.add(customerlocationPspEntity);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return customerPspModelList;
	}

	/*@Override
	public List<MachineConfigPaymentMode> getCustLocationPsp(String customerLocationId, String machineId) {

		List<MachineConfigPaymentMode> machineConfigPaymentModeList = new ArrayList<MachineConfigPaymentMode>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getCustLocationPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				if(machineId.equalsIgnoreCase("0")){
				
				String sql = "SELECT * FROM customerlocation_psp where customerLocationId='"+customerLocationId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
					machineConfigPaymentMode.setPspId(result.getString("psp_id"));
					machineConfigPaymentModeList.add(machineConfigPaymentMode);
				}
				} else{
					String sql = "SELECT * FROM machine_config where customerLocationId='"+customerLocationId+"' and machineId='"+machineId+"'";
					Statement statement = conn.createStatement();
					ResultSet result = statement.executeQuery(sql);
					while (result.next()){
						MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
						machineConfigPaymentMode.setPspId(result.getString("psp_id"));
						machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
						machineConfigPaymentMode.setPspQrcode(result.getString("psp_qrcode"));
						machineConfigPaymentMode.setPspTid(result.getString("psp_tid"));
						machineConfigPaymentMode.setRefundMsg(result.getString("refund_msg"));
						machineConfigPaymentMode.setType(result.getString("type"));
						machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
						machineConfigPaymentModeList.add(machineConfigPaymentMode);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return machineConfigPaymentModeList;
	}*/
	
	@Override
	public List<MachineConfigPaymentMode> getCustLocationPspp(String customerId, String customerLocationId, String machineId, int operatorId) {

		List<MachineConfigPaymentMode> machineConfigPaymentModeList = new ArrayList<MachineConfigPaymentMode>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getCustLocationPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				if(machineId.equalsIgnoreCase("0")){
				
				String sql = "SELECT * FROM customerlocation_psp where customerLocationId='"+customerLocationId+"' and OperatorId='"+operatorId+"' and isActive=1  order by sequence";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if(!result.isBeforeFirst()){
//				    System.out.println("result set is empty");
					
					String sql1 = "SELECT * FROM customerlocation_psp where customerLocationId='"+customerLocationId+"' and OperatorId='"+operatorId+"' and isActive=1  order by sequence";
					Statement statement1 = conn.createStatement();
					ResultSet result1 = statement1.executeQuery(sql1);
					/*if(!result.isBeforeFirst()){
						String sql2 = "SELECT * FROM customer_psp where customerId='"+FROM REQUEST PARAMETER+"' and OperatorId='"+customerLocationId+"' GROUP BY psp_id";
						Statement statement1 = conn.createStatement();
						ResultSet result1 = statement1.executeQuery(sql1);
					}else{*/
					while (result1.next()){
						MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
						machineConfigPaymentMode.setPspId(result1.getString("psp_id"));
						machineConfigPaymentMode.setMethodType("new");
						machineConfigPaymentMode.setSelected(false);
						machineConfigPaymentMode.setIsActive(result1.getInt("isActive"));
						machineConfigPaymentMode.setPspName(result1.getString("psp_name"));
						machineConfigPaymentMode.setSequence(result1.getInt("sequence"));
						machineConfigPaymentMode.setWalletType(CommonService.getWalletType(operatorId, result1.getString("psp_id")));
						machineConfigPaymentModeList.add(machineConfigPaymentMode);
//					}
					}
				} else{
				while (result.next()){
					MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
					machineConfigPaymentMode.setPspId(result.getString("psp_id"));
					machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
					machineConfigPaymentMode.setPspName(result.getString("psp_name"));
					machineConfigPaymentMode.setIsActive(result.getInt("isActive"));
					machineConfigPaymentMode.setMethodType("exist");
					machineConfigPaymentMode.setSelected(false);
					machineConfigPaymentMode.setSequence(result.getInt("sequence"));
					machineConfigPaymentMode.setWalletType(CommonService.getWalletType(operatorId, result.getString("psp_id")));
					machineConfigPaymentModeList.add(machineConfigPaymentMode);
					
				}}
				} else{
					String sql = "SELECT * FROM machine_config where OperatorId="+operatorId+" and customerLocationId='"+customerLocationId+"' and customerId='"+customerId+"' and machineId='"+machineId+"' and isActive=1  order by sequence";
					Statement statement = conn.createStatement();
					ResultSet result = statement.executeQuery(sql);
					
					if(!result.isBeforeFirst()){
//					    System.out.println("result set is empty");
						
						String sql1 = "SELECT * FROM customerlocation_psp where customerLocationId='"+customerLocationId+"' and isActive=1";
						Statement statement1 = conn.createStatement();
						ResultSet result1 = statement1.executeQuery(sql1);
						/*if(!result.isBeforeFirst()){
							String sql2 = "SELECT * FROM customer_psp where customerId='"+FROM REQUEST PARAMETER+"' and OperatorId='"+customerLocationId+"' GROUP BY psp_id";
							Statement statement1 = conn.createStatement();
							ResultSet result1 = statement1.executeQuery(sql1);
						}else{*/
						while (result1.next()){
							MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
							machineConfigPaymentMode.setPspId(result1.getString("psp_id"));
							machineConfigPaymentMode.setMethodType("exist");
							if(result1.getInt("isActive")!=1) {
								machineConfigPaymentMode.setSelected(true);
							} else {
								machineConfigPaymentMode.setSelected(false);
							}
							
							machineConfigPaymentMode.setIsActive(result1.getInt("isActive"));
							machineConfigPaymentMode.setIsStatic(result1.getInt("is_static"));
							machineConfigPaymentMode.setPspName(result1.getString("psp_name"));
							machineConfigPaymentMode.setSequence(result1.getInt("sequence"));
							machineConfigPaymentMode.setWalletType(CommonService.getWalletType(operatorId, result1.getString("psp_id")));
							machineConfigPaymentModeList.add(machineConfigPaymentMode);
//						}
						}
					}
					else {
//					    System.out.println("Result set is not empty");
						StringBuilder pspIdList = new StringBuilder();
					    while (result.next()){
							MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
							
							pspIdList.append("'"+result.getString("psp_name")+"'"+",");
							
							machineConfigPaymentMode.setPspId(result.getString("psp_id"));
							machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
							machineConfigPaymentMode.setPspQrcode(result.getString("psp_qrcode"));
							machineConfigPaymentMode.setPspTid(result.getString("psp_tid"));
							machineConfigPaymentMode.setIsActive(result.getInt("isActive"));
							machineConfigPaymentMode.setRefundMsg(result.getString("refund_msg"));
							machineConfigPaymentMode.setType(result.getString("type"));
							machineConfigPaymentMode.setSequence(result.getInt("sequence"));
							machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
							machineConfigPaymentMode.setPspName(result.getString("psp_name"));
							machineConfigPaymentMode.setMethodType("exist");
							machineConfigPaymentMode.setSelected(true);
							machineConfigPaymentMode.setWalletType(CommonService.getWalletType(operatorId, result.getString("psp_id")));
							machineConfigPaymentModeList.add(machineConfigPaymentMode);
						}
					    String pspIdListStr = pspIdList.toString();
					    if (pspIdListStr.endsWith(",")) {
					    	pspIdListStr = pspIdListStr.substring(0, pspIdListStr.length() - 1);
					    	}
					    
					    
					    
					    String sql1 = "SELECT * FROM customerlocation_psp where customerId = '"+customerId+"' and customerLocationId='"+customerLocationId+"' and isActive=1  and psp_name NOT IN ("+pspIdListStr+") order by sequence";
//					    String sql1 = "SELECT * FROM customerlocation_psp where customerId = '"+customerId+"' and customerLocationId='"+customerLocationId+"' and isActive=1 order by sequence";
						Statement statement1 = conn.createStatement();
						ResultSet result1 = statement1.executeQuery(sql1);
						/*if(!result.isBeforeFirst()){
							String sql2 = "SELECT * FROM customer_psp where customerId='"+FROM REQUEST PARAMETER+"' and OperatorId='"+customerLocationId+"' GROUP BY psp_id";
							Statement statement1 = conn.createStatement();
							ResultSet result1 = statement1.executeQuery(sql1);
						}else{*/
						while (result1.next()){
							MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
							machineConfigPaymentMode.setPspId(result1.getString("psp_id"));
							machineConfigPaymentMode.setMethodType("exist");
							machineConfigPaymentMode.setIsStatic(result1.getInt("is_static"));
							machineConfigPaymentMode.setIsActive(result1.getInt("isActive"));
							machineConfigPaymentMode.setSelected(false);
							machineConfigPaymentMode.setSequence(result1.getInt("sequence"));
							machineConfigPaymentMode.setPspName(result1.getString("psp_name"));
							machineConfigPaymentMode.setWalletType(CommonService.getWalletType(operatorId, result1.getString("psp_id")));
							machineConfigPaymentModeList.add(machineConfigPaymentMode);
						}
					    
					    
					    
					    
					    
					    
					} 
					
					
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return machineConfigPaymentModeList;
	}
	
	public List<MachineConfigPaymentMode> getMachineConfigByMachine(String customerLocationId, String machineId) {

		List<MachineConfigPaymentMode> machineConfigPaymentModeList = new ArrayList<MachineConfigPaymentMode>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get getCustLocationPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				if(machineId.equalsIgnoreCase("0")){
				
				String sql = "SELECT * FROM customerlocation_psp where customerLocationId='"+customerLocationId+"' GROUP BY psp_id";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
					machineConfigPaymentMode.setPspId(result.getString("psp_id"));
					machineConfigPaymentModeList.add(machineConfigPaymentMode);
				}
				} else{
					String sql = "SELECT * FROM machine_config where customerLocationId='"+customerLocationId+"' and machineId='"+machineId+"'  GROUP BY psp_id";
					Statement statement = conn.createStatement();
					ResultSet result = statement.executeQuery(sql);
					while (result.next()){
						MachineConfigPaymentMode machineConfigPaymentMode = new MachineConfigPaymentMode();
						machineConfigPaymentMode.setPspId(result.getString("psp_id"));
						machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
						machineConfigPaymentMode.setPspQrcode(result.getString("psp_qrcode"));
						machineConfigPaymentMode.setPspTid(result.getString("psp_tid"));
						machineConfigPaymentMode.setRefundMsg(result.getString("refund_msg"));
						machineConfigPaymentMode.setType(result.getString("type"));
						machineConfigPaymentMode.setIsStatic(result.getInt("is_static"));
						machineConfigPaymentModeList.add(machineConfigPaymentMode);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return machineConfigPaymentModeList;
	}
	
}
