package com.limitlessmobility.iVendGateway.psp.model;

import com.google.gson.Gson;

public class CustomerPspModel {

	private String operatorId;
	private String customerId;
	private String pspId;
	private String isActive;
	private String operationLocationId;
	private String isStatic;
	private String pspName;
	private int sequence;
	private boolean isAvailable;
	
	
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPspId() {
		return pspId;
	}
	public void setPspId(String pspId) {
		this.pspId = pspId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getOperationLocationId() {
		return operationLocationId;
	}
	public void setOperationLocationId(String operationLocationId) {
		this.operationLocationId = operationLocationId;
	}
	public String getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(String isStatic) {
		this.isStatic = isStatic;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
