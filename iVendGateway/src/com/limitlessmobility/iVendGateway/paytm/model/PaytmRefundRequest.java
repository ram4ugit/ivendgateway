package com.limitlessmobility.iVendGateway.paytm.model;

public class PaytmRefundRequest {

	private String amount;
	private String merchantOrderId;
	private String txnGuid;
	private String currencyCode;
	private String refundRefId;
	
	
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getTxnGuid() {
		return txnGuid;
	}
	public void setTxnGuid(String txnGuid) {
		this.txnGuid = txnGuid;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getRefundRefId() {
		return refundRefId;
	}
	public void setRefundRefId(String refundRefId) {
		this.refundRefId = refundRefId;
	}
	
	
}
