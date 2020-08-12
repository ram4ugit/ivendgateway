package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class CaptureCardRequest {

	private double amount;
	private String refNo;
	private String terminalId;
	
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
