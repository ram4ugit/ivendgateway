package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PaymentStatusResponse {

	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
