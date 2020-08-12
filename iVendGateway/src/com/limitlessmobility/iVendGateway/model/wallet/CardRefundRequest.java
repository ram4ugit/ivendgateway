package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class CardRefundRequest {

	private int walletAccountId;
	private int transactionId;
	private String remark;
	public int getWalletAccountId() {
		return walletAccountId;
	}
	public void setWalletAccountId(int walletAccountId) {
		this.walletAccountId = walletAccountId;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	
	
}
