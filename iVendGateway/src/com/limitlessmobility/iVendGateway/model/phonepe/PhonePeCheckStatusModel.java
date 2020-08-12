package com.limitlessmobility.iVendGateway.model.phonepe;

public class PhonePeCheckStatusModel {
	private String posId;
	private String txnId;
	private String appId;
	private String pspId;
	private String amount;
	public final String getPosId() {
		return posId;
	}
	public final void setPosId(String posId) {
		this.posId = posId;
	}
	public final String getTxnId() {
		return txnId;
	}
	public final void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public final String getAppId() {
		return appId;
	}
	public final void setAppId(String appId) {
		this.appId = appId;
	}
	public final String getPspId() {
		return pspId;
	}
	public final void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public final String getAmount() {
		return amount;
	}
	public final void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
