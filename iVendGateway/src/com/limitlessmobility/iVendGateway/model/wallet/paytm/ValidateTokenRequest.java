package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class ValidateTokenRequest {
	private String sessionToken;
	
	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
