package com.limitlessmobility.iVendGateway.model.amazonpay;

public class CheckStatusInitialData {

	private String posId;
	
	private String txnId;
	
	private String appId;
	
	private String pspId;
	
	private String amount;

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	

}
