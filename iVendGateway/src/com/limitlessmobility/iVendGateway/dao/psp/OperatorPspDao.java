package com.limitlessmobility.iVendGateway.dao.psp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.limitlessmobility.iVendGateway.psp.model.CustomerPspModel;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspAll;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;

@Repository
public interface OperatorPspDao {

	public boolean saveOperatorPsp(OperatorPspEntity operatorPspEntity);
	
	public boolean updateOperatorPsp(OperatorPspEntity operatorPspEntity);
	
	public boolean updateOperatorSequence(OperatorPspEntity operatorPspEntity);
	
	public List<OperatorPspEntity> getOperatorPspList(OperatorPspEntity operatorPspEntity);
	
	public List<OperatorPspAll> getOperatorPspListAll(String opertorId);//Request:- operatorID
	
	public List<OperatorPspEntity> getOperatorPspListPromo(String opertorId);//Request:- operatorID
	
	public List<OperatorPspEntity> getOperatorPspListForCustomer(String opertorId);//Request:- operatorID
	
	public List<OperatorPspEntity> getOperatorPspListByLocation(String opertorId);
	
	public List<CustomerPspModel> getOperatorPspListForcustLocation(String opertorId, int customerId);//Request:- operatorID
	
}
