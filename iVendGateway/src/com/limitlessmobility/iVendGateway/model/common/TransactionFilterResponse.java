package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class TransactionFilterResponse {

	private String orderId;
	private float amount;
	private String date;
	private String mode;
	private String refNo;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
