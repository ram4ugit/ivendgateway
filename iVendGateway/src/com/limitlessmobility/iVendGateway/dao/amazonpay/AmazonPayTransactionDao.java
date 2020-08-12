package com.limitlessmobility.iVendGateway.dao.amazonpay;

import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public interface AmazonPayTransactionDao {

	public abstract boolean saveTransaction(PaymentTransaction paymentTransactions);

}
