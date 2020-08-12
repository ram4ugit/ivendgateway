package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class CommonQRRequest {

	 private String posId;
	 private String pspId;
	 private double amount;
	 private String appId;
	 private String productId;
	 
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	@Override
    public String toString() {
		 return new Gson().toJson(this);
    }
	
}
