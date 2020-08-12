package com.limitlessmobil.ivendgateway.dto;

import java.util.Map;

public class ResponseDTOObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String status;
	private String message;
	private Map<String, Object> errors;
	private Object responseObj;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
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
	public Map<String, Object> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, Object> errors) {
		this.errors = errors;
	}
	
	
	public Object getResponseObj() {
		return responseObj;
	}
	public void setResponseObj(Object responseObj) {
		this.responseObj = responseObj;
	}
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
}
