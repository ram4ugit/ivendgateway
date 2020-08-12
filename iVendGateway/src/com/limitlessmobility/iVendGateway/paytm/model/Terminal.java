package com.limitlessmobility.iVendGateway.paytm.model;

import java.io.Serializable;

public class Terminal extends StatusError implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String terminalId;
	private String merchantId;
	private String merchantName;
	private String deviceId;
	private String imei;
	private String terminalMac;
	private String terminalAddress;
	private String terminalCountry;
	private String terminalState;
	private String terminalCity;
	private String terminalLat;
	private String terminalLng;
	private String status;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getTerminalMac() {
		return terminalMac;
	}

	public void setTerminalMac(String terminalMac) {
		this.terminalMac = terminalMac;
	}

	public String getTerminalAddress() {
		return terminalAddress;
	}

	public void setTerminalAddress(String terminalAddress) {
		this.terminalAddress = terminalAddress;
	}

	public String getTerminalCountry() {
		return terminalCountry;
	}

	public void setTerminalCountry(String terminalCountry) {
		this.terminalCountry = terminalCountry;
	}

	public String getTerminalState() {
		return terminalState;
	}

	public void setTerminalState(String terminalState) {
		this.terminalState = terminalState;
	}

	public String getTerminalCity() {
		return terminalCity;
	}

	public void setTerminalCity(String terminalCity) {
		this.terminalCity = terminalCity;
	}

	public String getTerminalLat() {
		return terminalLat;
	}

	public void setTerminalLat(String terminalLat) {
		this.terminalLat = terminalLat;
	}

	public String getTerminalLng() {
		return terminalLng;
	}

	public void setTerminalLng(String terminalLng) {
		this.terminalLng = terminalLng;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setFlag(String success) {
		// TODO Auto-generated method stub

	}

}
