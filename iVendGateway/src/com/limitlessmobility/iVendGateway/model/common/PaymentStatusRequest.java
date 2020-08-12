package com.limitlessmobility.iVendGateway.model.common;

import com.google.gson.Gson;

public class PaymentStatusRequest {

	private String orderId;
	private String txnId;
	private int operatorId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	@Override
    public String toString() {
	    return new Gson().toJson(this);
    }
}
