package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class ManualRefundRequest {

	public String posId;
	public String transactionId;
	public String pspTransactionId;
	public double amount;
	public String pspId;
	public String source;
	public String version;
	
	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	
	public String getPspTransactionId() {
		return pspTransactionId;
	}
	public void setPspTransactionId(String pspTransactionId) {
		this.pspTransactionId = pspTransactionId;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
    public String toString() {
		return new Gson().toJson(this);
    }
	
	
}
