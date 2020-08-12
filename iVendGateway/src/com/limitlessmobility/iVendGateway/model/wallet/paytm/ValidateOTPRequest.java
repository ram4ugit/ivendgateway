package com.limitlessmobility.iVendGateway.model.wallet.paytm;

import com.google.gson.Gson;

public class ValidateOTPRequest {

	private String otp;
	private String state;
	private String clientId;
	private String clientSecret;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
