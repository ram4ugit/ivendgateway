package com.limitlessmobility.iVendGateway.model.paymentmode;

import com.google.gson.Gson;

public class GetMachinePspResponse {

	private String paymentModelName;
	private String isChild;
	private int type;
	private int isStatic;
	private String qrCode;
	private String message;
//	private String terminalId;
	private String refundMessage;
	private String keyboardType;
	private String pspName;
	private String imagePath;
	
	
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getPaymentModelName() {
		return paymentModelName;
	}
	public void setPaymentModelName(String paymentModelName) {
		this.paymentModelName = paymentModelName;
	}
	public String getIsChild() {
		return isChild;
	}
	public void setIsChild(String isChild) {
		this.isChild = isChild;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(int isStatic) {
		this.isStatic = isStatic;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRefundMessage() {
		return refundMessage;
	}
	public void setRefundMessage(String refundMessage) {
		this.refundMessage = refundMessage;
	}
	public String getKeyboardType() {
		return keyboardType;
	}
	public void setKeyboardType(String keyboardType) {
		this.keyboardType = keyboardType;
	}
	
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }

	
}
