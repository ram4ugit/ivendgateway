package com.limitlessmobil.ivendgateway.dto;

import com.google.gson.Gson;

public class ResponseObj {

	private Object responseObj;
	private String status;
	private String message;
	
	public Object getResponseObj() {
		return responseObj;
	}
	public void setResponseObj(Object responseObj) {
		this.responseObj = responseObj;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
