package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PayPendingRequest {

	private String walletId;
	private String walletKey;
	private String walletAccountId;
	private String walletAccountKey;
	private double amount;
	private String transactionRefNo;
	private String pspId;
	private String orderId;
	private String source;
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getWalletKey() {
		return walletKey;
	}
	public void setWalletKey(String walletKey) {
		this.walletKey = walletKey;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionRefNo() {
		return transactionRefNo;
	}
	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
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
