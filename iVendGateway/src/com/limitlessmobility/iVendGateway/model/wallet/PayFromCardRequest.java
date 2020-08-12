package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class PayFromCardRequest {

	private String referenceNo;
	private double amount;
	private String source;
	private int walletId;
	private String walletKey;
	private String walletAccountId;
	private String orderId;
	
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public int getWalletId() {
		return walletId;
	}
	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	public String getWalletKey() {
		return walletKey;
	}
	public void setWalletKey(String walletKey) {
		this.walletKey = walletKey;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
	
}
