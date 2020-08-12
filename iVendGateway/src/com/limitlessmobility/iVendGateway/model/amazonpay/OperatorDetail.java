package com.limitlessmobility.iVendGateway.model.amazonpay;

import org.springframework.stereotype.Component;

@Component
public class OperatorDetail {

	private int operatorId;
	
	private String merchantId;
	
	private String merchantKey;
	
	private String pspMguid;
	
	private String pspId;
	
	private String StoreId;

	

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getPspMguid() {
		return pspMguid;
	}

	public void setPspMguid(String pspMguid) {
		this.pspMguid = pspMguid;
	}

	public String getPspId() {
		return pspId;
	}

	public void setPspId(String pspId) {
		this.pspId = pspId;
	}

	public String getStoreId() {
		return StoreId;
	}

	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	
	
}
