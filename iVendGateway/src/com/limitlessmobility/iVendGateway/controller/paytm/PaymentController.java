package com.limitlessmobility.iVendGateway.controller.paytm;

import java.util.HashMap;
import java.util.Map;

import com.limitlessmobility.iVendGateway.dao.TransactionDao;
import com.limitlessmobility.iVendGateway.dao.TransactionDaoImpl;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;


public class PaymentController {

	public String saveTransaction(PaymentTransaction callbackTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.saveTransaction(callbackTransaction);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
	
	public String payPendingSaveTransaction(PaymentTransaction callbackTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.payPendingSaveTransaction(callbackTransaction);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
	
	public String updateTransaction(PaymentTransaction callbackTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.updateBlockedTransaction(callbackTransaction);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
	public String updateReleaseTransaction(PaymentTransaction callbackTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.updateReleaseTransaction(callbackTransaction);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
	public String updateVoidTransaction(PaymentTransaction callbackTransaction){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isInsert = tdao.updateVoidTransaction(callbackTransaction);
		
		if(isInsert){
//			dataResult = "Data has been saved successfully.";
//			resultCode = 200;
			
		}else{
//			dataResult = "Something wrong please try again.";
//			resultCode = 504;
		}
		
		return "success";
	}
	public PaymentTransaction getTransaction(String orderId){
		TransactionDao tdao = new TransactionDaoImpl();
		PaymentTransaction paymentTransactions = tdao.getTransactionByOrder(orderId);
		
		
		
		return paymentTransactions;
	}
	public boolean getTransactionExistByOrder(String orderId){
		TransactionDao tdao = new TransactionDaoImpl();
		boolean isExist = tdao.getTransactionExistByOrder(orderId);
		
		
		
		return isExist;
	}
}
