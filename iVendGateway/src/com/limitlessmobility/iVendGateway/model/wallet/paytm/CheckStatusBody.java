package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class CheckStatusBody {

	private String userToken;
	private String totalAmount;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}