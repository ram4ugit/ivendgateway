package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletBlockRequest {

	private String walletAccountId;
	private double amount;
	private String orderNo;
	private String source;
	
	
	public String getWalletAccountId() {
		return walletAccountId;
	}


	public void setWalletAccountId(String walletAccountId) {
		this.walletAccountId = walletAccountId;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
