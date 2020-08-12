package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletQRResponse {

	private String qrData;
	private String orderId;
	private String encryptedData;
	private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getQrData() {
		return qrData;
	}
	public void setQrData(String qrData) {
		this.qrData = qrData;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
	
}
