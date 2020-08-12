package com.limitlessmobility.iVendGateway.model.phonepe;

public class PPRefundRequestModel {
	
	private String merchantId;

	private String transactionId;

	private String originalTransactionId;

	private long amount;

	private String merchantOrderId;

	private String subMerchant;

	private String message;

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOriginalTransactionId() {
		return this.originalTransactionId;
	}

	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}

	public long getAmount() {
		return this.amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getMerchantOrderId() {
		return this.merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getSubMerchant() {
		return this.subMerchant;
	}

	public void setSubMerchant(String subMerchant) {
		this.subMerchant = subMerchant;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
