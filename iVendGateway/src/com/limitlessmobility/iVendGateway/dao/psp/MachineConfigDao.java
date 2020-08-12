package com.limitlessmobility.iVendGateway.dao.psp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.limitlessmobility.iVendGateway.model.paymentmode.GetMachinePspResponse;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigEntity;
import com.limitlessmobility.iVendGateway.psp.model.MachineConfigRequest;
import com.limitlessmobility.iVendGateway.psp.model.PspListModel;


@Repository
public interface MachineConfigDao {
	
	public boolean saveMachineConfig(MachineConfigRequest machineConfigRequest, Integer opertaorId);
	
	public boolean deleteMachineConfigBeforeInsert(MachineConfigRequest machineConfigRequest, String opertaorId);
	
	public List<MachineConfigEntity> getMachineConfig(MachineConfigEntity machineConfigEntity);
	
	public List<PspListModel> getMachinePspList(String telimetryId, Integer operatorId);
	
	public List<GetMachinePspResponse> getMachinePspListNew(String telimetryId, Integer operatorId, Integer machineId);

}
