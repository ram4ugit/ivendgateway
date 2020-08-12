package com.limitlessmobility.iVendGateway.services.payu;

import com.google.gson.Gson;

public class PayuRefundRequest {

	private String orderId;
	private String payuId;
	private String amount;
	private String posId;

	
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getPayuId() {
		return payuId;
	}


	public void setPayuId(String payuId) {
		this.payuId = payuId;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getPosId() {
		return posId;
	}


	public void setPosId(String posId) {
		this.posId = posId;
	}


	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
