package com.limitlessmobility.iVendGateway.dao.psp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.limitlessmobility.iVendGateway.psp.model.CustomerlocationPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;

@Repository
public interface CustomerLocationPspDao {

	public boolean saveCustomerLocationPsp(CustomerlocationPspEntity customerLocationPsp);
	
	public boolean updateCustomerLocationPsp(CustomerlocationPspEntity customerLocationPsp);
	
	public List<CustomerlocationPspEntity> getCustomerLocationPsp(CustomerlocationPspEntity customerLocationPsp);
	
//	public List<MachineConfigPaymentMode> getCustLocationPsp(String customerLocationId, String machineId);
	
	public List<MachineConfigPaymentMode> getCustLocationPspp(String customerId, String customerLocationId, String machineId, int operatorId);
}
