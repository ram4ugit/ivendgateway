package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PaymentHistoryResponseData {

	private String posName;
    private String paymentReferenceNo;
    private String Date;
    private String paymentModeName;
    private double paidAmount;
    private int refundStatus;
    private double refundAmount;
    private String refundedOn;
    private String refundReferenceNo;
	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
	public String getPaymentReferenceNo() {
		return paymentReferenceNo;
	}
	public void setPaymentReferenceNo(String paymentReferenceNo) {
		this.paymentReferenceNo = paymentReferenceNo;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getPaymentModeName() {
		return paymentModeName;
	}
	public void setPaymentModeName(String paymentModeName) {
		this.paymentModeName = paymentModeName;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	public int getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getRefundedOn() {
		return refundedOn;
	}
	public void setRefundedOn(String refundedOn) {
		this.refundedOn = refundedOn;
	}
	public String getRefundReferenceNo() {
		return refundReferenceNo;
	}
	public void setRefundReferenceNo(String refundReferenceNo) {
		this.refundReferenceNo = refundReferenceNo;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
