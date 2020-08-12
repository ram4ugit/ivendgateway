package com.limitlessmobility.iVendGateway.dao;

import java.util.List;

import com.limitlessmobility.iVendGateway.model.paymentmode.PspDetailModal;
import com.limitlessmobility.iVendGateway.paytm.model.TerminalPsp;
import com.limitlessmobility.iVendGateway.psp.model.PSPModel;

public interface TerminalPspDao {

	public TerminalPsp checkStatus(TerminalPsp terminalPsp);
	public abstract String getTerminalIdByDeviceID(String deviceid);
	public abstract List<String> getpspMerchantID(String terminalID);
	public abstract List<PSPModel> getpspList(String pspMeachantID);
	
	
}
