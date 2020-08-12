package com.limitlessmobility.iVendGateway.psp.model;

import java.util.List;

import com.google.gson.Gson;

public class MachineConfigRequest {

	private String keyboardType;
	private Integer customerLocationId;
	private String customerId;
	private Integer operationLocationId;
	private Integer machineId;
	private String telemetryId;
	private String vposId;
	private String cashboxId;
	private String pspName;
	private int operatorId;
	
	List<MachineConfigPaymentMode> paymentMode;
	
	public String getPspName() {
		return pspName;
	}

	public void setPspName(String pspName) {
		this.pspName = pspName;
	}

	

	public String getKeyboardType() {
		return keyboardType;
	}

	public void setKeyboardType(String keyboardType) {
		this.keyboardType = keyboardType;
	}

	public Integer getCustomerLocationId() {
		return customerLocationId;
	}

	public void setCustomerLocationId(Integer customerLocationId) {
		this.customerLocationId = customerLocationId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public List<MachineConfigPaymentMode> getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(List<MachineConfigPaymentMode> paymentMode) {
		this.paymentMode = paymentMode;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }

	public static void main(String[] args) {
	    System.out.println(new MachineConfigRequest().toString());
    }
	
}
