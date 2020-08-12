package com.limitlessmobility.iVendGateway.dao.sodexo;

import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public interface SodexoCallbackTransactionDao {

	public abstract boolean saveTransaction(PaymentTransaction paymentTransactions);

}
