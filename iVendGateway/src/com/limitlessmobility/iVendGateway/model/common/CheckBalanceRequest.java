package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class CheckBalanceRequest {

	private String clientId;
	private String userToken;
	private String totalAmount;
	private String mid;
	private String pspId;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
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
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
