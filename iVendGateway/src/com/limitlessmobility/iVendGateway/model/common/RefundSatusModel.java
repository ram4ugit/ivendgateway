package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class RefundSatusModel {

	private String appId;
	private String paymentRefNo;
	private int statusId;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPaymentRefNo() {
		return paymentRefNo;
	}

	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
