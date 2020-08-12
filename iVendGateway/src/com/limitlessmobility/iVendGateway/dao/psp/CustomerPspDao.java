package com.limitlessmobility.iVendGateway.dao.psp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;


@Repository
public interface CustomerPspDao {

	public boolean saveCustomerPsp(CustomerPspModel customerPspModel);
	
	public boolean updateCustomerPsp(CustomerPspModel customerPspModel);
	
	public List<CustomerPspModel> getCustomerPsp(CustomerPspModel customerPsp);
}
