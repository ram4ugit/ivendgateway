package com.limitlessmobility.iVendGateway.model.wallet;

import com.google.gson.Gson;

public class WalletQRRequestModel {

	private String orderNo;
	private String amount;
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
    public String toString() {
		return new Gson().toJson(this);
    }
}
