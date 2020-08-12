package com.limitlessmobility.iVendGateway.model.product;

public class MachineProductModel {

	private String productCode;
	private String terminalId;
	private String machineId;
	private String paCode;
	private String machinePrice;
	private String lastUpdate;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getPaCode() {
		return paCode;
	}

	public void setPaCode(String paCode) {
		this.paCode = paCode;
	}

	public String getMachinePrice() {
		return machinePrice;
	}

	public void setMachinePrice(String machinePrice) {
		this.machinePrice = machinePrice;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
