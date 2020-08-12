package com.limitlessmobility.iVendGateway.controller.psp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.limitlessmobility.iVendGateway.dao.psp.CustomerPspDao;
import com.limitlessmobility.iVendGateway.dao.psp.CustomerPspDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;

//@Service(value = "CustomerPspService")
public class CustomerPspServiceImpl {

	private CustomerPspDao customerPspDao = new CustomerPspDaoImpl();
	
	
	public boolean saveCustomerPsp(CustomerPspModel customerPspModel) {
		boolean isInsert=false;
		try{
			isInsert = customerPspDao.saveCustomerPsp(customerPspModel);
		} catch(Exception e) {
			System.out.println("saveCustomerPsp Service Error... "+e);
		}
		return isInsert;
	}
	
	
	public boolean updateCustomerPsp(CustomerPspModel customerPspModel) {
		boolean isInsert=false;
		try{
			isInsert = customerPspDao.updateCustomerPsp(customerPspModel);
		} catch(Exception e) {
			System.out.println("saveCustomerPsp Service Error... "+e);
		}
		return isInsert;
	}

	
	public List<CustomerPspModel> getCustomerList(CustomerPspModel customerPsp) {
		List<CustomerPspModel> customerPspModelList = new ArrayList<CustomerPspModel>();
		try{
			customerPspModelList = customerPspDao.getCustomerPsp(customerPsp);
		} catch(Exception e) {
			System.out.println("getOperatorList Service Error... "+e);
		}
		return customerPspModelList;
	}

}
