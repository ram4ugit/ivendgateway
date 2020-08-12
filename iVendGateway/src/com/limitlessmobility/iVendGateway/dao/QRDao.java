package com.limitlessmobility.iVendGateway.dao;

import java.util.List;

import com.limitlessmobility.iVendGateway.paytm.model.PaymentInitiation;

public abstract interface QRDao {

	public abstract boolean saveQRRequest(PaymentInitiation paymentInitiation);
//	public PaymentInitiation getPaymentInitiationByTxnId(String txnID);
}
