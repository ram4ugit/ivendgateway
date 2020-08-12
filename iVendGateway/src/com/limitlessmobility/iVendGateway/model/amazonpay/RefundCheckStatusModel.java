package com.limitlessmobility.iVendGateway.model.amazonpay;

public class RefundCheckStatusModel {
	private String AmazonRefundId;
	
	private String isSandbox;

	public String getAmazonRefundId() {
		return AmazonRefundId;
	}

	public void setAmazonRefundId(String amazonRefundId) {
		AmazonRefundId = amazonRefundId;
	}

	public String getIsSandbox() {
		return isSandbox;
	}

	public void setIsSandbox(String isSandbox) {
		this.isSandbox = isSandbox;
	}
	
	
}
