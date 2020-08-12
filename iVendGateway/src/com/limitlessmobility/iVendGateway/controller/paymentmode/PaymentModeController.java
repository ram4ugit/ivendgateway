package com.limitlessmobility.iVendGateway.controller.paymentmode;

import java.util.ArrayList;
import java.util.List;

import com.limitlessmobility.iVendGateway.dao.PaymentModeDao;
import com.limitlessmobility.iVendGateway.dao.PaymentModeDaoImpl;
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
 