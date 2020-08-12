package com.limitlessmobility.iVendGateway.dao;

import java.util.Map;

import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public abstract interface TransactionDao {
	
	public abstract boolean saveTransaction(PaymentTransaction paymentTransactions);
	public abstract boolean payPendingSaveTransaction(PaymentTransaction paymentTransactions);
	public abstract boolean updateBlockedTransaction(PaymentTransaction paymentTransactions);
	public abstract boolean updateReleaseTransaction(PaymentTransaction paymentTransactions);
	public abstract boolean updateVoidTransaction(PaymentTransaction callbackTransactions);
	public abstract boolean updatependingTransaction(PaymentTransaction paymentTransactions);
	public  PaymentTransaction getTransactionByOrder(String orderId);
	public  boolean getTransactionExistByOrder(String orderId);
}
