package com.limitlessmobility.iVendGateway.paytm.model;

import java.io.Serializable;

public class TerminalPsp extends StatusError implements Serializable {

	private static final long serialVersionUID = 1L;

	private String requestType;
	private String txnType;
	private String merchantGuid;
	private String txnId;
	private String terminalId;
	private String pspId;
	private String paymentId;
	private String appId;
	private String amount;
	private String posId;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMerchantGuid() {
		return merchantGuid;
	}

	public void setMerchantGuid(String merchantGuid) {
		this.merchantGuid = merchantGuid;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public void setFlag(String success) {
		// TODO Auto-generated method stub

	}

	public String getPspId() {
		return pspId;
	}

	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}
	
}
