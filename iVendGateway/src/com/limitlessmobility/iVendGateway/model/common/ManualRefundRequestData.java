package com.limitlessmobility.iVendGateway.model.common;

public class ManualRefundRequestData {
	private String merchant_order_id;
	
	private String amount;
	
	private String psp_id;
	
	private String app_id;
	
	private String telemetryId;
	
	private String walletAccountId;
	
	private String walletAcountKey;
	
	private int refundStatus;
	
	private String source;
	
	private String version;
	
	
	
	
	/*private String operatorId;*/

	public String getTelemetryId() {
		return telemetryId;
	}

	public void setTelemetryId(String telemetryId) {
		this.telemetryId = telemetryId;
	}

	public String getMerchant_order_id() {
		return merchant_order_id;
	}

	public void setMerchant_order_id(String merchant_order_id) {
		this.merchant_order_id = merchant_order_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPsp_id() {
		return psp_id;
	}

	public void setPsp_id(String psp_id) {
		this.psp_id = psp_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getWalletAccountId() {
		return walletAccountId;
	}

	public void setWalletAccountId(String walletAccountId) {
		this.walletAccountId = walletAccountId;
	}

	public String getWalletAcountKey() {
		return walletAcountKey;
	}

	public void setWalletAcountKey(String walletAcountKey) {
		this.walletAcountKey = walletAcountKey;
	}

	public int getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
