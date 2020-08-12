package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class CommonValidateOtpRequest {

	private String pspid;
	private String type;
	private String otp;
	private String state;
	public String getPspid() {
		return pspid;
	}
	public void setPspid(String pspid) {
		this.pspid = pspid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
