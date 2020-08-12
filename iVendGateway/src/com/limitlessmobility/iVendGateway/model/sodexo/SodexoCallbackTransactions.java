package com.limitlessmobility.iVendGateway.model.sodexo;

public class SodexoCallbackTransactions {

	private String entity;
	private String event;
	private Payload payload = new Payload();
	private MetaInfo metaInfo = new MetaInfo();
	private String status;
	private String statusCode;
	private String statusMessage;

	public static final String VERSION = "V1";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public MetaInfo getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(MetaInfo metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

}
