package com.limitlessmobility.iVendGateway.dao.common;

import com.limitlessmobility.iVendGateway.model.common.ManualRefundRequest;
import com.limitlessmobility.iVendGateway.psp.model.PspMerchant;


public interface CommonDao {

	public void getTerminalForRefund();
	public void autoCheckStatusForRefund();
	public String commonRefundProcess();
	public String commonRefundProcessV2();
	public PspMerchant getPspMerchant(String pspid, String terminalId);
	public String getPspMerchantKey(String pspid, String terminalId);
	public String getDeviceId(String terminalId);
	public String getMid(String deviceId);
	public String getBankCode(String pspId);
	public String manualRefundProcess(ManualRefundRequest refundRequest);
	public String manualRefund(ManualRefundRequest refundRequest);
	public void directRefund();  
}
