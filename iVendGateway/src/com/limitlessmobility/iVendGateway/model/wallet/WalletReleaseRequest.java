package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletReleaseRequest {

	private String walletAccountId;
	private String refTransactionId;
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
	public String getRefTransactionId() {
		return refTransactionId;
	}
	public void setRefTransactionId(String refTransactionId) {
		this.refTransactionId = refTransactionId;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
	
}
