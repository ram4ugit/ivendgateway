package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PspDetail {

	private String pspId;
	private String pspName;
	private String pspMerchantId;
	private String pspMerchantKey;
	
	
	
	public String getPspId() {
		return pspId;
	}



	public void setPspId(String pspId) {
		this.pspId = pspId;
	}



	public String getPspName() {
		return pspName;
	}



	public void setPspName(String pspName) {
		this.pspName = pspName;
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



	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
