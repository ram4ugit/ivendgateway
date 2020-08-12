package com.limitlessmobility.iVendGateway.model.amazonpay;

import java.io.Serializable;
import java.util.Map;

public class RequestToSign implements Serializable{
   

	private static final long serialVersionUID = 1L;
	private String method;
	private String host;
	private String uRI;
	private Map<String, Object> parameters;
	private long timeStamp;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getuRI() {
		return uRI;
	}
	public void setuRI(String uRI) {
		this.uRI = uRI;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
