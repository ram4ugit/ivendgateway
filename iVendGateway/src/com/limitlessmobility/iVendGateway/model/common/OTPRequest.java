package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class OTPRequest {

	private String pspid;
	private String type;
	private String email;
	private String phone;
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
	
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
