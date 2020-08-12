package com.limitlessmobility.iVendGateway.psp.model;

import com.google.gson.Gson;

public class GetOperatorPspRequest {

	private int operatorId;
	private String isActive;
	private int operationLocationId;
	private String pspId;
	private String pspMerchantId;
	
	
	public int getOperatorId() {
		return operatorId;
	}


	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public int getOperationLocationId() {
		return operationLocationId;
	}


	public void setOperationLocationId(int operationLocationId) {
		this.operationLocationId = operationLocationId;
	}


	public String getPspId() {
		return pspId;
	}


	public void setPspId(String pspId) {
		this.pspId = pspId;
	}


	public String getPspMerchantId() {
		return pspMerchantId;
	}


	public void setPspMerchantId(String pspMerchantId) {
		this.pspMerchantId = pspMerchantId;
	}


	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
