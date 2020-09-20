package com.limitlessmobility.iVendGateway.controller.bharatqr;

import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public class BharatQRController {

	/*
	 * saveTransaction is used for call the DAO and save the QR details is DB 
	 */
	public String saveTransaction(PaymentTransaction paymentTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.saveTransaction(paymentTransaction);
		
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
