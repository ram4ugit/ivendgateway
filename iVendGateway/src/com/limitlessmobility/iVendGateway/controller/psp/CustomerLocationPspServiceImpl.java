package com.limitlessmobility.iVendGateway.controller.psp;

import java.util.ArrayList;
import java.util.List;

import com.limitlessmobility.iVendGateway.dao.psp.CustomerLocationPspDao;
import com.limitlessmobility.iVendGateway.dao.psp.CustomerLocationPspDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.CustomerlocationPspEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigPaymentMode;

//@Service(value = "CustomerLocationPspService")
public class CustomerLocationPspServiceImpl{

	private CustomerLocationPspDao customerLocationPspDao = new CustomerLocationPspDaoImpl();
	
//	@Override
	public boolean saveCustomerLocationPsp(
			CustomerlocationPspEntity customerlocationPspEntity) {
		boolean isInsert=false;
		try{
			isInsert = customerLocationPspDao.saveCustomerLocationPsp(customerlocationPspEntity);
		} catch(Exception e) {
			System.out.println("saveCustomerLocationPsp Service Error... "+e);
		}
		return isInsert;
	}
	
//	@Override
	public boolean updateCustomerLocationPsp(
			CustomerlocationPspEntity customerlocationPspEntity) {
		boolean isInsert=false;
		try{
			isInsert = customerLocationPspDao.updateCustomerLocationPsp(customerlocationPspEntity);
		} catch(Exception e) {
			System.out.println("saveCustomerLocationPsp Service Error... "+e);
		}
		return isInsert;
	}

//	@Override
	public List<CustomerlocationPspEntity> getCustomerLocationPspList(CustomerlocationPspEntity customerlocationPspEntity) {
		List<CustomerlocationPspEntity> customerlocationPspList = new ArrayList<CustomerlocationPspEntity>();
		try{
			customerlocationPspList = customerLocationPspDao.getCustomerLocationPsp(customerlocationPspEntity);
		} catch(Exception e) {
			System.out.println("getCustomerLocationPspList Service Error... "+e);
		}
		return customerlocationPspList;
	}

	/*@Override
	public List<MachineConfigPaymentMode> getCustLocationPspList(String customerLocationId, String machineId) {
		List<MachineConfigPaymentMode> machineConfigPaymentMode = new ArrayList<MachineConfigPaymentMode>();
		try{
			machineConfigPaymentMode = customerLocationPspDao.getCustLocationPsp(customerLocationId, machineId);
		} catch(Exception e) {
			System.out.println("getCustomerLocationPspList Service Error... "+e);
		}
		return machineConfigPaymentMode;
	}*/
//	@Override
	public List<MachineConfigPaymentMode> getCustLocationPspListt(String customerId, String customerLocationId, String machineId, int operatorId) {
		List<MachineConfigPaymentMode> machineConfigPaymentMode = new ArrayList<MachineConfigPaymentMode>();
		try{
			machineConfigPaymentMode = customerLocationPspDao.getCustLocationPspp(customerId, customerLocationId, machineId, operatorId);
		} catch(Exception e) {
			System.out.println("getCustomerLocationPspList Service Error... "+e);
		}
		return machineConfigPaymentMode;
	}

}
