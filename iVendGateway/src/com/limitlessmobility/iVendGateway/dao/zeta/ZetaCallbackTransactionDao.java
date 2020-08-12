package com.limitlessmobility.iVendGateway.dao.zeta;

import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public interface ZetaCallbackTransactionDao {

	public abstract boolean saveTransaction(PaymentTransaction paymentTransactions,ZetaCallbackTransactions zetaCallbackTransactions);

}
