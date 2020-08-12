package com.limitlessmobility.iVendGateway.psp.model;

public class MachineConfigEntity {

	private Integer id;
	private Integer customerLocationId;
	private Integer operatorId;
	private String customerId;
	private String pspId;
	private String isActive;
	private Integer operationLocationId;
	private Integer machineId;
	private String telemetryId;
	private String vposId;
	private String cashboxId;
	private String pspTid;
	private String type;
	private Integer isStatic;
	private String pspQrcode;
	private String refundMsg;
	private String keyboardType;
	private String pspName;
	
	
	public String getPspName() {
		return pspName;
	}
	public void setPspName(String pspName) {
		this.pspName = pspName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public String getTelemetryId() {
		return telemetryId;
	}
	public void setTelemetryId(String telemetryId) {
		this.telemetryId = telemetryId;
	}
	public String getVposId() {
		return vposId;
	}
	public void setVposId(String vposId) {
		this.vposId = vposId;
	}
	public String getCashboxId() {
		return cashboxId;
	}
	public void setCashboxId(String cashboxId) {
		this.cashboxId = cashboxId;
	}
	public String getPspTid() {
		return pspTid;
	}
	public void setPspTid(String pspTid) {
		this.pspTid = pspTid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(Integer isStatic) {
		this.isStatic = isStatic;
	}
	public String getPspQrcode() {
		return pspQrcode;
	}
	public void setPspQrcode(String pspQrcode) {
		this.pspQrcode = pspQrcode;
	}
	public String getRefundMsg() {
		return refundMsg;
	}
	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}
	public String getKeyboardType() {
		return keyboardType;
	}
	public void setKeyboardType(String keyboardType) {
		this.keyboardType = keyboardType;
	}
	
}
