package com.limitlessmobility.iVendGateway.model.paymentmode;

public class PspDetailModal {

	private String id;
	private String pspName;
	private String pspType;
	private String pspApiUrl;
	private String isStatic;
	private String qrCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public String getPspType() {
		return pspType;
	}
	public void setPspType(String pspType) {
		this.pspType = pspType;
	}
	public String getPspApiUrl() {
		return pspApiUrl;
	}
	public void setPspApiUrl(String pspApiUrl) {
		this.pspApiUrl = pspApiUrl;
	}
	public String getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(String isStatic) {
		this.isStatic = isStatic;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	
	
	
}
