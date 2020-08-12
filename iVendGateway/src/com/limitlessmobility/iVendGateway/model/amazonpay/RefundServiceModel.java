package com.limitlessmobility.iVendGateway.model.amazonpay;

public class RefundServiceModel {

	private String amazonTransactionId;

	private String amazonTransactionIdType;

	private String refundReferenceId;

	private String refundAmount;

	private String refundCurrencyCode;

	private String isSandbox;

	public String getAmazonTransactionId() {
		return amazonTransactionId;
	}

	public void setAmazonTransactionId(String amazonTransactionId) {
		this.amazonTransactionId = amazonTransactionId;
	}

	public String getAmazonTransactionIdType() {
		return amazonTransactionIdType;
	}

	public void setAmazonTransactionIdType(String amazonTransactionIdType) {
		this.amazonTransactionIdType = amazonTransactionIdType;
	}

	public String getRefundReferenceId() {
		return refundReferenceId;
	}

	public void setRefundReferenceId(String refundReferenceId) {
		this.refundReferenceId = refundReferenceId;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundCurrencyCode() {
		return refundCurrencyCode;
	}

	public void setRefundCurrencyCode(String refundCurrencyCode) {
		this.refundCurrencyCode = refundCurrencyCode;
	}

	public String getIsSandbox() {
		return isSandbox;
	}

	public void setIsSandbox(String isSandbox) {
		this.isSandbox = isSandbox;
	}

}
