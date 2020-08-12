package com.limitlessmobility.iVendGateway.psp.model;

public class PspListRequest {

	private String customerLocationId;
	private String machineId;
	private String type;
	private String customerId;
	private String operatorId;
	
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	
}
