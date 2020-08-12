package com.limitlessmobility.iVendGateway.dao.psp;

import java.util.List;

import com.limitlessmobility.iVendGateway.model.paymentmode.PaymentModeModel;
import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.psp.model.PaymentModes;

public interface PaymentModeDao {

	public abstract List<PaymentModeModel> getPaymentModeList();
	public abstract List<PaymentModeModel> getPSPList(PaymentModeModel paymentMode);
	public abstract PspDetailModal getpspDetailByPspId(String pspId);
	public List<PaymentModes> getPaymentModeList(String type);
}
