package com.limitlessmobility.iVendGateway.psp.model;

public class PspListModel {

	private String pspId;
	private String keyboardType;
	private String refundMsg;
	private Integer isStatic;
	private String pspQrcode;
	
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getKeyboardType() {
		return keyboardType;
	}
	public void setKeyboardType(String keyboardType) {
		this.keyboardType = keyboardType;
	}
	public String getRefundMsg() {
		return refundMsg;
	}
	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}
	public Integer getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(Integer isStatic) {
		this.isStatic = isStatic;
	}
	public String getPspQrcode() {
		return pspQrcode;
	}
	public void setPspQrcode(String pspQrcode) {
		this.pspQrcode = pspQrcode;
	}
}
