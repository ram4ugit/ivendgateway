package com.limitlessmobility.iVendGateway.paytm.model;

public class PaymentTransaction {

	private Integer id;
	private String appId;
	private String pspId;
	private String deviceId;
	private String terminalId;
	private String merchantId;
	private String productName;
	private String productPrice;
	private String pspTransactionId;
	private String deviceTransactionId;
	private String terminalTransactionId;
	private double authAmount;
	private double paidAmount;
	private String authDate;
	private String settlementAmount;
	private String settlementTime;
	private String locationId;
	private String locationLat;
	private String locationLng;
	private String authTime;
	private String settlementDate;
	private String status;
	private String comments;
	private String statusCode;
	private String statusMsg;
	private String refundAmount;
	private String stage;
	private String orderId;
	private String merchantOrderId;
	private String serviceType;
	private String authCode;
	private String customerName;
	private String transactionType;
	private String pspMerchantId;
	//private String paidAmount;

	/*******Setter and Getter************/
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getPspTransactionId() {
		return pspTransactionId;
	}
	public void setPspTransactionId(String pspTransactionId) {
		this.pspTransactionId = pspTransactionId;
	}
	public String getDeviceTransactionId() {
		return deviceTransactionId;
	}
	public void setDeviceTransactionId(String deviceTransactionId) {
		this.deviceTransactionId = deviceTransactionId;
	}
	public String getTerminalTransactionId() {
		return terminalTransactionId;
	}
	public void setTerminalTransactionId(String terminalTransactionId) {
		this.terminalTransactionId = terminalTransactionId;
	}
	public double getAuthAmount() {
		return authAmount;
	}
	public void setAuthAmount(double authAmount) {
		this.authAmount = authAmount;
	}
	public String getAuthDate() {
		return authDate;
	}
	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}
	public String getSettlementAmount() {
		return settlementAmount;
	}
	public void setSettlementAmount(String settlementAmount) {
		this.settlementAmount = settlementAmount;
	}
	public String getSettlementTime() {
		return settlementTime;
	}
	public void setSettlementTime(String settlementTime) {
		this.settlementTime = settlementTime;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLocationLat() {
		return locationLat;
	}
	public void setLocationLat(String locationLat) {
		this.locationLat = locationLat;
	}
	public String getLocationLng() {
		return locationLng;
	}
	public void setLocationLng(String locationLng) {
		this.locationLng = locationLng;
	}
	public String getAuthTime() {
		return authTime;
	}
	public void setAuthTime(String authTime) {
		this.authTime = authTime;
	}
	public String getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getPspMerchantId() {
		return pspMerchantId;
	}
	public void setPspMerchantId(String pspMerchantId) {
		this.pspMerchantId = pspMerchantId;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
}
