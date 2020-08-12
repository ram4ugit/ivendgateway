package com.limitlessmobility.iVendGateway.dao.psp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.limitlessmobility.iVendGateway.controller.validation.CommonValidationUtility;
import com.limitlessmobility.iVendGateway.db.DbConfigration;
import com.limitlessmobility.iVendGateway.db.DbConfigrationImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;

public class CustomerPspDaoImpl implements CustomerPspDao{

	@Override
	public boolean saveCustomerPsp(CustomerPspModel customerPspModel) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		System.out.println("Get saveCustomerPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				String sql = "SELECT * FROM customer_psp WHERE customerId='"+customerPspModel.getCustomerId()+"' and OperatorId='"+customerPspModel.getOperatorId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if(result.next()){
					int isStatic=0;
					if(customerPspModel.getIsStatic().equalsIgnoreCase("static")) {
						isStatic=1;
			        } else if(customerPspModel.getIsStatic().equalsIgnoreCase("dynamic"))
			        {
			        	isStatic=0;
			        } else if(customerPspModel.getIsStatic().equalsIgnoreCase("1"))
			        {
			        	isStatic=1;
			        }  else if(customerPspModel.getIsStatic().equalsIgnoreCase("0"))
			        {
			        	isStatic=0;
			        }
					customerPspModel.setIsStatic(String.valueOf(isStatic));
					
					System.out.println("CustomerPSP already exist");
					String sqlUpdate = "UPDATE customer_psp " +
			                   "SET sequence='"+customerPspModel.getSequence()+"',customerId = '"+customerPspModel.getCustomerId()+"', isActive="+customerPspModel.getIsActive()+", is_static="+isStatic+" , operationLocationId='"+customerPspModel.getOperationLocationId()+"' WHERE customerId='"+customerPspModel.getCustomerId()+"' and OperatorId='"+customerPspModel.getOperatorId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
					System.out.println("UPDATE customer_psp "+ sqlUpdate);
					int u = statement.executeUpdate(sqlUpdate);
			        
			        
			        
			        
			        
			        
			        
			        
			        
			        
//				      if(u > 1) {
				    	  if(customerPspModel.getIsActive().equalsIgnoreCase("1")) {} else {
				    	  
				    	  try{
				    		  if(customerPspModel.getIsActive().equalsIgnoreCase("1")){} else{
				    			  String sqlUpdateCustLocation = "UPDATE customerlocation_psp " +
				                   "SET isActive="+customerPspModel.getIsActive()+" WHERE customerId='"+customerPspModel.getCustomerId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				    			  System.out.println("UPDATE customer_psp "+ sqlUpdateCustLocation);
				    			  statement.executeUpdate(sqlUpdateCustLocation);
				    		  }} catch(Exception e){
				    		  System.out.println("sqlUpdateCustLocation.. "+ e);
				    	  }
				    	  }
				    	  try{
				    		  if(customerPspModel.getIsActive().equalsIgnoreCase("1")){} else{ 
				    			  String sqlUpdateMachine = "UPDATE machine_config " +
					                   "SET isActive="+customerPspModel.getIsActive()+" WHERE customerId='"+customerPspModel.getCustomerId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				    			  System.out.println("UPDATE customer_psp "+ sqlUpdateMachine);
				    			  statement.executeUpdate(sqlUpdateMachine);
					    	  }
				    	  } catch(Exception e){
					    		  System.out.println("sqlUpdateCustLocation.. "+ e);
				    	  }
			        
			        
//				      }
			        
			        
			        
			        
			        
					return true;
			} else{
				PreparedStatement ps = conn.prepareStatement("INSERT INTO customer_psp VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");
		        ps.setInt(1, Integer.parseInt(customerPspModel.getOperatorId()));
		        ps.setString(2, customerPspModel.getCustomerId());
		        ps.setString(3, customerPspModel.getPspId());
		        ps.setInt(4, Integer.parseInt(customerPspModel.getIsActive()));
		        try{ps.setInt(5, Integer.parseInt(customerPspModel.getOperationLocationId()));}catch(Exception e) {ps.setInt(5, 0);}
		        if(customerPspModel.getIsStatic().equalsIgnoreCase("static")) {
		        	customerPspModel.setIsStatic("1");
		        } else if(customerPspModel.getIsStatic().equalsIgnoreCase("dynamic"))
		        {
		        	customerPspModel.setIsStatic("0");
		        }
		        ps.setInt(6, Integer.parseInt(customerPspModel.getIsStatic()));
		        ps.setString(7, customerPspModel.getPspName());
		        ps.setInt(8, customerPspModel.getSequence());
		        int i = ps.executeUpdate();
		        
		        if(i == 1) {
		        	return true;
		        }	
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in saveCustomerPspDao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return false;
	}
	
	@Override
	public boolean updateCustomerPsp(CustomerPspModel customerPspModel) {
		DbConfigration dbConfig = new DbConfigrationImpl();
		boolean response=false;
		System.out.println("Get saveCustomerPsp DAO method is calling!!");
		boolean isConnected = true;
		Connection conn = dbConfig.getCon();
		Statement stmt=null;
		if (conn == null) {
			isConnected = false;
		}
		try {
			if (isConnected) {
				
				
				String sqlExist = "SELECT * FROM customer_psp  WHERE customerId='"+customerPspModel.getCustomerId()+"' and OperatorId='"+customerPspModel.getOperatorId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sqlExist);
				if(result.next()){
					stmt = conn.createStatement();
					String sql = "UPDATE customer_psp " +
			                   "SET customerId = '"+customerPspModel.getCustomerId()+"', isActive="+customerPspModel.getIsActive()+", is_static='"+customerPspModel.getIsStatic()+"', operationLocationId='"+customerPspModel.getOperationLocationId()+"' WHERE customerId='"+customerPspModel.getCustomerId()+"' and OperatorId='"+customerPspModel.getOperatorId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
					System.out.println("UPDATE customer_psp "+ sql);
			        int i = stmt.executeUpdate(sql);
				      if(i == 1) {
				    	  try{
				    		  if(customerPspModel.getIsActive().equalsIgnoreCase("1")){} else{
				    			  String sqlUpdateCustLocation = "UPDATE customerlocation_psp " +
				                   "SET isActive="+customerPspModel.getIsActive()+" WHERE customerId='"+customerPspModel.getCustomerId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				    			  System.out.println("UPDATE customer_psp "+ sqlUpdateCustLocation);
				    			  stmt.executeUpdate(sqlUpdateCustLocation);
				    		  }} catch(Exception e){
				    		  System.out.println("sqlUpdateCustLocation.. "+ e);
				    	  }
				    	  
				    	  try{
				    		  if(customerPspModel.getIsActive().equalsIgnoreCase("1")){} else{ 
				    			  String sqlUpdateMachine = "UPDATE machine_config " +
					                   "SET isActive="+customerPspModel.getIsActive()+" WHERE customerId='"+customerPspModel.getCustomerId()+"' and psp_id='"+customerPspModel.getPspId()+"' and is_static='"+customerPspModel.getIsStatic()+"'";
				    			  System.out.println("UPDATE customer_psp "+ sqlUpdateMachine);
				    			  stmt.executeUpdate(sqlUpdateMachine);
					    	  }
				    	  } catch(Exception e){
					    		  System.out.println("sqlUpdateCustLocation.. "+ e);
				    	  }
				    	  
				    	  response = true;
				      }
			} else{
				PreparedStatement ps = conn.prepareStatement("INSERT INTO customer_psp VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)");
		        ps.setInt(1, Integer.parseInt(customerPspModel.getOperatorId()));
		        ps.setString(2, customerPspModel.getCustomerId());
		        ps.setString(3, customerPspModel.getPspId());
		        ps.setInt(4, Integer.parseInt(customerPspModel.getIsActive()));
		        ps.setInt(5, Integer.parseInt(customerPspModel.getOperationLocationId()));
		        ps.setInt(6, Integer.parseInt(customerPspModel.getIsStatic()));
		        ps.setString(7, customerPspModel.getPspName());
		        int i = ps.executeUpdate();
		        if(i == 1) {
		        	response = true;
		        }
		        
			}
			}
		} catch (Exception e) {
			System.out.println("Exception in updateCustomerPspDao.. "+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		return response;
	}

	@Override
	public List<CustomerPspModel> getCustomerPsp(CustomerPspModel customerPsp) {

		List<CustomerPspModel> customerPspModelList = new ArrayList<CustomerPspModel>();
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
				if(CommonValidationUtility.isEmpty(customerPsp.getCustomerId())) {
					
				} else{
					
					qry += " and customerId='"+customerPsp.getCustomerId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(customerPsp.getPspId())) {
					
				} else{
					
					qry += " and psp_id='"+customerPsp.getPspId()+"'";
				}
				
				if(CommonValidationUtility.isEmpty(customerPsp.getOperationLocationId())) {
					
				} else{
					
					qry += " and operationLocationId='"+customerPsp.getOperationLocationId()+"'";
				}
				if(CommonValidationUtility.isEmpty(customerPsp.getIsActive())) {
					
				} else{
					
					qry += " and isActive='"+customerPsp.getIsActive()+"'";
				} 
				
				String sql = "SELECT * FROM customer_psp where OperatorId='"+customerPsp.getOperatorId()+"'"+qry+ " order by sequence";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while (result.next()){
					CustomerPspModel customerPspModel = new CustomerPspModel();
					customerPspModel.setOperatorId(result.getString("operatorId"));
					customerPspModel.setCustomerId(result.getString("customerId"));
					customerPspModel.setPspId(result.getString("psp_id"));
					customerPspModel.setIsActive(result.getString("isActive"));
					customerPspModel.setOperationLocationId(result.getString("operationLocationId"));
					customerPspModel.setIsActive(result.getString("isActive"));
					customerPspModel.setIsStatic(result.getString("is_static"));
					customerPspModel.setPspName(result.getString("psp_name"));
					customerPspModel.setSequence(result.getInt("sequence"));
					customerPspModel.setAvailable(true);
					customerPspModelList.add(customerPspModel);
				}
				/*try{
				if(CommonValidationUtility.isEmpty(customerPsp.getCustomerId())){} else{
				String sql1 = "SELECT * FROM operator_psp where OperatorId='"+customerPsp.getOperatorId()+"'"+qry;
				Statement statement1 = conn.createStatement();
				ResultSet result1 = statement.executeQuery(sql1);
				while (result1.next()){
					
					boolean isExist=true;
					Iterator<CustomerPspModel> itr = customerPspModelList.listIterator();
					while(itr.hasNext()){
						String pspName = itr.next().getPspName();
						if(pspName.equalsIgnoreCase(result1.getString("psp_name"))){
							isExist=false;
						}
					}
					if(isExist){
					CustomerPspModel customerPspModel = new CustomerPspModel();
					customerPspModel.setOperatorId(customerPsp.getOperatorId());
					customerPspModel.setCustomerId(customerPsp.getCustomerId());
					customerPspModel.setPspId(result1.getString("psp_id"));
					customerPspModel.setIsActive(result1.getString("isActive"));
					customerPspModel.setOperationLocationId(result1.getString("operationLocationId"));
					customerPspModel.setIsActive(result1.getString("isActive"));
					String paymentType = result1.getString("payment_type");
					String isStatic = "0";
					if(paymentType.equalsIgnoreCase("Static")){
						isStatic="1";
					}
					customerPspModel.setIsStatic(isStatic);
					customerPspModel.setPspName(result1.getString("psp_name"));
					customerPspModel.setAvailable(true);
					
					customerPspModelList.add(customerPspModel);
					}
				}
				}
				} catch(Exception e){
					System.out.println("Get from operator in get customen psp... "+ e);
				}*/
			}
		} catch (Exception e) {
			System.out.println("Exception is.."+e);
		}finally{
			dbConfig.closeConnection(conn);
		}
		
		return customerPspModelList;
	}

}
