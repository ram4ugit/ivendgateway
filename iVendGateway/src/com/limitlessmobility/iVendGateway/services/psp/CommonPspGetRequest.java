package com.limitlessmobility.iVendGateway.services.psp;

public class CommonPspGetRequest {
	private String label;
	private String customerId;
	private String pspId;
	private String isActive;
	private String operationLocationId;
	private String customerLocationId;
	private String machineId;
	private String type;
	private String isStatic;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
	public String getCustomerLocationId() {
		return customerLocationId;
	}
	public void setCustomerLocationId(String customerLocationId) {
		this.customerLocationId = customerLocationId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(String isStatic) {
		this.isStatic = isStatic;
	}
}
