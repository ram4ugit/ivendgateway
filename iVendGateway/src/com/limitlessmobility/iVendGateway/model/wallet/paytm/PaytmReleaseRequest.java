package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class PaytmReleaseRequest {

	private String mid;
	private String merchantKey;
	private String token;
	private String preAuthId;
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMerchantKey() {
		return merchantKey;
	}
	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPreAuthId() {
		return preAuthId;
	}
	public void setPreAuthId(String preAuthId) {
		this.preAuthId = preAuthId;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
		
}
