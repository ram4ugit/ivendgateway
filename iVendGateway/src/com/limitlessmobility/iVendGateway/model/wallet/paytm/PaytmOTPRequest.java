package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class PaytmOTPRequest {

	private String email;
	private String phone;
	private String clientId;
	private String scope;
	private String responseType;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
