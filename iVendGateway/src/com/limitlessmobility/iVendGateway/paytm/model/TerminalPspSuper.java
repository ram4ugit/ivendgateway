package com.limitlessmobility.iVendGateway.paytm.model;

import java.io.Serializable;

public class TerminalPspSuper extends StatusError implements Serializable {

	private static final long serialVersionUID = 1L;

	TerminalPsp request;

	private String platformName;
	private String operationType;
	private String channel;
	private String version;
	

	

	public TerminalPsp getRequest() {
		return request;
	}



	public void setRequest(TerminalPsp request) {
		this.request = request;
	}



	public String getPlatformName() {
		return platformName;
	}



	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}



	public String getOperationType() {
		return operationType;
	}



	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	

	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}



	public String getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = version;
	}



	public void setFlag(String success) {
		// TODO Auto-generated method stub

	}

}
