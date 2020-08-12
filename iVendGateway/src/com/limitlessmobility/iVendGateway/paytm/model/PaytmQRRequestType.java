package com.limitlessmobility.iVendGateway.paytm.model;

public class PaytmQRRequestType {

	private String requestType;
	private String merchantContactNO;
	private String posId;
	private String channelId;
	private String amount;
	private String currency;
	private String merchantGuid;
	private String orderId;
	private String validity;
	private String industryType;
	private String productId;
	private String orderDetails;
	private String deviceId;
	
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getMerchantContactNO() {
		return merchantContactNO;
	}
	public void setMerchantContactNO(String merchantContactNO) {
		this.merchantContactNO = merchantContactNO;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMerchantGuid() {
		return merchantGuid;
	}
	public void setMerchantGuid(String merchantGuid) {
		this.merchantGuid = merchantGuid;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	public String getIndustryType() {
		return industryType;
	}
	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(String orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
