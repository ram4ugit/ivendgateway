package com.limitlessmobility.iVendGateway.dao.psp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.limitlessmobil.ivendgateway.util.CommonUtil;
import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspAll;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;

public class OperatorPspDaoImpl implements OperatorPspDao{
	
	@Autowired
	JdbcTemplate template;

	@Override
	public boolean saveOperatorPsp(OperatorPspEntity operatorPspEntity) {

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("saveOperatorPsp DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				
				String sql = "SELECT * FROM operator_psp WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and operationLocationId='"+operatorPspEntity.getOperationLocationId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and payment_id='"+operatorPspEntity.getPaymentId()+"' and payment_type='"+operatorPspEntity.getPaymentType()+"'";
				System.out.println("CheckExist query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				if(result.next()){
					if(updateOperatorPsp(operatorPspEntity)){
						return true;
					} else {
						return false;
					}
				} else{

				PreparedStatement ps = conn.prepareStatement("INSERT INTO operator_psp VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		        ps.setInt(1, operatorPspEntity.getOperatorId());
		        ps.setInt(2, operatorPspEntity.getOperationLocationId());
		        ps.setString(3, operatorPspEntity.getPspMerchantId());
		        ps.setString(4, operatorPspEntity.getPspMerchantKey());
		        ps.setString(5, operatorPspEntity.getPspId());
		        ps.setString(6, operatorPspEntity.getPspMguid());
		        ps.setInt(7, 1);
		        if(CommonValidationUtility.isEmpty(operatorPspEntity.getPaymentId())) {
		        	ps.setInt(8, 0);
		        }else{
		        	ps.setInt(8, operatorPspEntity.getPaymentId());
		        }
		        
		        ps.setString(9, operatorPspEntity.getPaymentType());
		        
		        ps.setString(10, operatorPspEntity.getWalletType());
		        
		        ps.setString(11, operatorPspEntity.getCustomerId());
		        
		        ps.setString(12, operatorPspEntity.getPspName());
		        
		        if(CommonUtil.isNullOrEmpty(operatorPspEntity.getImagePath())){
		        	if(operatorPspEntity.getPspId().equalsIgnoreCase("paytm")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Paytm.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Cards")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/crdrcard.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Zeta")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/zeta.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Bharatqr")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/bharatqr.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Phonepe")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/phonepay.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Sodexo")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/sodexo.PNG");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Prepaid Cards")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/prepaidcard.png");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Amazonpay")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Amazonpay.PNG");
			        }
		        }
		        ps.setString(13, operatorPspEntity.getImagePath());
		        
		        ps.setString(14, "");
		        ps.setInt(15, operatorPspEntity.getSequence());
		        int i = ps.executeUpdate();
		        try{
		        String walletType = operatorPspEntity.getWalletType();
		        if(walletType.equalsIgnoreCase("CU")){
		        	CustomerPspModel c = new CustomerPspModel();
		        	c.setCustomerId(operatorPspEntity.getCustomerId());
		        	c.setIsActive("1");
		        	String paymenttype = operatorPspEntity.getPaymentType();
		        	c.setIsStatic("0");
		        	if(paymenttype.equalsIgnoreCase("static")){
		        		c.setIsStatic("1");
		        	}
		        	
		        	c.setOperationLocationId(String.valueOf(operatorPspEntity.getOperationLocationId()));
		        	c.setOperatorId(String.valueOf(operatorPspEntity.getOperatorId()));
		        	c.setPspId(operatorPspEntity.getPspId());
		        	c.setPspName(operatorPspEntity.getPspName());
		        	
		        	CustomerPspDao cdao = new CustomerPspDaoImpl();
		        	cdao.saveCustomerPsp(c);
		        }} catch(Exception e){
		        	System.out.println("exception in save cu customerpsp.. "+e);
		        }
		      if(i == 1) {
		        return true;
		      }
			}
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
	public List<OperatorPspEntity> getOperatorPspList(OperatorPspEntity operatorPspEntity) {

		List<OperatorPspEntity> operatorPspEntityList = new ArrayList<OperatorPspEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String qry="";
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getOperationLocationId()) || operatorPspEntity.getOperationLocationId()==0) {
					
				} else{
					
					qry += " and operationLocationId='"+operatorPspEntity.getOperationLocationId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspMerchantId())) {
					
				} else{
					
					qry += " and psp_merchant_id='"+operatorPspEntity.getPspMerchantId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getPspId())) {
					
				} else{
					
					qry += " and psp_id='"+operatorPspEntity.getPspId()+"'";
				}
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getIsActive())) {
					
				} else{
					
					qry += " and isActive='"+operatorPspEntity.getIsActive()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getPaymentId())) {
					
				} else{
					
					qry += " and payment_id='"+operatorPspEntity.getPaymentId()+"'";
				}
				if(CommonValidationUtility.isEmpty(operatorPspEntity.getPaymentType())) {
					
				} else{
					
					qry += " and payment_type='"+operatorPspEntity.getPaymentType()+"'";
				}
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+operatorPspEntity.getOperatorId()+"' "+qry+ " and wallet_type!='CU' order by sequence";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					OperatorPspEntity operatorEntity = new OperatorPspEntity();
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					operatorEntity.setSequence(result.getInt("sequence"));
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
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					operatorPspEntityList.add(operatorEntity);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return operatorPspEntityList;
	}

	@Override
	public boolean updateOperatorPsp(OperatorPspEntity operatorPspEntity) {

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("updateOperatorPsp DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				
				if(CommonUtil.isNullOrEmpty(operatorPspEntity.getImagePath())){
		        	if(operatorPspEntity.getPspId().equalsIgnoreCase("paytm")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Paytm.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Cards")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/crdrcard.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Zeta")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/zeta.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Bharatqr")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/bharatqr.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Phonepe")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/phonepay.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Sodexo")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/sodexo.PNG");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Prepaid Cards")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/prepaidcard.png");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Amazonpay")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Amazonpay.PNG");
			        }
		        }
				
				
				String updateOperatorSql = "UPDATE operator_psp " +
						"SET psp_merchant_id = '"+operatorPspEntity.getPspMerchantId()+"', "
						+ "psp_merchant_key='"+operatorPspEntity.getPspMerchantKey()+"', sequence='"+operatorPspEntity.getSequence()+"', wallet_type='"+operatorPspEntity.getWalletType()+"', psp_mguid='"+operatorPspEntity.getPspMguid()+"', image_path='"+operatorPspEntity.getImagePath()+"', isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and payment_type='"+operatorPspEntity.getPaymentType()+"' and payment_id='"+operatorPspEntity.getPaymentId()+"'";
				System.out.println("updateOperatorSql== "+updateOperatorSql);
				PreparedStatement ps = conn.prepareStatement(updateOperatorSql);
		        
		        
		        int i = ps.executeUpdate();
		      if(i == 1) {
		    	  System.out.println("operator_psp updated successfully");
		      }
		      
		      int isStatic = 0;
		      if(operatorPspEntity.getPaymentType().equalsIgnoreCase("Dynamic")){
		    	  isStatic=0;
		      } else if(operatorPspEntity.getPaymentType().equalsIgnoreCase("Static")){
		    	  isStatic=1;
		      }
		      if(operatorPspEntity.getIsActive().equalsIgnoreCase("0")){
		      String updateCusomerSql="UPDATE customer_psp " +
						"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
		      System.out.println("updateCusomerSql in update operator.. "+updateCusomerSql);
		      PreparedStatement preparedStatement2 = conn.prepareStatement(updateCusomerSql);
				int i2 = preparedStatement2.executeUpdate();
		      
				if (i2 > 0) {
					System.out.println("customer_psp updated successfully");
				}
				
				try{
					String customerLocationUpdateSql = "UPDATE customerlocation_psp " +
							"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
					System.out.println("customerLocationUpdateSql updateOperator== "+customerLocationUpdateSql);
					PreparedStatement preparedStatement3 = conn.prepareStatement(customerLocationUpdateSql);
					int i3 = preparedStatement3.executeUpdate();
	
					if (i3 > 0) {
						System.out.println("customerlocation_psp updated successfully");
					}
				} catch(Exception e){
					System.out.println("update operator psp--customerlocation_psp error... "+e);
				}
				
				try{
					String machineUpdateSql = "UPDATE machine_config " +
							"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
					System.out.println("machineUpdateSql.. updateOperator.. "+machineUpdateSql);
					PreparedStatement preparedStatement4 = conn.prepareStatement(machineUpdateSql);
					int i4 = preparedStatement4.executeUpdate();
	
					if (i4 > 0) {
						System.out.println("machine_config updated successfully");
					}
				} catch(Exception e){
					System.out.println("update operator psp--machine_config error... "+e);
				}
		      }
				return true;
		      
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
    public List<OperatorPspAll> getOperatorPspListAll(String opertorId) {

		List<OperatorPspAll> operatorPspEntityList = new ArrayList<OperatorPspAll>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspListAll DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+opertorId+"'";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					
					OperatorPspAll operatorEntity = new OperatorPspAll();
					operatorEntity.setPspName(result.getString("psp_name"));
					operatorEntity.setPspId(result.getString("psp_id").trim());
					if(result.getString("payment_type").trim().equalsIgnoreCase("static")) {
						operatorEntity.setIsStatic(1);
					} else {
						operatorEntity.setIsStatic(0);
					}
					
					
					/*operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
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
					
					operatorEntity.setImagePath(result.getString("image_path"));*/
					
					/*if(result.getString("customerId")==null){
						operatorEntity.setCustomerId("");
					} else{
						operatorEntity.setCustomerId(result.getString("customerId"));
					}*/
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					if(result.getString("payment_type").trim().equalsIgnoreCase("Wallet")) {} else {
					operatorPspEntityList.add(operatorEntity);
					}
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return operatorPspEntityList;
	}

	@Override
	public boolean updateOperatorSequence(OperatorPspEntity operatorPspEntity) {

		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("updateOperatorPsp DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				
				if(CommonUtil.isNullOrEmpty(operatorPspEntity.getImagePath())){
		        	if(operatorPspEntity.getPspId().equalsIgnoreCase("paytm")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Paytm.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Cards")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/crdrcard.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Zeta")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/zeta.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Bharatqr")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/bharatqr.PNG");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Phonepe")){
		        		operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/phonepay.png");
		        	} else if(operatorPspEntity.getPspId().equalsIgnoreCase("Sodexo")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/sodexo.PNG");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Prepaid Cards")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/prepaidcard.png");
			        } else if(operatorPspEntity.getPspId().equalsIgnoreCase("Amazonpay")){
			        	operatorPspEntity.setImagePath("http://vendiman.limitlessmobil.com:802/media/logo/Amazonpay.PNG");
			        }
		        }
				
				
				String updateOperatorSql = "UPDATE operator_psp " +
						"SET sequence='"+operatorPspEntity.getSequence()+"', isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and payment_type='"+operatorPspEntity.getPaymentType()+"' and payment_id='"+operatorPspEntity.getPaymentId()+"'";
				System.out.println("updateOperatorSql== "+updateOperatorSql);
				PreparedStatement ps = conn.prepareStatement(updateOperatorSql);
		        
		        
		        int i = ps.executeUpdate();
		      if(i == 1) {
		    	  System.out.println("operator_psp updated successfully");
		      }
		      
		      int isStatic = 0;
		      if(operatorPspEntity.getPaymentType().equalsIgnoreCase("Dynamic")){
		    	  isStatic=0;
		      } else if(operatorPspEntity.getPaymentType().equalsIgnoreCase("Static")){
		    	  isStatic=1;
		      }
		      if(operatorPspEntity.getIsActive().equalsIgnoreCase("0")){
		      String updateCusomerSql="UPDATE customer_psp " +
						"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
		      System.out.println("updateCusomerSql in update operator.. "+updateCusomerSql);
		      PreparedStatement preparedStatement2 = conn.prepareStatement(updateCusomerSql);
				int i2 = preparedStatement2.executeUpdate();
		      
				if (i2 > 0) {
					System.out.println("customer_psp updated successfully");
				}
				
				try{
					String customerLocationUpdateSql = "UPDATE customerlocation_psp " +
							"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
					System.out.println("customerLocationUpdateSql updateOperator== "+customerLocationUpdateSql);
					PreparedStatement preparedStatement3 = conn.prepareStatement(customerLocationUpdateSql);
					int i3 = preparedStatement3.executeUpdate();
	
					if (i3 > 0) {
						System.out.println("customerlocation_psp updated successfully");
					}
				} catch(Exception e){
					System.out.println("update operator psp--customerlocation_psp error... "+e);
				}
				
				try{
					String machineUpdateSql = "UPDATE machine_config " +
							"SET isActive="+operatorPspEntity.getIsActive()+" WHERE operatorId='"+operatorPspEntity.getOperatorId()+"' and psp_id='"+operatorPspEntity.getPspId()+"' and is_static='"+isStatic+"'";
					System.out.println("machineUpdateSql.. updateOperator.. "+machineUpdateSql);
					PreparedStatement preparedStatement4 = conn.prepareStatement(machineUpdateSql);
					int i4 = preparedStatement4.executeUpdate();
	
					if (i4 > 0) {
						System.out.println("machine_config updated successfully");
					}
				} catch(Exception e){
					System.out.println("update operator psp--machine_config error... "+e);
				}
		      }
				return true;
		      
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
	public List<OperatorPspEntity> getOperatorPspListPromo(String opertorId) {

		List<OperatorPspEntity> operatorPspEntityList = new ArrayList<OperatorPspEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String qry="";
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+opertorId+"' and wallet_type!='CU' order by sequence";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					OperatorPspEntity operatorEntity = new OperatorPspEntity();
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					operatorEntity.setSequence(result.getInt("sequence"));
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
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					operatorPspEntityList.add(operatorEntity);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return operatorPspEntityList;
	}

	@Override
	public List<OperatorPspEntity> getOperatorPspListByLocation(
	        String opertorId) {

		List<OperatorPspEntity> operatorPspEntityList = new ArrayList<OperatorPspEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String qry="";
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+opertorId+"' and  and wallet_type!='CU' order by sequence";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					OperatorPspEntity operatorEntity = new OperatorPspEntity();
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					operatorEntity.setSequence(result.getInt("sequence"));
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
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					operatorPspEntityList.add(operatorEntity);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return operatorPspEntityList;
	}

	@Override
	public List<CustomerPspModel> getOperatorPspListForcustLocation(String opertorId, int customerId) {

		List<CustomerPspModel> customerPspEntityList = new ArrayList<CustomerPspModel>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String qry="";
				
				String sql = "SELECT * FROM customer_psp where OperatorId='"+opertorId+"' and customerId='"+customerId+"' and isActive='1' order by sequence";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					CustomerPspModel customerPspModel = new CustomerPspModel();
					customerPspModel.setOperatorId(String.valueOf(result.getInt("OperatorId")));
					customerPspModel.setOperationLocationId(String.valueOf(result.getInt("operationLocationId")));
//					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
//					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					customerPspModel.setPspId(result.getString("psp_id").trim());
					customerPspModel.setPspName(result.getString("psp_name").trim());
//					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					customerPspModel.setIsActive(result.getString("isActive"));
					customerPspModel.setSequence(result.getInt("sequence"));
					customerPspModel.setIsStatic(String.valueOf(result.getInt("is_static")));
//					System.out.println("String setPaymentId.. "+ result.getInt("payment_id"));
//					operatorEntity.setPaymentId(result.getInt("payment_id"));
//					System.out.println("String payment_type.. "+ result.getString("payment_type"));
//					operatorEntity.setPaymentType(result.getString("payment_type"));
//					System.out.println("String wallet_type.. "+ result.getString("wallet_type"));
//					operatorEntity.setWalletType(result.getString("wallet_type"));
					
//					operatorEntity.setImagePath(result.getString("image_path"));
//					operatorEntity.setPspName(result.getString("psp_name"));
					
					if(result.getString("customerId")==null){
						customerPspModel.setCustomerId("");
					} else{
						customerPspModel.setCustomerId(result.getString("customerId"));
					}
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					customerPspEntityList.add(customerPspModel);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return customerPspEntityList;
	}

	@Override
	public List<OperatorPspEntity> getOperatorPspListForCustomer(
	        String opertorId) {

		List<OperatorPspEntity> operatorPspEntityList = new ArrayList<OperatorPspEntity>();
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("getOperatorPspList DAO method is calling!!");
		boolean isConnected = true;

		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			
			if (isConnected) {
				
				String qry="";
				
				String sql = "SELECT * FROM operator_psp where operatorId='"+opertorId+"' and wallet_type!='CU' and isActive='1' order by sequence";
				System.out.println("getOperatorPspList query.." +sql);
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				 
				while (result.next()){ 
					OperatorPspEntity operatorEntity = new OperatorPspEntity();
					operatorEntity.setOperatorId(Integer.parseInt(result.getString("operatorId").trim()));
					operatorEntity.setOperationLocationId(Integer.parseInt(result.getString("operationLocationId").trim()));
					operatorEntity.setPspMerchantId(result.getString("psp_merchant_id").trim());
					operatorEntity.setPspMerchantKey(result.getString("psp_merchant_key").trim());
					operatorEntity.setPspId(result.getString("psp_id").trim());
					operatorEntity.setPspMguid(result.getString("psp_mguid"));
					operatorEntity.setIsActive(result.getString("isActive"));
					operatorEntity.setSequence(result.getInt("sequence"));
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
					
					/*String customerId = result.getString("customerId");
					String walletType =result.getString("wallet_type");
					if(walletType.equalsIgnoreCase("CU")){
						if(customerId.equalsIgnoreCase(operatorPspEntity.getCustomerId())){
							operatorPspEntityList.add(operatorEntity);
						}
					} else{
						operatorPspEntityList.add(operatorEntity);
					}*/
					operatorPspEntityList.add(operatorEntity);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
//				log.error("Saving failed...."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}

		return operatorPspEntityList;
	}

	
}
