package com.limitlessmobility.iVendGateway.dao.zeta;

import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;

public interface ZetaRefundDao {
	
	public PspTerminal getTransactionDetails(String pspTxnId);
	public PspTerminal getTransactionDetailsZeta(String pspTxnId);
	public String getOrderId(String pspTxnId);
}
