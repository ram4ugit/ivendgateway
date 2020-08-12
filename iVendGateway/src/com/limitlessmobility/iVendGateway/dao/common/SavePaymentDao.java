package com.limitlessmobility.iVendGateway.dao.common;

import com.limitlessmobility.iVendGateway.model.common.RefundData;
import com.limitlessmobility.iVendGateway.paytm.model.PaymentTransaction;

public interface SavePaymentDao {
	
	public boolean saveRefundData(RefundData  reData);
	
	public PaymentTransaction getPspTxnId(String merchantOrderId);
	
}
