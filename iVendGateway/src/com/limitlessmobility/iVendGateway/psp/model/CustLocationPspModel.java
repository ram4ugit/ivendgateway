package com.limitlessmobility.iVendGateway.psp.model;

public class CustLocationPspModel {

	private String pspId;
	private Integer isStatic;
	private String pspQrcode;
	private String pspTid;
	private String refundMsg;
	private String type;
	
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
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
	public String getPspTid() {
		return pspTid;
	}
	public void setPspTid(String pspTid) {
		this.pspTid = pspTid;
	}
	public String getRefundMsg() {
		return refundMsg;
	}
	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
