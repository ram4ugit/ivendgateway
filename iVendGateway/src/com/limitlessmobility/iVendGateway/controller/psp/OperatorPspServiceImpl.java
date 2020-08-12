package com.limitlessmobility.iVendGateway.controller.psp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.limitlessmobility.iVendGateway.dao.psp.OperatorPspDao;
import com.limitlessmobility.iVendGateway.dao.psp.OperatorPspDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspAll;
import com.limitlessmobility.iVendGateway.psp.model.OperatorPspEntity;

//@Service(value = "OperatorPspService")
public class OperatorPspServiceImpl {

	private OperatorPspDao operatorPspDao = new OperatorPspDaoImpl();
	
	
	public boolean saveOperatorPsp(OperatorPspEntity operatorPspEntity) {
		boolean isInsert=false;
		try{
			isInsert = operatorPspDao.saveOperatorPsp(operatorPspEntity);
		} catch(Exception e) {
			System.out.println("saveOperatorPsp Service Error... "+e);
		}
		return isInsert;
	}

	
	public List<OperatorPspEntity> getOperatorList(OperatorPspEntity operatorPspEntity) {
		List<OperatorPspEntity> operatorPspEntityList = new ArrayList<OperatorPspEntity>();
		try{
			operatorPspEntityList = operatorPspDao.getOperatorPspList(operatorPspEntity);
		} catch(Exception e) {
			System.out.println("getOperatorList Service Error... "+e);
		}
		return operatorPspEntityList;
	}
	
	public List<OperatorPspAll> getAllPsp(String opertorId) {
		List<OperatorPspAll> operatorPspEntityList = new ArrayList<OperatorPspAll>();
		try{
			operatorPspEntityList = operatorPspDao.getOperatorPspListAll(opertorId);
		} catch(Exception e) {
			System.out.println("getOperatorList Service Error... "+e);
		}
		return operatorPspEntityList;
	}

	
	public boolean updateOperatorPsp(OperatorPspEntity operatorPspEntity) {
		boolean isInsert=false;
		try{
			isInsert = operatorPspDao.updateOperatorPsp(operatorPspEntity);
		} catch(Exception e) {
			System.out.println("saveOperatorPsp Service Error... "+e);
		}
		return isInsert;
	}
	public boolean updateOperatorSequence(OperatorPspEntity operatorPspEntity) {
		boolean isInsert=false;
		try{
			isInsert = operatorPspDao.updateOperatorSequence(operatorPspEntity);
		} catch(Exception e) {
			System.out.println("saveOperatorPsp Service Error... "+e);
		}
		return isInsert;
	}

}
