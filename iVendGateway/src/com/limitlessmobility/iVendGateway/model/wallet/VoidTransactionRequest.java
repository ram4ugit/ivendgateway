package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class VoidTransactionRequest {

	private String terminalId;
	private String refNo;
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getRefId() {
		return refNo;
	}
	public void setRefId(String refId) {
		this.refNo = refId;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
