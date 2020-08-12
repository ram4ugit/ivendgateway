package com.limitlessmobility.iVendGateway.model.phonepe;

public class TransactionContext {
	
	private String qrCodeId;
	private String storeId;
	private String terminalId;
	
	
	public final String getQrCodeId() {
		return qrCodeId;
	}

	public final void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}

	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public final String getTerminalId() {
		return terminalId;
	}

	public final void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

}
