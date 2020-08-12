package com.limitlessmobility.iVendGateway.paytm.model;

public class PaytmFinalQRRequest {

	private PaytmQRRequestType request;
	private String platformName;
	private String ipAddress;
	private String operationType;
	
	
	public PaytmQRRequestType getRequest() {
		return request;
	}
	public void setRequest(PaytmQRRequestType request) {
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
}
