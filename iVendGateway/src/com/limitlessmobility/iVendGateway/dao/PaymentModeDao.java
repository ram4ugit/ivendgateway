package com.limitlessmobility.iVendGateway.dao;

import java.util.List;

import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;

public interface PaymentModeDao {

	public abstract List<PaymentModeModel> getPaymentModeList();
	public abstract List<PaymentModeModel> getPSPList(PaymentModeModel paymentMode);
	public abstract PspDetailModal getpspDetailByPspId(String pspId);
}
