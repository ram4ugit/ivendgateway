package com.limitlessmobility.iVendGateway.model.bharatqr;

public class PspTerminal {
	
	
	private String pspId;

	private String terminal;

	private String authCode;

	private String txnType;

	public String getPspId() {
		return pspId;
	}

	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	
	

}
