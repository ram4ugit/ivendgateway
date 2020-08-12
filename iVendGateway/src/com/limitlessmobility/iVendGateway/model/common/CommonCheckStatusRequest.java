package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class CommonCheckStatusRequest {

	private String posId;
	private String amount;
	private String txnId;
	private String appId;
	private String pspId;
	
	//for wallet only
	private String walletAccountId;
	private String walletAccountKey;
	
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getWalletAccountId() {
		return walletAccountId;
	}
	public void setWalletAccountId(String walletAccountId) {
		this.walletAccountId = walletAccountId;
	}
	public String getWalletAccountKey() {
		return walletAccountKey;
	}
	public void setWalletAccountKey(String walletAccountKey) {
		this.walletAccountKey = walletAccountKey;
	}
	@Override
    public String toString() {
		 return new Gson().toJson(this);
    }
}
