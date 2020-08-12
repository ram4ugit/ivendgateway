package com.limitlessmobility.iVendGateway.model.common;

import org.springframework.stereotype.Component;

@Component
public class ConfigDetail {

	private int operatorId;

	private int isStatic;
	
	private String psp_qrcode;

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public int getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(int isStatic) {
		this.isStatic = isStatic;
	}

	public String getPsp_qrcode() {
		return psp_qrcode;
	}

	public void setPsp_qrcode(String psp_qrcode) {
		this.psp_qrcode = psp_qrcode;
	}
	
	

}
