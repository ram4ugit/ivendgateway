package com.limitlessmobility.iVendGateway.model.phonepe;

public class PaymentDetail {
        
	private String pspId;
	
	private String terminalId;
	
	private String type;

	public final String getPspId() {
		return pspId;
	}

	public final void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public final String getTerminalId() {
		return terminalId;
	}

	public final void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
