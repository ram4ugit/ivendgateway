package com.limitlessmobility.iVendGateway.dao.zeta;

import com.limitlessmobility.iVendGateway.model.zeta.ZetaCallbackTransactions;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public interface ZetaCallbackInitiationDao {

	public abstract boolean saveTransaction(PaymentTransaction callbackTransactions,
			ZetaCallbackTransactions zetaCallbackTransactions);
	
	public abstract boolean saveTransactionV2(PaymentTransaction callbackTransactions,
			ZetaCallbackTransactions zetaCallbackTransactions);

}
