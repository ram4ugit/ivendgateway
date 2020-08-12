package com.limitlessmobil.ivendgateway.dto;

import java.io.Serializable;
import java.util.Map;

import com.google.gson.Gson;

/**
 * @author Ram Narayan Roy
 *
 */
public class ResponseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String status;
	private String message;
	private Map<String, Object> errors;
	private Object responseData;
	
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
	public Object getResponseData() {
		return responseData;
	}
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
	
	@Override
	public String toString(){
	    return new Gson().toJson(this);
	}
}
