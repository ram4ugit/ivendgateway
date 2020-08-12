package com.limitlessmobility.iVendGateway.paytm.model;

import com.google.gson.Gson;

public class PaytmRefundRequestModelFinal {

	PaytmRefundRequest request = new PaytmRefundRequest();
	private String platformName;
	private String ipAddress;
	private String operationType;
	private String channel;
	private String version;
	
	
	public PaytmRefundRequest getRequest() {
		return request;
	}
	public void setRequest(PaytmRefundRequest request) {
		this.request = request;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
