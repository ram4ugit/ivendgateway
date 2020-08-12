package com.limitlessmobility.iVendGateway.psp.model;


public class CustomerlocationPspEntity {

	private Integer customerLocationId;
	private Integer operatorId;
	private String customerId;
	private String pspId;
	private String isActive;
	private Integer operationLocationId;
	private Integer isStatic;
	private String pspName;
	private int sequence;
	
	
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public Integer getCustomerLocationId() {
		return customerLocationId;
	}
	public void setCustomerLocationId(Integer customerLocationId) {
		this.customerLocationId = customerLocationId;
	}
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
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
	public Integer getOperationLocationId() {
		return operationLocationId;
	}
	public void setOperationLocationId(Integer operationLocationId) {
		this.operationLocationId = operationLocationId;
	}
	public Integer getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(Integer isStatic) {
		this.isStatic = isStatic;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
}
