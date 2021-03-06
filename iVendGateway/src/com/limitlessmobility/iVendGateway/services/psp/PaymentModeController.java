package com.limitlessmobility.iVendGateway.services.psp;

import java.util.List;

import com.limitlessmobility.iVendGateway.dao.psp.PaymentModeDao;
import com.limitlessmobility.iVendGateway.dao.psp.PaymentModeDaoImpl;
import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;

public class PaymentModeController {

	PaymentModeDao paymentModeDao = new PaymentModeDaoImpl();
	
	public List<PaymentModeModel> getPaymentModes(){
		return paymentModeDao.getPaymentModeList();
	}
	public List<PaymentModeModel> getPspDetails(PaymentModeModel paymentMode){
		return paymentModeDao.getPSPList(paymentMode);
	}
	
	public PspDetailModal getPspDetailById(String pspId){
		return paymentModeDao.getpspDetailByPspId(pspId);
	}
}
 