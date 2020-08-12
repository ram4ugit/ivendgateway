package com.limitlessmobility.iVendGateway.psp.model;

public class MachineConfigPaymentMode {
	
	private String pspId;
	private String pspTid;
	private String pspQrcode;
	private Integer isStatic;
	private String refundMsg;
	private String type;
	private String methodType;
	private String pspName;
	private boolean selected;
	private String walletType;
	private int sequence;
	private int isActive;
	
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getPspTid() {
		return pspTid;
	}
	public void setPspTid(String pspTid) {
		this.pspTid = pspTid;
	}
	public String getPspQrcode() {
		return pspQrcode;
	}
	public void setPspQrcode(String pspQrcode) {
		this.pspQrcode = pspQrcode;
	}
	public Integer getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(Integer isStatic) {
		this.isStatic = isStatic;
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
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getWalletType() {
		return walletType;
	}
	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
}
