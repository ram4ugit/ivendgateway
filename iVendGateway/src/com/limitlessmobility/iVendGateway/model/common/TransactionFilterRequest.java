package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class TransactionFilterRequest {

	private String orderId;

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
