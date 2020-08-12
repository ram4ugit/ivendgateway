package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletRefundRequest {

	private long refTransactionId;
	private double amount;
	private String source;
	
	
	

	




	public long getRefTransactionId() {
		return refTransactionId;
	}




	public void setRefTransactionId(long refTransactionId) {
		this.refTransactionId = refTransactionId;
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




	@Override
    public String toString() {
		return new Gson().toJson(this);
    }
}
