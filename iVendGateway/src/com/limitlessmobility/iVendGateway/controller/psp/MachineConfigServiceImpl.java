package com.limitlessmobility.iVendGateway.controller.psp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.limitlessmobility.iVendGateway.dao.psp.MachineConfigDao;
import com.limitlessmobility.iVendGateway.dao.psp.MachineConfigDaoImpl;
import com.limitlessmobility.iVendGateway.model.paymentmode.GetMachinePspResponse;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigRequest;
import com.limitlessmobility.iVendGateway.psp.model.PspListModel;

//@Service(value = "MachineConfigService")
public class MachineConfigServiceImpl {

	private MachineConfigDao machineConfigDao=new MachineConfigDaoImpl();
	
	
	public boolean saveMachineConfig(MachineConfigRequest machineConfigRequest, Integer opertaorId) {
		boolean isInsert=false;
		try{
			isInsert = machineConfigDao.saveMachineConfig(machineConfigRequest,opertaorId);
		} catch(Exception e) {
			System.out.println("saveMachineConfig Service Error... "+e);
		}
		return isInsert;
	}

	
	public List<MachineConfigEntity> getMachineConfig(
			MachineConfigEntity machineConfigEntity) {
		List<MachineConfigEntity> machineConfiglList = new ArrayList<MachineConfigEntity>();
		try{
			machineConfiglList = machineConfigDao.getMachineConfig(machineConfigEntity);
		} catch(Exception e) {
			System.out.println("getMachineConfig Service Error... "+e);
		}
		return machineConfiglList;
	}

	
	public List<PspListModel> getMachinePspList(String telimetryId,
			Integer operatorId) {
		List<PspListModel> machinePspList = new ArrayList<PspListModel>();
		try{
			machinePspList = machineConfigDao.getMachinePspList(telimetryId, operatorId);
		} catch(Exception e) {
			System.out.println("getMachineConfig Service Error... "+e);
		}
		return machinePspList;
	}
	
	public List<GetMachinePspResponse> getMachinePspListNew(String telimetryId,
			Integer operatorId, int machineId) {
		List<GetMachinePspResponse> machinePspList = new ArrayList<GetMachinePspResponse>();
		try{
			machinePspList = machineConfigDao.getMachinePspListNew(telimetryId, operatorId, machineId);
		} catch(Exception e) {
			System.out.println("getMachineConfig Service Error... "+e);
		}
		return machinePspList;
	}

	
}
