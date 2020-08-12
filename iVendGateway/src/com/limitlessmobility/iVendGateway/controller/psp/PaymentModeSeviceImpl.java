package com.limitlessmobility.iVendGateway.controller.psp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.limitlessmobility.iVendGateway.dao.psp.PaymentModeDao;
import com.limitlessmobility.iVendGateway.dao.psp.PaymentModeDaoImpl;
import com.limitlessmobility.iVendGateway.psp.model.PaymentModes;

//@Service(value = "paymentModeService")
public class PaymentModeSeviceImpl {

	PaymentModeDao paymentModeDao = new PaymentModeDaoImpl();
	
	List<PaymentModes> paymentModeList;
	
	
	public List<PaymentModes> findAllActivePsp() {
		paymentModeList = new ArrayList<>();
		paymentModeList = paymentModeDao.getPaymentModeList("ivend");
		return paymentModeList;
	}

}
