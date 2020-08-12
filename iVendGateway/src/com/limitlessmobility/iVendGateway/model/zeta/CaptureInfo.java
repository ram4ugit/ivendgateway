package com.limitlessmobility.iVendGateway.model.zeta;

public class CaptureInfo {

	private Boolean shouldAutoCapture;
	private Integer captureTimeoutInMillis;

	public Boolean getShouldAutoCapture() {
		return shouldAutoCapture;
	}

	public void setShouldAutoCapture(Boolean shouldAutoCapture) {
		this.shouldAutoCapture = shouldAutoCapture;
	}

	public Integer getCaptureTimeoutInMillis() {
		return captureTimeoutInMillis;
	}

	public void setCaptureTimeoutInMillis(Integer captureTimeoutInMillis) {
		this.captureTimeoutInMillis = captureTimeoutInMillis;
	}

}
