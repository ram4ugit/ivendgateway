package com.limitlessmobility.iVendGateway.psp.model;

import com.google.gson.Gson;

public class OperatorPspEntity {

	private int operatorId;
	private int operationLocationId;
	private String pspMerchantId;
	private String pspMerchantKey;
	private String pspId;
	private String pspMguid;
	private String isActive;
	private Integer paymentId;
	private String paymentType;
	private String walletType;
	private String customerId;
	private String imagePath;
	private String pspName;
	private int sequence;
	
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public int getOperationLocationId() {
		return operationLocationId;
	}
	public void setOperationLocationId(int operationLocationId) {
		this.operationLocationId = operationLocationId;
	}
	public String getPspMerchantId() {
		return pspMerchantId;
	}
	public void setPspMerchantId(String pspMerchantId) {
		this.pspMerchantId = pspMerchantId;
	}
	public String getPspMerchantKey() {
		return pspMerchantKey;
	}
	public void setPspMerchantKey(String pspMerchantKey) {
		this.pspMerchantKey = pspMerchantKey;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getPspMguid() {
		return pspMguid;
	}
	public void setPspMguid(String pspMguid) {
		this.pspMguid = pspMguid;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getWalletType() {
		return walletType;
	}
	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
	
}
