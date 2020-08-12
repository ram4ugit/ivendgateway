package com.limitlessmobility.iVendGateway.dao.bharatqr;

import com.limitlessmobility.iVendGateway.model.bharatqr.PspTerminal;


public interface BharatQRDao {

	public abstract String getDeviceIDByTerminalId(String terminalId);

	public abstract String getMachineIdByDeviceId(String deviceId);
	
	public abstract String getTIDByPspTerminalPaymentId(String pspId,String termainalId,String paymentId);


	public abstract String getMIDByPspId(String pspId);
	
	public abstract String getMIDPspTerminalPaymentId(String pspId,String termainalId,String paymentId);
	
//	public abstract MerchantDetail getAllCredential(String pspId,String termainalId,String paymentId);

	public boolean checkTxnId(String txnId);
	
    public boolean updateTerminalDevice(String txnId,String terminalId);
	
	public abstract String getTrIDByTxnId(String txnId);

	public abstract String getProductByTxnId(String txnId);

	public abstract String getTerminalId();

	public abstract String getTerminalId(String pspId);

//	public abstract String getAmount(String pspTxnId);
	
	public abstract String getPspIdByPSPTxnId(String pspTxnId);
	
	public abstract String getBankCodeByPspId(String pspId);
	
	public abstract String getAuthCodeByPspId(String pspTxnId);
	
	public abstract String getTxnTypeByPspTxnId(String txnId);
	
	public PspTerminal getTransactionDetails(String pspTxnId);
	
	public abstract String getRefNoByTxnId(String txnId);
}
