package com.limitlessmobility.iVendGateway.model.bharatqr;

public class RefundRequestModel {
   
	private String rrn;
	
	private String refundAmount;


	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
}
