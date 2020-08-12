package com.limitlessmobility.iVendGateway.psp.model;

import com.google.gson.Gson;


public class PaymentModes {
	
	private Integer paymentModeId;
    private String paymentMode;
    private Integer status;
    private Integer isStatic;
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
	public Integer getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(Integer isStatic) {
		this.isStatic = isStatic;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }

	
}
