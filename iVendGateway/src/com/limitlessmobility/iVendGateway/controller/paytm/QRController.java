package com.limitlessmobility.iVendGateway.controller.paytm;

import com.limitlessmobility.iVendGateway.dao.QRDao;
import com.limitlessmobility.iVendGateway.dao.QRDaoImpl;
import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class QRController {

	public String savePaymentInitiation(PaymentInitiation paymentInitiation){
		QRDao qrdao = new QRDaoImpl();
		boolean isInsert = qrdao.saveQRRequest(paymentInitiation);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
}
