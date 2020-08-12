package com.limitlessmobility.iVendGateway.controller.common;

import org.springframework.stereotype.Component;

import com.limitlessmobility.iVendGateway.dao.common.SavePaymentDao;
import com.limitlessmobility.iVendGateway.dao.common.SavePaymentDaoImpl;
import com.limitlessmobility.iVendGateway.model.common.RefundData;

@Component
public class SaveRefundDataServiceImpl  implements SaveRefundDataService{
	
	private SavePaymentDao savePaymentDao = new SavePaymentDaoImpl();
	

	@Override
	public boolean saveRefundData(RefundData refundData) {
		// TODO Auto-generated method stub

		boolean isInsert=false;
		try{
			isInsert = savePaymentDao.saveRefundData(refundData);
		} catch(Exception e) {
			System.out.println("SaveRefundData Service Error... "+e);
		}
		return isInsert;
	
	}
	
	

}
