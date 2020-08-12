package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletPayPandingRequest {

	private String walletAccountId;
	private String referenceNo;
	private double amount;
	private String source;
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getWalletAccountId() {
		return walletAccountId;
	}
	public void setWalletAccountId(String walletAccountId) {
		this.walletAccountId = walletAccountId;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
