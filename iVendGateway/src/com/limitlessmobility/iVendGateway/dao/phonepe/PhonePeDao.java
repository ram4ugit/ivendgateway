package com.limitlessmobility.iVendGateway.dao.phonepe;

import com.limitlessmobility.iVendGateway.model.phonepe.CheckStatusData;
import com.limitlessmobility.iVendGateway.model.phonepe.InitiationDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PSPMerchantDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.PaymentDetail;
import com.limitlessmobility.iVendGateway.model.phonepe.TerminalDetail;

public interface PhonePeDao {

	
	public abstract PSPMerchantDetail getMerchantDetailsByPspId(String pspId,String terminalId);
	
	public abstract String getMerchantOrderIdByTxnId(String txnId);
	
	public abstract int getAmountByTxnId(String txnId);
	
	public CheckStatusData getCheckStatusData(String txnId);
	
	public PaymentDetail getPaymentDetail(String txnId);
	
	public TerminalDetail getTerminalDetail(String terminalId);
	
	public InitiationDetail getInitiationDetail(String txnId);
}
