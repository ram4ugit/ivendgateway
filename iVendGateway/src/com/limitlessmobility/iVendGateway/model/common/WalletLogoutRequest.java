package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class WalletLogoutRequest {

	private String sessionToken;
	private String pspId;
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
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
