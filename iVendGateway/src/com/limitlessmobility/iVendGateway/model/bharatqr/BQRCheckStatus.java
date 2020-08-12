package com.limitlessmobility.iVendGateway.model.bharatqr;

public class BQRCheckStatus {

	private String fromEntity;
	private String bankCode;

	Data_CheckStatus data = new Data_CheckStatus();

	public Data_CheckStatus getData() {
		return data;
	}

	public void setData(Data_CheckStatus data) {
		this.data = data;
	}

	public String getFromEntity() {
		return fromEntity;
	}

	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

}
